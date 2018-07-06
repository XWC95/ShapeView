package com.github.xwc.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * 工厂代码生成器
 *
 */
public class FactoryCodeBuilder {

    private static final String SUFFIX = "Factory";

    private String mSupperClsName;

    private Map<String, FactoryAnnotatedCls> mAnnotatedClasses = new LinkedHashMap<>();

    public void add(FactoryAnnotatedCls annotatedCls) {
        if (mAnnotatedClasses.get(annotatedCls.getAnnotatedClsElement().getQualifiedName().toString()) != null)
            return;

        mAnnotatedClasses.put(
            annotatedCls.getAnnotatedClsElement().getQualifiedName().toString(),
            annotatedCls);
    }

    public void clear() {
        mAnnotatedClasses.clear();
    }

    public FactoryCodeBuilder setSupperClsName(String supperClsName) {
        mSupperClsName = supperClsName;
        return this;
    }

    public void generateCode(Messager messager, Elements elementUtils, Filer filer) throws IOException {
        TypeElement superClassName = elementUtils.getTypeElement(mSupperClsName);
        String factoryClassName = superClassName.getSimpleName() + SUFFIX;
        PackageElement pkg = elementUtils.getPackageOf(superClassName);
        String packageName = pkg.isUnnamed() ? null : pkg.getQualifiedName().toString();

        TypeSpec typeSpec = TypeSpec
            .classBuilder(factoryClassName)
            .addModifiers(Modifier.PUBLIC)
            .addMethod(newCreateMethod(elementUtils, superClassName))
            .build();

        // Write file
        JavaFile.builder(packageName, typeSpec).build().writeTo(filer);
    }

    private MethodSpec newCreateMethod(Elements elementUtils, TypeElement superClassName) {

        MethodSpec.Builder method =
            MethodSpec.methodBuilder("create") //设置方法名字
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC) //设置方法类型为public static
                .addParameter(int.class, "type") //设置参数
                .addParameter(Object.class, "shape")
                .returns(TypeName.get(superClassName.asType())); //设置返回

        method.beginControlFlow("if (type < 0)") //beginControlFlow与endControlFlow要成对调用
            .addStatement("throw new IllegalArgumentException($S)", "type is less then 0!")
            .endControlFlow();

        for (FactoryAnnotatedCls annotatedCls : mAnnotatedClasses.values()) { //遍历所有保存起来的被注解的生产线类
            String packName = elementUtils
                .getPackageOf(annotatedCls.getAnnotatedClsElement())
                .getQualifiedName().toString(); //获取生产线类的包名全路径
            String clsName = annotatedCls.getAnnotatedClsElement().getSimpleName().toString(); //获取生产线类名字
            ClassName cls = ClassName.get(packName, clsName); //组装成一个ClassName

            //将该生产线类的所有id组成数组
            int type = annotatedCls.getType();

            // CirclePath是不需要在构造方法中传递参数的所以这里检测一下
            for (Element enclosed : annotatedCls.getAnnotatedClsElement().getEnclosedElements()) {
                if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                    ExecutableElement constructorElement = (ExecutableElement) enclosed;
                    if (constructorElement.getParameters().size() == 0 && constructorElement.getModifiers()
                        .contains(Modifier.PUBLIC)) {
                        method.beginControlFlow("if (type == $L)", type)
                            .addStatement("return new $T()", cls)
                            .endControlFlow();
                    } else {
                        method.beginControlFlow("if (type == $L)", type)
                            .addStatement("return new $T(shape)", cls)
                            .endControlFlow();
                    }
                }
            }
        }

        method.addStatement("throw new IllegalArgumentException($S + type)", "Unknown type = ");

        return method.build();
    }
}