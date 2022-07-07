package com.pingcap.test;

import java.util.ArrayList;

public class BpTree <T_K extends Comparable<T_K>, T_V> {
    final int MAX_LEVEL = 10240;
    private int level = 3;

    Node<T_K, T_V> root;
    public int getLevel(){
        return level;
    }

    public BpTree(int level){
        if ( (level >=3) && (level <= MAX_LEVEL) )
            this.level = level;
    }

    private int findPos(ArrayList<T_K> keys, T_K key){
        int i = 0;
        for (; i < keys.size(); i ++)
            if (key.compareTo(keys.get(i)) <= 0)
                break;
        return i;
    }

    private <T> void insertObj(ArrayList<T> lst, T obj, int pos){
        lst.add(null);
        for (int i = lst.size() -1; i > pos; i --){
            lst.set(i, lst.get(i - 1));
        }
        
        lst.set(pos, obj);
    }

    private void insertKey(ArrayList<T_K> keys, T_K key, ArrayList<T_V> datas, T_V data, int pos){
        keys.add(null);
        datas.add(null);
        for (int i = keys.size() -1; i > pos; i --){
            try{
                keys.set(i, keys.get(i - 1));
                datas.set(i, datas.get(i - 1));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        keys.set(pos, key);
        datas.set(pos, data);
    }

    private void insertNode(ArrayList<Node<T_K, T_V>> nodes, Node<T_K, T_V> node, int pos){
        nodes.add(null);
        for (int i = nodes.size() - 1; i > pos; i --){
            nodes.set(i, nodes.get(i - 1));
            nodes.get(i).pos_in_father = i;
        }
        
        nodes.set(pos, node);
        node.pos_in_father = pos;
    }

    public boolean insert(T_K key, T_V val){
        // 如果是空树，生成根节点
        if (root == null){
            root = new Node<T_K, T_V>();
            root.keys.add(key);
            root.datas.add(val);
            return true;
        }

        // 不是空树，先找到要插入的叶子节点位置
        Node<T_K, T_V> cur = root;
        while(cur.children.size() > 0){ // 叶子节点是没有孩子的
            int pos = findPos(cur.keys, key);
            cur = cur.children.get(pos);  // 继续搜索下一层节点
        }

        // 叶子节点
        // 找到插入位置
        int pos = findPos(cur.keys, key);
        // insert key & value
        insertObj(cur.keys, key, pos);
        insertObj(cur.datas, val, pos);

        // 分裂
        while( (cur.keys.size() + 1) > level){
            //root.walk();

            // 当前节点，分裂成n1和n2，n1 左边保留 m / 2 个key
            int n1_size = cur.keys.size() / 2;

            Node<T_K, T_V> n1 = new Node<T_K, T_V>();
            n1.father = cur.father;
            n1.pos_in_father = cur.pos_in_father;

            Node<T_K, T_V> n2 = new Node<T_K, T_V>();
            n2.father = cur.father;
            n2.pos_in_father = cur.pos_in_father + 1;

            // 复制next/last指针
            if (cur.children.size() == 0){
                n1.next = n2;
                n1.last = cur.last;
                
                n2.next = cur.next;
                n2.last = n1;
                
                if (cur.next != null)
                    n2.next.last = n2;
                
                if (cur.last != null)
                    cur.last.next = n1;
            }

            // 复制key和data到n1,n2
            for (int i = 0; i < cur.keys.size(); i ++){
                if (i < n1_size){
                    n1.keys.add(cur.keys.get(i));
                    if (cur.children.size() ==0)
                        n1.datas.add(cur.datas.get(i));
                }
                else {
                    // 叶子节点
                    if  (cur.children.size() == 0){
                        n2.keys.add(cur.keys.get(i));
                        n2.datas.add(cur.datas.get(i));
                    }
                    // 非叶子节点， 中间key上提，不保留n2
                    else if (i > n1_size) 
                        n2.keys.add(cur.keys.get(i));
                }
            }

            // 复制children到n1,n2，调整children的指向和序pos_in_father 
            for (int i = 0; i < cur.children.size(); i ++){
                Node<T_K, T_V> child = cur.children.get(i);
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
            T_K k = cur.keys.get(n1_size);

            if (cur.father == null){ // 当前分裂的是根节点，生成一个新的根节点
                Node<T_K, T_V> n3 = new Node<T_K, T_V>();
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
                Node<T_K, T_V> father = cur.father;
                n1.father = n2.father = father;

                int pos_in_father = cur.pos_in_father;
                insertObj(father.keys, k, pos_in_father);
                insertNode(father.children, n1, pos_in_father);

                cur.father.children.set(pos_in_father + 1, n2);
                n1.pos_in_father = pos_in_father;
                n2.pos_in_father = pos_in_father + 1;

                cur.deleted = true;
                cur = father;
            }
        }

        return true;
    }

    public boolean delete(T_K key){
        return false;
    }

    public T_V find(T_K key){
        Node<T_K, T_V> cur = root;

        // 先找到叶子节点
        while(cur.children.size() >0){
            int pos = 0;
            while(pos < cur.keys.size() && ( key.compareTo(cur.keys.get(pos)) > 0) )
                pos ++;
            
            cur = cur.children.get(pos);
        }

        // 再找对应的Key
        int pos = 0;
        while(pos < cur.keys.size() && ( key.compareTo(cur.keys.get(pos)) > 0) )
            pos ++;

        if (key.compareTo(cur.keys.get(pos)) == 0)
            return cur.datas.get(pos);
        
        return null;
    }

    public void scanAll(){
        Node<T_K, T_V> cur = root;
        while (cur.children.size()!=0)
            cur = cur.children.get(0);

        while(cur != null){
            cur.log();
            cur = cur.next;
        }
    }

    public ArrayList<T_K> scanAll2(){
        Node<T_K, T_V> cur = root;
        while (cur.children.size()!=0)
            cur = cur.children.get(0);

        ArrayList<T_K> lst = new ArrayList<>();
        while(cur != null){
            for (int i = 0; i < cur.keys.size(); i ++)
                lst.add(cur.keys.get(i));

            cur = cur.next;
        }

        return lst;
    }

    public void walk(){
        root.walk();
    }
}
