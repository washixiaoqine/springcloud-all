package com.hui.base.springcloud.order.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class OrderDTO implements Serializable {

    private String orderId;

    private String orderName;

    private String productId;

    private Integer buyQuantity;
}
