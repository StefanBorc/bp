package org.example.aplikacia;

import lombok.Getter;

import java.io.*;

public class Text {
    @Getter
    private StringBuilder upravenyText;
    @Getter
    private StringBuilder text;

    public Text(String nazovSuboru) throws IOException {
        vratSubor(nazovSuboru);
        vratSuborU(nazovSuboru);
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
}
