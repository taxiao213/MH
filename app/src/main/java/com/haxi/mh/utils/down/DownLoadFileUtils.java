package com.haxi.mh.utils.down;

import android.os.Environment;
import android.text.TextUtils;

import com.haxi.mh.constant.HConstants;
import com.haxi.mh.network.HttpService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 下载文件 图片
 * Created by Han on 2017/6/1.
 */

public class DownLoadFileUtils {

    private static DownLoadFileUtils downLoadIconUtil;
    private final OkHttpClient builder;
    private String FOLDER_NAME = null;//根据type 切换存储的文件夹

    public static DownLoadFileUtils getInstance() {
        if (downLoadIconUtil == null) {
            synchronized (DownLoadFileUtils.class) {
                if (downLoadIconUtil == null) {
                    downLoadIconUtil = new DownLoadFileUtils();
                }
            }
        }
        return downLoadIconUtil;
    }

    private DownLoadFileUtils() {
        builder = new OkHttpClient.Builder().build();
    }


    /**
     * 保存文件 图片
     *
     * @param url           下载路径
     * @param file          文件名称
     * @param type          1 个人头像图片  2文件类型
     * @param isCompression 是否需要压缩 只有图片能用上
     */
    public void saveFile(String url, String file, int type, boolean isCompression) {
        if (!TextUtils.isEmpty(url) && url.contains("/")) {
            int indexOf = url.lastIndexOf("/");
            String baseUrl = url.substring(0, indexOf + 1);
            String urlpic = url.substring(indexOf + 1);
            String end = HConstants.PIC_SIGN;
            if (type != 1) {
                if (url.contains(".")) {
                    end = url.substring(url.lastIndexOf("."));
                }

            } else {
                FOLDER_NAME = HConstants.FILE_SUBFOLDER_NAME;
            }
            downFile(baseUrl, urlpic, file, end, isCompression, type);
        }
    }


    /**
     * 保存图片
     *
     * @param baseUrl       跟地址
     * @param urlpic        路径
     * @param fileName      文件名称
     * @param end           后缀名称 包括"."
     * @param isCompression 是否需要压缩 只有图片能用上
     * @param type          压缩保存到不同的路径下
     */
    private void downFile(String baseUrl, String urlpic, String fileName, String end, boolean isCompression, int type) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(builder)
                .build();

        HttpService fileDownloadApi = retrofit.create(HttpService.class);
        try {
            Response<ResponseBody> response = fileDownloadApi.downloadPicWithUrl(urlpic).execute();
            if (response != null && response.isSuccessful()) {
                InputStream is = response.body().byteStream();
                long fileSizeDownloaded = 0;
                if (isSDCardEnable()) {
                    // 首先保存图片
                    File appDir = new File(HConstants.FILE_SD_ROOT, HConstants.FILE_ROOT_NAME + FOLDER_NAME);
                    if (!appDir.exists()) {
                        appDir.mkdirs();
                    }

                    File filePic = new File(appDir, fileName + end);
                    FileOutputStream fos = null;

                    try {
                        fos = new FileOutputStream(filePic);
                        int count = 0;
                        byte[] buffer = new byte[1024];
                        while ((count = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, count);
                            fileSizeDownloaded += count;
                        }
                        fos.flush();

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            fos.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                    }
                    if (response.body().contentLength() != fileSizeDownloaded) {
                        if (filePic != null && filePic.exists()) {
                            filePic.delete();
                        }
                    } else {
                        if (isCompression) {
                            if (filePic != null && filePic.exists()) {
                                String absolutePath = filePic.getAbsolutePath();
                                if (!TextUtils.isEmpty(absolutePath)) {
                                    CompressLocalPicUtils.getCompToRealPath(absolutePath, type);
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}
