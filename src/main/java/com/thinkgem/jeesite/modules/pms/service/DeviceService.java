/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.dao.DeviceDao;
import com.thinkgem.jeesite.modules.pms.dao.HouseDao;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.utils.DeviceUtils;
import com.thinkgem.jeesite.modules.pms.utils.FeesUtils;
import com.thinkgem.jeesite.modules.pms.utils.HouseUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 单元信息Service
 * @author vriche
 * @version 2014-04-23
 */
@Component
@Transactional(readOnly = true)
public class DeviceService extends BaseService {

	@Autowired
	private DeviceDao deviceDao;
	
	@Autowired
	private HouseDao houseDao;
	
	public Device get(String id) {
		return deviceDao.get(id);
	}
	
	public Page<Device> find(Page<Device> page, Device device) {
		
		DetachedCriteria dc = findForPayemtDetailDC(device);

		dc.add(Restrictions.eq(Device.FIELD_DEL_FLAG, Device.DEL_FLAG_NORMAL));
//		dc.addOrder(Order.asc("code"));
		return deviceDao.find(page, dc);
	}
	
	
	public List<Device> findAll(Device device) {
		
		DetachedCriteria dc = findForPayemtDetailDC(device);

		dc.add(Restrictions.eq(Device.FIELD_DEL_FLAG, Device.DEL_FLAG_NORMAL));

	
		return deviceDao.find(dc);
	}
	
	
	public Page<Device> findBuild(Page<Device> page, Device device) {
		DetachedCriteria dc = deviceDao.createDetachedCriteria();
		
		String houseIds = device.getHouseIds();
        if(StringUtils.isNotEmpty(houseIds)){
    		List<House>  houseList = HouseUtils.getHousesList(houseIds);
    		List<String> values = Lists.newArrayList();
    		for(House e:houseList){
    			values.addAll(e.getDeviceIdList());
    		}
    	
//    		List values = Collections3.extractToList(devList,"id");
    		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>33333333333>>>>>>>>>>>>>"+values);
    		if(values.size() > 0){
    			dc.add(Restrictions.in("id", values));
    		}
        }
        
        if (device.getFees() != null){
			dc.createAlias("fees", "fees");
			String feesType= device.getFees().getFeesType();
			String feesId= device.getFees().getId();
			
//			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>feesType>>>>>>>>>>>>>"+feesType);
			if (StringUtils.isNotEmpty(feesType)){
				dc.add(Restrictions.eq("fees.feesType", feesType));
			}
			
			if (StringUtils.isNotEmpty(feesId)){
				dc.add(Restrictions.eq("fees.id", feesId));
			}
		}
		
//		if (StringUtils.isNotEmpty(device.getType())){
			dc.add(Restrictions.ne("type", "1"));
//		}
		dc.add(Restrictions.eq(Device.FIELD_DEL_FLAG, Device.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("code"));
		return deviceDao.find(page, dc);
	}
	
	public List<Device> find(Device device) {
		DetachedCriteria dc = deviceDao.createDetachedCriteria();

		String houseIds = device.getHouseIds();
        if(StringUtils.isNotEmpty(houseIds)){
    		List<House>  houseList = HouseUtils.getHousesList(houseIds);
    		List<String> values = Lists.newArrayList();
    		for(House e:houseList){
    			values.addAll(e.getDeviceIdList());
    		}
    	
//    		List values = Collections3.extractToList(devList,"id");
    		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>33333333333>>>>>>>>>>>>>"+values);
    		if(values.size() > 0){
    			dc.add(Restrictions.in("id", values));
    		}
        }

		if (device.getFees() != null){
			dc.createAlias("fees", "fees");
			String feesId = device.getFees().getId();
			String feesType = device.getFees().getFeesType();
			
			if(StringUtils.isNotEmpty(feesId)) dc.add(Restrictions.eq("fees.id", feesId));
			if(StringUtils.isNotEmpty(feesType)) dc.add(Restrictions.eq("fees.feesType", feesType));
			
			if (device.getFees().getCompany() != null){
				String proCompanyId =  device.getFees().getCompany().getId();
				dc.add(Restrictions.eq("fees.company.id", proCompanyId));
			}
		}
		
		if (device.getParent() != null){
			if (StringUtils.isNotEmpty(device.getParent().getId())){
				dc.add(Restrictions.eq("parent.id", device.getParent().getId()));
			}
		}
		
		
	
		if (StringUtils.isNotEmpty(device.getType())){
				dc.add(Restrictions.eq("type", device.getType()));
		}
		
//		dc.add(Restrictions.ne("id","0"));
		
		dc.add(Restrictions.eq(Device.FIELD_DEL_FLAG, Device.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("code"));
		return deviceDao.find(dc);
	}
	
	
	public List<Device> findForPayemtDetail(Device device) {
		DetachedCriteria dc = deviceDao.createDetachedCriteria();

		String houseIds = device.getHouseIds();
        if(StringUtils.isNotEmpty(houseIds)){
    		List<House>  houseList = HouseUtils.getHousesList(houseIds);
    		List<String> values = Lists.newArrayList();
    		for(House e:houseList){
    			values.addAll(e.getDeviceIdList());
    		}
    	
//    		List values = Collections3.extractToList(devList,"id");
    		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>33333333333>>>>>>>>>>>>>"+values);
    		if(values.size() > 0){
    			dc.add(Restrictions.in("id", values));
    		}
        }

        //排除公摊设备
//        dc.createAlias("fees", "fees");
//		dc.add(Restrictions.ne("fees.feesType", "2"));
		
		
		dc.add(Restrictions.eq(Device.FIELD_DEL_FLAG, Device.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("code"));
		return deviceDao.find(dc);
	}
	
	
	private DetachedCriteria findForPayemtDetailDC(Device device) {
		
		DetachedCriteria dc = deviceDao.createDetachedCriteria();

		String houseIds = device.getHouseIds();
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>33333333333 houseIds>>>>>>>>>>>>>"+houseIds);
		
		List<String> values = Lists.newArrayList();
		
//		if (!"1".equals(device.getType())){
		if("1".equals(device.getModel())){

	        if(StringUtils.isNotEmpty(houseIds)){
	    		List<House>  houseList = HouseUtils.getHousesList2(houseIds);
	    		
//	    		model.addAttribute("houseList", houseList);		
	    		
	    		for(House e:houseList){
	    			values.addAll(e.getDeviceIdList());
	    		}
	    	

	    		if(values.size() > 0){
	    			dc.add(Restrictions.in("id", values));
	    		}
	        }
	        
	        
        
		}
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>33333333333>>>>>>>>>>>>>"+ DateUtils.formatDateTime(device.getLastDate()));
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>44444444444>>>>>>>>>>>>>"+ DateUtils.formatDateTime(device.getPaymentDate()));
        
//        if(StringUtils.isNotEmpty(device.getLastDate().toString())){
//        	dc.add(Restrictions.or(
//        			Restrictions.gt("lastDate", device.getPaymentDate()),
//        			Restrictions.lt("paymentDate", device.getLastDate())
//			));
//        	
//        }
        
        
        //排除公摊设备
//        dc.createAlias("fees", "fees");
//    	dc.add(Restrictions.ne("fees.feesType", Global.PMS_FEES_TYPE_POOL));  // 1 正常  2 公摊
    	if(device.getFees() != null){
//        	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>33333333333 device.getFees().getId()>>>>>>>>>>>>>"+device.getFees().getId());
            if(StringUtils.isNotEmpty(device.getFees().getId())){
            	dc.add(Restrictions.eq("fees.id", device.getFees().getId()));  // 1 正常  2 公摊
            }   		
    	}

  
		
		if(device.getParent() != null && !"1".equals(device.getModel())){
//			System.out.println(">>>>>>>>>>>>>>>>>>>> device.getParent() >>>>>>>>>>>>>>>>>>>>>>>>"+device.getParent());
//			System.out.println(">>>>>>>>>>>>>>>>>>>> getParent().getId()>>>>>>>>>>>>>>>>>>>>>>>>"+device.getParent().getId());
//			System.out.println(">>>>>>>>>>>>>>>>>>>> device.getFees().getCompany().getId() >>>>>>>>>>>>>>>>>>>>>>>>"+device.getFees().getCompany().getId());
			if (StringUtils.isNotEmpty(device.getParent().getId())){
				dc.createAlias("parent", "parent");
				dc.add(Restrictions.eq("parent.id", device.getParent().getId()));
			}
		}
		
		if(device.getFees() != null){
			if(device.getFees().getCompany() != null){
				dc.createAlias("fees", "ff");
				dc.createAlias("ff.company", "cp");
				if(StringUtils.isNotEmpty(device.getFees().getCompany().getId())){
					dc.add(Restrictions.eq("cp.id", device.getFees().getCompany().getId()));
				}
				
			}
		}
		
	
		if(device.getHouse() != null && !"0".equals(device.getModel())){
			if(device.getHouse().getOwner()!= null){
				if(device.getHouse().getOwner().getCompany()!= null){
					dc.createAlias("house", "hhhhh");
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
		
        if(device.getLastDate() != null){
        	dc.add(Restrictions.or(
        			Restrictions.gt("lastDate", device.getPaymentDate()),
        			Restrictions.lt("paymentDate", device.getLastDate())
			));
        }		
		
		

		
		if (StringUtils.isNotEmpty(device.getName())){
			dc.add(Restrictions.like("name", "%"+device.getName()+"%"));
		}
		
		if (StringUtils.isNotEmpty(device.getType())){
			dc.add(Restrictions.eq("type", device.getType()));
		}	
		

		
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getType() 999999999999999999999999       >>>>>>>>>>>>>>>>>>>>>>>>"+ device.getType());
//		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getPool() 999999999999999999999999       >>>>>>>>>>>>>>>>>>>>>>>>"+ device.getPool());
		
		if("1".equals(device.getModel())){
			if(values.size() ==0){
				dc.add(Restrictions.eq("id", "-1")); 
			}
		}
//		if (StringUtils.isNotEmpty(device.getPool())){
//			dc.add(Restrictions.eq("pool", "1")); 
//		}
		if (StringUtils.isNotEmpty(device.getPool())){
			dc.add(Restrictions.eq("pool", device.getPool())); 
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
			dc.addOrder(Order.asc("ff.code")).addOrder(Order.asc("ccccc.id")).addOrder(Order.asc("code")); 
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
	
	public Page<Device> findForPayemtDetail(Page<Device> page, Device device) {
		DetachedCriteria dc = findForPayemtDetailDC(device);

		Page<Device> p = new Page<Device>();
		
//		if(values.size() >0){
			p =  deviceDao.find(page,dc);
//		}else{
//			p = new Page<Device>();
//		}
		
			 //feesMode 1 按住户   2 按房屋面积    3 按加建面积  4 按使用量    5 按实际应收金额    6 自定义
			DeviceUtils.resetList(p.getList());
			
			if(device.getParent() != null){
				if (StringUtils.isNotEmpty(device.getParent().getId())){
					//计算公摊
					DeviceUtils.resetPoolList(device.getParent().getId(),page.getList());
				}

			}

        return p;
	
	}
	
	
	public void saveDeviceByHouseList(String proCompanyId,List<House> ls){
		
		List<Fees> feesList = FeesUtils.getALLFees(proCompanyId);
		
		System.out.println("importFile>>>>>>>>>>>>>>1>>>>>>>>>>>>>>>>>>>>feesList.size()>>>>>>>>"+feesList.size());

		for(House h:ls){
			
			if(!"0".equals(h.getId())){

			User user = h.getOwner();
			String userType = user.getUserType(); //2 业主  3、法人
			String deviceType = "3"; //2 单位  3、个人
			
			System.out.println("importFile>>>>>>>>>>>>>>>>>2>>>>>>>>>>>>>>>>>feesList.size()>>>>>>>>"+feesList.size());
			
			   for(Fees fees:feesList){
				   String feesModel = fees.getFeesMode();
				   Device device = new Device();
				  
				   device.setHouse(h);
				   device.setFees(fees);
				   device.setParent(new Device());
				   if("4".equals(feesModel)){ //按使用量
					   device.setPool("1");
				   }else{
					   device.setPool("0");
				   }
				   
				   if("3".equals(userType)){
					   deviceType = "2";
				   }
				   device.setType(deviceType);	   
				   
                   String phone = h.getOwner().getPhone();
				   

					   if("5".equals(feesModel) && StringUtils.isBlank(phone)){
						  
					   }else{
						   System.out.println("importFile>>>>>>>3>>>>>>>>>>>>>>>>>>>>>>>>>>>feesList.size()>>>>>>>>"+feesList.size());
						   this.save(device);
						   System.out.println("importFile>>>>>>>4>>>>>>>>>>>>>>>>>>>>>>>>>>>feesList.size()>>>>>>>>"+feesList.size());
					   }

			  
				   
			   }		
			}
		}
	}

	public List<Device> findForPayemtDetailAll(Device device) {
		DetachedCriteria dc = findForPayemtDetailDC(device);
		List<Device> ls = deviceDao.find(dc);
		// feesMode 1 按住户 2 按房屋面积 3 按加建面积 4 按使用量 5 按实际应收金额 6 自定义
	
		
		DeviceUtils.resetList(ls);
		
		if(device.getParent() != null){
			if (StringUtils.isNotEmpty(device.getParent().getId())){
				DeviceUtils.resetPoolList(device.getParent().getId(), ls);
			}
		}
		
		return ls;
	}
	
	
	
	
	
	
	
	
	public List<Device> findAllList(String houseId,String feesId){
		return deviceDao.findAllList(houseId,feesId);
	}
	
	public List<Device> findAllChild(String houseId,String parentId){
		return deviceDao.findAllChild(houseId,parentId);
	}
	
	@Transactional(readOnly = false)
	public void save(Device device) {

//		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getHouse() >>>>>>>>>>>>>>>>>>>>>>>>"+device.getHouse());
//		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getHouse().getId() 22222222222 >>>>>>>>>>>>>>>>>>>>>>>>"+device.getHouse().getId());
		
		String houseId = "" ;
		if("1".equals(device.getType())){
			device.setHouse(new House("0"));
			houseId = "0";
		}else{
			houseId = device.getHouse().getId();
			if (StringUtils.isNotEmpty(houseId)){
				if(houseId.indexOf(",")>-1){
					String[] s = device.getHouse().getId().split(",");
					houseId = s[s.length-1];
				}
			}else{
				houseId = "0";
			}
		}
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>>  device.getPool() >>>>>>>>666666666666666666>>>>>>>>>>>>>>>>"+ device.getPool());
		device.setHouse(houseDao.get(houseId));
		
		Fees fees = FeesUtils.getFees(device.getFees().getId());
		device.setFees(fees);
		String pool = device.getPool() == null?"0":device.getPool();
		device.setPool(pool);
		device.setName("");
		String code = DeviceUtils.getCode(device);
		device.setCode(code);	

		if(device.getParent() ==null){
				device.setParent(new Device("0"));
				device.setParentIds("0,");
		}else{
			if(StringUtils.isEmpty(device.getParent().getId())){
				device.setParent(new Device("0"));
				device.setParentIds("0,");
			}	
		}

		

//		device.setFirstDate(DateUtils.parseDate(DateUtils.getDateStart(device.getFirstDate())));
//		device.setLastDate(DateUtils.parseDate(DateUtils.getDateStart(device.getLastDate())));
//		device.setPaymentDate(DateUtils.parseDate(DateUtils.getDateStart(device.getPaymentDate())));

		deviceDao.clear();
		deviceDao.save(device);

	}
	
	
	
	@Transactional(readOnly = false)
	public void saveDetail(Device device) {
		
//		device.setFirstDate(DateUtils.parseDate(DateUtils.getDateStart(device.getFirstDate())));
//		device.setLastDate(DateUtils.parseDate(DateUtils.getDateStart(device.getLastDate())));
//		device.setPaymentDate(DateUtils.parseDate(DateUtils.getDateStart(device.getPaymentDate())));
		
		deviceDao.clear();
		deviceDao.save(device);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		deviceDao.deleteById(id);
	}
	
}
