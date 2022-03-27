package com.ratmirdudin.jblog_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JavaBlogServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaBlogServerApplication.class, args);
    }

}
