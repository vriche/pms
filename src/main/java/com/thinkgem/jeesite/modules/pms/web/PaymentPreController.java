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
import com.thinkgem.jeesite.modules.pms.entity.PaymentPre;
import com.thinkgem.jeesite.modules.pms.service.PaymentPreService;

/**
 * 单元信息Controller
 * @author vriche
 * @version 2014-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/pms/paymentPre")
public class PaymentPreController extends BaseController {

	@Autowired
	private PaymentPreService paymentPreService;
	
	@ModelAttribute
	public PaymentPre get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return paymentPreService.get(id);
		}else{
			return new PaymentPre();
		}
	}
	
//	@RequiresPermissions("pms:paymentPre:view")
	@RequestMapping(value = {"list", ""})
	public String list(PaymentPre paymentPre, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			paymentPre.setCreateBy(user);
		}
        Page<PaymentPre> page = paymentPreService.find(new Page<PaymentPre>(request, response), paymentPre); 
        model.addAttribute("page", page);
		return "modules/pms/paymentPreList";
	}

//	@RequiresPermissions("pms:paymentPre:view")
	@RequestMapping(value = "form")
	public String form(PaymentPre paymentPre, Model model) {
		model.addAttribute("paymentPre", paymentPre);
		return "modules/pms/paymentPreForm";
	}

//	@RequiresPermissions("pms:paymentPre:edit")
	@RequestMapping(value = "save")
	public String save(PaymentPre paymentPre, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, paymentPre)){
			return form(paymentPre, model);
		}
		paymentPreService.save(paymentPre);
		addMessage(redirectAttributes, "保存单元信息'" + paymentPre.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/pms/paymentPre/?repage";
	}
	
//	@RequiresPermissions("pms:paymentPre:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		paymentPreService.delete(id);
		addMessage(redirectAttributes, "删除单元信息成功");
		return "redirect:"+Global.getAdminPath()+"/pms/paymentPre/?repage";
	}

}
