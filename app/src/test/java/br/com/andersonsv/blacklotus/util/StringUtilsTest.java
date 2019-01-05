package br.com.andersonsv.blacklotus.util;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class StringUtilsTest {

    @Test
    public void null_string() {
        String replaceType = null;
        String typeConverted = StringUtils.replaceTypetImgSrc(replaceType);
        assertEquals(typeConverted, Constants.EMPTY_STRING);
    }

    @Test
    public void replace() {
        String replaceType = "{W}";
        String typeConverted = StringUtils.replaceTypetImgSrc(replaceType);
        assertEquals(typeConverted, Constants.IMAGE_REPLACE.replace("$1", "W"));
    }

    @Test
    public void replace_two_items() {
        String replaceType = "{W}{1}";
        String typeConverted = StringUtils.replaceTypetImgSrc(replaceType);
        String expected = Constants.IMAGE_REPLACE.replace("$1", "W").concat(Constants.IMAGE_REPLACE.replace("$1", "1"));
        assertEquals(typeConverted, expected);
    }

    @Test
    public void no_replace_two_items() {
        String replaceType = "W1";
        String typeConverted = StringUtils.replaceTypetImgSrc(replaceType);
        String expected = "W1";
        assertEquals(typeConverted, expected);
    }

    @Test
    public void format_string_int_values() {
        String template = "%d/%d";
        String typeConverted = StringUtils.formatStringInt(template, Arrays.asList(1, 10));
        String expected = "1/10";
        assertEquals(typeConverted, expected);
    }





}
