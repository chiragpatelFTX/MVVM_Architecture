package com.ftx.mvvm_template.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Contains utility methods to perform manipulation on string
 */
public class StringUtils {
    public static final String EMPTY = "";

    /**
     * To check out that given string is null or empty after trimming
     *
     * @param aString String to check
     * @return true if String is empty even if it is pad with white space
     */
    public static boolean isTrimmedEmpty(String aString) {
        return aString == null || aString.trim().length() == 0;
    }

    /**
     * To check is it a valid email id or not.
     *
     * @param email email address to validate
     * @return true if valid email else false
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * This method is used to validate pattern
     *
     * @param asPattern : Here, we will pass a pattern which we need to map.
     * @param asTarget  : this is a string which will going to be map with pattern.
     * @return : return true if pattern matches otherwise it will return false.
     */
    public final static boolean validatePattern(String asPattern, String asTarget) {
        Pattern pattern = Pattern.compile(asPattern);
        return pattern.matcher(asTarget).matches();
    }

    /**
     * Purpose : This method will check does both string contents are equals or not with case.
     * @param s : string 1 which we need to check.
     * @param s1 :  string 2 which we need to check.
     * @return : true if same else false.
     */
    public static boolean isEquals(String s, String s1) {
        return s.equals(s1);
    }
}
