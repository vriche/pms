/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.web;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Buildings;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.DeviceDetail;
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
import com.thinkgem.jeesite.modules.pms.utils.DeviceUtils;
import com.thinkgem.jeesite.modules.pms.utils.FeesUtils;
import com.thinkgem.jeesite.modules.pms.utils.HouseUtils;
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
@RequestMapping(value = "${adminPath}/pms/house")
public class HouseController extends BaseController {

	@Autowired
	private OfficeService officeService;
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
		HouseUtils.getObjFromReq(house,  request,response, model,1,"house");
 		Page<House> page = new Page<House>(request, response);
// 		String communityId = request.getParameter("unit.buildings.community.id");
// 		if(StringUtils.isNotBlank(communityId)){
 			houseService.findPage(page,house,null,0);
// 		}
		model.addAttribute("page", page);
        
		return "modules/pms/houseList";
	}

//	@RequiresPermissions("pms:house:view")
	@RequestMapping(value = "form")
	public String form(House house, Model model) {
		
		List<Community> communityList =  Lists.newArrayList();
		List<Buildings> buildingsList =  Lists.newArrayList();
		List<Unit> unitList =  Lists.newArrayList();

		if(house.getUnit() == null) house.setUnit(new Unit());
		String unitId = house.getUnit().getId();
		
		String proCompanyId = house.getUnit().getBuildings().getCommunity().getProCompany().getId();
		
//		System.out.println("proCompanyId>>>>>>>>>>>>>>>>>111111111111111111>>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+proCompanyId);
//		System.out.println("unitId>>>>>>>>>>>>>>>>>111111111111111111>>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+unitId);
		

		if (StringUtils.isNotBlank(unitId)){
			communityList = communityService.findAllCommunity(house.getUnit().getBuildings().getCommunity());
			buildingsList = buildingsService.findAllBuildings(house.getUnit().getBuildings());
			unitList = unitService.findAllUnit(house.getUnit());
		}else{
			communityList = communityService.findAllCommunity(house.getUnit().getBuildings().getCommunity());
		}
		
		model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
		model.addAttribute("communityList", communityList);
		model.addAttribute("buildingsList", buildingsList);
		model.addAttribute("unitList", unitList);
//		model.addAttribute("ownerList",  userService.findAll());
		model.addAttribute("feesList",  FeesUtils.getALLFees(proCompanyId));
		model.addAttribute("house", house);
		
		return "modules/pms/houseForm";
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
		List<Device> deviceListDB2 =  Lists.newArrayList();
		List<String> feesIdListDB2 =  Lists.newArrayList();
		
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
			List<Device> ls = deviceService.findAllList(house.getId(),null,"1");
			List<Device> ls2 = deviceService.findAllList(house.getId(),null,"0");
			house.setDeviceList(ls);
			deviceListDB = house.getDeviceList();
			feesIdListDB = house.getFeesIdList();// 已经存在的项目
			
			House house2 = new House();
			house2.setDeviceList(ls2);
			deviceListDB2 = house2.getDeviceList();
			feesIdListDB2 = house2.getFeesIdList();// 已经存在的项目
//			System.out.println("unit>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+unit);
			System.out.println("deviceListDB.size()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+deviceListDB.size());
			
			System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+temp.size());
			
			temp = Collections3.subtract(feesIdListDT, feesIdListDB);
			
			tempOld = Collections3.subtract(feesIdListDB, feesIdListDT);
			
//			System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..00000000000000>>>>>>>>>>"+temp.size());
		}else{
			temp = feesIdListDT;
			unit = unitService.get(house.getUnit().getId());
		}
		
//		System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>111111111>>>>>>>>"+temp.size());
		houseService.save(house);
//		System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>222222222>>>>>>>>>"+temp.size());


		//补集
		for (String fid : temp) {
			
//			System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>temp>>>>>>>>>"+temp.toString());
//			System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>feesIdListDB>>>>>>>>>"+feesIdListDB.toString());
//			System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>feesIdListDB2>>>>>>>>>"+feesIdListDB2.toString());
//			System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>fid>>>>>>>>>"+fid);
		
			
			    
			if(!"".equals(fid) && !feesIdListDB.contains(fid)){

				if(feesIdListDB2.contains(fid)){
					for (Device d : deviceListDB2) {
						String f_id = d.getFees().getId();
						if(f_id.equals(fid)){
							d.setEnable("1");
							deviceService.save(d);					
						}
					}
				}else{
					Device device = new Device();
					Fees fees = FeesUtils.getFees(fid);
						device.setFees(fees);
						device.setName("");
						//一般设备
						User user = house.getOwner();
//						String userType = user.getUserType();
						String deviceType = "3"; //2 单位  3、个人
						String userType = user.getUserType(); //2 业主  3、法人
						String phone = user.getPhone();
						if("3".equals(userType)){deviceType = "2"; }
						device.setType(deviceType);
						device.setParent(new Device("0"));
						device.setParentIds("0,");
						device.setHouse(house);
						
						DeviceUtils.getCode(device);
						
//						String feesType = fees.getFeesType();
//						String feesModel = fees.getFeesMode();
						String feesCode = fees.getCode();
						
		                   if("A05".equals(feesCode)){
		                	   if(StringUtils.isNotBlank(phone)){
		                		   deviceService.save(device);
		                	   }
		                   }else{
		                	   deviceService.save(device);
		                   }
		                   
						
						//公摊
//						if("2".equals(feesType)){
//							device.setType("1");
//							Device dev= new Device();
//							dev.setFees(fees);
//							List<Device> ls = deviceService.find(dev);
//							Device d = ls.get(0);
//							if(d!= null){
//								device.setParentIds("0,"+d.getId()+",");
//								device.setParent(d);
//							}
//						}
						
//						//固话
//						if("2".equals(feesType)){
//							device.setType("2");
//							User u = house.getOwner();
//							String phone = u.getPhone();
//							if (StringUtils.isNotEmpty(phone)){
//								device.setCode(phone);
//							}
//						}
						
						
						
						
//						 if("5".equals(feesModel) && StringUtils.isBlank(phone)){
//							 if(StringUtils.isNotBlank(phone)){
//								 deviceService.save(device);
//							 }
//						 }else{
//							 deviceService.save(device);
//						 }
						
//						deviceList.add(device);
				}
				
					
			}

	
		}				

		System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>44444444444444>>>>>>>>>"+temp.toString());
		//交集
		if(feesIdListDB.size()>0){
			
			//交集  
			temp2 = Collections3.intersection(feesIdListDT, feesIdListDB);
			System.out.println("temp2>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+temp2.toString());
			for (String fid : temp2) {
				List<Device> ls = deviceService.findAllList(house.getId(),fid,"0");
				for (Device d : ls) {
					d.setEnable("1");
					deviceService.save(d);
				}
//				deviceList.addAll(ls);
			}
			//差集 
			List<String> temp3 = Collections3.subtract(feesIdListDB, feesIdListDT);
			System.out.println("temp3>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+temp3.toString());
			for (String fid : temp3) {
				List<Device> ls = deviceService.findAllList(house.getId(),fid,"1");
				for (Device d : ls) {
					d.setEnable("0");
					deviceService.save(d);
				}
				
			}
		}	
		
		System.out.println("temp>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>555555555>>>>>>>>>"+temp.toString());

	
		addMessage(redirectAttributes, "保存'" + house.getName() + "'成功");
		

		String proCompanyId = house.getUnit().getBuildings().getCommunity().getProCompany().getId();
		String communityId = house.getUnit().getBuildings().getCommunity().getId();
		String buildId = house.getUnit().getBuildings().getId();
		String unitId = house.getUnit().getId();
//		System.out.print(">>>>>>>>>>>>>>>>>>>>>>00000000000000000 s>>>>>>>>>>>>>>"+proCompanyId);
//		 model.addAttribute("proCompanyId", proCompanyId);
		
		 model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
		 

	        
		 if(proCompanyId != null){
	        	model.addAttribute("communityList", communityService.findAllCommunity(house.getUnit().getBuildings().getCommunity()));
	     }
		 
		 if(communityId != null){
	        	model.addAttribute("buildingsList", buildingsService.findAllBuildings(house.getUnit().getBuildings()));
	     }
        
		 if(communityId != null && StringUtils.isNotBlank(communityId)){
	        	 model.addAttribute("unitList", unitService.findAllUnit(house.getUnit()));
	     }             
        
//		 System.out.println("save HOUSE>>>>>>>>>>>>>>>>111111111111111111>    2222222         proCompanyId>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+proCompanyId);
//		 System.out.println("save HOUSE>>>>>>>>>>>>>>>>111111111111111111>    2222222         unitId>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+unitId);
//		 System.out.println("save HOUSE>>>>>>>>>>>>>>>>111111111111111111>    2222222         buildId>>>>>>>>>>>>>>>>>>>>>..>>>>>>>>>>"+buildId);
		 redirectAttributes.addAttribute("unit.buildings.community.proCompany.id", proCompanyId);
		 redirectAttributes.addAttribute("unit.buildings.community.id", communityId);
		 redirectAttributes.addAttribute("unit.buildings.id", buildId);
		 redirectAttributes.addAttribute("unit.id", unitId);
		
		
		return "redirect:"+Global.getAdminPath()+"/pms/house/?repage";
	}
	
//	@RequiresPermissions("pms:house:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		
		House house = houseService.get(id);
		
		Device dev = new Device();
		dev.setHouseIds("house"+id+",");
		List<Device> devList = deviceService.find(dev);
		for(Device dv:devList){
//			dv.setHouse(new House("0"));
			dv.setEnable("0");
			deviceService.save(dv);
		}
	
		
		houseService.delete(id);
		
		redirectAttributes.addAttribute("unit.buildings.community.id", house.getUnit().getBuildings().getCommunity().getId());
		redirectAttributes.addAttribute("unit.buildings.id", house.getUnit().getBuildings().getId());
		redirectAttributes.addAttribute("unit.id", house.getUnit().getId());
//		redirectAttributes.addAttribute("id", house.getId());
		
		
		addMessage(redirectAttributes, "删除收费项目成功");
		return "redirect:"+Global.getAdminPath()+"/pms/house/?repage";
	}
	
	
	@RequestMapping(value = "deletebyuser")
	public String deletebyuser(String id, String uid,RedirectAttributes redirectAttributes) {
		
		House house = houseService.get(id);
		
		houseService.delete(id);
		
		redirectAttributes.addAttribute("unit.buildings.community.id", house.getUnit().getBuildings().getCommunity().getId());
		redirectAttributes.addAttribute("unit.buildings.id", house.getUnit().getBuildings().getId());
		redirectAttributes.addAttribute("unit.id", house.getUnit().getId());
//		redirectAttributes.addAttribute("id", house.getId());
		
		
		addMessage(redirectAttributes, "删除收费项目成功");
		return "redirect:"+Global.getAdminPath()+"/pms/house/?repage";
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "housejson")
	public Map<String, Object> gethouseJson(String model,HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		Map<String, Object> map = Maps.newHashMap();
		map.put("", "");
		
		House house = new House();
		HouseUtils.getObjFromReq(house, request, response, null, 0, "device");
		Page<House> page = new Page<House>(request, response, -1);
		houseService.findPage(page, house, map, 1);
		return map;
	}  	
	
	
	 @RequestMapping(value = "import", method=RequestMethod.POST)
//	 public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
	 public String importFile(@RequestParam("cmpid") String cmpid, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {	 
		 
		 
			if(Global.isDemoMode()){
				addMessage(redirectAttributes, "演示模式，不允许操作！");
				return "redirect:"+Global.getAdminPath()+"/pms/house/?repage";
			}
			
//			 System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>222222222222222 cmpid >>>>>>>>>"+ cmpid);
//			 System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>222222222222222 file >>>>>>>>>"+ file.getSize());
			 
			try {
				int successNum = 0;
				int failureNum = 0;
				StringBuilder failureMsg = new StringBuilder();
				ImportExcel ei = new ImportExcel(file, 1, 1);
				
				List<House> list = ei.getDataList(House.class);
				List<User> allUserList = userService.findAll();
				Map userMap = Collections3.extractToMap(allUserList,"loginName");
				
//				 System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>222222222222222  list.size() >>>>>>>>>"+ list.size());
				  
				List<House> listTemp =  Lists.newArrayList();
				
				int i = 0;
				
				   Map<String,Office> mpProCompany = new HashMap<String,Office>();
				   Map<String,Community> mpCommunity = new HashMap<String,Community>();
				   Map<String,Buildings> mpBuildings = new HashMap<String,Buildings>();
				   Map<String,Unit> mpUnit = new HashMap<String,Unit>();
				   Map<String,House> mpHouse = new HashMap<String,House>();
				   
				   String proCompanyId = cmpid;
				   
				   Office office = officeService.get(proCompanyId);
				   String proCompanyName =  office.getName();
				   
//				   System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>222222222222222>>>>>>>>>"+proCompanyName);
				   
				   List<Fees> feesList = FeesUtils.getALLFees(proCompanyId);
				   
//				   System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>33333333333>>>>>>>>>"+proCompanyName);
				   
				   Map mp_house = new HashMap();   

				for (House house : list){
//					 if(i<2){
					 i++;
					try{

						  
//						   String loginName = house.getLoginName();
							String loginName = StringUtils.trim(house.getLoginName());
							BigDecimal bddd = new BigDecimal(loginName);
							loginName = bddd.toPlainString();
//						   System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>loginName>>>>>>>>>"+ loginName);
						   String communityName =  house.getCommunityName();
//						   System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>communityName>>>>>>>>>"+communityName);
						   String buildingsName = house.getBuildingsName();
//						   System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>buildingsName>>>>>>>>>"+buildingsName);
						   String unitName = house.getUnitName();
//						   System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>unitName>>>>>>>>>"+unitName);
						   String houseName = house.getName();
//						   System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>houseName>>>>>>>>>"+houseName);
//						   loginName =StringUtils.trim(loginName);
						   Object obj = userMap.get(loginName);
						   

						   if(obj instanceof User){
							   User owner = (User)obj;
							   house.setOwner(owner);
						   }else{
							   house.setOwner(UserUtils.getUser());
						   }
//						   System.out.println("importFile>>>>>>>>>>>>>>>888888>999999>>>>>>>>>>>>>>>>11111111111>>>>>>>");
						   House h = houseService.saveForExcel(house,office,mpCommunity,mpBuildings,mpUnit,mpHouse,i);
//						   System.out.println("importFile>>>>>>>>>>>>>>>888888>999999>>>>>>>>>>>>>>>>2222222222>>>>>>>>");
						   listTemp.add(h);
						   
//						   System.out.println("importFile> h >>>>>>>>>>>>>>888888>999999>>>>>>>>>>>>>>>>333333333>>>>>>>>"+h);
						   
						if(h != null){
							User user = h.getOwner();
							String userType = user.getUserType(); //2 业主  3、法人
							String deviceType = "3"; //2 单位  3、个人

							   for(Fees fees:feesList){
//								   String feesModel = fees.getFeesMode();
								   String feesCode = fees.getCode();
								   
								   Device dev = new Device();
								   dev.setFees(fees);
								   dev.setHouseIds("house"+h.getId()+",");
								   List<Device> devList = deviceService.find(dev);
								   
//								   System.out.println("importFile> devList.size() "+ fees.getName()+">>>>>>>>>>>>>>888888>999999>>>>>>>>>>>>>>>>333333333>>>>>>>>"+devList.size());
								   
								   if(devList.size() == 0){
									   Device device = new Device();
									   device.setHouse(h);
									   device.setFees(fees);
							
									   //用户是法人 设备类型是2，表示单位设备
										if("3".equals(userType)){deviceType = "2"; }
										device.setType(deviceType);
										device.setParent(new Device("0"));
										device.setParentIds("0,");
									   
					                   String phone = h.getOwner().getPhone();
					                   
					                   if("A05".equals(feesCode)){
					                	   if(StringUtils.isNotBlank(phone)){
					                		   deviceService.save(device);
					                	   }
					                   }else{
					                	   deviceService.save(device);
					                   }
					                   
					                   
//									   if("A05".equals(feesCode) && StringUtils.isBlank(phone)){
//											  
//									   }else{
//											 deviceService.save(device);
//									   } 
								   }


							   }		
						
							successNum++;
//							System.out.println("importFile>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>h.getFullName>>>>>>>>>"+h.getFullName()+"____"+h.getOwner().getName());
						}else{
							failureMsg.append("<br/>房产名1 "+house.getName()+" 已存在; ");
							failureNum++;
						}
						
						
					}catch(ConstraintViolationException ex){
						failureMsg.append("<br/>房产名2 "+house.getName()+" 导入失败：");
						List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
						for (String message : messageList){
							failureMsg.append(message+"; ");
							failureNum++;
						}
					}catch (Exception ex) {
						failureMsg.append("<br/>房产名3 "+ house.getName() +" 导入失败："+ex.getMessage());
					}
					
//					 }
				}
				
				//导入设备
//				deviceService.saveDeviceByHouseList(proCompanyId,listTemp);
				
				if (failureNum>0){
					failureMsg.insert(0, "，失败 "+failureNum+" 条房产，导入信息如下：");
				}
				addMessage(redirectAttributes, "已成功导入 "+successNum+" 条房产"+failureMsg);
				
			} catch (Exception e) {
				addMessage(redirectAttributes, "导入房产失败！失败信息："+e.getMessage());
			}
			
			
			
			return "redirect:"+Global.getAdminPath()+"/pms/house/?repage";
	    }
	 

	    @RequestMapping(value = "export", method=RequestMethod.POST)
	    public String exportFile(House house, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
			try {
				String fileSuffix = isNewExcel?".xlsx":".xls";
	            String fileName = "房屋数据"+DateUtils.getDate("yyyyMMddHHmmss")+ fileSuffix; 
	        
	    		getHouseParam(house,request);
	    		Page<House> page = new Page<House>(request, response,-1);
	    		houseService.findPage(page,house,null,0);

	    		List<House> ls = page.getList();
	    		 
	    		for (House h : ls){
	    			h.setLoginName(h.getOwner().getLoginName());
	    			h.setCommunityName(h.getUnit().getBuildings().getCommunity().getName());
	    			h.setBuildingsName(h.getUnit().getBuildings().getName());
	    			h.setUnitName(h.getUnit().getName());
	    			h.setNumFloorStr(h.getNumFloor());
	    			h.setBuildAreaStr(h.getBuildArea()+"");
	    			h.setUseAreaStr(h.getUseArea()+"");
	    		}
	    		
	    		
	    		
	    		new ExportExcel("房屋数据", House.class).setDataList(ls).write(response, fileName).dispose();
	    		return null;
			} catch (Exception e) {
				addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
			}
			return "redirect:"+Global.getAdminPath()+"/pms/house/?repage";
	    }
	    
	    
	    private void getHouseParam(House house, HttpServletRequest request){
	    	String proCompanyId = request.getParameter("unit.buildings.community.proCompany.id");
    		String communityId = request.getParameter("unit.buildings.community.id");
    		String buildingsId = request.getParameter("unit.buildings.id");
    		String unitId = request.getParameter("unit.id");

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

	    }

		
	    @RequestMapping(value = "import/template")
	    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
			try {
				String fileSuffix = isNewExcel?".xlsx":".xls";
	            String fileName = "房产数据导入模板"+fileSuffix;
//	    		List<House> list = Lists.newArrayList(); list.add(UserUtils.getUser());
//	    		new ExportExcel("房产数据",House.class, 2).setDataList(list).write(response, fileName).dispose();
	    		return null;
			} catch (Exception e) {
				addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
			}
			return "redirect:"+Global.getAdminPath()+"/pms/house/?repage";
	    }
	    
	    
	    

}
