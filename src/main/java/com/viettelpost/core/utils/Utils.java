package com.viettelpost.core.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {

    public static java.sql.Date StringToSqlDate(String d) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date parsed = format.parse(d);
            java.sql.Date sql = new java.sql.Date(parsed.getTime());
            return sql;
        } catch (Exception ex) {

        }
        return null;
    }

    public static boolean isNullOrEmpty(Object input) {
        if (input instanceof String) {
            return input == null || ((String) input).trim().isEmpty();
        }

        if (input instanceof List) {
            return input == null || ((List) input).isEmpty();
        }
        return input == null;
    }

    public static String convertNoUnicodeNormal(String str) {
        try {
            str = str.trim();
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").replaceAll("đ", "d").replaceAll("\u0111", "d").replaceAll("\u0110", "d");
        } catch (Exception e) {
            //ignored
        }
        return "";
    }

    public static String phoneHr(String phone) {
        StringBuilder result = null;
        if (phone != null && phone.length() >= 9) {
            try {
                String tmp = Long.valueOf(phone).toString();
                if (tmp.startsWith("84") && tmp.length() >= 11) {
                    result = new StringBuilder(tmp);
                } else {
                    result = new StringBuilder("84").append(tmp);
                }
            } catch (Exception e) {
            }
        }
        return result == null ? null : result.toString();
    }

    public static String[] validate84Phone(String phone) {
        phone = phone.trim();
        if (phone.startsWith("+")) {
            phone = phone.substring(1);
        }
        if (!StringUtils.isNumeric(phone)) {
            return new String[]{};
        }
        if (phone.startsWith("84") && phone.length() > 9) {
            phone = phone.substring(2);
        }
        if (phone.startsWith("000")) {
            phone = phone.substring(3);
        }
        if (phone.startsWith("00")) {
            phone = phone.substring(2);
        }
        if (phone.startsWith("0")) {
            phone = phone.substring(1);
        }
        if (phone.length() > 10) {
            return new String[]{};
        }
        if ((phone.startsWith("3") || phone.startsWith("5") || phone.startsWith("7") || phone.startsWith("8") || phone.startsWith("9")) && phone.length() != 9) {
            return new String[]{};
        }
        if (phone.length() >= 9 && !phone.startsWith("18") && !phone.startsWith("19")) {
            phone = "84" + phone;
        } else {
            return new String[]{phone, phone};
        }

        return new String[]{phone, phone};
    }

    public static String getValid84Phone(String phone) {
        if (!StringUtils.isNumeric(phone)) {
            return null;
        }
        String[] arr = validate84Phone(phone);
        String str = null;
        if (arr.length > 0 && !isNullOrEmpty(arr[0])) {
            str = arr[0];
            return str;
        }
        return null;
    }
    public static String pwd(String input) {
        if (input == null) {
            return null;
        }
        return DigestUtils.sha256Hex(DigestUtils.md5Hex(input));
    }
    public static String md5(String input) {
        if (input == null) {
            return null;
        }
        return DigestUtils.md5Hex(input);
    }
    public static String sha256(String input) {
        if (input == null) {
            return null;
        }
        return DigestUtils.sha256Hex(input);
    }



    public void writeLogTask(String msg, Date date) throws Exception {
        try {
            File directory = new File(System.getProperty("user.dir") + "/" + "StreamLogs");
            if (!directory.exists()) {
                directory.mkdir();
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            String fileName = formatter.format(date) + "_log.txt";
            File file = new File(directory.getAbsolutePath() + "/" + fileName);

            try (FileWriter myWriter = new FileWriter(file.getAbsoluteFile(), true)) {
                myWriter.write("\n" + "--------------------");
                myWriter.write("\nLog Entry: " + new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss").format(date));
                myWriter.write("; " + msg);
            }
//            System.out.println("Successfully wrote to the file.");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
