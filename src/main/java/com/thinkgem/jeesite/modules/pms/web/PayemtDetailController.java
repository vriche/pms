/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.web;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.Arith;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Buildings;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.PayemtDetail;
import com.thinkgem.jeesite.modules.pms.entity.PaymentAfter;
import com.thinkgem.jeesite.modules.pms.entity.Unit;
import com.thinkgem.jeesite.modules.pms.service.BuildingsService;
import com.thinkgem.jeesite.modules.pms.service.CommunityService;
import com.thinkgem.jeesite.modules.pms.service.FeesService;
import com.thinkgem.jeesite.modules.pms.service.HouseService;
import com.thinkgem.jeesite.modules.pms.service.PayemtDetailService;
import com.thinkgem.jeesite.modules.pms.service.PaymentAfterService;
import com.thinkgem.jeesite.modules.pms.service.UnitService;
import com.thinkgem.jeesite.modules.pms.utils.DeviceUtils;
import com.thinkgem.jeesite.modules.pms.utils.FeesUtils;
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
@RequestMapping(value = "${adminPath}/pms/payemtDetail")
public class PayemtDetailController extends BaseController {

	@Autowired
	private PayemtDetailService payemtDetailService;
	
	@Autowired
	private PaymentAfterService paymentAfterService;
	
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
	private OfficeService officeService;
	
	private boolean isNewExcel =  com.thinkgem.jeesite.common.config.Global.getOfficeVersion();
	
	@ModelAttribute
	public PayemtDetail get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return payemtDetailService.get(id);
		}else{
			return new PayemtDetail();
		}
	}
	
//	@RequiresPermissions("pms:payemtDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(PayemtDetail payemtDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			payemtDetail.setCreateBy(user);
		}
		

		if(payemtDetail.getDevice() == null){payemtDetail.setDevice(new Device());}
	    Device device = payemtDetail.getDevice();
		DeviceUtils.setObjFromReq(device,  request,  response,  model,2);
		
		device.setModel("1");
		
		
        if(device.getFees().getCompany() != null){
        	Page<PayemtDetail> page = payemtDetailService.findPayemtDetail(new Page<PayemtDetail>(request, response), payemtDetail); 
            model.addAttribute("page", page);
        }else{
       	   model.addAttribute("page", new Page<PayemtDetail>());
        }	

        
		return "modules/pms/payemtDetailList";
	}
	

	@RequestMapping(value = "index")
	public String index() {
		return "modules/pms/payIndex";
	}
	

	
//	public List<Map<String, Object>> tree(Model model,HttpServletRequest request, HttpServletResponse response) {
//		response.setContentType("application/json; charset=UTF-8");
//		Map<String, Object> map = Maps.newHashMap();
//		List<Map<String, Object>> mapList = Lists.newArrayList();
//		String id = request.getParameter("id");
//		String name = request.getParameter("name");
//		String level = request.getParameter("level");
//		String otherParam = request.getParameter("otherParam");
//		System.out.println("id>>"+id + "|name>>" + name + "|level>>" + level + "|otherParam>>" + otherParam);  
//		
//		if(StringUtils.isEmpty(level)){
//			map.put("id",-11);
//			map.put("pId",0);
//			map.put("level",-1);
//			map.put("name", "test");	
//			map.put("isParent", true);
//			map.put("open", false);
//			
//			mapList.add(map);
//		}
//
//		return mapList;
//
//	}

	

	@RequestMapping(value = "tree")
	public String tree(Model model,HttpServletRequest request, HttpServletResponse response) {
		return "modules/pms/payTree";
	}
	
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(Model model,HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
//		Map<String, Object> map = Maps.newHashMap();
		List<Map<String, Object>> mapList = Lists.newArrayList();
		String id = request.getParameter("id");
		String relid = request.getParameter("relid");
		String name = request.getParameter("name");
		String level = request.getParameter("level");
		String proCompanyId = request.getParameter("proCompanyId");
		String type = request.getParameter("type");
		
		
		System.out.println("type>>" + type +"| id>>"+id + "|name>>" + name + "|level>>" + level + "|proCompanyId>>" + proCompanyId);  
		
//		System.out.println("getAdminPath>>"+Global.getAdminPath()+"/jeesite/a/payemtDetail/none" );  
		System.out.println("getAdminPath>>"+Global.getAdminPath()+"/pms/payemtDetail/none");  
		


		
		
		if("1".equals(type)){
			
			if(StringUtils.isEmpty(level)){
				Office office = new Office();
				office.setParent( new Office(proCompanyId));
				List<Office> ls = officeService.findOffice(office);
				
				for(Office c:ls){
					
					String idd =c.getSort();
					if(!"0".equals(idd)){
						Map<String, Object> map = Maps.newHashMap();
						map.put("id","company"+c.getId());
						map.put("pId",0);
						map.put("level",0);
						map.put("name", c.getName());	
						map.put("isParent", true);
						map.put("open", true);	
						
						map.put("target", "cmsMainFrame");	
						map.put("url","/jeesite"+Global.getAdminPath()+"/pms/payemtDetail/form2?officeId="+c.getId()+"&device.fees.company.id="+proCompanyId);
						mapList.add(map);
					}
	
//					map.put("target", "cmsMainFrame");	
//					map.put("url","../payemtDetail/none");
//					map.put("url","/jeesite"+Global.getAdminPath()+"/pms/payemtDetail/none");

				
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
					if("3".equals(userType)){
						names = owner.getCompany().getName();
					}else{
//						names = h.getNumFloor()+"层  ("+owner.getName()+")";
						names ="("+owner.getName()+") " + h.getFullName();
						
					}
					
					map.put("id","House"+h.getId());
					map.put("pId",id);
					map.put("level",1);
					map.put("name", names);	
					map.put("target", "cmsMainFrame");	
					map.put("url","/jeesite"+Global.getAdminPath()+"/pms/payemtDetail/form?house.id="+h.getId()+"&device.fees.company.id="+proCompanyId);

					mapList.add(map);
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
					
					
//					map.put("target", "cmsMainFrame");	
//					map.put("url","../payemtDetail/none");
//					map.put("url","/jeesite"+Global.getAdminPath()+"/pms/payemtDetail/none");
					
					
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
				if("3".equals(userType)){
					names = owner.getCompany().getName();
				}else{
					names = h.getNumFloor()+"层  ("+owner.getName()+")";
				}
				
				map.put("id","House"+h.getId());
				map.put("pId",id);
				map.put("level",3);
				map.put("name", names);	
				map.put("target", "cmsMainFrame");	
				map.put("url","/jeesite"+Global.getAdminPath()+"/pms/payemtDetail/form?house.id="+h.getId()+"&device.fees.company.id="+proCompanyId);

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
	

//	@RequiresPermissions("pms:payemtDetail:view")
	@RequestMapping(value = "form")
	public String form(PayemtDetail payemtDetail, Model model) {
		
		
		payemtDetail.setFirstDate(Global.getDefStartDate());
		
		String houseId = payemtDetail.getHouse().getId();
		String officeId = payemtDetail.getOfficeId();
		
		if(StringUtils.isNotEmpty(officeId)){
			payemtDetail.setOfficeId(officeId);
		}else{
			System.out.println("getHouse().getId()>>>>>>>>>>>>>>>>"+ houseId ); 
			System.out.println("officeId>>>>>>>>>>>>>>>>"+ officeId ); 
			House house = houseService.get(houseId);
			payemtDetail.setHouse(house);
		}

		
		model.addAttribute("payemtDetail", payemtDetail);
		
		Office proCompany = payemtDetail.getDevice().getFees().getCompany();
		Fees fees = new Fees();
		fees.setCompany(proCompany);
		model.addAttribute("feesList", feesService.findNoPool(fees));
		
//		model.addAttribute("payTypeList", DictUtils.getDictList("pms_pay_type"));
		
		
		
		
//		model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
		
//		 List<Office> companyList = UserUtils.findProCompanyList();
//		 Office proCompany = null;
//		 String proCompanyId ="";
//		 if (StringUtils.isBlank(proCompanyId) && companyList.size() >0){
//			 proCompany = companyList.get(0);
//		 }else{
//			 proCompany = new Office(proCompanyId);
//		 }
//		 Fees fees = new Fees();
//		 fees.setCompany(proCompany);
//		
//		model.addAttribute("feesList", feesService.findNoPool(fees));
		 
		return "modules/pms/payemtDetailForm";
	}
	
	
	@RequestMapping(value = "form2")
	public String form2(PayemtDetail payemtDetail, Model model) {
		payemtDetail.setFirstDate(Global.getDefStartDate());
		String officeId = payemtDetail.getOfficeId();
		payemtDetail.setOfficeId(officeId);
	
		model.addAttribute("payemtDetail", payemtDetail);
		
		Office proCompany = payemtDetail.getDevice().getFees().getCompany();
		Fees fees = new Fees();
		fees.setCompany(proCompany);
		model.addAttribute("feesList", feesService.findNoPool(fees));
		
	

		return "modules/pms/payemtDetailForm2";
	}

//	@RequiresPermissions("pms:payemtDetail:edit")
	@RequestMapping(value = "save")
	public String save(PayemtDetail payemtDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, payemtDetail)){
			return form(payemtDetail, model);
		}
		payemtDetailService.save(payemtDetail);
		addMessage(redirectAttributes, "保存单元信息'" + "" + "'成功");
		return "redirect:"+Global.getAdminPath()+"/pms/payemtDetail/?repage";
	}

//	@RequiresPermissions("pms:payemtDetail:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		payemtDetailService.delete(id);
		addMessage(redirectAttributes, "删除单元信息成功");
		return "redirect:"+Global.getAdminPath()+"/pms/payemtDetail/?repage";
	}
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "getPaymentDetailsJson")
	public Map<String, Object> getPaymentDetailsJson(String model,HttpServletRequest request, HttpServletResponse response) {	
    
		Map<String, Object> map = Maps.newHashMap();
		StringBuffer sb = new StringBuffer();
		String houseId = request.getParameter("houseId");
		String officeId = request.getParameter("officeId");
		String feesId = request.getParameter("feesId");
		String firstDate = request.getParameter("firstDate");
		String lastDate = request.getParameter("lastDate");
		String isPay = request.getParameter("isPay");
		String type = request.getParameter("type");
		
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>isPay>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+isPay);
		
		
		PayemtDetail payemtDetail = new PayemtDetail();
		PaymentAfter paymentAfter = new PaymentAfter();
		Office office = new Office();
		
		
		if(StringUtils.isNotEmpty(feesId)){
			Device device = new Device();
			device.setFees(new Fees(feesId));
			payemtDetail.setDevice(device);
		}
		
		if("1".equals(type)){
			payemtDetail.setOfficeId(officeId);
			office = officeService.get(officeId);
		}else{
			House house = new House(houseId);
			paymentAfter.setHouse(house);
			payemtDetail.setHouse(house);
		}

		
		
		payemtDetail.setFirstDate(DateUtils.parseDate(firstDate));
		payemtDetail.setLastDate(DateUtils.parseDate(lastDate));
		if("0".equals(isPay)||"1".equals(isPay)){
			payemtDetail.setIsPay(isPay);
		}
		
		
		
		List<PayemtDetail> ls = payemtDetailService.findPayemtDetails(payemtDetail);
		
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
			paymentAfter.setType("1");  //预付款
			PaymentAfter pAfter = paymentAfterService.find(paymentAfter);
			preMoney = Double.parseDouble(StringUtils.getNullValue(pAfter.getRecMoney(), "0"));
		}

		
		for(PayemtDetail d:ls){
			
			System.out.println(">>>>>>>>>>>>>>>>>>>>>getdevicesJson>>>>>>>>>>> d.getHouse() >>>>>>>>>>>>>>>>>>>>>>>>>>"+d.getHouse());
			
			House h = d.getHouse();
			String fullName = "";
			String userName = "";
			
			
			 
//			if(h != null) {
//				fullName =  h.getFullName();
//				userName =  h.getOwner().getName();
//			}
			
//			,费项名称,单价,上次读数,本次读数,实际用量,本次应付,付款期限
			 
			double payMoney = Arith.roundEVEN(Arith.roundEVEN(d.getPayMoney().doubleValue(),2)+Arith.roundEVEN(d.getPoolPayMoney().doubleValue(),2),2);
//			String payMoneyStr = String.valueOf(payMoney);
			
			
			double incomeMoney = Arith.roundEVEN(Double.parseDouble(StringUtils.getNullValue(d.getIncomeMoney() , "0")),2);
			incomeMoneySum +=incomeMoney;
			
			costMoney = costMoney+payMoney; 
			
			sb.append("<row  id=\""+d.getId()  +"\">");
			sb.append("<cell></cell>");
			
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getDevice().getFees().getName(), "") +"]]></cell>");
			
			if("1".equals(type)){
				sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getHouse().getFullName(), "")  +"]]></cell>");
				sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getHouse().getOwner().getName(), "")  +"]]></cell>");
				
				mpOwnerNum.put(d.getHouse().getId(), 0);
			}

			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getFirstNum(), "")  +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getLastNum() , "") +"]]></cell>");
			
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getUsageAmount(), "")  +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getPoolUsageAmount() , "") +"]]></cell>");
			
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(Arith.roundEVEN(d.getSumUsageAmount().doubleValue(),2), "")  +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(Arith.roundEVEN(d.getUnitPrice().doubleValue(),4), "")  +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(payMoney , "0")  +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(incomeMoney , "0")  +"]]></cell>");
			
			sb.append("<cell>"+ StringUtils.getNullValue(DateUtils.formatDate(d.getPaymentDate(), "yyyy-MM-dd"),"" )+"</cell>");
			
			sb.append("<userdata name=\"costMoney\">"+ payMoney +"</userdata>");
			sb.append("<userdata name=\"incomeMoney\">"+ incomeMoney +"</userdata>");
			sb.append("<userdata name=\"houseId\">"+ d.getHouse().getId() +"</userdata>");
			sb.append("<userdata name=\"feesId\">"+ d.getDevice().getFees().getId() +"</userdata>");
			
			sb.append("</row>");
		 }
		
		
		sb.append("</rows>");
		
		costMoney = costMoney -incomeMoneySum;
		
		map.put("grid", sb.toString());
		map.put("costMoney", Arith.roundEVEN(costMoney,2));
		map.put("preMoney", Arith.roundEVEN(preMoney,2));
		map.put("officeName", office.getName());
		map.put("ownerNum", mpOwnerNum.size());
		
		
		
		
		return map;
		
  }
	
	
//	@RequiresPermissions("pms:paymentAfter:edit")
	@RequestMapping(value = "saveNew")
	public String saveNew(PayemtDetail payemtDetail,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		
		System.out.println("houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 3333333333333333" );  
//		System.out.println("houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 3333333333333333" +model); 
//		System.out.println("houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 3333333333333333" +orders.get("detailsIncome"));
		
//		System.out.println("receDate 1 >>"+ );  


 	    
		PaymentAfter paymentAfter = new PaymentAfter();
		String houseId = request.getParameter("houseId");
		String feeCode = request.getParameter("feeCode");
		String certCode = request.getParameter("certCode");
		String receDate = request.getParameter("receDate"); //收款日期
		String payType = request.getParameter("payType"); //收款方式
		String leaveMoneyOut = request.getParameter("leaveMoneyOut"); //余额支出
		String costMoney2 = request.getParameter("costMoney2"); //应付金额
		String costMoney3 = request.getParameter("costMoney3"); //实收金额
		
//		PayemtDetail[] payemtDetails = payemtDetail.getPayemtDetails();
		
		
		System.out.println("houseId 1 >>"+ houseId); 
		System.out.println("receDate 1 >>"+ receDate); 
		System.out.println("payType 1 >>"+ payType); 
		System.out.println("leaveMoneyOut 1 >>"+ leaveMoneyOut); 
		System.out.println("costMoney2 1 >>"+ costMoney2); 
		System.out.println("costMoney3 1 >>"+ costMoney3); 
//		System.out.println("unitPrice 1 >>"+); 
//		System.out.println("houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 3333333333333333"+payemtDetail );  
//		System.out.println("houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 3333333333333333"+payemtDetail.getPayemtDetails() );  
		
//		System.out.println("detailsIncome 2 >>"+ request.getParameter("detailsIncome"));  
//		paymentAfterService.save(paymentAfter);
		
		
		
		User u = UserUtils.getUser();
		Date receD = new Date();
		try {
			receD = DateUtil.parseDate(receDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		House house = new House(houseId);
		paymentAfter.setHouse(house);
		paymentAfter.setUser(u);
		paymentAfter.setReceDate(receD);

		//预付款  如果 leaveMoneyOut大于0，
		double leaveMoneyOutA = Double.parseDouble(StringUtils.getNullValue(leaveMoneyOut, "0"));
		double lle = Double.parseDouble(StringUtils.getNullValue(costMoney3, "0"))-Double.parseDouble(StringUtils.getNullValue(costMoney2, "0"));
		
//		if(lle >0){
//			paymentAfter.setRecMoney(new BigDecimal(lle));	
//	 	}		 
//		if(leaveMoneyOutA > 0){
//			paymentAfter.setRecMoney(new BigDecimal(-leaveMoneyOutA));
//		}
		
		if(leaveMoneyOutA > 0){
			paymentAfter.setId(null);
			paymentAfter.setRecMoney(new BigDecimal(-leaveMoneyOutA));
			paymentAfter.setPayType("1");  //现金
			paymentAfter.setType("1");   //1 预付   2现付 
			paymentAfter.setFeeCode("");
			paymentAfter.setCertCode("");
			paymentAfter.setCostMoney(new BigDecimal(0));
			paymentAfterService.save(paymentAfter);		
	 	}			
		

		if(lle >0){
			paymentAfter.setId(null);
			paymentAfter.setRecMoney(new BigDecimal(lle));
			paymentAfter.setPayType("1");  //现金
			paymentAfter.setType("1");   //1 预付   2现付 
			paymentAfter.setFeeCode("");
			paymentAfter.setCertCode("");
			paymentAfter.setCostMoney(new BigDecimal(0));
			paymentAfterService.save(paymentAfter);	
		}	
		
		
		
		
		
//		paymentAfter.setId(null);
//		paymentAfter.setType("2");    //1 预付   2现付
//		paymentAfter.setPayType(payType);  //现金
//		paymentAfter.setFeeCode(StringUtils.getNullValue(feeCode, ""));
//		paymentAfter.setCertCode(StringUtils.getNullValue(certCode, ""));
//		paymentAfter.setCostMoney(new BigDecimal(costMoney2)); //应付金额
//		if(lle >0){
//			costMoney3 = String.valueOf(Double.parseDouble(StringUtils.getNullValue(costMoney3, "0"))-lle);
//		}
//		paymentAfter.setRecMoney(new BigDecimal(costMoney3)); //实收金额
//		
//		
////		paymentAfter.setUser(UserUtils.getUser());
//		paymentAfterService.save(paymentAfter);
		
		
		
		String[] payments =  payemtDetail.getPayemtDetails().split(";");
		
		
			for (String s:payments){
				String[] ss = s.split(",");
				String id = ss[0];
				String incomeMoney = ss[1];
				String incomeMoney2 = ss[2];
				String feesId = ss[3];
				Fees fees = FeesUtils.getFees(feesId);
				String feedName = fees.getName();
				
				if(Double.parseDouble(StringUtils.getNullValue(incomeMoney, "0")) >0){
					PayemtDetail pDetail = payemtDetailService.get(id);
					pDetail.setIncomeMoney(new BigDecimal(incomeMoney));
					payemtDetailService.save(pDetail);
				}
	
				
				if(Double.parseDouble(StringUtils.getNullValue(incomeMoney2, "0")) >0){
					PaymentAfter paymentAfter2 = new PaymentAfter();
					paymentAfter2.setId(null);
					paymentAfter2.setPayemtDetailId(new Long(id));
					paymentAfter2.setHouse(house);
					paymentAfter2.setUser(u);
					paymentAfter2.setReceDate(receD);
					paymentAfter2.setType("2");    //1 预付   2现付
					paymentAfter2.setPayType(payType);  //现金
					paymentAfter2.setFeedName(feedName);
					paymentAfter2.setFeeCode(StringUtils.getNullValue(feeCode, ""));
					paymentAfter2.setCertCode(StringUtils.getNullValue(certCode, ""));
					paymentAfter2.setCostMoney(new BigDecimal(0)); //应付金额
					paymentAfter2.setRecMoney(new BigDecimal(incomeMoney2)); //实收金额
					paymentAfterService.save(paymentAfter2);	
				}
			}
			
			addMessage(redirectAttributes, "保存'"  + "'成功");	
		

		
		return null;
//		return "redirect:"+Global.getAdminPath()+"/pms/paymentAfter/?repage";
	}
	
	
	@RequestMapping(value = "saveNew2")
	public String saveNew2(PayemtDetail payemtDetail,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes) {
	
	
		String feeCode = request.getParameter("feeCode");
		String certCode = request.getParameter("certCode");
		String receDate = request.getParameter("receDate"); //收款日期
		String payType = request.getParameter("payType"); //收款方式
		String costMoney2 = request.getParameter("costMoney2"); //实收金额

		
		User u = UserUtils.getUser();
		Date receD = new Date();
		try {
			receD = DateUtil.parseDate(receDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] payments =  payemtDetail.getPayemtDetails().split(";");
		
		for (String s:payments){
			String[] ss = s.split(",");
			String id = ss[0];
			String incomeMoney = ss[1];
			String incomeMoney2 = ss[2];
			String houseId = ss[3];
			String feesId = ss[4];
			
			Fees fees = FeesUtils.getFees(feesId);
			String feedName = fees.getName();
			
			if(Double.parseDouble(StringUtils.getNullValue(incomeMoney, "0")) >0){
				PayemtDetail pDetail = payemtDetailService.get(id);
				pDetail.setIncomeMoney(new BigDecimal(incomeMoney));
				payemtDetailService.save(pDetail);				
			}


			
			
			if(Double.parseDouble(StringUtils.getNullValue(incomeMoney2, "0")) >0){
				PaymentAfter paymentAfter = new PaymentAfter();
				paymentAfter.setHouse(new House(houseId));
				paymentAfter.setUser(u);
				paymentAfter.setReceDate(receD);

				paymentAfter.setId(null);
				paymentAfter.setPayemtDetailId(new Long(id));
				paymentAfter.setType("2");    //1 预付   2现付
				paymentAfter.setPayType(payType);  //代扣
				paymentAfter.setFeedName(feedName);
				paymentAfter.setFeeCode(StringUtils.getNullValue(feeCode, ""));
				paymentAfter.setCertCode(StringUtils.getNullValue(certCode, ""));
				paymentAfter.setCostMoney(new BigDecimal(costMoney2)); //应付金额
				paymentAfter.setRecMoney(new BigDecimal(incomeMoney2)); //实收金额
				paymentAfterService.save(paymentAfter);	
			}

			
		}
		
		addMessage(redirectAttributes, "保存'"  + "'成功");	
		return null;
	}
	
	
	@RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(PayemtDetail payemtDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String fileSuffix = isNewExcel?".xlsx":".xls";
		
		String firstDate = request.getParameter("firstDate");
		String lastDate = request.getParameter("lastDate"); 
		String curDateStr = firstDate +"至"+ lastDate;

//		 String curDateStr =  DateUtils.getDate("yyyy年MM月");
		 
		if(payemtDetail.getDevice() == null){payemtDetail.setDevice(new Device());}
		Device device = payemtDetail.getDevice();		  
		 
		String proCompanyId =device.getFees().getCompany().getId();
		String feesId =device.getFees().getId();
//		String deviceType = device.getType();

		Office proCompany = officeService.get(proCompanyId);
		Fees fees = FeesUtils.getFees(feesId);
		
		
//		device.house.unit.buildings.community.id
		String proCompanyName =proCompany.getName();
//		String deviceTypeName= DictUtils.getLable("pms_device_type", deviceType);
		String communityName= device.getHouse().getUnit().getBuildings().getCommunity().getName();
		communityName ="";
		
		
		String feesName = StringUtils.getNullValue(fees.getName(), "");
		
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>feesId 11111>>>>>>>>>>>>>>>>>>>>>>"+ fees.getFeesMode());
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>feesId 22222>>>>>>>>>>>>>>>>>>>>>>"+ deviceTypeName);
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>feesId 3333>>>>>>>>>>>>>>>>>>>>>>"+ feesId);
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>proCompanyName 11111>>>>>>>>>>>>>>>>>>>>>>"+ proCompanyName);
		
		String titleMain = proCompanyName ;
		String titleSub = "收费项目("+curDateStr+ communityName + feesName + ")";
		String title = titleMain + titleSub;
//		String fileName = titleSub + DateUtils.getDate("yyyyMMddHHmmss")+ fileSuffix; 
		feesName = StringUtils.isBlank(feesName)?"":"("+feesName+")";
		String fileName = curDateStr + communityName  +feesName+ fileSuffix;
		
    	try {
    		

    		 fees.setCompany(proCompany);
    		 device.setFees(fees);	 
    		 
    			User user = UserUtils.getUser();
    			if (!user.isAdmin()){
    				device.setCreateBy(user);
    			}
    			
    			

    			DeviceUtils.setObjFromReq(device,  request,  response,  null,2);
    			
    			device.setModel("1");

    			List<PayemtDetail> ls = payemtDetailService.findAll(payemtDetail);
    		
//    		for (Device dev : ls){
//    			dev.setFirstNumStr(dev.getFirstNum());
//    			dev.setFirsDateStr(dev.getFirstDate());
//    			dev.setCurPaymentDateStr(DateUtils.parseDate(DateUtils.formatDate(dev.getPaymentDate(), "yyyy-MM-dd HH:mm:ss")));
//    			dev.setCurDateStr(DateUtils.parseDate(DateUtils.getDate()+" 00:00:00"));
//    		
//    		}
  
//    		System.out.println(">>>>>>>>>>>>>>>>>>>>>exportFile 1111111111 1111111111 >>>>>>>>>>> id >>>>>>>>>>>>>>>>>>>>>>>>>>"+ ls.size());
        	
    		
    		new ExportExcel(title, PayemtDetail.class).setDataList(ls).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出"+ title +"失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pms/user/?repage";
    }


}
