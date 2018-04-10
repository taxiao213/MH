package com.haxi.mh.utils.db;

import android.content.Context;

import com.haxi.mh.DaoMaster;
import com.haxi.mh.DaoSession;
import com.haxi.mh.PersonDao;
import com.haxi.mh.constant.Constant;
import com.haxi.mh.model.db.Person;
import com.haxi.mh.utils.ui.UIUtil;

import org.greenrobot.greendao.database.Database;
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
    private MyOpenHelper openHelper = null;
    private final Context context;
    private DaoSession daoSession;

    public PersonUtils() {
        context = UIUtil.getContext();
        openHelper = new MyOpenHelper(context, Constant.DB_NAME);
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
     * 保存联系人
     *
     * @param info
     */
    public void save(final Person info) {
        try {
            DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
            daoMaster.newSession().getPersonDao().insert(info);
        } catch (Exception e) {

        }
    }


    /**
     * 删除联系人
     *
     * @param info
     */
    public void delete(final Person info) {
        try {
            DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
            daoMaster.newSession().getPersonDao().delete(info);
        } catch (Exception e) {
        }
    }

    /**
     * 更新联系人
     *
     * @param info
     */
    public void update(final Person info) {
        try {
            DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
            daoMaster.newSession().getPersonDao().update(info);
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
            DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
            QueryBuilder<Person> builder = daoMaster.newSession().getPersonDao().queryBuilder();
            List<Person> list = builder.list();
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
     * 根据name 查询
     *
     * @param name
     * @return
     */
    public List<Person> queryByNodeLevel(String name) {
        try {
            DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
            QueryBuilder<Person> builder = daoMaster.newSession().getPersonDao().queryBuilder();
            builder.where(PersonDao.Properties.Name.eq(name));
            List<Person> list = builder.list();
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
     * 根据ID 查询升序排列
     *
     * @param ID
     * @return
     */
    public List<Person> queryByID(Long ID) {
        try {
            DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
            QueryBuilder<Person> builder = daoMaster.newSession().getPersonDao().queryBuilder();
            builder.where(PersonDao.Properties.Id.eq(ID));
            builder.orderAsc(PersonDao.Properties.Id);
            List<Person> list = builder.list();
            if (list != null && list.size() > 0) {
                return builder.list();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

}
