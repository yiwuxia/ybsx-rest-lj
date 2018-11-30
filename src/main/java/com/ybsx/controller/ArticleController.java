package com.ybsx.controller;


import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redare.devframework.common.pojo.CommonResult;
import com.redare.devframework.common.pojo.Page;
import com.ybsx.dao.ArticleDao;
import com.ybsx.entity.PostStaticInfo;
import com.ybsx.entity.ResultMap;
import com.ybsx.entity.StaticPUV;
import com.ybsx.entity.StaticVo;
import com.ybsx.entity.StaticVo2;
import com.ybsx.service.ArticleService;
import com.ybsx.util.BDKeyExtract;
import com.ybsx.util.StringUtil;


/**
 * 投诉控制器
 * @createDate 2018年4月19日 上午11:28:09
 */
@RestController
@RequestMapping(value = "/static")
public class ArticleController {

	

	@Autowired
	private ArticleService articleService;
	
	
	@Autowired
	ArticleDao  articleDao;
	
	
	private Logger logger = Logger.getLogger(getClass());
	
	
	
	/**
	 * 给星星提供的接口
	 * @param uid 文章或视频的uid
	 * @return
	 */
	@RequestMapping(value = "/getStatic",method = RequestMethod.POST)
	public CommonResult getStatic(String uid){
		//url="http://x.1234tv.com/?z=4#a";
		if (StringUtils.isEmpty(uid)) {
			return CommonResult.returnFailsWrapper("无效参数");
		}
		
		Map<String ,Object> map =new HashMap<String ,Object>();
		//一个总的数据 , 一 个是统计图数据
		long start=System.currentTimeMillis();
		StaticVo vo =articleService.getStaticByPageUrl(uid,"");//这个url总的数据
		List<StaticPUV> listDay=articleService.getSevenDayData(uid);//这个url过去7天的数据
		map.put("count", vo);
		map.put("dayData", listDay);
		//long spent=System.currentTimeMillis()-start;
		//logger.info("spend time controller: "+spent);
		return CommonResult.returnSuccessWrapper(map);
		
	}
	
		/*
		 * 			String startDate,
					String endDate,
					String type,
					String title,//add by lijin @2018年10月11日14:10:43 for yys要求
		 */
		//给XX供的接口
		@RequestMapping(value = "/getVedioPage",method = RequestMethod.POST)
		public CommonResult getStatic(
				@RequestParam(value="startDate",defaultValue="",required=false) String  startDate,// 开始日期
				@RequestParam(value="endDate",defaultValue="",required=false) String endDate,//结束日期
				@RequestParam(value="author",defaultValue="",required=false) String author,//作者
				@RequestParam(value="title",defaultValue="",required=false) String title,//标题 
				@RequestParam(value="groupName",defaultValue="",required=false) String groupName,//团队 
				@RequestParam(value="type",defaultValue="1") int type,//文章1  视频2 
				@RequestParam(value="page", defaultValue="1") int page,
				@RequestParam(value="limit", defaultValue="10") int limit
				){
			
			Page<PostStaticInfo> pages=	articleService.getVedioPage(type ,page, limit,groupName,title,author,endDate,startDate);
			for (PostStaticInfo info : pages.getResult()) {
				 info.setAvgExpireRate(StringUtil.transSecondToMS(Double.valueOf(info.getAvgExpireRate())));
				 info.setForwardRate(StringUtil.tranToPercent(Double.valueOf(info.getForwardRate())));
				 info.setShareRate(StringUtil.tranToPercent(Double.valueOf(info.getShareRate())));
				 info.setAvgPlayRate(StringUtil.tranToPercent(Double.valueOf(info.getAvgPlayRate())));
				 info.setPlayEndRate(StringUtil.tranToPercent(Double.valueOf(info.getPlayEndRate())));
			}
			return CommonResult.returnSuccessWrapper(pages);
		}
	
	
		
		//	@RequestParam(value="uid") String uid,//uid 图文/视频 唯一标识
		
		@PostMapping("/getTagsByPostId")
		public CommonResult getTagsByPostId(String id ) throws IOException, InterruptedException {
			if (!BDKeyExtract.checkValidata(id)) {
				CommonResult.returnSuccessWrapper("参数错误");
			}
			/*
			 * 先根据id查找 title，content，  
			 */
			String result=articleService.getArticheTagsById(id);
	        if (org.apache.commons.lang3.StringUtils.isEmpty(result)) {
	        	logger.info("=====no tag is extracted==========");
	        	return CommonResult.returnSuccessWrapper("文章没有可提取的标签");
			}else{
				String[] arr_key=result.split(",");
				/*
				 * 标签是否存在，存在就找出id，插入post_tag 表
				 * 不存在则插入后，找出id,插入post_tag表
				 */
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				Timestamp now = new Timestamp(System.currentTimeMillis()); 
				String str = df.format(now);
				String param="";
				for (int i = 0; i < arr_key.length; i++) {
					String tag=arr_key[i];
					param=param+"'"+tag+"'"+",";
			       articleService.saveTags(tag,str);//存入标签库
				}
				param=param.substring(0, param.length()-1);
				List<String> tagIds=articleService.getTagIds(param);
				for (String tagId : tagIds) {//id_parse
					articleService.savePostTags(tagId,Integer.valueOf(id),str);
				}
				//articleService.insertRecordToScore(id);
				logger.info("id is:"+id+"tags为:---"+tagIds.toString());
				
			}
			return CommonResult.returnSuccessWrapper("success");
			
		}
		
		/*
		 * 当前时间内 此uid对应的 详细
		 * 参数  开始 结束日期 uid
		 */
		
		@PostMapping("/getDetailByDate")
		public ResultMap getDetailByDate(
				@RequestParam(value="uid") String uid,//uid 图文/视频 唯一标识
				@RequestParam(value="type") String type,//uid 图文/视频 唯一标识
				String startDate,
				String endDate,
				@RequestParam(value="page", defaultValue="1") int page,
				@RequestParam(value="limit", defaultValue="10") int limit
				)  {
			Page<StaticVo2> pages=  articleService.getDetailByDate(uid,startDate,endDate,page,limit,type);
		  	ResultMap<StaticVo2> resultMap=ResultMap.getSuccessResultMap();
		  	resultMap.setCount(pages.getTotalCount());
		  	resultMap.setData(pages.getResult());
			return resultMap;
		} 
		
		/*
		 * 根据文章id  返回最相关的三篇文章id
		 */
		@PostMapping("/getMostSimilarity")
		public CommonResult getMostSimilarity(
				@RequestParam(value="id",required=false,defaultValue="0") int id,//文章id
				@RequestParam(value="gid",required=false,defaultValue="0") int gid,//分组id
				@RequestParam(value="userId") int userId//当前用户id
				)  {
			List<Integer> listIds=articleDao.getMostSimilarity(userId,id);
			return CommonResult.returnSuccessWrapper(listIds);
		} 
}
