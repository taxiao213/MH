package com.haxi.mh.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import com.haxi.mh.R;
import com.haxi.mh.utils.down.Function;
import com.haxi.mh.utils.ui.toast.ToastUtils;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

/**
 * 权限获取工具类
 * Created by Han on 2018/9/5
 */
public class XXPermissionsUtils {

    private static XXPermissionsUtils xxPermissionsUtils;
    public Context context;
    /*读写sd卡权限*/
    private String[] readOrReadPermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /*拍照权限*/
    private String[] cameraPermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    /*录音权限*/
    private String[] recordPermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS};

    public XXPermissionsUtils() {

    }

    public static XXPermissionsUtils getInstances() {
        if (xxPermissionsUtils == null) {
            synchronized (XXPermissionsUtils.class) {
                if (xxPermissionsUtils == null) {
                    xxPermissionsUtils = new XXPermissionsUtils();
                }
            }
        }
        return xxPermissionsUtils;
    }

    /**
     * 拍照权限
     *
     * @return
     */
    public void hasCameraPermission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isHasPermission(context, cameraPermission)) {
            booleanFunction.action(true);
        } else {
            requestPermission(cameraPermission, booleanFunction);
        }
    }

    /**
     * 读写sd卡权限
     *
     * @return
     */
    public void hasReadAndwritePermission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isHasPermission(context, readOrReadPermission)) {
            booleanFunction.action(true);
        } else {
            requestPermission(readOrReadPermission, booleanFunction);
        }
    }

    /**
     * 录音权限
     *
     * @return
     */
    public void hasRecordPermission(Function<Boolean> booleanFunction, Context context) {
        this.context = context;
        if (XXPermissions.isHasPermission(context, recordPermission)) {
            booleanFunction.action(true);
        } else {
            requestPermission(recordPermission, booleanFunction);
        }
    }

    /**
     * 是否有录音权限
     *
     * @return
     */
    public boolean hasRecordPermission(Context context) {
        if (XXPermissions.isHasPermission(context, recordPermission)) {
            return true;
        }
        return false;
    }

    private void requestPermission(String[] permission, final Function<Boolean> booleanFunction) {
        Activity context = (Activity) this.context;
        if (context == null || context.isDestroyed()) return;
        XXPermissions.with(context)
                //.constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES) //支持请求6.0悬浮窗权限8.0请求安装权限
                .permission(permission) //不指定权限则自动获取清单中的危险权限
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            booleanFunction.action(true);
                        } else {
                            booleanFunction.action(false);
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            ToastUtils.showShortToast(XXPermissionsUtils.this.context.getString(R.string.permissions_refuse_authorization));
                            XXPermissions.gotoPermissionSettings(XXPermissionsUtils.this.context);
                        } else {
                            ToastUtils.showShortToast(XXPermissionsUtils.this.context.getString(R.string.permissions_rror));
                        }
                        booleanFunction.action(false);
                    }
                });
    }
}
