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
    private double indexkoincidencie(String zasifrovanyText) {
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

    /* Slova ktore sa najcastejsie na seba viazu */
    protected Map<String, Integer> viazaneSlova(StringBuilder text) {
        Map<String, Integer> slova = new HashMap<>();
        vsetkySlova = text.toString().split(" ");

        for (var slovo : vsetkySlova) {
            slova.merge(slovo, 1, Integer::sum);
        }
        List<Map.Entry<String, Integer>> najcastejsieSlova = slova.entrySet().stream().sorted((vstup1, vstup2) -> vstup2.getValue().compareTo(vstup1.getValue())).collect(Collectors.toList());

        najcastejsieSlova.remove(0);

        while (najcastejsieSlova.size() != 10) {
            najcastejsieSlova.remove(10);
        }

        ArrayList<String> desatNajcastejsichSlov = new ArrayList<>();
        ArrayList<Map<String, Integer>> viazaneSlova = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            desatNajcastejsichSlov.add(najcastejsieSlova.get(i).getKey());
            viazaneSlova.add(new HashMap<>());
        }

        for (int i = 0; i < vsetkySlova.length - 1; i++) {
            if (desatNajcastejsichSlov.contains(vsetkySlova[i])) {
                if (!vsetkySlova[i + 1].equals("")) {
                    viazaneSlova.get(desatNajcastejsichSlov.indexOf(vsetkySlova[i])).merge(vsetkySlova[i + 1], 1, Integer::sum);
                }
            }
        }

        ArrayList<ArrayList<String>> najcastejsieSpojene = new ArrayList<>();
        ArrayList<ArrayList<Integer>> najcastejsieSpojenePocet = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            najcastejsieSpojene.add(new ArrayList<>());
            najcastejsieSpojenePocet.add(new ArrayList<>());
            List<Map.Entry<String, Integer>> d = viazaneSlova.get(i).entrySet().stream().sorted((vstup1, vstup2) -> vstup2.getValue().compareTo(vstup1.getValue())).collect(Collectors.toList());
            for (int j = 0; j < 5; j++) {
                najcastejsieSpojene.get(i).add(d.get(j).getKey());
                najcastejsieSpojenePocet.get(i).add(d.get(j).getValue());
            }
        }
        for (int i = 0; i < najcastejsieSpojene.size(); i++) {
            System.out.println();
            System.out.println(najcastejsieSlova.get(i).getKey() + "(" + najcastejsieSlova.get(i).getValue() + ")");

            for (int j = 0; j < 5; j++) {
                System.out.println(najcastejsieSpojene.get(i).get(j) + ": " + najcastejsieSpojenePocet.get(i).get(j));
            }
        }

        return slova;

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

    /*  Po kolkych znakoch sa opakuju jednotlive pismena  ,*/
    protected void vzdialenostiMedziPismenami(StringBuilder text) {

        ArrayList<Map<Integer,Integer>> rozmedzia= new ArrayList<>();
        for(var c='A';c<='Z';c++){
            rozmedzia.add(new HashMap<>());
            for (int i=0;i<text.length();i++) {
                int pocitadlo=0;
                if(text.charAt(i)==c){
                    int index=i+1;

                    while(index<text.length()  && text.charAt(index)!=c ){
                        pocitadlo++;
                        index++;
                        if(index>text.length()){
                            pocitadlo=-1;
                            break;
                        }
                    }
                    i=index;
                    rozmedzia.get(c-65).merge(pocitadlo,1,Integer::sum);
                }
            }
        }
        ArrayList<List<Map.Entry<Integer, Integer>>> utriedeneRozmedzia= new ArrayList<>();
        for(int i=0;i<rozmedzia.size();i++){
           utriedeneRozmedzia.add(rozmedzia.get(i).entrySet().stream().sorted((vstup1, vstup2) -> vstup2.getValue().compareTo(vstup1.getValue())).collect(Collectors.toList()));
        }

        for(int i=0;i<rozmedzia.size();i++){
            int p=i+65;
            char c=(char)p;
            System.out.println(c);
            for(int j=0;j<utriedeneRozmedzia.get(i).size();j++){
                int pocet=4;
                System.out.println(utriedeneRozmedzia.get(i).get(j).getKey()+" n:"+utriedeneRozmedzia.get(i).get(j).getValue());
                if(j==pocet){
                    break;
                }

            }
        }
    }



}
