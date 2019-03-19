/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1_mysql_root
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : 127.0.0.1:3306
 Source Schema         : db01

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 13/03/2019 17:20:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for r_cluster
-- ----------------------------
DROP TABLE IF EXISTS `r_cluster`;
CREATE TABLE `r_cluster`  (
  `ID_CLUSTER` bigint(20) NOT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `BASE_PORT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SOCKETS_BUFFER_SIZE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SOCKETS_FLUSH_INTERVAL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SOCKETS_COMPRESSED` tinyint(1) NULL DEFAULT NULL,
  `DYNAMIC_CLUSTER` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_CLUSTER`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_cluster_slave
-- ----------------------------
DROP TABLE IF EXISTS `r_cluster_slave`;
CREATE TABLE `r_cluster_slave`  (
  `ID_CLUSTER_SLAVE` bigint(20) NOT NULL,
  `ID_CLUSTER` int(11) NULL DEFAULT NULL,
  `ID_SLAVE` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_CLUSTER_SLAVE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_condition
-- ----------------------------
DROP TABLE IF EXISTS `r_condition`;
CREATE TABLE `r_condition`  (
  `ID_CONDITION` bigint(20) NOT NULL,
  `ID_CONDITION_PARENT` int(11) NULL DEFAULT NULL,
  `NEGATED` tinyint(1) NULL DEFAULT NULL,
  `OPERATOR` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LEFT_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CONDITION_FUNCTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `RIGHT_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ID_VALUE_RIGHT` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_CONDITION`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_condition
-- ----------------------------
INSERT INTO `r_condition` VALUES (1, NULL, 0, '-      ', 'flagfield', '=', NULL, 1);
INSERT INTO `r_condition` VALUES (2, NULL, 0, '-      ', 'flagfield', '=', NULL, 2);
INSERT INTO `r_condition` VALUES (3, NULL, 0, '-      ', 'flagfield', '=', NULL, 3);
INSERT INTO `r_condition` VALUES (4, NULL, 0, '-      ', 'flagfield', '=', NULL, 4);

-- ----------------------------
-- Table structure for r_database
-- ----------------------------
DROP TABLE IF EXISTS `r_database`;
CREATE TABLE `r_database`  (
  `ID_DATABASE` bigint(20) NOT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ID_DATABASE_TYPE` int(11) NULL DEFAULT NULL,
  `ID_DATABASE_CONTYPE` int(11) NULL DEFAULT NULL,
  `HOST_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DATABASE_NAME` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `PORT` int(11) NULL DEFAULT NULL,
  `USERNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PASSWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SERVERNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DATA_TBS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `INDEX_TBS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_DATABASE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_database
-- ----------------------------
INSERT INTO `r_database` VALUES (1, 'mysql:58.246.120.122', 33, 1, '58.246.120.122', 'cloudentp', 6033, 'saas', 'Encrypted 2beca9d8542dec7b78e08ac5c9b83eec9', NULL, NULL, NULL);
INSERT INTO `r_database` VALUES (2, 'mysql:127.0.0.1', 33, 1, '127.0.0.1', 'db01', 3306, 'root', 'Encrypted 2be98afc86aa7f2e4cb79ff228dc6fa8c', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for r_database_attribute
-- ----------------------------
DROP TABLE IF EXISTS `r_database_attribute`;
CREATE TABLE `r_database_attribute`  (
  `ID_DATABASE_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_DATABASE` int(11) NULL DEFAULT NULL,
  `CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `VALUE_STR` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID_DATABASE_ATTRIBUTE`) USING BTREE,
  UNIQUE INDEX `IDX_RDAT`(`ID_DATABASE`, `CODE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_database_attribute
-- ----------------------------
INSERT INTO `r_database_attribute` VALUES (1, 1, 'USE_POOLING', 'N');
INSERT INTO `r_database_attribute` VALUES (2, 1, 'PRESERVE_RESERVED_WORD_CASE', 'Y');
INSERT INTO `r_database_attribute` VALUES (3, 1, 'IS_CLUSTERED', 'N');
INSERT INTO `r_database_attribute` VALUES (4, 1, 'SUPPORTS_TIMESTAMP_DATA_TYPE', 'Y');
INSERT INTO `r_database_attribute` VALUES (5, 1, 'SUPPORTS_BOOLEAN_DATA_TYPE', 'Y');
INSERT INTO `r_database_attribute` VALUES (6, 1, 'STREAM_RESULTS', 'Y');
INSERT INTO `r_database_attribute` VALUES (7, 1, 'PORT_NUMBER', '6033');
INSERT INTO `r_database_attribute` VALUES (8, 1, 'FORCE_IDENTIFIERS_TO_UPPERCASE', 'N');
INSERT INTO `r_database_attribute` VALUES (9, 1, 'PREFERRED_SCHEMA_NAME', NULL);
INSERT INTO `r_database_attribute` VALUES (10, 1, 'FORCE_IDENTIFIERS_TO_LOWERCASE', 'N');
INSERT INTO `r_database_attribute` VALUES (11, 1, 'SQL_CONNECT', NULL);
INSERT INTO `r_database_attribute` VALUES (12, 1, 'QUOTE_ALL_FIELDS', 'N');
INSERT INTO `r_database_attribute` VALUES (13, 2, 'USE_POOLING', 'N');
INSERT INTO `r_database_attribute` VALUES (14, 2, 'PRESERVE_RESERVED_WORD_CASE', 'Y');
INSERT INTO `r_database_attribute` VALUES (15, 2, 'IS_CLUSTERED', 'N');
INSERT INTO `r_database_attribute` VALUES (16, 2, 'SUPPORTS_TIMESTAMP_DATA_TYPE', 'Y');
INSERT INTO `r_database_attribute` VALUES (17, 2, 'SUPPORTS_BOOLEAN_DATA_TYPE', 'Y');
INSERT INTO `r_database_attribute` VALUES (18, 2, 'STREAM_RESULTS', 'Y');
INSERT INTO `r_database_attribute` VALUES (19, 2, 'PORT_NUMBER', '3306');
INSERT INTO `r_database_attribute` VALUES (20, 2, 'FORCE_IDENTIFIERS_TO_UPPERCASE', 'N');
INSERT INTO `r_database_attribute` VALUES (21, 2, 'PREFERRED_SCHEMA_NAME', NULL);
INSERT INTO `r_database_attribute` VALUES (22, 2, 'FORCE_IDENTIFIERS_TO_LOWERCASE', 'N');
INSERT INTO `r_database_attribute` VALUES (23, 2, 'SQL_CONNECT', NULL);
INSERT INTO `r_database_attribute` VALUES (24, 2, 'QUOTE_ALL_FIELDS', 'N');

-- ----------------------------
-- Table structure for r_database_contype
-- ----------------------------
DROP TABLE IF EXISTS `r_database_contype`;
CREATE TABLE `r_database_contype`  (
  `ID_DATABASE_CONTYPE` bigint(20) NOT NULL,
  `CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_DATABASE_CONTYPE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_database_contype
-- ----------------------------
INSERT INTO `r_database_contype` VALUES (1, 'Native', 'Native (JDBC)');
INSERT INTO `r_database_contype` VALUES (2, 'ODBC', 'ODBC');
INSERT INTO `r_database_contype` VALUES (3, 'OCI', 'OCI');
INSERT INTO `r_database_contype` VALUES (4, 'Plugin', 'Plugin specific access method');
INSERT INTO `r_database_contype` VALUES (5, 'JNDI', 'JNDI');
INSERT INTO `r_database_contype` VALUES (6, ',', 'Custom');

-- ----------------------------
-- Table structure for r_database_type
-- ----------------------------
DROP TABLE IF EXISTS `r_database_type`;
CREATE TABLE `r_database_type`  (
  `ID_DATABASE_TYPE` bigint(20) NOT NULL,
  `CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_DATABASE_TYPE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_database_type
-- ----------------------------
INSERT INTO `r_database_type` VALUES (1, 'DERBY', 'Apache Derby');
INSERT INTO `r_database_type` VALUES (2, 'AS/400', 'AS/400');
INSERT INTO `r_database_type` VALUES (3, 'INTERBASE', 'Borland Interbase');
INSERT INTO `r_database_type` VALUES (4, 'INFINIDB', 'Calpont InfiniDB');
INSERT INTO `r_database_type` VALUES (5, 'IMPALASIMBA', 'Cloudera Impala');
INSERT INTO `r_database_type` VALUES (6, 'DBASE', 'dBase III, IV or 5');
INSERT INTO `r_database_type` VALUES (7, 'EXASOL4', 'Exasol 4');
INSERT INTO `r_database_type` VALUES (8, 'EXTENDB', 'ExtenDB');
INSERT INTO `r_database_type` VALUES (9, 'FIREBIRD', 'Firebird SQL');
INSERT INTO `r_database_type` VALUES (10, 'GENERIC', 'Generic database');
INSERT INTO `r_database_type` VALUES (11, 'GOOGLEBIGQUERY', 'Google BigQuery');
INSERT INTO `r_database_type` VALUES (12, 'GREENPLUM', 'Greenplum');
INSERT INTO `r_database_type` VALUES (13, 'SQLBASE', 'Gupta SQL Base');
INSERT INTO `r_database_type` VALUES (14, 'H2', 'H2');
INSERT INTO `r_database_type` VALUES (15, 'HIVE', 'Hadoop Hive');
INSERT INTO `r_database_type` VALUES (16, 'HIVE2', 'Hadoop Hive 2');
INSERT INTO `r_database_type` VALUES (17, 'HYPERSONIC', 'Hypersonic');
INSERT INTO `r_database_type` VALUES (18, 'DB2', 'IBM DB2');
INSERT INTO `r_database_type` VALUES (19, 'IMPALA', 'Impala');
INSERT INTO `r_database_type` VALUES (20, 'INFOBRIGHT', 'Infobright');
INSERT INTO `r_database_type` VALUES (21, 'INFORMIX', 'Informix');
INSERT INTO `r_database_type` VALUES (22, 'INGRES', 'Ingres');
INSERT INTO `r_database_type` VALUES (23, 'VECTORWISE', 'Ingres VectorWise');
INSERT INTO `r_database_type` VALUES (24, 'CACHE', 'Intersystems Cache');
INSERT INTO `r_database_type` VALUES (25, 'KINGBASEES', 'KingbaseES');
INSERT INTO `r_database_type` VALUES (26, 'LucidDB', 'LucidDB');
INSERT INTO `r_database_type` VALUES (27, 'MARIADB', 'MariaDB');
INSERT INTO `r_database_type` VALUES (28, 'SAPDB', 'MaxDB (SAP DB)');
INSERT INTO `r_database_type` VALUES (29, 'MONETDB', 'MonetDB');
INSERT INTO `r_database_type` VALUES (30, 'MSACCESS', 'MS Access');
INSERT INTO `r_database_type` VALUES (31, 'MSSQL', 'MS SQL Server');
INSERT INTO `r_database_type` VALUES (32, 'MSSQLNATIVE', 'MS SQL Server (Native)');
INSERT INTO `r_database_type` VALUES (33, 'MYSQL', 'MySQL');
INSERT INTO `r_database_type` VALUES (34, 'MONDRIAN', 'Native Mondrian');
INSERT INTO `r_database_type` VALUES (35, 'NEOVIEW', 'Neoview');
INSERT INTO `r_database_type` VALUES (36, 'NETEZZA', 'Netezza');
INSERT INTO `r_database_type` VALUES (37, 'OpenERPDatabaseMeta', 'OpenERP Server');
INSERT INTO `r_database_type` VALUES (38, 'ORACLE', 'Oracle');
INSERT INTO `r_database_type` VALUES (39, 'ORACLERDB', 'Oracle RDB');
INSERT INTO `r_database_type` VALUES (40, 'PALO', 'Palo MOLAP Server');
INSERT INTO `r_database_type` VALUES (41, 'KettleThin', 'Pentaho Data Services');
INSERT INTO `r_database_type` VALUES (42, 'POSTGRESQL', 'PostgreSQL');
INSERT INTO `r_database_type` VALUES (43, 'REDSHIFT', 'Redshift');
INSERT INTO `r_database_type` VALUES (44, 'REMEDY-AR-SYSTEM', 'Remedy Action Request System');
INSERT INTO `r_database_type` VALUES (45, 'SAPR3', 'SAP ERP System');
INSERT INTO `r_database_type` VALUES (46, 'SPARKSIMBA', 'SparkSQL');
INSERT INTO `r_database_type` VALUES (47, 'SQLITE', 'SQLite');
INSERT INTO `r_database_type` VALUES (48, 'SYBASE', 'Sybase');
INSERT INTO `r_database_type` VALUES (49, 'SYBASEIQ', 'SybaseIQ');
INSERT INTO `r_database_type` VALUES (50, 'TERADATA', 'Teradata');
INSERT INTO `r_database_type` VALUES (51, 'UNIVERSE', 'UniVerse database');
INSERT INTO `r_database_type` VALUES (52, 'VERTICA', 'Vertica');
INSERT INTO `r_database_type` VALUES (53, 'VERTICA5', 'Vertica 5+');

-- ----------------------------
-- Table structure for r_dependency
-- ----------------------------
DROP TABLE IF EXISTS `r_dependency`;
CREATE TABLE `r_dependency`  (
  `ID_DEPENDENCY` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) NULL DEFAULT NULL,
  `ID_DATABASE` int(11) NULL DEFAULT NULL,
  `TABLE_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FIELD_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_DEPENDENCY`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_directory
-- ----------------------------
DROP TABLE IF EXISTS `r_directory`;
CREATE TABLE `r_directory`  (
  `ID_DIRECTORY` bigint(20) NOT NULL,
  `ID_DIRECTORY_PARENT` int(11) NULL DEFAULT NULL,
  `DIRECTORY_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_DIRECTORY`) USING BTREE,
  UNIQUE INDEX `IDX_RDIR`(`ID_DIRECTORY_PARENT`, `DIRECTORY_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_element
-- ----------------------------
DROP TABLE IF EXISTS `r_element`;
CREATE TABLE `r_element`  (
  `ID_ELEMENT` bigint(20) NOT NULL,
  `ID_ELEMENT_TYPE` int(11) NULL DEFAULT NULL,
  `NAME` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID_ELEMENT`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_element_attribute
-- ----------------------------
DROP TABLE IF EXISTS `r_element_attribute`;
CREATE TABLE `r_element_attribute`  (
  `ID_ELEMENT_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_ELEMENT` int(11) NULL DEFAULT NULL,
  `ID_ELEMENT_ATTRIBUTE_PARENT` int(11) NULL DEFAULT NULL,
  `ATTR_KEY` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ATTR_VALUE` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID_ELEMENT_ATTRIBUTE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_element_type
-- ----------------------------
DROP TABLE IF EXISTS `r_element_type`;
CREATE TABLE `r_element_type`  (
  `ID_ELEMENT_TYPE` bigint(20) NOT NULL,
  `ID_NAMESPACE` int(11) NULL DEFAULT NULL,
  `NAME` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `DESCRIPTION` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID_ELEMENT_TYPE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_job
-- ----------------------------
DROP TABLE IF EXISTS `r_job`;
CREATE TABLE `r_job`  (
  `ID_JOB` bigint(20) NOT NULL,
  `ID_DIRECTORY` int(11) NULL DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DESCRIPTION` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `EXTENDED_DESCRIPTION` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `JOB_VERSION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JOB_STATUS` int(11) NULL DEFAULT NULL,
  `ID_DATABASE_LOG` int(11) NULL DEFAULT NULL,
  `TABLE_NAME_LOG` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CREATED_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CREATED_DATE` datetime(0) NULL DEFAULT NULL,
  `MODIFIED_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODIFIED_DATE` datetime(0) NULL DEFAULT NULL,
  `USE_BATCH_ID` tinyint(1) NULL DEFAULT NULL,
  `PASS_BATCH_ID` tinyint(1) NULL DEFAULT NULL,
  `USE_LOGFIELD` tinyint(1) NULL DEFAULT NULL,
  `SHARED_FILE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_JOB`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_job
-- ----------------------------
INSERT INTO `r_job` VALUES (1, 0, 'entp_job', NULL, NULL, NULL, -1, -1, NULL, '-', '2018-12-27 20:14:08', 'admin', '2018-12-27 21:30:07', 1, 0, 1, NULL);

-- ----------------------------
-- Table structure for r_job_attribute
-- ----------------------------
DROP TABLE IF EXISTS `r_job_attribute`;
CREATE TABLE `r_job_attribute`  (
  `ID_JOB_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_JOB` int(11) NULL DEFAULT NULL,
  `NR` int(11) NULL DEFAULT NULL,
  `CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `VALUE_NUM` bigint(20) NULL DEFAULT NULL,
  `VALUE_STR` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID_JOB_ATTRIBUTE`) USING BTREE,
  UNIQUE INDEX `IDX_JATT`(`ID_JOB`, `CODE`, `NR`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_job_attribute
-- ----------------------------
INSERT INTO `r_job_attribute` VALUES (1, 1, 0, 'LOG_SIZE_LIMIT', 0, NULL);
INSERT INTO `r_job_attribute` VALUES (2, 1, 0, 'JOB_LOG_TABLE_CONNECTION_NAME', 0, NULL);
INSERT INTO `r_job_attribute` VALUES (3, 1, 0, 'JOB_LOG_TABLE_SCHEMA_NAME', 0, NULL);
INSERT INTO `r_job_attribute` VALUES (4, 1, 0, 'JOB_LOG_TABLE_TABLE_NAME', 0, NULL);
INSERT INTO `r_job_attribute` VALUES (5, 1, 0, 'JOB_LOG_TABLE_TIMEOUT_IN_DAYS', 0, NULL);
INSERT INTO `r_job_attribute` VALUES (6, 1, 0, 'JOB_LOG_TABLE_FIELD_ID0', 0, 'ID_JOB');
INSERT INTO `r_job_attribute` VALUES (7, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME0', 0, 'ID_JOB');
INSERT INTO `r_job_attribute` VALUES (8, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED0', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (9, 1, 0, 'JOB_LOG_TABLE_FIELD_ID1', 0, 'CHANNEL_ID');
INSERT INTO `r_job_attribute` VALUES (10, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME1', 0, 'CHANNEL_ID');
INSERT INTO `r_job_attribute` VALUES (11, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED1', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (12, 1, 0, 'JOB_LOG_TABLE_FIELD_ID2', 0, 'JOBNAME');
INSERT INTO `r_job_attribute` VALUES (13, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME2', 0, 'JOBNAME');
INSERT INTO `r_job_attribute` VALUES (14, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED2', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (15, 1, 0, 'JOB_LOG_TABLE_FIELD_ID3', 0, 'STATUS');
INSERT INTO `r_job_attribute` VALUES (16, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME3', 0, 'STATUS');
INSERT INTO `r_job_attribute` VALUES (17, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED3', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (18, 1, 0, 'JOB_LOG_TABLE_FIELD_ID4', 0, 'LINES_READ');
INSERT INTO `r_job_attribute` VALUES (19, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME4', 0, 'LINES_READ');
INSERT INTO `r_job_attribute` VALUES (20, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED4', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (21, 1, 0, 'JOB_LOG_TABLE_FIELD_ID5', 0, 'LINES_WRITTEN');
INSERT INTO `r_job_attribute` VALUES (22, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME5', 0, 'LINES_WRITTEN');
INSERT INTO `r_job_attribute` VALUES (23, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED5', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (24, 1, 0, 'JOB_LOG_TABLE_FIELD_ID6', 0, 'LINES_UPDATED');
INSERT INTO `r_job_attribute` VALUES (25, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME6', 0, 'LINES_UPDATED');
INSERT INTO `r_job_attribute` VALUES (26, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED6', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (27, 1, 0, 'JOB_LOG_TABLE_FIELD_ID7', 0, 'LINES_INPUT');
INSERT INTO `r_job_attribute` VALUES (28, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME7', 0, 'LINES_INPUT');
INSERT INTO `r_job_attribute` VALUES (29, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED7', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (30, 1, 0, 'JOB_LOG_TABLE_FIELD_ID8', 0, 'LINES_OUTPUT');
INSERT INTO `r_job_attribute` VALUES (31, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME8', 0, 'LINES_OUTPUT');
INSERT INTO `r_job_attribute` VALUES (32, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED8', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (33, 1, 0, 'JOB_LOG_TABLE_FIELD_ID9', 0, 'LINES_REJECTED');
INSERT INTO `r_job_attribute` VALUES (34, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME9', 0, 'LINES_REJECTED');
INSERT INTO `r_job_attribute` VALUES (35, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED9', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (36, 1, 0, 'JOB_LOG_TABLE_FIELD_ID10', 0, 'ERRORS');
INSERT INTO `r_job_attribute` VALUES (37, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME10', 0, 'ERRORS');
INSERT INTO `r_job_attribute` VALUES (38, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED10', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (39, 1, 0, 'JOB_LOG_TABLE_FIELD_ID11', 0, 'STARTDATE');
INSERT INTO `r_job_attribute` VALUES (40, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME11', 0, 'STARTDATE');
INSERT INTO `r_job_attribute` VALUES (41, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED11', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (42, 1, 0, 'JOB_LOG_TABLE_FIELD_ID12', 0, 'ENDDATE');
INSERT INTO `r_job_attribute` VALUES (43, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME12', 0, 'ENDDATE');
INSERT INTO `r_job_attribute` VALUES (44, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED12', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (45, 1, 0, 'JOB_LOG_TABLE_FIELD_ID13', 0, 'LOGDATE');
INSERT INTO `r_job_attribute` VALUES (46, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME13', 0, 'LOGDATE');
INSERT INTO `r_job_attribute` VALUES (47, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED13', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (48, 1, 0, 'JOB_LOG_TABLE_FIELD_ID14', 0, 'DEPDATE');
INSERT INTO `r_job_attribute` VALUES (49, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME14', 0, 'DEPDATE');
INSERT INTO `r_job_attribute` VALUES (50, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED14', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (51, 1, 0, 'JOB_LOG_TABLE_FIELD_ID15', 0, 'REPLAYDATE');
INSERT INTO `r_job_attribute` VALUES (52, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME15', 0, 'REPLAYDATE');
INSERT INTO `r_job_attribute` VALUES (53, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED15', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (54, 1, 0, 'JOB_LOG_TABLE_FIELD_ID16', 0, 'LOG_FIELD');
INSERT INTO `r_job_attribute` VALUES (55, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME16', 0, 'LOG_FIELD');
INSERT INTO `r_job_attribute` VALUES (56, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED16', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (57, 1, 0, 'JOB_LOG_TABLE_FIELD_ID17', 0, 'EXECUTING_SERVER');
INSERT INTO `r_job_attribute` VALUES (58, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME17', 0, 'EXECUTING_SERVER');
INSERT INTO `r_job_attribute` VALUES (59, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED17', 0, 'N');
INSERT INTO `r_job_attribute` VALUES (60, 1, 0, 'JOB_LOG_TABLE_FIELD_ID18', 0, 'EXECUTING_USER');
INSERT INTO `r_job_attribute` VALUES (61, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME18', 0, 'EXECUTING_USER');
INSERT INTO `r_job_attribute` VALUES (62, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED18', 0, 'N');
INSERT INTO `r_job_attribute` VALUES (63, 1, 0, 'JOB_LOG_TABLE_FIELD_ID19', 0, 'START_JOB_ENTRY');
INSERT INTO `r_job_attribute` VALUES (64, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME19', 0, 'START_JOB_ENTRY');
INSERT INTO `r_job_attribute` VALUES (65, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED19', 0, 'N');
INSERT INTO `r_job_attribute` VALUES (66, 1, 0, 'JOB_LOG_TABLE_FIELD_ID20', 0, 'CLIENT');
INSERT INTO `r_job_attribute` VALUES (67, 1, 0, 'JOB_LOG_TABLE_FIELD_NAME20', 0, 'CLIENT');
INSERT INTO `r_job_attribute` VALUES (68, 1, 0, 'JOB_LOG_TABLE_FIELD_ENABLED20', 0, 'N');
INSERT INTO `r_job_attribute` VALUES (69, 1, 0, 'JOBLOG_TABLE_INTERVAL', 0, NULL);
INSERT INTO `r_job_attribute` VALUES (70, 1, 0, 'JOBLOG_TABLE_SIZE_LIMIT', 0, NULL);
INSERT INTO `r_job_attribute` VALUES (71, 1, 0, 'JOB_ENTRY_LOG_TABLE_CONNECTION_NAME', 0, NULL);
INSERT INTO `r_job_attribute` VALUES (72, 1, 0, 'JOB_ENTRY_LOG_TABLE_SCHEMA_NAME', 0, NULL);
INSERT INTO `r_job_attribute` VALUES (73, 1, 0, 'JOB_ENTRY_LOG_TABLE_TABLE_NAME', 0, NULL);
INSERT INTO `r_job_attribute` VALUES (74, 1, 0, 'JOB_ENTRY_LOG_TABLE_TIMEOUT_IN_DAYS', 0, NULL);
INSERT INTO `r_job_attribute` VALUES (75, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID0', 0, 'ID_BATCH');
INSERT INTO `r_job_attribute` VALUES (76, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME0', 0, 'ID_BATCH');
INSERT INTO `r_job_attribute` VALUES (77, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED0', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (78, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID1', 0, 'CHANNEL_ID');
INSERT INTO `r_job_attribute` VALUES (79, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME1', 0, 'CHANNEL_ID');
INSERT INTO `r_job_attribute` VALUES (80, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED1', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (81, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID2', 0, 'LOG_DATE');
INSERT INTO `r_job_attribute` VALUES (82, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME2', 0, 'LOG_DATE');
INSERT INTO `r_job_attribute` VALUES (83, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED2', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (84, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID3', 0, 'JOBNAME');
INSERT INTO `r_job_attribute` VALUES (85, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME3', 0, 'TRANSNAME');
INSERT INTO `r_job_attribute` VALUES (86, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED3', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (87, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID4', 0, 'JOBENTRYNAME');
INSERT INTO `r_job_attribute` VALUES (88, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME4', 0, 'STEPNAME');
INSERT INTO `r_job_attribute` VALUES (89, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED4', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (90, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID5', 0, 'LINES_READ');
INSERT INTO `r_job_attribute` VALUES (91, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME5', 0, 'LINES_READ');
INSERT INTO `r_job_attribute` VALUES (92, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED5', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (93, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID6', 0, 'LINES_WRITTEN');
INSERT INTO `r_job_attribute` VALUES (94, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME6', 0, 'LINES_WRITTEN');
INSERT INTO `r_job_attribute` VALUES (95, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED6', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (96, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID7', 0, 'LINES_UPDATED');
INSERT INTO `r_job_attribute` VALUES (97, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME7', 0, 'LINES_UPDATED');
INSERT INTO `r_job_attribute` VALUES (98, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED7', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (99, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID8', 0, 'LINES_INPUT');
INSERT INTO `r_job_attribute` VALUES (100, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME8', 0, 'LINES_INPUT');
INSERT INTO `r_job_attribute` VALUES (101, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED8', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (102, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID9', 0, 'LINES_OUTPUT');
INSERT INTO `r_job_attribute` VALUES (103, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME9', 0, 'LINES_OUTPUT');
INSERT INTO `r_job_attribute` VALUES (104, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED9', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (105, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID10', 0, 'LINES_REJECTED');
INSERT INTO `r_job_attribute` VALUES (106, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME10', 0, 'LINES_REJECTED');
INSERT INTO `r_job_attribute` VALUES (107, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED10', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (108, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID11', 0, 'ERRORS');
INSERT INTO `r_job_attribute` VALUES (109, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME11', 0, 'ERRORS');
INSERT INTO `r_job_attribute` VALUES (110, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED11', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (111, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID12', 0, 'RESULT');
INSERT INTO `r_job_attribute` VALUES (112, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME12', 0, 'RESULT');
INSERT INTO `r_job_attribute` VALUES (113, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED12', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (114, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID13', 0, 'NR_RESULT_ROWS');
INSERT INTO `r_job_attribute` VALUES (115, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME13', 0, 'NR_RESULT_ROWS');
INSERT INTO `r_job_attribute` VALUES (116, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED13', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (117, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID14', 0, 'NR_RESULT_FILES');
INSERT INTO `r_job_attribute` VALUES (118, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME14', 0, 'NR_RESULT_FILES');
INSERT INTO `r_job_attribute` VALUES (119, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED14', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (120, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID15', 0, 'LOG_FIELD');
INSERT INTO `r_job_attribute` VALUES (121, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME15', 0, 'LOG_FIELD');
INSERT INTO `r_job_attribute` VALUES (122, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED15', 0, 'N');
INSERT INTO `r_job_attribute` VALUES (123, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ID16', 0, 'COPY_NR');
INSERT INTO `r_job_attribute` VALUES (124, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_NAME16', 0, 'COPY_NR');
INSERT INTO `r_job_attribute` VALUES (125, 1, 0, 'JOB_ENTRY_LOG_TABLE_FIELD_ENABLED16', 0, 'N');
INSERT INTO `r_job_attribute` VALUES (126, 1, 0, 'CHANNEL_LOG_TABLE_CONNECTION_NAME', 0, NULL);
INSERT INTO `r_job_attribute` VALUES (127, 1, 0, 'CHANNEL_LOG_TABLE_SCHEMA_NAME', 0, NULL);
INSERT INTO `r_job_attribute` VALUES (128, 1, 0, 'CHANNEL_LOG_TABLE_TABLE_NAME', 0, NULL);
INSERT INTO `r_job_attribute` VALUES (129, 1, 0, 'CHANNEL_LOG_TABLE_TIMEOUT_IN_DAYS', 0, NULL);
INSERT INTO `r_job_attribute` VALUES (130, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID0', 0, 'ID_BATCH');
INSERT INTO `r_job_attribute` VALUES (131, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME0', 0, 'ID_BATCH');
INSERT INTO `r_job_attribute` VALUES (132, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED0', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (133, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID1', 0, 'CHANNEL_ID');
INSERT INTO `r_job_attribute` VALUES (134, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME1', 0, 'CHANNEL_ID');
INSERT INTO `r_job_attribute` VALUES (135, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED1', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (136, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID2', 0, 'LOG_DATE');
INSERT INTO `r_job_attribute` VALUES (137, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME2', 0, 'LOG_DATE');
INSERT INTO `r_job_attribute` VALUES (138, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED2', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (139, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID3', 0, 'LOGGING_OBJECT_TYPE');
INSERT INTO `r_job_attribute` VALUES (140, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME3', 0, 'LOGGING_OBJECT_TYPE');
INSERT INTO `r_job_attribute` VALUES (141, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED3', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (142, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID4', 0, 'OBJECT_NAME');
INSERT INTO `r_job_attribute` VALUES (143, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME4', 0, 'OBJECT_NAME');
INSERT INTO `r_job_attribute` VALUES (144, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED4', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (145, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID5', 0, 'OBJECT_COPY');
INSERT INTO `r_job_attribute` VALUES (146, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME5', 0, 'OBJECT_COPY');
INSERT INTO `r_job_attribute` VALUES (147, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED5', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (148, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID6', 0, 'REPOSITORY_DIRECTORY');
INSERT INTO `r_job_attribute` VALUES (149, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME6', 0, 'REPOSITORY_DIRECTORY');
INSERT INTO `r_job_attribute` VALUES (150, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED6', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (151, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID7', 0, 'FILENAME');
INSERT INTO `r_job_attribute` VALUES (152, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME7', 0, 'FILENAME');
INSERT INTO `r_job_attribute` VALUES (153, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED7', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (154, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID8', 0, 'OBJECT_ID');
INSERT INTO `r_job_attribute` VALUES (155, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME8', 0, 'OBJECT_ID');
INSERT INTO `r_job_attribute` VALUES (156, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED8', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (157, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID9', 0, 'OBJECT_REVISION');
INSERT INTO `r_job_attribute` VALUES (158, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME9', 0, 'OBJECT_REVISION');
INSERT INTO `r_job_attribute` VALUES (159, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED9', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (160, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID10', 0, 'PARENT_CHANNEL_ID');
INSERT INTO `r_job_attribute` VALUES (161, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME10', 0, 'PARENT_CHANNEL_ID');
INSERT INTO `r_job_attribute` VALUES (162, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED10', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (163, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID11', 0, 'ROOT_CHANNEL_ID');
INSERT INTO `r_job_attribute` VALUES (164, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME11', 0, 'ROOT_CHANNEL_ID');
INSERT INTO `r_job_attribute` VALUES (165, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED11', 0, 'Y');
INSERT INTO `r_job_attribute` VALUES (166, 1, 0, '_ATTR_	{\"_\":\"Embedded MetaStore Elements\",\"namespace\":\"pentaho\",\"type\":\"Default Run Configuration\"}	Pentaho local', 0, '{\"children\":[{\"children\":[],\"id\":\"server\",\"value\":null},{\"children\":[],\"id\":\"clustered\",\"value\":\"N\"},{\"children\":[],\"id\":\"name\",\"value\":\"Pentaho local\"},{\"children\":[],\"id\":\"description\",\"value\":null},{\"children\":[],\"id\":\"pentaho\",\"value\":\"N\"},{\"children\":[],\"id\":\"readOnly\",\"value\":\"Y\"},{\"children\":[],\"id\":\"sendResources\",\"value\":\"N\"},{\"children\":[],\"id\":\"logRemoteExecutionLocally\",\"value\":\"N\"},{\"children\":[],\"id\":\"remote\",\"value\":\"N\"},{\"children\":[],\"id\":\"local\",\"value\":\"Y\"},{\"children\":[],\"id\":\"showTransformations\",\"value\":\"N\"}],\"id\":\"Pentaho local\",\"value\":null,\"name\":\"Pentaho local\",\"owner\":null,\"ownerPermissionsList\":[]}');
INSERT INTO `r_job_attribute` VALUES (167, 1, 0, '_ATTR_	METASTORE.pentaho	Default Run Configuration', 0, '{\"namespace\":\"pentaho\",\"id\":\"Default Run Configuration\",\"name\":\"Default Run Configuration\",\"description\":\"Defines a default run configuration\",\"metaStoreName\":null}');

-- ----------------------------
-- Table structure for r_job_hop
-- ----------------------------
DROP TABLE IF EXISTS `r_job_hop`;
CREATE TABLE `r_job_hop`  (
  `ID_JOB_HOP` bigint(20) NOT NULL,
  `ID_JOB` int(11) NULL DEFAULT NULL,
  `ID_JOBENTRY_COPY_FROM` int(11) NULL DEFAULT NULL,
  `ID_JOBENTRY_COPY_TO` int(11) NULL DEFAULT NULL,
  `ENABLED` tinyint(1) NULL DEFAULT NULL,
  `EVALUATION` tinyint(1) NULL DEFAULT NULL,
  `UNCONDITIONAL` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_JOB_HOP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_job_hop
-- ----------------------------
INSERT INTO `r_job_hop` VALUES (1, 1, 1, 4, 1, 1, 1);
INSERT INTO `r_job_hop` VALUES (2, 1, 4, 3, 1, 1, 0);
INSERT INTO `r_job_hop` VALUES (3, 1, 3, 2, 1, 1, 0);

-- ----------------------------
-- Table structure for r_job_lock
-- ----------------------------
DROP TABLE IF EXISTS `r_job_lock`;
CREATE TABLE `r_job_lock`  (
  `ID_JOB_LOCK` bigint(20) NOT NULL,
  `ID_JOB` int(11) NULL DEFAULT NULL,
  `ID_USER` int(11) NULL DEFAULT NULL,
  `LOCK_MESSAGE` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `LOCK_DATE` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_JOB_LOCK`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_job_note
-- ----------------------------
DROP TABLE IF EXISTS `r_job_note`;
CREATE TABLE `r_job_note`  (
  `ID_JOB` int(11) NULL DEFAULT NULL,
  `ID_NOTE` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_jobentry
-- ----------------------------
DROP TABLE IF EXISTS `r_jobentry`;
CREATE TABLE `r_jobentry`  (
  `ID_JOBENTRY` bigint(20) NOT NULL,
  `ID_JOB` int(11) NULL DEFAULT NULL,
  `ID_JOBENTRY_TYPE` int(11) NULL DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DESCRIPTION` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID_JOBENTRY`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_jobentry
-- ----------------------------
INSERT INTO `r_jobentry` VALUES (1, 1, 75, 'Start', NULL);
INSERT INTO `r_jobentry` VALUES (2, 1, 60, '成功', NULL);
INSERT INTO `r_jobentry` VALUES (3, 1, 56, '发送邮件', NULL);
INSERT INTO `r_jobentry` VALUES (4, 1, 88, '转换', NULL);

-- ----------------------------
-- Table structure for r_jobentry_attribute
-- ----------------------------
DROP TABLE IF EXISTS `r_jobentry_attribute`;
CREATE TABLE `r_jobentry_attribute`  (
  `ID_JOBENTRY_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_JOB` int(11) NULL DEFAULT NULL,
  `ID_JOBENTRY` int(11) NULL DEFAULT NULL,
  `NR` int(11) NULL DEFAULT NULL,
  `CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `VALUE_NUM` double NULL DEFAULT NULL,
  `VALUE_STR` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID_JOBENTRY_ATTRIBUTE`) USING BTREE,
  UNIQUE INDEX `IDX_RJEA`(`ID_JOBENTRY_ATTRIBUTE`, `CODE`, `NR`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_jobentry_attribute
-- ----------------------------
INSERT INTO `r_jobentry_attribute` VALUES (1, 1, 1, 0, 'start', 0, 'Y');
INSERT INTO `r_jobentry_attribute` VALUES (2, 1, 1, 0, 'dummy', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (3, 1, 1, 0, 'repeat', 0, 'Y');
INSERT INTO `r_jobentry_attribute` VALUES (4, 1, 1, 0, 'schedulerType', 1, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (5, 1, 1, 0, 'intervalSeconds', 0, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (6, 1, 1, 0, 'intervalMinutes', 1, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (7, 1, 1, 0, 'hour', 12, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (8, 1, 1, 0, 'minutes', 0, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (9, 1, 1, 0, 'weekDay', 1, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (10, 1, 1, 0, 'dayOfMonth', 1, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (11, 1, 3, 0, 'server', 0, 'smtp.walkiesoft.com');
INSERT INTO `r_jobentry_attribute` VALUES (12, 1, 3, 0, 'port', 0, '25');
INSERT INTO `r_jobentry_attribute` VALUES (13, 1, 3, 0, 'destination', 0, '810653725@qq.com');
INSERT INTO `r_jobentry_attribute` VALUES (14, 1, 3, 0, 'destinationCc', 0, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (15, 1, 3, 0, 'destinationBCc', 0, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (16, 1, 3, 0, 'replyto', 0, 'lilianghui@walkiesoft.com');
INSERT INTO `r_jobentry_attribute` VALUES (17, 1, 3, 0, 'replytoname', 0, 'lilianghui@walkiesoft.com');
INSERT INTO `r_jobentry_attribute` VALUES (18, 1, 3, 0, 'subject', 0, '企业信息数据同步');
INSERT INTO `r_jobentry_attribute` VALUES (19, 1, 3, 0, 'include_date', 0, 'Y');
INSERT INTO `r_jobentry_attribute` VALUES (20, 1, 3, 0, 'contact_person', 0, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (21, 1, 3, 0, 'contact_phone', 0, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (22, 1, 3, 0, 'comment', 0, '同步完成');
INSERT INTO `r_jobentry_attribute` VALUES (23, 1, 3, 0, 'encoding', 0, 'UTF-8');
INSERT INTO `r_jobentry_attribute` VALUES (24, 1, 3, 0, 'priority', 0, 'normal');
INSERT INTO `r_jobentry_attribute` VALUES (25, 1, 3, 0, 'importance', 0, 'normal');
INSERT INTO `r_jobentry_attribute` VALUES (26, 1, 3, 0, 'sensitivity', 0, 'normal');
INSERT INTO `r_jobentry_attribute` VALUES (27, 1, 3, 0, 'include_files', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (28, 1, 3, 0, 'use_auth', 0, 'Y');
INSERT INTO `r_jobentry_attribute` VALUES (29, 1, 3, 0, 'use_secure_auth', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (30, 1, 3, 0, 'auth_user', 0, 'lilianghui@walkiesoft.com');
INSERT INTO `r_jobentry_attribute` VALUES (31, 1, 3, 0, 'auth_password', 0, 'Encrypted 2be98afc86aa7f2e4fa08af6afea59ce2');
INSERT INTO `r_jobentry_attribute` VALUES (32, 1, 3, 0, 'only_comment', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (33, 1, 3, 0, 'use_HTML', 0, 'Y');
INSERT INTO `r_jobentry_attribute` VALUES (34, 1, 3, 0, 'use_Priority', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (35, 1, 3, 0, 'secureconnectiontype', 0, 'SSL');
INSERT INTO `r_jobentry_attribute` VALUES (36, 1, 3, 0, 'zip_files', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (37, 1, 3, 0, 'zip_name', 0, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (38, 1, 3, 0, 'replyToAddresses', 0, 'lilianghui@walkiesoft.com');
INSERT INTO `r_jobentry_attribute` VALUES (39, 1, 4, 0, 'specification_method', 0, 'rep_name');
INSERT INTO `r_jobentry_attribute` VALUES (40, 1, 4, 0, 'trans_object_id', 0, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (41, 1, 4, 0, 'name', 0, 'entp_input');
INSERT INTO `r_jobentry_attribute` VALUES (42, 1, 4, 0, 'dir_path', 0, '/');
INSERT INTO `r_jobentry_attribute` VALUES (43, 1, 4, 0, 'file_name', 0, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (44, 1, 4, 0, 'arg_from_previous', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (45, 1, 4, 0, 'params_from_previous', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (46, 1, 4, 0, 'exec_per_row', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (47, 1, 4, 0, 'clear_rows', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (48, 1, 4, 0, 'clear_files', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (49, 1, 4, 0, 'set_logfile', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (50, 1, 4, 0, 'add_date', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (51, 1, 4, 0, 'add_time', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (52, 1, 4, 0, 'logfile', 0, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (53, 1, 4, 0, 'logext', 0, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (54, 1, 4, 0, 'loglevel', 0, 'Basic');
INSERT INTO `r_jobentry_attribute` VALUES (55, 1, 4, 0, 'cluster', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (56, 1, 4, 0, 'slave_server_name', 0, NULL);
INSERT INTO `r_jobentry_attribute` VALUES (57, 1, 4, 0, 'set_append_logfile', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (58, 1, 4, 0, 'wait_until_finished', 0, 'Y');
INSERT INTO `r_jobentry_attribute` VALUES (59, 1, 4, 0, 'follow_abort_remote', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (60, 1, 4, 0, 'create_parent_folder', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (61, 1, 4, 0, 'logging_remote_work', 0, 'N');
INSERT INTO `r_jobentry_attribute` VALUES (62, 1, 4, 0, 'run_configuration', 0, 'Pentaho local');
INSERT INTO `r_jobentry_attribute` VALUES (63, 1, 4, 0, 'pass_all_parameters', 0, 'Y');

-- ----------------------------
-- Table structure for r_jobentry_copy
-- ----------------------------
DROP TABLE IF EXISTS `r_jobentry_copy`;
CREATE TABLE `r_jobentry_copy`  (
  `ID_JOBENTRY_COPY` bigint(20) NOT NULL,
  `ID_JOBENTRY` int(11) NULL DEFAULT NULL,
  `ID_JOB` int(11) NULL DEFAULT NULL,
  `ID_JOBENTRY_TYPE` int(11) NULL DEFAULT NULL,
  `NR` int(11) NULL DEFAULT NULL,
  `GUI_LOCATION_X` int(11) NULL DEFAULT NULL,
  `GUI_LOCATION_Y` int(11) NULL DEFAULT NULL,
  `GUI_DRAW` tinyint(1) NULL DEFAULT NULL,
  `PARALLEL` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_JOBENTRY_COPY`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_jobentry_copy
-- ----------------------------
INSERT INTO `r_jobentry_copy` VALUES (1, 1, 1, 75, 0, 80, 112, 1, 0);
INSERT INTO `r_jobentry_copy` VALUES (2, 2, 1, 60, 0, 784, 144, 1, 0);
INSERT INTO `r_jobentry_copy` VALUES (3, 3, 1, 56, 0, 496, 128, 1, 0);
INSERT INTO `r_jobentry_copy` VALUES (4, 4, 1, 88, 0, 320, 128, 1, 0);

-- ----------------------------
-- Table structure for r_jobentry_database
-- ----------------------------
DROP TABLE IF EXISTS `r_jobentry_database`;
CREATE TABLE `r_jobentry_database`  (
  `ID_JOB` int(11) NULL DEFAULT NULL,
  `ID_JOBENTRY` int(11) NULL DEFAULT NULL,
  `ID_DATABASE` int(11) NULL DEFAULT NULL,
  INDEX `IDX_RJD1`(`ID_JOB`) USING BTREE,
  INDEX `IDX_RJD2`(`ID_DATABASE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_jobentry_type
-- ----------------------------
DROP TABLE IF EXISTS `r_jobentry_type`;
CREATE TABLE `r_jobentry_type`  (
  `ID_JOBENTRY_TYPE` bigint(20) NOT NULL,
  `CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_JOBENTRY_TYPE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_jobentry_type
-- ----------------------------
INSERT INTO `r_jobentry_type` VALUES (1, 'EMRJobExecutorPlugin', 'Amazon EMR job executor');
INSERT INTO `r_jobentry_type` VALUES (2, 'HiveJobExecutorPlugin', 'Amazon Hive job executor');
INSERT INTO `r_jobentry_type` VALUES (3, 'DataRefineryBuildModel', 'Build model');
INSERT INTO `r_jobentry_type` VALUES (4, 'CHECK_DB_CONNECTIONS', 'Check DB connections');
INSERT INTO `r_jobentry_type` VALUES (5, 'XML_WELL_FORMED', 'Check if XML file is well formed');
INSERT INTO `r_jobentry_type` VALUES (6, 'DOS_UNIX_CONVERTER', 'DOS和UNIX之间的文本转换');
INSERT INTO `r_jobentry_type` VALUES (7, 'DTD_VALIDATOR', 'DTD validator');
INSERT INTO `r_jobentry_type` VALUES (8, 'DummyJob', 'Example job (deprecated)');
INSERT INTO `r_jobentry_type` VALUES (9, 'FTP_PUT', 'FTP 上传');
INSERT INTO `r_jobentry_type` VALUES (10, 'FTP', 'FTP 下载');
INSERT INTO `r_jobentry_type` VALUES (11, 'FTP_DELETE', 'FTP 删除');
INSERT INTO `r_jobentry_type` VALUES (12, 'FTPS_PUT', 'FTPS 上传');
INSERT INTO `r_jobentry_type` VALUES (13, 'FTPS_GET', 'FTPS 下载');
INSERT INTO `r_jobentry_type` VALUES (14, 'GoogleBigQueryStorageLoad', 'Google BigQuery loader');
INSERT INTO `r_jobentry_type` VALUES (15, 'HadoopCopyFilesPlugin', 'Hadoop copy files');
INSERT INTO `r_jobentry_type` VALUES (16, 'HadoopJobExecutorPlugin', 'Hadoop job executor ');
INSERT INTO `r_jobentry_type` VALUES (17, 'HL7MLLPAcknowledge', 'HL7 MLLP acknowledge');
INSERT INTO `r_jobentry_type` VALUES (18, 'HL7MLLPInput', 'HL7 MLLP input');
INSERT INTO `r_jobentry_type` VALUES (19, 'HTTP', 'HTTP');
INSERT INTO `r_jobentry_type` VALUES (20, 'EVAL', 'JavaScript');
INSERT INTO `r_jobentry_type` VALUES (21, 'MS_ACCESS_BULK_LOAD', 'MS Access bulk load (deprecated)');
INSERT INTO `r_jobentry_type` VALUES (22, 'MYSQL_BULK_LOAD', 'MySQL 批量加载');
INSERT INTO `r_jobentry_type` VALUES (23, 'OozieJobExecutor', 'Oozie job executor');
INSERT INTO `r_jobentry_type` VALUES (24, 'PALO_CUBE_CREATE', 'Palo cube create (deprecated)');
INSERT INTO `r_jobentry_type` VALUES (25, 'PALO_CUBE_DELETE', 'Palo cube delete (deprecated)');
INSERT INTO `r_jobentry_type` VALUES (26, 'HadoopTransJobExecutorPlugin', 'Pentaho MapReduce');
INSERT INTO `r_jobentry_type` VALUES (27, 'HadoopPigScriptExecutorPlugin', 'Pig script executor');
INSERT INTO `r_jobentry_type` VALUES (28, 'PING', 'Ping 一台主机');
INSERT INTO `r_jobentry_type` VALUES (29, 'GET_POP', 'POP 收信');
INSERT INTO `r_jobentry_type` VALUES (30, 'DATASOURCE_PUBLISH', 'Publish model');
INSERT INTO `r_jobentry_type` VALUES (31, 'SFTPPUT', 'SFTP 上传');
INSERT INTO `r_jobentry_type` VALUES (32, 'SFTP', 'SFTP 下载');
INSERT INTO `r_jobentry_type` VALUES (33, 'SHELL', 'Shell');
INSERT INTO `r_jobentry_type` VALUES (34, 'SparkSubmit', 'Spark submit');
INSERT INTO `r_jobentry_type` VALUES (35, 'SQL', 'SQL');
INSERT INTO `r_jobentry_type` VALUES (36, 'MSSQL_BULK_LOAD', 'SQLServer 批量加载');
INSERT INTO `r_jobentry_type` VALUES (37, 'SqoopExport', 'Sqoop export');
INSERT INTO `r_jobentry_type` VALUES (38, 'SqoopImport', 'Sqoop import');
INSERT INTO `r_jobentry_type` VALUES (39, 'TALEND_JOB_EXEC', 'Talend 作业执行 (deprecated)');
INSERT INTO `r_jobentry_type` VALUES (40, 'XSD_VALIDATOR', 'XSD validator');
INSERT INTO `r_jobentry_type` VALUES (41, 'XSLT', 'XSL transformation');
INSERT INTO `r_jobentry_type` VALUES (42, 'ZIP_FILE', 'Zip 压缩文件');
INSERT INTO `r_jobentry_type` VALUES (43, 'ABORT', '中止作业');
INSERT INTO `r_jobentry_type` VALUES (44, 'MYSQL_BULK_FILE', '从 MySQL 批量导出到文件');
INSERT INTO `r_jobentry_type` VALUES (45, 'DELETE_RESULT_FILENAMES', '从结果文件中删除文件');
INSERT INTO `r_jobentry_type` VALUES (46, 'JOB', '作业');
INSERT INTO `r_jobentry_type` VALUES (47, 'WRITE_TO_FILE', '写入文件');
INSERT INTO `r_jobentry_type` VALUES (48, 'WRITE_TO_LOG', '写日志');
INSERT INTO `r_jobentry_type` VALUES (49, 'CREATE_FOLDER', '创建一个目录');
INSERT INTO `r_jobentry_type` VALUES (50, 'CREATE_FILE', '创建文件');
INSERT INTO `r_jobentry_type` VALUES (51, 'DELETE_FILE', '删除一个文件');
INSERT INTO `r_jobentry_type` VALUES (52, 'DELETE_FILES', '删除多个文件');
INSERT INTO `r_jobentry_type` VALUES (53, 'DELETE_FOLDERS', '删除目录');
INSERT INTO `r_jobentry_type` VALUES (54, 'SNMP_TRAP', '发送 SNMP 自陷');
INSERT INTO `r_jobentry_type` VALUES (55, 'SEND_NAGIOS_PASSIVE_CHECK', '发送Nagios 被动检查');
INSERT INTO `r_jobentry_type` VALUES (56, 'MAIL', '发送邮件');
INSERT INTO `r_jobentry_type` VALUES (57, 'COPY_MOVE_RESULT_FILENAMES', '复制/移动结果文件');
INSERT INTO `r_jobentry_type` VALUES (58, 'COPY_FILES', '复制文件');
INSERT INTO `r_jobentry_type` VALUES (59, 'EXPORT_REPOSITORY', '导出资源库到XML文件');
INSERT INTO `r_jobentry_type` VALUES (60, 'SUCCESS', '成功');
INSERT INTO `r_jobentry_type` VALUES (61, 'MSGBOX_INFO', '显示消息对话框');
INSERT INTO `r_jobentry_type` VALUES (62, 'WEBSERVICE_AVAILABLE', '检查web服务是否可用');
INSERT INTO `r_jobentry_type` VALUES (63, 'FILE_EXISTS', '检查一个文件是否存在');
INSERT INTO `r_jobentry_type` VALUES (64, 'COLUMNS_EXIST', '检查列是否存在');
INSERT INTO `r_jobentry_type` VALUES (65, 'FILES_EXIST', '检查多个文件是否存在');
INSERT INTO `r_jobentry_type` VALUES (66, 'CHECK_FILES_LOCKED', '检查文件是否被锁');
INSERT INTO `r_jobentry_type` VALUES (67, 'CONNECTED_TO_REPOSITORY', '检查是否连接到资源库');
INSERT INTO `r_jobentry_type` VALUES (68, 'FOLDER_IS_EMPTY', '检查目录是否为空');
INSERT INTO `r_jobentry_type` VALUES (69, 'TABLE_EXISTS', '检查表是否存在');
INSERT INTO `r_jobentry_type` VALUES (70, 'SIMPLE_EVAL', '检验字段的值');
INSERT INTO `r_jobentry_type` VALUES (71, 'FILE_COMPARE', '比较文件');
INSERT INTO `r_jobentry_type` VALUES (72, 'FOLDERS_COMPARE', '比较目录');
INSERT INTO `r_jobentry_type` VALUES (73, 'ADD_RESULT_FILENAMES', '添加文件到结果文件中');
INSERT INTO `r_jobentry_type` VALUES (74, 'TRUNCATE_TABLES', '清空表');
INSERT INTO `r_jobentry_type` VALUES (75, 'SPECIAL', '特殊作业项');
INSERT INTO `r_jobentry_type` VALUES (76, 'SYSLOG', '用 syslog 发送信息');
INSERT INTO `r_jobentry_type` VALUES (77, 'PGP_ENCRYPT_FILES', '用PGP加密文件');
INSERT INTO `r_jobentry_type` VALUES (78, 'PGP_DECRYPT_FILES', '用PGP解密文件');
INSERT INTO `r_jobentry_type` VALUES (79, 'PGP_VERIFY_FILES', '用PGP验证文件签名');
INSERT INTO `r_jobentry_type` VALUES (80, 'MOVE_FILES', '移动文件');
INSERT INTO `r_jobentry_type` VALUES (81, 'DELAY', '等待');
INSERT INTO `r_jobentry_type` VALUES (82, 'WAIT_FOR_SQL', '等待SQL');
INSERT INTO `r_jobentry_type` VALUES (83, 'WAIT_FOR_FILE', '等待文件');
INSERT INTO `r_jobentry_type` VALUES (84, 'UNZIP', '解压缩文件');
INSERT INTO `r_jobentry_type` VALUES (85, 'EVAL_FILES_METRICS', '计算文件大小或个数');
INSERT INTO `r_jobentry_type` VALUES (86, 'EVAL_TABLE_CONTENT', '计算表中的记录数');
INSERT INTO `r_jobentry_type` VALUES (87, 'SET_VARIABLES', '设置变量');
INSERT INTO `r_jobentry_type` VALUES (88, 'TRANS', '转换');
INSERT INTO `r_jobentry_type` VALUES (89, 'TELNET', '远程登录一台主机');
INSERT INTO `r_jobentry_type` VALUES (90, 'MAIL_VALIDATOR', '邮件验证');

-- ----------------------------
-- Table structure for r_log
-- ----------------------------
DROP TABLE IF EXISTS `r_log`;
CREATE TABLE `r_log`  (
  `ID_LOG` bigint(20) NOT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ID_LOGLEVEL` int(11) NULL DEFAULT NULL,
  `LOGTYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FILENAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FILEEXTENTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ADD_DATE` tinyint(1) NULL DEFAULT NULL,
  `ADD_TIME` tinyint(1) NULL DEFAULT NULL,
  `ID_DATABASE_LOG` int(11) NULL DEFAULT NULL,
  `TABLE_NAME_LOG` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_LOG`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_loglevel
-- ----------------------------
DROP TABLE IF EXISTS `r_loglevel`;
CREATE TABLE `r_loglevel`  (
  `ID_LOGLEVEL` bigint(20) NOT NULL,
  `CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_LOGLEVEL`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_loglevel
-- ----------------------------
INSERT INTO `r_loglevel` VALUES (1, 'Error', '错误日志');
INSERT INTO `r_loglevel` VALUES (2, 'Minimal', '最小日志');
INSERT INTO `r_loglevel` VALUES (3, 'Basic', '基本日志');
INSERT INTO `r_loglevel` VALUES (4, 'Detailed', '详细日志');
INSERT INTO `r_loglevel` VALUES (5, 'Debug', '调试');
INSERT INTO `r_loglevel` VALUES (6, 'Rowlevel', '行级日志(非常详细)');

-- ----------------------------
-- Table structure for r_namespace
-- ----------------------------
DROP TABLE IF EXISTS `r_namespace`;
CREATE TABLE `r_namespace`  (
  `ID_NAMESPACE` bigint(20) NOT NULL,
  `NAME` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID_NAMESPACE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_note
-- ----------------------------
DROP TABLE IF EXISTS `r_note`;
CREATE TABLE `r_note`  (
  `ID_NOTE` bigint(20) NOT NULL,
  `VALUE_STR` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `GUI_LOCATION_X` int(11) NULL DEFAULT NULL,
  `GUI_LOCATION_Y` int(11) NULL DEFAULT NULL,
  `GUI_LOCATION_WIDTH` int(11) NULL DEFAULT NULL,
  `GUI_LOCATION_HEIGHT` int(11) NULL DEFAULT NULL,
  `FONT_NAME` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `FONT_SIZE` int(11) NULL DEFAULT NULL,
  `FONT_BOLD` tinyint(1) NULL DEFAULT NULL,
  `FONT_ITALIC` tinyint(1) NULL DEFAULT NULL,
  `FONT_COLOR_RED` int(11) NULL DEFAULT NULL,
  `FONT_COLOR_GREEN` int(11) NULL DEFAULT NULL,
  `FONT_COLOR_BLUE` int(11) NULL DEFAULT NULL,
  `FONT_BACK_GROUND_COLOR_RED` int(11) NULL DEFAULT NULL,
  `FONT_BACK_GROUND_COLOR_GREEN` int(11) NULL DEFAULT NULL,
  `FONT_BACK_GROUND_COLOR_BLUE` int(11) NULL DEFAULT NULL,
  `FONT_BORDER_COLOR_RED` int(11) NULL DEFAULT NULL,
  `FONT_BORDER_COLOR_GREEN` int(11) NULL DEFAULT NULL,
  `FONT_BORDER_COLOR_BLUE` int(11) NULL DEFAULT NULL,
  `DRAW_SHADOW` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_NOTE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_partition
-- ----------------------------
DROP TABLE IF EXISTS `r_partition`;
CREATE TABLE `r_partition`  (
  `ID_PARTITION` bigint(20) NOT NULL,
  `ID_PARTITION_SCHEMA` int(11) NULL DEFAULT NULL,
  `PARTITION_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_PARTITION`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_partition_schema
-- ----------------------------
DROP TABLE IF EXISTS `r_partition_schema`;
CREATE TABLE `r_partition_schema`  (
  `ID_PARTITION_SCHEMA` bigint(20) NOT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DYNAMIC_DEFINITION` tinyint(1) NULL DEFAULT NULL,
  `PARTITIONS_PER_SLAVE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_PARTITION_SCHEMA`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_repository_log
-- ----------------------------
DROP TABLE IF EXISTS `r_repository_log`;
CREATE TABLE `r_repository_log`  (
  `ID_REPOSITORY_LOG` bigint(20) NOT NULL,
  `REP_VERSION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LOG_DATE` datetime(0) NULL DEFAULT NULL,
  `LOG_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERATION_DESC` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID_REPOSITORY_LOG`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_repository_log
-- ----------------------------
INSERT INTO `r_repository_log` VALUES (1, '5.0', '2018-12-27 16:50:56', 'admin', 'Creation of the Kettle repository');
INSERT INTO `r_repository_log` VALUES (2, '5.0', '2018-12-27 16:58:26', 'admin', '创建数据库: mysql:58.246.120.122');
INSERT INTO `r_repository_log` VALUES (3, '5.0', '2018-12-27 16:58:27', 'admin', 'Creation of initial version');
INSERT INTO `r_repository_log` VALUES (4, '5.0', '2018-12-27 17:01:27', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (5, '5.0', '2018-12-27 17:08:28', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (6, '5.0', '2018-12-27 17:14:18', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (7, '5.0', '2018-12-27 17:16:07', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (8, '5.0', '2018-12-27 17:28:16', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (9, '5.0', '2018-12-27 18:06:20', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (10, '5.0', '2018-12-27 18:20:24', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (11, '5.0', '2018-12-27 18:23:15', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (12, '5.0', '2018-12-27 18:25:10', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (13, '5.0', '2018-12-27 20:13:44', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (14, '5.0', '2018-12-27 20:25:41', 'admin', 'save job \'entp_job\'');
INSERT INTO `r_repository_log` VALUES (15, '5.0', '2018-12-27 20:27:59', 'admin', 'save job \'entp_job\'');
INSERT INTO `r_repository_log` VALUES (16, '5.0', '2018-12-27 20:28:24', 'admin', 'save job \'entp_job\'');
INSERT INTO `r_repository_log` VALUES (17, '5.0', '2018-12-27 20:34:43', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (18, '5.0', '2018-12-27 20:43:04', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (19, '5.0', '2018-12-27 20:46:08', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (20, '5.0', '2018-12-27 21:00:04', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (21, '5.0', '2018-12-27 21:01:02', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (22, '5.0', '2018-12-27 21:03:33', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (23, '5.0', '2018-12-27 21:05:04', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (24, '5.0', '2018-12-27 21:08:09', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (25, '5.0', '2018-12-27 21:10:49', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (26, '5.0', '2018-12-27 21:11:47', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (27, '5.0', '2018-12-27 21:12:13', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (28, '5.0', '2018-12-27 21:16:13', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (29, '5.0', '2018-12-27 21:16:38', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (30, '5.0', '2018-12-27 21:30:07', 'admin', 'save job \'entp_job\'');
INSERT INTO `r_repository_log` VALUES (31, '5.0', '2018-12-27 21:46:40', 'admin', 'save transformation \'entp_input\'');
INSERT INTO `r_repository_log` VALUES (32, '5.0', '2018-12-27 21:47:20', 'admin', 'save transformation \'entp_input\'');

-- ----------------------------
-- Table structure for r_slave
-- ----------------------------
DROP TABLE IF EXISTS `r_slave`;
CREATE TABLE `r_slave`  (
  `ID_SLAVE` bigint(20) NOT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `HOST_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PORT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `WEB_APP_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `USERNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PASSWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PROXY_HOST_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PROXY_PORT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NON_PROXY_HOSTS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MASTER` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_SLAVE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_step
-- ----------------------------
DROP TABLE IF EXISTS `r_step`;
CREATE TABLE `r_step`  (
  `ID_STEP` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) NULL DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DESCRIPTION` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `ID_STEP_TYPE` int(11) NULL DEFAULT NULL,
  `DISTRIBUTE` tinyint(1) NULL DEFAULT NULL,
  `COPIES` int(11) NULL DEFAULT NULL,
  `GUI_LOCATION_X` int(11) NULL DEFAULT NULL,
  `GUI_LOCATION_Y` int(11) NULL DEFAULT NULL,
  `GUI_DRAW` tinyint(1) NULL DEFAULT NULL,
  `COPIES_STRING` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_STEP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_step
-- ----------------------------
INSERT INTO `r_step` VALUES (1, 1, '127.0.0.1:entp', NULL, 218, 1, 1, 144, 416, 1, '1');
INSERT INTO `r_step` VALUES (2, 1, '58.246.120.122:entp', NULL, 218, 1, 1, 144, 224, 1, '1');
INSERT INTO `r_step` VALUES (3, 1, '值映射', NULL, 132, 1, 1, 496, 336, 1, '1');
INSERT INTO `r_step` VALUES (4, 1, '删除', NULL, 139, 1, 1, 944, 448, 1, '1');
INSERT INTO `r_step` VALUES (5, 1, '合并记录', NULL, 146, 1, 1, 368, 336, 1, '1');
INSERT INTO `r_step` VALUES (6, 1, '插入', NULL, 169, 1, 1, 1056, 256, 1, '1');
INSERT INTO `r_step` VALUES (7, 1, '更新', NULL, 183, 1, 1, 832, 608, 1, '1');
INSERT INTO `r_step` VALUES (8, 1, '空操作 (什么也不做)', NULL, 204, 1, 1, 672, 144, 1, '1');
INSERT INTO `r_step` VALUES (9, 1, '过滤记录', NULL, 230, 1, 1, 624, 336, 1, '1');
INSERT INTO `r_step` VALUES (10, 1, '过滤记录 2', NULL, 230, 1, 1, 784, 336, 1, '1');
INSERT INTO `r_step` VALUES (11, 1, '过滤记录 3', NULL, 230, 1, 1, 704, 464, 1, '1');
INSERT INTO `r_step` VALUES (12, 1, '过滤记录 4', NULL, 230, 1, 1, 576, 544, 1, '1');

-- ----------------------------
-- Table structure for r_step_attribute
-- ----------------------------
DROP TABLE IF EXISTS `r_step_attribute`;
CREATE TABLE `r_step_attribute`  (
  `ID_STEP_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) NULL DEFAULT NULL,
  `ID_STEP` int(11) NULL DEFAULT NULL,
  `NR` int(11) NULL DEFAULT NULL,
  `CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `VALUE_NUM` bigint(20) NULL DEFAULT NULL,
  `VALUE_STR` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID_STEP_ATTRIBUTE`) USING BTREE,
  UNIQUE INDEX `IDX_RSAT`(`ID_STEP`, `CODE`, `NR`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_step_attribute
-- ----------------------------
INSERT INTO `r_step_attribute` VALUES (1, 1, 1, 0, 'PARTITIONING_SCHEMA', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (2, 1, 1, 0, 'PARTITIONING_METHOD', 0, 'none');
INSERT INTO `r_step_attribute` VALUES (3, 1, 1, 0, 'id_connection', 2, NULL);
INSERT INTO `r_step_attribute` VALUES (4, 1, 1, 0, 'sql', 0, 'select * from db01.t_nimble_entp where del_flg=\'0\' order by SYS_UPD_TMSP');
INSERT INTO `r_step_attribute` VALUES (5, 1, 1, 0, 'limit', 0, '0');
INSERT INTO `r_step_attribute` VALUES (6, 1, 1, 0, 'lookup', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (7, 1, 1, 0, 'execute_each_row', 0, 'N');
INSERT INTO `r_step_attribute` VALUES (8, 1, 1, 0, 'variables_active', 0, 'N');
INSERT INTO `r_step_attribute` VALUES (9, 1, 1, 0, 'lazy_conversion_active', 0, 'N');
INSERT INTO `r_step_attribute` VALUES (10, 1, 1, 0, 'cluster_schema', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (11, 1, 1, 0, 'row_distribution_code', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (12, 1, 2, 0, 'PARTITIONING_SCHEMA', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (13, 1, 2, 0, 'PARTITIONING_METHOD', 0, 'none');
INSERT INTO `r_step_attribute` VALUES (14, 1, 2, 0, 'id_connection', 1, NULL);
INSERT INTO `r_step_attribute` VALUES (15, 1, 2, 0, 'sql', 0, 'select * from cloudentp.t_nimble_entp where del_flg=\'0\' order by SYS_UPD_TMSP');
INSERT INTO `r_step_attribute` VALUES (16, 1, 2, 0, 'limit', 0, '0');
INSERT INTO `r_step_attribute` VALUES (17, 1, 2, 0, 'lookup', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (18, 1, 2, 0, 'execute_each_row', 0, 'N');
INSERT INTO `r_step_attribute` VALUES (19, 1, 2, 0, 'variables_active', 0, 'N');
INSERT INTO `r_step_attribute` VALUES (20, 1, 2, 0, 'lazy_conversion_active', 0, 'N');
INSERT INTO `r_step_attribute` VALUES (21, 1, 2, 0, 'cluster_schema', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (22, 1, 2, 0, 'row_distribution_code', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (23, 1, 3, 0, 'PARTITIONING_SCHEMA', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (24, 1, 3, 0, 'PARTITIONING_METHOD', 0, 'none');
INSERT INTO `r_step_attribute` VALUES (25, 1, 3, 0, 'field_to_use', 0, 'flagfield');
INSERT INTO `r_step_attribute` VALUES (26, 1, 3, 0, 'target_field', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (27, 1, 3, 0, 'non_match_default', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (28, 1, 3, 0, 'source_value', 0, 'deleted');
INSERT INTO `r_step_attribute` VALUES (29, 1, 3, 0, 'target_value', 0, '删除');
INSERT INTO `r_step_attribute` VALUES (30, 1, 3, 1, 'source_value', 0, 'new');
INSERT INTO `r_step_attribute` VALUES (31, 1, 3, 1, 'target_value', 0, '新增');
INSERT INTO `r_step_attribute` VALUES (32, 1, 3, 2, 'source_value', 0, 'changed');
INSERT INTO `r_step_attribute` VALUES (33, 1, 3, 2, 'target_value', 0, '修改');
INSERT INTO `r_step_attribute` VALUES (34, 1, 3, 3, 'source_value', 0, 'identical');
INSERT INTO `r_step_attribute` VALUES (35, 1, 3, 3, 'target_value', 0, '不变');
INSERT INTO `r_step_attribute` VALUES (36, 1, 3, 0, 'cluster_schema', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (37, 1, 3, 0, 'row_distribution_code', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (38, 1, 4, 0, 'PARTITIONING_SCHEMA', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (39, 1, 4, 0, 'PARTITIONING_METHOD', 0, 'none');
INSERT INTO `r_step_attribute` VALUES (40, 1, 4, 0, 'id_connection', 2, NULL);
INSERT INTO `r_step_attribute` VALUES (41, 1, 4, 0, 'commit', 0, '100');
INSERT INTO `r_step_attribute` VALUES (42, 1, 4, 0, 'schema', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (43, 1, 4, 0, 'table', 0, 't_nimble_entp');
INSERT INTO `r_step_attribute` VALUES (44, 1, 4, 0, 'key_name', 0, 'ID');
INSERT INTO `r_step_attribute` VALUES (45, 1, 4, 0, 'key_field', 0, 'ID');
INSERT INTO `r_step_attribute` VALUES (46, 1, 4, 0, 'key_condition', 0, '=');
INSERT INTO `r_step_attribute` VALUES (47, 1, 4, 0, 'key_name2', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (48, 1, 4, 0, 'cluster_schema', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (49, 1, 4, 0, 'row_distribution_code', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (50, 1, 5, 0, 'PARTITIONING_SCHEMA', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (51, 1, 5, 0, 'PARTITIONING_METHOD', 0, 'none');
INSERT INTO `r_step_attribute` VALUES (52, 1, 5, 0, 'key_field', 0, 'ID');
INSERT INTO `r_step_attribute` VALUES (53, 1, 5, 0, 'value_field', 0, 'SYS_UPD_TMSP');
INSERT INTO `r_step_attribute` VALUES (54, 1, 5, 0, 'flag_field', 0, 'flagfield');
INSERT INTO `r_step_attribute` VALUES (55, 1, 5, 0, 'reference', 0, '127.0.0.1:entp');
INSERT INTO `r_step_attribute` VALUES (56, 1, 5, 0, 'compare', 0, '58.246.120.122:entp');
INSERT INTO `r_step_attribute` VALUES (57, 1, 5, 0, 'cluster_schema', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (58, 1, 5, 0, 'row_distribution_code', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (59, 1, 6, 0, 'PARTITIONING_SCHEMA', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (60, 1, 6, 0, 'PARTITIONING_METHOD', 0, 'none');
INSERT INTO `r_step_attribute` VALUES (61, 1, 6, 0, 'id_connection', 2, NULL);
INSERT INTO `r_step_attribute` VALUES (62, 1, 6, 0, 'commit', 0, '100');
INSERT INTO `r_step_attribute` VALUES (63, 1, 6, 0, 'schema', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (64, 1, 6, 0, 'table', 0, 't_nimble_entp');
INSERT INTO `r_step_attribute` VALUES (65, 1, 6, 0, 'update_bypassed', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (66, 1, 6, 0, 'key_name', 0, 'ID');
INSERT INTO `r_step_attribute` VALUES (67, 1, 6, 0, 'key_field', 0, 'ID');
INSERT INTO `r_step_attribute` VALUES (68, 1, 6, 0, 'key_condition', 0, '=');
INSERT INTO `r_step_attribute` VALUES (69, 1, 6, 0, 'key_name2', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (70, 1, 6, 0, 'value_name', 0, 'ID');
INSERT INTO `r_step_attribute` VALUES (71, 1, 6, 0, 'value_rename', 0, 'ID');
INSERT INTO `r_step_attribute` VALUES (72, 1, 6, 0, 'value_update', 0, 'N');
INSERT INTO `r_step_attribute` VALUES (73, 1, 6, 1, 'value_name', 0, 'PARENT_ENTP_ID');
INSERT INTO `r_step_attribute` VALUES (74, 1, 6, 1, 'value_rename', 0, 'PARENT_ENTP_ID');
INSERT INTO `r_step_attribute` VALUES (75, 1, 6, 1, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (76, 1, 6, 2, 'value_name', 0, 'ENTP_NO');
INSERT INTO `r_step_attribute` VALUES (77, 1, 6, 2, 'value_rename', 0, 'ENTP_NO');
INSERT INTO `r_step_attribute` VALUES (78, 1, 6, 2, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (79, 1, 6, 3, 'value_name', 0, 'ENTP_NAME');
INSERT INTO `r_step_attribute` VALUES (80, 1, 6, 3, 'value_rename', 0, 'ENTP_NAME');
INSERT INTO `r_step_attribute` VALUES (81, 1, 6, 3, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (82, 1, 6, 4, 'value_name', 0, 'ENTP_NATURE');
INSERT INTO `r_step_attribute` VALUES (83, 1, 6, 4, 'value_rename', 0, 'ENTP_NATURE');
INSERT INTO `r_step_attribute` VALUES (84, 1, 6, 4, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (85, 1, 6, 5, 'value_name', 0, 'ENTP_TYPE');
INSERT INTO `r_step_attribute` VALUES (86, 1, 6, 5, 'value_rename', 0, 'ENTP_TYPE');
INSERT INTO `r_step_attribute` VALUES (87, 1, 6, 5, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (88, 1, 6, 6, 'value_name', 0, 'SUPPLIER_NATURE');
INSERT INTO `r_step_attribute` VALUES (89, 1, 6, 6, 'value_rename', 0, 'SUPPLIER_NATURE');
INSERT INTO `r_step_attribute` VALUES (90, 1, 6, 6, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (91, 1, 6, 7, 'value_name', 0, 'REG_ADDRESS');
INSERT INTO `r_step_attribute` VALUES (92, 1, 6, 7, 'value_rename', 0, 'REG_ADDRESS');
INSERT INTO `r_step_attribute` VALUES (93, 1, 6, 7, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (94, 1, 6, 8, 'value_name', 0, 'CONTACT_PERSON');
INSERT INTO `r_step_attribute` VALUES (95, 1, 6, 8, 'value_rename', 0, 'CONTACT_PERSON');
INSERT INTO `r_step_attribute` VALUES (96, 1, 6, 8, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (97, 1, 6, 9, 'value_name', 0, 'CONTACT_DUTIES');
INSERT INTO `r_step_attribute` VALUES (98, 1, 6, 9, 'value_rename', 0, 'CONTACT_DUTIES');
INSERT INTO `r_step_attribute` VALUES (99, 1, 6, 9, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (100, 1, 6, 10, 'value_name', 0, 'CONTACT_DETAIL');
INSERT INTO `r_step_attribute` VALUES (101, 1, 6, 10, 'value_rename', 0, 'CONTACT_DETAIL');
INSERT INTO `r_step_attribute` VALUES (102, 1, 6, 10, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (103, 1, 6, 11, 'value_name', 0, 'CONTACT_FAX');
INSERT INTO `r_step_attribute` VALUES (104, 1, 6, 11, 'value_rename', 0, 'CONTACT_FAX');
INSERT INTO `r_step_attribute` VALUES (105, 1, 6, 11, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (106, 1, 6, 12, 'value_name', 0, 'COUNTRY_ID');
INSERT INTO `r_step_attribute` VALUES (107, 1, 6, 12, 'value_rename', 0, 'COUNTRY_ID');
INSERT INTO `r_step_attribute` VALUES (108, 1, 6, 12, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (109, 1, 6, 13, 'value_name', 0, 'PROVINCE_ID');
INSERT INTO `r_step_attribute` VALUES (110, 1, 6, 13, 'value_rename', 0, 'PROVINCE_ID');
INSERT INTO `r_step_attribute` VALUES (111, 1, 6, 13, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (112, 1, 6, 14, 'value_name', 0, 'CITY_ID');
INSERT INTO `r_step_attribute` VALUES (113, 1, 6, 14, 'value_rename', 0, 'CITY_ID');
INSERT INTO `r_step_attribute` VALUES (114, 1, 6, 14, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (115, 1, 6, 15, 'value_name', 0, 'AREA_ID');
INSERT INTO `r_step_attribute` VALUES (116, 1, 6, 15, 'value_rename', 0, 'AREA_ID');
INSERT INTO `r_step_attribute` VALUES (117, 1, 6, 15, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (118, 1, 6, 16, 'value_name', 0, 'ENTP_ADDRESS');
INSERT INTO `r_step_attribute` VALUES (119, 1, 6, 16, 'value_rename', 0, 'ENTP_ADDRESS');
INSERT INTO `r_step_attribute` VALUES (120, 1, 6, 16, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (121, 1, 6, 17, 'value_name', 0, 'FIELD_LATITUDE');
INSERT INTO `r_step_attribute` VALUES (122, 1, 6, 17, 'value_rename', 0, 'FIELD_LATITUDE');
INSERT INTO `r_step_attribute` VALUES (123, 1, 6, 17, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (124, 1, 6, 18, 'value_name', 0, 'FIELD_LONGITUDE');
INSERT INTO `r_step_attribute` VALUES (125, 1, 6, 18, 'value_rename', 0, 'FIELD_LONGITUDE');
INSERT INTO `r_step_attribute` VALUES (126, 1, 6, 18, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (127, 1, 6, 19, 'value_name', 0, 'ENTP_POSTCODE');
INSERT INTO `r_step_attribute` VALUES (128, 1, 6, 19, 'value_rename', 0, 'ENTP_POSTCODE');
INSERT INTO `r_step_attribute` VALUES (129, 1, 6, 19, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (130, 1, 6, 20, 'value_name', 0, 'BIZ_REG_NUMBER');
INSERT INTO `r_step_attribute` VALUES (131, 1, 6, 20, 'value_rename', 0, 'BIZ_REG_NUMBER');
INSERT INTO `r_step_attribute` VALUES (132, 1, 6, 20, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (133, 1, 6, 21, 'value_name', 0, 'LEGAL_REPRESENTATIVE');
INSERT INTO `r_step_attribute` VALUES (134, 1, 6, 21, 'value_rename', 0, 'LEGAL_REPRESENTATIVE');
INSERT INTO `r_step_attribute` VALUES (135, 1, 6, 21, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (136, 1, 6, 22, 'value_name', 0, 'ESTABLISHMENT_DATE');
INSERT INTO `r_step_attribute` VALUES (137, 1, 6, 22, 'value_rename', 0, 'ESTABLISHMENT_DATE');
INSERT INTO `r_step_attribute` VALUES (138, 1, 6, 22, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (139, 1, 6, 23, 'value_name', 0, 'REGISTERED_CAPITAL');
INSERT INTO `r_step_attribute` VALUES (140, 1, 6, 23, 'value_rename', 0, 'REGISTERED_CAPITAL');
INSERT INTO `r_step_attribute` VALUES (141, 1, 6, 23, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (142, 1, 6, 24, 'value_name', 0, 'ENTP_WEBSITE');
INSERT INTO `r_step_attribute` VALUES (143, 1, 6, 24, 'value_rename', 0, 'ENTP_WEBSITE');
INSERT INTO `r_step_attribute` VALUES (144, 1, 6, 24, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (145, 1, 6, 25, 'value_name', 0, 'ENTP_INTRO');
INSERT INTO `r_step_attribute` VALUES (146, 1, 6, 25, 'value_rename', 0, 'ENTP_INTRO');
INSERT INTO `r_step_attribute` VALUES (147, 1, 6, 25, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (148, 1, 6, 26, 'value_name', 0, 'ENTP_WECHAT');
INSERT INTO `r_step_attribute` VALUES (149, 1, 6, 26, 'value_rename', 0, 'ENTP_WECHAT');
INSERT INTO `r_step_attribute` VALUES (150, 1, 6, 26, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (151, 1, 6, 27, 'value_name', 0, 'MARKET_ID');
INSERT INTO `r_step_attribute` VALUES (152, 1, 6, 27, 'value_rename', 0, 'MARKET_ID');
INSERT INTO `r_step_attribute` VALUES (153, 1, 6, 27, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (154, 1, 6, 28, 'value_name', 0, 'SYSTEM_RESERVE1');
INSERT INTO `r_step_attribute` VALUES (155, 1, 6, 28, 'value_rename', 0, 'SYSTEM_RESERVE1');
INSERT INTO `r_step_attribute` VALUES (156, 1, 6, 28, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (157, 1, 6, 29, 'value_name', 0, 'SYSTEM_RESERVE2');
INSERT INTO `r_step_attribute` VALUES (158, 1, 6, 29, 'value_rename', 0, 'SYSTEM_RESERVE2');
INSERT INTO `r_step_attribute` VALUES (159, 1, 6, 29, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (160, 1, 6, 30, 'value_name', 0, 'SYSTEM_RESERVE3');
INSERT INTO `r_step_attribute` VALUES (161, 1, 6, 30, 'value_rename', 0, 'SYSTEM_RESERVE3');
INSERT INTO `r_step_attribute` VALUES (162, 1, 6, 30, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (163, 1, 6, 31, 'value_name', 0, 'SYSTEM_RESERVE4');
INSERT INTO `r_step_attribute` VALUES (164, 1, 6, 31, 'value_rename', 0, 'SYSTEM_RESERVE4');
INSERT INTO `r_step_attribute` VALUES (165, 1, 6, 31, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (166, 1, 6, 32, 'value_name', 0, 'SYSTEM_RESERVE5');
INSERT INTO `r_step_attribute` VALUES (167, 1, 6, 32, 'value_rename', 0, 'SYSTEM_RESERVE5');
INSERT INTO `r_step_attribute` VALUES (168, 1, 6, 32, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (169, 1, 6, 33, 'value_name', 0, 'DEL_FLG');
INSERT INTO `r_step_attribute` VALUES (170, 1, 6, 33, 'value_rename', 0, 'DEL_FLG');
INSERT INTO `r_step_attribute` VALUES (171, 1, 6, 33, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (172, 1, 6, 34, 'value_name', 0, 'SYS_REG_TMSP');
INSERT INTO `r_step_attribute` VALUES (173, 1, 6, 34, 'value_rename', 0, 'SYS_REG_TMSP');
INSERT INTO `r_step_attribute` VALUES (174, 1, 6, 34, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (175, 1, 6, 35, 'value_name', 0, 'SYS_REG_USR_CD');
INSERT INTO `r_step_attribute` VALUES (176, 1, 6, 35, 'value_rename', 0, 'SYS_REG_USR_CD');
INSERT INTO `r_step_attribute` VALUES (177, 1, 6, 35, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (178, 1, 6, 36, 'value_name', 0, 'SYS_UPD_TMSP');
INSERT INTO `r_step_attribute` VALUES (179, 1, 6, 36, 'value_rename', 0, 'SYS_UPD_TMSP');
INSERT INTO `r_step_attribute` VALUES (180, 1, 6, 36, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (181, 1, 6, 37, 'value_name', 0, 'SYS_UPD_USR_CD');
INSERT INTO `r_step_attribute` VALUES (182, 1, 6, 37, 'value_rename', 0, 'SYS_UPD_USR_CD');
INSERT INTO `r_step_attribute` VALUES (183, 1, 6, 37, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (184, 1, 6, 38, 'value_name', 0, 'ENTP_SHORT_NAME');
INSERT INTO `r_step_attribute` VALUES (185, 1, 6, 38, 'value_rename', 0, 'ENTP_SHORT_NAME');
INSERT INTO `r_step_attribute` VALUES (186, 1, 6, 38, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (187, 1, 6, 39, 'value_name', 0, 'OTHER_ID');
INSERT INTO `r_step_attribute` VALUES (188, 1, 6, 39, 'value_rename', 0, 'OTHER_ID');
INSERT INTO `r_step_attribute` VALUES (189, 1, 6, 39, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (190, 1, 6, 40, 'value_name', 0, 'SYSTEM_ID');
INSERT INTO `r_step_attribute` VALUES (191, 1, 6, 40, 'value_rename', 0, 'SYSTEM_ID');
INSERT INTO `r_step_attribute` VALUES (192, 1, 6, 40, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (193, 1, 6, 41, 'value_name', 0, 'END_DATE');
INSERT INTO `r_step_attribute` VALUES (194, 1, 6, 41, 'value_rename', 0, 'END_DATE');
INSERT INTO `r_step_attribute` VALUES (195, 1, 6, 41, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (196, 1, 6, 42, 'value_name', 0, 'FILE_INFO_ID');
INSERT INTO `r_step_attribute` VALUES (197, 1, 6, 42, 'value_rename', 0, 'FILE_INFO_ID');
INSERT INTO `r_step_attribute` VALUES (198, 1, 6, 42, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (199, 1, 6, 43, 'value_name', 0, 'BAK_DATE');
INSERT INTO `r_step_attribute` VALUES (200, 1, 6, 43, 'value_rename', 0, 'BAK_DATE');
INSERT INTO `r_step_attribute` VALUES (201, 1, 6, 43, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (202, 1, 6, 44, 'value_name', 0, 'trace_flag');
INSERT INTO `r_step_attribute` VALUES (203, 1, 6, 44, 'value_rename', 0, 'trace_flag');
INSERT INTO `r_step_attribute` VALUES (204, 1, 6, 44, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (205, 1, 6, 45, 'value_name', 0, 'STALL_NUM');
INSERT INTO `r_step_attribute` VALUES (206, 1, 6, 45, 'value_rename', 0, 'STALL_NUM');
INSERT INTO `r_step_attribute` VALUES (207, 1, 6, 45, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (208, 1, 6, 46, 'value_name', 0, 'ENTP_QR_CODE');
INSERT INTO `r_step_attribute` VALUES (209, 1, 6, 46, 'value_rename', 0, 'ENTP_QR_CODE');
INSERT INTO `r_step_attribute` VALUES (210, 1, 6, 46, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (211, 1, 6, 47, 'value_name', 0, 'ENTP_TEMPLATE');
INSERT INTO `r_step_attribute` VALUES (212, 1, 6, 47, 'value_rename', 0, 'ENTP_TEMPLATE');
INSERT INTO `r_step_attribute` VALUES (213, 1, 6, 47, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (214, 1, 6, 48, 'value_name', 0, 'PRODUCT_FILE_INFO');
INSERT INTO `r_step_attribute` VALUES (215, 1, 6, 48, 'value_rename', 0, 'PRODUCT_FILE_INFO');
INSERT INTO `r_step_attribute` VALUES (216, 1, 6, 48, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (217, 1, 6, 49, 'value_name', 0, 'email');
INSERT INTO `r_step_attribute` VALUES (218, 1, 6, 49, 'value_rename', 0, 'email');
INSERT INTO `r_step_attribute` VALUES (219, 1, 6, 49, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (220, 1, 6, 50, 'value_name', 0, 'alpay');
INSERT INTO `r_step_attribute` VALUES (221, 1, 6, 50, 'value_rename', 0, 'alpay');
INSERT INTO `r_step_attribute` VALUES (222, 1, 6, 50, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (223, 1, 6, 51, 'value_name', 0, 'wechatpay');
INSERT INTO `r_step_attribute` VALUES (224, 1, 6, 51, 'value_rename', 0, 'wechatpay');
INSERT INTO `r_step_attribute` VALUES (225, 1, 6, 51, 'value_update', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (226, 1, 6, 0, 'cluster_schema', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (227, 1, 6, 0, 'row_distribution_code', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (228, 1, 7, 0, 'PARTITIONING_SCHEMA', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (229, 1, 7, 0, 'PARTITIONING_METHOD', 0, 'none');
INSERT INTO `r_step_attribute` VALUES (230, 1, 7, 0, 'id_connection', 2, NULL);
INSERT INTO `r_step_attribute` VALUES (231, 1, 7, 0, 'skip_lookup', 0, 'N');
INSERT INTO `r_step_attribute` VALUES (232, 1, 7, 0, 'commit', 0, '100');
INSERT INTO `r_step_attribute` VALUES (233, 1, 7, 0, 'use_batch', 0, 'Y');
INSERT INTO `r_step_attribute` VALUES (234, 1, 7, 0, 'schema', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (235, 1, 7, 0, 'table', 0, 't_nimble_entp');
INSERT INTO `r_step_attribute` VALUES (236, 1, 7, 0, 'error_ignored', 0, 'N');
INSERT INTO `r_step_attribute` VALUES (237, 1, 7, 0, 'ignore_flag_field', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (238, 1, 7, 0, 'key_name', 0, 'ID');
INSERT INTO `r_step_attribute` VALUES (239, 1, 7, 0, 'key_field', 0, 'ID');
INSERT INTO `r_step_attribute` VALUES (240, 1, 7, 0, 'key_condition', 0, '=');
INSERT INTO `r_step_attribute` VALUES (241, 1, 7, 0, 'key_name2', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (242, 1, 7, 0, 'value_name', 0, 'PARENT_ENTP_ID');
INSERT INTO `r_step_attribute` VALUES (243, 1, 7, 0, 'value_rename', 0, 'PARENT_ENTP_ID');
INSERT INTO `r_step_attribute` VALUES (244, 1, 7, 1, 'value_name', 0, 'ENTP_NO');
INSERT INTO `r_step_attribute` VALUES (245, 1, 7, 1, 'value_rename', 0, 'ENTP_NO');
INSERT INTO `r_step_attribute` VALUES (246, 1, 7, 2, 'value_name', 0, 'ENTP_NAME');
INSERT INTO `r_step_attribute` VALUES (247, 1, 7, 2, 'value_rename', 0, 'ENTP_NAME');
INSERT INTO `r_step_attribute` VALUES (248, 1, 7, 3, 'value_name', 0, 'ENTP_NATURE');
INSERT INTO `r_step_attribute` VALUES (249, 1, 7, 3, 'value_rename', 0, 'ENTP_NATURE');
INSERT INTO `r_step_attribute` VALUES (250, 1, 7, 4, 'value_name', 0, 'ENTP_TYPE');
INSERT INTO `r_step_attribute` VALUES (251, 1, 7, 4, 'value_rename', 0, 'ENTP_TYPE');
INSERT INTO `r_step_attribute` VALUES (252, 1, 7, 5, 'value_name', 0, 'SUPPLIER_NATURE');
INSERT INTO `r_step_attribute` VALUES (253, 1, 7, 5, 'value_rename', 0, 'SUPPLIER_NATURE');
INSERT INTO `r_step_attribute` VALUES (254, 1, 7, 6, 'value_name', 0, 'REG_ADDRESS');
INSERT INTO `r_step_attribute` VALUES (255, 1, 7, 6, 'value_rename', 0, 'REG_ADDRESS');
INSERT INTO `r_step_attribute` VALUES (256, 1, 7, 7, 'value_name', 0, 'CONTACT_PERSON');
INSERT INTO `r_step_attribute` VALUES (257, 1, 7, 7, 'value_rename', 0, 'CONTACT_PERSON');
INSERT INTO `r_step_attribute` VALUES (258, 1, 7, 8, 'value_name', 0, 'CONTACT_DUTIES');
INSERT INTO `r_step_attribute` VALUES (259, 1, 7, 8, 'value_rename', 0, 'CONTACT_DUTIES');
INSERT INTO `r_step_attribute` VALUES (260, 1, 7, 9, 'value_name', 0, 'CONTACT_DETAIL');
INSERT INTO `r_step_attribute` VALUES (261, 1, 7, 9, 'value_rename', 0, 'CONTACT_DETAIL');
INSERT INTO `r_step_attribute` VALUES (262, 1, 7, 10, 'value_name', 0, 'CONTACT_FAX');
INSERT INTO `r_step_attribute` VALUES (263, 1, 7, 10, 'value_rename', 0, 'CONTACT_FAX');
INSERT INTO `r_step_attribute` VALUES (264, 1, 7, 11, 'value_name', 0, 'COUNTRY_ID');
INSERT INTO `r_step_attribute` VALUES (265, 1, 7, 11, 'value_rename', 0, 'COUNTRY_ID');
INSERT INTO `r_step_attribute` VALUES (266, 1, 7, 12, 'value_name', 0, 'PROVINCE_ID');
INSERT INTO `r_step_attribute` VALUES (267, 1, 7, 12, 'value_rename', 0, 'PROVINCE_ID');
INSERT INTO `r_step_attribute` VALUES (268, 1, 7, 13, 'value_name', 0, 'CITY_ID');
INSERT INTO `r_step_attribute` VALUES (269, 1, 7, 13, 'value_rename', 0, 'CITY_ID');
INSERT INTO `r_step_attribute` VALUES (270, 1, 7, 14, 'value_name', 0, 'AREA_ID');
INSERT INTO `r_step_attribute` VALUES (271, 1, 7, 14, 'value_rename', 0, 'AREA_ID');
INSERT INTO `r_step_attribute` VALUES (272, 1, 7, 15, 'value_name', 0, 'ENTP_ADDRESS');
INSERT INTO `r_step_attribute` VALUES (273, 1, 7, 15, 'value_rename', 0, 'ENTP_ADDRESS');
INSERT INTO `r_step_attribute` VALUES (274, 1, 7, 16, 'value_name', 0, 'FIELD_LATITUDE');
INSERT INTO `r_step_attribute` VALUES (275, 1, 7, 16, 'value_rename', 0, 'FIELD_LATITUDE');
INSERT INTO `r_step_attribute` VALUES (276, 1, 7, 17, 'value_name', 0, 'FIELD_LONGITUDE');
INSERT INTO `r_step_attribute` VALUES (277, 1, 7, 17, 'value_rename', 0, 'FIELD_LONGITUDE');
INSERT INTO `r_step_attribute` VALUES (278, 1, 7, 18, 'value_name', 0, 'ENTP_POSTCODE');
INSERT INTO `r_step_attribute` VALUES (279, 1, 7, 18, 'value_rename', 0, 'ENTP_POSTCODE');
INSERT INTO `r_step_attribute` VALUES (280, 1, 7, 19, 'value_name', 0, 'BIZ_REG_NUMBER');
INSERT INTO `r_step_attribute` VALUES (281, 1, 7, 19, 'value_rename', 0, 'BIZ_REG_NUMBER');
INSERT INTO `r_step_attribute` VALUES (282, 1, 7, 20, 'value_name', 0, 'LEGAL_REPRESENTATIVE');
INSERT INTO `r_step_attribute` VALUES (283, 1, 7, 20, 'value_rename', 0, 'LEGAL_REPRESENTATIVE');
INSERT INTO `r_step_attribute` VALUES (284, 1, 7, 21, 'value_name', 0, 'ESTABLISHMENT_DATE');
INSERT INTO `r_step_attribute` VALUES (285, 1, 7, 21, 'value_rename', 0, 'ESTABLISHMENT_DATE');
INSERT INTO `r_step_attribute` VALUES (286, 1, 7, 22, 'value_name', 0, 'REGISTERED_CAPITAL');
INSERT INTO `r_step_attribute` VALUES (287, 1, 7, 22, 'value_rename', 0, 'REGISTERED_CAPITAL');
INSERT INTO `r_step_attribute` VALUES (288, 1, 7, 23, 'value_name', 0, 'ENTP_WEBSITE');
INSERT INTO `r_step_attribute` VALUES (289, 1, 7, 23, 'value_rename', 0, 'ENTP_WEBSITE');
INSERT INTO `r_step_attribute` VALUES (290, 1, 7, 24, 'value_name', 0, 'ENTP_INTRO');
INSERT INTO `r_step_attribute` VALUES (291, 1, 7, 24, 'value_rename', 0, 'ENTP_INTRO');
INSERT INTO `r_step_attribute` VALUES (292, 1, 7, 25, 'value_name', 0, 'ENTP_WECHAT');
INSERT INTO `r_step_attribute` VALUES (293, 1, 7, 25, 'value_rename', 0, 'ENTP_WECHAT');
INSERT INTO `r_step_attribute` VALUES (294, 1, 7, 26, 'value_name', 0, 'MARKET_ID');
INSERT INTO `r_step_attribute` VALUES (295, 1, 7, 26, 'value_rename', 0, 'MARKET_ID');
INSERT INTO `r_step_attribute` VALUES (296, 1, 7, 27, 'value_name', 0, 'SYSTEM_RESERVE1');
INSERT INTO `r_step_attribute` VALUES (297, 1, 7, 27, 'value_rename', 0, 'SYSTEM_RESERVE1');
INSERT INTO `r_step_attribute` VALUES (298, 1, 7, 28, 'value_name', 0, 'SYSTEM_RESERVE2');
INSERT INTO `r_step_attribute` VALUES (299, 1, 7, 28, 'value_rename', 0, 'SYSTEM_RESERVE2');
INSERT INTO `r_step_attribute` VALUES (300, 1, 7, 29, 'value_name', 0, 'SYSTEM_RESERVE3');
INSERT INTO `r_step_attribute` VALUES (301, 1, 7, 29, 'value_rename', 0, 'SYSTEM_RESERVE3');
INSERT INTO `r_step_attribute` VALUES (302, 1, 7, 30, 'value_name', 0, 'SYSTEM_RESERVE4');
INSERT INTO `r_step_attribute` VALUES (303, 1, 7, 30, 'value_rename', 0, 'SYSTEM_RESERVE4');
INSERT INTO `r_step_attribute` VALUES (304, 1, 7, 31, 'value_name', 0, 'SYSTEM_RESERVE5');
INSERT INTO `r_step_attribute` VALUES (305, 1, 7, 31, 'value_rename', 0, 'SYSTEM_RESERVE5');
INSERT INTO `r_step_attribute` VALUES (306, 1, 7, 32, 'value_name', 0, 'DEL_FLG');
INSERT INTO `r_step_attribute` VALUES (307, 1, 7, 32, 'value_rename', 0, 'DEL_FLG');
INSERT INTO `r_step_attribute` VALUES (308, 1, 7, 33, 'value_name', 0, 'SYS_REG_TMSP');
INSERT INTO `r_step_attribute` VALUES (309, 1, 7, 33, 'value_rename', 0, 'SYS_REG_TMSP');
INSERT INTO `r_step_attribute` VALUES (310, 1, 7, 34, 'value_name', 0, 'SYS_REG_USR_CD');
INSERT INTO `r_step_attribute` VALUES (311, 1, 7, 34, 'value_rename', 0, 'SYS_REG_USR_CD');
INSERT INTO `r_step_attribute` VALUES (312, 1, 7, 35, 'value_name', 0, 'SYS_UPD_TMSP');
INSERT INTO `r_step_attribute` VALUES (313, 1, 7, 35, 'value_rename', 0, 'SYS_UPD_TMSP');
INSERT INTO `r_step_attribute` VALUES (314, 1, 7, 36, 'value_name', 0, 'SYS_UPD_USR_CD');
INSERT INTO `r_step_attribute` VALUES (315, 1, 7, 36, 'value_rename', 0, 'SYS_UPD_USR_CD');
INSERT INTO `r_step_attribute` VALUES (316, 1, 7, 37, 'value_name', 0, 'ENTP_SHORT_NAME');
INSERT INTO `r_step_attribute` VALUES (317, 1, 7, 37, 'value_rename', 0, 'ENTP_SHORT_NAME');
INSERT INTO `r_step_attribute` VALUES (318, 1, 7, 38, 'value_name', 0, 'OTHER_ID');
INSERT INTO `r_step_attribute` VALUES (319, 1, 7, 38, 'value_rename', 0, 'OTHER_ID');
INSERT INTO `r_step_attribute` VALUES (320, 1, 7, 39, 'value_name', 0, 'SYSTEM_ID');
INSERT INTO `r_step_attribute` VALUES (321, 1, 7, 39, 'value_rename', 0, 'SYSTEM_ID');
INSERT INTO `r_step_attribute` VALUES (322, 1, 7, 40, 'value_name', 0, 'END_DATE');
INSERT INTO `r_step_attribute` VALUES (323, 1, 7, 40, 'value_rename', 0, 'END_DATE');
INSERT INTO `r_step_attribute` VALUES (324, 1, 7, 41, 'value_name', 0, 'FILE_INFO_ID');
INSERT INTO `r_step_attribute` VALUES (325, 1, 7, 41, 'value_rename', 0, 'FILE_INFO_ID');
INSERT INTO `r_step_attribute` VALUES (326, 1, 7, 42, 'value_name', 0, 'BAK_DATE');
INSERT INTO `r_step_attribute` VALUES (327, 1, 7, 42, 'value_rename', 0, 'BAK_DATE');
INSERT INTO `r_step_attribute` VALUES (328, 1, 7, 43, 'value_name', 0, 'trace_flag');
INSERT INTO `r_step_attribute` VALUES (329, 1, 7, 43, 'value_rename', 0, 'trace_flag');
INSERT INTO `r_step_attribute` VALUES (330, 1, 7, 44, 'value_name', 0, 'STALL_NUM');
INSERT INTO `r_step_attribute` VALUES (331, 1, 7, 44, 'value_rename', 0, 'STALL_NUM');
INSERT INTO `r_step_attribute` VALUES (332, 1, 7, 45, 'value_name', 0, 'ENTP_QR_CODE');
INSERT INTO `r_step_attribute` VALUES (333, 1, 7, 45, 'value_rename', 0, 'ENTP_QR_CODE');
INSERT INTO `r_step_attribute` VALUES (334, 1, 7, 46, 'value_name', 0, 'ENTP_TEMPLATE');
INSERT INTO `r_step_attribute` VALUES (335, 1, 7, 46, 'value_rename', 0, 'ENTP_TEMPLATE');
INSERT INTO `r_step_attribute` VALUES (336, 1, 7, 47, 'value_name', 0, 'PRODUCT_FILE_INFO');
INSERT INTO `r_step_attribute` VALUES (337, 1, 7, 47, 'value_rename', 0, 'PRODUCT_FILE_INFO');
INSERT INTO `r_step_attribute` VALUES (338, 1, 7, 48, 'value_name', 0, 'email');
INSERT INTO `r_step_attribute` VALUES (339, 1, 7, 48, 'value_rename', 0, 'email');
INSERT INTO `r_step_attribute` VALUES (340, 1, 7, 49, 'value_name', 0, 'alpay');
INSERT INTO `r_step_attribute` VALUES (341, 1, 7, 49, 'value_rename', 0, 'alpay');
INSERT INTO `r_step_attribute` VALUES (342, 1, 7, 50, 'value_name', 0, 'wechatpay');
INSERT INTO `r_step_attribute` VALUES (343, 1, 7, 50, 'value_rename', 0, 'wechatpay');
INSERT INTO `r_step_attribute` VALUES (344, 1, 7, 0, 'cluster_schema', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (345, 1, 7, 0, 'row_distribution_code', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (346, 1, 8, 0, 'PARTITIONING_SCHEMA', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (347, 1, 8, 0, 'PARTITIONING_METHOD', 0, 'none');
INSERT INTO `r_step_attribute` VALUES (348, 1, 8, 0, 'cluster_schema', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (349, 1, 8, 0, 'row_distribution_code', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (350, 1, 9, 0, 'PARTITIONING_SCHEMA', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (351, 1, 9, 0, 'PARTITIONING_METHOD', 0, 'none');
INSERT INTO `r_step_attribute` VALUES (352, 1, 9, 0, 'id_condition', 1, NULL);
INSERT INTO `r_step_attribute` VALUES (353, 1, 9, 0, 'send_true_to', 0, '空操作 (什么也不做)');
INSERT INTO `r_step_attribute` VALUES (354, 1, 9, 0, 'send_false_to', 0, '过滤记录 2');
INSERT INTO `r_step_attribute` VALUES (355, 1, 9, 0, 'cluster_schema', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (356, 1, 9, 0, 'row_distribution_code', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (357, 1, 10, 0, 'PARTITIONING_SCHEMA', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (358, 1, 10, 0, 'PARTITIONING_METHOD', 0, 'none');
INSERT INTO `r_step_attribute` VALUES (359, 1, 10, 0, 'id_condition', 2, NULL);
INSERT INTO `r_step_attribute` VALUES (360, 1, 10, 0, 'send_true_to', 0, '插入');
INSERT INTO `r_step_attribute` VALUES (361, 1, 10, 0, 'send_false_to', 0, '过滤记录 3');
INSERT INTO `r_step_attribute` VALUES (362, 1, 10, 0, 'cluster_schema', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (363, 1, 10, 0, 'row_distribution_code', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (364, 1, 11, 0, 'PARTITIONING_SCHEMA', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (365, 1, 11, 0, 'PARTITIONING_METHOD', 0, 'none');
INSERT INTO `r_step_attribute` VALUES (366, 1, 11, 0, 'id_condition', 3, NULL);
INSERT INTO `r_step_attribute` VALUES (367, 1, 11, 0, 'send_true_to', 0, '删除');
INSERT INTO `r_step_attribute` VALUES (368, 1, 11, 0, 'send_false_to', 0, '过滤记录 4');
INSERT INTO `r_step_attribute` VALUES (369, 1, 11, 0, 'cluster_schema', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (370, 1, 11, 0, 'row_distribution_code', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (371, 1, 12, 0, 'PARTITIONING_SCHEMA', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (372, 1, 12, 0, 'PARTITIONING_METHOD', 0, 'none');
INSERT INTO `r_step_attribute` VALUES (373, 1, 12, 0, 'id_condition', 4, NULL);
INSERT INTO `r_step_attribute` VALUES (374, 1, 12, 0, 'send_true_to', 0, '更新');
INSERT INTO `r_step_attribute` VALUES (375, 1, 12, 0, 'send_false_to', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (376, 1, 12, 0, 'cluster_schema', 0, NULL);
INSERT INTO `r_step_attribute` VALUES (377, 1, 12, 0, 'row_distribution_code', 0, NULL);

-- ----------------------------
-- Table structure for r_step_database
-- ----------------------------
DROP TABLE IF EXISTS `r_step_database`;
CREATE TABLE `r_step_database`  (
  `ID_TRANSFORMATION` int(11) NULL DEFAULT NULL,
  `ID_STEP` int(11) NULL DEFAULT NULL,
  `ID_DATABASE` int(11) NULL DEFAULT NULL,
  INDEX `IDX_RSD1`(`ID_TRANSFORMATION`) USING BTREE,
  INDEX `IDX_RSD2`(`ID_DATABASE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_step_database
-- ----------------------------
INSERT INTO `r_step_database` VALUES (1, 1, 2);
INSERT INTO `r_step_database` VALUES (1, 2, 1);
INSERT INTO `r_step_database` VALUES (1, 4, 2);
INSERT INTO `r_step_database` VALUES (1, 6, 2);
INSERT INTO `r_step_database` VALUES (1, 7, 2);

-- ----------------------------
-- Table structure for r_step_type
-- ----------------------------
DROP TABLE IF EXISTS `r_step_type`;
CREATE TABLE `r_step_type`  (
  `ID_STEP_TYPE` bigint(20) NOT NULL,
  `CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `HELPTEXT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_STEP_TYPE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_step_type
-- ----------------------------
INSERT INTO `r_step_type` VALUES (1, 'AccessInput', 'Access 输入', 'Read data from a Microsoft Access file');
INSERT INTO `r_step_type` VALUES (2, 'AccessOutput', 'Access 输出', 'Stores records into an MS-Access database table.');
INSERT INTO `r_step_type` VALUES (3, 'CheckSum', 'Add a checksum', 'Add a checksum column for each input row');
INSERT INTO `r_step_type` VALUES (4, 'AddXML', 'Add XML', 'Encode several fields into an XML fragment');
INSERT INTO `r_step_type` VALUES (5, 'FieldMetadataAnnotation', 'Annotate stream', 'Add more details to describe data for published models used by the Streamlined Data Refinery.');
INSERT INTO `r_step_type` VALUES (6, 'AvroInput', 'Avro input', 'Reads data from an Avro file');
INSERT INTO `r_step_type` VALUES (7, 'AvroInputNew', 'Avro input', 'Reads data from Avro file');
INSERT INTO `r_step_type` VALUES (8, 'AvroOutput', 'Avro output', 'Writes data to an Avro file according to a mapping');
INSERT INTO `r_step_type` VALUES (9, 'BlockingStep', 'Blocking step', 'The Blocking step blocks all output until the very last row is received from the previous step.');
INSERT INTO `r_step_type` VALUES (10, 'CallEndpointStep', 'Call endpoint', 'Call an endpoint of the Pentaho Server.');
INSERT INTO `r_step_type` VALUES (11, 'CassandraInput', 'Cassandra input', 'Reads data from a Cassandra table');
INSERT INTO `r_step_type` VALUES (12, 'CassandraOutput', 'Cassandra output', 'Writes to a Cassandra table');
INSERT INTO `r_step_type` VALUES (13, 'ChangeFileEncoding', 'Change file encoding', 'Change file encoding and create a new file');
INSERT INTO `r_step_type` VALUES (14, 'CloneRow', 'Clone row', 'Clone a row as many times as needed');
INSERT INTO `r_step_type` VALUES (15, 'ClosureGenerator', 'Closure generator', 'This step allows you to generates a closure table using parent-child relationships.');
INSERT INTO `r_step_type` VALUES (16, 'ColumnExists', 'Column exists', 'Check if a column exists');
INSERT INTO `r_step_type` VALUES (17, 'ConcatFields', 'Concat fields', 'Concat fields together into a new field (similar to the Text File Output step)');
INSERT INTO `r_step_type` VALUES (18, 'CouchDbInput', 'CouchDB input', 'Reads from a Couch DB view');
INSERT INTO `r_step_type` VALUES (19, 'CsvInput', 'CSV文件输入', 'Simple CSV file input');
INSERT INTO `r_step_type` VALUES (20, 'CubeInput', 'Cube 文件输入', '从一个cube读取记录.');
INSERT INTO `r_step_type` VALUES (21, 'CubeOutput', 'Cube输出', '把数据写入一个cube');
INSERT INTO `r_step_type` VALUES (22, 'TypeExitEdi2XmlStep', 'EDI to XML', 'Converts Edi text to generic XML');
INSERT INTO `r_step_type` VALUES (23, 'ElasticSearchBulk', 'Elasticsearch bulk insert', 'Performs bulk inserts into ElasticSearch');
INSERT INTO `r_step_type` VALUES (24, 'ShapeFileReader', 'ESRI shapefile reader', 'Reads shape file data from an ESRI shape file and linked DBF file');
INSERT INTO `r_step_type` VALUES (25, 'MetaInject', 'ETL metadata injection', 'ETL元数据注入');
INSERT INTO `r_step_type` VALUES (26, 'DummyStep', 'Example step', 'This is a plugin example step');
INSERT INTO `r_step_type` VALUES (27, 'ExcelInput', 'Excel输入', '从一个微软的Excel文件里读取数据. 兼容Excel 95, 97 and 2000.');
INSERT INTO `r_step_type` VALUES (28, 'ExcelOutput', 'Excel输出', 'Stores records into an Excel (XLS) document with formatting information.');
INSERT INTO `r_step_type` VALUES (29, 'getXMLData', 'Get data from XML', 'Get data from XML file by using XPath.\n This step also allows you to parse XML defined in a previous field.');
INSERT INTO `r_step_type` VALUES (30, 'GetSlaveSequence', 'Get ID from slave server', 'Retrieves unique IDs in blocks from a slave server.  The referenced sequence needs to be configured on the slave server in the XML configuration file.');
INSERT INTO `r_step_type` VALUES (31, 'RecordsFromStream', 'Get records from stream', 'This step allows you to read records from a streaming step.');
INSERT INTO `r_step_type` VALUES (32, 'GetSessionVariableStep', 'Get session variables', 'Get session variables from the current user session.');
INSERT INTO `r_step_type` VALUES (33, 'TypeExitGoogleAnalyticsInputStep', 'Google Analytics', 'Fetches data from google analytics account');
INSERT INTO `r_step_type` VALUES (34, 'GPBulkLoader', 'Greenplum bulk loader', 'Greenplum bulk loader');
INSERT INTO `r_step_type` VALUES (35, 'GPLoad', 'Greenplum load', 'Greenplum load');
INSERT INTO `r_step_type` VALUES (36, 'ParallelGzipCsvInput', 'GZIP CSV input', 'Parallel GZIP CSV file input reader');
INSERT INTO `r_step_type` VALUES (37, 'HadoopFileInputPlugin', 'Hadoop file input', 'Process files from an HDFS location');
INSERT INTO `r_step_type` VALUES (38, 'HadoopFileOutputPlugin', 'Hadoop file output', 'Create files in an HDFS location ');
INSERT INTO `r_step_type` VALUES (39, 'HBaseInput', 'HBase input', 'Reads data from a HBase table according to a mapping ');
INSERT INTO `r_step_type` VALUES (40, 'HBaseOutput', 'HBase output', 'Writes data to an HBase table according to a mapping');
INSERT INTO `r_step_type` VALUES (41, 'HBaseRowDecoder', 'HBase row decoder', 'Decodes an incoming key and HBase result object according to a mapping ');
INSERT INTO `r_step_type` VALUES (42, 'HL7Input', 'HL7 input', 'Reads and parses HL7 messages and outputs a series of values from the messages');
INSERT INTO `r_step_type` VALUES (43, 'HTTP', 'HTTP client', 'Call a web service over HTTP by supplying a base URL by allowing parameters to be set dynamically');
INSERT INTO `r_step_type` VALUES (44, 'HTTPPOST', 'HTTP post', 'Call a web service request over HTTP by supplying a base URL by allowing parameters to be set dynamically');
INSERT INTO `r_step_type` VALUES (45, 'InfobrightOutput', 'Infobright 批量加载', 'Load data to an Infobright database table');
INSERT INTO `r_step_type` VALUES (46, 'VectorWiseBulkLoader', 'Ingres VectorWise 批量加载', 'This step interfaces with the Ingres VectorWise Bulk Loader \"COPY TABLE\" command.');
INSERT INTO `r_step_type` VALUES (47, 'UserDefinedJavaClass', 'Java 代码', 'This step allows you to program a step using Java code');
INSERT INTO `r_step_type` VALUES (48, 'ScriptValueMod', 'JavaScript代码', 'This is a modified plugin for the Scripting Values with improved interface and performance.\nWritten & donated to open source by Martin Lange, Proconis : http://www.proconis.de');
INSERT INTO `r_step_type` VALUES (49, 'Jms2Consumer', 'JMS consumer', 'Consumes JMS streams');
INSERT INTO `r_step_type` VALUES (50, 'Jms2Producer', 'JMS producer', 'Produces JMS streams');
INSERT INTO `r_step_type` VALUES (51, 'JsonInput', 'JSON input', 'Extract relevant portions out of JSON structures (file or incoming field) and output rows');
INSERT INTO `r_step_type` VALUES (52, 'JsonOutput', 'JSON output', 'Create JSON block and output it in a field or a file.');
INSERT INTO `r_step_type` VALUES (53, 'KafkaConsumerInput', 'Kafka consumer', 'Consume messages from a Kafka topic');
INSERT INTO `r_step_type` VALUES (54, 'KafkaProducerOutput', 'Kafka producer', 'Produce messages to a Kafka topic');
INSERT INTO `r_step_type` VALUES (55, 'LDAPInput', 'LDAP 输入', 'Read data from LDAP host');
INSERT INTO `r_step_type` VALUES (56, 'LDAPOutput', 'LDAP 输出', 'Perform Insert, upsert, update, add or delete operations on records based on their DN (Distinguished  Name).');
INSERT INTO `r_step_type` VALUES (57, 'LDIFInput', 'LDIF 输入', 'Read data from LDIF files');
INSERT INTO `r_step_type` VALUES (58, 'LucidDBStreamingLoader', 'LucidDB streaming loader', 'Load data into LucidDB by using Remote Rows UDX.');
INSERT INTO `r_step_type` VALUES (59, 'HadoopEnterPlugin', 'MapReduce input', 'Enter a Hadoop Mapper or Reducer transformation');
INSERT INTO `r_step_type` VALUES (60, 'HadoopExitPlugin', 'MapReduce output', 'Exit a Hadoop Mapper or Reducer transformation ');
INSERT INTO `r_step_type` VALUES (61, 'TypeExitExcelWriterStep', 'Microsoft Excel 输出', 'Writes or appends data to an Excel file');
INSERT INTO `r_step_type` VALUES (62, 'MondrianInput', 'Mondrian 输入', 'Execute and retrieve data using an MDX query against a Pentaho Analyses OLAP server (Mondrian)');
INSERT INTO `r_step_type` VALUES (63, 'MonetDBAgileMart', 'MonetDB Agile Mart', 'Load data into MonetDB for Agile BI use cases');
INSERT INTO `r_step_type` VALUES (64, 'MonetDBBulkLoader', 'MonetDB 批量加载', 'Load data into MonetDB by using their bulk load command in streaming mode.');
INSERT INTO `r_step_type` VALUES (65, 'MongoDbInput', 'MongoDB input', 'Reads from a Mongo DB collection');
INSERT INTO `r_step_type` VALUES (66, 'MongoDbOutput', 'MongoDB output', 'Writes to a Mongo DB collection');
INSERT INTO `r_step_type` VALUES (67, 'MQTTConsumer', 'MQTT consumer', 'Subscribes and streams an MQTT Topic');
INSERT INTO `r_step_type` VALUES (68, 'MQTTProducer', 'MQTT producer', 'Produce messages to a MQTT Topic');
INSERT INTO `r_step_type` VALUES (69, 'MultiwayMergeJoin', 'Multiway merge join', 'Multiway merge join');
INSERT INTO `r_step_type` VALUES (70, 'MySQLBulkLoader', 'MySQL 批量加载', 'MySQL bulk loader step, loading data over a named pipe (not available on MS Windows)');
INSERT INTO `r_step_type` VALUES (71, 'OlapInput', 'OLAP 输入', 'Execute and retrieve data using an MDX query against any XML/A OLAP datasource using olap4j');
INSERT INTO `r_step_type` VALUES (72, 'OpenERPObjectDelete', 'OpenERP object delete', 'Deletes OpenERP objects');
INSERT INTO `r_step_type` VALUES (73, 'OpenERPObjectInput', 'OpenERP object input', 'Reads data from OpenERP objects');
INSERT INTO `r_step_type` VALUES (74, 'OpenERPObjectOutputImport', 'OpenERP object output', 'Writes data into OpenERP objects using the object import procedure');
INSERT INTO `r_step_type` VALUES (75, 'OraBulkLoader', 'Oracle 批量加载', 'Use Oracle bulk loader to load data');
INSERT INTO `r_step_type` VALUES (76, 'OrcInput', 'ORC input', 'Reads data from ORC file');
INSERT INTO `r_step_type` VALUES (77, 'OrcOutput', 'ORC output', 'Writes data to an Orc file according to a mapping');
INSERT INTO `r_step_type` VALUES (78, 'PaloCellInput', 'Palo cell input', 'Reads data from a defined Palo Cube ');
INSERT INTO `r_step_type` VALUES (79, 'PaloCellOutput', 'Palo cell output', 'Writes data to a defined Palo Cube');
INSERT INTO `r_step_type` VALUES (80, 'PaloDimInput', 'Palo dim input', 'Reads data from a defined Palo Dimension');
INSERT INTO `r_step_type` VALUES (81, 'PaloDimOutput', 'Palo dim output', 'Writes data to defined Palo Dimension');
INSERT INTO `r_step_type` VALUES (82, 'ParquetInput', 'Parquet input', 'Reads data from a Parquet file.');
INSERT INTO `r_step_type` VALUES (83, 'ParquetOutput', 'Parquet output', 'Writes data to a Parquet file according to a mapping.');
INSERT INTO `r_step_type` VALUES (84, 'PentahoReportingOutput', 'Pentaho 报表输出', 'Executes an existing report (PRPT)');
INSERT INTO `r_step_type` VALUES (85, 'PGPDecryptStream', 'PGP decrypt stream', 'Decrypt data stream with PGP');
INSERT INTO `r_step_type` VALUES (86, 'PGPEncryptStream', 'PGP encrypt stream', 'Encrypt data stream with PGP');
INSERT INTO `r_step_type` VALUES (87, 'PGBulkLoader', 'PostgreSQL 批量加载', 'PostgreSQL Bulk Loader');
INSERT INTO `r_step_type` VALUES (88, 'Rest', 'REST client', 'Consume RESTfull services.\nREpresentational State Transfer (REST) is a key design idiom that embraces a stateless client-server\narchitecture in which the web services are viewed as resources and can be identified by their URLs');
INSERT INTO `r_step_type` VALUES (89, 'RssInput', 'RSS 输入', 'Read RSS feeds');
INSERT INTO `r_step_type` VALUES (90, 'RssOutput', 'RSS 输出', 'Read RSS stream.');
INSERT INTO `r_step_type` VALUES (91, 'RuleAccumulator', 'Rules accumulator', 'Rules accumulator step');
INSERT INTO `r_step_type` VALUES (92, 'RuleExecutor', 'Rules executor', 'Rules executor step');
INSERT INTO `r_step_type` VALUES (93, 'S3CSVINPUT', 'S3 CSV input', 'Is capable of reading CSV data stored on Amazon S3 in parallel');
INSERT INTO `r_step_type` VALUES (94, 'S3FileOutputPlugin', 'S3 file output', 'Create files in an S3 location');
INSERT INTO `r_step_type` VALUES (95, 'SalesforceDelete', 'Salesforce delete', 'Delete records in Salesforce module.');
INSERT INTO `r_step_type` VALUES (96, 'SalesforceInput', 'Salesforce input', 'Extract data from Salesforce');
INSERT INTO `r_step_type` VALUES (97, 'SalesforceInsert', 'Salesforce insert', 'Insert records in Salesforce module.');
INSERT INTO `r_step_type` VALUES (98, 'SalesforceUpdate', 'Salesforce update', 'Update records in Salesforce module.');
INSERT INTO `r_step_type` VALUES (99, 'SalesforceUpsert', 'Salesforce upsert', 'Insert or update records in Salesforce module.');
INSERT INTO `r_step_type` VALUES (100, 'SAPINPUT', 'SAP input', 'Read data from SAP ERP, optionally with parameters');
INSERT INTO `r_step_type` VALUES (101, 'SASInput', 'SAS 输入', 'This step reads files in sas7bdat (SAS) native format');
INSERT INTO `r_step_type` VALUES (102, 'Script', 'Script', 'Calculate values by scripting in Ruby, Python, Groovy, JavaScript, ... (JSR-223)');
INSERT INTO `r_step_type` VALUES (103, 'SetSessionVariableStep', 'Set session variables', 'Set session variables in the current user session.');
INSERT INTO `r_step_type` VALUES (104, 'SFTPPut', 'SFTP put', 'Upload a file or a stream file to remote host via SFTP');
INSERT INTO `r_step_type` VALUES (105, 'CreateSharedDimensions', 'Shared dimension', 'Create shared dimensions for use with Streamlined Data Refinery.');
INSERT INTO `r_step_type` VALUES (106, 'SimpleMapping', 'Simple mapping (sub-transformation)', 'Run a mapping (sub-transformation), use MappingInput and MappingOutput to specify the fields interface.  This is the simplified version only allowing one input and one output data set.');
INSERT INTO `r_step_type` VALUES (107, 'SingleThreader', 'Single threader', 'Executes a transformation snippet in a single thread.  You need a standard mapping or a transformation with an Injector step where data from the parent transformation will arive in blocks.');
INSERT INTO `r_step_type` VALUES (108, 'SocketWriter', 'Socket 写', 'Socket writer.  A socket server that can send rows of data to a socket reader.');
INSERT INTO `r_step_type` VALUES (109, 'SocketReader', 'Socket 读', 'Socket reader.  A socket client that connects to a server (Socket Writer step).');
INSERT INTO `r_step_type` VALUES (110, 'SQLFileOutput', 'SQL 文件输出', 'Output SQL INSERT statements to file');
INSERT INTO `r_step_type` VALUES (111, 'SSTableOutput', 'SSTable output', 'Writes to a filesystem directory as a Cassandra SSTable');
INSERT INTO `r_step_type` VALUES (112, 'SwitchCase', 'Switch / case', 'Switch a row to a certain target step based on the case value in a field.');
INSERT INTO `r_step_type` VALUES (113, 'TableAgileMart', 'Table Agile Mart', 'Load data into a table for Agile BI use cases');
INSERT INTO `r_step_type` VALUES (114, 'TeraFast', 'Teradata Fastload 批量加载', 'The Teradata Fastload bulk loader');
INSERT INTO `r_step_type` VALUES (115, 'TeraDataBulkLoader', 'Teradata TPT bulk loader', 'Teradata TPT bulkloader, using tbuild command');
INSERT INTO `r_step_type` VALUES (116, 'OldTextFileInput', 'Text file input', '从一个文本文件（几种格式）里读取数据{0}这些数据可以被传递到下一个步骤里...');
INSERT INTO `r_step_type` VALUES (117, 'TextFileOutputLegacy', 'Text file output', '写记录到一个文本文件.');
INSERT INTO `r_step_type` VALUES (118, 'TransExecutor', 'Transformation executor', 'This step executes a Pentaho Data Integration transformation, sets parameters and passes rows.');
INSERT INTO `r_step_type` VALUES (119, 'VerticaBulkLoader', 'Vertica bulk loader', 'Bulk load data into a Vertica database table');
INSERT INTO `r_step_type` VALUES (120, 'WebServiceLookup', 'Web 服务查询', '使用 Web 服务查询信息');
INSERT INTO `r_step_type` VALUES (121, 'XBaseInput', 'XBase输入', '从一个XBase类型的文件(DBF)读取记录');
INSERT INTO `r_step_type` VALUES (122, 'XMLInputStream', 'XML input stream (StAX)', 'This step is capable of processing very large and complex XML files very fast.');
INSERT INTO `r_step_type` VALUES (123, 'XMLJoin', 'XML join', 'Joins a stream of XML-Tags into a target XML string');
INSERT INTO `r_step_type` VALUES (124, 'XMLOutput', 'XML output', 'Write data to an XML file');
INSERT INTO `r_step_type` VALUES (125, 'XSDValidator', 'XSD validator', 'Validate XML source (files or streams) against XML Schema Definition.');
INSERT INTO `r_step_type` VALUES (126, 'XSLT', 'XSL transformation', 'Make an XSL transformation');
INSERT INTO `r_step_type` VALUES (127, 'YamlInput', 'YAML 输入', 'Read YAML source (file or stream) parse them and convert them to rows and writes these to one or more output.');
INSERT INTO `r_step_type` VALUES (128, 'ZipFile', 'Zip 文件', 'Zip a file.\nFilename will be extracted from incoming stream.');
INSERT INTO `r_step_type` VALUES (129, 'Abort', '中止', 'Abort a transformation');
INSERT INTO `r_step_type` VALUES (130, 'FilesFromResult', '从结果获取文件', 'This step allows you to read filenames used or generated in a previous entry in a job.');
INSERT INTO `r_step_type` VALUES (131, 'RowsFromResult', '从结果获取记录', '这个允许你从同一个任务的前一个条目里读取记录.');
INSERT INTO `r_step_type` VALUES (132, 'ValueMapper', '值映射', 'Maps values of a certain field from one value to another');
INSERT INTO `r_step_type` VALUES (133, 'Formula', '公式', '使用 Pentaho 的公式库来计算公式');
INSERT INTO `r_step_type` VALUES (134, 'WriteToLog', '写日志', 'Write data to log');
INSERT INTO `r_step_type` VALUES (135, 'AnalyticQuery', '分析查询', 'Execute analytic queries over a sorted dataset (LEAD/LAG/FIRST/LAST)');
INSERT INTO `r_step_type` VALUES (136, 'GroupBy', '分组', '以分组的形式创建聚合.{0}这个仅仅在一个已经排好序的输入有效.{1}如果输入没有排序, 仅仅两个连续的记录行被正确处理.');
INSERT INTO `r_step_type` VALUES (137, 'SplitFieldToRows3', '列拆分为多行', 'Splits a single string field by delimiter and creates a new row for each split term');
INSERT INTO `r_step_type` VALUES (138, 'Denormaliser', '列转行', 'Denormalises rows by looking up key-value pairs and by assigning them to new fields in the输出 rows.{0}This method aggregates and needs the输入 rows to be sorted on the grouping fields');
INSERT INTO `r_step_type` VALUES (139, 'Delete', '删除', '基于关键字删除记录');
INSERT INTO `r_step_type` VALUES (140, 'Janino', '利用Janino计算Java表达式', 'Calculate the result of a Java Expression using Janino');
INSERT INTO `r_step_type` VALUES (141, 'StringCut', '剪切字符串', 'Strings cut (substring).');
INSERT INTO `r_step_type` VALUES (142, 'UnivariateStats', '单变量统计', 'This step computes some simple stats based on a single input field');
INSERT INTO `r_step_type` VALUES (143, 'Unique', '去除重复记录', '去除重复的记录行，保持记录唯一{0}这个仅仅基于一个已经排好序的输入.{1}如果输入没有排序, 仅仅两个连续的记录行被正确处理.');
INSERT INTO `r_step_type` VALUES (144, 'SyslogMessage', '发送信息至syslog', 'Send message to syslog server');
INSERT INTO `r_step_type` VALUES (145, 'Mail', '发送邮件', 'Send eMail.');
INSERT INTO `r_step_type` VALUES (146, 'MergeRows', '合并记录', '合并两个数据流, 并根据某个关键字排序.  这两个数据流被比较，以标识相等的、变更的、删除的和新建的记录.');
INSERT INTO `r_step_type` VALUES (147, 'ExecProcess', '启动一个进程', 'Execute a process and return the result');
INSERT INTO `r_step_type` VALUES (148, 'UniqueRowsByHashSet', '唯一行 (哈希值)', 'Remove double rows and leave only unique occurrences by using a HashSet.');
INSERT INTO `r_step_type` VALUES (149, 'FixedInput', '固定宽度文件输入', 'Fixed file input');
INSERT INTO `r_step_type` VALUES (150, 'MemoryGroupBy', '在内存中分组', 'Builds aggregates in a group by fashion.\nThis step doesn\'t require sorted input.');
INSERT INTO `r_step_type` VALUES (151, 'Constant', '增加常量', '给记录增加一到多个常量');
INSERT INTO `r_step_type` VALUES (152, 'Sequence', '增加序列', '从序列获取下一个值');
INSERT INTO `r_step_type` VALUES (153, 'ProcessFiles', '处理文件', 'Process one file per row (copy or move or delete).\nThis step only accept filename in input.');
INSERT INTO `r_step_type` VALUES (154, 'FilesToResult', '复制文件到结果', 'This step allows you to set filenames in the result of this transformation.\nSubsequent job entries can then use this information.');
INSERT INTO `r_step_type` VALUES (155, 'RowsToResult', '复制记录到结果', '使用这个步骤把记录写到正在执行的任务.{0}信息将会被传递给同一个任务里的下一个条目.');
INSERT INTO `r_step_type` VALUES (156, 'SelectValues', '字段选择', '选择或移除记录里的字。{0}此外，可以设置字段的元数据: 类型, 长度和精度.');
INSERT INTO `r_step_type` VALUES (157, 'StringOperations', '字符串操作', 'Apply certain operations like trimming, padding and others to string value.');
INSERT INTO `r_step_type` VALUES (158, 'ReplaceString', '字符串替换', 'Replace all occurences a word in a string with another word.');
INSERT INTO `r_step_type` VALUES (159, 'SymmetricCryptoTrans', '对称加密', 'Encrypt or decrypt a string using symmetric encryption.\nAvailable algorithms are DES, AES, TripleDES.');
INSERT INTO `r_step_type` VALUES (160, 'SetValueConstant', '将字段值设置为常量', 'Set value of a field to a constant');
INSERT INTO `r_step_type` VALUES (161, 'Delay', '延迟行', 'Output each input row after a delay');
INSERT INTO `r_step_type` VALUES (162, 'DynamicSQLRow', '执行Dynamic SQL', 'Execute dynamic SQL statement build in a previous field');
INSERT INTO `r_step_type` VALUES (163, 'ExecSQL', '执行SQL脚本', '执行一个SQL脚本, 另外，可以使用输入的记录作为参数');
INSERT INTO `r_step_type` VALUES (164, 'ExecSQLRow', '执行SQL脚本(字段流替换)', 'Execute SQL script extracted from a field\ncreated in a previous step.');
INSERT INTO `r_step_type` VALUES (165, 'JobExecutor', '执行作业', 'This step executes a Pentaho Data Integration job, sets parameters and passes rows.');
INSERT INTO `r_step_type` VALUES (166, 'FieldSplitter', '拆分字段', '当你想把一个字段拆分成多个时，使用这个类型.');
INSERT INTO `r_step_type` VALUES (167, 'SortedMerge', '排序合并', 'Sorted merge');
INSERT INTO `r_step_type` VALUES (168, 'SortRows', '排序记录', '基于字段值把记录排序(升序或降序)');
INSERT INTO `r_step_type` VALUES (169, 'InsertUpdate', '插入 / 更新', '基于关键字更新或插入记录到数据库.');
INSERT INTO `r_step_type` VALUES (170, 'NumberRange', '数值范围', 'Create ranges based on numeric field');
INSERT INTO `r_step_type` VALUES (171, 'SynchronizeAfterMerge', '数据同步', 'This step perform insert/update/delete in one go based on the value of a field.');
INSERT INTO `r_step_type` VALUES (172, 'DBLookup', '数据库查询', '使用字段值在数据库里查询值');
INSERT INTO `r_step_type` VALUES (173, 'DBJoin', '数据库连接', '使用数据流里的值作为参数执行一个数据库查询');
INSERT INTO `r_step_type` VALUES (174, 'Validator', '数据检验', 'Validates passing data based on a set of rules');
INSERT INTO `r_step_type` VALUES (175, 'PrioritizeStreams', '数据流优先级排序', 'Prioritize streams in an order way.');
INSERT INTO `r_step_type` VALUES (176, 'ReservoirSampling', '数据采样', '[Transform] Samples a fixed number of rows from the incoming stream');
INSERT INTO `r_step_type` VALUES (177, 'LoadFileInput', '文件内容加载至内存', 'Load file content in memory');
INSERT INTO `r_step_type` VALUES (178, 'TextFileInput', '文本文件输入', '从一个文本文件（几种格式）里读取数据{0}这些数据可以被传递到下一个步骤里...');
INSERT INTO `r_step_type` VALUES (179, 'TextFileOutput', '文本文件输出', '写记录到一个文本文件.');
INSERT INTO `r_step_type` VALUES (180, 'Mapping', '映射 (子转换)', '运行一个映射 (子转换), 使用MappingInput和MappingOutput来指定接口的字段');
INSERT INTO `r_step_type` VALUES (181, 'MappingInput', '映射输入规范', '指定一个映射的字段输入');
INSERT INTO `r_step_type` VALUES (182, 'MappingOutput', '映射输出规范', '指定一个映射的字段输出');
INSERT INTO `r_step_type` VALUES (183, 'Update', '更新', '基于关键字更新记录到数据库');
INSERT INTO `r_step_type` VALUES (184, 'IfNull', '替换NULL值', 'Sets a field value to a constant if it is null.');
INSERT INTO `r_step_type` VALUES (185, 'SampleRows', '样本行', 'Filter rows based on the line number.');
INSERT INTO `r_step_type` VALUES (186, 'JavaFilter', '根据Java代码过滤记录', 'Filter rows using java code');
INSERT INTO `r_step_type` VALUES (187, 'FieldsChangeSequence', '根据字段值来改变序列', 'Add sequence depending of fields value change.\nEach time value of at least one field change, PDI will reset sequence.');
INSERT INTO `r_step_type` VALUES (188, 'WebServiceAvailable', '检查web服务是否可用', 'Check if a webservice is available');
INSERT INTO `r_step_type` VALUES (189, 'FileExists', '检查文件是否存在', 'Check if a file exists');
INSERT INTO `r_step_type` VALUES (190, 'FileLocked', '检查文件是否已被锁定', 'Check if a file is locked by another process');
INSERT INTO `r_step_type` VALUES (191, 'TableExists', '检查表是否存在', 'Check if a table exists on a specified connection');
INSERT INTO `r_step_type` VALUES (192, 'DetectEmptyStream', '检测空流', 'This step will output one empty row if input stream is empty\n(ie when input stream does not contain any row)');
INSERT INTO `r_step_type` VALUES (193, 'CreditCardValidator', '检验信用卡号码是否有效', 'The Credit card validator step will help you tell:\n(1) if a credit card number is valid (uses LUHN10 (MOD-10) algorithm)\n(2) which credit card vendor handles that number\n(VISA, MasterCard, Diners Club, EnRoute, American Express (AMEX),...)');
INSERT INTO `r_step_type` VALUES (194, 'MailValidator', '检验邮件地址', 'Check if an email address is valid.');
INSERT INTO `r_step_type` VALUES (195, 'FuzzyMatch', '模糊匹配', 'Finding approximate matches to a string using matching algorithms.\nRead a field from a main stream and output approximative value from lookup stream.');
INSERT INTO `r_step_type` VALUES (196, 'RegexEval', '正则表达式', 'Regular expression Evaluation\nThis step uses a regular expression to evaluate a field. It can also extract new fields out of an existing field with capturing groups.');
INSERT INTO `r_step_type` VALUES (197, 'TableCompare', '比较表', 'Compares 2 tables and gives back a list of differences');
INSERT INTO `r_step_type` VALUES (198, 'StreamLookup', '流查询', '从转换中的其它流里查询值.');
INSERT INTO `r_step_type` VALUES (199, 'StepMetastructure', '流的元数据', 'This is a step to read the metadata of the incoming stream.');
INSERT INTO `r_step_type` VALUES (200, 'SecretKeyGenerator', '生成密钥', 'Generate secret key for algorithms such as DES, AES, TripleDES.');
INSERT INTO `r_step_type` VALUES (201, 'RowGenerator', '生成记录', '产生一些空记录或相等的行.');
INSERT INTO `r_step_type` VALUES (202, 'RandomValue', '生成随机数', 'Generate random value');
INSERT INTO `r_step_type` VALUES (203, 'RandomCCNumberGenerator', '生成随机的信用卡号', 'Generate random valide (luhn check) credit card numbers');
INSERT INTO `r_step_type` VALUES (204, 'Dummy', '空操作 (什么也不做)', '这个步骤类型什么都不作.{0} 当你想测试或拆分数据流的时候有用.');
INSERT INTO `r_step_type` VALUES (205, 'DimensionLookup', '维度查询/更新', '在一个数据仓库里更新一个渐变维 {0} 或者在这个维里查询信息.');
INSERT INTO `r_step_type` VALUES (206, 'CombinationLookup', '联合查询/更新', '更新数据仓库里的一个junk维 {0} 可选的, 科研查询维里的信息.{1}junk维的主键是所有的字段.');
INSERT INTO `r_step_type` VALUES (207, 'AutoDoc', '自动文档输出', 'This step automatically generates documentation based on input in the form of a list of transformations and jobs');
INSERT INTO `r_step_type` VALUES (208, 'DataGrid', '自定义常量数据', 'Enter rows of static data in a grid, usually for testing, reference or demo purpose');
INSERT INTO `r_step_type` VALUES (209, 'GetVariable', '获取变量', 'Determine the values of certain (environment or Kettle) variables and put them in field values.');
INSERT INTO `r_step_type` VALUES (210, 'GetSubFolders', '获取子目录名', 'Read a parent folder and return all subfolders');
INSERT INTO `r_step_type` VALUES (211, 'GetFileNames', '获取文件名', 'Get file names from the operating system and send them to the next step.');
INSERT INTO `r_step_type` VALUES (212, 'GetFilesRowsCount', '获取文件行数', 'Returns rows count for text files.');
INSERT INTO `r_step_type` VALUES (213, 'SystemInfo', '获取系统信息', '获取系统信息，例如时间、日期.');
INSERT INTO `r_step_type` VALUES (214, 'GetTableNames', '获取表名', 'Get table names from database connection and send them to the next step');
INSERT INTO `r_step_type` VALUES (215, 'GetRepositoryNames', '获取资源库配置', 'Lists detailed information about transformations and/or jobs in a repository');
INSERT INTO `r_step_type` VALUES (216, 'Flattener', '行扁平化', 'Flattens consequetive rows based on the order in which they appear in the输入 stream');
INSERT INTO `r_step_type` VALUES (217, 'Normaliser', '行转列', 'De-normalised information can be normalised using this step type.');
INSERT INTO `r_step_type` VALUES (218, 'TableInput', '表输入', '从数据库表里读取信息.');
INSERT INTO `r_step_type` VALUES (219, 'TableOutput', '表输出', '写信息到一个数据库表');
INSERT INTO `r_step_type` VALUES (220, 'Calculator', '计算器', '通过执行简单的计算创建一个新字段');
INSERT INTO `r_step_type` VALUES (221, 'JoinRows', '记录关联 (笛卡尔输出)', '这个步骤的输出是输入流的笛卡尔的结果.{0} 输出结果的记录数是输入流记录之间的乘积.');
INSERT INTO `r_step_type` VALUES (222, 'Injector', '记录注射', 'Injector step to allow to inject rows into the transformation through the java API');
INSERT INTO `r_step_type` VALUES (223, 'MergeJoin', '记录集连接', 'Joins two streams on a given key and outputs a joined set. The input streams must be sorted on the join key');
INSERT INTO `r_step_type` VALUES (224, 'NullIf', '设置值为NULL', '如果一个字段值等于某个固定值，那么把这个字段值设置成null');
INSERT INTO `r_step_type` VALUES (225, 'SetVariable', '设置变量', 'Set environment variables based on a single input row.');
INSERT INTO `r_step_type` VALUES (226, 'SetValueField', '设置字段值', 'Set value of a field with another value field');
INSERT INTO `r_step_type` VALUES (227, 'DetectLastRow', '识别流的最后一行', 'Last row will be marked');
INSERT INTO `r_step_type` VALUES (228, 'DBProc', '调用DB存储过程', '通过调用数据库存储过程获得返回值.');
INSERT INTO `r_step_type` VALUES (229, 'StepsMetrics', '转换步骤信息统计', 'Return metrics for one or several steps');
INSERT INTO `r_step_type` VALUES (230, 'FilterRows', '过滤记录', '使用简单的相等来过滤记录');
INSERT INTO `r_step_type` VALUES (231, 'SSH', '运行SSH命令', 'Run SSH commands and returns result.');
INSERT INTO `r_step_type` VALUES (232, 'Append', '追加流', 'Append 2 streams in an ordered way');
INSERT INTO `r_step_type` VALUES (233, 'MailInput', '邮件信息输入', 'Read POP3/IMAP server and retrieve messages');
INSERT INTO `r_step_type` VALUES (234, 'PropertyInput', '配置文件输入', 'Read data (key, value) from properties files.');
INSERT INTO `r_step_type` VALUES (235, 'PropertyOutput', '配置文件输出', 'Write data to properties file');
INSERT INTO `r_step_type` VALUES (236, 'BlockUntilStepsFinish', '阻塞数据直到步骤都完成', 'Block this step until selected steps finish.');

-- ----------------------------
-- Table structure for r_trans_attribute
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_attribute`;
CREATE TABLE `r_trans_attribute`  (
  `ID_TRANS_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) NULL DEFAULT NULL,
  `NR` int(11) NULL DEFAULT NULL,
  `CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `VALUE_NUM` bigint(20) NULL DEFAULT NULL,
  `VALUE_STR` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID_TRANS_ATTRIBUTE`) USING BTREE,
  UNIQUE INDEX `IDX_TATT`(`ID_TRANSFORMATION`, `CODE`, `NR`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_trans_attribute
-- ----------------------------
INSERT INTO `r_trans_attribute` VALUES (1, 1, 0, 'UNIQUE_CONNECTIONS', 0, 'N');
INSERT INTO `r_trans_attribute` VALUES (2, 1, 0, 'FEEDBACK_SHOWN', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (3, 1, 0, 'FEEDBACK_SIZE', 50000, NULL);
INSERT INTO `r_trans_attribute` VALUES (4, 1, 0, 'USING_THREAD_PRIORITIES', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (5, 1, 0, 'SHARED_FILE', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (6, 1, 0, 'CAPTURE_STEP_PERFORMANCE', 0, 'N');
INSERT INTO `r_trans_attribute` VALUES (7, 1, 0, 'STEP_PERFORMANCE_CAPTURING_DELAY', 1000, NULL);
INSERT INTO `r_trans_attribute` VALUES (8, 1, 0, 'STEP_PERFORMANCE_CAPTURING_SIZE_LIMIT', 0, '100');
INSERT INTO `r_trans_attribute` VALUES (9, 1, 0, 'STEP_PERFORMANCE_LOG_TABLE', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (10, 1, 0, 'LOG_SIZE_LIMIT', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (11, 1, 0, 'LOG_INTERVAL', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (12, 1, 0, 'TRANSFORMATION_TYPE', 0, 'Normal');
INSERT INTO `r_trans_attribute` VALUES (13, 1, 0, 'SLEEP_TIME_EMPTY', 50, NULL);
INSERT INTO `r_trans_attribute` VALUES (14, 1, 0, 'SLEEP_TIME_FULL', 50, NULL);
INSERT INTO `r_trans_attribute` VALUES (15, 1, 0, 'TRANS_LOG_TABLE_CONNECTION_NAME', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (16, 1, 0, 'TRANS_LOG_TABLE_SCHEMA_NAME', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (17, 1, 0, 'TRANS_LOG_TABLE_TABLE_NAME', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (18, 1, 0, 'TRANS_LOG_TABLE_TIMEOUT_IN_DAYS', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (19, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID0', 0, 'ID_BATCH');
INSERT INTO `r_trans_attribute` VALUES (20, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME0', 0, 'ID_BATCH');
INSERT INTO `r_trans_attribute` VALUES (21, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED0', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (22, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID1', 0, 'CHANNEL_ID');
INSERT INTO `r_trans_attribute` VALUES (23, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME1', 0, 'CHANNEL_ID');
INSERT INTO `r_trans_attribute` VALUES (24, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED1', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (25, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID2', 0, 'TRANSNAME');
INSERT INTO `r_trans_attribute` VALUES (26, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME2', 0, 'TRANSNAME');
INSERT INTO `r_trans_attribute` VALUES (27, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED2', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (28, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID3', 0, 'STATUS');
INSERT INTO `r_trans_attribute` VALUES (29, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME3', 0, 'STATUS');
INSERT INTO `r_trans_attribute` VALUES (30, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED3', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (31, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID4', 0, 'LINES_READ');
INSERT INTO `r_trans_attribute` VALUES (32, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME4', 0, 'LINES_READ');
INSERT INTO `r_trans_attribute` VALUES (33, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED4', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (34, 1, 0, 'TRANS_LOG_TABLE_FIELD_SUBJECT4', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (35, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID5', 0, 'LINES_WRITTEN');
INSERT INTO `r_trans_attribute` VALUES (36, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME5', 0, 'LINES_WRITTEN');
INSERT INTO `r_trans_attribute` VALUES (37, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED5', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (38, 1, 0, 'TRANS_LOG_TABLE_FIELD_SUBJECT5', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (39, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID6', 0, 'LINES_UPDATED');
INSERT INTO `r_trans_attribute` VALUES (40, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME6', 0, 'LINES_UPDATED');
INSERT INTO `r_trans_attribute` VALUES (41, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED6', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (42, 1, 0, 'TRANS_LOG_TABLE_FIELD_SUBJECT6', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (43, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID7', 0, 'LINES_INPUT');
INSERT INTO `r_trans_attribute` VALUES (44, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME7', 0, 'LINES_INPUT');
INSERT INTO `r_trans_attribute` VALUES (45, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED7', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (46, 1, 0, 'TRANS_LOG_TABLE_FIELD_SUBJECT7', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (47, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID8', 0, 'LINES_OUTPUT');
INSERT INTO `r_trans_attribute` VALUES (48, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME8', 0, 'LINES_OUTPUT');
INSERT INTO `r_trans_attribute` VALUES (49, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED8', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (50, 1, 0, 'TRANS_LOG_TABLE_FIELD_SUBJECT8', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (51, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID9', 0, 'LINES_REJECTED');
INSERT INTO `r_trans_attribute` VALUES (52, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME9', 0, 'LINES_REJECTED');
INSERT INTO `r_trans_attribute` VALUES (53, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED9', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (54, 1, 0, 'TRANS_LOG_TABLE_FIELD_SUBJECT9', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (55, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID10', 0, 'ERRORS');
INSERT INTO `r_trans_attribute` VALUES (56, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME10', 0, 'ERRORS');
INSERT INTO `r_trans_attribute` VALUES (57, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED10', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (58, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID11', 0, 'STARTDATE');
INSERT INTO `r_trans_attribute` VALUES (59, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME11', 0, 'STARTDATE');
INSERT INTO `r_trans_attribute` VALUES (60, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED11', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (61, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID12', 0, 'ENDDATE');
INSERT INTO `r_trans_attribute` VALUES (62, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME12', 0, 'ENDDATE');
INSERT INTO `r_trans_attribute` VALUES (63, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED12', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (64, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID13', 0, 'LOGDATE');
INSERT INTO `r_trans_attribute` VALUES (65, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME13', 0, 'LOGDATE');
INSERT INTO `r_trans_attribute` VALUES (66, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED13', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (67, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID14', 0, 'DEPDATE');
INSERT INTO `r_trans_attribute` VALUES (68, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME14', 0, 'DEPDATE');
INSERT INTO `r_trans_attribute` VALUES (69, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED14', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (70, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID15', 0, 'REPLAYDATE');
INSERT INTO `r_trans_attribute` VALUES (71, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME15', 0, 'REPLAYDATE');
INSERT INTO `r_trans_attribute` VALUES (72, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED15', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (73, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID16', 0, 'LOG_FIELD');
INSERT INTO `r_trans_attribute` VALUES (74, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME16', 0, 'LOG_FIELD');
INSERT INTO `r_trans_attribute` VALUES (75, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED16', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (76, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID17', 0, 'EXECUTING_SERVER');
INSERT INTO `r_trans_attribute` VALUES (77, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME17', 0, 'EXECUTING_SERVER');
INSERT INTO `r_trans_attribute` VALUES (78, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED17', 0, 'N');
INSERT INTO `r_trans_attribute` VALUES (79, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID18', 0, 'EXECUTING_USER');
INSERT INTO `r_trans_attribute` VALUES (80, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME18', 0, 'EXECUTING_USER');
INSERT INTO `r_trans_attribute` VALUES (81, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED18', 0, 'N');
INSERT INTO `r_trans_attribute` VALUES (82, 1, 0, 'TRANS_LOG_TABLE_FIELD_ID19', 0, 'CLIENT');
INSERT INTO `r_trans_attribute` VALUES (83, 1, 0, 'TRANS_LOG_TABLE_FIELD_NAME19', 0, 'CLIENT');
INSERT INTO `r_trans_attribute` VALUES (84, 1, 0, 'TRANS_LOG_TABLE_FIELD_ENABLED19', 0, 'N');
INSERT INTO `r_trans_attribute` VALUES (85, 1, 0, 'TRANSLOG_TABLE_INTERVAL', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (86, 1, 0, 'TRANSLOG_TABLE_SIZE_LIMIT', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (87, 1, 0, 'STEP_LOG_TABLE_CONNECTION_NAME', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (88, 1, 0, 'STEP_LOG_TABLE_SCHEMA_NAME', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (89, 1, 0, 'STEP_LOG_TABLE_TABLE_NAME', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (90, 1, 0, 'STEP_LOG_TABLE_TIMEOUT_IN_DAYS', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (91, 1, 0, 'STEP_LOG_TABLE_FIELD_ID0', 0, 'ID_BATCH');
INSERT INTO `r_trans_attribute` VALUES (92, 1, 0, 'STEP_LOG_TABLE_FIELD_NAME0', 0, 'ID_BATCH');
INSERT INTO `r_trans_attribute` VALUES (93, 1, 0, 'STEP_LOG_TABLE_FIELD_ENABLED0', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (94, 1, 0, 'STEP_LOG_TABLE_FIELD_ID1', 0, 'CHANNEL_ID');
INSERT INTO `r_trans_attribute` VALUES (95, 1, 0, 'STEP_LOG_TABLE_FIELD_NAME1', 0, 'CHANNEL_ID');
INSERT INTO `r_trans_attribute` VALUES (96, 1, 0, 'STEP_LOG_TABLE_FIELD_ENABLED1', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (97, 1, 0, 'STEP_LOG_TABLE_FIELD_ID2', 0, 'LOG_DATE');
INSERT INTO `r_trans_attribute` VALUES (98, 1, 0, 'STEP_LOG_TABLE_FIELD_NAME2', 0, 'LOG_DATE');
INSERT INTO `r_trans_attribute` VALUES (99, 1, 0, 'STEP_LOG_TABLE_FIELD_ENABLED2', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (100, 1, 0, 'STEP_LOG_TABLE_FIELD_ID3', 0, 'TRANSNAME');
INSERT INTO `r_trans_attribute` VALUES (101, 1, 0, 'STEP_LOG_TABLE_FIELD_NAME3', 0, 'TRANSNAME');
INSERT INTO `r_trans_attribute` VALUES (102, 1, 0, 'STEP_LOG_TABLE_FIELD_ENABLED3', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (103, 1, 0, 'STEP_LOG_TABLE_FIELD_ID4', 0, 'STEPNAME');
INSERT INTO `r_trans_attribute` VALUES (104, 1, 0, 'STEP_LOG_TABLE_FIELD_NAME4', 0, 'STEPNAME');
INSERT INTO `r_trans_attribute` VALUES (105, 1, 0, 'STEP_LOG_TABLE_FIELD_ENABLED4', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (106, 1, 0, 'STEP_LOG_TABLE_FIELD_ID5', 0, 'STEP_COPY');
INSERT INTO `r_trans_attribute` VALUES (107, 1, 0, 'STEP_LOG_TABLE_FIELD_NAME5', 0, 'STEP_COPY');
INSERT INTO `r_trans_attribute` VALUES (108, 1, 0, 'STEP_LOG_TABLE_FIELD_ENABLED5', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (109, 1, 0, 'STEP_LOG_TABLE_FIELD_ID6', 0, 'LINES_READ');
INSERT INTO `r_trans_attribute` VALUES (110, 1, 0, 'STEP_LOG_TABLE_FIELD_NAME6', 0, 'LINES_READ');
INSERT INTO `r_trans_attribute` VALUES (111, 1, 0, 'STEP_LOG_TABLE_FIELD_ENABLED6', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (112, 1, 0, 'STEP_LOG_TABLE_FIELD_ID7', 0, 'LINES_WRITTEN');
INSERT INTO `r_trans_attribute` VALUES (113, 1, 0, 'STEP_LOG_TABLE_FIELD_NAME7', 0, 'LINES_WRITTEN');
INSERT INTO `r_trans_attribute` VALUES (114, 1, 0, 'STEP_LOG_TABLE_FIELD_ENABLED7', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (115, 1, 0, 'STEP_LOG_TABLE_FIELD_ID8', 0, 'LINES_UPDATED');
INSERT INTO `r_trans_attribute` VALUES (116, 1, 0, 'STEP_LOG_TABLE_FIELD_NAME8', 0, 'LINES_UPDATED');
INSERT INTO `r_trans_attribute` VALUES (117, 1, 0, 'STEP_LOG_TABLE_FIELD_ENABLED8', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (118, 1, 0, 'STEP_LOG_TABLE_FIELD_ID9', 0, 'LINES_INPUT');
INSERT INTO `r_trans_attribute` VALUES (119, 1, 0, 'STEP_LOG_TABLE_FIELD_NAME9', 0, 'LINES_INPUT');
INSERT INTO `r_trans_attribute` VALUES (120, 1, 0, 'STEP_LOG_TABLE_FIELD_ENABLED9', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (121, 1, 0, 'STEP_LOG_TABLE_FIELD_ID10', 0, 'LINES_OUTPUT');
INSERT INTO `r_trans_attribute` VALUES (122, 1, 0, 'STEP_LOG_TABLE_FIELD_NAME10', 0, 'LINES_OUTPUT');
INSERT INTO `r_trans_attribute` VALUES (123, 1, 0, 'STEP_LOG_TABLE_FIELD_ENABLED10', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (124, 1, 0, 'STEP_LOG_TABLE_FIELD_ID11', 0, 'LINES_REJECTED');
INSERT INTO `r_trans_attribute` VALUES (125, 1, 0, 'STEP_LOG_TABLE_FIELD_NAME11', 0, 'LINES_REJECTED');
INSERT INTO `r_trans_attribute` VALUES (126, 1, 0, 'STEP_LOG_TABLE_FIELD_ENABLED11', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (127, 1, 0, 'STEP_LOG_TABLE_FIELD_ID12', 0, 'ERRORS');
INSERT INTO `r_trans_attribute` VALUES (128, 1, 0, 'STEP_LOG_TABLE_FIELD_NAME12', 0, 'ERRORS');
INSERT INTO `r_trans_attribute` VALUES (129, 1, 0, 'STEP_LOG_TABLE_FIELD_ENABLED12', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (130, 1, 0, 'STEP_LOG_TABLE_FIELD_ID13', 0, 'LOG_FIELD');
INSERT INTO `r_trans_attribute` VALUES (131, 1, 0, 'STEP_LOG_TABLE_FIELD_NAME13', 0, 'LOG_FIELD');
INSERT INTO `r_trans_attribute` VALUES (132, 1, 0, 'STEP_LOG_TABLE_FIELD_ENABLED13', 0, 'N');
INSERT INTO `r_trans_attribute` VALUES (133, 1, 0, 'PERFORMANCE_LOG_TABLE_CONNECTION_NAME', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (134, 1, 0, 'PERFORMANCE_LOG_TABLE_SCHEMA_NAME', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (135, 1, 0, 'PERFORMANCE_LOG_TABLE_TABLE_NAME', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (136, 1, 0, 'PERFORMANCE_LOG_TABLE_TIMEOUT_IN_DAYS', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (137, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ID0', 0, 'ID_BATCH');
INSERT INTO `r_trans_attribute` VALUES (138, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_NAME0', 0, 'ID_BATCH');
INSERT INTO `r_trans_attribute` VALUES (139, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ENABLED0', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (140, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ID1', 0, 'SEQ_NR');
INSERT INTO `r_trans_attribute` VALUES (141, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_NAME1', 0, 'SEQ_NR');
INSERT INTO `r_trans_attribute` VALUES (142, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ENABLED1', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (143, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ID2', 0, 'LOGDATE');
INSERT INTO `r_trans_attribute` VALUES (144, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_NAME2', 0, 'LOGDATE');
INSERT INTO `r_trans_attribute` VALUES (145, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ENABLED2', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (146, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ID3', 0, 'TRANSNAME');
INSERT INTO `r_trans_attribute` VALUES (147, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_NAME3', 0, 'TRANSNAME');
INSERT INTO `r_trans_attribute` VALUES (148, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ENABLED3', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (149, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ID4', 0, 'STEPNAME');
INSERT INTO `r_trans_attribute` VALUES (150, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_NAME4', 0, 'STEPNAME');
INSERT INTO `r_trans_attribute` VALUES (151, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ENABLED4', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (152, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ID5', 0, 'STEP_COPY');
INSERT INTO `r_trans_attribute` VALUES (153, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_NAME5', 0, 'STEP_COPY');
INSERT INTO `r_trans_attribute` VALUES (154, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ENABLED5', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (155, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ID6', 0, 'LINES_READ');
INSERT INTO `r_trans_attribute` VALUES (156, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_NAME6', 0, 'LINES_READ');
INSERT INTO `r_trans_attribute` VALUES (157, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ENABLED6', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (158, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ID7', 0, 'LINES_WRITTEN');
INSERT INTO `r_trans_attribute` VALUES (159, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_NAME7', 0, 'LINES_WRITTEN');
INSERT INTO `r_trans_attribute` VALUES (160, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ENABLED7', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (161, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ID8', 0, 'LINES_UPDATED');
INSERT INTO `r_trans_attribute` VALUES (162, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_NAME8', 0, 'LINES_UPDATED');
INSERT INTO `r_trans_attribute` VALUES (163, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ENABLED8', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (164, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ID9', 0, 'LINES_INPUT');
INSERT INTO `r_trans_attribute` VALUES (165, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_NAME9', 0, 'LINES_INPUT');
INSERT INTO `r_trans_attribute` VALUES (166, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ENABLED9', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (167, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ID10', 0, 'LINES_OUTPUT');
INSERT INTO `r_trans_attribute` VALUES (168, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_NAME10', 0, 'LINES_OUTPUT');
INSERT INTO `r_trans_attribute` VALUES (169, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ENABLED10', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (170, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ID11', 0, 'LINES_REJECTED');
INSERT INTO `r_trans_attribute` VALUES (171, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_NAME11', 0, 'LINES_REJECTED');
INSERT INTO `r_trans_attribute` VALUES (172, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ENABLED11', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (173, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ID12', 0, 'ERRORS');
INSERT INTO `r_trans_attribute` VALUES (174, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_NAME12', 0, 'ERRORS');
INSERT INTO `r_trans_attribute` VALUES (175, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ENABLED12', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (176, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ID13', 0, 'INPUT_BUFFER_ROWS');
INSERT INTO `r_trans_attribute` VALUES (177, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_NAME13', 0, 'INPUT_BUFFER_ROWS');
INSERT INTO `r_trans_attribute` VALUES (178, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ENABLED13', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (179, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ID14', 0, 'OUTPUT_BUFFER_ROWS');
INSERT INTO `r_trans_attribute` VALUES (180, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_NAME14', 0, 'OUTPUT_BUFFER_ROWS');
INSERT INTO `r_trans_attribute` VALUES (181, 1, 0, 'PERFORMANCE_LOG_TABLE_FIELD_ENABLED14', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (182, 1, 0, 'PERFORMANCELOG_TABLE_INTERVAL', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (183, 1, 0, 'CHANNEL_LOG_TABLE_CONNECTION_NAME', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (184, 1, 0, 'CHANNEL_LOG_TABLE_SCHEMA_NAME', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (185, 1, 0, 'CHANNEL_LOG_TABLE_TABLE_NAME', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (186, 1, 0, 'CHANNEL_LOG_TABLE_TIMEOUT_IN_DAYS', 0, NULL);
INSERT INTO `r_trans_attribute` VALUES (187, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID0', 0, 'ID_BATCH');
INSERT INTO `r_trans_attribute` VALUES (188, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME0', 0, 'ID_BATCH');
INSERT INTO `r_trans_attribute` VALUES (189, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED0', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (190, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID1', 0, 'CHANNEL_ID');
INSERT INTO `r_trans_attribute` VALUES (191, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME1', 0, 'CHANNEL_ID');
INSERT INTO `r_trans_attribute` VALUES (192, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED1', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (193, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID2', 0, 'LOG_DATE');
INSERT INTO `r_trans_attribute` VALUES (194, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME2', 0, 'LOG_DATE');
INSERT INTO `r_trans_attribute` VALUES (195, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED2', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (196, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID3', 0, 'LOGGING_OBJECT_TYPE');
INSERT INTO `r_trans_attribute` VALUES (197, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME3', 0, 'LOGGING_OBJECT_TYPE');
INSERT INTO `r_trans_attribute` VALUES (198, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED3', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (199, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID4', 0, 'OBJECT_NAME');
INSERT INTO `r_trans_attribute` VALUES (200, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME4', 0, 'OBJECT_NAME');
INSERT INTO `r_trans_attribute` VALUES (201, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED4', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (202, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID5', 0, 'OBJECT_COPY');
INSERT INTO `r_trans_attribute` VALUES (203, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME5', 0, 'OBJECT_COPY');
INSERT INTO `r_trans_attribute` VALUES (204, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED5', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (205, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID6', 0, 'REPOSITORY_DIRECTORY');
INSERT INTO `r_trans_attribute` VALUES (206, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME6', 0, 'REPOSITORY_DIRECTORY');
INSERT INTO `r_trans_attribute` VALUES (207, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED6', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (208, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID7', 0, 'FILENAME');
INSERT INTO `r_trans_attribute` VALUES (209, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME7', 0, 'FILENAME');
INSERT INTO `r_trans_attribute` VALUES (210, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED7', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (211, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID8', 0, 'OBJECT_ID');
INSERT INTO `r_trans_attribute` VALUES (212, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME8', 0, 'OBJECT_ID');
INSERT INTO `r_trans_attribute` VALUES (213, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED8', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (214, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID9', 0, 'OBJECT_REVISION');
INSERT INTO `r_trans_attribute` VALUES (215, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME9', 0, 'OBJECT_REVISION');
INSERT INTO `r_trans_attribute` VALUES (216, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED9', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (217, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID10', 0, 'PARENT_CHANNEL_ID');
INSERT INTO `r_trans_attribute` VALUES (218, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME10', 0, 'PARENT_CHANNEL_ID');
INSERT INTO `r_trans_attribute` VALUES (219, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED10', 0, 'Y');
INSERT INTO `r_trans_attribute` VALUES (220, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ID11', 0, 'ROOT_CHANNEL_ID');
INSERT INTO `r_trans_attribute` VALUES (221, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_NAME11', 0, 'ROOT_CHANNEL_ID');
INSERT INTO `r_trans_attribute` VALUES (222, 1, 0, 'CHANNEL_LOG_TABLE_FIELD_ENABLED11', 0, 'Y');

-- ----------------------------
-- Table structure for r_trans_cluster
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_cluster`;
CREATE TABLE `r_trans_cluster`  (
  `ID_TRANS_CLUSTER` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) NULL DEFAULT NULL,
  `ID_CLUSTER` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_TRANS_CLUSTER`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_trans_hop
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_hop`;
CREATE TABLE `r_trans_hop`  (
  `ID_TRANS_HOP` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) NULL DEFAULT NULL,
  `ID_STEP_FROM` int(11) NULL DEFAULT NULL,
  `ID_STEP_TO` int(11) NULL DEFAULT NULL,
  `ENABLED` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_TRANS_HOP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_trans_hop
-- ----------------------------
INSERT INTO `r_trans_hop` VALUES (1, 1, 2, 5, 1);
INSERT INTO `r_trans_hop` VALUES (2, 1, 1, 5, 1);
INSERT INTO `r_trans_hop` VALUES (3, 1, 5, 3, 1);
INSERT INTO `r_trans_hop` VALUES (4, 1, 3, 9, 1);
INSERT INTO `r_trans_hop` VALUES (5, 1, 9, 8, 1);
INSERT INTO `r_trans_hop` VALUES (6, 1, 9, 10, 1);
INSERT INTO `r_trans_hop` VALUES (7, 1, 10, 11, 1);
INSERT INTO `r_trans_hop` VALUES (8, 1, 11, 12, 1);
INSERT INTO `r_trans_hop` VALUES (9, 1, 10, 6, 1);
INSERT INTO `r_trans_hop` VALUES (10, 1, 11, 4, 1);
INSERT INTO `r_trans_hop` VALUES (11, 1, 12, 7, 1);

-- ----------------------------
-- Table structure for r_trans_lock
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_lock`;
CREATE TABLE `r_trans_lock`  (
  `ID_TRANS_LOCK` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) NULL DEFAULT NULL,
  `ID_USER` int(11) NULL DEFAULT NULL,
  `LOCK_MESSAGE` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `LOCK_DATE` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_TRANS_LOCK`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_trans_note
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_note`;
CREATE TABLE `r_trans_note`  (
  `ID_TRANSFORMATION` int(11) NULL DEFAULT NULL,
  `ID_NOTE` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_trans_partition_schema
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_partition_schema`;
CREATE TABLE `r_trans_partition_schema`  (
  `ID_TRANS_PARTITION_SCHEMA` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) NULL DEFAULT NULL,
  `ID_PARTITION_SCHEMA` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_TRANS_PARTITION_SCHEMA`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_trans_slave
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_slave`;
CREATE TABLE `r_trans_slave`  (
  `ID_TRANS_SLAVE` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) NULL DEFAULT NULL,
  `ID_SLAVE` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_TRANS_SLAVE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_trans_step_condition
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_step_condition`;
CREATE TABLE `r_trans_step_condition`  (
  `ID_TRANSFORMATION` int(11) NULL DEFAULT NULL,
  `ID_STEP` int(11) NULL DEFAULT NULL,
  `ID_CONDITION` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_trans_step_condition
-- ----------------------------
INSERT INTO `r_trans_step_condition` VALUES (1, 9, 1);
INSERT INTO `r_trans_step_condition` VALUES (1, 10, 2);
INSERT INTO `r_trans_step_condition` VALUES (1, 11, 3);
INSERT INTO `r_trans_step_condition` VALUES (1, 12, 4);

-- ----------------------------
-- Table structure for r_transformation
-- ----------------------------
DROP TABLE IF EXISTS `r_transformation`;
CREATE TABLE `r_transformation`  (
  `ID_TRANSFORMATION` bigint(20) NOT NULL,
  `ID_DIRECTORY` int(11) NULL DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DESCRIPTION` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `EXTENDED_DESCRIPTION` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `TRANS_VERSION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TRANS_STATUS` int(11) NULL DEFAULT NULL,
  `ID_STEP_READ` int(11) NULL DEFAULT NULL,
  `ID_STEP_WRITE` int(11) NULL DEFAULT NULL,
  `ID_STEP_INPUT` int(11) NULL DEFAULT NULL,
  `ID_STEP_OUTPUT` int(11) NULL DEFAULT NULL,
  `ID_STEP_UPDATE` int(11) NULL DEFAULT NULL,
  `ID_DATABASE_LOG` int(11) NULL DEFAULT NULL,
  `TABLE_NAME_LOG` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `USE_BATCHID` tinyint(1) NULL DEFAULT NULL,
  `USE_LOGFIELD` tinyint(1) NULL DEFAULT NULL,
  `ID_DATABASE_MAXDATE` int(11) NULL DEFAULT NULL,
  `TABLE_NAME_MAXDATE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FIELD_NAME_MAXDATE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OFFSET_MAXDATE` double NULL DEFAULT NULL,
  `DIFF_MAXDATE` double NULL DEFAULT NULL,
  `CREATED_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CREATED_DATE` datetime(0) NULL DEFAULT NULL,
  `MODIFIED_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODIFIED_DATE` datetime(0) NULL DEFAULT NULL,
  `SIZE_ROWSET` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_TRANSFORMATION`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_transformation
-- ----------------------------
INSERT INTO `r_transformation` VALUES (1, 0, 'entp_input', NULL, NULL, NULL, -1, NULL, NULL, NULL, NULL, NULL, -1, NULL, 1, 1, -1, NULL, NULL, 0, 0, '-', '2018-12-27 16:55:11', 'admin', '2018-12-27 21:47:20', 10000);

-- ----------------------------
-- Table structure for r_user
-- ----------------------------
DROP TABLE IF EXISTS `r_user`;
CREATE TABLE `r_user`  (
  `ID_USER` bigint(20) NOT NULL,
  `LOGIN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PASSWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ENABLED` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_USER`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_user
-- ----------------------------
INSERT INTO `r_user` VALUES (1, 'admin', '2be98afc86aa7f2e4cb79ce71da9fa6d4', 'Administrator', 'User manager', 1);
INSERT INTO `r_user` VALUES (2, 'guest', '2be98afc86aa7f2e4cb79ce77cb97bcce', 'Guest account', 'Read-only guest account', 1);

-- ----------------------------
-- Table structure for r_value
-- ----------------------------
DROP TABLE IF EXISTS `r_value`;
CREATE TABLE `r_value`  (
  `ID_VALUE` bigint(20) NOT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `VALUE_TYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `VALUE_STR` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `IS_NULL` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_VALUE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_value
-- ----------------------------
INSERT INTO `r_value` VALUES (1, 'constant', 'String', '不变', 0);
INSERT INTO `r_value` VALUES (2, 'constant', 'String', '新增', 0);
INSERT INTO `r_value` VALUES (3, 'constant', 'String', '删除', 0);
INSERT INTO `r_value` VALUES (4, 'constant', 'String', '修改', 0);
INSERT INTO `r_value` VALUES (5, 'constant', 'String', 'N', 0);
INSERT INTO `r_value` VALUES (6, 'constant', 'String', 'I', 0);
INSERT INTO `r_value` VALUES (7, 'constant', 'String', 'D', 0);
INSERT INTO `r_value` VALUES (8, 'constant', 'String', 'U', 0);

-- ----------------------------
-- Table structure for r_version
-- ----------------------------
DROP TABLE IF EXISTS `r_version`;
CREATE TABLE `r_version`  (
  `ID_VERSION` bigint(20) NOT NULL,
  `MAJOR_VERSION` int(11) NULL DEFAULT NULL,
  `MINOR_VERSION` int(11) NULL DEFAULT NULL,
  `UPGRADE_DATE` datetime(0) NULL DEFAULT NULL,
  `IS_UPGRADE` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_VERSION`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_version
-- ----------------------------
INSERT INTO `r_version` VALUES (1, 5, 0, '2018-12-27 16:50:56', 0);

SET FOREIGN_KEY_CHECKS = 1;
