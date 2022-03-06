package BTree;

/**
 * DataStructure
 * Created by Blaise Wang on 16/6/13.
 */
interface Tree<K extends Comparable<K>, E> {
    boolean isEmpty();

    BTNode<Pair<K, E>> getRoot();

    int getTreeSize();

    int getHeight();

    String toString();
}
