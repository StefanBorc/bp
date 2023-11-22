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

        final double startTime = System.currentTimeMillis();

        substitucia = new JednoduchaSubstitucia(korpus, Character.toUpperCase(c), kluce.get(0), 50000);
        odhadAbecedy = new OdhadAbecedy(substitucia, vlastnosti);
        ArrayList<Integer> uspesnost=new ArrayList<>();
        int pocet=odhadAbecedy.porovnatStatistiky();
        double pocitadlo=pocet;
        int i=0;

        for(var kluc:kluce){
            if(kluc.equals(kluce.get(0))){
                continue;
            }
            if(i==1000){
                break;
            }

            substitucia.setKluc(kluc);
            pocet=odhadAbecedy.porovnatStatistiky();

            pocitadlo+=pocet;

            i++;
        }
        System.out.println((pocitadlo/1000));
        double duration = System.currentTimeMillis() - startTime;
        System.out.println(duration/60000);
    }

}
