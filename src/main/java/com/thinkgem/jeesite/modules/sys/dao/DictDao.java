/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 字典DAO接口
 * @author ThinkGem
 * @version 2013-8-23
 */
@Repository
public class DictDao extends BaseDao<Dict> {

	public List<Dict> findAllList(){
		return find("from Dict where delFlag=:p1 order by sort", new Parameter(Dict.DEL_FLAG_NORMAL));
	}
	
	public List<Dict> findAllListByType(String type){
		return find("from Dict where delFlag=:p1 and type=:p2 order by sort", new Parameter(Dict.DEL_FLAG_NORMAL,type));
	}

	public List<String> findTypeList(){
		return find("select type from Dict where delFlag=:p1 group by type", new Parameter(Dict.DEL_FLAG_NORMAL));
	}
	
//	public List<Dict> findDescGroupByDescriptionList(){
//		return find("select * from Dict where delFlag=:p1 group by type", new Parameter(Dict.DEL_FLAG_NORMAL));
//	}
	
	public List<Dict> findDescGroupByDescriptionList() {
		return find("from Dict where delFlag=:p1  group by description", new Parameter(Dict.DEL_FLAG_NORMAL));
	}
	
}
