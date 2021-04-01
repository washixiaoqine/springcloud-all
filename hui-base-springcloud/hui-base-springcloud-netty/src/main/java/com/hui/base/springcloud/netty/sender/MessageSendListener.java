package com.hui.base.springcloud.netty.sender;

import com.hui.base.springcloud.netty.protocol.message.MessageReceiveProtocol;
import com.hui.base.springcloud.netty.protocol.message.MessageSendProtocol;
import com.hui.base.springcloud.netty.config.SendResult.SendResultStatus;

/**
 * 发送结果监听器 TcpSendHandler 中触发
 */
public interface MessageSendListener {
    void complete(MessageSendProtocol sendProtocol, MessageReceiveProtocol receiveProtocol);

    void fail(MessageSendProtocol sendProtocol, SendResultStatus reason, Throwable throwable);
}
