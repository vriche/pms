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
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.dao.CommunityDao;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 收费项目Service
 * @author vriche
 * @version 2014-04-18
 */
@Component
@Transactional(readOnly = true)
public class CommunityService extends BaseService {

	@Autowired
	private CommunityDao communityDao;
	
	public Community get(String id) {
		return communityDao.get(id);
	}
	
	public Page<Community> find(Page<Community> page, Community community) {
		DetachedCriteria dc = communityDao.createDetachedCriteria();
		
		Office proCompany  = community.getProCompany();
		
		if(proCompany != null){
			dc.createAlias("proCompany", "proCompany");
			
			if (StringUtils.isNotEmpty(proCompany.getId())){
				dc.add(Restrictions.eq("proCompany.id", proCompany.getId()));
			}
//			if (StringUtils.isNotEmpty(proCompany.getName())){
//				dc.add(Restrictions.eq("proCompany.name", proCompany.getName()));
//			}
		}

		if (StringUtils.isNotEmpty(community.getName())){
			dc.add(Restrictions.like("name", "%"+community.getName()+"%"));
		}
		if (StringUtils.isNotEmpty(community.getCode())){
			dc.add(Restrictions.like("code", "%"+community.getCode()+"%"));
		}
		dc.add(Restrictions.eq(Community.FIELD_DEL_FLAG, Community.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return communityDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(Community community) {
		communityDao.clear();
		communityDao.save(community);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		communityDao.deleteById(id);
	}
	
	public List<Community> findAllCommunity(Community community){
		DetachedCriteria dc = communityDao.createDetachedCriteria();
		
		if(community != null){
			Office proCompany = community.getProCompany();
			if(proCompany!= null){
				dc.createAlias("proCompany", "proCompany");
				if (StringUtils.isNotEmpty(proCompany.getId())){
					dc.add(Restrictions.eq("proCompany.id", proCompany.getId()));
				}
			}
		}

		
		dc.add(Restrictions.eq(Community.FIELD_DEL_FLAG, Community.DEL_FLAG_NORMAL));
//		User user = UserUtils.getUser();
		return communityDao.find(dc);
	}
	
	
	public List<Community> findAllCommunityByProCompanyId(String  proCompanyId){
		Office proCompany = new Office(proCompanyId);
		Community community = new Community();
		community.setProCompany(proCompany);
		return this.findAllCommunity(community);
	}
	
	public Community getCommunityByName(String loginName) {
		return communityDao.findByName(loginName);
	}
	
}
