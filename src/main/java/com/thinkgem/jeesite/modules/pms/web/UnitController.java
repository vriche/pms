/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Buildings;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.Unit;
import com.thinkgem.jeesite.modules.pms.service.BuildingsService;
import com.thinkgem.jeesite.modules.pms.service.CommunityService;
import com.thinkgem.jeesite.modules.pms.service.UnitService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 单元信息Controller
 * @author vriche
 * @version 2014-04-18
 */
@Controller
@RequestMapping(value = "${adminPath}/pms/unit")
public class UnitController extends BaseController {
//	@Autowired
//	private OfficeService officeService;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private BuildingsService buildingsService;
	@Autowired
	private UnitService unitService;
	
	@ModelAttribute
	public Unit get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return unitService.get(id);
		}else{
			return new Unit();
		}
	}
	
//	@RequiresPermissions("pms:unit:view")
	@RequestMapping(value = {"list", ""})
	public String list(Unit unit, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			unit.setCreateBy(user);
		}
		
		List<Office> proCompanyList =UserUtils.findProCompanyList(); 		
	    
	    Buildings buildings = new Buildings();
	    Community community = new Community();
	    Office proCompany = new Office();
	    
		String proCompanyId = request.getParameter("buildings.community.proCompany.id");
		String communityId = request.getParameter("buildings.community.id");
		String buildingsId = request.getParameter("buildings.id");
		

		if(unit.getBuildings() == null) unit.setBuildings(buildings);
		if(unit.getBuildings().getCommunity() == null) unit.getBuildings().setCommunity(community);
		if(unit.getBuildings().getCommunity().getProCompany() == null) unit.getBuildings().getCommunity().setProCompany(proCompany);
		
		
		if (StringUtils.isNotBlank(proCompanyId)){
			unit.getBuildings().getCommunity().getProCompany().setId(proCompanyId);
//			unit.setProCompanyId(proCompanyId);
		}else{
			if(proCompanyList.size() > 0){
				community.setProCompany(proCompanyList.get(0));
				proCompanyId = proCompanyList.get(0).getId();
			}
		}
		
		
		if (StringUtils.isNotBlank(communityId)){
			unit.getBuildings().getCommunity().setId(communityId);
//			unit.setCommunityId(communityId);
		}
		
		
		if (StringUtils.isNotBlank(buildingsId)){
			unit.getBuildings().setId(buildingsId);
//			unit.setBuildingsId(buildingsId);
		}		
		
		 Page<Unit> page = new Page<Unit>(request, response);
		 
		 model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
		 
		 if(proCompanyId != null){
	        	model.addAttribute("communityList", communityService.findAllCommunity(unit.getBuildings().getCommunity()));
	     }
		 
		 if(communityId != null){
	        	model.addAttribute("buildingsList", buildingsService.findAllBuildings(unit.getBuildings()));
	        	page = unitService.find(new Page<Unit>(request, response), unit); 
	     }

//        Page<Unit> page = unitService.find(new Page<Unit>(request, response), unit); 
       
        model.addAttribute("page", page);
		return "modules/pms/unitList";
	}

//	@RequiresPermissions("pms:unit:view")
	@RequestMapping(value = "form")
	public String form(Unit unit, Model model) {
		
		List<Community> communityList =  Lists.newArrayList();
		List<Buildings> buildingsList =  Lists.newArrayList();

		if(unit.getBuildings() == null) unit.setBuildings(new Buildings());
		String buildingsId = unit.getBuildings().getId();

		if (StringUtils.isNotBlank(buildingsId)){
			communityList = communityService.findAllCommunity(unit.getBuildings().getCommunity());
			buildingsList = buildingsService.findAllBuildings(unit.getBuildings());
		}
		
		model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
		model.addAttribute("communityList", communityList);
		model.addAttribute("buildingsList", buildingsList);
		model.addAttribute("unit", unit);
		
		return "modules/pms/unitForm";
	}

//	@RequiresPermissions("pms:unit:edit")
	@RequestMapping(value = "save")
	public String save(Unit unit, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, unit)){
			return form(unit, model);
		}
		unitService.save(unit);
		addMessage(redirectAttributes, "保存单元信息'" + unit.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/pms/unit/?repage";
	}
	
//	@RequiresPermissions("pms:unit:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		unitService.delete(id);
		addMessage(redirectAttributes, "删除单元信息成功");
		return "redirect:"+Global.getAdminPath()+"/pms/unit/?repage";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "unitjson")
	public Map<String, Object> getBuildingsJson(String model,HttpServletRequest request, HttpServletResponse response) {
  		
  		String buildingsId = null;
//  		System.out.println(">>>>>>>>>>>>>>>>>>>>>getBuildingsJson>>>>>>>>>>> communityId >>>>>>>>>>>>>>>>>>>>>>>>>>"+communityId);
  		
  		
  		if("house".endsWith(model)){
  			buildingsId = request.getParameter("unit.buildings.id"); 
    	}
  		
  		
  		if("paymentDetail".endsWith(model)){
  			buildingsId = request.getParameter("device.house.unit.buildings.id");  
    	}
  		
  		if("device".endsWith(model)){
  			buildingsId = request.getParameter("house.unit.buildings.id");  
    	}		
  		
  		
  		
		response.setContentType("application/json; charset=UTF-8");
		Map<String, Object> map = Maps.newHashMap();
		map.put("", "");
		
	
		if(buildingsId != null && StringUtils.isNotBlank(buildingsId)){
			Buildings buildings = new Buildings();
			buildings.setId(buildingsId);
			Unit unit = new Unit();
			unit.setBuildings(buildings);
			List<Unit> list = unitService.findAllUnit(unit);

			for (int i=0; i<list.size(); i++){
				Unit e = list.get(i);
				map.put(e.getId(), e.getName());
			}
		}

		return map;
	}  	
	 

}
