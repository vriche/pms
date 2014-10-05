SET SESSION FOREIGN_KEY_CHECKS=0;




/* Drop Tables */


DROP TABLE IF EXISTS pms_fees;

/* Create Tables */

CREATE TABLE pms_fees
(
	id varchar(64) NOT NULL COMMENT '编号',
	company_id varchar(64) NOT NULL COMMENT '归属公司',
	parent_id varchar(64)  COMMENT '关联费项',
	name varchar(100) NOT NULL COMMENT '费项名称',
	code varchar(100) COMMENT '费项编号',
	fees_type char(1) COMMENT '收费方式',
	fees_mode char(1) COMMENT '收费方式',
	spe_month int  COMMENT '收费间隔月份',
	unit_price decimal(10,2)   NOT NULL COMMENT '单位价格',
    is_enable_mod_price char(1) DEFAULT '0' COMMENT '允许【生成物业费】时直接修改此费项单价',
	remind_type char(1) COMMENT '提醒业主方式 短信、电话、邮件',
	remind_days int  COMMENT '从费用到期前 天开始提醒',
	is_ladder char(1) DEFAULT '0' COMMENT '按单价收费  按阶梯收费',
	ladder_rules varchar(64) COMMENT '阶梯规则编号',
	sort int NOT NULL COMMENT '排序（升序）',
	
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '收费项目表';


CREATE INDEX pms_fees_company_id ON pms_fees (company_id ASC);
CREATE INDEX pms_fees_parent_id ON pms_fees (parent_id ASC);
CREATE INDEX pms_fees_del_flag ON pms_fees (del_flag ASC);



	
/* Create Tables */
DROP TABLE IF EXISTS pms_community;
CREATE TABLE pms_community
(
	id varchar(64) NOT NULL COMMENT '编号',
	pro_company_id varchar(64) NOT NULL COMMENT '物业公司',
	dep_company_id varchar(64)  COMMENT '开发商信息',
	name varchar(100) NOT NULL COMMENT '名称',
	code varchar(100) COMMENT '编号',
	
	total_area  decimal(10,2)   DEFAULT '0' COMMENT '占地面积',
	build_area  decimal(10,2)   DEFAULT '0' COMMENT '建筑面积',
	green_area  decimal(10,2)   DEFAULT '0' COMMENT '绿地面积',
	road_area  decimal(10,2)    DEFAULT '0' COMMENT '道路面积',
	build_count int  DEFAULT '0' NOT NULL COMMENT '楼宇数量',
	charge_person varchar(100)  COMMENT '负责人',
	address varchar(200)   COMMENT '住宅地址',
	
	sort int NOT NULL COMMENT '排序（升序）',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0'  COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '小区信息表';

CREATE INDEX pms_community_pro_ompany_id ON pms_community (pro_company_id ASC);
CREATE INDEX pms_community_del_flag ON pms_community (del_flag ASC);


	
/* Create Tables */
DROP TABLE IF EXISTS pms_buildings;
CREATE TABLE pms_buildings
(
	id varchar(64) NOT NULL COMMENT '编号',
	community_id varchar(64) NOT NULL COMMENT '所属小区',
	name varchar(100) NOT NULL COMMENT '名称',
	code varchar(100) COMMENT '编号',

	
	build_area decimal(10,2)   NOT NULL COMMENT '建筑面积',
	use_area decimal(10,2)   NOT NULL COMMENT '使用面积',
	floor_count int  DEFAULT '0' NOT NULL COMMENT '楼层数',
	cap_date datetime COMMENT '封顶日期',
	completion_date datetime COMMENT '竣工日期 ',
	presale_permit varchar(100) COMMENT '预售许可证',
	build_permit varchar(100) COMMENT '建筑许可证',
	
	sort int NOT NULL COMMENT '排序（升序）',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '楼宇信息表';

CREATE INDEX pms_buildings_community_id ON pms_buildings (community_id ASC);
CREATE INDEX pms_buildings_del_flag ON pms_buildings (del_flag ASC);



	

/* Create Tables */
DROP TABLE IF EXISTS pms_unit;
CREATE TABLE pms_unit
(
	id varchar(64) NOT NULL COMMENT '编号',
	buildings_id varchar(64) NOT NULL COMMENT '所属楼宇',
	name varchar(100) NOT NULL COMMENT '名称',
	code varchar(100) COMMENT '编号',

	start_floor int  NOT NULL COMMENT '开始楼层',
	end_floor int  NOT NULL COMMENT '结束楼层',
	start_room int  NOT NULL COMMENT '开始房号',
	end_room int  NOT NULL COMMENT '结束房号',


	sort int NOT NULL COMMENT '排序（升序）',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '单元信息表';

CREATE INDEX pms_unit_buildings_id ON pms_unit (buildings_id ASC);
CREATE INDEX pms_unit_del_flag ON pms_unit (del_flag ASC);



	



/* Create Tables */
DROP TABLE IF EXISTS pms_house;
CREATE TABLE pms_house
(
	id varchar(64) NOT NULL COMMENT '编号',
	unit_id varchar(64) NOT NULL COMMENT '所属单元',
	name varchar(100) NOT NULL COMMENT '名称',
	code varchar(100) COMMENT '编号',
	num_floor char(1) COMMENT '第几层',
    build_area decimal(10,2)  NOT NULL COMMENT '建筑面积',
	use_area decimal(10,2)  NOT NULL COMMENT '使用面积',

	apartment char(1) COMMENT '户型 ',
	face char(1) COMMENT '朝向 ',
	funct char(1) COMMENT '功能 ',
	is_sell char(1) COMMENT '已售 ',
	is_rent char(1) COMMENT '已租 ',
	user_id varchar(64)  COMMENT '业主',
	sort int NOT NULL COMMENT '排序（升序）',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '房屋信息表';

CREATE INDEX pms_house_unit_id ON pms_house (unit_id ASC);
CREATE INDEX pms_house_del_flag ON pms_house (del_flag ASC);





/* Create Tables */
DROP TABLE IF EXISTS pms_device;
CREATE TABLE pms_device
( 
	id varchar(64) NOT NULL COMMENT '编号',
	name varchar(100) NOT NULL COMMENT '名称',
	parent_id varchar(64) NOT NULL COMMENT '父级编号',
	parent_ids varchar(2000) NOT NULL COMMENT '所有父级编号',	
	code varchar(100) COMMENT '编号  私有表=房号+收费编号  公表自定义、固话号码',
	type char(1) COMMENT '1 是个人  2、公共表 3、话费',
	first_num decimal(10,2)   NOT NULL COMMENT '上次读数',
	last_num decimal(10,2)   NOT NULL COMMENT '本次读数',
	first_date datetime COMMENT '上次日期',
	last_date datetime COMMENT '本次日期',
	payment_date datetime COMMENT '交费限期',
	usage_amount decimal(10,2)   NOT NULL COMMENT '本次读数',
	arrears decimal(10,2)   NOT NULL COMMENT '本次读数',
	apportion_house_count int NOT NULL COMMENT '分摊户数  公表记录',
	apportion_company_count int NOT NULL COMMENT '分摊公司  公表记录',
	fees_id varchar(64) NOT NULL COMMENT '编号',
	
	sort int NOT NULL COMMENT '排序（升序）',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '计费设备表';

CREATE INDEX pms_device_type ON pms_device (type ASC);
CREATE INDEX pms_device_code ON pms_device (code ASC);
CREATE INDEX pms_device_del_flag ON pms_device (del_flag ASC);





DROP TABLE IF EXISTS `pms_house_device`;
CREATE TABLE `pms_house_device` (
  `house_id` varchar(64) NOT NULL COMMENT '房子编号',
  `device_id` varchar(64) NOT NULL COMMENT '设备编号',
  PRIMARY KEY (`house_id`,`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备-费用';



/* Create Tables */
DROP TABLE IF EXISTS pms_payment_detail;
CREATE TABLE pms_payment_detail
(
	id varchar(64) NOT NULL COMMENT '编号',
	payment_before_id varchar(64)  COMMENT '收款凭证',
	payment_after_id varchar(64)  COMMENT '付款凭证',
	device_id varchar(64) NOT NULL COMMENT '设备编号',
	first_num decimal(10,2)    COMMENT '上次读数',
	last_num decimal(10,2)  COMMENT '本次读数',
	first_date datetime COMMENT '上次日期',
	last_date datetime COMMENT '本次日期',
	payment_date datetime COMMENT '交费限期',
	usage_amount decimal(10,2)    COMMENT '本次读数',
	cost_money decimal(10,2)   COMMENT '费用金额',
	income_money decimal(10,2)   COMMENT '收款金额',
	favour_money decimal(10,2)   COMMENT '优惠金额',
	is_pay char(1) COMMENT '是否已付款',
	
	sort int NOT NULL COMMENT '排序（升序）',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '计费设备表';

CREATE INDEX pms_payment_detail_device_id ON pms_payment_detail (device_id ASC);
CREATE INDEX pms_payment_detail_is_pay ON pms_payment_detail (is_pay ASC);
CREATE INDEX pms_payment_detail_del_flag ON pms_payment_detail (del_flag ASC);






/* Create Tables */
DROP TABLE IF EXISTS pms_payment_befor;
CREATE TABLE pms_payment_befor
(
	id varchar(64) NOT NULL COMMENT '编号',
	name varchar(100) NOT NULL COMMENT '名称',
	rece_date datetime COMMENT '生成日期',
    fees_id varchar(64) NOT NULL COMMENT '费用编号',

	sort int NOT NULL COMMENT '排序（升序）',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '应收款表';

CREATE INDEX pms_payment_befor_fees_id ON pms_payment_befor (fees_id ASC);
CREATE INDEX pms_payment_befor_del_flag ON pms_payment_befor (del_flag ASC);


 	
/* Create Tables */
DROP TABLE IF EXISTS pms_payment_after;
CREATE TABLE pms_payment_after
(
	id varchar(64) NOT NULL COMMENT '编号',
	fee_code varchar(100) NOT NULL COMMENT '收款单号',
	cert_code varchar(100) NOT NULL COMMENT '收款单号',
	rece_date datetime COMMENT '收款日期',
	cost_money decimal(10,2)   NOT NULL COMMENT '应付金额',
	rec_money decimal(10,2)   NOT NULL COMMENT '应付金额',
	pay_type char(1) COMMENT ' 收款方式  现金 支票',
    collect_id varchar(64) NOT NULL COMMENT '收款人',

	sort int NOT NULL COMMENT '排序（升序）',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '已收款表';

CREATE INDEX pms_payment_after_rece_date ON pms_payment_after (rece_date ASC);
CREATE INDEX pms_payment_after_del_flag ON pms_payment_after (del_flag ASC);




	

/* Create Tables */
DROP TABLE IF EXISTS pms_payment_pre;
CREATE TABLE pms_payment_pre
(
	id varchar(64) NOT NULL COMMENT '编号',
	tyep char(1) COMMENT ' 1 个人  2、公司 ',
	name varchar(100) NOT NULL COMMENT '名称',
	code varchar(100) NOT NULL COMMENT '凭证号',
	user varchar(20) NOT NULL COMMENT '经手人',
	money decimal(10,2)   NOT NULL COMMENT '金额',
	rece_date datetime COMMENT '收款日期',
	fees_id varchar(64) NOT NULL COMMENT '费用',
	device_id varchar(64) NOT NULL COMMENT '设备',
	company_id varchar(64) NOT NULL COMMENT '公司',
	
	sort int NOT NULL COMMENT '排序（升序）',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '预收款表';

CREATE INDEX pms_payment_pre_device_id ON pms_payment_pre (rece_date ASC);
CREATE INDEX pms_payment_pre_del_flag ON pms_payment_pre (del_flag ASC);




