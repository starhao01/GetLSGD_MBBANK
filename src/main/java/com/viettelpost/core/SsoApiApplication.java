package com.viettelpost.core;

import co.elastic.apm.attach.ElasticApmAttacher;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.viettelpost.core.utils.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@EnableEncryptableProperties
public class SsoApiApplication {
    static final String defaultPassword = "123456a#";

    public static void main(String[] args) {
        String password = System.getenv("jasypt_erpapi_password");
        System.setProperty("jasypt.encryptor.password", Utils.isNullOrEmpty(password) ? defaultPassword : password);
        System.out.println("----");
        System.out.println("#loading private key " + password);
        SpringApplication.run(SsoApiApplication.class, args);
        ElasticApmAttacher.attach();
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        System.out.println("Spring boot application running in +7 timezone :" + new Date());
    }
}

