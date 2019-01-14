package br.com.andersonsv.blacklotus.util;

import java.util.List;

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

    public static String formatStringInt(String template, List<Integer> values){
        return String.format(template, values.toArray());
    }

    public static String leftCompleteString(String leftContent, String rightContent){
        return leftContent.concat(rightContent);
    }

    public static String transformHex(Integer color) {
        return Integer.toHexString(color).toUpperCase();
    }


}
