package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String kluc="abcdefghijklmnopq";

        System.out.println("dlzka kluca:"+kluc.length());
        PokusTabulkovaTranspozicia p=new PokusTabulkovaTranspozicia("SK2.txt",kluc);
        Invariant pokusOT=new Invariant(p.getText(),p.getUpravenyText());

       // System.out.println(p.getZt());

    }

}