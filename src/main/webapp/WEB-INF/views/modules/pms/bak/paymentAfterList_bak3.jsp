<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>单元信息管理</title>
	<meta name="decorator" content="default"/>
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


		$(document).ready(function() {
		
			initGrid();
		
		    var houseId = $.getUrlParam('house.id');
		    var type = $.getUrlParam('type');
		    
			var url = "${ctx}/pms/payemtDetail/form?house.id="+houseId;
			$("#tabs1").attr("href",url);
			
			if(type == 1){
				$("#tabs2class").attr("class","active");
			}else{
				$("#tabs3class").attr("class","active");
			}
			
			$("#tabs2").click(function(){
				location.href = "${ctx}/pms/paymentAfter/list?type=1&house.id="+houseId; 
			})				
			$("#tabs3").click(function(){
				location.href = "${ctx}/pms/paymentAfter/list?type=2&house.id="+houseId; 
			})				
			
			
		});
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
			var flds = "序,收款单号,发票号 ,收款日期,应付金额,收款金额,收款方式,收款人,操作";
			mygrid.setHeader(flds);
			var columnIds = "inedx,feedName,price,firstNum,lastNum,useNum,cost,incone,paydates";
			mygrid.setColumnIds(columnIds);
			
		    mygrid.setInitWidthsP("2,15,15,15,15,10,10,8,10");
			mygrid.setColAlign("center,center,right,right,right,right,right,right,center");
			mygrid.setColTypes("ed,ed,ed,ed,ed,ed,ed,ed,ed");
		    
		    mygrid.setMultiLine(false);
			mygrid.setEditable(true);
		    mygrid.setSkin("modern2");
		    mygrid.setColSorting("na,str,str,str,str,int,co,int,int,int") ;
		    mygrid.enableAlterCss("even","uneven"); 
		
			mygrid.init();	 
			mygrid.setSortImgState(true,1,"ASC"); 
			//mygrid.attachFooter('合计:, , , , , , ',['text-align:center;','text-align:right;','text-align:right;','text-align:right;','text-align:right;','text-align:right;','text-align:right;']);
			//mygrid.style.height = gridbox.offsetHeight  +"px";	
			mygrid.setSizes();	

	}		       
        
        
        
	</script>
</head>
<body>

	<ul class="nav nav-tabs"><shiro:hasPermission name="pms:paymentAfter:edit"></shiro:hasPermission>
		<li><a id="tabs1" href="#">缴费明细</a></li>
		<li id="tabs2class"><a id="tabs2" href="#" onClick="reloadForm()">预付款</a></li>
		<li id="tabs3class"><a id="tabs3" href="#" onClick="reloadForm()">付款历史</a></li>
	</ul>	
	

	<form:form id="searchForm" modelAttribute="paymentAfter" action="${ctx}/pms/paymentAfter/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>收款单号 ：</label><form:input path="feeCode" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	
	<tags:message content="${message}"/>
	
	<div class="controls">
			<div id="gridbox" width="100%" height="30%" style="background-color:white;z-index:0"></div>
	</div>
			
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
				<shiro:hasPermission name="pms:paymentAfter:edit"></shiro:hasPermission>
				<td>
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
