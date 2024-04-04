package com.viettelpost.core.utils;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

public class CommonUtils {
    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return true;
        }
        return !pattern.matcher(strNum).matches();
    }

    public static String convertPhone(String phone) {
        String converted = "";
        if (StringUtils.isNotEmpty(phone)) {
            if (StringUtils.startsWith(phone, "0"))
                converted = "84" + phone.substring(1);
            if (StringUtils.startsWith(phone, "84"))
                converted = phone;
            if (StringUtils.startsWith(phone, "+84"))
                converted = "84" + phone.substring(3);
            if (StringUtils.startsWith(phone, "9") || StringUtils.startsWith(phone, "5") || StringUtils.startsWith(phone, "3") || StringUtils.startsWith(phone, "7"))
                converted = "84" + phone;
        }
        return converted;
    }

    public static boolean isValidPhone(String phone) {
        if (StringUtils.isEmpty(phone))
            return false;

        boolean isValid = true;

        if (phone.startsWith("+84"))
            isValid = phone.length() == 12 || phone.length() == 13;

        if (!isNumeric(phone)) {
            if (phone.startsWith("84"))
                isValid = phone.length() == 11 || phone.length() == 12;
            else if (phone.startsWith("0"))
                isValid = phone.length() == 10 || phone.length() == 11;
            else if (phone.length() < 9)
                isValid = false;
            else if (phone.length() > 11)
                isValid = false;
            return isValid;

        } else return false;
    }
}
