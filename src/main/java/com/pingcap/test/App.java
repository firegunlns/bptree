package com.pingcap.test;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        BpTree bptree = new BpTree(5);
        bptree.insert(97);
        bptree.insert(92);
        bptree.insert(16);

        //System.out.println(val);
    }
}
