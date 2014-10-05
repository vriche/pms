<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		

		    $("#feesId").remoteChained({
		        parents : "#proCompanyId",
		        url : "${ctx}/pms/fees/feesjson2?model=build",
		        loading : " ",
		        clear : true
	   		 });	   
	   		 
		   // $("#proCompanyId").trigger("change");
		   
		   
		    var url = '${ctx}/pms/fees/getPriceByFees';
		   	$("#feesId").change(function(){
		   	      //var id =  $("option:selected", this).val();
		   	      var id =  $("#feesId").val();
		   	      if(id > 0){
		   	         $.getJSON(url,{id:$("#feesId").val()},function(data){
		   	        	  $("#price").val(data.unitPrice);
		   	         });
		   	      }else{
		   	      	 $("#price").val(0);
		   	      }
		   	});

		
			$("#btnSubmit").click(function(){
			    var houseIds = $("#houseIds").val();
				$("#searchForm").action = "${ctx}/pms/build?houseIds="+houseIds;
				$("#searchForm").submit();
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
		<li class="active"><a href="${ctx}/pms/build/">欠费信息列表</a></li>
		
	</ul>
	<form:form id="searchForm" modelAttribute="device" action="${ctx}/pms/build/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		
		
		<div>
			<label class="control-label">物业公司:</label>
			<form:select id="proCompanyId" name="proCompanyId" path="house.unit.buildings.community.proCompany.id"  class="text medium;required">
						<form:option value="" label=""/>
						<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>
			
			<label class="control-label">收费项目:</label>
			<form:select id="feesId" name="feesId" path="fees.id" class="input-small text medium;required">
					<form:option value="" label=""/>
					<form:options items="${feesList}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>		
			
			&nbsp;价格<input id="price" class="input-small text medium;"  value="0"/>
		</div>
		
		<div>
   	    <label class="control-label">房&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;产：</label>  
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
	                      cssClass="input-xxlarge required" 
	                      proCompany="proCompanyId" 
	                      />      
	          		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="button" value="查询"/>
		&nbsp;<input id="btnSave" class="btn btn-primary" type="button" value="生成台帐"/>            
	                      
	</div>

	
	</form:form>
	
	
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
		<thead><tr>
			<th> <input type="checkbox"></th>
			<th>房屋</th>
			<th>业主</th>
			<th>单位</th>

		<th>编码</th>
		<th>收费项目</th>
		<th>上次读数</th>
		<th>本次读数</th>
		<th>上次日期</th>
		<th>本次日期</th>
		<th>使用量</th>
		<th>单价格</th>
		<th>应收金额</th>

		
		<shiro:hasPermission name="pms:device:edit"></shiro:hasPermission>
		<th>操作</th>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="device">
			<tr>
			   
			    <td><input type="checkbox"></td>
			    <td>${device.house.fullName}</td>
			    <td>${device.house.owner.name}</td>
			    <td>${device.house.owner.company.name}</td>
			 
				<td><a href="${ctx}/pms/device/form?id=${device.id}">${device.code}</a></td>
				
				<td>${device.fees.name}</td>
				<td>${device.firstNum}</td>
				<td>${device.lastNum}</td>
				<td>${device.firstDate}</td>
				<td>${device.lastDate}</td>				
				<td>200</td>
				<td>${device.fees.unitPrice}</td>			
			    <td>6000</td>
		
				
				<shiro:hasPermission name="pms:device:edit"></shiro:hasPermission>
				<td>
    				<a href="${ctx}/pms/device/form?id=${device.id}">修改</a>
					<a href="${ctx}/pms/device/delete?id=${device.id}" onclick="return confirmx('确认要删除该信息吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
