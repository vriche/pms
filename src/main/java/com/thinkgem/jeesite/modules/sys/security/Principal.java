package com.thinkgem.jeesite.modules.sys.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.thinkgem.jeesite.modules.sys.entity.User;


public class Principal  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String loginName;
	private String phone;


	private String name;
	private Map<String, Object> cacheMap;

	public Principal(User user) {
		this.id = user.getId();
		this.loginName = user.getLoginName();
//		this.phone = user.getPhone();
		this.name = user.getName();
	}

	public String getId() {
		return id;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getName() {
		return name;
	}
//	public String getPhone() {
//		return phone;
//	}
//
//	public void setPhone(String phone) {
//		this.phone = phone;
//	}
	public Map<String, Object> getCacheMap() {
		if (cacheMap==null){
			cacheMap = new HashMap<String, Object>();
		}
		return cacheMap;
	}
}
