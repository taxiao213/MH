package com.haxi.mh.utils.dbutil;


import com.haxi.mh.DBMsgTipDoInfoDao;
import com.haxi.mh.DaoMaster;
import com.haxi.mh.DaoSession;
import com.haxi.mh.MyApplication;
import com.haxi.mh.model.db.DBMsgTipDoInfo;
import com.haxi.mh.utils.im.IMConstants;
import com.haxi.mh.utils.im.IMUtils;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Collections;
import java.util.List;


/**
 * 数据库操作工具类
 * Created by Han on 2018/10/15
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class DBMsgTipUtil {

    private static DBMsgTipUtil msgTipUtil;
    private DBOpenHelper openHelper;

    private DBMsgTipUtil() {

    }

    /**
     * 获取单例
     *
     * @return
     */
    public static DBMsgTipUtil getInstance() {
        if (msgTipUtil == null) {
            synchronized (DBMsgTipUtil.class) {
                if (msgTipUtil == null) {
                    msgTipUtil = new DBMsgTipUtil();
                }
            }
        }
        return msgTipUtil;
    }

    private DBOpenHelper getOpenHelper() {
        if (openHelper == null) {
            synchronized (DBMsgTipUtil.class) {
                if (openHelper == null) {
                    openHelper = new DBOpenHelper(MyApplication.getMyApplication(), IMConstants.DB_NAME);
                }
            }
        }
        return openHelper;
    }

    /**
     * 获取可读数据库
     */
    private Database getReadableDatabase() {
        getOpenHelper();
        if (IMConstants.DB_ENCRYPTION) {
            DBEncrypt.getInstences().encrypt(IMConstants.DB_KEY);
            return openHelper.getEncryptedReadableDb(IMConstants.DB_KEY);
        } else {
            return openHelper.getReadableDb();
        }
    }


    /**
     * 获取可写数据库
     */
    private Database getWritableDatabase() {
        getOpenHelper();
        if (IMConstants.DB_ENCRYPTION) {
            DBEncrypt.getInstences().encrypt(IMConstants.DB_KEY);
            return openHelper.getEncryptedWritableDb(IMConstants.DB_KEY);
        } else {
            return openHelper.getReadableDb();
        }
    }

    /**
     * 获取 DBMsgTipDoInfoDao
     *
     * @return
     */
    private DBMsgTipDoInfoDao getDbMsgTipDoInfoDao() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        return daoSession.getDBMsgTipDoInfoDao();
    }


    /**
     * 存取单个数据
     *
     * @param info
     */
    public void save(DBMsgTipDoInfo info) {
        try {
            DBMsgTipDoInfoDao tipDoInfo = getDbMsgTipDoInfoDao();
            tipDoInfo.insertOrReplace(info);
        } catch (Exception e) {

        }

    }


    /**
     * 更新单个数据
     *
     * @param info
     */

    public void update(DBMsgTipDoInfo info) {
        try {
            DBMsgTipDoInfoDao tipDoInfo = getDbMsgTipDoInfoDao();
            tipDoInfo.update(info);
        } catch (Exception e) {

        }
    }

    /**
     * 删除单个数据
     *
     * @param info
     */
    public void delete(DBMsgTipDoInfo info) {
        try {
            DBMsgTipDoInfoDao tipDoInfo = getDbMsgTipDoInfoDao();
            tipDoInfo.delete(info);
        } catch (Exception e) {

        }
    }

    /**
     * 删除当前表全部数据
     */
    public void deleteAllData() {
        try {
            DBMsgTipDoInfoDao tipDoInfo = getDbMsgTipDoInfoDao();
            tipDoInfo.deleteAll();
        } catch (Exception e) {

        }
    }

    /**
     * 数据库中是否有条数
     */
    public Long DBCount() {
        Long count = 0L;
        try {
            DBMsgTipDoInfoDao tipDoInfo = getDbMsgTipDoInfoDao();
            count = tipDoInfo.count();
        } catch (Exception e) {
            count = 0L;
        }
        return count;
    }

    /**
     * 删除 当前账号和默认文章类数据
     *
     * @param
     */
    public void deleteAccountData() {
        try {
            DBMsgTipDoInfoDao tipDoInfo = getDbMsgTipDoInfoDao();
            List<DBMsgTipDoInfo> tipDoInfos = DBMsgTipUtil.getInstance().queryAllByAccount(IMUtils.getInstance().getAccount());
            if (tipDoInfos != null && tipDoInfos.size() > 0) {
                tipDoInfo.deleteInTx(tipDoInfos);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 查询条件不同，未登陆时查询false的值 登陆不限制查询条件
     * 新闻数据账号(Msgtip_is_login == false) {@link IMConstants#KNOWLEDGE_ACCOUNT}
     *
     * @param qb QueryBuilder<DBMsgTipDoInfo>
     */
    private void isLogin(QueryBuilder<DBMsgTipDoInfo> qb) {
        if (!IMUtils.getInstance().isLogin()) {
            qb.where(DBMsgTipDoInfoDao.Properties.Msgtip_is_login.eq(false));
        }
    }

    /**
     * 排序集合 1.按照是否置顶，当前条目的排序，降序 限制10条 qb.orderDesc(DBMsgTipDoInfoDao.Properties.Msgtip_is_top, DBMsgTipDoInfoDao.Properties.Msgtip_sort_type )
     * 2.按照时间的排序
     *
     * @param qb     QueryBuilder<DBMsgTipDoInfo>对象
     * @param isDesc true降序(lt) false升序(gt)
     */
    private void orderList(QueryBuilder<DBMsgTipDoInfo> qb, Boolean isDesc) {
        if (isDesc) {
            qb.orderDesc(DBMsgTipDoInfoDao.Properties.Msgtip_create_time).limit(IMConstants.MESSAGE_NUM);//按照时间的降序,限制条数
        } else {
            qb.orderAsc(DBMsgTipDoInfoDao.Properties.Msgtip_create_time);//按照时间的升序
        }
    }


    /**
     * 查询所有关联账号数据 qb.where(DBMsgTipDoInfoDao.Properties.Msgtip_relation_account.eq(account));
     *
     * @param account 关联账号
     * @return List<DBMsgTipDoInfo>
     */
    public List<DBMsgTipDoInfo> queryAllByAccount(String account) {
        try {
            DBMsgTipDoInfoDao tipDoInfo = getDbMsgTipDoInfoDao();
            QueryBuilder<DBMsgTipDoInfo> qb = tipDoInfo.queryBuilder();
            qb.whereOr(DBMsgTipDoInfoDao.Properties.Msgtip_relation_account.eq(account), DBMsgTipDoInfoDao.Properties.Msgtip_relation_account.eq(IMConstants.KNOWLEDGE_ACCOUNT));
            return qb.list();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 查询关联账号最近10条数据 带新闻数据账号 {@link IMConstants#KNOWLEDGE_ACCOUNT}
     * 需要考虑分页 10条数据 {@link IMConstants#MESSAGE_NUM}
     *
     * @param account 关联账号
     * @return List<DBMsgTipDoInfo>
     */
    public List<DBMsgTipDoInfo> queryByRelationAccount(String account) {
        try {
            DBMsgTipDoInfoDao tipDoInfo = getDbMsgTipDoInfoDao();
            QueryBuilder<DBMsgTipDoInfo> qb = tipDoInfo.queryBuilder();
            qb.whereOr(DBMsgTipDoInfoDao.Properties.Msgtip_relation_account.eq(account), DBMsgTipDoInfoDao.Properties.Msgtip_relation_account.eq(IMConstants.KNOWLEDGE_ACCOUNT));
            isLogin(qb);
            orderList(qb, true);//降序
            List<DBMsgTipDoInfo> list = qb.list();
            if (list != null && list.size() > 0) {
                Collections.reverse(list);
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 查询关联账号在此时间之前的数据 最近10条数据  带新闻数据账号 {@link IMConstants#KNOWLEDGE_ACCOUNT}
     * 需要考虑分页 10条数据 {@link IMConstants#MESSAGE_NUM}
     *
     * @param account 关联账号
     * @param time    最后一条数据time
     * @param isLess  是否是小于当前时间 true <(lt) 需要将数据reverse ;  false >(gt)
     * @return List<DBMsgTipDoInfo>
     */
    public List<DBMsgTipDoInfo> queryLastMessage(String account, Long time, Boolean isLess) {
        try {
            DBMsgTipDoInfoDao tipDoInfo = getDbMsgTipDoInfoDao();
            QueryBuilder<DBMsgTipDoInfo> qb = tipDoInfo.queryBuilder();
            qb.whereOr(DBMsgTipDoInfoDao.Properties.Msgtip_relation_account.eq(account), DBMsgTipDoInfoDao.Properties.Msgtip_relation_account.eq(IMConstants.KNOWLEDGE_ACCOUNT));
            if (isLess) {
                qb.where(DBMsgTipDoInfoDao.Properties.Msgtip_create_time.lt(time));
            } else {
                qb.where(DBMsgTipDoInfoDao.Properties.Msgtip_create_time.gt(time));
            }
            isLogin(qb);
            if (isLess) {
                orderList(qb, true);
            } else {
                orderList(qb, false);
            }
            List<DBMsgTipDoInfo> list = qb.list();
            if (isLess) {
                if (list != null && list.size() > 0) {
                    Collections.reverse(list);
                }
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 查询关联账号所有未读的消息 带新闻数据账号 {@link IMConstants#KNOWLEDGE_ACCOUNT}
     *
     * @return List<DBMsgTipDoInfo>
     */
    public List<DBMsgTipDoInfo> queryNoReadMessage() {
        try {
            DBMsgTipDoInfoDao tipDoInfo = getDbMsgTipDoInfoDao();
            QueryBuilder<DBMsgTipDoInfo> qb = tipDoInfo.queryBuilder();
            qb.whereOr(DBMsgTipDoInfoDao.Properties.Msgtip_relation_account.eq(IMUtils.getInstance().getAccount()), DBMsgTipDoInfoDao.Properties.Msgtip_relation_account.eq(IMConstants.KNOWLEDGE_ACCOUNT));
            qb.where(DBMsgTipDoInfoDao.Properties.Msgtip_is_read.eq(false));
            return qb.list();
        } catch (Exception e) {
            return null;
        }
    }
}
