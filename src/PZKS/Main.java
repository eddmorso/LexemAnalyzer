package PZKS;

import javax.swing.*;
import java.awt.*;

public class Main {
    static String inputString = "-2*x*(-3)-(-1.25/2+y)";
/*
    розкрити дужки
            конвеєр з постійним тактсм
*/

    public static void main(String [] args) {
        //Tree tree = new Tree();
        System.out.println(inputString);
        System.out.println();

        TreeDrawer treeDrawer = new TreeDrawer();
        if (treeDrawer.isReady) {

            treeDrawer.printTree();

            JFrame jFrame = new JFrame("Tree");

            treeDrawer.setBackground(Color.WHITE);
            jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jFrame.add(treeDrawer);
            jFrame.setSize(800, 800);
            jFrame.setLocationRelativeTo(null);
            jFrame.setVisible(true);
        }

    }
}
