package com.songhj.facade;

import com.songhj.config.WeiXinConfig;
import com.songhj.entity.WxUser;

/**
 * 微信授权登录
 * @author songhj
 *
 */
public interface LoginFacadeService {

	/**
	 * 获取微信配置信息
	 * @return
	 */
	public WeiXinConfig getWeiXinConfig();
	
	/**
	 * 获取微信用户信息
	 * @param code
	 * @return
	 */
	public WxUser getWxUserByCode(String code);
	
}
