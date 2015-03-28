/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.web;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.solr.common.util.DateUtil;
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

import com.ckfinder.connector.ServletContextFactory;
//import com.dhtmlx.xml2excel.ExcelWriter;
import com.google.common.collect.Lists;
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
import com.thinkgem.jeesite.modules.pms.entity.Buildings;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.DeviceDetail;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.PaymentAfter;
import com.thinkgem.jeesite.modules.pms.entity.PaymentBefor;
import com.thinkgem.jeesite.modules.pms.entity.Unit;
import com.thinkgem.jeesite.modules.pms.service.BuildingsService;
import com.thinkgem.jeesite.modules.pms.service.CommunityService;
import com.thinkgem.jeesite.modules.pms.service.CompanyService;
import com.thinkgem.jeesite.modules.pms.service.DeviceDetailService;
import com.thinkgem.jeesite.modules.pms.service.DeviceService;
import com.thinkgem.jeesite.modules.pms.service.FeesService;
import com.thinkgem.jeesite.modules.pms.service.HouseService;
import com.thinkgem.jeesite.modules.pms.service.PaymentAfterService;
import com.thinkgem.jeesite.modules.pms.service.PaymentBeforService;
import com.thinkgem.jeesite.modules.pms.service.UnitService;
import com.thinkgem.jeesite.modules.pms.utils.DeviceDetailUtils;
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
 * @version 2015-07-16
 */
@Controller
@RequestMapping(value = "${adminPath}/pms/deviceDetail")
public class DeviceDetailController extends BaseController {

	@Autowired
	private DeviceDetailService deviceDetailService;
	@Autowired
	private PaymentBeforService paymentBeforService;
	@Autowired
	private PaymentAfterService paymentAfterService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private FeesService feesService;
	@Autowired
	private HouseService houseService;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private BuildingsService buildingsService;
	@Autowired
	private UnitService unitService;
	
	
	
	
	private boolean isNewExcel =  com.thinkgem.jeesite.common.config.Global.getOfficeVersion();
	
	@ModelAttribute
	public DeviceDetail get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return deviceDetailService.get(id);
		}else{
			return new DeviceDetail();
		}
	}
	
//	@RequiresPermissions("pms:deviceDetails:view")
	@RequestMapping(value = {"list", ""})
	public String list(DeviceDetail deviceDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DeviceDetail> page = new Page<DeviceDetail>(request, response);
		
		DeviceDetail dtParam = new DeviceDetail();
		DeviceDetailUtils.getObjFromReq(dtParam,request,model,1);
		List<DeviceDetail> lastDateList = deviceDetailService.findLastDate(dtParam,null,request, response);
		model.addAttribute("lastDateList", lastDateList);
		
		if(dtParam.getLastDate() != null){
			page = deviceDetailService.findPage(new Page<DeviceDetail>(request,response), dtParam,null,"1");
			DeviceDetail dt = deviceDetailService.findSumRow(dtParam,"1");
			model.addAttribute("deviceDetailSumRow", dt);	
		}

		Device device = dtParam.getDevice();
		Fees fees = FeesUtils.getFees(device.getFees().getId());
		String feesMode = fees.getFeesMode();
		device.getFees().setFeesMode(feesMode);
		deviceDetail.setDevice(device);
		deviceDetail.setLastDate(dtParam.getLastDate());
        model.addAttribute("page", page);
		return "modules/pms/deviceDetailList";
	}
	
	
	

//	@RequiresPermissions("pms:deviceDetails:view")
	@RequestMapping(value = "form")
	public String form(DeviceDetail deviceDetail, Model model) {
		Device device = deviceDetail.getDevice();
		Device parent = deviceDetail.getParent();
		if(parent ==null){
			parent = new Device("0");
			deviceDetail.setParent(parent);
		}

		model.addAttribute("feesList", feesService.findNoPool(device.getFees())); 
		model.addAttribute("device", device);
		model.addAttribute("deviceDetail", deviceDetail);

		return "modules/pms/deviceDetailForm";
	}

//	@RequiresPermissions("pms:deviceDetail:edit")
	@RequestMapping(value = "save")
	public String save(DeviceDetail deviceDetail, Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, deviceDetail)){
			return form(deviceDetail, model);
		}
		deviceDetailService.save(deviceDetail);
		
		addMessage(redirectAttributes, "保存单元信息'" + deviceDetail.getDevice().getName() + "'成功");
		
		Device device = deviceDetail.getDevice();
		String lasteDate = DateUtils.formatDate(deviceDetail.getLastDate(),"yyyy-MM-dd");
		
		redirectAttributes.addAttribute("device.fees.company.id", device.getFees().getCompany().getId());
		redirectAttributes.addAttribute("device.type", device.getType());
		redirectAttributes.addAttribute("device.fees.id", device.getFees().getId());
		redirectAttributes.addAttribute("lastDate",lasteDate);
		
		
		if(!"1".equals(device.getType())){
			House house =device.getHouse();
			redirectAttributes.addAttribute("device.house.unit.buildings.community.id", house.getUnit().getBuildings().getCommunity().getId());
			redirectAttributes.addAttribute("device.house.unit.buildings.id", house.getUnit().getBuildings().getId());
			redirectAttributes.addAttribute("device.house.unit.id", house.getUnit().getId());
//			redirectAttributes.addAttribute("device.house.id", house.getId());
		}	
		
		
		return "redirect:"+Global.getAdminPath()+"/pms/deviceDetail/?repage";
	}
	
//	@RequiresPermissions("pms:deviceDetails:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		deviceDetailService.delete(id);
		addMessage(redirectAttributes, "删除单元信息成功");
		return "redirect:"+Global.getAdminPath()+"/pms/deviceDetail/?repage";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "datejson")
	public Map<String, Object> getDateJson(String model,HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		Map<String, Object> map = Maps.newHashMap();
		map.put("", "选择读表日期.....");
		DeviceDetail dtParam = new DeviceDetail();
		DeviceDetailUtils.getObjFromReq(dtParam,request,null,2);
		deviceDetailService.findLastDate(dtParam,map,request, response);
		return map;
	} 
	
	
	
	@RequestMapping(value = "autoBuild", method=RequestMethod.POST)
    public String autoBuild(DeviceDetail dt, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Device device = dt.getDevice();
		Fees fs = device.getFees();
		String lastDate = request.getParameter("paymentDate");
		Date payDate = new Date();
		try {
			payDate = DateUtils.parseDate(lastDate, new String[]{"yyyy-MM-dd"});
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//				Date theDate = DateUtils.parseDate(lastDate, new String[]{"yyyy-MM-dd"});
		String proCompanyId = fs.getCompany().getId();
		String feesId = fs.getId();
	
		String deviceType =device.getType();

		Office proCompany = officeService.get(proCompanyId);
		Fees fees = FeesUtils.getFees(feesId);
		String feesMode = fees.getFeesMode();

		String proCompanyName = proCompany.getName();
		String deviceTypeName= DictUtils.getLable("pms_device_type", deviceType);
		String feesName = StringUtils.getNullValue(fees.getName(), "");
		BigDecimal defUnitPrice = fees.getUnitPrice();
		
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>autoBuild>>>>>>>>>>>111111111111 333 555 payDate>>>>>>>>>>>>>>>>>>>>>>>>>>"+payDate);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>autoBuild>>>>>>>>>>>111111111111 333 555 defUnitPrice>>>>>>>>>>>>>>>>>>>>>>>>>>"+defUnitPrice);
		
		
		if("1".equals(device.getType())){
			addMessage(redirectAttributes, "公摊设备，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/pms/deviceDetail/?repage";
		}
		if( !"1".equals(feesMode) &&  !"2".equals(feesMode) &&  !"6".equals(feesMode) &&  !"5".equals(feesMode) ){
			addMessage(redirectAttributes, "费用类型有误，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/pms/deviceDetail/?repage"; 
		}
		


		
		try {

	   		DeviceDetailUtils.getObjFromReq(dt,request,null,2);
	        Page<Device> pageTemp = deviceService.findPage(new Page<Device>(request, response,-1), dt.getDevice());
	        List<Device> ls = pageTemp.getList();
	   		
	 		for (Device deviceDb : ls){

					DeviceDetail deviceDetailParam =  new DeviceDetail();
					deviceDetailParam.setDevice(deviceDb);
					deviceDetailParam.setLastDate(payDate);
					Page<DeviceDetail> pageDeviceDetailDb = deviceDetailService.findPage(new Page<DeviceDetail>(request, response,-1), deviceDetailParam,null,"1");
//					System.out.println(">>>>>>>>>>>>>>>>>>>>>autoBuild>>>>>>>>>>>111111111111 333 555 pageDeviceDetailDb.getList().size()>>>>>>>>>>>>>>>>>>>>>>>>>>"+pageDeviceDetailDb.getList().size());
					if(pageDeviceDetailDb.getList().size() == 0){
						DeviceDetail deviceDetail = new DeviceDetail();
						deviceDetail.setDevice(deviceDb);
						deviceDetail.setFeesMode(deviceDb.getFeesMode());
						deviceDetail.setFirstDate(deviceDb.getFirstDate());
						deviceDetail.setLastDate(payDate);
						deviceDetail.setFirstNum(deviceDb.getFirstNum());
						deviceDetail.setLastNum(deviceDb.getLastNum());
						deviceDetail.setPaymentDate(payDate);
						deviceDetail.setUsageAmount(deviceDb.getUsageAmount());
						deviceDetail.setPoolUsageAmount(deviceDb.getPoolUsageAmount());
						deviceDetail.setUnitPrice(defUnitPrice);
						deviceDetail.setPayMoney(deviceDb.getPayMoney());
						deviceDetail.setPoolPayMoney(deviceDb.getPoolPayMoney());
						deviceDetail.setSumUsageAmount(deviceDb.getSumUsageAmount());
						deviceDetail.setSumPayMoney(deviceDb.getSumPayMoney());
						deviceDetail.setFeesMode(deviceDb.getFeesMode());
						deviceDetail.setFeesParams(deviceDb.getFeesParams());
						
						if(deviceDb.getParent() ==null){
							deviceDetail.setParent(new Device("0"));
						}else{
							deviceDetail.setParent(deviceDb.getParent());
						}
	
						//按户 卫生费
						if("1".equals(feesMode)){
							deviceDetail.setUsageAmount(new BigDecimal(String.valueOf(1)));
							
							deviceDetail.setSumPayMoney(defUnitPrice);
						}
						//按房屋面积 物业管理  目前单位没有使用面积，希望虚拟一个面积
						if("2".equals(feesMode)){
							House h = deviceDb.getHouse();
	//						BigDecimal buildArea = h.getBuildArea();
							double buildArea = h.getBuildArea()==null?0:h.getBuildArea().doubleValue();
							double sumPayMoney = Arith.roundEVEN(defUnitPrice.doubleValue()*buildArea, 2);
			        		String sumPayMoneyStr = String.valueOf(sumPayMoney);
			        		
			        		deviceDetail.setUsageAmount(new BigDecimal(String.valueOf(buildArea)));
							deviceDetail.setSumPayMoney(new BigDecimal(sumPayMoneyStr)); 
						}
						
						//按房屋面积 物业管理  目前单位没有使用面积，希望虚拟一个面积
						if("5".equals(feesMode)){
							House h = deviceDb.getHouse();
	//						BigDecimal buildArea = h.getBuildArea();
							double useArea = h.getBuildArea()==null?0:h.getUseArea().doubleValue();
							double sumPayMoney = Arith.roundEVEN(defUnitPrice.doubleValue()*useArea, 2);
			        		String sumPayMoneyStr = String.valueOf(sumPayMoney);
			        		
			        		deviceDetail.setUsageAmount(new BigDecimal(String.valueOf(useArea)));
							deviceDetail.setSumPayMoney(new BigDecimal(sumPayMoneyStr)); 
						}
//						//按实际应收
//						if("6".equals(feesMode)){
//							House h = deviceDb.getHouse(); 
//							String feesParams = fees.getRemarks();
//							double buildArea = h.getBuildArea()==null?0:h.getBuildArea().doubleValue();
//					        double rate = StringUtils.toDouble(feesParams);
//					        double useArea =  Arith.roundEVEN(buildArea*rate, 2);
//					   
//					        double sumPayMoney = Arith.roundEVEN(defUnitPrice.doubleValue()*useArea, 2);
//					        String sumPayMoneyStr = String.valueOf(sumPayMoney);
//					      
//					        deviceDetail.setUsageAmount(new BigDecimal(String.valueOf(useArea)));
//					        deviceDetail.setPayMoney(new BigDecimal(sumPayMoneyStr));				
//						}
						
						BeanValidators.validateWithException(validator, deviceDetail);
						deviceDetailService.save(deviceDetail);
					 }	
						
				}

	 		  addMessage(redirectAttributes, "保存单元信息'" + " " + "'成功");
	 		
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出"+ "" +"失败！失败信息："+e.getMessage());
		}
		


		String lasteDate = DateUtils.formatDate(payDate,"yyyy-MM-dd");
		redirectAttributes.addAttribute("device.fees.company.id", device.getFees().getCompany().getId());
		redirectAttributes.addAttribute("device.type", device.getType());
		redirectAttributes.addAttribute("device.fees.id", device.getFees().getId());
		redirectAttributes.addAttribute("lastDate",lasteDate);

		
		if(!"1".equals(device.getType())){
			House house =device.getHouse();
			redirectAttributes.addAttribute("device.house.unit.buildings.community.id", house.getUnit().getBuildings().getCommunity().getId());
			redirectAttributes.addAttribute("device.house.unit.buildings.id", house.getUnit().getBuildings().getId());
			redirectAttributes.addAttribute("device.house.unit.id", house.getUnit().getId());
		}
		


		return "redirect:"+Global.getAdminPath()+"/pms/deviceDetail/?repage";
	}
	
	@RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(DeviceDetail deviceDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Device device = deviceDetail.getDevice();
		String type = request.getParameter("type");
		String lastDate = request.getParameter("lastDate");

		String fileSuffix = isNewExcel?".xlsx":".xls";
		 String curDateStr =  DateUtils.getDate("yyyy年MM月");
		String proCompanyId =device.getFees().getCompany().getId();
		String feesId = device.getFees().getId();
		String deviceType =device.getType();

		Office proCompany = officeService.get(proCompanyId);
		Fees fees = FeesUtils.getFees(feesId);

		String proCompanyName =proCompany.getName();
		String deviceTypeName= DictUtils.getLable("pms_device_type", deviceType);
		String feesName = StringUtils.getNullValue(fees.getName(), "");
		BigDecimal defUnitPrice =fees.getUnitPrice();
		
		boolean isFromHistory = lastDate != null && !"".equals(lastDate);
		
		if(isFromHistory){
			deviceDetail.setLastDate(DateUtils.parseDate(lastDate));
		}else{
			deviceDetail.setLastDate(null);
		}

//		System.out.println(">>>>>>>>>>>>>>>>>>>>>feesId 11111>>>>>>>>>>>>>>>>>>>>>>"+ fees.getFeesMode());
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>feesId 22222>>>>>>>>>>>>>>>>>>>>>>"+ deviceTypeName);

		String titleMain = proCompanyName ;
		String titleSub = "收费项目("+curDateStr+deviceTypeName + feesName + ")";
		String title = titleMain + titleSub;
//		String fileName = titleSub + DateUtils.getDate("yyyyMMddHHmmss")+ fileSuffix; 
		feesName = StringUtils.isBlank(feesName)?"":"("+feesName+")";
		String fileName = curDateStr +deviceTypeName +feesName+ fileSuffix;
		
    	try {

    		
    		Map<String,DeviceDetail> mp = new HashMap<String,DeviceDetail>();
    		DeviceDetail dtParam = new DeviceDetail();
    		DeviceDetailUtils.getObjFromReq(dtParam,request,null,2);
    		deviceDetailService.findPage(new Page<DeviceDetail>(request,response,-1), dtParam,mp,"1");
    
//    		List<Device> ls = deviceService.findAll(device);
    		
            Page<Device> page = deviceService.findPage(new Page<Device>(request, response,-1), dtParam.getDevice());
            List<Device> ls = page.getList();
            
    		long end = System.currentTimeMillis();

    		for (Device dev : ls){
    			String id = dev.getId();
        		if(isFromHistory && mp.size()>0){
        			DeviceDetail dt = mp.get(id);

        			//已经导入过
        			if(dt != null){
        				//type 1、 导出模板   2、 导出历史
        				if("1".equals(type)){
	        				Date paymentDate = dt.getPaymentDate();
	            			double unitPrice = Double.valueOf(StringUtils.getNullValue(dt.getUnitPrice(), "0"));
	            			if(paymentDate == null){
	            				dev.setCurPaymentDateStr(DateUtils.parseDate(DateUtils.getDate()+" 00:00:00"));
	            			}else{
	            				dev.setCurPaymentDateStr(DateUtils.parseDate(DateUtils.formatDate(dt.getPaymentDate())));
	            			}

                			if(unitPrice == 0){	dev.setUnitPrice(defUnitPrice);}	
//                			dev.setCurDateStr(DateUtils.parseDate(DateUtils.getDate()+" 00:00:00"));
                			dev.setCurDateStr(DateUtils.getDate());
                			
                			
                			dev.setFirsDateStr(DateUtils.formatDate(dt.getLastDate()));
                			dev.setFirstNumStr(dt.getLastNum());
                			dev.setCurUsageAmountStr(null);
                			dev.setCurNumStr(null);
                			dev.setFeesParams(dev.getFeesParams());
                		
                			dev.setPayMoney(new BigDecimal(0));
                			dev.setPoolPayMoney(new BigDecimal(0));
                			dev.setSumPayMoney(new BigDecimal(0));
                			dev.setIncomeMoney(new BigDecimal(0));
                			if(dev.getParent() != null){dev.setPoolId(dev.getParent().getId());}
            			}else{    			
            				dev.setFeesMode(dt.getFeesMode());
            				dev.setFirsDateStr(DateUtils.formatDate(dt.getFirstDate()));
//            				dev.setCurDateStr(dt.getLastDate());
            				dev.setCurDateStr(DateUtils.formatDate(dt.getLastDate()));
            			
            				
            				
                			dev.setFirstNumStr(dt.getFirstNum());
                			dev.setCurNumStr(dt.getLastNum());
                			dev.setCurUsageAmountStr(dt.getUsageAmount());
                			dev.setUnitPrice(dt.getUnitPrice());
                			dev.setCurPoolUsageAmountStr(dt.getPoolUsageAmount());
                			dev.setParent(dt.getParent());
                			dev.setFeesParams(dt.getFeesParams());
                			if(dt.getParent() != null){dev.setPoolId(dt.getParent().getId());}
                			dev.setPoolPayMoney(StringUtils.toBigDecimal(dt.getPoolPayMoney()));
                			dev.setSumPayMoney(StringUtils.toBigDecimal(dt.getSumPayMoney()));
                			dev.setIncomeMoney(StringUtils.toBigDecimal(dt.getIncomeMoney()));
            			}

        			}
        			
        			
        		}else{
        			//from device
        			System.out.println(">>>>>>>>>>>>>>>>>>>>>exportFile use not isFromHistory  >>>>>>>>>>>>>>>>>>>>>>>>>");
        			System.out.println(">>>>>>>>>>>>>>>>>>>>>exportFile use not isFromHistory  fees.getFeesMode()>>>>>>>>>>>>>>>>>>>>>>>>>"+fees.getFeesMode());
        			System.out.println(">>>>>>>>>>>>>>>>>>>>>exportFile use not isFromHistory  fees.getName()>>>>>>>>>>>>>>>>>>>>>>>>>"+fees.getName());
        			System.out.println(">>>>>>>>>>>>>>>>>>>>>exportFile use not isFromHistory  fees.getRemarks()>>>>>>>>>>>>>>>>>>>>>>>>>"+fees.getRemarks());
        			
//        			dev.setFeesMode(fees.getFeesMode());
        			dev.setFirstNumStr(dev.getLastNum());
        			dev.setFirsDateStr(DateUtils.formatDate(dev.getLastDate()));
        			
        			
        			
        			Date paymentDate = dev.getPaymentDate();
//        			Fees fees = dev.getFees();
        			double unitPrice = Double.valueOf(StringUtils.getNullValue(dev.getUnitPrice(), "0"));
        			
        			if(paymentDate == null){
        				dev.setCurPaymentDateStr(DateUtils.parseDate(DateUtils.getDate()+" 00:00:00"));
        			}else{
        				dev.setCurPaymentDateStr(DateUtils.parseDate(DateUtils.formatDate(dev.getPaymentDate())));
        			}
        			if(unitPrice == 0){dev.setUnitPrice(defUnitPrice);}		
//        			dev.setCurDateStr(DateUtils.parseDate(DateUtils.getDate()+" 00:00:00")); java导出excel 日期格式为文本格式
        			dev.setCurDateStr(DateUtils.getDate());
        		
        			
        			dev.setFeesParams(fees.getRemarks());
        			if(dev.getParent() != null){dev.setPoolId(dev.getParent().getId());}
        		}

        		System.out.println(">>>>>>>>>>>>>>>>>>>>>lastDate9 getUnitPrice>>>>>>>>>>>>>>>>>>>>>>"+ dev.getUnitPrice());
        		System.out.println(">>>>>>>>>>>>>>>>>>>>>lastDate9 getPoolPayMoney>>>>>>>>>>>>>>>>>>>>>>"+ dev.getPoolPayMoney());
        		System.out.println(">>>>>>>>>>>>>>>>>>>>>lastDate9 getSumPayMoney>>>>>>>>>>>>>>>>>>>>>>"+ dev.getSumPayMoney());
        		System.out.println(">>>>>>>>>>>>>>>>>>>>>lastDate9 dev.getIncomeMoney()>>>>>>>>>>>>>>>>>>>>>>"+ dev.getIncomeMoney());
        		
        		if(dev.getUnitPrice().doubleValue() == 0) dev.setUnitPrice(null);
        		if(dev.getPoolPayMoney().doubleValue() == 0) dev.setPoolPayMoney(null);
        		if(dev.getSumPayMoney().doubleValue() == 0) dev.setSumPayMoney(null);
        		if(StringUtils.getNullValue(dev.getIncomeMoney(), "0") == "0") dev.setIncomeMoney(null);
    		
    		}

    		new ExportExcel(title, Device.class).setDataList(ls).write(response, fileName).dispose();
    		
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出"+ title +"失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pms/deviceDetail/?repage";
    }
	
	
	
	
//	@RequestMapping(value = "exportExcel", method=RequestMethod.POST)
//    public String exportFile2(DeviceDetail deviceDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
//		Device device = deviceDetail.getDevice();
//		String type = request.getParameter("type");
//		String lastDate = request.getParameter("lastDate");
//
//		String fileSuffix = isNewExcel?".xlsx":".xls";
//		 String curDateStr =  DateUtils.getDate("yyyy年MM月");
//	
//
//		String title = "收费项目("+curDateStr+")";
//	
//		String fileName = curDateStr + fileSuffix;
//		
//    	try {
////    		System.out.println(">>>>>>>>>>>>>>>>>>>>>exportFile 1111111111 1111111111 >>>>>>>>>>> id >>>>>>>>>>>>>>>>>>>>>>>>>>"+ ls.size());
//    		new ExportExcel(title, Device.class).setDataList(ls).write(response, fileName).dispose();
//    		
//    		return null;
//		} catch (Exception e) {
//			addMessage(redirectAttributes, "导出"+ title +"失败！失败信息："+e.getMessage());
//		}
//		return "redirect:"+Global.getAdminPath()+"/pms/deviceDetail/?repage";
//    }
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "exportExcel2",method=RequestMethod.POST)
	public String exportExcel2(String model,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {	
    
		Map<String, Object> map = Maps.newHashMap();
		StringBuffer sb = new StringBuffer();
		String houseId = request.getParameter("houseId");
		String officeId = request.getParameter("officeId");
		String feesId = request.getParameter("feesId");
		String firstDate = request.getParameter("firstDate");
		String lastDate = request.getParameter("lastDate");
		String isPay = request.getParameter("isPay");
		String type = request.getParameter("type");
		
		System.out.println("getDeviceDetailsJson>>>>>>>>>>>>>>>>>>>>>feesId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+feesId);
		System.out.println("getDeviceDetailsJson>>>>>>>>>>>>>>>>>>>>>type>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+type);
		System.out.println("getDeviceDetailsJson>>>>>>>>>>>>>>>>>>>>>isPay>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+isPay);
		System.out.println("getDeviceDetailsJson>>>>>>>>>>>>>>>>>>>>>officeId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+officeId);
		
//		PayemtDetail payemtDetail = new PayemtDetail();
		DeviceDetail deviceDetail = new DeviceDetail();
		PaymentAfter paymentAfter = new PaymentAfter();
		Office office = new Office();
		
		Device device = new Device();
		deviceDetail.setDevice(device);
		
		if(StringUtils.isNotEmpty(feesId)){
			device.setFees(new Fees(feesId));
		}
		
		if("1".equals(type)){
			deviceDetail.setOfficeId(officeId);
			office = officeService.get(officeId);
		}else{
			House house = new House(houseId);
			device.setHouse(house);
			paymentAfter.setHouse(house);
		}

		
		
		deviceDetail.setFirstDate(DateUtils.parseDate(firstDate));
		deviceDetail.setLastDate(DateUtils.parseDate(lastDate));
		if("0".equals(isPay)||"1".equals(isPay)||"2".equals(isPay)){
			deviceDetail.setIsPay(isPay);
		}
		

		List<DeviceDetail> ls = deviceDetailService.findDeviceDetails(deviceDetail);
		try {
			new ExportExcel("111111", Device.class).setDataList(ls).write(response, "222222222").dispose();

		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出"+ "title" +"失败！失败信息："+e.getMessage());
		}
		
	return "redirect:"+Global.getAdminPath()+"/pms/deviceDetail/?repage";
		
  }	
	
	

	
	
	@RequestMapping(value = "exportExcel", method=RequestMethod.POST)
    public String exportFile2(DeviceDetail deviceDetail, HttpServletRequest req, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Device device = deviceDetail.getDevice();
//		String type = req.getParameter("type");
//		String lastDate = req.getParameter("lastDate");
		
		String longinName = device.getHouse().getOwner().getLoginName();

		String userName = "";
		if (StringUtils.isNotEmpty(longinName)){
			User u = UserUtils.getUserByLoginName(longinName);
			userName = u.getName();
		}
	
		
		
		String proCompanyId = device.getFees().getCompany().getId();
		String feesId = device.getFees().getId();
		String deviceType =device.getType();

		Office proCompany = officeService.get(proCompanyId);
		Fees fees = FeesUtils.getFees(feesId);
		String feesName = fees.getName();

		String proCompanyName =proCompany.getName();
		
		String firstDate = req.getParameter("firstDate");
		String lastDate = req.getParameter("lastDate");

		feesName = StringUtils.isBlank(feesName)?"":"{"+feesName+"}";
		String fileSuffix = isNewExcel?".xlsx":".xls";
//		String curDateStr =  DateUtils.getDate("yyyy年MM月");
		
		userName = StringUtils.isBlank(userName)?"":" "+userName+" ";
	
		String title = proCompanyName +feesName+"("+firstDate +"至"+lastDate+")";
		String fileName = firstDate +lastDate +feesName+ fileSuffix;
		
    	try {

    		DeviceDetail dtParam = new DeviceDetail();
    		Page<DeviceDetail> page = new Page<DeviceDetail>(req, response,-1);
    		DeviceDetailUtils.getObjFromReq(dtParam,req,null,2);
    		
    		dtParam.getDevice().setType("3");
    		deviceDetailService.findPage(page, dtParam,null,"1");
    		List<DeviceDetail> lsDt = page.getList();
    		List<Device> ls = new ArrayList<Device>();
    		   
    		for(DeviceDetail dt:lsDt){
    			Device dev = deviceService.get(dt.getDevice().getId());
    			dev.setFeesMode(dt.getFeesMode());
    			dev.setFeesParams(dt.getFeesParams());
    			
    			dev.setFirsDateStr(DateUtils.formatDate(dt.getFirstDate()));
//    			dev.setCurDateStr(dt.getLastDate());
    			dev.setCurDateStr(DateUtils.formatDate(dt.getLastDate()));
    			
    			
    			dev.setFirstNumStr(dt.getFirstNum());
    			dev.setCurNumStr(dt.getLastNum());
    			dev.setCurUsageAmountStr(dt.getUsageAmount());
    			dev.setUnitPrice(dt.getUnitPrice());
    			dev.setCurPoolUsageAmountStr(dt.getPoolUsageAmount());
    			dev.setParent(dt.getParent());
    		
    			if(dt.getParent() != null){dev.setPoolId(dt.getParent().getId());}
    			dev.setPoolPayMoney(StringUtils.toBigDecimal(dt.getPoolPayMoney()));
    			dev.setSumPayMoney(StringUtils.toBigDecimal(dt.getSumPayMoney()));
    			dev.setIncomeMoney(StringUtils.toBigDecimal(dt.getIncomeMoney()));	
    			
    			ls.add(dev);
    		}
    		new ExportExcel(title, Device.class).setDataList(ls).write(response, fileName).dispose();
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出"+ title +"失败！失败信息："+e.getMessage());
		}
    	
    	return null ;

//    	return "redirect:"+Global.getAdminPath()+"/pms/deviceDetail/?repage";
    }	
	
	
	
	
	
	
	
	
	

	
	@RequestMapping(value = "import", method=RequestMethod.POST)
//  public String importFile(MultipartFile file,Device device, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
  	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {

		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/pms/deviceDetail/?repage";
		}

		  
		 System.out.println("333333333333333333333333333333 77777777777777777777 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>file"+file);
		 List<DeviceDetail> lsDetail = new ArrayList<DeviceDetail>();
		 List<DeviceDetail> lsDetailPool = new ArrayList<DeviceDetail>();
		 List<DeviceDetail> lsDetailAll = new ArrayList<DeviceDetail>();
		String lastDate ="";
		String poolId ="";
		Fees fs = null;
		Community community = null;
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1,0);
			List<Device> list = ei.getDataList(Device.class);
			
			
			System.out.println(" list.size()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+list.size());
			
			for (Device dev : list){
				try{
					String id = dev.getId();

					if(StringUtils.isNotEmpty(id)){
                      
						Device deviceDb = deviceService.get(id);
						
//						System.out.println(">>>>>>>>>>>>>>deviceDb 33333333333 5555555555 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ deviceDb);
//						System.out.println(">>>>>>>>>>>>>>deviceDb.getFees 33333333333 5555555555 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ deviceDb.getFees());
						
						if(deviceDb != null){
							
//							System.out.println(">>>>>>>>>>>>>>ttttttttttttt 33333333333 deviceDb.getLastDate() >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ dev.getLastDate());
							System.out.println(">>>>>>>>>>>>>>ttttttttttttt 33333333333 deviceDb.getCurDateStr() >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ dev.getCurDateStr());
							
							deviceDb.setFirstNum(StringUtils.toBigDecimal(dev.getFirstNumStr()));
							deviceDb.setLastNum(StringUtils.toBigDecimal(dev.getCurNumStr()));
							deviceDb.setFirstDate(DateUtils.parseDate(dev.getFirsDateStr()));
//							deviceDb.setLastDate(dev.getCurDateStr());
//							deviceDb.setPaymentDate(dev.getCurDateStr());
							
							
							
							deviceDb.setLastDate(DateUtils.parseDate(dev.getCurDateStr()));
							deviceDb.setPaymentDate(DateUtils.parseDate(dev.getCurDateStr()));
							
							
							
							
							deviceDb.setUnitPrice(StringUtils.toBigDecimal(dev.getUnitPrice()));
							deviceDb.setFeesMode(dev.getFeesMode());
							deviceDb.setFeesParams(dev.getFeesParams());
							deviceDb.setUsageAmount(StringUtils.toBigDecimal(dev.getUsageAmount()));
							deviceDb.setPoolUsageAmount(StringUtils.toBigDecimal(dev.getPoolUsageAmount()));
							deviceDb.setPoolPayMoney(StringUtils.toBigDecimal(dev.getPoolPayMoney()));
							deviceDb.setSumUsageAmount(StringUtils.toBigDecimal(dev.getSumUsageAmount()));
							deviceDb.setSumPayMoney(StringUtils.toBigDecimal(dev.getSumPayMoney()));
							
							double firstNum = Double.parseDouble(StringUtils.getNullValue(dev.getFirstNumStr(), "0"));
							double curNum = Double.parseDouble(StringUtils.getNullValue(dev.getCurNumStr(), "0"));
							
//							System.out.println(">>>>>>>>>>>>>>yyyyyyyyyyyyyyyy 33333333333 5555555555 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ fs);
								
							if(curNum>0 && curNum < firstNum){
								failureMsg.append("<br/>序号为（"+id+"）有误 导入失败,当前读不允许小于上次读数，请检查导入模板。");
							}

							if(dev.getPoolId() !=null&& StringUtils.isNotBlank(dev.getPoolId())){
								Device parent =new Device(dev.getPoolId());
								deviceDb.setParent(parent);
								if(dev.getPoolId() != null){
									poolId = dev.getPoolId();
								}
							}else{
								Device parent = new Device("0");
								deviceDb.setParent(parent);
							}
//							System.out.println(">>>>>>>>>>>>>>ttttttttttttt 33333333333 5555555555 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ fs);
//							System.out.println(">>>>>>>>>>>>>>ttttttttttttt 33333333333 deviceDb.getLastDate() >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ deviceDb.getLastDate());
							if(StringUtils.isBlank(lastDate)){
								lastDate = DateUtils.formatDate(deviceDb.getLastDate(),"yyyy-MM-dd");
							}
							
//							System.out.println(">>>>>>>>>>>>>>qqqqqqqqqqqqqq 33333333333 5555555555 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ fs);
							
							if(fs == null){
								fs = deviceDb.getFees();
//								System.out.println(">>>>>>>>>>>>>>wwwwwwwwwwww 33333333333 5555555555 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ fs);
								
							}
//							System.out.println(">>>>>>>>>>>>>>deviceDb.getFees 33333333333 5555555555 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ deviceDb.getFees());
							

							try{
								BeanValidators.validateWithException(validator, deviceDb);
//								deviceService.saveDetail(deviceDb);
								
								DeviceDetail deviceDetail = new DeviceDetail();
								deviceDetail.setDevice(deviceDb);

								//查找 deviceDetail 里是否存在
								deviceDetail.setLastDate(deviceDb.getLastDate());
								List<DeviceDetail> ls = deviceDetailService.find(deviceDetail);
//								System.out.println(">>>>>>>>>>>>>>>+ls.size() 33 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>3>>>>>>>>>"+ls.size());
//								System.out.println(" saveDetail>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>33333333333");
								
								if(ls.size() >0){
									DeviceDetail dtail = ls.get(0);
									deviceDetail.setId(dtail.getId());
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
								deviceDetail.setUnitPrice(deviceDb.getUnitPrice());
								deviceDetail.setPayMoney(deviceDb.getPayMoney());
								deviceDetail.setPoolPayMoney(deviceDb.getPoolPayMoney());
								deviceDetail.setSumUsageAmount(deviceDb.getSumUsageAmount());
								deviceDetail.setSumPayMoney(deviceDb.getSumPayMoney());
								deviceDetail.setFeesMode(deviceDb.getFeesMode());
								deviceDetail.setFeesParams(deviceDb.getFeesParams());

								deviceDetail.setParent(deviceDb.getParent());
								System.out.println(" saveDetail>>>>>>>>>>>>>>>>>>>1111111111111111111>>>>>>>>>>>>>>>>>deviceDb.getPayMoney()>>>>>>>>>>"+deviceDb.getPayMoney());
								BeanValidators.validateWithException(validator, deviceDetail);
//								System.out.println(" saveDetail>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>555555555555");
								deviceDetailService.save(deviceDetail);
								System.out.println(" saveDetail>>>>>>>>>>>>>>>>>>>>>777777777777777>>>>>>>>>>>>>>>>>>>>>>>>>>deviceDetail.getSumPayMoney()>>>>>>>>>>>>>"+deviceDetail.getSumPayMoney());
								boolean isPoolDevice = !"0".equals(deviceDb.getParent().getId());
								if(isPoolDevice){
									lsDetailPool.add(deviceDetail);
								}else{
									lsDetail.add(deviceDetail);
								}
							

								successNum++;
								
							}catch(ConstraintViolationException ex){
								failureMsg.append("<br/>序号为（"+id+"） 导入失败：");
								List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
								for (String message : messageList){
									failureMsg.append(message+"; ");
									failureNum++;
								}
								
//								System.out.println("deviceService.saveDetail ConstraintViolationException>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ex.getMessage());
								
							}catch (Exception ex) {
								failureMsg.append("<br/>序号为（"+id+"） 导入失败："+ex.getMessage());
								failureNum++;
//								System.out.println("deviceService.saveDetail Exception>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ex.getMessage());
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
		
		
		//计算正常费用
		lsDetailAll.addAll(lsDetailPool);
		lsDetailAll.addAll(lsDetail);
		if(lsDetailAll.size()>0){ 
			DeviceDetailUtils.resetList(lsDetailAll);
		}
		
		//查找公摊设备 通过日期和 poolID查找到 deviceDetail 里的的 detail id,修改 deviceDetail 里的parent id

		if(poolId != null && !"0".equals(poolId)){
			DeviceDetail deviceDetail = new DeviceDetail();
			Device device = new Device(poolId);
			
			System.out.println(">>>>>>>>>>>>>>fs 33333333333 5555555555 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ fs);
			
			device.setFees(fs);
			deviceDetail.setDevice(device);
			deviceDetail.setLastDate(DateUtils.parseDate(lastDate));
			List<DeviceDetail> ls =  deviceDetailService.find2(deviceDetail);
			
			System.out.println(">>>>>>>>>>>>>>>+ls.size() 44 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>3>>>"+ls.size());
			System.out.println(">>>>>>>>>>>>>>>+ls.size() 44 poolId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>3>>>"+poolId);
			
			if(ls.size() !=1){
				addMessage(redirectAttributes, "没有找到公摊信息，请确认〈" + lastDate+"〉是否存在公摊费用");
			}else{
//				System.out.println(" resetList 1 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>555555555555");
				DeviceDetail deviceDetailPool = ls.get(0);
//				System.out.println(" resetList 2 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>555555555555");
				//计算公摊费用，只有个人才需要计算公摊
//				System.out.println(" resetList 3>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>555555555555" +lsDetail.size());
				
				if(lsDetailPool.size()>0){ 
//					System.out.println(" resetList 4>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>555555555555");
					DeviceDetailUtils.resetList(deviceDetailPool,lsDetailPool);
				}
			}
		}

		
		for (DeviceDetail dt : lsDetailAll) {
//			System.out.println(" saveDetail>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>555555555555");
			deviceDetailService.save(dt);
//			System.out.println(" saveDetail>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>6666666666");
		}
		

		redirectAttributes.addAttribute("lastDate", lastDate);
		return "redirect:"+Global.getAdminPath()+"/pms/deviceDetail/?repage";
//		return null;
  }
	
	
	@RequestMapping(value = "index")
	public String index() {
		return "modules/pms/payIndex";
	}
	
	@RequestMapping(value = "tree")
	public String tree(Model model,HttpServletRequest request, HttpServletResponse response) {
		return "modules/pms/payTree";
	}
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(Model model,HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		String id = request.getParameter("id");
		String relid = request.getParameter("relid");
		String name = request.getParameter("name");
		String level = request.getParameter("level");
		String proCompanyId = request.getParameter("proCompanyId");
		String type = request.getParameter("type");

		
		String ctx =  "";
		try {
			 ctx =  ServletContextFactory.getServletContext().getContextPath();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		System.out.println("getAdminPath>>"+ctx);
		
		if("1".equals(type)){
			
			if(StringUtils.isEmpty(level)){
				Office office = new Office();
				office.setParent( new Office(proCompanyId));
				List<Office> ls = officeService.findOffice(office);
				
				for(Office c:ls){
					
					String idd = c.getSort();
					if(!"0".equals(idd)){
						Map<String, Object> map = Maps.newHashMap();
						map.put("id","company"+c.getId());
						map.put("pId",0);
						map.put("level",0);
						map.put("name", c.getName());	
						map.put("isParent", true);
						map.put("open", true);	
						map.put("target", "cmsMainFrame");	
//						System.out.println("ctx>>>>>>>>>>>>>>>"+ ctx);  
						
						map.put("url", ctx +Global.getAdminPath()+"/pms/deviceDetail/form2?officeId="+c.getId()+"&device.fees.company.id="+proCompanyId+"&device.type=3");
						mapList.add(map);
					}

				}
			}

			
			
			if("0".equals(level)){
//				buildingsService StringUtils
				House house = new House();
				String companyId = StringUtils.remove(id, "company");
				Office company = new Office(companyId);
				User u = new User();
				u.setCompany(company);
				house.setOwner(u);
				
				List<House> ls2 = houseService.findAllHouse(house);
				for(House h:ls2){
					Map<String, Object> map = Maps.newHashMap();
					User owner = h.getOwner();
					String userType = owner.getUserType();
					String names = "";
					String deviceType = "3";
					if("3".equals(userType)){
						names = owner.getCompany().getName();
						deviceType = "2";
					}else{
//						names = h.getNumFloor()+"层  ("+owner.getName()+")";
						names ="("+owner.getName()+") " + h.getFullName();
						
						map.put("id","House"+h.getId());
						map.put("pId",id);
						map.put("level",1);
						map.put("name", names);	
						map.put("target", "cmsMainFrame");	
						map.put("url",ctx +Global.getAdminPath()+"/pms/deviceDetail/form3?device.house.id="+h.getId()+"&officeId="+owner.getCompany().getId()+"&device.fees.company.id="+proCompanyId+"&device.type="+deviceType);
//						map.put("value",h.getOwner().getLoginName());
						mapList.add(map);
						
					}

//					System.out.println("h.getOwner().getLoginName()>>>>>>>>>>>>>>>"+ h.getOwner().getLoginName());  
				}
			}	
		}else{
			
			if(StringUtils.isEmpty(level)){

				List<Community> ls = communityService.findAllCommunityByProCompanyId(proCompanyId);
				
				for(Community c:ls){
					Map<String, Object> map = Maps.newHashMap();
					map.put("id","Community"+c.getId());
					map.put("pId",0);
					map.put("level",0);
					map.put("name", c.getName());	
					map.put("isParent", true);
					map.put("open", true);	
					mapList.add(map);
				}
				
				
				
			}
		
//		 { id:-1, pId:0,level:-1, name:"parentNode 1", isParent:true, open:false}
		
		if("0".equals(level)){
//			buildingsService StringUtils
			Buildings buildings = new Buildings();
			String communityId = StringUtils.remove(id, "Community");
			buildings.setCommunity(new Community(communityId));
			List<Buildings> ls = buildingsService.findAllBuildings(buildings);
			for(Buildings b:ls){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id","Buildings"+b.getId());
				map.put("pId",id);
				map.put("level",1);
				map.put("name", b.getName());	
				map.put("isParent", true);
//				map.put("url","../payemtDetail/form?house.id=1");
//				map.put("target", "mainFrame");	
				mapList.add(map);
			}
		}
		
		
		if("1".equals(level)){
//			buildingsService StringUtils
			Unit unit = new Unit();
			String buildingsId = StringUtils.remove(id, "Buildings");
			unit.setBuildings(new Buildings(buildingsId));
			List<Unit> ls = unitService.findAllUnit(unit);
			for(Unit u:ls){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id","Unit"+u.getId());
				map.put("pId",id);
				map.put("level",2);
				map.put("name", u.getName());	
				map.put("isParent", true);
				mapList.add(map);
			}
		}		
		
		
		if("2".equals(level)){
//			buildingsService StringUtils
			House house = new House();
			String unitId = StringUtils.remove(id, "Unit");
			house.setUnit(new Unit(unitId));
			List<House> ls = houseService.findAllHouse(house);
			for(House h:ls){
				Map<String, Object> map = Maps.newHashMap();
				User owner = h.getOwner();
				String userType = owner.getUserType();
				String names = "";
				String deviceType = "3";
				
				if("3".equals(userType)){
					names = owner.getCompany().getName();
					deviceType = "2";
				}else{
					names = h.getNumFloor()+"层  ("+owner.getName()+")";
				}
				
				
				
				map.put("id","House"+h.getId());
				map.put("pId",id);
				map.put("level",3);
				map.put("name", names);	
				map.put("target", "cmsMainFrame");	
				map.put("url",ctx+Global.getAdminPath()+"/pms/deviceDetail/form3?device.house.id="+h.getId()+"&officeId="+owner.getCompany().getId()+"&device.fees.company.id="+proCompanyId+"&device.type="+deviceType);

				mapList.add(map);
			}
		}	

		
//		model.addAttribute("categoryList", categoryService.findByUser(true, null));
		}
		
		return mapList;
//		return "modules/pms/payTree";
	}
	

	@RequestMapping(value = "none")
	public String none() {
		return "modules/pms/payNone";
	}
	
	@ResponseBody
	@RequestMapping(value = "getDeviceDetailsJson")
	public Map<String, Object> getDeviceDetailsJson(String model,HttpServletRequest request, HttpServletResponse response) {	
    
		Map<String, Object> map = Maps.newHashMap();
		StringBuffer sb = new StringBuffer();
		String houseId = request.getParameter("device.house.id");
		String officeId = request.getParameter("device.house.owner.company.id");
		String longinName = request.getParameter("device.house.owner.longinName");
		String feesId = request.getParameter("device.fees.id");
		String deviceType = request.getParameter("device.type");
		String firstDate = request.getParameter("firstDate");
		String lastDate = request.getParameter("lastDate");
		String isPay = request.getParameter("isPay");
		String type = request.getParameter("type");
		String paymentBeforId = request.getParameter("paymentBeforId");

//		if(StringUtils.isBlank(deviceType)||"null".equals(deviceType)){
//			return map;
//		}

//		System.out.println("getDeviceDetailsJson>>>>>>>>>>>>>>>>>>>>>houseId 6666 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+houseId);
//		System.out.println("getDeviceDetailsJson>>>>>>>>>>>>>>>>>>>>>officeId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+officeId);
//		System.out.println("getDeviceDetailsJson>>>>>>>>>>>>>>>>>>>>>loginName>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+longinName);
//		System.out.println("getDeviceDetailsJson>>>>>>>>>>>>>>>>>>>>>feesId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+feesId);
//		System.out.println("getDeviceDetailsJson>>>>>>>>>>>>>>>>>>>>>firstDate>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+firstDate);
//		System.out.println("getDeviceDetailsJson>>>>>>>>>>>>>>>>>>>>>lastDate>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+lastDate);
//		System.out.println("getDeviceDetailsJson>>>>>>>>>>>>>>>>>>>>>isPay>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+isPay);
//		System.out.println("getDeviceDetailsJson>>>>>>>>>>>>>>>>>>>>>type>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+type);
//		System.out.println("getDeviceDetailsJson>>>>>>>>>>>>>>>>>>>>>deviceType>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+deviceType);
//		System.out.println("getDeviceDetailsJson>>>>>>>>>>>>>>>>>>>>>paymentBeforId 6666 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+paymentBeforId);

		DeviceDetail deviceDetail = new DeviceDetail();
		PaymentAfter paymentAfter = new PaymentAfter();
		PaymentBefor paymentBefor = new PaymentBefor();
		
		Office office = new Office();
		
		Device device = new Device();
		deviceDetail.setDevice(device);
		
		if(StringUtils.isNotEmpty(feesId)){
			device.setFees(new Fees(feesId));
		}
		
		if("1".equals(type)){
			deviceDetail.setOfficeId(officeId);
			office = officeService.get(officeId);
		}else{
			House house = new House(houseId);
			device.setHouse(house);
			paymentAfter.setHouse(house);
			paymentBefor.setHouse(house);
		}

		
		
		deviceDetail.setFirstDate(DateUtils.parseDate(firstDate));
		deviceDetail.setLastDate(DateUtils.parseDate(lastDate));
		if("0".equals(isPay)||"1".equals(isPay)||"2".equals(isPay)){
			deviceDetail.setIsPay(isPay);
		}
		
		if(device.getHouse() == null){
			device.setHouse(new House("0"));
		}
		
		User user = new User();
		user.setLoginName(longinName);
		device.getHouse().setOwner(user);

		DeviceDetail dtParam = new DeviceDetail();
		Page<DeviceDetail> page = new Page<DeviceDetail>(request, response,-1);
		DeviceDetailUtils.getObjFromReq(dtParam,request,null,2);
		
		dtParam.getDevice().setType(deviceType);
		deviceDetailService.findPage(page, dtParam,null,"1");
		List<DeviceDetail> ls = page.getList();
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>        vvvvvvvvvvvvvvvvvvvvv >>>>>>>>>>>>>>>>>>>>>>>>>>"+ls.size());

		sb.delete(0,sb.length());
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<rows>");  

//		"房间,表编号,业主,上次日期,上次读数,本次读数,使用量";
		int i=0;
		double incomeMoneySum = 0;
		double costMoney = 0;
		double preMoney = 0;
		Map mpOwnerNum = new HashMap();
		
		if("2".equals(type)){
			paymentBefor.setType("2");  //预付款
			PaymentBefor pBefor = paymentBeforService.find(paymentBefor);
			preMoney = Double.parseDouble(StringUtils.getNullValue(pBefor.getRecMoney(), "0"));
		}

		
		for(DeviceDetail dt:ls){
			Device dev = dt.getDevice();
			House h = dev.getHouse();
			String fullName = "";
			String userName = "";
			
//			System.out.println(">>>>>>>>>>>>>>>>>>>>>getdevicesJson>>>>>>>>>>> d.getHouse() >>>>>>>>>>>>>>>>>>>>>>>>>>"+dev.getHouse());

//			,费项名称,单价,上次读数,本次读数,实际用量,本次应付,付款期限
			double payMoney = Arith.roundEVEN(StringUtils.toDouble(dt.getPayMoney()).doubleValue(),2); 
			double sumPayMoney = Arith.roundEVEN(StringUtils.toDouble(dt.getSumPayMoney()).doubleValue(),2); 
			double incomeMoney = Arith.roundEVEN(Double.parseDouble(StringUtils.getNullValue(dt.getIncomeMoney() , "0")),2);
			incomeMoneySum +=incomeMoney;
			
			costMoney = costMoney+sumPayMoney; 
			
			sb.append("<row  id=\""+dt.getId()  +"\">");
			sb.append("<cell></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(dev.getFees().getName(), "") +"]]></cell>");
			
			if("1".equals(type)){
				sb.append("<cell><![CDATA["+ StringUtils.getNullValue(dev.getHouse().getFullName(), "")  +"]]></cell>");
				sb.append("<cell><![CDATA["+ StringUtils.getNullValue(dev.getHouse().getOwner().getName(), "")  +"]]></cell>");
				mpOwnerNum.put(dev.getHouse().getId(), 0);
			}

			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(dt.getFirstNum(), "")  +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(dt.getLastNum() , "") +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(dt.getUsageAmount(), "")  +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(Arith.roundEVEN(dt.getUnitPrice().doubleValue(),4), "")  +"]]></cell>");
//			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(dt.getPoolUsageAmount() , "") +"]]></cell>");
//			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(Arith.roundEVEN(dt.getSumUsageAmount().doubleValue(),2), "")  +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(payMoney , "0")  +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(dt.getPoolPayMoney() , "") +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(sumPayMoney , "0")  +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(incomeMoney , "0")  +"]]></cell>");
			
			sb.append("<cell>"+ StringUtils.getNullValue(DateUtils.formatDate(dt.getLastDate(), "yyyy-MM-dd"),"" )+"</cell>");
			
			sb.append("<userdata name=\"costMoney\">"+ sumPayMoney +"</userdata>");
			sb.append("<userdata name=\"incomeMoney\">"+ incomeMoney +"</userdata>");
			sb.append("<userdata name=\"houseId\">"+ dev.getHouse().getId() +"</userdata>");
			sb.append("<userdata name=\"feesId\">"+dev.getFees().getId() +"</userdata>");
			
			sb.append("</row>");
		 }
		
		
		sb.append("</rows>");
		
		costMoney = costMoney -incomeMoneySum;
		
		map.put("grid", sb.toString());
		map.put("costMoney", Arith.roundEVEN(costMoney,2));
		map.put("preMoney", Arith.roundEVEN(preMoney,2));
		map.put("officeName", office.getName());
		map.put("ownerNum", mpOwnerNum.size());
		
		//查找发票信息
//		map.put("paymentBefor", new PaymentBefor());
		if(StringUtils.isNotBlank(paymentBeforId)){
			PaymentBefor pBefor = paymentBeforService.getWithPay(paymentBeforId);
			map.put("feeCode", pBefor.getFeeCode());
			map.put("costMoneyLeave", pBefor.getCostMoneyLeave());
		}
	
		return map;
		
  }
	
	@ResponseBody
	@RequestMapping(value = "getDeviceDetailsJsonReport")
	public Map<String, Object> getDeviceDetailsJsonReport(String model,HttpServletRequest request, HttpServletResponse response) {	
		Map<String, Object> map = Maps.newHashMap();
		StringBuffer sb = new StringBuffer();

		String proCompanyId = request.getParameter("device.fees.company.id");
		String houseId = request.getParameter("device.house.id");
		String officeId = request.getParameter("device.house.owner.company.id");
		String longinName = request.getParameter("device.house.owner.longinName");
		String feesId = request.getParameter("device.fees.id");
		String firstDate = request.getParameter("firstDate");
		String lastDate = request.getParameter("lastDate");
		String isPay = request.getParameter("isPay");
		String type = request.getParameter("type");
		String paymentBeforId = request.getParameter("paymentBeforId");



		
		
		
		//get title
		Office proCompany = officeService.get(proCompanyId);
		String feesName = "";
		if(StringUtils.isNotEmpty(feesId)){
			Fees fees = FeesUtils.getFees(feesId);
			feesName = fees.getName();
			feesName = StringUtils.isBlank(feesName)?"":"{"+feesName+"}";
		}

		String proCompanyName =proCompany.getName();
		String title = proCompanyName +feesName+"("+firstDate +"至"+lastDate+")";
		

		DeviceDetail deviceDetail = new DeviceDetail();
		PaymentAfter paymentAfter = new PaymentAfter();
		Office office = new Office();
		
		Device device = new Device();
		deviceDetail.setDevice(device);
		
		if(StringUtils.isNotEmpty(feesId)){
			device.setFees(new Fees(feesId));
		}
		
		if("1".equals(type)){
			if (StringUtils.isNotEmpty(officeId)){
				deviceDetail.setOfficeId(officeId);
				office = officeService.get(officeId);	
			}

		}else{
			
			if (StringUtils.isNotEmpty(houseId)){
				House house = new House(houseId);
				device.setHouse(house);
				paymentAfter.setHouse(house);
			}
		
		}

		
		
		deviceDetail.setFirstDate(DateUtils.parseDate(firstDate));
		deviceDetail.setLastDate(DateUtils.parseDate(lastDate));
		if("0".equals(isPay)||"1".equals(isPay)||"2".equals(isPay)){
			deviceDetail.setIsPay(isPay);
		}
		
		if(device.getHouse() == null){
			device.setHouse(new House("0"));
		}
		
		User user = new User();
		user.setLoginName(longinName);
		device.getHouse().setOwner(user);

		DeviceDetail dtParam = new DeviceDetail();
		Page<DeviceDetail> page = new Page<DeviceDetail>(request, response,-1);
		DeviceDetailUtils.getObjFromReq(dtParam,request,null,2);
		
		dtParam.getDevice().setType("3");
		deviceDetailService.findPage(page, dtParam,null,"1");
		List<DeviceDetail> ls = page.getList();
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>        vvvvvvvvvvvvvvvvvvvvv >>>>>>>>>>>>>>>>>>>>>>>>>>"+ls.size());

		sb.delete(0,sb.length());
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<rows>");  

//		"房间,表编号,业主,上次日期,上次读数,本次读数,使用量";
		int i=0;
		double incomeMoneySum = 0;
		double costMoney = 0;
		double preMoney = 0;
		Map mpOwnerNum = new HashMap();
		

		 List<Map<String, String>> lsDetailAll = new ArrayList<Map<String, String>>();
		
		for(DeviceDetail dt:ls){
			
			Map<String, String> mapOne = Maps.newHashMap();

			Device dev = dt.getDevice();
			House h = dev.getHouse();

//			,费项名称,单价,上次读数,本次读数,实际用量,本次应付,付款期限
			double payMoney = Arith.roundEVEN(StringUtils.toDouble(dt.getPayMoney()).doubleValue(),2); 
			double sumPayMoney = Arith.roundEVEN(StringUtils.toDouble(dt.getSumPayMoney()).doubleValue(),2); 
			double incomeMoney = Arith.roundEVEN(Double.parseDouble(StringUtils.getNullValue(dt.getIncomeMoney() , "0")),2);
			incomeMoneySum +=incomeMoney;
			
			costMoney = costMoney+sumPayMoney; 
			
			mapOne.put("feesName",  StringUtils.getNullValue(dev.getFees().getName(), ""));
			
			if("1".equals(type)){
				sb.append("<cell><![CDATA["+ StringUtils.getNullValue(dev.getHouse().getFullName(), "")  +"]]></cell>");
				sb.append("<cell><![CDATA["+ StringUtils.getNullValue(dev.getHouse().getOwner().getName(), "")  +"]]></cell>");
				mpOwnerNum.put(dev.getHouse().getId(), 0);
				
				mapOne.put("houseFullName",  StringUtils.getNullValue(dev.getHouse().getFullName(), ""));
				mapOne.put("ownerName",  StringUtils.getNullValue(dev.getHouse().getOwner().getName(), ""));
				
			}

			mapOne.put("firstNum",   StringUtils.getNullValue(dt.getFirstNum(), ""));
			mapOne.put("LastNum",   StringUtils.getNullValue(dt.getLastNum(), ""));
			mapOne.put("usageAmount",   StringUtils.getNullValue(dt.getUsageAmount(), ""));
			mapOne.put("unitPrice",   StringUtils.getNullValue(Arith.roundEVEN(dt.getUnitPrice().doubleValue(),4), ""));
			mapOne.put("payMoney",    StringUtils.getNullValue(payMoney , "0"));
			mapOne.put("poolPayMoney",   StringUtils.getNullValue(dt.getPoolPayMoney() , ""));
			mapOne.put("sumPayMoney",   StringUtils.getNullValue(sumPayMoney, "0"));
			mapOne.put("incomeMoney",   StringUtils.getNullValue(incomeMoney, "0"));
//			mapOne.put("lastDate",   StringUtils.getNullValue(DateUtils.formatDate(dt.getLastDate(), "yyyy-MM-dd"),""));
			mapOne.put("lastDate",   StringUtils.getNullValue(DateUtils.formatDate(dt.getLastDate(), "yyyy年MM月"),""));
			mapOne.put("officeName",   StringUtils.getNullValue(dev.getHouse().getOwner().getCompany().getName(), ""));

			lsDetailAll.add(mapOne);
			
		 }
		
		
	
		
		costMoney = costMoney -incomeMoneySum;
		
		map.put("Detail", lsDetailAll);
		
		Map<String, String> Master = Maps.newHashMap();
		Master.put("title",title);
		
		if(dtParam.getDevice().getFees().getFeesIdList().size() == 0){
			Master.put("company",office.getName());
			Master.put("officeName", office.getName());
		}

		Master.put("costMoney", Arith.roundEVEN(costMoney,2)+"");
		Master.put("preMoney", Arith.roundEVEN(preMoney,2)+"");
		Master.put("ownerNum", mpOwnerNum.size()+"");

		
		map.put("Master", Master);
		
		return map;
		
	}	
	
	
	@ResponseBody
	@RequestMapping(value = "getDetailJsonReportOffice")
	public Map<String, Object> getDetailJsonReportOffice(String details,HttpServletRequest request, HttpServletResponse response) {	
		Map<String, Object> map = Maps.newHashMap();
		StringBuffer sb = new StringBuffer();
		
//		 System.out.println("details>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 3333333333333333" + details); 
		 
		 String json = StringUtils.replace(details,"&quot;" ,  "'");  
		 
		 System.out.println("json>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 3333333333333333" + json); 
		 
		 JSONObject jo = new JSONObject(json);
		 String officeId = String.valueOf(jo.get("officeId"));
	     JSONArray detailsJson= jo.getJSONArray("details");

	     List<DeviceDetail> lsAll = new ArrayList<DeviceDetail>();
	     for(int i = 0;i<detailsJson.length();i++){
	    	 JSONObject obj = detailsJson.getJSONObject(i);
	    	 String fid = String.valueOf(obj.get("fid"));
	    	 String firstDate = String.valueOf(obj.get("firstDate"));
	    	 String lastDate = String.valueOf(obj.get("lastDate"));
//	    	 System.out.println(">>>>>>>>>>>>>>>>>>>>"+obj.get("firstDate"));
	    	 Map<String, Object> params = new HashMap();
	    	 params.put("officeId", officeId);
	    	 params.put("fid", fid);
	    	 params.put("firstDate", firstDate);
	    	 params.put("lastDate", lastDate);
	    	 List<DeviceDetail> ls =  deviceDetailService.myBatisFind(params);
	    	 lsAll.addAll(ls);
	     }
	     
	     
	     Map<String, String> Master = Maps.newHashMap();
	     List<Map<String, String>> lsDetailAll = new ArrayList<Map<String, String>>();
	     
		for(DeviceDetail dt:lsAll){
			Device dev = dt.getDevice();
			House house = dev.getHouse();
			Fees fees = dev.getFees();
			User owner = house.getOwner();
			Office company = owner.getCompany();
			
			Map<String, String> mapOne = Maps.newHashMap();
			mapOne.put("user_login_name",   StringUtils.getNullValue(owner.getLoginName(), ""));
			mapOne.put("company_code",   StringUtils.getNullValue(company.getCode(), ""));
			mapOne.put("company_name",   StringUtils.getNullValue(company.getName(), ""));
			mapOne.put("house_pos_name",   StringUtils.getNullValue(house.getName(), ""));
			mapOne.put("house_unit_name",   StringUtils.getNullValue(house.getUnit().getName(), ""));
			mapOne.put("house_num_floor",   StringUtils.getNullValue(house.getNumFloor(), ""));
			mapOne.put("house_code",   StringUtils.getNullValue(house.getCode(), ""));
			mapOne.put("user_name",   StringUtils.getNullValue(owner.getName(), ""));
			mapOne.put("fees_name",   StringUtils.getNullValue(fees.getName(), ""));
			mapOne.put("sum_usage_amount",   StringUtils.getNullValue(dt.getSumUsageAmount(), "0"));
			mapOne.put("sum_pay_money",   StringUtils.getNullValue(dt.getSumPayMoney(), "0"));
			mapOne.put("sum_income_money",   StringUtils.getNullValue(dt.getIncomeMoney(), "0"));
			
			mapOne.put("payment_date",   StringUtils.getNullValue(dev.getCurDateStr(), ""));

			
			lsDetailAll.add(mapOne);	
				
		}
	     
//	       <result column="user_login_name" property="device.house.owner.loginName"/>
//	       <result column="company_code" property="device.house.owner.company.name"/>
//	       <result column="house_pos_name" property="device.house.name"/>
//	       <result column="house_unit_name" property="device.house.unit.name"/>
//	       <result column="house_num_floor" property="device.house.numFloor"/>
//	       <result column="house_code" property="device.house.code"/>
//	       <result column="user_name" property="device.house.owner.name"/>
//	       <result column="payment_date" property="device.curDateStr"/>
//	       <result column="fees_name" property="device.fees.name"/>
//	       <result column="sum_usage_amount" property="sumUsageAmount"/>
//	       <result column="sum_pay_money" property="sumPayMoney"/>
//	       <result column="sum_income_money" property="incomeMoney"/>

		Office office = officeService.get(officeId);
		
		map.put("Detail", lsDetailAll);
	
		
		Master.put("title",office.getName());

		map.put("Master", Master);
		
		return map;
		
	}	
	
	@ResponseBody
	@RequestMapping(value = "saveNew")
	public String saveNew(DeviceDetail deviceDetail,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		
		System.out.println("houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 3333333333333333" );  
//		System.out.println("houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 3333333333333333" +model); 
//		System.out.println("houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 3333333333333333" +orders.get("detailsIncome"));
		
//		System.out.println("receDate 1 >>"+ );  

		PaymentAfter paymentAfter = new PaymentAfter();
		PaymentBefor paymentBefor1 = new PaymentBefor();
		PaymentBefor paymentBefor2 = new PaymentBefor();
//		PaymentBefor paymentBefor3 = new PaymentBefor();
		
		String officeId = request.getParameter("officeId"); //实收金额
		String houseId = request.getParameter("houseId");
		String feeCode = request.getParameter("feeCode");
		String certCode = request.getParameter("certCode");
		String receDate = request.getParameter("receDate"); //收款日期
		String payType = request.getParameter("payType"); //收款方式
		String leaveMoneyOut = request.getParameter("leaveMoneyOut"); //余额支出
		String costMoney2 = request.getParameter("costMoney2"); //应付金额
		String costMoney3 = request.getParameter("costMoney3"); //实收金额
		String remarks = request.getParameter("remarks"); //备注
		String paymentBeforId1 = request.getParameter("paymentBeforId"); //备注
		
		
		String paymentBeforId = null;
//		PayemtDetail[] payemtDetails = payemtDetail.getPayemtDetails();
		
		
		System.out.println("houseId 1 >>"+ houseId); 
		System.out.println("receDate 1 >>"+ receDate); 
		System.out.println("payType 1 >>"+ payType); 
		System.out.println("leaveMoneyOut 1 >>"+ leaveMoneyOut); 
		System.out.println("costMoney2 1 >>"+ costMoney2); 
		System.out.println("costMoney3 1 >>"+ costMoney3); 

		
		User u = UserUtils.getUser();
		Date receD = new Date();
		try {
			receD = DateUtil.parseDate(receDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


		//预付款  如果 leaveMoneyOut大于0，
		double leaveMoneyOutA = Double.parseDouble(StringUtils.getNullValue(leaveMoneyOut, "0"));
		double lle = Double.parseDouble(StringUtils.getNullValue(costMoney3, "0"))-Double.parseDouble(StringUtils.getNullValue(costMoney2, "0"));

		
		paymentBefor1.setId(null);
		paymentBefor1.setReceDate(receD);
		paymentBefor1.setPayType(payType); // 现金
		paymentBefor1.setType("2"); //1现付 2 预付 
		paymentBefor1.setFeeCode(feeCode);
		paymentBefor1.setCertCode(certCode);
		paymentBefor1.setHouse(HouseUtils.getHouse(houseId));
		paymentBefor1.setCompany(new Office(officeId));
		paymentBefor1.setUser(u);
		paymentBefor1.setPayFrom("2");
	
		
		BigDecimal recMoney = new BigDecimal(0);
		if(leaveMoneyOutA > 0){
			recMoney = new BigDecimal(-leaveMoneyOutA);	
			paymentBefor1.setRecMoney(recMoney);
			paymentBeforService.save(paymentBefor1);
	 	}

		//实收大于应收，多余的转预付
		if(lle >0){
			recMoney = new BigDecimal(lle);	
			paymentBefor1.setRecMoney(recMoney);
			paymentBeforService.save(paymentBefor1);
		}	

		if(Double.parseDouble(StringUtils.getNullValue(costMoney3, "0")) >0){
			recMoney = new BigDecimal(Double.parseDouble(StringUtils.getNullValue(costMoney3, "0"))+leaveMoneyOutA);
	        if(StringUtils.isBlank(paymentBeforId1)){
				paymentBefor2.setId(null);
				paymentBefor2.setReceDate(receD);
				paymentBefor2.setPayType(payType); // 现金
				paymentBefor2.setType("1"); //1现付 2 预付 
				paymentBefor2.setFeeCode(feeCode);
				paymentBefor2.setCertCode(certCode);
				paymentBefor2.setHouse(HouseUtils.getHouse(houseId));
				paymentBefor2.setCompany(new Office(officeId));
				paymentBefor2.setUser(u);
				paymentBefor2.setRecMoney(recMoney);
				paymentBefor2.setRemarks(remarks);
				paymentBefor2.setPayFrom("2");
				paymentBeforService.save(paymentBefor2);
				paymentBeforId = paymentBefor2.getId();
	        }else{
//	        	PaymentBefor paymentBefor3 = paymentBeforService.get(paymentBeforId1);
	        	paymentBeforId = paymentBeforId1;
	        }

//			System.out.println("paymentBeforId >>"+ paymentBeforId); 
		}

		
		String[] payments =  deviceDetail.getPayemtDetails().split(";");

			for (String s:payments){
				String[] ss = s.split(",");
				String id = ss[0];
				String incomeMoney = ss[1];
				String incomeMoney2 = ss[2];
				String feesId = ss[3];
//				Fees fees = FeesUtils.getFees(feesId);
//				String feedName = fees.getName();
				
				if(Double.parseDouble(StringUtils.getNullValue(incomeMoney, "0")) >0){
					DeviceDetail pDetail = deviceDetailService.get(id);
					pDetail.setIncomeMoney(new BigDecimal(incomeMoney));
					deviceDetailService.save(pDetail);
				}
	
				
				if(Double.parseDouble(StringUtils.getNullValue(incomeMoney2, "0")) >0){
					PaymentAfter paymentAfter2 = new PaymentAfter();
					paymentAfter2.setId(null);
					paymentAfter2.setDeviceDetail(new DeviceDetail(id));
					paymentAfter2.setPaymentBefor(new PaymentBefor(paymentBeforId));
					paymentAfter2.setRecMoney(new BigDecimal(incomeMoney2)); //实收金额 
					paymentAfterService.save(paymentAfter2);	
				}
			}
			
			addMessage(redirectAttributes, "保存'"  + "'成功");	
		
		return paymentBeforId;
		
//		return null;
//		return "redirect:"+Global.getAdminPath()+"/pms/paymentAfter/?repage";
	}	
	
	@ResponseBody
	@RequestMapping(value = "saveNew2")
	public String saveNew2(DeviceDetail deviceDetail,Model model,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
	//单位付款，只记录付款信息 paymentBefo 及付款明细paymentAfter
	
		String feeCode = request.getParameter("feeCode");
		String certCode = request.getParameter("certCode");
		String receDate = request.getParameter("receDate"); //收款日期
		String payType = request.getParameter("payType"); //收款方式
		String costMoney2 = request.getParameter("costMoney2"); //实收金额
		String officeId = request.getParameter("officeId"); //实收金额
		String remarks = request.getParameter("remarks"); //备注
		String paymentBeforId1 = request.getParameter("paymentBeforId"); //备注
		
		User u = UserUtils.getUser();
		Date receD = new Date();
		try {
			receD = DateUtil.parseDate(receDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String paymentBeforId = null;
		if(Double.parseDouble(StringUtils.getNullValue(costMoney2, "0")) >0){
			PaymentBefor paymentBefor = new PaymentBefor();
			 if(StringUtils.isBlank(paymentBeforId1)){
					paymentBefor.setId(null);
					paymentBefor.setPayType(payType);  //代扣
					paymentBefor.setType("1"); //1现付 2 预付 
					paymentBefor.setFeeCode(StringUtils.getNullValue(feeCode, ""));
					paymentBefor.setCertCode(StringUtils.getNullValue(certCode, ""));
					paymentBefor.setRecMoney(new BigDecimal(costMoney2)); //实收金额
					paymentBefor.setHouse(new House("0"));
					paymentBefor.setCompany(new Office(officeId));
					paymentBefor.setUser(u);
					paymentBefor.setReceDate(receD);
					paymentBefor.setRemarks(remarks);
					paymentBefor.setPayFrom("1");
					paymentBeforService.save(paymentBefor);	
					paymentBeforId = paymentBefor.getId();
			 }else{
				 paymentBeforId = paymentBeforId1;
			 }

//			System.out.println("saveNew2>>>>>>>>>>>>>>>>>>>>paymentBeforId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+paymentBeforId);
		}	
		

		
		String[] payments =  deviceDetail.getPayemtDetails().split(";");
		
		for (String s:payments){
			String[] ss = s.split(",");
			String id = ss[0];
			String incomeMoney = ss[1];
			String incomeMoney2 = ss[2];
//			String houseId = ss[3];
//			String feesId = ss[4];
			
//			Fees fees = FeesUtils.getFees(feesId);
//			String feedName = fees.getName();
			
			if(Double.parseDouble(StringUtils.getNullValue(incomeMoney, "0")) >0){
				DeviceDetail pDetail = deviceDetailService.get(id);
				pDetail.setIncomeMoney(new BigDecimal(incomeMoney));
				deviceDetailService.save(pDetail);				
			}


			if(Double.parseDouble(StringUtils.getNullValue(incomeMoney2, "0")) >0){
				PaymentAfter paymentAfter = new PaymentAfter();
				paymentAfter.setId(null);
				paymentAfter.setDeviceDetail(new DeviceDetail(id));
				paymentAfter.setPaymentBefor(new PaymentBefor(paymentBeforId));
				paymentAfter.setRecMoney(new BigDecimal(incomeMoney2)); //实收金额
				paymentAfterService.save(paymentAfter);	
			}

			
		}
		
		addMessage(redirectAttributes, "保存'"  + "'成功");	
//		redirectAttributes.addAttribute("paymentBeforId", paymentBeforId);
//		String result ="{paymentBeforId:"+paymentBeforId+"}";
		return paymentBeforId;
//		return null;
	}
	
	
	

	
	
	@RequestMapping(value = "form2")
	public String form2(DeviceDetail deviceDetail, Model model) {
		
//		System.out.println("form2       >>>>>>>>>>>>>>>>"+deviceDetail.getOfficeId()); 
//		System.out.println("form2       >>>>>>>>>>>>>>>>"+deviceDetail.getDevice().getFees().getCompany().getId()); 
		
//		System.out.println("form2       >>>>>>>>>>>>>>>>"+deviceDetail.getDevice().getHouse().getOwner().getCompany()); 
		

		
		Device device = deviceDetail.getDevice();
		if(device != null){
			deviceDetail.setFirstDate(Global.getDefStartDate());
			String officeId = deviceDetail.getOfficeId();
			deviceDetail.setOfficeId(officeId);
			model.addAttribute("deviceDetail", deviceDetail);
			if("1".equals(officeId)){
				List<Office> proCompanyList = UserUtils.findProCompanyList();
				if(proCompanyList.size()>0){
					Office proCompany = proCompanyList.get(0);
					List<Fees> feesList = FeesUtils.getALLFees(proCompany.getId());
					model.addAttribute("feesList", feesList);	
				}

			}else{
				String proCompanyId = device.getFees().getCompany().getId();
				
//				System.out.println("proCompanyId       >>>>>>>>>>>>>>>>"+ proCompanyId); 
				
				if (StringUtils.isNotBlank(proCompanyId)){
					List<Fees> feesList = FeesUtils.getALLFees(proCompanyId);
					model.addAttribute("feesList", feesList);
				}
				
				Office company = officeService.get(officeId);
				device.getHouse().getOwner().setCompany(company);
//				System.out.println("form2       >>>>>>>>>>>>>>>>"+company.getName()); 

			}

			
//			System.out.println("form3       >>>>>>>>>>>>>>>>"+officeId); 
		}
//		model.addAttribute("deviceDetail.firstDate", new Date());
		model.addAttribute("paymentBeforId", deviceDetail.getPaymentBeforId());
		

		return "modules/pms/payemtDetailForm2";
	}
	
	@RequestMapping(value = "form3")
	public String form3(DeviceDetail deviceDetail, Model model) {
		System.out.println("form3>>>>>>>>>>deviceDetail.getPaymentBeforId()>>>>>>"+ deviceDetail.getPaymentBeforId() ); 
		deviceDetail.setFirstDate(Global.getDefStartDate());
		Device device = deviceDetail.getDevice();
		
		String houseId = device.getHouse().getId();
		String officeId = deviceDetail.getOfficeId();
		System.out.println("officeId>>>>>>>>>>>>>>>>"+ officeId ); 
		
		if(StringUtils.isNotEmpty(officeId)){
			deviceDetail.setOfficeId(officeId);
		}
		

		if("1".equals(officeId)){
			List<Office> proCompanyList = UserUtils.findProCompanyList();
			if(proCompanyList.size()>0){
				Office proCompany = proCompanyList.get(0);
				List<Fees> feesList = FeesUtils.getALLFees(proCompany.getId());
				model.addAttribute("feesList", feesList);	
			}

		}else{
			String proCompanyId = device.getFees().getCompany().getId();
			if (StringUtils.isNotBlank(proCompanyId)){
				List<Fees> feesList = FeesUtils.getALLFees(proCompanyId);
				model.addAttribute("feesList", feesList);
			}
		}
		
		if(StringUtils.isNotEmpty(houseId)){
//			System.out.println("getHouse().getId()>>>>>>>>>>>>>>>>"+ houseId ); 
//			System.out.println("officeId>>>>>>>>>>>>>>>>"+ officeId ); 
			House house = houseService.get(houseId);
			device.setHouse(house);
		}

		
		model.addAttribute("deviceDetail", deviceDetail);
		
		Office proCompany = device.getFees().getCompany();
		Fees fees = new Fees();
		fees.setCompany(proCompany);
		model.addAttribute("feesList", feesService.findNoPool(fees));
		model.addAttribute("paymentBeforId", deviceDetail.getPaymentBeforId());
		
		return "modules/pms/payemtDetailForm3";
	}
	@RequestMapping(value = "usertorole")
	public String selectUserToRole(DeviceDetail deviceDetail,Model model) {
		String proCompanyId = deviceDetail.getId();
		System.out.println("proCompanyId>>>>>>>>>>>>>>>>"+ deviceDetail.getId() ); 
//		System.out.println("proCompanyId>>>>>>>>>>>>>>>>"+ proCompanyId ); 
		
		Fees fees = new Fees();
		fees.setCompany(officeService.get(proCompanyId)); 
		List<Fees> ls = feesService.find(fees); 
		
//		model.addAttribute("role", role);
		model.addAttribute("feesList", ls);
		model.addAttribute("selectIds", null);
		model.addAttribute("officeList", UserUtils.findCompanyListByProCompany(proCompanyId));
		return "modules/pms/selectUserToRole";
	}
	
	
	
	
}
