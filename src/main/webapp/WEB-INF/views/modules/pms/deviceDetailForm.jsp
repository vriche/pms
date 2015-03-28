<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/include/dialog.jsp" %>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	
	<script type="text/javascript">
	
	
	
		$(document).ready(function() {
		
		  	function selectDate(){WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});};
			$("#firstDate").click(function (e) {selectDate();});		
			$("#lastDate").click(function (e) {selectDate();});	
			$("#paymentDate").click(function (e) {selectDate();});	
			
			
			$("#btnSubmit").click(function (e) {
			    var action = "${ctx}/pms/deviceDetail/save?a="+ $("#proCompanyId").val();
			    $("#inputForm").attr("action",action);
				$("#inputForm").submit();
			});	
			
			$("#btnRemove").click(function (e) {
				top.$.jBox.confirm("您确定要删除该信息吗？","系统提示",function(v,h,f){
					if(v=="ok"){
					    var action = "${ctx}/pms/deviceDetail/delete?id=${deviceDetail.id}";
					    $("#inputForm").attr("action",action);
						$("#inputForm").submit();			
					}
				});
			});		
			
			
			
			$("#firstNum").keyup(function (e) { resetNum();});	
			$("#lastNum").keyup(function (e) {resetNum();});	
			
	
				

			
			
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
		
		function resetNum(){
		    var firstNum = $("#firstNum").val();
		    var lastNum = $("#lastNum").val();
		    var usageAmount = lastNum - firstNum;
			$("#usageAmount").val(usageAmount);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/pms/deviceDetail/">单元信息列表</a></li> <shiro:hasPermission name="pms:device:edit"></shiro:hasPermission>
		<li class="active"><a href="${ctx}/pms/deviceDetail/form?id=${device.id}">单元信息${not empty device.id?'修改':'添加'}<shiro:lacksPermission name="pms:device:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="deviceDetail" action="#" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="parent.id"/>
		<form:hidden id="proCompanyId" path="device.fees.company.id"/>
	
		
	 
		<tags:message content="${message}"/>
		
		
		<div class="control-group">
			<label class="control-label">设备类型:</label>
			<div class="controls">
				<form:select path="device.type">
					<form:options items="${fns:getDictList('pms_device_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">编号:</label>
			<div class="controls">
				<form:input path="device.code" htmlEscape="false" maxlength="200" />
			</div>
		</div>	
					

		
		<div class="control-group">
			<label class="control-label">收费项目:</label>
			<div class="controls">
					<form:select id="feesId" name="feesId" path="device.fees.id"  class="required">
								<form:option value="" label=""/>
								<form:options items="${feesList}" itemLabel="name" itemValue="id" htmlEscape="false" />
					</form:select>
			</div>
		</div>	
		

		

		
		<div class="control-group">
			<label class="control-label">上次读数:</label>
			<div class="controls">
			    <form:input path="firstNum" htmlEscape="false" maxlength="200"/>
				
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">本次读数:</label>
			<div class="controls">
				 <form:input path="lastNum" htmlEscape="false" maxlength="200"/>
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
			<label class="control-label">本次用量:</label>
			<div class="controls">
				 <form:input path="usageAmount" htmlEscape="false" maxlength="200"/>
			</div>
		</div>	
		
		<c:if test="${deviceDetail.device.type  == 2}">
		
		  <c:if test="${deviceDetail.device.pool  == 1}">
		   <div class="control-group">
				<label class="control-label">本次公摊量:</label>
				<div class="controls">
					 <form:input path="poolUsageAmount" htmlEscape="false" maxlength="200"/>
				</div>
			</div>		
			
			</c:if>
		</c:if> 

		<div class="control-group">
			<label class="control-label">单价:</label>
			<div class="controls">
				 <form:input path="unitPrice" htmlEscape="false" maxlength="200"/>
			</div>
		</div>		
		
		<c:if test="${deviceDetail.device.type  != 1}">

			<div class="control-group">
				<label class="control-label">交费限期:</label>
				<div class="controls">
				<input id="paymentDate" name="paymentDate" type="text" readonly="readonly" maxlength="20" 
								value="<fmt:formatDate value="${device.paymentDate}" pattern="yyyy-MM-dd"/>" class=" Wdate"/>
				</div>
			</div>		
				
		
		</c:if> 
		

		
		<div class="form-actions"><shiro:hasPermission name="pms:deviceDetail:edit"></shiro:hasPermission>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnRemove" class="btn btn-primary" type="button" value="删 除"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
