package com.hui.base.springcloud.order.service;


import com.hui.base.springcloud.order.model.Order;

import javax.annotation.Resource;
import java.util.List;

@Resource
public interface OrderService {
    Order get(String id);

    List<Order> list();

    Order add(Order order, String exFlag);

    /**
     * 测试TX-LCN的TCC模式
     */
    void testTCC(Order order, String exFlag);

    /**
     * 测试TX-LCN的TXC模式
     */
    void testTXC(Order order, String exFlag);

    /**
     * 测试TX-LCN的LCN模式
     */
    void testLCN(Order order, String exFlag);
}
