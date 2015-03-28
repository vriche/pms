<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
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
			
			

			$("#emptyPmsData").click(function(e){
				var url = '${ctx}/sys/menu/emptyPmsDataAll';
				top.$.jBox.confirm("确认要删除数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$.ajax({
			                type:"post",
			                url:url,
			                datetype:'text',
			                success:function(result){
			                      alert("操作成功");
			                },
			               error : function(){
			                  alert("操作失败");
			              }
			            });	
						
					}
				},{buttonsFocus:1});
				
			})
			$("#emptyPmsDataPayment").click(function(e){
				var url = '${ctx}/sys/menu/emptyPmsDataPayment';
				top.$.jBox.confirm("确认要删除数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$.ajax({
			                type:"post",
			                url:url,
			                datetype:'text',
			                success:function(result){
			                      alert("操作成功");
			                },
			               error : function(){
			                  alert("操作失败");
			              }
			            });	
						
					}
				},{buttonsFocus:1});
			})
			$("#emptyPmsDataDevice").click(function(e){
				var url = '${ctx}/sys/menu/emptyPmsDataDevice';
				top.$.jBox.confirm("确认要删除数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$.ajax({
			                type:"post",
			                url:url,
			                datetype:'text',
			                success:function(result){
			                      alert("操作成功");
			                },
			               error : function(){
			                  alert("操作失败");
			              }
			            });	
						
					}
				},{buttonsFocus:1});
			})
			$("#emptyPmsDataUser").click(function(e){
				var url = '${ctx}/sys/menu/emptyPmsDataUser';
				top.$.jBox.confirm("确认要删除数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$.ajax({
			                type:"post",
			                url:url,
			                datetype:'text',
			                success:function(result){
			                      alert("操作成功");
			                },
			               error : function(){
			                  alert("操作失败");
			              }
			            });	
						
					}
				},{buttonsFocus:1});
			})			
			
		});
	</script>
</head>
<body>
	<input type=button id="emptyPmsData" name="emptyPmsData" value="清空所有数据">
	<input type=button id="emptyPmsDataPayment" name="emptyPmsDataPayment" value="清空付款数据">
	<input type=button id="emptyPmsDataDevice" name="emptyPmsDataDevice" value="清空房屋及设备数据">
	<input type=button id="emptyPmsDataUser" name="emptyPmsDataUser" value="清空用户及单位数据">
</body>
</html>