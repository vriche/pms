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
			            url : "${ctx}/pms/deviceDetail/treeData", 
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
							 onAsyncSuccess:onAsyncSuccess,
							 onAsyncError:onAsyncError,
							 beforeExpand:beforeExpand,
							 onCheck:onCheck
//						rightClick : zTreeOnRightClick   //右键事件  
//            			onClick: zTreeOnRightClick   //右键事件  
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
	
//			tree.selectNode(tree.getNodeByParam("id",266, null)); 
//			tree.selectNode(0);//设置为第一个子节点为选中状态
//		    var node = tree.selectNode(nodes[0]);  

		});
		
		function onAsyncSuccess(event, treeId, treeNode, msg){
			var nodes = tree.getNodesByParam("level", 0);
//			tree.selectNode(nodes[0]);  
			if(!msg || msg.length == 0){return;}
//			tree.selectNode(nodes[2]);

		}
		function onAsyncError(event, treeId, treeNode, clickFlag){
			
		}
		function beforeExpand(event, treeId, treeNode, clickFlag){
			
		}
		function onCheck(event, treeId, treeNode, clickFlag){
			
		}
           
		function zTreeOnRightClick(event, treeId, treeNode, clickFlag){
		
			  if (!treeNode) {  
				  tree.cancelSelectedNode();  
		            showRMenu("root", event.clientX, event.clientY);  
		        } else if (treeNode && !treeNode.noR) { //noR属性为true表示禁止右键菜单  
		            if (treeNode.newrole && event.target.tagName != "a" && $(event.target).parents("a").length == 0) {  
		            	tree.cancelSelectedNode();  
		                showRMenu("root", event.clientX, event.clientY);  
		            } else {  
		            	tree.selectNode(treeNode);  
		                showRMenu("node", event.clientX, event.clientY);  
		            }  
		        }  
			
//			   alert(11111)
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
		
		function reloadTree(i){
			var v = $("input[name='optionsRadios']:checked").val(); 
			setting.async.otherParam.proCompanyId =$("#proCompanyId").val();
			setting.async.otherParam.type = v;
			tree =  $.fn.zTree.init($("#tree"), setting); 

			if(i ==1){
				var url  ="${ctx}/pms/deviceDetail/form2?officeId=1&device.fees.company.id=0";
//				location.href = "${ctx}/pms/paymentAfter/list?type=1&house.id="+houseId +"&device.fees.company.id="+proCompanyId;
				window.parent.frames['cmsMainFrame'].location = url;
			}else{
				var url  ="${ctx}/pms/deviceDetail/form3?officeId=1&device.fees.company.id=0";
				window.parent.frames['cmsMainFrame'].location =url;
			}
		}
		
		
		//显示右键菜单  
	    function showRMenu(type, x, y) {  
	        $("#rMenu ul").show();  
	        if (type=="root") {  
	            $("#m_del").hide();  
	            $("#m_check").hide();  
	            $("#m_unCheck").hide();  
	        }  
	        $("#rMenu").css({"top":y+"px", "left":x+"px", "display":"block"});  
	    }  
	    //隐藏右键菜单  
	    function hideRMenu() {  
	        $("#rMenu").hide();  
	    }  
		
		

		
	</script>

</head>
<body>
	<div class="accordion-group">
	    <div class="accordion-heading">
	        <br>
	    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label class="radio"><input type="radio" name="optionsRadios" id="optionsRadios1" value="1" checked onclick="reloadTree(this.value)">单位</label>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label class="radio"><input type="radio" name="optionsRadios" id="optionsRadios2" value="2" onclick="reloadTree(this.value)">房产</label>
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
	
	
	
	
	
    <p><span style="background-color: #fafafa;"><!-- 右键菜单div -->  
    <div id="rMenu" style="position:absolute; display:none;">  
    <li>  
    <ul id="m_add" onclick="addPrivilege();"><li>增加</li></ul>  
    <ul id="m_del" onclick="delPrivilege();"><li>删除</li></ul>  
    <ul id="m_del" onclick="editPrivilege();"><li>编辑</li></ul>  
    <ul id="m_del" onclick="queryPrivilege();"><li>查看</li></ul>  
    </li>  
    </div></span></p>  
	
	
	
</body>
</html>