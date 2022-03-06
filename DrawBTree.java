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
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class DrawBTree extends JFrame {

    private int key;
    private MyCanvas canvas;
    private JTextField keyText = new JTextField(10);

    private int index = 0;
    private LinkedList<BTree<Integer>> bTreeLinkedList = new LinkedList<BTree<Integer>>();

    private BTree<Integer> bTree;

    public DrawBTree(BTree<Integer> tree) {
        super("B-tree GUI");
        bTree = tree;
        final int windowHeight = 700;
        final int windowWidth = 700;
        canvas = new MyCanvas(windowWidth, windowHeight, bTree);

        JButton insertButton = new JButton("Insert");
        JButton deleteButton = new JButton("Delete");
        JLabel keyPrompt = new JLabel("key: ");

        JPanel contentPanel = new JPanel();
        JPanel controlPanel = new JPanel();
        JPanel menuPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        controlPanel.setLayout(new BorderLayout());
        menuPanel.setLayout(new FlowLayout());
        menuPanel.add(keyPrompt);
        menuPanel.add(keyText);
        menuPanel.add(insertButton);
        menuPanel.add(deleteButton);
        controlPanel.add(menuPanel, BorderLayout.CENTER);
        contentPanel.add(controlPanel, BorderLayout.NORTH);
        contentPanel.add(canvas);   //hiển thị
        setContentPane(contentPanel);

//        insertButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                insertValue();
//            }
//        });
        insertButton.addActionListener(e -> insertValue());
        deleteButton.addActionListener(e -> deleteValue());

        setSize(windowWidth, windowHeight);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

//    private void checkValid() {
//        if (index > 0 && index < bTreeLinkedList.size() - 1) {
//            previousButton.setEnabled(true);
//            nextButton.setEnabled(true);
//        } else if (index > 0 && index == bTreeLinkedList.size() - 1) {
//            previousButton.setEnabled(true);
//            nextButton.setEnabled(false);
//        } else if (index == 0 && index < bTreeLinkedList.size() - 1) {
//            previousButton.setEnabled(false);
//            nextButton.setEnabled(true);
//        } else {
//            previousButton.setEnabled(false);
//            nextButton.setEnabled(false);
//        }
//    }
    private void deleteList() {
        for (int i = bTreeLinkedList.size() - 1; i >= index; i--) {
            bTreeLinkedList.removeLast();
        }
    }

    private void insertValue() {
        try {
            key = Integer.parseInt(keyText.getText());
            keyText.setText("");
            if (index < bTreeLinkedList.size() - 1) {
                deleteList();
            }
            bTree.insert(key);
            index = bTreeLinkedList.size() - 1;
            canvas.updateCanvas(bTree);
        } catch (NumberFormatException e) {
            System.out.println("Int only!");
        }
    }

    private void deleteValue() {
        try {
            key = Integer.parseInt(keyText.getText());
            keyText.setText("");
            if (index < bTreeLinkedList.size() - 1) {
                deleteList();
            }
            bTree.delete(key);
            index = bTreeLinkedList.size() - 1;
            canvas.updateCanvas(bTree);
        } catch (NumberFormatException e) {
            System.out.println("Int only!");
        }
    }

}
