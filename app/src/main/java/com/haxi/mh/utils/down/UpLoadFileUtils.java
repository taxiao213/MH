package com.haxi.mh.utils.down;

import android.text.TextUtils;

import com.haxi.mh.utils.ui.toast.ToastUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.Executors;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 上传文件的工具类
 * Created by Han on 2018/9/5
 * Email:yin13753884368@163.com
 * type 切换不同的地址
 */
public class UpLoadFileUtils {

    private final static String baseUrl ="http://ww.baidu.com";
    /* 上传我的头像action */
    private final static String myPicaction = "savePicAction/saveMyPic.action";

    private static String action = null;

    /**
     * @param imgPath
     * @param type     1 上传个人头像
     * @param consumer
     */
    public static void updateLoadFile(final String imgPath, final int type, final Function<String> consumer) {
        if (TextUtils.isEmpty(imgPath)) return;
        if (type == 1) {
            action = myPicaction;
        } else {

        }
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String upload = upload(baseUrl + action, new File(imgPath), type);
                try {
                    if (upload != null) {
                        JSONObject object = new JSONObject(upload);
                        String resMsg = object.getString("resMsg");
                        if (object.has("resCode")) {
                            String code = object.getString("resCode");
                            if (TextUtils.equals(code, "2") || TextUtils.equals(code, "-2")) {
                                if (resMsg != null) {
                                    ToastUtils.showShortToast(resMsg);
                                }
                                if (TextUtils.equals(code, "2")) {
                                    String url = object.getString("data");
                                    consumer.action(url);
                                } else {
                                    consumer.action("0");
                                }
                            } else {
                                consumer.action("0");
                            }
                        } else {
                            consumer.action("0");
                        }
                    } else {
                        consumer.action("0");
                    }
                } catch (Exception e) {
                    consumer.action("0");
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * @param url  上传地址
     * @param file 文件
     * @param type action不一样替换不同的参数
     * @return
     */
    private static String upload(final String url, File file, int type) {
        /**
         * 表头:     "Content-Disposition","form-data; name=\"mFile\"; filename=\"" + "file" + "\""
         * name=\"mFile\"  与后台接口保持一致
         */
        OkHttpClient mOkHttpClient = new OkHttpClient
                .Builder()
                .build();
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"mFile\"; filename=\"" + "file" + "\""), fileBody);
        if (type == 1) {
            String flieType = "jpg";
            String name = file.getName();
            if (!TextUtils.isEmpty(name)) {
                if (name.contains(".")) {
                    flieType = name.substring(name.lastIndexOf(".") + 1);
                }
            }
            builder.addFormDataPart("imgType", flieType);
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        Response response = null;
        try {
            response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }
        return null;
    }

}
