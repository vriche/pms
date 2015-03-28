package com.thinkgem.jeesite.modules.pms.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.IdEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;


@Entity
@Table(name = "pms_device_detail") //计费设备
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DeviceDetail extends IdEntity<DeviceDetail> {
	
	private static final long serialVersionUID = 1L;
	
	
	private Device device; // device_id  表设备
//	private String feesMode; // fees_mode pms_fees_mode '公摊方式' 1：一级；2：二级；3：三级；4：四级）
	
	private BigDecimal firstNum; // first_num 上次读数
	private BigDecimal lastNum; // last_num 本次读数
	private Date firstDate; // first_date 上次日期
	private Date lastDate; // last_date 本次日期
	private Date paymentDate; // payment_date 交费限期
	private BigDecimal usageAmount; // usage_amount  本次用量 usageAmount = lastNum - firstNum;
	private BigDecimal poolUsageAmount; // pool_usage_amount  本次公摊用量
	private BigDecimal arrears;   // arrears 上次欠缴
	private Integer apportionHouseCount; // apportion_house_count  分摊户数  公表记录
	private Integer apportionCompanyCount; // apportion_company_count  分摊公司  公表记录
	
	private BigDecimal payMoney;   // arrears 应付金额 payMoney = usageAmount*unitPrice
	private BigDecimal poolPayMoney; // pool_usage_amount  本次公摊金额  poolPayMoney = poolUsageAmount*unitPrice
	private BigDecimal favourMoney; // favour_money 优惠金额
	private BigDecimal sumUsageAmount;   // 总用量 sum_usage_amount 	 sumUsageAmount = usageAmount +poolUsageAmount
	private BigDecimal sumPayMoney; // pool_usage_amount  本次公摊金额 sumPayMoney = payMoney+poolPayMoney-favourMoney
	private BigDecimal incomeMoney; // income_money 到账金额
	
	private String model;
	private Integer sort; // '排序（升序）',
	
	private Device parent;// parent 父级菜单
	private String houseIds ="";
	
	private String feesMode;  // '公摊方式' feesMode 1 按住户   2 按房屋面积    3 按加建面积  4 按使用量    5 按实际应收金额    6 自定义
	private BigDecimal unitPrice; // unit_price '单位价格'
	private String feesParams =""; //费用参数，来源于费用的 Remarks，目前取暖费按面积的90%收取

	private String isPay;// is_pay是否已付款
	
	private String poolId ="0";
	
	private String officeId ;
	
	private String payemtDetails ;
	
	private String paymentBeforId ;
	
	
//	private List<PaymentAfter> paymentAfterList = Lists.newArrayList(); // 拥有用户列表
//
//	@ManyToMany(mappedBy = "deviceDetailList", fetch=FetchType.LAZY)
//	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
//	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
//	@NotFound(action = NotFoundAction.IGNORE)
//	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//	public List<PaymentAfter> getPaymentAfterList() {
//		return paymentAfterList;
//	}
//
//	public void setPaymentAfterList(List<PaymentAfter> paymentAfterList) {
//		this.paymentAfterList = paymentAfterList;
//	}



	public DeviceDetail() {
		super();
		this.sort = 30;
		this.firstDate = new java.util.Date();
		this.lastDate = new java.util.Date();
		this.paymentDate = new java.util.Date();
		this.firstNum = new BigDecimal("0");
		this.lastNum =  new BigDecimal("0");
		this.usageAmount = new BigDecimal("0"); 
		this.poolUsageAmount = new BigDecimal("0");
		this.arrears =  new BigDecimal("0");
		
		
		this.payMoney =  new BigDecimal("0"); //
		this.poolPayMoney =  new BigDecimal("0");
		this.favourMoney =  new BigDecimal("0");

		this.sumUsageAmount =  new BigDecimal("0");  //
		this.sumPayMoney =  new BigDecimal("0"); //
		this.incomeMoney =  new BigDecimal("0");
		this.isPay = NO;
		
//		this.device = new Device("0");
		
	}
	
	@Transient
	public String getPayemtDetails() {
		return payemtDetails;
	}

	public void setPayemtDetails(String payemtDetails) {
		this.payemtDetails = payemtDetails;
	}
	public DeviceDetail(String id) {
		this();
		this.id = id;
	}
	
	
	public BigDecimal getFavourMoney() {
		return favourMoney;
	}

	public void setFavourMoney(BigDecimal favourMoney) {
		this.favourMoney = favourMoney;
	}

	public BigDecimal getIncomeMoney() {
		return incomeMoney;
	}

	public void setIncomeMoney(BigDecimal incomeMoney) {
		this.incomeMoney = incomeMoney;
	}

	@Transient
	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}

	
	
	@Transient
	public String getHouseIds() {
		return houseIds;
	}

	public void setHouseIds(String houseIds) {
		this.houseIds = houseIds;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
//	@NotNull
	public Device getParent() {
		return parent;
	}

	public void setParent(Device parent) {
		this.parent = parent;
	}
	
	
	@Transient
	@ExcelField(title="公摊编号",  align=2, sort=1)
	public String getPoolId() {
		return poolId;
	}

	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}
	

	public BigDecimal getUsageAmount() {
		return usageAmount;
	}

	public BigDecimal getLastNum() {
		return lastNum;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastDate() {
		return lastDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}


	@Temporal(TemporalType.TIMESTAMP)
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}



	
	public BigDecimal getArrears() {
		return arrears;
	}

	public void setArrears(BigDecimal arrears) {
		this.arrears = arrears;
	}

	public Integer getApportionHouseCount() {
		return apportionHouseCount;
	}

	public void setApportionHouseCount(Integer apportionHouseCount) {
		this.apportionHouseCount = apportionHouseCount;
	}

	public Integer getApportionCompanyCount() {
		return apportionCompanyCount;
	}

	public void setApportionCompanyCount(Integer apportionCompanyCount) {
		this.apportionCompanyCount = apportionCompanyCount;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}


	

	public BigDecimal getPayMoney() { 
		return payMoney;
	}


	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	
	@Transient
	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		this.model = model;
	}
	
	@ExcelField(title="公摊方式",  align=2, sort=3)
	public String getFeesMode() {
		return feesMode;
	}


	public void setFeesMode(String feesMode) {
		this.feesMode = feesMode;
	}

	@ExcelField(title="费用参数",  align=2, sort=4)
	public String getFeesParams() {
		return feesParams;
	}

	public void setFeesParams(String feesParams) {
		this.feesParams = feesParams;
	}
	public BigDecimal getPoolPayMoney() {
		return poolPayMoney;
	}


	public void setPoolPayMoney(BigDecimal poolPayMoney) {
		this.poolPayMoney = poolPayMoney;
	}
	

	public BigDecimal getSumPayMoney() {
		return sumPayMoney;
	}


	public void setSumPayMoney(BigDecimal sumPayMoney) {
		this.sumPayMoney = sumPayMoney;
	}
	

	public BigDecimal getSumUsageAmount() {
		return sumUsageAmount;
	}


	public void setSumUsageAmount(BigDecimal sumUsageAmount) {
		this.sumUsageAmount = sumUsageAmount;
	}


	
	public BigDecimal getPoolUsageAmount() {
		return poolUsageAmount;
	}
	

	public BigDecimal getFirstNum() {
		return firstNum;
	}

	private String deviceIdStr;
	private BigDecimal firstNumStr;
	private BigDecimal curNumStr;
	private BigDecimal curUsageAmountStr;
	private BigDecimal curPoolUsageAmountStr;
	private Date firsDateStr;
	
	private Date curDateStr; // 
	private Date curPaymentDateStr; // 
	private String ownerCodeStr;
	private String ownerStr;
	private String companyStr;
	

	@Transient
	@ExcelField(title="序",  align=2, sort=0)
	public String getDeviceIdStr() {
		deviceIdStr = this.id;
		return deviceIdStr;
	}
	


	
//	@ManyToOne
//	@JoinColumn(name="fees_id")
//	@NotFound(action = NotFoundAction.IGNORE)
//	@NotNull
//	@ExcelField(title="收费项目", align=2, sort=2,value="Fees.name")
//	public Fees getFees() {
//		return fees;
//	}



	@Transient
	@ExcelField(title="业主编号", align=2, sort=5,value="House.owner.loginName")
	@NotFound(action = NotFoundAction.IGNORE)
	public String getOwnerCodeStr() {
		return ownerCodeStr;
	}


	
	@Transient
	@ExcelField(title="业主", align=2, sort=6,value="House.owner.name")
	@NotFound(action = NotFoundAction.IGNORE)
	public String getOwnerStr() {
		return ownerStr;
	}
	
	

	@Transient
	@ExcelField(title="单位", align=1, sort=7,value="House.owner.company.name")
	@NotFound(action = NotFoundAction.IGNORE)
	public String getCompanyStr() {
		return companyStr;
	}
	

	
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	@NotFound(action = NotFoundAction.IGNORE)
	@ExcelField(title="上次读表日", align=2, sort=8)
	public Date getFirsDateStr() {
		return firsDateStr;
	}

	@Transient 
	@Temporal(TemporalType.TIMESTAMP)
	@NotFound(action = NotFoundAction.IGNORE)
//	@ExcelField(title="付款期限", align=2,  sort=9)
	public Date getCurPaymentDateStr() {
		return curPaymentDateStr;
	}
	
	
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	@NotFound(action = NotFoundAction.IGNORE)
	@ExcelField(title="本次读表日", align=2,  sort=9)
	public Date getCurDateStr() {
//		curDateStr =  DateUtils.parseDate(DateUtils.getDate()+" 00:00:00");
		return curDateStr;
	}
	
	@Transient
	@NotFound(action = NotFoundAction.IGNORE)
	@ExcelField(title="上次读数",  align=3,  sort=10)
	public BigDecimal getFirstNumStr() {
		return firstNumStr;
	}
	



	@Transient
	@NotFound(action = NotFoundAction.IGNORE)
	@ExcelField(title="本次读数", align=3,  sort=11)
	public BigDecimal getCurNumStr() {
		return curNumStr;
	}


	@Transient
	@NotFound(action = NotFoundAction.IGNORE)
	@ExcelField(title="本次用量", align=3, sort=12) 
	public BigDecimal getCurUsageAmountStr() {
		return curUsageAmountStr;
	}
	
	



	@Transient
	@NotFound(action = NotFoundAction.IGNORE)
	@ExcelField(title="本次公摊量", align=3, sort=13) 
	public BigDecimal getCurPoolUsageAmountStr() {
		return curPoolUsageAmountStr;
	}
	

	
	@ExcelField(title="单价", align=3, sort=14)
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	
	public void setDeviceIdStr(String deviceIdStr) {
		this.deviceIdStr = deviceIdStr;
		this.id = deviceIdStr;
	}

	
	public void setCurUsageAmountStr(BigDecimal curUsageAmountStr) {
		this.curUsageAmountStr = curUsageAmountStr;
		this.usageAmount = curUsageAmountStr;
	}



	public void setCurPoolUsageAmountStr(BigDecimal curPoolUsageAmountStr) {
		this.curPoolUsageAmountStr = curPoolUsageAmountStr;
		this.poolUsageAmount = curPoolUsageAmountStr;
	}
	


	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	
	
	public void setPoolUsageAmount(BigDecimal poolUsageAmount) {
		this.poolUsageAmount = poolUsageAmount;
	}
	

	public void setFirsDateStr(Date firsDateStr) {
		this.firsDateStr = firsDateStr;
	}
	

	public void setUsageAmount(BigDecimal usageAmount) {
		this.usageAmount = usageAmount;
	}
	

	public void setFirstNum(BigDecimal firstNum) {
		this.firstNum = firstNum;
	}

	public void setLastNum(BigDecimal lastNum) {
		this.lastNum = lastNum;
	}
	
	public void setFirstNumStr(BigDecimal firstNumStr) {
		this.firstNumStr = firstNumStr;
	}
	


	public void setCurDateStr(Date curDateStr) {
		this.curDateStr = curDateStr;
	}

	public void setCurPaymentDateStr(Date curPaymentDateStr) {
		this.curPaymentDateStr = curPaymentDateStr;
	}


	public void setCurNumStr(BigDecimal curNumStr) {
		this.curNumStr = curNumStr;
		this.lastNum = curNumStr;
	}
	public void setOwnerCodeStr(String ownerCodeStr) {
		this.ownerCodeStr = ownerCodeStr;
	}
	public void setOwnerStr(String ownerStr) {
		this.ownerStr = ownerStr;
	}

	public void setCompanyStr(String companyStr) {
		this.companyStr = companyStr;
	}
//	public void setFees(Fees fees) {
//		this.fees = fees;
//	}
	@Transient
	public String getPaymentBeforId() {
		return paymentBeforId;
	}

	public void setPaymentBeforId(String paymentBeforId) {
		this.paymentBeforId = paymentBeforId;
	}


}
//公表编码	费用名称	公表读数	分摊方法	状态