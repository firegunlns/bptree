package com.pingcap.test;

import java.util.ArrayList;

public class Node {
    ArrayList<Integer> keys;
    Node father;
    int  pos_in_father;
    ArrayList<Node> children;
}
