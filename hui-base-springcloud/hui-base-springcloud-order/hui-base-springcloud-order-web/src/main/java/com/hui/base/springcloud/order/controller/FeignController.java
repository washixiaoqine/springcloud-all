package com.hui.base.springcloud.order.controller;

import com.hui.base.springcloud.common.action.BaseAction;
import com.hui.base.springcloud.common.json.JsonResult;
import com.hui.base.springcloud.order.model.Order;
import com.hui.base.springcloud.order.service.OrderService;
import com.hui.base.springcloud.product.api.ProductFeignApi;
import dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Feign Feign=hystrix+ribbon
 */
@RestController
@Slf4j
public class FeignController extends BaseAction {

    /**
     * 调用方式 1 httpClient封装的 restTemplate
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 调用方式 2 负载均衡 loadBalancerClient
     */
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /**
     * 调用方式 3 Feign
     */
    @Autowired
    private ProductFeignApi productFeignApi;

    @Autowired
    private OrderService orderService;

    /**
     * 普通调用
     */
    @GetMapping("/order/product/{id}")
    public JsonResult getProduct(@PathVariable("id") String id) {
        RestTemplate restTemplate = new RestTemplate();
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("id", id);
        Object product = restTemplate.getForObject("http://localhost:8180/product-service/product/{id}", JsonResult.class, paramMap)
            .getData();
        return JsonResult.SUCCESS(product);
    }

    /**
     * loadBalancerClient负载均衡找到ip:port后发起调用
     */
    @GetMapping("/order/product2/{id}")
    public JsonResult getProduct2(@PathVariable("id") String id) {
        RestTemplate restTemplate = new RestTemplate();
        ServiceInstance instance = loadBalancerClient.choose("product-service");
        String url = String.format("http://%s:%s/product-service/product/{id}", instance.getHost(), instance.getPort());
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("id", id);
        Object product = restTemplate.getForObject(url, JsonResult.class, paramMap).getData();
        return JsonResult.SUCCESS(product);
    }

    /**
     * 注入@loadBalancerClient后获取.
     */
    @GetMapping("/order/product3/{id}")
    public JsonResult getProduct3(@PathVariable("id") String id) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("id", id);
        Object product =  restTemplate.getForObject("http://product-service/product-service/product/{id}", JsonResult.class, paramMap).getData();
        return JsonResult.SUCCESS(product);
    }

    /**
     * fegin获取.
     */
    @GetMapping("/order/product4/{id}")
    public JsonResult<ProductDTO> getProduct4(@PathVariable("id") String id) {
        return productFeignApi.get(id, getToken());
    }

    /**
     * 普通方式添加
     */
    @PutMapping("/orders/addProduct")
    public JsonResult addProduct(@RequestBody Order order, String exFlag) {
        orderService.add(order, exFlag);
        return JsonResult.SUCCESS();
    }

    /**
     * 用于测试TCC模式
     */
    @PutMapping("/orders/addProduct/tcc")
    public JsonResult addProductByTcc(@RequestBody Order order, String exFlag) {
        orderService.testTCC(order, exFlag);
        return JsonResult.SUCCESS();
    }

    /**
     * 用于测试TXC模式
     */
    @PutMapping("/orders/addProduct/txc")
    public JsonResult addProductByTxc(@RequestBody Order order, String exFlag) {
        orderService.testTXC(order, exFlag);
        return JsonResult.SUCCESS();
    }

    /**
     * 用于测试TXC模式
     */
    @PutMapping("/orders/addProduct/lcn")
    public JsonResult addProductByLcn(@RequestBody Order order, String exFlag) {
        orderService.testLCN(order, exFlag);
        return JsonResult.SUCCESS();
    }

}
