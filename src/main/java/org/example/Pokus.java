package org.example;

import lombok.Getter;
import org.example.sifry.JednoduchaSubstitucia;
import org.example.sifry.TabulkovaTranspozicia;
import org.example.sifry.Vigenere;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Pokus {
    public static final int INT_MAX = 999 ;
    public static final int INT_MIN = -999;
    private String nazovSuboru;
    @Getter
    private StringBuilder upravenyText;
    @Getter
    private StringBuilder text;
    private TabulkovaTranspozicia t;
    private Vigenere v;
    private JednoduchaSubstitucia s;
    private ArrayList<String> vygenerovaneKluce;
    private String prednastavenyKluc;

    public Pokus(String nazovSuboru,String kluc) throws IOException {
        this.nazovSuboru = nazovSuboru;
        this.prednastavenyKluc = kluc;
        vygenerovaneKluce = new ArrayList<>();
        s = new JednoduchaSubstitucia('C', kluc);
        t = new TabulkovaTranspozicia(kluc);
        v = new Vigenere(kluc);
        vygenerujKluce(10);
        upravenyText = new StringBuilder();
        sifrujTexty();
    }
    private void vygenerujKluce(int n){
        Map<String,Integer> kluce=new HashMap<>();
        Random r=new Random();
        int minI = 10;
        int maxI = 30;
        int minC = 'a';
        int maxC = 'z';
        while(kluce.size()!=n){
            String s= new String();
            for(int i=0;i<r.nextInt(maxI-minI) + minI;i++){
                s+=(char)(r.nextInt(maxC-minC) + minC);
            }
            kluce.merge(s,1,Integer::sum);
        }
        for ( Map.Entry<String, Integer> vstup : kluce.entrySet()) {
            String kluc = vstup.getKey();
            vygenerovaneKluce.add(kluc);
        }

    }
    private void sifrujTexty() throws IOException {
        upravenyText = s.vratSuborU(nazovSuboru);
        text = s.vratSubor(nazovSuboru);
        s.sifrovanie(upravenyText,prednastavenyKluc);
        t.sifrovanie(upravenyText,prednastavenyKluc);
    }

    private void otestujPattern(StringBuilder text, int n) {
        Map<String, Integer> paterny = new HashMap<>();

        for (int i = 0; i < text.length() - n; i++) {
            String patern=new String();
            for (int j = i; j < i+n; j++) {
                patern+=text.charAt(j);
            }
            if(n==3){
                if(patern.charAt(0) == patern.charAt(2)){
                    paterny.merge(patern,1,Integer::sum);
                }

            }
            else if(n==4){
                if(patern.charAt(1) == patern.charAt(2)){
                    paterny.merge(patern,1,Integer::sum);
                }
            }

        }

        List<Map.Entry<String, Integer>> vytriedeneNgramy = paterny.entrySet()
                .stream()
                .sorted((vstup1, vstup2) -> vstup2.getValue().compareTo(vstup1.getValue()))
                .collect(Collectors.toList());
        int pocitadlo = 0;
        for (Map.Entry<String, Integer> vstup : vytriedeneNgramy) {
            if(pocitadlo <  20){
                System.out.println(vstup.getKey() + ": " + vstup.getValue());
            }
            pocitadlo++ ;
        }
    }
    private double vigenerePriemerDlzka(StringBuilder zt, int oKolko,double priemernaDlzka){
        Map<Character,Integer> pismena=new HashMap<>();
        double najviacVyskytovane=0;
        int pocet=0;
        for(int i=0;i<zt.length()-oKolko;i+=oKolko){
            pismena.merge(zt.charAt(i),1,Integer::sum);
            pocet++;
        }

        List<Map.Entry<Character, Integer>> f = pismena.entrySet()
                .stream()
                .sorted((vstup1, vstup2) -> vstup2.getKey().compareTo(vstup1.getKey()))
                .collect(Collectors.toList());
        double p=f.get(0).getValue();
        double c=(p/pocet)*100;
        int x=v.getKluc().length();
        return c/priemernaDlzka;
    }
    private int[] vigenereNajdiMozneKluce(StringBuilder zt,double priemernaDlzka){
        int d=0;
        double max = INT_MIN;
        double min = INT_MAX;
        int moznyKluc1 = 0;
        int moznyKluc2 = 0;
        for(int i=10;i<=30;i++) {
            var pomocna=vigenerePriemerDlzka(zt,i,priemernaDlzka);
            if(pomocna>max){
                max=pomocna;
                moznyKluc1=i;
            }
            if(pomocna<min){
                min=pomocna;
                moznyKluc2=i;
            }

        }
        return new int[]{moznyKluc2,moznyKluc1};
    }
    private void vigenereUspesnostKlucov(double priemernaDlzka){

        double pocetUhadnutychKlucov=0;
        double pocetVsetkychKlucov=vygenerovaneKluce.size();

        for(int i=0;i<vygenerovaneKluce.size();i++){
            int [] mozneKluce=new int[2];
            int dlzkaKluca=vygenerovaneKluce.get(i).length();
            v.sifrovanie(upravenyText,vygenerovaneKluce.get(i));
            v.setKluc(vygenerovaneKluce.get(i));
            mozneKluce=vigenereNajdiMozneKluce(v.getZasifrovanyText(),priemernaDlzka);
            if(dlzkaKluca==mozneKluce[0] || dlzkaKluca==mozneKluce[1]){
                pocetUhadnutychKlucov++;
            }

        }
        double vysledok=(pocetUhadnutychKlucov/pocetVsetkychKlucov)*100;
        int k=0;
        System.out.println(vysledok);
    }
    /* Skusanie roznych dlziek z ktoreho sa da ziskat dlzka kluca na zaklade bigramov  */
    protected int zhodnostBigramov(StringBuilder zasifrovanyText,int oKolko){
        ArrayList<Character> spoluhlasky=new ArrayList<>(List.of('B','C','D','F','G','H','J','K','L','M','N','P','Q','R','S','T','V','F','Z','X'));
        ArrayList<Character> samohlasky=new ArrayList<>(List.of('A','E','I','O','U','Y'));

        Map<String, Integer> najdene = new HashMap<>();
        for (int i=0;i<zasifrovanyText.length()-oKolko*2;i++){

            String ngram = new String();
            ngram+=zasifrovanyText.charAt(i);
            int j=i+oKolko;
            ngram+=zasifrovanyText.charAt(j);
            if((spoluhlasky.contains(ngram.charAt(0)) && samohlasky.contains(ngram.charAt(1)))
                    || (samohlasky.contains(ngram.charAt(0)) && spoluhlasky.contains(ngram.charAt(1)))) {
                najdene.merge(ngram,1,Integer::sum);
            }
        }
        List<Map.Entry<String, Integer>> vytriedeneNgramy = najdene.entrySet()
                .stream()
                .sorted((vstup1, vstup2) -> vstup2.getValue().compareTo(vstup1.getValue()))
                .collect(Collectors.toList());

        return vytriedeneNgramy.get(0).getValue();
    }
    private int transpoziciaPorovnajDlzkyKlucov(StringBuilder zt){
        Map<Integer,Integer> mozneKluce=new HashMap<>();
        int maxIndex=0;
        int max=0;
        for(int i=10;i<=30;i++){
            mozneKluce.put(i,zhodnostBigramov(zt,i));
        }

        for(Map.Entry<Integer,Integer> vstup : mozneKluce.entrySet()){
            if(vstup.getValue()>max){
                max=vstup.getValue();
                maxIndex=vstup.getKey();
            }
        }
        System.out.println(maxIndex);
        return maxIndex;
    }
    private void transpozicnaUspesnostKlucov(){

        double pocetUhadnutychKlucov=0;
        double pocetVsetkychKlucov=vygenerovaneKluce.size();

        for(int i=0;i<vygenerovaneKluce.size();i++){

            int dlzkaKluca=vygenerovaneKluce.get(i).length();
            t.setKluc(vygenerovaneKluce.get(i));
            t.sifrovanie(upravenyText,vygenerovaneKluce.get(i));
            int moznyKluc=transpoziciaPorovnajDlzkyKlucov(t.getZasifrovanyText());
            System.out.println("kluc "+t.getKluc().length());
            System.out.println("odhad "+moznyKluc);
            if(dlzkaKluca==moznyKluc){
                pocetUhadnutychKlucov++;
            }


        }
        double vysledok=(pocetUhadnutychKlucov/pocetVsetkychKlucov)*100;
        int k=0;
        System.out.println(vysledok);
    }



}
