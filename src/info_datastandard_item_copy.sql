/*
Navicat MySQL Data Transfer

Source Server         : 10.10.130.147
Source Server Version : 50637
Source Host           : 10.10.130.147:3306
Source Database       : gta_data_center20180416

Target Server Type    : MYSQL
Target Server Version : 50637
File Encoding         : 65001

Date: 2018-04-25 17:47:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for info_datastandard_item_copy
-- ----------------------------
DROP TABLE IF EXISTS `info_datastandard_item_copy`;
CREATE TABLE `info_datastandard_item_copy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增序列',
  `subclass_code` varchar(30) COLLATE utf8_bin NOT NULL COMMENT '数据子类编号',
  `item_code` varchar(40) COLLATE utf8_bin NOT NULL COMMENT '元数据编号',
  `item_name` varchar(40) COLLATE utf8_bin NOT NULL COMMENT '数据项名称，字段名',
  `item_comment` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '数据项中文名称,即字段的注释',
  `data_type` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '数据类型',
  `data_length` varchar(30) COLLATE utf8_bin NOT NULL COMMENT '数据长度',
  `data_primarykey` int(1) NOT NULL COMMENT '是否数据主键。0=否，1=是。',
  `data_nullable` int(1) NOT NULL COMMENT '是否可空，0=否，1=是',
  `data_value_source` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '取值范围',
  `data_explain` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '说明/示例',
  `data_referenced` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '引用管理',
  `selectable` varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'M' COMMENT '是否可选。默认M=必选，O=必选',
  `creator` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT 'SuperAdmin' COMMENT '创建人',
  `createtime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updater` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT 'SuperAdmin' COMMENT '更新人',
  `updatetime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  `sourceid` int(11) DEFAULT NULL COMMENT '代码标准分类的自增序列id',
  `set_id` int(11) DEFAULT NULL COMMENT '代码标准集合的id',
  `type_id` int(11) DEFAULT NULL COMMENT '代码标准集合中的分类的id',
  `code_id` int(11) DEFAULT NULL COMMENT '代码标准集合中的代码id',
  `data_referenced1` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '大类',
  `data_referenced2` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '二级类',
  `data_referenced3` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '第三级',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25574 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='元数据定义';
