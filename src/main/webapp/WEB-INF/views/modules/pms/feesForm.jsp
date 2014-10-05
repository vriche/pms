<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收费项目管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			
			$("#list").click(function(){
			    location.href = "${ctx}/pms/fees/list?company.id="+$("#proCompanyId").val() +"&feesType="+$("#feesType").val() ;
			})
			
			$("#btnCancel").click(function(){
			    location.href = "${ctx}/pms/fees/list?company.id="+$("#proCompanyId").val() +"&feesType="+$("#feesType").val() ;
			})		
			
			
			
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
		<li><a id="list" href="#">收费项目列表</a></li>
		<li class="active"><a href="${ctx}/pms/fees/form?id=${fees.id}">收费项目<shiro:hasPermission name="pms:fees:edit">${not empty fees.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="pms:fees:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="fees" action="${ctx}/pms/fees/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden id="proCompanyId" path="company.id" class="required"/>
		<form:hidden id="feesType" path="feesType" class="required"/>
		
		<tags:message content="${message}"/>
		
		



		<div class="control-group">
			<label class="control-label">费项编号:</label>
			<div class="controls">
				<form:input path="code" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>

		
		<div class="control-group">
			<label class="control-label">收费方式:</label>
			<div class="controls">
				<form:select path="feesMode">
					<form:options items="${fns:getDictList('pms_fees_mode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		
	
		
		<div class="control-group">
			<label class="control-label">单位价格:</label>
			<div class="controls">
				<form:input path="unitPrice" htmlEscape="false" maxlength="50" class="required number"/>*元 
		    </div>
		</div>	
		
		
		
		<div class="control-group">
				<label class="control-label">收费周期</label>
				<div class="controls">
				<form:select path="speMonth">
					<form:options items="${fns:getDictList('pms_spe_month')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>(月)
			</div>
		</div>	
	
		
		<div class="control-group">
			<label class="control-label">排序:</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="50" class="required digits"/>
		</div>
		
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="pms:fees:edit"></shiro:hasPermission><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" />
		</div>
	</form:form>
</body>
</html>
