package com.hui.base.springcloud.product.mapper;

import com.hui.base.springcloud.product.common.config.MyMapper;
import com.hui.base.springcloud.product.model.Product;

import java.util.List;

public interface ProductMapper extends MyMapper<Product> {

    List<Product> listAll();
}