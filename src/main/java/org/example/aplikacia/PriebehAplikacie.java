package org.example.aplikacia;

import lombok.Getter;
import lombok.Setter;
import org.example.Text;
import org.example.lustenie_tabulkovej_transpozicie.Priebeh;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

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
    @Setter@Getter
    private int dolnaHranicaBigramov;
    @Setter@Getter
    private int hornaHranicaBigramov;
    @Setter
    private JLabel zvolenyPocetRiadkov;
    @Setter
    private JLabel zvolenyPocetKlucov;
    @Setter
    private JLabel statistikyPreLabely;
    @Setter
    private JLabel znakyStatistika;
    @Setter
    private JLabel bigramyStatistika;
    @Setter
    private JLabel trigramyStatistika;
    @Setter
    private JLabel pokusStatistika;
    @Setter
    private JLabel dolnaHranicaBigramovText;
    @Setter
    private JLabel hornaHranicaBigramovText;
    @Setter
    private JLabel horneHraniceTrigramovText;
    @Setter
    private JLabel dolneHraniceTrigramovText;
    private String pokus;
    @Setter
    private JProgressBar progressBar;
    @Setter
    private JButton ladenieBigramov;
    @Setter
    private JButton ladenieTrigramov;
    private int[] hraniceTrigramov;
    private ArrayList<String> kluceNaPorovnanie1;
    private ArrayList<String> kluceNaPorovnanie2;
    public PriebehAplikacie() throws IOException {
        super();
        //kluceNaPorovnanie1 =new ArrayList<>(List.of("drugs","state","value","steps","miles","speed","horse","nurse","phone","lemon","lists","bored","angry","japan","hurry"));
        //kluceNaPorovnanie2=new ArrayList<>(List.of("ciphertexts","cryptography","quickstepping","complexifying","biotechnologies", "heliocentricism","denationalization","decriminalisation","keytextishiddenhere","warisstartingtomorrow"));
        cisloKorpusu=0;
        dolnaHranicaBigramov=200;
        hornaHranicaBigramov=20;
        pocetRiadkov=100;
        jazyk=Jazyk.DE;
        text=new Text(jazyk.toString());
        priebeh=new Priebeh(pocetRiadkov,jazyk,text.getTextyNaSifrovanie().get(0),text.getUpravenyText());
        priebeh.setDolnaHranicaBigramov(200);
        priebeh.setHornaHranicaBigramov(20);
        hraniceTrigramov=new int[]{50,100,300,2500,3500,4000,6000};
    //    priebeh.otestujRozneKluce(kluceNaPorovnanie2,text.getTextyNaSifrovanie());
    }
    protected void inicializaciaNadpisov(){
        statistikyPreLabely.setText("<html><font color='red'>"+PODIEL_NADPIS+" <font color='black'>"+priebeh.podielSamohlasokSpoluhlasokOT()+
                "<br><br><br><br><font color='red'>"+PRIEMERNA_DLZKA_SLOV_NADPIS+" <font color='black'>"+priebeh.priemernaDlzkaSlovOT()+" znakov"+
                "<br><br><br><br><font color='red'>"+INDEX_KOINCIDENCIE_NADPIS+" <font color='black'>"+priebeh.indexKoincidencieOT()+"<html>");
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
        znakyStatistika.setText("<html><font color='red'>" +ZNAKY_NADPIS+"<br><font color='black'>"+ textZnaky + "</html>");
        bigramyStatistika.setText("<html><font color='red'>" +BIGRAMY_NADPIS+"<br><font color='black'>"+ textBigramy + "</html>");
        trigramyStatistika.setText("<html><font color='red'>" +TRIGRAMY_NADPIS+"<br><font color='black'>"+ textTrigramy + "</html>");
    }
    @Override
    public void stateChanged(ChangeEvent e) {

        if(((JSlider)e.getSource()).getName().equals(PREPINAC_KLUCOV)){
            if(((JSlider)e.getSource()).getValue()%500==0){
                zvolenyPocetKlucov.setText("Počet klúčov : "+((JSlider)e.getSource()).getValue());
                pocetKlucov=((JSlider) e.getSource()).getValue();
            }
        }
        else if(((JSlider)e.getSource()).getName().equals(PREPINAC_RIADKOV)){
            if(((JSlider)e.getSource()).getValue()%100==0){
                zvolenyPocetRiadkov.setText("Počet riadkov : "+((JSlider)e.getSource()).getValue());
                pocetRiadkov=((JSlider) e.getSource()).getValue();
                priebeh.setPocetRiadkov(pocetRiadkov);
            }
        }
        else if(((JSlider)e.getSource()).getName().equals(PREPINAC_HORNEJ_HRANICE_BIGRAMOV)){
                hornaHranicaBigramovText.setText("<html>horná hranica <br> bigramov : <html>"+((JSlider)e.getSource()).getValue());
                hornaHranicaBigramov=((JSlider) e.getSource()).getValue();
                priebeh.setHornaHranicaBigramov(hornaHranicaBigramov);
        }
        else if(((JSlider)e.getSource()).getName().equals(PREPINAC_DOLNEJ_HRANICE_BIGRAMOV)){
                dolnaHranicaBigramovText.setText("<html>dolná hranica <br> bigramov : <html>"+((JSlider)e.getSource()).getValue());
                dolnaHranicaBigramov=((JSlider) e.getSource()).getValue();
                priebeh.setDolnaHranicaBigramov(dolnaHranicaBigramov);
        }
        else{
            char c=((JSlider) e.getSource()).getName().charAt(0);
            int index=(int)c-48;
            hraniceTrigramov[index]=((JSlider) e.getSource()).getValue();
            updatujOchylkyTrigramov(index,((JSlider) e.getSource()).getValue());
            String text="";
            if(index>2){
                text="<html>dolné hranice <br> trigramov : <html>";
                for(int i=3;i<hraniceTrigramov.length;i++){
                    text+=hraniceTrigramov[i]+" ";
                }
                dolneHraniceTrigramovText.setText(text);
            }
            else {
                text="<html>horné hranice <br> trigramov :<br> <html>";
                for(int i=0;i<3;i++){
                    text+=hraniceTrigramov[i]+" ";
                }
                horneHraniceTrigramovText.setText(text);

            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(SPUSTIT)){
            ((JButton) e.getSource()).setEnabled(false);
            new Thread(() -> {
                    pokus = priebeh.otestovatKorpus(text.getTextyNaSifrovanie().get(cisloKorpusu), text.getKluce(), pocetKlucov, progressBar);
                    final String vysledokPokusu = pokus;
                    SwingUtilities.invokeLater(() -> {
                        pokusStatistika.setText("<html>" + POKUS_NADPIS + "<br><br>" + vysledokPokusu + "<html>");
                        ((JButton) e.getSource()).setEnabled(true);
                    });
            }).start();

        }
        else if(e.getActionCommand().equals(LADENIE_BIGRAMOV)){
            ladenieBigramov.setEnabled(false);
            ladenieTrigramov.setEnabled(true);
            priebeh.setModLadenia(LADENIE_BIGRAMOV);
        }
        else if(e.getActionCommand().equals(LADENIE_TRIGRAMOV)){
            ladenieBigramov.setEnabled(true);
            ladenieTrigramov.setEnabled(false);
            priebeh.setModLadenia(LADENIE_TRIGRAMOV);
        }
        else {
            String polozka = ((JComboBox) e.getSource()).getSelectedItem().toString();
            if (polozka.length() > 1) {
                try {
                    text.setNazovSuboru(polozka);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                jazyk = Jazyk.valueOf(polozka);
                priebeh.setJazyk(text.getUpravenyText());
                priebeh.setDolnaHranicaBigramov(dolnaHranicaBigramov);
                priebeh.setHornaHranicaBigramov(hornaHranicaBigramov);
                priebeh.setHraniceTrigramov(hraniceTrigramov);
                inicializaciaNadpisov();
            } else {
                switch (polozka) {
                    case "1":
                        cisloKorpusu = 0;
                        break;
                    case "2":
                        cisloKorpusu = 1;
                        break;
                    case "3":
                        cisloKorpusu = 2;
                        break;
                    default:
                        break;
                }
                priebeh.setTextPreTranspoziciu(text.getTextyNaSifrovanie().get(cisloKorpusu));
            }
        }
    }
    private void updatujOchylkyTrigramov(int indexOchylky,int n){
        priebeh.setHranicaTrigramov(indexOchylky,n);
    }
}
