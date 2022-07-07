package com.pingcap.test;

import java.util.Random;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        BpTree<Integer, String> tree1 = new BpTree<>(3);
        Random r = new Random(System.currentTimeMillis());
        System.out.print("data:");
        for (int i = 0; i < 10; i ++){
            int x = r.nextInt(1000);
            String val = String.format("val%d", i);
            tree1.insert(x, val);
            System.out.printf("%d,", x);
        }
        System.out.println();

        tree1.walk();
        tree1.scanAll();
    }

    void test1(){
    }
}
