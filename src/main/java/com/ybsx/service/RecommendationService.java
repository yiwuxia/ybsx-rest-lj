package com.ybsx.service;

import java.util.List;
import java.util.Map;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.ybsx.entity.PostStaticInfo;

public interface RecommendationService {
	List<RecommendedItem> getRecommendations(int userId);

	DataModel getDateModelByUserId(int userId);

	void countSimilarity(Map<String, Object> map);

	void dealWithValue(PostStaticInfo todayPartOne, String today);
}
