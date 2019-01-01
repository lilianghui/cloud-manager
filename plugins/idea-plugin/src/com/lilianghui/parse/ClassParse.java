package com.lilianghui.parse;

import com.intellij.lang.jvm.types.JvmReferenceType;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiTypeElementImpl;
import com.intellij.psi.impl.source.tree.java.*;

import java.util.*;

public class ClassParse implements Parse {
    private Set<String> classNames = new HashSet<>();
    private Set<String> removeClassNames = new HashSet<>();

    {
        removeClassNames.add("void");
        removeClassNames.add("String");
    }

    @Override
    public Set<String> build(Set<PsiElement> psiElements) {
        methodParse(psiElements);
        fieldParse(psiElements);
        this.classNames.removeAll(removeClassNames);
        return this.classNames;
    }

    /**
     * 类字段
     *
     * @param psiElements
     */
    private void fieldParse(Set<PsiElement> psiElements) {
        psiElements.forEach(psiElement -> {
            Arrays.asList(((PsiClass) psiElement).getFields()).forEach(psiField -> {
                try {
                    addClassName(psiField.getType().getCanonicalText(true));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void annotationParse(PsiAnnotation[] psiAnnotations) {
        Arrays.asList(psiAnnotations).forEach(psiAnnotation -> {
            try {
                addClassName(((PsiAnnotationImpl) psiAnnotation).getQualifiedName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void superClassParse(JvmReferenceType superClassType) {
        addClassName(superClassType.getName());
    }


    private void methodParse(Set<PsiElement> psiElements) {
        psiElements.forEach(psiElement -> {
            PsiClass psiClass = (PsiClass) psiElement;
            //内部类
            Set<PsiElement> innerClasses = new HashSet<>();
            Arrays.asList(psiClass.getInnerClasses()).forEach(innerPsiClass -> {
                innerClasses.add(innerPsiClass);
            });

            //继承
            if (psiClass.getSuperClassType() != null) {
                superClassParse(psiClass.getSuperClassType());
            }

            //接口
            if (psiClass.getInterfaces() != null) {
                Collections.addAll(innerClasses, psiClass.getInterfaces());
            }
            if (innerClasses.size() > 0) {
                build(innerClasses);
            }
            //类注解
            annotationParse(psiClass.getAnnotations());


            //类方法
            Arrays.asList(psiClass.getMethods()).forEach(psiMethod -> {
                //方法返回
                PsiType type = psiMethod.getReturnType();
                addClassName(type.getCanonicalText(true));
                //方法注解
                annotationParse(psiMethod.getAnnotations());
                //方法参数
                Arrays.asList(psiMethod.getParameterList().getParameters()).forEach(psiParameter -> {
                    try {
                        annotationParse(psiParameter.getAnnotations());
                        addClassName(psiParameter.getType().getCanonicalText(true));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                //方法体
                Arrays.asList(psiMethod.getBody().getStatements()).forEach(psiStatement -> {
                    Arrays.asList(psiStatement.getChildren()).forEach(varPsiElement -> {
                        try {
                            if (varPsiElement instanceof PsiLocalVariableImpl) {
                                varParse(varPsiElement);
                                addClassName(((PsiLocalVariableImpl) varPsiElement).getType().getCanonicalText(true));
                            } else if (varPsiElement instanceof PsiAssignmentExpressionImpl) {
                                PsiExpression psiExpression = ((PsiAssignmentExpressionImpl) varPsiElement).getRExpression();
                                if (psiExpression instanceof PsiMethodCallExpressionImpl) {
                                    addClassName(((PsiMethodCallExpressionImpl) psiExpression).getMethodExpression().getFirstChild().getText());
                                } else if (psiExpression.getType() != null) {
                                    addClassName(psiExpression.getType().getCanonicalText(true));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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

    private void varParse(PsiElement varPsiElement) {
        Arrays.asList(((PsiLocalVariableImpl) varPsiElement).getPsi().getChildren()).forEach(psiElement -> {
            if (psiElement instanceof PsiTypeElementImpl) {
                for(String s:psiElement.getText().trim().replaceAll(">","").split("<")){
                    addClassName(s);
                }
            } else if (psiElement instanceof PsiNewExpressionImpl) {
            }
        });
    }
}
