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
import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

class MyCanvas extends Canvas {

    private BTree<Integer> bTree;
    private int width, height;
    private int fontSize = 12;
    private int rectangleWidth = 45;

    MyCanvas(int width, int height, BTree<Integer> bTree) {
        setBackground(Color.white);
        this.width = width;
        this.height = height;
        setSize(width, height);
        this.bTree = bTree;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.setFont(new Font("SimHei", Font.BOLD, 16));
        g.drawString("Size : " + bTree.getTreeSize(), 50, 50);
        g.drawString("Height : " + bTree.getHeight(), 50, 70);
        DrawBTree(g);
    }

    void updateCanvas(BTree<Integer> bTree) {   //cập nhật lại giao diện
        this.bTree = bTree;
        this.repaint();
    }

    private void DrawNode(Graphics g, String s, int x, int y) {
        String firstString = s.substring(1, s.length() - 1);
        g.setFont(new Font("Arial", Font.BOLD, fontSize));
        // hiện số
        g.drawString(firstString, x + 12, y + 15);
        // hiện ô
        g.drawRect(x, y, rectangleWidth, 3 * fontSize);
    }

    private void DrawBTree(Graphics g) {
        BTNode<Key<Integer>> root = bTree.getRoot();
//        Graphics2D g2d = (Graphics2D) g;
//        //làm đậm nét vẽ các đường thẳng và ô
//        g2d.setStroke(new BasicStroke(1.5f));
//        // khử răng cưa
//        g2d.setRenderingHint(
//                RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setRenderingHint(
//                RenderingHints.KEY_TEXT_ANTIALIASING,
//                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (root != null) {
            int lastSize = 0, keySize = 0;

            LinkedList<BTNode<Key<Integer>>> queue = new LinkedList<BTNode<Key<Integer>>>();
            LinkedList<BTNode<Key<Integer>>> treeNodes = new LinkedList<BTNode<Key<Integer>>>();
            LinkedList<Integer> nodeSize = new LinkedList<Integer>();
            LinkedList<Integer> lastNodeSize = new LinkedList<Integer>();
            LinkedList<Integer> tempLastSize = new LinkedList<Integer>();
            LinkedList<Integer> lastX = new LinkedList<Integer>();
            LinkedList<Integer> tempLastX = new LinkedList<Integer>();

            
            queue.add(root);
            BTNode<Key<Integer>> currentNode;
            while ((currentNode = queue.poll()) != null) {  //lấy thằng head và xoá head
                treeNodes.add(currentNode);
                if (currentNode.isLeaf()) {
                    lastSize++;
                    keySize += currentNode.getKeySize();    //lấy số key hiện tại của node đó
                }
                nodeSize.push(currentNode.getKeySize() + 1);
                for (int i = 0; i <= currentNode.getKeySize(); i++) {
                    if (!currentNode.getChild(i).isNull()) {
                        queue.add(currentNode.getChild(i));
                    }
                }
            }

            int blockSpace = 90;    // khoảng cách các hàng
            int treeNodeSize = treeNodes.size();
            int x = (width - (keySize * rectangleWidth + (lastSize - 1) * 20)) / 2;
            int y = (height + ((bTree.getHeight() - 2) * blockSpace)) / 2;

            for (int i = 0; i < lastSize; i++) {
                int temp = nodeSize.poll(); //lấy thằng head và xoá head
                lastNodeSize.add(temp);   //add vào cuối list
                tempLastSize.add(temp);
            }

            for (int i = treeNodeSize - lastSize; i < treeNodes.size(); i++) {
                BTNode<Key<Integer>> node = treeNodes.get(i);
                if (node.isLeaf()) {    //check lá
                    for (int j = 0; j < node.getKeySize(); j++) {
                        String string = node.getKey(j).toString();
                        DrawNode(g, string, x, y);  //vẽ các node
                        lastX.push(x);
                        x += rectangleWidth;
                    }
                    x += 20;
                }
            }

            int m = 1;
            while (nodeSize.size() != 0) {
                int l = 0, n = 0;
                y -= blockSpace;    //vẽ lên trên
                tempLastX.clear();
                LinkedList<Integer> nodeX = new LinkedList<>();

                if (nodeSize.size() == 1) {
                    n = 1;
                } else {
                    for (int i = 0; i < nodeSize.size(); i++) {
                        if (n != lastNodeSize.size()) {
                            n += nodeSize.get(i);
                        } else {
                            n = i;
                            break;
                        }
                    }
                }

                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < nodeSize.get(i); j++) {
                        for (int k = 0; k < lastNodeSize.get(l) - 1; k++) {
                            nodeX.push(lastX.pollFirst());
                        }
                        l++;
                    }
                    for (int j = nodeX.size() - 1; j > 0; j--) {
                        if (nodeX.get(j) - nodeX.get(j - 1) == rectangleWidth) {
                            nodeX.remove(j);
                        }
                    }
                    BTNode<Key<Integer>> node = treeNodes.get(treeNodeSize - lastSize - m);
                    int size = node.getKeySize();
                    int halfSize = (size + 1) / 2;  //vẽ mid line
                    if ((size + 1) % 2 == 0) {
                        int tempX1 = (tempLastSize.get(node.getKeySize() - halfSize) - 1) * rectangleWidth;
                        int tempX2 = (tempLastSize.get(node.getKeySize() - halfSize + 1) - 1) * rectangleWidth;
                        tempX1 = (tempX1 / 2) + nodeX.get(halfSize);
                        tempX2 = (tempX2 / 2) + nodeX.get(halfSize - 1);
                        x = ((tempX1 - tempX2) / 2 + tempX2) - ((nodeSize.get(i) - 1) * rectangleWidth / 2);
                    } else {
                        int tempX1 = (tempLastSize.get(node.getKeySize() - halfSize) - 1) * rectangleWidth;
                        tempX1 = (tempX1 / 2) + nodeX.get(halfSize);
                        x = tempX1 - ((nodeSize.get(i) - 1) * rectangleWidth / 2);
                    }
                    for (int j = 0; j <= node.getKeySize(); j++) {
                        int tempXi = (tempLastSize.get(node.getKeySize() - j) - 1) * rectangleWidth;
                        tempLastSize.remove((node.getKeySize() - j));
                        g.drawLine(x, y + 3 * fontSize, nodeX.get(j) + (tempXi / 2), y + blockSpace);   //(x1,y1)->(x2,y2)
                        if (j != node.getKeySize()) {
                            String string = node.getKey(j).toString();
                            DrawNode(g, string, x, y);
                            tempLastX.push(x);
                            x += rectangleWidth;
                        }
                    }
                    m++;
                }

                lastNodeSize.clear();
                for (int i = 0; i < n; i++) {
                    int temp = nodeSize.pollFirst();    //lấy và xoá đi thằng đầu tiên của list
                    lastNodeSize.add(temp);
                    tempLastSize.add(temp);
                }
                Collections.sort(tempLastX);
                for (int i = 0; i < tempLastX.size(); i++) {
                    int temp = tempLastX.pollLast();    //lấy và xoá thằng cuối cùng của list
                    tempLastX.add(i, temp);// và thêm vào cuối
                    lastX.add(i, temp);
                }
            }
        }
    }
}
