/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.pms.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.pms.dao.FeesDao;
import com.thinkgem.jeesite.modules.pms.entity.Fees;

/**
 * 字典工具类
 * @author ThinkGem
 * @version 2013-5-29
 */
public class FeesUtils {
	
	private static FeesDao feesDao = SpringContextHolder.getBean(FeesDao.class);

	public static final String CACHE_FEES_MAP = "feesMap";
	
	
	
	public static Fees getFees(String id){
		@SuppressWarnings("unchecked")
		Map<String, Fees> dictMap = (Map<String, Fees>)CacheUtils.get(CACHE_FEES_MAP);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			for (Fees fees : feesDao.findAll()){
				dictMap.put(fees.getId(), fees);
			}
			CacheUtils.put(CACHE_FEES_MAP, dictMap);
		}
		Fees dictList = dictMap.get(id);
		if (dictList == null){
			dictList = new Fees();
		}
		return dictList;
	}
	
	public static String getFeesMode(String id){
		@SuppressWarnings("unchecked")
		Fees  fees = getFees(id);
		return fees.getFeesMode();
	}
	
	public static Map<String, Fees>  getALLFees(){
		@SuppressWarnings("unchecked")
		Map<String, Fees> dictMap = (Map<String, Fees>)CacheUtils.get(CACHE_FEES_MAP);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			for (Fees fees : feesDao.findAll()){
				dictMap.put(fees.getId(), fees);
			}
			CacheUtils.put(CACHE_FEES_MAP, dictMap);
		}
	
		return dictMap;
	}
	

	
	public static List<Fees>  getALLFees(String proCoumtryId){
		@SuppressWarnings("unchecked")
		List<Fees> list = new ArrayList();
		Map<String, Fees> dictMap = getALLFees();
		if (dictMap.size() >0){
		    Iterator it = dictMap.values().iterator();
			while (it.hasNext()){
				Fees fees = (Fees)it.next();
				if("0".equals(fees.getDelFlag()) && fees.getCompany().getId().equals(proCoumtryId)){
					list.add(fees);
				}
			}
			
		}
		return list;
	}
	
}
