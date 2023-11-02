package org.example.sifry;

import java.util.*;

public class NepouziteFunkcie {
/*
    private void najdiSpravnyStlpec(ArrayList<Double[]> usporiadaneBigramyPodlaOt, ArrayList<Integer[]> mozneKombinacie, double moznaOdchylka){

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

                if(index<10){
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

        double minimalnaOdchylka= Collections.min(odchylky);
        int indexMinOdchylky=odchylky.indexOf(minimalnaOdchylka);
        poradieStlpcov.add(mozneKombinacie.get(indexMinOdchylky));
        System.out.println();
    }
    private void najdiPermutaciu(){
        poradieStlpcov =new ArrayList<>();

        int rozsahSkumania=transpozicia.getZtVBlokoch().size();
        int prvyStlpec=0;

        while(poradieStlpcov.size()!=dlzkaKluca-1){
            if(!poradieStlpcov.isEmpty()){
                prvyStlpec=poradieStlpcov.get(poradieStlpcov.size()-1)[1];
                rozsahSkumania=prvyStlpec+1;

            }
            // hladajPermutaciu(transpozicia.getZtVBlokoch(),prvyStlpec,rozsahSkumania);
        }
        int[] permutacia=new int[poradieStlpcov.size()+1];
        for(int k=0;k<poradieStlpcov.size();k++){
            if(k==poradieStlpcov.size()-1){
                permutacia[k]=(poradieStlpcov.get(k)[0]);
                permutacia[k+1]=(poradieStlpcov.get(k)[1]);
            }
            else{
                permutacia[k]=(poradieStlpcov.get(k)[0]);
            }
        }
        for(var stlpec:permutacia){
            System.out.print(stlpec+" ");
        }
        System.out.println();
        if(Arrays.equals(permutacia, transpozicia.getPoradie())){
            System.out.println("Uhadol si permutaciu");
        }

    }



        private ArrayList<Integer> hladajPermutaciu(ArrayList<StringBuilder> bloky) {
        List<Map.Entry<String, Double>> bigramyZT;
        ArrayList<List<Map.Entry<String,Double>>> bigramyKombinacii=new ArrayList<>();
        ArrayList<Integer[]> mozneKombinacie=new ArrayList<>();

        for(int prvy=0;prvy<bloky.size();prvy++) {
            for (int druhy = 0; druhy < bloky.size(); druhy++) {
               // if (prvy != druhy && !existujeKombinacia(prvy,druhy)) {
                if (prvy != druhy ) {
                    StringBuilder text = premenBlokyNaText(bloky, prvy, druhy);
                    bigramyZT = invariant.ngramy(text, 2, false);
                    int pocitadlo=0;
                    int velkostPorovnania=10;
                    for(int bigram=0;bigram<velkostPorovnania;bigram++){
                        if(statistikaBigramovOT.get(bigramyZT.get(bigram).getKey())!=null){
                            if(topZlych.contains(bigramyZT.get(bigram).getKey())){
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
            ArrayList<Double[]> usporiadanePodlaOT=usporiadajPodlaOT(bigramyKombinacii);
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



    invarianty
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
    private double indexKoincidencie(String zasifrovanyText) {
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
 */
}
