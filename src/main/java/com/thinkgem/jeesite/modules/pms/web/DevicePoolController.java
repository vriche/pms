/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.web;

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
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.service.DeviceService;
import com.thinkgem.jeesite.modules.pms.service.FeesService;
import com.thinkgem.jeesite.modules.pms.utils.DeviceUtils;
import com.thinkgem.jeesite.modules.pms.utils.HouseUtils;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 单元信息Controller
 * @author vriche
 * @version 2014-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/pms/pool")
public class DevicePoolController extends BaseController {
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
		
//		
		String proCompanyId = request.getParameter("fees.company.id");
		 List<Office> companyList = UserUtils.findProCompanyList();
		 if (StringUtils.isBlank(proCompanyId) && companyList.size() >0){
			 proCompanyId = companyList.get(0).getId();
		 }

		
        model.addAttribute("proCompanyList", companyList);
		Device d = new Device();
		d.setType("1");
		Fees fees = new Fees();
		fees.setCompany(new Office(proCompanyId));
		d.setFees(fees);
		model.addAttribute("parentList", deviceService.find(d));
		
		device.setLastDate(null);
		device.setFirstDate(null);
		device.setPaymentDate(null);
		device.setModel("2");
		device.setPool("1");
		
		Page<Device> page = null;
		if(device.getParent() != null){
			if (StringUtils.isNotEmpty(device.getParent().getId())){
				page = deviceService.find(new Page<Device>(request, response), device);
			}
		}


        model.addAttribute("page", page);
		return "modules/pms/devicePoolList";
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
		String pfid = parent.getFees().getId();
		String[] houseIds = device.getHouseIds().split(",");
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>> parentId >>>>>>>>>>>>>>>>>>>>>>>>"+parentId);
//		System.out.println(">>>>>>>>>>>>>>>>>>>> houseIds >>>>>>>>>>>>>>>>>>>>>>>>"+houseIds);
//		System.out.println(">>>>>>>>>>>>>>>>>>>> parentId >>>>>>>>>>>>>>>>>>>>>>>>"+parentId);
		
	
		List<Device> ls11 = deviceService.find(device);
		for(Device d:ls11){
//			System.out.println(">>>>>>>>>>>>>>>>>>>d.getPool() >>>>>>>>>>>>>>>>>>>>>>>>"+ d.getPool());
			if("1".equals(d.getPool())){
				d.setParent(new Device("0"));
				d.setParentIds("0,");
				deviceService.save(d);
			}
		}
		
		
		
		List<House> ls1 = HouseUtils.getHousesList3(device.getHouseIds());
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>>House ls1.size() >>>>>>>>>>>>>>>>>>>>>>>>"+ls1.size());
		
		for (House h : ls1) { 
//			List<Fees> list =  Lists.newArrayList();
//			List<Device> list = feesService.findList(houseId);
//			System.out.println(">>>>>>>>>>>>>>>>>>>> houseId >>>>>>>>>>>>>>>>>>>>>>>>"+houseId);
//			System.out.println(">>>>>>>>>>>>>>>>>>>> list.size() >>>>>>>>>>>>>>>>>>>>>>>>"+list.size());
//			
//			for (Device d : list) { 
				
			String houseId = h.getId();
//				String feesId = d.getFees().getId();
				List<Device> ls = deviceService.findAllList(houseId, pfid);
//				List<Device> ls = h.getDeviceList();
				
//				System.out.println(">>>>>>>>>>>>>>>>>>>Device> ls.size() >>>>>>>>>>>>>>>>>>>>>>>>"+ls.size());
				
//				if(ls.size() == 0 ){
//					Device dd = new Device();
//					dd.setName("");
//			
//					dd.setType("3");
//					dd.setHouse(h);
//					dd.setParent(parent);
//					dd.setFees(parent.getFees());
//					dd.setParentIds("0,"+parent.getId()+",");
//					dd.setHouse(new House(houseId)); 
//					String code = DeviceUtils.getCode(dd);
//					device.setCode(code);
//					
//					deviceService.save(dd);
//				}else{
					for(Device d:ls){
						System.out.println(">>>>>>>>>>>>>>>>>>>d.getPool() >>>>>>>>>>>>>>>>>>>>>>>>"+ d.getPool());
						if("1".equals(d.getPool())){
							d.setParent(parent);
							d.setParentIds("0,"+parent.getId()+",");
							deviceService.save(d);
						}
					}
//				}
				
//			}
			
			
			
		}

//		deviceService.save(device);
		
		redirectAttributes.addAttribute("parent.id", parentId);
		addMessage(redirectAttributes, "保存分摊信息'" + device.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/pms/pool/?repage";
	}
	
//	@RequiresPermissions("pms:device:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, String parentId, RedirectAttributes redirectAttributes) {
		Device dd = deviceService.get(id);
		dd.setParent(new Device("0"));
		dd.setParentIds("0,");
		deviceService.save(dd);
		
		System.out.println(">>>>>>>>>>>>>>>>>>>> parentId >>>>>>>>>>>>>>>>>>>>>>>>"+ parentId);
		
		redirectAttributes.addAttribute("parent.id", parentId);
		
//		deviceService.delete(id);
		addMessage(redirectAttributes, "删除计费对象成功");
		
		
		return "redirect:"+Global.getAdminPath()+"/pms/pool/?repage";
	}
	
	  @ResponseBody
		@RequestMapping(value = "devicePooljson")
		public Map<String, Object> getCommunityJson(String model,HttpServletRequest request, HttpServletResponse response) {

	    	String proCompanyId = null;
	    	
	    	response.setContentType("application/json; charset=UTF-8");
	    	
	    
	    	if("device".endsWith(model)){
	   		 	proCompanyId = request.getParameter("fees.company.id"); 
	    	}
	    	
	    	Device d = new Device();
			d.setType("1");
			Fees fees = new Fees();
			fees.setCompany(new Office(proCompanyId));
			d.setFees(fees);
			List<Device> list = deviceService.find(d);
			
			Map<String, Object> map = Maps.newHashMap();
			map.put("", "");
			for (int i=0; i<list.size(); i++){
				Device e = list.get(i);
				map.put(e.getId(), e.getCode()+"_"+e.getName());
			}
			return map;
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
