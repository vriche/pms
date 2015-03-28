/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.dao.MyBatisPaymentAfterDao;
import com.thinkgem.jeesite.modules.pms.dao.PaymentAfterDao;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.PaymentAfter;
import com.thinkgem.jeesite.modules.pms.entity.PaymentBefor;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 单元信息Service
 * @author vriche
 * @version 2014-04-23
 */
@Component
@Transactional(readOnly = true)
public class PaymentAfterService extends BaseService {

	@Autowired
	private PaymentAfterDao paymentAfterDao;
	
	@Autowired
	private MyBatisPaymentAfterDao myBatisPaymentAfterDao;
	
	
	public PaymentAfter get(String id) {
		return paymentAfterDao.get(id);
	}
	
	
	private DetachedCriteria findPaymentAfterDC(PaymentAfter paymentAfter,String from) {
		DetachedCriteria dc = paymentAfterDao.createDetachedCriteria();
		
		PaymentBefor paymentBefor =paymentAfter.getPaymentBefor();
		String paymentBeforId = paymentBefor.getId();
		String type = paymentBefor.getType();
		String payFrom = paymentBefor.getPayFrom();
		String certCode = paymentBefor.getCertCode();
		String feeCode = paymentBefor.getFeeCode();
		String houseId = paymentAfter.getHouse().getId();
		
		
		String companyId = null;
	
		if(paymentBefor != null){
			Office company = paymentBefor.getCompany();
			if(company != null){
				companyId =  company.getId();
			}
		}
		
		
		System.out.println("findPaymentBeforDC>>>>>>>>>>>>>>>>>>>>>>>>>paymentBeforId>>>>>"+paymentBeforId);
		System.out.println("findPaymentBeforDC>>>>>>>>>>>>>>>>>>>>>>>>>type>>>>>"+type);
		System.out.println("findPaymentBeforDC>>>>>>>>>>>>>>>>>>>>>>>>>houseId>>>>>"+houseId);
		System.out.println("findPaymentBeforDC>>>>>>>>>>>>>>>>>>>>>>>>>certCode>>>>>"+certCode);
		System.out.println("findPaymentBeforDC>>>>>>>>>>>>>>>>>>>>>>>>>feeCode>>>>>"+feeCode);
		System.out.println("findPaymentBeforDC>>>>>>>>>>>>>>>>>>>>>>>>>companyId>>>>>"+companyId);
		System.out.println("findPaymentBeforDC>>>>>>>>>>>>>>>>>>>>>>>>>payFrom>>>>>"+payFrom);
		

		dc.createAlias("paymentBefor", "paymentBefor");
		dc.createAlias("deviceDetail", "deviceDetail");
		dc.createAlias("deviceDetail.device", "device");
		dc.createAlias("device.house", "house");
		dc.createAlias("house.owner", "owner");
		dc.createAlias("owner.company", "company");


		if (StringUtils.isNotEmpty(paymentBeforId)){
			dc.add(Restrictions.eq("paymentBefor.id", paymentBeforId));
		}
		
		if (StringUtils.isNotEmpty(type)){
			dc.add(Restrictions.eq("paymentBefor.type", type));
		}
		
		if (StringUtils.isNotEmpty(payFrom)){
			dc.add(Restrictions.eq("paymentBefor.payFrom", payFrom));
		}
		
		if (StringUtils.isNotEmpty(certCode)){
			dc.add(Restrictions.like("paymentBefor.certCode", certCode));
		}
		
		if (StringUtils.isNotEmpty(feeCode)){
			dc.add(Restrictions.like("paymentBefor.feeCode", feeCode));
		}
		
		if (StringUtils.isNotEmpty(houseId)){
			dc.add(Restrictions.eq("house.id", houseId));
		}	
		
		if (StringUtils.isNotEmpty(companyId)){
			dc.add(Restrictions.eq("company.id", companyId));
		}	
		
		dc.add(Restrictions.eq(PaymentAfter.FIELD_DEL_FLAG, PaymentAfter.DEL_FLAG_NORMAL));
		
		return dc;
	}
	
	public List<PaymentAfter> findAll(PaymentAfter paymentAfter) {
		DetachedCriteria dc = findPaymentAfterDC(paymentAfter,"1");
		dc.addOrder(Order.desc("id"));
		return paymentAfterDao.find(dc);
	}
	
	public Map<String,PaymentAfter> findGroupByBeforId(PaymentAfter paymentAfter) {
		PaymentBefor paymentBefor = paymentAfter.getPaymentBefor();
		String officeId = paymentBefor.getCompany().getId();
		String type = paymentBefor.getType();
		String houseId = paymentBefor.getHouse().getId();
		String feeCode = paymentBefor.getFeeCode();
		String payFrom = paymentBefor.getPayFrom();
		Date firstDate = paymentBefor.getFirstDate();
		Date lastDate = paymentBefor.getLastDate();
		
//		System.out.println("findGroupByBeforId>>>>>>>>>>>>>>>>>>>>>>>>>houseId 1>>>>>"+paymentBefor.getHouse());
//		System.out.println("findGroupByBeforId>>>>>>>>>>>>>>>>>>>>>>>>>houseId 2>>>>>"+paymentBefor.getHouse().getId());
//		System.out.println("findGroupByBeforId>>>>>>>>>>>>>>>>>>>>>>>>>houseId 3>>>>>"+paymentBefor.getHouse().getId().equals("null"));

		System.out.println("findGroupByBeforId>>>>>>>>>>>>>>>>>>>>>>>>>houseId>>>>>"+ houseId);
		System.out.println("findGroupByBeforId>>>>>>>>>>>>>>>>>>>>>>>>>officeId>>>>>"+officeId);
		System.out.println("findGroupByBeforId>>>>>>>>>>>>>>>>>>>>>>>>>type>>>>>"+type);
		System.out.println("findGroupByBeforId>>>>>>>>>>>>>>>>>>>>>>>>>firstDate>>>>>"+firstDate);
		System.out.println("findGroupByBeforId>>>>>>>>>>>>>>>>>>>>>>>>>lastDate>>>>>"+lastDate);
		System.out.println("findGroupByBeforId>>>>>>>>>>>>>>>>>>>>>>>>>payFrom>>>>>"+payFrom);
		
		if("0".equals(payFrom) ||"".equals(payFrom)||"null".equals(payFrom)){
			paymentBefor.setPayFrom(null);
		}
		if("null".equals(houseId) ||"".equals(houseId)||"0".equals(houseId)){
			paymentBefor.setHouse(new House());
		}

		List<PaymentAfter> ls = myBatisPaymentAfterDao.find(paymentAfter);

//		for(PaymentAfter pAfter:ls){
//			 PaymentBefor pBefor = pAfter.getPaymentBefor();
//			 System.out.println("findGroupByBeforId>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ pBefor.getId());
//
//		}
//		Map<String,PaymentAfter> mp = Collections3.extractToMap(ls, "id");
		Map<String,PaymentAfter> mp = Collections3.extractToMap(ls, "paymentBefor.id");
//		Map<String,PaymentAfter> mp = new HashMap<String,PaymentAfter>();
		return mp;
	}
	
	public List<PaymentAfter> findGroupByFees( Map<String, Object> params) {
		
		List<PaymentAfter> ls = myBatisPaymentAfterDao.findGroupByFees(params);

		return ls;
	}

	public Page<PaymentAfter> find(Page<PaymentAfter> page, PaymentAfter paymentAfter) {
		DetachedCriteria dc = findPaymentAfterDC(paymentAfter,"1");
//		dc.addOrder(Order.desc("paymentBefor.receDate"));
//		dc.addOrder(Order.desc("deviceDetail.id"));
//		dc.addOrder(Order.desc("device.fees.id"));
		
		
		return paymentAfterDao.find(page, dc);
	}
	
	

	
	public PaymentAfter find(PaymentAfter paymentAfter) {
		DetachedCriteria dc = findPaymentAfterDC(paymentAfter,"1");
		List<PaymentAfter> ls =  paymentAfterDao.find(dc);
		double  costSum = 0;
		for(PaymentAfter p:ls){
			costSum +=p.getRecMoney().doubleValue();
		}
		paymentAfter.setRecMoney(new BigDecimal(costSum));
		
		return paymentAfter;
	}
	
	
	@Transactional(readOnly = false)
	public void save(PaymentAfter paymentAfter) {
		paymentAfterDao.clear();
		paymentAfterDao.save(paymentAfter);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		paymentAfterDao.deleteById(id);
	}
	
}
