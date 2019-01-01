package com.lilianghui.parse;

import com.intellij.psi.PsiElement;

import java.util.Set;

public interface Parse {
    Set<String> build(Set<PsiElement> psiElementList);
}
