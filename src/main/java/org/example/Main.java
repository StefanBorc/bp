package org.example;

import org.example.lustenie_jednoduchej_substitucie.LustenieJednoduchejSubstitucie;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    public static Random r = new Random();
    public static int POCIATOCNA_VELKOST = 100;
    public static String SUBOR="CZ";

    public static void main(String[] args) throws IOException {
        Text text = new Text(SUBOR);

        ArrayList<String> kluce=text.nacitajKluce("KLUCESUB");
        /*
        System.out.println("CZ");
        LustenieTabulkovejTranspozicie priebeh = new LustenieTabulkovejTranspozicie(text.getTextyNaSifrovanie().get(0), text.getUpravenyText());
        priebeh.otestujKorpusy(text.getKluce(),text.getTextyNaSifrovanie());
        */
        LustenieJednoduchejSubstitucie priebeh1=new LustenieJednoduchejSubstitucie(text.getUpravenyText(),text.getTextyNaSifrovanie().get(0));
        priebeh1.otestujKluce(kluce);
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
    private static void vygenerujKluce(int n,String dir) throws IOException {
        FileWriter wr = new FileWriter(dir+".txt");
        Map<String, Integer> kluce = new HashMap<>();
        Random r = new Random();
        int minI = 5;
        int maxI = 25;
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