package com.github.xwc.compiler;

import com.github.xwc.annotations.ShapeType;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * 被注解标记的类属性
 */
public class FactoryAnnotatedCls {
    private TypeElement mAnnotatedClsElement;

    private String mSupperClsQualifiedName;

    private String mSupperClsSimpleName;

    private int type;


    public FactoryAnnotatedCls(TypeElement classElement) throws ProcessingException {
        this.mAnnotatedClsElement = classElement;
        ShapeType annotation = classElement.getAnnotation(ShapeType.class);
        type = annotation.value();
        try {

            mSupperClsSimpleName = annotation.superClass().getSimpleName();
            mSupperClsQualifiedName = annotation.superClass().getCanonicalName();
        } catch (MirroredTypeException mte) {
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            mSupperClsQualifiedName = classTypeElement.getQualifiedName().toString();
            mSupperClsSimpleName = classTypeElement.getSimpleName().toString();
        }

        if (mSupperClsSimpleName == null || mSupperClsSimpleName.equals("")) {
            throw new ProcessingException(classElement,
                    "superClass() in @%s for class %s is null or empty! that's not allowed",
                    ShapeType.class.getSimpleName(), classElement.getQualifiedName().toString());
        }
    }

    public int getType() {
        return type;
    }

    public String getSupperClsQualifiedName() {
        return mSupperClsQualifiedName;
    }

    public String getSupperClsSimpleName() {
        return mSupperClsSimpleName;
    }

    public TypeElement getAnnotatedClsElement() {
        return mAnnotatedClsElement;
    }
}
