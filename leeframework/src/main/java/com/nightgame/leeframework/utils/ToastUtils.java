package com.nightgame.leeframework.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtils
{
    private static Toast _TextToast;
    private static String _ToastText;
    private static long _TextShowTime;

    public static synchronized void showShortToast(Context context, int resId)
    {
        showToast(context, context.getString(resId), Toast.LENGTH_SHORT);
    }

    public static synchronized void showShortToast(Context context, String text)
    {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static synchronized void showLongToast(Context context, int resId)
    {
        showToast(context, context.getString(resId), Toast.LENGTH_LONG);
    }

    public static synchronized void showLongToast(Context context, String text)
    {
        showToast(context, text, Toast.LENGTH_LONG);
    }

    public static synchronized void showToast(Context context, String text, int duration)
    {
        if (TextUtils.isEmpty(text)) return;
        if (_TextToast != null)
        {
            if (_ToastText.equals(text))
            {
                long interval = System.currentTimeMillis() - _TextShowTime;
                if (interval < 1000 * 2)
                {
                    return;
                }
            }
            _TextToast.setText(text);
        }
        else
        {
            _TextToast = Toast.makeText(context.getApplicationContext(), text, duration);
        }
        _ToastText = text;
        _TextToast.show();
        _TextShowTime = System.currentTimeMillis();
    }
}
