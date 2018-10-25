package com.ybsx.entity;

import java.util.List;

/**
    * @ClassName: ResultMap
    * @Description: TODO(这里用一句话描述这个类的作用)
    * @author Administrator
    * @date 2018年7月12日
    *
    */

public class ResultMap<T> {
	private int code;
	private String msg;
	private int count;
	private List<T> data;
	
	public static <T> ResultMap<T> getSuccessResultMap(){
		ResultMap<T> resultMap=new ResultMap<T>();
		resultMap.setCode(0);
		resultMap.setMsg("");
		return resultMap;
	}
	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	
	
	
}
