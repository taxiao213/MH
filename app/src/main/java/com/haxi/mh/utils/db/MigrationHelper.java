package com.haxi.mh.utils.db;

import android.database.Cursor;
import android.text.TextUtils;

import com.facebook.stetho.common.LogUtil;
import com.haxi.mh.BuildConfig;
import com.haxi.mh.model.TableColumn;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 数据迁移的策略
 * Created by Han on 2018/3/17
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class MigrationHelper {

    private static final String CONVERSION_CLASS_NOT_FOUND_EXCEPTION =
            "MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT PARAMETERS";

    private static MigrationHelper migrationHelper;

    private MigrationHelper() {

    }

    public static MigrationHelper getInstance() {
        if (migrationHelper == null) {
            synchronized (MigrationHelper.class) {
                if (migrationHelper == null) {
                    migrationHelper = new MigrationHelper();
                }
            }
        }
        return migrationHelper;
    }


    public void migrate(Database db, Class<? extends AbstractDao<?, ?>> daoClasses) {
        //1. 备份
        generateTempTables(db, daoClasses);
        //2. 只删除需要更新的表 DaoMaster.dropAllTables(db, true);
        deleteTables(db, daoClasses);
        //3. 只创建需要更新的表 DaoMaster.createAllTables(db, false);
        createTables(db, daoClasses);
        //4. 恢复数据
        restoreData(db, daoClasses);
    }


    /**
     * 恢复数据
     *
     * @param db
     * @param daoClasses
     */

    private void restoreData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        String primarykey = null;//约束键 insert 时不能select

        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList();
            ArrayList<TableColumn> list = new ArrayList<>();//表中多出的参数
            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;
                if (getColumns(db, tempTableName).contains(columnName)) {
                    columnName = resultColumnName(columnName);
                    properties.add(columnName);
                    if (daoConfig.properties[j].primaryKey) {
                        primarykey = columnName;
                    }
                } else {
                    //表中多出的参数
                    String type = null;
                    try {
                        type = getTypeByClass(daoConfig.properties[j].type);
                        list.add(new TableColumn(columnName, type));
                    } catch (Exception exception) {
                    }
                }
            }
            if (properties != null && properties.size() > 0) {
                Iterator<String> iterator = properties.iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    if (TextUtils.equals(primarykey, next)) {
                        //(2018.08.06) UNIQUE constraint failed: MSG_TIP_DO_INFO._id 所以删掉_id
                        iterator.remove();
                    }
                }
            }

            StringBuilder insertTableStringBuilder = new StringBuilder();
            insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");
            StringBuilder dropTableStringBuilder = new StringBuilder();
            dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);
            try {
                db.execSQL(insertTableStringBuilder.toString());
                db.execSQL(dropTableStringBuilder.toString());

                if (list != null && list.size() > 0) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    for (int j = 0; j < list.size(); j++) {
                        TableColumn tableColumn = list.get(j);
                        if (TextUtils.equals(tableColumn.getTypes(), "INTEGER") || TextUtils.equals(tableColumn.getTypes(), "BOOLEAN")) {
                            arrayList.add(tableColumn.getColumnName());
                        }
                    }
                    if (arrayList != null && arrayList.size() > 0) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("UPDATE ").append(tableName).append(" SET");
                        for (int j = 0; j < arrayList.size(); j++) {
                            String name = arrayList.get(j);
                            stringBuilder.append(" ");
                            stringBuilder.append(name);
                            stringBuilder.append(" = ");
                            stringBuilder.append(0);
                            if (j == arrayList.size() - 1) {
                                stringBuilder.append(";");
                            } else {
                                stringBuilder.append(",");
                            }
                        }
                        db.execSQL(stringBuilder.toString());
                        LogUtil.e("MyOpenHelper  ---", "更新语句 stringBuilder ===" + stringBuilder.toString());
                        if (arrayList.contains("IS_READ_MESSAGE")) {
                            StringBuilder st = new StringBuilder();
                            st.append("UPDATE ").append(tableName).append(" SET IS_READ_MESSAGE = 1;");
                            db.execSQL(st.toString());
                            LogUtil.e("MyOpenHelper  ---", "更新语句 st ===" + st.toString());
                        }
                    }
                    LogUtil.e("MyOpenHelper  ---", "添加的参数 list.size =" + list.toString());
                }
            } catch (Exception e) {
                LogUtil.e("MyOpenHelper  ---", "错误" + e.getMessage() + "  添加的参数 list.size =" + list.toString());
            }
        }
        LogUtil.e("MyOpenHelper----", "restoreData 恢复数据");
    }


    /**
     * 创建表
     *
     * @param db
     * @param daoClasses
     */
    private void createTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        String buildType = BuildConfig.BUILD_TYPE;
        if (TextUtils.equals(buildType, "debug")) {
            for (Class<? extends AbstractDao<?, ?>> daoClass : daoClasses) {
                try {
                    Method method = daoClass.getMethod("createTable", Database.class, boolean.class);
                    method.invoke(null, db, false);
                } catch (Exception e) {
                    LogUtil.e("MyOpenHelper---- createTables debug e == " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = 0; i < daoClasses.length; i++) {
                DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
                String tableName = daoConfig.tablename;
                StringBuilder createTableStringBuilder = new StringBuilder();
                createTableStringBuilder.append("CREATE TABLE ").append(tableName).append(" (");
                String divider = "";
                try {
                    for (int j = 0; j < daoConfig.properties.length; j++) {
                        String columnName = daoConfig.properties[j].columnName;
                        columnName = resultColumnName(columnName);
                        String type = null;
                        type = getTypeByClass(daoConfig.properties[j].type);
                        createTableStringBuilder.append(divider).append(columnName).append(" ").append(type);
                        if (daoConfig.properties[j].primaryKey) {
                            createTableStringBuilder.append(" PRIMARY KEY");
                        }
                        divider = ",";
                    }
                    createTableStringBuilder.append(");");
                    db.execSQL(createTableStringBuilder.toString());
                    LogUtil.e("MyOpenHelper  ---createTables release sql== " + createTableStringBuilder.toString());
                } catch (Exception e) {
                    LogUtil.e("MyOpenHelper  ---createTables release e == " + e.getMessage());
                }
            }
        }
    }


    /**
     * 删除表
     *
     * @param db
     * @param daoClasses
     */
    private void deleteTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        String buildType = BuildConfig.BUILD_TYPE;
        if (TextUtils.equals(buildType, "debug")) {
            for (Class<? extends AbstractDao<?, ?>> daoClass : daoClasses) {
                try {
                    Method method = daoClass.getMethod("dropTable", Database.class, boolean.class);
                    method.invoke(null, db, true);
                } catch (Exception e) {
                    LogUtil.e("MyOpenHelper---- debug deleteTables e == " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = 0; i < daoClasses.length; i++) {
                try {
                    DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
                    StringBuilder dropTableStringBuilder = new StringBuilder();
                    String tableName = daoConfig.tablename;
                    dropTableStringBuilder.append("DROP TABLE ").append(tableName);
                    db.execSQL(dropTableStringBuilder.toString());
                    LogUtil.e("MyOpenHelper---- release deleteTables sql== " + dropTableStringBuilder.toString());
                } catch (Exception e) {
                    LogUtil.e("MyOpenHelper---- release deleteTables e == " + e.getMessage());
                }
            }
        }
    }

    /**
     * 备份表
     *
     * @param db
     * @param daoClasses
     */
    private void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        String primarykey = null;//约束键 insert 时不能select

        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String divider = "";
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList<>();
            StringBuilder createTableStringBuilder = new StringBuilder();
            createTableStringBuilder.append("CREATE TABLE ").append(tempTableName).append(" (");

            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;
                if (getColumns(db, tableName).contains(columnName)) {
                    columnName = resultColumnName(columnName);
                    properties.add(columnName);
                    String type = null;
                    try {
                        type = getTypeByClass(daoConfig.properties[j].type);
                    } catch (Exception exception) {
                    }
                    createTableStringBuilder.append(divider).append(columnName).append(" ").append(type);
                    if (daoConfig.properties[j].primaryKey) {
                        primarykey = columnName;
                        createTableStringBuilder.append(" PRIMARY KEY");
                    }
                    divider = ",";
                }
            }
            createTableStringBuilder.append(");");
            try {
                db.execSQL(createTableStringBuilder.toString());
                LogUtil.e("MyOpenHelper  ---", "generateTempTables create sql== " + createTableStringBuilder.toString());
            } catch (Exception e) {
                LogUtil.e("MyOpenHelper  ---", "generateTempTables create e== " + e.getMessage());
            }
            if (properties != null && properties.size() > 0) {
                Iterator<String> iterator = properties.iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    if (TextUtils.equals(primarykey, next)) {
                        iterator.remove();
                    }
                }
            }
            StringBuilder insertTableStringBuilder = new StringBuilder();
            insertTableStringBuilder.append("INSERT INTO ").append(tempTableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(" FROM ").append(tableName).append(";");
            try {
                db.execSQL(insertTableStringBuilder.toString());
                LogUtil.e("MyOpenHelper  ---", "generateTempTables insert sql== " + insertTableStringBuilder.toString());
            } catch (Exception e) {
                LogUtil.e("MyOpenHelper  ---", "generateTempTables insert e== " + e.getMessage());
            }
        }
    }

    /**
     * 获取列数
     *
     * @param db
     * @param tableName
     * @return
     */
    private static List<String> getColumns(Database db, String tableName) {
        List<String> columns = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 1", null);
            if (cursor != null) {
                columns = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
            }
        } catch (Exception e) {
            LogUtil.e("MyOpenHelper  ---getColumns", e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return columns;
    }

    private String getTypeByClass(Class<?> type) throws Exception {
        if (type.equals(String.class)) {
            return "TEXT";
        }
        if (type.equals(int.class) || type.equals(Long.class) || type.equals(Integer.class) || type.equals(long.class)) {
            return "INTEGER";
        }
        if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return "BOOLEAN";
        }

        Exception exception =
                new Exception(CONVERSION_CLASS_NOT_FOUND_EXCEPTION.concat(" - Class: ").concat(type.toString()));
        throw exception;
    }

    /**
     * arr关键字 替换
     *
     * @param columnName
     */
    String[] arr = new String[]{"FROM", "TO", "TIME", "CONTENT", "NAME", "SELECT", "INSERT", "DELETE", "UPDATE", "ADD", "VISIBLE", "TYPE"};
    List<String> list = Arrays.asList(arr);

    private String resultColumnName(String columnName) {
        if (list.contains(columnName)) {
            columnName = "`" + columnName + "`";
        }
        return columnName;
    }


}
