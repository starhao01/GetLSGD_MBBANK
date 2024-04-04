package com.viettelpost.core.utils;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.viettelpost.core.services.domains.UserInfo;
import net.minidev.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.Security;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.stream.Collectors;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

public class BaseSecurity {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static UserInfo vtmanToken(String token) {
        SignedJWT signedJWT;
        try {
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(parseBase64Binary(getKey("keys/vtman.public", false)));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey bobPubKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
            signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier(bobPubKey);
            if (signedJWT.verify(verifier)) {
                JSONObject objs = signedJWT.getPayload().toJSONObject();
                if (new UserInfo(objs).verify())
                    return new UserInfo(objs);
            }
        } catch (Exception e) {
            System.out.println("Token error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static UserInfo evtpToken(String token) {
        SignedJWT signedJWT;
        try {
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(parseBase64Binary(getKey("keys/Evtp.public", false)));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey bobPubKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
            signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier(bobPubKey);
            if (signedJWT.verify(verifier)) {
                JSONObject objs = signedJWT.getPayload().toJSONObject();
                if (new UserInfo(objs).verify())
                    return new UserInfo(objs);
            }
        } catch (Exception e) {
            System.out.println("Token error: " + e.getLocalizedMessage());
        }
        return null;
    }

    public static String getKey(String filename, boolean isPrivateKey) throws Exception {
        try (InputStream is = new ClassPathResource("/" + filename).getInputStream()) {
            String temp = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines()
                    .collect(Collectors.joining(""));
            String pem;
            if (isPrivateKey) {
                pem = temp.replace("-----BEGIN RSA PRIVATE KEY-----", "");
                pem = pem.replace("-----END RSA PRIVATE KEY-----", "");
            } else {
                pem = temp.replace("-----BEGIN PUBLIC KEY-----", "");
                pem = pem.replace("-----END PUBLIC KEY-----", "");
            }
            return pem;
        }
    }

//    public static void main(String[] args) {
//        try {
//            System.out.println(DigestUtils.sha256Hex("ctbc#2019Nh3"));
//        } catch (Exception e) {
//
//        }
//    }

}
