package com.songhj.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.songhj.facade.PayFacadeService;
import com.songhj.util.CommUtil;

/**
 * 微信支付
 * @author songhj
 *
 */
@RestController
public class PayViewController {
	
	@Autowired
	private PayFacadeService payFacadeService;
	
	/**
	 * 微信发起支付
	 * @param request
	 * @param response
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/wx/pay", method ={ RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public Map<String,Object> wxPay(HttpServletRequest request, HttpServletResponse response,
			String orderNo) throws Exception{
		String clientIp = CommUtil.getIpAddr(request);
		String openId = "ocU1mwjVRN5s3VQ9pQP7j49i0zgU";
		Map<String,Object> map = payFacadeService.wxPay(openId ,orderNo, clientIp);
		return map;
	}
	
	
	/**
	 * 微信支付成功回调
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/wx/payCallback", method ={ RequestMethod.GET,RequestMethod.POST })
	public String payWebhooks(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String requestXml = CommUtil.getXmlString(request);
        String responseXml = "";
		try {
			boolean resutl = payFacadeService.payCallback(requestXml);
		    if(resutl){
		    	responseXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
		    }else{
		    	responseXml = "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
		    }
		} catch (Exception e) {
			e.printStackTrace();
			responseXml = "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
		}
		return responseXml;
	}
	
}
