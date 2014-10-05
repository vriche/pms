<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>小区信息</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#name").focus();
			
			$("#btn_remove").click(
				function (e) {  
				
				  var choice=confirm("您确认要删除吗？", function(r) { alert(1);},null);
				  if(choice){
				   	   var url = "${ctx}/pms/community/delete?id="+$("#id")[0].value;
				   	   location.href = url;
				   }
	   			 }
   			 );  
   			 
   		
    
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
		<li><a href="${ctx}/pms/community/">小区信息列表</a></li>
		<li class="active"><a href="${ctx}/pms/community/form?id=${community.id}">小区信息<shiro:hasPermission name="pms:community:edit">${not empty community.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="pms:community:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="community" action="${ctx}/pms/community/save" method="post" class="form-horizontal">
		<form:hidden  path="id"/>
		<tags:message content="${message}"/>
		
	
		
		<div class="control-group">
			<label class="control-label">物业公司:</label>
			<div class="controls">
				<form:select id="proCompany.id" name="proCompany.id" path="proCompany.id"  class="text medium;required">
					<form:option value="" label="选择物业"/>
					<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
			</div>
		</div>	
		

				
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">编号:</label>
			<div class="controls">
				<form:input path="code" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>		
		
		<div class="control-group">
			<label class="control-label">占地面积:</label>
			<div class="controls">
				<form:input path="totalArea" htmlEscape="false" maxlength="10" class="number"/>
			</div>
		</div>		
		
		<div class="control-group">
			<label class="control-label">建筑面积:</label>
			<div class="controls">
				<form:input path="buildArea" htmlEscape="false" maxlength="50" class="number"/>
			</div>
		</div>		
			
		<div class="control-group">
			<label class="control-label">绿地面积:</label>
			<div class="controls">
				<form:input path="greenArea" htmlEscape="false" maxlength="50" class="number"/>
			</div>
		</div>	
		
		<div class="control-group">
			<label class="control-label">道路面积:</label>
			<div class="controls">
				<form:input path="roadArea" htmlEscape="false" maxlength="50" class="number"/>
			</div>
		</div>		
		
		
		<div class="control-group">
			<label class="control-label">楼宇数量:</label>
			<div class="controls">
				<form:input path="buildCount" htmlEscape="false" maxlength="50" class="digits"/>
			</div>
		</div>		
		


		<div class="control-group">
			<label class="control-label">地址:</label>
			<div class="controls">
				<form:input path="address" htmlEscape="false" maxlength="50" />
			</div>
		</div>		


		


		<div class="form-actions">
			<shiro:hasPermission name="pms:community:edit"></shiro:hasPermission>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btn_remove" class="btn" id="btn_remove" type="button" value="删除" />&nbsp;
			
			&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		
	
		
	</form:form>
</body>
</html>
