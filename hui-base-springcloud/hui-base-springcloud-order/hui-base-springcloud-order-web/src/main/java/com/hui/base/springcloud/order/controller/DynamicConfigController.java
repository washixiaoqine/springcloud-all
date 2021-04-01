package com.hui.base.springcloud.order.controller;

import com.hui.base.springcloud.common.json.JsonResult;
import com.hui.base.springcloud.order.common.constant.DynamicConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 动态配置demo consul
 */
@RestController
public class DynamicConfigController {


    @Autowired
    private DynamicConfig dynamicConfig;


    @GetMapping("/dynamicConfig")
    public JsonResult testConfig(){
        return JsonResult.SUCCESS(dynamicConfig);
    }
}
