package org.example.aplikacia;

import lombok.Getter;
import org.example.sifry.TabulkovaTranspozicia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OdhadKluca {
    @Getter
    private int dlzkaKluca;
    private Map<Integer,Integer> odhadovaneDlzky;
    private Bigramy bigramy;

    public OdhadKluca(Bigramy bigramy,TabulkovaTranspozicia transpozicia){
        this.bigramy=bigramy;
        najdiDlzkuKluca(transpozicia.getZasifrovanyText().toString(),transpozicia);
    }
    private void hodnotyDlzkyKluca(ArrayList<StringBuilder> bloky) {
        List<Map.Entry<String, Double>> vyskytBigramov;
        int pocet=0;
        for(int prvy=0;prvy<bloky.size();prvy++) {
            for (int druhy = 0; druhy < bloky.size(); druhy++) {
                if (prvy != druhy) {
                    StringBuilder text = bigramy.premenBlokyNaText(bloky, prvy, druhy);
                    vyskytBigramov = bigramy.ngramy(text, 2, false);
                    int pocitadlo=0;
                    int velkostPorovnania=20;
                    for(int bigram=0;bigram<velkostPorovnania;bigram++){
                        if(bigramy.getStatistikaBigramov().get(vyskytBigramov.get(bigram).getKey())!=null){
                            if(bigramy.getTopZlych().contains(vyskytBigramov.get(bigram).getKey()) &&
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
    private int spravnyKluc(){
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
    protected void najdiDlzkuKluca(String zt, TabulkovaTranspozicia transpozicia) {
        odhadovaneDlzky=new TreeMap<>();
        ArrayList<StringBuilder> riadky;
        ArrayList<List<Map.Entry<String, Double>>> statistika = new ArrayList<>();
        int n = zt.length();
        ArrayList<ArrayList<StringBuilder>> blokyDlzkyN=new ArrayList<>();
        //tu bolo 30
        for (int i = 0; i <= 20; i++) {
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
                }
                if (zt.length() > j) {
                    riadok.append(zt.charAt(j));
                }
            }
            hodnotyDlzkyKluca(riadky);
            blokyDlzkyN.add(riadky);
        }
        dlzkaKluca= spravnyKluc();
        transpozicia.setZtVBlokoch(blokyDlzkyN.get(dlzkaKluca));
    }


}
