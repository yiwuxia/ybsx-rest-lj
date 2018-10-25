package com.ybsx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redare.devframework.common.pojo.Page;
import com.ybsx.dao.ArticleDao;
import com.ybsx.entity.StaticPUV;
import com.ybsx.entity.StaticVo;
import com.ybsx.entity.StaticVo2;
import com.ybsx.service.ArticleService;
import com.ybsx.util.BDKeyExtract;

/*
 * 
    * @ClassName: ArticleServiceImpl
    * @Description: 对文章视频操作
    * @author lijin
    * @date 2018年6月11日
    *
 */
@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	ArticleDao  articleDao;

	    
	@Override
	public StaticVo getStaticByPageUrl(String uid,String type) {
		// TODO Auto-generated method stub
		return articleDao.getStaticByPageUrl(uid,type);
	}


	    
	@Override
	public List<StaticPUV> getSevenDayData(String uid) {
		// TODO Auto-generated method stub
		return articleDao.getSevenDayData(uid);
	}



	
	    /* (非 Javadoc)
	    * 
	    * 
	    * @param name
	    * @param startDate
	    * @param endDate
	    * @param page
	    * @param limit
	    * @see com.ybsx.service.ArticleService#getVedioPage(java.lang.String, java.lang.String, java.lang.String, int, int)
	    */
	    
	@Override
	public Page<StaticVo2> getVedioPage(String name, String startDate, String endDate,String type, String title,int page, int limit) {
		return articleDao.getVedioPage( name,  startDate,  endDate,  type,title,page,  limit);
	}



	
	    /* (非 Javadoc)
	    * 
	    * 
	    * @param tag
	    * @param str
	    * @see com.ybsx.service.ArticleService#saveTags(java.lang.String, java.lang.String)
	    */
	    
	@Override
	public void saveTags(String tag, String str) {
		 articleDao.saveTags(tag, str);
	}



	
	    /* (非 Javadoc)
	    * 
	    * 
	    * @param result
	    * @return
	    * @see com.ybsx.service.ArticleService#getTagIds(java.lang.String)
	    */
	    
	@Override
	public List<String> getTagIds(String tagsSplitByComma) {
		return articleDao.getTagIds(tagsSplitByComma);
	}



	    
	@Override
	public void savePostTags(String tagId, int id_parse,String time) {
		articleDao.savePostTags( tagId,  id_parse,time);
		
	}



	
	    /* (非 Javadoc)
	    * 
	    * 
	    * @param id
	    * @return
	    * @see com.ybsx.service.ArticleService#getArticheTagsById(java.lang.String)
	    */
	    
	@Override
	public String getArticheTagsById(String id) {
		List<String> list= articleDao.getArticheTagsById(Integer.valueOf(id));
	    String tags=BDKeyExtract.keyExtract(list.get(0),list.get(1));
		return tags;
	}



	    
	@Override
	public Page<StaticVo2> getDetailByDate(String uid, String startDate, String endDate, int page, int limit,String type) {
		Page<StaticVo2> pages=articleDao.getDetailByDate( uid,  startDate,  endDate,  page,  limit,type);
		return pages;
	}



	    
	@Override
	public void insertRecordToScore(String id) {
		articleDao.insertRecordToScore(Integer.valueOf(id));
		
	}



	
	    /* (非 Javadoc)
	    * 
	    * 
	    * @param id
	    * @return
	    * @see com.ybsx.service.ArticleService#getMostSimilarity(int)
	    */
	    
	@Override
	public List<Integer> getMostSimilarity(int id) {
		return articleDao.getMostSimilarity(id);
	}



	    
	
	

}
