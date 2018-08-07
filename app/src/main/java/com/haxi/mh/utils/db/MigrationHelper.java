package com.haxi.mh.utils.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.stetho.common.LogUtil;
import com.haxi.mh.BuildConfig;
import com.haxi.mh.model.TableColumn;
import com.haxi.mh.model.db.Person;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;

import java.io.File;
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

    /**
     * 数据库备份
     * @param db
     * @param daoClasses
     */
    public void migrate(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
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
     * 恢复数据 如果新建的表中有Integer类型的参数，需要手动赋值，不然会报错，
     * 所以我手动算出需要赋值的参数
     *
     * @param db
     * @param daoClasses
     */

    private void restoreData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {

        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList();
            ArrayList<TableColumn> list = new ArrayList<>();//表中多出的参数
            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;
                if (getColumns(db, tempTableName).contains(columnName)) {
                    properties.add(columnName);

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
                if (iterator.hasNext()) {
                    String next = iterator.next();
                    if (TextUtils.equals(next, "_id")) {
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
                        LogUtil.e("MyOpenHelper  ---", "更新语句===" + stringBuilder.toString());
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
                    properties.add(columnName);

                    String type = null;

                    try {
                        type = getTypeByClass(daoConfig.properties[j].type);
                    } catch (Exception exception) {
                    }

                    createTableStringBuilder.append(divider).append(columnName).append(" ").append(type);

                    if (daoConfig.properties[j].primaryKey) {
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
            Log.e(tableName, e.getMessage(), e);
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
     * 恢复旧数据 数据库表存放地方 String DB_PATH = "/data/data/com.haxi.mh/databases/";
     */
    public void rostoreOldData() {
        String DB_PATH = "/data/data/com.haxi.mh/databases/";
        File db = new File(DB_PATH);
        if (db.exists()) {
            rostoreMsgTextData(db);
        }
    }

    /**
     * 恢复数据库数据 "SELECT name FROM sqlite_master WHERE type='table' and name = 'PERSON' ORDER BY name;" 查找库中所对应的表
     * "SELECT * FROM PERSON;" 查找表中的数据
     *
     * @param db
     */
    private void rostoreMsgTextData(File db) {
        File message_db = new File(db, "person_db");
        if (message_db.exists()) {
            SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(message_db.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
            sqLiteDatabase.beginTransaction();
            try {
                Cursor cursor1 = sqLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table' and name = 'PERSON' ORDER BY name;", null);
                if (cursor1.moveToFirst()) {
                    Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM PERSON;", null);
                    boolean flag = cursor.moveToFirst();
                    ArrayList<Person> list = new ArrayList<>();
                    while (flag) {
                        Person person = new Person();
                        list.add(person);
                        flag = cursor.moveToNext();
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                    for (Person msg : list) {
                        PersonUtils.getInstance().save(msg);
                    }
                    sqLiteDatabase.execSQL("DROP table PERSON");
                }
                if (cursor1 != null) {
                    cursor1.close();
                }
                sqLiteDatabase.setTransactionSuccessful();
            } catch (Exception ignored) {

            } finally {
                sqLiteDatabase.endTransaction();
            }
            sqLiteDatabase.close();

        }
    }
}
