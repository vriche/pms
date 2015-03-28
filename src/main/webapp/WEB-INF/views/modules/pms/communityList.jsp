<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>小区信息</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
		
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出小区数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/pms/community/export");
						$("#searchForm").submit();
						$("#searchForm").attr("action","${ctx}/pms/community/list");
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
			$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			});
			
			
			$("#proCompanyId").change(function(){
			    $("#searchForm").submit();
			})
			
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
		<form id="importForm" action="${ctx}/pms/community/import" method="post" enctype="multipart/form-data"
			style="padding-left:20px;text-align:center;" class="form-search" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/pms/community/import/template">下载模板</a>
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/pms/community/">小区信息列表</a></li><shiro:hasPermission name="pms:community:edit"></shiro:hasPermission>
		<li><a href="${ctx}/pms/community/form?proCompany.id=2">小区信息添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="community" action="${ctx}/pms/community/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

		 &nbsp;	<label class="control-label">物业公司:</label>
				<form:select id="proCompanyId" name="proCompanyId" path="proCompany.id"  class="text medium;required">
					<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
			
				
		
		&nbsp;<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		&nbsp;<input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
		&nbsp;<input id="btnImport" class="btn btn-primary" type="button" value="导入"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		
		<th>物业公司</th>
		<th>编号</th>
		<th>名称</th>
		<th>占地面积</th>
		<th>建筑面积</th>
		<th>楼宇数量</th>
		<th>地址</th>
		<th>操作</th>
		<shiro:hasPermission name="pms:community:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="community">
			<tr>
			    <td>${community.proCompany.name}</td>
				<td>${community.code}</td>
				<td><a href="${ctx}/pms/community/form?id=${community.id}">${community.name}</a></td>
			    <td>${community.totalArea}</td>
				<td>${community.buildArea}</td>
				<td>${community.buildCount}</td>
				<td>${community.address}</td>
				<shiro:hasPermission name="pms:community:edit"></shiro:hasPermission>
				
				<td>
    				<a href="${ctx}/pms/buildings/list?community.proCompany.id=${community.proCompany.id}&community.id=${community.id}">查看楼宇</a>
				</td>
				
				<!-- td>
    				<a href="${ctx}/pms/community/form?id=${community.id}"> 修改</a>
					<a href="${ctx}/pms/community/delete?id=${community.id}" onclick="return confirmx('确认要否删除？？', this.href)">删除</a>
					<a href="${ctx}/pms/community/delete?id=${community.id}" onclick="return confirmx('确认要否删除？？', this.href)">删除</a>
				</td -->
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
