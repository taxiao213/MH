package com.haxi.mh.utils.db;

import android.content.Context;

import com.haxi.mh.CookieResulteDao;
import com.haxi.mh.PersonDao;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

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
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        //判断之前的版本
        switch (oldVersion) {
            case 1:
                //CookieResulteDao.createTable(db,false); 创建表 无变动
                CookieResulteDao.createTable(db,false);
            case 2:
                //更新 PersonDao 表字段 增加字段
                MigrationHelper.getInstance().migrate(db, PersonDao.class);
        }
    }
}
