package com.hui.base.springcloud.product.service.impl;

import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.codingapi.txlcn.tc.annotation.TccTransaction;
import com.codingapi.txlcn.tc.annotation.TxTransaction;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.codingapi.txlcn.tracing.TracingContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import com.hui.base.springcloud.product.mapper.ProductMapper;
import com.hui.base.springcloud.product.model.Product;
import com.hui.base.springcloud.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    private ConcurrentHashMap<String, Set<String>> ids = new ConcurrentHashMap<>();


    @Override
    public Product get(String id) {
        Product product = productMapper.selectByPrimaryKey(id);
        //throw new RuntimeException();  // 降级测试
        return product;
    }

    @Override
    public List<Product> list() {
        return productMapper.listAll();
    }

    @Override
    public PageInfo<Product> productPage(Product product, PageInfo pageInfo) {
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());

        Example exe = new Example(Product.class);
        Example.Criteria criteria = exe.createCriteria();
        if (StringUtils.isNotEmpty(product.getProductName())) {
            criteria.andCondition(" product_name like '%" + product.getProductName() + "%'");
        }
        //exe.setOrderByClause(" create_time asc");

        List<Product> list =  this.productMapper.selectByExample(exe);
        PageInfo<Product> page = new PageInfo<>(list);
        return page;
    }

    @Override
    public Product add(Product product) {
        int result = productMapper.insertSelective(product);
        return product;
    }

    @Override
    @TccTransaction(propagation = DTXPropagation.SUPPORTS)
    @Transactional(rollbackFor = Exception.class)
    public void tccAdd(Product product) {
        productMapper.insertSelective(product);

        ids.putIfAbsent(TracingContext.tracing().groupId(), Sets.newHashSet(product.getProductId()));
        ids.get(TracingContext.tracing().groupId()).add(product.getProductId());
    }

    @Override
    @TxcTransaction(propagation = DTXPropagation.SUPPORTS)
    @Transactional(rollbackFor = Exception.class)
    public void txcAdd(Product product) {
        productMapper.insertSelective(product);
    }

    @Override
    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    @Transactional(rollbackFor = Exception.class)
    public void lcnAdd(Product product) {
        productMapper.insertSelective(product);
    }

    public void confirmTccAdd (Product product) {
        ids.get(TracingContext.tracing().groupId()).forEach(id -> {
            log.info("tcc-confirm-{}-{}" , TracingContext.tracing().groupId(), id);
            ids.get(TracingContext.tracing().groupId()).remove(id);
        });
    }

    public void cancelTccAdd(Product product) {
        ids.get(TracingContext.tracing().groupId()).forEach(id -> {
            log.info("tcc-cancel-{}-{}", TracingContext.tracing().groupId(), id);
            productMapper.deleteByPrimaryKey(id);
        });
    }

}
