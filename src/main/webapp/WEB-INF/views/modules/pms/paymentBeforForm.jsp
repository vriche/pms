<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	
	(function($){
		$.getUrlParam = function(name)
		{
			var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if (r!=null) return unescape(r[2]); return null;
		}
	})(jQuery);
	
	 var id;
	 var type;
		$(document).ready(function() {
			$("#feeCode").focus();
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
			
			
			id = $.getUrlParam('id');
			type = $.getUrlParam('type');
			if(id ==''){
				
				 $("#tabs1").hide();
				 $("#btn_remove").hide();
				 
				 $("#btnSubmit").hide();
				 $("#btn_remove").hide();
				 $("#btnCancel").hide();

					
			}
			
			
			$("#btn_remove").click(
					function (e) {  
					  var choice=confirm("您确认要删除吗？", function(r) { alert(1);},null);
					  if(choice){
					   	   var url = "${ctx}/pms/paymentBefor/delete?id="+$("#id")[0].value;
//					   	   alert(url)
					   	   location.href = url;
					   }
		   			 }
	   			 );   			
		});
	</script>
</head>
<body>

	
	<ul class="nav nav-tabs">
	<li id="tabs1" class="active"><a  href="#">收款单据</a></li>
	</ul>	

	
	<form:form id="inputForm" name="inputForm" modelAttribute="paymentBefor" action="${ctx}/pms/paymentBefor/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="house.id"/>
		<form:hidden path="company.id"/>
		<form:hidden path="user.id"/>
		<form:hidden path="type"/>
		<form:hidden path="payFrom" />
		
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
			<form:input path="feeCode" htmlEscape="false" maxlength="200" class="required" readonly="true"/>
		</div>
	</div>
	
	
	<div class="control-group">
		<label class="control-label">发票号:</label>
		<div class="controls">
			<form:input path="certCode" htmlEscape="false" maxlength="200"/>
		</div>
	</div>	
	
	
	
	<div class="control-group">

		<label class="control-label">收款日期:</label>
		<div class="controls">
		<input id="receDate" name="receDate" type="text" readonly="readonly" maxlength="20" onClick="WdatePicker({})" 
						value="<fmt:formatDate value="${paymentBefor.receDate}" pattern="yyyy-MM-dd"/>"  class=" Wdate"/>					
		</div>	

	</div>	
	
	
	
	<div class="control-group">
		<label class="control-label">收款金额:</label>
		<div class="controls">
			<form:input path="recMoney" htmlEscape="false" maxlength="200" class="number"/>
		</div>
	</div>			
	

	
	
	
	
	<div class="control-group">
		<label class="control-label">备注:</label>
		<div class="controls">
			<form:textarea path="remarks" htmlEscape="false" rows="2" maxlength="200" class="input-xxlarge"/>
		</div>
	</div>
	
		<div class="form-actions"><shiro:hasPermission name="pms:paymentBefor:edit"></shiro:hasPermission>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btn_remove" class="btn btn-primary" id="btn_remove" type="button" value="删除" />&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
