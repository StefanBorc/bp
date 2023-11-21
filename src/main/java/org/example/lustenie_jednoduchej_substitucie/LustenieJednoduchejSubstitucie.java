package org.example.lustenie_jednoduchej_substitucie;

import org.example.sifry.JednoduchaSubstitucia;

import java.util.ArrayList;

public class LustenieJednoduchejSubstitucie {
    private Vlastnosti vlastnosti;
    private JednoduchaSubstitucia substitucia;
    private OdhadAbecedy odhadAbecedy;
    private StringBuilder korpus;

    public LustenieJednoduchejSubstitucie(StringBuilder textOt,StringBuilder korpus) {
        vlastnosti = new Vlastnosti(textOt);
        this.korpus=korpus;

    }

    public void otestujKluce(ArrayList<String> kluce){
        char c='a';
        substitucia = new JednoduchaSubstitucia(korpus, Character.toUpperCase(c), kluce.get(0), 5000);
        odhadAbecedy = new OdhadAbecedy(substitucia, vlastnosti);
        ArrayList<Integer> uspesnost=new ArrayList<>();
        int pocet=odhadAbecedy.porovnatStatistiky();
        int pocitadlo=pocet;
        for(var kluc:kluce){
            if(kluc.equals(kluce.get(0))){
                continue;
            }
            System.out.println(kluc);
            substitucia.setKluc(kluc);
            pocet=odhadAbecedy.porovnatStatistiky();
            pocitadlo+=pocet;
        }
        System.out.println(pocet/5000);
    }

}
