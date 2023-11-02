package org.example.aplikacia;

import lombok.Getter;
import org.example.sifry.TabulkovaTranspozicia;


import java.io.*;
import java.util.*;


public class Permutacia {
    @Getter
    private StringBuilder upravenyText;
    @Getter
    private StringBuilder zt;
    private Bigramy bigramy;
    private int dlzkaKluca;

    public Permutacia(Bigramy bigramy,TabulkovaTranspozicia transpozicia,int dlzkaKluca) throws IOException {
        this.dlzkaKluca= dlzkaKluca;
        this.bigramy=bigramy;

        ArrayList<Integer> s = hladajPermutaciu(transpozicia.getZtVBlokoch());
        for(int i=0;i<s.size();i++){
            System.out.print(s.get(i)+" ");
        }

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

    protected ArrayList<Integer> hladajPermutaciu(ArrayList<StringBuilder> bloky) {
        List<Map.Entry<String, Double>> bigramyZT;
        ArrayList<List<Map.Entry<String,Double>>> bigramyKombinacii=new ArrayList<>();
        ArrayList<Integer[]> mozneKombinacie=new ArrayList<>();

        for(int prvy=0;prvy<bloky.size();prvy++) {
            for (int druhy = 0; druhy < bloky.size(); druhy++) {

                if (prvy != druhy ) {
                    StringBuilder text = premenBlokyNaText(bloky, prvy, druhy);
                    bigramyZT = bigramy.ngramy(text, 2, false);
                    int pocitadlo=0;
                    int velkostPorovnania=25;
                    for(int bigram=0;bigram<velkostPorovnania;bigram++){
                        if(bigramy.getStatistikaBigramov().get(bigramyZT.get(bigram).getKey())!=null){
                            if(bigramy.getTopZlych().contains(bigramyZT.get(bigram).getKey())){
                                pocitadlo+=1;
                            }
                        }
                    }
                    if(pocitadlo<1){
                        mozneKombinacie.add(new Integer[]{prvy,druhy});
                        bigramyKombinacii.add(bigramyZT);
                    }
                }
            }
        }

        if(!mozneKombinacie.isEmpty()) {
            ArrayList<Double[]> usporiadanePodlaOT = bigramy.usporiadajPodlaOT(bigramyKombinacii);
            //najdiSpravnyStlpec(usporiadanePodlaOT,mozneKombinacie,0.8);
            return najdiCestu(mozneKombinacie);
        }
        else{
            return null;
        }
    }
    private ArrayList<Integer> najdiCestu(ArrayList<Integer[]> mozneKombinacie){
        boolean najdenyPrvyStlpec;
        int stlpec=0;
        ArrayList<Integer> cesta=new ArrayList<>();
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
        if(mozneKombinacie.size()!=dlzkaKluca-1){
            //System.out.println("Smola");
        }
        else{
            //este preverit aby dlzka bola rovnaka pre kluc 17 napr lebo moze byt vela stlpcov ktore sa tam vyskytnu naviac
            while(cesta.size()!=dlzkaKluca){
                boolean nasielSusednyStlpec=false;
                for(int i=0;i<mozneKombinacie.size();i++){
                    if(stlpec==mozneKombinacie.get(i)[0]){
                        stlpec=mozneKombinacie.get(i)[1];
                        cesta.add(mozneKombinacie.get(i)[1]);
                        nasielSusednyStlpec=true;
                    }
                }
                if(!nasielSusednyStlpec){
                //    System.out.println("neda sa");
                    break;
                }
            }
            for(int i=0;i<cesta.size();i++){
              //  System.out.print(cesta.get(i)+" ");
            }
           // System.out.println();
        }
        return cesta;

    }
}
