package com.haxi.mh;

import com.haxi.mh.network.cookie.CookieResulte;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class CookieResulteTest extends AbstractDaoTestLongPk<CookieResulteDao, CookieResulte> {

    public CookieResulteTest() {
        super(CookieResulteDao.class);
    }

    @Override
    protected CookieResulte createEntity(Long key) {
        CookieResulte entity = new CookieResulte();
        entity.setId(key);
        return entity;
    }

}
