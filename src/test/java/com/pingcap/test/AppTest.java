package com.pingcap.test;

import static org.junit.Assert.assertTrue;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void test1(){
        BpTree tree1 = new BpTree(3);
        //tree1.insert(3);
        int [] data = { 641,974,240,245,117,10,703,232,480,535,493,22,952,205,448};
        for (int i = 0; i < data.length; i++){
            tree1.insert(data[i]);
            tree1.walk();
        }
        //tree1.insert(6);
        //tree1.insert(5);
        //tree1.insert(146);
        //tree1.insert(21);

        tree1.scanAll();
    }

    @Test
    public void test2(){
        BpTree tree1 = new BpTree(3);
        Random r = new Random(System.currentTimeMillis());
        ArrayList<Integer> datas = new ArrayList<>();
        for (int i = 0; i < 15; i ++){
            int x = r.nextInt(1000);
            datas.add(x);
            tree1.insert(x);
            System.out.printf("%d,", x);
        }
        System.out.println();

        tree1.scanAll();
    }
}
