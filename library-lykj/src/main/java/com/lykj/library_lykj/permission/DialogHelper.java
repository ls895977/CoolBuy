package com.lykj.library_lykj.permission;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.lykj.library_lykj.permission.PermissionUtils;
import com.lykj.library_lykj.permission.PermissionUtils.OnRationaleListener.ShouldRequest;

/**
 * 权限Dialog
 */
public class DialogHelper {

    public static void showRationaleDialog(Context context, final ShouldRequest shouldRequest) {
        new AlertDialog.Builder(context)
                .setTitle("注意")
                .setMessage("您拒绝了某些权限,此项功能不能正常工作,是否重新获取?")
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shouldRequest.again(true);
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shouldRequest.again(false);
                    }
                })
                .setCancelable(false)
                .create()
                .show();

    }

    public static void showOpenAppSettingDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("注意")
                .setMessage("您拒绝了某些权限,此项功能不能使用,是否在设置中打开?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionUtils.launchAppDetailsSettings();
                    }
                })
                .setNegativeButton("否", null)
                .setCancelable(false)
                .create()
                .show();
    }

}
