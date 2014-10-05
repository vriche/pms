/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.pms.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkgem.jeesite.common.persistence.IdEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.common.utils.excel.fieldtype.CommunityType;

/**
 * 区域Entity
 * 
 * @author ThinkGem
 * @version 2013-05-15
 */
@Entity
@Table(name = "pms_buildings") //楼宇信息
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Buildings extends IdEntity<Buildings> {

	private static final long serialVersionUID = 1L;
	
	
	private String name; // name '楼宇名称' 第16栋
	private String code; // code 楼宇编号 B-16
	private Community community; // community_id 小区
	private BigDecimal buildArea; //建筑面积  building_area
	private BigDecimal useArea;   //使用面积 use_area
	private Integer floorCount; // floor_count '单元数量'

	private Date capDate;// cap_date 封顶日期
	private Date completionDate;// completion_date 竣工日期 
	private String presalePermit; // presale_permit  预售许可证
	private String buildPermit; // build_permit 建筑许可证

	private Integer sort; // '排序（升序）',
	
	
	private String proCompanyId;
	private String communityId;
	
	
	

	public Buildings() {
		super();
		this.sort = 30;
		this.buildArea = new BigDecimal(0);
		this.useArea = new BigDecimal(0);
		this.floorCount = 0;
//		this.capDate = new Date();
//		this.completionDate = new Date();
	}

	public Buildings(String id) {
		this();
		this.id = id;
	}
	
	@ExcelField(title="使用面积", align=2, sort=5) 
	public BigDecimal getUseArea() {
		return useArea;
	}

	public void setUseArea(BigDecimal useArea) {
		this.useArea = useArea;
	}

	@Temporal(TemporalType.DATE)
	public Date getCapDate() {
		return capDate;
	}

	public void setCapDate(Date capDate) {
		this.capDate = capDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public String getPresalePermit() {
		return presalePermit;
	}

	public void setPresalePermit(String presalePermit) {
		this.presalePermit = presalePermit;
	}

	public String getBuildPermit() {
		return buildPermit;
	}

	public void setBuildPermit(String buildPermit) {
		this.buildPermit = buildPermit;
	}

	@ExcelField(title="单元数量", align=2, sort=6) 
	public Integer getFloorCount() {
		return floorCount;
	}

	public void setFloorCount(Integer floorCount) {
		this.floorCount = floorCount;
	}

	@ExcelField(title="建筑面积", align=2, sort=4)
	public BigDecimal getBuildArea() {
		return buildArea;
	}

	public void setBuildArea(BigDecimal buildArea) {
		this.buildArea = buildArea;
	}
	
	@ManyToOne
	@JoinColumn(name="community_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull(message="小区不能为空")
	@ExcelField(title="小区", align=2, sort=1,fieldType=CommunityType.class)
	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	

	

	@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}



	@Length(min = 1, max = 100)
	@ExcelField(title="楼宇名称", align=2, sort=2)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min = 0, max = 50)
	@ExcelField(title="楼宇编号", align=2, sort=3)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
	public String getProCompanyId() {
		return proCompanyId;
	}

	public void setProCompanyId(String proCompanyId) {
		this.proCompanyId = proCompanyId;
	}
	@Transient
	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
}