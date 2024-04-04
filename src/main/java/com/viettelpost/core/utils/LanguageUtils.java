package com.viettelpost.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

//@Component
public class LanguageUtils {

    public final static String LANG_VI = "vi-VN";
    public final static String LANG_EN = "en-US";

    @Autowired
    MessageSource messageSource;

    public String getMessage(String languageHeader, String key) {
        String langTag = LANG_VI;
        if (Utils.isNullOrEmpty(languageHeader) || LANG_VI.equals(languageHeader)) {
            langTag = LANG_VI;
        }
        if (LANG_EN.equals(languageHeader)) {
            langTag = LANG_EN;
        }
        String outMessage = messageSource.getMessage(key, null, Locale.forLanguageTag(langTag));
        if (Utils.isNullOrEmpty(outMessage)) {
            outMessage = messageSource.getMessage("common.system.error", null, Locale.getDefault());
        }
        return outMessage;
    }

    String getHeaderLanguage() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return request.getHeader("Language");
        }
        return null;
    }

    public String getLanguageTag() {
        String languageHeader = getHeaderLanguage();
        if (Utils.isNullOrEmpty(languageHeader) || LANG_VI.equals(languageHeader)) {
            languageHeader = LANG_VI;
        }
        if (LANG_EN.equals(languageHeader)) {
            languageHeader = LANG_EN;
        }
        return languageHeader;
    }

    public String getMessageByKey(String key) {
        String languageHeader = getHeaderLanguage();
        return getMessage(languageHeader == null ? LANG_VI : languageHeader, key);
    }

}
