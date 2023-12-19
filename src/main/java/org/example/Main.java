package org.example;

import org.example.lustenie_tabulkovej_transpozicie.Priebeh;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    public static Random r = new Random();
    public static String SUBOR="CZ";

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        //skusit naprogramovat aby to zobralo 2 najlepsie kombinacie a podla trigramov ich vysortovalo
        Text text = new Text(SUBOR);
        System.out.println(SUBOR.toString());
        Priebeh priebeh = new Priebeh(text.getTextyNaSifrovanie().get(0), text.getUpravenyText());
        priebeh.statistikaKorpusov(text.getKluce(),text.getTextyNaSifrovanie());
        SUBOR="SK";
        text = new Text(SUBOR);
        System.out.println(SUBOR.toString());
        priebeh = new Priebeh(text.getTextyNaSifrovanie().get(0), text.getUpravenyText());
        priebeh.statistikaKorpusov(text.getKluce(),text.getTextyNaSifrovanie());
        SUBOR="DE";
        text = new Text(SUBOR);
        System.out.println(SUBOR.toString());
        priebeh = new Priebeh(text.getTextyNaSifrovanie().get(0), text.getUpravenyText());
        priebeh.statistikaKorpusov(text.getKluce(),text.getTextyNaSifrovanie());
        SUBOR="EN";
        text = new Text(SUBOR);
        System.out.println(SUBOR.toString());
        priebeh = new Priebeh(text.getTextyNaSifrovanie().get(0), text.getUpravenyText());
        priebeh.statistikaKorpusov(text.getKluce(),text.getTextyNaSifrovanie());


        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        long minutes = (elapsedTime / 1000) / 60;
        long seconds = (elapsedTime / 1000) % 60;
        System.out.println("Elapsed Time: " + minutes + " minutes and " + seconds + " seconds");
        /*
        String kluc="dskaodaodakddsadsad";
        System.out.println(kluc.length());
        priebeh.otestujKlucPermutaciu(200,kluc);
*/

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