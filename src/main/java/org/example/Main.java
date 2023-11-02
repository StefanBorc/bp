package org.example;

import org.example.aplikacia.Priebeh;

import java.io.IOException;

import java.util.Random;

public class Main {

    public static int POCIATOCNA_VELKOST = 300;
    public static String SUBOR="EN2.txt";

    public static void main(String[] args) throws IOException {
        Text text=new Text(SUBOR);
        new Priebeh(text.getUpravenyText());
    }

    public static String vygenerujKluc(){
        Random r = new Random();
        int minI = 10;
        int maxI = 20;
        int minC = 'a';
        int maxC = 'z';

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < r.nextInt(maxI - minI) + minI; i++) {
            s.append((char) (r.nextInt(maxC - minC) + minC));
        }
        return s.toString();
    }


}