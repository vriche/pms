package com.thinkgem.jeesite.common.uuid;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.web.context.ContextLoaderListener;


public class GlobalSequenceIdProvider {
	
	/**
	 * SessionFactory
	 */
//	@Autowired
//	private static SessionFactory sessionFactory;
	private static final SequenceId global;
    private static final SequenceId table1;
    private static final SequenceId table2;
    private static SessionFactory sessionFactory=null;
 
    static {
    	sessionFactory = (SessionFactory) ContextLoaderListener.getCurrentWebApplicationContext().getBean("sessionFactory");
        SequenceIdProvider provider = new JdbcSequenceIdProvider(getDataSource());
        global = provider.create("global");
        table1 = provider.create("pms_payment_befor_feeCode1");
        table2 = provider.create("pms_payment_befor_feeCode2");
    }
 
    public static long nextVal() {
        return global.nextVal();
    }
 
    public static long nextVal_table1() {
        return table1.nextVal();
    }
 
    public static long nextVal_table2() {
        return table2.nextVal();
    }

    private static DataSource getDataSource() {
//    	  System.out.println("getDataSource>>>>>>>>>>>>>>>>>>>>>>>>>>>>sessionFactory>>>>>>>>>>>"+ sessionFactory);
    	  return SessionFactoryUtils.getDataSource(sessionFactory);
    }
    
    public static Connection getConnection(){
		try {
			return getDataSource().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
