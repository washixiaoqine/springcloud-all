package com.hui.base.springcloud.common.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基础控制层
 */
public class BaseAction {

    private static ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();
    private static ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<>();
    private static ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    /**
     * 初始化
     */
    public void init(HttpServletRequest request, HttpServletResponse response) {
        requestHolder.set(request);
        responseHolder.set(response);
        tokenHolder.set(request.getHeader("token"));
    }

    /**
     * 销毁
     */
    public void destroy() {
        requestHolder.remove();
        responseHolder.remove();
        tokenHolder.remove();
    }


    public static String getToken() {
        return tokenHolder.get();
    }

    public void setToken(String token) {
        tokenHolder.set(token);
    }

    public String getUserId() {
        return TokenProcessor.ID.apply(tokenHolder.get());
    }

    public String getUserPassport() {
        return TokenProcessor.PASSPORT.apply(tokenHolder.get());
    }

    public String getUserNickName() {
        return TokenProcessor.NICKNAME.apply(tokenHolder.get());
    }

    public HttpServletRequest getRequest() {
        return requestHolder.get();
    }

    public HttpServletResponse getResponse() {
        return responseHolder.get();
    }

    private enum TokenProcessor {
        ID {
            String apply(String token) {
                return null;
            }
        },
        PASSPORT {
            String apply(String token) {
                return null;
            }
        },
        NICKNAME {
            String apply(String token) {
                return null;
            }
        };

        TokenProcessor() {
        }

        abstract String apply(String token);
    }
}
