package PZKS;

public class Main {
    static String inputString = "(a+b)*(7+g+h/4)";
/*
    розкрити дужки
            конвеєр з постійним тактом
*/

    public static void main(String [] args) {
        System.out.println(inputString);
        System.out.println();

        Opener opener = new Opener(inputString);
        opener.printResult();

    }
}
