package com.fejq.utils;

import android.media.Image;

import com.fejq.blin.App;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ImageUtil
{
    /**
     * 将接收的字符串转换成图片保存
     *
     * @param imgStr  二进制流转换的字符串
     * @param imgPath 图片的保存路径
     * @param imgName 图片的名称
     * @return 1：保存正常
     * 0：保存失败
     */
    public static int saveToImgByStr(String imgStr, String imgPath, String imgName)
    {
        int stateInt = 1;
        if (imgStr != null && imgStr.length() > 0)
        {
            try
            {
                // 将字符串转换成二进制，用于显示图片
                // 将上面生成的图片格式字符串 imgStr，还原成图片显示
                byte[] imgByte = hex2byte(imgStr);
                InputStream in = new ByteArrayInputStream(imgByte);
                File file = new File(imgPath, imgName);//可以是任何图片格式.jpg,.png等
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int nRead = 0;
                while ((nRead = in.read(b)) != -1)
                {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                fos.close();
                in.close();

            }
            catch (Exception e)
            {
                stateInt = 0;
                e.printStackTrace();
            }
            finally
            {
            }
        }
        return stateInt;
    }

    /**
     * 将二进制转换成图片保存
     *
     * @param imgFile  二进制流转换的字符串
     * @param imgPath 图片的保存路径
     * @param imgName 图片的名称
     * @return 1：保存正常
     * 0：保存失败
     */
    public static int saveToImgByBytes(File imgFile, String imgPath, String imgName)
    {

        int stateInt = 1;
        if (imgFile.length() > 0)
        {
            try
            {
                File file = new File(imgPath, imgName);//可以是任何图片格式.jpg,.png等
                FileOutputStream fos = new FileOutputStream(file);

                FileInputStream fis = new FileInputStream(imgFile);

                byte[] b = new byte[1024];
                int nRead = 0;
                while ((nRead = fis.read(b)) != -1)
                {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                fos.close();
                fis.close();

            }
            catch (Exception e)
            {
                stateInt = 0;
                e.printStackTrace();
            }
            finally
            {
            }
        }
        return stateInt;
    }

    /**
     * 字符串转二进制
     *
     * @param str 要转换的字符串
     * @return 转换后的二进制数组
     */
    public static byte[] hex2byte(String str)
    { // 字符串转二进制
        if (str == null)
            return null;
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1)
            return null;
        byte[] b = new byte[len / 2];
        try
        {
            for (int i = 0; i < str.length(); i += 2)
            {
                b[i / 2] = (byte) Integer
                        .decode("0X" + str.substring(i, i + 2)).intValue();
            }
            return b;
        }
        catch (Exception e)
        {
            return null;
        }
    }

}
