package com.haxi.mh.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseFragment;
import com.haxi.mh.utils.model.LogUtils;
import com.haxi.mh.utils.ui.UIUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 发现
 * Created by Han on 2017/12/27
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class HomeManageFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CODE_CHOOSE = 0x0001;
    private static final int REQUEST_CHOOSE = 0x0002;
    private static final int RC_CAMERA_PERM = 0x0003;
    private static final int RC_CAMERA_CHOOSE = 0x0004;
    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_tv)
    TextView titleTv;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_homemanager;
    }

    @Override
    protected void initView() {
        titleBack.setVisibility(View.GONE);
        titleTv.setText(R.string.homemanage_name);
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.select_pic,R.id.select_camera})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.select_pic:
                select();
                break;
            case R.id.select_camera:
                openCamera();
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CHOOSE){
            LogUtils.e(REQUEST_CHOOSE+"");
        }
        if (requestCode==RC_CAMERA_PERM){
            LogUtils.e(RC_CAMERA_PERM+"");
        }

    }

    @AfterPermissionGranted(REQUEST_CHOOSE)
    private void select() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(mActivity, perms)) {
            Matisse.from(mActivity)
                    .choose(MimeType.ofImage())
//                    .capture(true)
//                    .captureStrategy(new CaptureStrategy(true,"com.haxi.mh.fileprovider"))
                    .countable(false)
                    .maxSelectable(9)//最大可以选择数
                    .gridExpectedSize(UIUtil.getScreenWidth() / 3)//一行展示的个数
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(1.0f)
                    .theme(R.style.Matisse_Dracula)
                    .imageEngine(new GlideEngine())
                    .forResult(REQUEST_CODE_CHOOSE);
        } else {
            EasyPermissions.requestPermissions(this, "请允许权限读取图片", REQUEST_CHOOSE, perms);
        }
    }

    /**
     * 开启相机
     */
    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void openCamera() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this.getContext(), perms)) {
            String path = Environment.getExternalStorageDirectory().toString() + "/picture";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String capturePath = path + "/" + System.currentTimeMillis() + ".jpg";
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(capturePath)));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, RC_CAMERA_CHOOSE);

        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.open_camera), RC_CAMERA_PERM, perms);
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
