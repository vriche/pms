<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
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
	<ul class="nav nav-tabs"><shiro:hasPermission name="pms:paymentAfter:edit"></shiro:hasPermission>
		<li class="active"><a href="${ctx}/pms/paymentAfter/">收款信息列表</a></li>
		<li><a href="${ctx}/pms/paymentAfter/form">付款信息添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="paymentAfter" action="${ctx}/pms/paymentAfter/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>收款单号 ：</label><form:input path="feeCode" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>收款单号</th>
		<th>发票号</th>
		<th>收款日期</th>
		<th>应付金额</th>
		<th>收款金额</th>
		<th>收款方式</th>
		<th>收款人</th>
		<shiro:hasPermission name="pms:paymentAfter:edit"></shiro:hasPermission>
		<th>操作</th>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="paymentAfter">
			<tr>
				<td><a href="${ctx}/pms/paymentAfter/form?id=${paymentAfter.id}">${paymentAfter.feeCode}</a></td>
				<td>${paymentAfter.certCode}</td>
				<td>${paymentAfter.receDate}</td>
				<td>${paymentAfter.costMoney}</td>
				<td>${paymentAfter.recMoney}</td>
				<td>${paymentAfter.payType}</td>
				<td>${paymentAfter.user.name}</td>
				<td>${paymentAfter.remarks}</td>
				<shiro:hasPermission name="pms:paymentAfter:edit"></shiro:hasPermission><td>
    				<a href="${ctx}/pms/paymentAfter/form?id=${paymentAfter.id}">修改</a>
					<a href="${ctx}/pms/paymentAfter/delete?id=${paymentAfter.id}" onclick="return confirmx('确认要删除该单元信息吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
