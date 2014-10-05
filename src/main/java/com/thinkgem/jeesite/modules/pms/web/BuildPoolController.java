/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.service.DeviceService;
import com.thinkgem.jeesite.modules.pms.service.FeesService;
import com.thinkgem.jeesite.modules.pms.utils.DeviceUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 单元信息Controller
 * @author vriche
 * @version 2014-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/pms/buildpool")
public class BuildPoolController extends BaseController {
//	@Autowired
//	private OfficeService officeService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private FeesService feesService;
	
	
	
	@ModelAttribute
	public Device get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return deviceService.get(id);
		}else{
			return new Device();
		}
	}
	
//	@RequiresPermissions("pms:device:view")
	@RequestMapping(value = {"list", ""})
	public String list(Device device, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			device.setCreateBy(user);
		}
		
//		if(device.getType() == null){
//			device.setType("1");
//		}
		
        model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
		Device d = new Device();
		d.setType("1");
		model.addAttribute("parentList", deviceService.find(d));
		
		device.setPool(null);
		device.setModel("1");
		
		Page<Device> page = null;
		if(device.getParent() != null){
			if (StringUtils.isNotEmpty(device.getParent().getId())){
				page = deviceService.find(new Page<Device>(request, response), device);
				//计算公摊
				DeviceUtils.resetPoolList(device.getParent().getId(),page.getList());
			}

		}


        model.addAttribute("page", page);
		return "modules/pms/buildPoolList";
	}
	

	

//	@RequiresPermissions("pms:device:view")
	@RequestMapping(value = "form")
	public String form(Device device, Model model) {

//		Device parent = new Device();
//		parent.setId("0");
//		parent.setParent(parent);
		Device d = new Device();
		d.setType("1");
		model.addAttribute("parentList", deviceService.find(d));
		
		model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
		model.addAttribute("feesList", feesService.findAllList());
		model.addAttribute("device", device);
		return "modules/pms/deviceForm";
	}

//	@RequiresPermissions("pms:device:edit")
	@RequestMapping(value = "save")
	public String save(Device device, Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) {
//		if (!beanValidator(model, device)){
//			return form(device, model);
//		}
		
		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getHouseIds >>>>>>>>>>>>>>>>>>>>>>>>"+device.getHouseIds());
		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getParent().getId() >>>>>>>>>>>>>>>>>>>>>>>>"+device.getParent().getId());
//		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getHouse() >>>>>>>>>>>>>>>>>>>>>>>>"+device.getHouse().getId());
		
		String parentId = device.getParent().getId();
		Device parent = deviceService.get(parentId);
//		String pfid = parent.getFees().getId();
		String[] houseIds = device.getHouseIds().split(",");
		
		System.out.println(">>>>>>>>>>>>>>>>>>>> parentId >>>>>>>>>>>>>>>>>>>>>>>>"+parentId);
		System.out.println(">>>>>>>>>>>>>>>>>>>> houseIds >>>>>>>>>>>>>>>>>>>>>>>>"+houseIds);
//		System.out.println(">>>>>>>>>>>>>>>>>>>> parentId >>>>>>>>>>>>>>>>>>>>>>>>"+parentId);
		
		
		for (String houseId : houseIds) { 

				List<Device> ls = deviceService.findAllChild(houseId, parentId);
				
				System.out.println(">>>>>>>>>>>>>>>>>>>> ls.size() >>>>>>>>>>>>>>>>>>>>>>>>"+ls.size());
				
				if(ls.size() == 0 ){
					Device dd = new Device();
					dd.setName(parent.getCode());
					dd.setCode(parent.getCode());
					dd.setType("3");
					dd.setHouse(new House(houseId));
					dd.setParent(parent);
					dd.setFees(parent.getFees());
					dd.setParentIds("0,"+parent.getId()+",");
					deviceService.save(dd);
				}else{
					for(Device d:ls){
						d.setDelFlag("1");
						deviceService.save(d);
					}
				}

			
		}

		deviceService.save(device);
		addMessage(redirectAttributes, "保存分摊信息'" + device.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/pms/pool/?repage";
	}
	
//	@RequiresPermissions("pms:device:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		deviceService.delete(id);
		addMessage(redirectAttributes, "删除单元信息成功");
		return "redirect:"+Global.getAdminPath()+"/pms/buildpool/?repage";
	}
	

	
//	public Map<String, List<Device>> getDevicesJson(String model,HttpServletRequest request, HttpServletResponse response) {
//
//    	String houserId = null;
//    	response.setContentType("application/json; charset=UTF-8");
// 
//		
//    	if("house".endsWith(model)){
//    		houserId = request.getParameter("houserId");
//    	} 
//    	
//
//    	System.out.println(">>>>>>>>>>>>>>>>>>>>>getDevicesJson>>>>>>>>>>> houserId >>>>>>>>>>>>>>>>>>>>>>>>>>"+houserId);
//  		
//    	
//		Device device = new Device();
//		List<House> houseList =  Lists.newArrayList();
//		device.setHouseList(houseList);
//		List<Device> list = deviceService.find(device);
//		
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>getDevicesJson>>>>>>>>>>> Device list.size()>>>>>>>>>>>>>>>>>>>>>>>>>>"+list.size());
//		
//		Map<String, List<Device>> map = Maps.newHashMap();
//		
////		map.put("Total", String.valueOf(2));
//		map.put("results", list);
//		return map;
//	}

}
