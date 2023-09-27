package com.marginallyclever.nodegraphcore;

import org.junit.jupiter.api.Test;
import org.junit.runners.Parameterized;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class TestReflection {
    @Test
    public void test() {
        System.out.println("Hello World!");
        Method [] methods = Math.class.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("\t"+method.getName());
            Parameter [] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                System.out.println("\t\t"+parameter.getName()+" ("+parameter.getType().getName()+")");
            }
        }
    }
}
