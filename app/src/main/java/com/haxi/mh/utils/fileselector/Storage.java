package com.haxi.mh.utils.fileselector;


import com.haxi.mh.utils.model.SdCardUtil;

import java.io.File;
import java.io.Serializable;
import java.util.Locale;

/**
 * 文件名称置换
 * Created by Han on 2018/1/5
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class Storage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String absPath;// 全路径
    private String pathName;// 文件名
    private String localName;// 转换成“内部存储”这样的名称

    /**
     * 构造函数
     *
     * @param absPath 存储设置全路径
     */
    public Storage(String absPath) {
        super();
        this.absPath = absPath;
        setPathNameByAbsPath(absPath);
        setLocalNameByAbsPath(absPath);
    }

    public String getAbsPath() {
        return absPath;
    }

    public void setAbsPath(String absPath) {
        this.absPath = absPath;
    }

    private void setPathNameByAbsPath(String absPath) {
        File file = new File(absPath);
        this.pathName = file.getName();
    }

    private void setLocalNameByAbsPath(String absPath) {
        String path = absPath.toLowerCase(Locale.CHINA);
        if (path.equals(SdCardUtil.getInnerExterPath().toLowerCase(Locale.CHINA))) {
            this.localName = FileSelectConstant.SELECTOR_STORAGE_INNDER;
        } else if (path.contains("sd") || path.contains("sdcard")) {
            this.localName = FileSelectConstant.SELECTOR_STORAGE_OUTTER + "[" + this.pathName + "]";
        } else if (path.contains("usb")) {
            this.localName = FileSelectConstant.SELECTOR_STORAGE_USB + "[" + this.pathName + "]";
        } else {
            this.localName = FileSelectConstant.SELECTOR_STORAGE_OTHER + "[" + this.pathName + "]";
        }

    }

    public String getPathName() {
        return pathName;
    }

    public String getLocalName() {
        return localName;
    }
}
