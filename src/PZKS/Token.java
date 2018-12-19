package PZKS;

import java.util.ArrayList;
import java.util.List;

class Token {

private Type type;
private String value;
private int priority;
private int subValue;
private List<Token> subToken = new ArrayList<>();

    Token(Type type, String value, int priority){
        setPriority(priority);
        setType(type);
        setValue(value);
    }

    Type getType() {
        return type;
    }

    int getPriority() {
        return priority;
    }

    String getValue() {
        return value;
    }

    private void setType(Type type) {
        this.type = type;
    }

    void setValue(String value) {
        this.value = value;
    }

    private void setPriority(int priority) {
        this.priority = priority;
    }

    int getSubValue() {
        return subValue;
    }

    void setSubValue(int subValue) {
        this.subValue = subValue;
    }

    void setSubToken(Token subToken) {
        this.subToken.add(subToken);
    }

    List<Token> getSubToken() {
        return subToken;
    }
}
