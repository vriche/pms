/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.dao;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 收费项目DAO接口
 * @author vriche
 * @version 2014-04-18
 */
@Repository
public class CommunityDao extends BaseDao<Community> {
	public Community findByName(String name){
		return getByHql("from Community where name = :p1 and delFlag = :p2", new Parameter(name, Community.DEL_FLAG_NORMAL));
	}
}
