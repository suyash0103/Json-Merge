package com.suyash.jsonmerge;

import com.suyash.jsonmerge.controller.JsonMergeController;
import com.suyash.jsonmerge.service.JsonMergeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = { JsonMergeController.class, JsonMergeService.class })
public class JsonMergeApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsonMergeApplication.class, args);
    }

}
