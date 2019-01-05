package br.com.andersonsv.blacklotus.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CardColorTest {

    @Test
    public void get_success_color_code_by_id(){
        CardColor cardColor = CardColor.getById("B");
        CardColor expected = CardColor.BLACK;
        assertEquals(cardColor, expected);
    }

    @Test
    public void get_success_color_code_by_code(){
        CardColor cardColor = CardColor.getByCode("{B}");
        CardColor expected = CardColor.BLACK;
        assertEquals(cardColor, expected);
    }

    @Test
    public void get_null_color_code_by_id(){
        CardColor cardColor = CardColor.getById("Y");
        assertEquals(cardColor, null);
    }

    @Test
    public void get_null_color_code_by_code(){
        CardColor cardColor = CardColor.getByCode("{Y}");
        assertEquals(cardColor, null);
    }
}
