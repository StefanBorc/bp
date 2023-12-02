package org.example.lustenie_jednoduchej_substitucie;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Vlastnosti {
    @Getter
    private Map<Character,Double> frekvenciaZnakovOt;
    @Getter
    private List<Map.Entry<Character,Double>> frekvenciaOtUsporiadana;
    public Vlastnosti(StringBuilder text){
        frekvenciaZnakovOt=frekvenciaZnakov(text);
        frekvenciaOtUsporiadana=usporiadatMapu(frekvenciaZnakovOt);
    }
    protected Map<Character,Double> frekvenciaZnakov(StringBuilder text){
        Map<Character,Double> frekvenciaZnakov=new HashMap<>();
        frekvenciaZnakovOt=new HashMap<>();
        for(var c:text.toString().toCharArray()){
            if(Character.isAlphabetic(c) && ((c<91 && c>64))){
                frekvenciaZnakov.merge(c,1.0,Double::sum);
            }
        }
        double pocetZnakov=frekvenciaZnakov.values().stream().mapToDouble(a->a).sum();
        for(var znak:frekvenciaZnakov.entrySet()){
            frekvenciaZnakov.replace(znak.getKey(),znak.getValue(),(znak.getValue()/pocetZnakov)*100);
        }

        char c='?';
        while(frekvenciaZnakov.size()<26){
            frekvenciaZnakov.put(c++,1.0);
        }
        return frekvenciaZnakov;
    }
    protected List<Map.Entry<Character,Double>> usporiadatMapu(Map<Character,Double> mapa){
        List<Map.Entry<Character, Double>> usporiadanaMapa = mapa.entrySet()
                .stream()
                .sorted((vstup1, vstup2) -> vstup2.getValue().compareTo(vstup1.getValue()))
                .collect(Collectors.toList());
        return usporiadanaMapa;

    }
    public ArrayList<Character> abeceda(){
        ArrayList<Character> abeceda=new ArrayList<>();
        for(var c:frekvenciaOtUsporiadana){
            abeceda.add(c.getKey());
        }
        return abeceda;
    }
}
