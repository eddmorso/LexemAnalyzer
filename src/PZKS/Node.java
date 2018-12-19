package PZKS;

import java.util.List;
import java.util.Stack;

class Node implements Comparable<Node>{

    private Node rightChild;
    private Node leftChild;
    private List<Token> tokenSet;
    private int parentLayer;
    private int layer;
    private int name;
    private Node parent;
    private int x,y;

    Node(Node rightChild, Node leftChild, List<Token> tokenSet){
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.tokenSet = tokenSet;
    }

    void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    void setTokenSet(List<Token> tokenSet) {
        this.tokenSet = tokenSet;
    }

    void setLayer(int layer) {
        this.layer = layer;
    }

    int getLayer() {
        return layer;
    }

    Node getLeftChild() {
        return leftChild;
    }

    Node getRightChild() {
        return rightChild;
    }

    List<Token> getTokenSet() {
        return tokenSet;
    }

    void setParentLayer(int parentLayer) {
        this.parentLayer = parentLayer;
    }

    int getParentLayer() {
        return parentLayer;
    }

    void setName(int name) {
        this.name = name;
    }

    int getName() {
        return name;
    }

    void setParent(Node parent) {
        this.parent = parent;
    }

    Node getParent() {
        return parent;
    }

    void setX(int x) {
        this.x = x;
    }

    int getX() {
        return x;
    }

    void setY(int y) {
        this.y = y;
    }

    int getY() {
        return y;
    }

    @Override
    public int compareTo(Node node) {
        try {
            if (parent.getName() == node.getParent().getName()) {
                return 0;
            } else if (parent.getName() < node.getParent().getName()) {
                return 1;
            } else return -1;
        }catch (NullPointerException e){
            return -1;
        }
    }
}
