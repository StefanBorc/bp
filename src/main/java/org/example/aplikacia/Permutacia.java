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
    private OdhadKluca odhadKluca;
    private ArrayList<String> topZlychBigramovOT;

    public Permutacia(Bigramy bigramy,TabulkovaTranspozicia transpozicia,OdhadKluca odhadKluca,int dlzkaKluca) {
        this.dlzkaKluca= dlzkaKluca;
        this.bigramy=bigramy;
        this.odhadKluca=odhadKluca;
        topZlych();
        hladajPermutaciu();
    }
    private void topZlych(){

        topZlychBigramovOT=new ArrayList<>();
        if(SUBOR.equals("EN1.txt") || SUBOR.equals("EN2.txt") || SUBOR.equals("DE.txt")){
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
    protected void hladajPermutaciu() {
            ArrayList<Double[]> usporiadanePodlaOT = bigramy.usporiadajPodlaOT(odhadKluca.getStatistikaKombinacii());
            najdiCestu(odhadKluca.getKombinacie(),usporiadanePodlaOT);

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
