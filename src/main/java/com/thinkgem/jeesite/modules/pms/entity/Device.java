package com.thinkgem.jeesite.modules.pms.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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


@Entity
@Table(name = "pms_device") //计费设备
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Device extends IdEntity<Device> {
	
	private static final long serialVersionUID = 1L;
	
	
	private String type; // tyep 设备类型//1 公摊  2、单位  3、个人
	private Device parent;// parent 父级菜单
	private String parentIds;// parent_ids 所有父级编号
	private String code; // code 编号  私有表=房号+收费编号 ，公表自定义、固话号码    
	private String name; 
	
	private String codeName; 


	private String feesMode; // fees_mode pms_fees_mode '公摊方式' 1：一级；2：二级；3：三级；4：四级）
	



	private BigDecimal firstNum; // first_num 上次读数
	private BigDecimal lastNum; // last_num 本次读数


	private Date firstDate; // first_date 上次日期
	private Date lastDate; // last_date 本次日期
	private Date paymentDate; // payment_date 交费限期
	private BigDecimal usageAmount; // usage_amount  本次用量
	private BigDecimal poolUsageAmount; // pool_usage_amount  本次公摊用量
	


	private BigDecimal arrears;   // arrears 上次欠缴
	private Integer apportionHouseCount; // apportion_house_count  分摊户数  公表记录
	private Integer apportionCompanyCount; // apportion_company_count  分摊公司  公表记录
	private String enable; // enable '按单价收费 按阶梯收费',
	private String pool; // pool '是否公摊',
	


	private BigDecimal payMoney;   // arrears 应付金额
	private BigDecimal poolPayMoney; // pool_usage_amount  本次公摊金额
	
	private BigDecimal sumUsageAmount;   // sum_usage_amount  总用量	
	private BigDecimal sumPayMoney; // pool_usage_amount  本次公摊金额




	private String model;



	private Integer sort; // '排序（升序）',
	
	private Fees fees;     //   收费项目
//	private House house;     //   收费项目
	private List<Device> childList = Lists.newArrayList();// 拥有分摊到的子设备

//	private List<House> houseList = Lists.newArrayList();// 拥有分摊到的子设备
	private House house = new House();// 拥有分摊到的子设备
	
//	private Office company; // company_id 归属公司
	
	private String houseIds ="";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@ManyToOne
//	@JoinColumn(name="company_id")
//	@NotFound(action = NotFoundAction.IGNORE)
//	@JsonIgnore
//	@NotNull(message="归属单位不能为空")
//	@ExcelField(title="归属单位", align=2, sort=20)
//	public Office getCompany() {
//		return company;
//	}
//
//	public void setCompany(Office company) {
//		this.company = company;
//	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
//	@ManyToMany(mappedBy = "deviceList", fetch=FetchType.LAZY)
//	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
//	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
//	@NotFound(action = NotFoundAction.IGNORE)
//	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//	public List<House> getHouseList() {
//		return houseList;
//	}
//
//	public void setHouseList(List<House> houseList) {
//		this.houseList = houseList;
//	}



	public Device() {
		super();
		this.sort = 30;
		this.firstDate = new java.util.Date();
		this.lastDate = new java.util.Date();
		this.paymentDate = new java.util.Date();
		this.enable ="1";
		this.pool ="1";
		this.firstNum = new BigDecimal("0");
		this.lastNum =  new BigDecimal("0");
		this.usageAmount = new BigDecimal("0");
		this.poolUsageAmount = new BigDecimal("0");
		this.arrears =  new BigDecimal("0");
		this.payMoney =  new BigDecimal("0");
		this.poolPayMoney =  new BigDecimal("0");
		
	}

	public Device(String id) {
		this();
		this.id = id;
	}
	
	


	
	
//	@ManyToOne
//	@JoinColumn(name="house_id")
//	@NotFound(action = NotFoundAction.IGNORE)
//	@NotNull
//	public House getHouse() {
//		return house;
//	}
//	public void setHouse(House house) {
//		this.house = house;
//	}
	
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
	
	
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@Where(clause = "del_flag='" + DEL_FLAG_NORMAL + "'")
	@OrderBy(value = "sort")
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Device> getChildList() {
		return childList;
	}

	public void setChildList(List<Device> childList) {
		this.childList = childList;
	}

//	@Length(min=1, max=255)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
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
	

	
	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}
	
	public String getPool() {
		return pool;
	}


	public void setPool(String pool) {
		this.pool = pool;
	}

	


	@Transient
	public String getHouseIds() {
		return this.houseIds;
	}
	
	@Transient
	public BigDecimal getPayMoney() { 
//		double price = this.fees.getUnitPrice().doubleValue();
//	    double pay  = this.usageAmount.doubleValue() * price;
//	    return new BigDecimal(pay);
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
	
	
	public String getFeesMode() {
		return feesMode;
	}


	public void setFeesMode(String feesMode) {
		this.feesMode = feesMode;
	}

	
	@Transient
	public BigDecimal getPoolPayMoney() {
		return poolPayMoney;
	}


	public void setPoolPayMoney(BigDecimal poolPayMoney) {
		this.poolPayMoney = poolPayMoney;
	}
	
	@Transient
	public BigDecimal getSumPayMoney() {
		return sumPayMoney;
	}


	public void setSumPayMoney(BigDecimal sumPayMoney) {
		this.sumPayMoney = sumPayMoney;
	}
	
	@Transient
	public BigDecimal getSumUsageAmount() {
		return sumUsageAmount;
	}


	public void setSumUsageAmount(BigDecimal sumUsageAmount) {
		this.sumUsageAmount = sumUsageAmount;
	}
//	public String getHouseIds() {
//		List<String> nameIdList = Lists.newArrayList();
//		if(houseIds != null && !"".equals(houseIds)){
//			if(houseIds.indexOf(",")>-1){
//				String[] s= houseIds.split(",");
//				for (String id : s) { 
//					if(id.contains("House")){
//						nameIdList.add(id);
//					}
//				}
//			}
//		}
//
//		if(nameIdList.size()>0){
//			return StringUtils.join(nameIdList, ",");
//		}else{
//			return "";
//		}
//	}


	public void setHouseIds(String houseIds) {
		this.houseIds = houseIds;
	}
	

	
	
	public BigDecimal getPoolUsageAmount() {
		return poolUsageAmount;
	}
	

	public BigDecimal getFirstNum() {
		return firstNum;
	}

	
	@Transient
	public String getCodeName() {
		return this.codeName = this.getCode() +"_"+ this.fees.getName();
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
	@ExcelField(title="序",  align=2, sort=1)
	public String getDeviceIdStr() {
		deviceIdStr = this.id;
		return deviceIdStr;
	}
	

//	@ExcelField(title="类别",  align=2, sort=2, dictType="pms_device_type")
	public String getType() {
		return type;
	}



//	@ExcelField(title="收费编号", align=0, sort=3)
	public String getCode() {
		return code;
	}


	
	@ManyToOne
	@JoinColumn(name="fees_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	@ExcelField(title="收费项目", align=2, sort=2,value="Fees.name")
	public Fees getFees() {
		return fees;
	}

	@ManyToOne
	@JoinColumn(name="house_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@ExcelField(title="房屋", align=1, sort=3,value="House.fullName")
	public House getHouse() {
		return house;
	}
	

	@Transient
	@ExcelField(title="业主编号", align=2, sort=4,value="House.owner.loginName")
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
	@ExcelField(title="付款期限", align=2,  sort=9)
	public Date getCurPaymentDateStr() {
		return curPaymentDateStr;
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
	
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	@NotFound(action = NotFoundAction.IGNORE)
//	@ExcelField(title="本次读表日", align=2,  sort=13)
	public Date getCurDateStr() {
//		curDateStr =  DateUtils.parseDate(DateUtils.getDate()+" 00:00:00");
		return curDateStr;
	}
	

	

	
	public void setDeviceIdStr(String deviceIdStr) {
		this.deviceIdStr = deviceIdStr;
		this.id = deviceIdStr;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public void setCode(String code) {
		this.code = code;
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
	
	public void setHouse(House house) {
		this.house = house;
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
	public void setFees(Fees fees) {
		this.fees = fees;
	}
	
//	@Override
//	public String toString() {
//		return "Device [type=" + type + ", parentIds=" + parentIds + ", code="
//				+ code + ", name=" + name + ", codeName=" + codeName
//				+ ", firstNum=" + firstNum + ", lastNum=" + lastNum
//				+ ", firstDate=" + firstDate + ", lastDate=" + lastDate
//				+ ", paymentDate=" + paymentDate + ", usageAmount="
//				+ usageAmount + ", arrears=" + arrears
//				+ ", apportionHouseCount=" + apportionHouseCount
//				+ ", apportionCompanyCount=" + apportionCompanyCount
//				+ ", enable=" + enable + ", payMoney=" + payMoney + ", sort="
//				+ sort + ", childList=" + childList + ", houseIds=" + houseIds
//				+ "]";
//	}
	
	
//	@Transient
//	public String getHouseIds() {
//		List<String> nameIdList = Lists.newArrayList();
//		for (Device device : childList) { 
//			nameIdList.add(device.getHouse().getId());
//		}
//		return StringUtils.join(nameIdList, ",");
//	}
//	
//	@Transient
//	public void setHouseIds(String menuIds) {
//		childList = Lists.newArrayList();
//		if (menuIds != null){
//			String[] ids = StringUtils.split(menuIds, ",");
//			for (String id : ids) {
//				Device device = new Device();
//				House house = new House();
//				house.setId(id);
//				device.setHouse(house);
//				childList.add(device);
//			}
//		}
//	}

}
//公表编码	费用名称	公表读数	分摊方法	状态