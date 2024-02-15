package org.example.aplikacia;

import lombok.Setter;
import org.example.Text;
import org.example.lustenie_tabulkovej_transpozicie.Priebeh;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.io.IOException;

import static org.example.aplikacia.Aplikacia.*;

public class PriebehAplikacie extends UniverzalnyAdapter {
    private Text text;
    private Priebeh priebeh;
    private Jazyk jazyk;
    private int cisloKorpusu;
    @Setter
    private int pocetRiadkov;
    @Setter
    private int pocetKlucov;
    @Setter
    private JLabel zvolenyPocetRiadkov;
    @Setter
    private JLabel zvolenyPocetKlucov;
    @Setter
    private JLabel podielHlasokStatistika;
    @Setter
    private JLabel znakyStatistika;
    @Setter
    private JLabel bigramyStatistika;
    @Setter
    private JLabel trigramyStatistika;
    @Setter
    private JLabel priemernaDlzkaSlovStatistika;
    @Setter
    private JLabel indexKoincidencieStatistika;
    @Setter
    private JLabel pokusStatistika;
    private String pokus;

    public PriebehAplikacie() throws IOException {
        super();
        jazyk=Jazyk.DE;
        cisloKorpusu=0;
        pocetRiadkov=100;
        text=new Text(jazyk.toString());
        priebeh=new Priebeh(pocetRiadkov,jazyk,text.getTextyNaSifrovanie().get(0),text.getUpravenyText());
    }
    protected void inicializaciaNadpisov(){
        podielHlasokStatistika.setText(PODIEL_NADPIS+" "+priebeh.podielSamohlasokSpoluhlasokOT());
        var znakyOT=priebeh.znakyOT();
        int i=0;
        String textZnaky=" ";
        for(var znak:znakyOT){
            char c=znak.getKey().charAt(0);
            if(c>90 || c<65)
                continue;
            textZnaky+=znak.toString();
            textZnaky+="<br>";
            i++;
        }
        var bigramyOT=priebeh.bigramyOT();
        String textBigramy=" ";
        String textTrigramy=" ";
        for(var bigram:bigramyOT){
            textBigramy+=bigram.toString();
            textBigramy+="<br>";
        }
        var trigramyOT=priebeh.trigramyOT();
        i=0;
        for(var trigram:trigramyOT){
            if(i==500){
                break;
            }
            textTrigramy+=trigram.toString();
            textTrigramy+="<br>";
            i++;
        }
        priemernaDlzkaSlovStatistika.setText(PRIEMERNA_DLZKA_SLOV_NADPIS+" "+priebeh.priemernaDlzkaSlovOT());
        indexKoincidencieStatistika.setText(INDEX_KOINCIDENCIE_NADPIS+" "+priebeh.indexKoincidencieOT());
        znakyStatistika.setText("<html><font color='red'>" +ZNAKY_NADPIS+"<br><font color='black'>"+ textZnaky + "</html>");
        bigramyStatistika.setText("<html><font color='red'>" +BIGRAMY_NADPIS+"<br><font color='black'>"+ textBigramy + "</html>");
        trigramyStatistika.setText("<html><font color='red'>" +TRIGRAMY_NADPIS+"<br><font color='black'>"+ textTrigramy + "</html>");
    }
    @Override
    public void stateChanged(ChangeEvent e) {
        if(((JSlider)e.getSource()).getName().equals(PREPINAC_KLUCOV)){
            if(((JSlider)e.getSource()).getValue()%500==0){
                zvolenyPocetKlucov.setText("Pocet klucov : "+((JSlider)e.getSource()).getValue());
                pocetKlucov=((JSlider) e.getSource()).getValue();
            }
        }
        else if(((JSlider)e.getSource()).getName().equals(PREPINAC_RIADKOV)){
            if(((JSlider)e.getSource()).getValue()%100==0){
                zvolenyPocetRiadkov.setText("Pocet riadkov : "+((JSlider)e.getSource()).getValue());
                pocetRiadkov=((JSlider) e.getSource()).getValue();
                priebeh.setPocetRiadkov(pocetRiadkov);
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(SPUSTIT)){
            pokus=priebeh.otestovatKorpus(text.getTextyNaSifrovanie().get(cisloKorpusu),text.getKluce(),pocetKlucov);
            pokusStatistika.setText("<html>"+POKUS_NADPIS+"<br>"+pokus+"<html>");
            return;
        }
        String item=((JComboBox)e.getSource()).getSelectedItem().toString();
        if(item.length()>1){
            try {
                text.setNazovSuboru(item);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            jazyk = Jazyk.valueOf(item);
            priebeh.setJazyk(jazyk,text.getUpravenyText());
            inicializaciaNadpisov();
        }
        else{
            switch(item){
                case "1" : cisloKorpusu=0;
                break;
                case "2" : cisloKorpusu=1;
                break;
                case "3" : cisloKorpusu=2;
                break;
                default:break;
            }
            priebeh.setTextPreTranspoziciu(text.getTextyNaSifrovanie().get(cisloKorpusu));
        }
    }
}
