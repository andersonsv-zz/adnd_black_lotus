package br.com.andersonsv.blacklotus.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StringUtilsTest {

    @Test
    public void testNullString() {
        String replaceType = null;
        String typeConverted = StringUtils.replaceTypetImgSrc(replaceType);
        assertEquals(typeConverted, Constants.EMPTY_STRING);
    }

    @Test
    public void testReplaceString() {
        String replaceType = "{W}";
        String typeConverted = StringUtils.replaceTypetImgSrc(replaceType);
        assertEquals(typeConverted, Constants.IMAGE_REPLACE.replace("$1", "W"));
    }

    @Test
    public void testReplaceTwoItemsString() {
        String replaceType = "{W}{1}";
        String typeConverted = StringUtils.replaceTypetImgSrc(replaceType);
        String expected = Constants.IMAGE_REPLACE.replace("$1", "W").concat(Constants.IMAGE_REPLACE.replace("$1", "1"));
        assertEquals(typeConverted, expected);
    }


    @Test
    public void testFormatWithParams() {
        String format = "%d/%d";
        String expected = "1/10";

        int [] values = {1, 10};

        String result = StringUtils.formatWithParams(format, values );

        assertEquals(result, expected);
    }
}
