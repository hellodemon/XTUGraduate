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
import com.ischoolbar.programmer.service.admin.MessageService;
import com.ischoolbar.programmer.service.admin.UserService;

@RequestMapping("/admin/message")
@Controller
public class MessageController {
	@Autowired
	private MessageService messageService;
	@Autowired
	private UserService userService;
	
	/**
	 * �����б�ҳ��
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model){
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("offset", 0);
		queryMap.put("pageSize", 999);
		model.addObject("messageList", messageService.findList(queryMap));
		model.setViewName("message/list");
		return model;
	}
	
	/**
	 * ɾ������
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> delete(String ids){
		Map<String,String> ret = new HashMap<String, String>();
		if(ids == null){
			ret.put("type", "error");
			ret.put("msg", "��ѡ��Ҫɾ����������Ϣ��");
			return ret;
		}
		if(ids.contains(",")){
			ids = ids.substring(0,ids.length()-1);
		}
		if(messageService.delete(ids) <= 0){
			ret.put("type", "error");
			ret.put("msg", "����ɾ��ʧ�ܣ�����ϵ����Ա��");
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
			@RequestParam(name="content",required=false,defaultValue="") String content,
			Page page
			){
		Map<String,Object> ret = new HashMap<String, Object>();
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("content", content);
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", messageService.findList(queryMap));
		ret.put("total", messageService.getTotal(queryMap));
		return ret;
	}
	
	/**
	 * ��������ҳ��
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/detail",method=RequestMethod.GET)
	public ModelAndView detail(ModelAndView model,Long id){
		model.addObject("message", messageService.find(id));
		model.setViewName("message/detail");
		return model;
	}

}
