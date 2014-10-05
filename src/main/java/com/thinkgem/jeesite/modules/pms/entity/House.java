/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.pms.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.IdEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.common.utils.excel.fieldtype.HouseType;
import com.thinkgem.jeesite.common.utils.excel.fieldtype.UserType;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 区域Entity
 * 
 * @author ThinkGem
 * @version 2013-05-15
 */
@Entity
@Table(name = "pms_house") //房屋信息
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class House extends IdEntity<House> {

	private static final long serialVersionUID = 1L;
	private String name; // name '房屋名称'
	private String code; // code 编号
	private Unit unit; // unit_id 楼宇
	private Integer numFloor; // num_floor '第几层'
	private BigDecimal buildArea;  //建筑面积 building_area
	private BigDecimal useArea; //使用面积 use_area
	private User owner = new User(); // 业主
	private String apartment; // apartment 户型  二室一厅 三室一厅 二室二厅
	private String face; // face  朝向 坐西朝东 坐北朝南 坐南朝北
	private String funct; // funct 功能   居住 商用 商住两用
	private String isSell; //  is_sell 已售
	private String isRent; //  is_rent 已租
	private Integer sort; // '排序（升序）',
	
	private String fullName; // name '房屋全名称'
	
	
//	private String proCompanyName; // name '单位'
	
	private String loginName; // name '房屋全名称'
	private String communityName; // name '房屋全名称'
	private String buildingsName; // name '房屋全名称'
	private String unitName; // name '房屋全名称'
	private String numFloorStr; // name '楼层'

	private String buildAreaStr;  //建筑面积 
	private String useAreaStr; //使用面积 

	private List<Device> deviceList = Lists.newArrayList(); // 拥有设备
	private List<PayemtDetail> payemtDetail = Lists.newArrayList(); // 拥有设备
	private List<PaymentPre> paymentPreList = Lists.newArrayList(); // 拥有设备
	
	
	
	
	



	String houseFees ="";

	public House() {
		super();
		this.isSell = YES;
		this.isRent = NO;
		this.sort = 30;
	}

	public House(String id) {
		this();
		this.id = id;
	}
	



	
//	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(name = "pms_house_device", joinColumns = { @JoinColumn(name = "house_id") }, inverseJoinColumns = { @JoinColumn(name = "device_id") })
//	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
//	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
//	@NotFound(action = NotFoundAction.IGNORE)
//	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//	@JsonIgnore
//	@ExcelField(title="拥有设备", align=1, sort=800, fieldType=RoleListType.class)

	@OneToMany(mappedBy = "house", fetch=FetchType.LAZY,cascade = CascadeType.MERGE)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="code") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Device> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Device> deviceList) {
		this.deviceList = deviceList;
	}

	@OneToMany(mappedBy = "house", fetch=FetchType.LAZY,cascade = CascadeType.MERGE)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="code") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<PayemtDetail> getPayemtDetail() {
		return payemtDetail;
	}

	public void setPayemtDetail(List<PayemtDetail> payemtDetail) {
		this.payemtDetail = payemtDetail;
	}	
	
	@OneToMany(mappedBy = "house", fetch=FetchType.LAZY,cascade = CascadeType.MERGE)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="code") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<PaymentPre> getPaymentPreList() {
		return paymentPreList;
	}

	public void setPaymentPreList(List<PaymentPre> paymentPreList) {
		this.paymentPreList = paymentPreList;
	}

	
//	户型 朝向 装修 楼层数 建筑面积 使用面积 阁楼面积 储藏室面积 物业类型 房间状态

	@ManyToOne
	@JoinColumn(name="unit_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}


	@ManyToOne
	@JoinColumn(name="user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	@Transient
	@ExcelField(title="登录名", type=2,align=2, sort=2)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}	
	
//	@Transient
//	public String getProCompanyName() {
//		return proCompanyName;
//	}
//
//	public void setProCompanyName(String proCompanyName) {
//		this.proCompanyName = proCompanyName;
//	}
	
	@Transient
	@ExcelField(title="小区", type=2, align=2, sort=2)
	public String getCommunityName() {
		return communityName;
	}
	
	@Transient
	@ExcelField(title="楼号", type=2, align=2, sort=3)
	public String getBuildingsName() {
		return buildingsName;
	}
	

	@Transient
	@ExcelField(title="单元", type=2, align=2, sort=4)
	public String getUnitName() {
		return unitName;
	}
	
	@Transient
	@ExcelField(title="楼层",  type=2, align=2, sort=5)
	public String getNumFloorStr() {
		return numFloorStr;
	}

	public void setNumFloorStr(String numFloorStr) {
		this.numFloorStr = numFloorStr;
	}

	
	
	public Integer getNumFloor() {
		return numFloor;
	}
	
	@Length(min = 1, max = 100)
	@ExcelField(title="门牌号",  type=2, align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	public void setNumFloor(Integer numFloor) {
		this.numFloor = numFloor;
	}
	
	@Transient
	@ExcelField(title="建筑面积",  type=2, align=2, sort=7)
	public String getBuildAreaStr() {
		return buildAreaStr;
	}

	public void setBuildAreaStr(String buildAreaStr) {
		this.buildAreaStr = buildAreaStr;
	}
	@Transient
	@ExcelField(title="使用面积",  type=2, align=2, sort=8)
	public String getUseAreaStr() {
		return useAreaStr;
	}

	public void setUseAreaStr(String useAreaStr) {
		this.useAreaStr = useAreaStr;
	}
	

	public BigDecimal getBuildArea() {
		return buildArea;
	}

	public void setBuildArea(BigDecimal buildArea) {
		this.buildArea = buildArea;
	}

	public BigDecimal getUseArea() {
		return useArea;
	}

	public void setUseArea(BigDecimal useArea) {
		this.useArea = useArea;
	}



	@Length(min = 0, max = 50)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}











	public String getApartment() {
		return apartment;
	}

	public void setApartment(String apartment) {
		this.apartment = apartment;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public String getFunct() {
		return funct;
	}

	public void setFunct(String funct) {
		this.funct = funct;
	}

	public String getIsSell() {
		return isSell;
	}

	public void setIsSell(String isSell) {
		this.isSell = isSell;
	}

	public String getIsRent() {
		return isRent;
	}

	public void setIsRent(String isRent) {
		this.isRent = isRent;
	}

	
	
	
	
	@Transient
	@JsonIgnore
	public List<String> getDeviceIdList() {
		List<String> deviceIdList = Lists.newArrayList();
		for (Device device : deviceList) {
			deviceIdList.add(device.getId());
		}
		return deviceIdList;
	}
	
	
	@Transient
	@JsonIgnore
	public List<Fees> getFeesList() {
		List<Fees> feesList = Lists.newArrayList();
		for (Device device : deviceList) {
			feesList.add(device.getFees());
		}
		return feesList;
	}
	
	@Transient
	@JsonIgnore
	public List<String> getFeesIdList() {
		List<String> feesIdList = Lists.newArrayList();
		List<Fees> feesList = getFeesList();
		for (Fees fees : feesList) {
			feesIdList.add(fees.getId());
		}
		return feesIdList;
	}	
	
	
	@Transient
	@JsonIgnore
	public String getFullName() {
		String communityName ="";
		String buildingsName ="";
		String unitName ="";
		if(unit != null){
			 communityName = unit.getBuildings().getCommunity().getName();
			 buildingsName = unit.getBuildings().getName();
			 unitName = unit.getName(); 
		}

		String houseName = getName();
		String fullName = communityName + buildingsName+unitName + houseName;
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}	
	
	
	@Transient
	@JsonIgnore
	public String getHouseFees() {
		return houseFees;
	}

	public void setHouseFees(String houseFees) {
		this.houseFees = houseFees;
	}

	@Transient
	public boolean isAdmin() {
		return isAdmin(this.id);
	}

	@Transient
	public static boolean isAdmin(String id) {
		return (id != null) && id.equals("1");
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public void setBuildingsName(String buildingsName) {
		this.buildingsName = buildingsName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	
	
}