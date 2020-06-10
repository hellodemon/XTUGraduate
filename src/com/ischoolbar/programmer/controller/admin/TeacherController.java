package com.ischoolbar.programmer.controller.admin;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ischoolbar.programmer.entity.admin.News;
import com.ischoolbar.programmer.entity.teacher.Academy;
import com.ischoolbar.programmer.entity.teacher.Department;
import com.ischoolbar.programmer.entity.teacher.Teacher;
import com.ischoolbar.programmer.page.admin.Page;
import com.ischoolbar.programmer.service.teacher.AcademyService;
import com.ischoolbar.programmer.service.teacher.DepartmentService;
import com.ischoolbar.programmer.service.teacher.TeacherService;
/**
 * ��ʦ��Ϣ������
 * @author 20161
 *
 */
@RequestMapping("/admin/teacher")
@Controller
public class TeacherController {
	@Autowired
	private AcademyService academyService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private TeacherService teacherService;
	
	/**
	 * ��ʦ��Ϣ�б�ҳ��
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView teacherList(ModelAndView model){
		model.addObject("academyList", academyService.findAll());
		model.addObject("departmentList", departmentService.findAll());
		model.setViewName("teacher/list");
		return model;
	}
	
	/**
	 * ��ʦ����ҳ��
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public ModelAndView add(ModelAndView model){
		model.addObject("academyList", academyService.findAll());
		model.addObject("departmentList", departmentService.findAll());
		model.setViewName("teacher/add");
		return model;
	}
	/*@RequestMapping(value="/loadAcademys")
	public @ResponseBody List<Academy> loadAcademys(){
		List<Academy> academies = academyService.findAll();
		return academies;
	}
	
	@RequestMapping(value="/loadDepartments",method=RequestMethod.POST)
	@ResponseBody
	public List<Department> loadDepartments(Long academyId){
		List<Department> departments = departmentService.findListByAcademyId(academyId);
		return departments;
	}*/
	/**
	 * ��ʦ����
	 * @param teacher
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> add(Teacher teacher){
		Map<String,String> ret = new HashMap<String, String>();
		if(teacher == null){
			ret.put("type", "error");
			ret.put("msg", "����д��ȷ����Ϣ��");
			return ret;
		}
		if(StringUtils.isEmpty(teacher.getTeacherName())){
			ret.put("type", "error");
			ret.put("msg", "��ʦ��������Ϊ�գ�");
			return ret;
		}
		if(teacher.getAcademyId() == null){
			ret.put("type", "error");
			ret.put("msg", "��ѡ��ѧԺ���࣡");
			return ret;
		}
		if(teacher.getDepartmentId() == null){
			ret.put("type", "error");
			ret.put("msg", "��ѡ��רҵ���࣡");
			return ret;
		}
		if(StringUtils.isEmpty(teacher.getContent())){
			ret.put("type", "error");
			ret.put("msg", "��ʦ��Ϣ����Ϊ�գ�");
			return ret;
		}
		if(teacherService.add(teacher) <= 0){
			ret.put("type", "error");
			ret.put("msg", "����ʧ�ܣ�����ϵ����Ա��");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "���ӳɹ���");
		return ret;
	}
	
	/**
	 * ��ʦ�༭ҳ��
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public ModelAndView edit(ModelAndView model,Long id){
		model.addObject("academyList", academyService.findAll());
		model.addObject("departmentList", departmentService.findAll());
		model.addObject("teacher", teacherService.find(id));
		model.setViewName("teacher/edit");
		return model;
	}
	
	/**
	 * ��ʦ��Ϣ�༭
	 * @param teacher
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> edit(Teacher teacher){
		Map<String,String> ret = new HashMap<String, String>();
		if(teacher == null){
			ret.put("type", "error");
			ret.put("msg", "����д��ȷ����Ϣ��");
			return ret;
		}
		if(StringUtils.isEmpty(teacher.getTeacherName())){
			ret.put("type", "error");
			ret.put("msg", "��ʦ��������Ϊ�գ�");
			return ret;
		}
		if(teacher.getAcademyId() == null){
			ret.put("type", "error");
			ret.put("msg", "��ѡ��ѧԺ��");
			return ret;
		}
		if(teacher.getDepartmentId() == null){
			ret.put("type", "error");
			ret.put("msg", "רҵ����Ϊ�գ�");
			return ret;
		}
		if(StringUtils.isEmpty(teacher.getContent())){
			ret.put("type", "error");
			ret.put("msg", "��ʦ��Ϣ����Ϊ�գ�");
			return ret;
		}
		if(teacherService.edit(teacher) <= 0){
			ret.put("type", "error");
			ret.put("msg", "�޸�ʧ�ܣ�����ϵ����Ա��");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "�޸ĳɹ���");
		return ret;
	}
	
	/**
	 * ɾ����ʦ��Ϣ
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> delete(String ids){
		Map<String,String> ret = new HashMap<String, String>();
		if(ids == null){
			ret.put("type", "error");
			ret.put("msg", "��ѡ��Ҫɾ���ĵ�ʦ��Ϣ��");
			return ret;
		}
		if(ids.contains(",")){
			ids = ids.substring(0,ids.length()-1);
		}
		if(teacherService.delete(ids) <= 0){
			ret.put("type", "error");
			ret.put("msg", "��ʦɾ��ʧ�ܣ�����ϵ����Ա��");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "ɾ���ɹ���");
		return ret;
	}
	/**
	 * ��ҳģ��������ѯ�б�
	 * @param name
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getList(
			@RequestParam(name="teacherName",required=false,defaultValue="") String teacherName,
			@RequestParam(name="academyId",required=false) Long academyId,
			@RequestParam(name="departmentId",required=false) Long departmentId,
			Page page
			){
		Map<String,Object> ret = new HashMap<String, Object>();
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("teacherName", teacherName);
		if(academyId != null && academyId.longValue() != -1){
			queryMap.put("academyId", academyId);
		}
		if(departmentId != null && departmentId.longValue() != -1){
			queryMap.put("departmentId", departmentId);
		}
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", teacherService.findList(queryMap));
		ret.put("total", teacherService.getTotal(queryMap));
		return ret;
	}
	
	/**
	 * �ϴ�ͼƬ
	 * @param photo
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/upload_photo",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> uploadPhoto(MultipartFile photo,HttpServletRequest request){
		Map<String, String> ret = new HashMap<String, String>();
		if(photo == null){
			ret.put("type", "error");
			ret.put("msg", "ѡ��Ҫ�ϴ����ļ���");
			return ret;
		}
		if(photo.getSize() > 1024*1024*1024){
			ret.put("type", "error");
			ret.put("msg", "�ļ���С���ܳ���10M��");
			return ret;
		}
		//��ȡ�ļ���׺
		String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".")+1,photo.getOriginalFilename().length());
		if(!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())){
			ret.put("type", "error");
			ret.put("msg", "��ѡ��jpg,jpeg,gif,png��ʽ��ͼƬ��");
			return ret;
		}
		String savePath = request.getServletContext().getRealPath("/") + "/resources/upload/";
		File savePathFile = new File(savePath);
		if(!savePathFile.exists()){
			//�������ڸ�Ŀ¼���򴴽�Ŀ¼
			savePathFile.mkdir();
		}
		String filename = new Date().getTime()+"."+suffix;
		try {
			//���ļ�������ָ��Ŀ¼
			photo.transferTo(new File(savePath+filename));
		}catch (Exception e) {
			// TODO Auto-generated catch block
			ret.put("type", "error");
			ret.put("msg", "�����ļ��쳣��");
			e.printStackTrace();
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "�û��ϴ�ͼƬ�ɹ���");
		ret.put("filepath",request.getServletContext().getContextPath() + "/resources/upload/" + filename );
		return ret;
	}
}