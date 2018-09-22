package com.lilianghui.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.lilianghui.ui.MyComponent;

/**
 * Say Hello Action
 * User: cdai
 * Date: 13-11-4
 * Time: 上午10:16
 */
public class SayHelloAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Application application = ApplicationManager.getApplication();
        MyComponent myComponent = application.getComponent(MyComponent.class);
        myComponent.sayHello();
    }
}
