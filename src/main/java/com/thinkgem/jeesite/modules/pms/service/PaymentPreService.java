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
import com.thinkgem.jeesite.modules.pms.entity.PaymentPre;
import com.thinkgem.jeesite.modules.pms.dao.PaymentPreDao;

/**
 * 单元信息Service
 * @author vriche
 * @version 2014-04-23
 */
@Component
@Transactional(readOnly = true)
public class PaymentPreService extends BaseService {

	@Autowired
	private PaymentPreDao paymentPreDao;
	
	public PaymentPre get(String id) {
		return paymentPreDao.get(id);
	}
	
	public Page<PaymentPre> find(Page<PaymentPre> page, PaymentPre paymentPre) {
		DetachedCriteria dc = paymentPreDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(paymentPre.getName())){
			dc.add(Restrictions.like("name", "%"+paymentPre.getName()+"%"));
		}
		dc.add(Restrictions.eq(PaymentPre.FIELD_DEL_FLAG, PaymentPre.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return paymentPreDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(PaymentPre paymentPre) {
		paymentPreDao.clear();
		paymentPreDao.save(paymentPre);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		paymentPreDao.deleteById(id);
	}
	
}
