<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/xeditable.jsp" %>
	
	<script type="text/javascript">
	

			
		$(document).ready(function() {

			$("#add").click(function(){
			    location.href = "${ctx}/pms/device/form?type="+$("#type").val();
			    
			     var a = $('#lastNum').editable('getValue');
			})
		
			//$("#Btndevice").click(function(e){
			   // console.log(e)
				//var url = "${ctx}/pms/deviceDetail/form?id=${device.id}&lastDate="+$("#lastDate").val()+"&paymentDate="+$("#paymentDate").val();
			    //location.href = "${ctx}/pms/deviceDetail/form?id=${device.id}&lastDate="+$("#lastDate").val();
			    //alert(url); +“&paymentDate="+$("#paymentDate").val();
			//})
			
	
			
			
		   function selectDate(){WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});};	
			$("#lastDate").click(function (e) {selectDate();});	
			$("#paymentDate").click(function (e) {selectDate();});	
			

		   
		   var url1 = "${ctx}/pms/fees/feesjson2?model=device";
		   $("#feesId").remoteChained({parents : "#proCompanyId",url : url1,loading : " ",clear : true});	 	   
		   
		   
		   var url0 = "${ctx}/pms/office/companyjson?model=device";
		   $("#companyId").remoteChained("#proCompanyId", url0);	
		   
		   var url = "${ctx}/pms/community/communityjson?model=device";
		   $("#communityId").remoteChained("#proCompanyId", url);	   
		   
		   var url2 = "${ctx}/pms/buildings/buildingsjson?model=device";
		   $("#buildingsId").remoteChained("#communityId", url2);
		   
		   var url2 = "${ctx}/pms/unit/unitjson?model=device";
		   $("#unitId").remoteChained("#buildingsId", url2);		
		   
		   var url4 = "${ctx}/pms/house/housejson?model=device";
		   $("#houseId").remoteChained("#unitId", url4);		
			
			
			$.fn.editable.defaults.mode = 'inline';
			//$.fn.editable.defaults.ajaxOptions = {type: "put",url:"${ctx}/pms/deviceDetail/savelastnum"};
			//$.fn.editable.defaults.url = '${ctx}/pms/payemtDetail/saveLastNum/'; 
		//	$('.myeditable').editable({url: '${ctx}/pms/payemtDetail/saveLastNum/' });
			
			$('#id').editable();   
			$('#lastNum').editable();   	

			$("#btnSave").click(function(){
			     var a = $('#id,#lastNum').editable('getValue');
			})
			
			
			//$("#lastNum").submit(function(){alert(111)})
			

			$("#btnSubmit").click(function(){
			  if(checkFees()){
				//loading('正在提交，请稍等...');
		    	$("#searchForm").attr("action", "${ctx}/pms/deviceDetail/list");
		    	$("#searchForm").attr("onsubmit","loading('正在生成，请稍等...');");
		    	$("#searchForm").submit();
			  }
			});
			
			$("#btnSave").click(function(){
			  if(checkFees()){
			  // loading('正在保存，请稍等...');
			   $("#searchForm").attr("action", "${ctx}/pms/deviceDetail/savedetail");
			   $("#searchForm").attr("onsubmit","loading('正在保存，请稍等...');");
			   $("#searchForm").submit();
			  }
			});
			
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
		   	        	//  $("#searchForm").submit();
		   	         });
		   	      }else{
		   	      	 $("#usageAmount").val(0);$("#unitPrice").val(0);$("#payMoney").val(0);
		   	      	// $("#searchForm").submit();
		   	      }
		   	});		
		   	

		   	
			
		});
		
		
		function gett(id){
			location.href = "${ctx}/pms/deviceDetail/form?id="+ id +"&lastDate="+$("#lastDate").val()+"&paymentDate="+$("#paymentDate").val();
		}
		function checkFees(){
				if($("#feesId").val() ==""){
					top.$.jBox.alert("收费项目必选");
				   $("#feesId").focus();
				}else{
				    var paymentDate = $("#paymentDate").val();
				    var lastDate = $("#lastDate").val();
				    if(paymentDate < lastDate){
				    	top.$.jBox.alert("交费限期必须大于读表日期");
				 		$("#paymentDate").focus();
				    }else{
				  	  return true;
				    }
					
				}
		}	
		
			
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			//$("#searchForm").attr("action","${ctx}/pms/deviceDetail/");
			 $("#searchForm").attr("onsubmit","loading('正在生成，请稍等...');");
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>  
	<ul class="nav nav-tabs">
		<li><a href="#">生成缴费信息</a></li>
		
	</ul>
	
	<form:form id="searchForm" modelAttribute="device" action="#" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
		
					<label class="control-label">物业公司:</label>
			<form:select id="proCompanyId" name="proCompanyId" path="fees.company.id"  class="text medium;required">
						<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>		
	
		<label>小区:</label>
		<form:select id="communityId" name="communityId" path="house.unit.buildings.community.id"  class="input-small">
					<form:option value="" label=""/>
					<form:options items="${communityList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
		<label>楼宇:</label>
		<form:select id="buildingsId" name="buildingsId" path="house.unit.buildings.id"  class="input-small">
					<form:option value="" label=""/>
					<form:options items="${buildingsList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>		
		
		<label>单元:</label>
		<form:select id="unitId" name="unitId" path="house.unit.id"  class="input-small">
					<form:option value="" label=""/>
					<form:options items="${unitList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>	
		
		<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;房屋:</label>
		<form:select id="houseId" name="houseId" path="house.id"  class="input-small">
					<form:option value="" label=""/>
					<form:options items="${houseList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>		
		
		</div>
		<div>
		
			<label class="control-label">单位名称:</label>
			<form:select id="companyId" name="companyId" path="house.owner.company.id"  class="text medium;required">
						<form:option value="" label=""/>
						<form:options items="${companyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>	
		
		
		
		<label class="control-label">费项:</label>
		<form:select id="feesId" name="feesId" path="fees.id" class="input-small text medium;required">
					<form:option value="" label=""/>
					<form:options items="${feesList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>	
		
		

		
		     <label class="control-label">读表日期:</label>
					<input id="lastDate" name="lastDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${device.lastDate}" pattern="yyyy-MM-dd"/>" class=" Wdate input-small"/>	
							
			 <label class="control-label">交费限期:</label>
					<input id="paymentDate" name="paymentDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${device.paymentDate}" pattern="yyyy-MM-dd"/>" class=" Wdate input-small"/>							
							
							
	
		

		

		
		</div>
		
		<div>	
				<label>公摊设备:</label>
				<form:select id="deviceId" name="deviceId" path="parent.id"  class="text medium;required">
							<form:option value="" label=""/>
							<form:options items="${parentList}" itemLabel="codeName" itemValue="id" htmlEscape="false" />
				</form:select>
	  	   
		    &nbsp;用量: <form:input id="usageAmount" path="usageAmount"  class="input-small"  readonly="true"/>
			&nbsp;单价 <form:input id="unitPrice" path="fees.unitPrice"  class="input-small"  readonly="true"/>
			&nbsp;应收 <form:input id="payMoney" path="payMoney"  class="input-small"  readonly="true"/>
			
			
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="button" value="&nbsp;&nbsp;&nbsp;生成&nbsp;&nbsp;&nbsp;"/>
			&nbsp;<input id="btnSave" 	class="btn btn-primary" type="button" value="&nbsp;&nbsp;保存&nbsp;&nbsp;"/>		
	
		</div>
		
	</form:form>

	
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
		<thead><tr>
		<th>房间</th>
		<th>业主</th>
		<th>费用</th>
		
	<!-- 1 按住户 -->
	<c:if test="${feesMode == 1}">			
		<th>上次日期</th>
		<th>缴费期限</th>
		<th>使用量</th>
		<th>单价(户)</th>
	</c:if> 
	
	<!-- 2 按房屋面积 -->		
	<c:if test="${feesMode == 2}">			
		<th>上次日期</th>	
		<th>缴费期限</th>
		<th>建筑面积</th>
		<th>单价(m)</th>
	</c:if>

	<!-- 3  按加建面积 -->		
	<c:if test="${feesMode == 3}">		
		<th>上次日期</th>		
		<th>缴费期限</th>
		<th>加建面积</th>
		<th>单价(m)</th>
	</c:if>

	<!-- 4 按使用量 -->		
	<c:if test="${feesMode == 4}">			
		<th>表编号</th>
		<th>上次日期</th>	
		<th>缴费期限</th>	
		<th>上次读数</th>
		<th>本次读数</th>
		<th>本次用量</th>
		<th>单价	</th>
	</c:if>
	
	<!-- 5  按通话时长 -->		
	<c:if test="${feesMode == 5}">			
		<th>电话号码</th>
		<th>上次日期</th>		
		<th>缴费期限</th>
		<th>本次话费</th>
		<th>单价	(分钟)</th>
	</c:if>
	
	
	<!-- 6 按房屋面积的90% -->		
	<c:if test="${feesMode == 6}">			
		<th>上次日期</th>	
		<th>缴费期限</th>
		<th>建筑面积</th>
		<th>取暖面积</th>
		<th>单价(m)</th>
	</c:if>
	
		<th>应收</th>
		<th>公摊量</th>
		<th>公摊金额</th>
		<th>总应收</th>
		
		<th>操作</th>
		</tr></thead>
		<tbody>

		<c:forEach items="${page.list}" var="device">
			<tr>
			    <td style="display:none"><a href="#" id="id"  data-type="text" >${device.id}</a></td>
				<td style="display:none"><a href="#" id="lastNum" data-url="${ctx}/pms/deviceDetail/savelastnum/" data-type="text" data-pk="${device.id}" data-title="输入本次读数">${device.lastNum}</a></td>
			    <td>${device.house.fullName}</td>
			    <td>${device.house.owner.name}</td>
				<td>${device.fees.name}</td>
				
				
			    <!-- 1 按住户 -->
				<c:if test="${device.fees.feesMode == 1}">			
					<td><fmt:formatDate value="${device.lastDate}" pattern="yyyy-MM-dd"/></td>
					<td><fmt:formatDate value="${device.paymentDate}" pattern="yyyy-MM-dd"/></td>
					<td>${device.usageAmount}</td>
					<td>${device.fees.unitPrice}</td>		
				</c:if> 
				
				<!-- 2 按房屋面积 -->		
				<c:if test="${device.fees.feesMode == 2}">		
					<td><fmt:formatDate value="${device.lastDate}" pattern="yyyy-MM-dd"/></td>
					<td><fmt:formatDate value="${device.paymentDate}" pattern="yyyy-MM-dd"/></td>
					<td>${device.house.buildArea}</td>
					<td>${device.fees.unitPrice}</td>		
				</c:if>
			
				<!-- 3  按加建面积 -->		
				<c:if test="${device.fees.feesMode == 3}">		
					<td><fmt:formatDate value="${device.lastDate}" pattern="yyyy-MM-dd"/></td>
					<td><fmt:formatDate value="${device.paymentDate}" pattern="yyyy-MM-dd"/></td>	
					<td>${device.house.buildArea}</td>
					<td>${device.fees.unitPrice}</td>	
				</c:if>
			
				<!-- 4 按使用量 -->		
				<c:if test="${device.fees.feesMode == 4}">			
					<td>${device.code}</td>
					<td><fmt:formatDate value="${device.lastDate}" pattern="yyyy-MM-dd"/></td>
					<td><fmt:formatDate value="${device.paymentDate}" pattern="yyyy-MM-dd"/></td>
					<td>${device.firstNum}</td>
					<td>${device.lastNum}</td>
					<td>${device.usageAmount}</td>
					<td>${device.fees.unitPrice}</td>		
				</c:if>			
				

	
				<!-- 5 按通话时长 -->		
				<c:if test="${device.fees.feesMode == 5}">			
					<td>${device.code}</td>
					<td><fmt:formatDate value="${device.lastDate}" pattern="yyyy-MM-dd"/></td>
					<td><fmt:formatDate value="${device.paymentDate}" pattern="yyyy-MM-dd"/></td>
					<td>${device.usageAmount}</td>
					<td>${device.fees.unitPrice}</td>		
				</c:if>	
				
				
				<!-- 6 按房屋面积的90% -->		
				<c:if test="${device.fees.feesMode == 6}">		
					<td><fmt:formatDate value="${device.lastDate}" pattern="yyyy-MM-dd"/></td>
					<td><fmt:formatDate value="${device.paymentDate}" pattern="yyyy-MM-dd"/></td>
					<td>${device.house.buildArea}</td>
					<td>${device.usageAmount}</td>
					<td>${device.fees.unitPrice}</td>		
				</c:if>			
				
		
				<td>${device.payMoney}</td>	 
				<td>${device.poolUsageAmount}</td>
				<td>${device.poolPayMoney}</td>
				<td>${device.sumPayMoney}</td>	
				
				<shiro:hasPermission name="pms:device:edit"></shiro:hasPermission>
				<td>
    				<a id="Btndevice" href="#" value="${device.id}" onclick="gett(${device.id})">修改</a>
					<!-- a href="${ctx}/pms/payemtDetail/delete?id=${device.id}" onclick="return confirmx('确认要删除该信息吗？', this.href)">删除</a -->
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
