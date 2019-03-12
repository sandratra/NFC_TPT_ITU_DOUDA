package com.sharingame.utility;

import com.google.gson.Gson;
import com.sharingame.entity.ShargModel;

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

    public static <T extends ShargModel>T FromJsonDataMapping(Class<T> targetClass, String result) {
        String minimal = result.replace("{\"data\":", "");
        minimal = minimal.substring(0, minimal.length()-2);
        //Log.w("MINIMAL", minimal);
        return new Gson().fromJson(minimal, targetClass);
    }

    public static <T>T FromJsonSimple(Class<T> targetClass, String result) {
        return new Gson().fromJson(result, targetClass);
    }

    public static String ToJsonSimple(Object obj){
        return new Gson().toJson(obj);
    }
}
