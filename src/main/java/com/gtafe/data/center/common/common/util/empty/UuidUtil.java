package com.gtafe.data.center.common.common.util.empty;

import java.util.UUID;

public class UuidUtil {
    public static String getUuid() {
        String uuid = "";
        uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }
}
