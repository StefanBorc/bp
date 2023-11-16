package org.example.aplikacia;

import lombok.Getter;
import org.example.sifry.TabulkovaTranspozicia;

import java.util.*;

import static org.example.Main.POCIATOCNA_VELKOST;
import static org.example.Main.SUBOR;

public class OdhadKluca {
    @Getter
    private int dlzkaKluca;
    private Map<Integer,Integer> odhadovaneDlzky;
    private Vlastnosti vlastnosti;
    ArrayList<String> topZlychBigramovOT;
    @Getter
    private ArrayList<Integer[]> kombinacie;
    @Getter
    private ArrayList<List<Map.Entry<String, Double>>> statistikaKombinacii;
    int index;

    public OdhadKluca(Vlastnosti vlastnosti, TabulkovaTranspozicia transpozicia){
        this.vlastnosti = vlastnosti;
        topZlych();
        najdiDlzkuKluca(transpozicia.getZasifrovanyText().toString(),transpozicia);

    }
    private void topZlych(){
        topZlychBigramovOT=new ArrayList<>();
        if(SUBOR.equals("EN1.txt") || SUBOR.equals("EN2.txt") || SUBOR.equals("DE.txt")){
            if(POCIATOCNA_VELKOST>500){
                for(int i = 50; i< vlastnosti.getTopZlych().size(); i++){
                    topZlychBigramovOT.add(vlastnosti.getTopZlych().get(i));
                }
            }
            else{
                topZlychBigramovOT= vlastnosti.getTopZlych();
            }
        }
        else{
            topZlychBigramovOT= vlastnosti.getTopZlych();
        }
    }
    private void hodnotyDlzkyKluca(ArrayList<StringBuilder> bloky,ArrayList<ArrayList<List<Map.Entry<String,Double>>>> bigramyMoznychKlucov,ArrayList<ArrayList<Integer[]>> mozneKombinacieKlucov) {
        List<Map.Entry<String, Double>> bigramyZT;
        int pocet=0;
        int vaha=1;
        int velkostPorovnaniaPrePermutaciu=30;
        int velkostPorovnaniaPreKluc=20;
        ArrayList<Integer[]> mozneKombinacie=new ArrayList<>();
        ArrayList<List<Map.Entry<String,Double>>> bigramyMoznejPermutacieDlzkyKlucaN=new ArrayList<>();

        if(SUBOR.equals("EN2.txt")){
            if(POCIATOCNA_VELKOST>500){
                vaha=2;
            }
            else if(POCIATOCNA_VELKOST>900){
                vaha=4;
            }
            else{
                vaha=2;
            }
        }
        for(int prvy=0;prvy<bloky.size();prvy++) {
            for (int druhy = 0; druhy < bloky.size(); druhy++) {
                if (prvy != druhy) {
                    StringBuilder text = vlastnosti.premenBlokyNaText(bloky, prvy, druhy);
                    bigramyZT = vlastnosti.ngramy(text, 2, false);
                    int pocitadloKluca=0;
                    int pocitadloPermutacie=0;

                    for(int bigram=0;bigram<velkostPorovnaniaPrePermutaciu;bigram++){
                        if(bigram<velkostPorovnaniaPreKluc){
                            if(vlastnosti.getStatistikaBigramov().get(bigramyZT.get(bigram).getKey())!=null){
                                if(vlastnosti.getTopZlych().contains(bigramyZT.get(bigram).getKey()) &&
                                        bigramyZT.get(bigram).getValue()>0.5){

                                    pocitadloKluca+=1;
                                }
                            }
                        }
                        if(vlastnosti.getStatistikaBigramov().get(bigramyZT.get(bigram).getKey())!=null){
                            if(topZlychBigramovOT.contains(bigramyZT.get(bigram).getKey())){
                                pocitadloPermutacie+=1;
                            }
                        }
                        if(pocitadloKluca>vaha || pocitadloKluca>1){
                            break;
                        }
                    }
                    if(pocitadloKluca<1 ){
                        pocet++;
                    }
                    if(pocitadloPermutacie<vaha){
                        mozneKombinacie.add(new Integer[]{prvy,druhy});
                        bigramyMoznejPermutacieDlzkyKlucaN.add(bigramyZT);
                    }

                }
            }
        }
        if(pocet>0) {
            odhadovaneDlzky.put(bloky.size(),pocet);
            mozneKombinacieKlucov.add(mozneKombinacie);
            bigramyMoznychKlucov.add(bigramyMoznejPermutacieDlzkyKlucaN);
        }
    }
    private int spravnyKluc(){
        int max=0;
        int maxIndex=0;
        int poziciaKluca=0;
        index=0;
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
        for(var odhadovanaDlzka:odhadovaneDlzky.entrySet()){
            if(odhadovanaDlzka.getKey().equals(maxIndex)){
                index=poziciaKluca;
                break;
            }
            poziciaKluca++;
        }

        return maxIndex;
    }
    public double dKluca(ArrayList<StringBuilder> bloky){

        ArrayList<StringBuilder> text=citajStlpce(bloky);

        ArrayList<Double> percentaSamohlaskySpoluhlasky= new ArrayList<>();

        for(int i=0;i<text.size();i++){
            percentaSamohlaskySpoluhlasky.add(vlastnosti.samohlaskySpoluhlasky(text.get(i).toString()));
        }
        double suma= percentaSamohlaskySpoluhlasky.stream().mapToDouble(a->a).sum();
        double pocet=percentaSamohlaskySpoluhlasky.size();

        return suma/pocet;

        //spravit frekvenciu spoluhlasok samohlasok a porovnat to pre jednotlive dlzky klucov
    }
    protected void najdiDlzkuKluca(String zt, TabulkovaTranspozicia transpozicia) {
        odhadovaneDlzky=new TreeMap<>();
        ArrayList<ArrayList<Integer[]>> mozneKombinacieKlucov=new ArrayList<>();
        ArrayList<ArrayList<List<Map.Entry<String,Double>>>> bigramyMoznychKlucov=new ArrayList<>();
        ArrayList<StringBuilder> riadky;

        ArrayList<Double> odchylky=new ArrayList<>();
        int n = zt.length();
        ArrayList<ArrayList<StringBuilder>> blokyDlzkyN=new ArrayList<>();
        for (int i = 0; i <= 30; i++) {
            if (i < 10) {
                odchylky.add(null);
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
                }
                if (zt.length() > j) {
                    riadok.append(zt.charAt(j));
                }
            }

            hodnotyDlzkyKluca(riadky,bigramyMoznychKlucov,mozneKombinacieKlucov);
            double odchylka=dKluca(riadky);
            odchylky.add(odchylka);
            blokyDlzkyN.add(riadky);
        }
        dlzkaKluca= spravnyKluc();
        int o=vratKluc(odchylky);
        System.out.println(o);
        statistikaKombinacii=bigramyMoznychKlucov.get(index);
        kombinacie=mozneKombinacieKlucov.get(index);
        transpozicia.setZtVBlokoch(blokyDlzkyN.get(dlzkaKluca));
    }

    private ArrayList<StringBuilder> citajStlpce(ArrayList<StringBuilder> bloky){
        ArrayList<StringBuilder> riadky=new ArrayList<>();

        for(int i=0;i< bloky.get(0).length();i++){
            StringBuilder text=new StringBuilder();
            for(int j=0;j<bloky.size();j++){
                if(bloky.get(j).length()>i){
                    text.append(bloky.get(j).charAt(i));
                }
            }
            riadky.add(text);

        }
        return riadky;
    }

    private int vratKluc(ArrayList<Double> samohlaskySpoluhlaskyStatistika){
        ArrayList<Double> odchylky=new ArrayList<>();
        double otStatistika= vlastnosti.getStatistikaSamohlasokSpoluhlasok();
        for(var hodnota:samohlaskySpoluhlaskyStatistika){
            double odchylka=99;
            if(hodnota!=null){
                odchylka=Math.abs(otStatistika-hodnota);
            }
            odchylky.add(odchylka);
        }
        int index=odchylky.indexOf(Collections.min(odchylky));

        return index;
    }

}
