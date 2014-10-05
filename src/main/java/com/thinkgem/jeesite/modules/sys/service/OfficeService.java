/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 机构Service
 * @author ThinkGem
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends BaseService {

	@Autowired
	private OfficeDao officeDao;
	
	public Office get(String id) {
		return officeDao.get(id);
	}
	
	public List<Office> findAll(){
		return UserUtils.getOfficeList();
	}
	public List<Office> findOffice(Office office){
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(office.getName())){
			dc.add(Restrictions.like("name", "%"+office.getName()+"%"));
		}
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+office.getSort());
		if (StringUtils.isNotEmpty(office.getSort())){
			dc.add(Restrictions.eq("sort", office.getSort()));
		}
		
		if (office.getParent() != null){
			dc.add(Restrictions.eq("parent.id", office.getParent().getId()));
		}
		
		dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
		
		List ls =  officeDao.find(dc);
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ls.size());
		return ls;
	}
	
//	public List<Office> findProCompanyList(){
//		 Office office = new Office();
//	     office.setSort("1");
//		return  this.findOffice(office);
//	}	
	
	
	@Transactional(readOnly = false)
	public void save(Office office) {
		office.setParent(this.get(office.getParent().getId()));
		String oldParentIds = office.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		office.setParentIds(office.getParent().getParentIds()+office.getParent().getId()+",");
		officeDao.clear();
		officeDao.save(office);
		// 更新子节点 parentIds
		List<Office> list = officeDao.findByParentIdsLike("%,"+office.getId()+",%");
		for (Office e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, office.getParentIds()));
		}
		officeDao.save(list);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		officeDao.deleteById(id, "%,"+id+",%");
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}
	
}
