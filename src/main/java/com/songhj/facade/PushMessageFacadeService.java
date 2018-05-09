package com.songhj.facade;
/**
 * 微信推送
 * @author songhj
 *
 */
public interface PushMessageFacadeService {

	/**
	 * 发送图文到单个用户
	 * @param message
	 * @param openId
	 * @return
	 */
	public boolean sendWxCustomMessage(String message,String openId);
	
	/**
	 * 微信模版信息推送
	 * @param openId
	 * @return
	 */
	public boolean sendWxTempMessage(String openId);
	
	/**
	 * type : 0发送文字消息  1发送模版
	 * @param type
	 * @param message
	 * @return
	 */
	public boolean postWxMessage(int type, String message);
}
