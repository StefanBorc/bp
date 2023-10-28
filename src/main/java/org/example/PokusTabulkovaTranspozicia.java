package org.example;

import lombok.Getter;
import org.example.sifry.TabulkovaTranspozicia;


import java.io.*;
import java.util.*;


public class PokusTabulkovaTranspozicia {
    public static int POCIATOCNA_VELKOST = 900;
    @Getter
    private StringBuilder upravenyText;
    @Getter
    private StringBuilder zt;
    private TabulkovaTranspozicia transpozicia;
    private Invariant invariant;
    private List<Map.Entry<String, Double>> usporiadaneBigramyOT;
    private Map<String,Double> statistikaBigramovOT;
    private ArrayList<String> topZlych;
    private ArrayList<String> topDobrych;
    private Map<Integer,Integer> odhadovaneDlzky;
    private int dlzkaKluca;
    private ArrayList<Integer[]> poradieStlpcov;

    public PokusTabulkovaTranspozicia(String nazovSuboru, String kluc) throws IOException {
        dlzkaKluca=0;
        odhadovaneDlzky=new TreeMap<>();

        transpozicia = new TabulkovaTranspozicia(kluc);
        upravenyText = transpozicia.vratSuborU(nazovSuboru);
        invariant = new Invariant(transpozicia.vratSubor(nazovSuboru), upravenyText);

        spustiSifrovanie(kluc, POCIATOCNA_VELKOST);

        najdiDlzkuKluca();

        najdiPermutaciu();

    }
    private void spustiSifrovanie(String kluc, int n) {
        transpozicia.zasifrujText(upravenyText, kluc, n);
        zt = transpozicia.getZasifrovanyText();
        spravStatistikuOT();
    }
    private void spravStatistikuOT(){
        usporiadaneBigramyOT = invariant.getStatistikaBigramovUsporiadana();
        statistikaBigramovOT =invariant.getSkumanaMapa();
        topZlych=new ArrayList<>();
        for(int i = statistikaBigramovOT.size()-1; i>= statistikaBigramovOT.size()-300; i--){
            topZlych.add(usporiadaneBigramyOT.get(i).getKey());
        }
        topDobrych=new ArrayList<>();
        for(int i=0;i<100;i++){
            topDobrych.add(usporiadaneBigramyOT.get(i).getKey());
        }
    }
    private ArrayList<String> vygenerujKluce(int n) {
        ArrayList<String> vygenerovaneKluce=new ArrayList<>();
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
        return vygenerovaneKluce;
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
    private void otestujDlzkuKluca(ArrayList<StringBuilder> bloky) {
        List<Map.Entry<String, Double>> vyskytBigramov;
        int pocet=0;
        for(int prvy=0;prvy<bloky.size();prvy++) {
            for (int druhy = 0; druhy < bloky.size(); druhy++) {
                if (prvy != druhy) {
                    StringBuilder text = premenBlokyNaText(bloky, prvy, druhy);
                    vyskytBigramov = invariant.ngramy(text, 2, false);
                    int pocitadlo=0;
                    int velkostPorovnania=20;
                    for(int bigram=0;bigram<velkostPorovnania;bigram++){
                        if(statistikaBigramovOT.get(vyskytBigramov.get(bigram).getKey())!=null){
                            if(topZlych.contains(vyskytBigramov.get(bigram).getKey()) &&
                                    vyskytBigramov.get(bigram).getValue()>0.5){
                                pocitadlo+=1;
                            }
                        }
                    }
                    if(pocitadlo<1 ){
                        pocet++;
                    }
                }
            }
        }
        if(pocet>0) {
            odhadovaneDlzky.put(bloky.size(),pocet);
        }
    }
    private int najdiHladanyKluc(){
        int max=0;
        int maxIndex=0;
        for(var odhadovanaDlzka:odhadovaneDlzky.entrySet()){
            if(odhadovanaDlzka.getValue()>max){
                maxIndex=odhadovanaDlzka.getKey();
                max=odhadovanaDlzka.getValue();
            }
        }
        if(maxIndex>20 && maxIndex%2==0){
            for(var odhad:odhadovaneDlzky.entrySet()){
                if(maxIndex/2==odhad.getKey() && odhad.getValue()>5){
                    maxIndex/=2;
                    break;
                }
            }
        }
        return maxIndex;
    }
    private void najdiDlzkuKluca() {
        ArrayList<StringBuilder> riadky;
        ArrayList<List<Map.Entry<String, Double>>> statistika = new ArrayList<>();
        int n = zt.length();
        ArrayList<ArrayList<StringBuilder>> blokyDlzkyN=new ArrayList<>();
        for (int i = 0; i <= 30; i++) {
            if (i < 10) {
                statistika.add(new ArrayList<>());
                blokyDlzkyN.add(new ArrayList<>());
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
            otestujDlzkuKluca(riadky);
            blokyDlzkyN.add(riadky);
        }

        dlzkaKluca=najdiHladanyKluc();
        transpozicia.setZtVBlokoch(blokyDlzkyN.get(dlzkaKluca));
        System.out.println("odhadnuta dlzka kluca : "+dlzkaKluca);
    }
    private ArrayList<Double[]> usporiadajPodlaOT(ArrayList<List<Map.Entry<String,Double>>> mapy){
        Map<String,Integer> indexyMapy=invariant.vratIndexyUsporiadanejMapy(usporiadaneBigramyOT);
        ArrayList<Double[]> listyPercient=new ArrayList<>(mapy.size());
        for(var mapa : mapy){

            Double[] usporiadanePercenta=new Double[statistikaBigramovOT.size()];
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
    private boolean existujeKombinacia(int i,int j){
        for(var kombinacia:poradieStlpcov){
            if((i==kombinacia[0] && j==kombinacia[1]) || j==kombinacia[0] && i==kombinacia[1]){
                return true;
            }
        }
        return false;
    }
    private void hladajPermutaciu(ArrayList<StringBuilder> bloky) {
        List<Map.Entry<String, Double>> bigramyZT=new ArrayList<>();
        ArrayList<List<Map.Entry<String,Double>>> bigramyVelkostiKluca=new ArrayList<>();
        ArrayList<Integer[]> mozneKombinacie=new ArrayList<>();

        for(int prvy=0;prvy<bloky.size();prvy++) {
            for (int druhy = 0; druhy < bloky.size(); druhy++) {
                if (prvy != druhy && !existujeKombinacia(prvy,druhy)) {
                    StringBuilder text = premenBlokyNaText(bloky, prvy, druhy);
                    bigramyZT = invariant.ngramy(text, 2, false);
                    int pocitadlo=0;
                    int velkostPorovnania=20;
                    for(int bigram=0;bigram<velkostPorovnania;bigram++){
                        if(statistikaBigramovOT.get(bigramyZT.get(bigram).getKey())!=null){
                            if(topZlych.contains(bigramyZT.get(bigram).getKey())){
                                pocitadlo+=1;
                            }
                        }
                    }
                    if(pocitadlo<1){
                        mozneKombinacie.add(new Integer[]{prvy,druhy});
                        bigramyVelkostiKluca.add(bigramyZT);
                    }
                }
            }
        }

        if(!mozneKombinacie.isEmpty()) {
         //   System.out.println(mozneKombinacie.size());
            for (var permutacia:mozneKombinacie) {
               // System.out.print("[ ");
                for(var prvok:permutacia){
                  //  System.out.print(prvok+" ");
                }
               // System.out.print("] ");
            }
            ArrayList<Double[]> usporiadanePodlaOT=usporiadajPodlaOT(bigramyVelkostiKluca);

            najdiSpravnyStlpec(usporiadanePodlaOT,mozneKombinacie,0.8);


        }
    }
    private void otestujChi(ArrayList<Double[]> observedFrequencies){
        Double[] expectedFrequencies= new Double[statistikaBigramovOT.size()];
        for(int i = 0; i< statistikaBigramovOT.size(); i++){
            expectedFrequencies[i]= usporiadaneBigramyOT.get(i).getValue();
        }
        double[] chiSquaredValues = new double[observedFrequencies.size()];

        for (int i = 0; i < observedFrequencies.size(); i++) {
            Double[] observed = observedFrequencies.get(i);

            double chiSquared = calculateChiSquared(observed, expectedFrequencies);
            chiSquaredValues[i] = chiSquared;
        }

        // Find the combination with the smallest chi-squared value
        int bestCombinationIndex = findIndexOfSmallestValue(chiSquaredValues);
        //System.out.println("The best combination of rows is: " + bestCombinationIndex);
    }
    private static double calculateChiSquared(Double[] observed, Double[] expected) {
        double chiSquared = 0.0;
        for (int i = 0; i < observed.length; i++) {
            double O = observed[i];
            double E = expected[i];
            chiSquared += Math.pow(O - E, 2) / E;
        }
        return chiSquared;
    }

    private static int findIndexOfSmallestValue(double[] values) {
        int index = 0;
        double smallestValue = values[0];

        for (int i = 1; i < values.length; i++) {
            if (values[i] < smallestValue) {
                smallestValue = values[i];
                index = i;
            }
        }

        return index;
    }
    private void najdiSpravnyStlpec(ArrayList<Double[]> usporiadaneBigramyPodlaOt,ArrayList<Integer[]> mozneKombinacie,double moznaOdchylka){

        ArrayList<Double> odchylky=new ArrayList<>(usporiadaneBigramyPodlaOt.size());
        ArrayList<Double> pocetMaloPravdepodobnych=new ArrayList<>();

        ArrayList<Double> sucetPercent=new ArrayList<>();
        for(var bigramy:usporiadaneBigramyPodlaOt){
            int index=0;
            int indexOdzadu=bigramy.length-1;
            double odchylkyBigramov=0.0;
            double maloPravdepodobnych=0;
            double pocitadlo=0;
            for(var bigram:bigramy){

                double odchylka;

                if(index<5){
                    odchylka=Math.abs(usporiadaneBigramyOT.get(index).getValue()-bigram);

                    if(odchylka>moznaOdchylka){
                        if(odchylka== usporiadaneBigramyOT.get(index).getValue()){
                            odchylkyBigramov+=10;
                        }
                    }
                    pocitadlo+=bigram;
                    odchylkyBigramov+=odchylka;
                }
                if(index<400){
                    if(bigramy[indexOdzadu]>0.0){
                        maloPravdepodobnych+=bigramy[indexOdzadu]*indexOdzadu*0.001;
                        odchylkyBigramov+=0.00001*(index);
                    }
                }
                else{
                    break;
                }

                index++;
                indexOdzadu--;
            }
            sucetPercent.add(pocitadlo);
            odchylky.add(odchylkyBigramov);
            pocetMaloPravdepodobnych.add(maloPravdepodobnych);

        }
        double minimalnaOdchylka=Collections.min(odchylky);
        int indexMinOdchylky=odchylky.indexOf(minimalnaOdchylka);
        poradieStlpcov.add(mozneKombinacie.get(indexMinOdchylky));
      //  System.out.println();
        /*
        ArrayList<Double[]> najlepsich5BigramovZT=new ArrayList<>();

        for(var bigramy:usporiadaneBigramyPodlaOt){
            Double[] top5Bigramov=new Double[usporiadaneBigramyPodlaOt.size()];
            for(int i=0;i<usporiadaneBigramyPodlaOt.size();i++){
                top5Bigramov[i]=bigramy[i];
            }
            najlepsich5BigramovZT.add(top5Bigramov);
        }
        //otestujChi(usporiadaneBigramyPodlaOTNajlepsich5);
         */
    }
    private void najdiPermutaciu(){
        poradieStlpcov =new ArrayList<>();
        int i=0;
        while(poradieStlpcov.size()!=dlzkaKluca-1){
            hladajPermutaciu(transpozicia.getZtVBlokoch());
            System.out.println("["+poradieStlpcov.get(i)[0]+" "+poradieStlpcov.get(i)[1]+"]");
            i++;
        }
        for(var kombinacia:poradieStlpcov){
            //System.out.print(kombinacia[0]+" "+kombinacia[1]+" ");
        }
    }
}
