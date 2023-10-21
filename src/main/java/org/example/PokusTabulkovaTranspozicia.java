package org.example;

import lombok.Getter;
import org.example.sifry.TabulkovaTranspozicia;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class PokusTabulkovaTranspozicia {
    public static int POCIATOCNA_VELKOST=200;
    @Getter
    private StringBuilder upravenyText;
    @Getter
    private StringBuilder text;
    private TabulkovaTranspozicia transpozicia;
    private final ArrayList<String> vygenerovaneKluce;
    private Invariant invariant;

    public PokusTabulkovaTranspozicia(String nazovSuboru, String kluc) throws IOException {
        vygenerovaneKluce=new ArrayList<>();
        vygenerujKluce(10);
        transpozicia = new TabulkovaTranspozicia(kluc);

        upravenyText = transpozicia.vratSuborU(nazovSuboru);
        text = transpozicia.vratSubor(nazovSuboru);

        spustiSifrovanie(kluc,POCIATOCNA_VELKOST);

        invariant = new Invariant(text, upravenyText);

        transpoziciaPorovnajDlzkyKlucov(transpozicia.getZasifrovanyText());
    }

    private void spustiSifrovanie(String kluc,int n) {
        transpozicia.zasifrujText(upravenyText, kluc,n);
    }
    private void vygenerujKluce(int n){
        Map<String,Integer> kluce=new HashMap<>();
        Random r=new Random();
        int minI = 10;
        int maxI = 30;
        int minC = 'a';
        int maxC = 'z';
        while(kluce.size()!=n){
            StringBuilder s= new StringBuilder("");
            for(int i=0;i<r.nextInt(maxI-minI) + minI;i++){
                s.append((char) (r.nextInt(maxC - minC) + minC));
            }
            kluce.merge(s.toString(),1,Integer::sum);
        }
        for ( Map.Entry<String, Integer> vstup : kluce.entrySet()) {
            String kluc = vstup.getKey();
            vygenerovaneKluce.add(kluc);
        }

    }
    /* skusame rozne pravdepodobne bigramy */
    protected int zhodnostBigramov(StringBuilder zasifrovanyText, int oKolko) {
        ArrayList<Character> spoluhlasky = new ArrayList<>(List.of('B', 'C', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'F', 'Z', 'X'));
        ArrayList<Character> samohlasky = new ArrayList<>(List.of('A', 'E', 'I', 'O', 'U', 'Y'));

        Map<String, Integer> najdene = new HashMap<>();
        for (int i = 0; i < zasifrovanyText.length() - oKolko * 2; i++) {

            String ngram = new String();
            ngram += zasifrovanyText.charAt(i);
            int j = i + oKolko;
            ngram += zasifrovanyText.charAt(j);
            if ((spoluhlasky.contains(ngram.charAt(0)) && samohlasky.contains(ngram.charAt(1)))
                    || (samohlasky.contains(ngram.charAt(0)) && spoluhlasky.contains(ngram.charAt(1)))) {
                najdene.merge(ngram, 1, Integer::sum);
            }
        }
        List<Map.Entry<String, Integer>> vytriedeneNgramy = najdene.entrySet()
                .stream()
                .sorted((vstup1, vstup2) -> vstup2.getValue().compareTo(vstup1.getValue()))
                .collect(Collectors.toList());

        return vytriedeneNgramy.get(0).getValue();
    }

    /* porovnavame najlepsie vysledky klucov */
    private int transpoziciaPorovnajDlzkyKlucov(StringBuilder zt) {
        Map<Integer, Integer> mozneKluce = new HashMap<>();
        int maxIndex = 0;
        int max = 0;
        for (int i = 10; i <= 30; i++) {
            mozneKluce.put(i, zhodnostBigramov(zt, i));
        }

        for (Map.Entry<Integer, Integer> vstup : mozneKluce.entrySet()) {
            if (vstup.getValue() > max) {
                max = vstup.getValue();
                maxIndex = vstup.getKey();
            }
        }
        System.out.println(maxIndex);
        return maxIndex;
    }

    /* testujeme uspesnost klucov pre tabulkovu transpoziciu */
    private void transpozicnaUspesnostKlucov() {

        double pocetUhadnutychKlucov = 0;
        double pocetVsetkychKlucov = vygenerovaneKluce.size();

        for (String s : vygenerovaneKluce) {
            int dlzkaKluca = s.length();
            transpozicia.setKluc(s);
            transpozicia.sifrovanie(upravenyText, s);
            int moznyKluc = transpoziciaPorovnajDlzkyKlucov(transpozicia.getZasifrovanyText());
            System.out.println("kluc " + transpozicia.getKluc().length());
            System.out.println("odhad " + moznyKluc);
            if (dlzkaKluca == moznyKluc) {
                pocetUhadnutychKlucov++;
            }


        }
        double vysledok = (pocetUhadnutychKlucov / pocetVsetkychKlucov) * 100;

        System.out.println(vysledok);
    }


}
