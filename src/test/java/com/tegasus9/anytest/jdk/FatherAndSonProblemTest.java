package com.tegasus9.anytest.jdk;

import org.junit.jupiter.api.Test;

/**
 * @author XiongYiGe
 * @date 2022/8/17
 * @description 非子类是否可以调用被子类覆盖的父类方法？
 */
public class FatherAndSonProblemTest {
    private static class A{
        public void show(){
            System.out.println("A show");
        }
    }
    private static class B extends A{
        @java.lang.Override
        public void show() {
            System.out.println("B show");
        }
    }
    @Test
    public void testSonToFather(){
        B b = new B();
        ((A)b).show();//always B show
    }
}
