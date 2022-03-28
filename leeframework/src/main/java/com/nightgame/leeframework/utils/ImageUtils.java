package com.nightgame.leeframework.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class ImageUtils
{
    public static String getImagePath(Activity activity, Uri uri)
    {
        if (uri == null)
        {
            return null;
        }
        String path = null;
        final String scheme = uri.getScheme();
        if (null == scheme)
        {
            path = uri.getPath();
        }
        else if (ContentResolver.SCHEME_FILE.equals(scheme))
        {
            path = uri.getPath();
        }
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme))
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
            int nPhotoColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor != null)
            {
                cursor.moveToFirst();
                path = cursor.getString(nPhotoColumn);
            }
            cursor.close();
        }
        return path;
    }

    public static Bitmap getDiskBitmap(String pathString)
    {
        Bitmap getBbitmap = null;
        try
        {
            File file = new File(pathString);
            if (file.exists())
            {
                getBbitmap = BitmapFactory.decodeFile(pathString);
            }
        }
        catch (Exception e)
        {
            Utils.error("getDiskBitmap error : " + e.toString());
        }
        return getBbitmap;
    }

    public static Bitmap byteToBitmap(byte[] b)
    {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    public static byte[] bitmapToBytes(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        return baos.toByteArray();
    }

    public static void openPhotoAlbum(Activity activity)
    {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        //activity.startActivityForResult(intent, PlatformProto.Req_PhotoPicth);
    }

    /**
     * 保存图片到相册
     */
    public static boolean saveToPhotoAlbum(Activity activity, byte[] picture)
    {
        String storePath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera";
        File appDir = new File(storePath);
        if (!appDir.exists())
        {
            appDir.mkdir();
        }

        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);

        Bitmap bitmap = byteToBitmap(picture);
        try
        {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
            fos.flush();
            fos.close();

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            activity.getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Utils.log("Success");
            if (isSuccess)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            Utils.log("Failed ： " + e.toString());
            e.printStackTrace();
        }
        return false;
    }
}
