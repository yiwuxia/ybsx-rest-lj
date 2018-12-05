package com.ybsx.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ybsx.dao.RecommendDao;
import com.ybsx.entity.PostStaticInfo;
import com.ybsx.util.SendEmail;

@Component
public class SchedulerTaskTest {
	
	
		private Logger logger = Logger.getLogger(getClass());
	

		
		@Autowired
		RecommendDao recommendDao;
			
		@Autowired
		RecommendationService recommendationService;
		
		//private static int count = 0 ;
		
		

	    @Scheduled(cron = "0 0 0/3 * * ? ")   //先等待下面的先计算出数据
	    public void recommend() {//生成推荐结果
	    	long start=System.currentTimeMillis();
	    	List<Integer>  list= recommendDao.getRecordUserIds();
	    	for (Integer userId : list) {
	    		List<RecommendedItem> items= recommendationService.getRecommendations(userId);
	    		//保存数据
	    		if (items.size()==0) {
					continue;
				}
	    		//logger.info("recommend --userId"+userId+",recommend item size is "+items.size());
	    		recommendDao.saveRecommendList(userId,items);
			}
	    	long end=System.currentTimeMillis();
	    	logger.info("timeCount"+(end-start)/1000+"s");
	    }
		    /*
	     		计算新文章标签
		     */
	    @Scheduled(cron = "0/10 * * * * ? ")
	    public void calcNewPosts() {//计算刚出来的文章
	    	/*
	    	 * 查看是否新文章，有就计算
	    	 */
	    	try {
	    		List<Integer> max_ids=recommendDao.getNowMaxid();
	    		int maxId=0;
	    		Iterator<Integer> it=max_ids.iterator();
	    		List<Object[]> id_update=new ArrayList<>();
	    		while (it.hasNext()) {
	    			Integer integer = (Integer) it.next();
	    			maxId=Math.max(maxId, integer);
	    			Map<String, Object> map=recommendDao.getIdAndTagsInfo(integer);
	    			logger.error("calcing--------------"+integer+"map.size="+map.size());
	    			if (map==null||map.size()==0) {
						continue;
					}
	    			if (map.get("name")==null) {
	    				continue;
	    			}
	    			recommendationService.countSimilarity(map);
	    			
	    			id_update.add(new Object[]{integer});
	    		}
	    		recommendDao.udaptePostIsCount(id_update);
			} catch (Exception e) {
				e.printStackTrace();
				SendEmail.sendEmail(e.getCause().getMessage());
			}
	    	
	    }
	    
	    @Scheduled(cron = "0 0 0/1 * * ? ")
	    public void calcNewPostsAll() {//计算所有文章
	    	/*
	    	 * 查看是否新文章，有就计算
	    	 */
	    	try {
	    		List<Map<String, Object>> map=recommendDao.getIdAndTagsInfoList();
	    		for (Map<String, Object> map2 : map) {
	    			if (map2.get("name")==null) {
	    				continue;
	    			}
	    			recommendationService.countSimilarity(map2);
	    		}
			} catch (Exception e) {
				SendEmail.sendEmail(e.getCause().getMessage());
			}
	    	
	    }
	    
	    /*I
	     * 每天插入一条数据 该post的
	     */
	    @Scheduled(cron = "0 0 2 * * ? ")
	   // @Scheduled(cron = "0 35 10 * * ? ")
	    public void insertPostToDaily() {
	    	
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
	     			
	    }
	    
	
}
