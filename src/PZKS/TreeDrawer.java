package PZKS;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

class TreeDrawer extends JPanel {

    private Tree tree = new Tree();

    private List<Node> nodes = tree.nodes;

    boolean isReady = !nodes.isEmpty();

    TreeDrawer(){
        Collections.sort(nodes);
    }

    @Override
    public void paintComponent(Graphics g) {

        final int STEP_UP = 20, STEP_SIDE = 80;

        g.setColor(Color.BLACK);
        int x = 400;
        int y = 50;

        g.drawString(nodes.get(0).getTokenSet().get(0).getValue(), x, y);
        nodes.get(0).setX(x);
        nodes.get(0).setY(y);

        y += STEP_UP;
        x -= STEP_SIDE;

        g.drawString(nodes.get(0).getLeftChild().getTokenSet().get(0).getValue(), x, y);
        nodes.get(0).getLeftChild().setX(x);
        nodes.get(0).getLeftChild().setY(y);
        g.drawLine(nodes.get(0).getX() - 10, nodes.get(0).getY(), nodes.get(0).getLeftChild().getX() + 10, nodes.get(0).getLeftChild().getY() - 10);

        x += STEP_SIDE + STEP_SIDE;

        g.drawString(nodes.get(0).getRightChild().getTokenSet().get(0).getValue(), x, y);
        nodes.get(0).getRightChild().setX(x);
        nodes.get(0).getRightChild().setY(y);
        g.drawLine(nodes.get(0).getX() + 10, nodes.get(0).getY(), nodes.get(0).getRightChild().getX() - 10, nodes.get(0).getRightChild().getY() - 10);

        for(int i = 1; i < nodes.size(); i++) {

            x = nodes.get(i).getX();
            //y = nodes.get(i).getY();
            y += STEP_UP;
            if(nodes.get(i).getRightChild() != null) {
                g.drawString(nodes.get(i).getRightChild().getTokenSet().get(0).getValue(), x + STEP_SIDE, y);
                nodes.get(i).getRightChild().setX(x + STEP_SIDE);
                nodes.get(i).getRightChild().setY(y + STEP_UP);
                g.drawLine(nodes.get(i).getX() + 10, nodes.get(i).getY(), nodes.get(i).getRightChild().getX() - 10, nodes.get(i).getRightChild().getY() - 10);
            }

            if(nodes.get(i).getLeftChild() != null) {
                g.drawString(nodes.get(i).getLeftChild().getTokenSet().get(0).getValue(), x - STEP_SIDE, y);
                nodes.get(i).getLeftChild().setX(x - STEP_SIDE);
                nodes.get(i).getLeftChild().setY(y + STEP_UP);
                g.drawLine(nodes.get(i).getX() - 10, nodes.get(i).getY(), nodes.get(i).getLeftChild().getX() + 10, nodes.get(i).getLeftChild().getY() - 10);
            }
        }
    }

    void printTree(){

        List<Node> nodes = tree.nodes;

        for(int i = 0; i < nodes.size(); i++){
            try {
                System.out.println("Node: " + nodes.get(i).getName() + " parent: " + nodes.get(i).getParent().getName());
                System.out.println(nodes.get(i).getTokenSet().get(0).getValue());
            }catch(NullPointerException e){
                System.out.println("Node: " + nodes.get(i).getName() + " parent: null");
                System.out.println(nodes.get(i).getTokenSet().get(0).getValue());
            }
        }
    }
}
