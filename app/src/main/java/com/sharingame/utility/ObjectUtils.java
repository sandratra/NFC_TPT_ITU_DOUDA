package com.sharingame.utility;

public abstract class ObjectUtils {
    public static String joinString(String[] array, String separator) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0, il = array.length; i < il; i++) {
            if (i > 0)
                sbStr.append(separator);
            sbStr.append(array[i]);
        }
        return sbStr.toString();
    }
}
