package com.haxi.mh.utils.dbutil;

import android.content.Context;


import com.haxi.mh.DaoMaster;
import com.haxi.mh.utils.model.LogUtils;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

import java.util.concurrent.Executors;

import static com.haxi.mh.DaoMaster.SCHEMA_VERSION;


/**
 * 数据库升级策略
 * Created by Han on 2018/10/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class DBOpenHelper extends DatabaseOpenHelper {
    public DBOpenHelper(Context context, String name) {
        super(context, name, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
        DaoMaster.createAllTables(db, false);
    }

    @Override
    public void onUpgrade(final Database db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1://方法预留 哪个表增删字段，传入那个类，记得发送广播刷新界面
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.e("DBOpenHelper  --- onUpgrade");
//                        DBMigrationHelper.getInstance().migrate(db, MsgTipDoInfoDao.class);
//                        UIUtil.getContext().sendBroadcast(new Intent(Contents.ACTION_MESSAGE_REFRESH));
                    }
                });
        }
    }
}
