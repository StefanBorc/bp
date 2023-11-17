package org.example.aplikacia;

import org.example.sifry.TabulkovaTranspozicia;

import java.util.ArrayList;

import static org.example.Main.POCIATOCNA_VELKOST;
import static org.example.Main.vygenerujKluc;

public class Priebeh {
    private TabulkovaTranspozicia transpozicia;
    private OdhadKluca odhadKluca;
    private Permutacia permutacia;
    private Vlastnosti vlastnosti;

    public Priebeh(StringBuilder otNasifrovanie,StringBuilder otUpraveny){
        String kluc=vygenerujKluc(10,30);

        transpozicia=new TabulkovaTranspozicia(otNasifrovanie,kluc);
        spustiSifrovanie(kluc,POCIATOCNA_VELKOST,otNasifrovanie);
        vlastnosti = new Vlastnosti(otUpraveny);
        odhadKluca =new OdhadKluca(vlastnosti,transpozicia);
        permutacia=new Permutacia(vlastnosti,odhadKluca);
        //transpozicia.vytlacPermutaciu();
        //permutacia.vytlacTestovanuPermutaciu();

    }
    private void spustiSifrovanie(String kluc, int n,StringBuilder text) {
        transpozicia.zasifrujText(text, kluc, n);
    }

    public void otestujRozneKluce(ArrayList<String> kluce){
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
            permutacia.hladajPermutaciu();

            if (!transpozicia.jeZhodnaPermutacia(permutacia.getPermutacia())) {
                pocetNeuspesnychPermutacii++;
            }
        }
        double uspesnostKlucov=((kluce.size()-pocetNeuhadnutychKlucov)/kluce.size())*100;
        double uspesnostPermutacii=((kluce.size()-pocetNeuspesnychPermutacii)/kluce.size())*100;
        System.out.print(POCIATOCNA_VELKOST+" "+uspesnostKlucov+" "+uspesnostPermutacii);
        System.out.println();


    }


}
