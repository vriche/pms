/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.service.FeesService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 收费项目Controller
 * 
 * @author vriche
 * @version 2014-04-16
 */
@Controller
@RequestMapping(value = "${adminPath}/pms/feesAssignNormal")
public class FeesAssignNormalController extends BaseController {

	@Autowired
	private FeesService feesService;

	@ModelAttribute
	public Fees get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return feesService.get(id);
		} else {
			return new Fees();
		}
	}

	@RequiresPermissions("pms:fees:view")
	@RequestMapping(value = { "list", "" })
	public String list(Fees fees, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			fees.setCreateBy(user);
		}
		Page<Fees> page = feesService.find(new Page<Fees>(request, response), fees);
		model.addAttribute("page", page); 
		return "modules/pms/feesList";
	}

	@RequiresPermissions("pms:fees:view")
	@RequestMapping(value = "form")
	public String form(Fees fees, Model model) {
		 model.addAttribute("fees", fees);
		return "modules/pms/feesForm";
	}

	@RequiresPermissions("pms:fees:edit")
	@RequestMapping(value = "save")
	public String save(Fees fees, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, fees)) {
			return form(fees, model);
		}
		feesService.save(fees);
		addMessage(redirectAttributes, "保存收费项目'" + fees.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/pms/fees/?repage";
	}

	@RequiresPermissions("pms:fees:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		feesService.delete(id);
		addMessage(redirectAttributes, "删除收费项目成功");
		return "redirect:" + Global.getAdminPath() + "/pms/fees/?repage";
	}

}
