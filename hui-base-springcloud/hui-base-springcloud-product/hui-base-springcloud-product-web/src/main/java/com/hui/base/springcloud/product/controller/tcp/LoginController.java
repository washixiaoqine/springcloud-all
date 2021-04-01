package com.hui.base.springcloud.product.controller.tcp;

import java.util.Map;

import com.hui.base.springcloud.common.enums.CodeEC;
import com.hui.base.springcloud.common.json.JsonResult;
import com.hui.base.springcloud.netty.annotation.Body;
import com.hui.base.springcloud.netty.annotation.Param;
import com.hui.base.springcloud.netty.annotation.TcpAttr;
import com.hui.base.springcloud.netty.annotation.tcp.TcpController;
import com.hui.base.springcloud.netty.annotation.tcp.TcpMapping;
import com.hui.base.springcloud.netty.constant.TcpConstant;
import com.hui.base.springcloud.netty.enums.ActionEnum;
import com.hui.base.springcloud.netty.enums.TcpClientEnum;
import com.hui.base.springcloud.netty.enums.TcpReturnEnum;
import com.hui.base.springcloud.netty.protocol.message.MessageReceiveProtocol;
import com.hui.base.springcloud.netty.sender.MessageSendHandler;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * tcp客户端登录
 */
@TcpController
@Slf4j
public class LoginController {
    @Autowired
    private MessageSendHandler sendHandler;

    /**
     * 客户端登录接口
     *
     * @param body
     * @param channel
     * @return
     */
    @TcpMapping(action = ActionEnum.LOGIN)
    public JsonResult login(@Body Map body, Channel channel) {
        TcpClientEnum clientType = TcpClientEnum.valueOf(String.valueOf(body.get("clientType")).toUpperCase());
        String id = String.valueOf(body.get("id"));
        if (clientType == null || StringUtils.isEmpty(id)) {
            log.info("登入错误，client type 为空:{}", body.get("clientType"));
            return JsonResult.FAIL(CodeEC.CLIENT_TYPE_UNKNOW);
        }
        channel.attr(AttributeKey.valueOf(TcpConstant.ATTRIBUTE_KEY_CLIENT_TYPE)).set(clientType);
        sendHandler.addChannel(clientType, id, channel);
        return JsonResult.SUCCESS(CodeEC.LOGIN_SUCESS);
    }

    @TcpMapping(action = ActionEnum.TX0001)
    public JsonResult gettingMsg(Channel channel, MessageReceiveProtocol protocol, @Body Map<String, String> body,
        @Param("clientType")String clientTypeParam, @TcpAttr(TcpConstant.ATTRIBUTE_KEY_CLIENT_TYPE) String clientTypeAttr) {

        String bodyStr = body.toString();
        Object type = channel.attr(AttributeKey.valueOf(TcpConstant.ATTRIBUTE_KEY_CLIENT_TYPE)).get();
        return JsonResult.SUCCESS(body);
    }
    @TcpMapping(action = ActionEnum.TX0002, immediatelyReturn= TcpReturnEnum.RIGHT_NOW)
    public void gettingMsgWithoutReturn(@Body Map<String, Integer> body, Channel channel) {
        String bodyStr = body.toString();

    }
}
