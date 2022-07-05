package com.pingcap.test;

import java.util.ArrayList;

public class BpTree {
    final int MAX_LEVEL = 10240;
    private int level = 5;

    Node root;
    public int getLevel(){
        return level;
    }

    public BpTree(int level){
        if ( (level >=3) && (level <= MAX_LEVEL) )
            this.level = level;
    }

    private int findPos(ArrayList<Integer> keys, Integer key){
        int i = 0;
        for (; i < keys.size(); i ++)
            if (key <= keys.get(i))
                break;
        return i;
    }

    private void insertKey(ArrayList<Integer> keys, Integer key, int pos){
        keys.add(0);
        for (int i = keys.size() -1; i > pos; i --){
            try{
                keys.set(i, keys.get(i - 1));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        keys.set(pos, key);
    }

    private void insertNode(ArrayList<Node> nodes, Node node, int pos){
        nodes.add(null);
        for (int i = nodes.size() - 1; i > pos; i --){
            nodes.set(i, nodes.get(i - 1));
            nodes.get(i).pos_in_father = i;
        }
        
        nodes.set(pos, node);
        node.pos_in_father = pos;
    }

    public boolean insert(Integer key){
        // 如果是空树，生成根节点
        if (root == null){
            root = new Node();
            root.keys.add(key);
            return true;
        }

        // 不是空树，先找到要插入的叶子节点位置
        Node cur = root;
        while(cur.children.size() > 0){ // 叶子节点是没有孩子的
            int pos = findPos(cur.keys, key);
            cur = cur.children.get(pos);  // 继续搜索下一层节点
        }

        // 叶子节点
        // 找到插入位置
        int pos = findPos(cur.keys, key);
        // insert key
        insertKey(cur.keys, key, pos);

        // 分裂
        while( (cur.keys.size() + 1) > level){
            //root.walk();

            // 当前节点，分裂成n1和n2，n1 左边保留 m / 2 个key
            int n1_size = cur.keys.size() / 2;

            Node n1 = new Node();
            n1.father = cur.father;
            n1.pos_in_father = cur.pos_in_father;

            Node n2 = new Node();
            n2.father = cur.father;
            n2.pos_in_father = cur.pos_in_father + 1;

            // 复制next/last指针
            if (cur.children.size() == 0){
                n1.next = n2;
                n2.last = n1;
                n2.next = cur.next;
                
                if (cur.next != null)
                    n2.next.last = n2;
                
                if (cur.last != null)
                    cur.last.next = n1;
            }

            // 复制key到n1,n2
            for (int i = 0; i < cur.keys.size(); i ++){
                if (i < n1_size)
                    n1.keys.add(cur.keys.get(i));
                else {
                    // 非叶子节点， 中间key上提，不保留n2
                    if ((i > n1_size) || (cur.children.size() == 0))
                        n2.keys.add(cur.keys.get(i));
                }
            }

            // 复制children到n1,n2，调整children的指向和序pos_in_father 
            for (int i = 0; i < cur.children.size(); i ++){
                Node child = cur.children.get(i);
                if (i <= n1_size){
                    n1.children.add(child);
                    child.father = n1;
                    child.pos_in_father = i;
                }
                else {
                    n2.children.add(child);
                    child.father = n2;
                    child.pos_in_father = i - n1_size - 1;
                }
            }

            // m / 2 + 1 位置的key要上提(copy)到父节点的原key位置的左边
            Integer k = cur.keys.get(n1_size);

            if (cur.father == null){ // 当前分裂的是根节点，生成一个新的根节点
                Node n3 = new Node();
                root = n3;

                n3.keys.add(k);
                n3.children.add(n1);                
                n3.children.add(n2);     
                
                n1.father = n2.father = n3;
                n1.pos_in_father = 0;           
                n2.pos_in_father = 1;           

                cur.deleted = true;
                cur = n3;
            }else{
                Node father = cur.father;
                n1.father = n2.father = father;

                insertKey(father.keys, k, cur.pos_in_father);
                insertNode(father.children, n1, cur.pos_in_father);

                cur.father.children.set(cur.pos_in_father + 1, n2);
                n1.pos_in_father = cur.pos_in_father;
                n2.pos_in_father = cur.pos_in_father + 1;

                cur = father;
            }
        }

        return true;
    }

    public boolean delete(Integer key){
        return false;
    }

    public Integer find(Integer key){
        return null;
    }

    public void scanAll(){
        Node cur = root;
        while (cur.children.size()!=0)
            cur = cur.children.get(0);

        while(cur != null){
            cur.log();
            cur = cur.next;
        }
    }

    public void walk(){
        root.walk();
    }
}
