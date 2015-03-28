<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>

	
	<script type="text/javascript">
	
	   function selectDate(){WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});};
	   
    function submit(){
   	 if(checkFeesQuery()){
			$("#searchForm").attr("action","${ctx}/pms/deviceDetail/list/");
			$("#searchForm").attr("enctype","application/x-www-form-urlencoded");
			$("#searchForm").attr("onsubmit","loading('正在搜索，请稍等...');");
//			$(".lastDateTemp").val($("#lastDateTemp").val());
			$("#searchForm").submit();
   	 }
   }

    
    function btnAutoBuild(f){
		var paymentDate = f.paymentDate;
        
		if(paymentDate ==""){
			top.$.jBox.alert("请选择抄表日期");
			return false;
		}
		 if(checkFees()){
			 $("#paymentDate").val(paymentDate);
			 $("#searchForm").attr("action","${ctx}/pms/deviceDetail/autoBuild/");
			 $("#searchForm").attr("enctype","application/x-www-form-urlencoded");
			 $("#searchForm").submit();
			 $("#searchForm").attr("action","${ctx}/pms/deviceDetail/list/");
		 }else{
				return false;
		 }
	}
	
		$(document).ready(function() {


		   var url0 = "${ctx}/pms/office/companyjson?model=device";
		   $("#companyId").remoteChained("#proCompanyId", url0);	
		   
		   var url1 = "${ctx}/pms/community/communityjson?model=deviceDetail";
		   $("#communityId").remoteChained("#proCompanyId", url1);	   
		   
		   var url5 = "${ctx}/pms/fees/feesjson2?model=deviceDetail";
		   $("#feesId").remoteChained("#proCompanyId", url5);	    
		   
		   var url2 = "${ctx}/pms/buildings/buildingsjson?model=deviceDetail";
		   $("#buildingsId").remoteChained("#communityId", url2);
		   
		   var url3 = "${ctx}/pms/unit/unitjson?model=deviceDetail";
		   $("#unitId").remoteChained("#buildingsId", url3);	
		   
		   var url4 = "${ctx}/pms/house/housejson?model=deviceDetail";
		   $("#houseId").remoteChained("#unitId", url4);			
		   
		   var url5 = "${ctx}/pms/deviceDetail/datejson?model=deviceDetail";
		   $("#lastDate").remoteChained("#proCompanyId,#type,#feesId,#communityId,#buildingsId,#unitId", url5);   

//			$("#add").click(function(){
//			    location.href = "${ctx}/pms/device/form?type="+$("#type").val()+"&fees.company.id="+$("#proCompanyId").val();
//			})
			
			//$("#saveDeviceByHouseList").click(function(){ location.href = "${ctx}/pms/device/savehouse";})
		   
		
		   
//		   $("#paymentDate").click(function (e) {
//			   alert(99);
//			   selectDate();});	
		   

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
			   
			$("#btnExport1").click(function(){
				 if(checkFees()){
						top.$.jBox.confirm("确认要导出吗？","系统提示",function(v,h,f){
							if(v=="ok"){
								$("#searchForm").attr("action","${ctx}/pms/deviceDetail/export/?type=1");
								$("#searchForm").attr("enctype","application/x-www-form-urlencoded");
								$("#searchForm").submit();					
							}
						},{buttonsFocus:1});
						top.$('.jbox-body .jbox-icon').css('top','55px');					 
				 }
			});
			
			$("#btnExport2").click(function(){
				 if(checkFeesQuery()){
						top.$.jBox.confirm("确认要导出吗？","系统提示",function(v,h,f){
							if(v=="ok"){
								$("#searchForm").attr("action","${ctx}/pms/deviceDetail/export/?type=2");
								$("#searchForm").attr("enctype","application/x-www-form-urlencoded");
								$("#searchForm").submit();					
							}
						},{buttonsFocus:1});
						top.$('.jbox-body .jbox-icon').css('top','55px');					 
				 }
			});		
			
	
			
			
			

			
			$("#btnAutoBuild1").click(function(){
				
				$.jBox($("#autoBuildBox").html(), {
					title:"自动生成", 
					buttons:{ '确  定': 1, '关   闭': 0 }, 
					bottomText:"",
					submit: function (v, h, f) {
						 if(v == 1){
							 btnAutoBuild(f);
						 }else{
							 return true;
						 }
					}
				}
				);
			});  			
			
			$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			});   

			
		});
		
		
		function importSubmit(){
					$("#importForm").attr("action","${ctx}/pms/deviceDetail/import");
					$("#importForm").attr("enctype","multipart/form-data");
					$("#importForm").attr("onsubmit","loading('正在导入，请稍等...');");
					$("#importForm").submit();
		}

		function checkFees(){
			
			var pass = true;
			
			if($("#feesId").val() ==""){
				top.$.jBox.alert("收费项目必选");
			   $("#feesId").focus();
			   pass = false;
			}
				
			if($("#type").val() =="3" && $("#communityId").val() ==""){
					top.$.jBox.alert("请选择小区");
				   $("#communityId").focus();
				   pass = false;
			}
			
			return pass;
	    }	
		
		
	function checkFeesQuery(){
			
		    var pass = checkFees();
			
		    if(pass){
				if($("#lastDate").val() ==""){
					top.$.jBox.alert("请选择抄表日期");
				   $("#lastDate").focus();
				   pass = false;
				}		    	
		    }

			
			return pass;
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
		<li class="active"><a href="${ctx}/pms/deviceDetail/">读表信息列表</a></li>
	</ul>
	
	
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/pms/deviceDetail/import" method="post" enctype="multipart/form-data"
			style="padding-left:20px;text-align:center;" class="form-search" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   " />
		</form>
	</div>
	

	<form:form id="searchForm" modelAttribute="deviceDetail" action="${ctx}/pms/deviceDetail/" method="post" class="breadcrumb form-search">
	
	
	
	<div id="autoBuildBox" class="hide">
			<label class="control-label">抄表日期:</label>
			<input id="paymentDate" name="paymentDate" type="text" readonly="readonly" maxlength="20" onClick="WdatePicker({})" 
value="<fmt:formatDate value="${paymentDate}" pattern="yyyy-MM-dd"/>" class=" Wdate"/>
	
    </div>
    


	<div>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	
		
		<label class="control-label">物业:</label>
		<form:select id="proCompanyId" name="proCompanyId" path="device.fees.company.id"  class="text medium;required">
					<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
		<label class="control-label">计费类型:</label>
		<form:select id="type" name="type" path="device.type"  style="width:100px;">
					<form:options items="${fns:getDictList('pms_device_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		</form:select>	
		
		<label class="control-label">费项:</label>
		<form:select id="feesId" name="feesId" path="device.fees.id"  style="width:100px;">
					<!-- form:option value="" label=""/ -->
					<form:options items="${feesList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>		
		
		<span id="company_area">
			<label class="control-label">单位:</label>
			<form:select id="companyId" name="companyId" path="device.house.owner.company.id"  class="text medium;required">
						<form:option value="" label=""/>
						<form:options items="${companyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>		
			
				
		</span>	



	</div>	
	

	
	<div style="margin-top:8px;">	
	
        <span id="house_area">
        

 
	 <c:choose>
       <c:when test="${deviceDetail.device.type !=3}">
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
       <c:when test="${deviceDetail.device.type ==1}">
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
		<form:select id="communityId" name="communityId" path="device.house.unit.buildings.community.id"  class="text medium;">
					<form:option value="" label=""/>
					<form:options items="${communityList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
		<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;楼宇:</label>
		<form:select id="buildingsId" name="buildingsId" path="device.house.unit.buildings.id"  style="width:100px;">
					<form:option value="" label=""/>
					<form:options items="${buildingsList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>		
		
		<label class="control-label">&nbsp;&nbsp;&nbsp;单元:</label>
		<form:select id="unitId" name="unitId" path="device.house.unit.id"   style="width:100px;">
					<form:option value="" label=""/>
					<form:options items="${unitList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>	
		
		<label class="control-label">&nbsp;房屋:</label>
		<form:select id="houseId" name="houseId" path="device.house.id"   style="width:120px;">
					<form:option value="" label=""/>
					<form:options items="${houseList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>	
		   
		          
		</span>		
		
		</div>
		
		<div style="margin-top:8px;">	
		<label class="control-label">日期:</label>
		<form:select id="lastDate" name="lastDate" path="lastDate"  class="text medium;required">
			<form:option value="" label="选择读表日期....."/>
			<form:options items="${lastDateList}" itemLabel="remarks" itemValue="remarks" htmlEscape="false" />
		</form:select>	
		
		
		
		&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="button" value="查     询"/>
		&nbsp;&nbsp;<input id="btnExport2" class="btn btn-primary" type="button" value="导出历史"/>
		&nbsp;&nbsp;<input id="btnExport1" class="btn btn-primary" type="button" value="导出模板"/>
		&nbsp;&nbsp;<input id="btnImport" class="btn btn-primary" type="button" value="导入数据"/>
		
		&nbsp;&nbsp;<input id="btnAutoBuild1" class="btn btn-primary" type="button" value="自动生成"/>


	
		</div>
		</div>	
		
	</form:form>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
		<thead><tr>
		
		<!-- th>收费项目</th -->
		
		<c:if test="${deviceDetail.device.type  == 1}">	
		    <th>编码</th>
		    <th>费用方式</th>
			<th>分摊户数</th>		
			<th>上次读数</th>
			<th>本次读数</th>
			<th>使用量</th>
			<th>单价</th>
			<th>应付金额</th>
			<th>上次日期</th>
			<th>本次日期</th>	
		</c:if> 	
		
		<c:if test="${deviceDetail.device.type  == 2}">	
			<th>单位</th>
			<th>上次日期</th>
			<th>本次日期</th>	
			<th>上次读数</th>
			<th>本次读数</th>
			<th>使用量</th>
			<th>单价</th>
			<!-- th>本次公摊量</th -->	
			<th>应付金额</th>
			<th>已付金额</th>
		</c:if> 		
		
		<c:if test="${deviceDetail.device.type  == 3}">	
		
			<c:if test="${deviceDetail.device.fees.feesMode  == 5}">	
			<th>收费编号</th>
			</c:if> 
		
		    <th>用户编码</th>
			<th>业主</th>
			<!-- th>公摊</th -->
			<th>房屋</th>
			<th>单位</th>	
			<th>上次日期</th>
			<th>本次日期</th>	
			<th>上次读数</th>
			<th>本次读数</th>
			<th>使用量</th>
			<th>单价</th>
			<th>本次付</th>	
			<th>公摊费</th>	
			<th>总应付</th>
			<th>已付</th>
		</c:if> 	
		
		<!-- th>交费限期</th -->	

		
		<shiro:hasPermission name="pms:device:edit"></shiro:hasPermission>
		<!-- th>操作</th -->
		
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="deviceDetail">
			<tr>
				
				<!-- td>${deviceDetail.device.fees.name}</td -->
			
				<c:if test="${deviceDetail.device.type  == 1}">
				    <td><a href="${ctx}/pms/deviceDetail/form?id=${deviceDetail.id}">${deviceDetail.device.code}</a></td>
					<td>${fns:getDictLabel(deviceDetail.device.feesMode, 'pms_fees_mode', '')}</td>
					<td>${fn:length(deviceDetail.device.childList)}</td>		
					<td>${deviceDetail.firstNum}</td>
					<td>${deviceDetail.lastNum}</td>
				    <td>${deviceDetail.usageAmount}</td>	
				    <td>${deviceDetail.unitPrice}</td>	
				    <td>${deviceDetail.sumPayMoney}</td>
				    <td><fmt:formatDate value="${deviceDetail.firstDate}" pattern="yyyy-MM-dd"/></td>
					<td><fmt:formatDate value="${deviceDetail.lastDate}" pattern="yyyy-MM-dd"/></td>	
				</c:if> 					

				<c:if test="${deviceDetail.device.type  == 2}">
				    <td><a href="${ctx}/pms/deviceDetail/form?id=${deviceDetail.id}">${deviceDetail.device.house.owner.company.name}</a></td>
				    <td><fmt:formatDate value="${deviceDetail.firstDate}" pattern="yyyy-MM-dd"/></td>
					<td><fmt:formatDate value="${deviceDetail.lastDate}" pattern="yyyy-MM-dd"/></td>		
					<td>${deviceDetail.firstNum}</td>
					<td>${deviceDetail.lastNum}</td>
				    <td>${deviceDetail.usageAmount}</td>	
				    <td>${deviceDetail.unitPrice}</td>	
				    <td>${deviceDetail.sumPayMoney}</td>
				    <td>${deviceDetail.incomeMoney}</td>			
				</c:if> 	
				
				<c:if test="${deviceDetail.device.type  == 3}">
					<c:if test="${deviceDetail.device.fees.feesMode  == 5}">	
					 <td>${deviceDetail.device.code}</td>
					</c:if> 
				    <td><a href="${ctx}/pms/deviceDetail/form?id=${deviceDetail.id}">${deviceDetail.device.house.owner.loginName}</a></td>
				    <td>${deviceDetail.device.house.owner.name}</td>
				    <td>${deviceDetail.device.house.fullName}</td>
				    <td>${deviceDetail.device.house.owner.company.name}</td>	
				    <td><fmt:formatDate value="${deviceDetail.firstDate}" pattern="yyyy-MM-dd"/></td>
					<td><fmt:formatDate value="${deviceDetail.lastDate}" pattern="yyyy-MM-dd"/></td>	
					<td>${deviceDetail.firstNum}</td>
					<td>${deviceDetail.lastNum}</td>
				    <td>${deviceDetail.usageAmount}</td>	
				    <td>${deviceDetail.unitPrice}</td>
				    <td>${deviceDetail.payMoney}</td>
				    <td>${deviceDetail.poolPayMoney}</td>	
				    <td>${deviceDetail.sumPayMoney}</td>
				    <td>${deviceDetail.incomeMoney}</td>
				</c:if> 	
				
				<!-- td><fmt:formatDate value="${deviceDetail.paymentDate}" pattern="yyyy-MM-dd"/></td -->
												
				<shiro:hasPermission name="pms:device:edit"></shiro:hasPermission>
				<!-- td -->
    				<!-- a href="${ctx}/pms/deviceDetail/form?id=${deviceDetail.id}">修改</a -- >
					<!-- a href="${ctx}/pms/deviceDetail/delete?id=${deviceDetail.id}" onclick="return confirmx('确认要删除该信息吗？', this.href)">删除</a -->
				<!--/td -->				

			</tr>
		</c:forEach>
		
		

		
		<c:if test="${page.count > 0}">		
			<c:if test="${deviceDetailSumRow.device.type == 1}">	
				<tr><td>合计:</td><td></td><td></td><td></td><td></td><td>${deviceDetailSumRow.sumUsageAmount}</td><td></td><td>${deviceDetailSumRow.sumPayMoney}</td><td></td><td></td></tr>
			</c:if> 	
			<c:if test="${deviceDetailSumRow.device.type == 2}">	
				<tr><td>合计:</td><td></td><td></td><td></td><td></td><td></td><td></td><td>${deviceDetailSumRow.sumPayMoney}</td><td>${deviceDetailSumRow.incomeMoney}</td></tr> 
			</c:if> 
			
			
			
			<c:if test="${deviceDetailSumRow.device.type == 3}">	
				<tr><td>合计:</td><c:if test="${deviceDetail.device.fees.feesMode  == 5}"><td></td></c:if> 
				<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>${deviceDetailSumRow.usageAmount}</td><td></td><td>${deviceDetailSumRow.payMoney}</td><td>${deviceDetailSumRow.poolPayMoney}</td><td>${deviceDetailSumRow.sumPayMoney}</td><td>${deviceDetailSumRow.incomeMoney}</td></tr>
			</c:if> 	
		</c:if> 	 
		</tbody>
	</table> 

	<div class="pagination">${page}</div>
</body>
</html>
