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
public class Key<A extends Comparable<A>> implements Comparable<Key<A>> {

    A first;

    Key(A a) {
        first = a;
    }

    public String toString() {
        if (first == null) {
            return "(null)";
        }
        return "(" + first.toString() + ")";
    }

    @Override
    public int compareTo(Key<A> o) {
        return first.compareTo(o.first);
    }
}
