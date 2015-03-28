/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.service;

import java.math.BigDecimal;
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
import com.thinkgem.jeesite.common.utils.Arith;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.dao.BuildingsDao;
import com.thinkgem.jeesite.modules.pms.dao.CommunityDao;
import com.thinkgem.jeesite.modules.pms.dao.HouseDao;
import com.thinkgem.jeesite.modules.pms.dao.UnitDao;
import com.thinkgem.jeesite.modules.pms.entity.Buildings;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.DeviceDetail;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.Unit;
import com.thinkgem.jeesite.modules.pms.utils.HouseUtils;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 收费项目Service
 * @author vriche
 * @version 2014-04-18
 */
@Component
@Transactional(readOnly = true)
public class HouseService extends BaseService {
	
	
	@Autowired
	private AreaDao areaDao;
	
	@Autowired
	private OfficeDao officeDao;

	@Autowired
	private HouseDao houseDao;
	
	@Autowired
	private UnitDao unitDao;
	
	@Autowired
	private CommunityDao communityDao;
	
	@Autowired
	private BuildingsDao buildingsDao;
	
	@Autowired
	private DeviceService deviceService;
	
	
	public House get(String id) {
		return houseDao.get(id);
	}
	
	public Page<House> find(Page<House> page, House house) {
		DetachedCriteria dc = houseDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(house.getName())){
			dc.add(Restrictions.like("name", "%"+house.getName()+"%"));
		}
		dc.add(Restrictions.eq(House.FIELD_DEL_FLAG, House.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return houseDao.find(page, dc);
	}
	
	public Page<House> find2(Page<House> page, String proCompanyId, String communityId, String buildingsId, String unitId,String houseId) {
		page.setList(houseDao.findByProcompany(proCompanyId, communityId, buildingsId, unitId,houseId));
		return page;
	}	
	
	
	public Page<House> find3(Page<House> page, String proCompanyId, String communityId, String buildingsId, String unitId,String houseId) {
		DetachedCriteria dc = houseDao.createDetachedCriteria();
		
		dc.createAlias("owner", "owner");
		dc.createAlias("owner.company", "office");
		dc.createAlias("unit", "unit");
		dc.createAlias("unit.buildings", "buildings");
		dc.createAlias("buildings.community", "community");
		dc.createAlias("community.proCompany", "proCompany");
		
		if (StringUtils.isNotEmpty(proCompanyId)){
			dc.add(Restrictions.eq("proCompany.id", proCompanyId));
		}		
		if (StringUtils.isNotEmpty(communityId)){
			dc.add(Restrictions.eq("community.id", communityId));
		}
		if (StringUtils.isNotEmpty(buildingsId)){
			dc.add(Restrictions.eq("buildings.id", buildingsId));
		}	
		if (StringUtils.isNotEmpty(unitId)){
			dc.add(Restrictions.eq("unit.id", unitId));
		}	
		dc.add(Restrictions.eq(House.FIELD_DEL_FLAG, House.DEL_FLAG_NORMAL));
		
		return houseDao.find(page, dc);

	}	
	
	public DetachedCriteria findHouseDC(House house) {
		DetachedCriteria dc = houseDao.createDetachedCriteria();
		
		dc.createAlias("owner", "owner");
		dc.createAlias("owner.company", "office");
		dc.createAlias("unit", "unit");
		dc.createAlias("unit.buildings", "buildings");
		dc.createAlias("buildings.community", "community");
		dc.createAlias("community.proCompany", "proCompany");
		
//		String proCompanyId, String communityId, String buildingsId, String unitId,String houseId
		
		String proCompanyId = house.getUnit().getBuildings().getCommunity().getProCompany().getId();
		String communityId = house.getUnit().getBuildings().getCommunity().getId();
		String buildingsId = house.getUnit().getBuildings().getId();
		String unitId = house.getUnit().getId();
		
		if (StringUtils.isNotEmpty(proCompanyId)){
			dc.add(Restrictions.eq("proCompany.id", proCompanyId));
		}		
		if (StringUtils.isNotEmpty(communityId)){
			dc.add(Restrictions.eq("community.id", communityId));
		}
		if (StringUtils.isNotEmpty(buildingsId)){
			dc.add(Restrictions.eq("buildings.id", buildingsId));
		}	
		if (StringUtils.isNotEmpty(unitId)){
			dc.add(Restrictions.eq("unit.id", unitId));
		}	
		dc.add(Restrictions.eq(House.FIELD_DEL_FLAG, House.DEL_FLAG_NORMAL));
		
		dc.addOrder(Order.asc("community.id")).addOrder(Order.asc("buildings.sort")).addOrder(Order.asc("unit.sort")).addOrder(Order.asc("sort"));
		
		return dc;
//		return houseDao.find(page, dc);

	}	
	
	
	public Page<House> findPage(Page<House> page, House house,Map<String,Object> mp,int mapType) {
		DetachedCriteria dc = findHouseDC(house);
		houseDao.find(page,dc);
		if(mp != null){
			List<House> list = page.getList();
			 for (House h : list){
				User owner = h.getOwner();
				String userName = owner.getName();
				String userType = owner.getUserType();
				String companyName = owner.getCompany().getName();
				String id =  h.getId();
				String code = h.getCode();
				if("2".equals(userType)){
					code += "("+ userName+")";
				}else{
					code = companyName;
				}
				
				h.setCode(code);
				if(mapType ==1){
					mp.put(id, code);
				}else{
					mp.put(id, h);
				}

			 }
		}else{
			List<House> list = page.getList();
			
			if(mapType ==1){
				 for (House h : list){
						User owner = h.getOwner();
						String userName = owner.getName();
						String userType = owner.getUserType();
						String companyName = owner.getCompany().getName();
						String id =  h.getId();
						String code = h.getCode();

						if("2".equals(userType)){
							code += "("+ userName+")";
						}else{
							code = companyName;
						}
						
//						 System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..userType>>>>>>>>"+code);
						 
						h.setCode(code);
			

					 }		
			}
		

		}
		
		
		return page;
	}
	

	
	
	public House findByDevice(String deviceId) {
		return houseDao.findByDevice(deviceId);
	}
	
	
	   public List<House> findHouseByDevice(String deviceId){
		   return houseDao.findHouseByDevice(deviceId);
	   }
	
	   public List<House> findAllHouse(House house){
			DetachedCriteria dc = houseDao.createDetachedCriteria();
			Unit unit = house.getUnit();
			User owner = house.getOwner();

			
			if(unit != null){
				dc.createAlias("unit", "unit");
				String unitId = unit.getId();
				if (StringUtils.isNotEmpty(unitId)){
						dc.add(Restrictions.eq("unit.id", unitId));
				}
				Buildings buildings = unit.getBuildings();
				if(buildings != null){
					String buildingsId = buildings.getId();
					if (StringUtils.isNotEmpty(buildingsId)){
						dc.createAlias("unit.buildings", "buildings");
						dc.add(Restrictions.eq("buildings.id", buildingsId));
					}
				}
				
				
				
			}
			
			
			
			
			
			if(owner != null){
				dc.createAlias("owner", "owner");
				String ownerId = owner.getId();
				
				String loginName = owner.getLoginName();
				
				if (StringUtils.isNotEmpty(ownerId)){
					dc.add(Restrictions.eq("owner.id", ownerId));
				}
				
//				if (StringUtils.isNotEmpty(loginName)){
//					dc.add(Restrictions.eq("owner.loginName", loginName));
//				}
				
				Office company = owner.getCompany();
				
				if(company != null){
					dc.createAlias("owner.company", "company");
					String companyId = company.getId();
					if (StringUtils.isNotEmpty(companyId)){
						dc.add(Restrictions.eq("company.id", companyId));
					}
				}
			}

	
			
			dc.add(Restrictions.eq(House.FIELD_DEL_FLAG, House.DEL_FLAG_NORMAL));
			dc.addOrder(Order.asc("numFloor")).addOrder(Order.asc("code"));
		
			return houseDao.find(dc);
		}
	
	@Transactional(readOnly = false)
	public void save(House house) {
		houseDao.clear();
		houseDao.save(house);
		CacheUtils.remove(HouseUtils.CACHE_HOUSE_TREE_MAP);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		houseDao.deleteById(id);
		CacheUtils.remove(HouseUtils.CACHE_HOUSE_TREE_MAP);
	}
	
	@Transactional(readOnly = false)
	public void deleteByUserId(String id) {
//		houseDao.deleteByUsserId(id);
	}
	
	@Transactional(readOnly = false)
	public House saveForExcel(House house,Office offic,Map<String,Community> mpCommunity, Map<String,Buildings> mpBuildings,Map<String,Unit> mpUnit, Map<String,House> mpHouse,int i){

		String proCompanyName = offic.getName();
		String communityName =  house.getCommunityName();

				   String key1 = proCompanyName+communityName;
				   if(!mpCommunity.containsKey(key1)){
					   String sql = "select distinct A from Office P,Community A where  P.name like '%"+ proCompanyName +"%' AND A.name like '%"+ communityName +"%' and A.delFlag=0";
					   sql +=" and P.id = A.proCompany.id"; 
					   List<Community> lsCommunity = communityDao.find(sql);
					   if(lsCommunity.size() == 1){
						   Community community = lsCommunity.get(0);
						   community.setProCompany(offic);
						   community.setDevCompany(offic);
						   mpCommunity.put(key1, community);
					   }else{
						   Community community = new Community();
						   community.setProCompany(offic);
						   community.setDevCompany(offic);
						   community.setName(communityName);
						   community.setCode(communityName);
//						   community.setSort(Integer.valueOf(communityName));
						   
						   communityDao.clear();
						   communityDao.save(community);
//						   System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>community.getId()>>>>>>>>>"+community.getId());
						   mpCommunity.put(key1, community);
					   }
					   
				   }
				   
				   
				   
				   String buildingsName = house.getBuildingsName();
//				   System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>communityName>>>>>>>>>"+communityName);
				   String key2 = proCompanyName+communityName+buildingsName;
				   if(!mpBuildings.containsKey(key2)){
					   
					   String sql = "select distinct B from Office P,Community A,Buildings B where  P.name like '%"+ proCompanyName +"%' AND A.name like '%"+ communityName +"%' and B.name like '%"+ buildingsName +"%'  and B.delFlag=0";
					   sql +=" and P.id = A.proCompany.id"; 
					   sql +=" and A.id = B.community.id"; 
					   
					   List<Buildings> lsBuildings = communityDao.find(sql);
					   if(lsBuildings.size() == 1){
						   Buildings buildings = lsBuildings.get(0);
						   buildings.setCommunity(mpCommunity.get(key1));
						   mpBuildings.put(key2, buildings);
						 
					   }else{
						   Buildings buildings = new Buildings();
						   buildings.setCommunity(mpCommunity.get(key1));
						   buildings.setName(buildingsName);
						   buildings.setCode(buildingsName);
//						   buildings.setSort(Integer.valueOf(buildingsName));
						   
						   buildingsDao.clear();
						   buildingsDao.save(buildings);
						   mpBuildings.put(key2, buildings);
					   }
					   
				   }	   
				   

				   
				   String unitName = house.getUnitName();
				   if(!"单元".equals(unitName)){ unitName = unitName+"单元"; }
//				   System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>unitName>>>>>>>>>"+unitName);
				   String key3 = proCompanyName+communityName+buildingsName+unitName;
				   if(!mpUnit.containsKey(key3)){
					   
					   String sql = "select distinct C from Office P,Community A,Buildings B,Unit C where  P.name like '%"+ proCompanyName +"%' AND A.name like '%"+ communityName +"%' and B.name like '%"+ buildingsName +"%' and C.name like '%"+ unitName +"%'  and C.delFlag=0";
					   sql +=" and P.id = A.proCompany.id"; 
					   sql +=" and A.id = B.community.id"; 
					   sql +=" and B.id = C.buildings.id"; 
							   
					   List<Unit> lsUnit = unitDao.find(sql);
					   if(lsUnit.size() == 1){
						   Unit unit = lsUnit.get(0);
						   unit.setBuildings(mpBuildings.get(key2));
						   mpUnit.put(key3, unit);
					   }else{
						   Unit unit = new Unit();
						   unit.setBuildings(mpBuildings.get(key2));
						   unit.setName(unitName);
						   unit.setCode(unitName);
						   unit.setSort(Integer.valueOf(house.getUnitName()));
						   unitDao.clear();
						   unitDao.save(unit);
						   mpUnit.put(key3, unit);
					   }
					   
				   } 
				   
					   String houseName = house.getName();
					   String numFloor = house.getNumFloorStr();
					   String key4 = proCompanyName+communityName+buildingsName+unitName+numFloor+houseName;
					   
					   
					   if(!mpHouse.containsKey(key4)){
						   String sql ="select distinct D from Office P,Community A,Buildings B,Unit C,House D  where  P.name like '%"+ proCompanyName +"%' AND A.name like '%"+ communityName +"%' and B.name like '%"+ buildingsName +"%'"+"and C.name like '%"+ unitName +"%' and D.name like '%"+ houseName +"%'  and D.numFloor like '%"+ numFloor +"%' and D.delFlag=0";
//						   String sql ="select distinct D from Office P,Community A,Buildings B,Unit C,House D  where  P.name = '"+ proCompanyName +"' AND A.name = '"+ communityName +"' and B.name = '"+ buildingsName +"' and C.name = '"+ unitName +"' and D.name = '"+ houseName +"' and D.numFloor = '"+ numFloor +"'  and D.delFlag=0";
						   
						   sql +=" and P.id = A.proCompany.id"; 
						   sql +=" and A.id = B.community.id"; 
						   sql +=" and B.id = C.buildings.id"; 
						   sql +=" and C.id = D.unit.id"; 
						   List<House> lsHouse = houseDao.find(sql);
						   
						   if(lsHouse.size() == 1){
							   House h1 = lsHouse.get(0);
							   h1.setUnit(mpUnit.get(key3));
							   h1.setOwner(house.getOwner());
							   h1.setBuildArea(StringUtils.toBigDecimal(house.getBuildAreaStr()));
							   h1.setUseArea(StringUtils.toBigDecimal(Arith.roundEVEN(h1.getBuildArea().doubleValue()*0.9, 2)));
							   h1.setNumFloor(StringUtils.getNullValue(house.getNumFloorStr(), "1"));	
							   houseDao.clear();
							   houseDao.save(h1);
							   mpHouse.put(key4, h1);
						   }else{
							   House h = new House();
							   h.setUnit(mpUnit.get(key3));
							   h.setOwner(house.getOwner());
							   h.setName(houseName);
							   h.setCode(houseName);
							   h.setSort(Integer.valueOf(houseName));
//							   System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>1>>>>>>>>>>>..>houseName>>>>>>>>>"+houseName);
							   h.setBuildArea(StringUtils.toBigDecimal(house.getBuildAreaStr()));
							   h.setUseArea(StringUtils.toBigDecimal(StringUtils.toDouble(house.getBuildAreaStr())*0.9));
							   h.setNumFloor(StringUtils.getNullValue(house.getNumFloorStr(), "1"));	
//							   System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>2>>>>>>>>>>>..>houseName>>>>>>>>>"+h.getOwner().getName());
							   houseDao.clear();
							   houseDao.save(h);
//							   System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>3>>>>>>>>>>>..>houseName>>>>>>>>>"+houseName);
							   mpHouse.put(key4, h);
						   }			   
					   
					   }	   
				   
				   return mpHouse.get(key4);

	   }
	
}
