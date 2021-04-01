package com.hui.base.springcloud.order.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Stream API 监听
 */
public interface StreamSender {

    String OUTPUT = "huiStream";
    String OUTPUT2 = "huiStream2";

    String OUTPUT_OBJ = "huiStreamObj";

    @Output(StreamSender.OUTPUT)
    MessageChannel output();

    @Output(StreamSender.OUTPUT2)
    MessageChannel output2();

    @Output(StreamSender.OUTPUT_OBJ)
    MessageChannel outputObj();
}
