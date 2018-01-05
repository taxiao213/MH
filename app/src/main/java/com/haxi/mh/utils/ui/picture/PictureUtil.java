package com.haxi.mh.utils.ui.picture;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.haxi.mh.R;
import com.haxi.mh.utils.ui.UIUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

/**
 * 图片工具类
 * Created by Han on 2018/1/5
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class PictureUtil {

    private static final int REQUEST_CODE_CHOOSE = 102;

    /**
     * 返回照片的uri 版本号大于19
     * if (Build.VERSION.SDK_INT >= 19) {
     * returnOnKitKatPath(mSelected);
     * } else {
     * returnPath(mSelected);
     * }
     *
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String returnOnKitKatPath(Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(UIUtil.getContext(), uri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = returnPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = returnPath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            path = returnPath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            path = uri.getPath();
        }
        return path;
    }

    /**
     * 获取照片的路径 版本号小于19
     *
     * @param uri       照片uri
     * @param selection 未用到
     * @return
     */
    public static String returnPath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection老获取真实的图片路径
        Cursor cursor = UIUtil.getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 相册的选择 Matisse实现单选 多选 预览
     */
    public static void select(String domid) {
        // 配置和启用
        Matisse.from((Activity) UIUtil.getContext())
                .choose(MimeType.ofImage())
                .countable(false)
                .maxSelectable(9)
                .gridExpectedSize(UIUtil.getScreenWidth() / 3)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(1.0f)
                .theme(R.style.Matisse_Dracula)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }
}
