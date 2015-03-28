package com.thinkgem.jeesite.modules.pms.utils;

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.entity.Buildings;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.DeviceDetail;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.PaymentAfter;
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

public class PaymentAfterUtils {
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
	

	public PaymentAfterUtils() {
		// TODO Auto-generated constructor stub
	}

	public static void getObjFromReq(PaymentAfter paymentAfter, HttpServletRequest request,Model model,int type){
			 String proCompanyId = request.getParameter("device.fees.company.id");
			 String feesId = request.getParameter("device.fees.id");
			 String deviceType = request.getParameter("device.type");
			 
			 String companyId = request.getParameter("device.house.owner.company.id");
			 String communityId = request.getParameter("device.house.unit.buildings.community.id");
			 String buildingsId = request.getParameter("device.house.unit.buildings.id");
			 String unitId = request.getParameter("device.house.unit.id");
			 String houseId = request.getParameter("device.house.id");
			 String longinName = request.getParameter("device.house.owner.longinName");
			 String officeId = request.getParameter("officeId");
			 
			 String certCode = request.getParameter("certCode");
			 String feeCode = request.getParameter("feeCode");
			 
			 
			 
			 
				System.out.println("findPaymentAfterDC>>>>>>>>>>>>>>>>>>>>>>>>>>>>222222222222222222 type>>>>>>"+type);
				System.out.println("findPaymentAfterDC>>>>>>>>>>>>>>>>>>>>>>>>>>>>222222222222222222 certCode>>>>>>"+certCode);
				System.out.println("findPaymentAfterDC>>>>>>>>>>>>>>>>>>>>>>>>>>>>222222222222222222 feeCode>>>>>>"+feeCode);
				System.out.println("findPaymentAfterDC>>>>>>>>>>>>>>>>>>>>>>>>>>>>222222222222222222 houseId>>>>>>"+houseId);
				System.out.println("findPaymentAfterDC>>>>>>>>>>>>>>>>>>>>>>>>>>>>222222222222222222 feesId>>>>>>"+feesId);
				System.out.println("findPaymentAfterDC>>>>>>>>>>>>>>>>>>>>>>>>>>>>222222222222222222 companyId>>>>>>"+companyId);
		 
		 	DeviceDetail deviceDetail = new DeviceDetail();
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
			deviceDetail.setDevice(device);
			paymentAfter.setDeviceDetail(deviceDetail);
			paymentAfter.setFeeCode(feeCode);
			paymentAfter.setCertCode(certCode);
			
			
			if (StringUtils.isNotBlank(proCompanyId)){
				proCompany.setId(proCompanyId);
			}
			
			if (StringUtils.isNotBlank(deviceType)){
				device.setType(deviceType);
			}	

			if (StringUtils.isNotBlank(feesId)){
				fees.setId(feesId);
			}
			
			if (StringUtils.isNotBlank(companyId)){
				company.setId(companyId);
			}
			if (StringUtils.isNotBlank(officeId)){
				company.setId(officeId);
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
			if (StringUtils.isNotBlank(houseId)){
				house.setId(houseId);
			}		
					
			if (StringUtils.isNotBlank(longinName)){
				owner.setLoginName(longinName);
			}	
		 
		 
	}

}
