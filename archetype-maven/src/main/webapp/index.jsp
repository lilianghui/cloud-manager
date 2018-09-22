<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<% String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";%><!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
    <meta name="description" content="Sentir, Responsive admin and dashboard UI kits template">
    <meta name="keywords" content="admin,bootstrap,template,responsive admin,dashboard template,web apps template">
    <meta name="author" content="Ari Rusmanto, Isoh Design Studio, Warung Themes">

    <title><spring:message code="login.title"/></title>
    <jsp:include page="common/common.jsp"></jsp:include>
    <link href="${ctx}modular/index/index.css" rel="stylesheet"/>
    <script src="${ctx}assets/plugins/rsa/security.js"></script>
    <script type="text/javascript">
        var modulus = "${publicKeyMap.modulus}";
        var exponent = "${publicKeyMap.exponent}";
        var encrypt = "${encrypt}";
    </script>
    <script type="text/javascript" src="${ctx}modular/index/index.js"></script>
</head>

<body style="min-width: 800px">


<div style="position: absolute;top: 0;left: -50px">
    <img src="${ctx}assets/images/u_bg_buttom.png" style="width: 500px">
</div>
<div style="position: absolute;bottom: 0;right: -100px">
    <img src="${ctx}assets/images/u_bg_top.png" style="width: 500px">
</div>


<div id="index_div" align="center">
    <div style="width: 100%;height: 20%;min-height: 80px;color: #f88b8b;margin-top: 6%;"> ${error} </div>
    <div style="width: 100%;height: 60%;min-height: 500px;min-width: 900px;background: #f1f1f1;">
        <div style="width:480px;height: auto;background: #FCFCFC;border-radius: 3px;position: relative">
            <form name='f' action="${ctx}user/login.shtml" method='POST' id="loginForm" style="padding-top: 10px;padding-bottom: 25px">
                <div class="form-group has-feedback lg left-feedback no-label"
                     style="width: 360px;height: 45px;line-height: 45px;">
                    <%--<img src="${ctx}assets/images/u155.png" width="320px" height="42px" style="margin-top: 14px;">--%>
                    <img src="${ctx}assets/images/u_mange.png" class="index-logo">
                    <label style="background-color: red;width: 1px;height: 30px;vertical-align: middle;margin-bottom: 0px"></label>
                    <img src="${ctx}assets/images/u_mession.png" class="index-session">
                </div>
                <div class="view-line" style="margin-bottom: 10px"></div>
                <div class="form-group has-feedback lg left-feedback no-label" style="width: 360px;">
                    <input name='account'  placeholder="<spring:message code="login.account" />" id="login" type="text"
                           class="form-control input-lg rounded" placeholder="Enter username" autofocus>
                    <span class="form-control-feedback logo-icon"
                          style="background-image: url(${ctx}assets/images/ic_user_login.png);"></span>
                </div>

                <div class="form-group has-feedback lg left-feedback no-label" style="width: 360px;">
                    <input name='password'  placeholder="<spring:message code="login.password" />" id="password"
                           type="password" class="form-control input-lg rounded" placeholder="Enter password">
                    <span class="form-control-feedback logo-icon"
                          style="background-image: url(${ctx}assets/images/ic_code_login.png);"></span>
                </div>

                <%--  <div style="width: 360px;">
                    <div style="float: left;width:240px;" class="form-group has-feedback lg left-feedback no-label" >
                       <input type="text" maxlength="4" name="captcha" id="code" placeholder="<spring:message code="login.captcha" />" class="form-control input-lg rounded" style="ime-mode: disabled" />
                       <span class="form-control-feedback logo-icon" style="background-image: url(${ctx}assets/images/ic_captcha_login.png);"></span>
                    </div>
                    <div style="width: 110px;float: right;">
                        <img alt="a" src="${ctx}captcha.shtml" id="imgcode" style="padding-top: 3px;" onclick="loadCodeImage();"/>
                        <img src="${ctx}assets/images/btn-refresh.png" onclick="loadCodeImage();" id="refresh" style="width: 27px;height: 30px;margin-top: 4px;"/>
                    </div>
                 </div> --%>

                <div style="width: 360px;clear: both">
                    <button id="btn_form_submit" class="btn btn-success ladda-button" data-style="expand-right"
                            type="submit">
                        <span class="ladda-label"><spring:message code="login.name"/></span>
                    </button>
                    <a id="btn_form_register" href="javascript:void(0)" onclick="register(this)"
                       class="btn btn-default"><spring:message code="user.register.title"/></a>
                    <!-- <a style="padding-left:20px;" href="register.shtml">注册</a> -->
                </div>
            </form>


        </div>

        <div>
            <div class="i18n" style="display: inline-block">
                <c:if test="${pageContext.response.locale=='zh_CN'}">
                    <a href="${ctx}?locale=en_US">English(英文)</a> </c:if>
                <c:if test="${pageContext.response.locale=='en_US'}">
                    <a href="${ctx}?locale=zh_CN">中文(chiness)</a> </c:if>
            </div>
            <div class="i18n" style="display: inline-block">
                <a href="${ctx}entrust.shtml"><spring:message code="project.entrust.create"/> </a>
            </div>
        </div>
    </div>

    <%--  <div style="width: 100%;height: 20%;min-height: 150px;">
        <img src="${ctx}assets/images/u_atmss.png" width="350px" height="60px">
     </div> --%>
</div>


</body>
</html>

