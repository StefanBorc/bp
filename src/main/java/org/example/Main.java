package org.example;

import org.example.aplikacia.Priebeh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    public static Random r = new Random();
    public static int POCIATOCNA_VELKOST = 100;
    public static String SUBOR="CZ1.txt";

    public static void main(String[] args) throws IOException {
        long start =System.currentTimeMillis();
        Text text=new Text(SUBOR);
        for(int i=POCIATOCNA_VELKOST;i<=1000;i+=100){
            POCIATOCNA_VELKOST=i;
            Priebeh priebeh=new Priebeh(text.getTextNaSifrovanie(),text.getUpravenyText());
            priebeh.otestujRozneKluce(text.getKluce());
        }
        long end=System.currentTimeMillis();
        System.out.println((end-start)/(1000*60));
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
    private void vygenerujKluce(int n) {
        ArrayList<String> vygenerovaneKluce=new ArrayList<>();
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
            vygenerovaneKluce.add(kluc);
        }

    }


}