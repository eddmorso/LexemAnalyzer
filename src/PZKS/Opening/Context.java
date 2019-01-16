package PZKS.Opening;

public class Context {
    private String context = new String();
    private int counter = 0;

    public void addToContext(char c){
        context = new String(context + c);
    }

    public void setNullContext(){
        context = new String();
    }

    public void setContext(String context){
        this.context = context;
    }

    public String getContext(){
        return context;
    }

    public void addCounter(){
        counter++;
    }

    public void subCounter(){
        counter--;
    }

    public int getCounter(){
        return counter;
    }

    public String toString(){
        return context;
    }
}

