package org.example.lustenie_tabulkovej_transpozicie;

import org.example.aplikacia.Jazyk;
import org.example.sifry.TabulkovaTranspozicia;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.example.Text.text;
import static org.example.aplikacia.Aplikacia.LADENIE_BIGRAMOV;

public class Priebeh {
    private TabulkovaTranspozicia transpozicia;
    private OdhadKluca odhadKluca;
    private Permutacia permutacia;
    private Vlastnosti vlastnosti;
    private int pocetRiadkov ;

    public Priebeh(int pocetRiadkov,Jazyk jazyk, StringBuilder otNasifrovanie, StringBuilder otUpraveny){
        this.pocetRiadkov=pocetRiadkov;
        transpozicia=new TabulkovaTranspozicia(otNasifrovanie,null);
        vlastnosti = new Vlastnosti(otUpraveny);
        odhadKluca =new OdhadKluca(vlastnosti);
        permutacia=new Permutacia(vlastnosti,odhadKluca,jazyk);

    }
    public void setJazyk(StringBuilder ot) {
        vlastnosti.setStatistikaSamohlasokSpoluhlasok(vlastnosti.samohlaskySpoluhlasky(ot.toString()));
        vlastnosti.setStatistikaZnakov(vlastnosti.ngramy(ot,1,true,true,true));
        vlastnosti.setStatistikaBigramovUsporiadana(vlastnosti.ngramy(ot,2,true,true,true));
        vlastnosti.setStatistikaTrigramovUsporiadana(vlastnosti.ngramy(ot,3,true,true,true));
        vlastnosti.priemernaDlzkaSlov(text.toString());
        vlastnosti.setIndexKoincidencie(vlastnosti.indexKoincidencie(ot));
        transpozicia.setPocetRiadkov(pocetRiadkov);
    }
    public void setModLadenia(String mod){
        if(mod.equals(LADENIE_BIGRAMOV)){
            permutacia.setZahrnutTrigramy(false);
        }
        else{
            permutacia.setZahrnutTrigramy(true);
        }
    }
    public void setTextPreTranspoziciu(StringBuilder textPreTranspoziciu){
        transpozicia.setTextNaSifrovanie(textPreTranspoziciu);
    }
    public void setPocetRiadkov(int pocetRiadkov) {
        this.pocetRiadkov=pocetRiadkov;
        transpozicia.setPocetRiadkov(pocetRiadkov);
    }

    public double podielSamohlasokSpoluhlasokOT(){
        return vlastnosti.getStatistikaSamohlasokSpoluhlasok();
    }
    public List<Map.Entry<String, Double>> bigramyOT(){
        return vlastnosti.getStatistikaBigramovUsporiadana();
    }
    public List<Map.Entry<String, Double>> trigramyOT(){
        return vlastnosti.getStatistikaTrigramovUsporiadana();
    }
    public List<Map.Entry<String,Double>> znakyOT(){
        return vlastnosti.getStatistikaZnakov();
    }
    public double priemernaDlzkaSlovOT(){
        return vlastnosti.getPriemernaDlzkaSlov();
    }
    public double indexKoincidencieOT(){
        return vlastnosti.getIndexKoincidencie();
    }
    public void setDolnaHranicaBigramov(int n){
       permutacia.odhadnutDolnuHranicuBigramov(n);
    }
    public void setHornaHranicaBigramov(int n){
        permutacia.odhadnutHornuHranicuBigramov(n);
    }
    public String otestovatKorpus(StringBuilder text, ArrayList<String> kluce, int pocetKlucov, JProgressBar progressBar){
        transpozicia.setTextNaSifrovanie(text);
        transpozicia.setPocetRiadkov(pocetRiadkov);
        int index=0;
        double pocetNeuspesnychPermutacii=0;
        double pocetNeuhadnutychKlucov=0;

        for(int i=0;i<pocetKlucov;i++) {

            int progres = (int)(((double) i / pocetKlucov) * 100)+1;
            progressBar.setValue(progres);

            String kluc = kluce.get(index);
            transpozicia.setKluc(kluc,transpozicia.getTextNaSifrovanie());
            //StringBuilder zt=new StringBuilder(transpozicia.getZasifrovanyText());
            //transpozicia.setKluc(kluc,zt);
            odhadKluca.najdiDlzkuKluca(transpozicia.getZasifrovanyText().toString(), transpozicia);
            //odhadKluca.setDlzkaKluca(kluc.length(),transpozicia);
            index++;
            if (transpozicia.getKluc().length() != odhadKluca.getDlzkaKluca()) {
                pocetNeuhadnutychKlucov++;
                pocetNeuspesnychPermutacii++;
                continue;
            }
            permutacia.setDlzkaKluca(odhadKluca.getDlzkaKluca());
            permutacia.setBlokyZt(transpozicia.getZtVBlokoch());
            permutacia.hladatPermutaciu();
            //transpozicia.vytlacPermutaciu();
            //permutacia.vytlacTestovanuPermutaciu();

            if (!transpozicia.jeZhodnaPermutacia(permutacia.getPermutacia())) {
                pocetNeuspesnychPermutacii++;
            }

        }
        double uspesnostKlucov=((pocetKlucov-pocetNeuhadnutychKlucov)/pocetKlucov)*100;
        double uspesnostPermutacii=((pocetKlucov-pocetNeuspesnychPermutacii)/pocetKlucov)*100;
        System.out.print(pocetRiadkov+"        "+uspesnostKlucov+"         "+uspesnostPermutacii);
        System.out.println();
        return "odhadnute kluce : "+uspesnostKlucov+"%"+"<br><br>"+"odhadnute poradia stlpcov : "+
                uspesnostPermutacii+"%"+"<br><br>"+"celkova uspesnost odhadnutia permutacii : "+(uspesnostPermutacii/uspesnostKlucov)*100+" % ";
    }
    public void otestujRozneKluce(ArrayList<String> kluce, ArrayList<StringBuilder> texty){
        pocetRiadkov=100;
        for(int r=1;r<3;r++){
            transpozicia.setTextNaSifrovanie(texty.get(r));
            for(int d=pocetRiadkov;d<=500;d+=100){
                transpozicia.setPocetRiadkov(d);
                int index=0;
                double pocetNeuspesnychPermutacii=0;
                double pocetNeuhadnutychKlucov=0;
                for(int i=0;i<kluce.size();i++){
                    String kluc=kluce.get(index);
                    transpozicia.setKluc(kluc,transpozicia.getTextNaSifrovanie());

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
            System.out.println();
        }

    }
}