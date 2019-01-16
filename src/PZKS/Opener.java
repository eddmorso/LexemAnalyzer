package PZKS;

import PZKS.Opening.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Opener {

    void doOpening(){
        String outputstring = "";
        LexicalAnalyzer lexycalanalyzer = new LexicalAnalyzer();
        List<Token1> lexicalarray = null;
        try {
            lexicalarray = lexycalanalyzer.analyze(Main.inputString);
        } catch (LexicalException e) {
            System.out.println(e);
        }
        if(lexicalarray == null){
            return;
        }

        List<Token1> list = new LinkedList<Token1>();

        List<Token1> postfixnotation = lexycalanalyzer.toPostfix(lexicalarray);

        SyntaxAnalyzer sintaxanalyzer = new SyntaxAnalyzer();
        list.addAll(sintaxanalyzer.analyze(postfixnotation));

        DistributiveAnalyzer distributiveanalyzer = new DistributiveAnalyzer();
        list.addAll(distributiveanalyzer.analyze(postfixnotation));
        outputstring += distributiveanalyzer.getLog();

        System.out.println(outputstring);
    }

//    private List<Token> tokens = new ArrayList<>();
//
//    Opener(String phrase){
//
//        Parser parser = new Parser(phrase);
//
//        if(parser.check()) {
//
//            tokens = parser.makeLexem();
//
//            openMulDivBefore();
//            openMulDivAfter();
//            openMinus();
//            openPlus();
//
//        }else System.out.println("correct your phrase");
//    }
//
//    private void openMinus(){
//
//        for(int i = 0; i < tokens.size() - 1; i++){
//            if(tokens.get(i).getType() == Type.Minus && tokens.get(i + 1).getType() == Type.OpenParenthesis){
//                i++;
//                tokens.remove(i);
//                while(tokens.get(i).getType() != Type.CloseParenthesis){
//
//                    if(tokens.get(i).getType() == Type.Minus){
//
//                        tokens.get(i).setValue("+");
//                        tokens.get(i).setType(Type.Plus);
//
//                    }else if(tokens.get(i).getType() == Type.Plus){
//
//                        tokens.get(i).setValue("-");
//                        tokens.get(i).setType(Type.Minus);
//                    }
//                    i++;
//                }
//                tokens.remove(i);
//                i--;
//            }
//        }
//    }
//
//    private void openPlus(){
//        for(int i = 0; i < tokens.size() - 1; i++){
//            if(tokens.get(i).getType() == Type.Plus && tokens.get(i + 1).getType() == Type.OpenParenthesis){
//                i++;
//                tokens.remove(i);
//                while(tokens.get(i).getType() != Type.CloseParenthesis){
//                    i++;
//                }
//                tokens.remove(i);
//                i--;
//            }
//        }
//    }
//
//    private void openMulDivBefore(){
//
//        for(int i = 0; i < tokens.size() - 1; i++){
//            if((tokens.get(i).getType() == Type.Divide || tokens.get(i).getType() == Type.Multiply) && tokens.get(i + 1).getType() == Type.OpenParenthesis){
//                int end = i;
//                while(tokens.get(i).getType() != Type.Plus && tokens.get(i).getType() != Type.Minus && i > 0){
//                    i--;
//                }
//                int begin = i + 1;
//
//                List<Token> coefficient = new ArrayList<>(tokens.subList(begin, end + 1));
//
//                i = end + 1;
//
//                while(tokens.get(i).getType() != Type.CloseParenthesis && i < tokens.size() - 1){
//                    if((tokens.get(i).getType() == Type.Number || tokens.get(i).getType() == Type.Variable) && (tokens.get(i + 1).getType() != Type.Multiply && tokens.get(i + 1).getType() != Type.Divide)){
//                        tokens.addAll(i, coefficient);
//                        i += coefficient.size() + 1;
//                    }else
//                    i++;
//                }
//                tokens.subList(begin, end + 1).clear();
//            }
//        }
//    }
//
//    private void openMulDivAfter() {
//
//        for (int i = 0; i < tokens.size() - 1; i++) {
//            if (tokens.get(i).getType() == Type.CloseParenthesis && (tokens.get(i + 1).getType() == Type.Divide || tokens.get(i + 1).getType() == Type.Multiply)) {
//                int begin = i + 1;
//                while (tokens.get(i).getType() != Type.Plus && tokens.get(i).getType() != Type.Minus && i < tokens.size() - 1) {
//                    i++;
//                }
//                int end = i;
//
//                List<Token> coefficient = new ArrayList<>(tokens.subList(begin, end));
//                tokens.subList(begin, end).clear();
//                i -= coefficient.size() + 1;
//
//                while (tokens.get(i).getType() != Type.OpenParenthesis) {
//                    i--;
//                }
//                while (tokens.get(i).getType() != Type.CloseParenthesis && i < tokens.size() - 1) {
//                    if ((tokens.get(i).getType() == Type.Number || tokens.get(i).getType() == Type.Variable) && (tokens.get(i + 1).getType() != Type.Multiply && tokens.get(i + 1).getType() != Type.Divide)) {
//                        i++;
//                        tokens.addAll(i, coefficient);
//                        i += coefficient.size();
//                    } else
//                        i++;
//                }
//            }
//        }
//    }
//
//    private void openMulDivBetween(){
//        for(int i = 0; i < tokens.size(); i++){
//            if(tokens.get(i).getType() == Type.Multiply && tokens.get(i + 1).getType() == Type.OpenParenthesis
//                && tokens.get(i - 1).getType() == Type.CloseParenthesis){
//
//            }
//        }
//    }
//
//    void printResult(){
//        for(Token token : tokens){
//            System.out.print(token.getValue());
//        }
//    }
}
