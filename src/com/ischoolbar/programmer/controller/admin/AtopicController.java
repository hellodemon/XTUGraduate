package com.ischoolbar.programmer.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ischoolbar.programmer.page.admin.Page;
import com.ischoolbar.programmer.service.forum.ReplyService;
import com.ischoolbar.programmer.service.forum.TabService;
import com.ischoolbar.programmer.service.forum.TopicService;
/**
 * 帖子控制类
 * @author 20161
 *
 */
@RequestMapping("/admin/topic")
@Controller
public class AtopicController {
	@Autowired
	private TabService tabService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private ReplyService replyService;
	
	/**
	 * 帖子列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model){
		model.addObject("tabList", tabService.findAll());
		model.setViewName("topic/list");
		return model;
	}
	/**
	 * 删除帖子
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> delete(String ids){
		Map<String,String> ret = new HashMap<String, String>();
		if(ids == null){
			ret.put("type", "error");
			ret.put("msg", "请选择要删除的信息！");
			return ret;
		}
		if(ids.contains(",")){
			ids = ids.substring(0,ids.length()-1);
		}
		if(topicService.delete(ids) <= 0){
			ret.put("type", "error");
			ret.put("msg", "删除失败，请联系管理员！");
			return ret;
		}
		replyService.deleteByTopicId(Long.valueOf(ids));
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;
	}
	
	/**
	 * 分页模糊搜索查询列表
	 * @param name
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(
			@RequestParam(name="title",required=false,defaultValue="") String title,
			@RequestParam(name="nickname",required=false,defaultValue="") String nickname,
			@RequestParam(name="tabId",required=false) Long tabId,
			Page page
			){
		Map<String,Object> ret = new HashMap<String, Object>();
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("title", title);
		queryMap.put("nickname", nickname);
		if(tabId != null && tabId.longValue() != -1){
			queryMap.put("tabId", tabId);
		}
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", topicService.findList(queryMap));
		ret.put("total", topicService.getTotal(queryMap));
		return ret;
	}
	

}
