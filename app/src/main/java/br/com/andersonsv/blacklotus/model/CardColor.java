package br.com.andersonsv.blacklotus.model;

import java.util.HashMap;
import java.util.Map;

import br.com.andersonsv.blacklotus.R;

public enum CardColor {
    WHITE("W", R.drawable.white),
    BLACK("B", R.drawable.black),
    BLUE("U", R.drawable.blue),
    RED("R", R.drawable.red),
    GREEN("G", R.drawable.green);

    private String colorId;
    private int image;

    CardColor(String colorId, int image) {
        this.colorId = colorId;
        this.image = image;
    }

    private static Map<String, CardColor> colorById = new HashMap<>();


    public String getColorId() {
        return colorId;
    }

    public int getImage() {
        return image;
    }

    static {
        for (final CardColor cardColor : values()) {
            colorById.put(cardColor.getColorId(), cardColor);
        }
    }

    public static CardColor getById(final String colorId)  {
        return colorById.get(colorId);
    }
}
