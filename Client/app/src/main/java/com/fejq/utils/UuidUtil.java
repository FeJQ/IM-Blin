package com.fejq.utils;

import java.util.UUID;

public class UuidUtil
{
    public static String make()
    {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }
}
