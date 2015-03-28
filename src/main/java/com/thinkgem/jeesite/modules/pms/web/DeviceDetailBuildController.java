/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.PayemtDetail;
import com.thinkgem.jeesite.modules.pms.service.BuildingsService;
import com.thinkgem.jeesite.modules.pms.service.CommunityService;
import com.thinkgem.jeesite.modules.pms.service.DeviceService;
import com.thinkgem.jeesite.modules.pms.service.FeesService;
import com.thinkgem.jeesite.modules.pms.service.HouseService;
import com.thinkgem.jeesite.modules.pms.service.PayemtDetailService;
import com.thinkgem.jeesite.modules.pms.service.UnitService;
import com.thinkgem.jeesite.modules.pms.utils.DeviceUtils;
import com.thinkgem.jeesite.modules.pms.utils.FeesUtils;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 单元信息Controller
 * @author vriche
 * @version 2014-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/pms/deviceBuild")
public class DeviceDetailBuildController extends BaseController {
//	@Autowired
//	private OfficeService officeService;
	@Autowired
	private DeviceService deviceService;

	@Autowired
	private CommunityService communityService;
	
	@Autowired
	private FeesService feesService;
	
	@Autowired
	private BuildingsService buildingsService;
	
	@Autowired
	private UnitService unitService;
	
	@Autowired
	private HouseService houseService;
	
	@Autowired
	private PayemtDetailService payemtDetailService;
	
	
	@Autowired
	private OfficeService officeService;
	
	
	private boolean isNewExcel =  com.thinkgem.jeesite.common.config.Global.getOfficeVersion();
	
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
//		System.out.println(">>>>>>>>>>>>>>>>>>>> proCompanyId >>>>>>>>>>>>>>>>>>>>>>>>");
		
		DeviceUtils.setObjFromReq(device,  request,  response,  model,1);
		device.setPool(null);
		device.setModel("1");
        
        if(device.getFees().getCompany() != null){
             Page<Device> page = deviceService.findForPayemtDetail(new Page<Device>(request, response), device);
        	 model.addAttribute("page", page);
        }else{
        	 model.addAttribute("page", new Page<Device>());
        }
       
        
		return "modules/pms/deviceDetailBuildList";
	}
	

	

//	@RequiresPermissions("pms:device:view")
	@RequestMapping(value = "form")
	public String form(Device device, Model model) {

//		Device parent = new Device();
//		parent.setId("0");
//		parent.setParent(parent);

		
//		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getLastDate() >>>>>>>>>>>>>>>>>>>>>>>>"+ device.getLastDate());
		
//		Device d = new Device();
		device.setLastDate( device.getLastDate());
		device.setPaymentDate(device.getPaymentDate());
//		d.setType("1");
		model.addAttribute("parentList", deviceService.find(device));
		
		model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
		model.addAttribute("feesList", feesService.findAllList());
		model.addAttribute("device", device);
		return "modules/pms/deviceDetailBuildForm";
	}
	

	
//	@RequiresPermissions("pms:device:edit")
	@RequestMapping(value = "save")
	@SuppressWarnings("unchecked")
	public String save(Device device, Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) {
//		if (!beanValidator(model, device)){
//			return form(device, model);
//		}

		
//		Map<String, String> map = new HashMap<String, String>();  
		   
		String proCompanyId = request.getParameter("house.unit.buildings.community.proCompany.id");
		String feesId = request.getParameter("fees.id");
		String communityId = request.getParameter("house.unit.buildings.community.id");
		String buildingsId = request.getParameter("house.unit.buildings.id");
		String unitId = request.getParameter("house.unit.id");

		System.out.println(">>>>>>>>>>>>>>>>>>>> proCompanyId >>>>>>>>>>>>>>>>>>>>>>>>"+proCompanyId);
		System.out.println(">>>>>>>>>>>>>>>>>>>> feesId >>>>>>>>>>>>>>>>>>>>>>>>"+feesId);
		System.out.println(">>>>>>>>>>>>>>>>>>>> communityId >>>>>>>>>>>>>>>>>>>>>>>>"+communityId);
		System.out.println(">>>>>>>>>>>>>>>>>>>> buildingsId >>>>>>>>>>>>>>>>>>>>>>>>"+buildingsId);
		System.out.println(">>>>>>>>>>>>>>>>>>>> unitId >>>>>>>>>>>>>>>>>>>>>>>>"+unitId);
	
		String id = device.getId();
		Device d = deviceService.get(id);
		d.setHouse(houseService.findByDevice(id));
//		d.setParent(deviceService.get("0"));
//		
//		 List<Device> childList = Lists.newArrayList();
//		 d.setChildList(childList);
		
		System.out.println("111111111111 >>>>>>>>>>>>>>>>>>>> device.getFees() >>>>>>>>>>>>>>>>>>>>>>>>"+device.getFees().getId());
		System.out.println("222222222222 >>>>>>>>>>>>>>>>>>>> device.getFees() >>>>>>>>>>>>>>>>>>>>>>>>"+device.getHouse().getId());

		d.setFirstDate(device.getLastDate());
		  //feesMode 1 按住户   2 按房屋面积    3 按加建面积  4 按使用量    5 按实际应收金额    6自定义
		if("4".equals(device.getFees().getFeesMode())){
			d.setFirstNum(device.getLastNum());
			d.setUsageAmount(device.getUsageAmount());		
		}
		
//		d.setPaymentDate(device.getPaymentDate());
		d.setPayMoney(device.getPayMoney());
		
//		System.out.println("333333333333 >>>>>>>>>>>>>>>>>>> >>>>>>>>>>>>>>>>>>>>>>>>"+d.toString());
		
		System.out.println("444444444  device.getPayMoney()>>>>>>>>>>>>>>>>>>> >>>>>>>>>>>>>>>>>>>>>>>>"+ device.getPayMoney());

		deviceService.saveDetail(d);
		
		payemtDetailService.saveByDevice(device);

		redirectAttributes.addAttribute("house.unit.buildings.community.proCompany.id",proCompanyId);
		redirectAttributes.addAttribute("fees.id",feesId);
		redirectAttributes.addAttribute("house.unit.buildings.community.id",communityId);
		redirectAttributes.addAttribute("house.unit.buildings.id",buildingsId);
		redirectAttributes.addAttribute("house.unit.id",unitId);

		addMessage(redirectAttributes, "保存单元信息'" + device.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/pms/deviceBuild/?repage";
	}
	
	
	
	@RequestMapping(value = "savelastnum")
	@SuppressWarnings("unchecked")
	public String savelastnum(Device device, Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		String id = request.getParameter("pk");
		String lastNum = request.getParameter("value");
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>saveLastNum pk>>>>>>>>>>>>>>>>>>>>>>>>"+id);
		System.out.println(">>>>>>>>>>>>>>>>>>>>saveLastNum lastNum>>>>>>>>>>>>>>>>>>>>>>>>"+lastNum);
		
		
		Device d = deviceService.get(id);
		d.setHouse(houseService.findByDevice(id));
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>saveLastNum getHouse>>>>>>>>>>>>>>>>>>>>>>>>"+d.getHouse());
		System.out.println(">>>>>>>>>>>>>>>>>>>>saveLastNum getHouse getId>>>>>>>>>>>>>>>>>>>>>>>>"+d.getHouse().getId());
		
		d.setLastNum(new BigDecimal(lastNum));
//		deviceService.saveDetail(d);
		addMessage(redirectAttributes, "保存单元信息'" + device.getName() + "'成功");
//		return null;
		return "redirect:"+Global.getAdminPath()+"/pms/deviceBuild/?repage";
	}

	@RequestMapping(value = "savedetail")
	@SuppressWarnings("unchecked")
	public String savedetail(Device device, HttpServletRequest request, HttpServletResponse response, Model model) {

		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			device.setCreateBy(user);
		}
		
		
		device.setPool(null);
		device.setModel("1");

//		System.out.println(">>>>>>>>>>>>>>>>>>>>88888888888888888888888888   777777777777777     proCompanyId >>>>>>>>>>>>>>>>>>>>>>>>");
		DeviceUtils.setObjFromReq( device,  request,  response,  model,1);
		
		
        List<Device> ls = deviceService.findForPayemtDetailAll(device);
   
        for(Device d:ls){
        	 d.setLastDate(device.getLastDate());
        	 d.setPaymentDate(device.getPaymentDate());
        	 double sumpay = d.getSumPayMoney().doubleValue();
        	 if(sumpay >0){
            	 payemtDetailService.saveByDevice(d);
            	 deviceService.saveDetail(d);     		 
        	 }

        }
       
        
        
        if(device.getFees().getCompany() != null){
             Page<Device> page = deviceService.findForPayemtDetail(new Page<Device>(request, response), device);
        	 model.addAttribute("page", page);
        }else{
        	 model.addAttribute("page", new Page<Device>());
        }
       
		return "modules/pms/deviceDetailBuildList";
	}
	
//	@RequiresPermissions("pms:device:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		deviceService.delete(id);
		addMessage(redirectAttributes, "删除单元信息成功");
		return "redirect:"+Global.getAdminPath()+"/pms/deviceBuild/?repage";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "getdevicesJson")
	public Map<String, Object> getdevicesJson(String model,HttpServletRequest request, HttpServletResponse response) {	
    
		Map<String, Object> map = Maps.newHashMap();
		StringBuffer sb = new StringBuffer();
		String houseIds = request.getParameter("houseIds");
		Device device = new Device();
		device.setHouseIds(houseIds);

		List<Device> ls = deviceService.findForPayemtDetail(device);
		
		sb.delete(0,sb.length());
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<rows>");  

//		"房间,表编号,业主,上次日期,上次读数,本次读数,使用量";
		int i=0;
		for(Device d:ls){
			
			System.out.println(">>>>>>>>>>>>>>>>>>>>>getdevicesJson>>>>>>>>>>> d.getHouse() >>>>>>>>>>>>>>>>>>>>>>>>>>"+d.getHouse());
			
			House h = d.getHouse();
			String fullName = "";
			String userName = "";
			if(h != null) {
				fullName =  h.getFullName();
				userName =  h.getOwner().getName();
			}
			

			sb.append("<row  id=\""+d.getId()  +"\">");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(fullName, "") +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue( d.getCode(), "")  +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(userName, "")  +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getFirstDate() , "") +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getFirstNum() , "0")  +"]]></cell>");
			sb.append("<cell>0</cell>");
			sb.append("<cell>0</cell>");
			sb.append("</row>");
		 }


		sb.append("</rows>");
		map.put("grid", sb.toString());
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
