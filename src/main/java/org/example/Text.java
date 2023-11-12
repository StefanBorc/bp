package org.example;

import lombok.Getter;

import java.io.*;
import java.util.ArrayList;

public class Text {
    @Getter
    private StringBuilder upravenyText;
    @Getter
    private StringBuilder textNaSifrovanie;
    @Getter
    private ArrayList<String> kluce;

    public Text(String nazovSuboru) throws IOException {

        vratSuborU(nazovSuboru);
        nacitajKluce();
    }

    protected StringBuilder vratSuborU(String nazovSuboru) throws IOException {
        upravenyText =new StringBuilder();
        StringBuilder text = new StringBuilder();

        BufferedReader br = otvorSubor(nazovSuboru);
        String st;

        while ((st = br.readLine()) != null) {
            text.append(st);
        }

        for(char c: text.toString().toCharArray()){
            if(Character.isAlphabetic(c)){
                upravenyText.append(Character.toUpperCase(c));
            }
        }
        br.close();
        return upravenyText;
    }
    private BufferedReader otvorSubor(String nazovSuboru) throws FileNotFoundException {
        File slovaTxt = new File("C:\\Users\\stefa\\OneDrive\\Počítač\\pokus\\"+nazovSuboru);
        return new BufferedReader(new FileReader(slovaTxt));
    }
    protected void nacitajKluce() throws IOException {
        kluce=new ArrayList<>();
        textNaSifrovanie=new StringBuilder();
        StringBuilder text=new StringBuilder();
        File k=new File("KLUCE.txt");
        File p=new File("OT.txt");
        BufferedReader br =new BufferedReader(new FileReader(k));
        String st;
        while ((st = br.readLine()) != null) {
            kluce.add(st);
        }
        br=new BufferedReader(new FileReader(p));
        while ((st = br.readLine()) != null) {
            text.append(st);
        }
        for(char c: text.toString().toCharArray()){
            if(Character.isAlphabetic(c)){
                textNaSifrovanie.append(Character.toUpperCase(c));
            }
        }
        br.close();


    }
}
