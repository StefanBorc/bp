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
    private ArrayList<Integer[]> kombinacie;
    private ArrayList<List<Map.Entry<String, Double>>> bigramyPreMozneKombinacie;
    private ArrayList<List<Map.Entry<String, Double>>> trigramyPreMozneKombinacie;
    private double dolnaHranicaBigramov;
    private double dolnaHranicaTrigramov;
    private double hornaHranicaTrigramov;
    @Setter
    private int pocetRiadkov;

    public Permutacia(Vlastnosti vlastnosti, OdhadKluca odhadKluca) {
        this.dlzkaKluca= odhadKluca.getDlzkaKluca();
        this.blokyZt=odhadKluca.blokyDlzkyKluca;
        this.vlastnosti = vlastnosti;
        odhadnutHranicuTrigramov(2500,1000);
        odhadnutHranicuBigramov();
    }
    protected void hladatPermutaciu() {
        ArrayList<ArrayList<Double>> odchylky = vyladitBigramy(blokyZt);
        najstPoradie(odchylky,false);
    }
    public void odhadnutHranicuBigramov(){
        int n;
        if(SUBOR.equals("DE")){
            n=450;
        }
        else if(SUBOR.equals("CZ") && pocetRiadkov<=100){
            n=375;
        }
        else{
            n=350;
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
        int vaha=3;
        if(pocetRiadkov<=100){
            if(!SUBOR.equals("EN")){
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
                    StringBuilder text = vlastnosti.premenBlokyNaText(bloky, prvy, druhy);
                    bigramyZT = vlastnosti.ngramy(text, 2, false);
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
                    odchylka=20.0;
                }
                odchylkyStlpcov.get(prvy).add(odchylka);
            }
        }
        return odchylkyStlpcov;
    }

    private void vyladitTrigramy(){
        ArrayList<Integer[]> kombinacie3Stlpov =spravitKombinacie3Stlpcov();

        for(int i=0;i<kombinacie3Stlpov.size();i++){
            if(kombinacie3Stlpov.get(i)!=null){
                StringBuilder text=vlastnosti.premenBlokyNaText(blokyZt,kombinacie3Stlpov.get(i)[0],kombinacie3Stlpov.get(i)[1]);
                text=pridatStlpec(text,kombinacie3Stlpov.get(i)[2],3);
                trigramyPreMozneKombinacie.add(vlastnosti.ngramy(text,3,false));
            }
            else {
                trigramyPreMozneKombinacie.add(new ArrayList<>());
            }

        }
        ArrayList<Integer[]> kopiaKombinacii=new ArrayList<>();
        ArrayList<List<Map.Entry<String, Double>>> kopiaStatistik=new ArrayList<>();

        ArrayList<Double> odchylky=spravitOdchylkyTrigramov(25);

        int index1;
        int index2;
        int vitaz;
        for(int i=0;i<kombinacie3Stlpov.size();i+=4){
            boolean a=odchylky.get(i)>odchylky.get(i+1);
            boolean b=odchylky.get(i+2)>odchylky.get(i+3);
            index1=i;
            index2=i+2;
            if(a){
                index1=i+1;
            }
            if(b){
                index2=i+3;
            }
            boolean c=odchylky.get(index1)>odchylky.get(index2);
            if(c){
                vitaz=index2;
            }
            else{
                vitaz=index1;
            }
            kopiaKombinacii.add(new Integer[]{kombinacie3Stlpov.get(vitaz)[0],kombinacie3Stlpov.get(vitaz)[1],kombinacie3Stlpov.get(vitaz)[2]});
            kopiaStatistik.add(bigramyPreMozneKombinacie.get(vratIndexKombinacie(kombinacie3Stlpov.get(vitaz)[0],kombinacie3Stlpov.get(vitaz)[1])));
        }
        kombinacie=(kopiaKombinacii);
        bigramyPreMozneKombinacie=(kopiaStatistik);

    }
    private ArrayList<Double> spravitOdchylkyTrigramov(int velkostPorovnania){
        ArrayList<Double> odchylky=new ArrayList<>();

        for(int i=0;i<trigramyPreMozneKombinacie.size();i++){
            double odchylka=0.0;
            if(trigramyPreMozneKombinacie.get(i).isEmpty()){
                odchylky.add(500.0);
                continue;
            }
            for(int j=0;j<velkostPorovnania;j++){
                if(vlastnosti.getStatistikaTrigramov().containsKey(trigramyPreMozneKombinacie.get(i).get(j).getKey())) {
                    if (vlastnosti.getStatistikaTrigramov().get(trigramyPreMozneKombinacie.get(i).get(j).getKey()) < dolnaHranicaTrigramov) {
                        odchylka+=Math.abs(vlastnosti.getStatistikaTrigramov().get(trigramyPreMozneKombinacie.get(i).get(j).getKey())-trigramyPreMozneKombinacie.get(i).get(j).getValue())*15;
                    }
                    else if(vlastnosti.getStatistikaTrigramov().get(trigramyPreMozneKombinacie.get(i).get(j).getKey()) > hornaHranicaTrigramov){
                        odchylka+=Math.abs(vlastnosti.getStatistikaTrigramov().get(trigramyPreMozneKombinacie.get(i).get(j).getKey())-trigramyPreMozneKombinacie.get(i).get(j).getValue());
                    }
                    else{
                        odchylka+=Math.abs(vlastnosti.getStatistikaTrigramov().get(trigramyPreMozneKombinacie.get(i).get(j).getKey())-trigramyPreMozneKombinacie.get(i).get(j).getValue())*5;
                    }
                }
            }
            odchylky.add(odchylka);
        }
        return odchylky;
    }
    private ArrayList<Integer[]> spravitKombinacie3Stlpcov(){
        ArrayList<Integer[]> kombinacie3Stlpov=new ArrayList<>();
        for(int i=0;i<kombinacie.size();i++){
            Integer[] moznaKombinaciaPredchodcu=najdiDalsiStlpec(kombinacie.get(i)[1]);
            for(int j=0;j<2;j++){
                if(!opakujuceCisla(kombinacie.get(i)[0],kombinacie.get(i)[1],moznaKombinaciaPredchodcu[j])){
                    kombinacie3Stlpov.add(new Integer[]{kombinacie.get(i)[0],kombinacie.get(i)[1],moznaKombinaciaPredchodcu[j]});
                }
                else{
                    kombinacie3Stlpov.add(null);
                }
            }
        }
        return kombinacie3Stlpov;
    }
    private boolean opakujuceCisla(int n1,int n2,int n3){
        return n1 == n2 || n1 == n3 || n2 == n3;
    }
    private int vratIndexKombinacie(int n1,int n2){
        var kombo=new Integer[]{n1, n2};
        for(int i=0;i<kombinacie.size();i++){
            if(Arrays.equals(kombinacie.get(i),kombo)){
                return i;
            }
        }
        return 0;
    }
    private StringBuilder pridatStlpec(StringBuilder text,int stlpec,int pozicia){
        int index=pozicia-1;
        for(int i=0;i<blokyZt.get(stlpec).length()-pozicia;i++){
            text.insert(index,blokyZt.get(stlpec).charAt(i));
            index+=pozicia;
        }
        return text;
    }
    private Integer[] najdiDalsiStlpec(int n){
        Integer[] nastupcovia=new Integer[2];
        int index=0;
        for(int i=0;i<kombinacie.size();i++){
            if(kombinacie.get(i)[0]==n){
                nastupcovia[index]=kombinacie.get(i)[1];
                index++;
            }
        }
        return nastupcovia;
    }
    private void odhadnutHranicuTrigramov(int dolnaHranica,int hornaHranica){
        dolnaHranicaTrigramov =vlastnosti.getStatistikaTrigramovUsporiadana().get(dolnaHranica).getValue();
        hornaHranicaTrigramov = vlastnosti.getStatistikaTrigramovUsporiadana().get(hornaHranica).getValue();
    }
    private void vybratNajlepsieKombinacie(ArrayList<ArrayList<Double>> odchylky,boolean zahrnutViac) {
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
            bigramyPreMozneKombinacie.add(vlastnosti.ngramy(text,2,false));
            if(zahrnutViac){
                text = vlastnosti.premenBlokyNaText(blokyZt, i, minStlpec2);
                kombinacia = new Integer[]{i, minStlpec2};
                kombinacie.add(kombinacia);
                bigramyPreMozneKombinacie.add(vlastnosti.ngramy(text,2,false));
            }
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
        vybratNajlepsieKombinacie(odchylky,zahrnutTrigramy);
        if(zahrnutTrigramy){
            vyladitTrigramy();
            najstCestu();
        }
        else{
            poskladatCestu();
        }
    }
}
