/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.entity.Buildings;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.dao.BuildingsDao;

/**
 * 收费项目Service
 * @author vriche
 * @version 2014-04-18
 */
@Component
@Transactional(readOnly = true)
public class BuildingsService extends BaseService {

	@Autowired
	private BuildingsDao buildingsDao;
	
	public Buildings get(String id) {
		return buildingsDao.get(id);
	}
	
	public Page<Buildings> find(Page<Buildings> page, Buildings buildings) {
		
		DetachedCriteria dc = buildingsDao.createDetachedCriteria();

		if (buildings.getCommunity() != null){
			dc.createAlias("community", "community");
			
			String communityId = buildings.getCommunity().getId();
			if (StringUtils.isNotEmpty(communityId)){
				dc.add(Restrictions.eq("community.id", communityId));
			}
			
			if (buildings.getCommunity().getProCompany() != null){
				String proCompanyId = buildings.getCommunity().getProCompany().getId();
				if (StringUtils.isNotEmpty(proCompanyId)){
					dc.createAlias("community.proCompany", "proCompany");
					dc.add(Restrictions.eq("proCompany.id", proCompanyId));
				}
			}
			
		}
		
		if (StringUtils.isNotEmpty(buildings.getName())){
			dc.add(Restrictions.like("name", "%"+buildings.getName()+"%"));
		}	
		
		
		
		dc.add(Restrictions.eq(Buildings.FIELD_DEL_FLAG, Buildings.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("code"));
		return buildingsDao.find(page, dc);
	}
	
	
	public List<Buildings> findAllBuildings(Buildings buildings){
		
		DetachedCriteria dc = buildingsDao.createDetachedCriteria();

		Community community = buildings.getCommunity();
		
		if(community != null){
			
			dc.createAlias("community", "community");
			
			String communityId = community.getId();
			String communityName = community.getName();

			if (StringUtils.isNotEmpty(communityId)){
				dc.add(Restrictions.eq("community.id", communityId));
			}
			if (StringUtils.isNotEmpty(communityName)){
				dc.add(Restrictions.eq("community.name", communityName));
			}		
			
		}
		
		if (StringUtils.isNotEmpty(buildings.getName())){
			dc.add(Restrictions.eq("name", buildings.getName()));
		}

		dc.add(Restrictions.eq(Buildings.FIELD_DEL_FLAG, Buildings.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("code"));
//		User user = UserUtils.getUser();
		return buildingsDao.find(dc);
	}
	
	
	@Transactional(readOnly = false)
	public void save(Buildings buildings) {
		buildingsDao.clear();
		buildingsDao.save(buildings);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		buildingsDao.deleteById(id);
	}
	
	
	
}
