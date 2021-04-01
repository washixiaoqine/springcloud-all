package com.hui.base.springcloud.order.common.filter;

import com.hui.base.springcloud.common.action.BaseAction;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description: token 拦截器
 */
@Component
public class BaseActionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        BaseAction ba = null;
        // 是否有此方法
        if (handler instanceof HandlerMethod) {
            // 封装请求的request和response
            HandlerMethod method = (HandlerMethod) handler;
            Object obj1 = method.getBean();
            if (obj1 instanceof BaseAction) {
                ba = (BaseAction) obj1;
                ba.init(request, response);
            }
        } else {
            // 没有此方法 直接返回404错误
            //this.outWriteErrorNotFind(response);
            return false;
        }
        // 是否不需登陆
        //if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
        //    UnCheckLogin unCheckLogin = ((HandlerMethod) handler).getMethodAnnotation(UnCheckLogin.class);
        //    if (unCheckLogin != null && unCheckLogin.check() == true) {
        //        return true;
        //    }
        //}

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception arg3) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            Object obj1 = method.getBean();
            if (obj1 instanceof BaseAction) {
                BaseAction ba = (BaseAction) obj1;
                ba.destroy();
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler, ModelAndView arg3) throws Exception {

    }

    /**
     * 404
     */
    /*private void outWriteErrorNotFind(HttpServletResponse response) throws Exception {
        JsonResult result = new JsonResult();
        result.setCode("404");
        result.setMessage("您访问的资源无效");
        result.setSuccess(Boolean.FALSE);
        result.setData(null);
        String jsonStr = JSONObject.toJSONString(result);
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(jsonStr);
    }*/



}