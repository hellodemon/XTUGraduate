package com.ischoolbar.programmer.dao.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ischoolbar.programmer.entity.admin.Comment;
import com.ischoolbar.programmer.entity.admin.Message;
/**
 * �û�����dao
 * @author 20161
 *
 */
@Repository
public interface MessageDao {
	public int add(Message message);
	public int delete(String ids);
	public List<Message> findList(Map<String,Object> queryMap);
	public List<Message> findAll();
	public int getTotal(Map<String,Object> queryMap);
	public Message find(Long id);
	public String getEmailById(Long id);
}
