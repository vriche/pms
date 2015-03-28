/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.Arith;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.DeviceDetail;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.PaymentAfter;
import com.thinkgem.jeesite.modules.pms.entity.PaymentBefor;
import com.thinkgem.jeesite.modules.pms.service.DeviceDetailService;
import com.thinkgem.jeesite.modules.pms.service.FeesService;
import com.thinkgem.jeesite.modules.pms.service.HouseService;
import com.thinkgem.jeesite.modules.pms.service.PayemtDetailService;
import com.thinkgem.jeesite.modules.pms.service.PaymentAfterService;
import com.thinkgem.jeesite.modules.pms.service.PaymentBeforService;
import com.thinkgem.jeesite.modules.pms.utils.PaymentAfterUtils;
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
@RequestMapping(value = "${adminPath}/pms/paymentAfter")
public class PaymentAfterController extends BaseController {

	@Autowired
	private PaymentAfterService paymentAfterService;
	
	@Autowired
	private  PaymentBeforService paymentBeforService;
	
	@Autowired
	private PayemtDetailService payemtDetailService;
	
	@Autowired
	private DeviceDetailService deviceDetailService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private FeesService feesService;
	
	@Autowired
	private HouseService houseService;
	
	
	@ModelAttribute
	public PaymentAfter get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return paymentAfterService.get(id);
		}else{
			return new PaymentAfter();
		}
	}
	
//	@RequiresPermissions("pms:paymentAfter:view")
	@RequestMapping(value = {"list", ""})
	public String list(PaymentAfter paymentAfter, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			paymentAfter.setCreateBy(user);
		}
		

//		PaymentAfterUtils.getObjFromReq(paymentAfter,request,null,2);
//		Page<DeviceDetail> page1 = new Page<DeviceDetail>(request, response,-1);
//		DeviceDetail dtParam = paymentAfter.getDeviceDetail();
//		dtParam.setIsPay("1");
//		dtParam.getDevice().setType(null);
//		dtParam.setFirstDate(null);
//		dtParam.setLastDate(null);
//		Page<DeviceDetail> pageDeviceDetailList = deviceDetailService.findPage(page1,dtParam ,null);
//		paymentAfter.setDeviceDetailList(pageDeviceDetailList.getList());
////		
//        Page<PaymentAfter> page = paymentAfterService.find(new Page<PaymentAfter>(request, response), paymentAfter); 
//        model.addAttribute("page", page);

		return "modules/pms/paymentAfterList";
	}
	
	@RequestMapping(value = {"list2", ""})
	public String list2(PaymentAfter paymentAfter, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			paymentAfter.setCreateBy(user);
		}
		

		PaymentAfterUtils.getObjFromReq(paymentAfter,request,null,3);
		Page<DeviceDetail> page1 = new Page<DeviceDetail>(request, response,-1);
		DeviceDetail dtParam = paymentAfter.getDeviceDetail();
		dtParam.setIsPay("2");
		dtParam.getDevice().setType(null);
		dtParam.setFirstDate(null);
		dtParam.setLastDate(null);
//		dtParam.getDevice().setType("3");
		Page<DeviceDetail> pageDeviceDetailList = deviceDetailService.findPage(page1,dtParam ,null,"2");
		List<DeviceDetail> ls = pageDeviceDetailList.getList();
		paymentAfter.setDeviceDetailList(ls);
		
		System.out.println("pageDeviceDetailList.getList()>>>>>>>>>>>>>>>>>>>>>>>>>>>>222222222222222222>>>>>"+pageDeviceDetailList.getList().size());
		Page<PaymentAfter> page2 = new Page<PaymentAfter>(request, response);
		if(ls.size() >0){
			page2 = paymentAfterService.find(new Page<PaymentAfter>(request, response), paymentAfter); 
		}
        model.addAttribute("page", page2);

		return "modules/pms/paymentAfterList2";
	}
	
	
	@RequestMapping(value = {"listPayDetail", ""})
	public String listPayDetail(PaymentAfter paymentAfter, HttpServletRequest request, HttpServletResponse response, Model model) {
//		PaymentBefor paymentBefor = paymentAfter.getPaymentBefor();
		
//		System.out.println("listPayDetail>>>>>>>>>>>>>>>>>>>>>>>>>>>>getId>>>>>"+paymentBefor.getId());
		
//		User user = UserUtils.getUser();
//		if (!user.isAdmin()){
//			paymentBefor.setCreateBy(user);
//		}
		
		Page<PaymentAfter> page =  paymentAfterService.find(new Page<PaymentAfter>(request, response,-1), paymentAfter); 
		
		System.out.println("listPayDetail>>>>>>>>>>>>>>>>>>>>>>>>>>>>page.getList().size>>>>>"+page.getList().size());
        model.addAttribute("page", page);

		return "modules/pms/paymentAfterList3";
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "getPayDetailJsonReport")
	public Map<String, Object> getPayDetailJsonReport(String model,HttpServletRequest request, HttpServletResponse response) {	
		Map<String, Object> map = Maps.newHashMap();
		StringBuffer sb = new StringBuffer();

		String proCompanyId = request.getParameter("device.fees.company.id");
		String firstDate = request.getParameter("firstDate");
		String lastDate = request.getParameter("lastDate");
		String paymentBeforId = request.getParameter("paymentBeforId");
		String houseId = request.getParameter("house.id");
		String officeId = request.getParameter("device.house.owner.company.id");
		String feeCode = request.getParameter("feeCode");
		String type = request.getParameter("type");
		String payFrom = request.getParameter("payFrom");
		
//		System.out.println("proCompanyId"+proCompanyId);;
//		System.out.println("officeId"+officeId);
//		System.out.println("houseId"+houseId);
//		System.out.println("type"+type);
//		System.out.println("payFrom"+payFrom);
//		System.out.println("feeCode"+feeCode);
//		System.out.println("firstDate"+firstDate);
//		System.out.println("feeCode"+feeCode);
//		System.out.println("lastDate"+lastDate);
//		System.out.println("paymentBeforId"+paymentBeforId);
		
		
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>   getPayDetailJsonReport    paymentBeforId vvvvvvvvvvvvvvvvvvvvv >>>>>>>>>>>>>>>>>>>>>>>>>>"+paymentBeforId);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>   getPayDetailJsonReport    proCompanyId vvvvvvvvvvvvvvvvvvvvv >>>>>>>>>>>>>>>>>>>>>>>>>>"+proCompanyId);
		
		//get title
		Office proCompany = officeService.get(proCompanyId);
		String title = proCompany.getName()+"缴费单";
		

		Map<String, Object> params = new HashMap<String, Object>();
		
		List<Map<String, String>> lsDetailAll = new ArrayList<Map<String, String>>();
		
		
		
		if(paymentBeforId != null & StringUtils.isNotEmpty(paymentBeforId) && !"null".equals(paymentBeforId) && !"0".equals(paymentBeforId)){
			String[] ids = paymentBeforId.split(",");
			params.put("ids", Arrays.asList(ids)); 
		}else{
			PaymentAfter pAfterParam = new PaymentAfter();
			PaymentBefor pBeforParam = new PaymentBefor();
			pAfterParam.setPaymentBefor(pBeforParam);
			
			if (StringUtils.isNotEmpty(type) && !"null".equals(type)){
				pBeforParam.setType(type);
			}
			
			if (StringUtils.isNotEmpty(payFrom) && !"null".equals(payFrom) && !"0".equals(payFrom)){
				pBeforParam.setPayFrom(payFrom);
			}
			
			
			if (StringUtils.isNotEmpty(feeCode) && !"null".equals(feeCode)){
				pBeforParam.setFeeCode(feeCode);
			}
		
			if (StringUtils.isNotEmpty(houseId) && !"null".equals(houseId) && !"0".equals(houseId)){
				pBeforParam.setHouse(new House(houseId));
				pAfterParam.setHouse(new House(houseId));
			}
			
			if (StringUtils.isNotEmpty(officeId) && !"null".equals(officeId)&& !"0".equals(officeId)){
				pBeforParam.setCompany(new Office(officeId));
			}

			Page<PaymentAfter> page =  paymentAfterService.find(new Page<PaymentAfter>(request, response,-1), pAfterParam); 
			
			
			
			List<String> lsIds = new ArrayList<String>();
			for(PaymentAfter sf:page.getList()){
				lsIds.add(sf.getPaymentBefor().getId());
			}
			params.put("ids", lsIds); 

			if(lsIds.size() == 0) params.put("ids",Arrays.asList(new String[]{"-1"}));
		}

//		System.out.println(">>>>>>>>>>>>>>>>>> getPayDetailJsonReport    lsIds.size() vvvvvvvvvvvvvvvvvvvvv >>>>>>>>>>>>>>>>>>>>>>>>>>"+lsIds.size());
		

		
		
		
//		if(idsCount > 0){
			
			List<PaymentAfter>  ls =  paymentAfterService.findGroupByFees(params); 
			
			for(PaymentAfter dt:ls){

				Map<String, String> mapOne = Maps.newHashMap();
				PaymentBefor pBefor = dt.getPaymentBefor();
				Office company = pBefor.getCompany(); 
				DeviceDetail deviceDetail = dt.getDeviceDetail();
				Device device = deviceDetail.getDevice();
//				House house = device.getHouse();
				House house = houseService.get(device.getHouse().getId());
				
				System.out.println(">>>>>>>>>>>>>>>>> device.getHouse().getId() vvvvvvvvvvvvvvvvvv >>>>>>>>>>>>>>>>>>>>>>>>>>"+ device.getHouse().getId());
				

				

//				,费项名称,单价,上次读数,本次读数,实际用量,本次应付,付款期限
				double payMoney = Arith.roundEVEN(StringUtils.toDouble(deviceDetail.getSumPayMoney()).doubleValue(),2); 
				double incomeMoney = Arith.roundEVEN(Double.parseDouble(StringUtils.getNullValue(deviceDetail.getIncomeMoney() , "0")),2);

				//group by
//				户号：房屋地址房屋地址房 面积：56   单位：                  收款时间：2015/10/9
//				姓名：小小小小    户主编码：   手机号码：11111111     电话：13233847625  
				mapOne.put("houseFullName",  StringUtils.getNullValue(house.getFullName(), "")); //户号
//				mapOne.put("companyName",  StringUtils.getNullValue(company.getName(), ""));//单位
				mapOne.put("companyName",  StringUtils.getNullValue(house.getOwner().getCompany().getName(), ""));//单位
				mapOne.put("receDate", StringUtils.getNullValue(DateUtils.formatDate(pBefor.getReceDate(), "yyyy-MM-dd"),"")); //收款时间
				mapOne.put("feeCode",  StringUtils.getNullValue(pBefor.getFeeCode(), "")); //户号
				mapOne.put("ownerName",  StringUtils.getNullValue(house.getOwner().getName(), "")); //姓名
				mapOne.put("ownerCode",  StringUtils.getNullValue(house.getOwner().getLoginName(), "")); // 户主编码
				mapOne.put("ownerMobile",  StringUtils.getNullValue(house.getOwner().getMobile(), "")); // 手机号码
				mapOne.put("ownerPhone",  StringUtils.getNullValue(house.getOwner().getPhone(), "")); // 电话
				
				//details  
//				收费项目、	收费周期	收费周期	用量	单价	应交费用
				mapOne.put("feesName",  StringUtils.getNullValue(device.getFees().getName(), "")); //收费项目
				mapOne.put("firstDate",   StringUtils.getNullValue(DateUtils.formatDate(deviceDetail.getFirstDate(), "yyyy-MM-dd"),"")); //收费周期 开始
				mapOne.put("lastDate",   StringUtils.getNullValue(DateUtils.formatDate(deviceDetail.getLastDate(), "yyyy-MM-dd"),""));//收费周期 结束
				mapOne.put("usageAmount",   StringUtils.getNullValue(deviceDetail.getUsageAmount(), "")); //用量
				mapOne.put("unitPrice",   StringUtils.getNullValue(Arith.roundEVEN(deviceDetail.getUnitPrice().doubleValue(),4), "")); //单价
				mapOne.put("sumPayMoney",   StringUtils.getNullValue(payMoney, "0")); //应交费用
				mapOne.put("incomeMoney",   StringUtils.getNullValue(incomeMoney, "0")); //已交费用

				//需要3联样式，所以重复3次数据  每页分3栏
				mapOne.put("ToWho",   "1"); //已交费用
				lsDetailAll.add(mapOne);

				Map<String, String> cloneMap2 = (HashMap)((HashMap)mapOne).clone();
				cloneMap2.put("ToWho",   "2"); //已交费用
				lsDetailAll.add(cloneMap2);
				
				Map<String, String> cloneMap3 = (HashMap)((HashMap)mapOne).clone();
				cloneMap3.put("ToWho",   "3"); //已交费用
				lsDetailAll.add(cloneMap3);

			 }
//		}

		
	
		map.put("Detail", lsDetailAll);
		Map<String, String> Master = Maps.newHashMap();
		Master.put("title",title);
		map.put("Master", Master);
		
		return map;
		
	}		

//	@RequiresPermissions("pms:paymentAfter:view")
	@RequestMapping(value = "form")
	public String form(PaymentAfter paymentAfter, Model model) {
		PaymentBefor paymentBefor = paymentAfter.getPaymentBefor();
		if(paymentBefor.getReceDate() == null){
			paymentBefor.setReceDate(new java.util.Date());
		}
		paymentBefor.setUser(UserUtils.getUser());
		model.addAttribute("paymentAfter", paymentAfter);
		return "modules/pms/paymentAfterForm";
	}

//	@RequiresPermissions("pms:paymentAfter:edit")
	@RequestMapping(value = "save")
	public String save(PaymentAfter paymentAfter, Model model, RedirectAttributes redirectAttributes) {
//		if (!beanValidator(model, paymentAfter)){
//			return form(paymentAfter, model);
//		}
		PaymentBefor paymentBefor = paymentAfter.getPaymentBefor();
		
		if(paymentBefor.getReceDate() == null){
			paymentBefor.setReceDate(new java.util.Date());
		}
		
		if(paymentAfter.getRecMoney() == null){
			paymentAfter.setRecMoney(new BigDecimal(0));
		}
				
		paymentBefor.setUser(UserUtils.getUser());
		
		String id = paymentAfter.getId();
		
		
		
		if(id != null && "2".equals(paymentBefor.getType())){
			PaymentAfter paymentAfterBak = paymentAfterService.get(id);
			double recMoney = paymentAfter.getRecMoney().doubleValue();
			double recMoneyBak = paymentAfterBak.getRecMoneyBak().doubleValue();
//			double money_new = 0;
			
			System.out.println("save>>>>>>>>>>>>>>>>>>>>>>>>>>>>money>>>>>>>>>>>"+ recMoney);
			System.out.println("save>>>>>>>>>>>>>>>>>>>>>>>>>>>>money_bak>>>>>>>>>>>"+ recMoneyBak);
			
			if(recMoney <= recMoneyBak){
				if(recMoney < recMoneyBak){
//					PayemtDetail pDetail = payemtDetailService.get(paymentAfter.getDeviceDetailId().toString());
					DeviceDetail deviceDetail  = deviceDetailService.get(paymentAfter.getDeviceDetail().getId());
					double incomeMoney_bak  = deviceDetail.getIncomeMoney().doubleValue();
					double incomeMoney = incomeMoney_bak -(recMoneyBak-recMoney);
					deviceDetail.setIncomeMoney(new BigDecimal(incomeMoney));
					deviceDetailService.save(deviceDetail);
				}
			}
			paymentAfterService.save(paymentAfter);
		}else{
			paymentAfterService.save(paymentAfter);
		}
		
		
	
		addMessage(redirectAttributes, "保存单元信息'" + "'成功");
		return null;
//		return "redirect:"+Global.getAdminPath()+"/pms/paymentAfter/?repage";
	}
	
//	@RequiresPermissions("pms:paymentAfter:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {

		System.out.println("delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>id>>>>>>>>>>>"+ id);
//		System.out.println("delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>paymentDetailId>>>>>>>>>>>"+ deviceDetailId);
		
		long pid = Long.parseLong(StringUtils.getNullValue(id, "0"));
		
		String paymentBeforId = null;
		String type = null;
		String officeId = null;
		String houseId = null;
		String payFrom = null;
		
		if(pid >0){
			
			PaymentAfter paymentAfter = paymentAfterService.get(id);
			double recMoney = paymentAfter.getRecMoney().doubleValue();
			String deviceDetailId = paymentAfter.getDeviceDetail().getId();
			paymentBeforId = paymentAfter.getPaymentBefor().getId();
			  
			PaymentBefor paymentBefor = paymentBeforService.get(paymentBeforId);
			type = paymentBefor.getType();
			payFrom = paymentBefor.getPayFrom();
			officeId = paymentBefor.getCompany().getId();
			House h =  paymentBefor.getHouse();
			if(h != null){
				houseId  = h.getId();
			}
			
//			double recMoneyBak = paymentBefor.getRecMoney().doubleValue();
//			paymentBefor.setRecMoney(new BigDecimal(recMoneyBak-recMoney));
//			if(paymentBefor.getHouse() == null){
//				paymentBefor.setHouse(new House("0"));
//			}
//			System.out.println("delete>>>>>>>>>>>>>>>>>>>>>>>>>>>> paymentBefor.getHouse().getId()>>>>>>>>>>>"+ paymentBefor.getHouse().getId());
//			paymentBeforService.save(paymentBefor);
			
			DeviceDetail deviceDetail = deviceDetailService.get(deviceDetailId);
			double income = deviceDetail.getIncomeMoney().doubleValue();
			deviceDetail.setIncomeMoney(new BigDecimal(income-recMoney));
			System.out.println("delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>recMoney>>>>>>>>>>>"+ recMoney);
			System.out.println("delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>income>>>>>>>>>>>"+ income);
			System.out.println("delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>getIncomeMoney>>>>>>>>>>>"+deviceDetail.getIncomeMoney());
			deviceDetailService.save(deviceDetail);

		}
		
		paymentAfterService.delete(id);
		
//		addMessage(redirectAttributes, "删除成功");
		
//		var url = "${ctx}/pms/paymentAfter/listPayDetail?paymentBefor.id="+ paymentBeforId +"&paymentBefor.type="+ type+"&paymentBefor.company.id="+ officeId +"&paymentBefor.house.id="+houseId;

		return "redirect:"+Global.getAdminPath()+"/pms/paymentAfter/listPayDetail?paymentBefor.id="+ paymentBeforId +"&payFrom="+ payFrom +"&paymentBefor.type="+ type+"&paymentBefor.company.id="+ officeId +"&paymentBefor.house.id="+houseId;
		
//		return "modules/pms/paymentAfterList3";
//		return "redirect:"+Global.getAdminPath()+"/pms/paymentAfter/?repage";
	}
	
	
//	deleteBatch
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(String idss, RedirectAttributes redirectAttributes) {

		System.out.println("delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>ids>>>>>>>>>>>"+ idss);
		
		String[] ids = idss.split(",");
		
	
		for(String id:ids){

			long pid = Long.parseLong(StringUtils.getNullValue(id, "0"));
			
			String paymentBeforId = null;
			String type = null;
			String officeId = null;
			String houseId = null;
			String payFrom = null;
			
			if(pid >0){
				PaymentAfter paymentAfter = paymentAfterService.get(id);
				double recMoney = paymentAfter.getRecMoney().doubleValue();
				String deviceDetailId = paymentAfter.getDeviceDetail().getId();
				paymentBeforId = paymentAfter.getPaymentBefor().getId();
				  
				PaymentBefor paymentBefor = paymentBeforService.get(paymentBeforId);
				type = paymentBefor.getType();
				payFrom = paymentBefor.getPayFrom();
				officeId = paymentBefor.getCompany().getId();
				House h =  paymentBefor.getHouse();
				if(h != null){
					houseId  = h.getId();
				}
	
				
				DeviceDetail deviceDetail = deviceDetailService.get(deviceDetailId);
				double income = deviceDetail.getIncomeMoney().doubleValue();
				deviceDetail.setIncomeMoney(new BigDecimal(income-recMoney));
				System.out.println("delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>recMoney>>>>>>>>>>>"+ recMoney);
				System.out.println("delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>income>>>>>>>>>>>"+ income);
				System.out.println("delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>getIncomeMoney>>>>>>>>>>>"+deviceDetail.getIncomeMoney());
				deviceDetailService.save(deviceDetail);
				paymentAfterService.delete(id);
	
			}
		}

//		return "redirect:"+Global.getAdminPath()+"/pms/paymentAfter/listPayDetail?paymentBefor.id="+ paymentBeforId +"&payFrom="+ payFrom +"&paymentBefor.type="+ type+"&paymentBefor.company.id="+ officeId +"&paymentBefor.house.id="+houseId;
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "getPaymentAfterJson")
	public Map<String, Object> getPaymentAfterJson(String model,HttpServletRequest request, HttpServletResponse response) {	
    
		Map<String, Object> map = Maps.newHashMap();
		StringBuffer sb = new StringBuffer();
		
		
//		String houseId = request.getParameter("houseId");
//		String type = request.getParameter("type");
//		String certCode = request.getParameter("certCode");
//		String feeCode = request.getParameter("feeCode");
//		PaymentAfter paymentAfter = new PaymentAfter();
//		paymentAfter.setHouse(new House(houseId));
//		paymentAfter.setType(type);
//		paymentAfter.setCertCode(certCode);
//		paymentAfter.setFeeCode(feeCode);
//		List<PaymentAfter> ls = paymentAfterService.findAll(paymentAfter);
		
		PaymentAfter paymentAfter = new PaymentAfter();
		PaymentAfterUtils.getObjFromReq(paymentAfter,request,null,2);
		Page<DeviceDetail> page1 = new Page<DeviceDetail>(request, response,-1);
		DeviceDetail dtParam = paymentAfter.getDeviceDetail();
		dtParam.setIsPay("2");
		dtParam.getDevice().setType(null);
		dtParam.setFirstDate(null);
		dtParam.setLastDate(null);
		Page<DeviceDetail> pageDeviceDetailList = deviceDetailService.findPage(page1,dtParam ,null,"1");
		paymentAfter.setDeviceDetailList(pageDeviceDetailList.getList());
        Page<PaymentAfter> page = paymentAfterService.find(new Page<PaymentAfter>(request, response), paymentAfter); 
        List<PaymentAfter> ls = page.getList();
        
//        System.out.println("findPaymentAfter list 2 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+page.getList().size());	
		

		sb.delete(0,sb.length());
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<rows>");  

//		"房间,表编号,业主,上次日期,上次读数,本次读数,使用量";
		int i=1;
		double costMoney = 0;
		double preMoney = 0;
		for(PaymentAfter after:ls){
			System.out.println(">>>>>>>>>>>>>>>>>>>>>getdevicesJson>>>>>>>>>>> d.getHouse() >>>>>>>>>>>>>>>>>>>>>>>>>>"+after.getHouse());
			PaymentBefor d = after.getPaymentBefor();
			House h = d.getHouse();
//			costMoney = costMoney+d.getCostMoney().doubleValue(); 
			//"序,收费项目,收款单号,发票号 ,收款日期,应付金额,收款金额,收款方式,收款人,操作";
			double recMoney = Arith.roundEVEN(d.getRecMoney().doubleValue(),2);
			String deviceDetailId = after.getDeviceDetail().getId();
//			double incomeMoney = Arith.roundEVEN(Double.parseDouble(StringUtils.getNullValue(d.getIncomeMoney() , "0")),2);
			sb.append("<row  id=\""+d.getId()  +"\">");
			sb.append("<cell>"+ i++ +"</cell>");
//			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getFeedName(), "") +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getFeeCode(), "") +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getCertCode(), "") +"]]></cell>");
			sb.append("<cell>"+ StringUtils.getNullValue(DateUtils.formatDate(d.getReceDate(), "yyyy-MM-dd"),"" )+"</cell>");
			sb.append("<cell><![CDATA["+ recMoney +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(DictUtils.getLable("pms_pay_type",d.getPayType()), "") +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getUser().getName(), "") +"]]></cell>");
//			sb.append("<cell src=image/button_delete.gif><![CDATA["+"^javascript:deletePaymentDetail(this)^;" +"]]></cell>");
//			sb.append("<cell><![CDATA["+"^javascript:deletePaymentDetail(this)^;" +"]]></cell>");
			
			String pos = "<a href=\"javascript:void 0\" onClick= javascript:deletePaymentDetail("+ d.getId() +","+ deviceDetailId +")>" + "删除"+ "</a>";
			sb.append("<cell><![CDATA["+pos +"]]></cell>");
			
			sb.append("<userdata name=\"deviceDetailId\">"+ deviceDetailId +"</userdata>");
			
			sb.append("</row>");
		 }
		
		
		sb.append("</rows>");
		map.put("grid", sb.toString());
		
//		map.put("costMoney", Arith.roundEVEN(costMoney,2));
//		map.put("preMoney", Arith.roundEVEN(preMoney,2));
		
		
		return map;
		
  }	
	
	



}
