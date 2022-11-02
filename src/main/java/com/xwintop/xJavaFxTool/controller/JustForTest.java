package com.xwintop.xJavaFxTool.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JustForTest {
    public static final String TOGEN_ACB = "123123123";

    public static void main(String[] args) {
        List<String> as = new ArrayList<>();
        as.add("hello world ");
        System.out.println("hello world");
        Collections.emptyList();//for sonarlint
        System.out.println("hello world c");
        System.out.println(TOGEN_ACB);
    }
}
