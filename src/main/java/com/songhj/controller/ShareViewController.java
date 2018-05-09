package com.songhj.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.songhj.facade.ShareFacadeService;

/**
 * 微信分享
 * @author songhj
 *
 */
@RestController
public class ShareViewController {

	@Autowired
	private ShareFacadeService shareFacadeService;
	
	/**
	 * 微信分享
	 * @param request
	 * @param response
	 * @param url
	 * @return
	 */
	@RequestMapping(value="/wx/share", method ={ RequestMethod.GET,RequestMethod.POST })
	public Map<String,Object> wxShare(HttpServletRequest request, HttpServletResponse response,
			String url){
		Map<String,Object> map = shareFacadeService.wxShare(url);
		return map;
	}
}
