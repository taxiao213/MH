package com.haxi.mh.mvp.view;

import com.haxi.mh.mvp.base.IBaseView;

/**
 * 微信支付view 接口
 * Created by Han on 2018/8/29
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public interface IWxView extends IBaseView {
    void setText(Long log);

    void setAdapter(Object object);
}
