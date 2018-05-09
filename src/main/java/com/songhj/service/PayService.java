package com.songhj.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.alibaba.fastjson.JSONObject;
import com.songhj.config.WeiXinConfig;
import com.songhj.facade.PayFacadeService;
import com.songhj.util.CommUtil;
import com.songhj.util.HttpUtil;

/**
 * 微信支付
 * @author songhj
 *
 */
@Service
public class PayService implements PayFacadeService {

	private static final Logger logger = LoggerFactory.getLogger(PayService.class);
	
	@Autowired
	private LoginService loginService;
	
	/**
	 * 微信发起支付签名
	 */
	@Override
	public Map<String, Object> wxPay(String openId, String orderNo,String clientIp) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<>();
		WeiXinConfig weiXinConfig = loginService.getWeiXinConfig();
		if(weiXinConfig == null){
			map.put("code",-1);
			map.put("message", "获取微信配置信息失败");
		}
		String amount = "100"; //1分钱
		String prepayId = "";
		try {
			//微信统一下单
			Map<String,String> resultMap = getWxPrepayId(weiXinConfig, orderNo, "微信支付", openId, amount, clientIp);
			if(!"SUCCESS".equals(resultMap.get("return_code"))){
				map.put("code",-1);
				map.put("message", resultMap.get("return_msg"));
				return map;
			}
			prepayId = resultMap.get("prepay_id");
		} catch (Exception e) {
			map.put("code",-1);
			map.put("message", e.getMessage());
		}
		if(map.get("code") != null){
			return map;
		}
		
		String appId = weiXinConfig.getOpenAppid();
		String paternerKey = weiXinConfig.getApiSecret();
		String nonceStr = CommUtil.randomString(16);
		String timeStamp = Long.toString(System.currentTimeMillis() / 1000);
		
		Map<String,String> params = new HashMap<>();
		params.put("appId", appId); //公众号名称，由商户传入 
		params.put("timeStamp", timeStamp); //时间戳，自1970年以来的秒数     
		params.put("nonceStr", nonceStr); //随机串 
		params.put("package", "prepay_id=" + prepayId);
		params.put("signType", "MD5"); //签名算法
		params.put("paySign", CommUtil.signVeryfy(params, paternerKey));  //微信签名
		
		map.put("charge", params);
		map.put("code", 1);
		map.put("message","微信签名成功！");
		return map;
	}
	
	/**
	 * 微信统一下单
	 * @param order_no
	 * @param subject
	 * @param body
	 * @param openId
	 * @param create_ip
	 * @param amount
	 * @return
	 * @throws IOException 
	 * @throws XmlPullParserException 
	 */
	private Map<String,String> getWxPrepayId(WeiXinConfig weiXinConfig,String orderNo,String body,String openId,
			String amount,String clientIp) throws XmlPullParserException, IOException{
		String appId = weiXinConfig.getOpenAppid();
		String mch_id = weiXinConfig.getMchid();
		String nonceStr = CommUtil.randomString(16);
		String paternerKey = weiXinConfig.getApiSecret();
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("appid", appId);
		paraMap.put("body", body);
		paraMap.put("mch_id", mch_id);
		paraMap.put("nonce_str", nonceStr);
		paraMap.put("openid", openId);
		paraMap.put("out_trade_no", orderNo);
		paraMap.put("spbill_create_ip", clientIp);
		paraMap.put("total_fee", amount);
		paraMap.put("trade_type", "JSAPI");
		paraMap.put("notify_url", weiXinConfig.getNotifyUrl());
		//微信签名
		String sign = CommUtil.signVeryfy(paraMap, paternerKey);
		paraMap.put("sign", sign);
		String xml = CommUtil.doMapToXml(paraMap);
		logger.info("微信支付统一下单请求报文：" + xml);
		String result = HttpUtil.post(weiXinConfig.getOrderUrl(), xml);
		logger.info("微信支付统一下单返回报文：" + result);
		return CommUtil.doXmlToMap(result);
	}
	
	
	/**
	 * xml转Map
	 * @param xml
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static Map<String, String> doXmlToMap(String xml) 
			throws XmlPullParserException, IOException{
		Map<String, String> map = null;
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
		XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
		pullParser.setInput(inputStream, "UTF-8"); // 为xml设置要解析的xml数据
		int eventType = pullParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				map = new HashMap<>();
				break;
			case XmlPullParser.START_TAG:
				String key = pullParser.getName();
				if (key.equals("xml"))
					break;
				String value = pullParser.nextText();
				map.put(key, value);
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			eventType = pullParser.next();
		}
		return map;
	}

	/**
	 * 支付回调
	 */
	@Override
	public boolean payCallback(String requestXml) {
		// TODO Auto-generated method stub
		try {
			Map<String, String> params = CommUtil.doXmlToMap(requestXml);
			String orderNo = params.get("out_trade_no");
			//业务处理
			
			
			
			
			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
