package com.hui.base.springcloud.order.controller;

import com.hui.base.springcloud.order.model.Order;
import com.hui.base.springcloud.order.mq.StreamSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Stream MQ的使用
 */
@RestController
@EnableBinding(StreamSender.class)
public class MQStreamController {

    @Autowired
    private StreamSender streamSender;

    /**
     * 通过CloudStream 发送文本消息.
     */
    @GetMapping("/test_stream")
    public void sendStreamMsg(){
        String msg = new Date()+ " send  msg  to receiver";
        streamSender.output().send(MessageBuilder.withPayload(msg).build());
    }

    /**
     * 通过CloudStream发送序列化后的消息.
     */
    @GetMapping("/test_stream_obj")
    public void sendStreamObj(){
        Order order = new Order();
        order.setOrderId("order-1");
        order.setProductId("mapper-1");
        order.setOrderName("大订单");
        streamSender.outputObj().send(MessageBuilder.withPayload(order).build());
    }
}
