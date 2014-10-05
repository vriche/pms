package com.thinkgem.jeesite.modules.pms.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

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



@Entity
@Table(name = "pms_payment_pre") //预收款
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PaymentPre extends IdEntity<PaymentPre> {
	private String type; // type //1 个人  2、公司 
	private String name; // name 名称
	private String code; // name 凭证号
	private String user; // name 经手人
	private BigDecimal money; //  money 金额  
	private Date receDate; // rece_date   收款日期
 	private Fees fees; // fees_id   费用项目
 	private House house; // house_id  表设备
	private Office company; // company_id  公司

	private Integer sort; // '排序（升序）',
	
	
	@ManyToOne
	@JoinColumn(name="company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@ExcelField(title="公司", align=2, sort=20)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}

	public PaymentPre() {
		super();
		this.sort = 30;
	}

	public PaymentPre(String id) {
		this();
		this.id = id;
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
	public String getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@ManyToOne
	@JoinColumn(name="house_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
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
	

}
