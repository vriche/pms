/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.Arith;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.DeviceDetail;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.service.CommunityService;
import com.thinkgem.jeesite.modules.pms.service.DeviceDetailService;
import com.thinkgem.jeesite.modules.pms.service.DeviceService;
import com.thinkgem.jeesite.modules.pms.service.FeesService;
import com.thinkgem.jeesite.modules.pms.service.HouseService;
import com.thinkgem.jeesite.modules.pms.utils.DeviceUtils;
import com.thinkgem.jeesite.modules.pms.utils.FeesUtils;
import com.thinkgem.jeesite.modules.pms.utils.HouseUtils;
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
@RequestMapping(value = "${adminPath}/pms/device")
public class DeviceController extends BaseController {
	@Autowired
	private OfficeService officeService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private DeviceDetailService deviceDetailService;
	@Autowired
	private FeesService feesService;
	@Autowired
	private HouseService houseService;
	@Autowired
	private CommunityService communityService;
	
	
	
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
//		User user = UserUtils.getUser();
//		if (!user.isAdmin()){
//			device.setCreateBy(user);
//		}
		DeviceUtils.getObjFromReq(device,request,response,model,1);
		device.setEnable(null);
        Page<Device> page = deviceService.findPage(new Page<Device>(request, response), device);

        model.addAttribute("page", page);
		return "modules/pms/deviceList";
	}
	

	 @RequestMapping(value = "initDevice", method=RequestMethod.POST)
	public String initDevice(Device device, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		 Office proCompany = device.getFees().getCompany();
		String proCompanyId = proCompany.getId();
		
		
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			device.setCreateBy(user);
		}

		System.out.println(">>>>>>>>>>>>>>>>>>>initDevice>>>>>>>>>>>>>>>>>>>>>>>>111111111111111111111111");
		
		DeviceUtils.getObjFromReq(device,request,response,model,1);
		
		System.out.println(">>>>>>>>>>>>>>>>>>>initDevice>>>>>>>>>>>>>>>>>>>>>>>>2222222222222222222222222");
        Page<Device> pageTemp = deviceService.findPage(new Page<Device>(request, response,-1), device);
        System.out.println(">>>>>>>>>>>>>>>>>>>initDevice>>>>>>>>>>>>>>>>>>>>>>>>33333333333333333");
        List<Device> lsAll = pageTemp.getList();

		String deviceType = device.getType();
		int size = lsAll.size();
		
		
//		 String proCompanyId = device.getFees().getCompany().getId();
		 Fees  fees = feesService.get(device.getFees().getId());
		 System.out.println(">>>>>>>>>>>>>>>>>>>initDevice>>>>>>>>>>>>>>>>>>>>>>>>4444444444444");
		 String feesCode = fees.getCode();
		 BigDecimal unitPrice = fees.getUnitPrice();
		 String feesMode = fees.getFeesMode();
		 
		//初始化公摊设备，小区名 +费用
		if("1".equals(deviceType)){
			 System.out.println(">>>>>>>>>>>>>>>>>>>initDevice>>>>>>>>>>>>>>>>>>>>>>>>5555555555");
			 List<Community> communityList = communityService.findAllCommunityByProCompanyId(proCompanyId);
			 System.out.println(">>>>>>>>>>>>>>>>>>>initDevice>>>>>>>>>>>>>>>>>>>>>>>>666666666");
			int communitySize = communityList.size();
			if(communitySize >0 && size ==0){
				for(Community community:communityList){
					String communityName = community.getName();
					System.out.println(">>>>>>>>>>>>>>>>>>>initDevice>>>>>>>>>>>>>>>>>>>>>>>>7777777777"+communityName);
					if(!"单位".equals(communityName)){
						System.out.println(">>>>>>>>>>>>>>>>>>>initDevice>>>>>>>>>>>>>>>>>>>>>>>>8888888");
						String communityId =  community.getId();
						Device dev = new Device();
						dev.setFees(fees);
						dev.setHouse(new House("0"));
						dev.setParent(new Device("0"));
						dev.setEnable("1");
						dev.setCode(communityName+"_"+feesCode);
						dev.setName(communityId+"_"+fees.getId());
						dev.setFirstDate(DateUtils.parseDate("1900-01-01"));
						dev.setLastDate(DateUtils.parseDate("1900-01-01"));
						dev.setPaymentDate(DateUtils.parseDate("1900-01-01"));
						System.out.println(">>>>>>>>>>>>>>>>>>>initDevice>>>>>>>>>>>>>>>>>>>>>>>>unitPrice"+unitPrice);
						dev.setUnitPrice(unitPrice);
						dev.setType(deviceType);
						dev.setFeesMode(feesMode);
						dev.setChildList(new ArrayList<Device>());
						System.out.println(">>>>>>>>>>>>>>>>>>>initDevice>>>>>>>>>>>>>>>>>>>>>>>>9999999999");
						BeanValidators.validateWithException(validator, dev);
						System.out.println(">>>>>>>>>>>>>>>>>>>initDevice>>>>>>>>>>>>>>>>>>>>>>>>AAAAAAAAAAAAA");
						deviceService.saveDetail(dev);
						System.out.println(">>>>>>>>>>>>>>>>>>>initDevice>>>>>>>>>>>>>>>>>>>>>>>>BBBBBBBBBBBB");
						
					}

				}
			}
			 
		}
		if("2".equals(deviceType)){
			List<Office> companyList = UserUtils.findCompanyListByProCompany(proCompanyId);
		}
		if("3".equals(deviceType)){
			
		}
	

        Page<Device> page = deviceService.findPage(new Page<Device>(request, response), device);
        model.addAttribute("page", page);
		return "modules/pms/deviceList";
	}

	


//	@RequiresPermissions("pms:device:view")
	@RequestMapping(value = "form")
	public String form(Device device, Model model) {

//		Device parent = new Device();
//		parent.setId("0");
//		parent.setParent(parent);

		
//		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getType() >>>>>>>>>>>>>>>>>>>>>>>>"+ device.getType());
		
		Device d = new Device();
		d.setType("1");
		
		 List<Office> companyList = UserUtils.findProCompanyList();
		 
		model.addAttribute("parentList", deviceService.find(d));
		
		model.addAttribute("proCompanyList", companyList);
		
		 String proCompanyId ="";
		 if(device.getFees() != null){
			 if(device.getFees().getCompany() != null){
				 proCompanyId = device.getFees().getCompany().getId();
			 }	 
		 }

		 Office proCompany = null;
		 if (StringUtils.isBlank(proCompanyId) && companyList.size() >0){
			 proCompany = companyList.get(0);
		 }else{
			 proCompany = new Office(proCompanyId);
		 }
		 Fees fees = new Fees();
		 fees.setCompany(proCompany);

		 
		model.addAttribute("feesList", feesService.findNoPool(fees));
		model.addAttribute("device", device);
	
		return "modules/pms/deviceForm";
	}
	@RequestMapping(value = "savehouse")
	public String savehouse(Device device, Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		List<House> listTemp =  houseService.findAllHouse(new House());
		deviceService.saveDeviceByHouseList("2",listTemp);
		return "redirect:"+Global.getAdminPath()+"/pms/device/?repage";
	}


//	@RequiresPermissions("pms:device:edit")
	@RequestMapping(value = "save")
	public String save(Device device, Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) {

		

//		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getHouse() 1111111111111111 >>>>>>>>>>>>>>>>>>>>>>>>"+ request.getParameter("houseId"));
//		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getHouse() 666666666666666666 >>>>>>>>>>>>>>>>>>>>>>>>"+ request.getParameter("a"));
//		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getHouse() 777 >>>>>>>>>>>>>>>>>>>>>>>>"+ model.asMap().get("a"));
		Fees fees = device.getFees();
		String deviceType = device.getType();
		String houseId = request.getParameter("houseId");

		if(device.getParent() == null || (device.getParent() != null && StringUtils.isBlank(device.getParent().getId()))){
			device.setParent(new Device("0"));
		}
		
		if (StringUtils.isBlank(houseId) || "1".equals(deviceType)) {
			houseId = "0";
		}
		House house = HouseUtils.getHouse(houseId);
		device.setHouse(house);
		
		
		if (!beanValidator(model, device)){
			return form(device, model);
		}
			
		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getHouse() 777 >>>>>>>>>>>>>>>>>>>>>>>>"+ house);
		deviceService.save(device);
		addMessage(redirectAttributes, "保存单元信息'" + device.getName() + "'成功");
		
		redirectAttributes.addAttribute("fees.company.id", device.getFees().getCompany().getId());
		redirectAttributes.addAttribute("type", deviceType);
		redirectAttributes.addAttribute("fees.id", fees.getId());
		if(!"1".equals(deviceType)){
			redirectAttributes.addAttribute("house.unit.buildings.community.id", house.getUnit().getBuildings().getCommunity().getId());
			redirectAttributes.addAttribute("house.unit.buildings.id", house.getUnit().getBuildings().getId());
			redirectAttributes.addAttribute("house.unit.id", house.getUnit().getId());
//			redirectAttributes.addAttribute("house.id", house.getId());
		}

		
		return "redirect:"+Global.getAdminPath()+"/pms/device/?repage";
	}
	

//	@RequiresPermissions("pms:device:edit")
	@RequestMapping(value = "delete" , method=RequestMethod.GET)
	public String delete(String id, String proCompanyId,String type,String feesId,RedirectAttributes redirectAttributes) {
		deviceService.delete(id);
		addMessage(redirectAttributes, "删除单元信息成功");
	

		 Fees fees = new Fees();
		 fees.setCompany(new Office(proCompanyId));

		redirectAttributes.addAttribute("fees.company.id", proCompanyId);
		redirectAttributes.addAttribute("type", type);
		redirectAttributes.addAttribute("fees.id", feesId);

		return "redirect:"+Global.getAdminPath()+"/pms/device/?repage";
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
	
	@ResponseBody
	@RequestMapping(value = "getDevicePoolPayment")
	public Map<String,String> getDevicePoolPayment(String id,HttpServletRequest request, HttpServletResponse response) {
    	response.setContentType("application/json; charset=UTF-8");
    	Map<String,String> mp = new HashMap<String,String>();
    	
    	System.out.println(">>>>>>>>>>>>>>>>>>>>>getDevicePoolPayment 1111111111 1111111111 >>>>>>>>>>> id >>>>>>>>>>>>>>>>>>>>>>>>>>"+ id);
    	Device d = deviceService.get(id);
    	String usageAmount = StringUtils.toBigDecimal(d.getUsageAmount()).toString();
    	String unitPrice = StringUtils.toBigDecimal(d.getFees().getUnitPrice()).toString();
    	mp.put("usageAmount",usageAmount);
    	mp.put("unitPrice", unitPrice);
  	    double payMoney =  Arith.roundEVEN(d.getUsageAmount().doubleValue()*d.getFees().getUnitPrice().doubleValue(), 2) ;
    	mp.put("payMoney", String.valueOf(payMoney));

//    	System.out.println(">>>>>>>>>>>>>>>>>>>>>getDevicePoolPayment 1111111111 22222222222 >>>>>>>>>>> d >>>>>>>>>>>>>>>>>>>>>>>>>>"+ d.getFees().getName());
		return mp;
		
	}
	
	
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Device device, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String fileSuffix = isNewExcel?".xlsx":".xls";
		 String curDateStr =  DateUtils.getDate("yyyy年MM月");
		String proCompanyId =device.getFees().getCompany().getId();
		String feesId =device.getFees().getId();
		String deviceType =device.getType();

		Office proCompany = officeService.get(proCompanyId);
		Fees fees = FeesUtils.getFees(feesId);
		
		
		
		String proCompanyName =proCompany.getName();
		String deviceTypeName= DictUtils.getLable("pms_device_type", deviceType);
		String feesName = StringUtils.getNullValue(fees.getName(), "");
		
		String titleMain = proCompanyName ;
		String titleSub = "收费项目("+curDateStr+deviceTypeName + feesName + ")";
		String title = titleMain + titleSub;
//		String fileName = titleSub + DateUtils.getDate("yyyyMMddHHmmss")+ fileSuffix; 
		feesName = StringUtils.isBlank(feesName)?"":"("+feesName+")";
		String fileName = curDateStr +deviceTypeName +feesName+ fileSuffix;
		
    	try {
    		

    		 fees.setCompany(proCompany);
    		 device.setFees(fees);	 
//    		Page<User> page = userService.findUser(new Page<User>(request, response, -1), user); 
//            new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
            String m = "3".equals(device.getType())?"1":"0";
    		if("2".equals(device.getType())) m = "2";
    		
    		device.setLastDate(null);
    		device.setFirstDate(null);
    		device.setPaymentDate(null);
    		device.setModel(m);
    		device.setPool(null);
    		device.setUnitPrice(null);
    		
    	
    		DeviceUtils.setObjFromReq(device,  request,  null,  null,1);
    		long start = System.currentTimeMillis();
    		List<Device> ls = deviceService.findAll(device);
    		long end = System.currentTimeMillis();
    		
    
    		
    		for (Device dev : ls){
    			dev.setFirstNumStr(dev.getLastNum());
    			dev.setFirsDateStr(DateUtils.formatDate(dev.getLastDate()));
    			Date paymentDate = dev.getPaymentDate();
    			if(paymentDate == null){
    				dev.setCurPaymentDateStr(DateUtils.parseDate(DateUtils.getDate()+" 00:00:00"));
    			}else{
    				dev.setCurPaymentDateStr(DateUtils.parseDate(DateUtils.formatDate(dev.getPaymentDate())));
    			}
    			
    			double unitPrice = Double.valueOf(StringUtils.getNullValue(dev.getUnitPrice(), "0"));
    			if(unitPrice == 0){dev.setUnitPrice(dev.getFees().getUnitPrice());}
//    			dev.setCurDateStr(DateUtils.parseDate(DateUtils.getDate()+" 00:00:00"));
    			dev.setCurDateStr(DateUtils.getDate());

    		
    		}
    		new ExportExcel(title, Device.class).setDataList(ls).write(response, fileName).dispose();
    		
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出"+ title +"失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pms/device/?repage";
    }
    
    
  
    @RequestMapping(value = "import", method=RequestMethod.POST)
//    public String importFile(MultipartFile file,Device device, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
    	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {

		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/pms/device/?repage";
		}
		
//		String proCompanyId =device.getFees().getCompany().getId();
//		int deviceType = Integer.parseInt(device.getType());
//				
//		 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>file"+file);
//		 System.out.println("111111111111111111111111111111111  66666666666666666666       >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>proCompanyId");
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
//			ImportExcel ei = new ImportExcel(file, 1, deviceType-1);
			ImportExcel ei = new ImportExcel(file, 1,0);
			List<Device> list = ei.getDataList(Device.class);
//			System.out.println(" list.size()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+list.size());
			
			for (Device dev : list){
				try{
					String id = dev.getId();
					
//					String code = dev.getCode();
					
					if(StringUtils.isNotEmpty(id)){
                        
						Device deviceDb = deviceService.get(id);
						
						if(deviceDb != null){
//							String devType = dev.getType();
//							System.out.println(" getId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+dev.getId());
////							System.out.println(" getType>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+dev.getType());
//							System.out.println(" getFirstNum>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+dev.getFirstNumStr());
//							System.out.println(" getLastNum>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+dev.getCurNumStr());
//							System.out.println(" getUsageAmount>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+dev.getUsageAmount());
//							System.out.println(" getPoolUsageAmount>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+dev.getPoolUsageAmount());
//							System.out.println(" getLastDate>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+dev.getLastDate());
//							System.out.println(" getPaymentDate>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+dev.getPaymentDate());
							
							deviceDb.setFirstNum(dev.getFirstNumStr());
							deviceDb.setLastNum(dev.getCurNumStr());
							deviceDb.setFirstDate(DateUtils.parseDate(dev.getFirsDateStr()));
//							deviceDb.setLastDate(dev.getCurDateStr());
//							dev.setCurDateStr(DateUtils.parseDate(DateUtils.getDate()+" 00:00:00"));
							
							Date curDate = DateUtils.parseDate(dev.getCurDateStr(), new String[]{"yyyy-MM-dd"});
							
							deviceDb.setLastDate(curDate);
							deviceDb.setPaymentDate(curDate);
							deviceDb.setUnitPrice(dev.getUnitPrice());
							
//							deviceDb.setLastDate(dev.getLastDate());
//							deviceDb.setPaymentDate(dev.getPaymentDate());
							
//							if(deviceDb.getParent() ==null){
//								deviceDb.setParent(new Device("0"));
//								deviceDb.setParentIds("0,");
//							}
							
							
							
						
							double  curUsageAmount =  Double.parseDouble(StringUtils.getNullValue(dev.getCurUsageAmountStr(), "0"));
							
							
							if(curUsageAmount > 0){
								deviceDb.setUsageAmount(StringUtils.toBigDecimal(dev.getCurUsageAmountStr()));
							}else{
								double firstNum = Double.parseDouble(StringUtils.getNullValue(dev.getFirstNumStr(), "0"));
								double curNum = Double.parseDouble(StringUtils.getNullValue(dev.getCurNumStr(), "0"));
								
								if(curNum>0 && curNum >= firstNum){
									double  usageAmount = curNum - firstNum;
									deviceDb.setUsageAmount(StringUtils.toBigDecimal(usageAmount));
								}else{
									failureMsg.append("<br/>序号为（"+id+"）有误 导入失败,当前读数步允许小于上次读数，请检查导入模板。");
								}
							}
							
//							deviceDb.setUsageAmount(StringUtils.toBigDecimal("0"));
							
							deviceDb.setPoolUsageAmount(StringUtils.toBigDecimal(dev.getPoolUsageAmount()));
							
							
							double  sumPayMoney =  Double.parseDouble(StringUtils.getNullValue(dev.getSumPayMoney(), "0"));
							deviceDb.setSumPayMoney(StringUtils.toBigDecimal(sumPayMoney));
							
//							if(sumPayMoney >0){
//								
//							}
							
							
							
							
							try{
								
//								System.out.println("deviceDb>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+deviceDb.toString());
								
								BeanValidators.validateWithException(validator, deviceDb);

								deviceService.saveDetail(deviceDb);
		
								DeviceDetail deviceDetail = new DeviceDetail();
								
								deviceDetail.setDevice(deviceDb);
								
//								System.out.println("deviceDetail.getLastDate()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+deviceDetail.getLastDate());
								
								List<DeviceDetail> ls = deviceDetailService.find(deviceDetail);
//								
//								System.out.println("ls.size()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ls.size());
								
								
								if(ls.size() >0){
									DeviceDetail dtail = ls.get(0);
									deviceDetail.setId(dtail.getId());
//									deviceDetail.setFeesMode(deviceDb.getFeesMode());
//									deviceDetail.setUnitPrice(deviceDb.getUnitPrice());
//									deviceDetail.setFirstDate(deviceDb.getFirstDate());
//									deviceDetail.setLastDate(deviceDb.getLastDate());
//									deviceDetail.setFirstNum(deviceDb.getFirstNum());
//									deviceDetail.setLastNum(deviceDb.getLastNum());
//									deviceDetail.setPaymentDate(deviceDb.getPaymentDate());
//									deviceDetail.setUsageAmount(deviceDb.getUsageAmount());
//									deviceDetail.setPoolUsageAmount(deviceDb.getPoolUsageAmount());
//									deviceDetail.setPoolPayMoney(deviceDb.getPoolPayMoney());
//									deviceDetail.setSumUsageAmount(deviceDb.getSumUsageAmount());
//									deviceDetail.setSumPayMoney(deviceDb.getSumPayMoney());
//									deviceDetail.setUnitPrice(deviceDb.getUnitPrice());
								}else{
//									deviceDetail.setFeesMode(deviceDb.getFeesMode());
//									deviceDetail.setUnitPrice(deviceDb.getUnitPrice());
//									deviceDetail.setFirstDate(deviceDb.getFirstDate());
//									deviceDetail.setLastDate(deviceDb.getLastDate());
//									deviceDetail.setFirstNum(deviceDb.getFirstNum());
//									deviceDetail.setLastNum(deviceDb.getLastNum());
//									deviceDetail.setPaymentDate(deviceDb.getPaymentDate());
//									deviceDetail.setUsageAmount(deviceDb.getUsageAmount());
//									deviceDetail.setPoolUsageAmount(deviceDb.getPoolUsageAmount());
//									deviceDetail.setPoolPayMoney(deviceDb.getPoolPayMoney());
//									deviceDetail.setSumUsageAmount(deviceDb.getSumUsageAmount());
//									deviceDetail.setSumPayMoney(deviceDb.getSumPayMoney());
//									deviceDetail.setUnitPrice(deviceDb.getUnitPrice());
								}
								
								deviceDetail.setFeesMode(deviceDb.getFeesMode());
								deviceDetail.setUnitPrice(deviceDb.getUnitPrice());
								deviceDetail.setFirstDate(deviceDb.getFirstDate());
								deviceDetail.setLastDate(deviceDb.getLastDate());
								deviceDetail.setFirstNum(deviceDb.getFirstNum());
								deviceDetail.setLastNum(deviceDb.getLastNum());
								deviceDetail.setPaymentDate(deviceDb.getPaymentDate());
								deviceDetail.setUsageAmount(deviceDb.getUsageAmount());
								deviceDetail.setPoolUsageAmount(deviceDb.getPoolUsageAmount());
								deviceDetail.setPoolPayMoney(deviceDb.getPoolPayMoney());
								deviceDetail.setSumUsageAmount(deviceDb.getSumUsageAmount());
								deviceDetail.setSumPayMoney(deviceDb.getSumPayMoney());
								deviceDetail.setUnitPrice(deviceDb.getUnitPrice());
								deviceDetail.setParent(new Device(deviceDb.getParent().getId()));
					
								
								deviceDetailService.save(deviceDetail);
								
								successNum++;
							}catch(ConstraintViolationException ex){
								failureMsg.append("<br/>序号为（"+id+"） 导入失败：");
								List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
								for (String message : messageList){
									failureMsg.append(message+"; ");
									failureNum++;
								}
								
								System.out.println("deviceService.saveDetail ConstraintViolationException>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ex.getMessage());
								
							}catch (Exception ex) {
								failureMsg.append("<br/>序号为（"+id+"） 导入失败："+ex.getMessage());
								failureNum++;
								System.out.println("deviceService.saveDetail Exception>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ex.getMessage());
							}

						}else{
							failureMsg.append("<br/>序号为（"+id+"）有误 导入失败,数据库里记录不存在，请检查导入模板。");
							failureNum++;
						}


					}else{
						failureMsg.append("<br/>序号为（"+dev.getCode()+"）有误 导入失败,请检查导入模板。");
						failureNum++;
					}

				
					
//					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>3"+dev.getCurNumStr());
				} catch (Exception ex) {
					failureMsg.append("<br/>1导入收费项目失败 "+" 导入失败："+ex.getMessage());
					failureNum++;
				}
			}
			
//			addMessage(redirectAttributes, "导入收费信息成功!");
		 
		} catch (Exception e) {
			addMessage(redirectAttributes, "2导入收费项目失败！失败信息："+e.getMessage());
		 }	 
		
		
		

		return "redirect:"+Global.getAdminPath()+"/pms/device/?repage";
//		return null;
    }
    
    
	
}
