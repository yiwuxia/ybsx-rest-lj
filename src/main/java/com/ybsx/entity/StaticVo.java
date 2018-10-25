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

public class StaticVo {
	private String browseCount;//总浏览量
	private String visitorCount;//总访客量
	private String shareCount;//总分享量
	private String playCount;//总播放量
	private String avgExpireRate;//平均停留时长
	private String forwardRate;//推荐页面跳转率
	
	private String shareRate;//分享率
	private String unPlayRate;//未播放率
	private String playEndRate;//完播率
	//private String url;//页面链接
	private String avgPlayRate;//平均播放比率
	
	
	private String  praiseCount;//点赞数
	private String  collectCount;//收藏数

	
	
	
	public String getPraiseCount() {
		return praiseCount;
	}
	public void setPraiseCount(String praiseCount) {
		this.praiseCount = praiseCount;
	}
	public String getCollectCount() {
		return collectCount;
	}
	public void setCollectCount(String collectCount) {
		this.collectCount = collectCount;
	}
	public String getAvgPlayRate() {
		return avgPlayRate;
	}
	public void setAvgPlayRate(String avgPlayRate) {
		this.avgPlayRate = avgPlayRate;
	}
	public String getBrowseCount() {
		return browseCount;
	}
	public void setBrowseCount(String browseCount) {
		this.browseCount = browseCount;
	}
	public String getVisitorCount() {
		return visitorCount;
	}
	public void setVisitorCount(String visitorCount) {
		this.visitorCount = visitorCount;
	}
	public String getShareCount() {
		return shareCount;
	}
	public void setShareCount(String shareCount) {
		this.shareCount = shareCount;
	}
	public String getPlayCount() {
		return playCount;
	}
	public void setPlayCount(String playCount) {
		this.playCount = playCount;
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
	public String getAvgExpireRate() {
		return avgExpireRate;
	}
	public void setAvgExpireRate(String avgExpireRate) {
		this.avgExpireRate = avgExpireRate;
	}
	/*public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}*/
	public String getForwardRate() {
		return forwardRate;
	}
	public void setForwardRate(String forwardRate) {
		this.forwardRate = forwardRate;
	}
	public String getShareRate() {
		return shareRate;
	}
	public void setShareRate(String shareRate) {
		this.shareRate = shareRate;
	}
	@Override
	public String toString() {
		return "StaticVo [browseCount=" + browseCount + ", visitorCount=" + visitorCount + ", shareCount=" + shareCount
				+ ", playCount=" + playCount + ", avgExpireRate=" + avgExpireRate + ", forwardRate=" + forwardRate
				+ ", shareRate=" + shareRate + ", unPlayRate=" + unPlayRate + ", playEndRate=" + playEndRate
				+ ", avgPlayRate=" + avgPlayRate + "]";
	}

	
	
	

	
	
}
