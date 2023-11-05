package org.example;

import org.example.aplikacia.Priebeh;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class Main {
    public static Random r = new Random();
    public static int POCIATOCNA_VELKOST = 400;
    public static String SUBOR="SK2.txt";

    public static void main(String[] args) throws IOException {
        Instant starts = Instant.now();
        Text text=new Text(SUBOR);

        Priebeh priebeh=new Priebeh(text.getUpravenyText());
        priebeh.otestujRozneKluce(text.getKluce(),100,10);
        Instant ends = Instant.now();
        System.out.println(Duration.between(starts, ends));
    }

    public static String vygenerujKluc(int min, int max){
        int minI = min;
        int maxI = max;
        int minC = 'a';
        int maxC = 'z';

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < r.nextInt(maxI - minI) + minI; i++) {
            s.append((char) (r.nextInt(maxC - minC) + minC));
        }
        return s.toString();
    }


}