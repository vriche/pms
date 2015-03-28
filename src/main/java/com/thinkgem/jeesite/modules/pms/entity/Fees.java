/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.pms.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.IdEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 区域Entity
 * 
 * @author ThinkGem
 * @version 2013-05-15
 */
@Entity
@Table(name = "pms_fees")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fees extends IdEntity<Fees> {

	private static final long serialVersionUID = 1L;
	private String name; // name '费项名称'
	private Fees parent; // parent_id '关联费项',
	private String feesMode; // fees_mode pms_fees_mode  '收费方式' 1：一级；2：二级；3：三级；4：四级）
    
	private String feesType;  // pms_fees_type '收费类型' 1：正常；2：公摊；
	
	private Office company; // office_id 归属公司
	private String code; // code 费项编号
	// private String remarks; // remarks 描述

	private String speMonth; // spe_month '收费周期(月)',
	private BigDecimal unitPrice; // unit_price '单位价格',
	private String isEnableModPrice; // is_enable_mod_price '允许【生成物业费】时直接修改此费项单价',
	private String remindType; // remind_type '提醒业主方式 短信、电话、邮件',
	private String remindDays; // remind_days '从费用到期前 天开始提醒',
	private String isLadder; // is_ladder '按单价收费 按阶梯收费',
	private String ladderRules;// ladder_rules '阶梯规则编号',
	private Integer sort; // '排序（升序）',
	
	private boolean withoutPool; // '排除公摊
	

	private List<String> feesIdList = Lists.newArrayList(); 



	

	private List<Device> deviceList = Lists.newArrayList(); // 公摊时用到


	@OneToMany(mappedBy = "fees", fetch=FetchType.LAZY)
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

	@Length(min = 0, max = 100)
	@ExcelField(title = "收费类型", align = 2, sort = 80, dictType = "pms_fees_type")
	public String getFeesType() {
		return feesType;
	}

	public void setFeesType(String feesType) {
		this.feesType = feesType;
	}


	public Fees() {
		super();
		this.remindType = NO;
		this.isLadder = NO;
		this.sort = 30;
//		super.setTableName("pms_fees");
		// this.company.setId("1");
	}

	public Fees(String id) {
		this();
		this.id = id;
	}
	
	
	@ManyToOne
	@JoinColumn(name="company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull(message="归属公司不能为空")
	@ExcelField(title="归属公司", align=2, sort=20)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}
	
	@Length(min = 0, max = 100)
	@ExcelField(title = "费用方式", align = 2, sort = 80, dictType = "pms_fees_mode")
	public String getFeesMode() {
		return feesMode;
	}

	public void setFeesMode(String feesMode) {
		this.feesMode = feesMode;
	}

	

	public String getSpeMonth() {
		return speMonth;
	}

	public void setSpeMonth(String speMonth) {
		this.speMonth = speMonth;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Length(min=1, max=1)
	public String getIsEnableModPrice() {
		return isEnableModPrice;
	}

	public void setIsEnableModPrice(String isEnableModPrice) {
		this.isEnableModPrice = isEnableModPrice;
	}

	public String getRemindType() {
		return remindType;
	}

	public void setRemindType(String remindType) {
		this.remindType = remindType;
	}

	public String getRemindDays() {
		return remindDays;
	}

	public void setRemindDays(String remindDays) {
		this.remindDays = remindDays;
	}

	@Length(min=1, max=1)
	public String getIsLadder() {
		return isLadder;
	}

	public void setIsLadder(String isLadder) {
		this.isLadder = isLadder;
	}

	public String getLadderRules() {
		return ladderRules;
	}

	public void setLadderRules(String ladderRules) {
		this.ladderRules = ladderRules;
	}

	@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

//	private List<Office> officeList = Lists.newArrayList(); // 部门列表
	private List<Fees> childList = Lists.newArrayList(); // 拥有子区域列表

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	// @NotFound(action = NotFoundAction.IGNORE)
	// @NotNull
	public Fees getParent() {
		return parent;
	}

	public void setParent(Fees parent) {
		this.parent = parent;
	}

	@Length(min = 1, max = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min = 0, max = 50)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	// @OneToMany(mappedBy = "Fees", fetch = FetchType.LAZY)
	// @Where(clause = "del_flag='" + DEL_FLAG_NORMAL + "'")
	// @OrderBy(value = "sort")
	// @Fetch(FetchMode.SUBSELECT)
	// @NotFound(action = NotFoundAction.IGNORE)
	// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	// public List<Office> getOfficeList() {
	// return officeList;
	// }
	//
	// public void setOfficeList(List<Office> officeList) {
	// this.officeList = officeList;
	// }

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@Where(clause = "del_flag='" + DEL_FLAG_NORMAL + "'")
	@OrderBy(value = "sort")
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Fees> getChildList() {
		return childList;
	}

	public void setChildList(List<Fees> childList) {
		this.childList = childList;
	}

	@Transient
	public static void sortList(List<Fees> list, List<Fees> sourcelist, String parentId) {
		for (int i = 0; i < sourcelist.size(); i++) {
			Fees e = sourcelist.get(i);
			if ((e.getParent() != null) && (e.getParent().getId() != null) && e.getParent().getId().equals(parentId)) {
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j = 0; j < sourcelist.size(); j++) {
					Fees childe = sourcelist.get(j);
					if ((childe.getParent() != null) && (childe.getParent().getId() != null)
							&& childe.getParent().getId().equals(e.getId())) {
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}
	


	@Transient
	public boolean isAdmin() {
		return isAdmin(this.id);
	}

	@Transient
	public static boolean isAdmin(String id) {
		return (id != null) && id.equals("1");
	}
	
	@Transient
	public boolean isWithoutPool() {
		return withoutPool;
	}

	public void setWithoutPool(boolean withoutPool) {
		this.withoutPool = withoutPool;
	}
	
	
	@Transient
	public List<String> getFeesIdList() {
		return feesIdList;
	}

	public void setFeesIdList(List<String> feesIdList) {
		this.feesIdList = feesIdList;
	}
}