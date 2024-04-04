package com.viettelpost.core.base;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class Utils {

    public static Date dateToDateFormat(String date, String strFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(strFormat);
            java.util.Date parsed = format.parse(date);
            java.sql.Date sql = new java.sql.Date(parsed.getTime());
            return sql;
        } catch (Exception ex) {

        }
        return null;
    }

    public String sendVTManNotify(String endpointURL, String token, String params) throws Exception {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(endpointURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(500);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("token", token);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.write(params.getBytes("UTF-8"));
            os.flush();
            BufferedReader br;
            if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
                br = new BufferedReader(new InputStreamReader((conn.getInputStream()), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader((conn.getErrorStream()), "UTF-8"));
            }
            StringBuilder output = new StringBuilder();
            String tmp;
            while ((tmp = br.readLine()) != null) {
                if (!tmp.isEmpty()) {
                    output.append(tmp);
                }
            }
            return output.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
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

    public static String[] validate84Phone(String phone) {
        phone = phone.trim();
        if (phone.startsWith("+")) {
            phone = phone.substring(1);
        }
        if (!Utils.isNumber(phone)) {
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
        if (!Utils.isNumber(phone)) {
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

    public static boolean isNumber(String input) {
        try {
            Long.valueOf(input);
            return true;
        } catch (Exception e) {
            //ignored
        }
        return false;
    }
}
