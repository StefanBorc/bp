package org.example.lustenie_jednoduchej_substitucie;

import org.example.sifry.JednoduchaSubstitucia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OdhadAbecedy {
    private JednoduchaSubstitucia substitucia;
    private Vlastnosti vlastnosti;

    public OdhadAbecedy(JednoduchaSubstitucia substitucia,Vlastnosti vlastnosti){
        this.substitucia=substitucia;
        this.vlastnosti=vlastnosti;
        substitucia.zasifrujText();
        porovnatStatistiky();
    }
    private List<Map.Entry<Character,Double>> frekvencnaAnalyzaZt(){
        Map<Character,Double> znakyZt=vlastnosti.frekvenciaZnakov(substitucia.getZasifrovanyText());
        return vlastnosti.usporiadatMapu(znakyZt);
    }
    private void porovnatStatistiky(){
        List<Map.Entry<Character,Double>> ot=new ArrayList<>(vlastnosti.getFrekvenciaOtUsporiadana());
        List<Map.Entry<Character,Double>> zt=frekvencnaAnalyzaZt();

        ArrayList<Character> abeceda=new ArrayList<>();
        for(var c:zt){
            abeceda.add(vyhodnotPismenoZt(c.getValue(),ot));
        }
        for(var c:abeceda){
            System.out.print(c+" ");
        }
        System.out.println();
        vlastnosti.vytlacAbecedu();

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
}
