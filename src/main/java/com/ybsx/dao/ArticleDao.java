package com.ybsx.dao;

import java.util.List;

import com.redare.devframework.common.pojo.Page;
import com.ybsx.entity.PostStaticInfo;
import com.ybsx.entity.Static;
import com.ybsx.entity.StaticPUV;
import com.ybsx.entity.StaticVo;
import com.ybsx.entity.StaticVo2;

public interface ArticleDao {

	void articleOperate(String sql);

	List<Static> queryStatics(String sql);

	
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
			    * @Description: TODO(这里用一句话描述这个方法的作用)
			    * @param @param name
			    * @param @param startDate
			    * @param @param endDate
			    * @param @param page
			    * @param @param limit
			    * @param @return    参数
			    * @return Object    返回类型
			    * @throws
			    */
			    

		void saveTags(String tag, String str);

		List<String> getTagIds(String tagsSplitByComma);

		void savePostTags(String tagId, int id_parse,String time);

		String getWord(String uuid);

		void deleteTmpWord(String uuid);

		List<String>  getArticheTagsById(Integer valueOf);

		Page<PostStaticInfo> getDetailByDate(String uid, String startDate, String endDate, int page, int limit,String type);

		void insertRecordToScore(int postid);

		List<Integer> getMostSimilarity(int userId, int id);

		void saveToPytonTagTemp(String string, String id);

		Page<PostStaticInfo> getVedioPage(int type, int page, int limit, String groupName, String groupName2,
				String author, String endDate, String startDate);

}
