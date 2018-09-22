package com.lilianghui.ui;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
/**
 * My Component
 * User: cdai
 * Date: 13-11-4
 * Time: 上午10:08
 */
public class MyComponent implements ApplicationComponent {
    public MyComponent() {
    }

    @Override
    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    @Override
    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "MyComponent";
    }

    public void sayHello() {
        // Show dialog with message
        Messages.showMessageDialog(
                "Hello World!",
                "Sample",
                Messages.getInformationIcon()
        );
    }
}
