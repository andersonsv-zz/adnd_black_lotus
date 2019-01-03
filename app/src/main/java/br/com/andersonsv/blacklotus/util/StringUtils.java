package br.com.andersonsv.blacklotus.util;

import static br.com.andersonsv.blacklotus.util.Constants.EMPTY_STRING;
import static br.com.andersonsv.blacklotus.util.Constants.IMAGE_REPLACE;
import static br.com.andersonsv.blacklotus.util.Constants.REG_EXP_COST;

public class StringUtils {

    public static String replaceTypetImgSrc(String textToReplace){
        if (textToReplace != null) {
            return textToReplace.replaceAll(REG_EXP_COST, IMAGE_REPLACE);
        }
        return EMPTY_STRING;
    }

}
