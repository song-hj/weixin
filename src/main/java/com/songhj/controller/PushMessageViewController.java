package com.songhj.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.songhj.facade.PushMessageFacadeService;

/**
 * 微信推送
 * @author songhj
 *
 */
@RestController
public class PushMessageViewController {
	
	@Autowired
	private PushMessageFacadeService pushMessageFacadeService;
	
	/**
	 * 微信推送
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/wx/push", method ={ RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public Map<String,Object> wxPush(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		boolean result = pushMessageFacadeService.sendWxCustomMessage("收到", "owaHU1QcBPLVb8dSycZuN_x7Wv_A");
		map.put("result", result);
		return map;
	}
}
