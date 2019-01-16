package PZKS.Opening;

import java.util.LinkedList;
import java.util.List;

public class SyntaxAnalyzer {
    private String log = "";

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Token1 toSimpleTree(List<Token1> list) {
        List<Token1> tmp = new LinkedList<Token1>(list);
        Token1 token1 = tmp.remove(0).copy();
        return toSimpleTree(tmp, token1);
    }

    private Token1 toSimpleTree(List<Token1> list, Token1 token1) {
        if (token1.getType() != TypeOfToken.VARIABLE && token1.getType() != TypeOfToken.CONSTANT) {
            Token1 right = list.remove(0).copy();
            toSimpleTree(list, right);
            Token1 left = list.remove(0).copy();
            toSimpleTree(list, left);
            token1.getChildren().add(left);
            token1.getChildren().add(right);
        }
        return token1;
    }

    public void toMultiTree(Token1 token1) {
        if (token1.getType().priority() > 0) {
            List<Token1> tmp = new LinkedList<Token1>();
            for (Token1 child : token1.getChildren()) {
                toMultiTree(child);
                if (child.getType() == token1.getType()) {
                    tmp.addAll(child.getChildren());
                } else {
                    tmp.add(child);
                }
            }
            token1.setChildren(tmp);
        }
    }

    public void sortMultiTree(Token1 token1) {
        if (token1.getType() == TypeOfToken.PLUS || token1.getType() == TypeOfToken.MULTIPLY) {
            List<Token1> newchildren = new LinkedList<Token1>();
            List<Token1> tmpchildren = new LinkedList<Token1>();
            for (Token1 tmptoken : token1.getChildren()) {
                if (tmptoken.getType() == TypeOfToken.CONSTANT) {
                    newchildren.add(0, tmptoken);
                } else {
                    if (tmptoken.getType() == TypeOfToken.VARIABLE) {
                        newchildren.add(tmptoken);
                    } else {
                        tmpchildren.add(tmptoken);
                    }
                }
            }
            int size = tmpchildren.size();
            for (int i = 0; i < size; i++) {
                int min = Integer.MAX_VALUE;
                Token1 mintoken = null;
                for (Token1 tmptoken : tmpchildren) {
                    if (tmptoken.getWeight() < min) {
                        min = tmptoken.getWeight();
                        mintoken = tmptoken;
                    }
                }
                tmpchildren.remove(mintoken);
                newchildren.add(mintoken);
            }
            token1.setChildren(newchildren);
        }
        for (Token1 child : token1.getChildren()) {
            sortMultiTree(child);
        }
    }

    public void toBinaryTree(Token1 token1) {
        if (token1.getType().priority() > 0 && token1.getChildren().size() > 2) {
            List<Token1> left = new LinkedList<Token1>();
            List<Token1> right = new LinkedList<Token1>();

            List<Token1> children = token1.getChildren();
            int allweight = 0;
            for (Token1 child : children) {
                allweight += child.getWeight();
            }
            allweight += (children.size() - 1) * token1.getType().weight();
            double halfpart = allweight / 2.0;
            int leftweight = 0;
            for (Token1 child : children) {
                int tmpleftweight = leftweight + child.getWeight() + token1.getType().weight() * left.size();
                double leftdelta = Math.abs(halfpart - leftweight);
                double rightdelta = Math.abs(halfpart - tmpleftweight);
                if (leftdelta >= rightdelta) {
                    leftweight = tmpleftweight;
                    left.add(child);
                } else {
                    if (left.size() < 2) {
                        left.add(child);
                    } else {
                        right.add(child);
                    }
                }
            }

            List<Token1> newchildren = new LinkedList<Token1>();
            Token1 lefttoken = new Token1(token1.getType(), null);
            lefttoken.setChildren(left);
            newchildren.add(lefttoken);
            if (right.size() == 1) {
                newchildren.add(right.get(0));
            } else {
                Token1 righttoken = new Token1(token1.getType(), null);
                righttoken.setChildren(right);
                if (token1.getType() == TypeOfToken.MINUS) {
                    righttoken.setType(TypeOfToken.PLUS);
                }
                if (token1.getType() == TypeOfToken.DIVIDE) {
                    righttoken.setType(TypeOfToken.MULTIPLY);
                }
                newchildren.add(righttoken);
            }
            token1.setChildren(newchildren);
        }
        for (Token1 child : token1.getChildren()) {
            toBinaryTree(child);
        }
    }

    public void addMinus(Token1 token1) {
        for (Token1 child : token1.getChildren()) {
            addMinus(child);
        }
        if (token1.getType() == TypeOfToken.MULTIPLY) {
            Token1 left = token1.getChildren().get(0);
            Token1 right = token1.getChildren().get(1);
            if (left.isNegative() && right.isNegative()) {
                left.doPositive();
                right.doPositive();
            }
        }
        if (token1.getType() == TypeOfToken.PLUS) {
            Token1 left = token1.getChildren().get(0);
            Token1 right = token1.getChildren().get(1);
            if (left.isNegative() && !right.isNegative()) {
                List<Token1> newchildren = new LinkedList<Token1>();
                newchildren.add(right);
                left.doPositive();
                newchildren.add(left);
                token1.setType(TypeOfToken.MINUS);
                token1.setChildren(newchildren);
            }
            if (!left.isNegative() && right.isNegative()) {
                right.doPositive();
                token1.setType(TypeOfToken.MINUS);
            }
//            if (left.isNegative() && right.isNegative()) {
//                List<Token1> children = token1.getChildren();
//                left.doPositive();
//                right.doPositive();
//                Token1 plus = new Token1(TypeOfToken.PLUS, null);
//                plus.setChildren(children);
//                Token1 one = new Token1(TypeOfToken.CONSTANT, "-1");
//                List<Token1> newchildren = new LinkedList<Token1>();
//                newchildren.add(one);
//                newchildren.add(plus);
//                token1.setType(TypeOfToken.MULTIPLY);
//                token1.setChildren(newchildren);
//            }
        }
    }

    public void addUnaryMinus(Token1 token1) {
        if (token1.getType() == TypeOfToken.MULTIPLY && token1.getChildren().get(0).getType() == TypeOfToken.CONSTANT && token1.getChildren().get(0).getContext().equals("-1")) {
            token1.getChildren().remove(0);
            token1.setType(TypeOfToken.UNMINUS);
        }
        for (Token1 child : token1.getChildren()) {
            addUnaryMinus(child);
        }
    }

    public void countConstant(Token1 root) {
        for (Token1 child : root.getChildren()) {
            countConstant(child);
        }
        if (root.getType() == TypeOfToken.PLUS) {
            double constant = 0;
            List<Token1> newchildren = new LinkedList<Token1>();
            for (Token1 child : root.getChildren()) {
                if (child.getType() == TypeOfToken.CONSTANT) {
                    double tmp = Double.parseDouble(child.getContext());
                    constant += tmp;
                } else {
                    newchildren.add(child);
                }
            }
            if (constant != 0) {
                Token1 newtoken = null;
                if ((constant - (int) constant) == 0) {
                    newtoken = new Token1(TypeOfToken.CONSTANT, Integer.toString((int) constant));
                } else {
                    newtoken = new Token1(TypeOfToken.CONSTANT, Double.toString(constant));
                }
                newchildren.add(newtoken);
            } else {
                if (newchildren.size() == 0) {
                    Token1 newtoken = new Token1(TypeOfToken.CONSTANT, "0");
                    newchildren.add(newtoken);
                }
            }
            root.setChildren(newchildren);
        }
        if (root.getType() == TypeOfToken.MULTIPLY) {
            double constant = 1;
            List<Token1> newchildren = new LinkedList<Token1>();
            for (Token1 child : root.getChildren()) {
                if (child.getType() == TypeOfToken.CONSTANT) {
                    double tmp = Double.parseDouble(child.getContext());
                    constant *= tmp;
                } else {
                    newchildren.add(child);
                }
            }
            if (constant != 0) {
                if (constant != 1) {
                    Token1 newtoken = null;
                    if ((constant - (int) constant) == 0) {
                        newtoken = new Token1(TypeOfToken.CONSTANT, Integer.toString((int) constant));
                    } else {
                        newtoken = new Token1(TypeOfToken.CONSTANT, Double.toString(constant));
                    }
                    newchildren.add(newtoken);
                } else {
                    if (newchildren.size() == 0) {
                        Token1 newtoken = new Token1(TypeOfToken.CONSTANT, "1");
                        newchildren.add(newtoken);
                    }
                }
            } else {
                Token1 newtoken = new Token1(TypeOfToken.CONSTANT, "0");
                newchildren.clear();
                newchildren.add(newtoken);
            }
            root.setChildren(newchildren);
        }
        if (root.getType() == TypeOfToken.DIVIDE) {
            Token1 left = root.getChildren().get(0);
            Token1 right = root.getChildren().get(1);
            while (left.getType() == TypeOfToken.CONSTANT && right != null && right.getType() == TypeOfToken.CONSTANT) {
                double tmp = Double.parseDouble(left.getContext());
                tmp /= Double.parseDouble(right.getContext());
                if ((tmp - (int) tmp) == 0) {
                    left.setContext(Integer.toString((int) tmp));
                } else {
                    left.setContext(Double.toString(tmp));
                }
                root.getChildren().remove(right);
                if (root.getChildren().size() > 1) {
                    right = root.getChildren().get(1);
                } else {
                    right = null;
                }
            }
        }
        if (root.getChildren().size() == 1) {
            root.setType(root.getChildren().get(0).getType());
            root.setContext(root.getChildren().get(0).getContext());
            root.setChildren(root.getChildren().get(0).getChildren());
        }
    }

    public List<Token1> analyze(List<Token1> list) {
        List<Token1> result = new LinkedList<Token1>();
        /**
         * To simple tree
         */
        Token1 treeroot = toSimpleTree(list);
        /**
         * Count constant
         */
        countConstant(treeroot);

        /**
         * To multi tree
         */
        toMultiTree(treeroot);

        /**
         * Count constant
         */
        countConstant(treeroot);

        /**
         * Sort multi tree
         */
        sortMultiTree(treeroot);

        /**
         * To binary tree
         */
        toBinaryTree(treeroot);

        /**
         * Add minus
         */
        //addMinus(treeroot);

        /**
         * Add unary minus
         */
        addUnaryMinus(treeroot);

        result.add(treeroot);
        return result;
    }
}
