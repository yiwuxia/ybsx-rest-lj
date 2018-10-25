package com.ybsx.service;

import java.util.List;

import com.redare.devframework.common.pojo.Page;
import com.ybsx.entity.StaticPUV;
import com.ybsx.entity.StaticVo;
import com.ybsx.entity.StaticVo2;

public interface ArticleService {
	
	/*void articleOperate(String articleId, String userId, String type, String type2);
	
	List<ArticleVo> queryStatics(String type);*/

	
	    /**
	     * @param url 
	    * @Title: getStaticByPageUrl
	    * @Description: TODO(这里用一句话描述这个方法的作用)
	    * @param @return    参数
	    * @return StaticVo    返回类型
	    * @throws
	    */
	    
	StaticVo getStaticByPageUrl(String url,String type);

		
		    /**
		    * @Title: getSevenDayData
		    * @Description: TODO(这里用一句话描述这个方法的作用)
		    * @param @param url
		    * @param @return    参数
		    * @return List<StaticPUV>    返回类型
		    * @throws
		    */
		    
		List<StaticPUV> getSevenDayData(String url);


			
			    /**
			    * @Title: getVedioPage
			    * @Description: 获取每天视频的播放信息
			    * @param @param name
			    * @param @param startDate
			    * @param @param endDate
			    * @param @param page
			    * @param @param limit    参数
			    * @return void    返回类型
			    * @throws
			    */
			    
		Page<StaticVo2> getVedioPage(String name, String startDate, String endDate,String type,String title, int page, int limit);


		void saveTags(String tag, String str);


		List<String> getTagIds(String result);


		void savePostTags(String tagId, int id_parse,String time);

		/*
		 * 根据文章id查找文章的title和content
		 */
		String getArticheTagsById(String id);

		/*
		 * 查询详情
		 */
		Page<StaticVo2> getDetailByDate(String uid, String startDate, String endDate, int page, int limit,String type);


		void insertRecordToScore(String id);
			

		List<Integer> getMostSimilarity(int id);


	
}


