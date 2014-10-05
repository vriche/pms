/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.service;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.dao.DeviceDao;
import com.thinkgem.jeesite.modules.pms.dao.FeesDao;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.utils.FeesUtils;

/**
 * 收费项目Service
 * @author vriche
 * @version 2014-04-16
 */
@Component
@Transactional(readOnly = true)
public class FeesService extends BaseService {

	@Autowired
	private FeesDao feesDao;
	
	@Autowired
	private DeviceDao deviceDao;
	
	public Fees get(String id) {
		return feesDao.get(id);
	}
	
	public Page<Fees> find(Page<Fees> page, Fees fees) {
		DetachedCriteria dc = feesDao.createDetachedCriteria();
		
		if(fees.getCompany() != null){
			String companyId = fees.getCompany().getId();
			if (StringUtils.isNotEmpty(companyId) && !"0".equals(companyId)){
				dc.add(Restrictions.eq("company.id", companyId));
			}
		}

		
		if (StringUtils.isNotEmpty(fees.getFeesType()) && !"0".equals(fees.getFeesType())){
			dc.add(Restrictions.like("feesType", "%"+fees.getFeesType()+"%"));
		}
		
		if (StringUtils.isNotEmpty(fees.getCode())){
			dc.add(Restrictions.like("code", "%"+fees.getCode()+"%"));
		}
		
		if (StringUtils.isNotEmpty(fees.getName())){
			dc.add(Restrictions.like("name", "%"+fees.getName()+"%"));
		}

		if (StringUtils.isNotEmpty(fees.getFeesMode()) && !"0".equals(fees.getFeesMode())){
			
			dc.add(Restrictions.eq("feesMode", fees.getFeesMode()));
		}
		
		dc.add(Restrictions.eq(Fees.FIELD_DEL_FLAG, Fees.DEL_FLAG_NORMAL));
		
		dc.addOrder(Order.asc("code"));
		return feesDao.find(page, dc);
	}
	
	
	
	
	public List<Fees> find(Fees fees) {
		DetachedCriteria dc = feesDao.createDetachedCriteria();
		if(fees.getCompany() != null){
			String companyId = fees.getCompany().getId();
			if (StringUtils.isNotEmpty(companyId) && !"0".equals(companyId)){
				dc.add(Restrictions.eq("company.id", companyId));
			}
		}

		dc.add(Restrictions.eq(Fees.FIELD_DEL_FLAG, Fees.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("feesType"));
		dc.addOrder(Order.asc("code"));
		return feesDao.find(dc);
	}	
	
	public List<Fees> findNoPool(Fees fees) {
		DetachedCriteria dc = feesDao.createDetachedCriteria();
		if(fees.getCompany() != null){
			String companyId = fees.getCompany().getId();
			if (StringUtils.isNotEmpty(companyId) && !"0".equals(companyId)){
				dc.add(Restrictions.eq("company.id", companyId));
			}
		}

		dc.add(Restrictions.ne("feesType", Global.PMS_FEES_TYPE_POOL));
		dc.add(Restrictions.eq(Fees.FIELD_DEL_FLAG, Fees.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("feesType"));
		dc.addOrder(Order.asc("code"));
		return feesDao.find(dc);
	}	
	public List<Fees> findPool(Fees fees) {
		DetachedCriteria dc = feesDao.createDetachedCriteria();
		if(fees.getCompany() != null){
			String companyId = fees.getCompany().getId();
			if (StringUtils.isNotEmpty(companyId) && !"0".equals(companyId)){
				dc.add(Restrictions.eq("company.id", companyId));
			}
		}

		dc.add(Restrictions.eq("feesType", Global.PMS_FEES_TYPE_POOL));
		dc.add(Restrictions.eq(Fees.FIELD_DEL_FLAG, Fees.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("feesType"));
		dc.addOrder(Order.asc("code"));
		return feesDao.find(dc);
	}	
	
	
	public List<Fees> findByUserId(String userId) {
		return feesDao.findByUserId(userId);
	}

	public List<Fees> findAllList(){
		return feesDao.findAllList();
	}

	
	public List<Device> findList(String houseId){
		return deviceDao.findAllList(houseId,null);
	}
	

	public List<Map> findListMap(Fees fees,String houseId){
		List<Map>  feesMap=  Lists.newArrayList();
		List<Fees> list =  Lists.newArrayList();
//		List<Device> deviceList = fees.getDeviceList();
		if(houseId != null){
			List<Device> deviceList = deviceDao.findAllList(houseId,null);
			for(Device e:deviceList){
//				    boolean enable =  Boolean.valueOf(e.getEnable()).booleanValue();
				    Fees fes = e.getFees();
					Map<String, Object> map = Maps.newHashMap();
					String name = fes.getCode()+"_"+fes.getName();
					
					map.put("id", fes.getId());
					map.put("text", name);
					map.put("name", name);
//					map.put("disabled",true);
//					map.put("locked",enable);
					feesMap.add(map);
							
			}
		}else{
			list = this.find(fees);
			for (Fees fes : list) {
				Map<String, Object> map = Maps.newHashMap();
				String name = fes.getCode()+"_"+fes.getName();
				map.put("id", fes.getId());
				map.put("text", name);
				map.put("name", name);
//				map.put("disabled",true);
//				map.put("locked",false);
				feesMap.add(map);
			}			
			
		}


		return feesMap;
	}
	
	public Map<String, Object> getFeesJson(Fees fees){
		Map<String, Object> map = Maps.newHashMap();
		List<Fees> list = this.find(fees);
		map.put("", "");
		
	
		
		for (Fees fes : list) {
			
			
			if(fees.isWithoutPool()){
				if(!Global.PMS_FEES_TYPE_POOL.equals(fes.getFeesType())){
					
//					System.out.println("fes.isWithoutPool()>>>>>>"+ fes.getName() +">>>>>>>>>>>>>"+fes.getFeesType());
					String name = fes.getName();
//					String name ="["+fes.getCode()+"]"+fes.getName();
					map.put(fes.getId(), name);
				}
	
			}else{
				String name = fes.getName();
//				String name ="["+fes.getCode()+"]"+fes.getName();
				map.put(fes.getId(), name);
			}

		}		
		return map;
	}
	
	
	@Transactional(readOnly = false)
	public void save(Fees fees) {
		feesDao.clear();
		feesDao.save(fees);
		CacheUtils.remove(FeesUtils.CACHE_FEES_MAP);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		feesDao.deleteById(id);
		CacheUtils.remove(FeesUtils.CACHE_FEES_MAP);
	}
	
}
