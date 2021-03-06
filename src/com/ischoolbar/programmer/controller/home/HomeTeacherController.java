package com.ischoolbar.programmer.controller.home;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ischoolbar.programmer.entity.admin.Comment;
import com.ischoolbar.programmer.entity.admin.News;
import com.ischoolbar.programmer.entity.admin.NewsCategory;
import com.ischoolbar.programmer.entity.teacher.Teacher;
import com.ischoolbar.programmer.entity.teacher.TeacherComment;
import com.ischoolbar.programmer.page.admin.Page;
import com.ischoolbar.programmer.service.admin.NewsCategoryService;
import com.ischoolbar.programmer.service.teacher.AcademyService;
import com.ischoolbar.programmer.service.teacher.DepartmentService;
import com.ischoolbar.programmer.service.teacher.TeacherCommentService;
import com.ischoolbar.programmer.service.teacher.TeacherService;

/**
 * 前台导师控制器
 * @author 20161
 *
 */
@RequestMapping("/teacher")
@Controller
public class HomeTeacherController {
	
	@Autowired
	private NewsCategoryService newsCategoryService;
	@Autowired
	private AcademyService academyService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TeacherCommentService teacherCommentService;
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView model){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		model.addObject("newsCategoryList", newsCategoryService.findAll());
		model.addObject("academyList", academyService.findAll());
//		model.addObject("departmentList", departmentService.findAll());
		model.addObject("teacherList", teacherService.findList(queryMap));
		model.setViewName("home/teacher/index");
		return model;
	}
	
	/**
	 * 按照学院显示导师列表
	 * @param model
	 * @param aid
	 * @return
	 */
	@RequestMapping(value="/teacher_a",method=RequestMethod.GET)
	public ModelAndView teacherList_a(ModelAndView model,
			@RequestParam(name="aid",required=true) Long aid,
			Page page
			){
		model.addObject("newsCategoryList", newsCategoryService.findAll());
		model.addObject("academyList", academyService.findAll());
		model.addObject("departmentList", departmentService.findListByAcademyId(aid));
		model.addObject("teacherList", teacherService.findListByAcademyId(aid));
		model.setViewName("home/teacher/teacher_a");
		return model;
	}
	/**
	 * 按照专业显示导师列表
	 * @param model
	 * @param aid
	 * @param did
	 * @return
	 */
	@RequestMapping(value="/teacher_d",method=RequestMethod.GET)
	public ModelAndView teacherList_d(ModelAndView model,
			@RequestParam(name="aid",required=true) Long aid,
			@RequestParam(name="did",required=true) Long did,
			Page page
			){
		model.addObject("newsCategoryList", newsCategoryService.findAll());
		model.addObject("academyList", academyService.findAll());
		model.addObject("departmentList", departmentService.findListByAcademyId(aid));
		model.addObject("teacherList", teacherService.findListByDepartmentId(did));
		model.setViewName("home/teacher/teacher_d");
		return model;
	}
	/**
	 * 查看导师详情
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/detail",method=RequestMethod.GET)
	public ModelAndView detail(ModelAndView model,Long id){
		model.addObject("newsCategoryList", newsCategoryService.findAll());
		Teacher teacher = teacherService.findById(id);
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("teacherId", id);
		model.addObject("teacher", teacher);
		model.addObject("title", teacher.getTeacherName());
		model.addObject("commentList", teacherCommentService.findList(queryMap));
		model.setViewName("home/teacher/detail");
		return model;
	}
	
	/**
	 * 获取搜索列表
	 * @param model
	 * @param keyword
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/search_list",method=RequestMethod.GET)
	public ModelAndView searchList(ModelAndView model,
			@RequestParam(name="keyword",required=false,defaultValue="") String keyword,
			Page page
			){
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("offset", 0);
		queryMap.put("pageSize", 10);
		queryMap.put("teacherName", keyword);
		model.addObject("newsCategoryList", newsCategoryService.findAll());
		model.addObject("academyList", academyService.findAll());
		model.addObject("teacherList", teacherService.findList(queryMap));
		model.addObject("teacherName", keyword + "关键字下的导师信息");
		model.addObject("keyword", keyword);
		model.setViewName("home/teacher/search_list");
		return model;
	}
	/**
	 * 添加评论
	 * @param comment
	 * @return
	 */
	@RequestMapping(value="/comment_teacher",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> commentNews(TeacherComment comment){
		Map<String, Object> ret = new HashMap<String, Object>();
		if(comment == null){
			ret.put("type", "error");
			ret.put("msg", "请填写完整的评价信息！");
			return ret;
		}
		if(comment.getTeacherId() == null){
			ret.put("type", "error");
			ret.put("msg", "请选择一位导师进行评论！");
			return ret;
		}
		if(StringUtils.isEmpty(comment.getNickname())){
			ret.put("type", "error");
			ret.put("msg", "请填写昵称！");
			return ret;
		}
		comment.setCreateTime(new Date());
		if(teacherCommentService.add(comment) <= 0){
			ret.put("type", "error");
			ret.put("msg", "评论失败，请联系管理员！");
			return ret;
		}
		//文章评论数加1
		teacherService.updateCommentNumber(comment.getTeacherId());
		ret.put("type", "success");
		ret.put("createTime", comment.getCreateTime());
		return ret;
	}
	
	/**
	 * 分页获取某一导师的评论
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/get_comment_list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getCommentList(Page page,Long teacherId){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		queryMap.put("teacherId", teacherId);
		ret.put("type", "success");
		ret.put("commentList", teacherCommentService.findList(queryMap));
		return ret;
	}

}
