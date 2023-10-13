package org.example.sifry;

import lombok.Getter;
import lombok.Setter;
import org.example.Sifra;

import java.io.IOException;
import java.util.ArrayList;

import static org.example.Pokus.INT_MIN;

public class TabulkovaTranspozicia extends Sifra {

    private int[] poradie;
    @Setter@Getter
    private String kluc;
    @Getter
    private StringBuilder zasifrovanyText;

    public TabulkovaTranspozicia(String kluc) {
        super();
        this.kluc = kluc;
        zasifrovanyText = new StringBuilder();
    }

    private int min(ArrayList<Integer> p) {
        int minimum = p.get(0);
        int index = 0;
        for (int i = 1; i < p.size(); i++) {
            if (minimum > p.get(i)) {
                minimum = p.get(i);
                index = i;
            }
        }
        return index;
    }

    protected void vratPoradie(String heslo) {
        poradie = new int[heslo.length()];
        ArrayList<Integer> p = new ArrayList<>();
        for (int i = 0; i < heslo.length(); i++) {
            p.add((int) heslo.charAt(i));
        }
        int pismenoVPoradi = 1;
        for (int i = 0; i < heslo.length(); i++) {
            var index = min(p);
            poradie[index] = pismenoVPoradi;
            p.set(index, 999);
            pismenoVPoradi++;
        }
    }

    @Override
    public void sifrovanie(StringBuilder text,String kluc) {
        this.kluc=kluc;
        vratPoradie(kluc);

        int pocetStlpcov = kluc.length();
        int pocetRiadkov = kluc.length();
        char[][] tabulka;
        int blok = 0;
        int index = 0;

        while (index != text.length()) {
            tabulka=new char[pocetStlpcov][pocetStlpcov];
            for (int i = 0; i < pocetStlpcov; i++) {
                for (int j = 0; j < pocetStlpcov; j++) {
                    if (index < text.length()) {
                        tabulka[i][j] = text.charAt(index);
                        index++;
                    }
                    else{
                        tabulka[i][j] = 'p';
                    }
                }
            }
            int stlpec = INT_MIN;
            int stlpecVPoradi = 1;
            while (stlpec != pocetStlpcov && stlpecVPoradi <= pocetStlpcov) {
                if (stlpecVPoradi != poradie[0]) {
                    for (int i = 0; poradie[i] != stlpecVPoradi && pocetStlpcov > i; i++) {
                        stlpec = i + 1;
                    }
                } else {
                    stlpec = 0;
                }
                for (int r = 0; r < pocetStlpcov; r++) {
                    if (index <= text.length()) {
                        zasifrovanyText.append(tabulka[r][stlpec]);
                    }
                }
                stlpecVPoradi++;
            }
        }
    }


}
