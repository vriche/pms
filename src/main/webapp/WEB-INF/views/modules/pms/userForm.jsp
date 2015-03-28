<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#loginName").focus();
			$("#inputForm").validate({
				rules: {
					loginName: {remote: "${ctx}/pms/user/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')}
				},
				messages: {
					loginName: {remote: "用户登录名已存在"},
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
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
		<li><a href="${ctx}/pms/user/">用户列表</a></li>
		<li class="active"><a href="${ctx}/pms/user/form?id=${user.id}">用户<shiro:hasPermission name="pms:user:edit">${not empty user.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission></a></li>

	   <!-- c:if test="${not empty user.id}" -->
	    <!-- li><a href="${ctx}/pms/fees/assignNormal/?userId=${user.id}">费用分布</a></li -->
	    <!-- li><a href="${ctx}/pms/fees/assignNormal/?userId=${user.id}">应缴费用</a></li -->
	   <!--/c:if -->  	
	   
	 	
		
	</ul><br/>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/pms/user/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>

		
		<input type="hidden"  id="deviceId"  name="deviceId"/>
		
		<tags:message content="${message}"/>
		
		
		<div class="control-group">
      <label class="control-label">物业单位:</label>
      <div class="controls">
                <tags:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
          title="部门" url="/sys/office/treeData?type=2" cssClass="required"/>
      </div>
    </div>
    
		<div class="control-group">
			<label class="control-label">归属公司:</label>
			<div class="controls">
                <tags:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}"
					title="公司" url="/sys/office/treeData?type=1" cssClass="required"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">住户编码:</label>
			<div class="controls">
				<input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
				<form:input path="loginName" htmlEscape="false" maxlength="50" class="required userName"/>
			</div>
		</div>
		
		  <div class="control-group">
      <label class="control-label">查询编码:</label>
      <div class="controls">
        <form:input path="no" htmlEscape="false" maxlength="50" class="required"/>
      </div>
    </div>

		<div class="control-group">
			<label class="control-label">姓名:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">证件号码:</label>
			<div class="controls">
				<form:input path="paperworkCode" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>		
		<div class="control-group">
			<label class="control-label">密码:</label>
			<div class="controls">
				<input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="${empty user.id?'required':''}"/>
				<c:if test="${not empty user.id}"><span class="help-inline">若不修改密码，请留空。</span></c:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">确认密码:</label>
			<div class="controls">
				<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" equalTo="#newPassword"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">电话:</label>
			<div class="controls">
				<form:input path="phone" htmlEscape="false" maxlength="100"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机1:</label>
			<div class="controls">
				<form:input path="mobile" htmlEscape="false" maxlength="100"/>
			</div>
		</div>
    <div class="control-group">
      <label class="control-label">手机2:</label>
      <div class="controls">
        <form:input path="mobile2" htmlEscape="false" maxlength="100"/>
      </div>
    </div>		
		<div class="control-group">
			<label class="control-label">邮箱:</label>
			<div class="controls">
				<form:input path="email" htmlEscape="false" maxlength="100" class="email"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">用户类型:</label>
			<div class="controls">
				<form:select path="userType">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户角色:</label>
			<div class="controls">
				<form:checkboxes path="roleIdList" items="${allRoles}" itemLabel="name" itemValue="id" htmlEscape="false" class="required"/>
			</div>
		</div>
		
		
    <div class="control-group">
                <label class="control-label">选择房产：</label>
                <div class="controls">
                    <tags:treeselect id="house" name="house" value="" labelName="${name}" labelValue="${id}"  proCompany="proCompanyId"  deviceId="deviceId" 
                      title="房间" url="/pms/buildings/treeData?Level=4"  nodesLevel="4" nameLevel="4"  notAllowSelectParent="true" cssClass="" allowClear="true"/>
                </div>
    </div>  		
		
		  <div class="control-group">
		    <label class="control-label">已拥有房产:</label>
		    <div class="controls">
            <table id="contentTable" class="table table-striped table-bordered table-condensed">
              <thead><tr>
              <th>编号</th>
              <th>名称</th>
              <!-- th>费用项目</th -->
              <!-- th>操作</th -->
              <tbody>
              <c:forEach items="${houseList}" var="house">
                <tr>
                  <td>${house.code}</td>
                  <td><a href="${ctx}/pms/house/form?id=${house.id}">${house.fullName}</a></td>
                  
                  
                   <!-- td><table><tr><c:forEach items="${house.deviceList}" var="fees"><td>${fees.name}</td> </c:forEach></tr> </table> </td -->
   
                </tr>
              </c:forEach>
              </tbody>
            </table>  		
         </div>
		  </div>
		

		
		 <!-- div class="control-group">
			<label class="control-label">费用:</label>
			<div class="controls">
				<form:checkboxes path="" items="${allFees}" itemLabel="name" itemValue="id" htmlEscape="false" class="required"/>
			</div>		
		</div -->
		
		
		
		<c:if test="${not empty user.id}">
			<div class="control-group">
				<label class="control-label">创建时间:</label>
				<div class="controls">
					<label class="lbl"><fmt:formatDate value="${user.createDate}" type="both" dateStyle="full"/></label>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">最后登陆:</label>
				<div class="controls">
					<label class="lbl">IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/></label>
				</div>
			</div>
		</c:if>
		<div class="form-actions">
			<shiro:hasPermission name="pms:user:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>