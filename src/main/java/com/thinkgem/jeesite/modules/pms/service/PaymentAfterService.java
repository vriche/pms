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

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.dao.PaymentAfterDao;
import com.thinkgem.jeesite.modules.pms.entity.PaymentAfter;

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
	
	public PaymentAfter get(String id) {
		return paymentAfterDao.get(id);
	}
	
	
	
	public List<PaymentAfter> findAll(PaymentAfter paymentAfter) {
		DetachedCriteria dc = paymentAfterDao.createDetachedCriteria();
		
		String houseId = paymentAfter.getHouse().getId();
		String type = paymentAfter.getType();
		String certCode = paymentAfter.getCertCode();
		String feeCode = paymentAfter.getFeeCode();
		
		if (StringUtils.isNotEmpty(paymentAfter.getType())){
			dc.add(Restrictions.eq("type", type));
		}
		
		if (StringUtils.isNotEmpty(certCode)){
			dc.add(Restrictions.like("certCode", certCode));
		}
		
		if (StringUtils.isNotEmpty(feeCode)){
			dc.add(Restrictions.like("feeCode", feeCode));
		}		
		if (StringUtils.isNotEmpty(houseId)){
			dc.createAlias("house", "house");
			dc.add(Restrictions.eq("house.id", houseId));
		}
		
		dc.add(Restrictions.eq(PaymentAfter.FIELD_DEL_FLAG, PaymentAfter.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		
		return paymentAfterDao.find(dc);
	}
	
	public Page<PaymentAfter> find(Page<PaymentAfter> page, PaymentAfter paymentAfter) {
		DetachedCriteria dc = paymentAfterDao.createDetachedCriteria();
		
		String houseId = paymentAfter.getHouse().getId();
		String type = paymentAfter.getType();
		String certCode = paymentAfter.getCertCode();
		String feeCode = paymentAfter.getFeeCode();
		
		
//		System.out.println("houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 getCertCode>>>>>>>>>"+paymentAfter.getCertCode() );
//		System.out.println("houseId>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111111111111111111 222222222222222222 getFeeCode>>>>>>"+paymentAfter.getFeeCode() );
		
		
		
		if (StringUtils.isNotEmpty(type)){
			dc.add(Restrictions.eq("type", type));
		}
		if (StringUtils.isNotEmpty(certCode)){
			dc.add(Restrictions.like("certCode", certCode));
		}
		
		if (StringUtils.isNotEmpty(feeCode)){
			dc.add(Restrictions.like("feeCode", feeCode));
		}
		
		if (StringUtils.isNotEmpty(houseId)){
			dc.createAlias("house", "house");
			dc.add(Restrictions.eq("house.id", houseId));
		}
		
		dc.add(Restrictions.eq(PaymentAfter.FIELD_DEL_FLAG, PaymentAfter.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		
		return paymentAfterDao.find(page, dc);
	}
	
	public PaymentAfter find(PaymentAfter paymentAfter) {
		DetachedCriteria dc = paymentAfterDao.createDetachedCriteria();
		
		String houseId = paymentAfter.getHouse().getId();
		String type = paymentAfter.getType();

		if (StringUtils.isNotEmpty(houseId)){
			dc.createAlias("house", "house");
			dc.add(Restrictions.eq("house.id", houseId));
		}		
		if (StringUtils.isNotEmpty(paymentAfter.getType())){
			dc.add(Restrictions.eq("type", type));
		}

		dc.add(Restrictions.eq(PaymentAfter.FIELD_DEL_FLAG, PaymentAfter.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));

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
