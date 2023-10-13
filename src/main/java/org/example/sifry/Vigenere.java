package org.example.sifry;

import lombok.Getter;
import lombok.Setter;
import org.example.Sifra;

import java.io.IOException;

public class Vigenere extends Sifra {
    private int[] poradie;
    @Getter@Setter
    private String kluc;
    @Getter
    private StringBuilder zasifrovanyText;

    public Vigenere(String kluc) {
        this.kluc = kluc;
        zasifrovanyText = new StringBuilder();
    }

    private void poradieHesla() {
        poradie = new int[kluc.length()];
        int indexPismena = 0;
        for (char p : kluc.toCharArray()) {
            int ascii = Character.toLowerCase(p);
            int index = ascii - 'a';
            poradie[indexPismena] = index;
            indexPismena++;
        }
    }
    private void upravKluc(){
        kluc=kluc.toLowerCase();
    }
    @Override
    public void sifrovanie(StringBuilder text,String kluc)  {
        this.kluc=kluc;
        upravKluc();
        int pocitadlo = 0;
        int blok = 0;

        poradieHesla();

        for (int i = 0; i < text.length(); i++) {

            if (pocitadlo > kluc.length() - 1) {
                pocitadlo = 0;
            }
            char posunutePismeno = (char) (text.charAt(i) + poradie[pocitadlo]);
            if ((int) posunutePismeno > 90) {
                posunutePismeno -= 26;
            }
            pocitadlo++;
            zasifrovanyText.append(posunutePismeno);

            blok++;
            if (blok == kluc.length()) {
                //  sifra+=' ';
                blok = 0;
            }
        }
    }
}
