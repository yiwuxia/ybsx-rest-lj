package com.ybsx.entity;

public class PostStaticInfo {
	
	/*
	 * post id
	 */
	private String id;
	
	private String uid;
	
	/*
	 * 所属团队
	 */
	private String groupName;
	/*
	 * 标题
	 */
	private String title;
	/*
	 * uv
	 */
	private String uv;
	/*
	 *	pv
	 */
	private String pv;
	/*
	 * 点赞数
	 */
	private String likeNum;
	/*
	 * 收藏数
	 */
	private String favorNum;
	/*
	 * 评论数
	 */
	private String commentNum;
	
	/*
	 * 作者
	 */
	private String author;
	/*
	 * 作者
	 */
	private String createdAt;
	/*
	 * 状态
	 */
	private String status;
	
	/*
	 * 时间 页面操作
	 */
	private String dateTime;
	
	
	
	
	
	
	
	
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	private String avgExpireRate;//平均停留时长
	private String forwardRate;//推荐页面跳转率
	private String shareRate;//分享率
	private String avgPlayRate;//平均播放比率
	private String playEndRate;//完播率
	private String shareCount;//总分享量
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
	public String getPlayEndRate() {
		return playEndRate;
	}
	public void setPlayEndRate(String playEndRate) {
		this.playEndRate = playEndRate;
	}

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUv() {
		return uv;
	}
	public void setUv(String uv) {
		this.uv = uv;
	}
	public String getPv() {
		return pv;
	}
	public void setPv(String pv) {
		this.pv = pv;
	}
	public String getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(String likeNum) {
		this.likeNum = likeNum;
	}
	public String getFavorNum() {
		return favorNum;
	}
	public void setFavorNum(String favorNum) {
		this.favorNum = favorNum;
	}
	public String getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	@Override
	public String toString() {
		return "PostStaticInfo [id=" + id + ", uid=" + uid + ", uv=" + uv + ", pv=" + pv + ", likeNum=" + likeNum
				+ ", favorNum=" + favorNum + ", commentNum=" + commentNum + ", dateTime=" + dateTime + "]";
	}
	
	
	
	
	
	
}
