/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.dao.DeviceDetailDao;
import com.thinkgem.jeesite.modules.pms.dao.MyBatisDeviceDetailDao;
import com.thinkgem.jeesite.modules.pms.entity.Buildings;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.DeviceDetail;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.PaymentAfter;
import com.thinkgem.jeesite.modules.pms.entity.Unit;
import com.thinkgem.jeesite.modules.pms.utils.HouseUtils;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 单元信息Service
 * @author vriche
 * @version 2015-07-16
 */
@Component
@Transactional(readOnly = true)
public class DeviceDetailService extends BaseService {

	@Autowired
	private DeviceDetailDao deviceDetailDao;
	
	@Autowired
	private MyBatisDeviceDetailDao myBatisDeviceDetailDao;
	
	
	public List<DeviceDetail> myBatisFind( Map<String, Object> params) {
		
		List<DeviceDetail> ls = myBatisDeviceDetailDao.find(params);

		return ls;
	}
	
	
	public DeviceDetail get(String id) {
		return deviceDetailDao.get(id);
	}
	
	public Page<DeviceDetail> find(Page<DeviceDetail> page, DeviceDetail deviceDetail) {
		DetachedCriteria dc = findForPayemtDetailDC(deviceDetail.getDevice());
		Date lastDate = deviceDetail.getLastDate();
		if(lastDate != null){
				dc.add(Restrictions.eq("lastDate", deviceDetail.getLastDate()));
		}
	
		dc.add(Restrictions.eq(DeviceDetail.FIELD_DEL_FLAG, deviceDetail.DEL_FLAG_NORMAL));

		return deviceDetailDao.find(page, dc);
	}
	
	
	
	
	
	public Page<DeviceDetail> findPage(Page<DeviceDetail> page, DeviceDetail deviceDetail,Map<String,DeviceDetail> mp,String from) {
		DetachedCriteria dc = findDeviceDetailDC(deviceDetail,from);
		deviceDetailDao.find(page,dc);
		if(mp != null){
			List<DeviceDetail> list = page.getList();
			 for (DeviceDetail detail : list){
				 String deviceID =  detail.getDevice().getId();
				 mp.put(deviceID, detail);
			 }		
		}
		return page;
	}

	
	public DeviceDetail findSumRow(DeviceDetail deviceDetail,String from) {
		
		DetachedCriteria dc = findDeviceDetailDC(deviceDetail,"1");

		DeviceDetail dt = new DeviceDetail();
		//合计行 <c:if test="${deviceDetail.device.type == 1}">	
		dt.setDevice(deviceDetail.getDevice());
//		System.out.println("findSumRow>>>>>>>>>>>>>>>>>>>>>>>>>>>deviceDetail.getDevice().getType()>>>>>>>>>"+ deviceDetail.getDevice().getType());
		
//		dc.setProjection( Projections.projectionList()
//			        .add( Projections.sum("payMoney").as("payMoney") )
//			        .add( Projections.sum("poolPayMoney").as("poolPayMoney") )
//			        .add( Projections.sum("usageAmount").as("usageAmount") )
//			        .add( Projections.sum("sumPayMoney").as("sumPayMoney") )
//			        .add( Projections.sum("incomeMoney").as("incomeMoney") )
//			        .add( Projections.groupProperty("proCompany.id") )
//			    );
	

		dc.setProjection(Projections.sum("payMoney"));  
		List  ls1 = deviceDetailDao.find(dc);
		 Iterator iter1 = ls1.iterator();  
		  while (iter1.hasNext()) {  
			  BigDecimal payMoney = (BigDecimal)iter1.next();
			  dt.setPayMoney(payMoney);
		  }	 
		dc.setProjection(Projections.sum("poolPayMoney"));  
			List  ls11 = deviceDetailDao.find(dc);
			 Iterator iter11 = ls11.iterator();  
			  while (iter11.hasNext()) {  
				  BigDecimal poolPayMoney = (BigDecimal)iter11.next();
				  dt.setPoolPayMoney(poolPayMoney);
			  }	 	  
		
		dc.setProjection(Projections.sum("usageAmount"));  
		List  ls = deviceDetailDao.find(dc);
		 Iterator iter = ls.iterator();  
		  while (iter.hasNext()) {  
			  BigDecimal usageAmount = (BigDecimal)iter.next();
			  dt.setUsageAmount(usageAmount);
		  }
		  
			dc.setProjection(Projections.sum("sumPayMoney"));  
			List  ls2 = deviceDetailDao.find(dc);
			 Iterator iter2 = ls2.iterator();  
			  while (iter2.hasNext()) {  
				  BigDecimal sumPayMoney = (BigDecimal)iter2.next();
				  dt.setSumPayMoney(sumPayMoney);
			  }	  
		  

				dc.setProjection(Projections.sum("incomeMoney"));  
				List  ls3 = deviceDetailDao.find(dc);
				 Iterator iter3 = ls3.iterator();  
				  while (iter3.hasNext()) {  
					  BigDecimal incomeMoney = (BigDecimal)iter3.next();
					  dt.setIncomeMoney(incomeMoney);
				  }	  
				  
		return dt;
	}
	
	private DetachedCriteria findDeviceDetailDC( DeviceDetail deviceDetail,String from) {
		DetachedCriteria dc = deviceDetailDao.createDetachedCriteria();
		Device device = deviceDetail.getDevice();
		Device deviceParent = device.getParent();
		Fees fees = device.getFees();
		House house = device.getHouse();
		Unit unit = house.getUnit();
		Buildings buildings = unit.getBuildings();
		Community community = buildings.getCommunity();
		Office proCompany = community.getProCompany();
		User owner = house.getOwner();
		Office company = owner.getCompany();
		
		
		String proCompanyId = proCompany.getId();
		String communityId = community.getId();
		String buildingsId = buildings.getId();
		String unitId = unit.getId();
		String houseId = house.getId();
		String feesId = fees.getId();
		String companyId = company.getId();
		String deviceType = device.getType();
		String deviceId = device.getId();
		Date firstDate = deviceDetail.getFirstDate();
		Date lastDate = deviceDetail.getLastDate();	
		String isPay = deviceDetail.getIsPay();
		String longinName = owner.getLoginName();
		String ownerId = owner.getId();
		
	     List<String> userIdList = owner.getUserIdList();
	     List<String> feesIdList = fees.getFeesIdList();
	

		dc.createAlias("device", "device");
		dc.createAlias("device.fees", "fees");
		dc.createAlias("fees.company", "proCompany");
		

		if(!"1".equals(deviceType)){
			dc.createAlias("device.house", "house");
			dc.createAlias("house.owner", "owner");
			dc.createAlias("owner.company", "company");
			dc.createAlias("house.unit", "unit");
			dc.createAlias("unit.buildings", "buildings");
			dc.createAlias("buildings.community", "community");
		}

		if (StringUtils.isNotEmpty(deviceType)){
			dc.add(Restrictions.eq("device.type", deviceType));
		}	
		
		if (StringUtils.isNotEmpty(deviceId)){
			dc.add(Restrictions.eq("device.id", deviceId));
		}		
		
		if(deviceDetail.getIsPay()!= null){
//			  <option value='1'>有欠款</option>
//			  <option value='2'>已付</option>
//			  <option value='0'>所有</option>
			if("1".equals(isPay)){
				dc.add(Restrictions.ltProperty("incomeMoney", "sumPayMoney"));
			}	
			
			//已付
			if("2".equals(isPay)){
				dc.add(Restrictions.gt("incomeMoney", new BigDecimal(0)));
			}	
			
		}

		if("1".equals(from)){
			if(firstDate == null && lastDate != null){
				dc.add(Restrictions.eq("lastDate", lastDate));
			}
			if(firstDate !=null && lastDate != null ){
				dc.add(Restrictions.between("lastDate", firstDate,lastDate));
			}		

		}

		if (StringUtils.isNotEmpty(feesId)){
			dc.add(Restrictions.eq("fees.id", feesId));
		}	
		
		
	
	 	if(feesIdList.size() > 0){
			dc.add(Restrictions.in("fees.id", feesIdList));
		}

		
		
		if (StringUtils.isNotEmpty(proCompanyId)){
			dc.add(Restrictions.eq("proCompany.id", proCompanyId));
		}	
		
		if(deviceParent != null){
			String parentId = deviceParent.getId();
			if(!"0".equals(parentId) && !StringUtils.isBlank(parentId)){
				dc.createAlias("device.parent", "deviceparent");
				dc.add(Restrictions.eq("deviceparent.id", parentId));
			}
		}	
		
		
	
		if(!"1".equals(deviceType)){
			
			if (StringUtils.isNotEmpty(communityId)){
				dc.add(Restrictions.eq("community.id", communityId));
			}	
			if (StringUtils.isNotEmpty(buildingsId)){
				dc.add(Restrictions.eq("buildings.id", buildingsId));
			}	
			if (StringUtils.isNotEmpty(unitId)){
				dc.add(Restrictions.eq("unit.id", unitId));
			}
			if (StringUtils.isNotEmpty(companyId)){
				dc.add(Restrictions.eq("company.id", companyId));
			}	
	
			if (StringUtils.isNotEmpty(ownerId)){
				dc.add(Restrictions.eq("owner.id", ownerId));
			}
			
		 	if(userIdList.size() > 0){
				dc.add(Restrictions.in("owner.id", userIdList));
			}
			
			
			if (StringUtils.isNotEmpty(longinName)){
				User u = UserUtils.getUserByLoginName(longinName);
				dc.add(Restrictions.eq("owner.id", u.getId()));		
			}else{
				if (StringUtils.isNotEmpty(houseId)){
					dc.add(Restrictions.eq("house.id", houseId));
				}	
			}
		
			
			
		}
		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>longinName>>>>"+ longinName);
		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>deviceId>>>>"+ deviceId);
		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>feesId>>>>>>>>>"+ feesId);
		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>proCompanyId>>>>>>>"+ proCompanyId);
		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>deviceType>>>>>"+ deviceType);		 
		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>firstDate>>>>>"+ firstDate);	
		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>lastDate>>>>>"+ lastDate);	
		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>isPay()>>>>"+ isPay);
		 
		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>companyId>>>>>>"+ companyId);
		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>communityId>>>>>>"+ communityId);
		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>buildingsId>>>>>>>"+ buildingsId);
		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>unitId>>>>>>"+ unitId);
		 System.out.println("getObjFromReq>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>houseId>>>>"+ houseId);
		 
		dc.add(Restrictions.eq(deviceDetail.FIELD_DEL_FLAG, deviceDetail.DEL_FLAG_NORMAL));

		
		return dc;
	}
	public Map<String,DeviceDetail> findAll(DeviceDetail deviceDetail) {
		Device device = deviceDetail.getDevice();
		DetachedCriteria dc = findForPayemtDetailDC(device);
		Date lastDate = deviceDetail.getLastDate();
		Map<String,DeviceDetail> mp = new HashMap<String,DeviceDetail>();
		
//		System.out.println(" 777777777777777777777777777>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> lastDate >>>>>>>>>>>>>"+lastDate);

		if(lastDate != null){
			if(StringUtils.isNotBlank(lastDate.toString())){
				dc.add(Restrictions.eq("lastDate", deviceDetail.getLastDate()));
			}	
		}
	
//		System.out.println(" 8888888888888888888888888888888>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> lastDate >>>>>>>>>>>>>"+lastDate);
//		dc.add(Restrictions.eq(DeviceDetail.FIELD_DEL_FLAG, deviceDetail.DEL_FLAG_NORMAL));
		
		if(lastDate != null){
    		if(StringUtils.isNotBlank(lastDate.toString())){
    			
    			List<DeviceDetail> lastDateList = deviceDetailDao.find(dc);
//	   			 System.out.println(" 8888888888888888888888888888888 22222222222>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> lastDate >>>>>>>>>>>>>"+lastDate);
				 for (DeviceDetail detail : lastDateList){
					 String deviceID =  detail.getDevice().getId();
					 mp.put(deviceID, detail);
				 }

//    	    try {
//		 
//     		} catch (Exception e) {
//     			e.getMessage();
//     		} 

    		}
		}
//		System.out.println(" 99999999999999999999999999>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> lastDate >>>>>>>>>>>>>"+lastDate);
		return mp;
	}
	
	public List<DeviceDetail> find(DeviceDetail deviceDetail) {
		DetachedCriteria dc = deviceDetailDao.createDetachedCriteria();
		Device device = deviceDetail.getDevice();
		dc.createAlias("device", "device");
		dc.add(Restrictions.eq("device.id", device.getId())); 
//		dc.add(Restrictions.eq("parent.id", device.getParent().getId())); 
		dc.add(Restrictions.eq("lastDate", deviceDetail.getLastDate())); 
		
//		if(device.getId() != null){
//			dc.add(Restrictions.eq("device.id", device.getId()));  	
//		}
//		
//		if(device.getFees() != null){
//			dc.createAlias("device.fees", "fees");
//			if(device.getFees().getId()!= null){
//				dc.add(Restrictions.eq("fees.id", device.getFees().getId()));  	
//			}
//		}
		
//		if(deviceDetail.getLastDate() != null){
//			dc.add(Restrictions.eq("lastDate", deviceDetail.getLastDate())); 
//		}
		 
//		if(device.getParent() != null){
//			dc.add(Restrictions.eq("parent.id", device.getParent().getId())); 
//		}		
		dc.add(Restrictions.eq(DeviceDetail.FIELD_DEL_FLAG, deviceDetail.DEL_FLAG_NORMAL));
 
		return deviceDetailDao.find(dc);
	}
	public List<DeviceDetail> find2(DeviceDetail deviceDetail) {
		DetachedCriteria dc = deviceDetailDao.createDetachedCriteria();
		Device device = deviceDetail.getDevice();
		dc.createAlias("device", "device");
		dc.createAlias("device.fees", "fees");
		
		System.out.println(">>>>>>>>>>>>>>>+ls.size() 44 1>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+device.getId());
		System.out.println(">>>>>>>>>>>>>>>+ls.size() 44 2>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+device.getFees().getId());
		System.out.println(">>>>>>>>>>>>>>>+ls.size() 44 3>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+deviceDetail.getLastDate());
		
		dc.add(Restrictions.eq("device.id", device.getId()));  	
		dc.add(Restrictions.eq("fees.id", device.getFees().getId()));  	
		dc.add(Restrictions.eq("lastDate", deviceDetail.getLastDate())); 
		
		dc.add(Restrictions.eq(DeviceDetail.FIELD_DEL_FLAG, deviceDetail.DEL_FLAG_NORMAL));
 
		return deviceDetailDao.find(dc);
	}
	
	public List<DeviceDetail> find3(DeviceDetail deviceDetail) {
		DetachedCriteria dc = deviceDetailDao.createDetachedCriteria();
		Device device = deviceDetail.getParent();
		
		System.out.println("parent.id 55555555555 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ device.getId());
		
		dc.add(Restrictions.eq("parent.id", device.getId())); 
		dc.add(Restrictions.eq(DeviceDetail.FIELD_DEL_FLAG, deviceDetail.DEL_FLAG_NORMAL));
 
		return deviceDetailDao.find(dc);
	}
//	public List<DeviceDetail> findLastDate(DeviceDetail deviceDetail,Map<String, Object> map) {
//		Device device = deviceDetail.getDevice();
//		 List<DeviceDetail> ls = deviceDetailDao.findLastDate(device);
//		 for(DeviceDetail e:ls){ 
//			try {
//				Date theDate = DateUtils.parseDate(DateUtils.formatDate(e.getLastDate(), "yyyy-MM-dd"), new String[]{"yyyy-MM-dd"});
//				 e.setLastDate(theDate);
//				 String lasteDate = DateUtils.formatDate(e.getLastDate(),"yyyy-MM-dd");
//				 e.setRemarks(lasteDate);
//				 if(map !=null) map.put(lasteDate, lasteDate);
//			} catch (ParseException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//		 }
//		return ls;
//	}
	
	public List<DeviceDetail> findLastDate(DeviceDetail deviceDetail,Map<String, Object> map,HttpServletRequest request, HttpServletResponse response) {
		DetachedCriteria dc = findDeviceDetailDC(deviceDetail,"2");
		
		ProjectionList projList = Projections.projectionList(); 
		projList.add(Projections.groupProperty("lastDate")); 
		dc.setProjection(projList); 
//		dc.setProjection(Property.forName("lastDate").group()); 
		
		dc.addOrder(Order.asc("lastDate")); 
		
		Page<DeviceDetail> page = new Page<DeviceDetail>(request, response,-1);
		List<DeviceDetail> list = new ArrayList<DeviceDetail>();
		deviceDetailDao.find(page,dc);
		 List<DeviceDetail> ls = page.getList();

		 Iterator it = ls.iterator();
		 while(it.hasNext()){
					java.sql.Timestamp lasteDateTimestamp = java.sql.Timestamp.valueOf(it.next().toString());
					Date theDate = new java.sql.Date(lasteDateTimestamp.getTime());
				    String lasteDate = DateUtils.formatDate(theDate,"yyyy-MM-dd");
//				    System.out.println("parent.id 55555555555 ffffffffff>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ lasteDate);
				    DeviceDetail dt = new DeviceDetail();
				    dt.setLastDate(theDate);
				    dt.setRemarks(lasteDate);
				    list.add(dt);
				    if(map !=null) map.put(lasteDate, lasteDate);
		 }
		 
		
		 

		 return list;
	}

	@Transactional(readOnly = false)
	public void save(DeviceDetail deviceDetail) {
		deviceDetailDao.clear();
		deviceDetailDao.save(deviceDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		deviceDetailDao.deleteById(id);
	}
	
private DetachedCriteria findForPayemtDetailDC(Device device) {
		
		DetachedCriteria dc = deviceDetailDao.createDetachedCriteria();
		String houseIds = device.getHouseIds();
		List<String> values = Lists.newArrayList();
		dc.createAlias("device", "device");
		
		
		
//		if (!"1".equals(device.getType())){
		if("1".equals(device.getModel())){

	        if(StringUtils.isNotEmpty(houseIds)){
	    		List<House>  houseList = HouseUtils.getHousesList2(houseIds);
	    		
//	    		model.addAttribute("houseList", houseList);		
	    		
	    		for(House e:houseList){
	    			values.addAll(e.getDeviceIdList());
	    		}
	    	

	    		if(values.size() > 0){
	    			dc.add(Restrictions.in("device.id", values));
	    		}
	        }
	        
	        
        
		}
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>33333333333>>>>>>>>>>>>>"+ DateUtils.formatDateTime(device.getLastDate()));
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>44444444444>>>>>>>>>>>>>"+ DateUtils.formatDateTime(device.getPaymentDate()));

        
        //排除公摊设备
//        dc.createAlias("fees", "fees");
//    	dc.add(Restrictions.ne("fees.feesType", Global.PMS_FEES_TYPE_POOL));  // 1 正常  2 公摊
    	if(device.getFees() != null){
//        	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>33333333333 device.getFees().getId()>>>>>>>>>>>>>"+device.getFees().getId());
            if(StringUtils.isNotEmpty(device.getFees().getId())){
            	dc.add(Restrictions.eq("device.fees.id", device.getFees().getId()));  // 1 正常  2 公摊
            }   		
    	}

  
		
		if(device.getParent() != null && !"1".equals(device.getModel())){
//			System.out.println(">>>>>>>>>>>>>>>>>>>> device.getParent() >>>>>>>>>>>>>>>>>>>>>>>>"+device.getParent());
//			System.out.println(">>>>>>>>>>>>>>>>>>>> getParent().getId()>>>>>>>>>>>>>>>>>>>>>>>>"+device.getParent().getId());
//			System.out.println(">>>>>>>>>>>>>>>>>>>> device.getFees().getCompany().getId() >>>>>>>>>>>>>>>>>>>>>>>>"+device.getFees().getCompany().getId());
			if (StringUtils.isNotEmpty(device.getParent().getId())){
				dc.createAlias("device.parent", "parent");
				dc.add(Restrictions.eq("parent.id", device.getParent().getId()));
			}
		}
		
		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getFees() >>>>>>>567 >>>>>>>>>>>>>>>>>"+device.getFees());
		
		if(device.getFees() != null){
			
			dc.createAlias("device.fees", "ff");
			
			if(device.getFees().getCompany() != null){
			
				dc.createAlias("ff.company", "cp");
				if(StringUtils.isNotEmpty(device.getFees().getCompany().getId())){
					dc.add(Restrictions.eq("cp.id", device.getFees().getCompany().getId()));
				}
				
			}
		}
		
	
		if(device.getHouse() != null && !"0".equals(device.getModel())){
			if(device.getHouse().getOwner()!= null){
				if(device.getHouse().getOwner().getCompany()!= null){
					dc.createAlias("device.house", "hhhhh");
					dc.createAlias("hhhhh.owner", "uuuuu");
					dc.createAlias("uuuuu.company", "ccccc");
					if(StringUtils.isNotEmpty(device.getHouse().getOwner().getCompany().getId())){
//						System.out.println(">>>>>>>>>>>>>>>>>>>> device.getHouse().getOwner().getCompany().getId() >>>>>>>>>>>>>>>>>>>>>>>>"+ device.getHouse().getOwner().getCompany().getId());
						dc.add(Restrictions.eq("ccccc.id", device.getHouse().getOwner().getCompany().getId()));
					}
					
				}

			}
		}
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getLastDate().toString() >>>>>>>>>>>>>>>>>>>>>>>>"+ device.getLastDate().toString());
		
//        if(device.getLastDate() != null){
//        	dc.add(Restrictions.or(
//        			Restrictions.gt("lastDate", device.getPaymentDate()),
//        			Restrictions.lt("paymentDate", device.getLastDate())
//			));
//        }		
		
		

		

		
		if (StringUtils.isNotEmpty(device.getType())){
			dc.add(Restrictions.eq("device.type", device.getType()));
		}	
		

		
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getType() 999999999999999999999999       >>>>>>>>>>>>>>>>>>>>>>>>"+ device.getType());
//		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getPool() 999999999999999999999999       >>>>>>>>>>>>>>>>>>>>>>>>"+ device.getPool());
		
		if("1".equals(device.getModel())){
			if(values.size() ==0){
				dc.add(Restrictions.eq("device.id", "-1")); 
			}
		}
//		if (StringUtils.isNotEmpty(device.getPool())){
//			dc.add(Restrictions.eq("pool", "1")); 
//		}
		if (StringUtils.isNotEmpty(device.getPool())){
			dc.add(Restrictions.eq("device.pool", device.getPool())); 
		}
		
		
//		if("2".equals(device.getModel())){
//				dc.add(Restrictions.eq("pool", "1")); 
//		}
		
		
//		 dc = deviceDao.createDetachedCriteria();
		dc.add(Restrictions.eq(Device.FIELD_DEL_FLAG, Device.DEL_FLAG_NORMAL));
		
//		 dc.addOrder(Order.asc("code")).addOrder(Order.asc("ff.code")); 
		
		
//		dc.addOrder(Order.asc("ff.code")); 
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>> dc.getAlias() 999999999999999999999999       >>>>>>>>>>>>>>>>>>>>>>>>"+ dc.getAlias());
		
		if("1".equals(device.getType())){
			dc.addOrder(Order.asc("ff.code")); 
		}
		
		if("2".equals(device.getType())){
//			dc.addOrder(Order.asc("ccccc.id")).addOrder(Order.asc("ff.code")).addOrder(Order.asc("code")); 
//			dc.addOrder(Order.asc("ff.code")).addOrder(Order.asc("device.code")); 
			dc.addOrder(Order.asc("ff.code")).addOrder(Order.asc("ccccc.id")).addOrder(Order.asc("device.code")); 
		}

		if("3".equals(device.getType())){
			dc.createAlias("hhhhh.unit", "unit");
			dc.createAlias("unit.buildings", "buildings");
			dc.createAlias("buildings.community", "community");
//			dc.addOrder(Order.asc("community.id")).addOrder(Order.asc("ff.code")).addOrder(Order.asc("ccccc.id")); 
//			dc.addOrder(Order.asc("community.id")).addOrder(Order.asc("unit.id")).addOrder(Order.asc("buildings.id")).addOrder(Order.asc("hhhhh.id")).addOrder(Order.asc("ff.code"));
//			dc.addOrder(Order.asc("ff.code")).addOrder(Order.asc("community.id")).addOrder(Order.asc("unit.id")).addOrder(Order.asc("buildings.id")).addOrder(Order.asc("hhhhh.id")); 
			dc.addOrder(Order.asc("ff.code")).addOrder(Order.asc("uuuuu.loginName")); 
			
		}
		
	
		
		return dc;
	}


public List<DeviceDetail> findDeviceDetails(DeviceDetail deviceDetail) {
	DetachedCriteria dc = deviceDetailDao.createDetachedCriteria();
	Device device = deviceDetail.getDevice();
	
		if(deviceDetail.getDevice() != null){
			dc.createAlias("device", "device");
		}

		if(deviceDetail.getIsPay()!= null){
				if("0".equals(deviceDetail.getIsPay())){
					dc.add(Restrictions.ltProperty("incomeMoney", "sumPayMoney"));
				}	
		}
	
		
	    if(deviceDetail.getDevice().getFees() != null){
	    	if(StringUtils.isNotBlank(deviceDetail.getDevice().getFees().getId())){
	    		
	    		 System.out.println(">>>>>>>>>>>>>>>>>>>> longinName 999999999999999999999999   88888888888888888    >>>>>>>>>>>>>>>>>>>>>>>>"+ deviceDetail.getDevice().getFees().getId());
	 	        
	    		 dc.createAlias("device.fees", "fees"); 
	    		dc.add(Restrictions.eq("fees.id", deviceDetail.getDevice().getFees().getId()));
	    	}
				
		}
	    
	    if(deviceDetail.getFirstDate()!= null){
	    	dc.add(Restrictions.between("lastDate", deviceDetail.getFirstDate(), deviceDetail.getLastDate()));
	    }	   

	
	    
	    String longinName =  device.getHouse().getOwner().getLoginName();
	
	    
	    
	    if(StringUtils.isBlank(longinName)){
			if(deviceDetail.getOfficeId() != null){
				dc.createAlias("device.house", "house");
				dc.createAlias("house.owner", "owner");
				dc.createAlias("owner.company", "company");
				dc.add(Restrictions.eq("company.id", deviceDetail.getOfficeId()));		
			}
	
			if(device.getHouse() != null){
				if(device.getHouse().getId() != null){
					dc.createAlias("device.house", "house");
					dc.add(Restrictions.eq("house.id", device.getHouse().getId()));
				}
			}
	    }else{
	    	dc.createAlias("device.house", "house");
			dc.createAlias("house.owner", "owner");
			User u = UserUtils.getUserByLoginName(longinName);
			dc.add(Restrictions.eq("owner.id", u.getId()));		
	    }
	    


	dc.add(Restrictions.eq(DeviceDetail.FIELD_DEL_FLAG, DeviceDetail.DEL_FLAG_NORMAL));

	
	dc.addOrder(Order.asc("id"));

	
	return deviceDetailDao.find(dc);
}
	
}
