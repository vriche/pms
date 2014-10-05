/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.common.utils.excel.fieldtype;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.service.CommunityService;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 字段类型转换
 * @author ThinkGem
 * @version 2013-03-10
 */
public class CommunityType {
	
	private static CommunityService communityService = SpringContextHolder.getBean(CommunityService.class);

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		for (Community e : communityService.findAllCommunity(new Community())){
			if (val.equals(e.getName())){
				return e;
			}
		}
		return null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((Community)val).getName() != null){
			return ((Community)val).getName();
		}
		return "";
	}
}
