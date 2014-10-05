/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.web;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresUser;
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
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Buildings;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.Unit;
import com.thinkgem.jeesite.modules.pms.service.BuildingsService;
import com.thinkgem.jeesite.modules.pms.service.CommunityService;
import com.thinkgem.jeesite.modules.pms.service.HouseService;
import com.thinkgem.jeesite.modules.pms.service.UnitService;
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
@RequestMapping(value = "${adminPath}/pms/buildings")
public class BuildingsController extends BaseController {
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
	
	

	private boolean isNewExcel =  com.thinkgem.jeesite.common.config.Global.getOfficeVersion();
	
	@ModelAttribute
	public Buildings get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return buildingsService.get(id);
		}else{
			return new Buildings();
		}
	}
	
//	@RequiresPermissions("pms:buildings:view")
	@RequestMapping(value = {"list", ""})
	public String list(Buildings buildings, HttpServletRequest request, HttpServletResponse response, Model model) {
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+request.getParameter("community.id"));


		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			buildings.setCreateBy(user);
		}
		
		List<Office> proCompanyList =UserUtils.findProCompanyList();
		String proCompanyId = request.getParameter("community.proCompany.id");
		String communityId = request.getParameter("community.id");
		

		
		if(buildings.getCommunity() == null) buildings.setCommunity(new Community());
		Community community = buildings.getCommunity();
		
		if (StringUtils.isNotBlank(proCompanyId)){
			community.setProCompany(new Office(proCompanyId));
		}else{
			if(proCompanyList.size() > 0){
				community.setProCompany(proCompanyList.get(0));
			}
		}
		
		buildings.setCommunity(community);
        model.addAttribute("proCompanyList", proCompanyList);
        model.addAttribute("communityList", communityService.findAllCommunity(buildings.getCommunity()));
		

//        Community community = new Community();
//        Office proCompany = new Office();
//
//		String proCompanyId = request.getParameter("community.proCompany.id");
//		String communityId = request.getParameter("community.id");
//		
//		if(buildings.getCommunity() == null) buildings.setCommunity(community);
//		if(buildings.getCommunity().getProCompany() == null) buildings.getCommunity().setProCompany(proCompany);
//		
//		if (StringUtils.isNotBlank(proCompanyId)){
//			buildings.getCommunity().getProCompany().setId(proCompanyId);
//		}
//		if (StringUtils.isNotBlank(communityId)){
//			buildings.getCommunity().setId(communityId);
//		}		
//        
//
//        model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
        
        if(proCompanyId != null){
        	model.addAttribute("communityList", communityService.findAllCommunity(buildings.getCommunity()));
        }

		Page<Buildings> page = buildingsService.find(new Page<Buildings>(request, response), buildings); 
	        
		
        model.addAttribute("page", page);
		return "modules/pms/buildingsList";
	}

//	@RequiresPermissions("pms:buildings:view")
	@RequestMapping(value = "form")
	public String form(Buildings buildings, Model model) {
		
		List<Community> communityList =  Lists.newArrayList();
//		List<Buildings> buildingsList =  Lists.newArrayList();
		
		if(buildings.getId() != null){
			communityList = communityService.findAllCommunity(buildings.getCommunity());
//			buildingsList = buildingsService.findAllBuildings(buildings);
		}
		
		model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
		model.addAttribute("communityList", communityList);
		model.addAttribute("buildings", buildings);
		return "modules/pms/buildingsForm";
	}

//	@RequiresPermissions("pms:buildings:edit")
	@RequestMapping(value = "save")
	public String save(Buildings buildings, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, buildings)){
			return form(buildings, model);
		}
		buildingsService.save(buildings);
		addMessage(redirectAttributes, "保存收费项目'" + buildings.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/pms/buildings/?repage";
	}
	
//	@RequiresPermissions("pms:buildings:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		buildingsService.delete(id);
		addMessage(redirectAttributes, "删除收费项目成功");
		return "redirect:"+Global.getAdminPath()+"/pms/buildings/?repage";
	}
	
	
	
//	@RequiresPermissions("pms:buildings:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Buildings buildings, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileSuffix = isNewExcel?".xlsx":".xls";
            String fileName = "楼宇数据"+DateUtils.getDate("yyyyMMddHHmmss")+ fileSuffix; 
    		List ls = buildingsService.findAllBuildings(new Buildings());
    		new ExportExcel("楼宇数据", Buildings.class).setDataList(ls).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出楼宇失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pms/buildings/?repage";
    }
    
//	@RequiresPermissions("pms:buildings:view")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/pms/buildings/?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Buildings> list = ei.getDataList(Buildings.class);
			int code = 1;
		
			
			for (Buildings buildings : list){
				
				String communityName = buildings.getCommunity().getName();
				
				try{
					
					if ("true".equals(checkName("", communityName,buildings.getName()))){
//						System.out.println(">>>>>>>>>>>>>>>>>>>>>333333333333333333");
						buildings.setUpdateDate(new Date());
						
						if (buildings.getBuildArea() == null) buildings.setBuildArea(new BigDecimal(0));
						if (buildings.getUseArea() == null) buildings.setUseArea(new BigDecimal(0));
						if (buildings.getFloorCount() == null) buildings.setFloorCount(new Integer(0));
							
						BeanValidators.validateWithException(validator, buildings);
						buildingsService.save(buildings);
						successNum++;
					}else{
						failureMsg.append("<br/>楼宇名 "+communityName+"_"+buildings.getName()+" 已存在; ");
						failureNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>楼宇名 "+communityName+"_" +buildings.getName()+" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>楼宇名 "+communityName+"_"+buildings.getName()+" 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条小区，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条小区"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入小区失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pms/buildings/?repage";
    }
	    
    public String checkName(String oldLoginName, String communityName, String name) {
    	Buildings buildings = new Buildings();
    	Community community = new Community();
    	community.setName(communityName);
    	buildings.setCommunity(community);
    	buildings.setName(name);
//    	System.out.println(">>>>>>>>>>>>>>>>>>>>>0000000000");
//    	System.out.println(">>>>>>>>>>>>>>>>>>>>>5555555>>>>>>>>>>>"+buildingsService.findAllBuildings(buildings).size());
//    	System.out.println(">>>>>>>>>>>>>>>>>>>>>1111111111111");
    	
    	boolean isExitDB =  buildingsService.findAllBuildings(buildings).size() ==0;
    			
		if (name !=null && name.equals(oldLoginName)) {
			return "true";
		} else if (name !=null && isExitDB) {
			return "true";
		}
		return "false";
	}
    
    
 		@ResponseBody
		@RequestMapping(value = "buildingsjson")
		public Map<String, Object> getBuildingsJson(String model,HttpServletRequest request, HttpServletResponse response) {
	  		
	  		String communityId = null;
//	  		System.out.println(">>>>>>>>>>>>>>>>>>>>>getBuildingsJson>>>>>>>>>>> communityId >>>>>>>>>>>>>>>>>>>>>>>>>>"+communityId);
	  		
	  		if("unit".endsWith(model)){
	  			communityId = request.getParameter("buildings.community.id"); 
	    	}
	  		
	  		if("house".endsWith(model)){
	  			communityId = request.getParameter("unit.buildings.community.id"); 
	    	}
	  		
	  		if("paymentDetail".endsWith(model)){
	  			communityId = request.getParameter("device.house.unit.buildings.community.id"); 
	    	}
	  		
	  		if("device".endsWith(model)){
	  			communityId = request.getParameter("house.unit.buildings.community.id"); 
	    	}  		
	  		
	  		
			response.setContentType("application/json; charset=UTF-8");
			Map<String, Object> map = Maps.newHashMap();
			map.put("", "");
			
		
			if(communityId != null && StringUtils.isNotBlank(communityId)){
				Buildings buildings = new Buildings();
				Community community = new Community();
				community.setId(communityId);
				buildings.setCommunity(community);
				List<Buildings> list = buildingsService.findAllBuildings(buildings);
				
			
				for (int i=0; i<list.size(); i++){
					Buildings e = list.get(i);
					map.put(e.getId(), e.getName());
				}
			}

			return map;
		}  
   
 		@RequiresUser
 		@ResponseBody
 		@RequestMapping(value = "treeData")
 		public List<Map<String, Object>> treeData(@RequestParam(required=false) String proCompanyId,@RequestParam(required=false) Long Level, HttpServletResponse response) {
 			
 			int nodeLevel = Level.intValue();
 			
 			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>Level                    >>>"+Level);
 			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>proCompanyId                    >>>"+proCompanyId);
 			
 			
 			
 			
// 			int level = nodesLevel.intValue();

 			response.setContentType("application/json; charset=UTF-8");
 			
 			List<Map<String, Object>> mapList = Lists.newArrayList();
 			
 		
 			if(proCompanyId != null && StringUtils.isNotEmpty(proCompanyId) && !"undefined".equals(proCompanyId)){
 				return   getCommunity(nodeLevel,proCompanyId);
 			}else{
 	 			List<Office> list = UserUtils.findProCompanyList();
 	 			for (int i=0; i<list.size(); i++){
 	 				Office e = list.get(i);
 					Map<String, Object> map = Maps.newHashMap();
 					map.put("id", "ProCompany"+e.getId());
 					map.put("name", e.getName());
 					
 					if(nodeLevel > 0){
 						List<Map<String, Object>> chList =  getCommunity(nodeLevel,e.getId());
 						if(chList.size() >0 ){
 							map.put("isParent", true);
 							map.put("children",chList);
 						}	
 					}
 					mapList.add(map);
 	 			
 	 			}	
 	 			return mapList;				
 			}

 		}
 		
 		public  List<Map<String, Object>>getCommunity(int nodeLevel,String proCompanyId){
 			List<Map<String, Object>> mapList = Lists.newArrayList();

 			Community community = new Community();
 			Office proCompany = new Office();
 			proCompany.setId(proCompanyId);
 			community.setProCompany(proCompany);
 			List<Community> communityList =communityService.findAllCommunity(community);
 		
 			for(Community e : communityList){
 				Map<String, Object> map = Maps.newHashMap();
 				map.put("id", "Community"+e.getId());
				map.put("name", e.getName());

				if(nodeLevel > 1){
					List<Map<String, Object>> chList = getBuildings(nodeLevel,e.getId());
					if(chList.size() >0 ){
						map.put("isParent", true);
//						map.put("disabled", true);
						map.put("children",chList);
					}	
				}
	
				
				mapList.add(map);
 			}
 			return mapList;
 		}
 		
 		
 		public  List<Map<String, Object>>getBuildings(int nodeLevel,String communityId){
 			List<Map<String, Object>> mapList = Lists.newArrayList();
 			Community community = new Community();
 			Buildings buildings = new Buildings();
 			community.setId(communityId);
 			buildings.setCommunity(community);
 			List<Buildings> buildingsList = buildingsService.findAllBuildings(buildings);
 		
 			for(Buildings e : buildingsList){
 				Map<String, Object> map = Maps.newHashMap();
 				map.put("id", "Buildings"+e.getId());
				map.put("name", e.getName());
				
				if(nodeLevel > 2){
					List<Map<String, Object>> chList =getUnit(nodeLevel,e.getId());
					if(chList.size() >0 ){
						map.put("isParent", true);
//						map.put("disabled", true);
						map.put("children",chList);
					}	
				}			
				mapList.add(map);
 			}
 			return mapList;
 		}
 		
 		public  List<Map<String, Object>>getUnit(int nodeLevel,String buildingsId){
 			List<Map<String, Object>> mapList = Lists.newArrayList();
 			
 			Buildings buildings = new Buildings();
 			Unit unit = new Unit();
 			buildings.setId(buildingsId);
 			unit.setBuildings(buildings);
 			List<Unit> unitList = unitService.findAllUnit(unit);
 		
 			for(Unit e : unitList){
 				Map<String, Object> map = Maps.newHashMap();
 				map.put("id", "Unit"+e.getId());
				map.put("name", e.getName());
				
				if(nodeLevel > 3){
					List<Map<String, Object>> chList = getHouse(nodeLevel,e.getId());
					if(chList.size() >0 ){
						map.put("isParent", true);
						map.put("children",chList);
					}	
				}					

				mapList.add(map);
 			}
 			return mapList;
 		}
 		
 		public  List<Map<String, Object>>getHouse(int nodeLevel,String unitId){
 			List<Map<String, Object>> mapList = Lists.newArrayList();
 			
 			House house = new House();
 			Unit unit = new Unit();
 			unit.setId(unitId);
 			house.setUnit(unit);
 			List<House> houseList = houseService.findAllHouse(house);
 			
 			for(House e : houseList){
 				Map<String, Object> map = Maps.newHashMap();
 				User owner = e.getOwner();
 				map.put("id", "House"+e.getId());
 				String name = e.getName();
 				if(owner != null) name = name + "("+owner.getName()+")";
				map.put("name", name);
				mapList.add(map);
 			}
 			return mapList;
 		}
}
