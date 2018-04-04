package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ian on 2018/4/4.
 */

public class CookieUtil {

    private static Logger logger = LoggerFactory.getLogger(CookieUtil.class);


    private final static String COOKIE_DOMAIN = ".loadbalance.com";
    private final static String COOKIE_NAME = "mmall_login_token";

    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                logger.info("read cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
                if (StringUtils.equals(ck.getName(), COOKIE_NAME)) {
                    logger.info("return cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }


//    X:domain=".loadbalance.com"
//    a:A.loadbalance.com           cookie:domain=A.loadbalance.com;path="/"
//    b:B.loadbalance.com           cookie:domain=B.loadbalance.com;path="/"
//    c:A.loadbalance.com/test/cc   cookie:domain=A.loadbalance.com;path="/test/cc"
//    d:A.loadbalance.com/test/dd   cookie:domain=A.loadbalance.com;path="/test/dd"
//    e:A.loadbalance.com/test      cookie:domain=A.loadbalance.com;path="/"

    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie ck = new Cookie(COOKIE_NAME, token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");//代表设置在根目录下
        ck.setHttpOnly(true); //防止脚本攻击
        //单位是秒。这个不设置的话，cookie不写入硬盘，只在内存，只在当前页面有效
        ck.setMaxAge(60 * 60 * 24 * 365);//-1，代表永久
        logger.info("write cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
        response.addCookie(ck);
    }


    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                if (StringUtils.equals(ck.getName(), COOKIE_NAME)) {
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setMaxAge(0); //设置成0，代表删除此cookie
                    logger.info("del cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
                    response.addCookie(ck);
                    return;

                }
            }
        }
    }

}
