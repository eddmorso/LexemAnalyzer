package PZKS;

import javax.swing.*;
import java.awt.*;

public class Main {
    static String inputString = "(2+m)*(a+9/b-c);";
/*
    розкрити дужки
            конвеєр з постійним тактом
*/

    public static void main(String [] args) {
        System.out.println(inputString);
        System.out.println();

        Opener opener = new Opener();
        opener.doOpening();

    }
}
