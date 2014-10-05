<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收费项目管理</title>
	<meta name="decorator" content="default"/>
	
	<script type="text/javascript">
		$(document).ready(function() {
		
		   var url = "${ctx}/pms/community/communityjson?model=house";
		   $("#communityId").remoteChained("#proCompanyId", url);
		   var url2 = "${ctx}/pms/buildings/buildingsjson?model=house";
		   $("#buildingsId").remoteChained("#communityId", url2);
		   var url2 = "${ctx}/pms/unit/unitjson?model=house";
		   $("#unitId").remoteChained("#buildingsId", url2);

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
		<li class="active"><a href="${ctx}/pms/house/">办公场所列表</a></li>
		<shiro:hasPermission name="pms:house:edit"></shiro:hasPermission>
		<li><a href="${ctx}/pms/houseoffice/form">办公场所添加</a></li>
	</ul>
	
	<form:form id="searchForm" modelAttribute="house" action="${ctx}/pms/houseoffice/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

		<label class="control-label">物业公司:</label>
		<form:select id="proCompanyId" name="proCompanyId" path="unit.buildings.community.proCompany.id"  class="text medium;required">
					<form:option value="" label="选择物业"/>
					<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
	
		<label>小区:</label>
		<form:select id="communityId" name="communityId" path="unit.buildings.community.id"  class="input-small">
					<form:option value="" label="选择小区"/>
					<form:options items="${communityList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
		<label>楼宇:</label>
		<form:select id="buildingsId" name="buildingsId" path="unit.buildings.id"  class="input-small">
					<form:option value="" label="选择楼宇"/>
					<form:options items="${buildingsList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>		
		
		<label>单元:</label>
		<form:select id="unitId" name="unitId" path="unit.id"  class="input-small">
					<form:option value="" label="选择单元"/>
					<form:options items="${unitList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>			
		
		
		
		
		
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
			
			<th>房间编码</th>
			<th>房间名称</th>
			<th>业主</th>
			<th>房间功能</th> 
			<th>第几层</th> 
			<th>朝向</th>
			<th>建筑面积</th>
			<th>阁楼面积</th>
			<th>已售</th>
			<th>已租</th>
		   <shiro:hasPermission name="pms:house:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="house">
			<tr>
			<td>${house.code}</td>
			<td><a href="${ctx}/pms/house/form?id=${house.id}">${house.name}</a></td>
			<td>${house.owner.name}</td>
			<td>${fns:getDictLabel(house.funct, 'pms_house_funct', ' ')}</td>
			<td>${house.numFloor}</td>
			<td>${house.apartment}</td>
			<td>${house.face}</td>
			<td>${house.buildArea}</td>
			<td>${house.useArea}</td>
			<td>${house.isSell}</td>
			<td>${house.isRent}</td>

				<shiro:hasPermission name="pms:house:edit"></shiro:hasPermission>
				<td>
    				<a href="${ctx}/pms/house/form?id=${house.id}">修改</a>
					<a href="${ctx}/pms/houseoffice/delete?id=${house.id}" onclick="return confirmx('确认要删除该办公场所吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
