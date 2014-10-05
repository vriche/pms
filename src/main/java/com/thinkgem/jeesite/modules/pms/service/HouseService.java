/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.dao.BuildingsDao;
import com.thinkgem.jeesite.modules.pms.dao.CommunityDao;
import com.thinkgem.jeesite.modules.pms.dao.HouseDao;
import com.thinkgem.jeesite.modules.pms.dao.UnitDao;
import com.thinkgem.jeesite.modules.pms.entity.Buildings;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.Unit;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
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
	
	
	
	
	public House findByDevice(String deviceId) {
		return houseDao.findByDevice(deviceId);
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
				if (StringUtils.isNotEmpty(ownerId)){
					dc.add(Restrictions.eq("owner.id", ownerId));
				}
				
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
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		houseDao.deleteById(id);
	}
	
	
	@Transactional(readOnly = false)
	public House saveForExcel(House house,Office offic,Map<String,Community> mpCommunity, Map<String,Buildings> mpBuildings,Map<String,Unit> mpUnit, Map<String,House> mpHouse,int i){
		   
	
		   
	
		String proCompanyName = offic.getName();

		   
//		   String key0 = proCompanyName;
//		   if(!mpProCompany.containsKey(key0)){
//			   List<Office> lsProCompany = officeDao.find("select distinct P from Office P where P.name like '%"+ proCompanyName +"%'");
//			   if(lsProCompany.size() == 1){
//				   Office office = lsProCompany.get(0);
//				   mpProCompany.put(key0, office);
//			   }else{
//				   Office office = new Office();
//				    office.setCreateDate(new Date());
//					Office parent = new Office();parent.setId("0");
//					office.setParent(parent);
////					Area area = new Area();area.setId("3");
//					office.setArea(areaDao.get("3"));
//					office.setType("1");
//					office.setGrade("1");
//					office.setSort("1");
//					office.setIsCharge("1");				   
//				   office.setName(proCompanyName);
//				   office.setCode(proCompanyName);
//			
//				   officeDao.save(office);
//				   mpProCompany.put(key0, office);
//			   }
//			   
//		   }   
		   

				   String communityName =  house.getCommunityName();
//				   System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>communityName>>>>>>>>>"+communityName);
				   
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
						   buildingsDao.clear();
						   buildingsDao.save(buildings);
						   mpBuildings.put(key2, buildings);
					   }
					   
				   }	   
				   

				   
				   String unitName = house.getUnitName();
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
						   
						   unitDao.clear();
						   unitDao.save(unit);
						   mpUnit.put(key3, unit);
					   }
					   
				   } 
					   String houseName = house.getName();
//					   
//					   System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>houseName>>>>>>>>>"+houseName);
//					   
					   String key4 = proCompanyName+communityName+buildingsName+unitName+houseName;
					   
					   
					   if(!mpHouse.containsKey(key4)){
						   String sql ="select distinct D from Office P,Community A,Buildings B,Unit C,House D  where  P.name like '%"+ proCompanyName +"%' AND A.name like '%"+ communityName +"%' and B.name like '%"+ buildingsName +"%'"+"and C.name like '%"+ unitName +"%' and D.name like '%"+ houseName +"%'  and D.delFlag=0";
						   sql +=" and P.id = A.proCompany.id"; 
						   sql +=" and A.id = B.community.id"; 
						   sql +=" and B.id = C.buildings.id"; 
						   sql +=" and C.id = D.unit.id"; 
						   List<House> lsHouse = unitDao.find(sql);
						   if(lsHouse.size() == 1){
							   House h1 = lsHouse.get(0);
							   h1.setUnit(mpUnit.get(key3));
							   h1.setOwner(house.getOwner());
							   mpHouse.put(key4, h1);
						   }else{
							   House h = new House();
//							   h.setUnit(mpUnit.get(key3));
//							   h.setOwner(house.getOwner());
//							   h.setName(houseName);
//							   h.setCode(houseName);
//							   h.setBuildArea(StringUtils.toBigDecimal(house.getBuildAreaStr()));
//							   h.setUseArea(StringUtils.toBigDecimal(house.getUseAreaStr()));
//							   houseDao.clear();
//							   houseDao.save(h);
							   
							   h.setUnit(mpUnit.get(key3));
							   h.setOwner(house.getOwner());
							   h.setName(houseName);
							   h.setCode(houseName);
							   h.setBuildArea(StringUtils.toBigDecimal(house.getBuildAreaStr()));
							   h.setUseArea(StringUtils.toBigDecimal(house.getUseAreaStr()));
							   h.setNumFloor(new Integer(StringUtils.getNullValue(house.getNumFloorStr(), "1")));					   
							   houseDao.clear();
							   houseDao.save(h);
							   
							   mpHouse.put(key4, h);
						   }			   
					   
					   }	   
				   
				   return mpHouse.get(key4);

	   }
	
}
