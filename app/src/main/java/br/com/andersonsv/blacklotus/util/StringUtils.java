package br.com.andersonsv.blacklotus.util;

public class StringUtils {

    public static String replaceTypetImgSrc(String textToReplace){
        return textToReplace.replaceAll("\\{([^}]*)\\}", "<img src='$1'>");
    }

}
