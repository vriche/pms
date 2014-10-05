/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.dao.MenuDao;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.dao.RoleDao;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Menu;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.Principal;

/**
 * 用户工具类
 * @author ThinkGem
 * @version 2013-5-29
 */
public class UserUtils extends BaseService {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);

	public static final String CACHE_USER = "user";
	
	public static final String CACHE_USER_LIST = "userList";
	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	
	public static User getUser(){
		User user = (User)getCache(CACHE_USER);
		if (user == null){
			try{
				Subject subject = SecurityUtils.getSubject();
				Principal principal = (Principal)subject.getPrincipal();
				if (principal!=null){
					user = userDao.get(principal.getId());
//					Hibernate.initialize(user.getRoleList());
					putCache(CACHE_USER, user);
				}
			}catch (UnavailableSecurityManagerException e) {
				
			}catch (InvalidSessionException e){
				
			}
		}
		if (user == null){
			user = new User();
			try{
				SecurityUtils.getSubject().logout();
			}catch (UnavailableSecurityManagerException e) {
				
			}catch (InvalidSessionException e){
				
			}
		}
		return user;
	}
	
	public static User getUser(boolean isRefresh){
		if (isRefresh){
			removeCache(CACHE_USER);
		}
		return getUser();
	}

	public static List<Role> getRoleList(){
		@SuppressWarnings("unchecked")
		List<Role> list = (List<Role>)getCache(CACHE_ROLE_LIST);
		if (list == null){
			User user = getUser();
			DetachedCriteria dc = roleDao.createDetachedCriteria();
			dc.createAlias("office", "office");
			dc.createAlias("userList", "userList", JoinType.LEFT_OUTER_JOIN);
			dc.add(dataScopeFilter(user, "office", "userList"));
			dc.add(Restrictions.eq(Role.FIELD_DEL_FLAG, Role.DEL_FLAG_NORMAL));
			dc.addOrder(Order.asc("office.code")).addOrder(Order.asc("name"));
			list = roleDao.find(dc);
			putCache(CACHE_ROLE_LIST, list);
		}
		return list;
	}
	
	public static List<User> getUserList(){
		@SuppressWarnings("unchecked")
		DetachedCriteria dc = userDao.createDetachedCriteria();
		List<User> list = userDao.find(dc);
		return list;
	}	
	

	
	public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
		if (menuList == null){
			User user = getUser();
			if (user.isAdmin()){
				menuList = menuDao.findAllList();
			}else{
				menuList = menuDao.findByUserId(user.getId());
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}
	
	public static List<Area> getAreaList(){
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>)getCache(CACHE_AREA_LIST);
		if (areaList == null){
//			User user = getUser();
//			if (user.isAdmin()){
				areaList = areaDao.findAllList();
//			}else{
//				areaList = areaDao.findAllChild(user.getArea().getId(), "%,"+user.getArea().getId()+",%");
//			}
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}
	
	public static List<Office> getOfficeList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_LIST);
		if (officeList == null){
			User user = getUser();
//			if (user.isAdmin()){
//				officeList = officeDao.findAllList();
//			}else{
//				officeList = officeDao.findAllChild(user.getOffice().getId(), "%,"+user.getOffice().getId()+",%");
//			}
			DetachedCriteria dc = officeDao.createDetachedCriteria();
			dc.add(dataScopeFilter(user, dc.getAlias(), ""));
			dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
			dc.addOrder(Order.asc("code"));
			officeList = officeDao.find(dc);
			putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
	}
	
	public static List<Office> getOfficeList2(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_LIST);
		if (officeList == null){

			DetachedCriteria dc = officeDao.createDetachedCriteria();

			dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
			dc.addOrder(Order.asc("code"));
			officeList = officeDao.find(dc);
			putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
	}
	
	public static List<Office> findProCompanyList(){
		 List<Office> newList = Lists.newArrayList();
		 List<Office> ls = getOfficeList();
		 for(Office o:ls){
			 if("1".equals(o.getSort())) newList.add(o);
		 }
		 return newList;
	}	
	
	public static List<Office> findProCompanyList2(String userName){
		 List<Office> newList = Lists.newArrayList();
		 List<Office> ls = getOfficeList();
		 for(Office o:ls){
			 if("1".equals(o.getSort())) newList.add(o);
		 }
		 return newList;
	}	
	
	public static List<Office> findCompanyListByProCompany(String proCompanyId){
		 List<Office> newList = Lists.newArrayList();
		 List<Office> ls = getOfficeList();
//		 System.out.println(">>>>>>>>>>>>>>>>>>>> proCompanyId >>>>>>>>>>>>>>>>>>>>>>>>"+ proCompanyId);
		 for(Office o:ls){
//			 String id = o.getId();
			
			 String parentIds = o.getParentIds();
			
//			  System.out.println(">>>>>>>>>>>>>>>>>>>>  o.getParentIds() >>>>>>>>>>>>>>>>>>>>>>>>"+   StringUtils.indexOf(parentIds, proCompanyId));
			 if(StringUtils.indexOf(parentIds, proCompanyId)>-1){
//				 System.out.println(">>>>>>>>>>>>>>>>>>>>  o.getParentIds() >>>>>>>>>>>>>>>>>>>>>>>>"+  o.getParentIds());
				 newList.add(o);
			 }
		 }
		 return newList;
	}
	
	
	public static List<User> findUserByCompany(List<User> userList,String companyId){
		List<User> list = Lists.newArrayList();
		 for(User u:userList){
			 String cId = u.getCompany().getId();
			 if(cId.equals(companyId)){
				 list.add(u);
			 }
		 }
		 
		 return list;
	}
	
	public static List<Map<String, Object>> findUserListMap(List<User> userList,String companyId){
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<User> list = findUserByCompany(userList, companyId);
		for(User u:list){
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", u.getId());
			map.put("name", u.getName());		
			mapList.add(map);			
		}
		return mapList;
	}
	
	
	
	
	public static User getUserById(String id){
		if(StringUtils.isNotBlank(id)) {
			return userDao.get(id);
		} else {
			return null;
		}
	}
	
	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
		Object obj = getCacheMap().get(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
		getCacheMap().put(key, value);
	}

	public static void removeCache(String key) {
		getCacheMap().remove(key);
	}
	
	public static Map<String, Object> getCacheMap(){
		Map<String, Object> map = Maps.newHashMap();
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			return principal!=null?principal.getCacheMap():map;
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return map;
	}
	
	
	
}
