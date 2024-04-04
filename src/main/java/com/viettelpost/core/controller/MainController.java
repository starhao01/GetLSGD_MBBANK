package com.viettelpost.core.controller;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.nio.file.Files;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.viettelpost.core.base.BaseController;
import com.viettelpost.core.controller.request.AuthRequest;
import com.viettelpost.core.controller.request.LoginAuthRequest;
import com.viettelpost.core.controller.request.LsgdRequest;
import com.viettelpost.core.controller.request.TransactionRequest;
import com.viettelpost.core.controller.response.AuthResponse;
import com.viettelpost.core.controller.response.TransactionResponse;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;

import javax.imageio.ImageIO;

@RestController
@RequestMapping("/api/bank") // Define a base URL path for the controller

public class MainController extends BaseController {

    private String captchaKey = "v4uvn";
    private String captchaUrl = "https://captcha.vsicloud.com";

    @PostMapping("/process")
    public String process(@RequestParam String phone, String pass) throws IOException {

        String accountNo = "";

        File sessionFile = new File("session.txt");

        String deviceId = generateRandomString(8) + "-" + generateRandomString(4) + "-" + generateRandomString(4) + "-"
                + generateRandomString(4) + "-" + getTimeRequest();

        byte[] captchaImage = getCaptchaImage(deviceId);
        if (!isPngImage(captchaImage)) {
           // return "Invalid captcha image format";
        }
        File captchaFile = new File("D:\\API BANKING\\captcha.png");
        Files.write(captchaFile.toPath(), captchaImage);

        // Send captcha to decode
        String captchaText = decodeCaptcha(captchaFile);
        if (captchaText == null || captchaText.equalsIgnoreCase("ERROR")) {
            return "Captcha decoding failed";
        }

        // Try to login
        AuthResponse auth = login(phone, pass, deviceId, captchaText);
        if (auth.getCode() != 0) {
            return auth.getMessage();
        }
        //Get acccountNO (STK)
        JSONObject jsonObject = new JSONObject(auth.getCust());
        accountNo = auth.getCust().getChrgAcctCd();
        // Save session
        Files.write(sessionFile.toPath(), (deviceId + "|" + auth.getSessionId()).getBytes());
        // Get transactions
        TransactionResponse transactions = getTransactions(phone, accountNo, auth.getSessionId(), deviceId);
        if (transactions.getCode() != 0) {
            // Retry login
            return transactions.getMessage();
        }

        return transactions.toString();
    }
    private boolean isPngImage(byte[] imageData) {
        byte[] pngSignature = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};

        if (imageData.length < pngSignature.length) {
            System.out.println("Image data is too short.");
            return false;
        }

        for (int i = 0; i < pngSignature.length; i++) {
            if (imageData[i] != pngSignature[i]) {
                System.out.println("Mismatch at index " + i + ": Expected " + pngSignature[i] + ", but found " + imageData[i]);
                return false;
            }
        }

        return true;
    }

    @GetMapping("/generateRamdomString")
    private String generateRandomString(int length) {
        String chars = "0123456789abcdef";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return sb.toString();
    }
    @GetMapping("/getTimeRequest")

    private String getTimeRequest() {
        Date now = new Date();
        int day = Integer.parseInt(now.getYear() + "" + now.getMonth() + "" + now.getDate());
        int time = Integer.parseInt(now.getHours() + "" + now.getMinutes() + "" + now.getSeconds());
        return day + time + "";
    }
    @PostMapping("/getCaptchaImage")
    private byte[] getCaptchaImage(String deviceId) throws IOException {
        // Define the request URL and payload data
        String requestUrl = "https://online.mbbank.com.vn/retail-web-internetbankingms/getCaptchaImage";
        String requestData = "{\"refNo\":\"2023090511525326\",\"deviceIdCommon\":\"" + deviceId + "\",\"sessionId\":\"\"}";

        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Host", "online.mbbank.com.vn");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json, text/plain, */*");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.9,vi;q=0.8");
        connection.setRequestProperty("Authorization", "Basic QURNSU46QURNSU4=");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("elastic-apm-traceparent", "00-17620ad87b0b1e04da1d1cf8e8d8c287-bfd8deead47f0f3c-01");
        connection.setRequestProperty("Origin", "https://online.mbbank.com.vn");
        connection.setRequestProperty("Referer", "https://online.mbbank.com.vn/pl/login?logout=1");
        connection.setRequestProperty("sec-ch-ua", "\"Chromium\";v=\"104\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"104\"");
        connection.setRequestProperty("sec-ch-ua-mobile", "?0");
        connection.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");
        connection.setRequestProperty("Sec-Fetch-Dest", "empty");
        connection.setRequestProperty("Sec-Fetch-Mode", "cors");
        connection.setRequestProperty("Sec-Fetch-Site", "same-origin");
        connection.setRequestProperty("X-Request-Id", "7ed665bc35bb47f19b263447bd1cc180-2022090511445886");

        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestData.getBytes("UTF-8"));
        }

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (InputStream is = connection.getInputStream()) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                String jsonResponse = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);

                // Parse the JSON response
                JSONObject jsonObject = new JSONObject(jsonResponse);
                String imageString = jsonObject.getString("imageString");

                // Convert the imageString to a byte array
                byte[] imageBytes = Base64.getDecoder().decode(imageString);
                return imageBytes;
            }
        } else {
            throw new IOException("Failed to fetch captcha image: " + responseCode);
        }
    }



    @PostMapping("/decodeCaptcha")

    private String decodeCaptcha(File image) throws IOException {
        // Step 1: Upload the captcha image to get a decoding ID
        String uploadUrl = "https://captcha.vsicloud.com/in.php";
        String apiKey = captchaKey;

        String uploadResponse = uploadCaptchaImage(uploadUrl, apiKey, image);
        String[] uploadParts = uploadResponse.split("\\|");

        if (uploadParts.length < 2 || !uploadParts[0].equals("OK")) {
            throw new IOException("Failed to upload captcha image");
        }

        String decodeId = uploadParts[1];

        // Step 2: Poll for the decoding result
        String getResultUrl = "https://captcha.vsicloud.com/res.php";

        while (true) {
            String result = pollDecodingResult(getResultUrl, apiKey, decodeId);

            String[] parts = result.split("\\|");
            if (parts.length >= 2 && parts[0].equals("OK")) {
                return parts[1];
            } else if (parts.length >= 2 && parts[0].equals("CAPCHA_NOT_READY")) {
                // CAPCHA_NOT_READY indicates that the decoding is still in progress, so wait and retry.
                try {
                    Thread.sleep(1000); // Wait for 1 second before retrying
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Interrupted while waiting for captcha decoding");
                }
            } else {
                throw new IOException("Failed to decode captcha");
            }
        }
    }

    @PostMapping("/login")

    private AuthResponse login(String user, String pass, String deviceId, String captcha) throws IOException {
        // Convert AuthRequest object to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        //String authRequestJson = objectMapper.writeValueAsString(new AuthRequest(user, pass, deviceId, captcha));
        String userMd5 = md5(user);
        String authRequestJson2 = objectMapper.writeValueAsString(new LoginAuthRequest(pass,(userMd5+"-2023090511533744"),null,user,captcha,deviceId));
        HttpURLConnection conn = null;
        OutputStreamWriter out = null;
        BufferedReader in = null;
        AuthResponse authResponse = new AuthResponse();

        try {
            URL url = new URL("https://online.mbbank.com.vn/api/retail_web/internetbanking/doLogin");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Host", "online.mbbank.com.vn");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json, text/plain, */*");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.9,vi;q=0.8");
            conn.setRequestProperty("Authorization", "Basic QURNSU46QURNSU4=");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("elastic-apm-traceparent", "00-17620ad87b0b1e04da1d1cf8e8d8c287-bfd8deead47f0f3c-01");
            conn.setRequestProperty("Host", "online.mbbank.com.vn");
            conn.setRequestProperty("Origin", "https://online.mbbank.com.vn");
            conn.setRequestProperty("Referer", "https://online.mbbank.com.vn/pl/login?logout=1");
            conn.setRequestProperty("sec-ch-ua", "\"Chromium\";v=\"104\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"104\"");
            conn.setRequestProperty("sec-ch-ua-mobile", "?0");
            conn.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");
            conn.setRequestProperty("Sec-Fetch-Dest", "empty");
            conn.setRequestProperty("Sec-Fetch-Mode", "cors");
            conn.setRequestProperty("Sec-Fetch-Site", "same-origin");
            conn.setRequestProperty("X-Request-Id", "7ed665bc35bb47f19b263447bd1cc180-2022090511445886");

            conn.setDoOutput(true);


            // Write the JSON request body
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(authRequestJson2);
            out.flush();

            // Get the HTTP response code
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read and parse the response
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                StringBuilder responseBuilder = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    responseBuilder.append(line);
                }

                String responseJson = responseBuilder.toString();

                // Parse the JSON response into AuthResponse object
                authResponse = objectMapper.readValue(responseJson, AuthResponse.class);
            } else {
                // Handle error responses here, based on the HTTP response code
                // You can set appropriate values in authResponse to indicate the error
                authResponse.setCode(responseCode);
                authResponse.setMessage("Login failed");
            }
        } finally {
            // Close resources and disconnect the connection
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return authResponse;
    }

    @PostMapping("/getLsgd")

    private TransactionResponse getTransactions(String user, String accountNo, String sessionId, String deviceId)
            throws IOException {
        // Call transaction history API
        String transactionUrl = "https://online.mbbank.com.vn/api/retail-web-transactionservice/transaction/getTransactionAccountHistory";
        HttpURLConnection connection = (HttpURLConnection) new URL(transactionUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Host", "online.mbbank.com.vn");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json, text/plain, */*");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.9,vi;q=0.8");
        connection.setRequestProperty("Authorization", "Basic QURNSU46QURNSU4=");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("elastic-apm-traceparent", "00-17620ad87b0b1e04da1d1cf8e8d8c287-bfd8deead47f0f3c-01");
        connection.setRequestProperty("Origin", "https://online.mbbank.com.vn");
        connection.setRequestProperty("Referer", "https://online.mbbank.com.vn/information-account/source-account");
        connection.setRequestProperty("sec-ch-ua", "\"Chromium\";v=\"104\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"104\"");
        connection.setRequestProperty("sec-ch-ua-mobile", "?0");
        connection.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");
        connection.setRequestProperty("Sec-Fetch-Dest", "empty");
        connection.setRequestProperty("Sec-Fetch-Mode", "cors");
        connection.setRequestProperty("Sec-Fetch-Site", "same-origin");
        connection.setRequestProperty("X-Request-Id", "7ed665bc35bb47f19b263447bd1cc180-2022090511445886");

        connection.setDoOutput(true);

        TransactionRequest transactionRequest = new TransactionRequest(user, accountNo, sessionId, deviceId);
        //Date now
        LocalDate currentDate = LocalDate.now();
        LocalDate currentDate2 = currentDate.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedCurrentDate = currentDate.format(formatter);
        String formattedCurrentDate2 = currentDate2.format(formatter);

        //Date after 1 days
        LocalDate yesterday = currentDate.minusDays(1);
        String formattedYesterday = yesterday.format(formatter);

        LsgdRequest lsgdRequest = new LsgdRequest(formattedCurrentDate,accountNo,null,"DATE_RANGE",sessionId,formattedYesterday,(user+"-2023090511534975"),"ACCOUNT",deviceId);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(lsgdRequest.toJsonString().getBytes());
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (InputStream is = connection.getInputStream()) {
                return TransactionResponse.fromJson(is);
            }
        } else {
            throw new IOException("Transaction request failed: " + responseCode);
        }
    }
    private String uploadCaptchaImage(String uploadUrl, String apiKey, File image) throws IOException {

        HttpURLConnection conn = null;
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String response = "";
        try {
            conn = (HttpURLConnection) new URL(uploadUrl).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setConnectTimeout(30 * 1000); // CURLOPT_TIMEOUT
            conn.setReadTimeout(30 * 1000);
            String boundary = "---------------------------" + System.currentTimeMillis();
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try(OutputStream output = conn.getOutputStream()) {

                output.write(("--" + boundary + "\r\n").getBytes());
                output.write("Content-Disposition: form-data; name=\"file\";".getBytes());
                output.write(("filename=\"" + image.getName() + "\"\r\n").getBytes());
                output.write("Content-Type: image/png\r\n\r\n".getBytes());

                // Nội dung file
                Files.copy(image.toPath(), output);
                output.write("\r\n".getBytes());

                output.write(("--" + boundary + "--\r\n").getBytes());

            }
            // Lấy response
            if (conn.getResponseCode() == 200) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    response += line;
                }
            }
        } finally {
            // Đóng kết nối
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return response;

    }

    @PostMapping("/pollDecodingResult")

    private String pollDecodingResult(String getResultUrl, String apiKey, String decodeId) throws IOException {
        HttpURLConnection conn = null;
        BufferedReader in = null;
        String response = "";

        try {
            String pollUrl = getResultUrl + "?key=" + apiKey + "&action=get&id=" + decodeId;
            conn = (HttpURLConnection) new URL(pollUrl).openConnection();
            conn.setRequestMethod("GET");

            // Set headers
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            // Get the response from the server
            if (conn.getResponseCode() == 200) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    response += line;
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return response;
    }
}