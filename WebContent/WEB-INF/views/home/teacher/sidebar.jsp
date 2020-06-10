<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<aside class="sidebar">
<div class="fixed">
<div class="widget widget-tabs">
	 <ul class="nav nav-tabs" role="tablist">
	  <li role="presentation" class="active"><a href="#notice" aria-controls="message" role="tab" data-toggle="tab" >留言板</a></li>
	</ul> 
	 <div class="tab-content">
	  <div role="tabpanel" class="tab-pane contact active" id="message">
		<form id="message-form" name="message-form" action="" method="POST">
			<div class="message">
				<input type="hidden" name="userId" id="userId" value="${user.id }">
				<div class="message-box">
					<textarea placeholder="请在写下您对我们的意见或建议" name="content" id="message-textarea" cols="43" rows="5" tabindex="3"  style="resize:none;"></textarea>
					<div class="comment-ctrl">
						<div class="comment-prompt" style="display: none;"> <i class="fa fa-spin fa-circle-o-notch"></i> <span class="comment-prompt-text">留言正在提交中...请稍后</span> </div>
						<div class="comment-success" style="display: none;"> <i class="fa fa-check"></i> <span class="comment-prompt-text">留言提交成功...</span> </div>
						<button type="button" class="btn btn-default btn-search" id="message-submit" tabindex="4" style="float:right">留言</button>
					</div>
				</div>
			</div>
		</form>
	  </div>
	</div> 
  </div>
  <div class="widget widget_search">
	<form class="navbar-form" action="../teacher/search_list" method="get">
	  <div class="input-group">
		<input type="text" name="keyword" class="form-control" size="35" placeholder="请输入关键字" maxlength="15" autocomplete="off" value="${keyword }">
		<span class="input-group-btn">
		<button class="btn btn-default btn-search" name="search" type="submit">搜索</button>
		</span> </div>
	</form>
  </div>
</div>
<div class="widget widget_hot">
	  
	  <h3>最热评论文章</h3>
	  
	  <ul id="last-comment-list">            

	  </ul>
	   
 </div>
 <!-- <div class="widget widget_sentence">    
	<a href="#" target="_blank" rel="nofollow" title="湘大研讯" >
	<img style="width: 100%" src="../resources/home/images/ad1.jpg" alt="湘大研讯" ></a>    
 </div> -->
<div class="widget widget_sentence">
  <h3>友情链接</h3>
  <div class="widget-sentence-link">
	<a href="https://www.xtu.edu.cn/" title="湘潭大学官网" target="_blank" >湘潭大学官网</a>&nbsp;&nbsp;&nbsp;
	<a href="https://yz.chsi.com.cn/" title="中国研究生招生信息网" target="_blank" >中国研究生招生信息网</a>&nbsp;&nbsp;&nbsp;
  </div>
</div>
</aside>
<script>
function add0(m){return m<10?'0'+m:m }
function format(shijianchuo){
//shijianchuo是整数，否则要parseInt转换
	var time = new Date(shijianchuo);
	var y = time.getFullYear();
	var m = time.getMonth()+1;
	var d = time.getDate();
	var h = time.getHours();
	var mm = time.getMinutes();
	var s = time.getSeconds();
	return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
}
$(document).ready(function(){
	$("#message-submit").click(function(){
		if($("#userId").val() == ''){
			alert('登陆后可留言！');
			return;
		}
		if($("#message-textarea").val() == ''){
			alert('请填写内容！');
			return;
		}
		$.ajax({
			url:'../user/message',
			type:'post',
			data:$("#message-form").serialize(),
			dataType:'json',
			success:function(data){
				if(data.type == 'success'){
					$("#message-textarea").val('');
				}else{
					alert(data.msg);
				}
			}
		});
	});
});


$(document).ready(function(){
	$.ajax({
		url:'../news/last_comment_list',
		type:'post',
		dataType:'json',
		success:function(data){
			if(data.type == 'success'){
				var newsList = data.newsList;
				var html = '';
				for(var i=0;i<newsList.length;i++){
					var li = '<li><a style="padding:10px;" title="'+newsList[i].title+'" href="../news/detail?id='+newsList[i].id+'" >';
					li += '<span class="text" >'+newsList[i].title+'</span><span class="muted"><i class="glyphicon glyphicon-time"></i>';
					li += format(newsList[i].createTime) + '</span><span class="muted"><i class="glyphicon glyphicon-eye-open"></i>'+newsList[i].viewNumber+'</span></a></li>';
					html += li;
				}
				$("#last-comment-list").append(html);
			}
		}
	});
	$.ajax({
		url:'../index/get_site_info',
		type:'post',
		dataType:'json',
		success:function(data){
			if(data.type == 'success'){
				$("#total-article-span").text(data.totalArticle);
				$("#sitetime").text(data.siteDays);
			}
		}
	});
});
</script>