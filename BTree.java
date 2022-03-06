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
import java.util.LinkedList;

public class BTree<K extends Comparable<K>> {

    private BTNode<Key<K>> root = null;
    private int order, index, treeSize;
    private final int halfNumber;
    private final BTNode<Key<K>> nullBTNode = new BTNode<Key<K>>();

    /**
     *
     * @param order of B-tree
     */
    public BTree(int order) {
        if (order < 3) {
            try {
                throw new Exception("B-tree's order can not lower than 3");
            } catch (Exception e) {
                
            }
            order = 3;
        }
        this.order = order;
        halfNumber = (order - 1) / 2;
    }

    /**
     * @return true, if tree is empty
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * @return root node
     */
    public BTNode<Key<K>> getRoot() {
        return root;
    }

    /**
     * @return size of nodes in the tree
     */
    public int getTreeSize() {
        return treeSize;
    }

    /**
     * @return height of tree
     */
    public int getHeight() {
        if (isEmpty()) {
            return 0;
        } else {
            return getHeight(root);
        }
    }

    /**
     * @param node , the node
     * @return the height of the node position
     */
    private int getHeight(BTNode<Key<K>> node) {
        int height = 0;
        BTNode<Key<K>> currentNode = node;
        while (!currentNode.equals(nullBTNode)) {
            currentNode = currentNode.getChild(0);
            height++;
        }
        return height;
    }

    /**
     * @param key , the key to find the match pair
     * @return the pair match the key
     */
    public Key<K> get(K key) {
        BTNode<Key<K>> node = getNode(key);
        if (node != nullBTNode) {
            return node.getKey(index);
        } else {
            return null;
        }
    }

    /**
     * @param key , use key to find pair
     * @return the node which contains of the key
     */
    private BTNode<Key<K>> getNode(K key) {
        if (isEmpty()) {
            return nullBTNode;
        }
        BTNode<Key<K>> currentNode = root;
        while (!currentNode.equals(nullBTNode)) {
            int i = 0;
            while (i < currentNode.getKeySize()) {
                if (currentNode.getKey(i).first.equals(key)) {
                    index = i;
                    return currentNode;
                } else if (currentNode.getKey(i).first.compareTo(key) > 0) {
                    currentNode = currentNode.getChild(i);
                    i = 0;
                } else {
                    i++;
                }
            }
            if (!currentNode.isNull()) {
                currentNode = currentNode.getChild(currentNode.getKeySize());
            }
        }
        return nullBTNode;
    }

    /**
     * @param key , the key to find pair
     * @param element , the element to be replaced
     */
    public void replace(K key) {
        Key<K> curKey = new Key<K>(key);
        BTNode<Key<K>> currentNode = root;

        if (get(curKey.first) == null) {
            return;
        }
        while (!currentNode.equals(nullBTNode)) {
            int i = 0;
            while (i < currentNode.getKeySize()) {
                if (currentNode.getKey(i).first.equals(curKey.first)) {
//                    currentNode.getKey(i).second = pair.second;
                    return;
                } else if (currentNode.getKey(i).first.compareTo(curKey.first) > 0) {
                    currentNode = currentNode.getChild(i);
                    i = 0;
                } else {
                    i++;
                }
            }
            if (!currentNode.isNull()) {
                currentNode = currentNode.getChild(currentNode.getKeySize());
            }
        }
    }

    /**
     * @param pair , the pair
     * @param fullNode , full node
     * @return half of the full node after inserting pair inside
     */
    private BTNode<Key<K>> getHalfKeys(Key<K> pair, BTNode<Key<K>> fullNode) {
        int fullNodeSize = fullNode.getKeySize();

        for (int i = 0; i < fullNodeSize; i++) {
            if (fullNode.getKey(i).first.compareTo(pair.first) > 0) {
                fullNode.addKey(i, pair);
                break;
            }
        }
        if (fullNodeSize == fullNode.getKeySize()) {
            fullNode.addKey(fullNodeSize, pair);
        }

        return getHalfKeys(fullNode);
    }

    private BTNode<Key<K>> getHalfKeys(BTNode<Key<K>> fullNode) {
        BTNode<Key<K>> newNode = new BTNode<Key<K>>(order);
        for (int i = 0; i < halfNumber; i++) {
            newNode.addKey(i, fullNode.getKey(0));
            fullNode.removeKey(0);
        }
        return newNode;
    }

    private BTNode<Key<K>> getRestOfHalfKeys(BTNode<Key<K>> halfNode) {   //trả về các key "bên trái"
        BTNode<Key<K>> newNode = new BTNode<Key<K>>(order);
        int halfNodeSize = halfNode.getKeySize();
        for (int i = 0; i < halfNodeSize; i++) {
            if (i != 0) {   //cái này sẽ là không node ở "giữa"
                newNode.addKey(i - 1, halfNode.getKey(1));
                halfNode.removeKey(1);
            }
            newNode.addChild(i, halfNode.getChild(0));
            halfNode.removeChild(0);
        }
        return newNode;
    }

    /**
     * @param childNode , merge childNode with its fatherNode
     * @param index , where to add node
     */
    private void mergeWithFatherNode(BTNode<Key<K>> childNode, int index) {
        childNode.getFather().addKey(index, childNode.getKey(0));
        childNode.getFather().removeChild(index);
        childNode.getFather().addChild(index, childNode.getChild(0));
        childNode.getFather().addChild(index + 1, childNode.getChild(1));
    }

    private void mergeWithFatherNode(BTNode<Key<K>> childNode) {   //merge childNode with its fatherNode
        int fatherNodeSize = childNode.getFather().getKeySize();
        for (int i = 0; i < fatherNodeSize; i++) {
            if (childNode.getFather().getKey(i).compareTo(childNode.getKey(0)) > 0) {
                mergeWithFatherNode(childNode, i);
                break;
            }
        }
        if (fatherNodeSize == childNode.getFather().getKeySize()) {
            mergeWithFatherNode(childNode, fatherNodeSize);
        }
        for (int i = 0; i <= childNode.getFather().getKeySize(); i++) {
            childNode.getFather().getChild(i).setFather(childNode.getFather());
        }
    }

    private void setSplitFatherNode(BTNode<Key<K>> node) { //set father for split node
        for (int i = 0; i <= node.getKeySize(); i++) {
            node.getChild(i).setFather(node);
        }
    }

    /**
     * @param currentNode , process node if the keys size is overflow
     */
    private void processOverflow(BTNode<Key<K>> currentNode) {
        BTNode<Key<K>> newNode = getHalfKeys(currentNode);
        for (int i = 0; i <= newNode.getKeySize(); i++) {
            newNode.addChild(i, currentNode.getChild(0));
            currentNode.removeChild(0);
        }
        BTNode<Key<K>> originalNode = getRestOfHalfKeys(currentNode);
        currentNode.addChild(0, newNode);
        currentNode.addChild(1, originalNode);
        originalNode.setFather(currentNode);
        newNode.setFather(currentNode);
        setSplitFatherNode(originalNode);
        setSplitFatherNode(newNode);
    }

    /**
     * @param key , the key to find a place to insert
     * @param element , the element to be inserted
     */
    public void insert(K key) {
        Key<K> curKey = new Key<K>(key);
        if (isEmpty()) {    //neu chua co phan tu nao
            root = new BTNode<Key<K>>(order);
            root.addKey(0, curKey);
            treeSize++;
            root.setFather(nullBTNode);
            root.addChild(0, nullBTNode);
            root.addChild(1, nullBTNode);
            return;
        }

        BTNode<Key<K>> currentNode = root;

        if (get(curKey.first) != null) {
            replace(key);
            return;
        }

        while (!currentNode.isLeaf()) {
            int i = 0;
            while (i < currentNode.getKeySize()) { //cái này là để di chuyển con trỏ hiện tại đến vị trí cần xét (trừ children cuối)
                if (currentNode.isLeaf()) {
                    i = currentNode.getKeySize();
                } else if (currentNode.getKey(i).first.compareTo(curKey.first) > 0) {
                    currentNode = currentNode.getChild(i);
                    i = 0;
                } else {
                    i++;
                }
            }
            if (!currentNode.isLeaf()) //di chuyển con trỏ theo children cuối
            {
                currentNode = currentNode.getChild(currentNode.getKeySize());
            }
        }

        if (!currentNode.isFull()) {    //if currentNode is not full
            int i = 0;
            while (i < currentNode.getKeySize()) { //key size
                if (currentNode.getKey(i).first.compareTo(curKey.first) > 0) {    //nếu key chuẩn bị add nhỏ hơn key của node đang xét thì thêm key vào trước trong node đó
                    currentNode.addKey(i, curKey);
                    currentNode.addChild(currentNode.getKeySize(), nullBTNode);
                    treeSize++;
                    return;
                } else {
                    i++;
                }
            }
            currentNode.addKey(currentNode.getKeySize(), curKey);
            currentNode.addChild(currentNode.getKeySize(), nullBTNode);
            treeSize++;
        } else {    //nếu node hiện tại full key
            BTNode<Key<K>> leftChildNode = getHalfKeys(curKey, currentNode);  //cắt nửa số key, lấy bên trái, cho vào newChildNode
            for (int i = 0; i < halfNumber; i++) {
                leftChildNode.addChild(i, currentNode.getChild(0));
                currentNode.removeChild(0);
            }
            leftChildNode.addChild(halfNumber, nullBTNode);

            BTNode<Key<K>> rightChildNode = getRestOfHalfKeys(currentNode);
            currentNode.addChild(0, leftChildNode);
            currentNode.addChild(1, rightChildNode);
            rightChildNode.setFather(currentNode);
            leftChildNode.setFather(currentNode);
            treeSize++;

            if (!currentNode.getFather().equals(nullBTNode)) {  //nếu còn node ở trên (nghĩa là node bố khác null)
                while (!currentNode.getFather().isOverflow() && !currentNode.getFather().equals(nullBTNode)) {
                    boolean flag = currentNode.getKeySize() == 1 && !currentNode.getFather().isOverflow();
                    if (currentNode.isOverflow() || flag) {
                        mergeWithFatherNode(currentNode);
                        currentNode = currentNode.getFather();
                        if (currentNode.isOverflow()) {
                            processOverflow(currentNode);
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    /**
     * @param node , the node
     * @return the number of the node's father child index which matches the
     * node
     */
    private int findChild(BTNode<Key<K>> node) {
        if (!node.equals(root)) {
            BTNode<Key<K>> fatherNode = node.getFather();

            for (int i = 0; i <= fatherNode.getKeySize(); i++) {
                if (fatherNode.getChild(i).equals(node)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * @param node , the node's father have different height of right and left
     * subtree balance the unbalanced tree
     */
    private BTNode<Key<K>> balanceDeletedNode(BTNode<Key<K>> node) {
        boolean flag;
        int nodeIndex = findChild(node);
        Key<K> pair;
        BTNode<Key<K>> fatherNode = node.getFather();
        BTNode<Key<K>> currentNode;
        if (nodeIndex == 0) {
            currentNode = fatherNode.getChild(1);
            flag = true;
        } else {
            currentNode = fatherNode.getChild(nodeIndex - 1);
            flag = false;
        }

        int currentSize = currentNode.getKeySize();
        if (currentSize > halfNumber) {
            if (flag) {
                pair = fatherNode.getKey(0);
                node.addKey(node.getKeySize(), pair);
                fatherNode.removeKey(0);
                pair = currentNode.getKey(0);
                currentNode.removeKey(0);
                node.addChild(node.getKeySize(), currentNode.getChild(0));
                currentNode.removeChild(0);
                fatherNode.addKey(0, pair);
                if (node.isLeaf()) {
                    node.removeChild(0);
                }
            } else {
                pair = fatherNode.getKey(nodeIndex - 1);
                node.addKey(0, pair);
                fatherNode.removeKey(nodeIndex - 1);
                pair = currentNode.getKey(currentSize - 1);
                currentNode.removeKey(currentSize - 1);
                node.addChild(0, currentNode.getChild(currentSize));
                currentNode.removeChild(currentSize);
                fatherNode.addKey(nodeIndex - 1, pair);
                if (node.isLeaf()) {
                    node.removeChild(0);
                }
            }
            return node;
        } else {
            if (flag) {
                currentNode.addKey(0, fatherNode.getKey(0));
                fatherNode.removeKey(0);
                fatherNode.removeChild(0);
                if (root.getKeySize() == 0) {
                    root = currentNode;
                    currentNode.setFather(nullBTNode);
                }
                if (node.getKeySize() == 0) {
                    currentNode.addChild(0, node.getChild(0));
                    currentNode.getChild(0).setFather(currentNode);
                }
                for (int i = 0; i < node.getKeySize(); i++) {
                    currentNode.addKey(i, node.getKey(i));
                    currentNode.addChild(i, node.getChild(i));
                    currentNode.getChild(i).setFather(currentNode);
                }
            } else {
                currentNode.addKey(currentNode.getKeySize(), fatherNode.getKey(nodeIndex - 1));
                fatherNode.removeKey(nodeIndex - 1);
                fatherNode.removeChild(nodeIndex);
                if (root.getKeySize() == 0) {
                    root = currentNode;
                    currentNode.setFather(nullBTNode);
                }
                int currentNodeSize = currentNode.getKeySize();
                if (node.getKeySize() == 0) {
                    currentNode.addChild(currentNodeSize, node.getChild(0));
                    currentNode.getChild(currentNodeSize).setFather(currentNode);
                }
                for (int i = 0; i < node.getKeySize(); i++) {
                    currentNode.addKey(currentNodeSize + i, node.getKey(i));
                    currentNode.addChild(currentNodeSize + i, node.getChild(i));
                    currentNode.getChild(currentNodeSize + i).setFather(currentNode);
                }
            }
            return fatherNode;
        }
    }

    /**
     * @param node , use the last internal node to replace the node
     * @return the last internal node
     */
    private BTNode<Key<K>> replaceNode(BTNode<Key<K>> node) {
        BTNode<Key<K>> currentNode = node.getChild(index + 1);
        while (!currentNode.isLeaf()) {
            currentNode = currentNode.getChild(0);
        }
        if (currentNode.getKeySize() - 1 < halfNumber) {
            currentNode = node.getChild(index);
            int currentNodeSize = currentNode.getKeySize();
            while (!currentNode.isLeaf()) {
                currentNode = currentNode.getChild(currentNodeSize);
            }
            node.addKey(index, currentNode.getKey(currentNodeSize - 1));
            currentNode.removeKey(currentNodeSize - 1);
            currentNode.addKey(currentNodeSize - 1, node.getKey(index + 1));
            node.removeKey(index + 1);
            index = currentNode.getKeySize() - 1;
        } else {
            node.addKey(index + 1, currentNode.getKey(0));
            currentNode.removeKey(0);
            currentNode.addKey(0, node.getKey(index));
            node.removeKey(index);
            index = 0;
        }
        return currentNode;
    }

    /**
     * @param key , the key to be deleted
     */
    public void delete(K key) {
        BTNode<Key<K>> node = getNode(key);
        BTNode<Key<K>> deleteNode = null;
        if (node.equals(nullBTNode)) {
            return;
        }

        if (node.equals(root) && node.getKeySize() == 1 && node.isLeaf()) {
            root = null;
            treeSize--;
        } else {
            boolean flag = true;
            boolean isReplaced = false;
            if (!node.isLeaf()) {   
                node = replaceNode(node);
                deleteNode = node;
                isReplaced = true;
            }

            if (node.getKeySize() - 1 < halfNumber) {
                node = balanceDeletedNode(node);
                if (isReplaced) {
                    for (int i = 0; i <= node.getKeySize(); i++) {
                        for (int j = 0; i < node.getChild(i).getKeySize(); j++) {
                            if (node.getChild(i).getKey(j).first.equals(key)) {
                                deleteNode = node.getChild(i);
                                break;
                            }
                        }
                    }
                }
            } else if (node.isLeaf()) {
                node.removeChild(0);
            }

            while (!node.getChild(0).equals(root) && node.getKeySize() < halfNumber && flag) {
                if (node.equals(root)) {
                    for (int i = 0; i <= root.getKeySize(); i++) {
                        if (root.getChild(i).getKeySize() == 0) {
                            flag = true;
                            break;
                        } else {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    node = balanceDeletedNode(node);
                }
            }

            if (deleteNode == null) {
                node = getNode(key);
            } else {
                node = deleteNode;
            }

            if (!node.equals(nullBTNode)) {
                for (int i = 0; i < node.getKeySize(); i++) {
                    if (node.getKey(i).first == key) {
                        node.removeKey(i);
                    }
                }
                treeSize--;
            }
        }
    }

    public String toString() {  //in thông tin
        if (isEmpty()) {
            return "NullNode";
        }
        StringBuilder sb = new StringBuilder();
        int height = getHeight();

        LinkedList<BTNode<Key<K>>> queue = new LinkedList<BTNode<Key<K>>>();
        queue.push(root);

        BTNode<Key<K>> temp;
        while ((temp = queue.poll()) != null) {
            for (int i = 0; i <= temp.getKeySize(); i++) {
                if (!temp.getChild(i).isNull()) {
                    queue.offer(temp.getChild(i));
                }
            }
            sb.append("[Level: ").append(height - getHeight(temp)).append("] ");
            sb.append(temp.toString()).append("\n");
        }
        return sb.toString();
    }
}
