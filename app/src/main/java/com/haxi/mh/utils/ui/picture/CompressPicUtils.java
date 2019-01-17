package com.haxi.mh.utils.ui.picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;


import com.haxi.mh.constant.HConstants;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 图片压缩工具类
 * Created by han on 2018/11/06
 */
public class CompressPicUtils {

    static String path = null;//根据不同的type 保存到不同目录下  1 个人头像图片 2 聊天照片压缩

    /**
     * @param strpath 图片路径
     * @param type    1 需要压缩图片的文件路径   2聊天照片全路径
     */
    public static String getCompToRealPath(String strpath, int type) {
        if (TextUtils.isEmpty(strpath)) {
        } else if (strpath.endsWith("gif")) {
            path = strpath;
        } else {
            try {
                if (type == 1) {
                    path = HConstants.FILE_COMPRESS_NAME;
                }
                if (strpath.contains("/")) {
                    int i = strpath.lastIndexOf("/");
                    String imagePath = strpath.substring(i + 1);
                    Bitmap bt = getImage(strpath, type);
                    int degree = readPictureDegree(strpath);
                    Bitmap rotaingBt = rotaingImageView(degree, bt);
                    path = saveFile(rotaingBt, path, imagePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param srcPath （根据路径获取图片并压缩）
     * @param type
     * @return
     */
    public static Bitmap getImage(String srcPath, int type) {
        Bitmap src = BitmapFactory.decodeFile(srcPath);
        if (src == null) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        int w = options.outWidth;
        int h = options.outHeight;
        float hh = 0f;
        float ww = 0f;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        if (type == 1) {
            hh = 480f;// 这里设置高度为800f---960*540
            ww = 480f;// 这里设置宽度为480f
        } else if (type == 2) {
            hh = 1920f;
            ww = 1080f;
        }
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (options.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (options.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;// 设置缩放比例
        options.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        options.inJustDecodeBounds = false;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        if (!src.isRecycled()) src.recycle();
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }


    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (image != null) {
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩(代表的是压缩率)，把压缩后的数据存放到baos中
        }
        byte[] bytes = baos.toByteArray();
        int options = 100;
        while (bytes.length / 1024 > 200) {//循环判断如果压缩后图片是否大于200kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            if (image != null) {
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            }
            bytes = baos.toByteArray();
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }


    /**
     * 将Bitmap转换成文件
     * 保存文件
     *
     * @param src      源文件
     * @param path     文件路径
     * @param fileName 文件名称
     */
    public static String saveFile(Bitmap src, String path, String fileName) {
        if (src == null) return "";
        BufferedOutputStream bos = null;
        File myCaptureFile = null;
        try {
            File dirFile = new File(path);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            myCaptureFile = new File(path, fileName);
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile();
            }
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            src.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            if (!src.isRecycled()) src.recycle();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return myCaptureFile == null ? "" : myCaptureFile.getAbsolutePath();
    }


    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle  被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        if (bitmap == null) return null;
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }
}
