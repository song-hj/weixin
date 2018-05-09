package com.songhj.facade;

import java.util.Map;

/**
 * 微信分享
 * @author songhj
 *
 */
public interface ShareFacadeService {

	/**
	 * 微信分享朋友或朋友圈
	 * @return
	 */
	public Map<String,Object> wxShare(String url);
}
