/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.pms.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.entity.PaymentBefor;
import com.thinkgem.jeesite.modules.pms.dao.PaymentBeforDao;

/**
 * 单元信息Service
 * @author vriche
 * @version 2014-04-23
 */
@Component
@Transactional(readOnly = true)
public class PaymentBeforService extends BaseService {

	@Autowired
	private PaymentBeforDao paymentBeforDao;
	
	public PaymentBefor get(String id) {
		return paymentBeforDao.get(id);
	}
	
	public Page<PaymentBefor> find(Page<PaymentBefor> page, PaymentBefor paymentBefor) {
		DetachedCriteria dc = paymentBeforDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(paymentBefor.getName())){
//			dc.add(Restrictions.like("name", "%"+paymentBefor.getName()+"%"));
//		}
		dc.add(Restrictions.eq(PaymentBefor.FIELD_DEL_FLAG, PaymentBefor.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return paymentBeforDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(PaymentBefor paymentBefor) {
		paymentBeforDao.clear();
		paymentBeforDao.save(paymentBefor);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		paymentBeforDao.deleteById(id);
	}
	
}
