package com.lilianghui.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.lilianghui.core.Generator2;
import com.lilianghui.entity.Config;
import com.lilianghui.entity.TableRef;
import com.lilianghui.util.Ctrls;
import org.apache.commons.lang3.StringUtils;


public class DataOperator {
	public Vector<Vector<String>> getTableName(Config config) throws Exception {
		Vector<Vector<String>> rows = new Vector<Vector<String>>();
		Connection connection = JdbcUtils.getConnection(config);
		PreparedStatement ps = connection.prepareStatement(getSql(config, "selectTableName"));
		ResultSet rs = ps.executeQuery();
		int i = 1;
		String prefix = "";
		String suffix = "Dao";
		if (Generator2.class.getName().equalsIgnoreCase(config.getGenerator())) {
			suffix = "Mapper";
			prefix = "";
		}
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");
			String beanName = Ctrls.getBeanName(tableName, config.getJdbcTablePrefix(), false);
			Vector<String> row = new Vector<String>();
			row.add(String.valueOf(i++));
			row.add(tableName);
			row.add(rs.getString("TABLE_COMMENT"));
			row.add(rs.getString("COLUMN_NAME"));
			if (StringUtils.isNotBlank(rs.getString("AUTO_INCREMENT"))) {
				row.add("自增");
			} else {
				row.add("无");
			}
			row.add(beanName);
			row.add(prefix + beanName + suffix);
			row.add(prefix + beanName + "Service");
			row.add(beanName + "ServiceImpl");
			row.add(beanName + "Controller");
			row.add(beanName.toUpperCase());
			rows.add(row);
		}
		JdbcUtils.close(rs, ps, connection);
		return rows;

	}

	public List<Map<String, String>> getColumnEntity(Config config, String table) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Connection connection = JdbcUtils.getConnection(config);
		String sql = getSql(config, "selectColumnName");
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, table.toUpperCase());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Map<String, String> map = new HashMap<String, String>();
			ResultSetMetaData meta = rs.getMetaData();
			for (int i = 0; i < meta.getColumnCount(); i++) {
				String col = meta.getColumnName(i + 1);
				String value = rs.getString(col);
				if (value != null) {
					if ("COLUMN_NAME".equalsIgnoreCase(col)) {
						if ("MYSQL".equalsIgnoreCase(config.getDatabaseType()) && value.indexOf("_") < 0 && config.isCamelCaseNamin()) {
							map.put("skip", "true");
						} else {
							value = value.toLowerCase();
						}
					}
				}
				map.put(col.toLowerCase(), value);
			}

			list.add(map);
		}
		JdbcUtils.close(rs, ps, connection);
		return list;
	}

	public List<TableRef> getColumnRef(Config config, String tables) throws Exception {
		Connection connection = JdbcUtils.getConnection(config);
		String sql = getSql(config, "selectColumnRef");
		sql += " AND A.TABLE_NAME in(" + tables + ")";
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		List<TableRef> list = new ArrayList<TableRef>();
		while (rs.next()) {
			TableRef ref = new TableRef();
			ref.setManyColumnName(rs.getString("MANY_COLUMN_NAME"));
			ref.setManyConstraintType(rs.getString("MANY_CONSTRAINT_TYPE"));
			ref.setManyTableName(rs.getString("MANY_TABLE_NAME"));

			ref.setOneColumnName(rs.getString("ONE_COLUMN_NAME"));
			ref.setOneConstraintType(rs.getString("ONE_CONSTRAINT_TYPE"));
			ref.setOneTableName(rs.getString("ONE_TABLE_NAME"));

			list.add(ref);
		}
		JdbcUtils.close(rs, ps, connection);
		return list;
	}

	private String getSql(Config config, String sqlType) {
		String type = config.getDatabaseType();
		if ("selectTableName".equalsIgnoreCase(sqlType)) {
			if ("ORACLE".equalsIgnoreCase(type)) {
				return "SELECT DISTINCT T.TABLE_NAME, COLUMN_NAME  FROM USER_TABLES T,(SELECT CU.TABLE_NAME, CU.COLUMN_NAME FROM USER_CONS_COLUMNS CU, USER_CONSTRAINTS AU  WHERE CU.CONSTRAINT_NAME = AU.CONSTRAINT_NAME AND AU.CONSTRAINT_TYPE = 'P') K  WHERE T.TABLE_NAME = K.TABLE_NAME ORDER BY T.TABLE_NAME ";
			} else if ("MYSQL".equalsIgnoreCase(type)) {
				return "SELECT DISTINCT T.TABLE_NAME,K.COLUMN_NAME,T1.TABLE_COMMENT,T1.AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS T INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE K ON UPPER(T.CONSTRAINT_NAME) = UPPER(K.CONSTRAINT_NAME) AND UPPER(T.TABLE_SCHEMA) = UPPER(K.TABLE_SCHEMA) AND UPPER(T.TABLE_NAME) = UPPER(K.TABLE_NAME) INNER JOIN information_schema.TABLES T1 ON T.TABLE_NAME=T1.TABLE_NAME AND T.TABLE_SCHEMA=T1.TABLE_SCHEMA WHERE T.CONSTRAINT_TYPE = 'PRIMARY KEY' AND T.TABLE_SCHEMA IN (SELECT DATABASE())";
			} else if ("sqlserver".equalsIgnoreCase(type)) {
				return "SELECT o. NAME TABLE_NAME, c. NAME COLUMN_NAME, cast(g.VALUE as varchar(500)) TABLE_COMMENT, CASE WHEN COLUMNPROPERTY ( o.id, CONVERT (nvarchar(max), g.VALUE), 'IsIdentity' ) = 1 THEN '1' ELSE NULL END AUTO_INCREMENT FROM sysindexes i JOIN sysindexkeys k ON i.id = k.id AND i.indid = k.indid JOIN sysobjects o ON i.id = o.id JOIN syscolumns c ON i.id = c.id AND k.colid = c.colid LEFT JOIN sys.extended_properties g ON ( o.id = g.major_id AND g.minor_id = 0 ) WHERE o.xtype = 'U' AND EXISTS ( SELECT 1 FROM sysobjects WHERE xtype = 'PK' AND NAME = i. NAME ) ORDER BY TABLE_NAME";
			}
		} else if ("selectColumnName".equalsIgnoreCase(sqlType)) {
			if ("ORACLE".equalsIgnoreCase(type)) {
				return "SELECT DISTINCT T.TABLE_NAME, T.COLUMN_NAME, T.DATA_TYPE,T.DATA_LENGTH, T1.COMMENTS,T.DATA_DEFAULT FROM USER_TAB_COLUMNS T INNER JOIN USER_COL_COMMENTS T1 ON T.TABLE_NAME = T1.TABLE_NAME  AND T.COLUMN_NAME = T1.COLUMN_NAME WHERE T.TABLE_NAME=?";
			} else if ("MYSQL".equalsIgnoreCase(type)) {
				return "SELECT DISTINCT T.TABLE_NAME,T.COLUMN_NAME,T.DATA_TYPE,T.CHARACTER_MAXIMUM_LENGTH DATA_LENGTH,T.COLUMN_COMMENT COMMENTS,T.COLUMN_DEFAULT DATA_DEFAULT,T.IS_NULLABLE  NULLABLE FROM information_schema. COLUMNS T WHERE  t.table_schema in (select database()) AND T.TABLE_NAME = ?";
			} else if ("sqlserver".equalsIgnoreCase(type)) {
				return "SELECT d. NAME TABLE_NAME, a. NAME COLUMN_NAME, b. NAME DATA_TYPE, COLUMNPROPERTY (a.id, a. NAME, 'PRECISION') DATA_LENGTH, cast(isnull(g. VALUE, '') as varchar(500)) COMMENTS, REPLACE(REPLACE(REPLACE(isnull(e.text,''),'(',''),')',''),'''','') DATA_DEFAULT, CASE WHEN a.isnullable = 1 then 'YES' ELSE 'NO' END NULLABLE FROM syscolumns a LEFT JOIN systypes b ON a.xusertype = b.xusertype INNER JOIN sysobjects d ON a.id = d.id AND d.xtype = 'U' AND d. NAME <> 'dtproperties' LEFT JOIN syscomments e ON a.cdefault = e.id LEFT JOIN sys.extended_properties g ON a.id = g.major_id AND a.colid = g.minor_id WHERE d. NAME = ?";
			}
		} else if ("selectColumnRef".equalsIgnoreCase(sqlType)) {
			if ("ORACLE".equalsIgnoreCase(type)) {
				return "SELECT DISTINCT A.TABLE_NAME MANY_TABLE_NAME,B.COLUMN_NAME MANY_COLUMN_NAME,A.CONSTRAINT_TYPE MANY_CONSTRAINT_TYPE,C.TABLE_NAME ONE_TABLE_NAME,D.COLUMN_NAME ONE_COLUMN_NAME,C.CONSTRAINT_TYPE ONE_CONSTRAINT_TYPE FROM USER_CONSTRAINTS A LEFT JOIN USER_CONS_COLUMNS B ON A.CONSTRAINT_NAME = B.CONSTRAINT_NAME LEFT JOIN USER_CONSTRAINTS C ON A.R_CONSTRAINT_NAME = C.CONSTRAINT_NAME LEFT JOIN USER_CONS_COLUMNS D ON C.CONSTRAINT_NAME = D.CONSTRAINT_NAME WHERE A.CONSTRAINT_TYPE = 'R' AND C.CONSTRAINT_TYPE = 'P'";
			} else if ("MYSQL".equalsIgnoreCase(type)) {
				return "SELECT DISTINCT t1.TABLE_NAME MANY_TABLE_NAME,t1.COLUMN_NAME MANY_COLUMN_NAME,A.CONSTRAINT_TYPE MANY_CONSTRAINT_TYPE,t1.REFERENCED_TABLE_NAME ONE_TABLE_NAME,t1.REFERENCED_COLUMN_NAME ONE_COLUMN_NAME,t3.CONSTRAINT_TYPE ONE_CONSTRAINT_TYPE FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS A INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE t1 ON A.CONSTRAINT_NAME = t1.CONSTRAINT_NAME AND A.TABLE_NAME = t1.TABLE_NAME INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE t2 ON t1.REFERENCED_TABLE_NAME = t2.TABLE_NAME AND t1.REFERENCED_COLUMN_NAME = t2.COLUMN_NAME INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS t3 ON t2.CONSTRAINT_NAME = t3.CONSTRAINT_NAME AND t2.TABLE_NAME = t3.TABLE_NAME WHERE t3.CONSTRAINT_TYPE = 'PRIMARY KEY' AND A.CONSTRAINT_TYPE = 'FOREIGN KEY' AND A.TABLE_NAME != t1.REFERENCED_TABLE_NAME AND A.CONSTRAINT_SCHEMA IN (SELECT DATABASE())";
			} else if ("sqlserver".equalsIgnoreCase(type)) {
				return "SELECT A.TABLE_NAME MANY_TABLE_NAME, A. NAME MANY_COLUMN_NAME, 'FOREIGN KEY' MANY_CONSTRAINT_TYPE, t2.rtableName ONE_TABLE_NAME, t2. NAME ONE_COLUMN_NAME, 'PRIMARY KEY' ONE_CONSTRAINT_TYPE FROM ( SELECT OBJECT_NAME (col.id) AS TABLE_NAME, col. NAME, f.constid AS temp FROM syscolumns col, sysforeignkeys f WHERE f.fkeyid = col.id AND f.fkey = col.colid AND f.constid IN ( SELECT DISTINCT (id) FROM sysobjects WHERE xtype = 'F' ) ) AS A, ( SELECT OBJECT_NAME (f.rkeyid) AS rtableName, col. NAME, f.constid AS temp FROM syscolumns col, sysforeignkeys f WHERE f.rkeyid = col.id AND f.rkey = col.colid AND f.constid IN ( SELECT DISTINCT (id) FROM sysobjects WHERE xtype = 'F' ) ) AS t2 WHERE A.temp = t2.temp";
			}
		}
		return null;
	}

}
