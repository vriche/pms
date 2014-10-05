/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.pms.entity;

import java.util.List;

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
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 区域Entity
 * 
 * @author ThinkGem
 * @version 2013-05-15
 */
@Entity
@Table(name = "pms_unit") //单元信息
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Unit extends IdEntity<Unit> {

	private static final long serialVersionUID = 1L;
	private String name; // name '单元名称'
	private String code; // code 单元编号
	private Buildings buildings; // buildings_id 楼宇
	
	private Integer startFloor; // start_floor 开始楼层
	private Integer endFloor; // end_floor 结束楼层
	private Integer startRoom; // start_room 开始房号
	private Integer endRoom; // end_room 结束房号


	private Integer sort; // '排序（升序）',
	
	
	private String proCompanyId;
	private String communityId;
	private String buildingsId;
	private String fullNmae;


	



	public Unit() {
		super();
		this.sort = 30;
		this.startFloor = 1;
		this.endFloor = 1;
		this.startRoom = 1;
		this.endRoom = 1;
	}

	public Unit(String id) {
		this();
		this.id = id;
	}
	
	
	
	public Integer getStartFloor() {
		return startFloor;
	}

	public void setStartFloor(Integer startFloor) {
		this.startFloor = startFloor;
	}

	public Integer getEndFloor() {
		return endFloor;
	}

	public void setEndFloor(Integer endFloor) {
		this.endFloor = endFloor;
	}

	public Integer getStartRoom() {
		return startRoom;
	}

	public void setStartRoom(Integer startRoom) {
		this.startRoom = startRoom;
	}

	public Integer getEndRoom() {
		return endRoom;
	}

	public void setEndRoom(Integer endRoom) {
		this.endRoom = endRoom;
	}

	@ManyToOne
	@JoinColumn(name="buildings_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull(message="楼宇不能为空")
	@ExcelField(title="楼宇", align=2, sort=20)
	public Buildings getBuildings() {
		return buildings;
	}

	public void setBuildings(Buildings buildings) {
		this.buildings = buildings;
	}

	


	

	@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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
	@Transient
	public String getBuildingsId() {
		return buildingsId;
	}

	public void setBuildingsId(String buildingsId) {
		this.buildingsId = buildingsId;
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
	public String getFullNmae() {
		
		String communityName = this.getBuildings().getCommunity().getName();
		String buildingsName = this.getBuildings().getName();
		String unitName = this.getName(); 
		String fullName = communityName + buildingsName + unitName;
		
		return fullNmae;
	}

	public void setFullNmae(String fullNmae) {
		this.fullNmae = fullNmae;
	}
	
	
}