/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.IdEntity;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.common.utils.excel.fieldtype.OfficeType;
import com.thinkgem.jeesite.common.utils.excel.fieldtype.RoleListType;
import com.thinkgem.jeesite.modules.pms.entity.House;

/**
 * 用户Entity
 * @author ThinkGem
 * @version 2013-5-15
 */
@Entity
@Table(name = "sys_user")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends IdEntity<User> {

	private static final long serialVersionUID = 1L;
	private Office company;	// 归属公司
	private Office office;	// 归属部门
	private String loginName;// 登录名
	private String password;// 密码
	private String no;		// 工号
	private String name;	// 姓名
	private String email;	// 邮箱
	private String phone;	// 电话
	private String mobile;	// 手机
	private String mobile2;	// 手机
	private String userType;// 用户类型
	private String loginIp;	// 最后登陆IP
	private Date loginDate;	// 最后登陆日期
	
	private String paperworkCode;	// 证件号码
	
	


	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表

	private List<House> houseList = Lists.newArrayList(); // 拥有费用列表
	
	private String houseIds;
	
	private String userTypeStr;// 用户类型
	
	
//	private List<Fees> feesList = Lists.newArrayList(); // 拥有费用列表  根据房产查找费用   因为费用是直接跟房产挂钩 一个房产可以拥有多个设备，也包含公摊设备

	
	




	public User() {
		super();
	}
	
	public User(String id) {
		this();
		this.id = id;
	}
	
	
	
	@OneToMany(mappedBy = "owner", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="code") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<House> getHouseList() {
		return houseList;
	}

	public void setHouseList(List<House> houseList) {
		this.houseList = houseList;
	}
	
	

//	public List<Fees> getFeesList() {
//		List<Fees> feList = Lists.newArrayList();
//		for (House house : houseList) {
//			List<Device> devList = house.getDeviceList();
//			for (Device device : devList) {
//				feList.add(device.getFees());
//			}
//		}
//		return feList;
//	}
//
//	public void setFeesList(List<Fees> feesList) {
//		this.feesList = feesList;
//	}


	@ManyToOne
	@JoinColumn(name="company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull(message="归属公司不能为空")
//	@ExcelField(title="归属公司", align=2, sort=20,value="Office.id")
	@ExcelField(title="归属公司", align=2, sort=1, fieldType=OfficeType.class)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}
	
	@ManyToOne
	@JoinColumn(name="office_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
//	@NotNull(message="归属部门不能为空")
	@ExcelField(title="归属部门", align=2, sort=8, fieldType=OfficeType.class)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@Length(min=1, max=100)
	@ExcelField(title="登录名", align=2, sort=2)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JsonIgnore
	@Length(min=1, max=100)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(min=1, max=100)
	@ExcelField(title="姓名", align=2, sort=3)
	public String getName() {
		return name;
	}
	
	
//	@Length(min=1, max=100)
	@ExcelField(title="证件号码", align=2, sort=4)
	public String getPaperworkCode() {
		return paperworkCode;
	}

	public void setPaperworkCode(String paperworkCode) {
		this.paperworkCode = paperworkCode;
	}
	
	
	@Length(min=0, max=200)
	@ExcelField(title="电话", align=2, sort=5)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

    @Length(min=0, max=200)
	@ExcelField(title="手机", align=2, sort=6)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
    @Length(min=0, max=200)
	@ExcelField(title="手机2", align=2, sort=6)
	public String getMobile2() {
		return mobile2;
	}

	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}
	@Email @Length(min=0, max=200)
	@ExcelField(title="邮箱", align=1, sort=7)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	@Length(min=1, max=100)
	@ExcelField(title="工号", align=2, sort=9)
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}



	
	@Transient
	@ExcelField(title="备注", align=1, sort=13)
	public String getRemarks() {
		return remarks;
	}
	
	@Length(min=0, max=100)
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Transient
	@ExcelField(title="用户类型", align=2, sort=10)
	public String getUserTypeStr() {
		return userTypeStr;
	}

	public void setUserTypeStr(String userTypeStr) {
		this.userTypeStr = userTypeStr;
	}
	
	@Transient
	@ExcelField(title="创建时间", type=0, align=1, sort=11)
	public Date getCreateDate() {
		return createDate;
	}

	@ExcelField(title="最后登录IP", type=1, align=1, sort=100)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="最后登录日期", type=1, align=1, sort=11)
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	


	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sys_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnore
	@ExcelField(title="拥有角色", align=1, sort=12, fieldType=RoleListType.class)
	public List<Role> getRoleList() {
		return roleList;
	}
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@Transient
	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	@Transient
	public void setRoleIdList(List<String> roleIdList) {
		roleList = Lists.newArrayList();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	
	
//	@Transient
//	@JsonIgnore
//	public List<String> getFeesIdList() {
//		List<String> feesIdList = Lists.newArrayList();
//		for (Fees fees : feesList) {
//			feesIdList.add(fees.getId());
//		}
//		return feesIdList;
//	}
//
//	@Transient
//	public void setFeesIdList(List<String> feesIdList) {
//		feesList = Lists.newArrayList();
//		for (String feesId : feesIdList) {
//			Fees fees = new Fees();
//			fees.setId(feesId);
//			feesList.add(fees);
//		}
//	}
	
	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	@Transient
	public String getRoleNames() {
		return Collections3.extractToString(roleList, "name", ", ");
	}
	
	@Transient
	public boolean isAdmin(){
		return isAdmin(this.id);
	}
	
	@Transient
	public static boolean isAdmin(String id){
		return id != null && id.equals("1");
	}
	

	
	@Transient
	@JsonIgnore
	public List<String> getHouseIdList() {
		List<String> houseIdList = Lists.newArrayList();
		for (House house : houseList) { 
			houseIdList.add(house.getId());
		}
		return houseIdList;
	}

	@Transient
	public void setHouseIdList(List<String> houseIdList) {
		houseList = Lists.newArrayList();
		for (String id : houseIdList) {
			House house = new House();
			house.setId(id);
			houseList.add(house);
		}
	}
	
	@Transient
	public String getHouseIds() {
		return houseIds;
	}
	@Transient
	public void setHouseIds(String houseIds) {
		this.houseIds = houseIds;
	}

	@Override
	public String toString() {
		return "User [company=" + company + ", office=" + office
				+ ", loginName=" + loginName + ", password=" + password
				+ ", no=" + no + ", name=" + name + ", email=" + email
				+ ", phone=" + phone + ", mobile=" + mobile + ", userType="
				+ userType + ", loginIp=" + loginIp + ", loginDate="
				+ loginDate + ", paperworkCode=" + paperworkCode
				+ ", roleList=" + roleList + ", houseList=" + houseList + "]";
	}
	
	
	
//	@Override
//	public String toString() {
//		return ToStringBuilder.reflectionToString(this);
//	}
}