<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dhtml.jsp" %>


	<script type="text/javascript">
		$(document).ready(function() {

			function selectDate(){WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});};	
			$("#lastDate").click(function (e) {selectDate();});		
			$("#btnGetdevices").click(function (e) {getdevices();});
			
			

			//$("#name").focus();
			
			initGrid();
			
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
		
		
	function initGrid(){

			mygrid = new dhtmlXGridObject('gridbox');
			mygrid.selMultiRows = true;
			mygrid.setImagePath("${ctxStatic}/dhtmlxTreeGrid/image/grid/");
			var flds = "房间,表编号,业主,上次日期,上次读数,本次读数,使用量";
			mygrid.setHeader(flds);
			var columnIds = "house,deviceCode,user,firstDate,firstNum,lastNum,useNum";
			mygrid.setColumnIds(columnIds);
			
		    mygrid.setInitWidthsP("25,15,15,15,10,10,10");
			mygrid.setColAlign("left,right,right,right,right,right,right");
			mygrid.setColTypes("ed,ed,ed,ed,ed,ed,ed");
		    
		    mygrid.setMultiLine(false);
			mygrid.setEditable(true);
		    mygrid.setSkin("modern2");
		    mygrid.setColSorting("str,str,str,int,int,int,int") ;
		    mygrid.enableAlterCss("even","uneven"); 
		
			mygrid.init();	 
			mygrid.setSortImgState(true,0,"ASC"); 
			//mygrid.attachFooter('合计:, , , , , , ',['text-align:center;','text-align:right;','text-align:right;','text-align:right;','text-align:right;','text-align:right;','text-align:right;']);

			
			gridbox.style.height = gridbox.offsetHeight  +"px";	
			
			mygrid.setSizes();	

	}		
	
	
	function getdevices(){
		var url = '${ctx}/pms/device/getdevicesJson';
		var houseIds = $("#houseId").val().split(",");
		$.getJSON(url,{model:'house',houserIds:houseIds},function(data){
			mygrid.clearAll();
			mygrid.loadXMLString(data.grid);
			mygrid.setSizes();	
		});
	}
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/pms/payemtDetail/">读表信息列表</a></li>
		<li class="active"><a href="${ctx}/pms/payemtDetail/form?id=${payemtDetail.id}">读表信息<shiro:hasPermission name="pms:payemtDetail:edit">${not empty payemtDetail.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="pms:payemtDetail:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	
	
	
	<form:form id="searchForm" modelAttribute="payemtDetail" action="${ctx}/pms/payemtDetail/" method="post" class="breadcrumb form-search">


		<c:if test="${ empty id}">
		
		<div>
		
		<label class="control-label">物业公司:</label>
		<form:select id="proCompanyId" name="proCompanyId" path="device.house.unit.buildings.community.proCompany.id"  class="text medium;required">
					<form:option value="" label=""/>
					<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
		<label class="control-label">收费项目:</label>
		<form:select id="feesId" name="feesId" path="device.fees.id" class="input-small text medium;required">
					<form:option value="" label=""/>
					<form:options items="${feesList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>				
	
		<label>小区:</label>
		<form:select id="communityId" name="communityId" path="device.house.unit.buildings.community.id"  class="input-small">
					<form:option value="" label=""/>
					<form:options items="${communityList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
		<label>楼宇:</label>
		<form:select id="buildingsId" name="buildingsId" path="device.house.unit.buildings.id"  class="input-small">
					<form:option value="" label=""/>
					<form:options items="${buildingsList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>		
		
		<label>单元:</label>
		<form:select id="unitId" name="unitId" path="device.house.unit.id"  class="input-small">
					<form:option value="" label=""/>
					<form:options items="${unitList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>	
		
		</div>
		
	 </c:if>    

	</form:form>	
	
	
	
	
	
	<form:form id="inputForm" modelAttribute="payemtDetail" action="${ctx}/pms/payemtDetail/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="device.id"/>

		<tags:message content="${message}"/>
	
		
		<div class="control-group">
			<label class="control-label">物业公司:</label>
			<div class="controls">
				<form:select id="proCompanyId" name="proCompanyId" path="device.house.unit.buildings.community.proCompany.id"  class="text medium;">
							<form:options items="${proCompanyList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
			</div>
		</div>		

	     
	     <c:if test="${not empty id}">
	     
			<div class="control-group">
				<label class="control-label">收费项目:</label>
				<div class="controls">
					<form:select id="feesId" name="feesId" path="device.fees.id" class="text medium;required">
								<form:option value="" label=""/>
								<form:options items="${feesList}" itemLabel="name" itemValue="id" htmlEscape="false" />
					</form:select>					
				</div>
			</div>		
	
		
		     <div class="control-group">
		     		<label class="control-label">上次日期:</label>
		     		<div class="controls">
						<input id="lastDate" name="lastDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${payemtDetail.lastDate}" pattern="yyyy-MM-dd"/>" class=" Wdate"/>	
		     	 </div>
		     </div>
	                 		
			<div class="control-group">
				<label class="control-label">上次读数:</label>
				<div class="controls">
					<form:input path="firstNum" htmlEscape="false" maxlength="200" class="required"/>
				</div>
			</div>     
		     
			<div class="control-group">
				<label class="control-label">本次读数:</label>
				<div class="controls">
					<form:input path="lastNum" htmlEscape="false" maxlength="200" class="required"/>
				</div>
			</div>     
			
		     <div class="control-group">
		     		<label class="control-label">本次日期:</label>
		     		<div class="controls">
						<input id="lastDate" name="lastDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${payemtDetail.lastDate}" pattern="yyyy-MM-dd"/>" class=" Wdate"/>	
		     	 </div>
		     </div>			
			
		</c:if>          
	     


		<c:if test="${ empty id}">
		     <div class="control-group">
		     		<label class="control-label">当前读表日期:</label>
		     		<div class="controls">
						<input id="lastDate" name="lastDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${payemtDetail.lastDate}" pattern="yyyy-MM-dd"/>" class=" Wdate"/>	
							
							<input id="btnGetdevices" class="btn" type="button" value="刷新" />
		     	 </div>
		     </div>
		     
		    <div class="control-group">
				<label class="control-label">表信息:</label>
				<div class="controls">
					<div id="gridbox" width="100%" height="30%" style="background-color:white;z-index:0"></div>
				</div>
			</div>     
		</c:if>       

		<div class="form-actions">
			<shiro:hasPermission name="pms:payemtDetail:edit"></shiro:hasPermission><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		

		
	</form:form>
</body>
</html>
