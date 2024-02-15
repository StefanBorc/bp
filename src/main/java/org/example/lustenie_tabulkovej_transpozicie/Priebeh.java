package org.example.lustenie_tabulkovej_transpozicie;

import org.example.aplikacia.Jazyk;
import org.example.sifry.TabulkovaTranspozicia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void setJazyk(Jazyk jazyk,StringBuilder ot) {
        permutacia.setJazyk(jazyk);
        vlastnosti.setStatistikaSamohlasokSpoluhlasok(vlastnosti.samohlaskySpoluhlasky(ot.toString()));
        vlastnosti.setStatistikaZnakov(vlastnosti.ngramy(ot,1,true,true,true));
        vlastnosti.setStatistikaBigramovUsporiadana(vlastnosti.ngramy(ot,2,true,true,true));
        vlastnosti.setStatistikaTrigramovUsporiadana(vlastnosti.ngramy(ot,3,true,true,true));
        vlastnosti.priemernaDlzkaSlov(ot.toString());
        vlastnosti.setIndexKoincidencie(vlastnosti.indexKoincidencie(ot));
    }
    public void setTextPreTranspoziciu(StringBuilder textPreTranspoziciu){
        transpozicia.setTextNaSifrovanie(textPreTranspoziciu);
    }
    public void setPocetRiadkov(int pocetRiadkov) {
        this.pocetRiadkov=pocetRiadkov;
        permutacia.setPocetRiadkov(pocetRiadkov);
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
    public String otestovatKorpus(StringBuilder text,ArrayList<String> kluce,int pocetKlucov){
        transpozicia.setTextNaSifrovanie(text);
        transpozicia.setPocetRiadkov(pocetRiadkov,permutacia);
        int index=0;
        double pocetNeuspesnychPermutacii=0;
        double pocetNeuhadnutychKlucov=0;

        for(int i=0;i<pocetKlucov;i++) {
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
        String pokus="odhadnute kluce : "+uspesnostKlucov+"%"+"<br>"+"odhadnute poradia stlpcov : "+uspesnostPermutacii+"%";
        return pokus;
    }

}