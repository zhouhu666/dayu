package com.example.p2p.controller;

public class Example<T> {
    private T foo;
    private final String bar;

    private Example(T foo, String bar){
        this.foo = foo;
        this.bar = bar;
    }

    public static <T> ExampleBuilder<T> builder(){
        return new ExampleBuilder<T>();
    }

    public static class ExampleBuilder<T>{
        private T foo;
        private String bar;

        private ExampleBuilder(){}

        public ExampleBuilder foo (T foo){
            this.foo = foo;
            return this;
        }

        public ExampleBuilder bar(String bar){
            this.bar = bar;
            return this;
        }

        @java.lang.Override
        public String toString(){
            return "ExampleBuilder(foo = " + foo + ", bar = " + bar + ")";
        }

        public Example build(){
            return new Example(foo, bar);
        }
    }
}
