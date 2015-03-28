/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.Arith;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.PaymentAfter;
import com.thinkgem.jeesite.modules.pms.entity.PaymentBefor;
import com.thinkgem.jeesite.modules.pms.service.PaymentAfterService;
import com.thinkgem.jeesite.modules.pms.service.PaymentBeforService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 单元信息Controller
 * @author vriche
 * @version 2014-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/pms/paymentBefor")
public class PaymentBeforController extends BaseController {

	@Autowired
	private PaymentBeforService paymentBeforService;
	
	@Autowired
	private PaymentAfterService paymentAfterService;
	
	@ModelAttribute
	public PaymentBefor get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return paymentBeforService.get(id);
		}else{
			return new PaymentBefor();
		}
	}
	
//	@RequiresPermissions("pms:paymentBefor:view")
	@RequestMapping(value = {"list", ""})
	public String list(PaymentBefor paymentBefor, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			paymentBefor.setCreateBy(user);
		}
        Page<PaymentBefor> page = paymentBeforService.find(new Page<PaymentBefor>(request, response), paymentBefor); 
        model.addAttribute("page", page);
		return "modules/pms/paymentBeforList";
	}
	
	

//	@RequiresPermissions("pms:paymentBefor:view")
	@RequestMapping(value = {"list2", ""})
	public String list2(PaymentBefor paymentBefor, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			paymentBefor.setCreateBy(user);
		}
		
//		String officeId = request.getParameter("company.id");
//		String houseId = request.getParameter("house.id");
//		String type = request.getParameter("type");
//		
//		System.out.println("list2>>>>>>>>>>>>>>officeId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ paymentBefor.getCompany().getId());
//		System.out.println("list2>>>>>>>>>>>>>>houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ paymentBefor.getHouse().getId());
//		System.out.println("list2>>>>>>>>>>>>>>type>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ paymentBefor.getType());
//		
//		paymentBefor.setCompany(new Office(officeId));
//		paymentBefor.setType(type);
//		paymentBefor.setHouse(new House(houseId));

		
		if(paymentBefor.getFirstDate() == null){
			String firstDay = DateUtils.getFirstDay();
			String lastDay = DateUtils.getLastDay();
			paymentBefor.setFirstDate(DateUtils.parseDate(firstDay));
			paymentBefor.setLastDate(DateUtils.parseDate(lastDay));
		}

		
        Page<PaymentBefor> page = paymentBeforService.find(new Page<PaymentBefor>(request, response), paymentBefor); 

        
        PaymentAfter paymentAfterParam = new PaymentAfter();
        paymentAfterParam.setPaymentBefor(paymentBefor);
        Map<String,PaymentAfter> mp  = paymentAfterService.findGroupByBeforId(paymentAfterParam);

        List<PaymentBefor> ls = page.getList();
    	for(PaymentBefor pBefor:ls){
    		 PaymentAfter pAfter2 = mp.get(pBefor.getId());
    		 if(pAfter2 != null){
    			 pBefor.setPayMoney(pAfter2.getRecMoney());
    		 }
		}
    	
    	Iterator it2 = mp.values().iterator();
    	double sumPayMoney =0;
        while(it2.hasNext()){
        	PaymentAfter After2 = (PaymentAfter)it2.next();
        	sumPayMoney+=After2.getRecMoney().doubleValue();
        }
        
        String sumPayMoneyStr = Arith.roundEVEN(sumPayMoney, 2)+"";
        
//        
        PaymentBefor pBefor = paymentBeforService.find(paymentBefor);
//        pBefor.setPayMoney(new BigDecimal(sumPayMoney));
        
        System.out.println(">>>>>>>>>>>>>>mp.size()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+mp.size());
        
        List fromList = new ArrayList();
    
        Fees fees = new Fees();
        fees.setId("1");fees.setName("单位");
        fromList.add(fees);
        Fees fees2 = new Fees();
        fees2.setId("2");fees2.setName("个人");
        fromList.add(fees2);
        Fees fees3 = new Fees();
        fees3.setId("0");fees3.setName("所有");
        fromList.add(fees3);
//        ion value='1'>单位</option>
//		  <option value='2'>个人</option>
//		  <option value='0'>所有</option>
//        System.out.println(">>>>>>>>>>>>paymentBefor.getPayFrom()paymentBefor.getPayFrom()paymentBefor.getPayFrom()paymentBefor.getPayFrom()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+paymentBefor.getPayFrom());
		  
    
        
        model.addAttribute("fromList", fromList);  
        
        model.addAttribute("page", page);
        model.addAttribute("totalRecMoney", pBefor.getRecMoney());
        model.addAttribute("totalPayMoney", sumPayMoneyStr);
//        model.addAttribute("paymentBefor", paymentBefor);
		return "modules/pms/paymentBefoList2";
	}

//	@RequiresPermissions("pms:paymentBefor:view")
	@RequestMapping(value = "form")
	public String form(PaymentBefor paymentBefor, Model model) {
		String id = paymentBefor.getId();
		if(StringUtils.isEmpty(id)){
			paymentBefor.setReceDate(new Date());
		}
		model.addAttribute("paymentBefor", paymentBefor);
		return "modules/pms/paymentBeforForm";
	}
	
	
	@RequestMapping(value = "payment")
	public String payment(PaymentBefor paymentBefor, Model model) {
		model.addAttribute("paymentBefor", paymentBefor);
		return "modules/pms/paymentForm";
	}	
	
	

//	@RequiresPermissions("pms:paymentBefor:edit")
	@RequestMapping(value = "save")
	public String save(PaymentBefor paymentBefor, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, paymentBefor)){
			return form(paymentBefor, model);
		}
		House house = paymentBefor.getHouse();
		Office company = paymentBefor.getCompany();
		User user = paymentBefor.getUser();
		
		System.out.println(">>>>>>>>>>>>>>house>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ house.getId());
		System.out.println(">>>>>>>>>>>>>>house>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ company.getId());
		System.out.println(">>>>>>>>>>>>>>house>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ user.getId());
		
		
		if(StringUtils.isBlank(house.getId()) || "null".equals(house.getId())){
			house.setId("0");
		}
		if(StringUtils.isBlank(company.getId())  || "null".equals(company.getId())){
			company.setId("0");
		}
		
		if(StringUtils.isBlank(user.getId()) || "null".equals(user.getId())){
			User u = UserUtils.getUser();
			user.setId(u.getId());
		}
		
		paymentBeforService.save(paymentBefor);

		addMessage(redirectAttributes, "保存收款单成功");

		String type = paymentBefor.getType();
		String houseId = paymentBefor.getHouse().getId();
		String officeId = paymentBefor.getCompany().getId();
		redirectAttributes.addAttribute("type", type);
		redirectAttributes.addAttribute("company.id", officeId);
		redirectAttributes.addAttribute("house.id", houseId);

		
		return "redirect:"+Global.getAdminPath()+"/pms/paymentBefor/list2";
//		return "redirect:"+Global.getAdminPath()+"/pms/paymentBefor/list2?type="+type +"&company.id="+officeId +"&house.id="+ houseId;
	}
	
//	@RequiresPermissions("pms:paymentBefor:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		
		PaymentBefor paymentBefor = paymentBeforService.get(id);
		String type = paymentBefor.getType();
		String houseId = paymentBefor.getHouse().getId();
		
		if("2".equals(type)){
			paymentBeforService.delete(id);
		}else{
			paymentBeforService.deleteAllData(id);
		}
	
		
		addMessage(redirectAttributes, "删除成功");

		return "redirect:"+Global.getAdminPath()+"/pms/paymentBefor/list2?type=2&house.id="+houseId;
	}
	
	@RequestMapping(value = "deleteAllData")
	public String deleteAllData(String id, RedirectAttributes redirectAttributes) {
		paymentBeforService.deleteAllData(id);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:"+Global.getAdminPath()+"/pms/paymentBefor/?repage";
	}
}
