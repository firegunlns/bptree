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

    public boolean insert(Integer key){
        // 如果是空树，生成根节点
        if (root == null){
            root = new Node();
            root.father = null;
            root.keys.add(key);
            return true;
        }

        // 不是空树，先找到要插入的叶子节点位置
        Node cur = root;
        while(cur.children.size() > 0){
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
            // 分裂 并向上衍生
            if (cur.father == null){ // 当前分裂的是根节点，生成一个新的根节点
                Node n3 = new Node();
                n3.father = null;
                cur.father = n3;
                root = n3;
            }

            // 左边保留 m / 2 个key
            int n1_size = cur.keys.size() / 2;

            // 分裂成n1 和 n2
            Node n1 = new Node();
            n1.father = cur.father;

            Node n2 = new Node();
            n2.father = cur.father;

            // 复制key到n1,n2
            for (int i = 0; i < cur.keys.size(); i ++){
                if (i < n1_size)
                    n1.keys.add(cur.keys.get(i));
                else
                    n2.keys.add(cur.keys.get(i));
            }

            // 如果不是叶子节点，m / 2 + 1 位置的key要上提到父节点的原key位置的左边

            for (int i = n1_size; i < cur.keys.size(); i ++){
                n2.keys.add(cur.keys.get(i));
                if (cur.children.size() > 0)
                    n1.children.add(cur.children.get(i));
            }

                n3.keys.add(cur.keys.get(n1_size));

                n1.father = n3;
                n1.pos_in_father = 0;
                
                n2.father = n3;
                n2.pos_in_father = 1;

                n3.children.add(n1);
                n3.children.add(n2);

                root = n3;

                if (cur.children.size() > 0){ // 当前分裂的如果是索引节点，则中间key上升到上层)

                }
            }

            if (cur.children.size() > 0){ // 当前分裂的如果是索引节点，则中间key上升到上层
                
            }
        }

        return false;
    }

    public boolean delete(Integer key){
        return false;
    }

    public Integer find(Integer key){
        return null;
    }
}
