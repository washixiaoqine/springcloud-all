package com.hui.base.springcloud.product.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootConfiguration
public class RestTemplateConfig {

    /**
     * ribbon
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    /**
     * feign 请求拦截器
     */
    @Bean
    public RequestInterceptor restIntercetor(){
        class FeignInterceptor implements RequestInterceptor{
            @Override
            public void apply(RequestTemplate requestTemplate){
                //requestTemplate.header("token", "something");
            }
        }
        return new FeignInterceptor();
    }

}
