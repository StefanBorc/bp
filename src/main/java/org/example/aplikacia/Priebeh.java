package org.example.aplikacia;

import org.example.sifry.TabulkovaTranspozicia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    private ArrayList<String> vygenerujKluce(int n) {
        ArrayList<String> vygenerovaneKluce=new ArrayList<>();
        Map<String, Integer> kluce = new HashMap<>();
        Random r = new Random();
        int minI = 10;
        int maxI = 30;
        int minC = 'a';
        int maxC = 'z';
        while (kluce.size() != n) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < r.nextInt(maxI - minI) + minI; i++) {
                s.append((char) (r.nextInt(maxC - minC) + minC));
            }
            kluce.merge(s.toString(), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> vstup : kluce.entrySet()) {
            String kluc = vstup.getKey();
            vygenerovaneKluce.add(kluc);
        }
        return vygenerovaneKluce;
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
