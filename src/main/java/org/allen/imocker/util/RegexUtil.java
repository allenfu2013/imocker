package org.allen.imocker.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式
 */
public class RegexUtil {

    public static boolean isMatch(String regexString, String text) {
        Pattern pattern = Pattern.compile(regexString);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

}
