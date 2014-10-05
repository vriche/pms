<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>预付款信息管理</title>
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
		<li class="active"><a href="${ctx}/pms/paymentPre/">预付款信息列表</a></li><shiro:hasPermission name="pms:paymentPre:edit"></shiro:hasPermission>
		<li><a href="${ctx}/pms/paymentPre/form">预付款信息添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="paymentPre" action="${ctx}/pms/paymentPre/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>名称</th>
		<th>凭证号</th>
		<th>经手人</th>
		<th>金额 </th>
		<th>收款日期 </th>
		<th>备注</th><shiro:hasPermission name="pms:paymentPre:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="paymentPre">
			<tr>
				<td><a href="${ctx}/pms/paymentPre/form?id=${paymentPre.id}">${paymentPre.name}</a></td>
				<td>${paymentPre.name}</td>
				<td>${paymentPre.code}</td>
				<td>${paymentPre.user}</td>
				<td>${paymentPre.money}</td>
				<td>${paymentPre.receDate}</td>	
				<td>${paymentPre.remarks}</td>

				<shiro:hasPermission name="pms:paymentPre:edit"><td>
    				<a href="${ctx}/pms/paymentPre/form?id=${paymentPre.id}">修改</a>
					<a href="${ctx}/pms/paymentPre/delete?id=${paymentPre.id}" onclick="return confirmx('确认要删除该单元信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
