package com.thinkgem.jeesite.common.sequence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class TestSequence {
    /**
     * 测试入口
     * @param args
     */
    public static void main(String args[]) {
        test();
    }

    /**
     * 测试Sequence方法
     */
    public static void test() {
        System.out.println("----------test()----------");
      
        for (int i = 0; i < 20; i++) {
            long x = SequenceUtils.getInstance().getNextKeyValue("sdaf");
            System.out.println(x);
           
        }
      
    }
    

  
   
}