package org.example.lustenie_jednoduchej_substitucie;

import org.example.sifry.JednoduchaSubstitucia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OdhadAbecedy {
    private JednoduchaSubstitucia substitucia;
    private Vlastnosti vlastnosti;
    private ArrayList<Character> abecedaOt;

    public OdhadAbecedy(JednoduchaSubstitucia substitucia,Vlastnosti vlastnosti){
        this.substitucia=substitucia;
        this.vlastnosti=vlastnosti;
        abecedaOt=vlastnosti.abeceda();
        substitucia.zasifrujText();

    }
    private List<Map.Entry<Character,Double>> frekvencnaAnalyzaZt(){
        Map<Character,Double> znakyZt=vlastnosti.frekvenciaZnakov(substitucia.getZasifrovanyText());
        return vlastnosti.usporiadatMapu(znakyZt);
    }
    protected int porovnatStatistiky(){
        List<Map.Entry<Character,Double>> ot=new ArrayList<>(vlastnosti.getFrekvenciaOtUsporiadana());
        List<Map.Entry<Character,Double>> zt=frekvencnaAnalyzaZt();

        ArrayList<Character> abeceda=new ArrayList<>();
        for(var c:zt){
            abeceda.add(vyhodnotPismenoZt(c.getValue(),ot));
        }
        int pocet=pocetUhadnutychPismen(abeceda);
        return pocet;
    }
    private Character vyhodnotPismenoZt(Double vyskytZnakuZt,List<Map.Entry<Character,Double>> otStatistika){
        ArrayList<Double> odchylky=new ArrayList<>();
        Character pismeno;
        for(var c:otStatistika){
            double odchylka=Math.abs(vyskytZnakuZt-c.getValue());
            odchylky.add(odchylka);
        }
        pismeno=otStatistika.get(odchylky.indexOf(Collections.min(odchylky))).getKey();
        otStatistika.remove(odchylky.indexOf(Collections.min(odchylky)));
        return pismeno;
    }
    private int pocetUhadnutychPismen(ArrayList<Character> ztAbeceda){
        int pocet=0;
        int index=0;
        for(var c:abecedaOt){
            if(c.equals(ztAbeceda.get(index))){
                pocet++;
            }
            index++;
        }
        return pocet;
    }
}
