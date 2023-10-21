package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String kluc="notaktomusimprerobit";

        PokusTabulkovaTranspozicia p=new PokusTabulkovaTranspozicia("SK3.txt",kluc);
        Invariant pokusOT=new Invariant(p.getText(),p.getUpravenyText());

        System.out.println(kluc.length());
    }

}