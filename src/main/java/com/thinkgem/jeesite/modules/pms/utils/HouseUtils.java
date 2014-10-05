/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.pms.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.dao.HouseDao;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
//import org.activiti.engine.identity.User;
import com.google.common.collect.Lists;

/**
 * 字典工具类
 * @author ThinkGem
 * @version 2013-5-29
 */
public class HouseUtils {
	
	private static HouseDao houseDao = SpringContextHolder.getBean(HouseDao.class);
//
	public static final CharSequence CS1 = "procompany";
	public static final CharSequence CS2 = "community";
	public static final CharSequence CS3 = "buildings";
	public static final CharSequence CS4 = "unit";
	public static final CharSequence CS5 = "house";
	
	
	
	
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
		
		System.out.println("HouseUtils.getHousesList>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+all.size());
		
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
			userList.add(u.getId());
		}
		
		return userList;
	}
	
	

	
	
}
