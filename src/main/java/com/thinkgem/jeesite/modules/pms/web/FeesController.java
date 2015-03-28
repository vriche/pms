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
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.service.DeviceService;
import com.thinkgem.jeesite.modules.pms.service.FeesService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 收费项目Controller
 * 
 * @author vriche
 * @version 2014-04-16
 */
@Controller
@RequestMapping(value = "${adminPath}/pms/fees")
public class FeesController extends BaseController {

	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private FeesService feesService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private DeviceService deviceService;

	@ModelAttribute
	public Fees get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return feesService.get(id);
		} else {
			return new Fees();
		}
	}

//	@RequiresPermissions("pms:fees:view")
	@RequestMapping(value = { "list", "" })
	public String list(Fees fees, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
//		if (!user.isAdmin()) {
//			fees.setCreateBy(user);
//		}
		
		
		String proCompanyId = request.getParameter("company.id");
		String feesType = request.getParameter("feesType");
		
	
		List<Office> proCompanyList =  Lists.newArrayList(); 
	   
		
		if (user.isAdmin()) {
			proCompanyList.add(officeService.get("1"));
		}
		 proCompanyList.addAll(UserUtils.findProCompanyList());
		
		if(proCompanyId == null){
			if(proCompanyList.size() > 0){
				fees.setCompany(proCompanyList.get(0));
			}

		}
		
		if(feesType == null){
			fees.setFeesType("1");
		}
		

		model.addAttribute("proCompanyList", proCompanyList);
		
		if(fees.getCompany() != null){
			Page<Fees> page = feesService.find(new Page<Fees>(request, response), fees);
			model.addAttribute("page", page); 
		}else{
			model.addAttribute("page", new Page<Fees>()); 
		}
		
		
		return "modules/pms/feesList";
	}
	

	
	

	 @RequestMapping(value = "assignNormal")
	 public String assignNormal(@RequestParam(required = false)   String userId,Model model) {  
		User user = systemService.getUser(userId);
//		System.out.println("userId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>>>>>userId>>>>"+userId);
//        List<Fees> list = user.s();
//        System.out.println("getFeesList>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>>>>>Feeslist>>>>"+list.size());
//        model.addAttribute("list", list);
		model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
        model.addAttribute("user", systemService.getUser(userId));
		return "modules/pms/feesAssignNormalList";
	 }
//	@RequestMapping(value = {"assignNormal", ""})
//	public String assignNormal(Fees fees, HttpServletRequest request, HttpServletResponse response, Model model) {
//		String userId = request.getParameter("id");   
//        List<Fees> list = feesService.findByUserId(userId);
//        model.addAttribute("list", list);
//        model.addAttribute("user", systemService.getUser(userId));
//		return "modules/pms/feesAssignNormalList";
//	}
//	

	

//	@RequiresPermissions("pms:fees:view")
	@RequestMapping(value = "form")
	public String form(Fees fees, Model model) {
		model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
		model.addAttribute("fees", fees);
		return "modules/pms/feesForm";
	}

//	@RequiresPermissions("pms:fees:edit")
	@RequestMapping(value = "save")
	public String save(Fees fees, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, fees)) {
			return form(fees, model);
		}
		
		String proCompanyId = fees.getCompany().getId();
		String feesType = fees.getFeesType();
		BeanValidators.validateWithException(validator, fees);
		feesService.save(fees);
		
//		System.out.println("proCompanyId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>>>>>proCompanyId>>>>"+proCompanyId);
//		System.out.println("feesType>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..>>>>>>feesType>>>>"+feesType);
	
		
		redirectAttributes.addAttribute("company.id",proCompanyId);
		redirectAttributes.addAttribute("feesType",feesType);
		
		addMessage(redirectAttributes, "保存收费项目'" + fees.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/pms/fees/?repage";
	}

//	@RequiresPermissions("pms:fees:edit")
	@RequestMapping(value = "delete")
	public String delete(String id,String proCompanyId, String feesType,RedirectAttributes redirectAttributes) {
		feesService.delete(id);
		addMessage(redirectAttributes, "删除收费项目成功");
		
		redirectAttributes.addAttribute("company.id",proCompanyId);
		redirectAttributes.addAttribute("feesType",feesType);
		
		return "redirect:" + Global.getAdminPath() + "/pms/fees/?repage";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "feesjson")
	public List<Map> getJson(String model,HttpServletRequest request, HttpServletResponse response) {
		
		String proCompanyId = request.getParameter("proCompanyId");
		
		System.out.println("proCompanyId222222222222222222>> 77777777777      >>>>>>>>>>>>>>>>>"+ proCompanyId);
		

    	String houserId = null;
    	response.setContentType("application/json; charset=UTF-8");

		Fees fees = new Fees();
		fees.setCompany(new Office(proCompanyId));
//		List<House> houseList = Lists.newArrayList();
	
    	if("device".equals(model)){
    		houserId = request.getParameter("houserId");
    	}

		return feesService.findListMap(fees,houserId,"1"); 
	}
	
	@ResponseBody
	@RequestMapping(value = "feesjson2")
	public Map<String, Object> getJson2(String model,HttpServletRequest request, HttpServletResponse response) {
    	response.setContentType("application/json; charset=UTF-8");
		Fees fees = new Fees();
		
//		System.out.println("fes.isWithoutPool() 222222222222222222>>>>>>>>>>>>>>>>>>>"+fees.isWithoutPool());
    	
    	String proCompanyId ="";
    	
    	if("build".equals(model)){
   		 proCompanyId = request.getParameter("house.unit.buildings.community.proCompany.id"); 
    	}
    	
    	if("device".equals(model)){
      		 proCompanyId = request.getParameter("fees.company.id"); 
       	}   	

    	if("paymentDetail".equals(model)){
    		 proCompanyId = request.getParameter("device.house.unit.buildings.community.proCompany.id");  
    	}
    	
    	if("deviceDetail".equals(model)){
   		 proCompanyId = request.getParameter("device.house.unit.buildings.community.proCompany.id");  
    	}

//    	System.out.println("proCompanyId>>>>>>>>>>>>>>>>>>>"+proCompanyId);
    	
    	fees.setWithoutPool(true); //排除公摊
//    	System.out.println("fes.isWithoutPool() 222222222222222222>>>>>>>>>>>>>>>>>>>"+fees.isWithoutPool());
		fees.setCompany(new Office(proCompanyId));
		return feesService.getFeesJson(fees);
	}
	
	@ResponseBody
	@RequestMapping(value = "getPriceByFees")
	public Fees getPriceByFees(String id,HttpServletRequest request, HttpServletResponse response) {
    	response.setContentType("application/json; charset=UTF-8");
		return feesService.get(id);
	}
	
	@RequestMapping(value = "initFees")
	public String initFees(HttpServletRequest request, HttpServletResponse response) {
		Fees fees = new Fees();
		String proCompanyId = request.getParameter("company.id");
		String feesType = request.getParameter("feesType");

		Office company = officeService.get("1");
		fees.setCompany(company); 
		List<Fees> ls = feesService.find(fees);
		
		Office company2 = officeService.get(proCompanyId);
		
		//如果当前物业公司下不存在收费项目，则复制默认的费用项目
		fees.setCompany(company2); 
		List<Fees> ls2 = feesService.find(fees);
		Map feesMap = Collections3.extractToMap(ls2,"code");
		for (Fees f : ls) {
			String code = f.getCode();
			Object obj = feesMap.get(code);
			if (obj == null) {
				f.setId(null);
				f.setCompany(company2);
				feesService.save(f);
			}
		}
	

		
		return "redirect:" + Global.getAdminPath() + "/pms/fees/?repage";
	}
	

}
