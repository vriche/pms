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
import com.thinkgem.jeesite.modules.pms.utils.HouseUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 单元信息Controller
 * @author vriche
 * @version 2014-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/pms/build")
public class BuildController extends BaseController {
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
		

//		System.out.println(">>>>>>>>>>>>>>>>>>>> houseIds >>>>>>>>>>>>>>>>>>>>>>>>"+ request.getParameter("houseIds"));
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>> fees.id >>>>>>>>>>>>>>>>>>>>>>>>"+ request.getParameter("fees.id"));
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>> fees.id >>>>>>>>>>>>>>>>>>>>>>>>"+ device.getFees().getId());
		
		device.setHouseIds(request.getParameter("houseIds"));
		
		device.setType("3");
		
        Page<Device> page = deviceService.findBuild(new Page<Device>(request, response), device);
        
        
        model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
        
        if(device.getFees() != null){
            model.addAttribute("feesList", feesService.find(device.getFees())); 
            model.addAttribute("page", page);
        }else{
        	 model.addAttribute("page", new Page<Device>());
        }

		return "modules/pms/buildList";
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
		if (!beanValidator(model, device)){
			return form(device, model);
		}
		
		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getHouse() >>>>>>>>>>>>>>>>>>>>>>>>"+device.getHouse());
		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getHouse() >>>>>>>>>>>>>>>>>>>>>>>>"+device.getHouse().getId());
		
	
		House house = new House();
		String houseId = "" ;
		if(device.getHouse() != null){
			if (StringUtils.isNotEmpty(device.getHouse().getId())){
				if(device.getHouse().getId().indexOf(",")>-1){
//					houseId = request.getParameter("house.id");
					String[] s = device.getHouse().getId().split(",");
					houseId = s[s.length-1];
					System.out.println(">>>>>>>>>>>>>>>>>>>> houseId1 >>>>>>>>>>>>>>>>>>>>>>>>"+ s[s.length-1]);
					
				}else{
					houseId = device.getHouse().getId();
				}
				
			}else{
				houseId = "0";
			}
		}else{
			houseId = "0";
		}
		
		System.out.println(">>>>>>>>>>>>>>>>>>>> houseId2 >>>>>>>>>>>>>>>>>>>>>>>>"+ houseId);
		
		house.setId(houseId);
		device.setHouse(house);
		
	  
		deviceService.save(device);
		addMessage(redirectAttributes, "保存单元信息'" + device.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/pms/build/?repage";
	}
	
//	@RequiresPermissions("pms:device:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		deviceService.delete(id);
		addMessage(redirectAttributes, "删除单元信息成功");
		return "redirect:"+Global.getAdminPath()+"/pms/build/?repage";
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
