package com.haxi.mh.ui.fragment;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseFragment;
import com.haxi.mh.ui.widget.Notification;
import com.haxi.mh.utils.ui.UIUtil;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页
 * Created by Han on 2017/12/26
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class HomePageFragment extends BaseFragment {


    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.bt_01)
    Button bt01;
    @BindView(R.id.bt_02)
    Button bt02;


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_homepage;
    }

    @Override
    protected void initView() {
        titleBack.setVisibility(View.GONE);
        titleTv.setText(UIUtil.getString(R.string.homepager_name));
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.bt_01, R.id.bt_02, R.id.bt_03})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_01:
                Notification.getInstances(mActivity).showNotice("测试。。", "2018-01-15 19:39", 22);
                break;
            case R.id.bt_02:
                Notification.getInstances(mActivity).CancelNotification(22);
                //                String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                //                File file1 = new File(rootPath + "/hrchat/personIcon");
                //                File file = null;
                //                if (file1.exists()) {
                //                    file = new File(file1, "050296.png");
                //                }
                //                if (file != null) {
                //                    UploadUtil.getInstance().upload(file);
                //                }
                break;
            case R.id.bt_03:
                int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
                int KEEP_ALIVE_TIME = 1;
                TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
                LinkedBlockingDeque<Runnable> runnables = new LinkedBlockingDeque<>();
                new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES * 2, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
                        runnables, new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

                    }
                });

                getBitmap();
                break;
        }
    }

    private LruCache<String, Bitmap> lruCache;

    private void getBitmap() {
        // 获取可用内存的最大值，使用内存超出这个值将抛出 OutOfMemory 异常。 LruCache 通
        // 过构造函数传入缓存值，以 KB 为单位。
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 把最大可用内存的 1/8 作为缓存空间
        int cacheMemory = maxMemory / 8;
        lruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {

                return value.getByteCount() / 1024;
            }
        };

    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            lruCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return lruCache.get(key);
    }

    public void loadBitmap(int resId, ImageView imageView) {
        final String imageKey = String.valueOf(resId);
        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.mh_icon);
            BitmapWorkTask task = new BitmapWorkTask(imageView);
            task.execute(resId);
        }
    }

    public class BitmapWorkTask extends AsyncTask<Integer, Void, Bitmap> {
        private ImageView mImageView;

        public BitmapWorkTask(ImageView imageView) {
            this.mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Integer... integers) {
            // 1.解码
            // 2.再加入缓存中，每次从缓存获取
            return null;
        }
    }
}
