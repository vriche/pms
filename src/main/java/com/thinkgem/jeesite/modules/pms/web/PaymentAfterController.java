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
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.PayemtDetail;
import com.thinkgem.jeesite.modules.pms.entity.PaymentAfter;
import com.thinkgem.jeesite.modules.pms.service.PayemtDetailService;
import com.thinkgem.jeesite.modules.pms.service.PaymentAfterService;
import com.thinkgem.jeesite.modules.sys.entity.User;
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
	private PayemtDetailService payemtDetailService;
	
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

//		System.out.println("houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 getCertCode>>>>>>>>>"+paymentAfter.getCertCode() );
//		System.out.println("houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 getFeeCode>>>>>>"+paymentAfter.getFeeCode() );
		
		
        Page<PaymentAfter> page = paymentAfterService.find(new Page<PaymentAfter>(request, response), paymentAfter); 
        model.addAttribute("page", page);
		return "modules/pms/paymentAfterList";
	}

//	@RequiresPermissions("pms:paymentAfter:view")
	@RequestMapping(value = "form")
	public String form(PaymentAfter paymentAfter, Model model) {
//		String id = paymentAfter.getId();

		if(paymentAfter.getReceDate() == null){
			paymentAfter.setReceDate(new java.util.Date());
		}
				
		paymentAfter.setUser(UserUtils.getUser());
		
//		System.out.println("houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 3333333333333333"+paymentAfter.getHouse().getId() );
		
		model.addAttribute("paymentAfter", paymentAfter);
		return "modules/pms/paymentAfterForm";
	}

//	@RequiresPermissions("pms:paymentAfter:edit")
	@RequestMapping(value = "save")
	public String save(PaymentAfter paymentAfter, Model model, RedirectAttributes redirectAttributes) {
//		if (!beanValidator(model, paymentAfter)){
//			return form(paymentAfter, model);
//		}
		
		
		if(paymentAfter.getReceDate() == null){
			paymentAfter.setReceDate(new java.util.Date());
		}
		
		if(paymentAfter.getRecMoney() == null){
			paymentAfter.setRecMoney(new BigDecimal(0));
		}
				
		paymentAfter.setUser(UserUtils.getUser());
		
		String id = paymentAfter.getId();
		
		
		
		if(id != null && "2".equals(paymentAfter.getType())){
			
			PaymentAfter paymentAfterBak = paymentAfterService.get(id);
			double recMoney = paymentAfter.getRecMoney().doubleValue();
			double recMoneyBak = paymentAfterBak.getRecMoneyBak().doubleValue();
//			double money_new = 0;
			
			System.out.println("save>>>>>>>>>>>>>>>>>>>>>>>>>>>>money>>>>>>>>>>>"+ recMoney);
			System.out.println("save>>>>>>>>>>>>>>>>>>>>>>>>>>>>money_bak>>>>>>>>>>>"+ recMoneyBak);
			
			if(recMoney <= recMoneyBak){
				if(recMoney < recMoneyBak){
					PayemtDetail pDetail = payemtDetailService.get(paymentAfter.getPayemtDetailId().toString());
					double incomeMoney_bak  = pDetail.getIncomeMoney().doubleValue();
					double incomeMoney = incomeMoney_bak -(recMoneyBak-recMoney);
					pDetail.setIncomeMoney(new BigDecimal(incomeMoney));
					payemtDetailService.save(pDetail);
				}
				paymentAfterService.save(paymentAfter);
			}
		}else{
			paymentAfterService.save(paymentAfter);
		}
		
		
	
		addMessage(redirectAttributes, "保存单元信息'" + "'成功");
		return null;
//		return "redirect:"+Global.getAdminPath()+"/pms/paymentAfter/?repage";
	}
	
//	@RequiresPermissions("pms:paymentAfter:edit")
	@RequestMapping(value = "delete")
	public String delete(String id,String payemtDetailId, RedirectAttributes redirectAttributes) {
		
	
		
		System.out.println("delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>id>>>>>>>>>>>"+ id);
		System.out.println("delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>paymentDetailId>>>>>>>>>>>"+ payemtDetailId);
		
		long pid = Long.parseLong(StringUtils.getNullValue(payemtDetailId, "0"));
		if(pid >0){
			PaymentAfter paymentAfter = paymentAfterService.get(id);
//			double payMoney = Arith.roundEVEN(paymentAfter.getRecMoney().doubleValue(), 2); 
			double recMoney = paymentAfter.getRecMoney().doubleValue();
			PayemtDetail payemtDetail = payemtDetailService.get(payemtDetailId);
			double income = payemtDetail.getIncomeMoney().doubleValue();
			payemtDetail.setIncomeMoney(new BigDecimal(income-recMoney));
			System.out.println("delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>recMoney>>>>>>>>>>>"+ recMoney);
			System.out.println("delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>income>>>>>>>>>>>"+ income);
			System.out.println("delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>getIncomeMoney>>>>>>>>>>>"+payemtDetail.getIncomeMoney());
			
			payemtDetailService.save(payemtDetail);
		}
		
		paymentAfterService.delete(id);
		
		addMessage(redirectAttributes, "删除成功");
		
		
		return "redirect:"+Global.getAdminPath()+"/pms/paymentAfter/?repage";
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "getPaymentAfterJson")
	public Map<String, Object> getPaymentAfterJson(String model,HttpServletRequest request, HttpServletResponse response) {	
    
		Map<String, Object> map = Maps.newHashMap();
		StringBuffer sb = new StringBuffer();
		String houseId = request.getParameter("houseId");
		String type = request.getParameter("type");
		String certCode = request.getParameter("certCode");
		String feeCode = request.getParameter("feeCode");

		

		
		PaymentAfter paymentAfter = new PaymentAfter();
		paymentAfter.setHouse(new House(houseId));
		paymentAfter.setType(type);
		paymentAfter.setCertCode(certCode);
		paymentAfter.setFeeCode(feeCode);

		List<PaymentAfter> ls = paymentAfterService.findAll(paymentAfter);
		
		sb.delete(0,sb.length());
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<rows>");  

//		"房间,表编号,业主,上次日期,上次读数,本次读数,使用量";
		int i=0;
		double costMoney = 0;
		double preMoney = 0;
		for(PaymentAfter d:ls){
			System.out.println(">>>>>>>>>>>>>>>>>>>>>getdevicesJson>>>>>>>>>>> d.getHouse() >>>>>>>>>>>>>>>>>>>>>>>>>>"+d.getHouse());
			House h = d.getHouse();
			costMoney = costMoney+d.getCostMoney().doubleValue(); 
			//"序,收费项目,收款单号,发票号 ,收款日期,应付金额,收款金额,收款方式,收款人,操作";
			double recMoney = Arith.roundEVEN(d.getRecMoney().doubleValue(),2);
//			double incomeMoney = Arith.roundEVEN(Double.parseDouble(StringUtils.getNullValue(d.getIncomeMoney() , "0")),2);
			sb.append("<row  id=\""+d.getId()  +"\">");
			sb.append("<cell></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getFeedName(), "") +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getFeeCode(), "") +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getCertCode(), "") +"]]></cell>");
			sb.append("<cell>"+ StringUtils.getNullValue(DateUtils.formatDate(d.getReceDate(), "yyyy-MM-dd"),"" )+"</cell>");
			sb.append("<cell><![CDATA["+ recMoney +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(DictUtils.getLable("pms_pay_type",d.getPayType()), "") +"]]></cell>");
			sb.append("<cell><![CDATA["+ StringUtils.getNullValue(d.getUser().getName(), "") +"]]></cell>");
//			sb.append("<cell src=image/button_delete.gif><![CDATA["+"^javascript:deletePaymentDetail(this)^;" +"]]></cell>");
//			sb.append("<cell><![CDATA["+"^javascript:deletePaymentDetail(this)^;" +"]]></cell>");
			
			String pos = "<a href=\"javascript:void 0\" onClick= javascript:deletePaymentDetail("+ d.getId() +","+ d.getPayemtDetailId() +")>" + "删除"+ "</a>";
			sb.append("<cell><![CDATA["+pos +"]]></cell>");
			
			sb.append("<userdata name=\"paymentDetailId\">"+ d.getPayemtDetailId() +"</userdata>");
			
			sb.append("</row>");
		 }
		
		
		sb.append("</rows>");
		
		
		
		map.put("grid", sb.toString());
//		map.put("costMoney", Arith.roundEVEN(costMoney,2));
//		map.put("preMoney", Arith.roundEVEN(preMoney,2));
		
		
		return map;
		
  }	


}
