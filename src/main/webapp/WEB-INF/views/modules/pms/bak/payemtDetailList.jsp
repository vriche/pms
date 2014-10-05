<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	
		$(document).ready(function() {
		
			function selectDate(){WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});};
			$("#firstDate").click(function (e) {selectDate();});		
			$("#lastDate").click(function (e) {selectDate();});	

		   var url1 = "${ctx}/pms/fees/feesjson2?model=paymentDetail";
		   $("#feesId").remoteChained({parents : "#proCompanyId",url : url1,loading : " ",clear : true});	   

		   var url = "${ctx}/pms/community/communityjson?model=paymentDetail";
		   $("#communityId").remoteChained("#proCompanyId", url);	   
		   
		   var url2 = "${ctx}/pms/buildings/buildingsjson?model=paymentDetail";
		   $("#buildingsId").remoteChained("#communityId", url2);
		   
		   var url3 = "${ctx}/pms/unit/unitjson?model=paymentDetail";
		   $("#unitId").remoteChained("#buildingsId", url3);		
		   	
		   var url4 = "${ctx}/pms/house/housejson?model=paymentDetail";
		   $("#houseId").remoteChained("#unitId", url4);		   	
			
			
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
	<ul class="nav nav-tabs"><shiro:hasPermission name="pms:payemtDetail:edit"></shiro:hasPermission>
		<li class="active"><a href="${ctx}/pms/payemtDetail/">读表信息列表</a></li>
		<li><a href="${ctx}/pms/deviceDetail">读表信息添加</a></li>
	</ul>
	
	<form:form id="searchForm" modelAttribute="payemtDetail" action="${ctx}/pms/payemtDetail/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		

		<div>
		
		<label class="control-label">物业公司:</label>
		<form:select id="proCompanyId" name="proCompanyId" path="device.fees.company.id"  class="text medium;required">
					<form:option value="" label=""/>
					<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
	
		
		<label class="control-label">收费项目:</label>
		<form:select id="feesId" name="feesId" path="device.fees.id" class="input-small text medium;required">
					<form:option value="" label=""/>
					<form:options items="${feesList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>			
		
				
	
		<label>小区:</label>
		<form:select id="communityId" name="communityId" path="device.house.unit.buildings.community.id"  class="input-small">
					<form:option value="" label=""/>
					<form:options items="${communityList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
		<label>楼宇:</label>
		<form:select id="buildingsId" name="buildingsId" path="device.house.unit.buildings.id"  class="input-small">
					<form:option value="" label=""/>
					<form:options items="${buildingsList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>		
		
		<label>单元:</label>
		<form:select id="unitId" name="unitId" path="device.house.unit.id"  class="input-small">
					<form:option value="" label=""/>
					<form:options items="${unitList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>	
		
		<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;房屋:</label>
		<form:select id="houseId" name="houseId" path="device.house.id"  class="input-small">
					<form:option value="" label=""/>
					<form:options items="${houseList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>	
		
		</div>
		
		<div>
			<label class="control-label">读表开始:</label>
			<input id="firstDate" name="firstDate" type="text" readonly="readonly" maxlength="20" 
					value="<fmt:formatDate value="${payemtDetail.firstDate}" pattern="yyyy-MM-dd"/>" class=" Wdate"/>			
			

			<label class="control-label">读表结束:</label>	
			<input id="lastDate" name="lastDate" type="text" readonly="readonly" maxlength="20" 
					value="<fmt:formatDate value="${payemtDetail.lastDate}" pattern="yyyy-MM-dd"/>" class=" Wdate"/>		
				
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="&nbsp;&nbsp;&nbsp;查询&nbsp;&nbsp;&nbsp;"/>
		&nbsp;<input id="btnExport" class="btn btn-primary" type="submit" value="导出模板"/>	
		&nbsp;<input id="btnExport" class="btn btn-primary" type="submit" value="导入数据"/>				
					
		</div>
		
		
		
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>房产</th>
		<th>面积</th>
		<th>业主</th>
		<th>费用</th>
		<th>上次读数</th>
		<th>本次读数</th>
		<th>本次用量	</th>
		<th>公摊量</th>
		<th>总量</th>
		<th>单价	</th>
		<th>总费用</th>
		<th>已交费用</th>
		<th>优惠金额</th>
		<th>读表日期</th>
		<th>交费限期</th>		
		<th>操作</th>
		<shiro:hasPermission name="pms:payemtDetail:edit"></shiro:hasPermission>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="payemtDetail">
			<tr>
			    <td>${payemtDetail.device.house.fullName}</td>
			    <td>${payemtDetail.device.house.buildArea}</td>
			    <td>${payemtDetail.device.house.owner.name}</td>
			    <td>${payemtDetail.device.fees.name}</td>
			    <td>${payemtDetail.device.firstNum}</td> 
				<td>${payemtDetail.lastNum}</td>
				<td>${payemtDetail.usageAmount}</td>
				<td>${payemtDetail.poolUsageAmount}</td>
				<td>${payemtDetail.sumUsageAmount}</td>
				<td>${payemtDetail.device.fees.unitPrice}</td>
				<td>${payemtDetail.costMoney}</td>
				<td>${payemtDetail.incomeMoney}</td>
				<td>${payemtDetail.favourMoney}</td>
				<td>${payemtDetail.lastDate}</td>
				<td>${payemtDetail.paymentDate}</td>
				
				<shiro:hasPermission name="pms:payemtDetail:edit"></shiro:hasPermission>
				<td>
    				<a href="${ctx}/pms/payemtDetail/form?id=${payemtDetail.id}">修改</a>
					<a href="${ctx}/pms/payemtDetail/delete?id=${payemtDetail.id}" onclick="return confirmx('确认要删除该单元信息吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
