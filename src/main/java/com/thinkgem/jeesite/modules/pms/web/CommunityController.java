/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.web;

import java.util.Date;
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
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.service.CommunityService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 收费项目Controller
 * @author vriche
 * @version 2014-04-18
 */
@Controller
@RequestMapping(value = "${adminPath}/pms/community")
public class CommunityController extends BaseController {

	@Autowired
	private CommunityService communityService;
	
//	@Autowired
//	private OfficeService officeService;
	
	
	private boolean isNewExcel =  com.thinkgem.jeesite.common.config.Global.getOfficeVersion();
	
	@ModelAttribute
	public Community get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return communityService.get(id);
		}else{
			return new Community();
		}
	}
	
//	@RequiresPermissions("pms:community:view")
	@RequestMapping(value = {"list", ""})
	public String list(Community community, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			community.setCreateBy(user);
		}
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>999999999999999999>>>>>>>>>>>>>");
	
		List<Office> proCompanyList = UserUtils.findProCompanyList();
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>999999999999999999>>>>>>>>>>>>>");
		
		String proCompanyId = request.getParameter("proCompany.id");
		if (StringUtils.isNotBlank(proCompanyId)){
			community.setProCompany(new Office(proCompanyId));
		}else{
			if(proCompanyList.size() > 0){
				community.setProCompany(proCompanyList.get(0));
			}
		}
        model.addAttribute("proCompanyList", proCompanyList);
		
        Page<Community> page = communityService.find(new Page<Community>(request, response), community); 
        model.addAttribute("page", page);
        
		return "modules/pms/communityList";
	}

//	@RequiresPermissions("pms:community:view")
	@RequestMapping(value = "form")
	public String form(Community community, Model model) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>999999999999999999>>>>>>>>>>>>>"+community.getProCompany().getId());
		
		model.addAttribute("proCompanyList", UserUtils.findProCompanyList());
		
		model.addAttribute("community", community);
		return "modules/pms/communityForm";
	}

//	@RequiresPermissions("pms:community:edit")
	@RequestMapping(value = "save")
	public String save(Community community, Model model, RedirectAttributes redirectAttributes) {
		
		community.getDevCompany().setId(community.getProCompany().getId());
		
		if (!beanValidator(model, community)){
			return form(community, model);
		}
		communityService.save(community);
		addMessage(redirectAttributes, "保存收费项目'" + community.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/pms/community/?repage";
	}
	
//	@RequiresPermissions("pms:community:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		communityService.delete(id);
		addMessage(redirectAttributes, "删除收费项目成功");
		return "redirect:"+Global.getAdminPath()+"/pms/community/?repage";
	}
	
	
//	@RequiresPermissions("pms:community:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Community community, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileSuffix = isNewExcel?".xlsx":".xls";
            String fileName = "小区数据"+DateUtils.getDate("yyyyMMddHHmmss")+ fileSuffix; 
//    		Page<User> page = userService.findUser(new Page<User>(request, response, -1), user); 
//            new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
    		List ls = communityService.findAllCommunity(null);
    		new ExportExcel("小区数据", Community.class).setDataList(ls).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pms/community/?repage";
    }
    
//	@RequiresPermissions("pms:community:view")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/pms/community/?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Community> list = ei.getDataList(Community.class);
			int code = 1;
		
			
			for (Community community : list){
		
				try{
					if ("true".equals(checkName("", community.getName()))){
//						System.out.println(">>>>>>>>>>>>>>>>>>>>>2222222222");
						community.setUpdateDate(new Date());
						community.setCode("A"+(code++));
//						community.getDevCompany().setId("0");
						community.getDevCompany().setId(community.getProCompany().getId());
						BeanValidators.validateWithException(validator, community);
						communityService.save(community);
						successNum++;
					}else{
						failureMsg.append("<br/>小区名 "+community.getName()+" 已存在; ");
						failureNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>小区名 "+community.getName()+" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>小区名 "+community.getName()+" 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条小区，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条小区"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入小区失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pms/community/?repage";
    }
	    
    public String checkName(String oldLoginName, String loginName) {

		if (loginName !=null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName !=null && communityService.getCommunityByName(loginName) == null) {
			return "true";
		}
		return "false";
	}
    
    
    @ResponseBody
	@RequestMapping(value = "communityjson")
	public Map<String, Object> getCommunityJson(String model,HttpServletRequest request, HttpServletResponse response) {

    	String proCompanyId = null;
    	
    	response.setContentType("application/json; charset=UTF-8");
    	
 
    	if("buildings".endsWith(model)){
    		 proCompanyId = request.getParameter("community.proCompany.id"); 
    	}
    	
    	if("unit".endsWith(model)){
    		proCompanyId = request.getParameter("buildings.community.proCompany.id");
    	}
		
    	if("house".endsWith(model)){
    		proCompanyId = request.getParameter("unit.buildings.community.proCompany.id");
    	} 
    	
    	if("paymentDetail".endsWith(model)){
   		 	proCompanyId = request.getParameter("device.fees.company.id"); 
    	}
    	
    
    	if("device".endsWith(model)){
   		 	proCompanyId = request.getParameter("fees.company.id"); 
    	}
    	
    	if("deviceDetail".endsWith(model)){
   		 	proCompanyId = request.getParameter("device.fees.company.id"); 
    	} 	
    	
		Community community = new Community();
		Office proCompany = new Office();
		proCompany.setId(proCompanyId);
		community.setProCompany(proCompany);
		List<Community> list = communityService.findAllCommunity(community);
		
		Map<String, Object> map = Maps.newHashMap();
		map.put("", "");
		for (int i=0; i<list.size(); i++){
			Community e = list.get(i);
			map.put(e.getId(), e.getName());
		}
		return map;
	}

}
