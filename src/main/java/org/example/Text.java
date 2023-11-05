package org.example;

import lombok.Getter;

import java.io.*;
import java.util.ArrayList;

public class Text {
    @Getter
    private StringBuilder upravenyText;
    @Getter
    private StringBuilder text;
    @Getter
    private ArrayList<String> kluce;

    public Text(String nazovSuboru) throws IOException {
        vratSubor(nazovSuboru);
        vratSuborU(nazovSuboru);
        nacitajKluce();
    }
    protected StringBuilder vratSubor(String nazovSuboru) throws IOException {
        text = new StringBuilder();

        BufferedReader br = otvorSubor(nazovSuboru);
        String st;

        while ((st = br.readLine()) != null) {
            text.append(st);
        }
        StringBuilder upravenyText=new StringBuilder();
        for(char c: text.toString().toCharArray()){
            if(Character.isAlphabetic(c) || Character.isSpaceChar(c)){
                upravenyText.append(Character.toUpperCase(c));
            }
            else{
                upravenyText.append(" ");
            }

        }
        br.close();
        return upravenyText;
    }
    protected StringBuilder vratSuborU(String nazovSuboru) throws IOException {
        upravenyText =new StringBuilder();
        text = new StringBuilder();

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
        File k=new File("KLUCE.txt");
        BufferedReader br =new BufferedReader(new FileReader(k));
        String st;
        while ((st = br.readLine()) != null) {
            kluce.add(st);
        }
        br.close();


    }
}
