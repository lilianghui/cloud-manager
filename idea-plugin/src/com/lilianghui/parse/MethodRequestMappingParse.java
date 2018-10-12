package com.lilianghui.parse;

import com.intellij.lang.jvm.annotation.JvmAnnotationAttribute;
import com.intellij.lang.jvm.annotation.JvmAnnotationAttributeValue;
import com.intellij.lang.jvm.annotation.JvmAnnotationEnumFieldValue;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

import java.util.*;
import java.util.stream.Collectors;

public class MethodRequestMappingParse {
    public static final String Controller = "org.springframework.web.bind.annotation.Controller";
    public static final String RestController = "org.springframework.web.bind.annotation.RestController";
    public static final String RequestMapping = "org.springframework.web.bind.annotation.RequestMapping";
    public static final String GetMapping = "org.springframework.web.bind.annotation.GetMapping";
    public static final String PostMapping = "org.springframework.web.bind.annotation.PostMapping";
    public static final String PutMapping = "org.springframework.web.bind.annotation.PutMapping";
    public static final String DeleteMapping = "org.springframework.web.bind.annotation.DeleteMapping";

    private static final String method_template = "@Test\n" +
            "public void ${method}() throws Exception {\n" +
            "\tMap<String, String> params = new HashMap<>();\n" +
            "\tString id = \"\";\n" +
            "\tApiResponse<String> response = client.${requestMethod}(String.format(\"%s%s/%s\", baseUrl, \"${requestURL}\", id), params);\n" +
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
        String baseRequestURL = "";
        PsiAnnotation psiAnnotation = psiClass.getAnnotation(RequestMapping);
        if (psiAnnotation != null) {
            Set<JvmAnnotationAttribute> annotations = psiAnnotation.getAttributes().stream().filter(jvmAnnotationAttribute -> "value".equalsIgnoreCase(jvmAnnotationAttribute.getAttributeName()))
                    .collect(Collectors.toSet());
            baseRequestURL = annotations.iterator().next().getAttributeValue().getSourceElement().getText();
        }
        PsiDirectory entityDirectory = psiClass.getContainingFile().getParent().findSubdirectory("entity");
        if (entityDirectory == null) {
            entityDirectory = psiClass.getContainingFile().getParent().createSubdirectory("entity");
        }
        PsiClass respClass;
        PsiFile file = entityDirectory.findFile(psiClass.getName() + "Test.java");
        if (file != null) {
            respClass = ((PsiJavaFile) file).getClasses()[0];
        } else {
            respClass = JavaDirectoryService.getInstance().createClass(entityDirectory, psiClass.getName() + "Test");
//            respClass.getExtendsList().add(getReferenceClass(elementFactory, BaseModelStr)); //确定继承关系
//            respClass.getModifierList().add(elementFactory.createKeyword("public"));//修改权限
        }


//        packageName.add(psiClass1);
        Arrays.asList(psiClass.getMethods()).forEach(psiMethod -> {
            PsiAnnotation annotation = psiMethod.getAnnotation(RequestMapping);
            String requestMethod = null;
            String requestURL = "";
            if (annotation != null) {
                Map<String, JvmAnnotationAttributeValue> stringStringMap = new HashMap<>();
                annotation.getAttributes().forEach(jvmAnnotationAttribute -> {
                    stringStringMap.put(jvmAnnotationAttribute.getAttributeName(), jvmAnnotationAttribute.getAttributeValue());
                });
                requestMethod = "post";
                JvmAnnotationAttributeValue value = stringStringMap.get("method");
                if (value != null) {
                    requestMethod = ((JvmAnnotationEnumFieldValue) value).getField().getName().toLowerCase();
                }
                requestURL = stringStringMap.get("value").getSourceElement().getText();
            }
            if (requestMethod == null && annotation == null) {
                annotation = psiClass.getAnnotation(GetMapping);
                if (annotation != null) {
                    requestMethod = "get";
                    Set<JvmAnnotationAttribute> annotations = annotation.getAttributes().stream().filter(jvmAnnotationAttribute -> "name".equalsIgnoreCase(jvmAnnotationAttribute.getAttributeName()))
                            .collect(Collectors.toSet());
                    if (annotations.size() > 0) {
                        requestURL = annotations.iterator().next().getAttributeValue().getSourceElement().getText();
                    }
                }
            }
            if (requestMethod == null && annotation == null) {
                annotation = psiClass.getAnnotation(PostMapping);
                if (annotation != null) {
                    requestMethod = "post";
                    Set<JvmAnnotationAttribute> annotations = annotation.getAttributes().stream().filter(jvmAnnotationAttribute -> "name".equalsIgnoreCase(jvmAnnotationAttribute.getAttributeName()))
                            .collect(Collectors.toSet());
                    if (annotations.size() > 0) {
                        requestURL = annotations.iterator().next().getAttributeValue().getSourceElement().getText();
                    }
                }
            }
            if (requestMethod == null && annotation == null) {
                annotation = psiClass.getAnnotation(PutMapping);
                if (annotation != null) {
                    requestMethod = "put";
                    Set<JvmAnnotationAttribute> annotations = annotation.getAttributes().stream().filter(jvmAnnotationAttribute -> "name".equalsIgnoreCase(jvmAnnotationAttribute.getAttributeName()))
                            .collect(Collectors.toSet());
                    if (annotations.size() > 0) {
                        requestURL = annotations.iterator().next().getAttributeValue().getSourceElement().getText();
                    }
                }
            }
            if (requestMethod == null && annotation == null) {
                annotation = psiClass.getAnnotation(DeleteMapping);
                if (annotation != null) {
                    requestMethod = "del";
                    Set<JvmAnnotationAttribute> annotations = annotation.getAttributes().stream().filter(jvmAnnotationAttribute -> "name".equalsIgnoreCase(jvmAnnotationAttribute.getAttributeName()))
                            .collect(Collectors.toSet());
                    if (annotations.size() > 0) {
                        requestURL = annotations.iterator().next().getAttributeValue().getSourceElement().getText();
                    }
                }
            }
            if (requestMethod != null && annotation != null) {
                String tpl = method_template.replace("${method}", psiMethod.getName() + "Test");
                tpl = tpl.replace("${requestMethod}", requestMethod);
                tpl = tpl.replace("${requestURL}", requestURL);
                PsiMethod method = elementFactory.createMethodFromText(tpl, respClass);
                respClass.add(method);
            }
        });
    }
}
