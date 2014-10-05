<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dhtml.jsp" %>


	<script type="text/javascript">
	
	
		$(document).ready(function() {
			//function selectDate(){WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});};	
			//$("#firstDate").click(function (e) {selectDate();});		
			//$("#lastDate").click(function (e) {selectDate();});		
			$("#receDate").click(function (e) {selectDate();});		
			$("#btnGetdevices").click(function (e) {getdevices();});
			
			
			$("#feesId").click(function (e) {getPaymentDetails();});  
			$("#isPay").click(function (e) {getPaymentDetails();});  
			
			initGrid();
			
			getPaymentDetails();
			
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
			
			//$("#btnSubmit").hide();
			//$("#costMoney3").change($("#btnSubmit").hide(););

			
		});
		
		
	
		
	function initGrid(){

			mygrid = new dhtmlXGridObject('gridbox');
			mygrid.selMultiRows = true;
			mygrid.setImagePath("${ctxStatic}/dhtmlxTreeGrid/image/grid/");
			var flds = "<input type='checkbox' onclick='ckall(this)' />,费项,房屋,业主,上次数,最后数,使用量,公摊量,总用量,单价,应付金额,到账金额,期限";
			mygrid.setHeader(flds);
			var columnIds = "inedx,feedName,house,owner,firstNum,lastNum,useNum,poolNum,sumNum,price,cost,incone,paydates";
			mygrid.setColumnIds(columnIds);
			
		    mygrid.setInitWidthsP("2,5,20,7,8,8,8,5,8,5,8,8,8");
			mygrid.setColAlign("center,center,left,center,right,right,right,right,right,right,right,right,center");
			mygrid.setColTypes("ch,ed,ed,ed,ed,ed,ed,ed,ed,ed,ed,ed,ed");
		    
		    mygrid.setMultiLine(false);
			mygrid.setEditable(false);
		    mygrid.setSkin("modern2");
		    mygrid.setColSorting("na,str,str,str,str,str,str,int,int,int,int,int,int") ;
		    mygrid.enableAlterCss("even","uneven"); 
		
			mygrid.init();	 
			mygrid.setSortImgState(true,1,"ASC"); 
			//mygrid.attachFooter('合计:, , , , , , ',['text-align:center;','text-align:right;','text-align:right;','text-align:right;','text-align:right;','text-align:right;','text-align:right;']);

			gridbox.style.height = gridbox.offsetHeight  +"px";	
			mygrid.setOnRowSelectHandler(onRowSelected,true);
			mygrid.setSizes();	
			
			
			function setCellCheck(grid,id,colIndex){
				var cell = grid.cells(id,colIndex);
				var v = cell.getValue()==0?1:0;
				cell.setValue(v);	
				return v;
			}	
	
			function onRowSelected(rowId,rowIndex){
			    //var rowId = this.getSelectedId();
	            setCellCheck(this,rowId,0);
	            getMoney(this);
			}	
		
		

	}		
	

		function ForDight(Dight,How){  
			  Dight =  Math.round(Dight*Math.pow(10,How))/Math.pow(10,How);  
			  return Dight;  
 		 }  	
 		 
 		function setIncomeMoney(grid,id){
 		   	var colIndex = grid.getColIndexById("incone");
 		   	var costMoney =  grid.getUserData(id,"costMoney")*1;
 		   	var incomeMoney =  grid.getUserData(id,"incomeMoney")*1;
 		   	var v = grid.cells(id,0).getValue();
 		   	if(v == 1){
 		   		grid.cells(id,colIndex).setValue(ForDight(costMoney,2));
 		   	}else{
 		   	   grid.cells(id,colIndex).setValue(ForDight(incomeMoney,2));
 		   	}
 		  }
 		 
 		 function getMoney(grid){
            var sumPayment = 0;
            var sumIncome = 0;
			var ids = new Array();
			for(var i=0; i< grid.getRowsNum();i++){
				var v = grid.cells2(i,0).getValue();
				var id = grid.getRowId(i);
				if(v == 1){
				 	ids.push(id);
				  	sumPayment +=  grid.getUserData(id,"costMoney")*1;
				  	sumIncome +=  grid.getUserData(id,"incomeMoney")*1;
				 }
				 setIncomeMoney(grid,id);
			}
            
            var sumAccountMoney = ForDight(sumPayment-sumIncome,2);
			$("#costMoney2").val(sumAccountMoney);
			$("#leaveMoneyOut").val("");
			$("#costMoney3").val("");
			//$("#btnSubmit").hide();

			return ids;
 		 }	
		function ckall(e){
		    if(e.checked){
		   		 mygrid.checkAll();
		    }else{
		  		 mygrid.uncheckAll();
		    }
		    getMoney(mygrid,e.checked);
		}		
	
	

	
	
	
	function getPaymentDetails(){
		var url = '${ctx}/pms/payemtDetail/getPaymentDetailsJson';
		var officeId = document.getElementById("officeId").value;
		var houseId = document.getElementById("house.id").value;
		var feesId = document.getElementById("feesId").value;
		//var firstDate = document.getElementById("firstDate").value;
		//var lastDate = document.getElementById("lastDate").value;
		var isPay  = document.getElementById("isPay").value;
		
   
        //$.getJSON(url,{model:'house',isPay:isPay,type:1,officeId:officeId,feesId:feesId,firstDate:firstDate,lastDate:lastDate},function(data){
		$.getJSON(url,{model:'house',isPay:isPay,type:1,officeId:officeId,feesId:feesId},function(data){
			mygrid.clearAll();
			mygrid.loadXMLString(data.grid);
			mygrid.setSizes();	
			$("#costMoney").val(data.costMoney);
			$("#leaveMoney").val(data.preMoney);
			$("#officeName").val(data.officeName);
			$("#ownerNum").val(data.ownerNum);
			
		});
		

	}
	
	

 	
 	
 	
	
 	function setRowIncomeMoney(grid,sumAccountMoney){
 	
 	        var colIndex = grid.getColIndexById("incone");

			for(var i=0; i< grid.getRowsNum();i++){
			   
				var v = grid.cells2(i,0).getValue();
				
				var id = grid.getRowId(i);
				var costMoney =  grid.getUserData(id,"costMoney")*1;
				var incomeMoney =  grid.getUserData(id,"incomeMoney")*1;
				 	        
				if(v == 1){

				    var rowVale = 0;

				    if( ForDight(sumAccountMoney,2) >= ForDight(costMoney-incomeMoney,2)){
				      rowVale = costMoney;
				    }else{
				      rowVale = sumAccountMoney;
				    }

				  	sumAccountMoney -= costMoney;
			
					grid.cells2(i,colIndex).setValue(ForDight(rowVale,2));
					
					if(sumAccountMoney <=0) break;
					
					continue;
				 }else{
			   		grid.cells2(i,colIndex).setValue(ForDight(incomeMoney,2));
				 }
				
			}
 	}
 	
 	

	function save(){

	    var costMoney2 = $("#costMoney2").val()*1;        //实收金额
 	    var payType  = $("#payType").val();               //收款方式
 	    var receDate =  $("#receDate").val(); 

 	    if(costMoney2 >0){
 	    }else{
 	    	alert("无实收，不需要保存!"); return false;
 	    }
 	    
 	   
 	    
 	    var type = 1;                                     //1现付 2 预付 
 	    var url = '${ctx}/pms/payemtDetail/saveNew2';
 	    var grid = mygrid;

		//保存 PaymentAfter 保存PayemtDetail  
		var colIndex = grid.getColIndexById("incone");
		var detailsIncome ="";
		var k = 0;
		for(var i=0; i< grid.getRowsNum();i++){
				var v = grid.cells2(i,0).getValue();       
				if(v == 1){
				    var id = grid.getRowId(i)*1;
				    var houseId =  grid.getUserData(id,"houseId")*1;
				    var feesId =  grid.getUserData(id,"feesId")*1;
				    var incomeMoney = grid.cells2(i,colIndex).getValue()*1;
				    var incomeMoney_bak =  grid.getUserData(id,"incomeMoney")*1;
				    var incomeMoney2 = incomeMoney - incomeMoney_bak;
				    var data1 = id +","+incomeMoney+","+incomeMoney2+","+houseId+","+feesId;
				    detailsIncome = detailsIncome + data1 +";"
				}		
		}	

        var data = {receDate:receDate,payType:payType,costMoney2:costMoney2,payemtDetails:detailsIncome};
   	
   		$.ajax({
                type:"post",
                url:url,
                data:data,
                datetype:'text',
                success:function(result){
                      alert("保存成功");
                      getPaymentDetails();
                },
               error : function(){
                  alert("错了");
                  getPaymentDetails();
              }
            });	
            
	

 	}
		
		
		
	</script>
</head>
<body>

	<ul class="nav nav-tabs">
		<li class="active"><a id="tabs1" href="#" >缴费明细</a></li>

	</ul>
 
	<form:form id="searchForm" modelAttribute="payemtDetail" action="${ctx}/pms/payemtDetail/" method="post" class="breadcrumb form-search">
			
			<form:hidden path="house.id"/>
	
			<form:hidden path="officeId"/>
			
			<form:hidden path="device.fees.company.id"/>
			
			<label class="control-label">收费项目:</label>
			<form:select id="feesId" name="feesId" path="device.fees.id" class="input-small text medium;required">
						<form:option value="" label=""/>
						<form:options items="${feesList}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>				
		
            <!-- 
			<label class="control-label">费用起收日:</label>
			<input id="firstDate" name="firstDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${payemtDetail.firstDate}" pattern="yyyy-MM-dd"/>" class=" Wdate input-small"/>	
	
			<label class="control-label">本次日期:</label>
			<input id="lastDate" name="lastDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${payemtDetail.lastDate}" pattern="yyyy-MM-dd"/>" class=" Wdate input-small"/>	
		     
		     -->
		 

	

			<select id="isPay"  style="width:100px">
			  <option value=0>有欠款</option>
			  <option value=1>所有</option>
			</select>
			
			<input id="btnQuery" class="btn" type="button" value="检  索" onclick="getPaymentDetails()"/>

	 		<c:if test="${ empty id}"> </c:if>    

			<div class="controls">
					<div id="gridbox" width="100%" height="50%" style="background-color:white;z-index:0"></div>
			</div>



		<div class="form-actions">
				<div class="control-group">
					<label class="control-label">单位名称:</label><input id="officeName" class="required" type="text" value="" readOnly=1/>
					<label class="control-label">业主户数:</label><input id="ownerNum" style="width:50px" class="required input-small"  value=""  readOnly=1/>
					<label class="control-label">欠费合计:</label><input id="costMoney" htmlEscape="false"   style="width:100px" class="required input-small"  readOnly=1/>
					<label class="control-label">实收金额:</label><input id="costMoney2" htmlEscape="false" style="width:100px" class="required input-small"  readOnly=1/>
				</div> 		
				
			<div class="control-group">
			
				
				<label class="control-label">收款方式:</label>
				<select id="payType" name="payType">
					<c:forEach items="${fns:getDictList('pms_pay_type')}" var="item">
 						<option value ="${item.value}">${item.label}</option>
					</c:forEach>	
				</select>		
				
			<label class="control-label">收款日期:</label>
			<input id="receDate" name="receDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${payemtDetail.lastDate}" pattern="yyyy-MM-dd"/>"   class=" Wdate input-small"/>	
							
							
			<label class="control-label">备注说明:</label><input id="lastNum9" htmlEscape="false" style="width:200px" class="required"/>								
				
					<shiro:hasPermission name="pms:payemtDetail:edit"></shiro:hasPermission>
				<input id="btnSubmit" class="btn btn-primary" type="button" value=" 保   存 "  onclick="save()"/>&nbsp;
			</div>		
	
				
			 
			
		</div>  		

		</form:form>	
		
		
	

		


</body>
</html>
