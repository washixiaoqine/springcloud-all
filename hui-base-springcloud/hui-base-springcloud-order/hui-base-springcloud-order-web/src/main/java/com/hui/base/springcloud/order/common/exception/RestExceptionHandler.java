package com.hui.base.springcloud.order.common.exception;


import com.hui.base.springcloud.common.enums.CodeEC;
import com.hui.base.springcloud.common.json.JsonResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description: 辅助Controller 异常处理器
 * Author: zy
 * Date: 2019-07-16 17:17:17
 */
@ControllerAdvice
@Slf4j
public class RestExceptionHandler {
    /**
     * Description: 系统异常
     * Author: zy
     * Date: 2019-07-16 17:21:18
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public JsonResult ExceptionHandler(Exception ex) {
        log.error(ex.getMessage());
        return JsonResult.FAIL(CodeEC.SERVICE_ERROR);
    }

    /**
     * Description: 业务异常
     * Author: zy
     * Date: 2019-07-16 17:21:29
     */
    @ResponseBody
    @ExceptionHandler(value = RestException.class)
    public JsonResult restExceptionHandler(RestException ex) {
        log.error(ex.getMessage());
        return JsonResult.FAIL(ex.getCodeEC());
    }

    /**
     * 可直接在Service曾抛出，会被RestExceptionHandler捕获并返回给前端
     */
    @Data
    @AllArgsConstructor
    public static class RestException extends RuntimeException {
        private CodeEC codeEC;
    }
}
