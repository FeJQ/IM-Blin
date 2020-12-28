package com.fejq.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class UtilAes
{
    private static final String KEY_AES = "AES";

    /**
     * AES加密
     * @param src 明文
     * @param key 密钥
     * @return 密文
     * @throws Exception
     */
    public static String encrypt(String src, byte[] key) throws Exception
    {
        if (key == null || key.length != 16)
        {
            throw new Exception("key不满足条件");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(key, KEY_AES);
        Cipher cipher = Cipher.getInstance(KEY_AES);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(src.getBytes());
        return byte2hex(encrypted);
    }

    /**
     * AES解密
     * @param src 密文
     * @param key 密钥
     * @return 明文
     * @throws Exception
     */
    public static String decrypt(String src, byte[] key) throws Exception
    {
        if (key == null || key.length != 16)
        {
            throw new Exception("key不满足条件");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(key, KEY_AES);
        Cipher cipher = Cipher.getInstance(KEY_AES);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] encrypted1 = hex2byte(src);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        return originalString;
    }

    /**
     * 随机生成128bit密钥
     *
     * @return 128bit密钥
     */
    public static byte[] generateKey()
    {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16]; // 128 bits are converted to 16 bytes;
        random.nextBytes(bytes);
        return bytes;
    }

    private static byte[] hex2byte(String strhex)
    {
        if (strhex == null)
        {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1)
        {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++)
        {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    private static String byte2hex(byte[] b)
    {
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < b.length; n++)
        {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
            {
                hs.append("0").append(stmp);
            }
            else
            {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }


}
