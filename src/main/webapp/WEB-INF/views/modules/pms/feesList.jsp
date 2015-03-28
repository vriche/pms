<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收费项目管理</title>
	<meta name="decorator" content="default"/>
	
	<script type="text/javascript">
		$(document).ready(function() {
		
			
		
			$("#proCompanyId").change(function(){
				$("#searchForm").submit();
			});		
			  	
			$("#feesType").change(function(){
				$("#searchForm").submit();
			});	
			
			
			if($("#proCompanyId").val() != ""){
				//$("#btnSubmit").trigger("click");
				//$("#searchForm").submit();
			}
			
			//alert($("#proCompanyId").val());
			
			$("#add").click(function(){
			    location.href = "${ctx}/pms/fees/form?company.id="+$("#proCompanyId").val() +"&feesType="+$("#feesType").val() ;
			})
			
			
			$("#btnInit").click(function(){
				$("#searchForm").attr("action","${ctx}/pms/fees/initFees?company.id="+$("#proCompanyId").val()+"&feesType="+$("#feesType").val());
				$("#searchForm").attr("enctype","application/x-www-form-urlencoded");
				$("#searchForm").attr("onsubmit","loading('正在初始化，请稍等...');");
				$("#searchForm").submit();
			});
			

					
			
			
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
		<shiro:hasPermission name="pms:fees:edit"></shiro:hasPermission><li><a id="add" href="#">收费项目添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="fees" action="${ctx}/pms/fees/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

		<label class="control-label">物业公司:</label>
		<form:select id="proCompanyId" name="proCompanyId" path="company.id"  style="width:250px;">
					<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
	    <label>收费类型:</label>
		<form:select  id="feesType" name="feesType" path="feesType"  style="width:100px;" >
					<form:options items="${fns:getDictList('pms_fees_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		</form:select>
			
		<!-- 
		<label>费项编号 ：</label><form:input id="code" name="code" path="code" htmlEscape="false" maxlength="50" class="input-small"/>
		  -->
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="hidden" value="查&nbsp;&nbsp;&nbsp;询"/>
		&nbsp;<input id="btnInit" class="btn btn-primary" type="button" value="初始化"/>
		<!-- 
		<form:radiobuttons  id="feesMode_option" onclick="$('#searchForm').submit();" path="feesMode" items="${fns:getDictList('pms_fees_mode')}" itemLabel="label" itemValue="value" htmlEscape="false" />
          -->
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th>费项编号</th>
			<th>名称</th>
			<!-- th>收费类型</th -->
			<th>收费方式</th>
			<th>单位价格</th>
			<th>收费周期(月)</th>
			<th>顺序</th>
			<th>操作</th>
			<shiro:hasPermission name="pms:fees:edit"></shiro:hasPermission>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="fees">
			<tr>
				
				<td>${fees.code}</td>
				<td><a href="${ctx}/pms/fees/form?id=${fees.id}">${fees.name}</a></td>
				<!-- td>${fns:getDictLabel(fees.feesType, 'pms_fees_type', ' ')}</td -->
				<td>${fns:getDictLabel(fees.feesMode, 'pms_fees_mode', ' ')}</td>
				<td>${fees.unitPrice}</td>
				<td>${fees.speMonth}</td>
			    <td>${fees.sort}</td>
				<shiro:hasPermission name="pms:fees:edit"></shiro:hasPermission><td>
    				<a href="${ctx}/pms/fees/form?id=${fees.id}">修改</a>
					<a href="${ctx}/pms/fees/delete?id=${fees.id}&feesType=${fees.feesType}&proCompanyId=${fees.company.id}" onclick="return confirmx('确认要删除该收费项目吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
