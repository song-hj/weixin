package com.songhj.facade;

import java.util.Map;

/**
 * 微信支付
 * @author songhj
 *
 */
public interface PayFacadeService {

	/**
	 * 微信发起支付
	 * @param orderNo
	 * @param clientIp
	 * @return
	 */
	public Map<String,Object> wxPay(String openId, String orderNo, String clientIp);

	/**
	 * 支付回调
	 * @param requestXml
	 * @return
	 */
	public boolean payCallback(String requestXml);
}
