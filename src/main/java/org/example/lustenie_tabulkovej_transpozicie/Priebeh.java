package org.example.lustenie_tabulkovej_transpozicie;

import org.example.aplikacia.Jazyk;
import org.example.sifry.TabulkovaTranspozicia;

import java.util.ArrayList;

public class Priebeh {
    private TabulkovaTranspozicia transpozicia;
    private OdhadKluca odhadKluca;
    private Permutacia permutacia;
    private Vlastnosti vlastnosti;
    private int pocetRiadkov ;

    public Priebeh(Jazyk jazyk, StringBuilder otNasifrovanie, StringBuilder otUpraveny){
        pocetRiadkov=100;
        transpozicia=new TabulkovaTranspozicia(otNasifrovanie,null);
        vlastnosti = new Vlastnosti(otUpraveny);
        odhadKluca =new OdhadKluca(vlastnosti);
        permutacia=new Permutacia(vlastnosti,odhadKluca,jazyk);

    }
    public void setJazyk(Jazyk jazyk,StringBuilder ot) {
        permutacia.setJazyk(jazyk);
        vlastnosti.samohlaskySpoluhlasky(ot.toString());
        vlastnosti.setStatistikaBigramovUsporiadana(vlastnosti.ngramy(ot,2,true,true));
    }
    public void setTextPreTranspoziciu(StringBuilder textPreTranspoziciu){
        transpozicia.setTextNaSifrovanie(textPreTranspoziciu);
    }
    public void setPocetRiadkov(int pocetRiadkov) {
        this.pocetRiadkov=pocetRiadkov;
        permutacia.setPocetRiadkov(pocetRiadkov);
    }

    public void otestovatKorpus(StringBuilder text,ArrayList<String> kluce,int pocetKlucov){
        transpozicia.setTextNaSifrovanie(text);
        transpozicia.setPocetRiadkov(pocetRiadkov,permutacia);
        int index=0;
        double pocetNeuspesnychPermutacii=0;
        double pocetNeuhadnutychKlucov=0;

        for(int i=0;i<pocetKlucov;i++) {
            String kluc = kluce.get(index);
            transpozicia.setKluc(kluc);

            odhadKluca.najdiDlzkuKluca(transpozicia.getZasifrovanyText().toString(), transpozicia);
            index++;
            if (transpozicia.getKluc().length() != odhadKluca.getDlzkaKluca()) {
                pocetNeuhadnutychKlucov++;
                pocetNeuspesnychPermutacii++;
                continue;
            }
            permutacia.setDlzkaKluca(odhadKluca.getDlzkaKluca());
            permutacia.setBlokyZt(transpozicia.getZtVBlokoch());
            permutacia.hladatPermutaciu();
            if (!transpozicia.jeZhodnaPermutacia(permutacia.getPermutacia())) {
                pocetNeuspesnychPermutacii++;
            }
        }
        double uspesnostKlucov=((pocetKlucov-pocetNeuhadnutychKlucov)/pocetKlucov)*100;
        double uspesnostPermutacii=((pocetKlucov-pocetNeuspesnychPermutacii)/pocetKlucov)*100;
        System.out.print(pocetRiadkov+"        "+uspesnostKlucov+"         "+uspesnostPermutacii);
        System.out.println();

    }

    public void otestovatRiadky(ArrayList<String> kluce){
        pocetRiadkov=100;
        for(int d=pocetRiadkov;d<=100;d+=100){
            transpozicia.setPocetRiadkov(d,permutacia);
            int index=0;
            double pocetNeuspesnychPermutacii=0;
            double pocetNeuhadnutychKlucov=0;

            for(int i=0;i<kluce.size();i++){
                String kluc=kluce.get(index);
                transpozicia.setKluc(kluc);

                odhadKluca.najdiDlzkuKluca(transpozicia.getZasifrovanyText().toString(),transpozicia);
                index++;
                if (transpozicia.getKluc().length() != odhadKluca.getDlzkaKluca()) {
                    pocetNeuhadnutychKlucov++;
                    pocetNeuspesnychPermutacii++;
                    continue;
                }
                permutacia.setDlzkaKluca(odhadKluca.getDlzkaKluca());
                permutacia.setBlokyZt(transpozicia.getZtVBlokoch());
                permutacia.hladatPermutaciu();
                if (!transpozicia.jeZhodnaPermutacia(permutacia.getPermutacia())) {
                    pocetNeuspesnychPermutacii++;
                }
            }
            double uspesnostKlucov=((kluce.size()-pocetNeuhadnutychKlucov)/kluce.size())*100;
            double uspesnostPermutacii=((kluce.size()-pocetNeuspesnychPermutacii)/kluce.size())*100;
            System.out.print(d+"        "+uspesnostKlucov+"         "+uspesnostPermutacii);
            System.out.println();
        }
    }
    public void statistikaKorpusov(ArrayList<String> kluce, ArrayList<StringBuilder> texty){
        for(int i=0;i<3;i++){
            transpozicia.setTextNaSifrovanie(texty.get(i));
            otestovatRiadky(kluce);
            System.out.println();
        }
    }

}