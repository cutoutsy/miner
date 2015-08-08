package miner.spider.utils;

import java.util.List;

/**
 * Created by cutoutsy on 7/24/15.
 */
public class StringOperatorUtil {
    public static String getValue(String source, String default_value) {
        if (source == null || source.trim().length() == 0) {
            return default_value;
        }
        return source;
    }

    public static boolean isBlank(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isBlankCollection(List list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        return true;
    }

    public static boolean isNotBlankCollection(List list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        return true;
    }
}
