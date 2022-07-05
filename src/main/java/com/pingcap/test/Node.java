package com.pingcap.test;

import java.util.ArrayList;

public class Node {
    static int counter = 0;
    int id;
    ArrayList<Integer> keys;
    ArrayList<Node> children;
    Node father;
    int  pos_in_father;
    Node next;
    Node last;
    boolean deleted;

    public Node(){
        counter ++;
        id = counter;
        keys = new ArrayList<>();
        children = new ArrayList<>();
        father = null;
        pos_in_father = -1;
        next = last = null;
        deleted = false;
    }

    public void log(){
        StringBuffer sb = new StringBuffer();
        StringBuffer sb1 = new StringBuffer();
        for (int i = 0; i < keys.size(); i++){
            sb.append(keys.get(i).toString());
            if (i < keys.size() - 1)
                sb.append(",");
        }

        for (int i = 0; i < children.size(); i++){
            sb1.append(Integer.toString(children.get(i).id));
            if (i < children.size() - 1)
                sb1.append(",");
        }

        int fid = 0;
        if (father != null)
            fid = father.id;
        
        int nextid = 0;
        if (next != null)
            nextid = next.id;

        int lastid = 0;
        if (last != null)
            lastid = last.id;
        System.out.printf("node(id=%d, key=[%s], father=%d, pos_in_father=%d, children=[%s]), next=%d, last=%d, deleted=%b\n", id, sb,
            fid, pos_in_father, sb1, nextid, lastid, deleted);
    }

    public void walk(){
        walk(this);
        System.out.println("-----------------");
    }

    public void walk(Node from){
        if (Debug.log == false)
            return;

        from.log();
        for (Node node: from.children){
            walk(node);
        }
    }
}
