package com.ybsx.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.redare.devframework.common.pojo.Page;
import com.redare.devframework.common.spring.db.SpringJdbcHelper;
import com.ybsx.dao.ArticleDao;
import com.ybsx.entity.Static;
import com.ybsx.entity.StaticPUV;
import com.ybsx.entity.StaticVo;
import com.ybsx.entity.StaticVo2;

@Repository
public class ArticleDaoImpl implements ArticleDao {

	@Autowired
	SpringJdbcHelper jdbcHelper;
	
	/**
	 * 日志实例
	 */
	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public void articleOperate(String sql) {
		jdbcHelper.update(sql);
	}

	@Override
	public List<Static> queryStatics(String sql) {
		// TODO Auto-generated method stub
		return jdbcHelper.queryForListBean(sql, Static.class);
	}

	
	
	
	@Override
	public StaticVo getStaticByPageUrl(String uid,String deviceType) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ");
		sql.append(" SUM(pv) browseCount, ");
		sql.append(" SUM(uv) visitorCount, ");
		sql.append(" SUM(SHARE) shareCount, ");
		//modify by 李劲 2018年8月14日16:16:24  增加点赞和收藏--start
		sql.append(" SUM(play) playCount, SUM(praise) praiseCount,SUM(collect) collectCount,");
		//modify by 李劲 2018年8月14日16:16:24 --start
		sql.append(" ROUND(SUM(expire)/(SUM(pv)*1000),2) avgExpireRate, ");
		sql.append(" CONCAT(ROUND(SUM(forward)*100/SUM(pv),2),'%')   forwardRate, ");
		sql.append(" CONCAT(ROUND(SUM(SHARE)*100/SUM(pv),2),'%')   shareRate, ");
		sql.append(" CONCAT(IFNULL(ROUND(SUM(playpercent)/SUM(play),2),0.00),'%')   avgPlayRate, ");
		sql.append(" '0.00%'   unPlayRate, ");
		sql.append(" CONCAT(IFNULL(ROUND(SUM(over_eight)*100/SUM(play),2),0.00),'%')   playEndRate ");
		sql.append(" FROM t_statics WHERE    uid ='"+uid+"'");//后期数据多可以加索引
		StaticVo vo=jdbcHelper.queryForBean(sql.toString(), StaticVo.class);
		return vo;
	}

	
	    /* (非 Javadoc)
	    * 获取过去7天的数据
	    * @param url
	    * @return
	    * @see com.ybsx.dao.ArticleDao#getSevenDayData(java.lang.String)
	    */
	    
	@Override
	public List<StaticPUV> getSevenDayData(String uid) {
		//获取过去七天的日期
		List<String> list =new ArrayList<String>(7);
		List<String> listMd =new ArrayList<String>(7);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2=new SimpleDateFormat("MM-dd");
		Calendar cal=Calendar.getInstance();
		Date date =new Date();
		cal.setTime(date);
		list.add(sdf.format(date));
		listMd.add(sdf2.format(date));
		int num=0;
		while (num>-6) {
			num--;			
			cal.add(Calendar.DATE, -1);
			list.add(sdf.format(cal.getTime()));
			listMd.add(sdf2.format(cal.getTime()));
		}
		List<StaticPUV> result =new ArrayList<StaticPUV>();
		for (int i = 0; i < list.size(); i++) {
			result.add(getStaticPUVByDate(list.get(i),uid,listMd.get(i)));
		}
		return result;
	}

	
	    /**
	     * @param dateMd 
	     * @param url 
	    * @Title: getStaticPUVByDate
	    * @Description: TODO(这里用一句话描述这个方法的作用)
	    * @param @param dateStr
	    * @param @return    参数
	    * @return StaticPUV    返回类型
	    * @throws
	    */
	    
	private StaticPUV getStaticPUVByDate(String dateStr, String uid, String dateMd) {
		//String sql="select pv,uv,share  from t_statics where uid='"+uid+"'  and  datetime='"+dateStr+"'";
		String sql="select pv,uv,share  from t_statics where uid='"+uid+"'  and  datetime='"+dateStr+"'";
		List<Static> static1s=jdbcHelper.queryForListBean(sql, Static.class);
		StaticPUV staticPUV=new StaticPUV(dateMd);
		if (static1s==null||static1s.size()==0) {
			return staticPUV;
		}
		Static static1 =static1s.get(0);
		staticPUV.setDateTime(dateMd);
		staticPUV.setPvCount(static1==null?"0":static1.getPv());
		staticPUV.setUvCount(static1==null?"0":static1.getUv());
		staticPUV.setShareCount(static1==null?"0":static1.getShare());//现在没有设置为0
		return staticPUV;
	}
	
	
	
		    
		/*@Override
		public Page<StaticVo2> getVedioPage(String name, 
				String startDate, 
				String endDate,
				String type, 
				String title, 
				int page, int limit) {
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT  ");
			sql.append("  a.dateTime, "
				//	+ "IFNULL(a.requestUrl,concat('http://v.1234tv.com/',p.uid))  requestUrl,"
				+ "concat('http://v.1234tv.com/',p.uid)  requestUrl,"
					+ " p.uid,");
			sql.append("  b.name, ");
			sql.append(" p.title,p.status, ");
			if ("2".equals(type)) { //视频的	1介绍 ,2视频
				sql.append(" p.desc,");
				sql.append(" p.video, ");
				sql.append("  a.avgplayRate, ");
				sql.append("  a.unPlayRate, ");
				sql.append("  a.playEndRate ,");
			}
			if ("1".equals(type)) {
				sql.append(" p.content, ");//文章	 内容
			}
			sql.append("  IFNULL(a.pv,0) pv, ");
			sql.append("  IFNULL(a.shareCount,0) shareCount, ");
			sql.append("  IFNULL(a.avgExpireRate,0) avgExpireRate, ");
			sql.append("  IFNULL(a.forwardRate,0) forwardRate, ");
			sql.append("  IFNULL(a.shareRate,0) shareRate, ");
			sql.append("  IFNULL(a.uv,0) uv ");
			sql.append(" FROM ");
			sql.append(" ( ");
			sql.append(" SELECT  ");
			sql.append(" dateTime,uid, ");
			sql.append(" SUM(pv) pv, ");
			sql.append(" SUM(uv) uv, ");
			sql.append(" SUM(SHARE) shareCount, ");
			sql.append(" IFNULL(ROUND(SUM(expire)/(SUM(pv)*1000),2),0)  avgExpireRate, ");
			sql.append(" CONCAT( IFNULL(ROUND(SUM(forward)*100/SUM(pv),2),0.00),'%')   forwardRate, ");
			sql.append(" CONCAT( IFNULL(ROUND(SUM(SHARE)*100/SUM(pv),2),0.00),'%')   shareRate ");
			if ("2".equals(type)) {
				sql.append(" ,CONCAT(IFNULL(ROUND(SUM(playpercent)/SUM(play),2),0.00),'%')   avgplayRate, ");
				sql.append(" '0.00%'   unPlayRate, ");
				sql.append(" CONCAT(IFNULL(ROUND(SUM(over_eight)*100/SUM(play),2),0.00),'%')   playEndRate ");
			}
		//	sql.append(" FROM t_statics GROUP BY DATETIME,uid,request_url ");
			sql.append(" FROM t_statics GROUP BY DATETIME,uid ");
			sql.append(" ) a ");
			sql.append("   RIGHT JOIN  kone.posts p   on  p.uid=a.uid   ");
			if (!StringUtils.isEmpty(startDate)) {
				sql.append(" AND a.datetime>='"+startDate+"' ");
			}
			if (!StringUtils.isEmpty(endDate)) {
				sql.append("	AND a.datetime<='"+endDate+"' ");
			}
			sql.append("  INNER JOIN  kone.users b  on  p.user_id=b.id ");
			//sql.append(" WHERE p.user_id=b.id AND p.uid=a.uid ");
			if (!StringUtils.isEmpty(name)) {
				sql.append(" AND b.name LIKE '%"+name+"%' ");
			}
			//add by lijin 2018年10月11日14:55:28 start 
			if (!StringUtils.isEmpty(title)) {
				sql.append(" AND p.title LIKE '%"+title+"%' ");
			}
			//add by lijin 2018年10月11日14:55:28 end
			sql.append(" where p.type="+Integer.valueOf(type));
			sql.append(" order by dateTime desc ,uid desc ");
			logger.info(sql.toString());
			return  jdbcHelper.queryForPageBean(sql.toString(), StaticVo2.class, page, limit);
		}*/

	
		    
		@Override
		public void saveTags(String tag, String time) {
		int num=	jdbcHelper.update("INSERT IGNORE INTO kone.tags(NAME,created_at,updated_at)"
					+ "VALUES('"+tag+"','"+time+"','"+time+"')");
		System.out.println(num);
		}

		    
		@Override
		public List<String> getTagIds(String tags) {
			String sql="select id from kone.tags where name in ("+tags+")";
			List<Map<String, Object>> list_map= jdbcHelper.queryForListMap(sql);
			List<String> list= new ArrayList<String>();
			for (Map<String, Object> map : list_map) {
				list.add(map.get("id").toString());
			}
			return list;
		}

		
		    
		@Override
		public void savePostTags(String tagId, int postId,String time) {
			Object [] arr={postId,Integer.valueOf(tagId),time,time};
			jdbcHelper.update("INSERT  IGNORE INTO  kone.post_tag(post_id,tag_id,created_at,updated_at)VALUES(?,?,?,?)",arr);
		}

		
		    /* (非 Javadoc)
		    * 
		    * 
		    * @param uuid
		    * @return
		    * @see com.ybsx.dao.ArticleDao#getWord(java.lang.String)
		    */
		    
		@Override
		public String getWord(String uuid) {
			String sql="select word from t_temp_word where tmp_uuid='"+uuid+"'";
			System.out.println(sql);
			List<Map<String, Object>> list_map= jdbcHelper.queryForListMap(sql);
			String words="";
			for (Map<String, Object> map : list_map) {
				words=words+map.get("word").toString()+",";
			}
			return words.substring(0, words.length()-1);
		}

		
		    
		@Override
		public void deleteTmpWord(String uuid) {
			String sql="delete from t_temp_word where tmp_uuid='"+uuid+"'";
			jdbcHelper.update(sql);
		}

		
		    
		@Override
		public List<String>  getArticheTagsById(Integer id) {
			String sql="select title,content from  kone.posts where id="+id;
			Map<String, Object> map=jdbcHelper.queryForMap(sql);
			String str="";
			List<String> list=new ArrayList<String>();
			if (map.size()>0) {
				str=str+map.get("title").toString()+","+map.get("content").toString();
				list.add(map.get("title").toString());
				list.add(map.get("content").toString());
			}
			System.out.println(str);
			return list;
		}

		@Override
		public Page<StaticVo2> getVedioPage(String name, 
				String startDate, 
				String endDate,
				String type, 
				String title, 
				int page, int limit) {
			StringBuffer sql=new StringBuffer();
			 sql.append(" SELECT p.created_at AS DATETIME, CONCAT('http://v.1234tv.com/', p.uid) AS requestUrl, p.uid ");
			 sql.append(" , b.NAME, p.title,  p.STATUS ");
			 sql.append(" , IFNULL(a.pv, 0) AS pv ");
			 
			 if ("2".equals(type)) { //视频的	1文章 ,2视频
					sql.append(" ,p.desc,");
					sql.append(" p.video, ");
					sql.append("  a.avgplayRate, ");
					sql.append("  a.unPlayRate, ");
					sql.append("  a.playEndRate ");
			}
			 if ("1".equals(type)) {
					//sql.append(" ,p.content");//文章	 内容
			 }
			
			 sql.append(" , IFNULL(a.shareCount, 0) AS shareCount ");
			 sql.append(" , IFNULL(a.avgExpireRate, 0) AS avgExpireRate ");
			 sql.append(" , IFNULL(a.forwardRate, 0) AS forwardRate ");
			 sql.append(" , IFNULL(a.shareRate, 0) AS shareRate ");
			 sql.append(" , IFNULL(a.uv, 0) AS uv ");
			 sql.append(" FROM ( ");
			 sql.append(" SELECT uid, SUM(pv) AS pv, SUM(uv) AS uv ");
			 sql.append(" , SUM(SHARE) AS shareCount ");
			 sql.append(" , IFNULL(ROUND(SUM(expire) / (SUM(pv) * 1000), 2), 0) AS avgExpireRate ");
			 sql.append(" , CONCAT(IFNULL(ROUND(SUM(forward) * 100 / SUM(pv), 2), 0.00), '%') AS forwardRate ");
			 sql.append(" , CONCAT(IFNULL(ROUND(SUM(SHARE) * 100 / SUM(pv), 2), 0.00), '%') AS shareRate ");
			 
			 if ("2".equals(type)) {
					sql.append(" ,CONCAT(IFNULL(ROUND(SUM(playpercent)/SUM(play),2),0.00),'%')   avgplayRate, ");
					sql.append(" '0.00%'   unPlayRate, ");
					sql.append(" CONCAT(IFNULL(ROUND(SUM(over_eight)*100/SUM(play),2),0.00),'%')   playEndRate ");
			 }
			 
			 sql.append(" FROM t_statics ");
			 sql.append(" GROUP BY uid ");
			 sql.append(" ORDER BY NULL ");
			 sql.append(" ) a ");
			 sql.append(" RIGHT JOIN  kone.posts p ");
			 sql.append(" ON p.uid = a.uid ");
		 	
		 
			
			 sql.append(" AND p.type =  "+Integer.valueOf(type));
			 sql.append(" INNER JOIN kone.users b ON p.user_id = b.id ");
			 sql.append(" where 1=1 ");
		 	if (!StringUtils.isEmpty(name)) {
				sql.append(" AND   b.name LIKE '%"+name+"%' ");
			}
		 	if (!StringUtils.isEmpty(title)) {
				sql.append(" AND p.title LIKE '%"+title+"%' ");
			}
		 	if (!StringUtils.isEmpty(startDate)) {
		 		startDate=formatDateBySplit(startDate);
				sql.append(" AND p.created_at>='"+startDate+"' ");
			}
			if (!StringUtils.isEmpty(endDate)) {
				endDate=formatDateBySplit(endDate);
				sql.append("  AND p.created_at<='"+endDate+"' ");
			}
				
			 sql.append(" ORDER BY p.created_at DESC ");
			 
			 logger.info(sql.toString());
			// System.out.println(sql.toString());
			return  jdbcHelper.queryForPageBean(sql.toString(), StaticVo2.class, page, limit);
		}

		private String formatDateBySplit(String date) {
			date=date.substring(0, 4)+"-"+date.substring(4, 6)+"-"+date.substring(6);
			return date;
		}

		    
		
		@Override
		public Page<StaticVo2> getDetailByDate(String uid, String startDate, String endDate,
				int page, int limit,String type)
		{
			StringBuffer sql=new StringBuffer();
			 sql.append(" SELECT a.datetime AS DATETIME,"
			 		+ " CONCAT('http://v.1234tv.com/', p.uid) AS requestUrl, "
			 		+ "p.uid ");
			 sql.append(" ,b.NAME,"
			 		+ " p.title, "
			 		+ " p.STATUS");
			 
			 if ("2".equals(type)) { //视频的	1文章 ,2视频
					sql.append(" ,p.desc,");
					sql.append(" p.video, ");
					sql.append("  a.avgplayRate, ");
					sql.append("  a.unPlayRate, ");
					sql.append("  a.playEndRate ");
			 }
			 if ("1".equals(type)) {
					//sql.append(" ,p.content");//文章	 内容
			 }
			 sql.append("   ,pv AS pv, uv AS uv ");
			 sql.append(" , SHARE AS shareCount ");
			 sql.append(" , IFNULL(ROUND(expire / (pv * 1000), 2), 0) AS avgExpireRate ");
			 sql.append(" , CONCAT(IFNULL(ROUND(forward * 100 / pv, 2), 0.00), '%') AS forwardRate ");
			 sql.append(" , CONCAT(IFNULL(ROUND(SHARE * 100 / pv, 2), 0.00), '%') AS shareRate ");
			 if ("2".equals(type)) {
					sql.append(" ,CONCAT(IFNULL(ROUND(playpercent/play,2),0.00),'%')   avgplayRate, ");
					sql.append(" '0.00%'   unPlayRate, ");
					sql.append(" CONCAT(IFNULL(ROUND(over_eight*100/play,2),0.00),'%')   playEndRate ");
			 }
			 sql.append(" FROM t_statics a ");
			 sql.append(" INNER JOIN  kone.posts p ");
			 sql.append(" ON p.uid = a.uid   and p.uid='"+uid+"'");
		 	 sql.append(" AND p.type =  "+Integer.valueOf(type));
			 sql.append(" INNER JOIN kone.users b ON p.user_id = b.id ");
		 	if (!StringUtils.isEmpty(startDate)) {
		 		startDate=formatDateBySplit(startDate);
				sql.append(" AND p.created_at>='"+startDate+"' ");
			}
			if (!StringUtils.isEmpty(endDate)) {
				endDate=formatDateBySplit(endDate);
				sql.append("  AND p.created_at<='"+endDate+"' ");
			}
			 sql.append(" order  by a.datetime DESC ");
			 logger.info(sql.toString());
			 System.out.println(sql.toString());
			return  jdbcHelper.queryForPageBean(sql.toString(), StaticVo2.class, page, limit);
		}

		
		    /* (非 Javadoc)
		    * 
		    * 
		   插入一条记录到 t_uid_score
		    */
		    
		@Override
		public void insertRecordToScore(int postid) {
			
			/*jdbcHelper.update("insert into t_uid_score (id,uid,score,up_times) select  id,uid,0,0  from kone.posts  t where t.id="+postid);
			 logger.info("t_uid_score 插入成功");*/
			
		}

		
		    
		@Override
		public List<Integer> getMostSimilarity(int id) {
			String sql="select sml_post_id  from t_recommend_post where post_id ="+id+"  order by sml_value desc  LIMIT 0,3";
			 Set<Integer> set= jdbcHelper.queryListToSet(sql);
			return new ArrayList<Integer>(set);
		}

		
		

}
