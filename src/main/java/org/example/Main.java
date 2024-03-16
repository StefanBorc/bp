package org.example;

import org.example.aplikacia.Aplikacia;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    public static Random r = new Random();

    public static void main(String[] args) throws Exception {
        //long startTime = System.currentTimeMillis();

        new Aplikacia();
        /*
        Text t=new Text("CZ");
        Priebeh priebeh=new Priebeh(100, Jazyk.CZ,t.getTextyNaSifrovanie().get(0),t.getUpravenyText());
        priebeh.otestovatKorpus(t.getTextyNaSifrovanie().get(0),t.getKluce(),t.getKluce().size());
         */
        //   System.out.println("Elapsed Time: " + minutes + " minutes and " + seconds + " seconds");
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
    private static ArrayList<String> vygenerujKluce(int n, String dir) throws IOException {
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
        if(dir!=null){
            for (Map.Entry<String, Integer> vstup : kluce.entrySet()) {
                String kluc = vstup.getKey();
                wr.write(kluc);
                wr.write("\n");
            }
            wr.close();
        }
        return (ArrayList<String>) kluce;

    }


}