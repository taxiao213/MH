package com.haxi.mh.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseFragment;
import com.haxi.mh.service.TestService;
import com.haxi.mh.utils.fileselector.FileSelectActivity;
import com.haxi.mh.utils.fileselector.FileSelectConstant;
import com.haxi.mh.utils.model.LogUtils;
import com.haxi.mh.utils.ui.UIUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
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
    private static final int FILE_SELECT_CODE = 0x101;
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


    @OnClick({R.id.select_pic, R.id.select_camera, R.id.select_file})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_pic:
                select();
                break;
            case R.id.select_camera:
//                openCamera();  com.tencent.mm   com.hr.deanoffice
//                doStartApplicationWithPackageName("com.tencent.mm");
                mActivity.startService(new Intent(mActivity, TestService.class));
                break;
            case R.id.select_file:
                selectFile();
                break;
        }

    }

    /**
     * 唤起其他APP
     * @param packagename
     */
    private void doStartApplicationWithPackageName(String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = mActivity.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = mActivity.getPackageManager().queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            startActivity(intent);
        }
    }



    /**
     * 选择文件
     */
    private void selectFile() {


        Intent intent = new Intent(mActivity, FileSelectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(FileSelectConstant.SELECTOR_REQUEST_CODE_KEY, FileSelectConstant.SELECTOR_MODE_FILE);
        intent.putExtra(FileSelectConstant.SELECTOR_IS_MULTIPLE, true);
        startActivityForResult(intent, FILE_SELECT_CODE);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE) {
            LogUtils.e(REQUEST_CODE_CHOOSE + "....." + Matisse.obtainResult(data).toString());
        }
        if (requestCode == RC_CAMERA_PERM) {
            LogUtils.e(RC_CAMERA_PERM + "");
        }
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                final ArrayList<String> uris = data.getStringArrayListExtra(FileSelectConstant.SELECTOR_BUNDLE_PATHS);
                LogUtils.e(FILE_SELECT_CODE + uris.toString());
            }
        }

    }


    @AfterPermissionGranted(REQUEST_CHOOSE)
    private void select() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(mActivity, perms)) {
            Matisse.from(this)
                    .choose(MimeType.ofImage())
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
