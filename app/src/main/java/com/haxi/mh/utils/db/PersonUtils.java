package com.haxi.mh.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.haxi.mh.DaoMaster;
import com.haxi.mh.DaoSession;
import com.haxi.mh.PersonDao;
import com.haxi.mh.model.db.Person;
import com.haxi.mh.utils.ui.UIUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * greenDao数据库工具类
 * Created by Han on 2017/12/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class PersonUtils {

    private static PersonUtils person;
    private String DB_NAME = "person_db";
    private DaoMaster.DevOpenHelper openHelper = null;
    private final Context context;
    private SQLiteDatabase readableDatabase;
    private SQLiteDatabase writableDatabase;
    private DaoSession daoSession;

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
            if (openHelper == null) {
                synchronized (PersonUtils.class) {
                    if (openHelper == null) {
                        openHelper = new DaoMaster.DevOpenHelper(context, DB_NAME);
                    }
                }
            }
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
            if (openHelper == null) {
                synchronized (PersonUtils.class) {
                    if (openHelper == null) {
                        openHelper = new DaoMaster.DevOpenHelper(context, DB_NAME);
                    }
                }
            }
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
     * 关闭数据库的操作，使用完毕数据库，必须执行此操作。
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
     * 保存联系人
     *
     * @param info
     */
    public void save(Person info) {
        try {
            if (info != null) {
                if (daoSession == null) {
                    synchronized (PersonUtils.class) {
                        if (daoSession == null) {
                            daoSession = getDaoSession();
                        }
                    }
                }
                daoSession.getPersonDao().insert(info);
                closeConnection();
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
                if (daoSession == null) {
                    synchronized (PersonUtils.class) {
                        if (daoSession == null) {
                            daoSession = getDaoSession();
                        }
                    }
                }
                daoSession.getPersonDao().delete(info);
                closeConnection();
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
                if (daoSession == null) {
                    synchronized (PersonUtils.class) {
                        if (daoSession == null) {
                            daoSession = getDaoSession();
                        }
                    }
                }
                daoSession.getPersonDao().update(info);
                closeConnection();
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
            if (daoSession == null) {
                synchronized (PersonUtils.class) {
                    if (daoSession == null) {
                        daoSession = getDaoSession();
                    }
                }
            }
            QueryBuilder<Person> builder = daoSession.getPersonDao().queryBuilder();
            closeConnection();
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
            if (daoSession == null) {
                synchronized (PersonUtils.class) {
                    if (daoSession == null) {
                        daoSession = getDaoSession();
                    }
                }
            }
            QueryBuilder<Person> builder = daoSession.getPersonDao().queryBuilder();
            builder.where(PersonDao.Properties.Name.eq(name));
            closeConnection();
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
            if (daoSession == null) {
                synchronized (PersonUtils.class) {
                    if (daoSession == null) {
                        daoSession = getDaoSession();
                    }
                }
            }
            QueryBuilder<Person> builder = daoSession.getPersonDao().queryBuilder();
            builder.where(PersonDao.Properties.Id.eq(ID));
            builder.orderAsc(PersonDao.Properties.Id);
            closeConnection();
            return builder.list();
        } catch (Exception e) {
            return null;
        }
    }


}
