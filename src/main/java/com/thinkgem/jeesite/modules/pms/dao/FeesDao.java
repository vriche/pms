/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.Menu;

/**
 * 收费项目DAO接口
 * @author vriche
 * @version 2014-04-16
 */
@Repository
public class FeesDao extends BaseDao<Fees> {
	
	public List<Fees> findByUserId(String userId){
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>userId"+userId);
//		return find("select distinct f from Fees f, User u where  u in elements (f.userList) and f in elements (u.feesList)  " +
//				" and f.delFlag=:p1 and u.delFlag=:p1  and u.id=:p2" + // or (m.user.id=:p2  and m.delFlag=:p1)" + 
//				" order by f.sort", new Parameter(Menu.DEL_FLAG_NORMAL, userId));
		
//		return find("select distinct f from Fees f, User u,House h where  u in elements (f.userList) and f in elements (u.feesList)  " +
//				" and f.delFlag=:p1 and u.delFlag=:p1  and u.id=:p2" + // or (m.user.id=:p2  and m.delFlag=:p1)" + 
//				" order by f.sort", new Parameter(Menu.DEL_FLAG_NORMAL, userId));
		
		return null;
		
	}
	
	
	public List<Fees> findAllList(){
		return find("from Fees where delFlag=:p1 order by sort", new Parameter(Fees.DEL_FLAG_NORMAL));
	}
	
	
	
//	public List<Fees> findAllList(Fees fees){
//		return find("from Fees f,Device d, where d in elements(f.deviceList) and d.id = f.id "
//				+ " f.delFlag=:p1 order by sort", new Parameter(Fees.DEL_FLAG_NORMAL));
//	}
	
//	public List<Fees> findAllList(Fees fees){
//		String houseId = "1";
//		return find("select distinct f from Fees f, Device d, House h where d in elements (h.deviceList) and h in elements (d.houseList)" +
//				" and d in elements (f.deviceList)   and d.fees.id = f.id and f.delFlag=:p1 and d.delFlag=:p1 and h.delFlag=:p1 and h.id=:p2" + // or (m.user.id=:p2  and m.delFlag=:p1)" + 
//				" order by f.sort", new Parameter(Fees.DEL_FLAG_NORMAL, houseId));
//	}
//	public List<Fees> findAllList(String houseId){
//	
//		return find("select distinct f from Fees f, Device d,House h where  d.fees.id =f.id  and d in elements (h.deviceList) " +
//				" and h in elements (d.houseList) and f.delFlag=:p1 and h.id=:p2" + // or (m.user.id=:p2  and m.delFlag=:p1)" + 
//				" order by f.sort", new Parameter(Fees.DEL_FLAG_NORMAL, houseId));
//	}	
	
	public List<Fees> findByUserId2(String userId){
		return find("from Fees  where delFlag=:p1", new Parameter(Fees.DEL_FLAG_NORMAL));
	
		
//		return find("select distinct m from Fees m, Role r, User u where m in elements (r.menuList) and r in elements (u.roleList)" +
//				" and m.delFlag=:p1 and r.delFlag=:p1 and u.delFlag=:p1 and u.id=:p2" + // or (m.user.id=:p2  and m.delFlag=:p1)" + 
//				" order by m.sort", new Parameter(Fees.DEL_FLAG_NORMAL, userId)); ab9fe48d36da42589f2ce488cef6fb52
	}

}
