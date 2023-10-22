package org.example;

import lombok.Getter;


import java.util.*;
import java.util.stream.Collectors;

public class Invariant {
    @Getter
    private StringBuilder textSMedzerami;
    @Getter
    private StringBuilder textBezMedzier;
    @Getter
    private String[] vsetkySlova;

    public Invariant(StringBuilder OT, StringBuilder OTUpraveny) {
        this.textSMedzerami = OT;
        this.textBezMedzier = OTUpraveny;
        vsetkySlova = textSMedzerami.toString().split(" ");
    }
    protected List<Map.Entry<String, Integer>> ngramy(StringBuilder text, int n) {

        Map<String, Integer> ngramy = new HashMap<>();

        for (int i = 0; i < text.length() - n; i++) {
            String ngram=new String();
            for (int j = i; j < i+n; j++) {
                ngram+=text.charAt(j);
            }
            i++;
            ngramy.merge(ngram,1,Integer::sum);
        }

        List<Map.Entry<String, Integer>> vytriedeneNgramy = ngramy.entrySet()
                .stream()
                .sorted((vstup1, vstup2) -> vstup2.getValue().compareTo(vstup1.getValue()))
                .collect(Collectors.toList());

        int pocitadlo = 0;
        int kolko=10;
        for (Map.Entry<String, Integer> vstup : vytriedeneNgramy) {
            if(pocitadlo <  kolko){
              //  System.out.println(vstup.getKey() + ": " + vstup.getValue());
            }
            pocitadlo++ ;
        }
        return vytriedeneNgramy;
    }

    protected double priemernaDlzka(StringBuilder text){
        String[] slova = text.toString().split("\\s+");

        int pocetPismen = 0;
        int pocetSlov=slova.length;

        for (String slovo : slova) {
            pocetPismen+=slovo.length();
        }
        double priemer = (double) pocetPismen / pocetSlov;
        return priemer;
    }
    private double indexKoincidencie(String zasifrovanyText) {
        HashMap<Character, Integer> frekvencie = new HashMap<>();
        int pocetPismen = 0;

        for (char c = 'A'; c <= 'Z'; c++) {
            frekvencie.put(c, 0);
        }

        for (int i = 0; i < zasifrovanyText.length(); i++) {
            char c = Character.toUpperCase(zasifrovanyText.charAt(i));
            if (Character.isLetter(c)) {
                frekvencie.put(c, frekvencie.get(c) + 1);
                pocetPismen++;
            }
        }

        double index = 0.0;
        for (char c = 'A'; c <= 'Z'; c++) {
            int pom = frekvencie.get(c);
            index += (pom * (pom - 1.0)) ;
        }

        return (1.0/ (pocetPismen * (pocetPismen - 1.0)))*index;
    }


    /* Vyskyt jednotlivych samohlasok a spoluhlasok na zaciatku a konci slova */
    protected ArrayList<Map<Character,Integer>> spoluhlaskySamohlasky(StringBuilder text) {
        Map<Character, Integer> zaciatokSlova = new HashMap<>();

        Map<Character, Integer> koniecSlova = new HashMap<>();
        vsetkySlova = text.toString().split(" ");

        for (var slovo : vsetkySlova) {
            if(!slovo.isEmpty() && Character.isAlphabetic(slovo.charAt(0))){
                zaciatokSlova.merge(slovo.charAt(0), 1, Integer::sum);
            }
            if (slovo.length() > 2) {
                koniecSlova.merge(slovo.charAt(slovo.length() -1), 1, Integer::sum);
            }
        }
        for (Map.Entry<Character,Integer> vstup : zaciatokSlova.entrySet()) {
            System.out.println(vstup.getKey()+" "+vstup.getValue());
        }
        System.out.println();
        for (Map.Entry<Character,Integer> vstup : koniecSlova.entrySet()) {
            System.out.println(vstup.getKey()+" "+vstup.getValue());
        }
        return new ArrayList<>(List.of(zaciatokSlova,koniecSlova));
    }

}
