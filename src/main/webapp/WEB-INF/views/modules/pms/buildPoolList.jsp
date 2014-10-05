<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		
		 	function selectDate(){WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});};	
			$("#lastDate").click(function (e) {selectDate();});	
			$("#paymentDate").click(function (e) {selectDate();});	
			
			$("#btnSave").click(function(){
				var houseIds = $("#houseId").val().split(",");
				$("#houseIds").val(houseIds);
				$("#parentId").val($("#deviceId").val());
				$("#inputForm").submit();
			})
			
			
			$("#deviceId").remoteChained({
		        parents : "#proCompanyId",
		        url : "${ctx}/pms/pool/devicePooljson?model=device",
		        loading : " ",
		        clear : true
	   		 });	
	   		 

		   	$("#deviceId").change(function(){
		   		  var url = '${ctx}/pms/device/getDevicePoolPayment';
		   	      var id =  $("#deviceId").val();
		   	      if(id > 0){
		   	         $.getJSON(url,{id:id},function(data){
		   	         	  $("#usageAmount").val(data.usageAmount);
		   	              $("#unitPrice").val(data.unitPrice);
		   	        	  $("#payMoney").val(data.payMoney);
		   	        	  $("#searchForm").submit();
		   	         });
		   	      }else{
		   	      	 $("#usageAmount").val(0);$("#unitPrice").val(0);$("#payMoney").val(0);
		   	      	 $("#searchForm").submit();
		   	      }
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
	</form:form>	
		
	<form:form id="searchForm" modelAttribute="device" action="${ctx}/pms/buildpool" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		
		
		<div>
		<label class="control-label">物业公司:</label>
		<form:select id="proCompanyId" name="proCompanyId" path="fees.company.id"  class="text medium;required">
					<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
		<label>公摊设备:</label>
		<form:select id="deviceId" name="deviceId" path="parent.id"  class="text medium;required">
					<form:option value="" label=""/>
					<form:options items="${parentList}" itemLabel="codeName" itemValue="id" htmlEscape="false" />
		</form:select>
	  	   
		    &nbsp;用量 <form:input id="usageAmount" path="usageAmount"  class="input-small"/>
			&nbsp;单价 <form:input id="unitPrice" path="fees.unitPrice"  class="input-small"/>
			&nbsp;应收 <form:input id="payMoney" path="payMoney"  class="input-small"/>
			</div>
		<div>	
			
			 
			 
					     <label class="control-label">读表日期:</label>
					<input id="lastDate" name="lastDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${device.lastDate}" pattern="yyyy-MM-dd"/>" class=" Wdate"/>	
							
			     <label class="control-label">交费限期:</label>
					<input id="paymentDate" name="paymentDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${device.paymentDate}" pattern="yyyy-MM-dd"/>" class=" Wdate"/>

		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="生成"/>
		&nbsp;<input id="btnSave" class="btn btn-primary" type="button" value="保存"/>
	</div>
		
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
		<thead><tr>
	
			<th>房屋</th>
			<th>业主</th>
			<th>单位</th>
	
		<th>编码</th>
		<th>收费项目</th>

		<th>上次日期</th>
		<th>缴费期限</th>
		<th>使用量</th>
		<th>单价格</th>
		<th>应收金额</th>
		
		<shiro:hasPermission name="pms:device:edit"></shiro:hasPermission>
		<th>操作</th>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="device">
			<tr>
			    <td>${device.house.fullName}</td>
			    <td>${device.house.owner.name}</td>
			    <td>${device.house.owner.company.name}</td>
		
				<td><a href="${ctx}/pms/device/form?id=${device.id}">${device.code}</a></td>
				
				<td>${device.fees.name}</td>
				<td><fmt:formatDate value="${device.lastDate}" pattern="yyyy-MM-dd"/></td>
				<td><fmt:formatDate value="${device.paymentDate}" pattern="yyyy-MM-dd"/></td>
				<td><fmt:formatNumber  value="${device.usageAmount}" pattern="#,##0.00"/></td>
				<td><fmt:formatNumber  value="${device.fees.unitPrice}" pattern="#,##0.00"/></td>			
			    <td align="rigth"><fmt:formatNumber  value="${device.payMoney}" pattern="#,#00.00"/></td>

				<shiro:hasPermission name="pms:device:edit"></shiro:hasPermission>
				<td>
    				<a href="${ctx}/pms/device/form?id=${device.id}">修改</a>
				</td>
			</tr>
		</c:forEach>
		<tr>
			    <td></td>
			    <td></td>
			    <td></td>
				<td></td>	
				<td></td>
			    <td></td>
			    <td></td>
			    <td></td>
				<td></td>	
				<td></td>
				<td></td>
			</tr>
		</tbody>
	</table>
	<div class="modal-header">
	

	
	</div>
	<div class="pagination">${page}</div>
	
</body>
</html>
