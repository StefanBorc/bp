package org.example;

import lombok.Getter;
import org.example.sifry.TabulkovaTranspozicia;
import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class PokusTabulkovaTranspozicia {
    public static int POCIATOCNA_VELKOST = 200;
    @Getter
    private StringBuilder upravenyText;
    @Getter
    private StringBuilder text;
    @Getter
    private StringBuilder zt;
    private TabulkovaTranspozicia transpozicia;
    private final ArrayList<String> vygenerovaneKluce;
    private Invariant invariant;
    private List<Map.Entry<String, Double>> statistikaBigramovOT;
    private Map<String,Double> statistikaBigramov;

    public PokusTabulkovaTranspozicia(String nazovSuboru, String kluc) throws IOException {

        vygenerovaneKluce = new ArrayList<>();
        vygenerujKluce(10);
        transpozicia = new TabulkovaTranspozicia(kluc);

        upravenyText = transpozicia.vratSuborU(nazovSuboru);
        text = transpozicia.vratSubor(nazovSuboru);

        invariant = new Invariant(text, upravenyText);

        spustiSifrovanie(kluc, POCIATOCNA_VELKOST);

        //transpoziciaPorovnajDlzkyKlucov(transpozicia.getZasifrovanyText());

        otestujDlzkuKluca();

    }

    private void spustiSifrovanie(String kluc, int n) {
        transpozicia.zasifrujText(upravenyText, kluc, n);
        zt = transpozicia.getZasifrovanyText();
        spravStatistikuOT();
    }
    private void spravStatistikuOT(){
        statistikaBigramovOT = invariant.getStatistikaBigramovOT();
        statistikaBigramov=new HashMap<>();
        for(var bigram:statistikaBigramovOT){
            statistikaBigramov.put(bigram.getKey(),bigram.getValue());
        }
    }
    private void vygenerujKluce(int n) {
        Map<String, Integer> kluce = new HashMap<>();
        Random r = new Random();
        int minI = 10;
        int maxI = 30;
        int minC = 'a';
        int maxC = 'z';
        while (kluce.size() != n) {
            StringBuilder s = new StringBuilder("");
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

    /* skusame rozne pravdepodobne bigramy */
    protected int zhodnostBigramov(StringBuilder zasifrovanyText, int oKolko) {
        ArrayList<Character> spoluhlasky = new ArrayList<>(List.of('B', 'C', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'F', 'Z', 'X'));
        ArrayList<Character> samohlasky = new ArrayList<>(List.of('A', 'E', 'I', 'O', 'U', 'Y'));

        Map<String, Integer> najdene = new HashMap<>();
        for (int i = 0; i < zasifrovanyText.length() - oKolko * 2; i++) {

            String ngram = new String();
            ngram += zasifrovanyText.charAt(i);
            int j = i + oKolko;
            ngram += zasifrovanyText.charAt(j);
            if ((spoluhlasky.contains(ngram.charAt(0)) && samohlasky.contains(ngram.charAt(1)))
                    || (samohlasky.contains(ngram.charAt(0)) && spoluhlasky.contains(ngram.charAt(1)))) {
                najdene.merge(ngram, 1, Integer::sum);
            }
        }
        List<Map.Entry<String, Integer>> vytriedeneNgramy = najdene.entrySet()
                .stream()
                .sorted((vstup1, vstup2) -> vstup2.getValue().compareTo(vstup1.getValue()))
                .collect(Collectors.toList());

        return vytriedeneNgramy.get(0).getValue();
    }

    /* porovnavame najlepsie vysledky klucov */
    private int transpoziciaPorovnajDlzkyKlucov(StringBuilder zt) {
        Map<Integer, Integer> mozneKluce = new HashMap<>();
        int maxIndex = 0;
        int max = 0;
        for (int i = 10; i <= 30; i++) {
            mozneKluce.put(i, zhodnostBigramov(zt, i));
        }

        for (Map.Entry<Integer, Integer> vstup : mozneKluce.entrySet()) {
            if (vstup.getValue() > max) {
                max = vstup.getValue();
                maxIndex = vstup.getKey();
            }
        }
        System.out.println(maxIndex);
        return maxIndex;
    }

    /* testujeme uspesnost klucov pre tabulkovu transpoziciu */
    private void transpozicnaUspesnostKlucov() {

        double pocetUhadnutychKlucov = 0;
        double pocetVsetkychKlucov = vygenerovaneKluce.size();

        for (String s : vygenerovaneKluce) {
            int dlzkaKluca = s.length();
            transpozicia.setKluc(s);
            transpozicia.sifrovanie(upravenyText, s);
            int moznyKluc = transpoziciaPorovnajDlzkyKlucov(transpozicia.getZasifrovanyText());
            System.out.println("kluc " + transpozicia.getKluc().length());
            System.out.println("odhad " + moznyKluc);
            if (dlzkaKluca == moznyKluc) {
                pocetUhadnutychKlucov++;
            }


        }
        double vysledok = (pocetUhadnutychKlucov / pocetVsetkychKlucov) * 100;

        System.out.println(vysledok);
    }


    private StringBuilder premenBlokyNaText(ArrayList<StringBuilder> bloky, int n1, int n2) {
        int pocetRiadkov = bloky.get(0).length();
        int pocetStlpcov = 2;
        int[] stlpce = new int[]{n1, n2};
        char[][] tabulka = new char[pocetRiadkov][2];
        for (int i = 0; i < pocetStlpcov; i++) {
            for (int j = 0; j < pocetRiadkov; j++) {
                if (bloky.get(stlpce[i]).length() > j) {
                    tabulka[j][i] = bloky.get(stlpce[i]).charAt(j);
                }
            }
        }
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < pocetRiadkov; i++) {
            for (int j = 0; j < pocetStlpcov; j++) {
                text.append(tabulka[i][j]);
            }
        }
        return text;
    }

    private List<Map.Entry<String, Double>> spravStatistikuBigramov(ArrayList<StringBuilder> bloky) {
        List<Map.Entry<String, Double>> vyskytBigramov=new ArrayList<>();

        Map<String,Double> statistikaZT = new HashMap<>();

        ArrayList<Map<String,Double>> bigramyVelkostiKluca=new ArrayList<>();
        ArrayList<Integer[]> mozneKombinacie=new ArrayList<>();

        double vaha=0.75;

        for(int prvy=0;prvy<bloky.size();prvy++) {
            for (int druhy = 0; druhy < bloky.size(); druhy++) {
                if (prvy != druhy) {
                    StringBuilder text = premenBlokyNaText(bloky, prvy, druhy);
                    vyskytBigramov = invariant.ngramy(text, 2, false);
                    int pocitadlo=0;
                    for(int bigram=0;bigram<10;bigram++){
                        if(statistikaBigramov.get(vyskytBigramov.get(bigram).getKey())!=null){
                            if(statistikaBigramov.get(vyskytBigramov.get(bigram).getKey())<vaha){
                                pocitadlo++;
                            }
                        }
                    }
                    if(pocitadlo>3){
                        //  System.out.println("nie je kombinacia ");
                    }
                    else{
                        mozneKombinacie.add(new Integer[]{prvy,druhy});
                        System.out.println(prvy);
                        System.out.println(druhy);
                        bigramyVelkostiKluca.add(statistikaBigramov);
                        System.out.println("odhadovana dlzka: "+bloky.size());
                    }
                }
            }
        }

        return vyskytBigramov;
    }

    private void otestujDlzkuKluca() {
        ArrayList<StringBuilder> riadky;
        ArrayList<List<Map.Entry<String, Double>>> statistika = new ArrayList<>();
        int n = zt.length();

        for (int i = 0; i < 30; i++) {
            if (i < 5) {
                statistika.add(new ArrayList<>());
                continue;
            }
            int pZnakovVRiadku = n / i;
            int zvysok = n % i;
            int zvysokPreRiadok = 0;
            if (zvysok != 0) {
                zvysokPreRiadok = 1;
                zvysok--;
            }
            riadky = new ArrayList<>();
            StringBuilder riadok = new StringBuilder();
            for (int j = 0; j <= zt.length(); j++) {
                if (riadok.length() == pZnakovVRiadku + zvysokPreRiadok) {
                    riadky.add(riadok);
                    riadok = new StringBuilder();
                    if (zvysok > 0) {
                        zvysok--;
                    } else {
                        zvysokPreRiadok = 0;
                    }
                    if (zt.length() > j) {
                        riadok.append(zt.charAt(j));
                    }
                } else {
                    if (zt.length() > j) {
                        riadok.append(zt.charAt(j));
                    }
                }
            }
            statistika.add(spravStatistikuBigramov(riadky));
        }
        System.out.println();
    }
}
