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

import java.util.*;

public class Context {
    public static final int IMPORT_TYPE = 0;
    public static final int CLASS_TYPE = 1;


    private Map<Integer, Set<PsiElement>> psiElementMap = new HashMap<>();
    private Set<String> classNames = new HashSet<>();
    private AnActionEvent event;
    private Project project;
    private LinkedList<String> keys = null;
    private Map<String, Set<PsiClass>> stringSetMap = new HashMap<>();

    private Parse parse = null;

    private String currentkey;
    private Set<PsiClass> importPsiClass = new HashSet<>();

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

    private Context buildPsiClassMapping() {
        this.classNames.forEach(classSimpleName -> {
            PsiClass[] psiClasses = PsiShortNamesCache.getInstance(project).getClassesByName(classSimpleName, EverythingGlobalScope.allScope(project));
            if (psiClasses != null) {
                if (stringSetMap.get(classSimpleName) == null) {
                    stringSetMap.put(classSimpleName, new HashSet<>());
                }
                Arrays.asList(psiClasses).forEach(psiClass1 -> {
                    stringSetMap.get(classSimpleName).add(psiClass1);
                });
            }
        });
        Set<String> remove = new HashSet<>();
        stringSetMap.forEach((s, psiClasses) -> {
            if (psiClasses.size() == 1) {
                remove.add(s);
                this.importPsiClass.add(psiClasses.iterator().next());
            }
        });
        if (remove.size() > 0) {
            remove.forEach(s -> stringSetMap.remove(s));
        }
        keys = new LinkedList<>(stringSetMap.keySet());
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

    public String pop() {
        this.currentkey = keys.pop();
        return this.currentkey;
    }

    public Set<PsiClass> getCurrentPsiClass() {
        return stringSetMap.get(this.currentkey);
    }

    public boolean hasElement() {
        return keys.size() > 0;
    }

    public Set<PsiClass> getImportPsiClass() {
        return importPsiClass;
    }

    public void setImportPsiClass(Set<PsiClass> importPsiClass) {
        this.importPsiClass = importPsiClass;
    }
}
