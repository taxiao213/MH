package com.haxi.mh.utils.down;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haxi.mh.MyApplication;
import com.haxi.mh.R;
import com.haxi.mh.constant.HConstants;
import com.haxi.mh.utils.net.NetUtils;
import com.zhihu.matisse.engine.GlideApp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 加载图片 图库 的工具类
 * Created by Han on 2018/9/5
 * Email:yin13753884368@163.com
 */
public class PictureUtils {
    /* 图片路径 */
    public static String mCurrentPhotoPath;
    public static final String FILE_NAME = "hrpatient";

    /**
     * 添加到图库
     */
    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取保存图片的目录
     *
     * @return
     */
    public static File getAlbumDir() {
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!externalStoragePublicDirectory.exists()) {
            externalStoragePublicDirectory.mkdirs();
        }
        File dir = new File(externalStoragePublicDirectory, FILE_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }


    /**
     * 把程序拍摄的照片放到 SD卡的 Pictures目录中 sheguantong 文件夹中
     * 照片的命名规则为：honry_20130125_173729.jpg
     *
     * @return
     * @throws IOException
     */
    @SuppressLint("SimpleDateFormat")
    public static File createImageFile() throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = format.format(new Date());
        String imageFileName = FILE_NAME + "_" + timeStamp + ".jpg";
        File image = new File(getAlbumDir(), imageFileName);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public static String getImagePath(Context context, Uri uri, String selection) {
        String path = null;
        //通过Uri和selection获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    /**
     * 根据路径返回照片名称
     *
     * @param picturePath
     * @return
     */
    public static String getPicNameFromPath(String picturePath) {
        String temp[] = picturePath.replaceAll("\\\\", "/").split("/");
        String fileName = "";
        if (temp.length > 1) {
            fileName = temp[temp.length - 1];
        }
        return fileName;
    }

    /**
     * 返回照片的uri
     *
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String returnPath(Context context, Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = PictureUtils.getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = PictureUtils.getImagePath(context, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            path = PictureUtils.getImagePath(context, uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            path = uri.getPath();
        }
        return path;
    }


    /**
     * 获取照片路径
     * 兼容高低版本
     *
     * @return
     */
    public static String getPicPath(Context context, Uri uri) {
        String imagePath = null;
        if (Build.VERSION.SDK_INT >= 19) {
            if (uri != null) {
                imagePath = returnPath(context, uri);
            }
        } else {
            if (uri != null) {
                imagePath = getImagePath(context, uri, null);
            }
        }
        return imagePath;
    }

    /**
     * 根据路径删除图片
     *
     * @param path
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }


    /**
     * 加载Bitmap
     *
     * @param mContext
     * @param bitmap
     * @param mImageView
     */
    public static void loadBitmapPic(Context mContext, Bitmap bitmap, ImageView mImageView) {
        GlideApp.with(mContext)
                .load(bitmap)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mImageView);
    }

    /**
     * 加载Uri图片
     *
     * @param mContext
     * @param uri
     * @param mImageView
     */
    public static void loadUriPic(Context mContext, Uri uri, ImageView mImageView) {
        GlideApp.with(mContext)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mImageView);
    }

    /**
     * 加载Url图片 有缓存
     *
     * @param mContext
     * @param url
     * @param mImageView
     */
    public static void loadUrlPic(Context mContext, String url, ImageView mImageView) {
        GlideApp.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(mImageView);
    }

    /**
     * 加载Url图片 有缓存
     *
     * @param mContext
     * @param url
     * @param mImageView
     */
    public static void loadUrlPic(Context mContext,String url, int holderIcon, ImageView mImageView) {
        GlideApp.with(mContext)
                .load(url)
                .placeholder(holderIcon)
                .error(holderIcon)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(mImageView);
    }

    /**
     * 加载本地图片 不做缓存处理
     *
     * @param mContext
     * @param file
     * @param mImageView
     */
    public static void loadFilePic(Context mContext, File file, ImageView mImageView) {
        GlideApp.with(mContext)
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mImageView);
    }

    /**
     * 加载个人头像 1
     *
     * @param mContext
     * @param mImageView
     */
    public static void loadPersonIcon(final Context mContext, final ImageView mImageView) {
        if (android.os.Process.myTid() == MyApplication.getMainThreadId()) {
            loadIcon(mContext, mImageView);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    loadIcon(mContext, mImageView);
                }
            });
        }

    }

    /**
     * 加载个人头像 2
     *
     * @param mContext
     * @param mImageView
     */
    private static void loadIcon(Context mContext, ImageView mImageView) {
        if (NetUtils.isNetworkConnected(mContext)) {
            String userPhoto = HConstants.USERPHOTO;
            if (!TextUtils.isEmpty(userPhoto)) {
                PictureUtils.loadUrlPic(mContext, userPhoto, mImageView);
            } else {
                loadLocalPersonIcon(mContext, mImageView);
            }
        } else {
            loadLocalPersonIcon(mContext, mImageView);
        }
    }

    /**
     * 加载个人头像 3 本地
     *
     * @param mContext
     * @param mImageView
     */
    private static void loadLocalPersonIcon(Context mContext, ImageView mImageView) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = new File(HConstants.FILE_COMPRESS_NAME, HConstants.NAME_SIGN + HConstants.ACCOUNT + HConstants.PIC_SIGN);
            if (file != null && file.exists()) {
                PictureUtils.loadFilePic(mContext, file, mImageView);
            } else {
                loadLocalError(mContext, mImageView);
            }
        } else {
            loadLocalError(mContext, mImageView);
        }
    }

    /**
     * /**
     * 加载个人头像 4 失败
     *
     * @param mContext
     * @param mImageView
     */
    public static void loadLocalError(Context mContext, ImageView mImageView) {
        GlideApp.with(mContext)
                .load(R.drawable.kong)
                .placeholder(R.drawable.kong)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mImageView);
    }
}
