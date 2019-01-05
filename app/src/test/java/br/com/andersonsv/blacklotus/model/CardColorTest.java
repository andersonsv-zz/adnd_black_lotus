package br.com.andersonsv.blacklotus.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CardColorTest {

    @Test
    public void getColorCodeById(){
        CardColor cardColor = CardColor.getById("B");
        CardColor expected = CardColor.BLACK;
        assertEquals(cardColor, expected);
    }

    @Test
    public void getColorCodeByCode(){
        CardColor cardColor = CardColor.getByCode("{B}");
        CardColor expected = CardColor.BLACK;
        assertEquals(cardColor, expected);
    }

    @Test
    public void getColorCodeByIdNull(){
        CardColor cardColor = CardColor.getById("Y");
        assertEquals(cardColor, null);
    }

    @Test
    public void getColorCodeByCodeNull(){
        CardColor cardColor = CardColor.getByCode("{Y}");
        assertEquals(cardColor, null);
    }
}
