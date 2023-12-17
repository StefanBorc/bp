package org.example.lustenie_tabulkovej_transpozicie;

import org.example.sifry.TabulkovaTranspozicia;

import java.util.ArrayList;

import static org.example.Main.vygenerujKluc;

public class Priebeh {
    private TabulkovaTranspozicia transpozicia;
    private OdhadKluca odhadKluca;
    private Permutacia permutacia;
    private Vlastnosti vlastnosti;
    private int pocetRiadkov ;

    public Priebeh(StringBuilder otNasifrovanie, StringBuilder otUpraveny){
        String kluc=vygenerujKluc(10,30);
        pocetRiadkov=100;
        transpozicia=new TabulkovaTranspozicia(otNasifrovanie,kluc);
        spustiSifrovanie(kluc,pocetRiadkov,otNasifrovanie);
        vlastnosti = new Vlastnosti(otUpraveny);
        odhadKluca =new OdhadKluca(vlastnosti,transpozicia);
        permutacia=new Permutacia(vlastnosti,odhadKluca);

    }
    private void spustiSifrovanie(String kluc, int n,StringBuilder text) {
        transpozicia.zasifrujText(text, kluc, n);
    }

    public void otestujRozneKluce(ArrayList<String> kluce){
        pocetRiadkov=500;
        for(int d=pocetRiadkov;d<900;d+=100){
            transpozicia.setPocetRiadkov(d,permutacia);
            int index=0;
            double pocetNeuspesnychPermutacii=0;
            double pocetNeuhadnutychKlucov=0;
            for(int i=0;i<kluce.size();i++){
                String kluc=kluce.get(index);
                transpozicia.setKluc(kluc);
                transpozicia.vytlacPermutaciu();
                odhadKluca.najdiDlzkuKluca(transpozicia.getZasifrovanyText().toString(),transpozicia);
                index++;
                if (transpozicia.getKluc().length() != odhadKluca.getDlzkaKluca()) {
                    pocetNeuhadnutychKlucov++;
                    pocetNeuspesnychPermutacii++;
                    continue;
                }
                permutacia.setDlzkaKluca(odhadKluca.getDlzkaKluca());
                permutacia.setBlokyZt(transpozicia.getZtVBlokoch());

                permutacia.hladajPermutaciu();

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
    public void otestujKlucPermutaciu(int n,String kluc){
        transpozicia.setPocetRiadkov(n,permutacia);
        transpozicia.setKluc(kluc);
        odhadKluca.najdiDlzkuKluca(transpozicia.getZasifrovanyText().toString(),transpozicia);
        permutacia.setDlzkaKluca(odhadKluca.getDlzkaKluca());
        System.out.println(odhadKluca.getDlzkaKluca());
        permutacia.setBlokyZt(transpozicia.getZtVBlokoch());
        permutacia.hladajPermutaciu();
        transpozicia.vytlacPermutaciu();
        permutacia.vytlacTestovanuPermutaciu();

    }
    public void otestujKorpusy(ArrayList<String> kluce,ArrayList<StringBuilder> texty){
        for(int i=0;i<3;i++){
            transpozicia.setTextNaSifrovanie(texty.get(i));
            otestujRozneKluce(kluce);
            System.out.println();
        }
    }


}
