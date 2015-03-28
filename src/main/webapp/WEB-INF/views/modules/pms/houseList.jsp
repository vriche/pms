<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收费项目管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	
	<script type="text/javascript">
		$(document).ready(function() {
		
		   var url = "${ctx}/pms/community/communityjson?model=house";
		   $("#communityId").remoteChained("#proCompanyId", url);
		   var url2 = "${ctx}/pms/buildings/buildingsjson?model=house";
		   $("#buildingsId").remoteChained("#communityId", url2);
		   var url2 = "${ctx}/pms/unit/unitjson?model=house";
		   $("#unitId").remoteChained("#buildingsId", url2);
		   
		   
		   	$("#btnImport").click(function(){
				var proCompanyId = document.getElementById("proCompanyId").value;
				document.getElementById("importForm").cmpid.value = proCompanyId;
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			});	
		   	
		   	
		   	


			$("#tabs2").click(function(){
				var proCompanyId = document.getElementById("proCompanyId").value;
				location.href = "${ctx}/pms/house/form?type=1&unit.buildings.community.proCompany.id="+proCompanyId;
			})	
		   	
		   	
		   	
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出房屋数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/pms/house/export");
						$("#searchForm").submit();
						$("#searchForm").attr("action","${ctx}/pms/house/list");
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});

		});
		

		function chkForm(){
			
			var proCompanyId = document.getElementById("proCompanyId").value;
			$("#cmpid").val(proCompanyId);
			$("#importForm").attr("action","${ctx}/pms/house/import?cmpid="+proCompanyId);
			return true;
//			$("#importForm").submit();
		}

//		function import(){
//			$("#importForm").attr("action","${ctx}/pms/house/import");
//			var proCompanyId = document.getElementById("proCompanyId").value;
//			$("#cmpid").val(proCompanyId);
//			$("#importForm").submit();
//        }
		
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
		<form id="importForm"   action="${ctx}/pms/house/import" method="post" enctype="multipart/form-data"
			style="padding-left:20px;text-align:center;" class="form-search" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="file" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="cmpid" name="cmpid"  value="0" type="hidden"/><br/><br/>　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/pms/house/import/template">下载模板</a>
		</form>
	</div>
		
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/pms/house/">房屋信息列表</a></li>
		<shiro:hasPermission name="pms:house:edit"></shiro:hasPermission>
		<li><a  id="tabs2" href="#">房屋信息添加</a></li>
	</ul>
	
	<form:form id="searchForm" modelAttribute="house" action="${ctx}/pms/house/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		

		
		<label class="control-label">物业公司:</label>
		<form:select id="proCompanyId" name="proCompanyId" path="unit.buildings.community.proCompany.id" style="width:160px"  class="text medium;required">
					<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
	
		<label>小区:</label>
		<form:select id="communityId" name="communityId" path="unit.buildings.community.id"  style="width:140px" class="input-small">
					<form:option value="" label="选择小区"/>
					<form:options items="${communityList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
		<label>楼宇:</label>
		<form:select id="buildingsId" name="buildingsId" path="unit.buildings.id" style="width:90px" class="input-small">
					<form:option value="" label="选择楼宇"/>
					<form:options items="${buildingsList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>		
		
		<label>单元:</label>
		<form:select id="unitId" name="unitId" path="unit.id"  style="width:80px" class="input-small">
					<form:option value="" label="选择单元"/>
					<form:options items="${unitList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>			
		
		
		
		
		<!--
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-small"/>
		-->
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		&nbsp;<input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
		&nbsp;<input id="btnImport" class="btn btn-primary" type="button" value="导入"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
			
			<th>房号</th>
			<th>房间名称</th>
			<th>业主编码</th>
			<th>业主</th>
			<th>单位</th>
			<th>房间功能</th> 
			<th>楼层</th> 
			<th>户型</th>
			<th>朝向</th>
			<th>建筑面积</th>
			<th>使用面积</th>
			<th>已售</th>
			<th>已租</th>
			<th>操作</th>
		   <shiro:hasPermission name="pms:house:edit"></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="house">
			<tr>
			<td>${house.code}</td>
			<td><a href="${ctx}/pms/house/form?id=${house.id}">${house.fullName}</a></td>
			<td>${house.owner.loginName}</td>
			<td>${house.owner.name}</td>
			<td>${house.owner.company.name}</td>
			<td>${fns:getDictLabel(house.funct, 'pms_house_funct', ' ')}</td>
			<td>${house.numFloor}</td>
			<td>${house.apartment}</td>
			<td>${house.face}</td>
			<td>${house.buildArea}</td>
			
			<td>${house.useArea}</td>
			<td>${house.isSell}</td>
			<td>${house.isRent}</td>

				<shiro:hasPermission name="pms:house:edit"></shiro:hasPermission>
				<td>
    				<a href="${ctx}/pms/house/form?id=${house.id}">修改</a>
					<a href="${ctx}/pms/house/delete?id=${house.id}" onclick="return confirmx('确认要删除该收费项目吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
