package com.songhj.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.songhj.config.WeiXinConfig;
import com.songhj.entity.WxUser;
import com.songhj.facade.LoginFacadeService;
import com.songhj.util.CommUtil;

/**
 * 
 * @author songhj
 *
 */
@RestController
public class LoginViewController {

	private static final Logger logger = LoggerFactory.getLogger(LoginViewController.class);
			
	@Autowired
	private LoginFacadeService loginFacadeService;
	
	@RequestMapping(value="/login_success", method ={ RequestMethod.GET,RequestMethod.POST } )
	public Map<String,Object> login_success(HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		map.put("message", "登录成功");
		map.put("user", request.getSession().getAttribute("wxUser"));
		return map;
	}
	
	@RequestMapping(value="/", method ={ RequestMethod.GET,RequestMethod.POST } )
	public Map<String,Object> index(HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> map = new HashMap<>();
		map.put("message", "首页");
		return map;
	}
	
	/**
	 * 微信授权
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/wxAuth", method ={ RequestMethod.GET,RequestMethod.POST })
	public void wxAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(request.getSession().getAttribute("user") != null){
			response.sendRedirect("/");
			return;
		}
		WeiXinConfig weiXinConfig = loginFacadeService.getWeiXinConfig();
		
		String redirect_uri = request.getScheme() + "://" + request.getServerName();
		if(request.getServerPort() == 443){
			redirect_uri = "https://" + request.getServerName();
		}else if(request.getServerPort() != 80 ){
			redirect_uri += ":" + request.getServerPort();
		}
		redirect_uri += "/wxAuth/openid";
		
		String authUrl = weiXinConfig.getOauthUrl()
				.replace("${openAppid}", weiXinConfig.getOpenAppid())
				.replace("${redirectUri}", CommUtil.encode(redirect_uri));
		logger.info("====authUrl：\n" + authUrl);
		response.sendRedirect(authUrl);
	}
	
	
	/**
	 * 获取微信信息
	 * @param request
	 * @param response
	 * @param code
	 * @param state
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/wxAuth/openid", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public Map<String,Object> wxAuthOpenId(HttpServletRequest request, HttpServletResponse response,
			String code,String state) throws IOException{
		logger.info("微信授权获取code：" + code + "，state：" + state);
		Map<String,Object> map = new HashMap<>();
		WxUser wxUser = (WxUser) request.getSession().getAttribute("wxUser");
		if(wxUser == null){
			wxUser = loginFacadeService.getWxUserByCode(code);
		}
		if(wxUser != null){
			request.getSession().setAttribute("wxUser", wxUser);
			response.sendRedirect("/login_success");
		}else{
			map.put("code", "-1");
			map.put("message", "获取微信失败");
		}
		return map;
	}
}
