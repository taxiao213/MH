package com.haxi.mh.ui.fragment;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.base.BaseFragment;
import com.haxi.mh.constant.HConstants;
import com.haxi.mh.model.db.Person;
import com.haxi.mh.service.TestService;
import com.haxi.mh.ui.adapter.HomeManagerAdapter;
import com.haxi.mh.utils.XXPermissionsUtils;
import com.haxi.mh.utils.animation.AnimationUtils;
import com.haxi.mh.utils.down.CameraUtils;
import com.haxi.mh.utils.down.DownLoadFileUtils;
import com.haxi.mh.utils.down.Function;
import com.haxi.mh.utils.down.PictureUtils;
import com.haxi.mh.utils.down.UpLoadFileUtils;
import com.haxi.mh.utils.fileselector.FileSelectActivity;
import com.haxi.mh.utils.fileselector.FileSelectConstant;
import com.haxi.mh.utils.model.LogUtils;
import com.haxi.mh.utils.net.NetUtils;
import com.haxi.mh.utils.ui.UIUtil;
import com.haxi.mh.utils.ui.toast.ToastUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 发现
 * Created by Han on 2017/12/27
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class HomeManageFragment extends BaseFragment {

    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.tv_ceshi)
    TextView tvCeshi;
    @BindView(R.id.ry)
    RecyclerView ry;
    @BindView(R.id.ry1)
    RecyclerView ry1;
    private ArrayList<Person> list;
    private ArrayList<Person> list1;
    private HomeManagerAdapter adapter;
    private HomeManagerAdapter adapter1;
    private boolean isExpand = false;
    private int height = 150;


    private static final int REQUEST_CODE_CHOOSE = 0x0001;
    private static final int REQUEST_CHOOSE = 0x0002;
    private static final int FILE_SELECT_CODE = 0x105;

    // 照相机
    private static final int ACTION_IMAGE_CAPTURE = 0x107;
    // 图库
    private static final int ACTION_IMAGE_GALLERY = 0X108;
    // 剪切图片
    private static final int ROUNDCUTRESULT = 0X109;
    private Uri uriTempFile;
    // 存储用的bitmap
    private Bitmap bitmap;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_homemanager;
    }

    @Override
    protected void initView() {
        titleBack.setVisibility(View.GONE);
        titleTv.setText(R.string.homemanage_name);
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Person person = new Person();
            person.setName(i + "");
            list1.add(person);
        }


        adapter = new HomeManagerAdapter(mActivity, list);
        ry.setLayoutManager(new LinearLayoutManager(mActivity));
        ry.setAdapter(adapter);


        adapter1 = new HomeManagerAdapter(mActivity, list1);
        ry1.setLayoutManager(new LinearLayoutManager(mActivity));
        ry1.setAdapter(adapter1);

    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.select_pic1, R.id.select_pic, R.id.select_camera, R.id.select_file, R.id.tv_ceshi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_pic1:
                select();
                break;
            case R.id.select_pic:
                XXPermissionsUtils instance = XXPermissionsUtils.getInstances();
                instance.hasReadAndwritePermission(new Function<Boolean>() {
                    @Override
                    public void action(Boolean var) {
                        if (var != null && var) {
                            CameraUtils.JumpGallery(mActivity, ACTION_IMAGE_GALLERY);
                        }
                    }
                }, mActivity);
                break;
            case R.id.select_camera:
                XXPermissionsUtils instances = XXPermissionsUtils.getInstances();
                instances.hasCameraPermission(new Function<Boolean>() {
                    @Override
                    public void action(Boolean var) {
                        if (var != null && var) {
                            CameraUtils.JumpCamera(mActivity, ACTION_IMAGE_CAPTURE);
                        }
                    }
                }, mActivity);
                mActivity.startService(new Intent(mActivity, TestService.class));
                break;
            case R.id.select_file:
                XXPermissionsUtils instan = XXPermissionsUtils.getInstances();
                instan.hasReadAndwritePermission(new Function<Boolean>() {
                    @Override
                    public void action(Boolean var) {
                        if (var != null && var) {
                            selectFile();
                        }
                    }
                }, mActivity);
                break;
            case R.id.tv_ceshi:
                animation();
                break;
        }
    }

    private void animation() {
        list.clear();
        list.addAll(list1);
        adapter.notifyDataSetChanged();
        isExpand = !isExpand;
        AnimationUtils.startAnimation(ry, height * list.size(), isExpand);
    }


    /**
     * 唤起其他APP
     *
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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTION_IMAGE_CAPTURE:
                    PictureUtils.galleryAddPic(mActivity, PictureUtils.mCurrentPhotoPath);
                    // 图片裁剪
                    File file = new File(PictureUtils.mCurrentPhotoPath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    uriTempFile = CameraUtils.startPhotoZoom(Uri.fromFile(file), mActivity, ROUNDCUTRESULT);
                    break;
                case ACTION_IMAGE_GALLERY:
                    if (data != null && !"".equals(data)) {
                        Uri imageuri = data.getData();
                        uriTempFile = CameraUtils.startPhotoZoom(imageuri, mActivity, ROUNDCUTRESULT);
                    }
                    break;
                case ROUNDCUTRESULT:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        bitmap = bundle.getParcelable("data");
                        UploadPic();
                    } else {
                        try {
                            if (uriTempFile == null) {
                                return;
                            }
                            UploadPic();
                        } catch (Exception e) {
                        }
                    }
                    break;

                case FILE_SELECT_CODE:
                    final ArrayList<String> uris = data.getStringArrayListExtra(FileSelectConstant.SELECTOR_BUNDLE_PATHS);
                    LogUtils.e(FILE_SELECT_CODE + uris.toString());
                    break;
                case REQUEST_CODE_CHOOSE:
                    List<Uri> mSelected = Matisse.obtainResult(data);
                    LogUtils.e(FILE_SELECT_CODE + mSelected.toString());
                    break;
            }
        }
    }

    /**
     * 上传图片
     */
    private void UploadPic() {
        if (uriTempFile != null) {
            String picPath = PictureUtils.getPicPath(mActivity, uriTempFile);
            if (!TextUtils.isEmpty(picPath)) {
                if (!NetUtils.isNetworkConnected(mActivity)) {
                    ToastUtils.showShortToast(getString(R.string.net_error));
                    return;
                }

                UpLoadFileUtils.updateLoadFile(picPath, 1, new Function<String>() {
                    @Override
                    public void action(final String url) {
                        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
                            String account = HConstants.ACCOUNT;
                            DownLoadFileUtils.getInstance().saveFile(url, HConstants.NAME_SIGN + account, 1, true);
//                            PictureUtils.loadPersonIcon(mActivity, ivAvatar);
                        }
                        if (TextUtils.equals(url, "0")) {
                            ToastUtils.showShortToast(getString(R.string.net_error));
                        }
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                });
            } else {
                uploadError(getString(R.string.upload_pic_error));
            }
        } else {
            uploadError(getString(R.string.upload_pic_error));
        }
    }


    /**
     * 上传提示语
     *
     * @param string
     */
    private void uploadError(String string) {
        ToastUtils.showShortToast(string);
    }


    private void select() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

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
    }
}
