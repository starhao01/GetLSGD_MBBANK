package com.viettelpost.core.base;

import org.apache.log4j.Logger;

import javax.crypto.SecretKey;
import javax.net.ssl.*;
import java.io.Serializable;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Constants implements Serializable {

    public final static String login = "login";
    public final static String userKey = "vsaUserToken";
    public final static String accTypeKey = "vsaAccountType";
    public final static String SaltKey ="@aa1df3194fb61c48d14ae095ec580480";

    public static ResourceBundle rb;
    private static Logger log = Logger.getLogger(Constants.class);

    public static String service;
    public static String ssoUrl;
    public static String domainCode;

    static {
        try {
            rb = ResourceBundle.getBundle("cas");
            service = rb.getString("service");
            ssoUrl = rb.getString("ssoUrl");
            domainCode = rb.getString("domainCode");
        } catch (MissingResourceException var1) {
            log.error("loading cas failed:", var1);
        }

    }

    public static String getStringConfig(String key) {
        try {
            return rb.getString(key);
        } catch (MissingResourceException var2) {
            log.debug("Init parameter " + key + " ERROR:" + var2.getMessage());
            return "";
        }
    }

    static {
        disableSslVerification();
    }

    private static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
