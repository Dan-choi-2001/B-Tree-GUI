/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTree;

import java.util.ArrayList;

/**
 *
 * @author minhn
 */
public class NewClass {
    public static void main(String[] args) {
        ArrayList<Integer> a=new ArrayList();
        a.add(0, 3);
        a.add(1, 7);
        a.add(2, 8);
        a.add(3, 2);
        a.add(4, 1);
        for (Integer integer : a) {
            System.out.println(integer);
        }
        a.remove(1);
        System.out.println(a.indexOf(8));
        
        String b="(10)";
        System.out.println(b.substring(1, b.length()-1));
    }
}
