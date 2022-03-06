/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTree;

/**
 *
 * @author minhn
 */
import java.util.ArrayList;

class BTNode<E extends Comparable<E>> {

    private int maxKey;
    private BTNode<E> father;
    private ArrayList<BTNode<E>> children = new ArrayList<BTNode<E>>();
    private ArrayList<E> keys = new ArrayList<>();

    BTNode() {
    }

    BTNode(int order) {
        maxKey = order - 1;
    }

    boolean isLeaf() {  //true if node is a leaf
        if (keys.size() == 0) {
            return false;
        }
        for (BTNode<E> node : children) {   //nếu nó có con thì trả về false
            if (node.keys.size() != 0) {
                return false;
            }
        }
        return true;
    }

    BTNode<E> getFather() {
        return father;
    }

    void setFather(BTNode<E> father) {
        this.father = father;
    }

    BTNode<E> getChild(int index) {
        return children.get(index);
    }

    void addChild(int index, BTNode<E> node) {
        children.add(index, node);
    }

    void removeChild(int index) {   //xoa con khoi array list
        children.remove(index);
    }

    E getKey(int index) {
        return keys.get(index);
    }

    void addKey(int index, E element) {
        keys.add(index, element);
    }

    void removeKey(int index) {
        keys.remove(index);
    }

    boolean isFull() {  //true if order - 1 == keys.size()
        return maxKey == keys.size();
    }

    boolean isOverflow() {  //true if keys.size() > order - 1
        return maxKey < keys.size();
    }

    boolean isNull() {  //true if keys is empty
        return keys.isEmpty();
    }

    int getKeySize() { //trả về số key trong node đó
        return keys.size();
    }

    public String toString() {  //in ra thuoc tinh moi node
        if (keys.size() == 0) {
            return "NullNode";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[Numbers of key(s): ").append(keys.size()).append("] [values: ");
        for (E e : keys) {
            sb.append(e).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());    //xoá ", "
        sb.append("] [father: ");
        if (father.keys.size() == 0) {
            sb.append("NullNode");
        } else {
            for (E e : father.keys) {
                sb.append(e).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("] [children: ");
        for (BTNode<E> node : children) {
            if (node.getKeySize() == 0) {
                sb.append(node).append(", ");
            } else {
                sb.append("NotNullNode" + ", ");
            }
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("] [childrenSize: ").append(children.size()).append("]");
        return sb.toString();
    }
}
