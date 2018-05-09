package com.songhj.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.songhj.config.WeiXinConfig;
import com.songhj.facade.PushMessageFacadeService;
import com.songhj.util.CommUtil;
import com.songhj.util.HttpUtil;

/**
 * 微信推送
 * @author songhj
 *
 */
@Service
public class PushMessageService implements PushMessageFacadeService {

	private static final Logger logger = LoggerFactory.getLogger(PushMessageService.class);
			
	@Autowired
	private LoginService loginService;
	
	@Override
	public boolean sendWxCustomMessage(String message, String openId) {
		// TODO Auto-generated method stub
		boolean result = false;
		try {
			Map<String,Object> map = new HashMap<>();
			map.put("touser", openId);
			map.put("msgtype", "text");
			Map<String,Object> contentMap = new HashMap<>();
			contentMap.put("content", message);
			map.put("text", contentMap);
			result = this.postWxMessage(0, JSONObject.toJSONString(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean sendWxTempMessage(String openId) {
		// TODO Auto-generated method stub
		boolean result = false;
		try {
			String templateId = "zQUs0wPRGke3McwonLBecjNhqv3KLsi9plwshdXLGm8";
			String title = "支付成功";
			String orderNo = "20180508";
			String url = "http://ub.bxcker.com/order/list.shtml?orderNo=" + orderNo;
			String content = "{\"url\": \"${url}\", \"data\": {\"first\": {\"color\": \"#173177\", \"value\": \"${title}\"}, "
					+ "\"keyword1\": {\"color\": \"#173177\", \"value\": \"${price}\"}, "
					+ "\"keyword2\": {\"color\": \"#173177\", \"value\": \"${riskName}\"}, "
					+ "\"keyword3\": {\"color\": \"#173177\", \"value\": \"${userName}\"}, "
					+ "\"keyword4\": {\"color\": \"#173177\", \"value\": \"${payTime}\"}, "
					+ "\"keyword5\": {\"color\": \"#173177\", \"value\": \"${orderNo}\"}}, \"touser\": \"${openId}\", \"template_id\": \"${templateId}\"}";
			Map<String,Object> map = new HashMap<>();
			map.put("templateId", templateId);
			map.put("openId", openId);
			map.put("title", title);
			map.put("price", "100元");
			map.put("userName", "songhj");
			map.put("orderNo", orderNo);
			map.put("payTime", CommUtil.formatLongDate(new Date()));
			map.put("url", url);
			map.put("riskName", "机动车综合保险");
			content = CommUtil.getTemplateString(content, map);
			result = postWxMessage(1, content);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * type : 0发送文字消息  1发送模版
	 * @param type
	 * @param message
	 * @return
	 */
	@Override
	public boolean postWxMessage(int type, String message) {
		// TODO Auto-generated method stub
		WeiXinConfig weiXinConfig = loginService.getWeiXinConfig();
		if(weiXinConfig == null){
			return false;
		}
		String url = "";
		if(type == 0){
			url = weiXinConfig.getMsgUrl().replace("${accessToken}", weiXinConfig.getAccessToken());
		}else{
			url = weiXinConfig.getTempUrl().replace("${accessToken}", weiXinConfig.getAccessToken());
		}
		logger.info("微信推送发送报文：\n ulr:" + url + "\nmessage:" + message);
		String result = HttpUtil.post(url, message);
		logger.info("微信推送返回报文：" + result);
		JSONObject json = JSONObject.parseObject(result);
		if("ok".equals(json.getString("errmsg"))){
			return true;
		}
		return false;
	}

}
