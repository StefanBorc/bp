package org.example.sifry;

import lombok.Getter;
import lombok.Setter;
import org.example.Sifra;

import java.util.ArrayList;

import static org.example.Pokus.INT_MIN;

public class TabulkovaTranspozicia extends Sifra {

    private int[] poradie;
    @Setter@Getter
    private String kluc;
    @Getter
    private StringBuilder zasifrovanyText;
    private int nZasifrovanychZnakov;

    public TabulkovaTranspozicia(String kluc) {
        nZasifrovanychZnakov=0;
        this.kluc = kluc;
        zasifrovanyText = new StringBuilder();
    }
    private void setnZasifrovanychZnakov(int n){
        nZasifrovanychZnakov=n;
        sifrovanie(getUpravenyText(),kluc);
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
        poradie=new int[heslo.length()];
        ArrayList<Integer> p=new ArrayList<>();
        for(int i=0;i<heslo.length();i++){
            p.add((int) heslo.charAt(i));
        }

        int pismenoVPoradi=0;
        for(int i=0;i<heslo.length();i++){
            var index=min(p);
            poradie[index]=pismenoVPoradi;
            p.set(index,999);
            pismenoVPoradi++;
        }
    }
    public void zasifrujText(StringBuilder text,String kluc,int n){
        nZasifrovanychZnakov=n;
        sifrovanie(text,kluc);
    }
    @Override
    public void sifrovanie(StringBuilder text,String kluc) {
        this.kluc = kluc;
        vratPoradie(kluc);
        int pocetStlpcov = kluc.length();
        int pocetRiadkov = nZasifrovanychZnakov/kluc.length();
        char[][] tabulka = new char[pocetRiadkov][pocetStlpcov];
        int index = 0;

        while (index != nZasifrovanychZnakov) {

            for (int i = 0; i < pocetRiadkov; i++) {
                for (int j = 0; j < pocetStlpcov; j++) {
                    if (index < nZasifrovanychZnakov) {
                        tabulka[i][j] = text.charAt(index);
                        index++;
                    }
                }
            }
            int stlpec = -999;
            int stlpecVPoradi = 0;
            while (stlpec != pocetStlpcov && stlpecVPoradi < pocetStlpcov) {
                if (stlpecVPoradi != poradie[0]) {
                    for (int i = 0; poradie[i] != stlpecVPoradi && pocetStlpcov > i; i++) {
                        stlpec = i + 1;
                    }
                } else {
                    stlpec = 0;
                }
                for (int r = 0; r < pocetRiadkov; r++) {
                    if (index <= text.length()) {
                        if (Character.isAlphabetic(tabulka[r][stlpec])) {
                            zasifrovanyText.append(tabulka[r][stlpec]);
                        }
                    }
                }
                stlpecVPoradi++;
            }
        }
      //  System.out.println(zasifrovanyText);
    }


}
