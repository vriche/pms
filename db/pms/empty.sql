TRUNCATE table  pms_payment_pre;
TRUNCATE table  pms_payment_after;
TRUNCATE table  pms_device_detail_payment;
UPDATE pms_device_detail SET income_money =0;

TRUNCATE table  pms_device_detail;
TRUNCATE table  pms_device;

TRUNCATE table  pms_house;
TRUNCATE table  pms_unit;
TRUNCATE table  pms_buildings;
TRUNCATE table  pms_community;


delete from act_id_membership where USER_ID_ >1;
delete from act_id_user where ID_ >1;


delete FROM sys_user_role where user_id>1;
delete FROM sys_user where id>1;
delete FROM sys_office where id>1;
delete FROM pms_fees  where company_id>1;