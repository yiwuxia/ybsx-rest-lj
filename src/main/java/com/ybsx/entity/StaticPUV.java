package com.ybsx.entity;


    /**
    * @ClassName: StaticPUV
    * @Description: TODO(这里用一句话描述这个类的作用)
    * @author Administrator
    * @date 2018年6月26日
    *
    */

public class StaticPUV {
//	private String url;//页面链接
	private String dateTime;//日期
	private String pvCount;//此日期pv
	private String uvCount;//此日期uv
	private String shareCount;//此日期分享数
	
	
	public StaticPUV(){}
	
	    
	public StaticPUV(String dateTime) {
		this.dateTime = dateTime;
		this.pvCount = "0";
		this.uvCount = "0";
		this.shareCount = "0";
	}



	/*public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}*/
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getPvCount() {
		return pvCount;
	}
	public void setPvCount(String pvCount) {
		this.pvCount = pvCount;
	}
	public String getUvCount() {
		return uvCount;
	}
	public void setUvCount(String uvCount) {
		this.uvCount = uvCount;
	}
	public String getShareCount() {
		return shareCount;
	}
	public void setShareCount(String shareCount) {
		this.shareCount = shareCount;
	}
	
	
	
	
}
