<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>房产列表</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<style type="text/css">
		.ztree {overflow:auto;margin:0;_margin-top:10px;padding:10px 0 0 10px;}<%--
		.ztree li span.button.level0, .ztree li a.level0 {display:none;height:0;}
		.ztree li ul.level0 {padding:0;background:none;}--%>
		.accordion-inner{padding:2px;}
	</style>
	
	<script type="text/javascript">
	
		 var setting = {};
		 var tree;

		$(document).ready(function(){

			setting = {
					showLine: false,   
					view:{selectedMulti:false},
					async : {  
			            enable : true, 
			            url : "${ctx}/pms/payemtDetail/treeData", 
			            autoParam:["id", "name","level"], 
		                otherParam:{"proCompanyId":$("#proCompanyId").val(),"type":1}
			        },  
					data:{simpleData:{
					 	enable : true,  
		                idKey : "id", // id编号命名 默认  
		                pIdKey : "pId", // 父id编号命名 默认   
		                rootPId : 0 // 用于修正根节点父节点数据，即 pIdKey 指定的属性值  
					}},
					 callback : {
            			//rightClick : zTreeOnRightClick   //右键事件  
            			onClick: zTreeOnRightClick   //右键事件  
        			}, 

		            onAsyncError : zTreeOnAsyncError,  
		            onAsyncSuccess : function(event, treeId, treeNode, msg){}
			};

			// 初始化树结构   渲染  
			tree =  $.fn.zTree.init($("#tree"), setting);  
			
			// 展开第一级节点
			var nodes = tree.getNodesByParam("level", 0);
			for(var i=0; i<nodes.length; i++) {tree.expandNode(nodes[i], true, true, false);}	
			wSize();
			


		});
		
		
           
		function zTreeOnRightClick(event, treeId, treeNode, clickFlag){
		       // window.top.mainFrame.location.href = "http://www.baidu.com";
		      // window.location.href.target="mainFrame";
		       //window.location.href ="http://www.baidu.com";
			  //if(!treeNode.isParent){   alert("treeId自动编号：" + treeNode.tId + ", 节点id是：" + treeNode.id + ", 节点文本是：" + treeNode.name);  }  
		}
		
	   // 加载错误提示  
		    function zTreeOnAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {  
		        alert("加载错误：" + XMLHttpRequest);  
		    };  
		  
		    // 过滤函数  
		    function filter(treeId, parentNode, childNodes) {  
		        if (!childNodes)  
		            return null;  
		        for ( var i = 0, l = childNodes.length; i < l; i++) {  
		            childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');  
		        }  
		        return childNodes;  
		    } 
		    


		$(window).resize(function(){
			wSize();
		});

		function wSize(){
			$(".ztree").width($(window).width()-16).height($(window).height()-62);
			$(".ztree").css({"overflow":"auto","overflow-x":"auto","overflow-y":"auto"});
			$("html,body").css({"overflow":"hidden","overflow-x":"hidden","overflow-y":"hidden"});
		}
		
		function reloadTree(){
			var v = $("input[name='optionsRadios']:checked").val(); 
			setting.async.otherParam.proCompanyId =$("#proCompanyId").val();
			setting.async.otherParam.type = v;
			tree =  $.fn.zTree.init($("#tree"), setting); 
		}
		
		

		
	</script>

</head>
<body>
	<div class="accordion-group">
	    <div class="accordion-heading">
	        <br>
	    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label class="radio"><input type="radio" name="optionsRadios" id="optionsRadios1" value="1" checked onclick="reloadTree()">单位</label>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label class="radio"><input type="radio" name="optionsRadios" id="optionsRadios2" value="2" onclick="reloadTree()">房产</label>
			<br>
	    </div>
	    
	    <div class="accordion-heading" onchange="reloadTree();">
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<select id="proCompanyId" name="proCompanyId" style="width:170px">
					<c:forEach items="${fnc:getProCompanyList('admin')}" var="item">
 						<option value ="${item.id}">${item.name}</option>
					</c:forEach>	
				</select>
		</div>
		
	    <div class="accordion-body">
			<div class="accordion-inner">
				<div id="tree" class="ztree"></div>
			</div>
	    </div>
	</div>
</body>
</html>