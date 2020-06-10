package com.ischoolbar.programmer.dao.teacher;
/**
 * רҵdao
 */
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ischoolbar.programmer.entity.teacher.Department;

@Repository
public interface DepartmentDao {
	public int add(Department department);
	public int delete(String ids);
	public int edit(Department department);
	public List<Department> findList(Map<String,Object> queryMap);
	public List<Department> findListByAcademyId(Long id);
	public List<Department> findAll();
	public int getTotal(Map<String,Object> queryMap);
	public Department find(int id);

}
