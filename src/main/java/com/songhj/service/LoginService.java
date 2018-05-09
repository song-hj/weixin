package com.songhj.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.songhj.config.WeiXinConfig;
import com.songhj.entity.WxUser;
import com.songhj.facade.LoginFacadeService;
import com.songhj.util.HttpUtil;

/**
 * 微信授权登录
 * @author songhj
 *
 */
@Service
public class LoginService implements LoginFacadeService {

	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
			
	public static String accessToken = "";
	
	public static String jsApiTicket = "";
	
	public static long times = 0l;
	
	@Autowired
	private WeiXinConfig weiXinConfig;
	
	@Override
	public WeiXinConfig getWeiXinConfig(){
		long timeStamp = System.currentTimeMillis() / 1000;
		//当accessToken还差一分钟过期时候，重新获取
		if(times - timeStamp < 60){
			//获取accessToken
			String url = weiXinConfig.getTokenUrl()
					.replace("${openAppid}", weiXinConfig.getOpenAppid())
					.replace("${openSecret}", weiXinConfig.getOpenSecret());
			String resultJSON = HttpUtil.get(url);
			logger.info("获取accessToken返回结果：" + resultJSON);
			JSONObject josn = JSONObject.parseObject(resultJSON);
			if(josn.get("access_token") != null){
				accessToken = josn.getString("access_token");
			}else{
				return null;
			}
			
			//获取jsApiTicket
			resultJSON = HttpUtil.get(weiXinConfig.getJsApiTicketUrl().replace("${accessToken}", accessToken));
			logger.info("获取jsApiTicket返回结果：" + resultJSON);
			josn = JSONObject.parseObject(resultJSON);
			if(josn.get("ticket") != null){
				jsApiTicket = josn.getString("ticket");
			}else{
				return null;
			}
			times = timeStamp + 7200;
		}
		weiXinConfig.setAccessToken(accessToken);
		weiXinConfig.setJsApiTicket(jsApiTicket);
		return weiXinConfig;
	}

	@Override
	public WxUser getWxUserByCode(String code) {
		// TODO Auto-generated method stub
		WeiXinConfig weiXinConfig = this.getWeiXinConfig();
		if(weiXinConfig == null){
			return null;
		}
		String url = weiXinConfig.getOauthTokenUrl()
				.replace("${openAppid}", weiXinConfig.getOpenAppid())
				.replace("${openSecret}", weiXinConfig.getOpenSecret())
				.replace("${code}", code);
		String resultJSON = HttpUtil.get(url);
		logger.info("获取用户openId返回结果：" + resultJSON);
		JSONObject josn = JSONObject.parseObject(resultJSON);
		if(josn.get("openid") != null){
			url = weiXinConfig.getUserInfoUrl()
					.replace("${accessToken}", weiXinConfig.getAccessToken())
					.replace("${openid}", josn.getString("openid"));
			resultJSON = HttpUtil.get(url);
			logger.info("获取用户wxUser返回结果：" + resultJSON);
			WxUser wxUser = JSONObject.toJavaObject(JSONObject.parseObject(resultJSON), WxUser.class);
			return wxUser;
		}
		return null;
	}

}
