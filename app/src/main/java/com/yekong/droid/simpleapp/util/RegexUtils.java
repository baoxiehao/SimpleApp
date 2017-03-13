package com.yekong.droid.simpleapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by baoxiehao on 17/3/18.
 */

public class RegexUtils {

    public static String matchJoin(final String string, final String regex) {
        return matchJoin(string, regex, "");
    }

    public static String matchJoin(final String string, final String regex, final String joiner) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < matcher.groupCount(); i++) {
                if (sb.length() > 0) {
                    sb.append(joiner);
                }
                sb.append(matcher.group(i + 1));
            }
            return sb.toString();
        }
        return string;
    }
}
