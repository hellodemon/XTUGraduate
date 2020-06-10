package com.ischoolbar.programmer.dao.admin;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ischoolbar.programmer.entity.admin.User;

/**
 * user用户dao
 * @author llq
 *
 */
@Repository
public interface UserDao {
	public User findByUserId(Long userId);
	public User findByUsername(String username);
	public User findByNickname(String nickname);
	public User findByEmail(String email);
	public int register(User user);
	public int add(User user);
	public int edit(User user);//修改管理员
	public int updateById(User user);//修改普通用户
	public int editPassword(User user);
	public int delete(String ids);
	public List<User> findList(Map<String, Object> queryMap);
	public int getTotal(Map<String, Object> queryMap);
	public int getUserCount();
    //多参数注解
    int addCredit(@Param("points") Integer points,@Param("id")Long id);
    int delCredit(@Param("points") Integer points,@Param("id")Long id);
    public int getCredit(Long id);//获取积分
}
