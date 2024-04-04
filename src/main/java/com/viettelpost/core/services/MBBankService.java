package com.viettelpost.core.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static com.viettelpost.core.utils.Utils.md5;

@Service
public class MBBankService {

    public String yourDeviceId = "";

    public String login(String user, String pass, String captcha) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Accept-Encoding", "gzip, deflate, br");
        headers.set("Accept-Language", "en-US,en;q=0.9,vi;q=0.8");
        headers.set("Authorization", "Basic QURNSU46QURNSU4=");
        headers.set("Connection", "keep-alive");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("elastic-apm-traceparent", "00-17620ad87b0b1e04da1d1cf8e8d8c287-bfd8deead47f0f3c-01");
        headers.set("Host", "online.mbbank.com.vn");
        headers.set("Origin", "https://online.mbbank.com.vn");
        headers.set("Referer", "https://online.mbbank.com.vn/pl/login?logout=1");
        headers.set("sec-ch-ua", "\"Chromium\";v=\"104\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"104\"");
        headers.set("sec-ch-ua-mobile", "?0");
        headers.set("sec-ch-ua-platform", "\"Windows\"");
        headers.set("Sec-Fetch-Dest", "empty");
        headers.set("Sec-Fetch-Mode", "cors");
        headers.set("Sec-Fetch-Site", "same-origin");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36");
        headers.set("X-Request-Id", "7ed665bc35bb47f19b263447bd1cc180-2022090511445886");

        RestTemplate restTemplate = new RestTemplate();

        // Define the request body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("password", pass);
        requestBody.add("refNo", md5(user) + "-2023090511533744");
        requestBody.add("sessionId", null); // Replace null with appropriate session value
        requestBody.add("userId", user);
        requestBody.add("captcha", captcha);
        requestBody.add("deviceIdCommon", yourDeviceId);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Define the URL
        String url = "https://online.mbbank.com.vn/api/retail_web/internetbanking/doLogin";

        // Send the HTTP POST request
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // Extract and return the response body
        String responseBody = responseEntity.getBody();
        return responseBody;
    }

    // Implement the md5 method for generating the refNo
}
