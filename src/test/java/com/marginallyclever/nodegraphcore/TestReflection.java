package com.marginallyclever.nodegraphcore;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class TestReflection {
    @Test
    public void test() {
        System.out.println("TestReflection:");
        Method [] methods = Math.class.getDeclaredMethods();
        for (Method method : methods) {
            StringBuilder sb = new StringBuilder();
            sb.append("\t").append(method.getName()).append("(");
            Parameter [] parameters = method.getParameters();
            String add="";
            for (Parameter parameter : parameters) {
                sb.append(add)
                    .append(parameter.getType().getName())
                    .append(" ")
                    .append(parameter.getName());
                add=", ";
            }
            sb.append(")");
            System.out.println(sb);
        }
    }
}
