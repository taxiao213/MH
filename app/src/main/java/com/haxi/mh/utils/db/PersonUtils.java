package com.haxi.mh.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.haxi.mh.DaoMaster;
import com.haxi.mh.DaoSession;
import com.haxi.mh.PersonDao;
import com.haxi.mh.db.Person;
import com.haxi.mh.utils.ui.UIUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * greenDao数据库工具类
 * Created by Han on 2017/12/16
 * Email:yin13753884368@163.com
 */

public class PersonUtils {

    private static PersonUtils person;
    private String DB_NAME = "person_db";
    private DaoMaster.DevOpenHelper openHelper = null;
    private final Context context;
    private SQLiteDatabase readableDatabase;
    private SQLiteDatabase writableDatabase;

    public PersonUtils() {
        context = UIUtil.getContext();
        openHelper = new DaoMaster.DevOpenHelper(context, DB_NAME);
    }

    public static PersonUtils getInstance() {
        if (person == null) {
            synchronized (PersonUtils.class) {
                if (person == null) {
                    person = new PersonUtils();
                }
            }
        }
        return person;
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
                synchronized (PersonUtils.this) {
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
     * 保存联系人
     *
     * @param info
     */
    public void save(Person info) {
        try {
            if (info != null) {
                DaoSession daoSession = getDaoSession();
                daoSession.getPersonDao().insert(info);
            }
        } catch (Exception e) {

        }
    }


    /**
     * 删除联系人
     *
     * @param info
     */
    public void delete(Person info) {
        try {
            if (info != null) {
                DaoSession daoSession = getDaoSession();
                daoSession.getPersonDao().delete(info);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 更新联系人
     *
     * @param info
     */
    public void update(Person info) {
        try {
            if (info != null) {
                DaoSession daoSession = getDaoSession();
                daoSession.getPersonDao().update(info);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 查询所有联系人
     *
     * @return
     */
    public List<Person> queryAll() {
        try {
            DaoSession daoSession = getDaoSession();
            QueryBuilder<Person> builder = daoSession.getPersonDao().queryBuilder();
            return builder.list();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据name 查询
     *
     * @param name
     * @return
     */
    public List<Person> queryByNodeLevel(String name) {
        try {
            DaoSession daoSession = getDaoSession();
            QueryBuilder<Person> builder = daoSession.getPersonDao().queryBuilder();
            builder.where(PersonDao.Properties.Name.eq(name));
            return builder.list();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据ID 查询升序排列
     *
     * @param ID
     * @return
     */
    public List<Person> queryByID(Long ID) {
        try {
            if (ID == null || ID.equals("")) {
                return null;
            }
            DaoSession daoSession = getDaoSession();
            QueryBuilder<Person> builder = daoSession.getPersonDao().queryBuilder();
            builder.where(PersonDao.Properties.Id.eq(ID));
            builder.orderAsc(PersonDao.Properties.Id);
            return builder.list();
        } catch (Exception e) {
            return null;
        }
    }


}
