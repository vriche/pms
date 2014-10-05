<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>台帐信息管理</title>
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
		<li class="active"><a href="${ctx}/pms/paymentBefor/">台帐信息列表</a></li>
		<shiro:hasPermission name="pms:paymentBefor:edit"></shiro:hasPermission><li><a href="${ctx}/pms/paymentBefor/form">台帐信息添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="paymentBefor" action="${ctx}/pms/paymentBefor/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>名称</th>
		<th>生成日期</th>
		<th>应付金额</th>
		<th>费用项目</th>		
		<shiro:hasPermission name="pms:paymentBefor:edit"></shiro:hasPermission><th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="paymentBefor">
			<tr>
				<td><a href="${ctx}/pms/paymentBefor/form?id=${paymentBefor.id}">${paymentBefor.name}</a></td>
				<td>${paymentBefor.receDate}</td>
				<td>${paymentBefor.costMoney}</td>
				<td>${paymentBefor.user}</td>
				<td>${paymentBefor.fees.name}</td>
				<shiro:hasPermission name="pms:paymentBefor:edit"></shiro:hasPermission><td>
    				<a href="${ctx}/pms/paymentBefor/form?id=${paymentBefor.id}">修改</a>
					<a href="${ctx}/pms/paymentBefor/delete?id=${paymentBefor.id}" onclick="return confirmx('确认要删除该单元信息吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
