package com.hui.base.springcloud.product.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 接收端 @Input与 @StreamListener 搭配使用
 */
public interface StreamAccept {

    String INPUT = "huiStream";
    String INPUT2 = "huiStream2";
    String INPUT_OBJ = "huiStreamObj";

    @Input(StreamAccept.INPUT)
    SubscribableChannel input();

    @Input(StreamAccept.INPUT2)
    SubscribableChannel input2();

    @Input(StreamAccept.INPUT_OBJ)
    SubscribableChannel inputObj();
}
