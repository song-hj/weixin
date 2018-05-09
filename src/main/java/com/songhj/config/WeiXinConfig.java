package com.songhj.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信配置基本信息
 * @author songhj
 * 
 * 微信自动登录配置
 * openAppid
 * openSecret
 * tokenUrl

 * 微信支付配置
 * mchid
 * apiSecret
 * orderUrl
 * notifyUrl
 * msgUrl
 * tempUrl

 * 微信JS-SDK
 * jsApiTicketUrl
 * jsApiTicket
 *
 */
@Component
@ConfigurationProperties(prefix = "wx")
public class WeiXinConfig {
	/**
	 * 公众号的appid
	 */
	private String openAppid;
	/**
	 * 公众号的密钥
	 */
	private String openSecret;
	/**
	 * 获取基础支持中的access_token的URL
	 */
	private String tokenUrl;
	/**
	 * Token
	 */
	private String accessToken;
	/**
	 * 微信授权
	 */
	private String oauthUrl;
	/**
	 * 获取网页授权access_token
	 */
	private String oauthTokenUrl;
	/**
	 * 获取微信用户信息
	 */
	private String userInfoUrl;
	
	
	
	/**
	 * 微信支付商户ID
	 */
	private String mchid;
	/**
	 * API密钥
	 */
	private String apiSecret;
	/**
	 * 统一下单URL
	 */
	private String orderUrl;
	
	/**
	 * 支付回调URL
	 */
	private String notifyUrl;
	/**
	 * 模板推送URL
	 */
	private String tempUrl;
	/**
	 * 文本推送URL
	 */
	private String msgUrl;
	
	
	
	/**
	 * 获取jsApiTicket URL
	 */
	private String jsApiTicketUrl;
	/**
	 * ticket
	 */
	private String jsApiTicket;
	
	
	public WeiXinConfig() {
//		openAppid = "wx421f0a88b7ffa47c";
//		openSecret = "bc5b0a5dc5046d2cd59d0b553286909f";
		tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=${openAppid}&secret=${openSecret}";
		accessToken = "";
		oauthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${openAppid}&redirect_uri=${redirectUri}&response_type=code&scope=snsapi_userinfo&state=stat#wechat_redirect";
		oauthTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=${openAppid}&secret=${openSecret}&code=${code}&grant_type=authorization_code";
		userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=${accessToken}&openid=${openid}&lang=zh_CN";
		
//		mchid = "1400648802";
//		apiSecret = "oJGc8HX1wT4R5rpj3GexOMK3Ix0H1281";
		orderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		notifyUrl = "http://ub.bxcker.com/wx/payCallback";
		tempUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=${accessToken}";
		msgUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=${accessToken}";
		
		jsApiTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=${accessToken}";
		jsApiTicket = "";
	}


	public String getOpenAppid() {
		return openAppid;
	}


	public void setOpenAppid(String openAppid) {
		this.openAppid = openAppid;
	}


	public String getOpenSecret() {
		return openSecret;
	}


	public void setOpenSecret(String openSecret) {
		this.openSecret = openSecret;
	}


	public String getTokenUrl() {
		return tokenUrl;
	}


	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}


	public String getAccessToken() {
		return accessToken;
	}


	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


	public String getOauthUrl() {
		return oauthUrl;
	}


	public void setOauthUrl(String oauthUrl) {
		this.oauthUrl = oauthUrl;
	}


	public String getOauthTokenUrl() {
		return oauthTokenUrl;
	}


	public void setOauthTokenUrl(String oauthTokenUrl) {
		this.oauthTokenUrl = oauthTokenUrl;
	}


	public String getUserInfoUrl() {
		return userInfoUrl;
	}


	public void setUserInfoUrl(String userInfoUrl) {
		this.userInfoUrl = userInfoUrl;
	}


	public String getMchid() {
		return mchid;
	}


	public void setMchid(String mchid) {
		this.mchid = mchid;
	}


	public String getApiSecret() {
		return apiSecret;
	}


	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}


	public String getOrderUrl() {
		return orderUrl;
	}


	public void setOrderUrl(String orderUrl) {
		this.orderUrl = orderUrl;
	}


	public String getNotifyUrl() {
		return notifyUrl;
	}


	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}


	public String getTempUrl() {
		return tempUrl;
	}


	public void setTempUrl(String tempUrl) {
		this.tempUrl = tempUrl;
	}


	public String getMsgUrl() {
		return msgUrl;
	}


	public void setMsgUrl(String msgUrl) {
		this.msgUrl = msgUrl;
	}


	public String getJsApiTicketUrl() {
		return jsApiTicketUrl;
	}


	public void setJsApiTicketUrl(String jsApiTicketUrl) {
		this.jsApiTicketUrl = jsApiTicketUrl;
	}


	public String getJsApiTicket() {
		return jsApiTicket;
	}


	public void setJsApiTicket(String jsApiTicket) {
		this.jsApiTicket = jsApiTicket;
	}
	
}
