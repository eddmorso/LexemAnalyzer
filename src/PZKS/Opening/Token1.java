package PZKS.Opening;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Token1 {
    private TypeOfToken type;
    private String context;
    private List<Token1> children = new LinkedList<Token1>();
    private boolean comutative = false;
    private boolean ready = false;
    private int x = 0;
    private int y = 0;

    public Token1(TypeOfToken type, String context) {
        this.type = type;
        this.context = context;
        if(type == TypeOfToken.VARIABLE || type == TypeOfToken.CONSTANT){
            ready = true;
        }
    }

    public TypeOfToken getType() {
        return type;
    }

    public void setType(TypeOfToken type) {
        this.type = type;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public synchronized List<Token1> getChildren() {
        return children;
    }

    public void setChildren(List<Token1> children) {
        this.children = children;
    }

    public boolean isComutative() {
        return comutative;
    }

    public void setComutative(boolean comutative) {
        this.comutative = comutative;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean checkReady(){
        boolean tmp = true;
        for(Token1 token1 : children){
            if(!token1.isReady()){
                tmp = false;
            }
        }
        return tmp;
    }

    public int getLevel() {
        if (type == TypeOfToken.VARIABLE || type == TypeOfToken.CONSTANT) {
            return 1;
        } else {
            int max = 0;
            for (Token1 child : children) {
                if (child.getLevel() > max) {
                    max = child.getLevel();
                }
            }
            return max + 1;
        }
    }

    public int getWeight() {
        if (type == TypeOfToken.VARIABLE || type == TypeOfToken.CONSTANT) {
            return 0;
        }
        int weight = 0;
        for (Token1 child : children) {
            weight += child.getWeight();
        }
        if (type == TypeOfToken.MULTIPLY
                && children.get(0).getType() == TypeOfToken.CONSTANT
                && children.get(0).getContext().equals("-1")) {
            weight -= 2;
        }
        return weight + type.weight() * (children.size() - 1);
    }

    public int[] getOperations() {
        int[] result = new int[4];
        for (Token1 child : children) {
            int[] tmpresult = child.getOperations();
            for (int i = 0; i < result.length; i++) {
                result[i] += tmpresult[i];
            }
        }
        if (type == TypeOfToken.PLUS) {
            result[0]++;
        }
        if (type == TypeOfToken.MINUS) {
            result[1]++;
        }
        if (type == TypeOfToken.MULTIPLY) {
            result[2]++;
        }
        if (type == TypeOfToken.DIVIDE) {
            result[3]++;
        }
        return result;
    }

    public boolean isNegative() {
        if (type == TypeOfToken.UNMINUS) {
            return true;
        }
        if (type == TypeOfToken.CONSTANT && context.charAt(0) == '-') {
            return true;
        }
        if (children.size() != 0) {
            if (type == TypeOfToken.MULTIPLY && children.get(0).isNegative()) {
                return true;
            }
            return type == TypeOfToken.DIVIDE && children.get(0).isNegative();
        }
        return false;
    }

    public void doPositive() {
        if (isNegative()) {
            if (type == TypeOfToken.CONSTANT) {
                context.replace("-", "");
            }
            if (type == TypeOfToken.MULTIPLY) {
                if (children.get(0).getType() == TypeOfToken.CONSTANT
                        && children.get(0).getContext().equals("-1")) {
                    if (children.size() > 2) {
                        children.remove(0);
                    } else {
                        type = children.get(1).getType();
                        context = children.get(1).getContext();
                        children = children.get(1).getChildren();
                    }
                } else {
                    children.get(0).doPositive();
                }
            }
        }
    }

    public Token1 getConstant() {
        if (type == TypeOfToken.CONSTANT) {
            return this;
        }
        if (type == TypeOfToken.MULTIPLY) {
            for (Token1 child : children) {
                if (child.getType() == TypeOfToken.CONSTANT) {
                    return child;
                }
            }
        }
        return null;
    }

    public Token1 copy() {
        Token1 newtoken = new Token1(type, context);
        if (type != TypeOfToken.CONSTANT || type != TypeOfToken.VARIABLE) {
            for (Token1 child : children) {
                newtoken.getChildren().add(child.copy());
            }
        }
        return newtoken;
    }

    @Override
    public boolean equals(Object obj) {
        Token1 token1 = (Token1) obj;
        boolean result = true;
        if (type != token1.getType()) {
            result = false;
        } else {
            if ((type == TypeOfToken.VARIABLE || type == TypeOfToken.CONSTANT)
                    && !context.equals(token1.getContext())) {
                result = false;
            } else {
                if (children.size() != token1.getChildren().size()) {
                    result = false;
                } else {
                    List<Token1> otherchildren = token1.getChildren();
                    for (Token1 ourtoken : children) {
                        boolean check = false;
                        for (Token1 othertoken : otherchildren) {
                            check = check || ourtoken.equals(othertoken);
                        }
                        result = result && check;
                    }
                }
            }
        }
        return result;
    }

    public List<Token1> getVariables() {
        List<Token1> result = new LinkedList<Token1>();
        if (type == TypeOfToken.VARIABLE) {
            result.add(this.copy());
        }
        if (type == TypeOfToken.MULTIPLY) {
            for (Token1 child : children) {
                if (child.getType() != TypeOfToken.CONSTANT) {
                    result.add(child.copy());
                }
            }
        }
        if (type == TypeOfToken.DIVIDE
                && children.get(0).getType() != TypeOfToken.CONSTANT) {
            result.add(children.get(0).copy());
        }
        return result;
    }

    public boolean containsVariables(List<Token1> list) {
        List<Token1> variables = this.getVariables();
        String context = "";
        for (Token1 tmp : variables) {
            context += tmp.toInfixForm() + "|";
        }
        boolean result = true;
        for (Token1 tmp : list) {
            if (!context.contains(tmp.toInfixForm() + "|")) {
                result = false;
            }
        }
        return result;
    }

    public void setCoord(int x, int y, int delta) {
        this.x = x;
        this.y = y;
        if (children.size() == 1) {
            children.get(0).setCoord(x, y - 60, delta / 2);
        }
        if (children.size() == 2) {
            children.get(0).setCoord(x - delta, y - 60, delta / 2);
            children.get(1).setCoord(x + delta, y - 60, delta / 2);
        }
    }

    public List<Token1> toList(){
        List<Token1> result = new LinkedList<Token1>();
        if(children.size() > 0){
            for(Token1 child : children){
                result.addAll(child.toList());
            }
            result.add(this);
        }
        return result;
    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(2));

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        String text = null;
        if (type == TypeOfToken.VARIABLE || type == TypeOfToken.CONSTANT) {
            text = context;
        } else {
            if (type == TypeOfToken.UNMINUS) {
                text = "-";
            }
            if (type == TypeOfToken.PLUS) {
                text = "+";
            }
            if (type == TypeOfToken.MINUS) {
                text = "-";
            }
            if (type == TypeOfToken.MULTIPLY) {
                text = "*";
            }
            if (type == TypeOfToken.DIVIDE) {
                text = "/";
            }
        }

        for (Token1 token1 : children) {
            g.drawLine(x, y, token1.getX(), token1.getY());
            token1.draw(g);
        }

        g.setColor(Color.WHITE);
        g.fillRect(x - 20, y - 20, 40, 40);
        g.setColor(Color.BLACK);
        g.drawOval(x - 20, y - 20, 40, 40);

        g.drawString(text, x - 5, y + 2);

    }

    public String toInfixForm() {
        String result = "";
        if (type == TypeOfToken.VARIABLE) {
            result += context;
        } else {
            if (type == TypeOfToken.CONSTANT) {
                result += context;
                if (this.isNegative()) {
                    result = "(" + result + ")";
                }
            } else {
                Token1 left = children.get(0);
                if (type == TypeOfToken.UNMINUS) {
                    if (left.getType() == TypeOfToken.CONSTANT
                            || left.getType() == TypeOfToken.VARIABLE) {
                        result = "-" + left.toInfixForm();
                    } else {
                        result = "-(" + left.toInfixForm() + ")";
                    }
                }
                if (type == TypeOfToken.PLUS) {
                    if (left.getType() == TypeOfToken.UNMINUS) {
                        result += "(" + left.toInfixForm() + ")";
                    } else {
                        result += left.toInfixForm();
                    }
                    for (int i = 1; i < children.size(); i++) {
                        if (children.get(i).getType() == TypeOfToken.UNMINUS) {
                            result += "+(" + children.get(i).toInfixForm()
                                    + ")";
                        } else {
                            result += "+" + children.get(i).toInfixForm();
                        }
                    }
                }
                if (type == TypeOfToken.MINUS) {
                    if (left.getType() == TypeOfToken.UNMINUS) {
                        result += "(" + left.toInfixForm() + ")";
                    } else {
                        result += left.toInfixForm();
                    }
                    for (int i = 1; i < children.size(); i++) {
                        if (children.get(i).getType() == TypeOfToken.PLUS
                                || children.get(i).getType() == TypeOfToken.MINUS
                                || children.get(i).getType() == TypeOfToken.UNMINUS) {
                            result += "-(" + children.get(i).toInfixForm()
                                    + ")";
                        } else {
                            result += "-" + children.get(i).toInfixForm();
                        }

                    }
                }
                if (type == TypeOfToken.MULTIPLY) {
                    if (left.getType() == TypeOfToken.PLUS
                            || left.getType() == TypeOfToken.MINUS
                            || left.getType() == TypeOfToken.UNMINUS) {
                        result += "(" + left.toInfixForm() + ")";
                    } else {
                        result += left.toInfixForm();
                    }
                    for (int i = 1; i < children.size(); i++) {
                        if (children.get(i).getType() == TypeOfToken.PLUS
                                || children.get(i).getType() == TypeOfToken.MINUS
                                || children.get(i).getType() == TypeOfToken.DIVIDE
                                || children.get(i).getType() == TypeOfToken.UNMINUS) {
                            result += "*(" + children.get(i).toInfixForm()
                                    + ")";
                        }
                        else if (children.get(i).getType() == TypeOfToken.CONSTANT) {
                            if (children.get(i).toInfixForm().equals("(-1)")) {
                                continue;
                            }
                        }
                        else {
                            result += "*" + children.get(i).toInfixForm();
                        }
                    }
                }
                if (type == TypeOfToken.DIVIDE) {
                    if (left.getType() == TypeOfToken.PLUS
                            || left.getType() == TypeOfToken.MINUS
                            || left.getType() == TypeOfToken.UNMINUS) {
                        result += "(" + left.toInfixForm() + ")";
                    } else {
                        result += left.toInfixForm();
                    }
                    for (int i = 1; i < children.size(); i++) {
                        if (children.get(i).getType() == TypeOfToken.PLUS
                                || children.get(i).getType() == TypeOfToken.MINUS
                                || children.get(i).getType() == TypeOfToken.MULTIPLY
                                || children.get(i).getType() == TypeOfToken.UNMINUS
                                || children.get(i).getType() == TypeOfToken.DIVIDE) {
                            result += "/(" + children.get(i).toInfixForm()
                                    + ")";
                        } else {
                            result += "/" + children.get(i).toInfixForm();
                        }
                    }
                }
            }
        }
        return result;
    }

    public String toString() {
        return this.toInfixForm();
    }
}
