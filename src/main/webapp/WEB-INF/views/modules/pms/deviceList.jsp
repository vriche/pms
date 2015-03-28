<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>

	
	<script type="text/javascript">
	
	    function submit(){
	    	 if(checkFees()){
				$("#searchForm").attr("action","${ctx}/pms/device/");
				$("#searchForm").attr("enctype","application/x-www-form-urlencoded");
				$("#searchForm").attr("onsubmit","loading('正在搜索，请稍等...');");
				$("#searchForm").submit();
	    	 }
	    }
	    
	    function initDevice(){
	    	 if(checkFees()){
				$("#searchForm").attr("action","${ctx}/pms/device/initDevice");
				$("#searchForm").attr("enctype","application/x-www-form-urlencoded");
				$("#searchForm").attr("onsubmit","loading('正在初始化，请稍等...');");
				$("#searchForm").submit();
	    	 }
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
			
			$("#feesId").change(function(){ 
				$("#searchForm").attr("action","${ctx}/pms/device/");
				$("#searchForm").attr("enctype","application/x-www-form-urlencoded");
				$("#searchForm").attr("onsubmit","loading('正在搜索，请稍等...');");
				$("#searchForm").submit();
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
		
		$("#btnInit").click(function(){
			initDevice();
		});
		
			   
			$("#btnExport").click(function(){
		       
				 if(checkFees()){
						top.$.jBox.confirm("确认要导出吗？","系统提示",function(v,h,f){
							if(v=="ok"){
								$("#searchForm").attr("action","${ctx}/pms/device/export/");
								$("#searchForm").attr("enctype","application/x-www-form-urlencoded");
								$("#searchForm").submit();					
							}
						},{buttonsFocus:1});
						top.$('.jbox-body .jbox-icon').css('top','55px');					 
				 }

				
				
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

		function checkFees(){
			if($("#feesId").val() ==""){
				top.$.jBox.alert("收费项目必选");
			   $("#feesId").focus();
			}else{
				return true;
			}
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
		<li class="active"><a href="${ctx}/pms/device/">计费设备列表</a></li>
		<li><a id="add" href="javascript:void 0">计费设备信息添加</a></li>
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
		<input id="initDevice" name="initDevice" type="hidden" value="0"/>
		
		<label class="control-label">物业:</label>
		<form:select id="proCompanyId" name="proCompanyId" path="fees.company.id"  class="text medium;required">
					<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
		<label class="control-label">计费类型:</label>
		<form:select id="type" name="type" path="type"   style="width:100px;">
					<form:options items="${fns:getDictList('pms_device_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		</form:select>	
		
		<label class="control-label">费项:</label>
			<form:select id="feesId" name="feesId" path="fees.id"  style="width:100px;">
					<form:options items="${feesList}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>		
		
		<span id="company_area">
			<label class="control-label">单位:</label>
			<form:select id="companyId" name="companyId" path="house.owner.company.id"  class="text medium;required">
						<form:option value="" label=""/>
						<form:options items="${companyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>		
			
				
		</span>	
		

	
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
		
		<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;楼宇:</label>
		<form:select id="buildingsId" name="buildingsId" path="house.unit.buildings.id"  style="width:100px;">
					<form:option value="" label=""/>
					<form:options items="${buildingsList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>		
		
		<label class="control-label">&nbsp;&nbsp;单元:</label>
		<form:select id="unitId" name="unitId" path="house.unit.id"   style="width:100px;">
					<form:option value="" label=""/>
					<form:options items="${unitList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>	
		
		<label class="control-label">&nbsp;房屋:</label>
		<form:select id="houseId" name="houseId" path="house.id"   style="width:120px;">
					<form:option value="" label=""/>
					<form:options items="${houseList}" itemLabel="code" itemValue="id" htmlEscape="false" />
		</form:select>	
		   
		          
		</span>		
		
		</div>
		
		<div  style="margin-top:8px;">
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="button" value="&nbsp;&nbsp;查&nbsp;&nbsp;  &nbsp;  询&nbsp;&nbsp;"/>
		
		&nbsp;<input id="btnExport" class="btn btn-primary" type="hidden" value="导  出"/>
		&nbsp;<input id="btnImport" class="btn btn-primary" type="hidden" value="导  入"/>
		
		&nbsp;<input id="btnInit" class="btn btn-primary" type="button" value="初始化计费项"/>
		</div>
		
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
		<thead><tr>
		<th>编码</th>
		<!-- th>收费项目</th -->
		<th>收费方式</th>
		<c:if test="${device.type  == 1}">	
			<th>分摊户数</th>		
		</c:if> 	
		
		<c:if test="${device.type  == 2}">	
			<th>单位</th>
		</c:if> 		
		
		<c:if test="${device.type  == 3}">	
			<th>有公摊</th>
			<th>房屋</th>
			<th>业主编码</th>
			<th>业主</th>
			<th>单位</th>			
		</c:if> 	
		

		<shiro:hasPermission name="pms:device:edit"></shiro:hasPermission>
		<th>启用</th>
	
		<th>操作</th>
		
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="device">
			<tr>
				<td><a href="${ctx}/pms/device/form?id=${device.id}">${device.code}</a></td>
				
				<!-- 
				<td>${device.fees.name}</td>
				<td>${fns:getDictLabel(device.fees.feesType, 'pms_fees_type', ' ')}</td>
				-->
				<td>${fns:getDictLabel(device.fees.feesMode, 'pms_fees_mode', '')}</td>
				<c:if test="${device.type  == 1}">
					
					<td>${fn:length(device.childList)}</td>		
				</c:if> 					

				<c:if test="${device.type  == 2}">
				    <td>${device.house.owner.company.name}</td>						
				</c:if> 	
				
				<c:if test="${device.type  == 3}">
					<!-- td>${device.pool}</td -->

					
				<c:choose>
				    <c:when test="${empty device.parent}">
				    	<td></td>
				    </c:when>
				    <c:otherwise>
				    	<td>有</td>
				    </c:otherwise>
				</c:choose>	
					
					
				    <td>${device.house.fullName}</td>
				    <td>${device.house.owner.loginName}</td>
				    <td>${device.house.owner.name}</td>
				    <td>${device.house.owner.company.name}</td>								
				</c:if> 	
				
				<c:choose>
			    <c:when test="${device.enable == '1'}">
			    	<td>是</td>
			    </c:when>
			    <c:otherwise>
			    <td></td>
			    </c:otherwise>
			    </c:choose>	
	
												
				<shiro:hasPermission name="pms:device:edit"></shiro:hasPermission>
				<td>
    				<a href="${ctx}/pms/device/form?id=${device.id}">修改</a> 
					<a href="${ctx}/pms/device/delete?id=${device.id}&proCompanyId=${device.fees.company.id}&type=${device.type}&feesId=${device.fees.id}" onclick="return confirmx('确认要删除该信息吗？', this.href)">删除</a>
				</td>				

			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
