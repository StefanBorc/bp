package org.example.lustenie_jednoduchej_substitucie;

import org.example.sifry.JednoduchaSubstitucia;

public class LustenieJednoduchejSubstitucie {
    private Vlastnosti vlastnosti;
    private JednoduchaSubstitucia substitucia;
    private OdhadAbecedy odhadAbecedy;

    public LustenieJednoduchejSubstitucie(StringBuilder textOt) {
        vlastnosti = new Vlastnosti(textOt);
        substitucia = new JednoduchaSubstitucia(textOt, 'c', "KOKOTINACELA", 50000);
        odhadAbecedy = new OdhadAbecedy(substitucia, vlastnosti);
    }

}
