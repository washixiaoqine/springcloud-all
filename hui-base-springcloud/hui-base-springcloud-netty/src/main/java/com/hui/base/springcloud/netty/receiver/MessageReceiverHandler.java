package com.hui.base.springcloud.netty.receiver;

import java.io.IOException;

import com.hui.base.springcloud.netty.enums.ActionEnum;
import com.hui.base.springcloud.netty.util.SpringUtil;
import com.hui.base.springcloud.netty.config.HeartbeatPinger;
import com.hui.base.springcloud.netty.protocol.message.MessageReceiveProtocol;
import com.hui.base.springcloud.netty.sender.MessageSendHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.task.TaskExecutor;

/**
 * 接收指令的handler
 * 基于netty事件，这里收到的消息，都是已经通过 MessageDecoder 解码之后的完整的数据包
 */
@Slf4j
public class MessageReceiverHandler extends SimpleChannelInboundHandler<MessageReceiveProtocol> {
    /**
     * 线程池，用来异步调用TcpController处理业务
     */
    private TaskExecutor executor;
    /**
     * 调用TcpController处理业务
     */
    private com.hui.base.springcloud.netty.receiver.TcpReceiveHandler handler = SpringUtil.getBean(
        com.hui.base.springcloud.netty.receiver.TcpReceiveHandler.class);
    /**
     * 消息发送的工具
     */
    private MessageSendHandler sendHandler = SpringUtil.getBean(MessageSendHandler.class);

    public MessageReceiverHandler(TaskExecutor executor) {
        this.executor = executor;
    }

    /**
     * netty读取到完整的业务包之后的事件通知
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, MessageReceiveProtocol msg) {
        if (msg.getAction() == ActionEnum.HEARTBEAT.getAction()) {
            log.trace("Channel {} receive heartbeat message: {}", ctx.channel().id().asShortText(), msg);
            return;
        } else {
            log.debug("Channel {} receive message: {}", ctx.channel().id().asShortText(), msg);
        }
        executor.execute(() -> {
            log.warn("channelId: {}", ctx.channel().id());
            log.warn("requestId: {}", msg.getRequestId());
            try {
                handler.handle(ctx, msg);
            } catch (IOException e) {
                log.error("消息处理异常", e);
            } finally {
                MDC.clear();
            }
        });
    }

    /**
     * channel激活，即有新的tcp连接，向对方发送心跳
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("Channel {} active: {}", ctx.channel().id().asShortText(), ctx.channel().remoteAddress().toString());
        HeartbeatPinger.ping(ctx.channel());
    }

    /**
     * channel失效，即当前连接关闭
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        sendHandler.removeChannel(ctx.channel());
        //日志
        String id = ctx.channel().id().asShortText();
        String address = ctx.channel().remoteAddress().toString();
        log.info("Channel {} inactive: address={}", id, address);
    }

    /**
     * channel中出现异常，任何异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Channel {} exception，will be closed: {}", ctx.channel().id().asShortText(), cause.getMessage());
        ctx.close();//直接关闭连接
    }

}
