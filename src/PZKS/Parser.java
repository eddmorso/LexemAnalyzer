package PZKS;

import java.util.*;

enum Type{
    CloseParenthesis,
    OpenParenthesis,
    Minus,
    Plus,
    Multiply,
    Divide,
    Number,
    Variable,
    Constant
}

class Parser {

    static final int HIGH_PRIORITY = 2;
    static final int LOW_PRIORITY = 1;
    static final int STANDARD_PRIORITY = 0;

    private char[] array;

    Parser(String s) {
        this.array = parseString(s);
    }

    boolean check() {

        boolean ending = checkEnding();
        boolean beginning = checkBeginning();
        boolean operations = checkOperations();
        boolean parenthesis = checkParenthesis();
        boolean variable = checkVar();

        return ending && beginning && operations && parenthesis && variable;
    }

    private char[] parseString(String s) {

        s = s.replaceAll(" ", "");

        char[] parsedArray = new char[s.length()];

        for (int i = 0; i < s.length(); i++) {
            parsedArray[i] = s.charAt(i);
        }
        return parsedArray;
    }

    private boolean checkBeginning() {
        boolean isOk = true;
        if (array[0] == '/' || array[0] == '*' || array[0] == ')') {
            isOk = false;
            System.out.println("unexpected token at the beginning");
        }
        return isOk;
    }

    private boolean checkEnding() {
        boolean isOk = true;
        if (array[array.length - 1] == '/' || array[array.length - 1] == '*' || array[array.length - 1] == '(' || array[array.length - 1] == '+' || array[array.length - 1] == '-') {
            isOk = false;
            System.out.println("unexpected token at the end");
        }
        return isOk;
    }

    private boolean checkParenthesis() {
        boolean isOk, isEmpty = true, isOdd = true, isWrongOrder = true, isOperationBetween = true;
        int countOpen = 0, countClose = 0;
        //check whether any empty parenthesis takes place
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] == '(' && array[i + 1] == ')') {
                isEmpty = false;
            }
            //check whether no parenthesis equals
            if (array[i] == ')') countClose++;
            if (array[i] == '(') countOpen++;

            if(array[i] == ')' && array[i + 1] == '('){
                isOperationBetween = false;
            }
        }
        if (array[array.length - 1] == '(') countOpen++;
        if (array[array.length - 1] == ')') countClose++;
        if (countClose != countOpen) {
            isOdd = false;
        }

        //check whether takes place wrong parenthesis order
        ArrayList<Integer> arrayListOpen = new ArrayList<>();
        ArrayList<Integer> arrayListClose = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            if (array[i] == '(') arrayListOpen.add(i);
            if (array[i] == ')') arrayListClose.add(i);
        }

        if (arrayListOpen.size() == arrayListClose.size()) {
            for (int i = 0; i < arrayListClose.size(); i++) {
                if (arrayListOpen.get(i) > arrayListClose.get(i)) isWrongOrder = false;
            }
        }

        isOk = isEmpty && isWrongOrder && isOdd && isOperationBetween;

        if (!isEmpty) System.out.println("empty parenthesis");
        if (!isWrongOrder) System.out.println("wrong order of parenthesis");
        if (!isOdd) System.out.println("odd number of parenthesis");
        if (!isOperationBetween) System.out.println("no operation between parenthesis");

        return isOk;
    }

    private boolean checkOperations() {
        boolean isOk, isAfter = true, isBefore = true, isDoubling = true, isOperationNearBy = true;

        //check if any parenthesis are next to operations
        for (int i = 0; i < array.length - 1; i++) {
            if ((array[i] == '*' || array[i] == '/' || array[i] == '+' || array[i] == '-') && array[i + 1] == ')') {
                isAfter = false;
            }
            if ((array[i + 1] == '*' || array[i + 1] == '/') && array[i] == '(') {
                isBefore = false;
            }

            //double operations check
            if (array[i] == '*' && (array[i + 1] == '*' || array[i + 1] == '/' || array[i + 1] == '+' || array[i + 1] == '-')) {
                isDoubling = false;
            }else {
                if (array[i] == '/' && (array[i + 1] == '*' || array[i + 1] == '/' || array[i + 1] == '+' || array[i + 1] == '-')) {
                    isDoubling = false;
                } else {
                    if (array[i] == '-' && (array[i + 1] == '*' || array[i + 1] == '/' || array[i + 1] == '+' || array[i + 1] == '-')) {
                        isDoubling = false;
                    } else {
                        if (array[i] == '+' && (array[i + 1] == '*' || array[i + 1] == '/' || array[i + 1] == '+' || array[i + 1] == '-')) {
                            isDoubling = false;
                        }
                    }
                }
            }

            //check operation after parenthesis
            if (array[i] == ')' && (array[i + 1] != ')' && array[i + 1] != '*' && array[i + 1] != '/' && array[i + 1] != '+' && array[i + 1] != '-') && i != array.length - 1) {
                isOperationNearBy = false;
            }else {
                if (array[i + 1] == '(' && array[i] != '(' && (array[i] != '*' && array[i] != '/' && array[i] != '+' && array[i] != '-' && array[i] != '(')) {
                    isOperationNearBy = false;
                }
            }
        }

        isOk = isAfter && isBefore && isDoubling && isOperationNearBy;

        if (!isAfter) System.out.println("parenthesis after operation");
        if (!isBefore) System.out.println("parenthesis before operation");
        if (!isDoubling) System.out.println("double operation");
        if (!isOperationNearBy) System.out.println("no operations near parenthesis");

        return isOk;
    }

    private boolean checkVar() {
        boolean isOk, isVarAfterNum = true, isAfterDot = true, isWrongName = true, isUsableVar = true;

        //check if variable is after number
        for (int i = 0; i < array.length - 1; i++) {
            if ((array[i] >= 48 && array[i] <= 57) && ((array[i + 1] >= 65 && array[i + 1] <= 90) || (array[i + 1] >= 97 && array[i + 1] <= 122))) {
                isVarAfterNum = false;
            }

            //check whether var is Near var
            if(((array[i] >= 65 && array[i] <= 90) || (array[i] >= 97 && array[i] <= 122)) && ((array[i + 1] >= 65 && array[i + 1] <= 90) || (array[i + 1] >= 97 && array[i + 1] <= 122))){
                isWrongName = false;
            }

            //check whether var is usable
            if(array[i] >= 33 && array[i] <= 39 || array[i] == 44 || array[i] >= 58 && array[i] <= 64 || array[i] >= 91 && array[i] <= 96 || array[i] >= 123 && array[i] <= 127){
                isUsableVar = false;
            }

            //check if number after variable
            if (((array[i] >= 65 && array[i] <= 90) || (array[i] >= 97 && array[i] <= 122)) && (array[i + 1] >= 48 && array[i + 1] <= 57)) {
                isVarAfterNum = false;
            }
            //check whether const is right
            if ((array[i] >= 48 && array[i] <= 57) && (array[i + 1] == '.')) {
                int j = i + 2;
                while (array[j] >= 48 && array[j] <= 57) {
                    if (array[j + 1] == '.') isAfterDot = false;
                    j++;
                }
            }
        }

        //check whether var is usable
        if(array[array.length - 1] >= 33 && array[array.length - 1] <= 39 || array[array.length - 1] == 44 || array[array.length - 1] >= 58 && array[array.length - 1] <= 64 || array[array.length - 1] >= 91 && array[array.length - 1] <= 96 || array[array.length - 1] >= 123 && array[array.length - 1] <= 127){
            isUsableVar = false;
        }

        isOk = isVarAfterNum && isAfterDot && isWrongName && isUsableVar;

        if (!isVarAfterNum) System.out.println("missing operation between variable and const");
        if (!isAfterDot) System.out.println("incorrect number format");
        if (!isWrongName) System.out.println("wrong variable name");
        if (!isUsableVar) System.out.println("use of unexpected token");

        return isOk;
    }

    List<Token> makeLexem() {
        List<Token> tokens = new ArrayList<>();

        for (int i = 0; i < array.length - 1; i++) {
            //adds operations as lexemes
            if (array[i] == '+' && (i-1 >= 0) && (array[i - 1] >= 48 && array[i - 1] <= 57 || array[i - 1] >= 65 && array[i - 1] <= 90 || array[i - 1] >= 97 && array[i - 1] <= 122 || array[i - 1] == ')')) {
                tokens.add(new Token(Type.Plus, String.valueOf(array[i]), HIGH_PRIORITY));
                continue;
            } else {
                if (array[i] == '-' && (i-1 >= 0) && (array[i - 1] >= 48 && array[i - 1] <= 57 || array[i - 1] >= 65 && array[i - 1] <= 90 || array[i - 1] >= 97 && array[i - 1] <= 122 || array[i - 1] == ')')) {
                    tokens.add(new Token(Type.Minus, String.valueOf(array[i]), HIGH_PRIORITY));
                    continue;
                } else {
                    if (array[i] == '/') {
                        tokens.add(new Token(Type.Divide, String.valueOf(array[i]), LOW_PRIORITY));
                        continue;
                    } else {
                        if (array[i] == '*') {
                            tokens.add(new Token(Type.Multiply, String.valueOf(array[i]), LOW_PRIORITY));
                            continue;
                        }
                    }
                }
            }
            //adds variables as lexemes
            if ((array[i] >= 65 && array[i] <= 90 || array[i] >= 97 && array[i] <= 122) ||
                    ((array[i] == '+' || array[i] == '-') && (i - 1 < 0) && (array[i + 1] >= 65 && array[i + 1] <= 90 || array[i + 1] >= 97 && array[i + 1] <= 122)) ||
                        ((array[i] == '+' || array[i] == '-') && (i - 1 >= 0) && ((array[i - 1] < 48 || array[i - 1] > 57) && (array[i - 1] < 65 || array[i - 1] > 90) && (array[i - 1] < 97 || array[i - 1] > 122)) && (array[i + 1] >= 65 && array[i + 1] <= 90 || array[i + 1] >= 97 && array[i + 1] <= 122))){

                if(array[i] == '+' || array[i] == '-'){
                    tokens.add(new Token(Type.Variable, String.valueOf(array[i]) + String.valueOf(array[i + 1]),STANDARD_PRIORITY));
                    i++;
                }else
                    tokens.add(new Token(Type.Variable, String.valueOf(array[i]), STANDARD_PRIORITY));
            }else

            //adds numbers as lexemes
            if ((array[i] >= 48 && array[i] <= 57) ||
                    ((array[i] == '+' || array[i] == '-') && (i - 1 < 0) && (array[i + 1] >= 48 && array[i + 1] <= 57) ||
                        ((array[i] == '+' || array[i] == '-') && (i - 1 >= 0) && (array[i - 1] != ')') && ((array[i - 1] < 48 || array[i - 1] > 57) && (array[i - 1] < 65 || array[i - 1] > 90) && (array[i - 1] < 97 || array[i - 1] > 122)) && (array[i + 1] >= 48 && array[i + 1] <= 57)))){

                StringBuilder number = new StringBuilder();
                int breaker = 0;

                do {
                    if (i == array.length - 1) {
                        number.append(array[i]);
                        breaker = 1;
                    } else {
                        number.append(array[i]);
                        i++;
                    }
                }
                while ((array[i] >= 48 && array[i] <= 57 || array[i] == '.') && breaker != 1);

                tokens.add(new Token(Type.Number, number.toString(), STANDARD_PRIORITY));

                if(i != array.length - 1) i--;
            }else

            //adds '(' and ')' tokens as lexemes
            if (array[i] == ')') {
                tokens.add(new Token(Type.CloseParenthesis, String.valueOf(array[i]), STANDARD_PRIORITY));
            } else if (array[i] == '(') {
                tokens.add(new Token(Type.OpenParenthesis, String.valueOf(array[i]), STANDARD_PRIORITY));
            }
        }
        //add last element
        if(array[array.length - 1] >= 97 && array[array.length - 1] <= 122 || array[array.length - 1] >= 65 && array[array.length - 1] <= 90){
            tokens.add(new Token(Type.Variable, String.valueOf(array[array.length - 1]), STANDARD_PRIORITY));
        }else if(array[array.length - 1] == ')'){
            tokens.add(new Token(Type.CloseParenthesis, String.valueOf(array[array.length - 1]), STANDARD_PRIORITY));
        }else if(array[array.length - 1] == '('){
            tokens.add(new Token(Type.OpenParenthesis, String.valueOf(array[array.length - 1]), STANDARD_PRIORITY));
        }else if(array[array.length - 1] >= 48 && array[array.length - 1] <= 57){
            tokens.add(new Token(Type.Number, String.valueOf(array[array.length - 1]), STANDARD_PRIORITY));
        }
        return tokens;
    }
}
