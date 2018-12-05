package com.ybsx.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.aspectj.weaver.AjAttribute.PrivilegedAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hdfs.server.datanode.tail_jsp;
import org.apache.log4j.Logger;

import com.thoughtworks.xstream.mapper.Mapper.Null;
import com.ybsx.dao.RecommendDao;
import com.ybsx.entity.PostStaticInfo;
import com.ybsx.entity.UserPostNum;
import com.ybsx.service.RecommendationService;
import com.ybsx.util.StringUtil;

@Service("userBased")
public class UserBasedRecommendationService implements RecommendationService {

	private static int NEIGHBORHOOD_NEAREST=6;
	private static int HOW_MANY_DEFAULT=5;
	
	
	private Logger logger = Logger.getLogger(getClass());
	private static Map<Integer,DataModel> map=new HashMap<>();

	
	@Autowired
	RecommendDao recommendDao;
	

	 @Override
	  public List<RecommendedItem> getRecommendations(int userId) {
		 List<RecommendedItem>  list=new ArrayList<>();
		    UserSimilarity similarity=null;
			try {
				DataModel dataModel=getDateModelByUserId(userId);
				
				similarity = new PearsonCorrelationSimilarity(dataModel);
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(NEIGHBORHOOD_NEAREST, similarity, dataModel);//计算用户的“邻居”，这里将与该用户最近距离为 3 的用户设置为该用户的“邻居”。
				GenericUserBasedRecommender builder=new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
				list= builder.recommend(userId, HOW_MANY_DEFAULT);
			} catch (TasteException e) {
				e.printStackTrace();
			}
			return list;
	  }

	 
	 
	 
	public DataModel getDateModelByUserId(int user_Id) {
		int group_id=recommendDao.getGroupIdByUserId(user_Id);
		/*if (map.get(group_id)!=null) {//这里是隐患，如果后期数据量过大 无法全部放入内存中
			return map.get(group_id);
		}*/
		List<UserPostNum>  list=recommendDao.getUserPostNumByType(group_id);
		FastByIDMap<PreferenceArray> preferenceIDMap = new FastByIDMap<PreferenceArray>();
		Map<Integer, List<UserPostNum>> preferenceMap = new HashMap<Integer, List<UserPostNum>>();
		for (UserPostNum userPostNum : list) {
			List<UserPostNum> preferences = preferenceMap.get(userPostNum.getUser_id());
			if (preferences==null) {
				preferences=new ArrayList<UserPostNum>();
				preferenceMap.put(userPostNum.getUser_id(), preferences);
			}
			preferences.add(userPostNum);
		}
		
		for (UserPostNum userPostNum : list) {
			List<UserPostNum> preferences = preferenceMap.get(userPostNum.getUser_id());
			PreferenceArray prefForUser = new GenericUserPreferenceArray(preferences.size());
			prefForUser.setUserID(0, userPostNum.getUser_id());
			int itemIndex = 0;
			for (UserPostNum pref : preferences) {
				prefForUser.setItemID(itemIndex, pref.getPost_id());
				prefForUser.setValue(itemIndex++, pref.getNum());
			}
			preferenceIDMap.put(userPostNum.getUser_id(), prefForUser);
		}
		DataModel dataModel=new GenericDataModel(preferenceIDMap);
	//	map.put(group_id, dataModel);
		return dataModel;
	}




	
	    /* (非 Javadoc)
	    * 
	    * 
	    * @param map
	    * @see com.ybsx.service.RecommendationService#countSimilarity(java.util.Map)
	    */
	  /*
	   * 计算和当前文档最相近的最近三百篇文章
	   */
	@Override
	public void countSimilarity(Map<String, Object> map) {
		try {
			String[] tag_arr_a=map.get("name").toString().split(",");//当前文章标签
			int postId=Integer.valueOf(map.get("id").toString());
			
			//最近三百条post
			List<Map<String, Object>> nearestMap=recommendDao.getNearestMap();
			List<Integer> ids=Arrays.asList(0,0,0);
			List<Double> sml=Arrays.asList(-1.0,-1.0,-1.0);
			for (Map<String, Object>  m : nearestMap) {
				int id =Integer.valueOf(m.get("id").toString());
				if (id==postId) {
					continue;
				}
				String[] tag_arr_b=m.get("name").toString().split(",");
				double smlValue=calcalateSml(tag_arr_a,tag_arr_b);
				double minValue=Collections.min(sml);
				int index=sml.indexOf(minValue);
				if(smlValue>=minValue){
					sml.set(index, smlValue);
					ids.set(index, id);
				}
			}
			List<Object[]> list =new ArrayList<>();
			for (int i = 0; i < ids.size(); i++) {
				Object[] arr=new Object[5];
				arr[0]=postId;
				arr[1]=ids.get(i);
				arr[2]=sml.get(i);
				arr[3]=0;//没登陆的情况不需要group_id
				arr[4]=sml.get(i);
				list.add(arr);
			}
			recommendDao.saveSmlRecord(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}




	private double calcalateSml(String[] arrSrc, String[] arrDes) {
		List<List<Integer>> list =new ArrayList<>();
		
		List<String> aList=new ArrayList<String>(Arrays.asList(arrSrc));
		List<String> bList=new ArrayList<String>(Arrays.asList(arrDes));

		
		List<String> aListTemp=new ArrayList<String>(Arrays.asList(arrSrc));
		List<String> bListTemp=new ArrayList<String>(Arrays.asList(arrDes));
		
		
		aList.removeAll(bList);
		bList.addAll(aList);
		List<Integer> x=new ArrayList<>(bList.size());
		List<Integer> y=new ArrayList<>(bList.size());
		for (String item : bList) {
			if (aListTemp.contains(item)) {
				x.add(1);
			}else{
				x.add(0);
			}
			if (bListTemp.contains(item)) {
				y.add(1);
			}else{
				y.add(0);
			}
		}
		double sum01=0;
		double x_r=0;
		double y_r=0;
		for (int i = 0; i < x.size(); i++) {
			/*
			 * 余弦值
			 */
			
		    sum01=sum01+x.get(i)*y.get(i);
			x_r=x_r+Math.pow(x.get(i), 2);
			y_r=y_r+Math.pow(y.get(i), 2);
		}
		DecimalFormat df = new DecimalFormat("#.0000");
		double value=sum01/(Math.sqrt(x_r)+Math.sqrt(y_r));
		return  Double.valueOf(df.format(value));
		
	}




	
	    
	@Override
	public void dealWithValue(PostStaticInfo todayPartOne,String today) {
		//todayPartOne.setCreatedAt(today);
		if (StringUtils.isEmpty(todayPartOne.getPv())) {
			//todayPartOne=new PostStaticInfo();
			todayPartOne.setPv("0");
			todayPartOne.setUv("0");
			todayPartOne.setAvgExpireRate("0分0秒");
			todayPartOne.setForwardRate("0");
			todayPartOne.setShareRate("0");
			todayPartOne.setAvgPlayRate("0");
			todayPartOne.setPlayEndRate("0");
			todayPartOne.setShareCount("0");
		}else{
			//平均停留时长
			todayPartOne.setAvgExpireRate(StringUtil.transSecondToMS(Double.valueOf(todayPartOne.getAvgExpireRate())));
			//推荐页面跳转率
			todayPartOne.setForwardRate(StringUtil.tranToPercent(Double.valueOf(todayPartOne.getForwardRate()==null?"0":todayPartOne.getForwardRate())));
			//分享率
			todayPartOne.setShareRate(StringUtil.tranToPercent(Double.valueOf(todayPartOne.getShareRate()==null?"0":todayPartOne.getShareRate())));
			//平均播放比例
			todayPartOne.setAvgPlayRate(StringUtil.tranToPercent(Double.valueOf(todayPartOne.getAvgPlayRate()==null?"0":todayPartOne.getAvgPlayRate())));
			//完播率
			//System.out.println(todayPartOne.getAvgPlayRate());
			todayPartOne.setPlayEndRate(StringUtil.tranToPercent(Double.valueOf(todayPartOne.getPlayEndRate()==null?"0":todayPartOne.getPlayEndRate())));
		}
		todayPartOne.setLikeNum(recommendDao.getTodayOpeNum(todayPartOne.getId(),1,today));//点赞
		todayPartOne.setFavorNum(recommendDao.getTodayOpeNum(todayPartOne.getId(),2,today));//收藏
		todayPartOne.setCommentNum(recommendDao.getTodayOpeNum(todayPartOne.getId(),3,today));//评论
		
	}


}
