<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>


	<script type="text/javascript">
		$(document).ready(function() {
		   var url = "${ctx}/pms/community/communityjson?model=unit";
		   $("#communityId").remoteChained("#proCompanyId", url);
		   var url2 = "${ctx}/pms/buildings/buildingsjson?model=unit";
		   $("#buildingsId").remoteChained("#communityId", url2);
		   
			$("#proCompanyId").change(function(){
			    $("#searchForm").submit();
			})
			
			$("#communityId").change(function(){
			    $("#searchForm").submit();
			})	   
			
			$("#buildingsId").change(function(){
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
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/pms/unit/">单元信息列表</a></li><shiro:hasPermission name="pms:unit:edit"></shiro:hasPermission>
		<li><a href="${ctx}/pms/unit/form">单元信息添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="unit" action="${ctx}/pms/unit/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		
		<label class="control-label">物业公司:</label>
		<form:select id="proCompanyId" name="proCompanyId" path="buildings.community.proCompany.id"  class="text medium;required">
					<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
	
		<label>小区:</label>
		<form:select id="communityId" name="communityId" path="buildings.community.id"  class="input-small">
					<form:option value="" label="选择小区"/>
					<form:options items="${communityList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
		<label>楼宇:</label>
		<form:select id="buildingsId" name="buildingsId" path="buildings.id"  class="input-small">
					<form:option value="" label="选择楼宇"/>
					<form:options items="${buildingsList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>				
		
		&nbsp;<label>单元名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		
		<th>楼宇名称</th>
		<th>单元编码</th>
		<th>单元名称</th>
		<th>开始楼层</th>
		<th>结束楼层</th>
		<th>开始房号</th>
		<th>结束房号</th>
		<shiro:hasPermission name="pms:unit:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="unit">
			<tr>
				<td>${unit.buildings.name}</td>
			    <td>${unit.code}</td>
			    <td><a href="${ctx}/pms/unit/form?id=${unit.id}">${unit.name}</a></td>
			    <td>${unit.startFloor}</td>
			    <td>${unit.endFloor}</td>
			    <td>${unit.startRoom}</td>
			    <td>${unit.startRoom}</td>
			    <td>${unit.endRoom}</td>
			    
					<td>
    				<a href="${ctx}/pms/house/list?unit.id=${unit.id}">查看楼宇</a>
				</td>		    
			    
				<shiro:hasPermission name="pms:unit:edit"></shiro:hasPermission>
				<!-- td>
    				<a href="${ctx}/pms/unit/form?id=${unit.id}">修改</a>
					<a href="${ctx}/pms/unit/delete?id=${unit.id}" onclick="return confirmx('确认要删除该单元信息吗？', this.href)">删除</a>
				</td -->
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
