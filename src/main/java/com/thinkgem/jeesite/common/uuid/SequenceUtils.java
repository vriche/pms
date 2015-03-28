package com.thinkgem.jeesite.common.uuid;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;

public class SequenceUtils {

	public SequenceUtils() {
		// TODO Auto-generated constructor stub
	}
	
	private static String getDateYYYYMM(){
		return DateUtils.getDate("yyyyMM");
	}
	
	//收款单号
    public static String nextVal(String code) {
    	if(code == null || "".equals(code)){
        	String preStr = getDateYYYYMM();
    		long uuid = GlobalSequenceIdProvider.nextVal();
    		int i = Integer.parseInt(uuid+"");
    		String midStr = StringUtils.lpad(9, i) +"";
    		code = preStr + "" +midStr;
    	}
        return code;
    }
    
	//收款单号  现付  N0000001 N0000002
    public static String nextVal_paymentBeforFeeCode1(String code) {
    	if(code == null || "".equals(code)){
        	String preStr = "N";
    		long uuid = GlobalSequenceIdProvider.nextVal_table1();
    		int i = Integer.parseInt(uuid+"");
    		String midStr = StringUtils.lpad(9, i) +"";
    		code = preStr + midStr;
    	}
        return code;
    }
  //收款单号  预付  P0000001  P0000002
    public static String nextVal_paymentBeforFeeCode2(String code) {
    	if(code == null || "".equals(code)){
        	String preStr = "P";
    		long uuid = GlobalSequenceIdProvider.nextVal_table1();
    		int i = Integer.parseInt(uuid+"");
    		String midStr = StringUtils.lpad(9, i) +"";
    		code = preStr +""+ midStr;
    	}
        return code;
    }
}
