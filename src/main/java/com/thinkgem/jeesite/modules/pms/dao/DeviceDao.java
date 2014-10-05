/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.Fees;

/**
 * 单元信息DAO接口
 * @author vriche
 * @version 2014-04-23
 */
@Repository
public class DeviceDao extends BaseDao<Device> {
	
	public List<Device> findAllList(String houseId,String feesId){
		
		if(feesId == null){
			return find("select distinct d from Fees f, Device d where  d.fees.id =f.id and d.enable=1 " +
					"  and f.delFlag=:p1 and d.house.id=:p2" + // or (m.user.id=:p2  and m.delFlag=:p1)" + 
					" order by f.sort", new Parameter(Fees.DEL_FLAG_NORMAL, houseId));
		}else{
			return find("select distinct d from Fees f, Device d where  d.fees.id =f.id  " +
					"  and f.delFlag=:p1 and d.house.id=:p2 and f.id =:p3 " + // or (m.user.id=:p2  and m.delFlag=:p1)" + 
					" order by f.sort", new Parameter(Fees.DEL_FLAG_NORMAL, houseId,feesId));
		}
	
	}
	
	
	public List<Device> findAllChild(String houseId,String parentId){
		
			return find("select  d from  Device d where d.delFlag=:p1 and  d.house.id=:p2  and d.parent.id =:p3" +
					" order by d.code", new Parameter(Device.DEL_FLAG_NORMAL, houseId,parentId));
		
	
	}
}
