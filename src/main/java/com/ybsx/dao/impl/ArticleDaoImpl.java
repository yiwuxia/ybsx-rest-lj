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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.redare.devframework.common.pojo.Page;
import com.redare.devframework.common.spring.db.SpringJdbcHelper;
import com.sun.jersey.core.util.StringIgnoreCaseKeyComparator;
import com.sun.tools.corba.se.idl.constExpr.And;
import com.ybsx.dao.ArticleDao;
import com.ybsx.entity.PostStaticInfo;
import com.ybsx.entity.Static;
import com.ybsx.entity.StaticPUV;
import com.ybsx.entity.StaticVo;
import com.ybsx.entity.StaticVo2;

@Repository
public class ArticleDaoImpl implements ArticleDao {

	@Autowired
	SpringJdbcHelper jdbcHelper;
	
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
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
	
	
	
		    

	
		    
		@Override
		public void saveTags(String tag, String time) {
		int num=	jdbcHelper.update("INSERT IGNORE INTO kone.tags(NAME,created_at,updated_at)"
					+ "VALUES('"+tag+"','"+time+"','"+time+"')");
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
			return list;
		}

		@Override
		public Page<PostStaticInfo> getVedioPage(int type, int page, int limit,
				String groupName, String title,
				String author, String endDate, String startDate) {
			
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT   ");
			sql.append(" 	a1.name groupName,a1.id,a1.uid,a1.title,IFNULL(t2.pv,0) pv,IFNULL(t2.uv,0)uv,"
					+ "IFNULL(t3.favor,0) favorNum, ");
			sql.append("   IFNULL(t4.likes,0)  likeNum,IFNULL(t5.comm,0) commentNum, ");
			sql.append("  a1.created_at  createdAt,a1.author, ");
			sql.append(" IFNULL(t2.avgExpireRate,0) avgExpireRate,  ");
			sql.append(" IFNULL(t2.forwardRate,0) forwardRate, ");
			sql.append(" IFNULL(t2.shareRate,0) shareRate, ");
			sql.append(" IFNULL(t2.avgplayRate,0) avgplayRate, ");
			sql.append(" IFNULL(t2.playEndRate,0) playEndRate ,IFNULL(shareCount,0) shareCount");//
			sql.append(" 	from ( ");
			sql.append(" 	select t7.name,t1.id,t1.uid,t1.title,t1.created_at,t6.name as author from kone.posts t1, ");
			sql.append(" 		kone.users t6, ");
			sql.append(" 		kone.groups t7 ");
			sql.append(" 	   where  t1.type=  "+type);
			if (!StringUtils.isEmpty(startDate)) {
					sql.append(" and t1.created_at>="+startDate);
			}
			if (!StringUtils.isEmpty(endDate)) {
				sql.append(" and t1.created_at<="+endDate);
			}
			if (!StringUtils.isEmpty(author)) {
				sql.append(" and t6.name like '%"+author+"%'");
			}
			if (!StringUtils.isEmpty(groupName)) {
				sql.append(" and t7.name like '%"+groupName+"%'");
			}
			if (!StringUtils.isEmpty(title)) {
				sql.append(" and t1.title like '%"+title+"%'");
			}
			sql.append(" 		and t1.user_id =t6.id and t6.group_id =t7.id ");
			sql.append(" 	) a1 LEFT JOIN  ");
			sql.append(" 	(select uid,sum(pv) pv,sum(uv), uv, ");
			sql.append(" 	 SUM(expire) / (SUM(pv) * 1000) AS avgExpireRate, ");
			sql.append("    SUM(forward)/ SUM(pv) AS forwardRate, ");
			sql.append(" 		sum(share)/SUM(pv)  AS shareRate, ");
			sql.append("     SUM(playpercent)/SUM(play) as  avgplayRate, ");
			sql.append("      SUM(over_eight)/SUM(play)  as playEndRate,sum(share) as shareCount ");
			sql.append("   from t_statics GROUP BY  uid)  t2 on a1.uid=t2.uid ");
			sql.append(" 	LEFT JOIN  ");
			sql.append(" 		(select post_id,COUNT(id) favor  from kone.favorites GROUP BY  post_id) t3 on  a1.id=t3.post_id ");
			sql.append(" LEFT JOIN  ");
			sql.append(" 		(select post_id,COUNT(id) likes  from kone.likes GROUP BY  post_id) t4 on a1.id =t4.post_id ");
			sql.append(" LEFT JOIN  ");
			sql.append(" 		(select post_id,COUNT(id)  comm from kone.comments GROUP BY  post_id) t5  on a1.id =t5.post_id ");
			sql.append(" order by a1.id	");
			System.out.println(sql.toString());
			return jdbcHelper.queryForPageBean(sql.toString(), PostStaticInfo.class, page, limit);
			
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
		
		
		/*
		 * 先去协同推荐数据表里面去取
		 * 不足3个的 去热门表里去取
		 */
		    
		@Override
		public List<Integer> getMostSimilarity(int userId,int postId) {
			
			if (userId==0) {
			String sql="select sml_post_id from t_recommend_post_group  where post_id="+postId+" order by sml_value desc LIMIT 0,3";
			 Set<Integer> set= jdbcHelper.queryListToSet(sql);
			 return new ArrayList<Integer>(set);
			}
			
			/*String sql="select sml_post_id  from t_recommend_post_group "
					+ "where post_id ="+id+"  and group_id="+gid+"  order by sml_value desc  LIMIT 0,3";
			 Set<Integer> set= jdbcHelper.queryListToSet(sql);*/
			String sql="SELECT SML_POST_ID  FROM t_recommend_post  WHERE USER_ID="+userId +"  and status=0  order by up_at desc limit 0,3  ";
			List<Integer> listCF =jdbcTemplate.queryForList(sql, Integer.class);
			int cfSize=listCF.size();
			List<Integer> listNew=new ArrayList<>();
			if (listCF==null||listCF.size()==0) {//没有产生推荐结果  去人额  
				//如果协同推荐没有结果 就推荐热门
				sql="select  t2.id from t_uid_score t1,kone.posts t2 where t1.uid =t2.uid  order by score desc limit 0,"+(3-cfSize);
				listNew=jdbcTemplate.queryForList(sql, Integer.class);
			}
			listCF.addAll(listNew); //不够三个的热门来凑
			return listCF;
		}

		
		    
		@Override
		public void saveToPytonTagTemp(String string, String id) {
			String sql="insert IGNORE into python_tag_temp(id,content,status)values("+id+",'"+string+"',0)";
			jdbcTemplate.update(sql);
		}


}
