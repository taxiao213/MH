package com.haxi.mh.utils.fileselector;

import android.app.Activity;
import android.content.Intent;

import com.haxi.mh.utils.ui.UIUtil;

/**
 * 选择文件的工具类
 * Created by Han on 2018/1/6
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class FileSelectUtil {

    private static final int FILE_SELECT_CODE = 0x101;

    /**
     * 选择文件
     * 在onActivityResult 返回
     * 用法：ArrayList<String> uris = data.getStringArrayListExtra(FileSelectConstant.SELECTOR_BUNDLE_PATHS);
     */
    public static void fileSelect() {
        //自定义文件选择器
        Intent intent = new Intent(UIUtil.getContext(), FileSelectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(FileSelectConstant.SELECTOR_REQUEST_CODE_KEY, FileSelectConstant.SELECTOR_MODE_FILE);
        intent.putExtra(FileSelectConstant.SELECTOR_IS_MULTIPLE, true);
        Activity context = (Activity) UIUtil.getContext();
        context.startActivityForResult(intent, FILE_SELECT_CODE);
    }
}
