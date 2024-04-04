package com.viettelpost.core.base;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.viettelpost.core.controller.response.LoginResponse;
import com.viettelpost.core.services.dtos.UserTokenDto;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApisUtils implements Serializable {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    ObjectMapper mapper;
    protected Client client;

    public ApisUtils() {
        ClientConfig config = new DefaultClientConfig();
        client = Client.create(config);
        client.setReadTimeout(30000);
        client.setConnectTimeout(30000);

        mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    HttpURLConnection getConnect(String url) throws Exception {
        HttpURLConnection conn = null;
        URL realUrl = new URL(null, url);
        if (realUrl.getProtocol().toLowerCase().equals("https")) {
            HttpsURLConnection https = (HttpsURLConnection) realUrl.openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            conn = https;
        } else {
            conn = (HttpURLConnection) realUrl.openConnection();
        }
        return conn;
    }

    public boolean logout(String ticket) throws Exception {
        BufferedReader in = null;
        HttpURLConnection conn = null;
        if (Utils.isNullOrEmpty(ticket)) {
            return true;
        }
        try {
            conn = getConnect(Constants.ssoUrl + "/" + ticket);
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            if (conn.getResponseCode() == 200 || conn.getResponseCode() == 403) {
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage(), ex);
            }
        }
        return false;
    }


    public UserTokenDto loginByAPIs(String params) throws Exception {

        String result = "";
        OutputStreamWriter out = null;
        BufferedReader in = null;
        HttpURLConnection conn = null;
        try {
            conn = getConnect(Constants.ssoUrl);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(params);
            out.flush();
            if (conn.getResponseCode() == 200 || conn.getResponseCode() == 201) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else if (conn.getResponseCode() == 401) {
                throw new VtException("Tài khoản hoặc mật khẩu không hợp lệ");
            } else {
                throw new IOException("INTERNAL_SERVER_ERROR");
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage(), ex);
            }
        }
        String token = result;
        result = EncryptionUtil.getJsonData(result);
        LoginResponse rs = mapper.readValue(result, LoginResponse.class);
        UserTokenDto user = new UserTokenDto();
        user.setUserId(Long.valueOf(rs.get1UserId()));
        user.setDeptId(Long.valueOf(rs.get1DeptId()));
        user.setDeptName("");
        user.setEmail(rs.get1Email());
        user.setTelephone(rs.get1PhoneNumber());
        user.setFullName(rs.get1FullName());
        user.setStaffCode(rs.get1StaffCode());
        user.setJti(rs.getJti());
        user.setToken(token);
        return user;
    }
}
