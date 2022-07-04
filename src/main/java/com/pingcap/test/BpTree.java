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
            if (key < keys.get(i))
                break;
        return i;
    }

    private void insertKey(ArrayList<Integer> keys, Integer key, int pos){
        keys.add(0);
        for (int i = keys.size() -1; i > pos; i --)
            keys.set(i, keys.get(i - 1));
        
        keys.set(pos, key);
    }

    private void insertNode(ArrayList<Node> nodes, Node node, int pos){
        nodes.add(null);
        for (int i = nodes.size() - 1; i > pos; i --)
            nodes.set(i, nodes.get(i - 1));
        
        nodes.set(pos, node);
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
            // 当前节点，分裂成n1和n2，n1 左边保留 m / 2 个key
            int n1_size = cur.keys.size() / 2;

            Node n1 = new Node();
            n1.father = cur.father;
            n1.pos_in_father = cur.pos_in_father;

            Node n2 = new Node();
            n2.father = cur.father;
            n2.pos_in_father = cur.pos_in_father + 1;

            for (int i = 0; i < cur.keys.size(); i ++){
                // 复制key到n1,n2
                if (i < n1_size)
                    n1.keys.add(cur.keys.get(i));
                else {
                    // 非叶子节点， 中间key上提，不保留n2
                    if ((i > n1_size) || (cur.children.size() == 0))
                        n2.keys.add(cur.keys.get(i));
                }

                // 如果是索引节点，复制children到n1,n2 
                if (cur.children.size() > 0){
                    if (i <= n1_size)
                        n1.keys.add(cur.keys.get(i));
                    else {
                        n2.keys.add(cur.keys.get(i));
                    }
                }
            }

            if (cur.father == null){ // 当前分裂的是根节点，生成一个新的根节点
                Node n3 = new Node();
                n3.father = null;
                root = n3;
                cur.father = n3;
            }


            // m / 2 + 1 位置的key要上提(copy)到父节点的原key位置的左边
            Node father = cur.father;
            Integer k = cur.keys.get(n1_size);

            if (father.children.size()==0){
                father.keys.add(k);
                father.children.add(n1);                
                father.children.add(n2);                
            }else{
                insertKey(father.keys, k, cur.pos_in_father);
                insertNode(father.children, n1, cur.pos_in_father);
                father.children.set(cur.pos_in_father + 1, n2);
            }

            cur = father;
        }

        return true;
    }

    public boolean delete(Integer key){
        return false;
    }

    public Integer find(Integer key){
        return null;
    }
}
