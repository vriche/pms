/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.service;

import java.math.BigDecimal;
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
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.dao.PayemtDetailDao;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.PayemtDetail;
import com.thinkgem.jeesite.modules.pms.utils.HouseUtils;

/**
 * 单元信息Service
 * @author vriche
 * @version 2014-04-23
 */
@Component
@Transactional(readOnly = true)
public class PayemtDetailService extends BaseService {

	@Autowired
	private PayemtDetailDao payemtDetailDao;
	
	public PayemtDetail get(String id) {
		return payemtDetailDao.get(id);
	}
	
	
private DetachedCriteria findForPayemtDetailDC(PayemtDetail payemtDetail) {
		
		Device device = payemtDetail.getDevice();
	
		DetachedCriteria dc = payemtDetailDao.createDetachedCriteria();
		
		dc.createAlias("device", "device");
		dc.createAlias("device.fees", "fees");

		String houseIds = device.getHouseIds();
		
	
		
		List<String> values = Lists.newArrayList();
		

		if("1".equals(device.getModel())){

	        if(StringUtils.isNotEmpty(houseIds)){
	    		List<House>  houseList = HouseUtils.getHousesList2(houseIds);
	    		
	    		for(House e:houseList){
	    			values.addAll(e.getDeviceIdList());
	    		}
	
	    		if(values.size() > 0){
	    			dc.add(Restrictions.in("device.id", values));
	    		}else{
	    			dc.add(Restrictions.eq("device.id", "-1")); 
	    		}
	        }
        
		}
		
	

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

  
		

		
		if(device.getFees() != null){
			if(device.getFees().getCompany() != null){
				dc.createAlias("fees.company", "cp");
				dc.add(Restrictions.eq("cp.id", device.getFees().getCompany().getId()));
			}
		}
		
	
		if(device.getHouse() != null && !"0".equals(device.getModel())){
			if(device.getHouse().getOwner()!= null){
				if(device.getHouse().getOwner().getCompany()!= null){
					dc.createAlias("device.house", "house");
					dc.createAlias("house.owner", "owner");
					dc.createAlias("owner.company", "ccccc");
					if(StringUtils.isNotEmpty(device.getHouse().getOwner().getCompany().getId())){
//						System.out.println(">>>>>>>>>>>>>>>>>>>> device.getHouse().getOwner().getCompany().getId() >>>>>>>>>>>>>>>>>>>>>>>>"+ device.getHouse().getOwner().getCompany().getId());
						dc.add(Restrictions.eq("ccccc.id", device.getHouse().getOwner().getCompany().getId()));
					}
					
				}

			}
		}
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>> device.getLastDate().toString() >>>>>>>>>>>>>>>>>>>>>>>>"+ device.getLastDate().toString());
		
        if(payemtDetail.getFirstDate()!= null){
        	dc.add(Restrictions.between("lastDate", payemtDetail.getFirstDate(), payemtDetail.getLastDate()));
        }		
        
        
//    	dc.createAlias("house.owner", "owner");
    	
    	dc.addOrder(Order.asc("owner.loginName"));
        

//		 dc = deviceDao.createDetachedCriteria();
		dc.add(Restrictions.eq(Device.FIELD_DEL_FLAG, Device.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("paymentDate"));
		
		return dc;
	}
	
	
	
	public Page<PayemtDetail> find(Page<PayemtDetail> page, PayemtDetail payemtDetail) {
		DetachedCriteria dc = payemtDetailDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(payemtDetail.getName())){
//			dc.add(Restrictions.like("name", "%"+payemtDetail.getName()+"%"));
//		}
		dc.add(Restrictions.eq(PayemtDetail.FIELD_DEL_FLAG, PayemtDetail.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return payemtDetailDao.find(page, dc);
	}
	
	public List<PayemtDetail> findAll(PayemtDetail payemtDetail) {
		DetachedCriteria dc = findForPayemtDetailDC(payemtDetail);
		
		return payemtDetailDao.find(dc);
	}
	
	public Page<PayemtDetail> findPayemtDetail(Page<PayemtDetail> page, PayemtDetail payemtDetail) {
		DetachedCriteria dc = findForPayemtDetailDC(payemtDetail);
		return payemtDetailDao.find(page, dc);
	}

	

	
	
	
	public List<PayemtDetail> findPayemtDetails(PayemtDetail payemtDetail) {
		DetachedCriteria dc = payemtDetailDao.createDetachedCriteria();
		
		if(payemtDetail.getIsPay()!= null){
//				dc.add(Restrictions.eq("isPay", payemtDetail.getIsPay()));
				if("0".equals(payemtDetail.getIsPay())){
//					System.out.println("houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 payemtDetail.getIsPay()>>"+payemtDetail.getIsPay() );  
					
//					dc.add(Restrictions.gt("incomeMoney", new BigDecimal(0)));
					dc.add(Restrictions.ltProperty("incomeMoney", "costMoney"));
					
					
				}	
		}
		
		if(payemtDetail.getHouse() != null){
			if(payemtDetail.getHouse().getId() != null){
				dc.createAlias("house", "house");
				dc.add(Restrictions.eq("house.id", payemtDetail.getHouse().getId()));
			}
		}
		
		if(payemtDetail.getDevice() != null){
				dc.createAlias("device", "device");
				dc.createAlias("device.fees", "fees");
				dc.add(Restrictions.eq("fees.id", payemtDetail.getDevice().getFees().getId()));
			
		}
		
		if(payemtDetail.getOfficeId() != null){
				dc.createAlias("house", "house");
				dc.createAlias("house.owner", "owner");
				dc.createAlias("owner.company", "company");
				dc.add(Restrictions.eq("company.id", payemtDetail.getOfficeId()));
		}
		
        if(payemtDetail.getFirstDate()!= null){
        	dc.add(Restrictions.between("paymentDate", payemtDetail.getFirstDate(), payemtDetail.getLastDate()));
        }	   	
		
		
//		if (StringUtils.isNotEmpty(payemtDetail.getName())){
//			dc.add(Restrictions.like("name", "%"+payemtDetail.getName()+"%"));
//		}
		dc.add(Restrictions.eq(PayemtDetail.FIELD_DEL_FLAG, PayemtDetail.DEL_FLAG_NORMAL));
		
//		dc.addOrder(Order.desc("id"));
		
		dc.addOrder(Order.asc("id"));
		
//		if(payemtDetail.getOfficeId() != null){
//			dc.createAlias("house", "house");
//			dc.createAlias("house.owner", "owner");
//			dc.addOrder(Order.asc("owner.loginName"));
//		}
		
		
		
		return payemtDetailDao.find(dc);
	}
	
	
	@Transactional(readOnly = false)
	public void save(PayemtDetail payemtDetail) {
		payemtDetailDao.clear();
		payemtDetailDao.save(payemtDetail);
	}
	
	
	@Transactional(readOnly = false)
	public void saveByDevice(Device device) {
		PayemtDetail payemtDetail = new PayemtDetail();
		payemtDetail.setHouse(device.getHouse());
		payemtDetail.setDevice(device);
		payemtDetail.setUnitPrice(device.getFees().getUnitPrice());
		payemtDetail.setFirstDate(device.getFirstDate());
		payemtDetail.setFirstNum(device.getFirstNum());
		payemtDetail.setLastDate(device.getLastDate());
		payemtDetail.setLastNum(device.getLastNum());
		payemtDetail.setPaymentDate(device.getPaymentDate());


//		double usageAmount =StringUtils.toBigDecimal(device.getUsageAmount()).doubleValue();
//		double poolUsageAmount =StringUtils.toBigDecimal(device.getPoolUsageAmount()).doubleValue();
//		double sumUsageAmount = usageAmount + poolUsageAmount;
		
		payemtDetail.setUsageAmount(device.getUsageAmount());
		payemtDetail.setPayMoney(device.getPayMoney());

		payemtDetail.setPoolUsageAmount(device.getPoolUsageAmount());
		payemtDetail.setPoolPayMoney(device.getPoolPayMoney());
		
		payemtDetail.setSumUsageAmount(device.getSumUsageAmount());
		payemtDetail.setCostMoney(device.getSumPayMoney());
		
		payemtDetail.setIncomeMoney(new BigDecimal(0));

		payemtDetailDao.clear();
		payemtDetailDao.save(payemtDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		payemtDetailDao.deleteById(id);
	}
	
}
