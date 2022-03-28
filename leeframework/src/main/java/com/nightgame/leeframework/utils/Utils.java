package com.nightgame.leeframework.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

import java.io.File;
import java.util.UUID;

public class Utils
{
    private static final String _Tag = "Unity";
    private static boolean _IsShowLog = false;
    private static Activity _MainAct = null;
    private static Application _MainApp = null;
    private static UnityPlayer _UnityPlayer = null;
    private static PackageManager _PackageMgr = null;

    public static void init(Application app, Activity act, UnityPlayer unityPlayer)
    {
        _MainApp = app;
        _MainAct = act;
        _UnityPlayer = unityPlayer;
        _PackageMgr = _MainAct.getPackageManager();
    }

    //region 日志

    public static void showLog(boolean isShow)
    {
        _IsShowLog = isShow;
    }

    public static void log(String msg)
    {
        if (_IsShowLog)
        {
            Log.d(_Tag, msg);
        }
    }

    public static void warring(String msg)
    {
        if (_IsShowLog)
        {
            Log.w(_Tag, msg);
        }
    }

    public static void error(String msg)
    {
        if (_IsShowLog)
        {
            Log.e(_Tag, msg);
        }
    }

    //endregion

    //region 系统接口

    /**
     * 获取设备信息
     */
    public static String getDeviceInfo()
    {
        //String sDescFormat = "{\"BOARD\":\"%s\",\"BRAND\":\"%s\", \"DEVICE\":\"%s\", \"DISPLAY\":\"%s\", \"MANUFACTURER\":\"%s\", \"MODEL\":\"%s\", \"PRODUCT\":\"%s\"}";
        String descFormat = "{$BOARD$:$%s$,$BRAND$:$%s$, $DEVICE$:$%s$, $DISPLAY$:$%s$, $MANUFACTURER$:$%s$, $MODEL$:$%s$, $PRODUCT$:$%s$}";
        String deviceInfo = String.format(descFormat, android.os.Build.BOARD, android.os.Build.BRAND, android.os.Build.DEVICE, android.os.Build.DISPLAY,
                android.os.Build.MANUFACTURER, android.os.Build.MODEL, android.os.Build.PRODUCT);
        log("GetDeviceInfo : " + deviceInfo);
        return deviceInfo;
    }

    /**
     * 获取UUID
     */
    public static String getUUID()
    {
        try
        {
            String androidId = Settings.System.getString(_MainAct.getContentResolver(), Settings.System.ANDROID_ID);
            String serialNumber = Build.SERIAL;
            log("androidId : " + androidId + "  serialNumber : " + serialNumber);
            return new UUID(androidId.hashCode(), Build.SERIAL.hashCode()).toString();
        }
        catch (Exception ex)
        {
            error(ex.toString());
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * 获取SDCard路径
     */
    public static String getSDCardPath()
    {
        try
        {
            File sdcardDir = Environment.getExternalStorageDirectory();
            return sdcardDir.getPath();
        }
        catch (Exception ex)
        {
            error(ex.toString());
            ex.printStackTrace();
            return "";
        }
    }
    //endregion

    //region 工具

    /**
     * 拷贝字符到粘贴板
     */
    public static void copyTxtToClipboard(final String text)
    {
        _MainAct.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                ClipboardManager clipboardManager = (ClipboardManager) _MainAct.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("playerId", text);
                clipboardManager.setPrimaryClip(clipData);
            }
        });
    }

    /**
     * 从粘贴板获取字符
     */
    public static String getTxtFromClipboard()
    {
        ClipboardManager clipboardManager = (ClipboardManager) _MainAct.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboardManager.getPrimaryClip();
        ClipData.Item item = clipData.getItemAt(0);
        return item.getText().toString();
    }

    /**
     * 跳转到应用商店
     */
    public static void gotoAppStore(String marketUrl, String webUrl)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(marketUrl));
        if (intent.resolveActivity(_PackageMgr) != null)
        {
            _MainAct.startActivity(intent);
        }
        else
        {
            if (webUrl != "")
            {
                intent.setData(Uri.parse(webUrl));
                if (intent.resolveActivity(_PackageMgr) != null)
                {
                    _MainAct.startActivity(intent);
                }
            }
        }
    }

    /**
     * 重绘界面
     */
    public static void redrawView()
    {
        //重新绘制view,解决oppo渠道支付页切回游戏半个屏幕不渲染的问题
        _UnityPlayer.getView().invalidate();
    }

    //endregion

}
