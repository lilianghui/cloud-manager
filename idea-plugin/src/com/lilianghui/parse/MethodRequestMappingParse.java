package com.lilianghui.parse;

import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

import java.util.Arrays;
import java.util.List;

public class MethodRequestMappingParse {

    private static final String method_template = "@Test\n" +
            "public void ${method}() throws Exception {\n" +
            "\tMap<String, String> params = new HashMap<>();\n" +
            "\tString id = \"\";\n" +
            "\tApiResponse<String> response = client.post(String.format(\"%s%s/%s\", baseUrl, Constants.BatchClientAPI.DELETE, id), params);\n" +
            "\tassertNotNull(response);\n" +
            "\tassertEquals(HttpStatus.SC_OK, response.getStatusCode());\n" +
            "}";

    public void parse(PsiClass psiClass) {
//        PsiJavaFile psiFile = (PsiJavaFile) PsiFileFactory.getInstance(psiClass.getProject()).createFileFromText("public class A{" +
//                "}", StdFileTypes.JAVA, "");
//        // 通过获取到PsiElementFactory来创建相应的Element，包括字段，方法，注解，类，内部类等等
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
//        PsiJavaCodeReferenceElement packageName = elementFactory.createPackageReferenceElement(psiClass.getName().substring(psiClass.getName().lastIndexOf(".")+1));
//        PsiClass psiClass1 = elementFactory.createClass(psiClass.getName() + "Test");


        PsiDirectory entityDirectory = psiClass.getContainingFile().getParent().findSubdirectory("entity");
        if (entityDirectory == null) {
            entityDirectory = psiClass.getContainingFile().getParent().createSubdirectory("entity");
        }
        PsiClass respClass;
        PsiFile file = entityDirectory.findFile(psiClass.getName() + "Test.java");
        if (file != null) {
            respClass = ((PsiJavaFile) file).getClasses()[0];
        } else {
            respClass = JavaDirectoryService.getInstance().createClass(entityDirectory, psiClass.getName()+"Test");
//            respClass.getExtendsList().add(getReferenceClass(elementFactory, BaseModelStr)); //确定继承关系
            respClass.getModifierList().add(elementFactory.createKeyword("public"));//修改权限
        }


//        packageName.add(psiClass1);
        Arrays.asList(psiClass.getMethods()).forEach(psiMethod -> {
            PsiAnnotation annotation = psiMethod.getAnnotation("org.springframework.web.bind.annotation.RequestMapping");
            annotation = annotation == null ? psiClass.getAnnotation("org.springframework.web.bind.annotation.GetMapping") : annotation;
            annotation = annotation == null ? psiClass.getAnnotation("org.springframework.web.bind.annotation.PostMapping") : annotation;
            annotation = annotation == null ? psiClass.getAnnotation("org.springframework.web.bind.annotation.PutMapping") : annotation;
            annotation = annotation == null ? psiClass.getAnnotation("org.springframework.web.bind.annotation.DeleteMapping") : annotation;
            if (annotation != null) {
                elementFactory.createMethodFromText(method_template.replace("${method}", psiMethod.getName() + "Test"), respClass);
            }
        });
    }
}
