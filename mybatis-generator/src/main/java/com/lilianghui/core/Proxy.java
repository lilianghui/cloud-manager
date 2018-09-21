package com.lilianghui.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Proxy implements InvocationHandler{
	public static void main(String[] args) {
//		IHello hello = new Hello();
//		hello =(IHello) java.lang.reflect.Proxy.newProxyInstance(hello.getClass()
//				.getClassLoader(), hello.getClass().getInterfaces(), new Proxy());
//		System.out.println(hello.say("==="));;
		Proxy proxy=new Proxy();
		System.out.println(proxy);
		swap(proxy);
		System.out.println(proxy);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println("Proxy.invoke()");
		return null;
	}
	
	private static void swap(Proxy proxy) {
		proxy=new Proxy();
	}
}

interface IHello {
	String say(String name);
}

class Hello implements IHello {

	@Override
	public String say(String name) {
		return name;
	}

}