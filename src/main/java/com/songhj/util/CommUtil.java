package com.songhj.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.alibaba.fastjson.JSONObject;

public class CommUtil {
	
	public static boolean isNotNull(Object obj) {
		if ((obj != null) && (!obj.toString().equals(""))) {
			return true;
		}
		return false;
	}
	
	/**
	 * 数字和字母组合
	 * @param length
	 * @return
	 */
	public static final String randomString(int length) {
		char[] numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		if (length < 1) {
			return "";
		}
		Random randGen = new Random();
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	/**
	 * 数字
	 * @param length
	 * @return
	 */
	public static final String randomInt(int length) {
		if (length < 1) {
			return null;
		}
		Random randGen = new Random();
		char[] numbersAndLetters = "0123456789".toCharArray();

		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(10)];
		}
		return new String(randBuffer);
	}
	
	
	public static String formatLongDate(Object v) {
		if ((v == null) || (v.equals("")))
			return "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(v);
	}

	public static String formatShortDate(Object v) {
		if (v == null)
			return null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(v);
	}
	
	
	/**
	 * 模板引擎
	 * @param content
	 * @param map
	 * @return
	 */
	public static String getTemplateString(String content,Map<String,Object> map) {
		VelocityContext context = new VelocityContext();
		if(map != null){
			for(String key : map.keySet()){
				context.put(key,map.get(key));
			}
		}
		StringWriter stringWriter = new StringWriter();
		Velocity.init();
		Velocity.evaluate(context, stringWriter, "template.log",content);
		return stringWriter.toString().trim();
	}

	public static String decode(String s) {
		String ret = s;
		try {
			ret = URLDecoder.decode(s.trim(), "UTF-8");
		} catch (Exception localException) {
		}
		return ret;
	}

	public static String encode(String s) {
		String ret = s;
		try {
			ret = URLEncoder.encode(s.trim(), "UTF-8");
		} catch (Exception localException) {
		}
		return ret;
	}
	
	
	/**
	 * Map转Xml
	 * @param map
	 * @return
	 */
	public static String doMapToXml(Map<String, String> map) {
		Iterator<String> iter = map.keySet().iterator();
		List<String> list = new ArrayList<String>();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			list.add(key);
		}
		Collections.sort(list);
		String xml = "<xml>";
		for(String key : list){
			xml += "<" + key + ">" + map.get(key) + "</" + key + ">";
		}
		xml += "</xml>";
		return xml;
	}
	
	
	/**
	 * xml转Map
	 * @param xml
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static Map<String, String> doXmlToMap(String xml) 
			throws XmlPullParserException, IOException{
		Map<String, String> map = null;
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
		XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
		pullParser.setInput(inputStream, "UTF-8"); // 为xml设置要解析的xml数据
		int eventType = pullParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				map = new HashMap<>();
				break;
			case XmlPullParser.START_TAG:
				String key = pullParser.getName();
				if (key.equals("xml"))
					break;
				String value = pullParser.nextText();
				map.put(key, value);
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			eventType = pullParser.next();
		}
		return map;
	}
	
	/**
	 * 微信分享签名
	 * @param decript
	 * @return
	 */
	public static String SHA1(String decript) {
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			//字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	/**
	 * 微信签名认证
	 * @param params
	 * @param paternerKey
	 * @return
	 */
	public static String signVeryfy(Map<String, String> params, String paternerKey) {
		String stringA = "";
		Iterator<String> iter = params.keySet().iterator();
		List<String> list = new ArrayList<>();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			list.add(key);
		}
		Collections.sort(list);
		for(String key : list){
			stringA += key + "=" + params.get(key) + "&";
		}
		String stringSignTemp = stringA + "key=" + paternerKey;
		String signValue = Md5Encrypt.md5(stringSignTemp).toUpperCase();
		return  signValue;
	}
	
	/**
     * IO解析获取微信的数据
     * @param request
     * @return
     */
	public static String getXmlString(HttpServletRequest request) {
		BufferedReader reader = null;
		String line = "";
		String xmlString = null;
		try {
			reader = request.getReader();
			StringBuffer inputString = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				inputString.append(line);
			}
			xmlString = inputString.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlString;
	}
	
	/**
	 * 获取参数
	 * @param request
	 * @return
	 */
	public static JSONObject getRequestMap(HttpServletRequest request){
		Enumeration<String> keys = request.getParameterNames();
		JSONObject obj = new JSONObject();
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			obj.put(key, request.getParameter(key));
		}
		return obj;
	}
	
	/**
	 * 获取IP地址
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("x-real-ip");
		}
		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
    private static CommUtil instance = new CommUtil();
	
	public static CommUtil getInstance(){
		return instance;
	}
	
	/**
	 * 获取当前所在城市
	 * @param request
	 * @return
	 */
	public String getCity(HttpServletRequest request){
		String current_city = "";
        String current_ip = CommUtil.getInstance().getIp(request);
		try{
			current_city = getCityDetail(current_ip);
		}catch(Exception e){
			e.printStackTrace();
		}
		return current_city;
	}
	
	
	/**
	 * 获取IP地址
	 * @param request
	 * @return
	 */
	public String getIp(HttpServletRequest request) {
        try {
        	if (request != null) {
	        	return CommUtil.getIpAddr(request);
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return "unknown";
    }
	
	public static String getCityDetail(String ip) {
		if("localhost".equals(ip) || ip.startsWith("127.0.0.1") || "0:0:0:0:0:0:0:1".equals(ip) || ip.startsWith("192.168")){
			return "深圳市";
		}
		if(ip.indexOf(",") > -1){
			ip = ip.split(",")[0];
		}
		String result = HttpUtil.get("http://api.map.baidu.com/location/ip?ak=Q4haDC6GHi6dkX2SOIIwwqGb&ip="+ip+"&coor=bd09ll"); 
        JSONObject json = JSONObject.parseObject(result);
        if(json.getInteger("status") == 0){
        	String address = json.getJSONObject("content").getJSONObject("address_detail").getString("city");  
        	return address;  
        }
        return "unknow";
	}
}
