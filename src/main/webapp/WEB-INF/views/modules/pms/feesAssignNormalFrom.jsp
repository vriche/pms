<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收费项目管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/pms/fees/">收费项目列表</a></li>
		<shiro:hasPermission name="pms:fees:edit"><li><a href="${ctx}/pms/fees/form">收费项目添加</a></li></shiro:hasPermission>
	</ul>


	
	<form:form id="inputForm" modelAttribute="user" class="form-horizontal">
		<form:hidden path="id"/>
		<label class="control-label">用户名称:</label>
		<div class="controls"><form:input path="name" htmlEscape="false" maxlength="50" class="required"/></div>
	</form:form>


	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th>费项编号</th>
			<th>名称</th>
			<th>收费类型</th>
			<th>费用方式</th>
			<th>单位价格</th>
			<th>收费周期(月)</th>
			<th>归属公司</th>
			
			<shiro:hasPermission name="pms:fees:edit"><th>操作</th></shiro:hasPermission>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="fees">
			<tr>
				
				<td>${fees.code}</td>
				<td>${fees.name}</td>
				<td>${fns:getDictLabel(fees.feesType, 'pms_fees_type', ' ')}</td>
				<td>${fns:getDictLabel(fees.feesMode, 'pms_fees_mode', ' ')}</td>
				<td>${fees.unitPrice}</td>
				<td>${fees.speMonth}</td>
				<td>${fees.company.name}</td>
				
				<shiro:hasPermission name="pms:fees:edit"><td>
					<a href="${ctx}/pms/fees/deleteFeesFromUser?id=${fees.id}" onclick="return confirmx('确认要删除该收费项目吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>

	
	<form:form id="searchForm" modelAttribute="fees" action="${ctx}/pms/fees/" method="post" class="breadcrumb form-search">
	
		<shiro:hasPermission name="pms:fees:edit">
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
		</shiro:hasPermission>
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>

	</form:form>	
	
</body>
</html>
