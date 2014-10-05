/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.pms.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
//import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.service.CompanyService;

/**
 * 机构Controller
 * @author ThinkGem
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/pms/office")
public class CompanyController extends BaseController {
	
	private boolean isNewExcel =  com.thinkgem.jeesite.common.config.Global.getOfficeVersion();

	@Autowired
	private CompanyService companyService;
	
	@ModelAttribute("office")
	public Office get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return companyService.get(id);
		}else{
			return new Office();
		}
	}

//	@RequiresPermissions("pms:office:view")
	@RequestMapping(value = {"list", ""})
	public String list(Office office, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Office> page = companyService.find(new Page<Office>(request,response), office);
		model.addAttribute("page", page);
		return "modules/pms/officeList";
	}

//	@RequiresPermissions("pms:office:view")
	@RequestMapping(value = "form")
	public String form(Office office, Model model) {
		User user = UserUtils.getUser();
		if (office.getParent()==null || office.getParent().getId()==null){
			office.setParent(user.getOffice());
		}
		office.setParent(companyService.get(office.getParent().getId()));
		if (office.getArea()==null){
			office.setArea(office.getParent().getArea());
		}
		model.addAttribute("office", office);
		return "modules/pms/officeForm";
	}
	
//	@RequiresPermissions("pms:office:edit")
	@RequestMapping(value = "save")
	public String save(Office office, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/pms/office/";
		}
		if (!beanValidator(model, office)){
			return form(office, model);
		}
		companyService.save(office);
		addMessage(redirectAttributes, "保存机构'" + office.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/pms/office/";
	}
	
	
	
//	@RequiresPermissions("pms:office:edit")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Office office, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileSuffix = isNewExcel?".xlsx":".xls";
            String fileName = "机构信息"+DateUtils.getDate("yyyyMMddHHmmss")+ fileSuffix; 
    		List<Office> ls = companyService.findAll(); 
    	
    		new ExportExcel("机构信息数据", Office.class).setDataList(ls).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出机构信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pms/office/?repage";
    }	
	

//	@RequiresPermissions("pms:office:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		
		
		 
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/pms/office/?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Office> list = ei.getDataList(Office.class);
			
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>222222>>"+list.size());
			 
			for (Office office : list){
				try{
					 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>3"+office.getName());
					 List<Office> ls =  companyService.findCompany(office); 
					 
					 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>4>>>>>>"+ls.size());
				
					if (ls.size() == 0){
						office.setCreateDate(new Date());
						Office parent = new Office();parent.setId("2");
						office.setParent(parent);
						office.setSort("2");
						office.setIsCharge("1");
						BeanValidators.validateWithException(validator, office);
						companyService.save(office);
						successNum++;
					}else{
						failureMsg.append("<br/>公司名 "+office.getName()+" 已存在; ");
						failureNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>公司名 "+office.getName()+" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>公司名 "+office.getName()+" 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条公司名，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条公司名"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入公司名失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pms/office/?repage";
    }
	
//	@RequiresPermissions("pms:office:view")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileSuffix = isNewExcel?".xlsx":".xls";
            String fileName = "机构信息数据导入模板"+fileSuffix;
    		List<Office> list = Lists.newArrayList(); list.add( new Office());
    		new ExportExcel("机构信息数据", Office.class, 2).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pms/office/?repage";
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@RequiresPermissions("pms:office:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/pms/office/";
		}
		if (Office.isRoot(id)){
			addMessage(redirectAttributes, "删除机构失败, 不允许删除顶级机构或编号空");
		}else{
			companyService.delete(id);
			addMessage(redirectAttributes, "删除机构成功");
		}
		return "redirect:"+Global.getAdminPath()+"/pms/office/";
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) Long extId, @RequestParam(required=false) Long type, @RequestParam(required=false) Long sort,
			@RequestParam(required=false) Long grade, HttpServletResponse response) {
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>extId                    >>>"+extId);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>type                    >>>"+type);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>sort                    >>>"+sort);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>grade                    >>>"+grade);
		
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>grade                    >>>"+Integer.parseInt(e.getType()));
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>grade                    >>>"+grade);
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>grade                    >>>"+grade);
		
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
//		User user = UserUtils.getUser();
		List<Office> list = companyService.findAll();
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);
//			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>e.getSort()                    >>>"+Integer.parseInt(e.getSort()));
//			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>sort.intValue()                    >>>"+sort.intValue());
			
			if ((extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))
//					&& (sort == null || (sort != null && Integer.parseInt(e.getSort()) <=s sort.intValue()))
					&& (type == null || (type != null && Integer.parseInt(e.getType()) <= type.intValue()))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
//				map.put("pId", !user.isAdmin() && e.getId().equals(user.getOffice().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	
	   @ResponseBody
		@RequestMapping(value = "companyjson")
		public Map<String, Object> getCompanyJson(String model,HttpServletRequest request, HttpServletResponse response) {

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
	   		 	proCompanyId = request.getParameter("device.house.unit.buildings.community.proCompany.id"); 
	    	}
	    	
	    
	    	if("device".endsWith(model)){
	   		 	proCompanyId = request.getParameter("fees.company.id"); 
	    	}
	    	

			List<Office> list = UserUtils.findCompanyListByProCompany(proCompanyId);
			
			Map<String, Object> map = Maps.newHashMap();
			map.put("", "");
			for (int i=0; i<list.size(); i++){
				Office e = list.get(i);
				map.put(e.getId(), e.getName());
			}
			return map;
		}

}
