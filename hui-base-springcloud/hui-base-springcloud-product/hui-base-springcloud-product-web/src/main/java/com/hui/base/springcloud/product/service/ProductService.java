package com.hui.base.springcloud.product.service;


import com.github.pagehelper.PageInfo;
import com.hui.base.springcloud.product.model.Product;

import javax.annotation.Resource;
import java.util.List;

@Resource
public interface ProductService {

    Product get(String id);

    List<Product> list();

    PageInfo<Product> productPage(Product product, PageInfo pageInfo);

    Product add(Product product);

    void tccAdd(Product product);

    void txcAdd(Product product);

    void lcnAdd(Product product);
}
