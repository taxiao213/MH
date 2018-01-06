package com.haxi.mh.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseActivity;
import com.haxi.mh.utils.ui.progress.ProgressDialog;
import com.haxi.mh.utils.ui.view.TextScrollView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 加载H5界面
 * Created by Han on 2018/01/05
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class WebActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final int FILE_SELECT_CODE = 101;
    private static final int RC_REQUEST_DOWM = 105;//下载

    @BindView(R.id.web_title_back)
    ImageView webTitleBack;
    @BindView(R.id.web_title_content)
    TextScrollView webTitleContent;
    @BindView(R.id.web_title)
    RelativeLayout webTitle;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.web_error_iv)
    ImageView webErrorIv;
    @BindView(R.id.web_error_rl)
    RelativeLayout webErrorRl;
    @BindView(R.id.rootLl)
    LinearLayout rootLl;


    private ExecutorService executorService;
    private boolean loadError;//加载是否出错
    public ProgressDialog dialog;
    private String type = null;
    private String keyBack;//H5控制后退键


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_web;
    }

    @Override
    protected void getData() {
        executorService = Executors.newFixedThreadPool(5);
        dialog = new ProgressDialog(mActivity);
        creatWebView();
        if (type != null && type.equals("1")) {
            mWebView.loadUrl("");
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void creatWebView() {
        //声明WebSettings子类
        WebSettings webSettings = mWebView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名 AndroidtoJS类对象映射到js的clickObj对象
        mWebView.addJavascriptInterface(new AndroidtoJs(this), "clickObj");

        //        mWebView.addJavascriptInterface(new AndroidtoJs(this), "test");
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setTextZoom(100);
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setBlockNetworkImage(false);
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setSavePassword(false);//设置不保存密码
        //优先使用缓存:
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);


        //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!WebActivity.this.isFinishing()) {
                    dialog.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dialog.dismiss();
                if (loadError) {
                    mWebView.setVisibility(View.GONE);
                    webErrorRl.setVisibility(View.VISIBLE);
                } else {
                    mWebView.setVisibility(View.VISIBLE);
                    webErrorRl.setVisibility(View.GONE);
                }
            }

            /**
             * 页面加载错误时执行的方法，但是在6.0以下，有时候会不执行这个方法
             * @param view
             * @param errorCode
             * @param description
             * @param failingUrl
             */
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                loadError = true;
            }

        });

        // 由于设置了弹窗检验调用结果,所以需要支持js对话框
        // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
        // 通过设置WebChromeClient对象处理JavaScript的对话框
        // 设置响应js 的Alert()函数
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(WebActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();

                return true;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //判断标题 title 中是否包含有“error”字段，如果包含“error”字段，则设置加载失败，显示加载失败的视图
                if (!TextUtils.isEmpty(title) && title.toLowerCase().contains("error")) {
                    loadError = true;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
    }

    @Override
    public void onBackPressed() {
        onBack(1);
    }

    /**
     * 返回操作
     *
     * @param back
     */
    private void onBack(int back) {
        if (type != null && type.equals("1")) {
            back(back, "http://www.baidu.com");
        } else {
            onFinish(back);
        }
    }

    /**
     * 返回上一级
     *
     * @param back
     * @param url
     */
    private void back(int back, String url) {
        if (!TextUtils.equals("true", keyBack)) {
            if (mWebView.canGoBack()) {
                if (mWebView.getUrl().equals(url)) {
                    onFinish(back);
                } else {
                    mWebView.goBack();
                }
            } else {
                onFinish(back);
            }
        } else {
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:phoneBack()");
                }
            });
            this.keyBack = "";
        }
    }

    /**
     * 返回
     *
     * @param back
     */
    private void onFinish(int back) {
        if (back == 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            rootLl.removeView(mWebView);
            mWebView.destroy();
        }
    }


    /**
     * H5控制后退按键
     *
     * @param back
     */
    public void setKeyBack(String back) {
        if (!TextUtils.isEmpty(back)) {
            this.keyBack = back;
        }
    }


    @OnClick(R.id.web_title_back)
    public void onViewClicked() {
        onBack(2);
    }


    @AfterPermissionGranted(RC_REQUEST_DOWM)
    public void down(String downUrl, String fileName) {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
            EasyPermissions.requestPermissions(this, "请允许权限进行下载", RC_REQUEST_DOWM, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }


}
