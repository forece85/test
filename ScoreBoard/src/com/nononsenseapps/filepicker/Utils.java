package com.nononsenseapps.filepicker;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class Utils {
    private static final String SEP = "/";

    public static boolean isValidFileName(@Nullable String name) {
        return (TextUtils.isEmpty(name) || name.contains(SEP) || name.equals(".") || name.equals("..")) ? false : true;
    }

    @NonNull
    public static String appendPath(@NonNull String first, @NonNull String second) {
        String result = first + SEP + second;
        while (result.contains("//")) {
            result = result.replaceAll("//", SEP);
        }
        if (result.length() <= 1 || !result.endsWith(SEP)) {
            return result;
        }
        return result.substring(0, result.length() - 1);
    }
}
