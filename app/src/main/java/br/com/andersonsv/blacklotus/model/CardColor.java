package br.com.andersonsv.blacklotus.model;

import java.util.HashMap;
import java.util.Map;

import br.com.andersonsv.blacklotus.R;

public enum CardColor {
    WHITE("W", "{W}", R.drawable.white),
    BLACK("B", "{B}", R.drawable.black),
    BLUE("U", "{U}", R.drawable.blue),
    RED("R", "{R}", R.drawable.red),
    GREEN("G", "{G}", R.drawable.green);

    private String colorId;
    private String colorCode;
    private int image;

    CardColor(String colorId, String colorCode, int image) {
        this.colorId = colorId;
        this.colorCode = colorCode;
        this.image = image;

    }

    private static final Map<String, CardColor> colorById = new HashMap<>();
    private static final Map<String, CardColor> colorByCode = new HashMap<>();

    public String getColorId() {
        return colorId;
    }

    public String getColorCode() {
        return colorCode;
    }

    public int getImage() {
        return image;
    }

    static {
        for (final CardColor cardColor : values()) {
            colorById.put(cardColor.getColorId(), cardColor);
            colorByCode.put(cardColor.getColorCode(), cardColor);
        }
    }

    public static CardColor getById(final String colorId)  {
        return colorById.get(colorId);
    }
    public static CardColor getByCode(final String colorCode)  {
        return colorByCode.get(colorCode);
    }
}
