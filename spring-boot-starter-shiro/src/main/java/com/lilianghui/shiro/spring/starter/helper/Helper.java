package com.lilianghui.shiro.spring.starter.helper;

public final class Helper {

    private static class Md5Inner {
        private static Md5 instance = new Md5();
    }



    private static class RSAInner {
        private static RSA instance = new RSA();
    }

    private static class AESInner {
        private static AES instance = new AES();
    }

    private static class TokenManagerInner {
        private static TokenManager instance = new TokenManager();
    }

    private static class CaptchaManagerInner {
        private static CaptchaManager instance = new CaptchaManager();
    }

    private static class UUIDInner {
        private static UUID instance = new UUID();
    }



    public static Md5 getMd5() {
        return Md5Inner.instance;
    }

    public static AES getAES() {
        return AESInner.instance;
    }


    public static RSA getRSA() {
        return RSAInner.instance;
    }

    public static TokenManager getTokenManager() {
        return TokenManagerInner.instance;
    }

    public static CaptchaManager getCaptchaManager() {
        return CaptchaManagerInner.instance;
    }

    public static UUID getUUID() {
        return UUIDInner.instance;
    }


}
