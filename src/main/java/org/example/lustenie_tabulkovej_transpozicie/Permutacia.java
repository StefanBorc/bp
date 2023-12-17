package org.example.lustenie_tabulkovej_transpozicie;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

import static org.example.Main.SUBOR;

public class Permutacia {
    private Vlastnosti vlastnosti;
    @Setter
    private int dlzkaKluca;
    @Getter
    private int[] permutacia;
    @Setter
    private ArrayList<StringBuilder> blokyZt;
    private ArrayList<String> topZlychBigramovOT;
    private ArrayList<Integer[]> kombinacie;
    private ArrayList<List<Map.Entry<String, Double>>> bigramyPreMozneKombinacie;
    private ArrayList<List<Map.Entry<String, Double>>> trigramyPreMozneKombinacie;

    public Permutacia(Vlastnosti vlastnosti, OdhadKluca odhadKluca) {
        this.dlzkaKluca= odhadKluca.getDlzkaKluca();
        this.blokyZt=odhadKluca.blokyDlzkyKluca;
        this.vlastnosti = vlastnosti;
        topZlych(100);
        hladajPermutaciu();
    }

    public void topZlych(int pocetRiadkov){
        topZlychBigramovOT=new ArrayList<>();
        if(SUBOR.equals("EN")){
            if(pocetRiadkov>500){
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
    protected void hladajPermutaciu() {

        vybratNajlepsieKombinacie();
        ArrayList<Double[]> usporiadanePodlaOT = vlastnosti.usporiadajPodlaOT(bigramyPreMozneKombinacie,vlastnosti.getStatistikaBigramovUsporiadana());
        poskladatCestu(kombinacie,usporiadanePodlaOT);
    }

    public void vytlacTestovanuPermutaciu(){
        if(permutacia.length>0){
            for(var c:permutacia){
                System.out.print(c+" ");
            }
            System.out.println();
        }

    }
    //novy pokus s hladanim stlpcov
    public ArrayList<ArrayList<Double>> vyladitBigramy(ArrayList<StringBuilder> bloky){
        List<Map.Entry<String, Double>> bigramyZT;
        int velkostPorovnaniaPrePermutaciu=30;
        ArrayList<ArrayList<Double>> odchylkyStlpcov=new ArrayList<>();

        for(int prvy=0;prvy<bloky.size();prvy++) {
            odchylkyStlpcov.add(new ArrayList<>());
            for (int druhy = 0; druhy < bloky.size(); druhy++) {
                double odchylka=0.0;
                if (prvy != druhy) {
                    StringBuilder text = vlastnosti.premenBlokyNaText(bloky, prvy, druhy);
                    bigramyZT = vlastnosti.ngramy(text, 2, false);
                    for(int bigram=0;bigram<velkostPorovnaniaPrePermutaciu;bigram++){
                        if(vlastnosti.getStatistikaBigramov().get(bigramyZT.get(bigram).getKey())!=null){
                            if(topZlychBigramovOT.contains(bigramyZT.get(bigram).getKey())){
                                odchylka+=Math.abs(bigramyZT.get(bigram).getValue()-vlastnosti.getStatistikaBigramov().get(bigramyZT.get(bigram).getKey()));
                            }
                        }
                        if(odchylka>3){
                            break;
                        }
                    }
                }
                else{
                    odchylka=20.0;
                }
                odchylkyStlpcov.get(prvy).add(odchylka);
            }
        }
        return odchylkyStlpcov;
    }
    private void vyladitTrigramy(){
        int velkostPorovnania=30;
        ArrayList<Integer[]> kopiaKombinacii=new ArrayList<>(kombinacie);
        kombinacie=new ArrayList<>();
        for(int i=0;i<trigramyPreMozneKombinacie.size();i++){
            double odchylka=0.0;
            for(int j=0;j<velkostPorovnania;j++){
                if(vlastnosti.getStatistikaTrigramov().containsKey(trigramyPreMozneKombinacie.get(i).get(j))) {
                    if (vlastnosti.getStatistikaTrigramov().get(trigramyPreMozneKombinacie.get(i).get(j)) < 0.15) {
                        System.out.println(1);
                    }
                }
            }
        }
    }
    private void vybratNajlepsieKombinacie() {
        ArrayList<ArrayList<Double>> odchylky = vyladitBigramy(blokyZt);
        kombinacie = new ArrayList<>();
        bigramyPreMozneKombinacie = new ArrayList<>();
        trigramyPreMozneKombinacie = new ArrayList<>();
        for (int i = 0; i < odchylky.size(); i++) {
            Integer minStlpec1 = odchylky.get(i).indexOf(Collections.min(odchylky.get(i)));
            odchylky.get(i).set(odchylky.get(i).indexOf(Collections.min(odchylky.get(i))), 20.0);
            Integer minStlpec2 = odchylky.get(i).indexOf(Collections.min(odchylky.get(i)));
            StringBuilder text = vlastnosti.premenBlokyNaText(blokyZt, i, minStlpec1);
            Integer[] kombinacia = new Integer[]{i, minStlpec1};
            kombinacie.add(kombinacia);
            //kombinacia = new Integer[]{i, minStlpec2};
            //kombinacie.add(kombinacia);
            bigramyPreMozneKombinacie.add(vlastnosti.ngramy(text,2,false));
        }
    }
    private void vyraditZvysneKombinacie(ArrayList<Integer[]> mozneKombinacie, ArrayList<Double[]> usporiadanePodlaOT){
        ArrayList<Double> odchylky=new ArrayList<>();
        int index=0;
        int n=10;
        for(var kombo:mozneKombinacie){
            double odchylka=0;
            for(int i=0;i<n;i++){
                odchylka+=(usporiadanePodlaOT.get(index)[i])*(n-1);
            }
            odchylky.add(odchylka);
            index++;
        }
        if(mozneKombinacie.size()>dlzkaKluca-1){
            while(mozneKombinacie.size()!=dlzkaKluca-1){
                int nevhodny=odchylky.indexOf(Collections.min(odchylky));
                odchylky.remove(nevhodny);
                mozneKombinacie.remove(nevhodny);
            }
        }

    }
    private void poskladatCestu(ArrayList<Integer[]> mozneKombinacie, ArrayList<Double[]> usporiadanePodlaOT){
       // vyladitTrigramy();
        if(mozneKombinacie.size()!=dlzkaKluca-1){
            vyraditZvysneKombinacie(mozneKombinacie, usporiadanePodlaOT);
        }
        ArrayList<Integer> cesta=new ArrayList<>();
        if(mozneKombinacie.size()==dlzkaKluca-1){
            boolean najdenyPrvyStlpec;
            int stlpec=0;
            for(int i=0;i<mozneKombinacie.size();i++){
                najdenyPrvyStlpec=true;
                for(int j=0;j<mozneKombinacie.size();j++){
                    if(j==i){
                        continue;
                    }
                    if(Objects.equals(mozneKombinacie.get(i)[0], mozneKombinacie.get(j)[1])){
                        najdenyPrvyStlpec=false;
                    }
                }
                if(najdenyPrvyStlpec ){
                    stlpec=mozneKombinacie.get(i)[0];
                }
            }
            cesta.add(stlpec);
            int dlzka=dlzkaKluca;
            boolean stop=false;
            while(cesta.size()!=dlzka){
                boolean nasielSusednyStlpec=false;
                for (Integer[] kombo : mozneKombinacie) {
                    if (stlpec == kombo[0]) {
                        stlpec = kombo[1];
                        cesta.add(kombo[1]);
                        nasielSusednyStlpec = true;
                        if(cesta.size()==dlzka){
                            stop=true;
                            break;
                        }
                    }
                }
                if(stop){
                    break;
                }
                if(!nasielSusednyStlpec){
                    break;
                }
            }
        }
        if(cesta.size()<32){
            permutacia=new int[cesta.size()];
            for(int i=0;i<cesta.size();i++){
                permutacia[i]=cesta.get(i);
            }
        }

    }
}