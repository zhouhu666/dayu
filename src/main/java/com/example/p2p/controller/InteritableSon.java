package com.example.p2p.controller;

import java.lang.annotation.*;

/**
 * 自定义的Annotation
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
//@Inherited
@interface Inheritable{

}

@Inheritable
class InheritableFather{
    public InheritableFather(){
        System.out.println("InheritableFather: "+InheritableFather.class.isAnnotationPresent(Inheritable.class));
    }
}

class InheritableSon extends InheritableFather{
    public InheritableSon(){
        super();
        System.out.println("InheritableSon: "+InheritableSon.class.isAnnotationPresent(Inheritable.class));
    }

    public static void main(String[] args){
        InheritableSon is = new InheritableSon();
    }
}


