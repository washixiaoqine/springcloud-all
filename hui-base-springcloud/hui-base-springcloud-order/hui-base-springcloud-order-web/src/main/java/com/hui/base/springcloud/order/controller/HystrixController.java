package com.hui.base.springcloud.order.controller;

import java.util.HashMap;

import com.hui.base.springcloud.common.enums.CodeEC;
import com.hui.base.springcloud.common.json.JsonResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * hystrix
 */
@RestController
public class HystrixController {

    @GetMapping("/hystrixTest")
    @HystrixCommand(fallbackMethod = "fallback")
    public JsonResult hystrixTest(){
        RestTemplate restTemplate = new RestTemplate();
        Object product = restTemplate.getForObject("http://localhost:8180/product-service/product/{id}", JsonResult.class,
            1).getData();
        return JsonResult.SUCCESS(product);
    }

    @GetMapping("/hystrixTest2")
    @HystrixCommand(fallbackMethod = "fallback")
    public JsonResult hystrixTest2(){
        throw new RuntimeException("抛异常~");
    }


    @GetMapping("/hystrixTest3")
    @HystrixCommand(defaultFallback = "defaultFallback")
    public JsonResult hystrixTest3(){
        throw new RuntimeException("抛异常~");
    }

    /**
     * 超时时间配置，超时则会降级
     */
    @GetMapping("/hystrixTest4")
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
    })
    public JsonResult hystrixTest4(){
        RestTemplate restTemplate = new RestTemplate();
        Object product = restTemplate.getForObject("http://localhost:8180/product-service/product/{id}", JsonResult.class,
            1).getData();
        return JsonResult.SUCCESS(product);
    }

    /**
     * 熔断设置
     */
    @GetMapping("/hystrixTest5")
    @HystrixCommand(commandProperties = {
            //开启熔断
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),
            //设置熔断器最少请求次数
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
            //错误率百分之60就会熔断
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),
            //设置熔断重连时间（断路器打开的时候会进行计时，超过这个时间再访问仍然请求失败会继续熔断，并重新计时）
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000")
    })
    public JsonResult hystrixTest5(){
        RestTemplate restTemplate = new RestTemplate();
        Object product = restTemplate.getForObject("http://localhost:8180/product-service/product/{id}", JsonResult.class,
            1).getData();
        return JsonResult.SUCCESS(product);
    }

    /**
     * 熔断设置（也可以通过commonKey改名，对应bootstrap.yml里面做配置,默认为方法名，这里即hystrixTestX）
     */
    @GetMapping("/hystrixTest6")
    @HystrixCommand(commandKey = "hystrixTestX")
    public JsonResult hystrixTest6(){
        RestTemplate restTemplate = new RestTemplate();
        Object product = restTemplate.getForObject("http://localhost:8180/product-service/product/{id}", JsonResult.class,
            1).getData();
        return JsonResult.SUCCESS(product);
    }

    private JsonResult fallback(){
        return JsonResult.FAIL(CodeEC.FALL_BACK);
    }

    private JsonResult defaultFallback(){
        return JsonResult.FAIL(CodeEC.FALL_BACK);
    }


}
