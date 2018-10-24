package com.lilianghui.shiro.spring.starter.core;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CaptchaUsernamePasswordToken extends UsernamePasswordToken {
	private String captcha;

	public CaptchaUsernamePasswordToken(final String username, final String password, final String captcha) {
		super(username, password);
		this.captcha = captcha;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	
}
