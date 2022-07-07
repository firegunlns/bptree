package com.pingcap.test;

import static org.junit.Assert.assertTrue;

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

    void PrintList(ArrayList<Integer> lst){
        System.out.print("[");
        for (int i = 0; i < lst.size(); i ++){
            System.out.printf("%d", lst.get(i));
            if (i < (lst.size()-1))
                System.out.printf(",");
        }
        System.out.println("]");
    }

    @Test
    public void test1(){
        BpTree<Integer, String> tree1 = new BpTree<>(3);
        //tree1.insert(3);
        int [] data = { 177,666,514,366,783,192,965,175,470,288,502,780,722,560,464,191,602,451,618,779,556,647,188,484,229,325,248,651,677,
            977,908,543,411,841,107,255,170,370,762,764,967,253,286,484,94,465,959,25,64,882,777,176,435,736,824,508,926,610,21,963,568,369,
            305,561,36,953,148,528,674,810,283,233,333,549,956,556,724,634,55,708,112,390,683,982,865,761,561,983,565,549,953,965,662,307,
            672,590,327,656,331,485};

        ArrayList<Integer> lst = new ArrayList<>();
        for (int i = 0; i < data.length; i++){
            String val = String.format("val-%d", data[i]);
            tree1.insert(data[i], val);
            lst.clear();
            for (int j = 0 ;j < (i + 1); j ++)
                lst.add(data[j]);
            lst.sort(null);
            
            ArrayList<Integer> lst2 = tree1.scanAll2();
            PrintList(lst2);
            PrintList(lst);
            //tree1.walk();
            System.out.println("-------------------");
        }
    }

    @Test
    public void test2(){
        BpTree<Integer,String> tree1 = new BpTree<>(3);

        long n1 = System.currentTimeMillis();
        Random r = new Random(System.currentTimeMillis());
        ArrayList<Integer> datas = new ArrayList<>();
        for (int i = 0; i < 10000000; i ++){
            int x = r.nextInt(1000);
            String val = String.format("val-%d", x);
            datas.add(x);
            tree1.insert(x, val);
            //System.out.printf("%d,", x);
        }
        long n2 = System.currentTimeMillis();

        System.out.printf("time used is: %d seconds", (n2 - n1) / 1000 );

        System.gc();
        /*ArrayList<Integer> lst = new ArrayList<>();
        for (int j = 0; j < datas.size(); j ++)
            lst.add(datas.get(j));*/
        datas.sort(null);

        //tree1.scanAll2();
        ArrayList<Integer> lst2 = tree1.scanAll2();

        /*System.out.print("source data:");
        PrintList(datas);

        System.out.print("sorted data:");
        PrintList(lst);

        System.out.print("s tree data:");
        PrintList(lst2);*/

        // compare
        boolean eq = true;
        for (int i = 0; i < datas.size(); i ++){
            if (!datas.get(i).equals(lst2.get(i))){
                eq = false;
                break;
            }
        }

        if (eq)
            System.out.println("ok");
        else
            System.out.println("not ok");
    }
}
