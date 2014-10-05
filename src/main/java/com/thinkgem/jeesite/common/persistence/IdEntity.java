/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.common.persistence;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.TableGenerator;

import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 数据Entity类
 * @author ThinkGem
 * @version 2013-05-28
 */
@MappedSuperclass
public abstract class IdEntity<T> extends DataEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String id;		// 编号


	public IdEntity() {
		super();
	}
	
	 @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@PrePersist
	public void prePersist(){
		super.prePersist();
//		this.id = IdGen.uuid();
		this.id = null;
//		String id = this.getId();
//		System.out.println(">>>>>>>>>>>>>>>>>>>> id  >>>>>>>>"+id);
//		this.id = id;
	}


	
}
