package com.hui.base.springcloud.lcn;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableTransactionManagerServer
@SpringBootApplication
@EnableDiscoveryClient
public class LcnServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LcnServerApplication.class,args);
    }
}
