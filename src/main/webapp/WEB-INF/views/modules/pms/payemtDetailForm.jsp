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
			//$("#firstDate").click(function (e) {selectDate();});		
			//$("#lastDate").click(function (e) {selectDate();});		
			$("#receDate").click(function (e) {selectDate();});		
			$("#btnGetdevices").click(function (e) {getdevices();});
			
			
			$("#feesId").click(function (e) {getPaymentDetails();});  
			$("#isPay").click(function (e) {getPaymentDetails();});  
			
			//var url5 = "${ctx}/pms/fees/feesjson2?model=device";
			//var obj = window.parent.cmsMenuFrame.document.getElementById("proCompanyId");
		    //$("#feesId").remoteChained(obj, url5);	   
			//var obj = document.getElementById("cmsMainFrame");
			//alert(window.parent.cmsMenuFrame.document.getElementById("proCompanyId"))

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
			$("#costMoney3").keyup(function(){getAccount();});
			
			$("#useLeave").click(function(){
			   if($("#useLeave").is(':checked')){
			   		
			   }else{
			  		 var leaveMoneyOut = $("#leaveMoneyOut").val()*1;
			  		 var costMoney3 = $("#costMoney3").val()*1;
			  		 costMoney3 = costMoney3 + leaveMoneyOut;
			  		 
			  		 $("#leaveMoneyOut").val(0);
			  		 $("#costMoney3").val(costMoney3);
			   }  
			   
			   getAccount();
			})
			
			
			
			
			$("#tabs1").click(function(){
				var feesId = document.getElementById("feesId").value; 
				var houseId = document.getElementById("house.id").value;
				var proCompanyId = document.getElementById("device.fees.company.id").value;
				location.href = "${ctx}/pms/payemtDetail/form?house.id="+ houseId +"&device.fees.company.id="+ proCompanyId;
			})
			$("#tabs2").click(function(){
				var houseId = document.getElementById("house.id").value;
				var proCompanyId = document.getElementById("device.fees.company.id").value;
				location.href = "${ctx}/pms/paymentAfter/list?type=1&house.id="+houseId +"&device.fees.company.id="+proCompanyId;
			})	
			
			$("#tabs3").click(function(){
				var houseId = document.getElementById("house.id").value;
				var proCompanyId = document.getElementById("device.fees.company.id").value;
				location.href = "${ctx}/pms/paymentAfter/list?type=2&house.id="+houseId +"&device.fees.company.id="+proCompanyId; 
			})				

		
			//document.getElementById('house.fullName').readonly= true;
		});
		
		
	function initGrid(){

			mygrid = new dhtmlXGridObject('gridbox');
			mygrid.selMultiRows = true;
			mygrid.setImagePath("${ctxStatic}/dhtmlxTreeGrid/image/grid/");
			var flds = "<input type='checkbox' onclick='ckall(this)' />,费项,上次读数,本次读数,用量,单价,本次付,公摊费,总应付,到账金额,缴费日期";
			mygrid.setHeader(flds);
			var columnIds = "inedx,feedName,firstNum,lastNum,useNum,price,payMoney,poolPay,cost,incone,paydates";
			mygrid.setColumnIds(columnIds);
			
		    mygrid.setInitWidthsP("2,8,10,10,10,10,10,10,10,10,10");
			mygrid.setColAlign("center,center,right,right,right,right,right,right,right,right,center");
			mygrid.setColTypes("ch,ed,ed,ed,ed,ed,ed,ed,ed,ed,ed");
		    
		    mygrid.setMultiLine(false);
			mygrid.setEditable(false);
		    mygrid.setSkin("modern2");
		    mygrid.setColSorting("na,str,str,str,str,int,int,int,int,int,int") ;
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

			$("#costMoney2").val(ForDight(sumPayment-sumIncome,2));
			$("#costMoney3").val(ForDight(sumPayment-sumIncome,2));
			
			$("#leaveMoneyOut").val("");
			//$("#costMoney3").val("");
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
	
	

	
	function getAccount(){
	    var sumAccountMoney = 0;
	    var costMoney2 = $("#costMoney2").val()*1;        //应付金额
	    var leaveMoney  = $("#leaveMoney").val()*1;       //帐户余额
	    var leaveMoneyOut  = $("#leaveMoneyOut").val()*1; //余额支出
	    var costMoney3  = $("#costMoney3").val()*1; 	  //实收金额
	    var leavesave = 0;                                                  
	    var leaveMoneyOut_temp = 0;
	    
	 
	    
	    if(costMoney2 <= 0){
	         alert("应付金额必须大于0.");
	    	 return false;
	     }
	    
	    if($("#useLeave").is(':checked')){
	        if(costMoney3 > costMoney2){
	      	   alert("实收大于应付金额不需要使用余额.");
	      	     $("#useLeave").attr("checked", false);  
	      	 // $("#costMoney3").val("");
	      	    $("#leaveMoneyOut").val("");
	      	  return false;
	        }else{
	          
	          //应付金额-实收金额
	          leaveMoneyOut_temp = costMoney2 - costMoney3;  
	          
	          //有预付款，需要计算预付的支出
	          if(leaveMoney >0){ 
		          if(leaveMoney >leaveMoneyOut_temp){
		             leaveMoneyOut = leaveMoneyOut_temp;
		          }else{
		             leaveMoneyOut = leaveMoney;
		          }
	          }

              sumAccountMoney = costMoney3+leaveMoneyOut;
	        }
	    }else{
	    	if(costMoney3 > costMoney2){
	        	leavesave = costMoney3 - costMoney2;
	    	}
	    	sumAccountMoney = costMoney3;
	    }
	    

	     $("#leaveMoneyOut").val(ForDight(leaveMoneyOut,2));

         if(sumAccountMoney > 0){
         	//$("#btnSubmit").show();
         }else{
         	  //alert("账户余额不足或没有填写实际应收款.");
         }
	    

	   setRowIncomeMoney(mygrid,sumAccountMoney)
	    
	}
	
	function getPaymentDetails(){
		var url = '${ctx}/pms/payemtDetail/getPaymentDetailsJson';
		var houseId = document.getElementById("house.id").value;
		var feesId = document.getElementById("feesId").value;
		//var firstDate = document.getElementById("firstDate").value;
		//var lastDate = document.getElementById("lastDate").value;
		var isPay  = document.getElementById("isPay").value;
		
		
		
		//$.getJSON(url,{model:'house',isPay:isPay,type:2,houseId:houseId,firstDate:firstDate,lastDate:lastDate},function(data){
		$.getJSON(url,{model:'house',isPay:isPay,type:2,houseId:houseId,feesId:feesId},function(data){
			mygrid.clearAll();
			mygrid.loadXMLString(data.grid);
			mygrid.setSizes();	
			$("#costMoney").val(data.costMoney);
			$("#leaveMoney").val(data.preMoney);
			
		    $("#leaveMoneyOut").val(0);
		    $("#costMoney2").val(0);
			$("#costMoney3").val(0);
			
		});
		

	}
	
	

 	
 	
 	
	
 	function setRowIncomeMoney(grid,sumAccountMoney){
 	
 	        var colIndex = grid.getColIndexById("incone");

			for(var i=0; i< grid.getRowsNum();i++){
			   
				var v = grid.cells2(i,0).getValue();
				 	        
				if(v == 1){
				    var id = grid.getRowId(i);
				    var costMoney =  grid.getUserData(id,"costMoney")*1;
				    var incomeMoney =  grid.getUserData(id,"incomeMoney")*1;

				    var rowVale = 0;

				    if( ForDight(sumAccountMoney,2) >= ForDight(costMoney-incomeMoney,2)){
				      rowVale = costMoney;
				    }else{
				      rowVale = sumAccountMoney+incomeMoney;
				    }

				  	sumAccountMoney -= costMoney;
			
					grid.cells2(i,colIndex).setValue(ForDight(rowVale,2));
					
					if(sumAccountMoney <=0) break;
					
					continue;
				 }
			}
 	}
 	
 	

	function save(){
	
	   
	
		var houseId = document.getElementById("house.id").value;
	    var costMoney2 = $("#costMoney2").val()*1;        //应付金额
 		var leaveMoneyOut  = $("#leaveMoneyOut").val()*1; //余额支出
	    var costMoney3  = $("#costMoney3").val()*1; 	  //实收金额
 	    var payType  = $("#payType").val();               //收款方式
 	    var receDate =  $("#receDate").val(); 
 	    
 	    var feeCode  = $("#feeCode").val();               //收款单号
 	    var certCode  = $("#certCode").val();               //发票号
 	    
 	    var lle = costMoney3 - costMoney2;
 	    if(lle >0){
 	    	alert("系统将自动把多付的"+ ForDight(lle,2) +"元，转入到预存帐户");
 	    }
 	    
 	    if(costMoney3 ==0 && leaveMoneyOut ==0){
 	       alert("账户余额不足或没有填写实际应收款.");return false;
 	    }
 	    
 	   
 	    
 	    var type = 2;                                     //1现付 2 预付 
 	    var url = '${ctx}/pms/payemtDetail/saveNew';
 	    var grid = mygrid;

		//保存 PaymentAfter 保存PayemtDetail  
		var colIndex = grid.getColIndexById("incone");
		var detailsIncome ="";
		var k = 0;
		for(var i=0; i< grid.getRowsNum();i++){
				var v = grid.cells2(i,0).getValue();       
				if(v == 1){
				    var id = grid.getRowId(i)*1;
				    var incomeMoney = grid.cells2(i,colIndex).getValue()*1;
				    var incomeMoney_bak =  grid.getUserData(id,"incomeMoney")*1;
				    var incomeMoney2 = incomeMoney - incomeMoney_bak;
				    var feesId =  grid.getUserData(id,"feesId")*1;
				    //var data1 ={"id":id,"incomeMoney":incomeMoney};
				    var data1 = id +","+incomeMoney+","+incomeMoney2+","+feesId;
				    detailsIncome = detailsIncome + data1 +";"
				    alert(detailsIncome);
				}		
		}	

        var data = {houseId:houseId, feeCode:feeCode,certCode:certCode,receDate:receDate,payType:payType,leaveMoneyOut:leaveMoneyOut,costMoney2:costMoney2,costMoney3:costMoney3,payemtDetails:detailsIncome};
   		// var data = {houseId:houseId,receDate:receDate,payType:payType,leaveMoneyOut:leaveMoneyOut,costMoney2:costMoney2,costMoney3:costMoney3,unitPrice:11};

   		$.ajax({
                type:"post",
                url:url,
                data:data,
                datetype:'text',
                //datetype:'json',
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
		<li><a id="tabs2" href="#" onClick="reloadForm()">预付款</a></li>
		<li><a id="tabs3" href="#" onClick="reloadForm()">付款历史</a></li>
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
							value="<fmt:formatDate value="${payemtDetail.firstDate}" pattern="yyyy-MM-dd"/>" class=" Wdate"/>	
	
			<label class="control-label">本次日期:</label>
			<input id="lastDate" name="lastDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${payemtDetail.lastDate}" pattern="yyyy-MM-dd"/>" class=" Wdate"/>	
		   -->
		     
		     <!-- 
			<label class="control-label">收费项目:</label>
			<form:select id="feesId" name="feesId" path="device.fees.id" class="input-small text medium;required">
						<form:option value="" label=""/>
						<form:options items="${feesList}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>	
			-->

			<select id="isPay" style="width:100px">
			  <option value=0>有欠款</option>
			  <option value=1>所有</option>
			</select>
			
			<input id="btnQuery" class="btn" type="button" value="检  索" onclick="getPaymentDetails()"/>

	 		<c:if test="${ empty id}"> </c:if>    

			<div class="controls">
					<div id="gridbox" width="100%" height="30%" style="background-color:white;z-index:0"></div>
			</div>





		<div class="form-actions">
				<div class="control-group">
					<label class="control-label">房间编号:</label><form:input path="house.fullName" class="required "  readonly="true" />
					<label class="control-label">业主姓名:</label><form:input path="house.owner.name"  class="required input-small" readonly="true"/>
					<label class="control-label">欠费合计:</label><input id="costMoney" htmlEscape="false"  class="required input-small" readonly="true"/>
				</div> 		
	
			<div class="control-group">
				<label class="control-label">帐户余额:</label><input id="leaveMoney" htmlEscape="false" class="required input-small" readonly="true"/>
				<label class="control-label">余额支出:</label><input id="leaveMoneyOut" htmlEscape="false" class="required input-small" readonly="true"/>
				<label class="control-label">应付金额:</label><input id="costMoney2" htmlEscape="false" class="required input-small" readonly="true"/>
				
			</div> 	
			
			<div class="control-group">
				<label class="control-label">收据编号:</label><input id="feeCode" htmlEscape="false" class="required input-small"/>
				<label class="control-label">发票编号:</label><input id="certCode" htmlEscape="false" class="required input-small"/>
				<label class="control-label">实收金额:</label><input id="costMoney3" htmlEscape="false" class="required input-small"/>
				<label class="control-label" for="useLeave">不足时用余额:</label> <input  name="useLeave" id="useLeave" type='checkbox' value=0/>	
			</div>
			
			<div class="control-group">
				<label class="control-label">备注说明:</label><input id="lastNum9" htmlEscape="false" class="required  input-small"/>
				
				<label class="control-label">收款方式:</label>
				<select id="payType" name="payType" class="required input-small">
					<c:forEach items="${fns:getDictList('pms_pay_type')}" var="item">
 						<option value ="${item.value}">${item.label}</option>
					</c:forEach>	
				</select>		
				
			<label class="control-label">收款日期:</label>
			<input id="receDate" name="receDate" type="text" readonly="readonly" maxlength="20" 
							value="<fmt:formatDate value="${payemtDetail.lastDate}" pattern="yyyy-MM-dd"/>" class=" Wdate input-small"/>					
				
				
				
			<input id="btnSubmit" class="btn btn-primary" type="button" value=" 保   存 "  onclick="save()"/>&nbsp;	
			</div>		
			
			
			
			<div class="control-group">

				<input id="btnAccount" class="btn" type="hidden" value=" 计  算 " onclick="getAccount()"/>	
				
				<shiro:hasPermission name="pms:payemtDetail:edit"></shiro:hasPermission>
				
		
			</div>			
				
			 
			
		</div>  		

		</form:form>	
		
		
	

		


</body>
</html>
