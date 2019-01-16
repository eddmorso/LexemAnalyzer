package PZKS.Opening;

public class LexicalException extends Exception {
    private String msg;
    private int position;

    public LexicalException(String msg, int position){
        this.msg = msg;
        this.position = position;
    }

    @Override
    public String toString() {
        return position + ": " + msg;
    }

    public String getMsg() {
        return msg;
    }

    public int getPosition() {
        return position;
    }
}

