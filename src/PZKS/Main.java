package PZKS;

import javax.swing.*;
import java.awt.*;

public class Main {
    static String inputString = "a-(b+c)";
/*
    розкрити дужки
            конвеєр з постійним тактом
*/

    public static void main(String [] args) {
        System.out.println(inputString);
        System.out.println();

        Opener opener = new Opener(inputString);

//        TreeDrawer treeDrawer = new TreeDrawer();
//        if (treeDrawer.isReady) {
//
//            treeDrawer.printTree();
//
//            JFrame jFrame = new JFrame("Tree");
//
//            treeDrawer.setBackground(Color.WHITE);
//            jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//            jFrame.add(treeDrawer);
//            jFrame.setSize(800, 800);
//            jFrame.setLocationRelativeTo(null);
//            jFrame.setVisible(true);
//        }
    }
}
