/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.pms.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.Device;
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
public class CompanyService extends BaseService {

	@Autowired
	private OfficeDao officeDao;
	
	public Office get(String id) {
		return officeDao.get(id);
	}
	
	
	public Page<Office> find(Page<Office> page, Office office) {
		
		DetachedCriteria dc = officeDao.createDetachedCriteria();

		if (StringUtils.isNotEmpty(office.getSort())){
			dc.add(Restrictions.eq("sort", office.getSort()));
		}
		if (StringUtils.isNotEmpty(office.getName())){
//			dc.add(Restrictions.like("name", office.getName()));
			dc.add(Restrictions.like("name", "%"+office.getName()+"%"));
		}	
		
		
		
		dc.add(Restrictions.eq(office.FIELD_DEL_FLAG, office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return officeDao.find(page, dc);
	}
	
	
	public List<Office> findAll(){
		return UserUtils.getOfficeList();
		
	}
	public List<Office> findCompany(Office office){
		
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		
		if (StringUtils.isNotEmpty(office.getName())){
			dc.add(Restrictions.like("name", "%"+office.getName()+"%"));
		}

		if (StringUtils.isNotEmpty(office.getSort())){
			dc.add(Restrictions.eq("sort", office.getSort()));
		}
		
		dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));

		return officeDao.find(dc);
	}
	
	public List<Office> findProCompanyList(){
//		 Office office = new Office();
//	     office.setSort("1");
//		return  this.findCompany(office);
		 List<Office> newList = Lists.newArrayList();
		 List<Office> ls = UserUtils.getOfficeList();
		 for(Office o:ls){
			 if("1".equals(o.getSort())) newList.add(o);
		 }
		 return newList;
	}	
	
	
	public Office findCompanyById(String companyId){
		Office office = new Office();
		 List<Office> ls = UserUtils.getOfficeList();
		 for(Office o:ls){
			 if(companyId.equals(o.getId())) office = o;
		 }
		 return office;
	}	
	
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
