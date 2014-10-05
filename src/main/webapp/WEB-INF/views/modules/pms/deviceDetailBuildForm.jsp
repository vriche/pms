<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		
		  	function selectDate(){WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});};
			//$("#firstDate").click(function (e) {selectDate();});		
			//$("#lastDate").click(function (e) {selectDate();});	
			$("#paymentDate").click(function (e) {selectDate();});	
			
			$("#lastNum").change(function (e) {
			  //feesMode 1 按住户   2 按房屋面积    3 按加建面积  4 按使用量    5 按实际应收金额    6自定义
			    var feesMode = $("#feesMode").val();
			    var unitPrice =  $("#unitPrice").val();
			    
			    if(feesMode == 1){
			    	$("#payMoney").val(unitPrice);
			    }
			    
			    if(feesMode == 2){
			        var buildArea = $("#buildArea").val()
			        var payMoney = unitPrice*buildArea;
			    	$("#payMoney").val(payMoney);
			    }
			    
			    if(feesMode == 3){
			        var buildArea = $("#buildArea").val()
			        var payMoney = unitPrice*buildArea;
			    	$("#payMoney").val(payMoney);
			    }
			    
			    if(feesMode == 4){
					var lastNum = $("#lastNum").val();
					var firstNum = $("#firstNum").val();
					var num = lastNum*1 - firstNum*1;
					$("#usageAmount").val(num);
					var payMoney = num*unitPrice;
					$("#payMoney").val(payMoney);
				}
				
			});	
			
			
			
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<shiro:hasPermission name="pms:device:edit"></shiro:hasPermission>
		<li><a href="${ctx}/pms/deviceDetail/">设备信息列表</a></li>
		<li class="active"><a href="${ctx}/pms/deviceDetail/form?id=${device.id}">设备信息${not empty device.id?'修改':'添加'}<shiro:lacksPermission name="pms:device:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="device" action="${ctx}/pms/deviceDetail/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="parent.id"/>
		<form:hidden path="parentIds"/>
		
		<form:hidden path="fees.id"/>
		<form:hidden id="feesMode" path="fees.feesMode"/>

		
		<form:hidden path="house.id"/>
		<form:hidden path="house.unit.buildings.community.proCompany.id"/>
		<form:hidden path="house.unit.buildings.community.id"/>
		<form:hidden path="house.unit.buildings.id"/>
		<form:hidden path="house.unit.id"/>
		

		<tags:message content="${message}"/>
		
		
		<div class="control-group">
			<label class="control-label">设备类型:</label>
			<div class="controls">
				<form:select path="type"  disabled="true">
					<form:options items="${fns:getDictList('pms_device_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">编号:</label>
			<div class="controls">
				<form:input path="code" htmlEscape="false" maxlength="200" readonly="true" class="required"/>
			</div>
		</div>	
					

		
		<div class="control-group">
			<label class="control-label">收费项目:</label>
			<div class="controls">
					<form:select id="feesId" name="feesId" path="fees.id"  class="required" disabled="true">
								<form:option value="" label=""/>
								<form:options items="${feesList}" itemLabel="name" itemValue="id" htmlEscape="false" />
					</form:select>
			</div>
		</div>	
		
		
			<div class="control-group">
	                <label class="control-label">房 间：</label>
	                <div class="controls">
	                    <tags:treeselect id="house" name="house.id" value="${device.house.id}" labelName="${house.name}" labelValue="${device.house.name}"   disabled="true" 
	                      title="房间" url="/pms/buildings/treeData?model=4"  nodesLevel="4" nameLevel="4" />
	                </div>
	        </div>	
	        
		<div class="control-group">
			<label class="control-label">上次日期:</label>
			<div class="controls">
			<input id="firstDate" name="firstDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${device.firstDate}" pattern="yyyy-MM-dd"/>" class=" Wdate"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">本次日期:</label>
			<div class="controls">
				<input id="lastDate" name="lastDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${device.lastDate}" pattern="yyyy-MM-dd"/>" class=" Wdate"/>
			</div>
		</div>			
		
		<div class="control-group">
			<label class="control-label">单价:</label>
			<div class="controls">
   	 		<form:input id="unitPrice" path="fees.unitPrice" htmlEscape="false" maxlength="200" readonly="true" />
			</div>
		</div>	
		
<c:if test="${device.fees.feesMode == 2}">	


		<div class="control-group">
			<label class="control-label">建筑面积:</label>
			<div class="controls">
   	 		<form:input id="buildArea" path="house.buildArea" htmlEscape="false" maxlength="200" readonly="true" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">应付金额:</label>
			<div class="controls">
				 <form:input path="payMoney" htmlEscape="false" maxlength="200" value="${device.house.buildArea*device.fees.unitPrice}"  readonly="true"/>
			</div>
		</div>			
		
		
</c:if> 	
		
		
<c:if test="${device.fees.feesMode  == 4}">	


				
		<div class="control-group">
			<label class="control-label">上次读数:</label>
			<div class="controls">
			    <form:input path="firstNum" htmlEscape="false" readonly="true" maxlength="200"/>
				
			</div>
		</div>

				
		<div class="control-group">
			<label class="control-label">本次读数:</label>
			<div class="controls">
				 <form:input path="lastNum" htmlEscape="false" maxlength="200"/>
			</div>
		</div>		
		

		
		<div class="control-group">
			<label class="control-label">本次用量:</label>
			<div class="controls">
				 <form:input path="usageAmount" htmlEscape="false" maxlength="200"  readonly="true"/>
			</div>
		</div>	
		
		<div class="control-group">
			<label class="control-label">应付金额:</label>
			<div class="controls">
				 <form:input path="payMoney" htmlEscape="false" maxlength="200" readonly="true"/>
			</div>
		</div>			
		
</c:if> 				
		
		
			
		
				
		
		
		 <div class="control-group">
			<label class="control-label">交费限期:</label>
			<div class="controls">
			<input id="paymentDate" name="paymentDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${device.paymentDate}" pattern="yyyy-MM-dd"/>" class=" Wdate"/>
			</div>
		</div>			
		




		
		
		
		
	
	
		
		
		<div class="form-actions"><shiro:hasPermission name="pms:device:edit"></shiro:hasPermission>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
