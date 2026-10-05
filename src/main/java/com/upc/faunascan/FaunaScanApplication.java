package com.upc.faunascan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // HU-59: recordatorios programados
public class FaunaScanApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaunaScanApplication.class, args);
    }

}
