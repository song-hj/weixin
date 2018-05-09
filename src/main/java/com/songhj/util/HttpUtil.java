package com.songhj.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.DefaultCookieSpec;
import org.apache.http.impl.cookie.DefaultCookieSpecProvider;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

public class HttpUtil {
	
	private static Logger log =LoggerFactory.getLogger(HttpUtil.class);
	
	public static String post(String url, Map<String, String> params) {
		BasicCookieStore cookieStore = new BasicCookieStore();
        CookieSpecProvider easySpecProvider = new CookieSpecProvider() {
            public CookieSpec create(HttpContext context) {
                return new DefaultCookieSpec() {
                    @Override
                    public void validate(Cookie cookie, CookieOrigin origin)
                            throws MalformedCookieException {
                    }
                };
            }
        };
        Registry<CookieSpecProvider> r = RegistryBuilder
                .<CookieSpecProvider> create()
                .register(CookieSpecs.DEFAULT, new DefaultCookieSpecProvider())
                .register(CookieSpecs.DEFAULT, new DefaultCookieSpecProvider())
                .register("easy", easySpecProvider).build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec("easy").setSocketTimeout(10000)
                .setConnectTimeout(10000).build();
        HttpClient httpClient = HttpClients.custom().setDefaultCookieSpecRegistry(r)
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore).build();
		String body = null;
		log.info("create httppost:" + url);
		HttpPost post = postForm(url, params);
		body = invoke(httpClient, post);
		return body;
	}
	
	public static String post(String url, String params) {
		BasicCookieStore cookieStore = new BasicCookieStore();
        CookieSpecProvider easySpecProvider = new CookieSpecProvider() {
            public CookieSpec create(HttpContext context) {
                return new DefaultCookieSpec(){
                    @Override
                    public void validate(Cookie cookie, CookieOrigin origin)
                            throws MalformedCookieException {
                    }
                };
            }
        };
        Registry<CookieSpecProvider> r = RegistryBuilder
                .<CookieSpecProvider> create()
                .register(CookieSpecs.DEFAULT, new DefaultCookieSpecProvider())
                .register(CookieSpecs.DEFAULT, new DefaultCookieSpecProvider())
                .register("easy", easySpecProvider).build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec("easy").setSocketTimeout(10000)
                .setConnectTimeout(10000).build();
        HttpClient httpClient = HttpClients.custom().setDefaultCookieSpecRegistry(r)
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore).build();
		String body = null;
		log.info("create httppost:" + url);
		HttpPost post = postForm(url, params);
		body = invoke(httpClient, post);
		return body;
	}
	
	public static String get(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		String body = null;
		log.info("create httppost:" + url);
		HttpGet get = new HttpGet(url);
		body = invoke(httpclient, get);
		return body;
	}
		
	
	private static String invoke(HttpClient httpclient,
			HttpUriRequest httpost) {
		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);
		return body;
	}

	private static String paseResponse(HttpResponse response) {
		log.info("get response from http server..");
		HttpEntity entity = response.getEntity();
		log.info("response status: " + response.getStatusLine());
		String charset = EntityUtils.getContentCharSet(entity);
		log.info(charset);
		String body = null;
		try {
			body = EntityUtils.toString(entity);
			log.info(body);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return body;
	}

	private static HttpResponse sendRequest(HttpClient httpclient,
			HttpUriRequest httpost) {
		log.info("execute post...");
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	private static HttpPost postForm(String url, Map<String, String> params){
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList <NameValuePair>();
		Set<String> keySet = params.keySet();
		for(String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}
		try {
			log.info("set utf-8 form entity to httppost");
			httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return httpost;
	}
	
	private static HttpPost postForm(String url, String params){
		HttpPost httpost = new HttpPost(url);
		try {
			log.info("set utf-8 form entity to httppost");
			httpost.setEntity(new StringEntity(params,"UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpost;
	}
	
	public static String sendRequestData(Object requestData, String urlStr, Long timeOut) {
        System.out.println("=======请求开始=======");
        long a = System.currentTimeMillis();
        URL url = null;
        HttpURLConnection conn = null;
        ByteArrayOutputStream byteOut = null;
        BufferedReader readInfo = null;
        OutputStream out = null;
        String returnXml = "";
        try {
            System.out.println("URL=" + urlStr);
            url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod(RequestMethod.GET.toString());
            conn.setRequestProperty("SOAPAction", "\"\"");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("content-type", "text/html;charset=utf-8");
            conn.setRequestProperty("User-Agent", "Axis/1.4");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setUseCaches(false); // 忽略缓存
            conn.setDoOutput(true); // 使用 URL 连接进行输入
            conn.setDoInput(true); // 使用 URL 连接进行输入
            conn.setConnectTimeout(timeOut.intValue());
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {// 正确返回
                readInfo = new BufferedReader(new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
                returnXml = (conn.getURL() == null ? "" : conn.getURL()).toString();
            }
        }
        catch (SocketException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                if (readInfo != null) {
                    readInfo.close();
                }
                if (byteOut != null) {
                    byteOut.close();
                }
                if (out != null) {
                    out.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        System.out.println("\r<br>===do request time执行耗时 : " + (System.currentTimeMillis() - a) / 1000f + " 秒 ");
        System.out.println("=======请求结束=======");
        return returnXml;
    }
}

