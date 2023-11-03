package org.example.aplikacia;

import lombok.Getter;
import lombok.Setter;
import org.example.sifry.TabulkovaTranspozicia;

import java.util.*;

import static org.example.Main.POCIATOCNA_VELKOST;
import static org.example.Main.SUBOR;


public class Permutacia {
    @Getter
    private StringBuilder upravenyText;
    @Getter
    private StringBuilder zt;
    private Bigramy bigramy;
    @Setter
    private int dlzkaKluca;
    @Getter
    private int[] permutacia;
    private ArrayList<String> topZlychBigramovOT;

    public Permutacia(Bigramy bigramy,TabulkovaTranspozicia transpozicia,int dlzkaKluca) {
        this.dlzkaKluca= dlzkaKluca;
        this.bigramy=bigramy;
        topZlych();
        hladajPermutaciu(transpozicia.getZtVBlokoch());
    }
    private void topZlych(){
        topZlychBigramovOT=new ArrayList<>();
        if(SUBOR.equals("EN1.txt") || SUBOR.equals("EN2.txt")){
            if(POCIATOCNA_VELKOST>500){
                for(int i=50;i<bigramy.getTopZlych().size();i++){
                    topZlychBigramovOT.add(bigramy.getTopZlych().get(i));
                }
            }
            else{
                topZlychBigramovOT=bigramy.getTopZlych();
            }
        }
        else{
            topZlychBigramovOT=bigramy.getTopZlych();
        }
    }
    protected void hladajPermutaciu(ArrayList<StringBuilder> bloky) {
        List<Map.Entry<String, Double>> bigramyZT;
        ArrayList<List<Map.Entry<String,Double>>> bigramyKombinacii=new ArrayList<>();
        ArrayList<Integer[]> mozneKombinacie=new ArrayList<>();
        int vaha=1;
        int velkostPorovnania=30;
        if(SUBOR.equals("EN1.txt") || SUBOR.equals("EN2.txt")){
            if(POCIATOCNA_VELKOST>500){
                vaha=3;
            }
            else if(POCIATOCNA_VELKOST>800){
                vaha=4;
            }
            else{
                vaha=2;
            }
        }
        for(int prvy=0;prvy<bloky.size();prvy++) {
            for (int druhy = 0; druhy < bloky.size(); druhy++) {

                if (prvy != druhy ) {
                    StringBuilder text = bigramy.premenBlokyNaText(bloky, prvy, druhy);
                    bigramyZT = bigramy.ngramy(text, 2, false);
                    int pocitadlo=0;
                    for(int bigram=0;bigram<velkostPorovnania;bigram++){
                        if(bigramy.getStatistikaBigramov().get(bigramyZT.get(bigram).getKey())!=null){
                            if(topZlychBigramovOT.contains(bigramyZT.get(bigram).getKey())){
                                //toto je zmenene inak to je bez podmienky
                                if(topZlychBigramovOT.indexOf(topZlychBigramovOT.contains(bigramyZT.get(bigram)))>250){
                                    pocitadlo+=1;
                                }

                            }
                        }
                        //toto tu nebolo
                        if(pocitadlo>vaha){
                            break;
                        }
                    }
                    if(pocitadlo<vaha){
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
    private void spravPodrobnuStatistikuRovnakych(ArrayList<Integer[]> mozneKombinacie, ArrayList<Double[]> usporiadanePodlaOT, int[] kombinacie){
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
    private void vyradNevhodneStlpce(ArrayList<Integer[]> mozneKombinacie, ArrayList<Double[]> usporiadanePodlaOT){
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
    private ArrayList<Integer[]> vyradNevhodneKombinacie(ArrayList<Integer[]> mozneKombinacie,ArrayList<Double[]> usporiadanePodlaOT){
        for(int i=0;i<mozneKombinacie.size();i++){
            for(int j=0;j<mozneKombinacie.size();j++){
                if(mozneKombinacie.get(i)!=mozneKombinacie.get(j)){
                    if(suRovnake(mozneKombinacie.get(i),mozneKombinacie.get(j))){
                        spravPodrobnuStatistikuRovnakych(mozneKombinacie,usporiadanePodlaOT,new int[]{mozneKombinacie.indexOf(mozneKombinacie.get(i)),mozneKombinacie.indexOf(mozneKombinacie.get(j))});
                    }
                }
            }
        }
        if(mozneKombinacie.size()!=dlzkaKluca-1){
            vyradNevhodneStlpce( mozneKombinacie,usporiadanePodlaOT);
        }
        return mozneKombinacie;
    }
    private void najdiCestu(ArrayList<Integer[]> mozneKombinacie,ArrayList<Double[]> usporiadanePodlaOT){
        if(mozneKombinacie.size()!=dlzkaKluca-1){
            vyradNevhodneKombinacie(mozneKombinacie,usporiadanePodlaOT);
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
    public void vytlacTestovanuPermutaciu(){
        if(permutacia.length>0){
            for(var c:permutacia){
                System.out.print(c+" ");
            }
            System.out.println();
        }

    }
}
