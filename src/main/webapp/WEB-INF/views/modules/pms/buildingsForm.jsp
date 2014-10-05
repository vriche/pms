<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>楼宇信息</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>

	<script type="text/javascript">
	

	  
	    
		$(document).ready(function() {
			$("#name").focus();


  			function selectDate(){WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});};
			$("#capDate").click(function (e) {selectDate();});		
			$("#completionDate").click(function (e) {selectDate();});	
			
		   var url = "${ctx}/pms/community/communityjson?model=buildings";
		   $("#communityId").remoteChained("#proCompanyId", url);
   			 
  			$("#btn_remove").click(
				function (e) {  
				  var choice=confirm("您确认要删除吗？", function(r) { alert(1);},null);
				  if(choice){
				   	   var url = "${ctx}/pms/buildings/delete?id="+$("#id")[0].value;
				   	   location.href = url;
				   }
	   			 }
   			 );   			 
   			 
   			 			
			
			
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
		<li><a href="${ctx}/pms/buildings/">楼宇列表</a></li>
		<li class="active"><a href="${ctx}/pms/buildings/form?id=${buildings.id}">楼宇信息<shiro:hasPermission name="pms:buildings:edit">${not empty buildings.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="pms:buildings:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="buildings" action="${ctx}/pms/buildings/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		

		
		<div class="control-group">
			<label class="control-label">物业公司:</label>
			<div class="controls">
					<form:select id="proCompanyId" name="proCompanyId" path="community.proCompany.id"  class="text medium;required">
								<form:option value="" label=""/>
								<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
					</form:select>
			</div>
		</div>		
		

		
		<div class="control-group">
			<label class="control-label">小区:</label>
			<div class="controls">
				<form:select id="communityId" name="communityId" path="community.id"  class="text medium;required">
					<form:options items="${communityList}" itemLabel="name" itemValue="id" htmlEscape="false" />
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
			<label class="control-label">编号:</label>
			<div class="controls">
				<form:input path="code" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		
	
	
		
		<div class="control-group">
			<label class="control-label">建筑面积:</label>
			<div class="controls">
				<form:input path="buildArea" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		
		
		
		<div class="control-group">
			<label class="control-label">使用面积:</label>
			<div class="controls">
				<form:input path="useArea" htmlEscape="false" maxlength="200" class="input-mini required"/>
				&nbsp;<label>单元数量:</label><form:input path="floorCount" htmlEscape="false" maxlength="200" class="input-mini required digits"/>
			</div>
		</div>
		


		
		
					
		<div class="control-group">
			<label class="control-label">封顶日期:</label>
			<div class="controls">
				<form:input path="capDate" htmlEscape="false" maxlength="200"  class=" Wdate"/>
			</div>
		</div>	
						
		<div class="control-group">
			<label class="control-label">竣工日期:</label>
			<div class="controls">
				<form:input path="completionDate" htmlEscape="false" maxlength="200"  class=" Wdate"/>
			</div>
		</div>	


						
		<div class="control-group">
			<label class="control-label">预售许可证:</label>
			<div class="controls">
				<form:input path="presalePermit" htmlEscape="false" maxlength="200" />
			</div>
		</div>		
		
		<div class="control-group">
			<label class="control-label">建筑许可证:</label>
			<div class="controls">
				<form:input path="buildPermit" htmlEscape="false" maxlength="200" />
			</div>
		</div>			

	
		<div class="form-actions">
			<shiro:hasPermission name="pms:buildings:edit"></shiro:hasPermission><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
			&nbsp;
			<input id="btn_remove" class="btn" id="btn_remove" type="button" value="删除" />&nbsp;
			
			
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		
	</form:form>
</body>
</html>
