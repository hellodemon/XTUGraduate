package com.ischoolbar.programmer.entity.forum;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ischoolbar.programmer.entity.admin.User;

/**
 * 帖子实体类
 */
public class Topic extends BaseDomain {

    private User user;

    private Tab tab;

    private Integer countReplies;

    private Integer id;//帖子id

    private Long userId;

    private Date createTime;//创建时间

    private Date updateTime;//最后更新时间

    private String title;//标题

    private Integer click;//点击量

    private Byte tabId;//发布板块

    private String content;//内容

    private String filename;//文件名
    
    private String filepath;//文件路径
    public Integer getCountReplies() {
        return countReplies;
    }

    public void setCountReplies(Integer countReplies) {
        this.countReplies = countReplies;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    public Tab getTab() {
        return tab;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getClick() {
        return click;
    }

    public void setClick(Integer click) {
        this.click = click;
    }

    public Byte getTabId() {
        return tabId;
    }

    public void setTabId(Byte tabId) {
        this.tabId = tabId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

    
//    public String getLocalCreateTime() {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");//设置日期格式
//        String date = df.format(this.createTime);
//        return date;
//    }
//    public String getLocalUpdateTime() {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");//设置日期格式
//        String date = df.format(updateTime);
//        return date;
//    }
}