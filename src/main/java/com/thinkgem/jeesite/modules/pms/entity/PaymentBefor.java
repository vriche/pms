package com.thinkgem.jeesite.modules.pms.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkgem.jeesite.common.persistence.IdEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;



@Entity
@Table(name = "pms_payment_befor") //收款信息表
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PaymentBefor extends IdEntity<PaymentBefor> {
	private String feeCode; 		// fee_code 收款单号
	private String certCode; 		// cert_code 发票号
	private Date receDate; 			// rece_date   生成日期
	private BigDecimal recMoney; 	// rec_money 收款金额
	private BigDecimal payMoney; 	// rec_money 付款金额
	private BigDecimal costMoneyLeave; 	// rec_money 发票余额



	private String type;             // 1现付 2 预付 
	private String payType;         // pay_type 收款方式  现金 支票
	private User user;              // collect_id 收款人
	private House house = new House();
	private Office company = new Office();
	
	private Date firstDate; 
	private Date lastDate; 
	
	private String payFrom;             // 1、单位 2 个人 
	
	
//	private List<String> idList = new ArrayList();
	








	private Integer sort; 			// '排序（升序）',
	
	public PaymentBefor() {
		super();
		this.user = this.currentUser;
		this.sort = 30;
	}

	public PaymentBefor(String id) {
		this();
		this.id = id;
	}

	
	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getCertCode() {
		return certCode;
	}

	public void setCertCode(String certCode) {
		this.certCode = certCode;
	}

	public BigDecimal getRecMoney() {
		return recMoney;
	}

	public void setRecMoney(BigDecimal recMoney) {
		this.recMoney = recMoney;
	}
 	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	@ManyToOne
	@JoinColumn(name="house_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@ExcelField(title="房屋", align=1, sort=5,value="House.fullName")
	public House getHouse() {
		return house;
	}
	
	public void setHouse(House house) {
		this.house = house;
	}
	
	@ManyToOne
	@JoinColumn(name="collect_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@ManyToOne
	@JoinColumn(name="company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
//	@NotNull(message="归属公司不能为空")
 	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}

	@Temporal(TemporalType.DATE)
	public Date getReceDate() {
		return receDate;
	}

	public void setReceDate(Date receDate) {
		this.receDate = receDate;
	}



	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getPayFrom() {
		return payFrom;
	}

	public void setPayFrom(String payFrom) {
		this.payFrom = payFrom;
	}
	
	
	@Transient
	public Date getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}
	@Transient
	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	@Transient
	public BigDecimal getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	
	@Transient
	public BigDecimal getCostMoneyLeave() {
		return costMoneyLeave;
	}

	public void setCostMoneyLeave(BigDecimal costMoneyLeave) {
		this.costMoneyLeave = costMoneyLeave;
	}
	
//	@Transient
//	public List<String> getIdList() {
//		return idList;
//	}
//
//	public void setIdList(List<String> idList) {
//		this.idList = idList;
//	}
}
