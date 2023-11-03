package org.example.aplikacia;

import org.example.sifry.TabulkovaTranspozicia;


import java.util.*;

import static org.example.Main.POCIATOCNA_VELKOST;
import static org.example.Main.vygenerujKluc;

public class Priebeh {
    private TabulkovaTranspozicia transpozicia;
    private OdhadKluca odhadKluca;
    private Permutacia permutacia;
    private Bigramy bigramy;

    public Priebeh(StringBuilder otUpraveny){
        String kluc=vygenerujKluc(10,30);
        transpozicia=new TabulkovaTranspozicia(otUpraveny,kluc);
        spustiSifrovanie(kluc,POCIATOCNA_VELKOST,otUpraveny);
        bigramy = new Bigramy(otUpraveny);
        odhadKluca =new OdhadKluca(bigramy,transpozicia);
        permutacia=new Permutacia(bigramy,transpozicia,odhadKluca.getDlzkaKluca());

        otestujRozneKluce(100);


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
    protected void otestujRozneKluce(int n){
        ArrayList<String> kluce=vygenerujKluce(n);

        for(int j=0;j<10;j++){
            double pocetNeuspesnychPermutacii=0;
            double pocetNeuhadnutychKlucov=0;
            for(int i=0;i<n;i++){
                String kluc=kluce.get(i);
                transpozicia.setKluc(kluc);
                odhadKluca.najdiDlzkuKluca(transpozicia.getZasifrovanyText().toString(),transpozicia);
                permutacia.setDlzkaKluca(odhadKluca.getDlzkaKluca());
                permutacia.hladajPermutaciu(transpozicia.getZtVBlokoch());

                if (transpozicia.getKluc().length() != odhadKluca.getDlzkaKluca()) {
                    pocetNeuhadnutychKlucov++;
                    pocetNeuspesnychPermutacii++;
                    continue;
                }
                if (!transpozicia.jeZhodnaPermutacia(permutacia.getPermutacia())) {
                    pocetNeuspesnychPermutacii++;
                }
            }
            double uspesnostKlucov=((n-pocetNeuhadnutychKlucov)/(n))*100;
            double uspesnostPermutacii=((n-pocetNeuspesnychPermutacii)/(n))*100;
            System.out.println(uspesnostKlucov);
            System.out.println(uspesnostPermutacii);
        }



    }


}
