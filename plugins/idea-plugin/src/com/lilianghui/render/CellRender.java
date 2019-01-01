package com.lilianghui.render;

import com.intellij.psi.PsiClass;

import javax.swing.*;
import java.awt.*;


public class CellRender extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof PsiClass) {
            setText(((PsiClass) value).getQualifiedName());
        }
        return c;
    }
}
