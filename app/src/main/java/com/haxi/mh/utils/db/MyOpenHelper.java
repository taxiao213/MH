package com.haxi.mh.utils.db;

import android.content.Context;

import com.haxi.mh.DaoMaster;
import com.haxi.mh.PersonDao;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

import java.util.concurrent.Executors;

import static com.haxi.mh.DaoMaster.SCHEMA_VERSION;

/**
 * 数据库升级策略 每次升级时
 * Created by Han on 2018/3/17
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class MyOpenHelper extends DatabaseOpenHelper {
    public MyOpenHelper(Context context, String name) {
        super(context, name, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
        DaoMaster.createAllTables(db, false);
    }

    @Override
    public void onUpgrade(final Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        //判断之前的版本
        switch (oldVersion) {
            case 1:
            case 2:
                //做相应的处理 传入表的class文件即可
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        MigrationHelper.getInstance().migrate(db, PersonDao.class);
                    }
                });
        }
    }
}
