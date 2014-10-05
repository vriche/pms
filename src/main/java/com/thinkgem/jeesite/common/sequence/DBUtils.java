package com.thinkgem.jeesite.common.sequence;
import java.sql.*;

/**
* 简单的数据连接工具
* File: DBUtils.java
* User: leizhimin
* Date: 2008-3-18 15:19:12
*/
public class DBUtils {
    public static final String url = "jdbc:mysql://127.0.0.1:3306/jeesite";
    public static final String username = "root";
    public static final String password = "root";
    public static final String driverClassName = "com.mysql.jdbc.Driver";

    /**
     * 获取数据库连接Connection
     *
     * @return 数据库连接Connection
     */
    public static Connection makeConnection() {
        Connection conn = null;
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String args[]) {
        testConnection();
    }

    public static void checkTable(Connection conn) {
 
   	 String sql_query_create_table2 = "CREATE TABLE  if not exists sys_sequences (sequence_name varchar(255) NOT NULL,next_value bigint(20) NOT NULL DEFAULT '0') ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		try {
			Statement stmt = conn.createStatement();
		
		     stmt.executeUpdate(sql_query_create_table2);
		     System.out.println("创建表 sys_sequences 成功");
	          stmt.close();
	          
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
           try {
               conn.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
       
         
   }

    /**
     * 测试连接方法
     */
    public static void testConnection() {
        Connection conn = makeConnection();
        boolean sucess =false;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM mysql.user");
            while (rs.next()) {
                String s1 = rs.getString(1);
                String s2 = rs.getString(2);
                System.out.println(s1 + "\t" + s2);
                sucess = true;
            }
            if(sucess) checkTable(conn);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}