package org.example.lustenie_tabulkovej_transpozicie;

import lombok.Getter;
import lombok.Setter;
import org.example.aplikacia.Jazyk;
import org.example.sifry.TabulkovaTranspozicia;

import java.util.ArrayList;

public class Priebeh {
    @Getter
    private TabulkovaTranspozicia transpozicia;
    @Getter
    private OdhadKluca odhadKluca;
    @Getter
    private Permutacia permutacia;
    @Getter
    private Vlastnosti vlastnosti;
    @Setter
    private int pocetRiadkov ;

    public Priebeh(StringBuilder otNasifrovanie, StringBuilder otUpraveny){
        pocetRiadkov=100;
        transpozicia=new TabulkovaTranspozicia(otNasifrovanie,null);
        vlastnosti = new Vlastnosti(otUpraveny);
        odhadKluca =new OdhadKluca(vlastnosti);
        permutacia=new Permutacia(vlastnosti,odhadKluca);
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
                /*
                transpozicia.vytlacPermutaciu();
                permutacia.vytlacTestovanuPermutaciu();
                System.out.println();
                 */

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
    public void otestovatKorpus(ArrayList<String> kluce,int pocetRiadkov,int pocetKlucov){

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
    public void otestujKlucPermutaciu(int n,String kluc){
        transpozicia.setPocetRiadkov(n,permutacia);
        transpozicia.setKluc(kluc);
        odhadKluca.najdiDlzkuKluca(transpozicia.getZasifrovanyText().toString(),transpozicia);
        permutacia.setDlzkaKluca(odhadKluca.getDlzkaKluca());
        System.out.println(odhadKluca.getDlzkaKluca());
        permutacia.setBlokyZt(transpozicia.getZtVBlokoch());
        permutacia.hladatPermutaciu();
        transpozicia.vytlacPermutaciu();
        permutacia.vytlacTestovanuPermutaciu();

    }
    public void statistikaKorpusov(ArrayList<String> kluce, ArrayList<StringBuilder> texty){
        for(int i=0;i<3;i++){
            transpozicia.setTextNaSifrovanie(texty.get(i));
            otestovatRiadky(kluce);
            System.out.println();
        }
    }
    public void statistikaDlzkyTextov(ArrayList<String> kluce){
        pocetRiadkov=100;
        for(int d=pocetRiadkov;d<900;d+=100){
            transpozicia.setPocetRiadkov(d,permutacia);
            int index=0;
            int dlzkaKlucov=0;
            int dlzkaZt=0;
            for(int i=0;i<kluce.size();i++){
                String kluc=kluce.get(index);
                transpozicia.setKluc(kluc);
                dlzkaKlucov+=kluc.length();
                dlzkaZt+=transpozicia.getZasifrovanyText().length();
                index++;

            }
            System.out.println(dlzkaZt/kluce.size());
        }
    }


}
