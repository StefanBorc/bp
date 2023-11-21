package org.example.lustenie_jednoduchej_substitucie;

import org.example.sifry.JednoduchaSubstitucia;

public class Priebeh {
    private Vlastnosti vlastnosti;
    private JednoduchaSubstitucia substitucia;
    private OdhadAbecedy odhadAbecedy;

    public Priebeh(StringBuilder textOt) {
        vlastnosti = new Vlastnosti(textOt);
        substitucia = new JednoduchaSubstitucia(textOt, 'c', "KOKOTINACELA", 1000);
        odhadAbecedy = new OdhadAbecedy(substitucia, vlastnosti);
    }

}
