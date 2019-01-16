package PZKS.Opening;

import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {
    public List<Token1> analyze(String input) throws LexicalException {
        String temp = input.replace(" ", "");
        char[] charbuf = temp.toCharArray();
        List<Token1> list = new ArrayList<Token1>();
        Context context = new Context();
        AnalyzerStates state = AnalyzerStates.BEGIN;
        for (int i = 0; i < charbuf.length; i++) {
            state = state.next(charbuf[i], context, list);
            if (state == AnalyzerStates.ERROR) {
                throw new LexicalException(context.getContext(), i);
            }
        }
        if (state != AnalyzerStates.EXITSTATE) {
            throw new LexicalException("Неправильный конец выражения, ожидаеться ;.", charbuf.length);
        }
        if (context.getCounter() != 0) {
            throw new LexicalException("Несогласованые скобки.", charbuf.length);
        }
        return list;
    }

    public List<Token1> toPostfix(List<Token1> inlist) {
        List<Token1> list = new ArrayList<Token1>();
        List<Token1> stack = new ArrayList<Token1>();
        for (Token1 token : inlist) {
            if (token.getType() == TypeOfToken.VARIABLE || token.getType() == TypeOfToken.CONSTANT) {
                list.add(0, token);
            } else {
                if (token.getType() == TypeOfToken.OPENBRACKET) {
                    stack.add(0, token);
                } else {
                    if (token.getType() == TypeOfToken.CLOSEBRACKET) {
                        while (stack.size() != 0 && stack.get(0).getType() != TypeOfToken.OPENBRACKET) {
                            list.add(0, stack.remove(0));
                        }
                        if (stack.size() != 0) {
                            stack.remove(0);
                        }
                    } else {
                        while (stack.size() != 0 && token.getType().priority() <= stack.get(0).getType().priority()) {
                            list.add(0, stack.remove(0));
                        }
                        stack.add(0, token);
                    }
                }
            }
        }
        while (stack.size() != 0) {
            list.add(0, stack.remove(0));
        }
        return list;
    }
}
