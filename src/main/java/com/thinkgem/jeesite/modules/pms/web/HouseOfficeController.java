/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Buildings;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.Unit;
import com.thinkgem.jeesite.modules.pms.service.BuildingsService;
import com.thinkgem.jeesite.modules.pms.service.CommunityService;
import com.thinkgem.jeesite.modules.pms.service.DeviceService;
import com.thinkgem.jeesite.modules.pms.service.FeesService;
import com.thinkgem.jeesite.modules.pms.service.HouseService;
import com.thinkgem.jeesite.modules.pms.service.UnitService;
import com.thinkgem.jeesite.modules.pms.service.UserService;
import com.thinkgem.jeesite.modules.pms.utils.FeesUtils;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 收费项目Controller
 * @author vriche
 * @version 2014-04-18
 */
@Controller
@RequestMapping(value = "${adminPath}/pms/houseoffice")
public class HouseOfficeController extends BaseController {

//	@Autowired
//	private OfficeService officeService;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private BuildingsService buildingsService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private HouseService houseService;
	@Autowired
	private UserService userService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private FeesService feesService;
	
	
	


	
	
	
	
	
	
	private boolean isNewExcel =  com.thinkgem.jeesite.common.config.Global.getOfficeVersion();
	
	@ModelAttribute
	public House get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return houseService.get(id);
		}else{
			return new House();
		}
	}
	
//	@RequiresPermissions("pms:house:view")
	@RequestMapping(value = {"list", ""})
	public String list(House house, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			house.setCreateBy(user);
		}
		
		Unit unit = new Unit();
	    Buildings buildings = new Buildings();
	    Community community = new Community();
	    Office proCompany = new Office();
	    
		String proCompanyId = request.getParameter("unit.buildings.community.proCompany.id");
		String communityId = request.getParameter("unit.buildings.community.id");
		String buildingsId = request.getParameter("unit.buildings.id");
		String unitId = request.getParameter("unit.id");

		if(house.getUnit() == null) house.setUnit(unit);
		if(house.getUnit().getBuildings() == null) house.getUnit().setBuildings(buildings);
		if(house.getUnit().getBuildings().getCommunity() == null) house.getUnit().getBuildings().setCommunity(community);
		if(house.getUnit().getBuildings().getCommunity().getProCompany() == null) house.getUnit().getBuildings().getCommunity().setProCompany(proCompany);
		
		
		if (StringUtils.isNotBlank(proCompanyId)){
			house.getUnit().getBuildings().getCommunity().getProCompany().setId(proCompanyId);
		}
		
		
		if (StringUtils.isNotBlank(communityId)){
			house.getUnit().getBuildings().getCommunity().setId(communityId);
		}
		
		
		if (StringUtils.isNotBlank(buildingsId)){
			house.getUnit().getBuildings().setId(buildingsId);
		}		
		
		if (StringUtils.isNotBlank(unitId)){
			house.getUnit().setId(unitId);
		}			
		

        Page<House> page = houseService.find(new Page<House>(request, response), house); 
        
        model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
        
		 if(proCompanyId != null){
	        	model.addAttribute("communityList", communityService.findAllCommunity(house.getUnit().getBuildings().getCommunity()));
	     }
		 
		 if(communityId != null){
	        	model.addAttribute("buildingsList", buildingsService.findAllBuildings(house.getUnit().getBuildings()));
	     }       
        
		 if(unitId != null){
	        	model.addAttribute("unitList", unitService.findAllUnit(house.getUnit()));
	     }             
        
        
        model.addAttribute("page", page);
        
		return "modules/pms/houseOfficeList";
	}

//	@RequiresPermissions("pms:house:view")
	@RequestMapping(value = "form")
	public String form(House house, Model model) {
		
		List<Community> communityList =  Lists.newArrayList();
		List<Buildings> buildingsList =  Lists.newArrayList();
		List<Unit> unitList =  Lists.newArrayList();

		if(house.getUnit() == null) house.setUnit(new Unit());
		String unitId = house.getUnit().getId();

		if (StringUtils.isNotBlank(unitId)){
			communityList = communityService.findAllCommunity(house.getUnit().getBuildings().getCommunity());
			buildingsList = buildingsService.findAllBuildings(house.getUnit().getBuildings());
			unitList = unitService.findAllUnit(house.getUnit());
		}
		
		model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
		model.addAttribute("communityList", communityList);
		model.addAttribute("buildingsList", buildingsList);
		model.addAttribute("unitList", unitList);
		model.addAttribute("ownerList",  userService.findAll());
		
		model.addAttribute("feesList",  feesService.findAllList());
		
		model.addAttribute("house", house);
		
		return "modules/pms/houseOfficeForm";
	}

//	@RequiresPermissions("pms:house:edit")
	@RequestMapping(value = "save")
	public String save(House house, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, house)){
			return form(house, model);
		}
		
		
		
		
		List<Device> deviceList = Lists.newArrayList(); // 拥有设备
		List<Device> deviceListNew = Lists.newArrayList(); // 拥有设备
		
		List<Device> deviceListDB =  Lists.newArrayList();
		List<String> feesIdListDB =  Lists.newArrayList();
		
		
		List<String> temp =  Lists.newArrayList();
		List<String> tempOld =  Lists.newArrayList();
		List<String> temp2 =  Lists.newArrayList();
		List<String> feesIdListDT =Lists.newArrayList();
		
//		System.out.println("feesIdListDB>>>>>>>>>>>>>>>>>111111111111111111>>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+feesIdListDB.toString());
		feesIdListDT = Arrays.asList(house.getHouseFees().split(",")); // 当前选择
//		System.out.println("feesIdListDB>>>>>>>>>>>>>>>>>>22222222222222222222>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+feesIdListDB.toString());
		
		Unit unit = new Unit();
		// 取数据库不存在的元素
		if(house.getId() != null){
			unit = house.getUnit();
			List<Device> ls = deviceService.findAllList(house.getId(),null);
			house.setDeviceList(ls);
			deviceListDB = house.getDeviceList();
			feesIdListDB = house.getFeesIdList();// 已经存在的项目
			
//			System.out.println("unit>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+unit);
			System.out.println("deviceListDB.size()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+deviceListDB.size());
			
			System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+temp.size());
			
			temp = Collections3.subtract(feesIdListDT, feesIdListDB);
			
			tempOld = Collections3.subtract(feesIdListDB, feesIdListDT);
			
			System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..00000000000000>>>>>>>>>>"+temp.size());
		}else{
			temp = feesIdListDT;
			unit = unitService.get(house.getUnit().getId());
		}
		
//		System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>111111111>>>>>>>>"+temp.size());
		houseService.save(house);
//		System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>222222222>>>>>>>>>"+temp.size());


		//补集
		for (String fid : temp) {
			
			System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>temp>>>>>>>>>"+temp.toString());
			System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>feesIdListDB>>>>>>>>>"+feesIdListDB.toString());
			System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>fid>>>>>>>>>"+fid);
		
			
			    
			if(!"".equals(fid) && !feesIdListDB.contains(fid)){
				Device device = new Device();
				Fees fees = FeesUtils.getFees(fid);
                    
					String communityCode = unit.getBuildings().getCommunity().getCode();
					String buildingsCode = unit.getBuildings().getCode();
					String unitCode = unit.getCode();
					device.setCode(communityCode +"-"+ buildingsCode +"-"+ unitCode +"-"+ house.getCode()+"-"+fees.getCode());
					device.setFees(fees);
					String defName = fees.getName();
					device.setName(defName);
					//一般设备
					device.setType("3");
					Device dd = new Device();dd.setId("0");
					device.setParent(dd);
					device.setParentIds("0,");
					device.setHouse(house);
					
					String feesType = fees.getFeesType();
					
					//公摊
//					if("2".equals(feesType)){
//						device.setType("1");
//						Device dev= new Device();
//						dev.setFees(fees);
//						List<Device> ls = deviceService.find(dev);
//						Device d = ls.get(0);
//						if(d!= null){
//							device.setParentIds("0,"+d.getId()+",");
//							device.setParent(d);
//						}
//					}
					
//					//固话
//					if("2".equals(feesType)){
//						device.setType("2");
//						User u = house.getOwner();
//						String phone = u.getPhone();
//						if (StringUtils.isNotEmpty(phone)){
//							device.setCode(phone);
//						}
//					}
					
					deviceService.save(device);
//					deviceList.add(device);
					
			}

	
		}				

		System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>44444444444444>>>>>>>>>"+temp.toString());
		//交集
		if(feesIdListDB.size()>0){
			temp2 = Collections3.intersection(feesIdListDT, feesIdListDB);
			System.out.println("temp2>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+temp2.toString());
			for (String fid : temp2) {
				List<Device> ls = deviceService.findAllList(house.getId(),fid);
				for (Device d : ls) {
					d.setEnable("1");
					deviceService.save(d);
				}
//				deviceList.addAll(ls);
			}
			
			List<String> temp3 = Collections3.subtract(feesIdListDB, feesIdListDT);
			System.out.println("temp3>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+temp3.toString());
			for (String fid : temp3) {
				List<Device> ls = deviceService.findAllList(house.getId(),fid);
				for (Device d : ls) {
					d.setEnable("0");
					deviceService.save(d);
				}
				
			}
		}	
		
		System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>555555555>>>>>>>>>"+temp.toString());

//		System.out.print("   feesIdListDB.size() >>>>>>>>>>>>>>"+ feesIdListDB.size());
//		System.out.print("   house.getHouseFees()>>>>>>>>>>>>>>"+ house.getHouseFees());
//		System.out.print("  feesIdListDT.size() >>>>>>>>>>>>>>"+ feesIdListDT.size());
//
//
//		for (String s : feesIdListDT) {
//			System.out.print(">>>>>>>>>>>>>>>>>>>>>>00000000000000000 s>>>>>>>>>>>>>>"+ s);
//		}


		
//		deviceList = Collections3.union(deviceListDB,deviceListNew);
//		house.setDeviceList(deviceList);


		addMessage(redirectAttributes, "保存'" + house.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/pms/houseoffice/?repage";
	}
	
//	@RequiresPermissions("pms:house:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		houseService.delete(id);
		addMessage(redirectAttributes, "删除收费项目成功");
		return "redirect:"+Global.getAdminPath()+"/pms/houseoffice/?repage";
	}
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "housejson")
	public Map<String, Object> gethouseJson(String model,HttpServletRequest request, HttpServletResponse response) {
  		
  		String unitId = null;
//  		System.out.println(">>>>>>>>>>>>>>>>>>>>>getBuildingsJson>>>>>>>>>>> communityId >>>>>>>>>>>>>>>>>>>>>>>>>>"+communityId);
  		
  		
  		if("house".endsWith(model)){
  			unitId = request.getParameter("unit.id"); 
    	}
  		
  		
  		if("paymentDetail".endsWith(model)){
  			unitId = request.getParameter("device.house.unit.id");  
    	}
  		
  		if("device".endsWith(model)){
  			unitId = request.getParameter("house.unit.id");  
    	}		
  		
  		System.out.println(">>>>>>>>>>>>>>>>>>>>>gethouseJson>>>>>>>>>>> unitId >>>>>>>>>>>>>>>>>>>>>>>>>>"+unitId);
  		
  		
  		
		response.setContentType("application/json; charset=UTF-8");
		Map<String, Object> map = Maps.newHashMap();
		map.put("", "");
		
	
		if(unitId != null && StringUtils.isNotBlank(unitId)){
			House house = new House();
			house.setUnit(new Unit(unitId));
			List<House> list = houseService.findAllHouse(house);
			for(House h:list){
				map.put(h.getId(), h.getName());
			}
		}

		return map;
	}  	

}
