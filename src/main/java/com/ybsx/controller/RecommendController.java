package com.ybsx.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redare.devframework.common.pojo2.CommonResult;
import com.ybsx.dao.RecommendDao;
import com.ybsx.entity.PostStaticInfo;
import com.ybsx.service.RecommendationService;



/**
 * 投诉控制器
 * @createDate 2018年4月19日 上午11:28:09
 */
@RestController
@RequestMapping(value = "/recommend")
public class RecommendController {
	
	private  static  int SIZE=3;
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	 private RecommendationService recommendationService;
	
	@Autowired
	RecommendDao recommendDao;
	 
	
	@GetMapping("/insert")
	public String Insert(){
		
    	
    	/* (历史数据不会变动)
    	 * 获取今天的文章 //recommendationService
    	 * 计算各项数据
    	 * 插入表中 
    	 */
    	//获取今天所有文章
    	try {
    		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    		SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMdd");
    		
    		Calendar calendar=Calendar.getInstance();
    		calendar.add(Calendar.DATE, -1);
    		String today=sdf.format(calendar.getTime());
    		List<PostStaticInfo> list=recommendDao.getTodayPost(today);
    		for (PostStaticInfo postStaticInfo : list) {
    			PostStaticInfo todayPartOne=recommendDao.getDayPartOne(postStaticInfo,sdf2.format(calendar.getTime()));
    			//将小数处理成百分数等 还有点赞数 收藏数 评论数
    			todayPartOne.setId(postStaticInfo.getId());
    			todayPartOne.setDateTime(today);
    			recommendationService.dealWithValue(todayPartOne,today);
    			recommendDao.saveDaylyData(todayPartOne);
    		}
		} catch (Exception e) {
			logger.info("erro in insertPostToDaily mission"+e.getClass());
			e.printStackTrace();
		}
     			
    	return "1";
		
	}
		
	@RequestMapping(value = "/getRecommendList",method = RequestMethod.POST)
	public CommonResult getRecommendList(
			 @RequestParam long userId,
			@RequestParam(required = false, defaultValue = "3") int howMany
			){
			
		// step:1 构建模型 2 计算相似度 3 查找k紧邻 4 构造推荐引擎
		/*List<RecommendedItem> recommendations = null;
		try {
			DataModel dataModel=recommendationService.getDateModelByUserId(userId);
			UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);//用PearsonCorrelation 算法计算用户相似度
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, similarity, dataModel);//计算用户的“邻居”，这里将与该用户最近距离为 3 的用户设置为该用户的“邻居”。
			Recommender recommender = new CachingRecommender(new GenericUserBasedRecommender(dataModel, neighborhood, similarity));//采用 CachingRecommender 为 RecommendationItem 进行缓存
			recommendations = recommender.recommend(userId, howMany);//得到推荐的结果，size是推荐结果的数目
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (RecommendedItem recommendedItem : recommendations) {
			System.out.println(recommendedItem.getItemID()+":"+recommendedItem.getValue());
		}*/
		return null;
	
	}
	
	
	
	
}
