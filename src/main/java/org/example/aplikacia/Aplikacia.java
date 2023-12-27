package org.example.aplikacia;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Aplikacia {
    public static final String SPUSTIT="SPUSTIT";
    public static final String PREPINAC_RIADKOV="PREPINAC_RIADKOV";
    public static final String PREPINAC_KLUCOV="PREPINAC_KLUCOV";
    public static final String PODIEL_NADPIS ="Podiel hlasok : ";
    public static final String BIGRAMY_NADPIS="bigramy OT : ";
    public static final String POKUS_NADPIS="Vysledok pokusu : ";
    public Aplikacia() throws IOException {
        JFrame frame = new JFrame("Tabulkova transpozicia");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.requestFocusInWindow();

        PriebehAplikacie priebehAplikacie = new PriebehAplikacie();
        frame.addKeyListener(priebehAplikacie);

        JPanel menu1 = new JPanel();
        JPanel menu2 = new JPanel();
        JButton tlacidloSpustit = new JButton(SPUSTIT);

        tlacidloSpustit.setBackground(Color.RED);
        tlacidloSpustit.addActionListener(priebehAplikacie);
        tlacidloSpustit.setFocusable(false);

        JSlider prepinacKlucov = new JSlider(JSlider.HORIZONTAL, 1000, 5000, 3000);
        prepinacKlucov.setName(PREPINAC_KLUCOV);
        prepinacKlucov.setBackground(Color.LIGHT_GRAY);
        prepinacKlucov.setMinorTickSpacing(500);
        prepinacKlucov.setMajorTickSpacing(1000);
        prepinacKlucov.setSnapToTicks(true);
        prepinacKlucov.setPaintTicks(true);
        prepinacKlucov.setPaintLabels(true);
        prepinacKlucov.addChangeListener(priebehAplikacie);

        JSlider prepinacRiadkov = new JSlider(JSlider.HORIZONTAL, 100, 1000, 500);
        prepinacRiadkov.setName(PREPINAC_RIADKOV);
        prepinacRiadkov.setBackground(Color.LIGHT_GRAY);
        prepinacRiadkov.setMinorTickSpacing(100);
        prepinacRiadkov.setMajorTickSpacing(200);
        prepinacRiadkov.setSnapToTicks(true);
        prepinacRiadkov.setPaintTicks(true);
        prepinacRiadkov.setPaintLabels(true);
        prepinacRiadkov.addChangeListener(priebehAplikacie);

        JLabel zvolenyPocetKlucov=new JLabel("Pocet klucov : "+prepinacKlucov.getValue());
        JLabel zvolenyPocetRiadkov=new JLabel("Pocet riadkov : "+prepinacRiadkov.getValue());
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

        JLabel text1 = new JLabel(PODIEL_NADPIS);
        JLabel text2 = new JLabel(BIGRAMY_NADPIS);
        JLabel text3 = new JLabel(POKUS_NADPIS);
        text1.setVerticalAlignment(SwingConstants.TOP);
        text2.setVerticalAlignment(SwingConstants.TOP);
        text3.setVerticalAlignment(SwingConstants.TOP);

        JPanel strednyPanel = new JPanel(new GridLayout(1, 3));
        JScrollPane panel1 = new JScrollPane(text1);
        strednyPanel.add(panel1);
        JScrollPane panel2 = new JScrollPane(text2);
        strednyPanel.add(panel2);
        JScrollPane panel3 = new JScrollPane(text3);
        strednyPanel.add(panel3);

        priebehAplikacie.setZvolenyPocetKlucov(zvolenyPocetKlucov);
        priebehAplikacie.setZvolenyPocetRiadkov(zvolenyPocetRiadkov);


        frame.add(strednyPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }


}