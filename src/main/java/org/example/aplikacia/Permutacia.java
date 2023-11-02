package org.example.aplikacia;

import lombok.Getter;
import org.example.sifry.TabulkovaTranspozicia;

import java.util.*;


public class Permutacia {
    @Getter
    private StringBuilder upravenyText;
    @Getter
    private StringBuilder zt;
    private Bigramy bigramy;
    private int dlzkaKluca;
    @Getter
    private int[] permutacia;

    public Permutacia(Bigramy bigramy,TabulkovaTranspozicia transpozicia,int dlzkaKluca) {
        this.dlzkaKluca= dlzkaKluca;
        this.bigramy=bigramy;

        hladajPermutaciu(transpozicia.getZtVBlokoch());

    }

    protected void hladajPermutaciu(ArrayList<StringBuilder> bloky) {
        List<Map.Entry<String, Double>> bigramyZT;
        ArrayList<List<Map.Entry<String,Double>>> bigramyKombinacii=new ArrayList<>();
        ArrayList<Integer[]> mozneKombinacie=new ArrayList<>();

        for(int prvy=0;prvy<bloky.size();prvy++) {
            for (int druhy = 0; druhy < bloky.size(); druhy++) {

                if (prvy != druhy ) {
                    StringBuilder text = bigramy.premenBlokyNaText(bloky, prvy, druhy);
                    bigramyZT = bigramy.ngramy(text, 2, false);
                    int pocitadlo=0;
                    int velkostPorovnania=30;
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
            najdiCestu(mozneKombinacie,usporiadanePodlaOT);
        }

    }
    private boolean suRovnake(Integer[] kombinacia1,Integer[] kombinacia2){
        Integer[] kombinaciaPomocna=new Integer[]{kombinacia1[1],kombinacia1[0]};
        return Arrays.equals(kombinaciaPomocna, kombinacia2);
    }
    private void spravPodrobnuStatistiku(ArrayList<Integer[]> mozneKombinacie,ArrayList<Double[]> usporiadanePodlaOT,int[] kombinacie){
        ArrayList<Double> odchylky=new ArrayList<>();
        for(int k=0;k<2;k++){
            double odchylka=0.0;
            for(int i=0;i<10;i++){
                odchylka+=usporiadanePodlaOT.get(kombinacie[k])[i]*(10-i);
            }
            odchylky.add(odchylka);
        }
        if(odchylky.get(0)>odchylky.get(1)){
            mozneKombinacie.remove(mozneKombinacie.remove(kombinacie[1]));
        }
        else{
            mozneKombinacie.remove(mozneKombinacie.remove(kombinacie[0]));
        }

    }
    private ArrayList<Integer[]> vyradNevhodneKombinacie(ArrayList<Integer[]> mozneKombinacie,ArrayList<Double[]> usporiadanePodlaOT){
        ArrayList<Integer[]> kombinacie = new ArrayList<>();
        kombinacie.addAll(mozneKombinacie);

        for(int i=0;i<mozneKombinacie.size();i++){
            for(int j=0;j<mozneKombinacie.size();j++){
                if(mozneKombinacie.get(i)!=mozneKombinacie.get(j)){
                    if(suRovnake(mozneKombinacie.get(i),mozneKombinacie.get(j))){
                        spravPodrobnuStatistiku(mozneKombinacie,usporiadanePodlaOT,new int[]{mozneKombinacie.indexOf(mozneKombinacie.get(i)),mozneKombinacie.indexOf(mozneKombinacie.get(j))});
                    }
                }
            }
        }
        return mozneKombinacie;
    }
    private void najdiCestu(ArrayList<Integer[]> mozneKombinacie,ArrayList<Double[]> usporiadanePodlaOT){
        if(mozneKombinacie.size()!=dlzkaKluca-1){
            vyradNevhodneKombinacie(mozneKombinacie,usporiadanePodlaOT);
        }
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
        if(mozneKombinacie.size()==dlzkaKluca-1){
            int dlzka=dlzkaKluca;
            boolean stop=false;
            while(cesta.size()!=dlzka){
                boolean nasielSusednyStlpec=false;
                for (Integer[] kombo : mozneKombinacie) {
                    if (stlpec == kombo[0]) {
                        stlpec = kombo[1];
                        cesta.add(kombo[1]);
                        nasielSusednyStlpec = true;
                        if(cesta.size()>4){
                            if(cesta.get(cesta.size()-1)==cesta.get(cesta.size()-3)){
                                stop=true;
                                break;
                            }
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
        permutacia=new int[cesta.size()];
        for(int i=0;i<cesta.size();i++){
            permutacia[i]=cesta.get(i);
        }

    }
    public void vytlacTestovanuPermutaciu(){
        if(permutacia.length>0){
            for(var c:permutacia){
                System.out.print(c+" ");
            }
            System.out.println();
        }

    }
}
