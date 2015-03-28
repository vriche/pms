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

       var houseId;
	   var type;
	   var officeId;
		    
		$(document).ready(function() {

			
			 type = document.getElementById("type").value;
			 officeId = document.getElementById("company.id").value;
			 houseId = document.getElementById("house.id").value;
			  
	
		
			$("#contentTable > thead").find("tr").each(function(){
				　　$(this).find("th").css("text-align","center");
			})
			
//			$("#contentTable > tbody").find("tr").each(function(){
//			　　$(this).find("td:first-child").css("text-align","right");
//			　　$(this).find("td:eq(4)").css("text-align","right");
//		　　})
  
			 $("#payFrom").change(function (e) {
				 $("#searchForm").submit();
			 	}
			 );  
			
			
//			$(#payFrom).toggle(1,function(){},false)
		
			 if(houseId >0){
				 $("#tabs2").show();
	
			 }else{
				 $("#tabs2").hide();
			 }
			
			 $("#tabs1").attr("class","");
			 $("#tabs2").attr("class","");
			 $("#tabs3").attr("class","");
			 
			 	
			 
			
			 if(type ==2){
					$("#tabs2").attr("class","active");
					 $("#btnNewt").show();
					 $("#btnPrint3").hide(); 
			 }else{
					$("#tabs3").attr("class","active");
					 $("#btnNewt").hide(); 
					 $("#btnPrint3").show();
			 }
			 
			 
			
			$("#tabs1").click(function(){
			     proCompanyId = $.getUrlParam('device.fees.company.id');
			     var deviceType = $.getUrlParam('device.type');
			    

			     var type = document.getElementById("type").value;
			     var houseId = document.getElementById("house.id").value;
			     var officeId = document.getElementById("company.id").value;

			     if(houseId >0){
			    	 location.href ="${ctx}/pms/deviceDetail/form3?officeId="+officeId +"&device.fees.company.id="+ proCompanyId+"&device.house.id="+ houseId +"&device.type="+deviceType;;
				 }else{
					 location.href ="${ctx}/pms/deviceDetail/form2?officeId="+officeId +"&device.fees.company.id="+ proCompanyId+"&device.house.id="+ houseId +"&device.type="+deviceType;
				 }
			    
			})	
			

			
			$("#tabs2").click(function(){
				 proCompanyId = $.getUrlParam('device.fees.company.id');
				   var type = document.getElementById("type").value;
				   var houseId = document.getElementById("house.id").value;
				   var officeId = document.getElementById("company.id").value;
				   var payFrom = houseId>0?2:1
					var deviceType = $.getUrlParam('device.type');
			
				     
				location.href = "${ctx}/pms/paymentBefor/list2?type=2&house.id="+houseId +"&company.id="+officeId+"&device.fees.company.id="+proCompanyId+"&payFrom="+payFrom+"&device.type="+deviceType;;
			})	
			

			$("#tabs3").click(function(){
//				var houseId = document.getElementById("device.house.id").value;
				var houseId = $.getUrlParam('house.id');
				var proCompanyId = $.getUrlParam('device.fees.company.id');
				var houseId = document.getElementById("house.id").value;
				var officeId = document.getElementById("company.id").value;
			    var deviceType = $.getUrlParam('device.type');
				location.href = "${ctx}/pms/paymentBefor/list2?type=1&house.id="+houseId +"&company.id="+officeId+"&device.fees.company.id="+proCompanyId+"&device.type="+deviceType;;
			})	
			
			
			$("#btnNewt").click(function(){
//				 houseId = $.getUrlParam('house.id');
				 proCompanyId = $.getUrlParam('device.fees.company.id');
				 var houseId = document.getElementById("house.id").value;
				 var officeId = document.getElementById("company.id").value;
				 var payFrom = document.getElementById("payFrom").value;
	
				 openWin(0,officeId,houseId,payFrom)	
			});	
	
		});
		
		
		function gotoPay(paymentBeforId,houseId){
			 var officeId = document.getElementById("company.id").value;
			 var proCompanyId = $.getUrlParam('device.fees.company.id');
			 var url = "";
		     if(houseId >0){
		    	 url ="${ctx}/pms/deviceDetail/form3?officeId="+officeId +"&device.fees.company.id="+ proCompanyId+"&device.house.id="+ houseId;
			 }else{
				 url ="${ctx}/pms/deviceDetail/form2?officeId="+officeId +"&device.fees.company.id="+ proCompanyId+"&device.house.id="+ houseId;
			 }
		     
		     url = url+"&paymentBeforId="+paymentBeforId;
		     
		     location.href = url;
		}
		
		
		function deletePaymentDetail(id,deviceDetailId){
		    var statu = confirm("请确认是否删除?");
	        if(!statu){
	            return false;
	        }else{
	          var url = "${ctx}/pms/paymentBefor/delete";
	         
			  $.ajax({ type:'POST', url:url,data:{'id':id,'deviceDetailId':deviceDetailId},success:function(data){
			  	  	 getPaymentDetails(houseId,type);  
					 
			   } ,error:function(XMLHttpRequest, textStatus, errorThrown) alert("删除失败") })				       
			 
	        }
		}

		
		function openWin(paymentBeforId,officeId,houseId,payFrom){
			
		

			    var deviceType = $.getUrlParam('device.type');
				var url = "${ctx}/pms/paymentBefor/form?type="+ type+"&company.id="+ officeId+"&payFrom="+ payFrom +"&house.id="+houseId +"&id=";

				var callHandler = function (v, h, f) {
				    var frm = $.jBox.getIframe("123").contentDocument.getElementById("inputForm");
				    if (v == '1') {
				    	var recMoney = frm.recMoney.value;
				    
				    	frm.submit();
				    	alert("保存完毕");
				    	 $.jBox.close();
//				        frm.submit();
//				        $.jBox.alert("保存完毕"); 
//				        $.jBox.close();
//				        getPaymentDetails(houseId,type);  
				        
				    	var houseId = $.getUrlParam('house.id');
						var proCompanyId = $.getUrlParam('device.fees.company.id');
						var officeId = $.getUrlParam('company.id');
						location.href = "${ctx}/pms/paymentBefor/list2?type=2&house.id="+houseId+"&company.id="+officeId +"&device.fees.company.id="+proCompanyId+"&payFrom="+ payFrom+"&device.type="+deviceType;
				    }
				    
//				    if (v == '2') {
//				        deletePaymentDetail(id,deviceDetailId);				       
//				    }
//	
				   return true;
				};
				
    
				$.jBox("iframe:"+ url, {
				    title: "收款明细",
				    width: 800,
				    height: 420,
				    id:"123",
				    buttons: { '保存': 1 , '关闭': 0 },
				    //closed:closed,
				    submit:callHandler
				});	
		
		}
		
		function openWin2(payFrom,paymentBeforId,officeId,houseId){
		    var deviceType = $.getUrlParam('device.type');
			var url = "${ctx}/pms/paymentAfter/listPayDetail?paymentBefor.id="+ paymentBeforId +"&paymentBefor.type="+ type+"&paymentBefor.company.id="+ officeId +"&paymentBefor.house.id="+houseId;
			var delUrl = "${ctx}/pms/paymentAfter/deleteBatch";
			function closed(){
				$.jBox.close();
		    	var houseId = $.getUrlParam('house.id');
				var proCompanyId = $.getUrlParam('device.fees.company.id');
				var officeId = $.getUrlParam('company.id');
				var payFrom = $.getUrlParam('payFrom');
			
				location.href = "${ctx}/pms/paymentBefor/list2?type=1&payFrom="+ payFrom +"&house.id="+houseId+"&company.id="+officeId +"&device.fees.company.id="+proCompanyId+"&device.type="+deviceType;
		   
			}
			var callHandler = function (v, h, f) {
//			    var frm = $.jBox.getIframe("123").contentDocument.getElementById("inputForm");
			    if (v == '0') {
			    	 var result = $.jBox.getIframe("123").contentWindow.window.getAllChecked();
			    	 if(result.length == 0) return false;
			    	 var statu = confirm("请确认是否删除?");
			    	 if(statu){
				    		  var idss = result.join(",");
							  $.ajax(
									  { 
										  type:'POST',
										  url:delUrl,
										  data:{'idss':idss},
										  success:function(data){
											  	$.jBox.getIframe("123").contentWindow.window.removeAll();
										  } ,
										  error:function(XMLHttpRequest, textStatus, errorThrown) alert("删除失败") 
									   }
							 )			    			  
			    		

								   
								   
			    	 }	//	end if(statu)	    	 
			    	 return false;
			    }
			    
			    if (v == '1') {
			     	closed();
			    }
			    
			    
			   return true;
			};
			

			$.jBox("iframe:"+ url, {
			    title: "付款明细",
			    width: 800,
			    height: 420,
			    id:"123",
			    buttons: { '删除': 0,'关闭': 1 },
			    closed:closed,
			    submit:callHandler
			});	
	
	}	
		
		
		
function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
}
        
 function initGrid(){

			mygrid = new dhtmlXGridObject('gridbox');
			mygrid.selMultiRows = true;
			mygrid.setImagePath("${ctxStatic}/dhtmlxTreeGrid/image/grid/");
			var flds = "序,收款单号,发票号 ,收款日期,收款金额,收款方式,收款人,操作";
			mygrid.setHeader(flds);
			var columnIds = "inedx,feeCode,cerCode,price,firstNum,lastNum,cost,incone,paydates";
			mygrid.setColumnIds(columnIds);
			
		    mygrid.setInitWidthsP("2,15,15,15,15,10,20,8");
			mygrid.setColAlign("center,center,right,right,right,right,right,center");
			mygrid.setColTypes("ed,ed,ed,ed,ed,ed,ed,ed,ed");
		    
		    mygrid.setMultiLine(false);
			mygrid.setEditable(false);
		    mygrid.setSkin("modern2");
		    mygrid.setColSorting("na,str,str,str,str,int,co,int,int,int") ;
		    mygrid.enableAlterCss("even","uneven"); 
		
			mygrid.init();	 
			mygrid.setSortImgState(true,1,"ASC"); 
			mygrid.setOnRowDblClickedHandler(RowDblClickedHandler);
			//mygrid.attachFooter('合计:, , , , , , ',['text-align:center;','text-align:right;','text-align:right;','text-align:right;','text-align:right;','text-align:right;','text-align:right;']);
			//mygrid.style.height = gridbox.offsetHeight  +"px";	
			mygrid.setSizes();	

	}		
	       
	function RowDblClickedHandler(rowId,ellIndex){
	       var deviceDetailId = this.getUserData(rowId,"deviceDetailId");
           openWin(rowId,deviceDetailId);
	}     
	function getPaymentDetails(houseId,type){
		var url = '${ctx}/pms/paymentBefor/getPaymentAfterJson';
		var feeCode = $("#feeCode").val();

		$.getJSON(url,
				  {model:'house',
			  	  isPay:1,
			  	  type:type,
			  	  'device.house.id':houseId,
			  	  feeCode:feeCode}
		  ,function(data){
				mygrid.clearAll();
				mygrid.loadXMLString(data.grid);
				mygrid.setSizes();	
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
		var proCompanyId = $.getUrlParam('device.fees.company.id');
		var firstDate = document.getElementById("firstDate").value;
		var lastDate = document.getElementById("lastDate").value;
		var officeId = document.getElementById("company.id").value;
		var houseId = document.getElementById("house.id").value;
		var feeCode = document.getElementById("feeCode").value;
		var type = document.getElementById("type").value;
		var payFrom = document.getElementById("payFrom").value;
		
//		alert(proCompanyId)
		
//		alert("proCompanyId"+proCompanyId)
//		alert("officeId"+officeId)
//		alert("houseId"+houseId)
//		alert("type"+type)
//		
//		alert("feeCode"+feeCode)
//		alert("firstDate"+firstDate)
//		alert("feeCode"+feeCode)
//		alert("lastDate"+lastDate)
//		
		jQuery('#searchForm').showLoading();

		  $.getJSON(url,
				  {model:'house',
			  	  'device.fees.company.id':proCompanyId,
			  	  'device.house.owner.company.id':officeId,
			  	  'house.id':houseId,
			  	  paymentBeforId:paymentBeforId,
			  	  feeCode:feeCode,
			  	  type:type,
			  	  payFrom:payFrom,
			  	  firstDate:firstDate,
			  	  lastDate:lastDate
			  	  }
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
	
	
	
	
        
	</script>
</head>
<body>

	<ul class="nav nav-tabs"><shiro:hasPermission name="pms:paymentBefor:edit"></shiro:hasPermission>
		<li id="tabs1"><a  href="#">缴费明细</a></li>
		<li id="tabs2"><a  href="#">预付款</a></li>
		<li id="tabs3"><a  href="#">付款历史</a></li>
	</ul>	

	<form:form id="searchForm" modelAttribute="paymentBefor" action="${ctx}/pms/paymentBefor/list2" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		
		<form:hidden path="type"/>
		<form:hidden path="house.id"/>
		<form:hidden path="company.id"/>

		
		<label>收款单号 ：</label><form:input path="feeCode" htmlEscape="false" maxlength="50" class="input-small"/>
		
		<label class="control-label">开始:</label>
		<input id="firstDate" name="firstDate" type="text" readonly="readonly" maxlength="20" onClick="WdatePicker({})" 
						value="<fmt:formatDate value="${paymentBefor.firstDate}" pattern="yyyy-MM-dd"/>"  style="width:90px;" class=" Wdate input-small"/>	

		<label class="control-label">结束:</label>
		<input id="lastDate" name="lastDate" type="text" readonly="readonly" maxlength="20" onClick="WdatePicker({})" 
						value="<fmt:formatDate value="${paymentBefor.lastDate}" pattern="yyyy-MM-dd"/>"  style="width:90px;" class=" Wdate input-small"/>	
	
		<!-- select id="payFrom"  name="payFrom" style="width:80px">
						  <option value='1'>单位</option>
						  <option value='2'>个人</option>
						  <option value='0'>所有</option>
		</select -->    
		
		
		<form:select id="payFrom" name="payFrom" path="payFrom"   style="width:80px" class="input-small">
		<form:options items="${fromList}" itemLabel="name" itemValue="id" htmlEscape="false" />
		</form:select>
		
		
						
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		<input id="btnNewt" class="btn btn-primary" type="button" value="新添"/>
		<input id="btnPrint3" class="btn btn-primary" type="button" value="打印"  onclick="print3(0)"/>&nbsp;
	</form:form>
	
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>收款单号</th>
		<th>发票号</th>
		<th>收款日期</th>
		<th>收款金额</th>
		
		
		
		<c:choose>
	    <c:when test="${paymentBefor.type  == 1}">
	    	<th  style="text-align:right;">付款金额</th>
	    </c:when>		
	    </c:choose>	
		
		<th>收款方式</th>
		<th>收款人</th>
		<th>来源</th>
		<shiro:hasPermission name="pms:paymentBefor:edit"></shiro:hasPermission>
		<th>备注</th>

		<c:choose>
		    <c:when test="${paymentBefor.type  == 1}">
		    	<th style="text-align:right;">明细</th>
		    </c:when>		
	    </c:choose>			

		
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="paymentBefor">
			<tr>
				<td style="text-align:center;"><a href="${ctx}/pms/paymentBefor/form?id=${paymentBefor.id}">${paymentBefor.feeCode}</a></td>
				<td style="text-align:center;">${paymentBefor.certCode}</td>
				<td style="text-align:center;">${paymentBefor.receDate}</td>
	
		
				<c:choose>
			    <c:when test="${paymentBefor.recMoney - paymentBefor.payMoney > 0 && paymentBefor.type == 1}">
			    	<c:choose>
			        <c:when test="${empty paymentBefor.house.id}">
			        	<td style="text-align:right;"><a href="#" onClick="gotoPay(${paymentBefor.id},0)">${paymentBefor.recMoney}</a></td>
			    	</c:when>
					<c:otherwise>
						<td style="text-align:right;"><a href="#" onClick="gotoPay(${paymentBefor.id},${paymentBefor.house.id})">${paymentBefor.recMoney}</a></td>
				    </c:otherwise>
				    </c:choose>		    	
			   </c:when>
			    <c:otherwise>
			    
			    	<td style="text-align:right;">${paymentBefor.recMoney}</td>

			    </c:otherwise>				
			    </c:choose>		
			    
				<td style="text-align:right;">${paymentBefor.payMoney}</td>
				<td style="text-align:center;">${fns:getDictLabel(paymentBefor.payType, 'pms_pay_type', ' ')}</td>
				<td style="text-align:center;">${paymentBefor.user.name}</td>
			
					
				<c:choose>
			    <c:when test="${paymentBefor.payFrom ==1}">
			    	<td style="text-align:center;">单位</td>
			    </c:when>
			    <c:otherwise>
			    	<td style="text-align:center;">个人</td>
			    </c:otherwise>
			    </c:choose>					
				
				
				<td>${paymentBefor.remarks}</td>
			
				<shiro:hasPermission name="pms:paymentBefor:edit"></shiro:hasPermission>

    				<c:choose>
					    <c:when test="${paymentBefor.type  == 1}">
						    
					    	<c:choose>
					        <c:when test="${empty paymentBefor.house.id}">
						    <td style="text-align:center;">
						    <a href="#"  onclick='openWin2(${paymentBefor.payFrom},${paymentBefor.id},${paymentBefor.company.id},0)'>查看</a>
						    <a href="#"  onclick='print3(${paymentBefor.id})'>打印</a>
						    </td>
					    	</c:when>
							<c:otherwise>
							<td style="text-align:center;">
							<a href="#"  onclick='openWin2(${paymentBefor.payFrom},${paymentBefor.id},${paymentBefor.company.id},${paymentBefor.house.id})'>查看</a>
							<a href="#"  onclick='print3(${paymentBefor.id})'>打印</a>
							</td>
						    </c:otherwise>
						    </c:choose>					    	
					   
					    </c:when>
				    </c:choose>			

				
			</tr>
		</c:forEach>
		
		 <tr>
	         <td><span class="totall">合</span><span class="totall">计</span></td>
	         <td></td>
	         <td></td>	
	         <td style="text-align:right;">${totalRecMoney}</td>
	         
		 		<c:choose>
			    <c:when test="${paymentBefor.type  == 1}">
			    	<td style="text-align:right;">${totalPayMoney}</td>
			    </c:when>
			    </c:choose>		
	    
	         <td></td>
	         <td></td>	
	         <td></td>
	         <td></td>
	 
	         
		 		<c:choose>
			    <c:when test="${paymentBefor.type  == 1}">
			    	<td></td>
			    </c:when>
			    </c:choose>	         
	         
         </tr>			
		
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	
	<script type="text/javascript">
	Install_InsertReport();
	var Installed = Install_Detect();
	if ( Installed )
		CreateReport("Report");
	</script>
</body>
</html>
