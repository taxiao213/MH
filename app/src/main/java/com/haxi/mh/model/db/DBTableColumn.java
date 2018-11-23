package com.haxi.mh.model.db;

/**
 * 数据库字段
 * Created by Han on 2018/10/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class DBTableColumn {
    String columnName;
    String types;

    public DBTableColumn(String columnName, String types) {
        this.columnName = columnName;
        this.types = types;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }
}
