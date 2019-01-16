package br.com.andersonsv.blacklotus.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        assertNull(cardColor);
    }

    @Test
    public void get_null_color_code_by_code(){
        CardColor cardColor = CardColor.getByCode("{Y}");
        assertNull(cardColor);
    }
}
