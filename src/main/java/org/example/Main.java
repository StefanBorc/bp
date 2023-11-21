package org.example;

import org.example.lustenie_jednoduchej_substitucie.Priebeh;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    public static Random r = new Random();
    public static int POCIATOCNA_VELKOST = 100;
    public static String SUBOR="CZ";

    public static void main(String[] args) throws IOException {
        Text text = new Text(SUBOR);
        /*
        System.out.println("CZ");
        Priebeh priebeh = new Priebeh(text.getTextyNaSifrovanie().get(1), text.getUpravenyText());
        priebeh.otestujRozneKluce(text.getKluce());
        */
        Priebeh priebeh=new Priebeh(text.getUpravenyText());

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
    private static void vygenerujKluce(int n) throws IOException {
        FileWriter wr = new FileWriter("KLUCE.txt");
        Map<String, Integer> kluce = new HashMap<>();
        Random r = new Random();
        int minI = 10;
        int maxI = 30;
        int minC = 'a';
        int maxC = 'z';
        while (kluce.size() != n) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < r.nextInt(maxI - minI) + minI; i++) {
                s.append((char) (r.nextInt(maxC - minC) + minC));
            }
            kluce.merge(s.toString(), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> vstup : kluce.entrySet()) {
            String kluc = vstup.getKey();
            wr.write(kluc);
            wr.write("\n");
        }
        wr.close();

    }


}