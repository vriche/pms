package com.thinkgem.jeesite.common.utils.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class CEVUtil {
	/**
	* 依据后缀名判断读取的是否为Excel文件
	* @param filePath
	* @return
	*/
	    public static boolean isExcel(String filePath){
	    if(filePath.matches("^.+\\.(?i)(xls)$")||filePath.matches("^.+\\.(?i)(xlsx)$")){
	    return true;
	    }
	        return false;
	    } 
	    
	    /** 
	     * 检查文件是否存在 
	     */  
	    public static boolean fileExist(String filePath){
	    if(filePath == null || filePath.trim().equals("")) return false;
	        File file = new File(filePath);  
	        if (file == null || !file.exists()){  
	            return false;  
	        } 
	    return true;
	    }
	    /**
	     * 依据内容判断是否为excel2003及以下
	     */
	    public static boolean isExcel2003(String filePath){
	        try {
	        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
	if(POIFSFileSystem.hasPOIFSHeader(bis)) {
	System.out.println("Excel版本为excel2003及以下");
	return true;
	}
	} catch (IOException e) {
	e.printStackTrace();
	return false;
	}
	return false;
	    }
	    /**
	     * 依据内容判断是否为excel2007及以上
	     */
	    public static boolean isExcel2007(String filePath){
	        try {
	        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
	    if(POIXMLDocument.hasOOXMLHeader(bis)) {
	    System.out.println("Excel版本为excel2007及以上");
	    return true;
	    }
	} catch (IOException e) {
	e.printStackTrace();
	return false;
	}
	return false;
	    }
}
