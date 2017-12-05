package com.huawei.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 操作cookie的工具类
 * @author l00152046
 *
 */
public class CookieUtil {
    public static Cookie getCookie(Cookie cookies[], String name) {
        if (cookies == null) {
            return null;
        }
   
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        
        return null;
    }

    /**
     * Adds a cookie with the specified name, value and expiry.
     * 
     * @param response
     *            the HttpServletResponse to add the cookie to
     * @param name
     *            the name of the cookie
     * @param value
     *            the value of the cookie
     * @param maxAge
     *            the maxAge of the cookie (in seconds)
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        setCookie(response, name, value, maxAge, "/", false);
    }

    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge, String path, boolean secure) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(Utils.isStrEmpty(path) ? "/" : path);
        cookie.setSecure(secure);
        cookie.setHttpOnly(true); //客户端js不能访问
        response.addCookie(cookie);
    }
    
    /**
     * 设置cookie，SESSION方式，当浏览器关闭时，则cookie失效
     * 设置age为0是不行的，因为age为0时，cookie立刻过期
     * @param response
     * @param name
     * @param value
     * @param path
     * @param secure
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String path, boolean secure) {
        Cookie cookie = new Cookie(name, value);
        
        //不设置有效期的情况，为session方式，浏览器关闭，则cookie失效
        //cookie.setMaxAge(maxAge);
        
        cookie.setPath(Utils.isStrEmpty(path) ? "/" : path);
        cookie.setSecure(secure);
        cookie.setHttpOnly(true); //客户端js不能访问
        response.addCookie(cookie);
    }
    
    /**
     * Removes a cookie with the specified name.
     * 
     * @param response
     *            the HttpServletResponse to remove the cookie from
     * @param name
     *            the name of the cookie
     */
    public static void removeCookie(HttpServletResponse response, String name, String path) {
        Cookie cookie = new Cookie(name, "");
        
        cookie.setMaxAge(0); //立刻过期
        cookie.setPath(Utils.isStrEmpty(path) ? "/" : path);
        response.addCookie(cookie);
    }
}
