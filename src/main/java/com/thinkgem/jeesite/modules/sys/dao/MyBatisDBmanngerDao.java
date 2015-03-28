/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.dao;


import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;


/**
 * MyBatis字典DAO接口
 * @author ThinkGem
 * @version 2013-8-23
 */
@MyBatisDao
public interface MyBatisDBmanngerDao {
    void emptyPmsData_payment_pre();
    void emptyPmsData_payment_befor();
    void emptyPmsData_payment_after();
    void emptyPmsData_device_detail_payment();
    void emptyPmsData_device_detail_income();
    void emptyPmsData_sys_sequences_feeCode();
    void emptyPmsData_device_detail();
    void emptyPmsData_pms_device();
    void emptyPmsData_pms_house();
    void emptyPmsData_pms_unit();
    void emptyPmsData_pms_buildings();
    void emptyPmsData_pms_community();
    void emptyPmsData_sys_user_role();
    void emptyPmsData_sys_user();
    void emptyPmsData_sys_office();
    void emptyPmsData_pms_fees();
}
