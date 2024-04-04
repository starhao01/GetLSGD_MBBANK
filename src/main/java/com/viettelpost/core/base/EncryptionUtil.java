package com.viettelpost.core.base;

import com.google.gson.JsonObject;
import com.viettelpost.core.utils.Constants;
import com.viettelpost.core.utils.RsaCrypto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.keys.AesKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.*;
import java.util.Map;

public class EncryptionUtil {
    // some random salt
    private static final byte[] SALT = {(byte) 0x21, (byte) 0x21, (byte) 0xF0, (byte) 0x55, (byte) 0xC3, (byte) 0x9F, (byte) 0x5A, (byte) 0x75};

    private final static int ITERATION_COUNT = 31;

    private EncryptionUtil() {
    }
    public static String createJWT(Map<String, Object> claim) throws Exception {
        Long exp = System.currentTimeMillis() + 24L * 60 * 60 * 1000;
        claim.remove(com.viettelpost.core.utils.Constants.pwdSaltKey);
        claim.remove(Constants.pwdKey);
        claim.put("exp", exp + "");
        claim.put("source", -1);
        JsonObject header = new JsonObject();
        header.addProperty("alg", "RS256");
        header.addProperty("typ", "JWT");
        header.addProperty("exp", exp + "");
        RSAPrivateKey _privateKey = RsaCrypto.LoadPrivateKey2("keys/EvtpPrivate.pem", true);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(_privateKey.getEncoded());
        String retStr = null;
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privKey = kf.generatePrivate(keySpec);
            retStr = Jwts.builder().setClaims(claim).signWith(SignatureAlgorithm.RS256, privKey).compact();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return retStr;
    }

    public static String encode(String input) {
        if (input == null) {
            throw new IllegalArgumentException();
        }
        try {

            KeySpec keySpec = new PBEKeySpec(null, SALT, ITERATION_COUNT);
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

            byte[] enc = ecipher.doFinal(input.getBytes());

            String res = new String(Base64.encodeBase64(enc));
            // escapes for url
            res = res.replace('+', '-').replace('/', '_').replace("%", "%25").replace("\n", "%0A");

            return res;

        } catch (Exception e) {
        }

        return "";

    }

    public static String decode(String token) {
        if (token == null) {
            return null;
        }
        try {

            String input = token.replace("%0A", "\n").replace("%25", "%").replace('_', '/').replace('-', '+');

            byte[] dec = Base64.decodeBase64(input.getBytes());

            KeySpec keySpec = new PBEKeySpec(null, SALT, ITERATION_COUNT);
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

            byte[] decoded = dcipher.doFinal(dec);

            String result = new String(decoded);
            return result;

        } catch (Exception e) {
            // use logger in production code
            e.printStackTrace();
        }

        return null;
    }

    public static String getJsonData(String input) throws Exception {

        final String signingKey = "ahN47WHSA3-_I7wAcfQ7W2qyTKMeQrbDBYJQoENpGeTs8xLWddVPaMfqgC_e_UboPB9wJluMVC3M8CtoBKt7Ow";
        final String encryptionKey = "rle6pMmf5eWeix5LHm2sil_aP8WWl3IB8RtMWsRw1vs";
        final Key key = new AesKey(signingKey.getBytes(StandardCharsets.UTF_8));
        final JsonWebSignature jws = new JsonWebSignature();

        String result = "";
        jws.setCompactSerialization(input);
        jws.setKey(key);
        if (!jws.verifySignature()) {
            throw new Exception("JWT verification failed");
        }
        final byte[] decodedBytes = Base64.decodeBase64(jws.getEncodedPayload().getBytes(StandardCharsets.UTF_8));
        final String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);
        final JsonWebEncryption jwe = new JsonWebEncryption();
        final JsonWebKey jsonWebKey = JsonWebKey.Factory
                .newJwk("\n" + "{\"kty\":\"oct\",\n" + " \"k\":\"" + encryptionKey + "\"\n" + "}");
        jwe.setCompactSerialization(decodedPayload);
        jwe.setKey(new AesKey(jsonWebKey.getKey().getEncoded()));
        result = jwe.getPlaintextString();

        return result;
    }

    public static void main(String args[]) {
        try {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
