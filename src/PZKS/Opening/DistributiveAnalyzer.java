package PZKS.Opening;

import java.util.LinkedList;
import java.util.List;

public class DistributiveAnalyzer extends SyntaxAnalyzer {
    @Override
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
            if (right.isNegative()) {
                right.doPositive();
                token1.setType(TypeOfToken.MINUS);
            }
        }
    }

    public void toTmpBinaryTree(Token1 token1) {
        if (token1.getType() == TypeOfToken.MULTIPLY) {
            Token1 left = new Token1(TypeOfToken.MULTIPLY, null);
            Token1 right = new Token1(TypeOfToken.MULTIPLY, null);
            for (Token1 child : token1.getChildren()) {
                if (child.getType() == TypeOfToken.PLUS) {
                    right.getChildren().add(child);
                } else {
                    left.getChildren().add(child);
                }
            }
            if (left.getChildren().size() == 1 && right.getChildren().size() == 1) {
                token1.getChildren().clear();
                token1.getChildren().add(left.getChildren().get(0));
                token1.getChildren().add(right.getChildren().get(0));
            }
            if (left.getChildren().size() > 1 && right.getChildren().size() == 1) {
                token1.getChildren().clear();
                token1.getChildren().add(left);
                token1.getChildren().add(right.getChildren().get(0));
            }
            if (left.getChildren().size() == 1 && right.getChildren().size() > 1) {
                token1.getChildren().clear();
                token1.getChildren().add(left.getChildren().get(0));
                token1.getChildren().add(right);
            }
            if (left.getChildren().size() > 1 && right.getChildren().size() > 1) {
                token1.getChildren().clear();
                token1.getChildren().add(left);
                token1.getChildren().add(right);
            }
        }
        for (Token1 child : token1.getChildren()) {
            toTmpBinaryTree(child);
        }
    }

    public List<Token1> distributive(Token1 token1) {
        List<Token1> result = new LinkedList<Token1>();

        if (token1.getType() == TypeOfToken.DIVIDE) {
            Token1 left = token1.getChildren().get(0);
            if (left.getType() == TypeOfToken.PLUS) {
                Token1 other = new Token1(TypeOfToken.DIVIDE, null);
                other.setChildren(token1.copy().getChildren());
                other.getChildren().remove(left);
                if (other.getChildren().size() == 1) {
                    other = other.getChildren().get(0);
                }
                List<Token1> newchildren = new LinkedList<Token1>();
                for (Token1 child : left.getChildren()) {
                    Token1 divide = new Token1(TypeOfToken.DIVIDE, null);
                    divide.getChildren().add(child.copy());
                    divide.getChildren().add(other.copy());
                    newchildren.add(divide);
                }
                Token1 newtoken = new Token1(TypeOfToken.PLUS, null);
                newtoken.setChildren(newchildren);
                result.add(newtoken);
                return result;
            }
        }
        if (token1.getType() == TypeOfToken.MULTIPLY) {
            Token1 left = token1.getChildren().get(0);
            Token1 right = token1.getChildren().get(1);
            if (left.getType() != TypeOfToken.PLUS && right.getType() == TypeOfToken.PLUS) {
                List<Token1> newchildren = new LinkedList<Token1>();
                for (Token1 child : right.getChildren()) {
                    Token1 multiply = new Token1(TypeOfToken.MULTIPLY, null);
                    multiply.getChildren().add(left.copy());
                    multiply.getChildren().add(child.copy());
                    newchildren.add(multiply);
                }
                Token1 newtoken = new Token1(TypeOfToken.PLUS, null);
                newtoken.setChildren(newchildren);
                result.add(newtoken);
                return result;
            }
            if (left.getType() == TypeOfToken.PLUS) {
                for (Token1 basic : token1.getChildren()) {
                    Token1 other = new Token1(TypeOfToken.MULTIPLY, null);
                    for (Token1 child : token1.getChildren()) {
                        if (!child.equals(basic)) {
                            other.getChildren().add(child);
                        }
                    }
                    if (other.getChildren().size() == 1) {
                        other = other.getChildren().get(0);
                    }
                    List<Token1> newchildren = new LinkedList<Token1>();
                    for (Token1 child : basic.getChildren()) {
                        Token1 multiply = new Token1(TypeOfToken.MULTIPLY, null);
                        multiply.getChildren().add(other.copy());
                        multiply.getChildren().add(child.copy());
                        newchildren.add(multiply);
                    }
                    Token1 newtoken = new Token1(TypeOfToken.PLUS, null);
                    newtoken.setChildren(newchildren);
                    result.add(newtoken);
                }
                return result;
            }
        }
        List<Token1> children = new LinkedList<Token1>(token1.getChildren());
        for (Token1 child : children) {
            List<Token1> tmp = distributive(child);
            if (tmp.size() != 0) {
                for (Token1 tmptoken : tmp) {
                    Token1 newtoken = token1.copy();
                    int index = newtoken.getChildren().indexOf(child);
                    newtoken.getChildren().set(index, tmptoken);
                    result.add(newtoken);
                }
            }
        }
        return result;
    }

    @Override
    public List<Token1> analyze(List<Token1> list) {
        /**
         * To simple tree
         */
        Token1 treeroot = toSimpleTree(list);

        System.out.println(treeroot.toInfixForm());
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
         * Distributive
         */
        List<Token1> distributive = new LinkedList<Token1>();
        List<Token1> newdistributive = new LinkedList<Token1>(distributive);
        newdistributive.add(treeroot);
        boolean flag = true;
        while (flag) {
            flag = false;
            List<Token1> tmp = new LinkedList<Token1>(newdistributive);
            newdistributive.clear();
            for (Token1 token1 : tmp) {
                toTmpBinaryTree(token1);
                List<Token1> tmpdistributive = distributive(token1);
                if (tmpdistributive.size() != 0) {
                    Token1 printtoken = token1.copy();
                    toMultiTree(printtoken);
                    countConstant(printtoken);
                    sortMultiTree(printtoken);
                    toBinaryTree(printtoken);
                    addMinus(printtoken);
                    addUnaryMinus(printtoken);
                    //setLog(printtoken.toInfixForm() + "\n");
                    for (Token1 tmptoken : tmpdistributive) {
                        toMultiTree(tmptoken);
                        countConstant(tmptoken);
                        if (!distributive.contains(tmptoken)) {
                            flag = true;
                            distributive.add(tmptoken);
                            newdistributive.add(tmptoken);
                            Token1 printtmptoken = tmptoken.copy();
                            sortMultiTree(printtmptoken);
                            toBinaryTree(printtmptoken);
                            addMinus(printtmptoken);
                            addUnaryMinus(printtmptoken);
                            setLog(printtmptoken.toInfixForm() + "\n");
                        }

                    }
                }
            }
        }

        /**
         * Sort multi tree
         */
        for (Token1 token1 : distributive) {
            sortMultiTree(token1);
        }

        /**
         * To binary tree
         */
        for (Token1 token1 : distributive) {
            toBinaryTree(token1);
        }

        /**
         * Add minus
         */
        for (Token1 token1 : distributive) {
            addMinus(token1);
        }

        /**
         * Add unary minus
         */
        for (Token1 token1 : distributive) {
            addUnaryMinus(token1);
        }

        return distributive;
    }

}
