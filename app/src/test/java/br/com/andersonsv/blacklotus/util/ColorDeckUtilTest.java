package br.com.andersonsv.blacklotus.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class ColorDeckUtilTest {

    @Test
    public void add_color_in_list_success() {
        String color = "#FFFFF";
        List<Integer> colors = new ArrayList<>();
        ColorDeckUtil.addColorIfNonNull(color, colors);
        assertTrue(!color.isEmpty());
    }

    @Test
    public void add_color_null_empty_list() {
        List<Integer> colors = new ArrayList<>();
        ColorDeckUtil.addColorIfNonNull(null, colors);
        assertTrue(colors.isEmpty());
    }
}
