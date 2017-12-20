package com.haxi.mh.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.haxi.mh.CookieResulteDao;
import com.haxi.mh.DaoMaster;
import com.haxi.mh.DaoSession;
import com.haxi.mh.network.cookie.CookieResulte;
import com.haxi.mh.utils.ui.UIUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * greenDao数据库工具类 读取缓存Cookie
 * Created by Han on 2017/12/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class CookieUtils {

    private static CookieUtils cookieUtils;
    private String DB_NAME = "cookie_db";
    private DaoMaster.DevOpenHelper openHelper = null;
    private final Context context;
    private SQLiteDatabase readableDatabase;
    private SQLiteDatabase writableDatabase;

    public CookieUtils() {
        context = UIUtil.getContext();
        openHelper = new DaoMaster.DevOpenHelper(context, DB_NAME);
    }

    public static CookieUtils getInstance() {
        if (cookieUtils == null) {
            synchronized (CookieUtils.class) {
                if (cookieUtils == null) {
                    cookieUtils = new CookieUtils();
                }
            }
        }
        return cookieUtils;
    }

    /**
     * 读取数据库
     *
     * @return
     */
    private SQLiteDatabase getWritableDatabase() {
        try {
            writableDatabase = openHelper.getWritableDatabase();
            return writableDatabase;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 读取数据库
     *
     * @return
     */
    private SQLiteDatabase getReadableDatabase() {
        try {
            readableDatabase = openHelper.getReadableDatabase();
            return readableDatabase;
        } catch (Exception e) {
            return null;
        }
    }

    private DaoSession getDaoSession() {
        try {
            if (writableDatabase == null) {
                synchronized (CookieUtils.this) {
                    if (writableDatabase == null) {
                        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME);
                        writableDatabase = devOpenHelper.getWritableDatabase();
                    }
                }
            }
            DaoMaster daoMaster = new DaoMaster(writableDatabase);
            return daoMaster.newSession();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 保存缓存Cookie
     *
     * @param info
     */
    public void save(CookieResulte info) {
        try {
            if (info != null) {
                DaoSession daoSession = getDaoSession();
                daoSession.getCookieResulteDao().insert(info);
            }
        } catch (Exception e) {

        }
    }


    /**
     * 删除缓存Cookie
     *
     * @param info
     */
    public void delete(CookieResulte info) {
        try {
            if (info != null) {
                DaoSession daoSession = getDaoSession();
                daoSession.getCookieResulteDao().delete(info);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 更新缓存Cookie
     *
     * @param info
     */
    public void update(CookieResulte info) {
        try {
            if (info != null) {
                DaoSession daoSession = getDaoSession();
                daoSession.getCookieResulteDao().update(info);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 查询所有缓存Cookie
     *
     * @return
     */
    public List<CookieResulte> queryAll() {
        try {
            DaoSession daoSession = getDaoSession();
            QueryBuilder<CookieResulte> builder = daoSession.getCookieResulteDao().queryBuilder();
            return builder.list();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据url 查询
     *
     * @param url
     * @return
     */
    public CookieResulte queryByUrl(String url) {
        try {
            DaoSession daoSession = getDaoSession();
            QueryBuilder<CookieResulte> builder = daoSession.getCookieResulteDao().queryBuilder();
            builder.where(CookieResulteDao.Properties.Url.eq(url));
            if (builder != null && builder.list().size() > 0) {
                return builder.list().get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


}
