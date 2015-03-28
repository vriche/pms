<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>


<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dhtml.jsp" %>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<%@include file="/WEB-INF/views/include/showLoading.jsp" %>
	<%@include file="/WEB-INF/views/include/gridReport.jsp" %>
	<%@include file="/WEB-INF/views/include/multiSelect.jsp" %>
	
	<script type="text/javascript">
	
	(function($){
		$.getUrlParam = function(name)
		{
			var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if (r!=null) return unescape(r[2]); return null;
		}
	})(jQuery);
	
		$(document).ready(function() {

			$("#feesId").change(function (e) {getPaymentDetails();});  
			$("#isPay").change(function (e) {getPaymentDetails();});  		 

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
//				alert(99)
			   if($("#useLeave").is(':checked')){
				    var costMoneyLeave = $("#costMoneyLeave").val()*1;
				    var costMoney2 = $("#costMoney2").val()*1;
			    	if(costMoneyLeave >= costMoney2){
				      	   alert("发票余额大于应付金额不需要使用余额.");
				      	     $("#useLeave").attr("checked", false);  
				      	 // $("#costMoney3").val("");
				      	    $("#leaveMoneyOut").val("");
				      	  return false;
				    }
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
				var houseId = document.getElementById("device.house.id").value;
				var proCompanyId = document.getElementById("device.fees.company.id").value;
				var officeId = document.getElementById("officeId").value;
				var deviceType = document.getElementById("device.type").value;
				
				var href = "${ctx}/pms/deviceDetail/form3?officeId="+ officeId +"&device.house.id="+ houseId +"&device.fees.company.id="+ proCompanyId+"&feesId="+ feesId+"&device.type="+deviceType;
				location.href =href;
			})
			
			$("#tabs2").click(function(){
				var houseId = document.getElementById("device.house.id").value;
				var proCompanyId = document.getElementById("device.fees.company.id").value;
				var officeId = document.getElementById("officeId").value;
				var payFrom = houseId>0?2:1; 
				var deviceType = document.getElementById("device.type").value;
				location.href = "${ctx}/pms/paymentBefor/list2?type=2&house.id="+houseId +"&company.id="+ officeId +"&device.fees.company.id="+proCompanyId+"&payFrom="+payFrom+"&device.type="+deviceType;;
			})	
			

			$("#tabs3").click(function(){
				var houseId = document.getElementById("device.house.id").value;
				var proCompanyId = document.getElementById("device.fees.company.id").value;
				var officeId = document.getElementById("officeId").value;
				var deviceType = document.getElementById("device.type").value;
				location.href = "${ctx}/pms/paymentBefor/list2?type=1&payFrom=2&house.id="+houseId +"&company.id="+officeId +"&device.fees.company.id="+proCompanyId+"&device.type="+deviceType;; 
			})				

		
			//document.getElementById('house.fullName').readonly= true;
			
			
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
			
			
			$("#btnPrint3").click(function(){
				var proCompanyId = document.getElementById("device.fees.company.id").value;
				
//				alert(proCompanyId)
				
				top.$.jBox.open("iframe:${ctx}/pms/deviceDetail/usertorole?id="+proCompanyId, "请选择",810,$(top.document).height()-240,{
					buttons:{"确定":"ok", "清除已选":"clear", "关闭":true}, bottomText:"通过选择部门，然后为列出的人员。",submit:function(v, h, f){

						var pre_ids = h.find("iframe")[0].contentWindow.pre_ids;
						var ids = h.find("iframe")[0].contentWindow.ids;

						//nodes = selectedTree.getSelectedNodes();
						if (v=="ok"){
							// 删除''的元素
							if(ids[0]==''){
								ids.shift();
								pre_ids.shift();
							}
							if(pre_ids.sort().toString() == ids.sort().toString()){
//								top.$.jBox.tip("未给角色【${role.name}】分配新成员！", 'info');
								return false;
							};
					    	// 执行保存
//					    	loading('正在提交，请稍等...');
					    	var idsArr = "";
					    	for (var i = 0; i<ids.length; i++) {
					    		idsArr = (idsArr + ids[i]) + (((i + 1)== ids.length) ? '':',');
					    	}
					    	var fids = h.find("iframe")[0].contentWindow.getFids();
					    	if(fids.length >0 && ids.length >0){
					    		var fidsArr = fids.join(",");
					    		print2(fidsArr,idsArr);
					    	}
//					    	$('#assignRoleForm').attr('action','${ctx}/sys/role/assignrole?id=${role.id}&idsArr='+idsArr).submit();
					    	return true;
						} else if (v=="clear"){
							h.find("iframe")[0].contentWindow.clearAssign();
							return false;
		                }
					}, loaded:function(h){
						$(".jbox-content", top.document).css("overflow-y","hidden");
					}
				});			
				
				
				
			});
			
			
			
//			$("#btnPrint3").click(function(){
//				var selectCmdHTML ="";
//					$("select[name='device.fees.id'] option").each(function(){
//						if($(this).text() !=''){
//							var fid = $(this).val();
//							var text = $(this).text();
//							selectCmdHTML += "<label>";
//							selectCmdHTML += "<input type=\"checkbox\" name=\"someBox\" value=\""+ fid +"\" />" + text +"&nbsp;&nbsp;&nbsp;";
//							selectCmdHTML += "</label> ";
//						
//						}
//					});				
//
//				var html = "<table id=\"contentTableWhere\" class=\"table-bordered  table-striped table-hover\">";
//						 html +="<tr>";
//						 html +="<td style=\"text-align:left;\">费用   <label><input type=\"checkbox\" name=\"checkboxAll\"  id=\"checkboxAll\">全选</label></td>";
//						 html +="</tr>";
//						 html +="<tr>";
//						 html +="<td>"+ selectCmdHTML +"</td>";
//						 html +="</tr>";
//						 html +="<tr>";
//						 html +="<td></td>";
//						 html +="</tr>";
//						 html +="<tr>";
//						 html +="<td><a id=\"assignButton\" href=\"javascript:assignButton()\" class=\"btn btn-primary\">分配角色</a></td>";
//						 html +="</tr>";
//
//				 html +="</table>";
//
//				$.jBox(html, {
//					title:'个人明细', 
//					buttons:{ '确  定': 1, '关   闭': 0 }, 
//					width:400,
//					bottomText:"",
//					submit: function (v, h, f) {
//						 if(v == 1){
//							 printOffice();
//						 }else{ 
//							 return true;
//						 }
//					}
//				});
//				
//
//			});  						
			
			
			
			
			
			
			
			
			
		});
		
	
		
		function getDetailByLoginName() {
			
			var url = '${ctx}/pms/user/getHousesByUser'; 
			var loginName = $("#longinName").val();
			

			function setHiddenValue(data,i){
				var houseId = data[i].houseId;
				var officeId =  data[i].officeId;
				var deviceType =  data[i].deviceType;
				var houseFullName =  data[i].houseFullName;
				var ownerName =  data[i].ownerName;

				document.getElementById("device.house.id").value = houseId;
				document.getElementById("officeId").value = officeId;
				document.getElementById("device.type").value = deviceType; 

            	document.getElementById("device.house.fullName").value = houseFullName;
            	document.getElementById("device.house.owner.name").value = ownerName;
            	
				
				getPaymentDetails();
			}
			
			
			$.getJSON(url,{loginName: loginName},function(result){
				if(result.count > 0){
				
					if(result.count == 1){
						var houses = eval(result.data);
						setHiddenValue(houses,0)
					}else{
						alert("此业主有多套房子");
						
					}
				}else{
					alert("找不到此业主");
				}

			});		
			
		} 			 
			
		
		function query(){
			var longinName = $("#longinName").val();
			if(longinName != ''){
				getDetailByLoginName();
			}else{
				getPaymentDetails();
			}
		}
		
	
		
	function exportExcel(){
//		    alert($("#grid_xml").innerHTML());
		    
//		    alert(mygrid.toExcel);
//		    mygrid.toExcel("/servlet/dhtmlExcelGeneratorServlet.jsp");
//		    mygrid.toExcel("http://127.0.0.1:9090/pms/servlet/dhtmlExcelGeneratorServlet");
//			location.href = "${ctx}/pms/deviceDetail/exportExcel"; 
//		    var url = "${ctx}/deviceDetail/exportExcel";
//		    alert(url);
//		    mygrid.toExcel(url);
		    
		    
//			$("#searchForm").attr("action","${ctx}/pms/deviceDetail/exportExcel");
//			$("#searchForm").attr("enctype","application/x-www-form-urlencoded");
//			$("#searchForm").attr("onsubmit","loading('正在初始化，请稍等...');");
//			$("#searchForm").submit();
		
		
		var url = '${ctx}/pms/deviceDetail/exportExcel2'; 
		var houseId = document.getElementById("device.house.id").value;
		var feesId = document.getElementById("feesId").value;
		var firstDate = document.getElementById("firstDate").value;
		var lastDate = document.getElementById("lastDate").value;
		var isPay  = document.getElementById("isPay").value;
		
		var data = {model:'house',isPay:isPay,type:2,houseId:houseId,firstDate:firstDate,lastDate:lastDate,feesId:feesId};
		
//		jQuery('#searchForm').showLoading();
//
//		$.ajax(url,data,async:false,function(data){
//			jQuery('#searchForm').hideLoading();
//		});	
		
		$.ajax({
		     type: 'POST',
		     url: url,
		     data: data
		});
			
	}
	
	function print2(fids,userids){
		var temples_file = "${ctxStatic}/grid_report/grf/normal26.grf";
		
		if(fids){
			temples_file = "${ctxStatic}/grid_report/grf/normalB28.grf";
		}
		
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
		var colum13;
		if(fids){
			colum13 = Report.FieldByName("colum13");
		}
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
			
		
			
			if(fids){
				feesId = null;
				houseId = null;
				officeId = null;
			}else{
				fids = null;
				userids= null;
			}

			
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
				  	  'device.house.id':houseId,
				  	   fids:fids,
				  	   userids:userids,
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
					    
					    if(fids){
					    	colum13.Value = j.officeName;
					    }
					    
					    
					    

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
	function initGrid(){
 
			mygrid = new dhtmlXGridObject('gridbox');
			mygrid.selMultiRows = true;
			mygrid.setImagePath("${ctxStatic}/dhtmlxTreeGrid/image/grid/");
			var flds = "<input type='checkbox' onclick='ckall(this)' />,费项,上次读数,本次读数,用量,单价,本次付,公摊费,总应付,已付金额,缴费日期";
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
	    var costMoneyLeave =  $("#costMoneyLeave").val();   //发票余额
	    var leavesave = 0;                                                  
	    var leaveMoneyOut_temp = 0;
	    
	 
	    
	    if(costMoney2 <= 0){
	         alert("应付金额必须大于0.");
	    	 return false;
	     }
	    
	    if($("#useLeave").is(':checked')){
	    	
	    	if(costMoneyLeave >= costMoney2){
		      	   alert("发票余额大于应付金额不需要使用余额.");
		      	     $("#useLeave").attr("checked", false);  
		      	 // $("#costMoney3").val("");
		      	    $("#leaveMoneyOut").val("");
		      	  return false;
		    }
	    	
	    	
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
		var url = '${ctx}/pms/deviceDetail/getDeviceDetailsJson'; 
		var houseId = document.getElementById("device.house.id").value;
		var feesId = document.getElementById("feesId").value;
		var firstDate = document.getElementById("firstDate").value;
		var lastDate = document.getElementById("lastDate").value;
		var isPay  = document.getElementById("isPay").value;
		var longinName = document.getElementById("longinName").value;
		var paymentBeforId = document.getElementById("paymentBeforId").value;
		var deviceType  = document.getElementById("device.type").value;
		
		if(deviceType == '') return false;
		
//		alert('houseId>>>>'+houseId)
//	alert('feesId>>>>'+feesId)
//	alert('houseId>>>>'+houseId)
//	alert('firstDate>>>>'+firstDate)
//	alert('lastDate>>>>'+lastDate)
//	alert('isPay>>>>'+isPay)
//	alert('longinName>>>>'+longinName)
	
		var data = {
				model:'house',
				isPay:isPay,
				type:2,
				'device.type':deviceType,
				firstDate:firstDate,
				lastDate:lastDate,
				'device.house.id':houseId,
//				'device.house.owner.longinName':longinName,
				 paymentBeforId:paymentBeforId,
				'device.fees.id':feesId
				};
		
		jQuery('#searchForm').showLoading();
		$.getJSON(url,data,function(data){
			mygrid.clearAll();
			mygrid.loadXMLString(data.grid);
			mygrid.setSizes();	
			$("#costMoney").val(data.costMoney);
			$("#leaveMoney").val(data.preMoney);
			
			$("#feeCode").val(data.feeCode); 
			$("#costMoneyLeave").val(data.costMoneyLeave);
			
			
		    $("#leaveMoneyOut").val(0);
		    $("#costMoney2").val(0);
			$("#costMoney3").val(0);
			jQuery('#searchForm').hideLoading();
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
			
//					if(sumAccountMoney <=0) break;
					
				
					if(rowVale <=0) {
						grid.cells2(i,colIndex).setValue('0');
					}
					
					continue;
				 }
			}
 	}
 	
 	

	function save(isPrint){
		var officeId= document.getElementById("officeId").value;
		var houseId = document.getElementById("device.house.id").value;
	    var costMoney2 = $("#costMoney2").val()*1;        //应付金额
 		var leaveMoneyOut  = $("#leaveMoneyOut").val()*1; //余额支出
	    var costMoney3  = $("#costMoney3").val()*1; 	  //实收金额
 	    var payType  = $("#payType").val();               //收款方式
 	    var receDate =  $("#receDate").val(); 
 	    var feeCode  = $("#feeCode").val();               //收款单号
 	    var certCode  = $("#certCode").val();               //发票号
 	   var remarks =  $("#lastNum9").val();                //备注
 	   var paymentBeforId = document.getElementById("paymentBeforId").value;
 	   var costMoneyLeave =  $("#costMoneyLeave").val();   //发票余额
 	   
	    if(costMoneyLeave >0 && costMoney3 >costMoneyLeave){
	  	       alert("实收金额不能大于发票余额");return false;
	  	}
	    
 	    if(costMoney3 ==0 && leaveMoneyOut ==0){
  	       alert("账户余额不足或没有填写实际应收款.");return false;
  	    }
 	   
 	    var lle = costMoney3 - costMoney2;
 	    if(lle >0){
 	    	alert("系统将自动把多付的"+ ForDight(lle,2) +"元，转入到预存帐户");
 	    }
 	    

 	    
 
 	   
 	    
 	    var type = 2;                                     //1现付 2 预付 
 	    var url = '${ctx}/pms/deviceDetail/saveNew';
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
//				    alert(detailsIncome);
				}		
		}	
		


        var data = {
        		officeId:officeId,
        		houseId:houseId,
        		feeCode:feeCode,
        		certCode:certCode,
        		receDate:receDate,
        		payType:payType,
        		leaveMoneyOut:leaveMoneyOut,
        		costMoney2:costMoney2,
        		costMoney3:costMoney3,
        		payemtDetails:detailsIncome,
        		paymentBeforId:paymentBeforId,
        		remarks:remarks
        		};
   		// var data = {houseId:houseId,receDate:receDate,payType:payType,leaveMoneyOut:leaveMoneyOut,costMoney2:costMoney2,costMoney3:costMoney3,unitPrice:11};
        if(detailsIncome =="") return false;  
   		$.ajax({
                type:"post",
                url:url,
                data:data,
                datetype:'text',
                //datetype:'json',
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
                  alert("错了");
                  getPaymentDetails();
              }
            });	

 	}
	
	
//	var treeObj = $.fn.zTree.getZTreeObj("tree");
//	var nodes = treeObj.getNodes();
//	if (nodes.length>0) {
//		treeObj.selectNode(nodes[0]);
//	}
		
		
		
	</script>
</head>
<body>

	<ul class="nav nav-tabs">
		<li class="active"><a id="tabs1" href="#">缴费明细</a></li>
		<li id="divdisplay"><a id="tabs2" href="#">预付款</a></li>
		<li><a id="tabs3" href="#">付款历史</a></li>
	</ul>

	
    
 
	<form:form id="searchForm" modelAttribute="deviceDetail" action="${ctx}/pms/deviceDetail/" method="post" class="breadcrumb form-search">
			
			<form:hidden path="device.house.id"/>
			<form:hidden path="officeId"/>
			<form:hidden path="device.fees.company.id"/> 
			<form:hidden path="paymentBeforId"/>
			<form:hidden path="device.type"/> 
			
			
			
	
		
		
			<label class="control-label">费项:</label>
			<form:select id="feesId" name="feesId" path="device.fees.id"  style="width:95px;" >
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

			
			<label class="control-label">业主:</label>
			<form:input id="longinName" name="longinName" path="device.house.owner.loginName" htmlEscape="false" maxlength="200" style="width:80px;"  class="required"/>

			
			<select id="isPay" style="width:80px">
			  <option value='1'>有欠款</option>
			  <option value='2'>已付</option>
			  <option value='0'>所有</option>
			</select>
			
		

			<input id="btnQuery" class="btn btn-primary" type="button" value="检索" onclick="query()"/>
			<!-- input id="btnExportExcel" class="btn btn-primary" type="hidden" value="excel" onclick="exportExcel()"/ -->
			<input id="btnExport" class="btn btn-primary" type="button" value="导出" />
			<input id="btnPrint2" class="btn btn-primary" type="button" value="打印"  onclick="print2()"/>&nbsp;
			<input id="btnPrint3" class="btn btn-primary" type="button" value="打印2" />&nbsp;

			
			

	 		<c:if test="${ empty id}"> </c:if>    

			<div class="controls">
					<div id="gridbox" width="100%" height="30%" style="background-color:white;z-index:0"></div>
			</div>





		<div class="form-actions">
			<div class="control-group"> 
					<label class="control-label">房间编号:</label><form:input path="device.house.fullName"  style="width:112px;" class="required input-small"  readonly="true" />
					<label class="control-label">业主姓名:</label><form:input path="device.house.owner.name"  style="width:112px;" class="required input-small" readonly="true"/>
					<label class="control-label">欠费合计:</label><input id="costMoney" htmlEscape="false"  style="width:112px;" class="required input-small" readonly="true"/>
			</div> 		
	
			<div class="control-group">
				<label class="control-label">帐户余额:</label><input id="leaveMoney" htmlEscape="false" class="required input-small" readonly="true"/>
				<label class="control-label">余额支出:</label><input id="leaveMoneyOut" htmlEscape="false" class="required input-small" readonly="true"/>
				<label class="control-label">应付金额:</label><input id="costMoney2" htmlEscape="false" class="required input-small" readonly="true"/>
			</div> 	
			
			<div class="control-group">
				<label class="control-label">收据编号:</label><input id="feeCode" htmlEscape="false" class="required input-small"  readonly="true"/>
				<label class="control-label">发票编号:</label><input id="certCode" htmlEscape="false" class="required input-small"/>
				<label class="control-label">实收金额:</label><input id="costMoney3" htmlEscape="false" class="required input-small"/>
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
			<label class="control-label">备注说明:</label><input id="lastNum9" htmlEscape="false" class="required  input-small"/>
			<label class="control-label" for="useLeave">使用预付:</label>
			<input  name="useLeave" id="useLeave" type='checkbox' value=0/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="button" value=" 保   存 "  onclick="save()"/>&nbsp;	
			<input id="btnPrint" class="btn btn-primary" type="button" value="保 存并打印"  onclick="save(true)"/>&nbsp;
		</div>			
			
			
			
			<div class="control-group">

				<input id="btnAccount" class="btn" type="hidden" value=" 计  算 " onclick="getAccount()"/>	
				
				<shiro:hasPermission name="pms:deviceDetail:edit"></shiro:hasPermission>
				
		
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
