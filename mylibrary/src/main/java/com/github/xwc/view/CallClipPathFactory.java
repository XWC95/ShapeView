package com.github.xwc.view;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xwc on 2018/7/4.
 */

public class CallClipPathFactory {

    private static final String CAL_PRICE_PACKAGE = "com.github.xwc.view";

    private ClassLoader classLoader = getClass().getClassLoader();

    private List<Class<? extends IClipPath>> calPriceList;

    public IClipPath createCalPrice(int shapeType) {

        for (Class<? extends IClipPath> clazz : calPriceList) {
            ShapeType validRegion = handleAnnotation(clazz);

            if (shapeType == validRegion.value()) {
                try {

                    return clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            }
        }
        throw new RuntimeException();
    }

    private ShapeType handleAnnotation(Class<? extends IClipPath> clazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        if (annotations == null || annotations.length == 0) {
            return null;
        }
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i] instanceof ShapeType) {
                return (ShapeType) annotations[i];
            }
        }
        return null;
    }

    private CallClipPathFactory() {
        init();
    }

    private void init() {
        calPriceList = new ArrayList<>();
        File[] resources = getResources();
        Class<IClipPath> calPriceClazz = null;
        try {
            calPriceClazz = (Class<IClipPath>) classLoader.loadClass(IClipPath.class.getName());
        } catch (ClassNotFoundException e1) {
            throw new RuntimeException("未找到策略接口");
        }
        for (int i = 0; i < resources.length; i++) {
            try {
                Class<?> clazz = classLoader.loadClass(CAL_PRICE_PACKAGE + "." + resources[i].getName().replace(".class", ""));
                if (IClipPath.class.isAssignableFrom(clazz) && clazz != calPriceClazz) {
                    calPriceList.add((Class<? extends IClipPath>) clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private File[] getResources() {

        try {
            File file = new File(classLoader.getResource(CAL_PRICE_PACKAGE.replace(".", "/")).toURI());
            return file.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    if (pathname.getName().endsWith(".class")) {
                        return true;
                    }
                    return false;
                }
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException();
        }
    }


    public static CallClipPathFactory getInstance() {
        return CalPriceFactoryInstance.instance;
    }

    private static class CalPriceFactoryInstance {

        private static CallClipPathFactory instance = new CallClipPathFactory();
    }
}
