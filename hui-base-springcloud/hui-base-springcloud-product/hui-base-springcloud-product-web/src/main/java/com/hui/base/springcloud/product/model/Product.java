package com.hui.base.springcloud.product.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Alias(value = "product")
@Table(name = "t_product")
@Data
@ToString
public class Product implements Serializable {

    @Id
    private String productId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_stock")
    private Integer productStock;
    @Column(name = "product_price")
    private BigDecimal productPrice;

    private static final long serialVersionUID = 1L;

}