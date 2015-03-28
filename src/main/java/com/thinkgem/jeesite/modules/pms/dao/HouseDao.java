/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.House;

/**
 * 收费项目DAO接口
 * @author vriche
 * @version 2014-04-18
 */
@Repository
public class HouseDao extends BaseDao<House> {
	
	public List<House> getRes(String sql,String id){
		return find(sql, new Parameter(House.DEL_FLAG_NORMAL, id));
	}
	
	
	public House findByDevice(String deviceId){
		String sql = "select distinct h from House h,Device d "
				  + "  where h.id = d.house.id "
				  + "  and  d.id=:p1";
		return getByHql(sql, new Parameter(deviceId));
	}
	
	

	public List<House> findHouseByDevice(String deviceId){
//		String sql = "select distinct h from House h,Device d "
//				  + "  where h.id = d.house.id "
//				  + "  and  d.parent.id=:p1";
		
//		String sql = "select distinct h from House h,Device d "
//		  + "  where h.id = d.house.id "
//		  + "  and  d.parent.id=:p1";
//		
//		return findBySql(sql, new Parameter(deviceId));
		
//		String sql = "select  h.* from pms_house h,pms_device d "
//				  + "  where h.id = d.house_id "
//				  + "  and  d.parent_id="+deviceId;
		
		
		
		
		String sql = "select distinct h from House h,Device d "
		  + "  where h.id = d.house.id "
		  + "  and  d.parent.id="+deviceId
		 + "  and  d.delFlag="+Device.DEL_FLAG_NORMAL;
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>666666666666666666666666666666666   sql          >>>"+sql);
		
		return find(sql);
	}
	
	public List<House> findByProcompany(String proCompanyId){
		String sql = "select distinct h from House h,Unit u,Buildings b,Community c,Office p "
				  + "  where h.unit.id = u.id and u.buildings.id = b.id and b.community.id = c.id and c.proCompany.id = p.id "
				  + "  and h.delFlag=:p1 and p.id=:p2";
		return getRes(sql,proCompanyId);
	}

	public List<House> findByCommunityId(String communityId){
		String sql = "select distinct h from House h,Unit u,Buildings b,Community c "
				  + " where  h.unit.id = u.id and u.buildings.id = b.id and b.community.id = c.id "
				  + " and h.delFlag=:p1 and c.id=:p2";
		return getRes(sql,communityId);
	}
	
	
	public List<House> findByBuildingsId(String buildingsId){
		String sql = "select distinct h from House h,Unit u,Buildings b "
				   + "  where  h.unit.id = u.id and u.buildings.id = b.id"
				   + "  and h.delFlag=:p1 and b.id=:p2";
		return getRes(sql,buildingsId);
	}
	
	
	public List<House> findByUnitId(String unitId){
		String sql = "select distinct h from House h,Unit u  where  h.unit.id = u.id and h.delFlag=:p1 and u.id=:p2";
		return getRes(sql,unitId);
	}
	
	public List<House> findByIds(String ids){
		String sql = "select distinct h from House h  where h.delFlag=:p1 and h.id  in("+ ids +")";
		return  find(sql, new Parameter(House.DEL_FLAG_NORMAL));
	}	
	
	public List<House> findByProcompany(String proCompanyId,String communityId,String buildingsId,String unitId,String houseId){
		Parameter p = new Parameter();
		p.put("p1", House.DEL_FLAG_NORMAL);
		
		String sql ="";
		
		if(houseId != null && StringUtils.isNotBlank(houseId)){
			 sql = "select distinct h from House h   where  h.delFlag=:p1 and h.id=:p2"
			 + "  order by h.code asc";
			 p.put("p2", houseId);
		}else{
			
			if(unitId != null && StringUtils.isNotBlank(unitId)){
				 sql = "select distinct h from House h,Unit u,Buildings b,Community c,Office p "
						  + "  where h.unit.id = u.id and u.buildings.id = b.id and b.community.id = c.id and c.proCompany.id = p.id "
						  + "  and h.delFlag=:p1 and u.id=:p2"
				 		  + "  order by c.code, b.code,u.code,h.code asc";
				 p.put("p2", unitId);
			}else{
				if(buildingsId != null  && StringUtils.isNotBlank(buildingsId)){
					 sql = "select distinct h from House h,Unit u,Buildings b,Community c,Office p "
							  + "  where h.unit.id = u.id and u.buildings.id = b.id and b.community.id = c.id and c.proCompany.id = p.id "
							  + "  and h.delFlag=:p1 and b.id=:p2"
					 		  + "  order by  c.code, b.code,u.code,h.code asc";
					 p.put("p2", buildingsId);
				}else{
					if(communityId != null  && StringUtils.isNotBlank(communityId)){
						 sql = "select distinct h from House h,Unit u,Buildings b,Community c,Office p "
								  + "  where h.unit.id = u.id and u.buildings.id = b.id and b.community.id = c.id and c.proCompany.id = p.id "
								  + "  and h.delFlag=:p1 and c.id=:p2"
						 		  + "  order by  c.code, b.code,u.code,h.code asc";
						 p.put("p2", communityId);
					}else{
						if(proCompanyId != null  && StringUtils.isNotBlank(proCompanyId)){
							 sql = "select distinct h from House h,Unit u,Buildings b,Community c,Office p "
									  + "  where h.unit.id = u.id and u.buildings.id = b.id and b.community.id = c.id and c.proCompany.id = p.id "
									  + "  and h.delFlag=:p1 and p.id=:p2 "
							 		  + "  order by  c.code, b.code,u.code,h.code asc";
							 p.put("p2", proCompanyId);
						}
					}
				}
				
			}			
			
			
		}
		
		
//		System.out.println("HouseUtils.  1111111111111111111111111111111            222222222222222222              3333333333sql>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+sql );
		
		return find(sql, p);	

	}
	
	
	
//	public List<House> findByHouseId(String houseId){
//		String sql = "from House h  where h.delFlag = :p1 and h.id = :p2";
//		return getRes(sql,houseId);
//	}


	

	
}
