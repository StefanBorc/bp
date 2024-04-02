package org.example.aplikacia;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Aplikacia {
    public static final String SPUSTIT="LÚŠTIŤ";
    public static final String LADENIE_BIGRAMOV="BIGRAMY";
    public static final String LADENIE_TRIGRAMOV="TRIGRAMY";
    public static final String PREPINAC_HORNEJ_HRANICE_BIGRAMOV="PREPINAC_HORNEJ_HRANICE_BIGRAMOV";
    public static final String PREPINAC_DOLNEJ_HRANICE_BIGRAMOV="PREPINAC_DOLNEJ_HRANICE_BIGRAMOV";
    public static final String PREPINAC_RIADKOV="PREPINAC_RIADKOV";
    public static final String PREPINAC_KLUCOV="PREPINAC_KLUCOV";
    public static final String PODIEL_NADPIS ="Podiel hlasok : ";
    public static final String BIGRAMY_NADPIS="bigramy : ";
    public static final String POKUS_NADPIS="Výsledok pokusu : ";
    public static final String PRIEMERNA_DLZKA_SLOV_NADPIS="Priemerná dĺžka slov : ";
    public static final String INDEX_KOINCIDENCIE_NADPIS="Index koincidencie : ";
    public static final String TRIGRAMY_NADPIS="trigramy : ";
    public static final String ZNAKY_NADPIS="znaky : ";
    public Aplikacia() throws IOException {
        JFrame frame = new JFrame("Tabulkova transpozicia");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setResizable(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();

        PriebehAplikacie priebehAplikacie = new PriebehAplikacie();
        frame.addKeyListener(priebehAplikacie);

        JPanel menu1 = new JPanel();
        JPanel menu2 = new JPanel();
        JButton tlacidloSpustit = new JButton(SPUSTIT);

        JProgressBar progressBar=new JProgressBar(0,100);
        progressBar.setStringPainted(true);

        tlacidloSpustit.setBackground(Color.RED);
        tlacidloSpustit.addActionListener(priebehAplikacie);
        tlacidloSpustit.setFocusable(false);

        JSlider prepinacKlucov = vytvoritJSlider(priebehAplikacie,true,PREPINAC_KLUCOV,1000,5000,3000,false,500,1000,Color.LIGHT_GRAY);
        JSlider prepinacRiadkov = vytvoritJSlider(priebehAplikacie,true,PREPINAC_RIADKOV,100,1000,100,false,100,100,Color.LIGHT_GRAY);
        JSlider prepinacHornejHraniceBigramov = vytvoritJSlider(priebehAplikacie,true,PREPINAC_HORNEJ_HRANICE_BIGRAMOV,0,580,30,true,10,10,Color.GREEN);
        JSlider prepinacDolnejHraniceBigramov =vytvoritJSlider(priebehAplikacie,true,PREPINAC_DOLNEJ_HRANICE_BIGRAMOV,0,580,200,true,10,10,Color.RED);
        ArrayList<JSlider> prepinaceOdchyliekTrigramov=new ArrayList<>();
        int[] pociatocneOdchylkyTrigramov=new int[]{60,100,300,2500,3500,4000,6000};
        Color farba;
        for(int i=0;i<7;i++){
            if(i>2){
                farba=Color.RED;
            }
            else{
                farba=Color.GREEN;
            }
            prepinaceOdchyliekTrigramov.add(vytvoritJSlider(priebehAplikacie,false,""+i,0,7500,pociatocneOdchylkyTrigramov[i],true,20,20,farba));
        }


        JLabel zvolenyPocetKlucov=new JLabel("Počet klúčov : "+prepinacKlucov.getValue());
        JLabel zvolenyPocetRiadkov=new JLabel("Počet riadkov : "+prepinacRiadkov.getValue());
        priebehAplikacie.setPocetKlucov(prepinacKlucov.getValue());
        priebehAplikacie.setPocetRiadkov(prepinacRiadkov.getValue());

        JComboBox prepinacKorpusov =new JComboBox<>();
        prepinacKorpusov.addItem(Jazyk.DE);
        prepinacKorpusov.addItem(Jazyk.SK);
        prepinacKorpusov.addItem(Jazyk.CZ);
        prepinacKorpusov.addItem(Jazyk.EN);
        prepinacKorpusov.setBackground(Color.LIGHT_GRAY);
        prepinacKorpusov.addActionListener(priebehAplikacie);

        JComboBox prepinacVzorky =new JComboBox<>();
        prepinacVzorky.addItem("1");
        prepinacVzorky.addItem("2");
        prepinacVzorky.addItem("3");
        prepinacVzorky.setBackground(Color.LIGHT_GRAY);
        prepinacVzorky.addActionListener(priebehAplikacie);

        menu1.setLayout(new GridLayout(1,3));
        menu1.setBackground(Color.LIGHT_GRAY);
        menu1.add(prepinacKorpusov);
        menu1.add(prepinacVzorky);
        menu1.add(tlacidloSpustit);
        menu1.add(progressBar);

        menu2.setLayout(new GridLayout(2,2));
        menu2.add(zvolenyPocetKlucov);
        zvolenyPocetKlucov.setHorizontalAlignment(SwingConstants.CENTER);
        menu2.add(prepinacKlucov);
        menu2.add(zvolenyPocetRiadkov);
        zvolenyPocetRiadkov.setHorizontalAlignment(SwingConstants.CENTER);
        menu2.add(prepinacRiadkov);
        menu2.setBackground(Color.LIGHT_GRAY);
        frame.add(menu1, BorderLayout.NORTH);
        frame.add(menu2,BorderLayout.SOUTH);

        JLabel statistikyPreLabely = new JLabel();
        JLabel znakyText=new JLabel();
        JLabel bigramyText = new JLabel();
        JLabel trigramyText = new JLabel();
        JLabel pokusText = new JLabel(POKUS_NADPIS);
        //znakyText.setHorizontalAlignment(SwingConstants.CENTER);
        //bigramyText.setHorizontalAlignment(SwingConstants.CENTER);
        //trigramyText.setHorizontalAlignment(SwingConstants.CENTER);

        statistikyPreLabely.setVerticalAlignment(SwingConstants.TOP);
        bigramyText.setVerticalAlignment(SwingConstants.TOP);

        pokusText.setVerticalAlignment(SwingConstants.TOP);

        JPanel hlavnyPanel = new JPanel(new GridLayout(1, 3));
        JPanel statistikyOt1=new JPanel(new GridLayout(2,1));

        JScrollPane pokusPanel = new JScrollPane(pokusText);
        JScrollPane podielHlasokPanel = new JScrollPane(statistikyPreLabely);

        statistikyOt1.add(podielHlasokPanel);
        JScrollPane znakyPanel = new JScrollPane(znakyText);
        statistikyOt1.add(znakyPanel);
        hlavnyPanel.add(statistikyOt1);

        JPanel statistikyOt2=new JPanel(new GridLayout(2,1));
        JScrollPane bigramyPanel = new JScrollPane(bigramyText);
        JScrollPane trigramyPanel = new JScrollPane(trigramyText);
        statistikyOt2.add(bigramyPanel);
        statistikyOt2.add(trigramyPanel);
        hlavnyPanel.add(statistikyOt2);


        JPanel statistikyOt3=new JPanel(new GridLayout(3,1));
        JPanel prepinanieModov=new JPanel(new GridLayout(1,2));
        JPanel prepinanieOdchyliekBigramov= new JPanel(new GridLayout(1,2));
        JPanel zvoleneOdchylkyBigramov = new JPanel(new GridLayout(1,2));
        JPanel zvoleneOdchylkyTrigramov= new JPanel(new GridLayout(1,2));
        JPanel mody1=new JPanel(new GridLayout(5,1));
        JPanel mody2=new JPanel(new GridLayout(1,7));

        JLabel prepinanieModovNadpis=new JLabel("Lúštenie pomocou :");
        JLabel zvolenaHornaOdchylkaBigramov= new JLabel("<html>horná odchýlka <br> bigramov : <html>"+prepinacHornejHraniceBigramov.getValue());
        JLabel zvolenaDolnaOdchylkaBigramov= new JLabel("<html>dolná odchýlka <br> bigramov : <html>"+prepinacDolnejHraniceBigramov.getValue());
        JLabel zvoleneHorneOdchylkyTrigramov= new JLabel("<html>horné odchýlky <br> trigramov : <br>" +
                " 60 100 300<html>"+prepinacHornejHraniceBigramov.getValue());
        JLabel zvoleneDolneOdchylkyTrigramov= new JLabel("<html>dolné odchýlky <br> trigramov : <br>" +
                "2500 3500 4000 6000 <html>"+prepinacDolnejHraniceBigramov.getValue());
        zvolenaHornaOdchylkaBigramov.setHorizontalAlignment(SwingConstants.CENTER);
        zvolenaDolnaOdchylkaBigramov.setHorizontalAlignment(SwingConstants.CENTER);
        zvoleneHorneOdchylkyTrigramov.setHorizontalAlignment(SwingConstants.CENTER);
        zvoleneDolneOdchylkyTrigramov.setHorizontalAlignment(SwingConstants.CENTER);
        prepinanieModovNadpis.setHorizontalAlignment(SwingConstants.CENTER);

        JButton ladenieBigramov=new JButton(LADENIE_BIGRAMOV);
        JButton ladenieTrigramov=new JButton(LADENIE_TRIGRAMOV);
        ladenieBigramov.setEnabled(false);
        ladenieBigramov.addActionListener(priebehAplikacie);
        ladenieTrigramov.addActionListener(priebehAplikacie);

        prepinanieModov.add(ladenieBigramov);
        prepinanieModov.add(ladenieTrigramov);
        prepinanieOdchyliekBigramov.add(prepinacHornejHraniceBigramov);
        prepinanieOdchyliekBigramov.add(prepinacDolnejHraniceBigramov);
        zvoleneOdchylkyBigramov.add(zvolenaHornaOdchylkaBigramov);
        zvoleneOdchylkyBigramov.add(zvolenaDolnaOdchylkaBigramov);
        zvoleneOdchylkyTrigramov.add(zvoleneHorneOdchylkyTrigramov);
        zvoleneOdchylkyTrigramov.add(zvoleneDolneOdchylkyTrigramov);


        mody1.add(prepinanieModovNadpis);
        mody1.add(prepinanieModov);
        mody1.add(zvoleneOdchylkyBigramov);
        mody1.add(prepinanieOdchyliekBigramov);
        mody1.add(zvoleneOdchylkyTrigramov);

        for(int i=0;i<7;i++){
            mody2.add(prepinaceOdchyliekTrigramov.get(i));
        }

        statistikyOt3.add(mody1);
        statistikyOt3.add(mody2);
        statistikyOt3.add(pokusPanel);
        hlavnyPanel.add(statistikyOt3);

        priebehAplikacie.setZvolenyPocetKlucov(zvolenyPocetKlucov);
        priebehAplikacie.setZvolenyPocetRiadkov(zvolenyPocetRiadkov);

        priebehAplikacie.setStatistikyPreLabely(statistikyPreLabely);;
        priebehAplikacie.setZnakyStatistika(znakyText);
        priebehAplikacie.setBigramyStatistika(bigramyText);
        priebehAplikacie.setTrigramyStatistika(trigramyText);
        priebehAplikacie.setPokusStatistika(pokusText);
        priebehAplikacie.inicializaciaNadpisov();
        priebehAplikacie.setProgressBar(progressBar);
        priebehAplikacie.setLadenieBigramov(ladenieBigramov);
        priebehAplikacie.setLadenieTrigramov(ladenieTrigramov);
        priebehAplikacie.setDolnaHranicaBigramovText(zvolenaDolnaOdchylkaBigramov);
        priebehAplikacie.setHornaHranicaBigramovText(zvolenaHornaOdchylkaBigramov);
        priebehAplikacie.setHorneHraniceTrigramovText(zvoleneHorneOdchylkyTrigramov);
        priebehAplikacie.setDolneHraniceTrigramovText(zvoleneDolneOdchylkyTrigramov);

        frame.add(hlavnyPanel, BorderLayout.CENTER);


        frame.setVisible(true);
    }
    private JSlider vytvoritJSlider(PriebehAplikacie priebehAplikacie,boolean horizontalne, String nazov,int min , int max, int hodnota,boolean malySlider,int minorTick,int majorTick,Color farba){
        JSlider slider;
        if(horizontalne){
            slider = new JSlider(JSlider.HORIZONTAL, min, max, hodnota);
        }
        else{
            slider = new JSlider(JSlider.VERTICAL, min, max, hodnota);
        }
        slider.setName(nazov);
        slider.setBackground(farba);
        slider.setMinorTickSpacing(minorTick);
        slider.setMajorTickSpacing(majorTick);
        slider.setSnapToTicks(true);
        if(!malySlider) {
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
        }
        slider.addChangeListener(priebehAplikacie);
        return slider;
    }

}