package com.haxi.mh.utils.db;

import android.content.Context;

import com.haxi.mh.CookieResulteDao;
import com.haxi.mh.DaoMaster;
import com.haxi.mh.DaoSession;
import com.haxi.mh.constant.Constant;
import com.haxi.mh.network.cookie.CookieResulte;
import com.haxi.mh.utils.ui.UIUtil;

import org.greenrobot.greendao.database.Database;
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
    private static MyOpenHelper openHelper = null;
    private final Context context;
    private DaoSession daoSession;

    public CookieUtils() {
        context = UIUtil.getContext();
        openHelper = new MyOpenHelper(context, Constant.DB_NAME);
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
    private Database getWritableDatabase() {
        try {
            if (Constant.DB_RELEASE) {
                return openHelper.getEncryptedWritableDb(Constant.DB_KEY);
            } else {
                return openHelper.getWritableDb();
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 读取数据库
     *
     * @return
     */
    private Database getReadableDatabase() {
        try {
            if (Constant.DB_RELEASE) {
                return openHelper.getEncryptedReadableDb(Constant.DB_KEY);
            } else {
                return openHelper.getReadableDb();
            }
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 关闭数据库会报错。。一般无需关闭
     */
    public void closeConnection() {
        closeHelper();
        colseDaoSession();
    }

    /**
     * 关闭helper
     */
    public void closeHelper() {
        try {
            if (openHelper != null) {
                openHelper.close();
                openHelper = null;
            }
        } catch (Exception e) {

        }
    }

    /**
     * 关闭Session会话层
     */
    public void colseDaoSession() {
        try {
            if (daoSession != null) {
                daoSession.clear();
                daoSession = null;
            }
        } catch (Exception e) {

        }
    }

    /**
     * 保存缓存Cookie
     *
     * @param info
     */
    public void save(CookieResulte info) {
        try {
            DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
            daoMaster.newSession().getCookieResulteDao().insert(info);
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
            DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
            daoMaster.newSession().getCookieResulteDao().delete(info);
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
            DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
            daoMaster.newSession().getCookieResulteDao().update(info);
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
            DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
            QueryBuilder<CookieResulte> builder =  daoMaster.newSession().getCookieResulteDao().queryBuilder();
            List<CookieResulte> list = builder.list();
            if (list != null && list.size() > 0) {
                return list;
            } else {
                return null;
            }
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
            DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
            QueryBuilder<CookieResulte> builder =  daoMaster.newSession().getCookieResulteDao().queryBuilder();
            builder.where(CookieResulteDao.Properties.Url.eq(url));
            List<CookieResulte> list = builder.list();
            if (list != null && list.size() > 0) {
               return list.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

}
