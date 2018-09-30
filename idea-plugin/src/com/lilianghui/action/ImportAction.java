package com.lilianghui.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiImportStatement;
import com.lilianghui.Context;
import com.lilianghui.ui.ImportDialog;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.combobox.ListComboBoxModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class ImportAction extends AnAction implements Clicked {
    private static int width = 800;
    private static int height = 400;


    @Override
    public void actionPerformed(AnActionEvent event) {
        final Project project = event.getData(PlatformDataKeys.PROJECT);
        WriteCommandAction.runWriteCommandAction(project, new Runnable() {
            @Override
            public void run() {
                ImportAction.this.build(event);
            }
        });
    }


    private void build(AnActionEvent event) {
        Context context = Context.build(event);
        try {
            if (context.hasElement()) {
                ImportDialog dialog = new ImportDialog(this, context);
                context.setDialog(dialog);
                dialog.pack();
                setModel(dialog, context.pop(0), context.getCurrentPsiClass());
                Dimension size = WindowManager.getInstance().getIdeFrame(context.getProject()).getComponent().getSize();
                dialog.setBounds((size.width - width) / 2, (size.height - height) / 2, width, height);
                dialog.setVisible(true);
            } else if (context.getImportPsiClass().size() > 0) {
                createImportText(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setModel(ImportDialog dialog, String title, Set<PsiClass> psiClasses) {
        if (StringUtils.isNotBlank(title)) {
            dialog.setTitle(title);
        }
        ListModel model = new ListComboBoxModel(new ArrayList(psiClasses));
        dialog.getList1().setModel(model);
    }


    @Override
    public boolean mouseClicked(ImportDialog importDialog, Context context, int type) {
        try {
            if (type == 1) {
                setModel(importDialog, context.previous(), context.getCurrentPsiClass());
            } else {
                PsiClass psiClass = (PsiClass) importDialog.getList1().getSelectedValue();
                if (psiClass != null) {
                    context.putImportPsiClass(psiClass);
                    if (!context.hasElement()) {
                        createImportText(context);
                        return true;
                    } else {
                        setModel(importDialog, context.pop(null), context.getCurrentPsiClass());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void refreshModel(Context context, ImportDialog dialog, String text) {
        Set<PsiClass> psiClasses = context.getCurrentPsiClass();
        setModel(dialog, null, psiClasses.stream().filter(psiClass ->
                StringUtils.isBlank(text) ? true : psiClass.getQualifiedName().contains(text)
        ).collect(Collectors.toSet()));
    }

    private void createImportText(Context context) {
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(context.getProject());
        context.getImportPsiClass().values().forEach(psiClass -> {
            if (psiClass != null) {
                PsiImportStatement psiImportStaticStatement = elementFactory.createImportStatement(psiClass);
                context.getPsiElementOne(Context.IMPORT_TYPE).add(psiImportStaticStatement);
            }
        });
    }


}
