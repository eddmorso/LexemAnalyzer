package PZKS;

import java.util.*;

class Tree {

private List<Token> tokens = new ArrayList<>();
List<Node> nodes = new ArrayList<>();
private int nodeName = 0;

Tree() {

    Parser parser = new Parser(Main.inputString);
    if(parser.check()) {
        tokens = parser.makeLexem();
        while (!isOneLeft(tokens)) {

            if(isLoneVarInParenthesis(tokens)) removeLoneVarInParenthesis(tokens);

            if (isParenthesisPresent(tokens)) {
                buildTripletsAmongParenthesis();
            } else buildTriplets(tokens);
        }
    }
}

private void buildTripletsAmongParenthesis(){

    for (int i = 0; i < tokens.size(); i++) {
        int begin, end;
        if(tokens.get(i).getType() == Type.CloseParenthesis){
            end = i;
//            int counter = 0;
//            int priority;
            while (tokens.get(i).getType() != Type.OpenParenthesis){
                if(tokens.get(i).getPriority() == Parser.LOW_PRIORITY){
                    //counter++;
                }
                i--;
            }

            begin = i;

            List<Token> subTokens = new ArrayList<>(tokens.subList(begin + 1, end));
            tokens.subList(begin + 1, end).clear();
            tokens.addAll(begin + 1, buildTriplets(subTokens));
//            priority = counter == 0 ? Parser.HIGH_PRIORITY : Parser.LOW_PRIORITY;
//
//            int breaker = 0;
//            while (breaker == 0){
//                if(tokens.get(i).getPriority() == priority){
//                    int parentName = makeThreeNodes(tokens.get(i), tokens.get(i - 1), tokens.get(i + 1));
//                    replaceWithConst(i - 1, i + 1, parentName);
//                    breaker = 1;
//                }
//                i++;
//            }
            i = tokens.size();
        }
    }
}

private List<Token> buildTriplets(List<Token> tokens){
    int counter = 0;
    int priority;

    for(int i = 0; i < tokens.size(); i++){
        if(tokens.get(i).getPriority() == Parser.LOW_PRIORITY){
            counter++;
        }
    }
    priority = counter == 0 ? Parser.HIGH_PRIORITY : Parser.LOW_PRIORITY;

    for(int i = 0; i < tokens.size(); i++){
        if(tokens.get(i).getPriority() == priority){
            int parentName = makeThreeNodes(tokens.get(i), tokens.get(i - 1), tokens.get(i + 1));
            replaceWithConst(i - 1, i + 1, parentName, tokens);
            //i = tokens.size();
        }
    }

    return tokens;
}

private Node findNode(int name){
    Node node = null;
    for(int i = 0; i < nodes.size(); i++) {
        if (nodes.get(i).getName() == name) {
            node = nodes.get(i);
            i = nodes.size();
        }
    }
    return node;
}

private int makeThreeNodes(Token parent, Token leftChild, Token rightChild) {

    Node nodeLeft = null;
    Node nodeRight = null;
    Node nodeParent = null;

    if(leftChild.getType() == Type.Constant && rightChild.getType() == Type.Constant){

        List<Token> parentSet = new ArrayList<>();
        parentSet.add(parent);

        nodeLeft = findNode(leftChild.getSubValue());
        nodeLeft.setName(nodeName);
        nodeName++;

        nodeRight = findNode(rightChild.getSubValue());
        nodeRight.setName(nodeName);
        nodeName++;

        nodeParent = new Node(nodeRight, nodeLeft, parentSet);

        nodeParent.setName(nodeName);
        nodes.add(nodeParent);
        nodeName++;

        nodeLeft.setParent(nodeParent);

        nodeRight.setParent(nodeParent);

    }else if (leftChild.getType() == Type.Constant) {

        List<Token> rightSet = new ArrayList<>();
        rightSet.add(rightChild);

        List<Token> parentSet = new ArrayList<>();
        parentSet.add(parent);

        nodeLeft = findNode(leftChild.getSubValue());
        nodeLeft.setName(nodeName);
        nodeName++;

        nodeRight = new Node(null, null, rightSet);
        nodeRight.setName(nodeName);
        nodeName++;

        nodeParent = new Node(nodeRight, nodeLeft, parentSet);

        nodeParent.setName(nodeName);
        nodes.add(nodeParent);
        nodeName++;

        nodeLeft.setParent(nodeParent);

        nodeRight.setParent(nodeParent);
        nodes.add(nodeRight);

    } else if (rightChild.getType() == Type.Constant) {
        List<Token> leftSet = new ArrayList<>();
        leftSet.add(leftChild);

        List<Token> parentSet = new ArrayList<>();
        parentSet.add(parent);

        nodeLeft = new Node(null, null, leftSet);
        nodeLeft.setName(nodeName);
        nodeName++;

        nodeRight = findNode(rightChild.getSubValue());
        nodeRight.setName(nodeName);
        nodeName++;

        nodeParent = new Node(nodeRight, nodeLeft, parentSet);

        nodeParent.setName(nodeName);
        nodes.add(nodeParent);
        nodeName++;

        nodes.add(nodeLeft);
        nodeLeft.setParent(nodeParent);

        nodeRight.setParent(nodeParent);

    } else if (rightChild.getType() != Type.Constant && rightChild.getType() != Type.Constant) {

        List<Token> leftSet = new ArrayList<>();
        leftSet.add(leftChild);

        List<Token> rightSet = new ArrayList<>();
        rightSet.add(rightChild);

        List<Token> parentSet = new ArrayList<>();
        parentSet.add(parent);

        nodeLeft = new Node(null, null, leftSet);
        nodeLeft.setName(nodeName);
        nodeName++;

        nodeRight = new Node(null, null, rightSet);
        nodeRight.setName(nodeName);
        nodeName++;

        nodeParent = new Node(nodeRight, nodeLeft, parentSet);

        nodeParent.setName(nodeName);
        nodes.add(nodeParent);
        nodeName++;

        nodes.add(nodeLeft);
        nodeLeft.setParent(nodeParent);

        nodes.add(nodeRight);
        nodeRight.setParent(nodeParent);

    }
    return nodeParent.getName();
}

private List<Token> replaceWithConst(int begin, int end, int parentName, List<Token> tokens){

    tokens.subList(begin, end + 1).clear();
    tokens.add(begin, new Token(Type.Constant, "C", Parser.STANDARD_PRIORITY));
    tokens.get(begin).setSubValue(parentName);

    return tokens;
}

private boolean isParenthesisPresent(List<Token> tokens){

    StringBuilder stringBuilder = new StringBuilder();

    for(Token token : tokens){
        stringBuilder.append(token.getValue());
    }

    return stringBuilder.toString().contains(")");
}

private boolean isOneLeft(List<Token> tokens){
    return tokens.size() == 1;
}

private boolean isLoneVarInParenthesis(List<Token> tokens){
    for (int i = 1; i < tokens.size() - 1; i++) {
        if (tokens.get(i - 1).getValue().equals("(") && tokens.get(i + 1).getValue().equals(")")) {
            return true;
        }
    }
    return false;
}

private void removeLoneVarInParenthesis(List<Token> tokens){
    while(isLoneVarInParenthesis(tokens)) {
        for (int i = 1; i < tokens.size() - 1; i++) {
            if (tokens.get(i - 1).getValue().equals("(") && tokens.get(i + 1).getValue().equals(")")) {
                tokens.remove(i - 1);
                tokens.remove(i);
                i = tokens.size();
            }
        }
    }
    this.tokens = tokens;
}

}