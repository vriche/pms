/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.common.utils.excel.fieldtype;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.pms.entity.Unit;
import com.thinkgem.jeesite.modules.pms.service.CommunityService;
import com.thinkgem.jeesite.modules.pms.service.UnitService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 字段类型转换
 * @author ThinkGem
 * @version 2013-03-10
 */
public class HouseType {
	
	private static UnitService  unitService = SpringContextHolder.getBean(UnitService.class);

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		for (Unit e : unitService.findAll()){
			if (val.equals(e.getFullNmae())){
				return e;
			}
		}
		return null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((Unit)val).getFullNmae() != null){
			return ((Unit)val).getFullNmae();
		}
		return "";
	}
}
