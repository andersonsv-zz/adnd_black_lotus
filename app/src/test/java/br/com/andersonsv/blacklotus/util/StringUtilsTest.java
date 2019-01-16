package br.com.andersonsv.blacklotus.util;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class StringUtilsTest {

    @Test
    public void null_string() {
        String typeConverted = StringUtils.replaceTypetImgSrc(null);
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

    @Test
    public void transform_hex_success() {
        Integer color =  -14399861;
        String expected = "FF24468B";

        String colorConverted = StringUtils.transformHex(color);
        assertEquals(colorConverted, expected);
    }

    @Test
    public void left_complete_string_success() {
        String colorHex = "FF24468B";
        String expected = "#FF24468B";

        String colorHexTransform = StringUtils.leftCompleteString("#", colorHex);
        assertEquals(colorHexTransform, expected);
    }





}
