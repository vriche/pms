<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<%@include file="/WEB-INF/views/include/dhtml.jsp" %>
	<%@include file="/WEB-INF/views/include/showLoading.jsp" %>
	<%@include file="/WEB-INF/views/include/gridReport.jsp" %>
	
	<script type="text/javascript">
	
	(function($){
		$.getUrlParam = function(name)
		{
			var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if (r!=null) return unescape(r[2]); return null;
		}
	})(jQuery);
	
//	 	Install_InsertReport();
//		CreateReport("Report");
		$(document).ready(function() {
//			 alert("${ctxStatic}")
			$("#feesId").change(function (e) {getPaymentDetails();});  
			$("#isPay").change(function (e) {getPaymentDetails();});  		
//			$("#firstDate").change(function (e) {mygrid.clearAll();});  
			
			$("#costMoney2").keyup(function(){getAccount();});
			
			
			 var  houseId = $.getUrlParam('house.id');
			 if(houseId >0){
				 $("#divdisplay").show();
			 }else{
				 $("#divdisplay").hide();
			 }
			 
			
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

			
			$("#btnExport").click(function(){
						top.$.jBox.confirm("确认要导出吗？","系统提示",function(v,h,f){
							if(v=="ok"){
								$("#searchForm").attr("action","${ctx}/pms/deviceDetail/exportExcel/?type=2");
								$("#searchForm").attr("enctype","application/x-www-form-urlencoded");
								$("#searchForm").submit();					
							}
						},{buttonsFocus:1});
						top.$('.jbox-body .jbox-icon').css('top','55px');					 
			});		
			
			
			$("#tabs3").click(function(){
				var officeId = document.getElementById("officeId").value;
				var proCompanyId = document.getElementById("device.fees.company.id").value;
				location.href = "${ctx}/pms/paymentBefor/list2?type=1&payFrom=1&company.id="+officeId +"&device.fees.company.id="+proCompanyId; 
			})		
			
			
			
			$("#btnPrint3").click(function(){

				var html = "<table id=\"contentTableWhere\" class=\"table-bordered  table-striped table-hover\">";
				 html +="<thead><tr>";
				 html +="<th style=\"text-align:center;\">&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"checkbox\" name=\"checkboxAll\"  id=\"checkboxAll\" onclick=\"checkboxAll(this)\">&nbsp;&nbsp;&nbsp;&nbsp;</th>";	
				 html +="<th>&nbsp;&nbsp;&nbsp;&nbsp;费用名称&nbsp;&nbsp;&nbsp;&nbsp;</th><th>&nbsp;&nbsp;&nbsp;&nbsp;开始日期&nbsp;&nbsp;&nbsp;&nbsp;</th><th>&nbsp;&nbsp;&nbsp;&nbsp;结束日期&nbsp;&nbsp;&nbsp;&nbsp;</th>";
				 html +="</tr></thead>";
				 html +="<tbody>";
				
				
				$("select[name='device.fees.id'] option").each(function(){
					if($(this).text() !=''){
						var fid = $(this).val();
						var text = $(this).text();
						var firstDate = document.getElementById("firstDate").value;
						var lastDate = document.getElementById("lastDate").value;
						 html +="<tr>";
						 html +="<td style=\"text-align:center;\"><input type=\"checkbox\" name=\"test\" value=\""+ fid +"\"></td>";
						 html +="<td>"+ text +"</td>";
						 html +="<td style=\"text-align:center;\">";
						 html +="<input id=\"firstDate"+ fid +"\" name=\"firstDate"+ fid +"\" type=\"text\" readonly=\"readonly\" maxlength=\"20\" onClick=\"WdatePicker({})\"";
						 html +=" value=\""+ firstDate +"\"   class=\" Wdate input-small\"/>";
						 html +="</td>";
						 
						 html +="<td style=\"text-align:center;\">";
						 html +="<input id=\"lastDate"+ fid +"\" name=\"lastDate"+ fid +"\" type=\"text\" readonly=\"readonly\" maxlength=\"20\" onClick=\"WdatePicker({})\"";
						 html +=" value=\""+ lastDate +"\"   class=\" Wdate input-small\"/>";
						 html +="</td>";
						 html +="</tr>";
					}
				});
				
				 html +="</tbody>";
				 html +="</table>";

				
				var title =  $("#officeName").val();
				$.jBox(html, {
					title:title, 
					buttons:{ '确  定': 1, '关   闭': 0 }, 
					width:400,
					bottomText:"",
					submit: function (v, h, f) {
						 if(v == 1){
							 printOffice();
//							 return true;
						 }else{ 
							 return true;
						 }
					}
				});
			});  					
			

			 
		});
		
		function checkboxAll(e){
			$("input[name='test']").each(function() { // 遍历选中的checkbox
				$(this).attr("checked", e.checked);
			});	
		}
		
//		$("input[name='test']:checked").each(function() { // 遍历选中的checkbox
//			n = $(this).parents("tr").index()+1;  // 获取checkbox所在行的顺序
//			$("table#contentTable").find("tr:eq("+n+")").remove();
//		});	
		
		function getAccount(){
		    var costMoney2 = $("#costMoney2").val()*1;        //实收金额
		    var costMoneyLeave =  $("#costMoneyLeave").val();   //发票余额
		    var leavesave = 0;                                                  
		    var leaveMoneyOut_temp = 0;

		    if(costMoney2 <= 0){
		         alert("实收金额必须大于0.");
		    	 return false;
		     }

		   setRowIncomeMoney(mygrid,costMoney2)
		    
		}
		
	function print(){
//		  <option value="2052">简体中文</option>
//		  <option value="1028">繁体中文</option>
//		  <option value="1033">英文</option>
//		Install_InsertReport();
//		var Installed = Install_Detect();
//		CreateReport("Report");
		
//		if (Installed){
//			CreateReport("Report");
//		}
		
//		Report.Language = 2052;
		
//		setReportPath("${ctxStatic}/grid_report/");

		var temples_file = "${ctxStatic}/grid_report/demo/simple.grf";

        Report.LoadFromURL(temples_file); //载入报表模板
			
		var j={"name":"Michael","city":"Beijing","street":" Chaoyang Road ","postcode":100025};
		
		
		//报表记录集的各个字段
		var fldCustomerID = Report.FieldByName("CustomerID");

		var fldCompanyName = Report.FieldByName("CompanyName");
		var fldContactName = Report.FieldByName("ContactName");
		var fldContactTitle = Report.FieldByName("ContactTitle");
		var fldPhone = Report.FieldByName("Phone");
		var fldFax = Report.FieldByName("Fax");

	
		
//		1、加载参数类型的数据:

//			//把数据传入报表中的参数
//			Report.ParameterByName("CustomerID").AsString = j.name;
//			Report.ParameterByName("CompanyName").AsString = j.city;
//			Report.ParameterByName("ContactName").AsString = j.street;
//			Report.ParameterByName("ContactTitle").AsString = j.postcode;
//
//			//把数据传给报表中的部件框
//			Report.ControlByName("Phone").AsStaticBox.Text = j.name;
//			Report.ControlByName("Fax").AsMemoBox.Text = j.city; 
//			//把数据传给自由表格的单元格
//			Report.ControlByName("FreeGrid1").CellAt(row,col).Text = j.name;
//			Report.ControlByName("FreeGrid1").CellByDataName("city").Text = j.city;
			 

//			2、加载明细类型的数据:
		    Report.PrepareLoadData();

		//报表记录集对象
		    var Recordset = Report.DetailGrid.Recordset;
		    Recordset.Append();
		    fldCustomerID.Value = j.name;
		    fldCompanyName.Value = j.name;
		    fldContactName.Value =j.name;
		    fldContactTitle.Value = j.name;
		    fldPhone.Value = j.name;
		    fldFax.Value = j.name;

		    Recordset.Post(); 
			Report.PrintPreview(true);
			
//			Report.ReportViewer.stop();

	}
	
	function print2(){
		var temples_file = "${ctxStatic}/grid_report/grf/normal26.grf";
//		var temples_file = "${ctxStatic}/grid_report/grf/test1.grf";
        Report.LoadFromURL(temples_file); //载入报表模板

		//报表记录集的各个字段
		var colum1 = Report.FieldByName("colum1");
		var colum2 = Report.FieldByName("colum2");
		var colum3 = Report.FieldByName("colum3");
		var colum4 = Report.FieldByName("colum4");
		var colum5 = Report.FieldByName("colum5");
		var colum6 = Report.FieldByName("colum6");
		var colum7 = Report.FieldByName("colum7");
		var colum8 = Report.FieldByName("colum8");
		var colum9 = Report.FieldByName("colum9");
		var colum10 = Report.FieldByName("colum10");
		var colum11 = Report.FieldByName("colum11");
		var colum12 = Report.FieldByName("colum12");
//		2、加载明细类型的数据:
	    Report.PrepareLoadData();

			var url = '${ctx}/pms/deviceDetail/getDeviceDetailsJsonReport'; 
			var proCompanyId = document.getElementById("device.fees.company.id").value;
			var officeId = document.getElementById("officeId").value;
			var houseId = document.getElementById("device.house.id").value;
			var feesId = document.getElementById("feesId").value;
			var firstDate = document.getElementById("firstDate").value;
			var lastDate = document.getElementById("lastDate").value;
			var isPay  = document.getElementById("isPay").value;
			
//			loading('正在搜索，请稍等...'');

			jQuery('#searchForm').showLoading();
//	        $.getJSON(url,{model:'house',isPay:isPay,type:1,officeId:officeId,feesId:feesId,firstDate:firstDate,lastDate:lastDate},function(data){
			  $.getJSON(url,
					  {model:'house',
				  	  isPay:isPay,
				  	  type:1,
				  	  'device.type':2,
				  	  'device.fees.company.id':proCompanyId,
				  	  'device.house.owner.company.id':officeId,
				  	  'device.fees.id':feesId,
				  	  firstDate:firstDate,
				  	  lastDate:lastDate}
			  ,function(data){

				    for(var i =0;i<data.Detail.length;i++){
						//报表记录集对象
					    var Recordset = Report.DetailGrid.Recordset;
					    var j = data.Detail[i];
					    Recordset.Append();
					    colum1.Value = j.ownerName;
					    colum2.Value = j.houseFullName;
					    colum3.Value = j.feesName;
					    colum4.Value = j.lastDate;
					    colum5.Value = j.firstNum;
					    colum6.Value = j.LastNum;
					    colum7.Value = j.usageAmount;
					    colum8.Value = j.unitPrice;
					    colum9.Value = j.payMoney;
					    colum10.Value = j.poolPayMoney;
					    colum11.Value =j.sumPayMoney;
					    colum12.Value = j.incomeMoney;
					    

					    Recordset.Post(); 
				    }
				    var Master = data.Master;
				    
				   
//				    Report.ParameterByName("title").AsString = Master.title;
				    Report.ParameterByName("title").Value= Master.title;
				    Report.ParameterByName("company").Value= Master.company;
				    
				    
				    jQuery('#searchForm').hideLoading();
				    
				    Report.PrintPreview(true);
				
			});

			
	}	
	
	
	function print3(paymentBeforId){
		var temples_file = "${ctxStatic}/grid_report/grf/payDetail63.grf";

        Report.LoadFromURL(temples_file); //载入报表模板

		//报表记录集的各个字段
        var colum0 = Report.FieldByName("colum0");
		var colum1 = Report.FieldByName("colum1");
		var colum2 = Report.FieldByName("colum2");
		var colum3 = Report.FieldByName("colum3");
		var colum4 = Report.FieldByName("colum4");
		var colum5 = Report.FieldByName("colum5");
		var colum6 = Report.FieldByName("colum6");
		var colum7 = Report.FieldByName("colum7");
		var colum8 = Report.FieldByName("colum8");
		var colum9 = Report.FieldByName("colum9");
		var colum10 = Report.FieldByName("colum10");
		var colum11 = Report.FieldByName("colum11");
		var colum12 = Report.FieldByName("colum12");
		var colum13 = Report.FieldByName("colum13");
		var colum14 = Report.FieldByName("colum14");
		
		var colum99 = Report.FieldByName("colum99");
//		2、加载明细类型的数据:
	    Report.PrepareLoadData();

			var url = '${ctx}/pms/paymentAfter/getPayDetailJsonReport'; 
			var proCompanyId = document.getElementById("device.fees.company.id").value;
			var officeId = document.getElementById("officeId").value;
			var houseId = document.getElementById("device.house.id").value;
			var feesId = document.getElementById("feesId").value;
			var firstDate = document.getElementById("firstDate").value;
			var lastDate = document.getElementById("lastDate").value;
			var isPay  = document.getElementById("isPay").value;
//			var paymentBeforId = document.getElementById("paymentBeforId").value;


			jQuery('#searchForm').showLoading();

			  $.getJSON(url,
					  {model:'house',
				  	  isPay:isPay,
				  	  type:1,
				  	  'device.type':2,
				  	  'device.fees.company.id':proCompanyId,
				  	  'device.house.owner.company.id':officeId,
				  	  'device.fees.id':feesId,
				  	  paymentBeforId:paymentBeforId,
				  	  firstDate:firstDate,
				  	  lastDate:lastDate}
			  ,function(data){

				    for(var i =0;i<data.Detail.length;i++){
						//报表记录集对象
					    var Recordset = Report.DetailGrid.Recordset;
					    var j = data.Detail[i];
					    Recordset.Append();
					    colum0.Value = j.feeCode;
					    colum1.Value = j.houseFullName;
					    colum2.Value = j.companyName;
					    colum3.Value = j.receDate;
					    colum4.Value = j.ownerName;
					    colum5.Value = j.ownerCode;
					    colum6.Value = j.ownerMobile;
					    colum7.Value = j.ownerPhone;
					    
					    colum8.Value = j.feesName;
					    colum9.Value = j.firstDate;
					    colum10.Value = j.lastDate;
					    colum11.Value = j.usageAmount;
					    colum12.Value = j.unitPrice;
					    colum13.Value = j.sumPayMoney;
					    colum14.Value = j.incomeMoney;
					    
					    colum99.Value = j.ToWho;

					    Recordset.Post(); 
				    }
				    var Master = data.Master;
				    Report.ParameterByName("title").Value= Master.title;
				    jQuery('#searchForm').hideLoading();
				    Report.PrintPreview(true);
			});
	
	}	
	

	
	function printOffice(){
		
		var temples_file = "${ctxStatic}/grid_report/grf/printOffice20.grf";

        Report.LoadFromURL(temples_file); //载入报表模板

		//报表记录集的各个字段
		var colum1 = Report.FieldByName("colum1");
		var colum2 = Report.FieldByName("colum2");
		var colum3 = Report.FieldByName("colum3");
		var colum4 = Report.FieldByName("colum4");
		var colum5 = Report.FieldByName("colum5");
		var colum6 = Report.FieldByName("colum6");
		var colum7 = Report.FieldByName("colum7");
		var colum8 = Report.FieldByName("colum8");
		var colum9 = Report.FieldByName("colum9");
		var colum10 = Report.FieldByName("colum10");
		var colum11 = Report.FieldByName("colum11");
		var colum12 = Report.FieldByName("colum12");


//		2、加载明细类型的数据:
	    Report.PrepareLoadData();	

		
		var officeId = document.getElementById("officeId").value;
		var url = '${ctx}/pms/deviceDetail/getDetailJsonReportOffice'; 

		var details =[];
		$("input[name='test']:checked").each(function() { // 遍历选中的checkbox
			var fid = $(this).val()+'';
			eval('var firstDate_name = \'firstDate'+fid+'\'');
			eval('var lastDate_name = \'lastDate'+fid+'\'');
			var firstDate = document.getElementById(firstDate_name).value;
			var lastDate = document.getElementById(lastDate_name).value;
			var map = {'fid':fid,'firstDate':firstDate,'lastDate':lastDate};
			details.push(map);
		});

		var jsonData = {'officeId':officeId,'details':details};
		var JSONstr = JSON.stringify(jsonData);  

		if(details.length >0){
			jQuery('#searchForm').showLoading();
			 $.getJSON(url,{"details":JSONstr},function(data){
				 
//				 alert(data.Detail.length);
				 
				  for(var i =0;i<data.Detail.length;i++){
						//报表记录集对象
					    var Recordset = Report.DetailGrid.Recordset;
					    var j = data.Detail[i];
					    Recordset.Append();
					 
					    colum1.Value = j.user_login_name;
					    colum2.Value = j.company_code;
					    colum3.Value = j.house_pos_name;
					    colum4.Value = j.house_unit_name;
					    colum5.Value = j.house_num_floor;
					    colum6.Value = j.house_code;
					    colum7.Value = j.user_name;
					    colum8.Value = j.usage_amount;
					    colum9.Value = j.sum_pay_money;
					    colum10.Value = j.sum_income_money;
					    
					    colum11.Value = j.fees_name+j.payment_date;
//					    colum12.Value = j.payment_date;

					    Recordset.Post(); 
				    }
				    var Master = data.Master;
				    Report.ParameterByName("title").Value= Master.title;
				    jQuery('#searchForm').hideLoading();
				    Report.PrintPreview(true);
				 
			 });
		}

		
		
	}
	
	
	function initGrid(){

		mygrid = new dhtmlXGridObject('gridbox');
		mygrid.selMultiRows = true;
		mygrid.setImagePath("${ctxStatic}/dhtmlxTreeGrid/image/grid/");
		var flds = "<input type='checkbox' onclick='ckall(this)' />,费项,房屋,业主,上次读数,本次读数,用量,单价,本次付,公摊费,总应付,已付金额,缴费日期";
		mygrid.setHeader(flds);
		var columnIds = "inedx,feedName,house,owner,firstNum,lastNum,useNum,price,payMoney,poolPay,cost,incone,paydates";
		mygrid.setColumnIds(columnIds);
		
	    mygrid.setInitWidthsP("2,5,23,5,7,7,6,5,8,8,8,8,8");
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
            
       
			$("#costMoney2").val(ForDight(sumPayment-sumIncome,2));
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
		var url = '${ctx}/pms/deviceDetail/getDeviceDetailsJson'; 
		var officeId = document.getElementById("officeId").value;
		var houseId = document.getElementById("device.house.id").value;
		var feesId = document.getElementById("feesId").value;
		var firstDate = document.getElementById("firstDate").value;
		var lastDate = document.getElementById("lastDate").value;
		var isPay  = document.getElementById("isPay").value;
		var paymentBeforId = document.getElementById("paymentBeforId").value;
		var deviceType  = document.getElementById("device.type").value;
		
		if(deviceType == '') return false;
		
//		loading('正在搜索，请稍等...'');

		jQuery('#searchForm').showLoading();
//        $.getJSON(url,{model:'house',isPay:isPay,type:1,officeId:officeId,feesId:feesId,firstDate:firstDate,lastDate:lastDate},function(data){
		  $.getJSON(url,
				  {model:'house',
			  	  isPay:isPay,
			  	  type:1,
			  	  'device.type':deviceType,
			  	  'device.house.owner.company.id':officeId,
			  	  'device.fees.id':feesId,
			  	  paymentBeforId:paymentBeforId,
			  	  firstDate:firstDate,
			  	  lastDate:lastDate}
		  ,function(data){
//		$.getJSON(url,{model:'house',isPay:isPay,type:1,officeId:officeId,feesId:feesId},function(data){
			mygrid.clearAll();
			mygrid.loadXMLString(data.grid);
			mygrid.setSizes();	
			$("#costMoney").val(data.costMoney);
			$("#leaveMoney").val(data.preMoney);
			$("#leaveMoney").val(data.preMoney);
			
			$("#feeCode").val(data.feeCode); 
			$("#costMoneyLeave").val(data.costMoneyLeave);
			
			$("#officeName").val(data.officeName);
			$("#ownerNum").val(data.ownerNum);
			jQuery('#searchForm').hideLoading();
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
//				      rowVale = sumAccountMoney;
				      rowVale = sumAccountMoney+incomeMoney;
				    }

				  	sumAccountMoney -= costMoney;
			
					grid.cells2(i,colIndex).setValue(ForDight(rowVale,2));
					
//					if(sumAccountMoney <=0) break;
					if(rowVale <=0) {
						grid.cells2(i,colIndex).setValue('');
					}
					
					continue;
				 }
				
//				else{
//			   		grid.cells2(i,colIndex).setValue(ForDight(incomeMoney,2));
//				 }
				
			}
 	}
 	
 	

	function save(isPrint){
		
	    var costMoney2 = $("#costMoney2").val()*1;        //实收金额
 	    var payType  = $("#payType").val();               //收款方式
 	    var receDate =  $("#receDate").val(); 
 	    var officeId =  $("#officeId").val(); 
 	    var feeCode =  $("#feeCode").val(); 
 	    var certCode =  $("#certCode").val(); 
 	    var remarks =  $("#lastNum9").val(); 
 	   	var paymentBeforId = document.getElementById("paymentBeforId").value;
 	  	var costMoneyLeave =  $("#costMoneyLeave").val();   //发票余额
 	 
 	 
 	    if(costMoney2 >0){
 	    }else{
 	    	alert("无实收，不需要保存!"); return false;
 	    }
 	    
	    if(costMoneyLeave >0 && costMoney2 >costMoneyLeave){
	  	       alert("实收金额不能大于发票余额");return false;
	  	}

 	    var type = 1;                                     //1现付 2 预付 
 	    var url = '${ctx}/pms/deviceDetail/saveNew2';
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

        var data = {
        		officeId:officeId,
        		feeCode:feeCode,
        		certCode:certCode,
        		receDate:receDate,
        		payType:payType,
        		costMoney2:costMoney2,
        		payemtDetails:detailsIncome,
        		paymentBeforId:paymentBeforId,
        		remarks:remarks
        		};
        
      if(detailsIncome =="") return false;  
   	
   		$.ajax({
                type:"post",
                url:url,
                data:data,
                datetype:'text',
                success:function(result){
                	var paymentBeforId = result;                	
                	if(isPrint){
                		print3(paymentBeforId);
                	}else{
                		 alert("保存成功!");
                	}
                     
                    getPaymentDetails();
                },
               error : function(){
            	   alert("保存出错了");
                  getPaymentDetails();
              }
            });	
            
	

 	}
		
		
		
	</script>
</head>
<body>




	<ul class="nav nav-tabs">
		<li class="active"><a id="tabs1" href="#">缴费明细</a></li>
		
		<li id="divdisplay"><a id="tabs2" href="#">预付款</a></li>
		
		<li><a id="tabs3" href="#">付款历史</a></li>
	</ul>

	<tags:message content="${message}"/>
	<form:form id="searchForm" modelAttribute="deviceDetail" action="${ctx}/pms/deviceDetail/" method="post" class="breadcrumb form-search">
			<form:hidden path="device.house.id"/>
			<form:hidden path="officeId"/>
			<form:hidden path="device.fees.company.id"/>
			<form:hidden path="paymentBeforId"/>
			<form:hidden path="device.type"/> 
		
			<label class="control-label">费项:</label>
			<form:select id="feesId" name="feesId" path="device.fees.id"  style="width:95px;">
						<form:option value="" label=""/>
						<form:options items="${feesList}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>			

          
	           
			<label class="control-label">开始:</label>
			<input id="firstDate" name="firstDate" type="text" readonly="readonly" maxlength="20" onClick="WdatePicker({})" 
							value="<fmt:formatDate value="${deviceDetail.firstDate}" pattern="yyyy-MM-dd"/>"  style="width:90px;" class=" Wdate input-small"/>	
	
			<label class="control-label">结束:</label>
			<input id="lastDate" name="lastDate" type="text" readonly="readonly" maxlength="20" onClick="WdatePicker({})" 
							value="<fmt:formatDate value="${deviceDetail.lastDate}" pattern="yyyy-MM-dd"/>"  style="width:90px;" class=" Wdate input-small"/>	
		
		     
		     <!-- 
			<label class="control-label">收费项目:</label>
			<form:select id="feesId" name="feesId" path="device.fees.id" class="input-small text medium;required">
						<form:option value="" label=""/>
						<form:options items="${feesList}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>	
			-->

			<select id="isPay" style="width:80px">
			  <option value='1'>有欠款</option>
			  <option value='2'>已付</option>
			  <option value='0'>所有</option>
			</select>
			
			<input id="btnQuery" class="btn btn-primary" type="button" value="检 索" onclick="getPaymentDetails()"/>
			<input id="btnExport" class="btn btn-primary" type="button" value="导 出"/>
			<input id="btnPrint2" class="btn btn-primary" type="button" value="打印"  onclick="print2()"/>&nbsp;
			<input id="btnPrint3" class="btn btn-primary" type="button" value="打印2" />&nbsp;
			

			

	 		<c:if test="${ empty id}"> </c:if>    

			<div class="controls">
					<div id="gridbox" width="100%" height="30%" style="background-color:white;z-index:0"></div>
			</div>



		<div class="form-actions">
		
				<div class="control-group">
					<label class="control-label">单位名称:</label><input id="officeName" class="required" type="text" value="${deviceDetail.device.house.owner.company.name}" readOnly=1/>
					<label class="control-label">户数:</label><input id="ownerNum" style="width:50px" class="required input-small"  value=""  readOnly=1/>
					<label class="control-label">欠费合计:</label><input id="costMoney" htmlEscape="false"   class="required input-small" readonly="true"/>
				</div> 		
				
				<div class="control-group">
					<label class="control-label">收据编号:</label><input id="feeCode" htmlEscape="false" class="required input-small" readonly="true"/>
					<label class="control-label">发票编号:</label><input id="certCode" htmlEscape="false" class="required input-small"/>
					<label class="control-label">实收金额:</label><input id="costMoney2" htmlEscape="false" class="required input-small" />
				</div>	
			

			
			<div class="control-group">
				<label class="control-label">发票余额:</label><input id="costMoneyLeave" htmlEscape="false" class="required input-small" readonly="true"/>
			
				
				
				<label class="control-label">收款方式:</label>
				<select id="payType" name="payType" class="required input-small">
					<c:forEach items="${fns:getDictList('pms_pay_type')}" var="item">
							<option value ="${item.value}">${item.label}</option>
					</c:forEach>	
				</select>		
				
				<label class="control-label">收款日期:</label>
				<input id="receDate" name="receDate" type="text" readonly="readonly" maxlength="20" onClick="WdatePicker({})" 
								value="<fmt:formatDate value="${deviceDetail.lastDate}" pattern="yyyy-MM-dd"/>" style="width:110px;"  class=" Wdate input-small"/>					
			</div>			
			
			
			<div class="control-group">
				<label class="control-label">备注说明:</label><input id="lastNum9" htmlEscape="false" class="required  input-normal"/>
					<shiro:hasPermission name="pms:payemtDetail:edit"></shiro:hasPermission>
				<input id="btnSubmit" class="btn btn-primary" type="button" value="保 存"  onclick="save(false)"/>&nbsp;
				<input id="btnPrint" class="btn btn-primary" type="button" value="保 存并打印"  onclick="save(true)"/>&nbsp;
			</div>		
			
			

		</div>  

		</form:form>	
		
		
	

		
		<script type="text/javascript">
		Install_InsertReport();
		var Installed = Install_Detect();
		if ( Installed )
			CreateReport("Report");
		</script>

</body>
</html>
