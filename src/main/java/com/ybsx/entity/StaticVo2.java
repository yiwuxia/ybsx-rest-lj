package com.ybsx.entity;


    /**
  页面：
总浏览量(pv)：页面进入或者刷新一次即记录一次
总访客量(uv)：单微信账号单日内进入不重复记录
(用户A 一天内重复进入只算一次  ，今天进一次 明天进一次 为 2 次)
总分享数：用户进行过成功的分享操作记录一次
总播放量：进行过视频播放操作的次数

平均页面停留时长：所有用户 单次进入页面的时长 之和/PV
（进入页面的时间 到 跳出页面的时间  为单次停留时长）
推荐页面跳转率=跳转到其他页面的总次数/PV 
分享率=分享数/PV
平均播放比例=每次视频播放的百分比之和/总播放量
完播率：播放超过80%的播放次数/总播放量
未播放率：（总浏览量-总播放量）/总浏览量
    */

public class StaticVo2 {
	private String dateTime;
	private String name;
	private String title;
	private String desc;
	private String video;
	private String content;
	private String pv;//总浏览量
	private String uv;//总访客量
	
	private String avgExpireRate;//平均停留时长
	private String forwardRate;//推荐页面跳转率
	private String shareCount;//总分享量
	private String shareRate;//分享率
	private String avgPlayRate;//平均播放比率
	private String playEndRate;//完播率
	
	private String unPlayRate;//未播放率
	private String requestUrl;
	private String status;//是否被冻结
	private String uid;
	
	
	
	
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPv() {
		return pv;
	}
	public void setPv(String pv) {
		this.pv = pv;
	}
	public String getUv() {
		return uv;
	}
	public void setUv(String uv) {
		this.uv = uv;
	}
	public String getAvgExpireRate() {
		return avgExpireRate;
	}
	public void setAvgExpireRate(String avgExpireRate) {
		this.avgExpireRate = avgExpireRate;
	}
	public String getForwardRate() {
		return forwardRate;
	}
	public void setForwardRate(String forwardRate) {
		this.forwardRate = forwardRate;
	}
	public String getShareCount() {
		return shareCount;
	}
	public void setShareCount(String shareCount) {
		this.shareCount = shareCount;
	}
	public String getShareRate() {
		return shareRate;
	}
	public void setShareRate(String shareRate) {
		this.shareRate = shareRate;
	}
	public String getAvgPlayRate() {
		return avgPlayRate;
	}
	public void setAvgPlayRate(String avgPlayRate) {
		this.avgPlayRate = avgPlayRate;
	}
	public String getUnPlayRate() {
		return unPlayRate;
	}
	public void setUnPlayRate(String unPlayRate) {
		this.unPlayRate = unPlayRate;
	}
	public String getPlayEndRate() {
		return playEndRate;
	}
	public void setPlayEndRate(String playEndRate) {
		this.playEndRate = playEndRate;
	}
	
	

	
	
}
