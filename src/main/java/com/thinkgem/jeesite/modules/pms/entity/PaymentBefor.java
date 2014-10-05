package com.thinkgem.jeesite.modules.pms.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.thinkgem.jeesite.common.persistence.IdEntity;



@Entity
@Table(name = "pms_payment_befor") //应收款表
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PaymentBefor extends IdEntity<PaymentBefor> {
	private String name; // name 名称
	private Date receDate; // rece_date   生成日期
	private BigDecimal costMoney; //  cost_money 应付金额
// 	private User user;              // create_by 生成人
 	private Integer sort; // '排序（升序）',
 	private Fees fees; // fees_id   费用项目
 	
 	
	public PaymentBefor() {
		super();
		this.sort = 30;
	}

	public PaymentBefor(String id) {
		this();
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne
	@JoinColumn(name="fees_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public Fees getFees() {
		return fees;
	}

	public void setFees(Fees fees) {
		this.fees = fees;
	}


	@Temporal(TemporalType.DATE)
	public Date getReceDate() {
		return receDate;
	}

	public void setReceDate(Date receDate) {
		this.receDate = receDate;
	}

	@Transient
	public BigDecimal getCostMoney() {
		return costMoney;
	}

	public void setCostMoney(BigDecimal costMoney) {
		this.costMoney = costMoney;
	}






	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	

}
