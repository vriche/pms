<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<style type="text/css">
		.sort{color:#0663A2;cursor:pointer;}
	</style>

	<script type="text/javascript">
	
		$(document).ready(function() {
			// 表格排序
			var orderBy = $("#orderBy").val().split(" ");
			$("#contentTable th.sort").each(function(){
				if ($(this).hasClass(orderBy[0])){
					orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="DESC"?"down":"up";
					$(this).html($(this).html()+" <i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
				}
			});
			$("#contentTable th.sort").click(function(){
				var order = $(this).attr("class").split(" ");
				var sort = $("#orderBy").val().split(" ");
				for(var i=0; i<order.length; i++){
					if (order[i] == "sort"){order = order[i+1]; break;}
				}
				if (order == sort[0]){
					sort = (sort[1]&&sort[1].toUpperCase()=="DESC"?"ASC":"DESC");
					$("#orderBy").val(order+" DESC"!=order+" "+sort?"":order+" "+sort);
				}else{
					$("#orderBy").val(order+" ASC");
				}
				page();
			});
			
			
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/pms/user/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			});

			
		});



	  
	  
	 function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/pms/user/");
			$("#searchForm").submit();
	    	return false;
	    }
	    
	    
	
	</script>
</head>
<body>
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/pms/user/import" method="post" enctype="multipart/form-data"
			style="padding-left:20px;text-align:center;" class="form-search" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/pms/user/import/template">下载模板</a>
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/pms/user/">业主列表</a></li>
		<shiro:hasPermission name="sys:user:edit"><li><a href="${ctx}/pms/user/form">业主添加</a></li></shiro:hasPermission>
		
	</ul>
	<form:form id="searchForm" modelAttribute="user" action="${ctx}/pms/user/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<div>
			<label>归属公司：</label>
			<tags:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}" 
				title="公司" url="/sys/office/treeData?type=1&sort=1" cssClass="input-small" allowClear="true"/>
			<label>登录名：</label><form:input path="loginName" htmlEscape="false" maxlength="50" class="input-small"/>
			
			
			<form:radiobuttons  id="userType" onclick="$('#searchForm').submit();" path="userType" items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />	
		    
		</div>
		
		<div style="margin-top:8px;">

			<label>房&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;屋：</label> 
			<tags:treeselect id="houseIds" name="houseIds" value="" labelName="${name}" labelValue="${id}" 
				title="楼宇" url="/pms/buildings/treeData?Level=4"  nodesLevel="2" nameLevel="1" cssClass="input-small" allowClear="true"/>
				
			<label>姓&nbsp;&nbsp;&nbsp;名：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-small"/>
			<label>电&nbsp;&nbsp;&nbsp;话：</label><form:input path="phone" htmlEscape="false" maxlength="50" class="input-small"/>
			
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			&nbsp;<input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
			&nbsp;<input id="btnImport" class="btn btn-primary" type="button" value="导入"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>归属公司</th>
		<th class="sort name">登录名</th>
		<th class="sort name">姓名</th>
		<th>证件号码</th>
		<th>固话</th>
		<th>手机1</th>
		<th>手机2</th>
		<th>邮箱</th>


		<shiro:hasPermission name="sys:user:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="user">
			<tr>
				<td>${user.company.name}</td>
				<td><a href="${ctx}/pms/user/form?id=${user.id}">${user.loginName}</a></td>
				<td>${user.name}</td>
				<td>${user.paperworkCode}</td>
				<td>${user.phone}</td>
				<td>${user.mobile}</td>
				<td>${user.mobile2}</td>
				<td>${user.email}</td>

				
				<shiro:hasPermission name="sys:user:edit"><td>
    				<a href="${ctx}/pms/user/form?id=${user.id}">修改</a>
					<a href="${ctx}/pms/user/delete?id=${user.id}" onclick="return confirmx('确认要删除该用户吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>