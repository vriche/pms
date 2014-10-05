<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			function selectDate(){WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});};
			$("#receDate").click(function (e) {selectDate();});
			
			
			$("#recMoneyBak").val($("#recMoney").val());
			
			$("#recMoney").keyup(function(){
			      var type =  $("#type").val();
			      if(type == 2){
	                  var recMoneyBak =   $("#recMoneyBak").val()*1;
	                  var recMoney =   $("#recMoney").val()*1;
	                  if(recMoney > recMoneyBak){
	                     alert('收款金额不能小于' +recMoneyBak);
	                     $("#recMoney").val(recMoneyBak);
	                  }
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

	
	<form:form id="inputForm" modelAttribute="paymentAfter" action="${ctx}/pms/paymentAfter/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="house.id"/>
		<form:hidden path="type"/>
		<form:hidden path="costMoney" value="0"/>
		<form:hidden path="payemtDetailId"/>
		<form:hidden path="recMoneyBak" value="0"/>
		<form:hidden path="feedName"/>

		<tags:message content="${message}"/>
		
		<div class="control-group">
			<label class="control-label">收款方式:</label>
			<div class="controls">
				<form:select path="payType">
					<form:options items="${fns:getDictList('pms_pay_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>				
			</div>
		</div>		
			
		
		

		<div class="control-group">
			<label class="control-label">收款单号:</label>
			<div class="controls">
				<form:input path="feeCode" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">发票号:</label>
			<div class="controls">
				<form:input path="certCode" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>	
		
		
		
		<div class="control-group">
			<label class="control-label">收款日期:</label>
			<div class="controls">
			<input id="receDate" name="receDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${paymentAfter.receDate}" pattern="yyyy-MM-dd"/>" class=" Wdate"/>
			</div>			

		</div>	
		

		
		<div class="control-group">
			<label class="control-label">收款金额:</label>
			<div class="controls">
				<form:input path="recMoney" htmlEscape="false" maxlength="200" />
			</div>
		</div>			
		

		
		
		
		
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="2" maxlength="200" class="input-xxlarge"/>
			</div>
		</div>

	</form:form>
</body>
</html>
