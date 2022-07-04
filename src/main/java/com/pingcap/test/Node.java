package com.pingcap.test;

import java.util.ArrayList;

public class Node {
    static int counter = 0;
    int id;
    ArrayList<Integer> keys;
    ArrayList<Node> children;
    Node father;
    int  pos_in_father;

    public Node(){
        counter ++;
        id = counter;
        keys = new ArrayList<>();
        children = new ArrayList<>();
        father = null;
        pos_in_father = -1;
    }
}
