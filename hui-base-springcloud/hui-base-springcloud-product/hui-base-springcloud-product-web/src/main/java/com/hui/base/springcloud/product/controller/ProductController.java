package com.hui.base.springcloud.product.controller;

import com.github.pagehelper.PageInfo;
import com.hui.base.springcloud.common.action.BaseAction;
import com.hui.base.springcloud.common.json.JsonResult;
import com.hui.base.springcloud.product.model.Product;
import com.hui.base.springcloud.product.service.ProductService;
import dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


@RestController
@Slf4j
public class ProductController extends BaseAction {
    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public JsonResult<ProductDTO> get(@PathVariable("id") String id) {
        ProductDTO productDTO = new ProductDTO();
        Product product = productService.get(id);
        BeanUtils.copyProperties(product,productDTO);
        log.info(productDTO.toString());
        return JsonResult.SUCCESS(productDTO);
    }


    @GetMapping("/products")
    public JsonResult listAll(){
        List<Product> list = productService.list();
        return JsonResult.SUCCESS(list);
    }


    @PostMapping("/productPage")
    public JsonResult productPage(@RequestBody Product product, PageInfo pageInfo){
        PageInfo<Product> page = productService.productPage(product, pageInfo);
        return JsonResult.SUCCESS(page);
    }


    @PutMapping("/products")
    public JsonResult add(@RequestBody ProductDTO productDTO){
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        productService.add(product);
        return JsonResult.SUCCESS(product);
    }


    @PutMapping("/lcn/tcc/products")
    public JsonResult tccAdd(@RequestBody ProductDTO productDTO){
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        productService.tccAdd(product);
        return JsonResult.SUCCESS();
    }

    @PutMapping("/lcn/txc/products")
    public JsonResult txcAdd(@RequestBody ProductDTO productDTO){
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        productService.txcAdd(product);
        return JsonResult.SUCCESS();
    }

    @PutMapping("/lcn/lcn/products")
    public JsonResult lcnAdd(@RequestBody ProductDTO productDTO){
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        productService.lcnAdd(product);
        return JsonResult.SUCCESS();
    }
}
