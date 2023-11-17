package org.example.aplikacia;

import lombok.Getter;
import org.example.sifry.TabulkovaTranspozicia;

import java.util.*;

import static org.example.Main.POCIATOCNA_VELKOST;
import static org.example.Main.SUBOR;

public class OdhadKluca {
    @Getter
    private int dlzkaKluca;

    private Vlastnosti vlastnosti;
    ArrayList<String> topZlychBigramovOT;
    @Getter
    ArrayList<StringBuilder> blokyDlzkyKluca;

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

            double odchylka=dKluca(riadky);
            odchylky.add(odchylka);
            blokyDlzkyN.add(riadky);
        }
        dlzkaKluca= vratKluc(odchylky);
        transpozicia.setZtVBlokoch(blokyDlzkyN.get(dlzkaKluca));
        blokyDlzkyKluca=transpozicia.getZtVBlokoch();
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
        odchylky.set(index,999.0);
        if(jeKlucDvojnasobok(index/2,odchylky)){
            index/=2;
        }
        return index;
    }

    private boolean jeKlucDvojnasobok(int n,ArrayList<Double> odchylky){
        int index = odchylky.indexOf(Collections.min(odchylky));
        return index == n;
    }


}
