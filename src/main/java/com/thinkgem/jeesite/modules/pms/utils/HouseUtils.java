/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.pms.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.dao.HouseDao;
import com.thinkgem.jeesite.modules.pms.entity.Buildings;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.DeviceDetail;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.Unit;
import com.thinkgem.jeesite.modules.pms.service.BuildingsService;
import com.thinkgem.jeesite.modules.pms.service.CommunityService;
import com.thinkgem.jeesite.modules.pms.service.DeviceDetailService;
import com.thinkgem.jeesite.modules.pms.service.DeviceService;
import com.thinkgem.jeesite.modules.pms.service.HouseService;
import com.thinkgem.jeesite.modules.pms.service.UnitService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
//import org.activiti.engine.identity.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 字典工具类
 * @author ThinkGem
 * @version 2013-5-29
 */
public class HouseUtils {
	private static CommunityService communityService = SpringContextHolder.getBean(CommunityService.class);
	private static BuildingsService buildingsService = SpringContextHolder.getBean(BuildingsService.class);
	private static UnitService unitService = SpringContextHolder.getBean(UnitService.class);
	private static HouseService houseService = SpringContextHolder.getBean(HouseService.class);
//	private static DeviceService deviceService = SpringContextHolder.getBean(DeviceService.class);
//	private static DeviceDetailService deviceDetailService = SpringContextHolder.getBean(DeviceDetailService.class);
	
	private static HouseDao houseDao = SpringContextHolder.getBean(HouseDao.class);
	
	public static final String CACHE_HOUSE_TREE_MAP = "house_tree_map";
	

//
	public static final CharSequence CS1 = "procompany";
	public static final CharSequence CS2 = "community";
	public static final CharSequence CS3 = "buildings";
	public static final CharSequence CS4 = "unit";
	public static final CharSequence CS5 = "house";
	
//	public static Fees getFees(String id){
//		@SuppressWarnings("unchecked")
////		Map<String, Fees> dictMap = (Map<String, Fees>)CacheUtils.get(CACHE_HOUSE_TREE_MAP);
//		Map<String, Object> map = (Map<String, Object>)CacheUtils.get(CACHE_HOUSE_TREE_MAP);
//		
//		if (map==null){
//			map = Maps.newHashMap();
//			for (Fees fees : feesDao.findAll()){
//				dictMap.put(fees.getId(), fees);
//			}
//			CacheUtils.put(CACHE_FEES_MAP, dictMap);
//		}
//		Fees dictList = dictMap.get(id);
//		if (dictList == null){
//			dictList = new Fees();
//		}
//		return dictList;
//	}
	
	
	public static House  getHouse(String houseId){
		@SuppressWarnings("unchecked")

		
		String idd = "0";
		
		if(houseId !=null ){
			
			
			if(StringUtils.isNotBlank(houseId) && houseId.length()>0){
				String tmp =  houseId.toLowerCase();
					if(tmp.contains(CS5)){
						 idd = tmp.replace(CS5, "");
					}else{
						 idd = tmp;
					}
			}

		}
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>>HouseUtils  house >>>>>>>>222222222222222222222222222>>>>>>>>>>>>>>>>"+ idd);
		House h = houseDao.get(idd);
//		System.out.println(">>>>>>>>>>>>>>>>>>>>HouseUtils  house >>>>>>>>333333333333333333333333333>>>>>>>>>>>>>>>>"+ h);
		return h;

	
	}
	

	public static List<House>  getHousesList(String houseIds){
		@SuppressWarnings("unchecked")
		
		List<House> all = Lists.newArrayList();
		
		Map<String, String> mp = new HashMap<String, String>();
		
		if(houseIds !=null ){
			
			if(StringUtils.isNotBlank(houseIds) && houseIds.length()>0){
				
				String tmp =  houseIds.toLowerCase();
				
				System.out.println("HouseUtils.getHousesList houseIds>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ tmp);
				
				String[] ids = tmp.split(",");
				
				for(String id:ids){
					
				
					
//					ProCompany Community Buildings Unit House
					if(id.contains(CS1)){
						String idd = id.replace(CS1, "");
//						mp.put(CS1.toString(), idd);
						all.addAll(houseDao.findByProcompany(idd));
					}else if(id.contains(CS2)){
						String idd = id.replace(CS2, "");
//						mp.put(CS2.toString(),  idd);
						all.addAll(houseDao.findByCommunityId(idd));
					}else if(id.contains(CS3)){
						String idd = id.replace(CS3, "");
//						mp.put(CS3.toString(),  id.replace(CS3, ""));
						all.addAll(houseDao.findByBuildingsId(idd));
					}else if(id.contains(CS4)){
						String idd = id.replace(CS4, "");
//						mp.put(CS4.toString(),  id.replace(CS4, ""));
						all.addAll(houseDao.findByUnitId(idd));
					}else if(id.contains(CS5)){
						String idd = id.replace(CS5, "");
//						mp.put(CS5.toString(),  id.replace(CS5, ""));
						all.add(houseDao.get(idd));
					}
				}

			}

		}
		
//		System.out.println("HouseUtils.getHousesList>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+mp.toString());
		
//		System.out.println("HouseUtils.getHousesList>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+all.size());
		
		return all;
	
	}
	
	
	
	public static List<House>  getHousesList2(String houseIds){
		@SuppressWarnings("unchecked")
		
		List<House> all = Lists.newArrayList();
		
		Map<String, String> mp = new HashMap<String, String>();
		
		if(houseIds !=null ){
			
			if(StringUtils.isNotBlank(houseIds) && houseIds.length()>0){
				
				String tmp =  houseIds.toLowerCase();
				
				System.out.println("HouseUtils.getHousesList houseIds tmp  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ tmp);
				
				String[] ids = tmp.split(",");
				String proCompanyId="";
				String communityId="";
				String buildingsId="";
				String unitId="";
				String houseId="";
				
				for(String id:ids){
//					ProCompany Community Buildings Unit House
					if(id.contains(CS1)){
						String idd = id.replace(CS1, "");
						mp.put(CS1.toString(), idd);
//						all.addAll(houseDao.findByProcompany(idd));
						proCompanyId = idd;
					}else if(id.contains(CS2)){
						String idd = id.replace(CS2, "");
						mp.put(CS2.toString(),  idd);
//						all.addAll(houseDao.findByCommunityId(idd));
						communityId = idd;
					}else if(id.contains(CS3)){
						String idd = id.replace(CS3, "");
						mp.put(CS3.toString(),  id.replace(CS3, ""));
//						all.addAll(houseDao.findByBuildingsId(idd));
						buildingsId = idd;
					}else if(id.contains(CS4)){
						String idd = id.replace(CS4, "");
						mp.put(CS4.toString(),  id.replace(CS4, ""));
//						all.addAll(houseDao.findByUnitId(idd));
						unitId = idd;
					}else if(id.contains(CS5)){
						String idd = id.replace(CS5, "");
						mp.put(CS5.toString(),  id.replace(CS5, ""));
//						all.add(houseDao.get(idd));
						houseId = idd;
					}
				}
				
				System.out.println("HouseUtils. proCompanyId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ proCompanyId);
				System.out.println("HouseUtils. communityId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ communityId);
				System.out.println("HouseUtils. buildingsId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ buildingsId);
				System.out.println("HouseUtils. unitId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ unitId);
				System.out.println("HouseUtils. houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ houseId);
				
				all.addAll(houseDao.findByProcompany(proCompanyId, communityId, buildingsId, unitId,houseId));

			}

		}
		
//		System.out.println("HouseUtils.getHousesList>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+mp.toString());
		
		System.out.println("HouseUtils.getHousesList>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+all.size());
		
		return all;
	
	}
	public static List<House>  getHousesList3(String houseIds){
		@SuppressWarnings("unchecked")
		
		List<House> all = Lists.newArrayList();
		
		Map<String, String> mp = new HashMap<String, String>();
		
		if(houseIds !=null ){
			
			if(StringUtils.isNotBlank(houseIds) && houseIds.length()>0){
				
				String tmp =  houseIds.toLowerCase();
				
				System.out.println("HouseUtils.getHousesList houseIds tmp  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ tmp);
				
				String[] ids = tmp.split(",");
				String proCompanyId="";
				String communityId="";
				String buildingsId="";
				String unitId="";
				String houseId="";
				
//				List<String> values = Lists.newArrayList();
				StringBuffer strbuf = new StringBuffer();
				for(String id:ids){
					if(id.contains(CS5)){
						String idd = id.replace(CS5, "");
//						values.add(idd);
						strbuf.append(",").append(idd);

					}
				}
				String values = strbuf.deleteCharAt(0).toString(); 
//				System.out.println("HouseUtils. proCompanyId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ proCompanyId);
//				System.out.println("HouseUtils. communityId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ communityId);
//				System.out.println("HouseUtils. buildingsId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ buildingsId);
				System.out.println("HouseUtils3. values.toString()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ values);
				
				all.addAll(houseDao.findByIds(values));

			}

		}
		
//		System.out.println("HouseUtils.getHousesList>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+mp.toString());
		
		System.out.println("HouseUtils.getHousesList>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+all.size());
		
		return all;
	
	}
	public static List<User>  getUsersByHouse(List<House> houseList){
		@SuppressWarnings("unchecked")
		List<User> userList = Lists.newArrayList();
		for(House h :houseList){
			User u = h.getOwner();
			userList.add(u);
		}
		
		return userList;
	}
	
	public static void  resetHouseList(List<House> houseList){
	
		for(House h :houseList){
			User u = h.getOwner();
			String userType = u.getUserType();
			String name ="";
			String companyName = u.getCompany().getName();
			if("2".equals(userType)){
				name = h.getName() +"("+ u.getName() +")";
			}else{

				name =companyName;
			}
			
			h.setName(name);
		}
		
		
	}
	
		
	public static List<String>  getUsersByHouseIds(List<House> houseList){
		@SuppressWarnings("unchecked")
		List<String> userList = Lists.newArrayList();
		for(House h :houseList){
			User u = h.getOwner();
			if(u != null) userList.add(u.getId());
		}
		
		return userList;
	}
	
	
	public static void getObjFromReq(House house, HttpServletRequest request, HttpServletResponse response,Model model,int type,String from){
		String proCompanyId = "";
		String communityId =  "";
		String buildingsId = "";
		String unitId =  "";
		

		if("house".equals(from)){
			 proCompanyId = request.getParameter("unit.buildings.community.proCompany.id");
			 communityId = request.getParameter("unit.buildings.community.id");
			 buildingsId = request.getParameter("unit.buildings.id");
			 unitId = request.getParameter("unit.id");
    	}
  		
		if("device".equals(from)){
			 proCompanyId = request.getParameter("house.unit.buildings.community.proCompany.id");
			 communityId = request.getParameter("house.unit.buildings.community.id");
			 buildingsId = request.getParameter("house.unit.buildings.id");
			 unitId = request.getParameter("house.unit.id");
		}	
  		if("deviceDetail".equals(from) || "paymentDetail".equals(from)){
			 proCompanyId = request.getParameter("device.house.unit.buildings.community.proCompany.id");
			 communityId = request.getParameter("device.house.unit.buildings.community.id");
			 buildingsId = request.getParameter("device.house.unit.buildings.id");
			 unitId = request.getParameter("device.house.unit.id");
    	}

  		
		
		
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>communityId>>>>>>"+ communityId);
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>buildingsId>>>>>>>"+ buildingsId);
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>unitId>>>>>>"+ unitId);
		 

		User owner = new User();
		Office company = new Office();
		Office proCompany = new Office();

		Unit unit = new Unit();
	    Buildings buildings = new Buildings();
	    Community community = new Community();
	    
		owner.setCompany(company);
		unit.setBuildings(buildings);
		buildings.setCommunity(community);
		community.setProCompany(proCompany);
		
		if (StringUtils.isNotBlank(proCompanyId)){
			proCompany.setId(proCompanyId);
		}
		if (StringUtils.isNotBlank(communityId)){
			community.setId(communityId);
		}
		if (StringUtils.isNotBlank(buildingsId)){
			buildings.setId(buildingsId);
		}
		if (StringUtils.isNotBlank(unitId)){
			unit.setId(unitId);
		} 		
		
	    house.setOwner(owner);
	    house.setUnit(unit);
	    
	    
	    
	    
		if(type == 1){
			List<Office> proCompanyList = UserUtils.findProCompanyList();
			if (StringUtils.isBlank(proCompanyId)){
				if(proCompanyList.size()>0){
					proCompanyId = proCompanyList.get(0).getId();
					proCompany.setId(proCompanyId);			
				}

			}
			model.addAttribute("proCompanyList", proCompanyList);

	
			if (StringUtils.isNotBlank(proCompanyId)){
				List<Office> companyList = UserUtils.findCompanyListByProCompany(proCompanyId);
				model.addAttribute("companyList", companyList);
				model.addAttribute("communityList",communityService.findAllCommunity(community));	
			}

			if (StringUtils.isNotBlank(communityId)){
				model.addAttribute("buildingsList", buildingsService.findAllBuildings(buildings));
			}
			
			if (StringUtils.isNotBlank(buildingsId)){
				model.addAttribute("unitList",unitService.findAllUnit(unit));
			}
			if (StringUtils.isNotBlank(unitId)){
				Page<House> page = new Page<House>(request, response,-1);
		    	houseService.findPage(page,house,null,0);
				model.addAttribute("houseList", page.getList());
			} 

		}
		
	}
	
	
}
