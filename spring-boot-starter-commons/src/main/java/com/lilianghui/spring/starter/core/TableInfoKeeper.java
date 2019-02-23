package com.lilianghui.spring.starter.core;

import com.google.code.or.binlog.impl.event.TableMapEvent;
import com.lilianghui.spring.starter.entity.BinlogInfo;
import com.lilianghui.spring.starter.entity.ColumnInfo;
import com.lilianghui.spring.starter.entity.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TableInfoKeeper {

    private Map<Long, TableInfo> tabledIdMap = new ConcurrentHashMap<>();
    private Map<String, List<ColumnInfo>> columnsMap = new ConcurrentHashMap<>();
    private DataSource dataSource;

    public TableInfoKeeper(DataSource dataSource) {
        this.dataSource = dataSource;
        refreshColumnsMap();
    }

    public void saveTableIdMap(TableMapEvent tme) {
        long tableId = tme.getTableId();
        tabledIdMap.remove(tableId);

        TableInfo table = new TableInfo();
        table.setDatabaseName(tme.getDatabaseName().toString());
        table.setTableName(tme.getTableName().toString());
        table.setFullName(tme.getDatabaseName() + "." + tme.getTableName());

        tabledIdMap.put(tableId, table);
    }

    public TableInfo getTableInfo(long tableId) {
        return tabledIdMap.get(tableId);
    }

    public List<ColumnInfo> getColumns(String fullName) {
        return columnsMap.get(fullName);
    }


    public BinlogInfo getBinlogInfo() throws Exception {
        BinlogInfo binlogInfo = new BinlogInfo();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dataSource.getConnection().createStatement();
            resultSet = statement.executeQuery("show master status");
            while (resultSet.next()) {
                binlogInfo.setBinlogName(resultSet.getString("File"));
                binlogInfo.setPosition(resultSet.getLong("Position"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        return binlogInfo;
    }

    public List<BinlogInfo> getBinlogInfos() throws Exception {
        List<BinlogInfo> list = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dataSource.getConnection().createStatement();
            resultSet = statement.executeQuery("show binary logs");
            while (resultSet.next()) {
                BinlogInfo binlogInfo = new BinlogInfo(resultSet.getString("Log_name"), resultSet.getLong("File_size"));
                list.add(binlogInfo);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }


        return list;
    }


    public synchronized void refreshColumnsMap() {
        Map<String, List<ColumnInfo>> cols = new HashMap<>();
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            ResultSet r = metaData.getCatalogs();
            String[] tableType = {"TABLE"};
            while (r.next()) {
                String databaseName = r.getString("TABLE_CAT");
                ResultSet result = metaData.getTables(databaseName, null, null, tableType);
                while (result.next()) {
                    String tableName = result.getString("TABLE_NAME");
//                  System.out.println(result.getInt("TABLE_ID"));
                    String key = databaseName + "." + tableName;
                    ResultSet colSet = metaData.getColumns(databaseName, null, tableName, null);
                    cols.put(key, new ArrayList<ColumnInfo>());
                    while (colSet.next()) {
                        ColumnInfo columnInfo = new ColumnInfo(colSet.getString("COLUMN_NAME"), colSet.getString("TYPE_NAME"));
                        cols.get(key).add(columnInfo);
                    }

                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } finally {

        }
        this.columnsMap = cols;
    }

    public int getMasterServerId() throws Exception {
        int serverId = 0;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dataSource.getConnection().createStatement();
            resultSet = statement.executeQuery("show variables like 'server_id'");
            while (resultSet.next()) {
                serverId = resultSet.getInt("Value");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        return serverId;

    }


}