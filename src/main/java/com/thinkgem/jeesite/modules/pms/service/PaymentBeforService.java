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
import com.thinkgem.jeesite.common.utils.Arith;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.uuid.SequenceUtils;
import com.thinkgem.jeesite.modules.pms.dao.MyBatisPaymentAfterDao;
import com.thinkgem.jeesite.modules.pms.dao.PaymentBeforDao;
import com.thinkgem.jeesite.modules.pms.entity.PaymentAfter;
import com.thinkgem.jeesite.modules.pms.entity.PaymentBefor;

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
	
	@Autowired
	private MyBatisPaymentAfterDao myBatisPaymentAfterDao;
	
	public PaymentBefor get(String id) {
		return paymentBeforDao.get(id);
	}
	
	public PaymentBefor getWithPay(String id) {
		PaymentBefor paymentBefor = paymentBeforDao.get(id);
		System.out.println("getWithPay>>>>>>>>>>>>>>>>>>>>>>>>>paymentBefor>>>>>"+paymentBefor);
		
		PaymentAfter paymentAfter = new PaymentAfter();
		paymentAfter.setPaymentBefor(new PaymentBefor(id));
		List<PaymentAfter> ls = myBatisPaymentAfterDao.find(paymentAfter);
		Map<String,PaymentAfter> mp = Collections3.extractToMap(ls, "paymentBefor.id");
		
		PaymentAfter paymentAfter2 = mp.get(id);
		if(paymentAfter2 != null){
			paymentBefor.setPayMoney(paymentAfter2.getRecMoney());
		}else{
			paymentBefor.setPayMoney(new BigDecimal("0"));
		}

		double recMoney = paymentBefor.getRecMoney().doubleValue();
		double payMoney = paymentBefor.getPayMoney().doubleValue();
		double costMoneyLeave = recMoney - payMoney;
		
//		System.out.println("getWithPay>>>>>>>>>>>>>>>>>>>>>>>>>costMoneyLeave>>>>>"+costMoneyLeave);
		
		paymentBefor.setCostMoneyLeave(new BigDecimal(Arith.roundEVEN(costMoneyLeave,2)+""));
		
		return paymentBefor;
	}
	
	private DetachedCriteria findPaymentBeforDC(PaymentBefor paymentBefor,String from) {
		 DetachedCriteria dc = paymentBeforDao.createDetachedCriteria();
		
		String officeId = paymentBefor.getCompany().getId();
		String type = paymentBefor.getType();
		String houseId = paymentBefor.getHouse().getId();
		String feeCode = paymentBefor.getFeeCode();
		String payFrom = paymentBefor.getPayFrom();
		Date firstDate = paymentBefor.getFirstDate();
		Date lastDate = paymentBefor.getLastDate();
		
		
//		System.out.println("findPaymentBeforDC>>>>>>>>>>>>>>>>>>>>>>>>>officeId>>>>>"+officeId);
//		System.out.println("findPaymentBeforDC>>>>>>>>>>>>>>>>>>>>>>>>>type>>>>>"+type);
//		System.out.println("findPaymentBeforDC>>>>>>>>>>>>>>>>>>>>>>>>>houseId>>>>>"+houseId);
//		System.out.println("findPaymentBeforDC>>>>>>>>>>>>>>>>>>>>>>>>>firstDate>>>>>"+firstDate);
//		System.out.println("findPaymentBeforDC>>>>>>>>>>>>>>>>>>>>>>>>>lastDate>>>>>"+lastDate);
//		System.out.println("findPaymentBeforDC>>>>>>>>>>>>>>>>>>>>>>>>>payFrom>>>>>"+payFrom);
		
//		dc.createAlias("company", "company");
//		dc.createAlias("house", "house");
		
		
		if (StringUtils.isNotEmpty(officeId) && !"null".equals(officeId)){
			dc.add(Restrictions.eq("company.id", officeId));
		}
		
		if (StringUtils.isNotEmpty(houseId) && !"null".equals(houseId)){
			dc.add(Restrictions.eq("house.id", houseId));
		}
		
		if (StringUtils.isNotEmpty(type)&& !"null".equals(type)){
			dc.add(Restrictions.eq("type", type));
		}
		
		if (StringUtils.isNotEmpty(feeCode)&& !"null".equals(feeCode)){
			dc.add(Restrictions.like("feeCode", "%"+feeCode+"%"));
		}
		
		
		
		if (firstDate != null){
			dc.add(Restrictions.between("receDate", firstDate, lastDate));
		}
		
		if (StringUtils.isNotEmpty(payFrom)&& !"0".equals(payFrom)&& !"null".equals(payFrom)){
			dc.add(Restrictions.eq("payFrom", payFrom));
		}
		
		dc.add(Restrictions.eq(PaymentBefor.FIELD_DEL_FLAG, PaymentBefor.DEL_FLAG_NORMAL));
		
		return dc;
	}
	
	public Page<PaymentBefor> find(Page<PaymentBefor> page, PaymentBefor paymentBefor) {
		DetachedCriteria dc = findPaymentBeforDC(paymentBefor,"1");
		dc.addOrder(Order.desc("id"));
		return paymentBeforDao.find(page, dc);
	}
	
	
	public PaymentBefor find(PaymentBefor paymentBefor) {
		DetachedCriteria dc = findPaymentBeforDC(paymentBefor,"1");
		List<PaymentBefor> ls =  paymentBeforDao.find(dc);
		double  costSum = 0;
		for(PaymentBefor p:ls){
			costSum +=p.getRecMoney().doubleValue();
		}
		
		String costSumStr = Arith.roundEVEN(costSum, 2)+"";
	
		paymentBefor.setRecMoney(new BigDecimal(costSumStr));
		
		return paymentBefor;
	}
	
	@Transactional(readOnly = false)
	public void save(PaymentBefor paymentBefor) {
		
		String feeCode = paymentBefor.getFeeCode();
		String type = paymentBefor.getType();
		if("1".equals(type)){
			feeCode = SequenceUtils.nextVal_paymentBeforFeeCode1(feeCode);
		}else if("2".equals(type)){
			feeCode = SequenceUtils.nextVal_paymentBeforFeeCode2(feeCode);
		}else{
			feeCode = SequenceUtils.nextVal(feeCode);
		}
		
		paymentBefor.setFeeCode(feeCode);

		paymentBeforDao.clear();
		paymentBeforDao.save(paymentBefor);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		paymentBeforDao.deleteById(id);
	}
	@Transactional(readOnly = false)
	public void deleteAllData(String id) {
		
		paymentBeforDao.deleteById(id);
	}
}
