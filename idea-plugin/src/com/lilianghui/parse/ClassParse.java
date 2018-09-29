package com.lilianghui.parse;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.tree.java.PsiAnnotationImpl;
import com.intellij.psi.impl.source.tree.java.PsiAssignmentExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiLocalVariableImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClassParse implements Parse {
    private Set<String> classNames = new HashSet<>();
    private Set<String> removeClassNames = new HashSet<>();

    {
        removeClassNames.add("void");
    }

    public Set<String> build(Set<PsiElement> psiElements) {
        methodParse(psiElements);
        fieldParse(psiElements);
        this.classNames.removeAll(removeClassNames);
        return this.classNames;
    }

    private void fieldParse(Set<PsiElement> psiElements) {
        psiElements.forEach(psiElement -> {
            Arrays.asList(((PsiClass) psiElement).getFields()).forEach(psiField -> {
                addClassName(psiField.getType().getCanonicalText(true));
            });
        });
    }

    private void annotationParse(PsiAnnotation[] psiAnnotations) {
        Arrays.asList(psiAnnotations).forEach(psiAnnotation -> {
            addClassName(((PsiAnnotationImpl) psiAnnotation).getQualifiedName());
        });
    }


    private void methodParse(Set<PsiElement> psiElements) {
        psiElements.forEach(psiElement -> {
            Set<PsiElement> innerClasses = new HashSet<>();
            Arrays.asList(((PsiClass) psiElement).getInnerClasses()).forEach(psiClass -> {
                innerClasses.add(psiClass);
            });
            if (innerClasses.size() > 0) {
                build(innerClasses);
            }
            Arrays.asList(((PsiClass) psiElement).getMethods()).forEach(psiMethod -> {
                PsiType type = psiMethod.getReturnType();
                annotationParse(psiMethod.getAnnotations());
                addClassName(type.getCanonicalText(true));
                Arrays.asList(psiMethod.getParameterList().getParameters()).forEach(psiParameter -> {
                    annotationParse(psiParameter.getAnnotations());
                    addClassName(psiParameter.getType().getCanonicalText(true));
                });
                Arrays.asList(psiMethod.getBody().getStatements()).forEach(psiStatement -> {
                    Arrays.asList(psiStatement.getChildren()).forEach(varPsiElement -> {
                        if (varPsiElement instanceof PsiLocalVariableImpl) {
                            addClassName(((PsiLocalVariableImpl) varPsiElement).getType().getCanonicalText(true));
                        } else if (varPsiElement instanceof PsiAssignmentExpressionImpl) {
                            addClassName(((PsiAssignmentExpressionImpl) varPsiElement).getRExpression().getType().getCanonicalText(true));
                        }
                    });
                });
            });
        });
    }

    private void addClassName(String canonicalText) {
        if (canonicalText.indexOf(".") < 0) {
            classNames.add(canonicalText);
        }
    }

}
