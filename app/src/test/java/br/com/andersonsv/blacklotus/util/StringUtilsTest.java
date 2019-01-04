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

}
