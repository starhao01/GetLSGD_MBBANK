package com.viettelpost.core.base;

public class Parser {
    public static int tryParseInt(String msg, int defaultVal) {
        try {
            return Integer.valueOf(msg);
        } catch (Exception e) {

        }
        return defaultVal;
    }
}
