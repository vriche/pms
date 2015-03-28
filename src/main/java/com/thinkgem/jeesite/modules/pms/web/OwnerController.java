/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.pms.web;

import java.math.BigDecimal;
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
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.service.CompanyService;
import com.thinkgem.jeesite.modules.pms.service.DeviceService;
import com.thinkgem.jeesite.modules.pms.service.FeesService;
import com.thinkgem.jeesite.modules.pms.service.HouseService;
import com.thinkgem.jeesite.modules.pms.service.UserService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
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
	
	@Autowired
	private DeviceService deviceService;
	
	
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
        String houseIds = request.getParameter("houseIds");
		String companyId = request.getParameter("proCompanyId");
		String userType = request.getParameter("userType");
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>companyId   >>>"+ companyId);
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>houseIds.labelName   >>>"+request.getParameter("labelName"));
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>extId   >>>"+request.getParameter("extId"));
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>checked   >>>"+request.getParameter("checked"));
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>selectIds   >>>"+request.getParameter("selectIds"));
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>nodesLevel   >>>"+request.getParameter("nodesLevel"));
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>proCompanyId   >>>"+request.getParameter("proCompanyId"));
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>house.name   >>>"+request.getParameter("house.name"));
		
		
		if(companyId != null){
			user.setCompany(companyService.get(companyId)); 
		}
		
		if(houseIds != null){
			String labelName = request.getParameter("house.name");
			House house = new House(houseIds);
			house.setName(labelName);
			user.setHouse(house);
		}
		
	
        Page<User> page = userService.findUser(new Page<User>(request, response), user); 
        
   
        
        
        model.addAttribute("page", page);
    	model.addAttribute("proCompanyId", companyId);
    	model.addAttribute("company", companyId);
    	model.addAttribute("company.id", companyId);
    	model.addAttribute("houseIds", houseIds);
    	model.addAttribute("userType", userType);

    	
    	
		
//		model.addAttribute("url", request.getParameter("url")); 	// 树结构数据URL
//		model.addAttribute("extId", request.getParameter("extId")); // 排除的编号ID
//		model.addAttribute("checked", request.getParameter("checked")); // 是否可复选
//		model.addAttribute("selectIds", request.getParameter("selectIds")); // 指定默认选中的ID
//		model.addAttribute("nodesLevel", request.getParameter("nodesLevel"));	// 菜单展开层数
//		model.addAttribute("proCompanyId", request.getParameter("proCompanyId")); 	// 树结构数据URL
    	
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
//	     System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111>>>"+user.getId());
//	     System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111>>>"+user.getHouseList().size());
	     
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
		
		
//		String proCompanyId = user.getCompany().getId();
//		redirectAttributes.addAttribute("proCompanyId",proCompanyId);
//		System.out.print(">>>>>>>>>>>>>>>>>>>>>>00000000000000000 111111111111111 proCompanyId>>>>>>>>>>>>>>"+request.getParameter("company.id"));
		
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

    		
            if(user == null) user = new User();

            String houseIds = request.getParameter("houseIds");
    		String companyId = request.getParameter("proCompanyId");
//    		String companyId = request.getParameter("company.id");
    		String userType = request.getParameter("userType");
    		

//    		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>houseIds   >>>"+request.getParameter("houseIds"));
//    		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>houseIds   >>>"+user.getHouseIds());
    		
    		user.setHouseIds(houseIds);
    		 
    		
    		Office company = user.getCompany();
//    		Office office = user.getOffice();
    		if(company == null) company = new Office("1");

    		
    		if (StringUtils.isNotBlank(companyId)){
    			user.setCompany(new Office(companyId));
    		}

    		if (StringUtils.isNotBlank(userType)){
    			user.setUserType(userType);
    		}			            

   
    		List<User> ls = userService.findAllUser(user);
//    		List<User> ls = userService.findAllUser(user);

//    		List<User> ls = userService.findAll();
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
			Map officeMap = Collections3.extractToMap(allOfficeList,"code");	
			
			
			 List<Role> roleList6 = Lists.newArrayList();
			 List<Role> roleList7 = Lists.newArrayList();
			 
			 roleList6.add(systemService.getRole("6"));
			
			
			for (User user : list){
				String loginName = StringUtils.trim(user.getLoginName());
				BigDecimal bddd = new BigDecimal(loginName);
				loginName = bddd.toPlainString();

//				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>loginName>>>>>>>>>>111111>>"+ loginName+"_"+DictUtils.getDictValue(user.getUserType(), "sys_user_type", ""));
//				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>user.getOffice()>>>>>>>>>>111111>>"+ user.getCompanyName());
				try{
//					BigDecimal bd = new BigDecimal(user.getPhone()); 
//					System.out.println(bd.toPlainString()); 
//					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>111111>>"+ bd.toPlainString());
					String companyCode = user.getCompanyCode();
					Office branch = new Office();
					if(StringUtils.isBlank(companyCode)){
						branch = (Office)officeMap.get("0");
					}else{
						branch = (Office)officeMap.get(companyCode);
					}

					user.setUserType(DictUtils.getDictValue(user.getUserType(), "sys_user_type", ""));
					user.setCompany(branch);
					user.setOffice(branch);
					
					if("2".equals(user.getUserType())){
						user.setRoleList(roleList6);
					}else{
						user.setRoleList(roleList7);
					}
					user.setLoginName(loginName);
					
					String userName = StringUtils.trim(user.getName());
					if(StringUtils.isBlank(userName)){
						user.setName(loginName);
					}else{
						user.setName(userName);
					}

					try{
						BigDecimal bd = new BigDecimal(StringUtils.getNullValue(StringUtils.trim(user.getPhone()), "0"));
						String phone = bd.toPlainString();
						phone = "0".equals(phone)?"":phone;
						user.setPhone(phone);	
					}catch (Exception ex) {
						user.setPhone(StringUtils.trim(user.getPhone()));	
					}

					
					try{
						BigDecimal bd2 = new BigDecimal(StringUtils.getNullValue(StringUtils.trim(user.getMobile()), "0"));
						String mobile = bd2.toPlainString();
						mobile = "0".equals(mobile)?"":mobile;
						user.setMobile(mobile);	
					}catch (Exception ex) {
						user.setMobile(StringUtils.trim(user.getMobile()));	
					}
					
					try{
						BigDecimal bd3 = new BigDecimal(StringUtils.getNullValue(StringUtils.trim(user.getMobile2()), "0"));
						String mobile2 = bd3.toPlainString();
						mobile2 = "0".equals(mobile2)?"":mobile2;
						user.setMobile2(mobile2);	
					}catch (Exception ex) {
						user.setMobile2(StringUtils.trim(user.getMobile2()));	
					}
					
					
					
//					if ("true".equals(checkLoginName("", loginName))){
//					user.setPassword(UserService.entryptPassword("123456"));
//					user.setEmail(loginName+"@vriche.com");
//					successNum++;
//				}else{
//					User u = UserUtils.getUserByLoginName(loginName);
//					user.setId(u.getId());
//					user.setPassword(u.getPassword());
//					failureMsg.append("<br/>A登录名 "+ loginName +" 已存在; ");
//					failureNum++;
//				}				
					
					
					
					User u = UserUtils.getUserByLoginName(loginName);

					if(u == null){
						user.setPassword(UserService.entryptPassword("123456"));
						user.setEmail(loginName+"@vriche.com");
						successNum++;
						
//						String curPhone = user.getPhone();
//						Device device = new Device();
//						device.setCode(curPhone);
//						deviceService.save(device);
					}else{
						user.setId(u.getId());
						user.setPassword(u.getPassword());
						failureMsg.append("<br/>A登录名 "+ loginName +" 已存在; ");
						failureNum++;
						//由于设备中的电话费用的编码采用电话编码，如果电话改变需要同时改变设备中的电话编码
						String oldPhone = u.getPhone();
						String curPhone = user.getPhone();
						if(StringUtils.isNotBlank(oldPhone)&&StringUtils.isNotBlank(curPhone)&&!curPhone.equals(oldPhone)){
							Device device = deviceService.findByPhone(oldPhone); 
							device.setCode(curPhone);
							deviceService.save(device);
						}
						
					}

					
					BeanValidators.validateWithException(validator, user);
					userService.saveUser(user);
					
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
	
	@ResponseBody
//	@RequiresPermissions("pms:user:edit")
	@RequestMapping(value = "checkPhone")
	public String checkPhone(String oldPhone, String curPhone) {
		if (curPhone !=null && curPhone.equals(oldPhone)) {
			return "true";
		} else if (curPhone !=null && userService.getUserByLoginName(curPhone) == null) {
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
					map.put("name", "("+e.getCode()+")"+e.getName());
					mapList.add(map); 	
		
			    

		
		}
		return mapList;
	}
	
	@ResponseBody
	@RequestMapping(value = "getHousesByUser")
	public Map<String, String> getHousesByUser(String loginName,HttpServletRequest request, HttpServletResponse response) {	 
		Map<String, String> map = Maps.newHashMap();
		
		map.put("count","0");
		
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>getHousesByUser    loginName           >>>"+ loginName);
		
		User owner = userService.getUserByLoginName(loginName);
		
		if(owner != null){
			
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>getHousesByUser       owner.getName()             >>>"+owner.getName());
			
			House h = new House();
			h.setOwner(owner);
			List<House> houseList =houseService.findAllHouse(h);
			
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>getHousesByUser      houseList.size()             >>>"+houseList.size());
			
//			House[] array= (House[]) houseList.toArray();
			
//			House[] array = houseList.toArray(new House[houseList.size()]);
			
			JsonMapper jsonMapper = JsonMapper.getInstance();

//			var houseId = data.houses[i].id;
//			var officeId =  data.user.company.id;
			
			int count =  houseList.size();
			int k = 0;
			
			
			StringBuffer sb = new StringBuffer();
			sb.append("[");
            for(House house:houseList){
            	k++;
            	User user = house.getOwner();
            	Map<String,String> mpp = Maps.newHashMap();
            	
            	String deviceType = "3".equals(user.getUserType())?"2":"3";
            	mpp.put("houseId", house.getId());
            	mpp.put("houseFullName", house.getFullName());
            	mpp.put("ownerName", user.getName());
            	mpp.put("officeId",  user.getCompany().getId());
            	mpp.put("deviceType", deviceType);
            	
    			sb.append(jsonMapper.toJson(mpp));
    			if(count>1&&k<count)  sb.append(",");
            }
            sb.append("]");
            
        	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>getHousesByUser      houseList.size()             >>>"+sb.toString());
            
        	map.put("count",count+"");
        	map.put("data",sb.toString());

		}
		

	
		return map;
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
