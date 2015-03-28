<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分配角色</title>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	
		var officeTree;
		var selectedTree;//zTree已选择对象
		
		// 初始化
		$(document).ready(function(){
			officeTree = $.fn.zTree.init($("#officeTree"), setting, officeNodes);
			selectedTree = $.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
		});

		var setting = {view: {selectedMulti:false,nameIsHTML:true,showTitle:false},
				data: {simpleData: {enable: true}},
				callback: {onClick: treeOnClick}};
		
		var officeNodes=[
	            <c:forEach items="${officeList}" var="office">
	            {id:"${office.id}",
	             pId:"${not empty office.parent?office.parent.id:0}", 
	             name:"${office.name}"},
	            </c:forEach>];
	
		var pre_selectedNodes =[
   		        <c:forEach items="${role.userList}" var="user">
   		        {id:"${user.id}",
   		         pId:"0",
   		         name:"<font color='red' style='font-weight:bold;'>${user.name}</font>"},
   		        </c:forEach>];
		
		var selectedNodes =[
		        <c:forEach items="${role.userList}" var="user">
		        {id:"${user.id}",
		         pId:"0",
		         name:"<font color='red' style='font-weight:bold;'>${user.name}</font>"},
		        </c:forEach>];
		
		var pre_ids = "${selectIds}".split(",");
		var ids = "${selectIds}".split(",");
		
		
		//点击选择项回调
		function treeOnClick(event, treeId, treeNode, clickFlag){
			if("officeTree"==treeId){
				$.get("${ctx}/sys/role/users?officeId=" + treeNode.id, function(userNodes){
					$.fn.zTree.init($("#userTree"), setting, userNodes);
				});
			}
			if("userTree"==treeId){
				//alert(treeNode.id + " | " + ids);
				//alert(typeof ids[0] + " | " +  typeof treeNode.id);
				if($.inArray(String(treeNode.id), ids)<0){
					selectedTree.addNodes(null, treeNode);
					ids.push(String(treeNode.id));
				}
			}
            if("selectedTree"==treeId){
                if($.inArray(String(treeNode.id), pre_ids)<0){
                    selectedTree.removeNode(treeNode);
                    ids.splice($.inArray(String(treeNode.id), ids), 1);
                }else{
                    top.$.jBox.tip("只能删除新添加人员！", 'info');
                }
            }
		};
				
		function clearAssign(){
			ids=pre_ids.slice(0);
			selectedNodes=pre_selectedNodes;
			$.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
		};
		
		function checkboxAll(e){
			$("input[name='test']").each(function() { // 遍历选中的checkbox
				$(this).attr("checked", e.checked);
			});	
		};
		
		function getFids(){
			var fids =[];
			$("input[name='test']:checked").each(function() { // 遍历选中的checkbox
				var fid = $(this).val()+'';
				fids.push(fid);
			});
			return fids;
		};
		
	</script>
</head>
<body>


	<div class="controls">
      <table>
      <tr>
      	<td  style="text-align:left;">费用   <label><input type="checkbox" name="checkboxAll"  id="checkboxAll" onclick="checkboxAll(this)">全选</label> </td>
      </tr>

      <tr>
    	  <td>
	            <c:forEach var="fees" items="${feesList}">
	            	<label>
	            	<input type="checkbox" name="test" value="<c:out value="${fees.id}"/>"/>
	            	<c:out value="${fees.name}"/></label>
	            </c:forEach>          
	      </td>
	    </tr>
	    </table>
	</div>


<hr/>
<h6>	
	
	<div id="assignRole" class="row-fluid span12">
		<div class="span4" style="border-right: 1px solid #A8A8A8;">
			<p>所在单位：</p>
			<div id="officeTree" class="ztree"></div>
		</div>
		<div class="span4">
			<p>待选人员：</p>
			<div id="userTree" class="ztree"></div>
		</div>
		<div class="span4" style="padding-left:16px;border-left: 1px solid #A8A8A8;">
			<p>已选人员：</p>
			<div id="selectedTree" class="ztree"></div>
		</div>
	</div>
</body>
</html>
