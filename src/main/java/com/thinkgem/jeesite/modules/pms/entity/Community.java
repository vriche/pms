/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.pms.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
import com.thinkgem.jeesite.common.utils.excel.fieldtype.OfficeType;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 区域Entity
 * 
 * @author ThinkGem
 * @version 2013-05-15
 */
@Entity
@Table(name = "pms_community") //小区信息
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Community extends IdEntity<Community> {

	private static final long serialVersionUID = 1L;
	private String name; // name '名称'
	private String code; // code 编号
	private Office proCompany; // pro_company Property company 物业公司
	private Office devCompany; // dev_company Developers 开发商信息
	private  BigDecimal totalArea;  //占地面积total_area
	private  BigDecimal buildArea; //建筑面积  build_area
	private  BigDecimal greenArea;  //绿地面积 green_area
	private  BigDecimal roadArea; //道路面积  road_area
	private  Integer buildCount; //楼宇数量  build_count
	private  String chargePerson; //负责人  charge_person
	private  String address; //住宅地址  address
	private Integer sort; // '排序（升序）',

	public Community() {
		super();
		this.sort = 30;
		this.devCompany = new Office();
		this.devCompany.setId("0");
		this.totalArea = new BigDecimal(0);
		this.buildArea = new BigDecimal(0);
		this.greenArea = new BigDecimal(0);
		this.roadArea = new BigDecimal(0);
		this.buildCount = 0;
	}

	public Community(String id) {
		this();
		this.id = id;
	}
	
	
	public Integer getBuildCount() {
		return buildCount;
	}

	public void setBuildCount(Integer buildCount) {
		this.buildCount = buildCount;
	}
	

	@ManyToOne
	@JoinColumn(name="pro_company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull(message="物业公司不能为空")
	@ExcelField(title="物业公司", align=2, sort=1,fieldType=OfficeType.class)
	public Office getProCompany() {
		return proCompany;
	}

	public void setProCompany(Office proCompany) {
		this.proCompany = proCompany;
	}

	@ManyToOne
	@JoinColumn(name="dep_company_id")
//	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
//	@NotNull(message="开发商不能为空")
//	@ExcelField(title="开发商", align=2, sort=20)
	public Office getDevCompany() {
		return devCompany;
	}

	public void setDevCompany(Office devCompany) {
		this.devCompany = devCompany;
	}

	public BigDecimal getTotalArea() {
		return totalArea;
	}

	public void setTotalArea(BigDecimal totalArea) {
		this.totalArea = totalArea;
	}

	public BigDecimal getBuildArea() {
		return buildArea;
	}

	public void setBuildArea(BigDecimal buildArea) {
		this.buildArea = buildArea;
	}

	public BigDecimal getGreenArea() {
		return greenArea;
	}

	public void setGreenArea(BigDecimal greenArea) {
		this.greenArea = greenArea;
	}

	public BigDecimal getRoadArea() {
		return roadArea;
	}

	public void setRoadArea(BigDecimal roadArea) {
		this.roadArea = roadArea;
	}


	public String getChargePerson() {
		return chargePerson;
	}

	public void setChargePerson(String chargePerson) {
		this.chargePerson = chargePerson;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	

	@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}



	@Length(min = 1, max = 100)
	@ExcelField(title="小区名称", align=2, sort=20)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min = 0, max = 50)
	@ExcelField(title="小区编码", align=2, sort=30)
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

	@Override
	public String toString() {
		return "Community [name=" + name + ", code=" + code + ",  totalArea="
				+ totalArea + ", buildArea=" + buildArea + ", greenArea="
				+ greenArea + ", roadArea=" + roadArea + ", buildCount="
				+ buildCount + ", chargePerson=" + chargePerson + ", address="
				+ address + ", sort=" + sort + "]";
	}
	
	
	
}