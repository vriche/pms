<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>公摊分布管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		
		//$("#type").options.remove(0);
		
			$("#btnSave").click(function(){
				var houseIds = $("#houseId").val().split(",");
				$("#houseIds").val(houseIds);
				$("#parentId").val($("#deviceId").val());
				$("#type").val(3);
				$("#inputForm").submit();
			})
			
			$("#proCompanyId").change(function(){
				$("#houseId").val("");
				$("#houseName").val("");
			})		
			
			$("#deviceId").change(function(){
				$("#searchForm").submit();
				return false;
			})					
	
		   	 $("#deviceId").remoteChained({
		        parents : "#proCompanyId",
		        url : "${ctx}/pms/pool/devicePooljson?model=device&fees.company.id="+ $("#proCompanyId").val(),
		        loading : " ",
		        clear : true
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
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/pms/pool/">设备信息列表</a></li>
		<li><a href="${ctx}/pms/device/form">设备信息添加</a></li>
	</ul>
	
	<form:form id="inputForm" modelAttribute="device" action="${ctx}/pms/pool/save" method="post" class="form-horizontal">
			<form:hidden path="houseIds" htmlEscape="false" maxlength="50" class="required"/>
			<form:hidden id="parentId" name="parentId" path="parent.id" htmlEscape="false" maxlength="50" class="required"/>
			<form:hidden id="type" name="type" path="type"/>
	</form:form>	
		
	<form:form id="searchForm" modelAttribute="device" action="${ctx}/pms/pool" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		
		<label class="control-label">物业公司:</label>
		<form:select id="proCompanyId" name="proCompanyId" path="fees.company.id"  class="text medium;required">
					<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
	
	
		
		<label>公摊设备:</label>
		<form:select id="deviceId" name="deviceId" path="parent.id"  class="text medium;required">
					<form:option value="" label=""/>
					<form:options items="${parentList}" itemLabel="codeName" itemValue="id" htmlEscape="false" />
		</form:select>
		



		&nbsp;<!-- input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/ -->
		
				<label class="control-label">房产：</label>  
	    <tags:treeselect id="house" 
	    				  name="houseId"  
	    				  title="房产" 
	    				  value="${device.house.id}" 
	    				  labelName="${house.name}" 
	    				  labelValue="${device.house.name}" 
	                      url="/pms/buildings/treeData?Level=4" 
	                      notAllowSelectParent="true"  
	                      allowClear="true" 
	                      checked="true"  
	                      nodesLevel="2" 
	                      nameLevel="1" 
	                      cssClass="required" 
	                      proCompany="proCompanyId" 
	                      />	
		<input id="btnSave" name="btnSave" class="btn btn-primary" type="button" value="保 存"/>&nbsp;
		
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
		<thead><tr>
		<th>业主</th>
		<th>单位</th>
		<th>房屋</th>
		<th>编码</th>
		<th>收费项目</th>
		<th>上次读数</th>
		<th>本次读数</th>
		<th>上次日期</th>
		<th>本次日期</th>

		
		<shiro:hasPermission name="pms:device:edit"></shiro:hasPermission>
		<th>操作</th>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="device">
			<tr>
			   
			  
			   
			    <td>${device.house.owner.name}</td>
			    <td>${device.house.owner.company.name}</td>
			    <td>${device.house.fullName}</td>
				<td><a href="${ctx}/pms/device/form?id=${device.id}">${device.code}</a></td>
				
				<td>${device.fees.name}</td>
				<td>${device.firstNum}</td>
				<td>${device.lastNum}</td>
				<td><fmt:formatDate value="${device.firstDate}" pattern="yyyy-MM-dd"/></td>
				<td><fmt:formatDate value="${device.lastDate}" pattern="yyyy-MM-dd"/></td>				
			
			
				
				<shiro:hasPermission name="pms:device:edit"></shiro:hasPermission>
				<td>
    				<!-- a href="${ctx}/pms/pool/form?id=${device.id}">修改</a -->
					<a href="${ctx}/pms/pool/delete?id=${device.id}&parentId=${device.parent.id}" onclick="return confirmx('确认要删除该信息吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
