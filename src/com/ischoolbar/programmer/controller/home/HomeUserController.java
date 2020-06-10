package com.ischoolbar.programmer.controller.home;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ischoolbar.programmer.entity.admin.Comment;
import com.ischoolbar.programmer.entity.admin.Message;
import com.ischoolbar.programmer.entity.admin.News;
import com.ischoolbar.programmer.entity.admin.User;
import com.ischoolbar.programmer.entity.forum.Topic;
import com.ischoolbar.programmer.service.admin.CommentService;
import com.ischoolbar.programmer.service.admin.MessageService;
import com.ischoolbar.programmer.service.admin.NewsCategoryService;
import com.ischoolbar.programmer.service.admin.UserService;
import com.ischoolbar.programmer.service.forum.ReplyService;
import com.ischoolbar.programmer.service.forum.TopicService;
import com.ischoolbar.programmer.util.ProduceMD5;
/**
 * ǰ̨�û�������Ϣ������
 * @author 20161
 *
 */
@RequestMapping("/user")
@Controller
public class HomeUserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private NewsCategoryService newsCategoryService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private MessageService messageService;
	
	/**
	 * �û�����ҳ��
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/info",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView model,HttpServletRequest request){
		User user = (User)request.getSession().getAttribute("user");
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("userId", user.getId());
		
		model.addObject("topicList", topicService.findList(queryMap));
		model.setViewName("home/user/info");
		return model;
	}
	/**
	 * �޸�����ҳ��
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/update",method = RequestMethod.GET)
	public ModelAndView update(ModelAndView model){
		model.setViewName("home/user/update");
		return model;
	}
	
	/**
	 * �޸������ύ
	 * @param password
	 * @param newPassword
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/update_pwd",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> updatePassword(String password,String newPassword,
			HttpServletRequest request){
		Map<String, String> ret = new HashMap<String, String>();
		User user = (User)request.getSession().getAttribute("user");
		ret.put("type", "error");
		if(StringUtils.isEmpty(password)){
			ret.put("msg", "�����벻��Ϊ�գ�");
			return ret;
		}
		if(StringUtils.isEmpty(newPassword)){
			ret.put("msg", "�����벻��Ϊ�գ�");
			return ret;
		}
		String user_password=user.getPassword();
		String old_password=ProduceMD5.getMD5(password);
		if(!user_password.equals(old_password)){
			ret.put("msg", "���������");
			return ret;
		}
		user.setPassword(ProduceMD5.getMD5(newPassword));//���������
		if(userService.editPassword(user)<= 0){
			ret.put("msg", "�޸�ʧ�ܣ�����ϵ����Ա��");
			return ret;
		}
		
		ret.put("type", "success");
		HttpSession session = request.getSession();
		session.setAttribute("user", null);
		return ret;
	}
	
	/**
	 * ��������
	 * @param User
	 * @return
	 */
	@RequestMapping(value = "/update_info",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> updateInfo(User User,
			HttpServletRequest request){
		Map<String, String> ret = new HashMap<String, String>();
		User onlineUser = (User)request.getSession().getAttribute("user");
		onlineUser.setAge(User.getAge());
		onlineUser.setNickname(User.getNickname());
		onlineUser.setSex(User.getSex());
		if(userService.edit(onlineUser) <= 0){
			ret.put("msg", "�޸�ʧ�ܣ�����ϵ����Ա��");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "���³ɹ�");
		return ret;
	}
	
	/**
	 * �����б�ҳ��
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/topic",method = RequestMethod.GET)
	public ModelAndView topic(ModelAndView model,Integer page,HttpServletRequest request){
		User user = (User)request.getSession().getAttribute("user");
		Map<String, Object> queryMap = new HashMap<String, Object>();
		model.addObject("newsCategoryList", newsCategoryService.findAll());//��ҳ������
		if(page == null || page.intValue() <= 0){
			page = 1;
		}
		int pageSize = 5;
		queryMap.put("offset", (page -1) * pageSize);
		queryMap.put("pageSize", pageSize);
		queryMap.put("userId", user.getId());
		
		int totalTopic = topicService.getTotal(queryMap);
		int totalPage = (totalTopic % pageSize ==0)?(totalTopic / pageSize):(totalTopic / pageSize + 1);
		model.addObject("topicList", topicService.findList(queryMap));
		model.addObject("page", page);
		model.addObject("totalPage", totalPage);
		model.setViewName("home/user/topic");
		return model;
	}
	/**
	 * ɾ������
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteTopic",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> deleteTopic(Long id){
		Map<String, String> ret = new HashMap<String, String>();
		ret.put("type", "error");
		if(id == null){
			ret.put("msg", "��ѡ��Ҫɾ��������");
			return ret;
		}
		Topic topic = topicService.findById(id);
		if(topic == null){
			ret.put("msg", "���Ӳ�����!");
			return ret;
		}
		if(topicService.delete(String.valueOf(id)) <= 0){
			ret.put("msg", "ɾ������������ϵ����Ա!");
			return ret;
		}
		//ɾ�������µ�����
		replyService.deleteByTopicId(id);
		ret.put("type", "success");
		return ret;
	}
	
	/**
	 * �����б�ҳ��
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/comment",method = RequestMethod.GET)
	public ModelAndView comment(ModelAndView model,Integer page,HttpServletRequest request){
		User user = (User)request.getSession().getAttribute("user");
		Map<String, Object> queryMap = new HashMap<String, Object>();
		model.addObject("newsCategoryList", newsCategoryService.findAll());//��ҳ������
		if(page == null || page.intValue() <= 0){
			page = 1;
		}
		int pageSize = 5;
		queryMap.put("offset", (page -1) * pageSize);
		queryMap.put("pageSize", pageSize);
		queryMap.put("userId", user.getId());
		
		int totalComment = commentService.getTotal(queryMap);
		int totalPage = (totalComment % pageSize ==0)?(totalComment / pageSize):(totalComment / pageSize + 1);
		model.addObject("commentList", commentService.findList(queryMap));
		model.addObject("page", page);
		model.addObject("totalPage", totalPage);
		model.setViewName("home/user/comment");
		return model;
	}
	/**
	 * ɾ����������
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteComment",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> deleteComment(Long id){
		Map<String, String> ret = new HashMap<String, String>();
		ret.put("type", "error");
		if(id == null){
			ret.put("msg", "��ѡ��Ҫɾ��������");
			return ret;
		}
		//ɾ����Ѷ������
		commentService.deleteOne(id);
		ret.put("type", "success");
		return ret;
	}
	/**
	 * ��������
	 * @param message
	 * @return
	 */
	@RequestMapping(value="/message",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> message(Message message){
		Map<String, Object> ret = new HashMap<String, Object>();
		if(message == null){
			ret.put("type", "error");
			ret.put("msg", "����д������������Ϣ��");
			return ret;
		}
		if(StringUtils.isEmpty(message.getContent())){
			ret.put("type", "error");
			ret.put("msg", "����д�������ݣ�");
			return ret;
		}
		message.setEmail(userService.findByUserId(message.getUserId()).getEmail());
		message.setCreateTime(new Date());
		if(messageService.add(message) <= 0){
			ret.put("type", "error");
			ret.put("msg", "����ʧ�ܣ�����ϵ����Ա��");
			return ret;
		}
		ret.put("type", "success");
		ret.put("createTime", message.getCreateTime());
		return ret;
	}
	
	/**
	 * �鿴������Ϣ
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/other",method=RequestMethod.GET)
	public ModelAndView other(ModelAndView model,Long id){
		User user = userService.findByUserId(id);
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("userId", id);
		model.addObject("other", user);
		model.addObject("newsCategoryList", newsCategoryService.findAll());//��ҳ������
		model.addObject("topicList", topicService.findList(queryMap));
		model.setViewName("home/user/other");
		return model;
	}
	/**
	 * �����ļ�
	 * @param UserId
	 * @param TopicUserId
	 * @return
	 */
	
	@RequestMapping(value="/download",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(
			@RequestParam(name="UserId",required=false) Long UserId,
			@RequestParam(name="TopicUserId",required=false) Long TopicUserId
			){
		Map<String, Object> ret = new HashMap<String, Object>();
		int userCredit = 10;
		int topicUserCredit = 1;
		int credit = userService.findByUserId(UserId).getCredit();
		if (credit < 10) {
			ret.put("type", "error");
			ret.put("msg", "�Բ������Ļ��ֲ�����");
			return ret;
		}
		userService.delCredit(userCredit,UserId);
		userService.addCredit(topicUserCredit,TopicUserId);
		ret.put("type", "success");
		ret.put("msg", userCredit);
		return ret;
	}
	

}