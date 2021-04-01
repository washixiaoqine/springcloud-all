package com.hui.base.springcloud.product.mq;

import com.hui.base.springcloud.order.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * 接收端 @Input与 @StreamListener 搭配使用
 */
@Slf4j
@Component
@EnableBinding(StreamAccept.class)
public class StreamReceiver {

    @StreamListener(StreamAccept.INPUT)
    @SendTo({StreamAccept.INPUT2})
    public String receiver(String msg){
        log.info("[StreamReceiver] receiver getMsg:::{}",msg);
        return "order the order , tell my boss";
    }

    @StreamListener(StreamAccept.INPUT2)
    public void tellTheBoss(String msg){
        log.info("[StreamReceiver] tellTheBoss getMsg:::{}",msg);
    }

    @StreamListener(StreamAccept.INPUT_OBJ)
    public void receiverObj(OrderDTO order){
        log.info("[StreamReceiver] receiver getMsg:::{}",order.toString());
    }
}
