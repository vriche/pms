<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>结算管理</title>
	<meta name="decorator" content="default"/>
</head>

	<body>
	<div id="content" class="row-fluid">
		<div id="left">
			<iframe id="cmsMenuFrame" name="cmsMenuFrame" src="${ctx}/pms/deviceDetail/tree" style="overflow:visible;"
				scrolling="yes" frameborder="no" width="100%"></iframe>
		</div>
		<div id="openClose" class="close">&nbsp;</div>

		<div id="right">
		
			<iframe id="cmsMainFrame" 
				name="cmsMainFrame" 
				src="${ctx}/pms/deviceDetail/form2?officeId=1&device.fees.company.id=0" 
				style="overflow:visible;"
				scrolling="no" frameborder="no" width="100%">
			</iframe>
		</div>
	</div>
	<script type="text/javascript"> 

		var leftWidth = "200"; // 左侧窗口大小
		
		function wSize(){
			//$("#cmsMainFrame").src="${ctx}/pms/deviceDetail/form2?officeId=1&device.fees.company.id=33";
			var minHeight = 500, minWidth = 980;
			var strs=getWindowSize().toString().split(",");
			$("#cmsMenuFrame, #cmsMainFrame, #openClose").height((strs[0]<minHeight?minHeight:strs[0])-$("#header").height()-$("#footer").height()-32);
			$("#openClose").height($("#openClose").height()-5);
			if(strs[1]<minWidth){
				$("#main").css("width",minWidth-10);
				$("html,body").css({"overflow":"auto","overflow-x":"auto","overflow-y":"auto"});
			}else{
				$("#main").css("width","auto");
				$("html,body").css({"overflow":"hidden","overflow-x":"hidden","overflow-y":"hidden"});
			}
			$("#right").width($("#content").width()-$("#left").width()-$("#openClose").width()-20);		
		}


	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>