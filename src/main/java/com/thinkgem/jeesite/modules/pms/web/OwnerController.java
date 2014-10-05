/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.pms.web;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.service.CompanyService;
import com.thinkgem.jeesite.modules.pms.service.FeesService;
import com.thinkgem.jeesite.modules.pms.service.HouseService;
import com.thinkgem.jeesite.modules.pms.service.UserService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 用户Controller
 * @author ThinkGem
 * @version 2013-5-31
 */
@Controller
@RequestMapping(value = "${adminPath}/pms/user")
public class OwnerController extends BaseController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private FeesService feesService;
	
	@Autowired
	private HouseService houseService;
	
	@Autowired
	private CompanyService companyService;	
	
	@Autowired
	private SystemService systemService;
	
	
	
	private boolean isNewExcel =  com.thinkgem.jeesite.common.config.Global.getOfficeVersion();
	
	
	@ModelAttribute
	public User get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return userService.getUser(id);
		}else{
			return new User();
		}
	}
	

	
//	@RequiresPermissions("pms:user:view")
	@RequestMapping(value = {"list", ""})
	public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
//		user.setUserType("2");
//		String buildingsId = request.getParameter("buildings");
//		if(buildingsId != null){
//			House house = new House();
//			Unit unit = new Unit();
//			Buildings buildings = new Buildings();
//			buildings.setId(buildingsId);
//			unit.setBuildings(buildings);
//			house.setUnit(unit);
//			List<House> ls =  Lists.newArrayList();
//			ls.add(house);
//		}
		
		
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>getUserType                    >>>"+user.getUserType());
        Page<User> page = userService.findUser(new Page<User>(request, response), user); 
        model.addAttribute("page", page);
		return "modules/pms/userList";
	}
	
	
	


//	@RequiresPermissions("pms:user:view")
	@RequestMapping(value = "form")
	public String form(User user, Model model) {
		if (user.getCompany()==null || user.getCompany().getId()==null){
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice()==null || user.getOffice().getId()==null){
			user.setOffice(UserUtils.getUser().getOffice());
		}
		
		//判断显示的用户是否在授权范围内
		String officeId = user.getOffice().getId();
		User currentUser = UserUtils.getUser();
		if (!currentUser.isAdmin()){
			String dataScope = userService.getDataScope(currentUser);
			//System.out.println(dataScope);
			if(dataScope.indexOf("office.id=")!=-1){
				String AuthorizedOfficeId = dataScope.substring(dataScope.indexOf("office.id=")+10, dataScope.indexOf(" or"));
				if(!AuthorizedOfficeId.equalsIgnoreCase(officeId)){
					return "error/403";
				}
			}
		}
		
		
		
//		 List<Fees> list = feesService.findByUserId(user.getId());
//		 user.setFeesList(list);
//		 user.setFeesIdList(user.getFeesIdList());
		 
//		 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111>>>"+user.getFeesIdList().size());
	     List<Fees> allFees =feesService.findAllList();
	     
//	     if(user.getId() != null){
//		     House house = new House();
//		     house.setOwner(user);
//		     List<House> houseList = houseService.findAllHouse(house);
//		     if(houseList.size()>0){
//		    	 user.setHouseList(houseList);
//		     }
//	     }
	     
//	     System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>user.getHouseList().size()>>>"+user.getHouseList().get(0).getName());

	     
	    model.addAttribute("houseList",user.getHouseList());
//		user.setUserType("2");
		model.addAttribute("user", user);
		model.addAttribute("allRoles", userService.findAllRole());
	    model.addAttribute("allFees",allFees);
		return "modules/pms/userForm";
	}

//	@RequiresPermissions("pms:user:edit")
	@RequestMapping(value = "save")
	public String save(User user, String oldLoginName, String newPassword, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/pms/user/?repage";
		}
		// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
		user.setCompany(new Office(request.getParameter("company.id")));
		user.setOffice(new Office(request.getParameter("office.id")));
		// 如果新密码为空，则不更换密码
		if (StringUtils.isNotBlank(newPassword)) {
			user.setPassword(UserService.entryptPassword(newPassword));
		}
		if (!beanValidator(model, user)){
			return form(user, model);
		}
		if (!"true".equals(checkLoginName(oldLoginName, user.getLoginName()))){
			addMessage(model, "保存用户'" + user.getLoginName() + "'失败，登录名已存在");
			return form(user, model);
		}
		// 角色数据有效性验证，过滤不在授权内的角色
		List<Role> roleList = Lists.newArrayList();
		List<String> roleIdList = user.getRoleIdList();
		for (Role r : userService.findAllRole()){
			if (roleIdList.contains(r.getId())){
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);
		
		

		
//		List<Fees> houseList = Lists.newArrayList();
//		List<String> feesIdList = user.getHouseList()
//		for (House r : houseListService.findAllList()){
//			if (feesIdList.contains(r.getId())){
//				feesList.add(r);
//			}
//		}
//		user.setFeesIdList(feesIdList);
		
		
//		user.setUserType("2");
		// 保存用户信息
		userService.saveUser(user);
		// 清除当前用户缓存
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())){
			UserUtils.getCacheMap().clear();
		}
		addMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/pms/user/?repage";
	}
	
//	@RequiresPermissions("pms:user:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/pms/user/?repage";
		}
		if (UserUtils.getUser().getId().equals(id)){
			addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
		}else if (User.isAdmin(id)){
			addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
		}else{
			userService.deleteUser(id);
			addMessage(redirectAttributes, "删除用户成功");
		}
		return "redirect:"+Global.getAdminPath()+"/pms/user/?repage";
	}
	
//	@RequiresPermissions("pms:user:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileSuffix = isNewExcel?".xlsx":".xls";
            String fileName = "用户数据"+DateUtils.getDate("yyyyMMddHHmmss")+ fileSuffix; 
//    		Page<User> page = userService.findUser(new Page<User>(request, response, -1), user); 
//            new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
    		List ls = userService.findAll();
    		new ExportExcel("用户数据", User.class).setDataList(ls).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pms/user/?repage";
    }

//	@RequiresPermissions("pms:user:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/pms/user/?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<User> list = ei.getDataList(User.class);
			
			
//			List<User> allUserList = userService.findAll();
//			Map userMap = Collections3.extractToMap(allUserList,"loginName");
			
			List<Office> allOfficeList = companyService.findAll();
			Map officeMap = Collections3.extractToMap(allOfficeList,"id");		
//			Map officeMapName = Collections3.extractToMap(allOfficeList,"name");
			Office branch = (Office)officeMap.get("2");
			 List<Role> roleList6 = Lists.newArrayList();
			 List<Role> roleList7 = Lists.newArrayList();
			 
			 roleList6.add(systemService.getRole("6"));
			
			
			for (User user : list){
				
				String loginName = StringUtils.trim(user.getLoginName());
				
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>loginName>>>>>>>>>>111111>>"+ loginName);
//				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>user.getOffice()>>>>>>>>>>111111>>"+ user.getCompanyName());
				try{
					
					
					
//					BigDecimal bd = new BigDecimal(user.getPhone()); 
//					System.out.println(bd.toPlainString()); 
//					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>111111>>"+ bd.toPlainString());
					
					if ("true".equals(checkLoginName("", loginName))){
//					if (!userMap.containsKey(loginName)){	
						user.setPassword(UserService.entryptPassword("123456"));
						user.setEmail(loginName+"@vriche.com");
						user.setUpdateDate(new Date());
						user.setCreateDate(new Date());
						user.setUserType(user.getUserTypeStr());
//						if(user.getCompany() == null){
//							user.setCompany(branch);
//						}
						if(user.getOffice() == null){
							user.setOffice(branch);
						}
						
						if("2".equals(user.getUserType())){
							user.setRoleList(roleList6);
						}else{
							user.setRoleList(roleList7);
						}
						
//						Office branch = new Office();
//						branch.setId("2"); 
						
//						Office company = (Office)officeMapName.get(user.getCompanyName());
//						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>user.getUserType()>>>>>>>>>>111111>>"+ user.getUserType());
//						user.setCompany(company);
//						user.setOffice(branch);
						user.setLoginName(loginName);
						user.setNo(loginName);
						user.setName(StringUtils.trim(user.getName()));
//						user.setUserType("2");
						
//						Office office = (Office)officeMapName.get("2");
						
//						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>user.getOffice()>>>>>>>>>>111111>>"+ branch.getName());
						BigDecimal bd = new BigDecimal(StringUtils.getNullValue(StringUtils.trim(user.getPhone()), "0"));
						String phone = bd.toPlainString();
						phone = "0".equals(phone)?"":phone;
						user.setPhone(phone);
						
					
						
//						user.setCompany(companyService.F);
//						if(StringUtils.isBlank(user.getEmail())){
//							user.setEmail(user.getLoginName()+"@vriche.com");
//						}
//						if(StringUtils.isBlank(user.getNo())){
//							user.setNo(user.getLoginName());
//						}						
						
//						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>222222>>"+user.getPhone());
						
						BeanValidators.validateWithException(validator, user);
						
//						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>222222>>"+user.getPhone());
						
						userService.saveUser(user);
						successNum++;
					}else{
						failureMsg.append("<br/>A登录名 "+ loginName +" 已存在; ");
						failureNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>B登录名 "+ loginName +" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>C登录名 "+ loginName +" 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pms/user/?repage";
    }
	
//	@RequiresPermissions("pms:user:view")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileSuffix = isNewExcel?".xlsx":".xls";
            String fileName = "用户数据导入模板"+fileSuffix;
    		List<User> list = Lists.newArrayList(); list.add(UserUtils.getUser());
    		new ExportExcel("用户数据", User.class, 2).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pms/user/?repage";
    }

	@ResponseBody
//	@RequiresPermissions("pms:user:edit")
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName !=null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName !=null && userService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}

	@RequiresUser
	@RequestMapping(value = "info")
	public String info(User user, Model model) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())){
			if(Global.isDemoMode()){
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/pms/userInfo";
			}
			currentUser = UserUtils.getUser(true);
			currentUser.setEmail(user.getEmail());
			currentUser.setPhone(user.getPhone());
			currentUser.setMobile(user.getMobile());
			currentUser.setRemarks(user.getRemarks());
			userService.saveUser(currentUser);
			model.addAttribute("message", "保存用户信息成功");
		}
		model.addAttribute("user", currentUser);
		return "modules/pms/userInfo";
	}

	@RequiresUser
	@RequestMapping(value = "modifyPwd")
	public String modifyPwd(String oldPassword, String newPassword, Model model) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
			if(Global.isDemoMode()){
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/pms/userModifyPwd";
			}
			if (UserService.validatePassword(oldPassword, user.getPassword())){
				userService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				model.addAttribute("message", "修改密码成功");
			}else{
				model.addAttribute("message", "修改密码失败，旧密码错误");
			}
		}
		model.addAttribute("user", user);
		return "modules/pms/userModifyPwd";
	}
	

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) Long extId, @RequestParam(required=false) Long type, @RequestParam(required=false) Long sort,
			@RequestParam(required=false) Long grade,@RequestParam(required=false) String proCompanyId, HttpServletResponse response) {
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>extId                    >>>"+extId);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>type                    >>>"+type);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>sort                    >>>"+sort);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>grade                    >>>"+grade);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>proCompanyId                    >>>"+proCompanyId);
		


		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
//		User user = UserUtils.getUser();
		List<Office> list = UserUtils.getOfficeList();
		List<User> userList = UserUtils.getUserList();
		
		
		for (int i=0; i<list.size(); i++){
			    Office e = list.get(i);
			    String ot = e.getType();
			    Office parent = e.getParent();
//			    String parentId = e.getParent().getId()
			    
			    Map<String, Object> map = Maps.newHashMap();
			    if(parent != null){
			    	
	
			    if(proCompanyId.equals(parent.getId())){
				    List<Map<String, Object>> chList =  UserUtils.findUserListMap(userList,e.getId());
					if(chList.size() >0 ){
							map.put("isParent", true);
							map.put("children",chList);
					}	
			    }				
			    }
					map.put("id", e.getId());
					map.put("pId", e.getParent()!=null?e.getParent().getId():0);
					map.put("name", e.getName());
					mapList.add(map); 	
		
			    

		
		}
		return mapList;
	}
	

    
//	@InitBinder
//	public void initBinder(WebDataBinder b) {
//		b.registerCustomEditor(List.class, "roleList", new PropertyEditorSupport(){
//			@Autowired
//			private UserService systemService;
//			@Override
//			public void setAsText(String text) throws IllegalArgumentException {
//				String[] ids = StringUtils.split(text, ",");
//				List<Role> roles = new ArrayList<Role>();
//				for (String id : ids) {
//					Role role = systemService.getRole(Long.valueOf(id));
//					roles.add(role);
//				}
//				setValue(roles);
//			}
//			@Override
//			public String getAsText() {
//				return Collections3.extractToString((List) getValue(), "id", ",");
//			}
//		});
//	}
}
