/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.pms.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import com.thinkgem.jeesite.common.utils.Arith;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.entity.Buildings;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.DeviceDetail;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.Unit;
import com.thinkgem.jeesite.modules.pms.service.BuildingsService;
import com.thinkgem.jeesite.modules.pms.service.CommunityService;
import com.thinkgem.jeesite.modules.pms.service.DeviceDetailService;
import com.thinkgem.jeesite.modules.pms.service.DeviceService;
import com.thinkgem.jeesite.modules.pms.service.FeesService;
import com.thinkgem.jeesite.modules.pms.service.HouseService;
import com.thinkgem.jeesite.modules.pms.service.UnitService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 字典工具类
 * 
 * @author ThinkGem
 * @version 2013-5-29
 */
public class DeviceDetailUtils {

	private static CommunityService communityService = SpringContextHolder
			.getBean(CommunityService.class);
	private static FeesService feesService = SpringContextHolder
			.getBean(FeesService.class);
	private static BuildingsService buildingsService = SpringContextHolder
			.getBean(BuildingsService.class);
	private static UnitService unitService = SpringContextHolder
			.getBean(UnitService.class);
	private static HouseService houseService = SpringContextHolder
			.getBean(HouseService.class);
	private static DeviceService deviceService = SpringContextHolder
			.getBean(DeviceService.class);
	private static DeviceDetailService deviceDetailService = SpringContextHolder
			.getBean(DeviceDetailService.class);

	

	// feesMode 1 按户收费 2 按房屋面积 3 按房屋建筑面积 4 按使用量 5按房屋使用面积 6 按房屋建筑面积的90% 
	public static void resetList(List<DeviceDetail> ls) {

		for (DeviceDetail dt : ls) {
			Device device = dt.getDevice();
			House h = device.getHouse();
			double unitPrice = StringUtils.toDouble(dt.getUnitPrice()).doubleValue();
			int feesMode = Integer.valueOf(dt.getFeesMode()).intValue();
//			String feesParams = dt.getFeesParams();
			double sumPayMoney = StringUtils.toDouble(dt.getSumPayMoney()).doubleValue();
			// String deviceType = device.getType(); //1公摊 2单位 3个人
			if(sumPayMoney >0){
				dt.setPayMoney(dt.getSumPayMoney());
			}else{
				// 1 按户收费
				if (feesMode == 1) {
					dt.setUsageAmount(new BigDecimal(0));
					dt.setPayMoney(new BigDecimal(unitPrice));
				}
	
				// 2 按房屋建筑面积
				if (feesMode == 2) {
					double buildArea =  StringUtils.toDouble(h.getBuildArea()).doubleValue();
					double payMoney = Arith.roundEVEN(unitPrice * buildArea, 2);
					dt.setUsageAmount(new BigDecimal(buildArea));
					dt.setPayMoney(new BigDecimal(String.valueOf(payMoney)));
				}
				// 3 按房屋加建面积
				if (feesMode == 3) {
					double extArea =  StringUtils.toDouble(h.getExtArea()).doubleValue();
					double payMoney = Arith.roundEVEN(unitPrice * extArea, 2);
					dt.setUsageAmount(new BigDecimal(extArea));
					dt.setPayMoney(new BigDecimal(String.valueOf(payMoney)));
				}
				// 4 按使用量
				if (feesMode == 4) {
					double lastNum = StringUtils.toDouble(dt.getLastNum()).doubleValue();
					double firstNum = StringUtils.toDouble(dt.getFirstNum()).doubleValue();
					double num = StringUtils.toDouble(dt.getUsageAmount()).doubleValue();
					if (num == 0 && lastNum > 0 && lastNum > firstNum) {
						num = lastNum - firstNum;
					}
					double payMoney = Arith.roundEVEN(num * unitPrice, 2);
					dt.setUsageAmount(new BigDecimal(num));
					dt.setPayMoney(new BigDecimal(String.valueOf(payMoney)));
				}
				// 5 按房屋使用面积
				if (feesMode == 5) {
					double useArea = h.getUseArea() == null ? 0 : h.getUseArea().doubleValue();
					double payMoney = Arith.roundEVEN(unitPrice * useArea, 2);
					dt.setUsageAmount(new BigDecimal(useArea));
					dt.setPayMoney(new BigDecimal(String.valueOf(payMoney)));
				}
	
				// 6 按房屋建筑面积的90%
				if (feesMode == 6) {
	//				double buildArea = h.getBuildArea().doubleValue();
	//				double rate = StringUtils.toDouble(feesParams);
	//				double buildArea2 = Arith.roundEVEN(buildArea * rate, 2);
	//				double payMoney = Arith.roundEVEN(unitPrice * buildArea2, 2);
	//				dt.setUsageAmount(new BigDecimal(String.valueOf(buildArea2)));
	//				dt.setPayMoney(new BigDecimal(String.valueOf(dt)));
					dt.setPayMoney(dt.getSumPayMoney());
				}
	
				dt.setSumUsageAmount(dt.getUsageAmount());
				dt.setSumPayMoney(dt.getPayMoney());
			
			}
		}

	}
	// feesMode 1 按户收费 2 按房屋建筑面积 3 按房屋扩建面积 4 按使用量 5按房屋使用面积 6 按房屋建筑面积的90% 
		public static void resetList(DeviceDetail deviceDetailPool,	List<DeviceDetail> ls) {
			// Fees fees = deviceDetailPool.getDevice().getFees();
			// System.out.println("lsChild.size 55 >>>>>>>>>>>>>  deviceDetailPool.getId()  TTT  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+deviceDetailPool.getId());

			System.out.println("lsChild.size 55 deviceDetailPool.getDevice().getId()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ deviceDetailPool.getDevice().getId());
			
			DeviceDetail deviceDetail = new DeviceDetail();
			deviceDetail.setParent(new Device(deviceDetailPool.getDevice().getId()));
			List<DeviceDetail> lsChild = deviceDetailService.find3(deviceDetail);
			System.out.println("lsChild.size 55 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ lsChild.size());

			double totalBuildArea = 0;
			double totalUseArea = 0;
			double totalExtArea = 0;
			double totalUsageAmount = 0;
//			double temp_totalNum = 0;
			double temp_poolTotalPay = 0;
			
			double maxValue = 0;
			Map<String,DeviceDetail> mp = new HashMap<String,DeviceDetail>();

			for (DeviceDetail dt : lsChild) {
				Device d = dt.getDevice();
				double buildArea = d.getHouse().getBuildArea() == null ? 0 : d.getHouse().getBuildArea().doubleValue();
				double useArea = d.getHouse().getUseArea() == null ? 0 : d.getHouse().getUseArea().doubleValue();
				double extArea = d.getHouse().getExtArea() == null ? 0 : d.getHouse().getExtArea().doubleValue();
				double usageAmount = dt.getUsageAmount() == null ? 0 : dt.getUsageAmount().doubleValue();
				totalBuildArea += buildArea;
				totalUseArea += useArea;
				totalExtArea += extArea;
				totalUsageAmount += usageAmount;
				
				dt.setPoolUsageAmount(new BigDecimal(0));
				dt.setPoolPayMoney(new BigDecimal(0));
			}
			System.out.println("lsChild.size 55 >>>>>>>>>>totalBuildArea>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ totalBuildArea);
			System.out.println("lsChild.size 55 >>>>>>>>>>totalUseArea>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ totalUseArea);
			System.out.println("lsChild.size 55 >>>>>>>>>>totalUsageAmount>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ totalUsageAmount);

			double poolCount = lsChild.size();
			if (poolCount == 0)poolCount = 1;
			double poolUsageAmount = StringUtils.toDouble(deviceDetailPool.getUsageAmount()).doubleValue();
			double poolPayMoney = StringUtils.toDouble(deviceDetailPool.getSumPayMoney()).doubleValue();
			double poolUnitPrice = StringUtils.toDouble(deviceDetailPool.getUnitPrice()).doubleValue();
			int poolFeesMode = Integer.valueOf(deviceDetailPool.getFeesMode()).intValue();
			String poolFeesParams = deviceDetailPool.getFeesParams();
			if (poolPayMoney == 0) poolPayMoney = poolUsageAmount * poolUnitPrice;

			double poolUsageAmountOne = poolUsageAmount / poolCount; // 每户公摊量
			double poolPayMoneyOne = poolPayMoney / poolCount; // 每户公摊金额

			for (DeviceDetail dt : ls) {
				Device device = dt.getDevice();
//				double unit_price = StringUtils.toDouble(dt.getUnitPrice()).doubleValue();

				// 1 按住户  取平均值
				if (poolFeesMode == 1) {
					dt.setPoolUsageAmount(new BigDecimal(0));
					dt.setPoolPayMoney(new BigDecimal(poolPayMoneyOne));
					temp_poolTotalPay +=poolPayMoneyOne;
					if(poolPayMoneyOne > maxValue ){
						maxValue = poolPayMoneyOne;
						mp.put("max", dt);
					}
				}
				
				// 2 按房屋建筑面积 6 按房屋建筑面积的90%
				if (poolFeesMode == 2) {
					House h = device.getHouse();
					double buildArea = h.getBuildArea() == null ? 0 : h.getBuildArea().doubleValue();
					double rate = buildArea / totalBuildArea;
					double payMoney = new Double(Arith.roundEVEN(poolPayMoney* rate, 2)).doubleValue();
//					boolean isPoolDevice = !"0".equals(dt.getParent().getId());
					dt.setPoolUsageAmount(new BigDecimal(rate));
					dt.setPoolPayMoney(new BigDecimal(payMoney));
					temp_poolTotalPay +=payMoney;
					if(payMoney > maxValue ){
						maxValue = payMoney;
						mp.put("max", dt);
					}
				}

				// 3 按加建面积 
				if (poolFeesMode == 3 ) {
					House h = device.getHouse();
					double extArea = h.getExtArea() == null ? 0 : h.getExtArea().doubleValue();
					double rate = extArea / totalExtArea;
					double payMoney = new Double(Arith.roundEVEN(poolPayMoney* rate, 2)).doubleValue();
					dt.setPoolUsageAmount(new BigDecimal(rate));
					dt.setPoolPayMoney(new BigDecimal(payMoney));
					temp_poolTotalPay +=payMoney;
					if(payMoney > maxValue ){
						maxValue = payMoney;
						mp.put("max", dt);
					}
//					maxValue = Math.max(maxValue,payMoney);
				}

				// 4 按使用量
				if (poolFeesMode == 4) {
					double usageAmount = StringUtils.toDouble(dt.getUsageAmount()).doubleValue();
					double rate = usageAmount / totalUsageAmount;
					double payMoney = new Double(Arith.roundEVEN(poolPayMoney* rate, 2)).doubleValue();
					dt.setPoolUsageAmount(new BigDecimal(rate));
					dt.setPoolPayMoney(new BigDecimal(payMoney));
					temp_poolTotalPay +=payMoney;
					if(payMoney > maxValue ){
						maxValue = payMoney;
						mp.put("max", dt);
					}
				}

				// 5  按使用面积
				if (poolFeesMode == 5) {
					House h = device.getHouse();
					double useArea = h.getUseArea() == null ? 0 : h.getUseArea().doubleValue();
					double rate = useArea / totalUseArea;
					double payMoney = new Double(Arith.roundEVEN(poolPayMoney* rate, 2)).doubleValue();
					dt.setPoolUsageAmount(new BigDecimal(rate));
					dt.setPoolPayMoney(new BigDecimal(payMoney));
					temp_poolTotalPay +=payMoney;
					if(payMoney > maxValue ){
						maxValue = payMoney;
						mp.put("max", dt);
					}
				}

				Double sumUsageAmount = Arith.roundEVEN(Arith.add(dt.getUsageAmount(), dt.getPoolUsageAmount()),2);
				Double sumPayMoney = Arith.roundEVEN(Arith.add(dt.getPayMoney(), dt.getPoolPayMoney()),2);
				
				dt.setSumUsageAmount(new BigDecimal(sumUsageAmount));
				dt.setSumPayMoney(new BigDecimal(sumPayMoney));
			}
			
			//公摊尾数处理
			if(temp_poolTotalPay >0 && poolPayMoney != temp_poolTotalPay ){
				DeviceDetail dDetail = mp.get("max");
				double pool_payMoney = StringUtils.toDouble(dDetail.getPoolPayMoney()).doubleValue();
				System.out.println(">>>>>>>>>>>>>>>>>>>>  temp_poolTotalPay >>>>>>>>666666666666666666>>>>>>>>>>>>>>>>"+temp_poolTotalPay);
				if(temp_poolTotalPay > poolPayMoney){
					double leave = temp_poolTotalPay - poolPayMoney;
					System.out.println(">>>>>>>>>>>>>>>>>>>>  leave >>>>>>>>666666666666666666>>>>>>>>>>>>>>>>"+leave);
					dDetail.setPoolPayMoney(new BigDecimal(pool_payMoney-leave));
				}else{
					double leave =  poolPayMoney - temp_poolTotalPay;
					dDetail.setPoolPayMoney(new BigDecimal(pool_payMoney+leave));
				}
				Double sumPayMoney = Arith.roundEVEN(Arith.add(dDetail.getPayMoney(), dDetail.getPoolPayMoney()),2);
				dDetail.setSumPayMoney(new BigDecimal(sumPayMoney));
			}
		}
	
	public static String getCode(Device device) {

		String type = device.getType();
		String code = device.getCode();
		House house = device.getHouse();

		// System.out.println(">>>>>>>>>>>>>>>>>>>>  house >>>>>>>>666666666666666666>>>>>>>>>>>>>>>>"+
		// house);
		// System.out.println(">>>>>>>>>>>>>>>>>>>>  house >>>>>>>>666666666666666666>>>>>>>>>>>>>>>>"+
		// house.getId()+""+ house.getCode());

		User owner = house.getOwner();
		String houseId = house.getId();
		Fees fees = device.getFees();
		String feesModel = fees.getFeesMode();

		if (!"1".equals(type)) {

			if (StringUtils.isEmpty(code)) {

				// 电话费 把电话号码作为设备号
				if ("5".equals(feesModel)) {
					if (owner != null) {
						String phone = owner.getPhone();
						if (StringUtils.isNotEmpty(phone)) {
							code = phone;
						}
					}
				}

				if (StringUtils.isEmpty(code)) {
					code = StringUtils.lpad(6, Integer.valueOf(houseId));
					code += "_" + fees.getCode();
				}
			}
		} else {
			if (StringUtils.isEmpty(code)) {
				code = "P_" + fees.getCode();
			}
		}
		return code;
	}

	public static void setObjFromReq(Device device, HttpServletRequest request,
			HttpServletResponse response, Model model, int from) {
		@SuppressWarnings("unchecked")
		Unit unit = new Unit();
		Buildings buildings = new Buildings();
		Community community = new Community();
		Office proCompany = new Office();
		House house = new House();
		Fees fees = new Fees();
		User owner = new User();
		Office company = new Office();

		StringBuffer houseIds = new StringBuffer();

		String proCompanyId = "";
		String companyId = "";
		String feesId = "";
		String communityId = "";
		String buildingsId = "";
		String unitId = "";
		String houseId = "";
		String type = "";

		if (from == 1) {
			proCompanyId = request.getParameter("fees.company.id");
			companyId = request.getParameter("device.house.owner.company.id");
			feesId = request.getParameter("fees.id");
			communityId = request
					.getParameter("house.unit.buildings.community.id");
			buildingsId = request.getParameter("house.unit.buildings.id");
			unitId = request.getParameter("house.unit.id");
			houseId = request.getParameter("house.id");
			// type = request.getParameter("type");
		}

		if (from == 2) {
			proCompanyId = request.getParameter("device.fees.company.id");
			companyId = request.getParameter("device.house.owner.company.id");
			feesId = request.getParameter("device.fees.id");
			communityId = request
					.getParameter("device.house.unit.buildings.community.id");
			buildingsId = request
					.getParameter("device.house.unit.buildings.id");
			unitId = request.getParameter("device.house.unit.id");
			houseId = request.getParameter("device.house.id");
			// type = request.getParameter("device.type");
		}

		// System.out.println("device.fees.company.id>>>>>>>>>>>>"+request.getParameter("device.fees.company.id"));
		// System.out.println("feesId>>>>>>>>>>>>"+request.getParameter("device.fees.id"));
		// System.out.println("deviceDetail.device.fees.id>>>>>>>>>>>>"+request.getParameter("deviceDetail.device.fees.id"));

		String feesMode = FeesUtils.getFeesMode(feesId);
		// fees.setId(feesId);

		List<Office> proCompanyList = UserUtils.findProCompanyList();

		if (device == null)
			device = new Device();

		if (device.getFees() == null)
			device.setFees(fees);
		if (device.getHouse() == null)
			device.setHouse(house);
		if (device.getFees().getCompany() == null)
			device.getFees().setCompany(proCompany);
		if (device.getHouse().getOwner() == null)
			device.getHouse().setOwner(owner);
		if (device.getHouse().getOwner().getCompany() == null)
			device.getHouse().getOwner().setCompany(company);

		if (device.getFees() == null)
			device.setFees(fees);
		if (device.getHouse().getUnit() == null)
			device.getHouse().setUnit(unit);
		if (device.getHouse().getUnit().getBuildings() == null)
			device.getHouse().getUnit().setBuildings(buildings);
		if (device.getHouse().getUnit().getBuildings().getCommunity() == null)
			device.getHouse().getUnit().getBuildings().setCommunity(community);
		if (device.getHouse().getUnit().getBuildings().getCommunity()
				.getProCompany() == null)
			device.getHouse().getUnit().getBuildings().getCommunity()
					.setProCompany(proCompany);

		House hhh = device.getHouse();

		if (StringUtils.isNotBlank(proCompanyId)) {
			hhh.getUnit().getBuildings().getCommunity().getProCompany()
					.setId(proCompanyId);
			device.getFees().getCompany().setId(proCompanyId);
			if (model != null)
				model.addAttribute(
						"communityList",
						communityService.findAllCommunity(hhh.getUnit()
								.getBuildings().getCommunity()));
			houseIds.append("procompany" + proCompanyId + ",");
		} else {
			if (proCompanyList.size() > 0) {
				proCompany = proCompanyList.get(0);
				proCompanyId = proCompany.getId();
				hhh.getUnit().getBuildings().getCommunity()
						.setProCompany(proCompanyList.get(0));
				device.getFees().setCompany(proCompanyList.get(0));
				if (model != null)
					model.addAttribute(
							"communityList",
							communityService.findAllCommunity(hhh.getUnit()
									.getBuildings().getCommunity()));
			}

		}

		if (StringUtils.isNotBlank(companyId)) {
			hhh.getOwner().setCompany((new Office(companyId)));
			// model.addAttribute("communityList",
			// communityService.findAllCommunity(hhh.getUnit().getBuildings().getCommunity()));
		}

		List<Fees> feesList = FeesUtils.getALLFees(proCompanyId);
		if (feesList.size() > 0 && feesId == null) {
			fees = feesList.get(0);
			feesId = fees.getId();
		}
		if (model != null)
			model.addAttribute("feesList", feesList);
		// if (StringUtils.isNotBlank(proCompanyId)){
		// // device.getFees().setId(feesId);
		// // device.getFees().setCompany(new Office(proCompanyId));
		// if(model!= null) model.addAttribute("feesList", feesList);
		// }else{
		// // if(proCompanyList.size() > 0){
		// // fees.setCompany(proCompanyList.get(0));
		// // }
		// if(model!= null) model.addAttribute("feesList", feesList);
		// // device.getFees().setCompany(new Office(proCompanyId));
		// }

		if (StringUtils.isNotBlank(communityId)) {
			hhh.getUnit().getBuildings().getCommunity().setId(communityId);
			if (model != null)
				model.addAttribute("buildingsList", buildingsService
						.findAllBuildings(hhh.getUnit().getBuildings()));
			houseIds.append("community" + communityId + ",");
		}

		if (StringUtils.isNotBlank(buildingsId)) {
			hhh.getUnit().getBuildings().setId(buildingsId);
			if (model != null)
				model.addAttribute("unitList",
						unitService.findAllUnit(hhh.getUnit()));
			houseIds.append("buildings" + buildingsId + ",");
		}

		if (StringUtils.isNotBlank(unitId)) {
			hhh.getUnit().setId(unitId);

			if (model != null) {
				List<House> houseList = houseService.findAllHouse(hhh);
				for (House h : houseList) {
					h.setName(h.getCode() + "(" + h.getOwner().getName() + ")");
				}

				model.addAttribute("houseList", houseList);
			}
			houseIds.append("unit" + unitId + ",");
		}

		if (StringUtils.isNotBlank(houseId)) {
			hhh.setId(houseId);
			houseIds.append("house" + houseId);
		}

		if (StringUtils.isNotBlank(feesId)) {
			device.getFees().setId(feesId);
		}

		device.setHouseIds(houseIds.toString());

		if (model != null) {
			List<Office> companyList = UserUtils
					.findCompanyListByProCompany(proCompanyId);
			Device dev = new Device();
			dev.setType("1");
			List<Device> parentDeviceList = deviceService.find(dev);
			model.addAttribute("proCompanyList", proCompanyList);
			model.addAttribute("companyList", companyList);
			model.addAttribute("feesMode", feesMode);
			model.addAttribute("parentList", parentDeviceList);
		}

		// model.addAttribute("fees.id", feesMode);

	}
	
	public static void getObjFromReq(DeviceDetail deviceDetal, HttpServletRequest request,Model model,int type){

		 String proCompanyId = request.getParameter("device.fees.company.id");
		 String feesId = request.getParameter("device.fees.id");
		 String firstDate = request.getParameter("firstDate");
		 String lastDate = request.getParameter("lastDate");
		 String deviceType = request.getParameter("device.type");
		 
		 String companyId = request.getParameter("device.house.owner.company.id");
		 String communityId = request.getParameter("device.house.unit.buildings.community.id");
		 String buildingsId = request.getParameter("device.house.unit.buildings.id");
		 String unitId = request.getParameter("device.house.unit.id");
		 String houseId = request.getParameter("device.house.id");

		 String isPay = request.getParameter("isPay");
		 String longinName = request.getParameter("device.house.owner.longinName");
		 String officeId = request.getParameter("officeId");
		 
		 String fids = request.getParameter("fids");
		 String userids = request.getParameter("userids");	
		 
		 
	
		 
	     List<String> userIdList = "null".equals(userids)||"".equals(userids)||userids == null?Collections.EMPTY_LIST:Arrays.asList(userids.split(","));
	     List<String> feesIdList = "null".equals(fids)||"".equals(fids)||fids == null?Collections.EMPTY_LIST:Arrays.asList(fids.split(","));
	   
         

//	     System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>userIdList.size()>>>>>>>>>"+ userIdList.size());
//	     System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>feesIdList.size()>>>>>>>>>"+ feesIdList.size());
		 
		Device device = new Device();
		Office company = new Office();	 
		User owner = new User();
		Office proCompany = new Office();
		House house = new House();
		Fees fees = new Fees();
		Unit unit = new Unit();
		Buildings buildings = new Buildings();
		Community community = new Community();

		unit.setBuildings(buildings);
		buildings.setCommunity(community);
		community.setProCompany(proCompany);
		fees.setCompany(proCompany);
		owner.setCompany(company);
		house.setUnit(unit);
		house.setOwner(owner);
		
		device.setHouse(house);
		device.setFees(fees);
		
		device.setLastDate(null);
		device.setFirstDate(null);
		device.setPaymentDate(null);
		device.setPool(null);
		deviceDetal.setFirstDate(null);
		deviceDetal.setLastDate(null);
		deviceDetal.setDevice(device);
		deviceDetal.setIsPay(null);
		
	
		if (StringUtils.isNotBlank(proCompanyId)){
			proCompany.setId(proCompanyId);
		}
		
		
		
		if (StringUtils.isNotBlank(deviceType)){
			device.setType(deviceType);
		}	
		
		if(firstDate != null && !StringUtils.isBlank(firstDate)){
			try {
				Date theDate = DateUtils.parseDate(firstDate, new String[]{"yyyy-MM-dd"});
				deviceDetal.setFirstDate(theDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(lastDate != null && !StringUtils.isBlank(lastDate)){
			try {
				Date theDate = DateUtils.parseDate(lastDate, new String[]{"yyyy-MM-dd"});
				deviceDetal.setLastDate(theDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (StringUtils.isNotBlank(feesId)){
			fees.setId(feesId);
		}

		
		if (feesIdList.size() > 0){
			fees.setFeesIdList(feesIdList);
		}

		if (userIdList.size() > 0){
			owner.setUserIdList(userIdList);
		}
		
		
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>feesIdList.size()>>>>>>>>>"+ feesIdList.size());
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>userIdList.size()>>>>>>>>>"+ userIdList.size());
		
		
		if (StringUtils.isNotBlank(communityId)){
			community.setId(communityId);
		}
		if (StringUtils.isNotBlank(buildingsId)){
			buildings.setId(buildingsId);
		}
		if (StringUtils.isNotBlank(unitId)){
			unit.setId(unitId);
		} 
		if (StringUtils.isNotBlank(houseId)){
			house.setId(houseId);
		}		
		
		if (StringUtils.isNotBlank(isPay)){
			deviceDetal.setIsPay(isPay);
		}		
		if (StringUtils.isNotBlank(longinName)){
			owner.setLoginName(longinName);
		}	
		
		 


		if(type == 1){
			List<Office> proCompanyList = UserUtils.findProCompanyList();
			if (StringUtils.isBlank(proCompanyId)){
				if(proCompanyList.size()>0){
					proCompanyId = proCompanyList.get(0).getId();
					proCompany.setId(proCompanyId);
				}

			}
			model.addAttribute("proCompanyList", proCompanyList);

			if (StringUtils.isBlank(deviceType)){
				device.setType("1");
				deviceType = "1";
			}
	
			List<Fees> feesList = FeesUtils.getALLFees(proCompanyId);
			if (StringUtils.isBlank(feesId)){
				if(feesList.size()>0){
					feesId = feesList.get(0).getId();
					fees.setId(feesId);
				}

			}
			model.addAttribute("feesList", feesList);
	
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
				List<House> houseList = houseService.findAllHouse(house);
				model.addAttribute("houseList", houseList);
			} 

		}
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>feesId>>>>>>>>>"+ feesId);
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>proCompanyId>>>>>>>"+ proCompanyId);
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>deviceType>>>>>"+ deviceType);		 
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>firstDate>>>>>"+ firstDate);	
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>lastDate>>>>>"+ lastDate);	
//		 
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>deviceDetal.getIsPay()>>>>"+ deviceDetal.getIsPay());
//		 
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>companyId>>>>>>"+ companyId);
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>communityId>>>>>>"+ communityId);
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>buildingsId>>>>>>>"+ buildingsId);
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>unitId>>>>>>"+ unitId);
//		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>houseId>>>>"+ houseId);

		 
	}

}
