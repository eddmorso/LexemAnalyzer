package PZKS.Opening;

import java.util.List;

public enum AnalyzerStates {
    BEGIN {
        @Override
        public AnalyzerStates next(char c, Context context, List<Token1> list){
            TypeOfSymbols symboltype = TypeOfSymbols.getTypeOFSymbols(c);
            AnalyzerStates nextstate = ERROR;
            if(symboltype == TypeOfSymbols.LETTER){
                nextstate = VARIABLE;
                context.addToContext(c);
            }
            if(symboltype == TypeOfSymbols.DIGIT){
                nextstate = CONSTANT;
                context.addToContext(c);
            }
            if(symboltype == TypeOfSymbols.MINUS){
                nextstate = MINUS;
            }
            if(symboltype == TypeOfSymbols.OPENBRACKET){
                nextstate = BEGIN;
                list.add(new Token1(TypeOfToken.OPENBRACKET, null));
                context.setNullContext();
                context.addCounter();
            }
            if(nextstate == ERROR){
                if(symboltype == TypeOfSymbols.OPERATOR){
                    context.setContext("Неправильный символ в начале выражения. Выражение не может начинаться с оператора.");
                }else{
                    if(symboltype == TypeOfSymbols.CLOSEBRACKET){
                        context.setContext("Неправильный символ в начале выражения. Выражение не может начинаться с закрывающейся скобки.");
                    }else{
                        context.setContext("Неправильный символ в начале выражения.");
                    }
                }
            }
            return nextstate;
        }},
    VARIABLE {
        @Override
        public AnalyzerStates next(char c, Context context, List<Token1> list) {
            TypeOfSymbols symboltype = TypeOfSymbols.getTypeOFSymbols(c);
            AnalyzerStates nextstate = ERROR;
            String err = "Неправильный символ в переменной.";
            if(symboltype == TypeOfSymbols.LETTER || symboltype == TypeOfSymbols.DIGIT){
                nextstate = VARIABLE;
                context.addToContext(c);
            }
            if(symboltype == TypeOfSymbols.CLOSEBRACKET){
                context.subCounter();
                if(context.getCounter() >= 0){
                    nextstate = CLOSEBRACKET;
                    list.add(new Token1(TypeOfToken.VARIABLE, context.getContext()));
                    context.setNullContext();
                }else{
                    err = "Неправильный порядок скобок.";
                }
            }
            if(symboltype == TypeOfSymbols.OPERATOR){
                nextstate = OPERATOR;
                list.add(new Token1(TypeOfToken.VARIABLE, context.getContext()));
                context.setNullContext();
                context.addToContext(c);
            }
            if(symboltype == TypeOfSymbols.MINUS){
                nextstate = MINUS;
                list.add(new Token1(TypeOfToken.VARIABLE, context.getContext()));
                list.add(new Token1(TypeOfToken.PLUS, null));
                context.setNullContext();
            }
            if(symboltype == TypeOfSymbols.EOF){
                nextstate = EXITSTATE;
                list.add(new Token1(TypeOfToken.VARIABLE, context.getContext()));
                context.setNullContext();
            }
            if(nextstate == ERROR){
                if(symboltype == TypeOfSymbols.OPENBRACKET){
                    context.setContext(err + " Ожидается оператор.");
                }else{
                    context.setContext(err);
                }
            }
            return nextstate;
        }
    },
    CONSTANT {
        @Override
        public AnalyzerStates next(char c, Context context, List<Token1> list) {
            TypeOfSymbols symboltype = TypeOfSymbols.getTypeOFSymbols(c);
            AnalyzerStates nextstate = ERROR;
            String err = "Неправильный символ в константе.";
            if(symboltype == TypeOfSymbols.DIGIT){
                nextstate = CONSTANT;
                context.addToContext(c);
            }
            if(symboltype == TypeOfSymbols.DOT){
                if(context.getContext().contains(".")){
                    nextstate = ERROR;
                    err = "Два разделителя в константе.";
                }else{
                    nextstate = CONSTANT;
                    context.addToContext(c);
                }
            }
            if(symboltype == TypeOfSymbols.CLOSEBRACKET){
                context.subCounter();
                if(context.getCounter() >= 0){
                    nextstate = CLOSEBRACKET;
                    list.add(new Token1(TypeOfToken.CONSTANT, context.getContext()));
                    context.setNullContext();
                }else{
                    err = "Неправильный порядок скобок.";
                }
            }
            if(symboltype == TypeOfSymbols.OPERATOR){
                nextstate = OPERATOR;
                list.add(new Token1(TypeOfToken.CONSTANT, context.getContext()));
                context.setNullContext();
                context.addToContext(c);
            }
            if(symboltype == TypeOfSymbols.MINUS){
                nextstate = MINUS;
                list.add(new Token1(TypeOfToken.CONSTANT, context.getContext()));
                list.add(new Token1(TypeOfToken.PLUS, null));
                context.setNullContext();
            }
            if(symboltype == TypeOfSymbols.EOF){
                nextstate = EXITSTATE;
                list.add(new Token1(TypeOfToken.CONSTANT, context.getContext()));
                context.setNullContext();
            }
            if(nextstate == ERROR){
                if(symboltype == TypeOfSymbols.OPENBRACKET){
                    context.setContext(err + " Ожидается оператор.");
                }else{
                    if(symboltype == TypeOfSymbols.LETTER){
                        context.setContext(err + " В константе недопустимо использование букв.");
                    }else{
                        context.setContext(err);
                    }
                }
            }
            return nextstate;
        }
    },
    MINUS {
        @Override
        public AnalyzerStates next(char c, Context context, List<Token1> list) {
            TypeOfSymbols symboltype = TypeOfSymbols.getTypeOFSymbols(c);
            AnalyzerStates nextstate = ERROR;
            if(symboltype == TypeOfSymbols.LETTER){
                nextstate = VARIABLE;
                list.add(new Token1(TypeOfToken.CONSTANT, "-1"));
                list.add(new Token1(TypeOfToken.MULTIPLY, null));
                context.setNullContext();
                context.addToContext(c);
            }
            if(symboltype == TypeOfSymbols.OPENBRACKET){
                nextstate = BEGIN;
                list.add(new Token1(TypeOfToken.CONSTANT, "-1"));
                list.add(new Token1(TypeOfToken.MULTIPLY, null));
                list.add(new Token1(TypeOfToken.OPENBRACKET, null));
                context.addCounter();
                context.setNullContext();
            }
            if(symboltype == TypeOfSymbols.DIGIT){
                nextstate = CONSTANT;
                context.addToContext('-');
                context.addToContext(c);
            }
            if(nextstate == ERROR){
                context.setContext("Неправильный знак после оператора \"-\". Ожидается переменная или константа.");
            }
            return nextstate;
        }
    },
    OPERATOR {
        @Override
        public AnalyzerStates next(char c, Context context, List<Token1> list) {
            TypeOfSymbols symboltype = TypeOfSymbols.getTypeOFSymbols(c);
            AnalyzerStates nextstate = ERROR;
            String operator = context.getContext();
            if(symboltype == TypeOfSymbols.LETTER){
                nextstate = VARIABLE;
                if(operator.equals("+")){
                    list.add(new Token1(TypeOfToken.PLUS, null));
                }
                if(operator.equals("*")){
                    list.add(new Token1(TypeOfToken.MULTIPLY, null));
                }
                if(operator.equals("/")){
                    list.add(new Token1(TypeOfToken.DIVIDE, null));
                }
                context.setNullContext();
                context.addToContext(c);
            }
            if(symboltype == TypeOfSymbols.DIGIT){
                nextstate = CONSTANT;
                if(operator.equals("+")){
                    list.add(new Token1(TypeOfToken.PLUS, null));
                }
                if(operator.equals("*")){
                    list.add(new Token1(TypeOfToken.MULTIPLY, null));
                }
                if(operator.equals("/")){
                    list.add(new Token1(TypeOfToken.DIVIDE, null));
                }
                context.setNullContext();
                context.addToContext(c);
            }
            if(symboltype == TypeOfSymbols.OPENBRACKET){
                nextstate = BEGIN;
                if(operator.equals("+")){
                    list.add(new Token1(TypeOfToken.PLUS, null));
                }
                if(operator.equals("*")){
                    list.add(new Token1(TypeOfToken.MULTIPLY, null));
                }
                if(operator.equals("/")){
                    list.add(new Token1(TypeOfToken.DIVIDE, null));
                }
                list.add(new Token1(TypeOfToken.OPENBRACKET, null));
                context.setNullContext();
                context.addCounter();
            }
            if(nextstate == ERROR){
                context.setContext("Неправильный знак после оператора \"" + operator + "\". Ожидается переменная или константа.");
            }
            return nextstate;
        }
    },
    CLOSEBRACKET {
        @Override
        public AnalyzerStates next(char c, Context context, List<Token1> list) {
            TypeOfSymbols symboltype = TypeOfSymbols.getTypeOFSymbols(c);
            AnalyzerStates nextstate = ERROR;
            String err = "Неправильный знак после \")\"";
            if(symboltype == TypeOfSymbols.CLOSEBRACKET){
                context.subCounter();
                if(context.getCounter() >= 0){
                    nextstate = CLOSEBRACKET;
                    list.add(new Token1(TypeOfToken.CLOSEBRACKET, null));
                    context.setNullContext();
                }else{
                    err = "Неправильный порядок скобок";
                }
            }
            if(symboltype == TypeOfSymbols.OPERATOR){
                nextstate = OPERATOR;
                list.add(new Token1(TypeOfToken.CLOSEBRACKET, null));
                context.setNullContext();
                context.addToContext(c);
            }
            if(symboltype == TypeOfSymbols.MINUS){
                nextstate = MINUS;
                list.add(new Token1(TypeOfToken.CLOSEBRACKET, null));
                list.add(new Token1(TypeOfToken.PLUS, null));
                context.setNullContext();
            }
            if(symboltype == TypeOfSymbols.EOF){
                nextstate = EXITSTATE;
                list.add(new Token1(TypeOfToken.CLOSEBRACKET, null));
                context.setNullContext();
            }
            if(nextstate == ERROR){
                context.setContext(err + " Ожидается оператор.");
            }
            return nextstate;
        }
    },
    EXITSTATE {
        @Override
        public AnalyzerStates next(char c, Context context, List<Token1> list) {
            return null;
        }
    },
    ERROR {
        @Override
        public AnalyzerStates next(char c, Context context, List<Token1> list) {
            return null;
        }
    };

    public abstract AnalyzerStates next(char c, Context context, List<Token1> list);
}
