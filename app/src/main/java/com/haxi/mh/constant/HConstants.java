package com.haxi.mh.constant;

import android.os.Environment;

import java.io.File;

/**
 * 常量类
 * Created by han on 2018/9/06.
 */

public class HConstants {
    public static final File FILE_SD_ROOT = Environment.getExternalStorageDirectory().getAbsoluteFile();//SD卡目录  注意小米手机必须这样获得public绝对路径
    public static final String FILE_ROOT_NAME = "hrpatient";//根目录名称
    public static final String FILE_SUBFOLDER_NAME = "/personfile";//子文件夹名称
    public static final String FILE_COMPRESS_NAME = FILE_SD_ROOT + "/" + FILE_ROOT_NAME + FILE_SUBFOLDER_NAME + "/";//需要压缩图片的文件路径
    public static final String NAME_SIGN = "hrpatient_";//文件统一名称前缀
    public static final String PIC_SIGN = ".jpg";//图片后缀

}
