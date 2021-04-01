package com.hui.base.springcloud.order.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Alias(value = "order")
@Table(name = "t_order")
@Data
@ToString
public class Order implements Serializable {

    @Id
    private String orderId;

    @Column(name="order_name")
    private String orderName;

    @Column(name="product_id")
    private String productId;

    @Column(name="buy_quantity")
    private Integer buyQuantity;

    private static final long serialVersionUID = 1L;
}