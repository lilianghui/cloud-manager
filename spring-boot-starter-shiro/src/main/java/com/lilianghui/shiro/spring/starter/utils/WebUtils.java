package com.lilianghui.shiro.spring.starter.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public class WebUtils {

    public static ModelAndView getModelAndView(String view, Map<String, Object> context) {
        Assert.isTrue(StringUtils.isNotBlank(view), "返回视图不能为空");
        return new ModelAndView(view, context);
    }

    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip) ? "127.0.0.1" : ip;
    }

    public static Locale getLocale(HttpServletRequest request) {
        return RequestContextUtils.getLocaleResolver(request).resolveLocale(request);
    }

    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        return attrs.getRequest();
    }

    public static HttpServletResponse getHttpServletResponse() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        return attrs.getResponse();
    }

    // 获取MAC地址的方法
    public static String getMACAddress() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

            // 下面代码是把mac地址拼装成String
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // mac[i] & 0xFF 是为了把byte转化为正整数
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }

            // 把字符串所有小写字母改为大写成为正规的mac地址并返回
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    public static boolean isEnglish(HttpServletRequest request) {
        if (request == null) {
            request = WebUtils.getHttpServletRequest();
        }
        Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);
        return "en_US".equalsIgnoreCase(locale.toString());
    }

    public static void writeJson(HttpServletResponse response, String writeValue) {
        writeJson(response, writeValue, true);
    }

    public static void writeJson(HttpServletResponse response, String writeValue, boolean flag) {
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter write = response.getWriter();
            write.write(writeValue);
            if (flag) {
                write.flush();
                write.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getWebsite(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    public static int getHttpCodeByException(Exception ex) {
//        if (ex instanceof NoSuchRequestHandlingMethodException) {
//            return HttpServletResponse.SC_NOT_FOUND;
//        } else
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            return HttpServletResponse.SC_METHOD_NOT_ALLOWED;
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            return HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            return HttpServletResponse.SC_NOT_ACCEPTABLE;
        } else if (ex instanceof MissingPathVariableException) {
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        } else if (ex instanceof MissingServletRequestParameterException) {
            return HttpServletResponse.SC_BAD_REQUEST;
        } else if (ex instanceof ServletRequestBindingException) {
            return HttpServletResponse.SC_BAD_REQUEST;
        } else if (ex instanceof ConversionNotSupportedException) {
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        } else if (ex instanceof TypeMismatchException) {
            return HttpServletResponse.SC_BAD_REQUEST;
        } else if (ex instanceof HttpMessageNotReadableException) {
            return HttpServletResponse.SC_BAD_REQUEST;
        } else if (ex instanceof HttpMessageNotWritableException) {
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        } else if (ex instanceof MethodArgumentNotValidException) {
            return HttpServletResponse.SC_BAD_REQUEST;
        } else if (ex instanceof MissingServletRequestPartException) {
            return HttpServletResponse.SC_BAD_REQUEST;
        } else if (ex instanceof BindException) {
            return HttpServletResponse.SC_BAD_REQUEST;
        } else if (ex instanceof NoHandlerFoundException) {
            return HttpServletResponse.SC_NOT_FOUND;
        }
        return HttpServletResponse.SC_OK;
    }


    public static boolean hasAnyRoles(Collection<String> roles) {
        boolean hasAnyRole = false;

        Subject subject = SecurityUtils.getSubject();

        if (subject != null) {

            // Iterate through roles and check to see if the user has one of the roles
            for (String role : roles) {

                if (subject.hasRole(role.trim())) {
                    hasAnyRole = true;
                    break;
                }

            }

        }

        return hasAnyRole;
    }
}
