package com.pingcap.test;

import static org.junit.Assert.assertTrue;

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
        tree1.insert(3);
        tree1.insert(6);
        tree1.insert(5);
        tree1.insert(146);
        tree1.insert(21);
    }
}
