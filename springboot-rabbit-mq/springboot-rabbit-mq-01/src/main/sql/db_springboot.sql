/*
Navicat MySQL Data Transfer

Source Server         : 本地数据库
Source Server Version : 50640
Source Host           : localhost:3306
Source Database       : db_springboot

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2018-09-01 23:26:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for order_record
-- ----------------------------
DROP TABLE IF EXISTS `order_record`;
CREATE TABLE `order_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) NOT NULL COMMENT '订单编号',
  `order_type` varchar(255) DEFAULT NULL COMMENT '订单类型',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_order_no` (`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='订单记录表-业务级别';

-- ----------------------------
-- Records of order_record
-- ----------------------------
INSERT INTO `order_record` VALUES ('7', '10010', '物流2', '2018-07-31 20:59:18', '2018-07-31 23:35:43');
INSERT INTO `order_record` VALUES ('8', '10011', '供应商3', '2018-07-31 20:59:30', '2018-07-31 23:34:56');
INSERT INTO `order_record` VALUES ('9', '10012', '采购2', '2018-07-22 20:59:36', '2018-07-23 21:06:47');
INSERT INTO `order_record` VALUES ('12', '10013', '测试类型1', '2018-07-22 21:02:38', '2018-07-30 23:34:41');
INSERT INTO `order_record` VALUES ('13', '10014', '测试类型1', '2018-07-23 21:02:50', '2018-07-30 23:34:44');
INSERT INTO `order_record` VALUES ('14', '10015', '测试类型3', '2018-07-23 21:06:30', '2018-07-31 23:34:45');
INSERT INTO `order_record` VALUES ('15', '10016', '测试类型4', '2018-07-30 20:53:39', '2018-07-31 23:34:47');
INSERT INTO `order_record` VALUES ('16', 'orderNo_20180821001', '物流12', '2018-08-22 21:12:46', null);

-- ----------------------------
-- Table structure for order_report
-- ----------------------------
DROP TABLE IF EXISTS `order_report`;
CREATE TABLE `order_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) NOT NULL COMMENT '订单编号',
  `order_type` varchar(255) DEFAULT NULL COMMENT '订单类型',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_order_no` (`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='订单报表表-分析级别';

-- ----------------------------
-- Records of order_report
-- ----------------------------
INSERT INTO `order_report` VALUES ('28', '10010', '物流2', '2018-07-31 20:59:18', '2018-07-31 23:35:43');
INSERT INTO `order_report` VALUES ('29', '10011', '供应商3', '2018-07-31 20:59:30', '2018-07-31 23:34:56');
INSERT INTO `order_report` VALUES ('30', '10015', '测试类型3', '2018-07-23 21:06:30', '2018-07-31 23:34:45');
INSERT INTO `order_report` VALUES ('31', '10016', '测试类型4', '2018-07-30 20:53:39', '2018-07-31 23:34:47');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_no` varchar(255) DEFAULT NULL COMMENT '商品编号',
  `total` int(255) DEFAULT NULL COMMENT '库存量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='商品信息表';

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES ('1', 'product_10010', '1000', '2018-08-24 21:16:20', '2018-08-25 17:59:57');

-- ----------------------------
-- Table structure for product_bak
-- ----------------------------
DROP TABLE IF EXISTS `product_bak`;
CREATE TABLE `product_bak` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `stock` int(11) DEFAULT NULL COMMENT '库存量',
  `purchase_date` date DEFAULT NULL COMMENT '采购日期',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_active` int(11) DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COMMENT='产品信息表';

-- ----------------------------
-- Records of product_bak
-- ----------------------------
INSERT INTO `product_bak` VALUES ('1', '戴尔笔记本', '100', '2018-06-01', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('2', '华硕笔记本', '200', '2018-07-10', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('3', '联想小新I', '15', '2018-05-10', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('4', '暗影精灵', '35', '2018-07-19', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('5', '外星人I', '1000', '2018-07-11', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('6', '戴尔XPS超极本', '200', '2018-02-07', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('7', '联想台式机', '123', '2018-07-12', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('8', '戴尔笔记本-二代', '100', '2018-06-01', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('9', '华硕笔记本', '200', '2018-07-10', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('10', '联想小新I', '15', '2018-05-10', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('11', '暗影精灵II', '35', '2018-06-12', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('12', '外星人II', '1000', '2018-07-11', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('13', '惠普战系列笔记本', '200', '2018-02-07', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('14', '海信笔记本', '123', '2018-06-19', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('15', '组装机', '100', '2018-06-01', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('16', '宏碁台式机', '200', '2018-07-10', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('17', '东芝笔记本', '15', '2018-05-10', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('18', '神州战船', '35', '2018-07-19', '2018-07-21 11:28:50', '2018-07-21 11:28:50', '1');
INSERT INTO `product_bak` VALUES ('19', 'Mac笔记本', '150', null, '2018-07-21 21:43:30', '2018-07-21 21:43:30', '1');
INSERT INTO `product_bak` VALUES ('20', '华硕笔记本2', '1520', '2018-07-01', '2018-07-21 21:46:14', '2018-07-21 22:08:52', '0');
INSERT INTO `product_bak` VALUES ('21', 'acer笔记本22', '1522', '2018-02-01', '2018-07-30 21:42:07', '2018-07-30 21:45:36', '1');
INSERT INTO `product_bak` VALUES ('22', 'acer笔记本2', '152', '2018-01-01', '2018-07-30 21:44:00', '2018-07-30 21:44:00', '1');
INSERT INTO `product_bak` VALUES ('23', '', '152', '2018-01-01', '2018-07-30 21:49:37', '2018-07-30 21:49:37', '1');
INSERT INTO `product_bak` VALUES ('24', '联想笔记本1010', '152', '2018-01-01', '2018-07-30 21:55:05', '2018-07-30 21:55:45', '0');
INSERT INTO `product_bak` VALUES ('25', '外星人第四代', '152', '2018-03-01', '2018-07-30 21:58:20', '2018-07-30 22:00:08', '0');

-- ----------------------------
-- Table structure for product_robbing_record
-- ----------------------------
DROP TABLE IF EXISTS `product_robbing_record`;
CREATE TABLE `product_robbing_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(255) DEFAULT NULL COMMENT '手机号',
  `product_id` int(11) DEFAULT NULL COMMENT '产品Id',
  `robbing_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '抢单时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抢单记录表';

-- ----------------------------
-- Records of product_robbing_record
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `sex` int(11) DEFAULT NULL COMMENT '性别（1=男；2=女）',
  `is_active` int(11) DEFAULT '1' COMMENT '是否有效（1=是；0=否）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户信息表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'debug', 'linsen', '1', '1', '2018-07-22 16:48:25', null);
INSERT INTO `user` VALUES ('2', 'jack', '123456', '1', '1', '2018-07-22 16:48:36', null);

-- ----------------------------
-- Table structure for user_log
-- ----------------------------
DROP TABLE IF EXISTS `user_log`;
CREATE TABLE `user_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) NOT NULL COMMENT '用户名',
  `module` varchar(255) DEFAULT NULL COMMENT '模块类型',
  `operation` varchar(255) DEFAULT NULL COMMENT '操作',
  `data` varchar(1000) DEFAULT NULL COMMENT '操作数据',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='用户操作日志';

-- ----------------------------
-- Records of user_log
-- ----------------------------
INSERT INTO `user_log` VALUES ('1', 'jack', 'Login', 'login', '{\"id\":2,\"userName\":\"jack\",\"password\":\"123456\",\"sex\":1,\"isActive\":1,\"createTime\":1532249316000,\"updateTime\":null}', '2018-08-30 23:22:10', null);
INSERT INTO `user_log` VALUES ('2', 'jack', 'Login', 'login', '{\"id\":2,\"userName\":\"jack\",\"password\":\"123456\",\"sex\":1,\"isActive\":1,\"createTime\":1532249316000,\"updateTime\":null}', '2018-08-30 23:29:04', null);
INSERT INTO `user_log` VALUES ('3', 'debug', 'Login', 'login', '{\"id\":1,\"userName\":\"debug\",\"password\":\"linsen\",\"sex\":1,\"isActive\":1,\"createTime\":1532249305000,\"updateTime\":null}', '2018-08-30 23:31:13', null);
INSERT INTO `user_log` VALUES ('4', 'debug', 'Login', 'login', '{\"id\":1,\"userName\":\"debug\",\"password\":\"linsen\",\"sex\":1,\"isActive\":1,\"createTime\":1532249305000,\"updateTime\":null}', '2018-09-01 09:26:54', null);
INSERT INTO `user_log` VALUES ('5', 'debug', 'Login', 'login', '{\"id\":1,\"userName\":\"debug\",\"password\":\"linsen\",\"sex\":1,\"isActive\":1,\"createTime\":1532249305000,\"updateTime\":null}', '2018-09-01 09:28:03', null);
INSERT INTO `user_log` VALUES ('6', 'debug', 'Login', 'login', '{\"id\":1,\"userName\":\"debug\",\"password\":\"linsen\",\"sex\":1,\"isActive\":1,\"createTime\":1532249305000,\"updateTime\":null}', '2018-09-01 09:29:29', null);

-- ----------------------------
-- Table structure for user_order
-- ----------------------------
DROP TABLE IF EXISTS `user_order`;
CREATE TABLE `user_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) NOT NULL COMMENT '订单编号',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `status` int(11) DEFAULT NULL COMMENT '状态(1=已保存；2=已付款；3=已取消)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='用户订单表';

-- ----------------------------
-- Records of user_order
-- ----------------------------
INSERT INTO `user_order` VALUES ('1', '10010', '1', '1', '2018-08-30 22:29:15', '2018-08-30 22:29:43');
INSERT INTO `user_order` VALUES ('2', '10011', '1', '1', '2018-08-30 22:29:54', null);
INSERT INTO `user_order` VALUES ('3', '10012', '1', '1', '2018-08-30 22:41:15', null);
INSERT INTO `user_order` VALUES ('4', '10013', '2', '1', '2018-08-30 22:51:35', null);
INSERT INTO `user_order` VALUES ('5', '10014', '3', '1', '2018-08-30 22:52:08', null);
INSERT INTO `user_order` VALUES ('6', '10015', '4', '1', '2018-08-30 22:53:43', null);
INSERT INTO `user_order` VALUES ('7', 'order_10010', '108', '1', '2018-09-01 16:18:14', null);
INSERT INTO `user_order` VALUES ('8', 'order_10011', '109', '1', '2018-09-01 16:35:24', null);
INSERT INTO `user_order` VALUES ('9', 'order_10011', '109', '1', '2018-09-01 16:36:28', null);
INSERT INTO `user_order` VALUES ('10', 'order_10012', '121', '3', '2018-09-01 16:44:57', '2018-09-01 16:45:07');
INSERT INTO `user_order` VALUES ('11', 'order_10013', '122', '2', '2018-09-01 16:45:32', '2018-09-01 16:45:38');
INSERT INTO `user_order` VALUES ('12', 'order_10014', '126', '3', '2018-09-01 16:55:14', '2018-09-01 16:55:25');
INSERT INTO `user_order` VALUES ('13', 'order_10015', '128', '3', '2018-09-01 16:56:02', '2018-09-01 16:56:13');
INSERT INTO `user_order` VALUES ('14', 'order_10015', '128', '1', '2018-09-01 16:57:45', null);
INSERT INTO `user_order` VALUES ('15', 'order_10016', '129', '1', '2018-09-01 17:01:33', null);

-- ----------------------------
-- View structure for view_ssm_poi_product
-- ----------------------------
DROP VIEW IF EXISTS `view_ssm_poi_product`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_ssm_poi_product` AS select `a`.`id` AS `id`,`a`.`name` AS `name`,`a`.`unit` AS `unit`,`a`.`price` AS `price`,`a`.`stock` AS `stock`,`a`.`remark` AS `remark`,`a`.`purchase_date` AS `purchase_date`,`a`.`create_time` AS `create_time`,`a`.`update_time` AS `update_time`,`a`.`is_delete` AS `is_delete` from `db_ssm_poi`.`product` `a` ;
