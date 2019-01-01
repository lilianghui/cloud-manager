package com.lilianghui.action;

import com.intellij.analysis.AnalysisScope;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ex.GlobalInspectionContextImpl;
import com.intellij.ide.util.PackageUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiClassUtil;
import com.lilianghui.Context;
import com.lilianghui.ImportRule;
import com.lilianghui.parse.MethodRequestMappingParse;
import com.lilianghui.ui.ImportDialog;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.Parser;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.JavaLanguageHandler;
import net.sourceforge.pmd.lang.java.JavaLanguageModule;
import net.sourceforge.pmd.renderers.Renderer;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.combobox.ListComboBoxModel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ImportAction extends AnAction implements Clicked {
    private static int width = 800;
    private static int height = 400;
    private Set<String> classNames = new HashSet<>();


    @Override
    public void actionPerformed(AnActionEvent event) {
        final Project project = event.getData(PlatformDataKeys.PROJECT);
        WriteCommandAction.runWriteCommandAction(project, new Runnable() {
            @Override
            public void run() {
                try {
//                    ImportAction.this.pmdRule(event, project);
    //                ImportAction.this.pmd(project);
    //                ImportAction.this.parseClass(JavaPsiFacade.getInstance(project).findPackage("net.zhuisuyun.saas.batch.client.web").getClasses());
                    ImportAction.this.build(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void pmdRule(AnActionEvent event, Project project) throws Exception {
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);

        Document document = FileDocumentManager.getInstance().getDocument(psiFile.getVirtualFile());
        String niceFileName = psiFile.getVirtualFile().getCanonicalPath();
        String canonicalPath = niceFileName;


        //总配置
        PMDConfiguration configuration = new PMDConfiguration();
        configuration.setSourceEncoding("utf-8");
        configuration.setInputPaths(canonicalPath);
//        LanguageVersion languageVersion = configuration.getLanguageVersionDiscoverer().getDefaultLanguageVersion(LanguageRegistry.getLanguage("Java"));
        LanguageVersion languageVersion = new LanguageVersion(new JavaLanguageModule(),"11",new JavaLanguageHandler(11));
        configuration.setDefaultLanguageVersion(languageVersion);

//        configuration.setRuleSets("C:\\Users\\Administrator\\Desktop\\pmd\\src\\main\\java\\rules\\unusedcode.xml," +
//                "C:\\Users\\Administrator\\Desktop\\pmd\\src\\main\\java\\rules\\imports.xml");
        //规则上下文
        RuleContext ruleContext = new RuleContext();
        ruleContext.setLanguageVersion(languageVersion);

        //规则
        RuleSets ruleSets = new RuleSets();
        RuleSetFactory factory = new RuleSetFactory();
        Rule rule = new ImportRule(this);
        rule.setLanguage(new JavaLanguageModule());
        RuleSet ruleSet = factory.createSingleRuleRuleSet(rule);
        ruleSets.addRuleSet(ruleSet);

        //报告
        Report report = Report.createReport(ruleContext, niceFileName);

        //其它
//        Parser parser = PMD.parserFor(ruleContext.getLanguageVersion(), configuration);
//        Node rootNode = parser.parse(ruleContext.getSourceCodeFilename(), new StringReader(text));

        configuration.setReportFormat("text");
        net.sourceforge.pmd.renderers.Renderer renderer = configuration.createRenderer(true);
        List<net.sourceforge.pmd.renderers.Renderer> renderers = Collections.singletonList(renderer);
        StringWriter stringWriter = new StringWriter();
        renderer.setWriter(stringWriter);
        renderer.start();

        //开始
//        ruleSets.start(ruleContext);
        try {
            SourceCodeProcessor processor = new SourceCodeProcessor(configuration);
//            ruleContext.setLanguageVersion(null);
            processor.processSourceCode(new StringReader(document.getText()), ruleSets, ruleContext);
        } catch (PMDException e) {
            report.addError(new Report.ProcessingError(e, niceFileName));
        }
        renderer.end();
        for (Renderer r : renderers) {
//            r.renderFileReport(report);
        }

        classNames.forEach(s -> {
            System.out.println(s);
        });
    }


    public void pmd(AnActionEvent event, Project project) throws Exception {
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        //总配置
        PMDConfiguration configuration = new PMDConfiguration();
        configuration.setSourceEncoding(psiFile.getVirtualFile().getCharset().displayName());
        configuration.setInputPaths(psiFile.getVirtualFile().getCanonicalPath());

        //代码文档
        Document document = FileDocumentManager.getInstance().getDocument(psiFile.getVirtualFile());
        String niceFileName = psiFile.getVirtualFile().getCanonicalPath();


        //规则上下文
        RuleContext ruleContext = new RuleContext();


        //规则
        RuleSets ruleSets = new RuleSets();
        RuleSetFactory factory = new RuleSetFactory();
        RuleSet ruleSet = factory.createRuleSet("");
        ruleSets.addRuleSet(ruleSet);

        //报告
        Report report = Report.createReport(ruleContext, niceFileName);

        //其它
        AnalysisScope analysisScope = new AnalysisScope(project);
        Parser parser = PMD.parserFor(ruleContext.getLanguageVersion(), configuration);
        Node rootNode = parser.parse(ruleContext.getSourceCodeFilename(), new StringReader(document.getText()));

        //开始
        ruleSets.start(ruleContext);
        try {
            SourceCodeProcessor processor = new SourceCodeProcessor(configuration);
            processor.processSourceCode(new StringReader(document.getText()), ruleSets, ruleContext);
        } catch (PMDException e) {
            report.addError(new Report.ProcessingError(e, niceFileName));
        }
        ruleSets.end(ruleContext);

        System.out.println(ruleContext.getReport().toString());
    }


    @Override
    public void update(AnActionEvent e) {
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if (editor == null || project == null) {
            e.getPresentation().setVisible(false);
        } else {
            e.getPresentation().setVisible(true);
        }
    }

    private void parseClass(PsiClass[] classes) {
        MethodRequestMappingParse methodRequestMappingParse = new MethodRequestMappingParse();
        Arrays.asList(classes).forEach(psiClass -> {
            PsiAnnotation annotation = psiClass.getAnnotation(MethodRequestMappingParse.RestController);
            annotation = annotation == null ? psiClass.getAnnotation(MethodRequestMappingParse.Controller) : annotation;
            if (annotation != null) {
                System.out.println(psiClass.getQualifiedName());
                methodRequestMappingParse.parse(psiClass);
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


    public void addName(String image) {
        this.classNames.add(image);
    }
}
