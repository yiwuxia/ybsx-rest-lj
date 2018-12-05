package com.ybsx.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.redare.devframework.common.spring.db.SpringJdbcHelper;
import com.ybsx.dao.RecommendDao;
import com.ybsx.entity.PostStaticInfo;
import com.ybsx.entity.UserPostNum;
/*
 * 某个团队都只能看某个团队
 */

import cn.hutool.core.date.DateUtil;


@Repository
public class RecommendDaoImpl implements RecommendDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	SpringJdbcHelper jdbcHelper;
	
	private static String LIKE_TABLE="kone.likes";//点赞表
	private static String FAVOR_TABLE="kone.favorites";//收藏表
	private static String COMMENT_TABLE="kone.comments";//评论表
	
	
	    
	@Override
	public List<Integer> getRecordUserIds() {
		String sql="select id from kone.users a  where exists (select id from kone.records b where a.id=b.user_id)";
		return  jdbcTemplate.queryForList(sql, Integer.class);
	}
	
	/*
	 * 获得分组的所有数据
	    * 
	    * 
	    * @return
	    * @see com.ybsx.dao.RecommendDao#getUserPostNumByType()
	 */
	
	@Override
	public List<UserPostNum> getUserPostNumByType(int group_id) {
		String sql="select user_id,post_id,count(1) num ,t2.group_id from kone.records t1 inner JOIN  kone.users  t2\n" +
				" on t1.user_id=t2.id  where t2.group_id="+group_id+"    group by t2.id,t1.post_id";
		List<UserPostNum> listRecord =jdbcHelper.queryForListBean(sql, UserPostNum.class);
		for (UserPostNum userPostNum : listRecord) {
			int score=userPostNum.getNum();
			 score +=getScoreCommon("kone.comments",userPostNum.getUser_id(),userPostNum.getPost_id());
			 score +=getScoreCommon("t_share",userPostNum.getUser_id(),userPostNum.getPost_id());
			 score +=getScoreCommon("kone.favorites",userPostNum.getUser_id(),userPostNum.getPost_id());
			 userPostNum.setNum(score);
		}
		return listRecord;
	}


	private int getScoreCommon(String  tableName,int user_id,int post_id) {
		String sql="select count(1) from "+tableName+" where user_id="+user_id+" and post_id ="+post_id;
		String num = jdbcHelper.queryForNumber(sql).toString();
		return Integer.valueOf(num);
	}


	    
	/*@Override
	public void saveUserPostPreference(List<UserPostNum> list) {
		List<Object[]> listParam =new ArrayList<>();
		Object[] arr=null;
		for (UserPostNum userPostNum : list) {
			 arr=new Object[4];
			arr[0]=userPostNum.getUser_id();
			arr[1]=userPostNum.getPost_id();
			arr[2]=userPostNum.getNum();
			arr[3]=userPostNum.getNum();
			listParam.add(arr);
		}
		String  sql="	insert into post_preferences(user_id,post_id,preference)values(?,?,?)"
				+ "  ON DUPLICATE KEY UPDATE  preference=?";
		jdbcHelper.batchUpdate(sql, listParam);
	}*/


	
	    
	@Override
	public void saveRecommendList(Integer userId, List<RecommendedItem> items) {
		String sql="insert IGNORE  into t_recommend_post(user_id,sml_post_id,sml_value)values(?,?,?)";
		List<Object[]> list =new ArrayList<>();
		for (RecommendedItem r : items) {
			Object[] arr=new Object[3];
			arr[0]=userId;
			arr[1]=r.getItemID();
			arr[2]=r.getValue();
			list.add(arr);
		}
		jdbcHelper.batchUpdate(sql, list);
	}

	
	    
	@Override
	public int getGroupIdByUserId(long userId) {
		String sql="	select group_id from kone.users where id ="+userId;
		Long groupId= jdbcHelper.queryForNumber(sql);
		return Integer.valueOf(groupId.toString());
	}

	/*
	 * 查询今天没有被计算的文章
	 */
	    
	@Override
	public List<Integer> getNowMaxid() {
		String timeFiveMinuteAgo=getTimeMinuteAgo();
		List<Integer> list= jdbcTemplate.queryForList("	select t1.id from  kone.posts t1 "
				+ " where t1.created_at >='"+timeFiveMinuteAgo+"' and t1.iscount=0  ", Integer.class);
		return list;
	}

	
	    
	private String getTimeMinuteAgo() {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, -2);///只计算2分钟之前的 主要是为了防止文章标签尚未提取好
		//calendar.add(Calendar.MINUTE, -30);///只计算2分钟之前的 主要是为了防止文章标签尚未提取好
		String format = DateUtil.format(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");
		return format;
	}

	@Override
	public Map<String, Object> getIdAndTagsInfo(Integer integer) {
		String sql="select  d.id,GROUP_CONCAT(c.name) name from kone.post_tag b,kone.tags c,kone.posts  d "
				+ "where d.id="+integer+" and b.tag_id=c.id\n" +
				"    and b.post_id=d.id and d.status=0 and  d.deleted_at is null\n" +
				"    group by b.post_id";
		Map<String, Object> map=jdbcHelper.queryForMap(sql);
		return map;
	}

	
	@Override
	public List<Map<String, Object>> getNearestMap() {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		String date=DateUtil.format(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");
		//取一个月前的数据
		String sql="select b.post_id id,GROUP_CONCAT(c.name) name from kone.post_tag b,kone.tags c,(\n" +
				"select id,status,deleted_at from  kone.posts   where  created_at>='"+date+"' and status=0 and  deleted_at is null order by created_at desc limit 0,300 \n" +
				")  d where     b.tag_id=c.id\n" +
				"    and b.post_id=d.id  \n" +
				"    group by b.post_id";
		return jdbcHelper.queryForListMap(sql);
	}

	    
	@Override
	public void saveSmlRecord(List<Object[]> list) {
		String	sql= "insert  into t_recommend_post_group(post_id,sml_post_id,sml_value,group_id)"+
                "values(?,?,?,?)  on  DUPLICATE KEY UPDATE  sml_value= ? ";
		jdbcHelper.batchUpdate(sql, list);
	}

	
	    
	@Override
	public void udaptePostIsCount(List<Object[]> id_update) {
		jdbcHelper.batchUpdate("update kone.posts set iscount=1 where id =?", id_update);
	}

	
	    
	@Override
	public List<Map<String, Object>> getIdAndTagsInfoList() {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		String date=DateUtil.format(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");
		String sql="select  d.id,GROUP_CONCAT(c.name) name from kone.post_tag b,kone.tags c,kone.posts  d "
				+ "where  d.created_at >='"+date+"'   and  b.tag_id=c.id\n" +
				"    and b.post_id=d.id and d.status=0 " +
				"    group by b.post_id";
		return jdbcHelper.queryForListMap(sql);
	}

	
	    
	@Override
	public List<PostStaticInfo> getTodayPost(String yesterday) {
		//where date_format(created_at,'%Y-%m-%d')= '"+yesterday+"'
		String sql="select id from kone.posts  ";
		return jdbcHelper.queryForListBean(sql, PostStaticInfo.class);
	}

	
	  
	    
	@Override
	public PostStaticInfo getDayPartOne(PostStaticInfo postStaticInfo,String today) {
		StringBuffer sql = new StringBuffer();
		sql.append(" 			select  ");
		sql.append(" 			t2.uid,sum(t2.pv) pv,sum(t2.uv) uv, ");
		sql.append(" 	 	SUM(t2.expire) / (SUM(t2.pv) * 1000) AS avgExpireRate, ");
		sql.append(" 		SUM(t2.forward)/ SUM(t2.pv) AS forwardRate, ");
		sql.append(" 		sum(t2.share)/SUM(t2.pv)  AS shareRate, ");
		sql.append("     SUM(t2.playpercent)/SUM(t2.play) as  avgplayRate, ");
		sql.append("     SUM(t2.over_eight)/SUM(t2.play)  as playEndRate, ");
		sql.append("     sum(t2.share) as shareCount ");
		sql.append(" 			from kone.posts t1,t_statics t2 ");
		sql.append(" 			where t1.id="+Integer.valueOf(postStaticInfo.getId())+"  and t2.datetime='"+today+"'  and t1.uid=t2.uid ");
		PostStaticInfo postStaticInfo2=null;
		postStaticInfo2=jdbcHelper.queryForBean(sql.toString(), PostStaticInfo.class);
		if (postStaticInfo2==null) {
			postStaticInfo2=new PostStaticInfo();
		}
		return postStaticInfo2;
	}

	
	    /* (非 Javadoc)
	    * 
	    	获取 1点赞2 收藏  3评论数
	    	
	    	id type today
	    */
	    
	@Override
	public String getTodayOpeNum(String id, int type,String today) {
		String tableName="";
		if (type==1) {
			tableName=LIKE_TABLE;
		}
		else if (type==2) {
			tableName=FAVOR_TABLE;
		}else {
			tableName=COMMENT_TABLE;
		}
		//System.out.println(id);
		String sql="	select count(id) from "+tableName+" where post_id =  "+Integer.valueOf(id)
				+ "		and date_format(updated_at,'%Y-%m-%d')= date_format(now(),'%Y-%m-%d') ";
		return jdbcHelper.queryForNumber(sql).toString();
	}

	
	    /* (非 Javadoc)
	    * 
	    * 
	    * @param postStaticInfo
	    * @see com.ybsx.dao.RecommendDao#saveDaylyData(com.ybsx.entity.PostStaticInfo)
	    */
	    
	@Override
	public void saveDaylyData(PostStaticInfo p) {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO t_post_static ( ");
		sql.append(" 	post_id, ");
		sql.append(" 	uv, ");
		sql.append(" 	pv, ");
		sql.append(" 	likes_num, ");
		sql.append(" 	favor_num, ");
		sql.append(" 	shareCount, ");
		sql.append(" 	commnet_num, ");
		sql.append(" 	datetime, ");
		sql.append(" 	avgExpireRate, ");
		sql.append(" 	forwardRate, ");
		sql.append(" 	shareRate, ");
		sql.append(" 	avgPlayRate, ");
		sql.append(" 	playEndRate ");
		sql.append(" ) ");
		sql.append(" VALUES ");
		sql.append(" 	( ");
		sql.append(" 		"+p.getId()+", ");
		sql.append(" 		"+p.getUv()+", ");
		sql.append(" 		"+p.getPv()+", ");
		sql.append(" 		"+p.getLikeNum()+", ");
		sql.append(" 		"+p.getFavorNum()+", ");
		sql.append(" 		"+p.getShareCount()+", ");
		sql.append(" 		"+p.getCommentNum()+", ");
		sql.append(" 		'"+p.getDateTime()+"', ");
		sql.append(" 		'"+p.getAvgExpireRate()+"', ");
		sql.append(" 		'"+p.getForwardRate()+"', ");
		sql.append(" 		'"+p.getShareRate()+"', ");
		sql.append(" 		'"+p.getAvgPlayRate()+"', ");
		sql.append(" 		'"+p.getPlayEndRate()+"' ");
		sql.append(" 	)");
		jdbcHelper.update(sql.toString());
	}	

	
		
}
