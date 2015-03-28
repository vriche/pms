<%@ page contentType="text/html;charset=gb2312"%> 
 <%@ page import="java.sql.*"%> 
 <html> 
 <body> 
 <%
 Class.forName("com.mysql.jdbc.Driver").newInstance(); 
 String url ="jdbc:mysql://localhost/gridreport?user=root&password=root&useUnicode=true&characterEncoding=utf8";
 //testDB为你的数据库名 
Connection conn= DriverManager.getConnection(url); 
 Statement stmt=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE); 
 String sql="select * from orders"; 
 ResultSet rs=stmt.executeQuery(sql); 
 while(rs.next()) {%> 
您的第一个字段内容为：<%=rs.getString(1)%> 
您的第二个字段内容为：<%=rs.getString(2)%> 
 <%}%> 
 <%out.print("数据库操作成功，恭喜你");%> 
 <%rs.close(); 
 stmt.close(); 
 conn.close(); 
 %> 
 </body> 
 </html> 