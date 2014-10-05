/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.pms.entity.PaymentBefor;
import com.thinkgem.jeesite.modules.pms.service.PaymentBeforService;

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
	@RequestMapping(value = "form")
	public String form(PaymentBefor paymentBefor, Model model) {
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
		paymentBeforService.save(paymentBefor);
//		addMessage(redirectAttributes, "保存单元信息'" + paymentBefor.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/pms/paymentBefor/?repage";
	}
	
//	@RequiresPermissions("pms:paymentBefor:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		paymentBeforService.delete(id);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:"+Global.getAdminPath()+"/pms/paymentBefor/?repage";
	}

}
