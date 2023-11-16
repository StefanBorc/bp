package org.example;

import lombok.Getter;

import java.io.*;

public abstract class Sifra {
    @Getter
    private StringBuilder upravenyText;
    @Getter
    private StringBuilder text;
      public Sifra()  {
      }

    public abstract void sifrovanie(StringBuilder text,String kluc) throws IOException;
}
