package org.example.sifry;

import lombok.Getter;
import org.example.Sifra;

import java.util.ArrayList;


public class JednoduchaSubstitucia extends Sifra {
    private String kluc;
    private char zaciatokSifrovania;
    private ArrayList<Character> zasifrovanaAbeceda;
    @Getter
    private StringBuilder zasifrovanyText;

    public JednoduchaSubstitucia(char c, String kluc) {
        this.zaciatokSifrovania = c;
        zasifrovanaAbeceda=new ArrayList<>();
        zasifrovanyText = new StringBuilder();
        this.kluc = kluc;
    }

    private ArrayList<Character> zasifrujAbecedu() {
        kluc=kluc.toUpperCase();
        if(kluc.length()>=26){
            return null;
        }
        ArrayList<Character> abeceda = new ArrayList<>();
        ArrayList<Character> opakovanaAbeceda = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            abeceda.add(c);
        }
        int pocitadlo = 0;
        int index = 0;
        for (int i = 0; opakovanaAbeceda.size() != 26; i++) {
            if (zaciatokSifrovania == abeceda.get(i)) {
                abeceda.set(i, kluc.charAt(0));
                opakovanaAbeceda.add(kluc.charAt(0));
                index = i;
                pocitadlo++;
                index++;
            }
            if (pocitadlo > 0) {
                if (!opakovanaAbeceda.contains(kluc.charAt(pocitadlo)) && index < abeceda.size()) {
                    abeceda.set(index, kluc.charAt(pocitadlo));
                    opakovanaAbeceda.add(kluc.charAt(pocitadlo));
                    index++;
                }
                pocitadlo++;
                if (index > abeceda.size()) {
                    index = 0;
                }
            }
            if (pocitadlo == kluc.length()) {
                for (int j = 0; j < abeceda.size() - opakovanaAbeceda.size(); j++) {
                    for (char c = 'A'; c <= 'Z'; c++) {
                        if (!opakovanaAbeceda.contains(c)) {
                            abeceda.set(index, c);
                            opakovanaAbeceda.add(c);
                            index++;
                            if (index > abeceda.size() - 1) {
                                index = 0;
                            }
                        }
                    }
                }
                break;
            }
        }
        return abeceda;
    }

    @Override
    public void sifrovanie(StringBuilder text,String kluc)  {
        this.kluc=kluc;
        zasifrovanaAbeceda=zasifrujAbecedu();
        ArrayList<Character> abeceda = new ArrayList<>();
        if(zasifrovanaAbeceda!=null){
            for (char c = 'A'; c <= 'Z'; c++) {
                abeceda.add(c);
            }
            for(char c: text.toString().toCharArray()){
                if((int)c-65<26){
                    zasifrovanyText.append(zasifrovanaAbeceda.get((int)c-65));
                }
            }
        }

    }
}
