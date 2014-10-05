/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.common.utils.excel.fieldtype;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

/**
 * 字段类型转换
 * @author ThinkGem
 * @version 2013-5-29
 */
public class UserListType {

	private static SystemService systemService = SpringContextHolder.getBean(SystemService.class);
	
	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<User> userList = Lists.newArrayList();
		List<User> allUserList = systemService.findAllUser();
		for (String s : StringUtils.split(val, ",")){
			for (User e : allUserList){
				if (e.getName().equals(s)){
					userList.add(e);
				}
			}
		}
		return userList.size()>0?userList:null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null){
			@SuppressWarnings("unchecked")
			List<User> userList = (List<User>)val;
			return Collections3.extractToString(userList, "name", ", ");
		}
		return "";
	}
	
}
