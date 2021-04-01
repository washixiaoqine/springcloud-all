package com.hui.base.springcloud.product.api;

import com.codingapi.txlcn.tc.support.DTXUserControls;
import com.codingapi.txlcn.tracing.TracingContext;
import com.hui.base.springcloud.common.enums.CodeEC;
import com.hui.base.springcloud.common.json.JsonResult;
import dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * name : service name
 * path : service context path
 * fallback : 各自的fallback方法
 */
@FeignClient(name = "product-service",path = "/product-service",fallback = ProductFeignApi.ProductFeignApiFallback.class )
public interface ProductFeignApi {

    /**
     * 自定义header传参方式
     */
    @GetMapping(value="/product/{id}", headers = {"token={token}"})
    JsonResult<ProductDTO> get(@PathVariable("id") String id, @RequestParam("token") String token);

    @PutMapping("/products")
    JsonResult add(@RequestBody ProductDTO productDTO);

    @PutMapping("/lcn/tcc/products")
    JsonResult tccAdd(@RequestBody ProductDTO productDTO);

    @PutMapping("/lcn/txc/products")
    JsonResult txcAdd(@RequestBody ProductDTO productDTO);

    @PutMapping("/lcn/lcn/products")
    JsonResult lcnAdd(@RequestBody ProductDTO productDTO);

    @Component
    class ProductFeignApiFallback implements ProductFeignApi{

        @Override
        public JsonResult<ProductDTO> get(String id, String token) {
            return JsonResult.FAIL(CodeEC.FALL_BACK);
        }

        @Override
        public JsonResult add(ProductDTO productDTO) {
            return JsonResult.FAIL(CodeEC.FALL_BACK);
        }

        @Override
        public JsonResult tccAdd(ProductDTO productDTO) {
            DTXUserControls.rollbackGroup(TracingContext.tracing().groupId());
            return JsonResult.FAIL(CodeEC.FALL_BACK);
        }

        @Override
        public JsonResult txcAdd(ProductDTO productDTO) {
            return JsonResult.FAIL(CodeEC.FALL_BACK);
        }

        @Override
        public JsonResult lcnAdd(ProductDTO productDTO) {
            return JsonResult.FAIL(CodeEC.FALL_BACK);
        }
    }


}
