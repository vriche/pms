/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.dao.BuildingsDao;
import com.thinkgem.jeesite.modules.pms.dao.CommunityDao;
import com.thinkgem.jeesite.modules.pms.dao.UnitDao;
import com.thinkgem.jeesite.modules.pms.entity.Buildings;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.Unit;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 单元信息Service
 * @author vriche
 * @version 2014-04-18
 */
@Component
@Transactional(readOnly = true)
public class UnitService extends BaseService {

	@Autowired
	private UnitDao unitDao;
	
//	@Autowired
//	private CommunityDao communityDao;
//	
//	@Autowired
//	private BuildingsDao buildingsDao;
	
	
	
	public Unit get(String id) {
		return unitDao.get(id);
	}
	
	public Page<Unit> find(Page<Unit> page, Unit unit) {
		DetachedCriteria dc = unitDao.createDetachedCriteria();
		
		if (unit.getBuildings() != null){
			Community community = unit.getBuildings().getCommunity() ;
			if (community != null){
				String communityId = community.getId();
				
				dc.createAlias("buildings", "b");
				dc.createAlias("b.community", "c");
				dc.createAlias("c.proCompany", "p");
				
				if (StringUtils.isNotEmpty(communityId)){
					dc.add(Restrictions.eq("c.id", communityId));
				}
				
				Office proCompany = community.getProCompany();
				if (proCompany != null){
					String proCompanyId = proCompany.getId();
					if (StringUtils.isNotEmpty(proCompanyId)){
						dc.add(Restrictions.eq("p.id", proCompanyId));
					}
				}
			}
			
			String buildingsId = unit.getBuildings().getId();
			if (StringUtils.isNotEmpty(buildingsId)){
				dc.add(Restrictions.eq("b.id", buildingsId));
			}
			
			String buildingsName= unit.getBuildings().getName();
			if (StringUtils.isNotEmpty(buildingsId)){
				dc.add(Restrictions.like("name", "%"+unit.getName()+"%"));
			}
		}	
	
		dc.add(Restrictions.eq(Unit.FIELD_DEL_FLAG, Unit.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("id"));
		return unitDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(Unit unit) {
		unitDao.clear();
		unitDao.save(unit);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		unitDao.deleteById(id);
	}
	
	
   public List<Unit> findAll(){
		DetachedCriteria dc = unitDao.createDetachedCriteria();
		dc.add(Restrictions.eq(Unit.FIELD_DEL_FLAG, Unit.DEL_FLAG_NORMAL));
		return unitDao.find(dc);
	}
   
   
   
   
   public List<Unit> findAllUnit(Unit unit){
		DetachedCriteria dc = unitDao.createDetachedCriteria();
		
		if(unit != null){
			Buildings buildings = unit.getBuildings();
			if(buildings!= null){
				dc.createAlias("buildings", "buildings");
				if (StringUtils.isNotEmpty(buildings.getId())){
					dc.add(Restrictions.eq("buildings.id", buildings.getId()));
				}
			}
		}

		
		dc.add(Restrictions.eq(Unit.FIELD_DEL_FLAG, Unit.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("code"));
//		User user = UserUtils.getUser();
		return unitDao.find(dc);
	}
	
}
