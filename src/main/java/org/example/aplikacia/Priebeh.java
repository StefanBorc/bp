package org.example.aplikacia;

import org.example.sifry.TabulkovaTranspozicia;

import java.io.IOException;
import java.util.*;

import static org.example.Main.POCIATOCNA_VELKOST;
import static org.example.Main.vygenerujKluc;

public class Priebeh {
    private ArrayList<String> kluce;
    private TabulkovaTranspozicia transpozicia;
    private OdhadKluca odhadKluca;
    private Permutacia permutacia;

    public Priebeh(StringBuilder otUpraveny){
        double pocetNeuspesnychPermutacii=0;
        double pocetNeuhadnutychKlucov=0;
        int n=100;
        for(int i=0;i<n;i++){
            String kluc=vygenerujKluc();
            transpozicia=new TabulkovaTranspozicia(otUpraveny,vygenerujKluc());
            spustiSifrovanie(kluc,POCIATOCNA_VELKOST,otUpraveny);
            Bigramy bigramy = new Bigramy(otUpraveny);
            odhadKluca =new OdhadKluca(bigramy,transpozicia);
            permutacia=new Permutacia(bigramy,transpozicia,odhadKluca.getDlzkaKluca());
            if (!transpozicia.jeZhodnaPermutacia(permutacia.getPermutacia())) {
                pocetNeuspesnychPermutacii++;
            }
            if (transpozicia.getKluc().length() != odhadKluca.getDlzkaKluca()) {
                pocetNeuhadnutychKlucov++;
            }

        }
        double uspesnostKlucov=((n-pocetNeuhadnutychKlucov)/n)*100;
        double uspesnostPermutacii=((n-pocetNeuspesnychPermutacii)/n)*100;
        System.out.println(uspesnostPermutacii+"% - permutacie");
        System.out.println(uspesnostKlucov+"% - kluce");


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
    protected double otestujRozneKluce(ArrayList<String> kluce){

        double uspesnostTestu=0;
        int pocetNeuhadnutychKlucov=0;
        for (String s : kluce) {
            transpozicia.setKluc(s);
            odhadKluca.najdiDlzkuKluca(transpozicia.getZasifrovanyText().toString(),transpozicia);
            transpozicia.vytlacPermutaciu();
            permutacia.hladajPermutaciu(transpozicia.getZtVBlokoch());
            permutacia.vytlacTestovanuPermutaciu();
            /*
            if (transpozicia.jeZhodnaPermutacia(permutacia.getPermutacia())) {
                uspesnostTestu++;
            }
            else if (transpozicia.getKluc().length() != odhadKluca.getDlzkaKluca()) {
                pocetNeuhadnutychKlucov++;
            }
             */

        }
        System.out.println();
        System.out.println("neuhadnute kluce "+(pocetNeuhadnutychKlucov/kluce.size())*100+"%");

        System.out.println(("uspesnost permutacii "+(uspesnostTestu/kluce.size())*100)+"%");
        return (uspesnostTestu/kluce.size())*100;
    }


}
