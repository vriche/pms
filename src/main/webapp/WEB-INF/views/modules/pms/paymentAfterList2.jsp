<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<%@include file="/WEB-INF/views/include/dhtml.jsp" %>
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
			
			
			
			
			$("#tabs1").click(function(){
			     proCompanyId = $.getUrlParam('device.fees.company.id');
			     officeId = $.getUrlParam('device.house.owner.company.id');
			     location.href = "${ctx}/pms/deviceDetail/form2?officeId="+officeId +"&device.fees.company.id="+ proCompanyId;
			})	
			

//		     houseId = $.getUrlParam('device.house.id');
//		     proCompanyId = $.getUrlParam('device.fees.company.id');
//		     officeId = $.getUrlParam('device.house.owner.company.id');
//		     type = $.getUrlParam('type');
//		    
//		    initGrid();
//		    
//			getPaymentDetails(houseId,type);
//		    
//			var url = "${ctx}/pms/deviceDetail/form3?device.house.id="+houseId+"&device.fees.company.id="+ proCompanyId;
//			$("#tabs1").attr("href",url);
//			
//			if(type == 1){
//				$("#tabs2class").attr("class","active");
//				$("#btnNewt").show();
//				$("#btnSave").show();
//			}else{
//				$("#tabs3class").attr("class","active");
//				$("#btnNewt").hide();
//				$("#btnSave").hide();
//			}
//			
//			$("#tabs2").click(function(){
//			     //$("#tabs2class").attr("class","active");
//			     // $("#tabs3class").attr("class","");
//			     //getPaymentDetails(houseId,type);
//				//var proCompanyId = document.getElementById("device.fees.company.id").value;
//				location.href = "${ctx}/pms/paymentAfter/list?type=1&house.id="+houseId +"&device.fees.company.id="+ proCompanyId;
//			})				
//			$("#tabs3").click(function(){
//			    //$("#tabs3class").attr("class","active");
//			    // $("#tabs2class").attr("class","");
//			    //getPaymentDetails(houseId,type);
//				//var proCompanyId = document.getElementById("device.fees.company.id").value;
//				location.href = "${ctx}/pms/paymentAfter/list?type=2&house.id="+houseId +"&device.fees.company.id="+ proCompanyId;
//			})	
//			
//
//			$("#btnNewt").click(function(){
//				openWin(0,0)	
//			});	
//			
//			$("#btnSubmit").click(function(){
//			    getPaymentDetails(houseId,type);
//			    //var feeCode = $("#feeCode").val();
//				//location.href = "${ctx}/pms/paymentAfter/list?type=1&house.id="+houseId +"&device.fees.company.id="+ proCompanyId +"&feeCode="+feeCode;
//			});		
			
			
		});
		
		
		function deletePaymentDetail(id,deviceDetailId){
		    var statu = confirm("请确认是否删除?");
	        if(!statu){
	            return false;
	        }else{
	          var url = "${ctx}/pms/paymentAfter/delete";
	         
			  $.ajax({ type:'POST', url:url,data:{'id':id,'deviceDetailId':deviceDetailId},success:function(data){
			  	  	 getPaymentDetails(houseId,type);  
					 
			   } ,error:function(XMLHttpRequest, textStatus, errorThrown) alert("删除失败") })				       
			 
	        }
		}

		
		function openWin(id,deviceDetailId){
				var url = "${ctx}/pms/paymentAfter/form?type="+ type +"&house.id="+houseId +"&deviceDetail.id="+deviceDetailId;
				if(id > 0) url +="&id="+id; 
				var callHandler = function (v, h, f) {
				    var frm = $.jBox.getIframe("123").contentDocument.getElementById("inputForm");
				    if (v == '1') {
				        frm.submit();
				        $.jBox.alert("保存完毕");
				        $.jBox.close();
				        getPaymentDetails(houseId,type);  
				    }
				    
				    if (v == '2') {
				        deletePaymentDetail(id,deviceDetailId);				       
				    }
	
				   return true;
				};
				
    
				$.jBox("iframe:"+ url, {
				    title: "收据信息",
				    width: 800,
				    height: 420,
				    id:"123",
				    buttons: { '保存': 1 ,'删除': 2 , '关闭': 0 },
				    //closed:closed,
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
		var url = '${ctx}/pms/paymentAfter/getPaymentAfterJson';
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
        
	</script>
</head>
<body>

	<ul class="nav nav-tabs"><shiro:hasPermission name="pms:paymentAfter:edit"></shiro:hasPermission>
		<li><a id="tabs1" href="#">缴费明细</a></li>
		<li class="active"><a id="tabs3" href="#">付款历史</a></li>
	</ul>	

	<form:form id="searchForm" modelAttribute="paymentAfter" action="${ctx}/pms/paymentAfter/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>收款单号 ：</label><form:input path="feeCode" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="button" value="查询"/>
		&nbsp;<input id="btnNewt" class="btn btn-primary" type="button" value="新添"/>
	</form:form>
	
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>收款单号</th>
		<th>发票号</th>
		<th>收款日期</th>
		<th>应付金额</th>
		<th>收款金额</th>
		<th>收款方式</th>
		<th>收款人</th>
		<shiro:hasPermission name="pms:paymentAfter:edit"></shiro:hasPermission>
		<th>操作</th>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="paymentAfter">
			<tr>
				<td><a href="${ctx}/pms/paymentAfter/form?id=${paymentAfter.id}">${paymentAfter.feeCode}</a></td>
				<td>${paymentAfter.certCode}</td>
				<td>${paymentAfter.receDate}</td>
				<td>${paymentAfter.costMoney}</td>
				<td>${paymentAfter.recMoney}</td>
				<td>${paymentAfter.payType}</td>
				<td>${paymentAfter.user.name}</td>
				
				
				<td>${paymentAfter.remarks}</td>
				<shiro:hasPermission name="pms:paymentAfter:edit"></shiro:hasPermission><td>
    				<a href="${ctx}/pms/paymentAfter/form?id=${paymentAfter.id}">修改</a>
					<a href="${ctx}/pms/paymentAfter/delete?id=${paymentAfter.id}" onclick="return confirmx('确认要删除该单元信息吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
