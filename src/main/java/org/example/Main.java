package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String kluc="abcdefghijklm";
        //
        System.out.println("dlzka kluca:"+kluc.length());
        PokusTabulkovaTranspozicia p=new PokusTabulkovaTranspozicia("SK1.txt",kluc);

    }

}