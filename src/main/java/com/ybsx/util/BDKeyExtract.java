package com.ybsx.util;

import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.baidu.aip.nlp.AipNlp;


public class BDKeyExtract{
	
	private  Logger logger = Logger.getLogger(getClass());
	
	public static final String APP_ID = "11825768";
    public static final String API_KEY = "zUKVQWzFRtUl05Se2wRY6qpR";
    public static final String SECRET_KEY = "IMoR7WrfZ6m5rrtDzRsPGsqREMvbiwnN";
    public static final int CONNECTIONTIMEOUT = 2000;
    public static final int SOCKETTIMEOUT = 60000;
	public static	AipNlp client=null;
	    
    static{
    	 client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
		 client.setConnectionTimeoutInMillis(CONNECTIONTIMEOUT);
        client.setSocketTimeoutInMillis(SOCKETTIMEOUT);
    }
	   
    public static AipNlp getAipNlp(){
    	return client;
    }
	
			public static String  keyExtract(String title,String content) {
				
				/*
				 * 请求参数的长度限制  title 80 字节;content 65535字节;
				 */
				 if (content.length()>20000) {
					 content=content.substring(0,20000);
				}
				 System.out.println(title);
				 if (title.length()>=26) {
					 title=title.substring(0,26);
				}
				
			//	content=content.replaceAll("<[.[^<]]*>", "");
				 String txtcontent = content.replaceAll("</?[^>]+>", ""); //剔出<html>的标签   
				 content = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");//去除字符串中的空格,回车,换行符,制表符
				 content=content.replaceAll("&nbsp;", "");
		        HashMap<String, Object> options = new HashMap<String, Object>();
		        org.json.JSONObject res = getAipNlp().keyword(title, content, options);
		        System.out.println(res);
		        String tags="";
	        	JSONArray jsonArr= res.getJSONArray("items");
	        	for (int i = 0; i <jsonArr.length(); i++) {
	        		tags=tags+jsonArr.getJSONObject(i).getString("tag")+",";
	        		System.out.println("title is:"+title+"tag为:---"+jsonArr.getJSONObject(i).getString("tag"));
	        		if (i==1) {//只取最多两个标签
						break;
					}
	        	}
	        	if (tags.length()>0) {
					tags=tags.substring(0, tags.length()-1);
				}
		        return tags;
				
			}
			
		/*public static void main(String[] args) {
			  String title = "iphone手机出现“白苹果”原因及解决办法，用苹果手机的可以看下";
		    String content = "如果下面的方法还是没有解决你的问题建议来我们门店看下成都市锦江区红星路三段99号银石广场24层01室。";
		    try {
				keyExtract(title,content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
			
			System.out.println(checkValidata("asdf444"));
			System.out.println(checkValidata("444"));
		    
		}*/	
		
		/*
		 * 检查参数是否有效
		 * true为有效整数
		 * 其他的false	
		 */
			
	public static 	boolean checkValidata(String id) {
		boolean validata=true;
		if (StringUtils.isEmpty(id)) {
			validata=false;
		}
		Pattern pattern = Pattern.compile("[0-9]*"); 
		validata=pattern.matcher(id).matches();
		return validata;
	}
			
			
			
			
}