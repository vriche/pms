<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>

	
	<script type="text/javascript">
	
	    function submit(){
				$("#searchForm").attr("action","${ctx}/pms/device/");
				$("#searchForm").attr("enctype","application/x-www-form-urlencoded");
				$("#searchForm").attr("onsubmit","loading('正在搜索，请稍等...');");
				$("#searchForm").submit();
	    }
	
	
		$(document).ready(function() {


		   var url0 = "${ctx}/pms/office/companyjson?model=device";
		   $("#companyId").remoteChained("#proCompanyId", url0);	
		   
		   var url1 = "${ctx}/pms/community/communityjson?model=device";
		   $("#communityId").remoteChained("#proCompanyId", url1);	   
		   
		   var url5 = "${ctx}/pms/fees/feesjson2?model=device";
		   $("#feesId").remoteChained("#proCompanyId", url5);	    
		   
		   var url2 = "${ctx}/pms/buildings/buildingsjson?model=device";
		   $("#buildingsId").remoteChained("#communityId", url2);
		   
		   var url3 = "${ctx}/pms/unit/unitjson?model=device";
		   $("#unitId").remoteChained("#buildingsId", url3);	
		   
		   var url4 = "${ctx}/pms/house/housejson?model=device";
		   $("#houseId").remoteChained("#unitId", url4);			
		   
		   
		      
		   
		   

			$("#add").click(function(){
			    location.href = "${ctx}/pms/device/form?type="+$("#type").val()+"&fees.company.id="+$("#proCompanyId").val();
			})
			
			//$("#saveDeviceByHouseList").click(function(){ location.href = "${ctx}/pms/device/savehouse";})
			
			 
			
			$("#type").change(function(e){
	
			   if($("#type").val() != 3){
			  		 $("#house_area").hide();
			   }else{
			  		 $("#house_area").show();
			   }
			   
			   if($("#type").val() == 1){
			  		 $("#company_area").hide();
			   }else{
			  		 $("#company_area").show();
			   }		   
			});
			 
	  
	
		$("#btnSubmit").click(function(){
				submit();
			});
			   
			$("#btnExport").click(function(){
		       
				top.$.jBox.confirm("确认要导出吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/pms/device/export/");
						$("#searchForm").attr("enctype","application/x-www-form-urlencoded");
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
		
		
		function importSubmit(){
					$("#importForm").attr("action","${ctx}/pms/device/import");
					$("#importForm").attr("enctype","multipart/form-data");
					$("#importForm").attr("onsubmit","loading('正在导入，请稍等...');");
					$("#importForm").submit();
		}

		
		
		
			
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			submit();
        	return false;
        }
	</script>
</head>
<body>  


	
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/pms/device/">设备信息列表</a></li>
		<li><a id="add" href="javascript:void 0">设备信息添加</a></li>
	</ul>
	
	
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/pms/device/import" method="post" enctype="multipart/form-data"
			style="padding-left:20px;text-align:center;" class="form-search" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   " />
		</form>
	</div>
	
	<form:form id="searchForm" modelAttribute="device" action="${ctx}/pms/device/" method="post" class="breadcrumb form-search">
	
	
	
	<!-- div id="importBox" class="hide">
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="button" value="   导    入   "  onclick="importSubmit()"/>
	</div -->
	
	
	<div>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		
		<label class="control-label">物业:</label>
		<form:select id="proCompanyId" name="proCompanyId" path="fees.company.id"  class="text medium;required">
					<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
		<label class="control-label">计费类型:</label>
		<form:select id="type" name="type" path="type"  class="input-small text medium">
					<form:options items="${fns:getDictList('pms_device_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		</form:select>	
		
		<label class="control-label">费项:</label>
			<form:select id="feesId" name="feesId" path="fees.id" class="input-small text medium;required">
					<form:option value="" label=""/>
					<form:options items="${feesList}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>		
		
		<span id="company_area">
			<label class="control-label">单位:</label>
			<form:select id="companyId" name="companyId" path="house.owner.company.id"  class="text medium;required">
						<form:option value="" label=""/>
						<form:options items="${companyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>		
			
				
		</span>	
		
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="button" value="查询"/>
		&nbsp;<input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
		&nbsp;<input id="btnImport" class="btn btn-primary" type="button" value="导入"/>
	
	</div>	
	
	<div style="margin-top:8px;">	
	
        <span id="house_area">
        

 
	 <c:choose>

       <c:when test="${device.type !=3}">
 			<script type="text/javascript"> 
 			$("#company_area").hide();
 			$("#house_area").hide();
 			</script>
       </c:when>

       <c:otherwise>
 			<script type="text/javascript"> 
 			$("#company_area").show();
 			$("#house_area").show();
 			</script>
       </c:otherwise>
       
	</c:choose>	
	
	
	 <c:choose>
       <c:when test="${device.type ==1}">
 			<script type="text/javascript"> 
 			$("#company_area").hide();
 			</script>
       </c:when>

       <c:otherwise>
 			<script type="text/javascript"> 
 			$("#company_area").show();
 			</script>
       </c:otherwise>     
       
	</c:choose>		

		<label class="control-label">&nbsp;&nbsp;&nbsp;小区:</label>
		<form:select id="communityId" name="communityId" path="house.unit.buildings.community.id"  class="text medium;">
					<form:option value="" label=""/>
					<form:options items="${communityList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
		<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;楼宇:</label>
		<form:select id="buildingsId" name="buildingsId" path="house.unit.buildings.id"  class="input-small">
					<form:option value="" label=""/>
					<form:options items="${buildingsList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>		
		
		<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;单元:</label>
		<form:select id="unitId" name="unitId" path="house.unit.id"  class="input-small">
					<form:option value="" label=""/>
					<form:options items="${unitList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>	
		
		<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;房屋:</label>
		<form:select id="houseId" name="houseId" path="house.id"  class="input-small">
					<form:option value="" label=""/>
					<form:options items="${houseList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>	
		   
		          
		</span>		
		
		</div>
		
		
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
		<thead><tr>
		<th>编码</th>
		<th>收费项目</th>
		
		<c:if test="${device.type  == 1}">	
		    <th>分摊方式</th>
			<th>分摊户数</th>		
			<th>上次读数</th>
			<th>本次读数</th>
			<th>使用量</th>
			<th>上次日期</th>
			<th>本次日期</th>	
		</c:if> 	
		
		<c:if test="${device.type  == 2}">	
			<th>单位</th>
			<th>上次读数</th>
			<th>本次读数</th>
			<th>使用量</th>
			<th>本次公摊量</th>	
			<th>上次日期</th>
			<th>本次日期</th>	
			
			
		</c:if> 		
		
		<c:if test="${device.type  == 3}">	
			<th>公摊</th>
			<th>房屋</th>
			<th>业主</th>
			<th>单位</th>			
			<th>上次读数</th>
			<th>本次读数</th>
			<th>使用量</th>
			<th>本次公摊量</th>	
			<th>上次日期</th>
			<th>本次日期</th>	
		</c:if> 	
		
		<th>交费限期</th>	

		
		<shiro:hasPermission name="pms:device:edit"></shiro:hasPermission>
		<th>操作</th>
		
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="device">
			<tr>
				<td><a href="${ctx}/pms/device/form?id=${device.id}">${device.code}</a></td>
				<td>${device.fees.name}</td>
			
				<c:if test="${device.type  == 1}">
					<td>${fns:getDictLabel(device.feesMode, 'pms_fees_mode', '')}</td>
					<td>${fn:length(device.childList)}</td>		
					<td>${device.firstNum}</td>
					<td>${device.lastNum}</td>
				    <td>${device.usageAmount}</td>	
				    <td><fmt:formatDate value="${device.firstDate}" pattern="yyyy-MM-dd"/></td>
					<td><fmt:formatDate value="${device.lastDate}" pattern="yyyy-MM-dd"/></td>	
				</c:if> 					

				<c:if test="${device.type  == 2}">
				    <td>${device.house.owner.company.name}</td>				
					<td>${device.firstNum}</td>
					<td>${device.lastNum}</td>
				    <td>${device.lastNum - device.firstNum}</td>	
				    <td>${device.usageAmount}</td>	
				    <td><fmt:formatDate value="${device.firstDate}" pattern="yyyy-MM-dd"/></td>
					<td><fmt:formatDate value="${device.lastDate}" pattern="yyyy-MM-dd"/></td>					
				</c:if> 	
				
				<c:if test="${device.type  == 3}">
					<td>${device.pool}</td>
				    <td>${device.house.fullName}</td>
				    <td>${device.house.owner.name}</td>
				    <td>${device.house.owner.company.name}</td>				
					<td>${device.firstNum}</td>
					<td>${device.lastNum}</td>
				    <td>${device.usageAmount}</td>	
				    <td>${device.poolUsageAmount}</td>	
				    <td><fmt:formatDate value="${device.firstDate}" pattern="yyyy-MM-dd"/></td>
					<td><fmt:formatDate value="${device.lastDate}" pattern="yyyy-MM-dd"/></td>					
				</c:if> 	
				
				<td><fmt:formatDate value="${device.paymentDate}" pattern="yyyy-MM-dd"/></td>
												
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
