<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收费项目管理</title>
	<meta name="decorator" content="default"/>
	
	<script type="text/javascript">
	
		$(document).ready(function() {
		
		   $("#name").focus();
			
		   var url = "${ctx}/pms/community/communityjson?model=house";
		   $("#communityId").remoteChained("#proCompanyId", url);
		   var url2 = "${ctx}/pms/buildings/buildingsjson?model=house";
		   $("#buildingsId").remoteChained("#communityId", url2);
		   var url2 = "${ctx}/pms/unit/unitjson?model=house";
		   $("#unitId").remoteChained("#buildingsId", url2);			

			//var feesAllUrl = '${ctx}/pms/fees/feesjson?model=house&houserId=1';
//			var feesAllUrl = '${ctx}/pms/fees/feesjson&proCompanyId='+ $("#proCompanyId").val();
//			var feesHouseUrl = '${ctx}/pms/fees/feesjson?mode=device&houserId='+ $("#id").val() +'&proCompanyId='+ $("#proCompanyId").val();
			var feesAllUrl = '${ctx}/pms/fees/feesjson?proCompanyId='+ $("#proCompanyId").val();
			var feesHouseUrl = '${ctx}/pms/fees/feesjson?mode=device&houserId='+ $("#id").val() +'&proCompanyId='+ $("#proCompanyId").val();
			$.getJSON(feesAllUrl,{model:'house',houserId:$("#id").val()},function(data){
						$("#devices").select2({
							createSearchChoice:function(term, data) { if ($(data).filter(function() { return this.text.localeCompare(term)===0; }).length===0) {return {id:term, text:term};} },
							multiple: true,
							data: data
						});	
						
						$.getJSON(feesHouseUrl,{model:'device',houserId:$("#id").val()},function(data){
						    $('#devices').select2('data', data )
						});						
						
				});
			
			

		
			
			//$('#devices').select2('data', preload_data )
			/*
			$('#devices').select2(
			{
			    placeholder: '',
			    multiple:true,      
			    minimumInputLength: 0,    
			    allowClear: true,
			    tags:[1,2],
			    ajax: {
               		url: feesAllUrl,
                	dataType: 'json',
                	data: function (term, page) {return {q: term};
                },
                results: function (data, page) {return { results: data };},
	   			dropdownCssClass: "bigdrop", 
	   			escapeMarkup: function (m) { return m; } 
	   			
            }
			}); 
			*/
			
			 //$('#devices').select2('data', data )	
			 // $("#devices").select2("val")	  
			 
			 
			$("#btnTest").click(function(){
				alert(123) 
				var v = $("#devices").select2("val");
				alert(v)  
			});		 
			 
			 
	
			
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					var houseFees = $("#devices").select2("val");
			
					$("#houseFees").val(houseFees);
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
		<li><a href="${ctx}/pms/house/">房屋信息列表</a></li>
		<li class="active"><a href="${ctx}/pms/house/form?id=${house.id}">房屋信息<shiro:hasPermission name="pms:house:edit">${not empty house.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="pms:house:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="house" action="${ctx}/pms/house/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="houseFees"/>
		<tags:message content="${message}"/>
		
		

		
		
		<div class="control-group">
			<label class="control-label">物业公司:</label>
			<div class="controls">
				<form:select id="proCompanyId" name="proCompanyId" path="unit.buildings.community.proCompany.id"  class="text medium;">
							<form:option value="" label="选择物业"/>
							<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		
		<div class="control-group">
			<label  class="control-label">小区:</label>
			<div class="controls">
				<form:select id="communityId" name="communityId" path="unit.buildings.community.id"  class="text medium;">
							<form:option value="" label="选择小区"/>
							<form:options items="${communityList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		
		<div class="control-group">
			<label  class="control-label">楼宇:</label>
			<div class="controls">
				<form:select id="buildingsId" name="buildingsId" path="unit.buildings.id"  class="text medium;">
							<form:option value="" label="选择楼宇"/>
							<form:options items="${buildingsList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>		
			</div>
		</div>
		

		<div class="control-group">
			<label class="control-label">单元:</label>
			<div class="controls">
			     <form:select id="unitId" name="unitId" path="unit.id"   class="text medium;required">
						<form:option value="" label="选择单元"/>
						<form:options items="${unitList}" itemLabel="name" itemValue="id" htmlEscape="false" />
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
				<form:input path="useArea" htmlEscape="false" maxlength="200"/>
			</div>
		</div>	
		
			<div class="control-group">
			<label class="control-label">第几层:</label>
			<div class="controls">
				<form:input path="numFloor" htmlEscape="false" maxlength="200"/>
			</div>
		</div>		


		<!-- div class="control-group">
			<label class="control-label">业主:</label>
			<div class="controls">
                <tags:treeselect id="owner" name="owner.id" value="${house.owner.id}" labelName="owner.name" labelValue="${house.owner.name}"
					title="业主" url="/pms/user/treeData" cssClass="required"/>
			</div>
		</div -->
	
	
	
			<div class="control-group">
			<label class="control-label">业主:</label>
			<div class="controls">
			    <tags:treeselect id="owner" 
		    				  name="owner.id"  
		    				  title="业主" 
		    				  value="${house.owner.id}" 
		    				  labelName="${owner.name}" 
		    				  labelValue="${house.owner.name}" 
		                      url="/pms/user/treeData?Level=4" 
		                      notAllowSelectParent="true"  
		                      allowClear="true" 
		                      nodesLevel="2" 
		                      nameLevel="1" 
		                      cssClass="required" 
		                      proCompany="proCompanyId" 
		                      />	
			</div>
		</div>	
	
		
		

		<!-- div class="control-group">
			<label class="control-label">业主:</label>
			<div class="controls">
				<form:select id="owner" name="owner.id" path="owner.id"  class="text medium;required">
					<form:options items="${ownerList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
			</div>
		</div -->			
				
		<div class="control-group">
			<label class="control-label">房间功能:</label>
			<div class="controls">
				<form:select path="funct">
					<form:options items="${fns:getDictList('pms_house_funct')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>	
		
		

		<div class="control-group">
			<label for="devices"  class="control-label">计费项目:</label>
			<div class="controls">
			    <input type="text" id="devices" class="input-xxlarge" />
			</div>
		</div>			

		


		<div class="control-group">
			<label for="devices"  class="control-label">计费项目:</label>
			<div class="controls">
			            <c:forEach var="fees" items="${feesList}">
			                <html:multibox name="${fees.name}" property="houseFees" >
								 <c:out value="${fees.id}"/>
							</html:multibox>
			                <label class="choice" for="<c:out value="${fees.name}"/>">
			                    <c:out value="${fees.name}"/>
			                </label>
			            </c:forEach>
 
			</div>
		</div>			

		
	
		
		
		
		
		
		
		<div class="control-group">
		<label class="control-label">顺序:</label>
		<div class="controls">
			<form:input path="sort" htmlEscape="false" rows="4" maxlength="200" class="text medium;"/>
		</div>
		</div>
		
				
		<!-- div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
		</div -->
		
		
		<div class="form-actions"><shiro:hasPermission name="pms:house:edit"></shiro:hasPermission>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			<!-- input id="btnTest" class="btn" type="button" value="test" / -->
		</div>
	</form:form>
</body>
</html>
