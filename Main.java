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
public class Main {
    public static void main(String[] args) {
        BTree<Integer> bTree = new BTree<Integer>(3);
//        System.out.print("Is B-tree empty: ");
//        System.out.println(bTree.isEmpty());

        // test insert
        bTree.insert(10);
        bTree.insert(20);
        bTree.insert(25);
        bTree.insert(30);
        bTree.insert(40);
        bTree.insert(50);
        bTree.insert(70);
        bTree.insert(80);
        bTree.insert(85);
        bTree.insert(90);
        bTree.insert(95);
        bTree.insert(55);
        bTree.insert(60);
        bTree.insert(35);
        bTree.insert(82);
        bTree.insert(44);
        
//        bTree.insert(3);
//        bTree.insert(2);
//        bTree.insert(1);
//        bTree.insert(60);
//        bTree.insert(35);
//        bTree.insert(82);
//        bTree.insert(44);

        // test delete
//        bTree.delete(78);

        // construct new B-tree
        DrawBTree drawBTree = new DrawBTree(bTree);
        System.out.print("Total keys: ");
        System.out.println(bTree.getTreeSize());
        System.out.print("Height of B-tree: ");
        System.out.println(bTree.getHeight());

        System.out.println("B-tree in level order: ");
        System.out.println(bTree);
    }
}
