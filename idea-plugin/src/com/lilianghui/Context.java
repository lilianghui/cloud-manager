package com.lilianghui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiImportList;
import com.intellij.psi.search.EverythingGlobalScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.lilianghui.parse.ClassParse;
import com.lilianghui.parse.Parse;
import com.lilianghui.ui.ImportDialog;

import java.util.*;

public class Context {
    public static final int IMPORT_TYPE = 0;
    public static final int CLASS_TYPE = 1;


    private Map<Integer, Set<PsiElement>> psiElementMap = new HashMap<>();
    private Set<String> classNames = new HashSet<>();
    private Set<String> removeClassNames = new HashSet<>();
    private AnActionEvent event;
    private Project project;
    private List<String> keys = null;
    private Map<String, Set<PsiClass>> stringSetMap = new HashMap<>();

    private Parse parse = null;
    private ImportDialog dialog;
    private int currentkey = 0;
    private Map<String, PsiClass> importPsiClass = new HashMap<>();

    public static Context build(AnActionEvent event) {
        return build(event, new ClassParse());
    }

    public static Context build(AnActionEvent event, Parse parse) {
        Context context = new Context();
        context.setEvent(event);
        context.buildPsiElement(event);
        context.setProject(event.getData(PlatformDataKeys.PROJECT));
        context.setClassNames(parse.build(context.getPsiElementList(CLASS_TYPE)));
        context.buildPsiClassMapping();
        return context;
    }

    private void buildRemoveImportList() {
        Arrays.asList(((PsiImportList) getPsiElementOne(0)).getImportStatements()).forEach(psiImportStatement -> {
            String name = psiImportStatement.getQualifiedName();
            int index = name.lastIndexOf(".");
            removeClassNames.add(name.substring(index > 0 ? index + 1 : 0));
        });
    }

    private Context buildPsiClassMapping() {
        Set<String> remove = new HashSet<>();
        Set<String> exists = new HashSet<>();
        this.classNames.stream().filter(s -> !removeClassNames.contains(s)).forEach(classSimpleName -> {
            PsiClass[] psiClasses = PsiShortNamesCache.getInstance(project).getClassesByName(classSimpleName, EverythingGlobalScope.allScope(project));
            if (psiClasses != null) {
                if (stringSetMap.get(classSimpleName) == null) {
                    stringSetMap.put(classSimpleName, new HashSet<>());
                }
                Arrays.asList(psiClasses).forEach(psiClass1 -> {
                    if (psiClass1.getQualifiedName().startsWith("java.lang.")) {
                        remove.add(classSimpleName);
                    } else if (!exists.contains(psiClass1.getQualifiedName())) {
                        stringSetMap.get(classSimpleName).add(psiClass1);
                        exists.add(psiClass1.getQualifiedName());
                    }
                });
            }
        });
        stringSetMap.forEach((s, psiClasses) -> {
            if (psiClasses.size() == 0) {
                remove.add(s);
            } else if (psiClasses.size() == 1) {
                remove.add(s);
                PsiClass psiClass = psiClasses.iterator().next();
                putImportPsiClass(psiClass);
            }
        });
        if (remove.size() > 0) {
            remove.forEach(s -> stringSetMap.remove(s));
        }
        keys = new ArrayList<>(stringSetMap.keySet());
        return this;
    }


    private Context buildPsiElement(AnActionEvent event) {
        Arrays.asList(CommonDataKeys.PSI_FILE.getData(event.getDataContext()).getChildren()).forEach(psiElement -> {
            if (psiElement instanceof PsiImportList) {
                putPsiElementMap(IMPORT_TYPE, psiElement);
            } else if (psiElement instanceof PsiClass) {
                putPsiElementMap(CLASS_TYPE, psiElement);
            }
        });
        buildRemoveImportList();
        return this;
    }

    public PsiElement getPsiElementOne(int type) {
        return psiElementMap.get(type).iterator().next();
    }


    public Set<PsiElement> getPsiElementList(int type) {
        return psiElementMap.get(type);
    }

    public void putPsiElementMap(int type, PsiElement psiElement) {
        if (this.psiElementMap.get(type) == null) {
            this.psiElementMap.put(type, new HashSet<>());
        }
        this.psiElementMap.get(type).add(psiElement);
    }

    public AnActionEvent getEvent() {
        return event;
    }

    public void setEvent(AnActionEvent event) {
        this.event = event;
    }


    public void setClassNames(Set<String> classNames) {
        this.classNames = classNames;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String pop(Integer currentkey) {
        if (currentkey != null) {
            this.currentkey = 0;
        } else {
            this.currentkey++;
        }
        setBtnVisable();
        dialog.getTextField1().setText(null);
        return keys.get(this.currentkey);
    }

    public String previous() {
        if (this.currentkey <= 0) {
            this.currentkey = 0;
        } else {
            this.currentkey--;
        }
        setBtnVisable();
        dialog.getTextField1().setText(null);
        return keys.get(this.currentkey);
    }

    public Set<PsiClass> getCurrentPsiClass() {
        return stringSetMap.get(keys.get(this.currentkey));
    }

    public boolean hasElement() {
        return keys.size() - 1 > this.currentkey;
    }

    public Map<String, PsiClass> getImportPsiClass() {
        return importPsiClass;
    }

    public void putImportPsiClass(PsiClass psiClass) {
        this.importPsiClass.put(psiClass.getQualifiedName(), psiClass);
    }

    public void setBtnVisable() {
        boolean flag = true;
        if (this.currentkey <= 0) {
            flag = false;
        }
        dialog.getPreviousButton().setVisible(flag);
    }

    public void setDialog(ImportDialog dialog) {
        this.dialog = dialog;
    }
}
