package com.ybsx.util;

import java.util.HashMap;
import java.util.regex.Matcher;
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
				
		        HashMap<String, Object> options = new HashMap<String, Object>();
		        org.json.JSONObject res = getAipNlp().keyword(title, content, options);
		        System.out.println(res);
		        if (res.getJSONArray("items").length()==0) {
					return "";
				}else{
					String tags="";
			    	JSONArray jsonArr= res.getJSONArray("items");
			    	for (int i = 0; i <jsonArr.length(); i++) {
			    		tags=tags+jsonArr.getJSONObject(i).getString("tag")+",";
			    		//System.out.println("title is:"+title+"tag为:---"+jsonArr.getJSONObject(i).getString("tag"));
			    		if (i==1) {//只取最多两个标签
							break;
						}
			    	}
			    	if (tags.length()>0) {
						tags=tags.substring(0, tags.length()-1);
					}
			    	return tags;
				}
		        
				
			
				
			}
			
		
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
	
	
	public static String removeHtmlTag(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String regEx_html = "<[^>]+>";
		// 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		String regEx_special = "\\&[a-zA-Z]{1,10};";
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		java.util.regex.Pattern p_special;
		java.util.regex.Matcher m_special;
		p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签
		p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
		m_special = p_special.matcher(htmlStr);
		htmlStr = m_special.replaceAll(""); // 过滤特殊标签
		 Pattern p = Pattern.compile("\\s*|\t|\r|\n");   
         Matcher m = p.matcher(htmlStr);   
         htmlStr = m.replaceAll("");   
		return htmlStr;
	}	
			
			
			
}