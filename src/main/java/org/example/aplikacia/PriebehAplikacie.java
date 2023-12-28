package org.example.aplikacia;

import lombok.Setter;
import org.example.Text;
import org.example.lustenie_tabulkovej_transpozicie.Priebeh;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    private JLabel bigramyStatistika;
    @Setter
    private JLabel pokusStatistika;
    private String pokus;
    private List<Map.Entry<String,Double>> bigramyOT;
    public PriebehAplikacie() throws IOException {
        super();
        jazyk=Jazyk.DE;
        cisloKorpusu=0;
        pocetRiadkov=500;
        text=new Text(jazyk.toString());
        priebeh=new Priebeh(pocetRiadkov,jazyk,text.getTextyNaSifrovanie().get(0),text.getUpravenyText());
    }
    protected void inicializaciaNadpisov(){
        podielHlasokStatistika.setText(PODIEL_NADPIS+" "+priebeh.podielSamohlasokSpoluhlasokOT());
        bigramyOT=priebeh.bigramyOT();
        String textBigramy=" ";
        for(var bigram:bigramyOT){
            textBigramy+=bigram.toString();
            textBigramy+="<br>";
        }
        bigramyStatistika.setText("<html>" +BIGRAMY_NADPIS+"<br>"+ textBigramy + "</html>");
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
