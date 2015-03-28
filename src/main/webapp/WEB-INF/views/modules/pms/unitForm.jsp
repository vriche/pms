<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#buildings.id").focus();

		
		   var url = "${ctx}/pms/community/communityjson?model=unit";
		   $("#communityId").remoteChained("#proCompanyId", url);
		   var url2 = "${ctx}/pms/buildings/buildingsjson?model=unit";
		   $("#buildingsId").remoteChained("#communityId", url2);
	
			
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
		<li><a href="${ctx}/pms/unit/">单元信息列表</a></li>
		<li class="active"><a href="${ctx}/pms/unit/form?id=${unit.id}">单元信息<shiro:hasPermission name="pms:unit:edit">${not empty unit.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="pms:unit:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="unit" action="${ctx}/pms/unit/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		
		
		
		<div class="control-group">
			<label class="control-label">物业公司:</label>
			<div class="controls">
					<form:select id="proCompanyId" name="proCompanyId" path="buildings.community.proCompany.id"  class="text medium;required">
								<form:option value="" label=""/>
								<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
					</form:select>
			</div>
		</div>		
		
		
		<div class="control-group">
			<label class="control-label">小区:</label>
			<div class="controls">
				<form:select id="communityId" name="communityId" path="buildings.community.id"  class="text medium;required">
					<form:options items="${communityList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
			</div>
		</div>	

		<div class="control-group">
			<label class="control-label">楼宇:</label>
			<div class="controls">
				<form:select id="buildingsId" name="buildingsId" path="buildings.id"  class="text medium;required">
					<form:options items="${buildingsList}" itemLabel="name" itemValue="id" htmlEscape="false" />
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
			<label class="control-label">编码:</label>
			<div class="controls">
				<form:input path="code" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">开始楼层:</label>
			<div class="controls">
				<form:input path="startFloor" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>			
		
		<div class="control-group">
			<label class="control-label">结束楼层:</label>
			<div class="controls">
				<form:input path="endFloor" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
	
	
		<div class="control-group">
			<label class="control-label">开始房号:</label>
			<div class="controls">
				<form:input path="startRoom" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>			
		
		<div class="control-group">
			<label class="control-label">结束房号:</label>
			<div class="controls">
				<form:input path="endRoom" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
	
		<div class="control-group">
		<label class="control-label">顺序:</label>
		<div class="controls">
			<form:input path="sort" htmlEscape="false" rows="4" maxlength="200" class="text medium;"/>
		</div>
		</div>	

		<div class="form-actions">
			<shiro:hasPermission name="pms:unit:edit"></shiro:hasPermission><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
