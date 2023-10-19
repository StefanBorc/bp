package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String kluc="qwertyzanglictiny";

        Pokus pokus= new Pokus("SK3.txt",kluc);
      //  System.out.println("kluc " + kluc.length());

        Invariant pokusOT=new Invariant(pokus.getText(),pokus.getUpravenyText());

        pokusOT.rozmedzieOpakovanychPismen(pokusOT.getTextBezMedzier());
    }

}