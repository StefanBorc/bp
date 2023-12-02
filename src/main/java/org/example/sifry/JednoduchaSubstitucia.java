package org.example.sifry;

import lombok.Getter;
import lombok.Setter;
import org.example.Sifra;

import java.util.ArrayList;


public class JednoduchaSubstitucia extends Sifra {
    private String kluc;

    private char zaciatokSifrovania;
    private ArrayList<Character> zasifrovanaAbeceda;
    @Getter
    private StringBuilder zasifrovanyText;
    @Setter
    private StringBuilder textOt;
    @Setter
    private int pocetZnakov;

    public void setKluc(String kluc) {
        this.kluc = kluc;
        zasifrujText();
    }
    public JednoduchaSubstitucia(StringBuilder textOt,char c, String kluc,int pocetZnakov) {
        this.zaciatokSifrovania = Character.toUpperCase(c);
        this.textOt=textOt;
        zasifrovanaAbeceda=new ArrayList<>();
        zasifrovanyText = new StringBuilder();
        this.kluc = kluc;
        this.pocetZnakov=pocetZnakov;
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

        int index = 0;
        int pocitadlo=abeceda.indexOf(zaciatokSifrovania);

        for (int i = 0; opakovanaAbeceda.size() != 26; i++) {
            if(i==kluc.length()){
                break;
            }
            if(opakovanaAbeceda.contains(kluc.charAt(i))){
                index++;
                continue;
            }

            if (!opakovanaAbeceda.contains(kluc.charAt(index)) && pocitadlo < abeceda.size()) {

                abeceda.set(pocitadlo, kluc.charAt(index));
                opakovanaAbeceda.add(kluc.charAt(index));
                pocitadlo++;
                index++;
            }

            if (index > abeceda.size()) {
                index = 0;
            }

            if (index == kluc.length()) {
                for (int j = 0; j < abeceda.size() - opakovanaAbeceda.size(); j++) {
                    for (char c = 'A'; c <= 'Z'; c++) {
                        if (!opakovanaAbeceda.contains(c)) {
                            if (pocitadlo > abeceda.size() - 1) {
                                pocitadlo = 0;
                            }
                            abeceda.set(pocitadlo, c);
                            opakovanaAbeceda.add(c);
                            pocitadlo++;

                        }
                    }
                }
                break;
            }
        }
        return abeceda;
    }
    public void zasifrujText(){
        sifrovanie(textOt,kluc);
    }
    @Override
    public void sifrovanie(StringBuilder text,String kluc)  {
        this.kluc=kluc;
        zasifrovanaAbeceda=new ArrayList<>(zasifrujAbecedu());
        ArrayList<Character> abeceda = new ArrayList<>();
        zasifrovanyText=new StringBuilder();
        if(zasifrovanaAbeceda!=null){
            for (char c = 'A'; c <= 'Z'; c++) {
                abeceda.add(c);
            }
            int i=0;
            for(char c: text.toString().toCharArray()){
                if(i==pocetZnakov){
                    break;
                }
                if((int)c-65<26){
                    zasifrovanyText.append(zasifrovanaAbeceda.get((int)c-65));
                    i++;
                }
            }
        }
    }
}
