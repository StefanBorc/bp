package org.example.aplikacia;

import org.example.sifry.TabulkovaTranspozicia;

import java.io.IOException;
import java.util.*;

import static org.example.Main.POCIATOCNA_VELKOST;

public class Priebeh {

    private TabulkovaTranspozicia transpozicia;
    private OdhadKluca odhadKluca;
    private Permutacia permutacia;

    public Priebeh(StringBuilder otUpraveny,String kluc) throws IOException {
        transpozicia=new TabulkovaTranspozicia(kluc);
        spustiSifrovanie(kluc,POCIATOCNA_VELKOST,otUpraveny);
        Bigramy bigramy = new Bigramy(otUpraveny);
        odhadKluca =new OdhadKluca(bigramy,transpozicia);
        permutacia=new Permutacia(bigramy,transpozicia,odhadKluca.getDlzkaKluca());
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

            ArrayList<Integer> najdenaPermutacia = permutacia.hladajPermutaciu(transpozicia.getZtVBlokoch());
            if (transpozicia.jeZhodnaPermutacia(najdenaPermutacia) && transpozicia.getKluc().length() == odhadKluca.getDlzkaKluca()) {
                uspesnostTestu++;
            } else if (transpozicia.getKluc().length() != odhadKluca.getDlzkaKluca()) {
                pocetNeuhadnutychKlucov++;
            }
        }
        System.out.println("neuhadnute kluce "+(pocetNeuhadnutychKlucov/kluce.size())*100+"%");
        return (uspesnostTestu/kluce.size())*100;
    }

}
