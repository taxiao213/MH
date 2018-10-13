package com.haxi.mh.utils.down;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import com.haxi.mh.R;
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
    private Context context;
    private String[] readOrReadPermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String[] cameraPermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

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

    private void requestPermission(String[] permission, final Function<Boolean> booleanFunction) {
        Activity context = (Activity) this.context;
        if (context == null || context.isFinishing()) return;
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
                            ToastUtils.showShortToast(context.getString(R.string.permissions_refuse_authorization));
                            XXPermissions.gotoPermissionSettings(context);
                        } else {
                            ToastUtils.showShortToast(context.getString(R.string.permissions_rror));
                        }
                        booleanFunction.action(false);
                    }
                });
    }
}
