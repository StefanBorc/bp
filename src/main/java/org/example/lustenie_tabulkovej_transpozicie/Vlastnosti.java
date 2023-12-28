package org.example.lustenie_tabulkovej_transpozicie;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

public class Vlastnosti {
    @Getter
    private String[] vsetkySlova;
    @Getter@Setter
    private List<Map.Entry<String,Double>> statistikaBigramovUsporiadana;
    @Getter@Setter
    private List<Map.Entry<String,Double>> statistikaTrigramovUsporiadana;
    @Getter
    private Map<String,Double> statistikaBigramov;
    @Getter
    private Map<String, Double> statistikaTrigramov;
    @Getter@Setter
    private double statistikaSamohlasokSpoluhlasok;
    public Vlastnosti(StringBuilder ot) {
        vsetkySlova = ot.toString().split(" ");
        statistikaBigramov =new HashMap<>();
        statistikaBigramovUsporiadana = ngramy(ot,2,true,false);
        statistikaTrigramov =new HashMap<>();
        statistikaTrigramovUsporiadana = ngramy(ot,3,true,false);
        statistikaSamohlasokSpoluhlasok=samohlaskySpoluhlasky(ot.toString());
    }
    private List<Map.Entry<String, Double>> vytriedeneNgramy(Map<String, Double> mapa) {
        List<Map.Entry<String, Double>> vytriedeneNgramy = mapa.entrySet()
                .stream()
                .sorted((vstup1, vstup2) -> vstup2.getValue().compareTo(vstup1.getValue()))
                .collect(Collectors.toList());

        return vytriedeneNgramy;
    }
    protected Map<String,Integer> vratIndexyUsporiadanejMapy(List<Map.Entry<String,Double>> usporiadanaMapa){
        Map<String, Integer> indexyMapy = new HashMap<>();
        for (int i = 0; i < usporiadanaMapa.size(); i++) {
            indexyMapy.put(usporiadanaMapa.get(i).getKey(), i);
        }
        return indexyMapy;
    }
    protected List<Map.Entry<String, Double>> ngramy(StringBuilder text, int n,boolean jeOtvorenyText,boolean zmenaTextu) {
        Map<String, Double> ngramy = new HashMap<>();
        int pripocitaj;
        if(jeOtvorenyText){
            pripocitaj=0;
        }
        else{
            pripocitaj=n-1;
        }

        for (int i = 0; i < text.length() - n; i++) {
            String ngram= "";
            for (int j = i; j < i+n; j++) {
                ngram+=text.charAt(j);
            }
            i+=pripocitaj;

            boolean suZnaky=true;
            for(var c:ngram.toCharArray()){
                if(!Character.isAlphabetic(c)){
                    suZnaky=false;
                    break;
                }
            }
            if(suZnaky){
                ngramy.merge(ngram,1.0,Double::sum);
            }
        }
        if(jeOtvorenyText && (statistikaBigramov.isEmpty()) || statistikaTrigramov.isEmpty() || zmenaTextu ){
            if(n==2){
                statistikaBigramov = ngramy;
                int a=0;
            }
            else if(n==3){
                statistikaTrigramov = ngramy;
            }
            double pocet = ngramy.values().stream().mapToDouble(v -> v).sum();
            Map<String, Double> ngramyPercenta = new HashMap<>();
            for (var ngram : ngramy.entrySet()) {
                ngramyPercenta.put(ngram.getKey(), (ngram.getValue() / pocet) * 100);
            }
            if(n==2){
                statistikaBigramov=ngramyPercenta;
            } else if (n==3) {
                statistikaTrigramov=ngramyPercenta;
            }

        }

        double pocet=ngramy.values().stream().mapToDouble(v -> v).sum();
        Map<String,Double> ngramyPercenta=new HashMap<>();
        for(var bigram:ngramy.entrySet()){
            ngramyPercenta.put(bigram.getKey(),(bigram.getValue()/pocet)*100);
        }
        return vytriedeneNgramy(ngramyPercenta);
    }
    protected ArrayList<Double[]> usporiadajPodlaOT(ArrayList<List<Map.Entry<String, Double>>> mapy,List<Map.Entry<String,Double>> statistikaOtUsporiadna){
        Map<String,Integer> indexyMapy=vratIndexyUsporiadanejMapy(statistikaOtUsporiadna);
        ArrayList<Double[]> listyPercient=new ArrayList<>(mapy.size());
        for(var mapa : mapy){
            Double[] usporiadanePercenta=new Double[statistikaOtUsporiadna.size()];
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
    protected StringBuilder premenBlokyNaText(ArrayList<StringBuilder> bloky, int n1, int n2) {
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

    protected double samohlaskySpoluhlasky(String text){
        ArrayList<Character> samohlasky=new ArrayList<>(List.of('a','e','i','o','u','y'));
        double pocetSamohlasok=0;
        double pocetSpoluhlasok=0;
        for(var c: text.toLowerCase().toCharArray()){
            if(samohlasky.contains(c)){
                pocetSamohlasok++;
            }
            else{
                pocetSpoluhlasok++;
            }
        }

        return pocetSamohlasok/pocetSpoluhlasok;
    }


}