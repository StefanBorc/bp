package org.example.lustenie_tabulkovej_transpozicie;

import lombok.Getter;
import lombok.Setter;
import org.example.aplikacia.Jazyk;

import java.util.*;

public class Permutacia {
    private Vlastnosti vlastnosti;
    @Setter
    private int dlzkaKluca;
    @Getter
    private int[] permutacia;
    @Setter
    private ArrayList<StringBuilder> blokyZt;
    private ArrayList<Integer[]> kombinacie;
    private ArrayList<List<Map.Entry<String, Double>>> bigramyPreMozneKombinacie;
    private ArrayList<List<Map.Entry<String, Double>>> trigramyPreMozneKombinacie;
    private double dolnaHranicaBigramov;
    private double[] hraniceTrigramov;
    @Setter
    private int pocetRiadkov;
    @Setter
    private Jazyk jazyk;

    public Permutacia(Vlastnosti vlastnosti, OdhadKluca odhadKluca,Jazyk jazyk) {
        this.dlzkaKluca= odhadKluca.getDlzkaKluca();
        this.blokyZt=odhadKluca.getBlokyDlzkyKluca();
        this.vlastnosti = vlastnosti;
        this.jazyk=jazyk;
        odhadnutHranicuTrigramov(new int[]{2500,3800,5000});
        odhadnutHranicuBigramov();
    }
    protected void hladatPermutaciu() {
        ArrayList<ArrayList<Double>> odchylky = vyladitBigramy(blokyZt);
        najstPoradie(odchylky,false);
    }
    public void odhadnutHranicuBigramov(){
        int n;
        if(jazyk.toString().equals("DE")){
            n=450;
        }
        else if(jazyk.toString().equals("CZ")){
            n=425;
        }
        else{
            n=400;
        }
        dolnaHranicaBigramov=vlastnosti.getStatistikaBigramovUsporiadana().get(vlastnosti.getStatistikaBigramovUsporiadana().size()-n).getValue();
    }
    public void vytlacTestovanuPermutaciu(){
        if(permutacia.length>0){
            for(var c:permutacia){
                System.out.print(c+" ");
            }
            System.out.println();
        }
    }
    private ArrayList<ArrayList<Double>> vyladitBigramy(ArrayList<StringBuilder> bloky){
        List<Map.Entry<String, Double>> bigramyZT;

        int velkostPorovnaniaPrePermutaciu=30;
        int vaha=4;
        if(pocetRiadkov<=100){
            if(!jazyk.toString().equals("EN")){
                velkostPorovnaniaPrePermutaciu=25;
            }
            vaha=10;
        }
        ArrayList<ArrayList<Double>> odchylkyStlpcov=new ArrayList<>();

        for(int prvy=0;prvy<bloky.size();prvy++) {
            odchylkyStlpcov.add(new ArrayList<>());
            for (int druhy = 0; druhy < bloky.size(); druhy++) {
                double odchylka=0.0;
                if (prvy != druhy) {
                    StringBuilder text = vlastnosti.premenBlokyNaText(bloky, new int[]{prvy,druhy});
                    bigramyZT = vlastnosti.ngramy(text, 2, false,false,true);
                    for(int bigram=0;bigram<velkostPorovnaniaPrePermutaciu;bigram++){
                        if(vlastnosti.getStatistikaBigramov().get(bigramyZT.get(bigram).getKey())!=null){
                            if(vlastnosti.getStatistikaBigramov().get(bigramyZT.get(bigram).getKey())<=dolnaHranicaBigramov ){
                                odchylka+=Math.abs(bigramyZT.get(bigram).getValue()-vlastnosti.getStatistikaBigramov().get(bigramyZT.get(bigram).getKey()));
                            }
                        }
                        if(odchylka>vaha){
                            break;
                        }
                    }
                }
                else{
                    odchylka=50.0;
                }
                odchylkyStlpcov.get(prvy).add(odchylka);
            }
        }
        return odchylkyStlpcov;
    }

    private ArrayList<ArrayList<Double>> vyladitTrigramy(){
        List<Map.Entry<String, Double>> trigramyZT;
        ArrayList<ArrayList<Double>> odchylkyStlpcov=new ArrayList<>();

        int vaha=200;

        for(int i=0;i<kombinacie.size();i++){
            odchylkyStlpcov.add(new ArrayList<>());
            for(int j=0;j<blokyZt.size();j++){
                double odchylka=0.0;
                if(j!=kombinacie.get(i)[0] && j!=kombinacie.get(i)[1]){
                    StringBuilder text=vlastnosti.premenBlokyNaText(blokyZt,new int[]{kombinacie.get(i)[0],kombinacie.get(i)[1],j});
                    trigramyZT = vlastnosti.ngramy(text, 3, false,false,true);
                    double velkostPorovnaniaPrePermutaciu=(double)trigramyZT.size()/1.2;
                    for(int trigram=0;trigram<velkostPorovnaniaPrePermutaciu;trigram++){
                        if(vlastnosti.getStatistikaTrigramov().get(trigramyZT.get(trigram).getKey())!=null){
                            if(vlastnosti.getStatistikaTrigramov().get(trigramyZT.get(trigram).getKey())<=hraniceTrigramov[0]){
                                odchylka+=Math.abs(trigramyZT.get(trigram).getValue()-vlastnosti.getStatistikaTrigramov().get(trigramyZT.get(trigram).getKey()));
                            }
                            else if(vlastnosti.getStatistikaTrigramov().get(trigramyZT.get(trigram).getKey())<=hraniceTrigramov[1]){
                                odchylka+=5*Math.abs(trigramyZT.get(trigram).getValue()-vlastnosti.getStatistikaTrigramov().get(trigramyZT.get(trigram).getKey()));
                            }
                            else if(vlastnosti.getStatistikaTrigramov().get(trigramyZT.get(trigram).getKey())<=hraniceTrigramov[2]){
                                odchylka+=10*Math.abs(trigramyZT.get(trigram).getValue()-vlastnosti.getStatistikaTrigramov().get(trigramyZT.get(trigram).getKey()));
                            }



                        }
                        if(odchylka>vaha){
                            break;
                        }
                    }
                }
                else{
                    odchylka=500;
                }
                odchylkyStlpcov.get(i).add(odchylka);
            }
        }
        return odchylkyStlpcov;
    }

    private void odhadnutHranicuTrigramov(int[] hranice){
        hraniceTrigramov=new double[hranice.length];
        for(int i=0;i<hranice.length;i++){
            hraniceTrigramov[i] =vlastnosti.getStatistikaTrigramovUsporiadana().get(hranice[i]).getValue();
        }
    }
    private void vybratNajlepsieKombinacie(ArrayList<ArrayList<Double>> odchylky) {
        bigramyPreMozneKombinacie = new ArrayList<>();
        trigramyPreMozneKombinacie = new ArrayList<>();
        //ak su na rade bigramy
        if (kombinacie.isEmpty()) {
            for (int i = 0; i < odchylky.size(); i++) {
                int minStlpec = odchylky.get(i).indexOf(Collections.min(odchylky.get(i)));
                StringBuilder text = vlastnosti.premenBlokyNaText(blokyZt, new int[]{i, minStlpec});
                Integer[] kombinacia = new Integer[]{i, minStlpec};
                kombinacie.add(kombinacia);
                bigramyPreMozneKombinacie.add(vlastnosti.ngramy(text, 2, false, false, true));

                /*
                odchylky.get(i).set(odchylky.get(i).indexOf(Collections.min(odchylky.get(i))), 200.0);
                int minStlpec2 = odchylky.get(i).indexOf(Collections.min(odchylky.get(i)));
                StringBuilder text2=vlastnosti.premenBlokyNaText(blokyZt, new int[]{i, minStlpec2});
                Integer[] kombinacia2=new Integer[]{i,minStlpec2}
                //kombinacie.add(kombinacia2);
                //bigramyPreMozneKombinacie.add(vlastnosti.ngramy(text2,2,false,false,true));

                 */
            }
            //vybratNajlepsie();
        }
        //ak su na rade trigramy
        else{
            for (int i = 0; i < odchylky.size(); i++) {
                int minStlpec = odchylky.get(i).indexOf(Collections.min(odchylky.get(i)));
                StringBuilder text = vlastnosti.premenBlokyNaText(blokyZt, new int[]{kombinacie.get(i)[0], kombinacie.get(i)[1],minStlpec});
                Integer[] kombinacia = new Integer[]{kombinacie.get(i)[0], kombinacie.get(i)[1],minStlpec};
                kombinacie.set(i,kombinacia);
                trigramyPreMozneKombinacie.add(vlastnosti.ngramy(text, 3, false, false, true));
            }
        }
        int a=0;
    }
    private void vybratNajlepsie(){
        //tu vybrat najlepsie kombinacie z dvoch odchylike
        ArrayList<Integer[]> kopiaKombinacii=new ArrayList<>();
        ArrayList<List<Map.Entry<String, Double>>> kopiaBigramovPreKombinacie=new ArrayList<>();

        ArrayList<Double> odchylky=new ArrayList<>();
        int velkostPorovnania=10;
        var bigramyZTUsporiadane=vlastnosti.usporiadajPodlaOT(bigramyPreMozneKombinacie,vlastnosti.getStatistikaBigramovUsporiadana());
        for(int i=0;i<kombinacie.size();i++){
            Double odchylka=0.0;
            for(int j=0;j<velkostPorovnania;j++){
                odchylka+=(Math.abs(bigramyZTUsporiadane.get(i)[j]-vlastnosti.getStatistikaBigramovUsporiadana().get(j).getValue()));
            }
            odchylky.add(odchylka);
        }

        for(int i=0;i<kombinacie.size();i+=2){
            if(odchylky.get(i)>odchylky.get(i+1)){
                kopiaKombinacii.add(kombinacie.get(i+1));
                kopiaBigramovPreKombinacie.add(bigramyPreMozneKombinacie.get(i+1));
            }
            else {
                kopiaKombinacii.add(kombinacie.get(i));
                kopiaBigramovPreKombinacie.add(bigramyPreMozneKombinacie.get(i));
            }
        }
        bigramyPreMozneKombinacie=new ArrayList<>(kopiaBigramovPreKombinacie);
        kombinacie=new ArrayList<>(kopiaKombinacii);

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
    private void poskladatCestu(){
        ArrayList<Double[]> usporiadanePodlaOT = vlastnosti.usporiadajPodlaOT(bigramyPreMozneKombinacie,vlastnosti.getStatistikaBigramovUsporiadana());
        if(kombinacie.size()!=dlzkaKluca-1){
            vyraditZvysneKombinacie(kombinacie, usporiadanePodlaOT);
        }
        ArrayList<Integer> cesta=new ArrayList<>();
        if(kombinacie.size()==dlzkaKluca-1){
            int stlpec=najstPrvyStlpec();
            cesta.add(stlpec);
            int dlzka=dlzkaKluca;
            boolean stop=false;
            while(cesta.size()!=dlzka){
                boolean nasielSusednyStlpec=false;
                for (Integer[] kombo : kombinacie) {
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
    private int najstPrvyStlpec(){
        boolean najdenyPrvyStlpec;
        int stlpec=0;
        for(int i=0;i<kombinacie.size();i++){
            najdenyPrvyStlpec=true;
            for(int j=0;j<kombinacie.size();j++){
                if(j==i){
                    continue;
                }
                if(Objects.equals(kombinacie.get(i)[0], kombinacie.get(j)[1])){
                    najdenyPrvyStlpec=false;
                }
            }
            if(najdenyPrvyStlpec ){
                stlpec=kombinacie.get(i)[0];
            }
        }
        return stlpec;
    }
    private int[] najstCestu(){
        permutacia=new int[dlzkaKluca];
        int koren=najstKoren();
        if(koren==-1){
            return null;
        }
        permutacia[0]=kombinacie.get(koren)[0];
        permutacia[1]=kombinacie.get(koren)[1];
        permutacia[2]=kombinacie.get(koren)[2];
        int i=3;
        int index=kombinacie.get(koren)[2];
        while(true){
            permutacia[i++]=kombinacie.get(index)[1];
            if(i==dlzkaKluca){
                break;
            }
            permutacia[i++]=kombinacie.get(index)[2];
            index=kombinacie.get(index)[2];
            if(i==dlzkaKluca){
                break;
            }
        }
        return permutacia;
    }
    private int najstKoren(){
        Set<Integer> druheCisla = new HashSet<>();

        for (Integer[] kombinacia : kombinacie) {
            Integer druheCislo = kombinacia[1];
            druheCisla.add(druheCislo);
        }

        for (Integer[] kombinacia : kombinacie) {
            Integer prveCislo = kombinacia[0];
            if (!druheCisla.contains(prveCislo)) {
                return prveCislo;
            }
        }
        return -1;
    }
    private void najstPoradie(ArrayList<ArrayList<Double>> odchylky,boolean zahrnutTrigramy){
        kombinacie = new ArrayList<>();
        vybratNajlepsieKombinacie(odchylky);
        if(zahrnutTrigramy){
            var odchylkyTrigramov=vyladitTrigramy();
            vybratNajlepsieKombinacie(odchylkyTrigramov);
            najstCestu();
        }
        else{
            poskladatCestu();
        }
    }
}