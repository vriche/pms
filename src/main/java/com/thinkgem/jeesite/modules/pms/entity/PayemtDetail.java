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
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.IdEntity;
import com.thinkgem.jeesite.common.utils.Arith;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;


@Entity
@Table(name = "pms_payment_detail") //收款明细
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PayemtDetail extends IdEntity<PayemtDetail> {
	
	private static final long serialVersionUID = 1L;
	private PaymentBefor paymentBefor; //   payment_before_id 收款凭证
	private PaymentAfter paymentAfter; //   payment_after_id 付款凭证
	private Device device; // device_id  表设备
	private House house = new House();// 拥有分摊到的子设备


	private BigDecimal firstNum; // first_num 上次读数
	private BigDecimal lastNum; // last_num 本次读数
	
	private Date firstDate;   // first_date 上次日期
	private Date lastDate;    // last_date 本次日期
	private Date paymentDate; // payment_date 交费限期
	
	private BigDecimal unitPrice; // unit_price '单位价格',
	
	private BigDecimal usageAmount; // usage_amount 本次用量	
	private BigDecimal poolUsageAmount; // pool_usage_amount  本次公摊用量
	
	private BigDecimal payMoney;   // pay_money  费用金额
	private BigDecimal poolPayMoney; // pool_pay_money  本次公摊金额

	private BigDecimal sumUsageAmount;   // sum_usage_amount  总用量	
	private BigDecimal costMoney;   // cost_money  费用金额
	
	private BigDecimal incomeMoney; // income_money 收款金额
	private BigDecimal favourMoney; // favour_money 优惠金额

	private String isPay;// is_pay是否已付款
	private Integer sort; // '排序（升序）',
	
//	private List<String> payemtDetails = Lists.newArrayList();
	
	private String payemtDetails;
	
	private String officeId;
	

	

	
	






	public PayemtDetail() {
		super();
		this.isPay = NO;
		this.sort = 0;
		this.firstDate = new java.util.Date();
		this.lastDate = new java.util.Date();
		this.paymentDate = new java.util.Date();
	}
	
	public PayemtDetail(String id){
		this();
		this.id = id;
	}
	
	
	@ManyToOne
	@JoinColumn(name="payment_before_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public PaymentBefor getPaymentBefor() {
		return paymentBefor;
	}

	public void setPaymentBefor(PaymentBefor paymentBefor) {
		this.paymentBefor = paymentBefor;
	}
	@ManyToOne
	@JoinColumn(name="payment_after_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public PaymentAfter getPaymentAfter() {
		return paymentAfter;
	}

	public void setPaymentAfter(PaymentAfter paymentAfter) {
		this.paymentAfter = paymentAfter;
	}


	
	@ManyToOne
	@JoinColumn(name="device_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	
	@Temporal(TemporalType.DATE)
	public Date getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}
	@Temporal(TemporalType.DATE)
	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	@Temporal(TemporalType.DATE)
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}


	

	


	public BigDecimal getCostMoney() {
		return costMoney;
	}

	public void setCostMoney(BigDecimal costMoney) {
		this.costMoney = costMoney;
	}



	public BigDecimal getFavourMoney() {
		return favourMoney;
	}

	public void setFavourMoney(BigDecimal favourMoney) {
		this.favourMoney = favourMoney;
	}



	@Length(min=1, max=1)
	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}




	public BigDecimal getPoolPayMoney() {
		return poolPayMoney;
	}

	public void setPoolPayMoney(BigDecimal poolPayMoney) {
		this.poolPayMoney = poolPayMoney;
	}




	@Transient
	public String getPayemtDetails() {
		return payemtDetails;
	}

	public void setPayemtDetails(String payemtDetails) {
		this.payemtDetails = payemtDetails;
	}
	
	
	
	private String companyStr;
	private String houseArea;
	private String ownerStr;
	private String feesStr;
	private String lastDateStr;
	private String paymentDateStr;
	
	private BigDecimal qiankuan; //  欠款
	
//	private BigDecimal firstNumStr; //上次读数
//	private BigDecimal curNumStr;  //本次读数


	@ManyToOne
	@JoinColumn(name="house_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@ExcelField(title="房屋", align=1, sort=1,value="House.fullName")
	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}
	
	
	@Transient
	@ExcelField(title="面积", align=2, sort=2,value="House.buildArea")
	@NotFound(action = NotFoundAction.IGNORE)
	public String getHouseArea() {
		return houseArea;
	}

	public void setHouseArea(String houseArea) {
		this.houseArea = houseArea;
	}
	
	@Transient
	@ExcelField(title="业主", align=2, sort=3,value="House.owner.name")
	@NotFound(action = NotFoundAction.IGNORE)
	public String getOwnerStr() {
		return ownerStr;
	}

	public void setOwnerStr(String ownerStr) {
		this.ownerStr = ownerStr;
	}

	
	@Transient
	@ExcelField(title="单位", align=1, sort=4,value="House.owner.company.name")
	@NotFound(action = NotFoundAction.IGNORE)
	public String getCompanyStr() {
		return companyStr;
	}

	public void setCompanyStr(String companyStr) {
		this.companyStr = companyStr;
	}
	
	
	@Transient
	@ExcelField(title="费用", align=2, sort=5,value="Device.fees.name")
	@NotFound(action = NotFoundAction.IGNORE)
	public String getFeesStr() {
		return feesStr;
	}

	public void setFeesStr(String feesStr) {
		this.feesStr = feesStr;
	}
	
	
	@ExcelField(title="上次读数", align=3, sort=6)
	public BigDecimal getFirstNum() {
		return firstNum;
	}

	public void setFirstNum(BigDecimal firstNum) {
		this.firstNum = firstNum;
	}

	@ExcelField(title="本次读数", align=3, sort=7)
	public BigDecimal getLastNum() {
		return lastNum;
	}

	public void setLastNum(BigDecimal lastNum) {
		this.lastNum = lastNum;
	}
	
	@ExcelField(title="本次用量", align=3, sort=8)
	public BigDecimal getUsageAmount() {
		return usageAmount;
	}

	public void setUsageAmount(BigDecimal usageAmount) {
		this.usageAmount = usageAmount;
	}
	
	
	@ExcelField(title="公摊量", align=3, sort=9)
	public BigDecimal getPoolUsageAmount() {
		return poolUsageAmount;
	}

	public void setPoolUsageAmount(BigDecimal poolUsageAmount) {
		this.poolUsageAmount = poolUsageAmount;
	}
	
	@ExcelField(title="总量", align=3, sort=10)
	public BigDecimal getSumUsageAmount() {
		return sumUsageAmount;
	}

	public void setSumUsageAmount(BigDecimal sumUsageAmount) {
		this.sumUsageAmount = sumUsageAmount;
	}
	@ExcelField(title="单价", align=3, sort=11)
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	@ExcelField(title="总费用", align=3, sort=12)
	public BigDecimal getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
 	
	@ExcelField(title="已缴费", align=3, sort=13)
	public BigDecimal getIncomeMoney() {
		return incomeMoney;
	}

	public void setIncomeMoney(BigDecimal incomeMoney) {
		this.incomeMoney = incomeMoney;
	}
	
	@Transient
	@ExcelField(title="欠款", align=3, sort=14)
	public BigDecimal getQiankuan() {
//		double s = Arith.sub(Double.parseDouble(StringUtils.getNullValue(this.payMoney, "0")),Double.parseDouble(StringUtils.getNullValue(this.incomeMoney, "0")));
		double s = Double.parseDouble(StringUtils.getNullValue(this.payMoney, "0"))-Double.parseDouble(StringUtils.getNullValue(this.incomeMoney, "0"));
		s = Arith.roundEVEN(s, 2);
		String v = String.valueOf(s);
//		qiankuan = new BigDecimal(Arith.roundEVEN(s, 2));

		return  new BigDecimal(v);
	}

	public void setQiankuan(BigDecimal qiankuan) {
		this.qiankuan = qiankuan;
	}
	
	@Transient
	@ExcelField(title="读表日期", align=0, sort=20)
	public String getLastDateStr() {
		return DateUtils.formatDate(this.getLastDate(), "yyyy-MM-dd");
	}

	public void setLastDateStr(String lastDateStr) {
		this.lastDateStr = lastDateStr;
	}

	@Transient
	@ExcelField(title="交费限期", align=0, sort=21)
	public String getPaymentDateStr() {
		return DateUtils.formatDate(this.getPaymentDate(), "yyyy-MM-dd");
	}

	public void setPaymentDateStr(String paymentDateStr) {
		this.paymentDateStr = paymentDateStr;
	}
	
	
	@Transient
	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	
	
	
	
	
	
	
	
}
