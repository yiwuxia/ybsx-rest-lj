package com.ybsx.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redare.devframework.common.pojo.Page;
import com.ybsx.dao.ArticleDao;
import com.ybsx.entity.PostStaticInfo;
import com.ybsx.entity.StaticPUV;
import com.ybsx.entity.StaticVo;
import com.ybsx.entity.StaticVo2;
import com.ybsx.service.ArticleService;
import com.ybsx.util.BDKeyExtract;
import com.ybsx.util.StringUtil;

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



	    
	@Override
	public Page<PostStaticInfo> getVedioPage(int type, int page, int limit, String groupName, String title,
			String author, String endDate, String startDate) {
		Page<PostStaticInfo> pages =articleDao.getVedioPage(type ,page, limit,groupName,title,author,endDate,startDate);
		for (PostStaticInfo info : pages.getResult()) {
			 info.setAvgExpireRate(StringUtil.transSecondToMS(Double.valueOf(info.getAvgExpireRate())));
			 info.setForwardRate(StringUtil.tranToPercent(Double.valueOf(info.getForwardRate())));
			 info.setShareRate(StringUtil.tranToPercent(Double.valueOf(info.getShareRate())));
			 info.setAvgPlayRate(StringUtil.tranToPercent(Double.valueOf(info.getAvgPlayRate())));
			 info.setPlayEndRate(StringUtil.tranToPercent(Double.valueOf(info.getPlayEndRate())));
		}
		return pages;
	}



	
	    
	@Override
	public void saveTags(String tag, String str) {
		 articleDao.saveTags(tag, str);
	}



	
	    
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
		String title=list.get(0); 
		String content=list.get(1);
		/*
		 * 请求参数的长度限制  title 80 字节;content 65535字节;
		 */
		 if (content.length()>20000) {
			 content=content.substring(0,20000);
		}
		 if (title.length()>=26) {
			 title=title.substring(0,26);
		}
		content=BDKeyExtract.removeHtmlTag(content);
	    String tags=BDKeyExtract.keyExtract(title,content);
	    System.out.println("baidu is no work");
	    if (tags.length()==0) {//百度没有提取到标签
	    	articleDao.saveToPytonTagTemp(title+","+content,id);///home/hadoop/script
	    	execPy();
	    	tags="";
		}
		return tags;
	}

	   public static void execPy() {
	        Process proc = null;
	        try {
	            proc = Runtime.getRuntime().exec("python3  /home/hadoop/script/tagExract.py ");
	            proc.waitFor();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
    }

	    
	@Override
	public Page<PostStaticInfo> getDetailByDate(String uid, String startDate, String endDate, int page, int limit,String type) {
		Page<PostStaticInfo> pages=articleDao.getDetailByDate( uid,  startDate,  endDate,  page,  limit,type);
		return pages;
	}



	    
	@Override
	public void insertRecordToScore(String id) {
		articleDao.insertRecordToScore(Integer.valueOf(id));
		
	}



	    



	



	    
	
	

}
