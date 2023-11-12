package org.example.aplikacia;

import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

import static org.example.Main.SUBOR;

public class Bigramy {
    @Getter
    private String[] vsetkySlova;
    @Getter
    private List<Map.Entry<String,Double>> statistikaBigramovUsporiadana;
    @Getter
    private Map<String,Double> statistikaBigramov;
    @Getter
    private ArrayList<String> topZlych;
    @Getter
    private ArrayList<String> topDobrych;
    public Bigramy(StringBuilder ot) {
        vsetkySlova = ot.toString().split(" ");
        statistikaBigramov =new HashMap<>();
        statistikaBigramovUsporiadana = ngramy(ot,2,true);
        urobTopBigramov();
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
    protected List<Map.Entry<String, Double>> ngramy(StringBuilder text, int n,boolean jeOtvorenyText) {
        Map<String, Double> ngramy = new HashMap<>();
        int pripocitaj;
        if(jeOtvorenyText){
            pripocitaj=0;
        }
        else{
            pripocitaj=1;
        }
        for (int i = 0; i < text.length() - n; i++) {
            String ngram= "";
            for (int j = i; j < i+n; j++) {
                ngram+=text.charAt(j);
            }
            i+=pripocitaj;
            boolean jeBigramZnakov=true;
            for(var c:ngram.toCharArray()){
                if(!Character.isAlphabetic(c)){
                    jeBigramZnakov=false;
                }
            }
            if(jeBigramZnakov){
                ngramy.merge(ngram,1.0,Double::sum);
            }
        }
        if(jeOtvorenyText){
            statistikaBigramov = ngramy;
        }

        double pocet=ngramy.values().stream().mapToDouble(v -> v).sum();
        Map<String,Double> ngramyPercenta=new HashMap<>();
        for(var bigram:ngramy.entrySet()){
            ngramyPercenta.put(bigram.getKey(),(bigram.getValue()/pocet)*100);
        }
        return vytriedeneNgramy(ngramyPercenta);
    }
    private void urobTopBigramov(){
        int n;
        if((SUBOR.equals("EN1.txt") || SUBOR.equals("EN2.txt")|| SUBOR.equals("DE.txt"))){
            n=400;
        }
        else {
            n=350;
        }
        topZlych=new ArrayList<>();
        for(int i = statistikaBigramovUsporiadana.size()-1; i>= statistikaBigramovUsporiadana.size()-n; i--){
            topZlych.add(statistikaBigramovUsporiadana.get(i).getKey());
        }
        topDobrych=new ArrayList<>();
        for(int i=0;i<50;i++){
            topDobrych.add(statistikaBigramovUsporiadana.get(i).getKey());
        }
    }
    protected ArrayList<Double[]> usporiadajPodlaOT(ArrayList<List<Map.Entry<String, Double>>> mapy){
        Map<String,Integer> indexyMapy=vratIndexyUsporiadanejMapy(statistikaBigramovUsporiadana);
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


}
