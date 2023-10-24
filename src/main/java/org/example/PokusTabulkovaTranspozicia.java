package org.example;

import lombok.Getter;
import org.example.sifry.TabulkovaTranspozicia;


import java.io.*;
import java.util.*;


public class PokusTabulkovaTranspozicia {
    public static int POCIATOCNA_VELKOST = 800;
    @Getter
    private StringBuilder upravenyText;
    @Getter
    private StringBuilder text;
    @Getter
    private StringBuilder zt;
    private TabulkovaTranspozicia transpozicia;
    private final ArrayList<String> vygenerovaneKluce;
    private Invariant invariant;
    private List<Map.Entry<String, Double>> statistikaOTUsporiadana;
    private Map<String,Double> statistikaBigramov;
    private ArrayList<String> topDobrych;
    private ArrayList<String> topZlych;
    public PokusTabulkovaTranspozicia(String nazovSuboru, String kluc) throws IOException {
        vygenerovaneKluce = new ArrayList<>();
        vygenerujKluce(10);
        transpozicia = new TabulkovaTranspozicia(kluc);

        upravenyText = transpozicia.vratSuborU(nazovSuboru);
        text = transpozicia.vratSubor(nazovSuboru);

        invariant = new Invariant(text, upravenyText);

        spustiSifrovanie(kluc, POCIATOCNA_VELKOST);

        topDobrych =new ArrayList<>();
        for(int i=0;i<20;i++){
            topDobrych.add(statistikaOTUsporiadana.get(i).getKey());
        }
        topZlych=new ArrayList<>();
        for(int i=statistikaBigramov.size()-1;i>=statistikaBigramov.size()-300;i--){
            topZlych.add(statistikaOTUsporiadana.get(i).getKey());
        }
        //transpoziciaPorovnajDlzkyKlucov(transpozicia.getZasifrovanyText());

        otestujDlzkuKluca();

    }
    private void spustiSifrovanie(String kluc, int n) {
        transpozicia.zasifrujText(upravenyText, kluc, n);
        zt = transpozicia.getZasifrovanyText();
        spravStatistikuOT();
    }
    private void spravStatistikuOT(){
        statistikaOTUsporiadana = invariant.getStatistikaBigramovUsporiadana();
        statistikaBigramov=invariant.getSkumanaMapa();
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
    private void najdiSpravnyStlpec(ArrayList<Double[]> usporiadaneBigramyPodlaOt,ArrayList<Integer[]> mozneKombinacie,double moznaOdchylka){

        ArrayList<Double> odchylky=new ArrayList<>(usporiadaneBigramyPodlaOt.size());
        ArrayList<Double> pocetMaloPravdepodobnych=new ArrayList<>();
        int indexBigramov=0;

        for(var bigramy:usporiadaneBigramyPodlaOt){
            int index=0;
            int indexOdzadu=bigramy.length-1;
            double odchylkyBigramov=0.0;
            double maloPravdepodobnych=0;
            for(var bigram:bigramy){

                double odchylka;
                if(index<50){
                    odchylka=Math.abs(statistikaOTUsporiadana.get(index).getValue()-bigram);
                    System.out.println();
                    if(odchylka>moznaOdchylka){
                        if(odchylka==statistikaOTUsporiadana.get(index).getValue()){
                            odchylkyBigramov+=10;
                        }
                    }
                    odchylkyBigramov+=odchylka;
                }
                if(index<300){
                    if(bigramy[indexOdzadu]>0.0){
                        maloPravdepodobnych+=bigramy[indexOdzadu]*indexOdzadu;
                    }
                }
                else{
                    break;
                }

                index++;
                indexOdzadu--;
            }

            indexBigramov++;
            odchylky.add(odchylkyBigramov);
            pocetMaloPravdepodobnych.add(maloPravdepodobnych);
            System.out.println();
        }
        System.out.println();
    }
    private ArrayList<Double[]> usporiadajPodlaOT(ArrayList<List<Map.Entry<String,Double>>> mapy){
        Map<String,Integer> indexyMapy=invariant.vratIndexyUsporiadanejMapy(statistikaOTUsporiadana);
        ArrayList<Double[]> listyPercient=new ArrayList<>(mapy.size());
        for(var mapa : mapy){
            Double[] usporiadanePercenta=new Double[statistikaBigramov.size()];
            Arrays.fill(usporiadanePercenta, 0.0);
            for(var bigram:mapa){
                int index=-1;
                if (indexyMapy.containsKey(bigram.getKey())) {
                     index = indexyMapy.get(bigram.getKey());
                     usporiadanePercenta[index]=bigram.getValue();
                }
            }
            listyPercient.add(usporiadanePercenta);
        }
        return listyPercient;
    }
    /* toto treba dorobit podrobnu analyzu maximalnych hodnot z predpokladanych statistik */

    private List<Map.Entry<String, Double>> spravStatistikuBigramov(ArrayList<StringBuilder> bloky) {
        List<Map.Entry<String, Double>> vyskytBigramov=new ArrayList<>();
        ArrayList<List<Map.Entry<String,Double>>> bigramyVelkostiKluca=new ArrayList<>();
        ArrayList<Integer[]> mozneKombinacie=new ArrayList<>();

        for(int prvy=0;prvy<bloky.size();prvy++) {

            for (int druhy = 0; druhy < bloky.size(); druhy++) {
                if (prvy != druhy) {
                    StringBuilder text = premenBlokyNaText(bloky, prvy, druhy);
                    vyskytBigramov = invariant.ngramy(text, 2, false);
                    int sucet=0;
                    int pocitadlo=0;
                    double percentaBigramu;
                    int velkostPorovnania=20;

                    for(int bigram=0;bigram<velkostPorovnania;bigram++){
                        if(statistikaBigramov.get(vyskytBigramov.get(bigram).getKey())!=null){
                            percentaBigramu=statistikaBigramov.get(vyskytBigramov.get(bigram).getKey());

                            if(topZlych.contains(vyskytBigramov.get(bigram).getKey())){
                                if(topZlych.indexOf(vyskytBigramov.get(bigram).getKey())> statistikaOTUsporiadana.size()-300){
                                    pocitadlo+=1*(velkostPorovnania-bigram);
                                }
                                pocitadlo++;
                            }
                            else if(topDobrych.contains(vyskytBigramov.get(bigram).getKey())){
                                int index=topDobrych.indexOf(vyskytBigramov.get(bigram).getKey());
                                if(index<5){
                                    sucet+=5*(velkostPorovnania-bigram);
                                }
                                else if(index<10){
                                    sucet+=3*(velkostPorovnania-bigram);
                                }
                                else{
                                    sucet+=2*(velkostPorovnania-bigram);
                                }
                            }
                        }
                    }
                    if(sucet>9 && pocitadlo<5){
                        mozneKombinacie.add(new Integer[]{prvy,druhy});
                        bigramyVelkostiKluca.add(vyskytBigramov);
                      //System.out.println("["+prvy+" , "+druhy+"]"+" : "+sucet+" , odhad: "+bloky.size());
                    }
                }
            }
        }

        if(!mozneKombinacie.isEmpty()) {
            System.out.println();
            System.out.println(bloky.size());
           // System.out.println(najvacsiePocty);
            for (var permutacia:mozneKombinacie) {
                System.out.print("[ ");
                for(var prvok:permutacia){
                    System.out.print(prvok+" ");
                }
                System.out.print("] ");
            }
            ArrayList<Double[]> usporiadanePodlaOT=usporiadajPodlaOT(bigramyVelkostiKluca);
            najdiSpravnyStlpec(usporiadanePodlaOT,mozneKombinacie,0.6);
            System.out.println();
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
