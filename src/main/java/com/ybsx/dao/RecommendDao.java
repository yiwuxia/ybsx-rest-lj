package com.ybsx.dao;

import java.util.List;
import java.util.Map;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.ybsx.entity.PostStaticInfo;
import com.ybsx.entity.UserPostNum;

public interface RecommendDao {

	List<Integer> getRecordUserIds();

	List<UserPostNum> getUserPostNumByType(int groupId);


	void saveRecommendList(Integer userId, List<RecommendedItem> items);

	int getGroupIdByUserId(long userId);

	List<Integer> getNowMaxid();

	Map<String, Object> getIdAndTagsInfo(Integer integer);

	List<Map<String, Object>> getNearestMap();

	void saveSmlRecord(List<Object[]> list);

	void udaptePostIsCount(List<Object[]> id_update);

	List<Map<String, Object>> getIdAndTagsInfoList();

	List<PostStaticInfo> getTodayPost(String today);

	PostStaticInfo getDayPartOne(PostStaticInfo postStaticInfo, String today);

	String getTodayOpeNum(String id, int i, String today);

	void saveDaylyData(PostStaticInfo postStaticInfo);

	
}
