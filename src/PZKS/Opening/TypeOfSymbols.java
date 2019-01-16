package PZKS.Opening;

public enum TypeOfSymbols {
    LETTER,
    DIGIT,
    DOT,
    MINUS,
    OPERATOR,
    OPENBRACKET,
    CLOSEBRACKET,
    EOF;

    public static TypeOfSymbols getTypeOFSymbols(char c){
        TypeOfSymbols result = null;
        if((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')){
            result = LETTER;
        }else{
            if(c >= '0' && c <= '9'){
                result = DIGIT;
            }else{
                switch(c){
                    case '.':
                        result = DOT;
                        break;
                    case '-':
                        result = MINUS;
                        break;
                    case '+':
                    case '*':
                    case '/':
                        result = OPERATOR;
                        break;
                    case '(':
                        result = OPENBRACKET;
                        break;
                    case ')':
                        result = CLOSEBRACKET;
                        break;
                    case ';':
                        result = EOF;
                        break;
                }
            }
        }
        return result;
    }
}

