package com.haxi.mh.utils.down;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * 调用相机工具类
 * Created by Han on 2018/9/5
 * Email:yin13753884368@163.com
 */
public class CameraUtils {
    private static String mPicUrl;

    public static void JumpCamera(Context context, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            // 获取图片保存路径
            File imageFile = PictureUtils.createImageFile();
            // 将图片保存到指定文件夹
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            intent.putExtra("return-data", true);
            ((Activity) context).startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void JumpGallery(Context context, int requestCode) {
        Intent iamgeInt = new Intent(Intent.ACTION_PICK, null);
        iamgeInt.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        ((Activity) context).startActivityForResult(iamgeInt, requestCode);
    }


    /**
     * 裁剪图片方法实现 小米手机会出现闪退(已经更换方法)
     *
     * @param uri
     */
    public static Uri startPhotoZoom(Uri uri, Context mContext, int requestCode) {
        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
         * yourself_sdk_path/docs/reference/android/content/Intent.html
         * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
         * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
         */
        Uri uriTempFile = null;
        String mCarrier = android.os.Build.MANUFACTURER;//获取手机厂商
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        if ("xiaomi".equals(mCarrier.toLowerCase()) || "mi".equals(mCarrier.toLowerCase())) {
            //裁剪后的图片Uri路径，uritempFile为Uri类变量
            uriTempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTempFile);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        } else {
            uriTempFile = uri;
            intent.putExtra("return-data", true);
        }
        ((Activity) mContext).startActivityForResult(intent, requestCode);
        return uriTempFile;
    }

}
