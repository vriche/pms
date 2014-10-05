/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.pms.utils;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import com.thinkgem.jeesite.common.utils.Arith;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pms.entity.Buildings;
import com.thinkgem.jeesite.modules.pms.entity.Community;
import com.thinkgem.jeesite.modules.pms.entity.Device;
import com.thinkgem.jeesite.modules.pms.entity.Fees;
import com.thinkgem.jeesite.modules.pms.entity.House;
import com.thinkgem.jeesite.modules.pms.entity.Unit;
import com.thinkgem.jeesite.modules.pms.service.BuildingsService;
import com.thinkgem.jeesite.modules.pms.service.CommunityService;
import com.thinkgem.jeesite.modules.pms.service.DeviceService;
import com.thinkgem.jeesite.modules.pms.service.FeesService;
import com.thinkgem.jeesite.modules.pms.service.HouseService;
import com.thinkgem.jeesite.modules.pms.service.UnitService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 字典工具类
 * @author ThinkGem
 * @version 2013-5-29
 */
public class DeviceUtils {
	
	private static CommunityService communityService = SpringContextHolder.getBean(CommunityService.class);
	private static FeesService feesService = SpringContextHolder.getBean(FeesService.class);
	private static BuildingsService buildingsService = SpringContextHolder.getBean(BuildingsService.class);
	private static UnitService unitService = SpringContextHolder.getBean(UnitService.class);
	private static HouseService houseService = SpringContextHolder.getBean(HouseService.class);
	private static DeviceService deviceService = SpringContextHolder.getBean(DeviceService.class);
	
	
	
	
	

	 //feesMode 1 按住户   2 按房屋面积    3 按加建面积  4 按使用量    5 按实际应收金额    6 自定义
	public static void resetList(List<Device> ls){
	
		 for(Device d:ls){
	        	Fees f = d.getFees();
	        	House h = d.getHouse();
		        double unitPrice = f.getUnitPrice().doubleValue();
		        		
	        	int feesMode = Integer.valueOf(f.getFeesMode()).intValue();
	        	String feesParams = f.getRemarks();
	        	String deviceType = d.getType(); //1公摊 2单位 3个人
	        	
//	        	 1 按住户
	        	if(feesMode == 1 || feesMode == 5|| feesMode == 6){
//	        		d.setUsageAmount(d.getUsageAmount());
	        		double payMoney = Arith.roundEVEN(f.getUnitPrice().doubleValue()*d.getUsageAmount().doubleValue(), 2);
	        		String payMoneyStr = String.valueOf(payMoney);
	        		d.setPayMoney(new BigDecimal(payMoneyStr));
			    }
//	       	 2 按房屋面积
			    if(feesMode == 2){
			        double buildArea = h.getBuildArea().doubleValue();
//			        double payMoney = unitPrice*buildArea;
			        double payMoney = Arith.roundEVEN(unitPrice*buildArea, 2);
			        String payMoneyStr = String.valueOf(payMoney);
			        d.setUsageAmount(new BigDecimal(buildArea));
			        d.setPayMoney(new BigDecimal(payMoneyStr));
			    }
//	       	 3  按加建面积
			    if(feesMode == 3){
			    	double buildArea = h.getBuildArea().doubleValue();
//			    	double payMoney = unitPrice*buildArea;
			        double payMoney = Arith.roundEVEN(unitPrice*buildArea, 2);
			        String payMoneyStr = String.valueOf(payMoney);
			    	d.setUsageAmount(new BigDecimal(buildArea));
			    	d.setPayMoney(new BigDecimal(payMoneyStr));
			    }	    
//	       	 4  按使用量
			    if(feesMode == 4){
			    	double lastNum = StringUtils.toDouble(d.getLastNum()).doubleValue();
			    	double firstNum = StringUtils.toDouble(d.getFirstNum()).doubleValue();
//			    	double num = lastNum*1 - firstNum*1;
			    	double num = StringUtils.toDouble(d.getUsageAmount()).doubleValue();
//			    	double payMoney =num*unitPrice;
			    	double payMoney = Arith.roundEVEN(num*unitPrice, 2);
			    	String payMoneyStr = String.valueOf(payMoney);
			    	d.setUsageAmount(new BigDecimal(num));
			    	d.setPayMoney(new BigDecimal(payMoneyStr));
				}	
			    
//		       	 6 按房屋面积的90%
				    if(feesMode == 6){
				        double buildArea = h.getBuildArea().doubleValue();
				        double rate = StringUtils.toDouble(feesParams);
				        double useArea =  Arith.roundEVEN(buildArea*rate, 2);
				        System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> useArea>>>>>>>>>>>>>>>>>>>>>>>>"+ useArea);
//				        double payMoney = unitPrice*buildArea;
				        double payMoney = Arith.roundEVEN(unitPrice*useArea, 2);
				        String payMoneyStr = String.valueOf(payMoney);
				        
				        String useAreaStr = String.valueOf(useArea);
				        d.setUsageAmount(new BigDecimal(useAreaStr));
				        d.setPayMoney(new BigDecimal(payMoneyStr));
				    }		    
			    
			    
			    

//			    d.setSumUsageAmount(d.getUsageAmount());
//				d.setSumPayMoney(d.getPayMoney());		
			    if("2".equals(deviceType)){
					double unit_price = StringUtils.toDouble(f.getUnitPrice()).doubleValue();
					double poolUsageAmount =StringUtils.toDouble(d.getPoolUsageAmount()).doubleValue();
					double poolPayMoney =  Arith.roundEVEN(poolUsageAmount*unit_price, 2) ;
					
//					String poolUsageAmountStr = String.valueOf(poolUsageAmount);
			        String poolPayMoneyStr = String.valueOf(poolPayMoney);
	    		 
//	    		    d.setPoolUsageAmount(new BigDecimal(poolUsageAmountStr));
	    		    d.setPoolPayMoney(new BigDecimal(poolPayMoneyStr));
					
				    d.setSumUsageAmount(new BigDecimal(Arith.add(d.getUsageAmount(), d.getPoolUsageAmount())));
				    d.setSumPayMoney(new BigDecimal(Arith.add(d.getPayMoney(), d.getPoolPayMoney())));				    	
			    }else{
				    d.setSumUsageAmount(d.getUsageAmount());
				    double payMoney = Arith.roundEVEN(d.getPayMoney().doubleValue(), 2);
				    String payMoneyStr = String.valueOf(payMoney);
				    d.setPayMoney(new BigDecimal(payMoneyStr));
				    d.setSumPayMoney(new BigDecimal(payMoneyStr));
			    }

			  

	        }
	
	}
	
	//feesMode 1 按住户   2 按房屋面积    3 按加建面积  4 按使用量    5 按实际应收金额    6 自定义
		public static void resetPoolList(String deviceParentId ,List<Device> ls){
			 Device pd = deviceService.get(deviceParentId);
			 Fees pf =pd.getFees();
			 
//			 System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> (pd.getFeesMode() >>>>>>>>>>>>>>>>>>>>>>>>"+ pd.getFeesMode());
			 
			int feesMode = pd.getFeesMode() == null?0: Integer.valueOf(pd.getFeesMode()).intValue();
				
			double p_lastNum = StringUtils.toDouble(pd.getLastNum()).doubleValue();
		    double p_firstNum = StringUtils.toDouble(pd.getFirstNum()).doubleValue();
		    double p_sum_num = p_lastNum*1 - p_firstNum*1;
		    double p_one_num = 0;
			double p_unit_price = StringUtils.toDouble(pf.getUnitPrice()).doubleValue();
			int count = ls.size();
			int p_feesMode = Integer.valueOf(pd.getFeesMode()).intValue();
			long p_fid = Long.valueOf(pf.getId());
			
			double totalArea = 0;
			double totalPay = 0;
			double temp_totalNum = 0;
			double temp_totalPay = 0;
			
			
			 List<Device> lsChild = pd.getChildList();
			 count = lsChild.size();
			if(p_feesMode == 1 || p_feesMode == 6){
				if(count>0){
					p_one_num = p_sum_num /count;
				}
				
		    }
			if(p_feesMode == 2){
				
//				 System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> p_feesMode >>>>>>>>>>>>>>>>>>>>>>>>"+ feesMode );

				
				 
				 
				 for(Device d:lsChild){
					 
					 
					 
//					 int fmode =  d.getFees() == null?0: Integer.valueOf(d.getFees().getFeesMode()).intValue();
					 
					 long fid = Long.valueOf(d.getFees().getId());
					 boolean isPoolDevice = "1".equals(d.getPool());
					 
//					 if(d.getParent() != null){
//						 String parentId = d.getParent().getId();
//					
////						 System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> fmode >>>>>>>>>>>>>>>>>>>>>>>>"+ fmode );
//						 if(isPoolDevice && fid == p_fid && deviceParentId.equals(parentId)){
							 double buildArea = d.getHouse().getBuildArea() == null?0:d.getHouse().getBuildArea().doubleValue();
							 totalArea += buildArea;
//						 } 
//					 } 

				 }
				 totalPay = p_sum_num*p_unit_price;
				 
//				 System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> totalPay >>>>>>>>>>>>>>>>>>>>>>>>"+ totalPay);
			        
			}
			
			
//			System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> p_sum_num >>>>>>>>>>>>>>>>>>>>>>>>"+ p_sum_num);
//			System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> totalArea >>>>>>>>>>>>>>>>>>>>>>>>"+ totalArea);
//			System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> totalPay >>>>>>>>>>>>>>>>>>>>>>>>"+ totalPay);
			 
			int temp_count = 0;
			
			 for(Device d:ls){
				 
				   String deviceType = d.getType(); //1公摊 2单位 3个人
				   
			 if(!"2".equals(deviceType)){
					   
				
				 
		        	Fees f = d.getFees();
		        	House h = d.getHouse();
		        	
		        	long fid = Long.valueOf(f.getId());
		        	
		        	boolean isPoolDevice = "1".equals(d.getPool());
		        	temp_count++;
//		        	int feesMode = Integer.valueOf(f.getFeesMode()).intValue();
		        	
//		        	System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> feesMode >>>>>>>>>>>>>>>>>>>>>>>>"+ feesModes);
		        	 String parentId = "";
		        	 if(d.getParent() != null){
		        		  parentId = d.getParent().getId();
		        	 }
		        	
		        	
		        	
		        	if(isPoolDevice && fid == p_fid && deviceParentId.equals(parentId)){
		        		
		        		
				        double unitPrice =f.getUnitPrice()==null?0: f.getUnitPrice().doubleValue();
		        		
			        
//			        	 1 按住户
			        	if(feesMode == 1 || feesMode == 5 || feesMode == 6){
//			        		d.setUsageAmount(new BigDecimal(1));
//			        		d.setPayMoney(new BigDecimal(1 * unitPrice));
			        		double poolUsageAmount = Arith.roundEVEN(p_one_num, 2);
			        		 temp_totalNum += Double.valueOf(poolUsageAmount).doubleValue();
			        		 if(count == temp_count){
			        			 poolUsageAmount +=p_sum_num-temp_totalNum;
						      }
			        		
			        		 String poolUsageAmountStr = String.valueOf(Arith.roundEVEN(poolUsageAmount, 2));
			        		 
			        		 
			        			double poolPayMoney =  Arith.roundEVEN(poolUsageAmount*p_unit_price, 2) ;
						        String poolPayMoneyStr = String.valueOf(poolPayMoney);
			        		 
			        		d.setPoolUsageAmount(new BigDecimal(poolUsageAmountStr));
			        		d.setPoolPayMoney(new BigDecimal(poolPayMoneyStr));
			        		
//			        		Double sumUsageAmount =  Arith.roundEVEN(Arith.add(d.getUsageAmount(), d.getPoolPayMoney()),2);
//			        		String sumUsageAmountStr = String.valueOf(sumUsageAmount);
//			        		
//			        		Double sumPayMoney =  Arith.roundEVEN(Arith.add(d.getPayMoney(), d.getPoolPayMoney()),2);
//			        		String sumPayMoneyStr = String.valueOf(sumPayMoney);
//			        		
//			        		d.setSumUsageAmount(new BigDecimal(sumUsageAmountStr));
//							d.setSumPayMoney(new BigDecimal(sumPayMoneyStr));	
							
//						    d.setSumUsageAmount(new BigDecimal(1));
//							d.setSumPayMoney(new BigDecimal(Arith.add(d.getPayMoney(), d.getPoolPayMoney())));	
					    }
//			       	 2 按房屋面积
					    if(feesMode == 2){
					        double buildArea = h.getBuildArea()==null?0:h.getBuildArea().doubleValue();
					        System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> buildArea >>>>>>>>>>>>>>>>>>>>>>>>"+ buildArea);
					        System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> totalArea >>>>>>>>>>>>>>>>>>>>>>>>"+ totalArea);
					        double rate = buildArea/totalArea;
					        
					        System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> rate >>>>>>>>>>>>>>>>>>>>>>>>"+ rate);
					        
//					        double total_payMoney = p_unit_price*p_sum_num;
//					        unitPrice = total_payMoney/count;
					        double usageAmount = Arith.roundEVEN(p_sum_num*rate, 2) ;
					        temp_totalNum += Double.valueOf(usageAmount).doubleValue();
					        
					        if(count == temp_count){
					        	usageAmount +=p_sum_num-temp_totalNum;
					        }
					        
					       
					        String usageAmountStr = String.valueOf(Arith.roundEVEN(usageAmount, 2));
					        
//					        System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> usageAmountStr >>>>>>>>>>>>>>>>>>>>>>>>"+ usageAmountStr);
					        
//					        System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> usageAmount >>>>>>>>>>>>>>>>>>>>>>>>"+ usageAmount);
					        
//					        f.setUnitPrice(new BigDecimal(unitPrice));
					        double payMoney =  Arith.roundEVEN(usageAmount*p_unit_price, 2) ;
					        String payMoneyStr = String.valueOf(payMoney);

//					        System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> temp_totalNum >>>>>>>>>>>>>>>>>>>>>>>>"+ temp_totalNum);

					        d.setPoolUsageAmount(new BigDecimal(usageAmountStr));
					        d.setPoolPayMoney(new BigDecimal(payMoneyStr));
					        
					        
//				    		Double sumUsageAmount =  Arith.roundEVEN(Arith.add(d.getUsageAmount(), d.getPoolPayMoney()),2);
//			        		String sumUsageAmountStr = String.valueOf(sumUsageAmount);
//			        		
//			        		Double sumPayMoney =  Arith.roundEVEN(Arith.add(d.getPayMoney(), d.getPoolPayMoney()),2);
//			        		String sumPayMoneyStr = String.valueOf(sumPayMoney);
//			        		
//					        
//						    d.setSumUsageAmount(new BigDecimal(sumUsageAmountStr));
//							d.setSumPayMoney(new BigDecimal(sumPayMoneyStr));	
					    }
//			       	 3  按加建面积
					    if(feesMode == 3){
					        double buildArea = h.getBuildArea().doubleValue();
					        double rate = buildArea/totalArea;
					        double total_payMoney = p_unit_price*totalArea;
					        double payMoney = total_payMoney*rate;
					        
					       
//					        d.setPoolUsageAmount(new BigDecimal(totalArea*rate));
					        d.setPoolUsageAmount(new BigDecimal(0));
					        d.setPoolPayMoney(new BigDecimal(payMoney));
					        
					        
//				    		Double sumUsageAmount =  Arith.roundEVEN(Arith.add(d.getUsageAmount(), d.getPoolPayMoney()),2);
//			        		String sumUsageAmountStr = String.valueOf(sumUsageAmount);
//			        		
//			        		Double sumPayMoney =  Arith.roundEVEN(Arith.add(d.getPayMoney(), d.getPoolPayMoney()),2);
//			        		String sumPayMoneyStr = String.valueOf(sumPayMoney);
//					        
//			        		d.setSumUsageAmount(new BigDecimal(sumUsageAmountStr));
//							d.setSumPayMoney(new BigDecimal(sumPayMoneyStr));	
					    }	    
//			       	 4  按使用量
					    if(feesMode == 4){
					    	double total_payMoney = p_unit_price*p_sum_num;
					    	double lastNum = d.getLastNum().doubleValue();
					    	double firstNum = d.getFirstNum().doubleValue();
					    	double num = lastNum*1 - firstNum*1;
					    	
//					    	 System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> num num>>>>>>>>>>>>>>>>>>>>>>>>"+ num);
//					    	 System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> num p_sum_num>>>>>>>>>>>>>>>>>>>>>>>>"+ p_sum_num);
//					    	 System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> num 2222>>>>>>>>>>>>>>>>>>>>>>>>"+ total_payMoney);
					    	double rate = num>0?num/p_sum_num:0;
					    	
					    	double usageAmount = Arith.roundEVEN(p_sum_num*rate, 2) ;
					    	String usageAmountStr = String.valueOf(usageAmount);
					        temp_totalNum += Double.valueOf(usageAmount).doubleValue();
					        if(count == temp_count){
					        	usageAmount +=p_sum_num-temp_totalNum;
					        }
					        
					        double payMoney =  Arith.roundEVEN(usageAmount*p_unit_price, 2) ;
					        String payMoneyStr = String.valueOf(payMoney);
					        
					    	
//					   	 System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> num rate>>>>>>>>>>>>>>>>>>>>>>>>"+rate);
//					    	double payMoney = total_payMoney>0?total_payMoney*rate:0;
//					    	d.setPayMoney(new BigDecimal(payMoney));
					    	
//					    	 System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> num 3333>>>>>>>>>>>>>>>>>>>>>>>>"+ payMoney);

//					        d.setPoolUsageAmount(new BigDecimal(p_sum_num*rate));
					        d.setPoolUsageAmount(new BigDecimal(usageAmountStr));
					        d.setPoolPayMoney(new BigDecimal(payMoneyStr));
					        
//							Double sumUsageAmount =  Arith.roundEVEN(Arith.add(d.getUsageAmount(), d.getPoolPayMoney()),2);
//			        		String sumUsageAmountStr = String.valueOf(sumUsageAmount);
//			        		
//			        		Double sumPayMoney =  Arith.roundEVEN(Arith.add(d.getPayMoney(), d.getPoolPayMoney()),2);
//			        		String sumPayMoneyStr = String.valueOf(sumPayMoney);
//					        
//			        		d.setSumUsageAmount(new BigDecimal(sumUsageAmountStr));
//							d.setSumPayMoney(new BigDecimal(sumPayMoneyStr));	
					        
						}	
		        	}else{
		        		  d.setPoolUsageAmount(new BigDecimal(0));
					      d.setPoolPayMoney(new BigDecimal(0));
					      
//							Double sumUsageAmount =  Arith.roundEVEN(Arith.add(d.getUsageAmount(), d.getPoolPayMoney()),2);
//			        		String sumUsageAmountStr = String.valueOf(sumUsageAmount);
//			        		
//			        		Double sumPayMoney =  Arith.roundEVEN(Arith.add(d.getPayMoney(), d.getPoolPayMoney()),2);
//			        		String sumPayMoneyStr = String.valueOf(sumPayMoney);
//					        
//			        		d.setSumUsageAmount(new BigDecimal(sumUsageAmountStr));
//							d.setSumPayMoney(new BigDecimal(sumPayMoneyStr));	
		        	}
		        	
	        		Double sumUsageAmount =  Arith.roundEVEN(Arith.add(d.getUsageAmount(), d.getPoolUsageAmount()),2);
	        		String sumUsageAmountStr = String.valueOf(sumUsageAmount);
	        		
	        		Double sumPayMoney =  Arith.roundEVEN(Arith.add(d.getPayMoney(), d.getPoolPayMoney()),2);
	        		String sumPayMoneyStr = String.valueOf(sumPayMoney);
	        		
	        		d.setSumUsageAmount(new BigDecimal(sumUsageAmountStr));
					d.setSumPayMoney(new BigDecimal(sumPayMoneyStr));	
			   }

		     }
			 
			 System.out.println(">>>>>>>>>>sssss          ssssssssssssss       >>>>>>>>>> temp_totalNum >>>>>>>>>>>>>>>>>>>>>>>>"+ temp_totalNum);
		
		}
		
		public static String getCode(Device device){
			
			String type = device.getType();
			String code = device.getCode();
			House house = device.getHouse();
			
//			System.out.println(">>>>>>>>>>>>>>>>>>>>  house >>>>>>>>666666666666666666>>>>>>>>>>>>>>>>"+ house);
			
			User owner = house.getOwner();
			String houseId = house.getId();
			Fees fees = device.getFees();
			String feesModel = fees.getFeesMode();
			
			if(!"1".equals(type)){
				
				if(StringUtils.isEmpty(code)){
                     
					 //电话费  把电话号码作为设备号
					 if("5".equals(feesModel)){
						    if(owner != null){
								String phone = owner.getPhone();
								if (StringUtils.isNotEmpty(phone)){
									code = phone;
								}
						    }
					   }
					
						if (StringUtils.isEmpty(code)){
							 code = StringUtils.lpad(6,Integer.valueOf(houseId));
							 code +="_"+fees.getCode();	
						}		
				}
			}else{
				if(StringUtils.isEmpty(code)){
					code = "P_"+fees.getCode();	
				}
			}
			return code;
		}
		
		
	public static void setObjFromReq(Device device, HttpServletRequest request, HttpServletResponse response, Model model,int from){
		@SuppressWarnings("unchecked")
        Unit unit = new Unit();
	    Buildings buildings = new Buildings();
	    Community community = new Community();
	    Office proCompany = new Office();	
	    House house = new House();
	    Fees fees = new Fees();
	    User owner = new User();
	    Office company = new Office();
	 
		StringBuffer houseIds = new StringBuffer();
		
		String proCompanyId = "";
		String companyId = "";
		String feesId = "";
		String communityId = "";
		String buildingsId = "";
		String unitId ="";
		String houseId = "";
		
		if(from == 1){
			 proCompanyId = request.getParameter("fees.company.id");
			 companyId = request.getParameter("device.house.owner.company.id");
			 feesId = request.getParameter("fees.id");
			 communityId = request.getParameter("house.unit.buildings.community.id");
			 buildingsId = request.getParameter("house.unit.buildings.id");
			 unitId = request.getParameter("house.unit.id");
			 houseId = request.getParameter("house.id");
		}
		

		
		if(from == 2){
			 proCompanyId = request.getParameter("device.fees.company.id");
			 companyId = request.getParameter("device.house.owner.company.id");
			 feesId = request.getParameter("device.fees.id");
			 communityId = request.getParameter("device.house.unit.buildings.community.id");
			 buildingsId = request.getParameter("device.house.unit.buildings.id");
			 unitId = request.getParameter("device.house.unit.id");
			 houseId = request.getParameter("device.house.id");
		}
		
		
		String feesMode = FeesUtils.getFeesMode(feesId);
		
		
		List<Office> proCompanyList = UserUtils.findProCompanyList();
		
		
		
		
		if(device == null) device = new Device();
		if(device.getFees() == null) device.setFees(fees);
		if(device.getHouse() == null) device.setHouse(house);
		if(device.getFees().getCompany() == null) device.getFees().setCompany(proCompany);
		if(device.getHouse().getOwner() == null) device.getHouse().setOwner(owner);
		if(device.getHouse().getOwner().getCompany() == null) device.getHouse().getOwner().setCompany(company);
		
		
		if(device.getFees() == null) device.setFees(fees);
		if(device.getHouse().getUnit() == null) device.getHouse().setUnit(unit);
		if(device.getHouse().getUnit().getBuildings() == null) device.getHouse().getUnit().setBuildings(buildings);
		if(device.getHouse().getUnit().getBuildings().getCommunity() == null) device.getHouse().getUnit().getBuildings().setCommunity(community);
		if(device.getHouse().getUnit().getBuildings().getCommunity().getProCompany() == null) device.getHouse().getUnit().getBuildings().getCommunity().setProCompany(proCompany);
			
		
		House hhh = device.getHouse();
		
		if (StringUtils.isNotBlank(proCompanyId)){
			hhh.getUnit().getBuildings().getCommunity().getProCompany().setId(proCompanyId);
			device.getFees().getCompany().setId(proCompanyId);
			if(model!= null) model.addAttribute("communityList", communityService.findAllCommunity(hhh.getUnit().getBuildings().getCommunity()));
			houseIds.append("procompany"+proCompanyId+",");
		}else{
			if(proCompanyList.size() > 0){
				proCompany = proCompanyList.get(0);
				proCompanyId = proCompany.getId();
				hhh.getUnit().getBuildings().getCommunity().setProCompany(proCompanyList.get(0));
				device.getFees().setCompany(proCompanyList.get(0));
				if(model!= null) model.addAttribute("communityList", communityService.findAllCommunity(hhh.getUnit().getBuildings().getCommunity()));
			}
			
		}
		
		if (StringUtils.isNotBlank(companyId)){
			 hhh.getOwner().setCompany((new Office(companyId)));
//			 model.addAttribute("communityList", communityService.findAllCommunity(hhh.getUnit().getBuildings().getCommunity()));
		}
	
		List<Fees> feesList = FeesUtils.getALLFees(proCompanyId);
		if(model!= null) model.addAttribute("feesList", feesList);
//		if (StringUtils.isNotBlank(proCompanyId)){
////			device.getFees().setId(feesId);
////			device.getFees().setCompany(new Office(proCompanyId));
//			if(model!= null) model.addAttribute("feesList", feesList);
//		}else{
////			if(proCompanyList.size() > 0){
////				fees.setCompany(proCompanyList.get(0));
////			}
//			if(model!= null) model.addAttribute("feesList", feesList);
////			device.getFees().setCompany(new Office(proCompanyId));
//		}

	
		if (StringUtils.isNotBlank(communityId)){
			hhh.getUnit().getBuildings().getCommunity().setId(communityId);
			if(model!= null) model.addAttribute("buildingsList", buildingsService.findAllBuildings(hhh.getUnit().getBuildings()));
			houseIds.append("community"+communityId+",");
		}
		
		if (StringUtils.isNotBlank(buildingsId)){
			hhh.getUnit().getBuildings().setId(buildingsId);
			if(model!= null) model.addAttribute("unitList", unitService.findAllUnit(hhh.getUnit()));
			houseIds.append("buildings"+buildingsId+",");
		}		
		
		if (StringUtils.isNotBlank(unitId)){
			hhh.getUnit().setId(unitId);
			
			if(model!= null){
				List<House> houseList = houseService.findAllHouse(hhh);
				for(House h:houseList){
					h.setName(h.getCode()+"("+h.getOwner().getName()+")");
				}
				
				model.addAttribute("houseList", houseList);			
			}
			houseIds.append("unit"+unitId+",");
		}	
		
		if (StringUtils.isNotBlank(houseId)){
			hhh.setId(houseId);
			houseIds.append("house"+houseId);
		}		
		
		if (StringUtils.isNotBlank(feesId)){
			device.getFees().setId(feesId);
		}	

		device.setHouseIds(houseIds.toString());
		
		
		if(model!= null){
			List<Office> companyList =UserUtils.findCompanyListByProCompany(proCompanyId);
			Device dev = new Device();
			dev.setType("1");
			List<Device> parentDeviceList = deviceService.find(dev);
	        model.addAttribute("proCompanyList", proCompanyList);
	        model.addAttribute("companyList", companyList);
	        model.addAttribute("feesMode", feesMode);
			model.addAttribute("parentList", parentDeviceList);		
		}

        
//        model.addAttribute("fees.id", feesMode);

        
	}
	
	
}
