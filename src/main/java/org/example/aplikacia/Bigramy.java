package org.example.aplikacia;

import lombok.Getter;


import java.util.*;
import java.util.stream.Collectors;

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
            String ngram=new String();
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

        double pocet=ngramy.entrySet().stream().mapToDouble(Map.Entry::getValue).sum();
        Map<String,Double> ngramyPercenta=new HashMap<>();
        for(var bigram:ngramy.entrySet()){
            ngramyPercenta.put(bigram.getKey(),(bigram.getValue()/pocet)*100);
        }
        return vytriedeneNgramy(ngramyPercenta);
    }
    private void urobTopBigramov(){
        topZlych=new ArrayList<>();
        for(int i = statistikaBigramovUsporiadana.size()-1; i>= statistikaBigramovUsporiadana.size()-350; i--){
            topZlych.add(statistikaBigramovUsporiadana.get(i).getKey());
        }
        topDobrych=new ArrayList<>();
        for(int i=0;i<100;i++){
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


}
