package org.example;

import org.example.sifry.JednoduchaSubstitucia;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String kluc="aioixnczouafwwaygxjcz";

        //pozor scitavas bigramy v pokuse
        new Pokus("SK.txt",kluc);
        System.out.println("kluc " + kluc.length());
    }

}