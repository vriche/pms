package com.thinkgem.jeesite.common.utils;
import java.math.BigDecimal;


	/**
	* 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精
	* 确的浮点数运算，包括加减乘除和四舍五入。
	*/
	public class Arith{
		
	    //默认除法运算精度
	    private static final int DEF_DIV_SCALE = 10;
	    //这个类不能实例化
	    private Arith(){
	    }

	    /**
	     * 提供精确的加法运算。
	     * @param v1 被加数
	     * @param v2 加数
	     * @return 两个参数的和
	     */
	    public static double add(double v1,double v2){
	        BigDecimal b1 = new BigDecimal(Double.toString(v1));
	        BigDecimal b2 = new BigDecimal(Double.toString(v2));
	        return b1.add(b2).doubleValue();
	    }
	    
	    public static double add(BigDecimal v1,BigDecimal v2){
	        return v1.add(v2).doubleValue();
	    }
	    /**
	     * 提供精确的减法运算。
	     * @param v1 被减数
	     * @param v2 减数
	     * @return 两个参数的差
	     */
	    public static double sub(double v1,double v2){
	        BigDecimal b1 = new BigDecimal(Double.toString(v1));
	        BigDecimal b2 = new BigDecimal(Double.toString(v2));
	        return b1.subtract(b2).doubleValue();
	    } 
	    /**
	     * 提供精确的乘法运算。
	     * @param v1 被乘数
	     * @param v2 乘数
	     * @return 两个参数的积
	     */
	    public static double mul(double v1,double v2){
	        BigDecimal b1 = new BigDecimal(Double.toString(v1));
	        BigDecimal b2 = new BigDecimal(Double.toString(v2));
	        return b1.multiply(b2).doubleValue();
	    }

	    /**
	     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
	     * 小数点以后10位，以后的数字四舍五入。
	     * @param v1 被除数
	     * @param v2 除数
	     * @return 两个参数的商
	     */
	    public static double div(double v1,double v2){
	        return div(v1,v2,DEF_DIV_SCALE);
	    }

	    /**
	     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	     * 定精度，以后的数字四舍五入。
	     * @param v1 被除数
	     * @param v2 除数
	     * @param scale 表示表示需要精确到小数点以后几位。
	     * @return 两个参数的商
	     */
	    public static double div(double v1,double v2,int scale){
	        if(scale<0){
	            throw new IllegalArgumentException(
	                "The scale must be a positive integer or zero");
	        }
	        BigDecimal b1 = new BigDecimal(Double.toString(v1));
	        BigDecimal b2 = new BigDecimal(Double.toString(v2));
	        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	    }

	    /**
	     * 提供精确的小数位四舍五入处理。
	     * @param v 需要四舍五入的数字
	     * @param scale 小数点后保留几位
	     * @return 四舍五入后的结果
	     */
	    public static double round(double v,int scale){
	        if(scale<0){
	            throw new IllegalArgumentException(
	                "The scale must be a positive integer or zero");
	        }
	        BigDecimal b = new BigDecimal(Double.toString(v));
	        BigDecimal one = new BigDecimal("1");
	        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	    }
	    
	  
	    /**  
	     *使用舍入模式的格式化操作  
	     *RoundingMode.HALF_EVEN ：向最接近数字方向舍入的舍入模式，如果与两个相邻数字的距离相等，则向相邻的偶数舍入。
	     *如果舍弃部分左边的数字为奇数，则舍入行为同 RoundingMode.HALF_UP；如果为偶数，则舍入行为同 RoundingMode.HALF_DOWN。
	     *注意，在重复进行一系列计算时，此舍入模式可以在统计上将累加错误减到最小。此舍入模式也称为“银行家舍入法”，主要在美国使用。
	     *此舍入模式类似于 Java 中对 float 和 double 算法使用的舍入策略
	    输入数字 	使用HALF_EVEN舍入模式将输入舍为一位 
	    5.5 	6 
	    2.5 	2 
	    1.6 	2 
	    1.1 	1 
	    -1.0 	-1 
	    -1.6 	-2 
	    -2.5 	-2 
	    -5.5 	-6 
	    assert (((BigDecimal)loadedItem.get("initialPrice")).compareTo(new BigDecimal(99)) == 0); 
	    
	     * @param v 需要四舍五入的数字
	     * @param scale 小数点后保留几位
	     * @return 四舍五入后的结果
	     */
	    public static double roundEVEN(double v,int scale){
	        if(scale<0){
	            throw new IllegalArgumentException(
	                "The scale must be a positive integer or zero");
	        }
	        BigDecimal b = new BigDecimal(Double.toString(v));
	        BigDecimal one = new BigDecimal("1");
	        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	    }
	       
	   
	    
}
