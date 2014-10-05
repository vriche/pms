<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>楼宇信息</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	

	
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出小区数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/pms/buildings/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
			$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			});
			

		   var url = "${ctx}/pms/community/communityjson?model=buildings";
		   $("#communityId").remoteChained("#proCompanyId", url);
		   
		   
		   	$("#proCompanyId").change(function(){
			    $("#searchForm").submit();
			})
			
			$("#communityId").change(function(){
			    $("#searchForm").submit();
			})


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

	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/pms/buildings/import" method="post" enctype="multipart/form-data"
			style="padding-left:20px;text-align:center;" class="form-search" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<!-- a href="${ctx}/pms/buildings/import/template">下载模板</a -->
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/pms/buildings/">楼宇列表</a></li><shiro:hasPermission name="pms:buildings:edit"></shiro:hasPermission>
		<li><a href="${ctx}/pms/buildings/form">楼宇添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="buildings" action="${ctx}/pms/buildings/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		
		<label class="control-label">物业公司:</label>
		<form:select id="proCompanyId" name="proCompanyId" path="community.proCompany.id"  class="text medium;required">
					<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
	
		<label>小区:</label>
		<form:select id="communityId" name="communityId" path="community.id"  class="input-small">
					<form:options items="${communityList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>

		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		&nbsp;<input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
		&nbsp;<input id="btnImport" class="btn btn-primary" type="button" value="导入"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>小区</th>
		<th>编号</th>
		<th>名称</th>
		<th>建筑面积</th>
		<th>使用面积</th>
		<th>单元数量</th>
		<th>封顶日期</th>
		<th>竣工日期 </th>
		<th>预售许可证 </th>
		<th>建筑许可证</th>
		<th>备注</th>
		<shiro:hasPermission name="pms:buildings:edit"></shiro:hasPermission>
		<th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="buildings">
			<tr>
			    <td>${buildings.community.name}</td>
			    <td>${buildings.code}</td>
				<td><a href="${ctx}/pms/buildings/form?id=${buildings.id}">${buildings.name}</a></td>
				<td>${buildings.buildArea}</td>
				<td>${buildings.useArea}</td>
				<td>${buildings.floorCount}</td>
				<td>${buildings.capDate}</td>
				<td>${buildings.completionDate}</td>
				<td>${buildings.presalePermit}</td>
				<td>${buildings.buildPermit}</td>
				<td>${buildings.remarks}</td>

				<td>
    				<a href="${ctx}/pms/unit/list?buildings.community.proCompany.id=${buildings.community.proCompany.id}&buildings.community.id=${buildings.community.id}&buildings.id=${buildings.id}">查看单元</a>
				</td>
				
				
				<shiro:hasPermission name="pms:buildings:edit"></shiro:hasPermission>
				<!-- td>
    				<a href="${ctx}/pms/buildings/form?id=${buildings.id}">修改</a>
					<a href="${ctx}/pms/buildings/delete?id=${buildings.id}" onclick="return confirmx('确认要否删除？', this.href)">删除</a>
				</td -->
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
