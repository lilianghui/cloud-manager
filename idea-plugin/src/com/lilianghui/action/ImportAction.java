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
import org.jdesktop.swingx.combobox.ListComboBoxModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public class ImportAction extends AnAction implements Clicked {
    private static int width = 800;
    private static int height = 400;


    @Override
    public void actionPerformed(AnActionEvent event) {
        final Project project = event.getData(PlatformDataKeys.PROJECT);
        (new WriteCommandAction.Simple(project) {
            protected void run() throws Throwable {
                ImportAction.this.build(event);
            }
        }).execute();


    }


    private void build(AnActionEvent event) {
        Context context = Context.build(event);
        try {
            if (context.hasElement()) {
                ImportDialog dialog = new ImportDialog(this, context);
                dialog.pack();
                setModel(dialog, context.pop(), context.getCurrentPsiClass());
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
        dialog.setTitle(title);
        ListModel model = new ListComboBoxModel(new ArrayList(psiClasses));
        dialog.getList1().setModel(model);
    }


    @Override
    public boolean mouseClicked(ImportDialog importDialog, Context context) {
        try {
            PsiClass psiClass = (PsiClass) importDialog.getList1().getSelectedValue();
            context.getImportPsiClass().add(psiClass);
            if (!context.hasElement()) {
                createImportText(context);
                return true;
            } else {
                setModel(importDialog, context.pop(), context.getCurrentPsiClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void createImportText(Context context) {
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(context.getProject());
        context.getImportPsiClass().forEach(psiClass -> {
            if (psiClass != null) {
                PsiImportStatement psiImportStaticStatement = elementFactory.createImportStatement(psiClass);
                context.getPsiElementOne(Context.IMPORT_TYPE).add(psiImportStaticStatement);
            }
        });
    }


}
