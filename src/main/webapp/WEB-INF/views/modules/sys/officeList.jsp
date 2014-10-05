<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 5});
			
			
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/sys/office/export");
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
			$("#searchForm").submit();
	    	return false;
	    }
	    
	    
	    
	    
	    
	</script>
</head>
<body>
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/sys/office/import" method="post" enctype="multipart/form-data"
			style="padding-left:20px;text-align:center;" class="form-search" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/sys/office/import/template">下载模板</a>
		</form>
	</div>
	

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/office/">机构列表</a></li>
		<shiro:hasPermission name="sys:office:edit"><li><a href="${ctx}/sys/office/form">机构添加</a></li></shiro:hasPermission>
	</ul>
	



   <form:form id="searchForm" modelAttribute="office" action="${ctx}/sys/office/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		
		
		<div class="control-group">
			<label class="control-label">机构分类:</label>
				<form:select path="sort">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('sys_office_sort')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
		    &nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			&nbsp;<input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
			&nbsp;<input id="btnImport" class="btn btn-primary" type="button" value="导入"/>
		</div>
	</form:form>	
	
	<tags:message content="${message}"/>
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<tr><th>机构名称</th><th>归属区域</th><th>机构编码</th><th>机构分类</th><th>机构类型</th><th>备注</th><shiro:hasPermission name="sys:office:edit"><th>操作</th></shiro:hasPermission></tr>
		<c:forEach items="${list}" var="office">
			<tr id="${office.id}" pId="${office.parent.id ne requestScope.office.id?office.parent.id:'0'}">
				<td><a href="${ctx}/sys/office/form?id=${office.id}">${office.name}</a></td>
				<td>${office.area.name}</td>
				<td>${office.code}</td>
				<td>${fns:getDictLabel(office.sort, 'sys_office_sort', '无')}</td>
				<td>${fns:getDictLabel(office.type, 'sys_office_type', '无')}</td>
				<td>${office.remarks}</td>
				<shiro:hasPermission name="sys:office:edit"><td>
					<a href="${ctx}/sys/office/form?id=${office.id}">修改</a>
					<a href="${ctx}/sys/office/delete?id=${office.id}" onclick="return confirmx('要删除该机构及所有子机构项吗？', this.href)">删除</a>
					<a href="${ctx}/sys/office/form?parent.id=${office.id}">添加下级机构</a> 
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
	</table>
	
	
	
</body>
</html>