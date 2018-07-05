package com.github.xwc.compiler;

import javax.lang.model.element.Element;

/**
 * 注解解析异常
 *
 */

public class ProcessingException extends Exception {

    Element element;

    public ProcessingException(Element element, String msg, Object... args) {
        super(String.format(msg, args));
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}