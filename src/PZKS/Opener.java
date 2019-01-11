package PZKS;

import java.util.List;

class Opener {

    Opener(String phrase){
        Parser parser = new Parser(phrase);
        if(parser.check()) {
            openParenthesis(parser.makeLexem());
        }else System.out.println("correct your phrase");
    }

    void openParenthesis(List<Token> tokens){

        for(int i = 0; i < tokens.size() - 1; i++){
            if(tokens.get(i).getType() == Type.Minus && tokens.get(i + 1).getType() == Type.OpenParenthesis){
                i++;
                tokens.remove(i);

                while(tokens.get(i).getType() != Type.CloseParenthesis){

                    if(tokens.get(i).getType() == Type.Minus){

                        tokens.get(i).setValue("+");
                        tokens.get(i).setType(Type.Plus);

                    }else if(tokens.get(i).getType() == Type.Plus){

                        tokens.get(i).setValue("-");
                        tokens.get(i).setType(Type.Minus);
                    }

                    i++;
                }
            }
        }
        for(Token token : tokens){
            System.out.print(token.getValue());
        }
    }
}
