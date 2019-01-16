package br.com.andersonsv.blacklotus.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RarityTest {
    @Test
    public void get_success_rarity_code_by_type(){
        Rarity rarity = Rarity.getByType("Common");
        Rarity expected = Rarity.COMMON;
        assertEquals(rarity, expected);
    }

    @Test
    public void get_null_rarity_code_by_type(){
        Rarity rarity = Rarity.getByType("Null");
        assertNull(rarity);
    }
}
