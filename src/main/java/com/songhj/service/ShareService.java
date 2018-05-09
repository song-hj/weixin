package com.songhj.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.songhj.config.WeiXinConfig;
import com.songhj.facade.ShareFacadeService;
import com.songhj.util.CommUtil;

/**
 * 微信分享
 * @author songhj
 *
 */
@Service
public class ShareService implements ShareFacadeService {

	@Autowired
	private LoginService loginService;
	
	@Override
	public Map<String, Object> wxShare(String url) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<>();
		WeiXinConfig weiXinConfig = loginService.getWeiXinConfig();
		if(weiXinConfig == null){
			return null;
		}
		String appId = weiXinConfig.getOpenAppid();
		String nonceStr = CommUtil.randomString(16);
		long timeStamp = System.currentTimeMillis() / 1000;
		String decript = "jsapi_ticket=" + weiXinConfig.getJsApiTicket() + "&noncestr=" + nonceStr + "&timestamp=" + timeStamp +"&url=" + url;
		String signature = CommUtil.SHA1(decript);
		if(!CommUtil.isNotNull(signature)){
			map.put("code", -1);
			map.put("message", "签名失败");
		}
		Map<String,Object> params = new HashMap<>();
		params.put("appId", appId); //公众号名称，由商户传入 
		params.put("timeStamp", timeStamp); //时间戳，自1970年以来的秒数     
		params.put("nonceStr", nonceStr); //随机串 
		params.put("signature", signature);
		params.put("title", "");
		params.put("link", url);
		params.put("imgUrl", "");
		params.put("desc", "");
		
		map.put("code", 1);
		map.put("charge", params);
		
		return map;
	}

}
