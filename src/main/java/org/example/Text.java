package org.example;

import lombok.Getter;

import java.io.*;
import java.util.ArrayList;

public class Text {
    @Getter
    private StringBuilder upravenyText;
    public static StringBuilder text;
    @Getter
    private ArrayList<StringBuilder> textyNaSifrovanie;
    @Getter
    private ArrayList<String> kluce;
    private String nazovSuboru;
    public void setNazovSuboru(String nazovSuboru) throws IOException {
        this.nazovSuboru=nazovSuboru;
        vratOT();
        vratOTSMedzerami();
        nacitajKluceText();

    }
    public Text(String nazovSuboru) throws IOException {
        this.nazovSuboru=nazovSuboru;
        vratOTSMedzerami();
        vratOT();
        nacitajKluceText();
    }

    protected StringBuilder vratOT() throws IOException {

        upravenyText =new StringBuilder();
        StringBuilder text = new StringBuilder();

        BufferedReader br = otvorSubor();
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
    protected StringBuilder vratOTSMedzerami() throws IOException {

        text =new StringBuilder();
        StringBuilder text = new StringBuilder();

        BufferedReader br = otvorSubor();
        String st;

        while ((st = br.readLine()) != null) {
            text.append(st);
        }

        for(char c: text.toString().toCharArray()){
            if(Character.isAlphabetic(c) || Character.isSpaceChar(c)){
                this.text.append(Character.toUpperCase(c));
            }
        }
        br.close();
        return text;
    }
    private BufferedReader otvorSubor() throws FileNotFoundException {
        File slovaTxt = new File("OT",nazovSuboru+".txt");
        return new BufferedReader(new FileReader(slovaTxt));
    }
    protected void nacitajKluceText() throws IOException {
        kluce=new ArrayList<>();
        textyNaSifrovanie=new ArrayList<>();


        File k=new File("KLUCE.txt");
        BufferedReader br =new BufferedReader(new FileReader(k));
        String st;
        while ((st = br.readLine()) != null) {
            kluce.add(st);
        }
        int i;
        File d=new File("KORPUSY",nazovSuboru);
        for(i=1;i<=3;i++){
            File p=new File(d,i+".txt");
            br=new BufferedReader(new FileReader(p));
            StringBuilder text=new StringBuilder();
            while ((st = br.readLine()) != null) {
                text.append(st);
            }
            StringBuilder t=new StringBuilder();
            for(char c: text.toString().toCharArray()){
                if(Character.isAlphabetic(c)){
                    t.append(Character.toUpperCase(c));
                }
            }
            textyNaSifrovanie.add(new StringBuilder(t));
        }
         br.close();
    }
    public ArrayList<String> nacitajKluce(String dir) throws IOException {
        ArrayList<String> kluce=new ArrayList<>();
        File k=new File(dir+".txt");
        BufferedReader br =new BufferedReader(new FileReader(k));
        String st;
        while ((st = br.readLine()) != null) {
            kluce.add(st);
        }
        return kluce;
    }
}
