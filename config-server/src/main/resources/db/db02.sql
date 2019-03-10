/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1_mysql_root
 Source Server Type    : MySQL
 Source Server Version : 50621
 Source Host           : 127.0.0.1:3306
 Source Schema         : db02

 Target Server Type    : MySQL
 Target Server Version : 50621
 File Encoding         : 65001

 Date: 10/03/2019 17:16:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` int(11) NOT NULL DEFAULT 0,
  `indate` datetime(0) NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of item
-- ----------------------------
INSERT INTO `item` VALUES (3, 1, '2019-03-09 18:20:56');
INSERT INTO `item` VALUES (25, 1, '2019-03-09 20:03:45');
INSERT INTO `item` VALUES (39, 1, '2019-03-09 20:21:12');
INSERT INTO `item` VALUES (40, 1, '2019-03-09 20:21:33');
INSERT INTO `item` VALUES (41, 1, '2019-03-09 20:21:38');
INSERT INTO `item` VALUES (42, 1, '2019-03-09 20:21:40');
INSERT INTO `item` VALUES (43, 1, '2019-03-09 20:30:07');

SET FOREIGN_KEY_CHECKS = 1;
