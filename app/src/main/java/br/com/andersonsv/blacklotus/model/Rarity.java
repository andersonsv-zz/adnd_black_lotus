package br.com.andersonsv.blacklotus.model;

import java.util.HashMap;
import java.util.Map;

import br.com.andersonsv.blacklotus.R;

public enum Rarity  {
    COMMON("Common", R.color.colorRarityCommon, "Comum"),
    UNCOMMON("Uncommon", R.color.colorRarityUncommon, "Incomum"),
    RARE("Rare", R.color.colorRarityRare, "Rara"),
    MYTHIC_RARE("Mythic Rare", R.color.colorRarityMithicRare, "Mítica Rara"),
    SPECIAL("Special", R.color.colorRarityTimeshifted, "Especial"),
    BASIC_LAND("Basic Land", R.color.colorRarityMasterpice, "Terreno");

    private String typeId;
    private int color;
    private String namePtBR;

    private static Map<String, Rarity> rarityByType = new HashMap<>();

    Rarity(String typeId, int color, String namePtBR) {
        this.typeId = typeId;
        this.color = color;
        this.namePtBR = namePtBR;
    }

    public String getTypeId() {
        return typeId;
    }

    public int getColor() {
        return color;
    }

    public String getNamePtBR() {
        return namePtBR;
    }

    static {

        for (final Rarity type : values()) {
            rarityByType.put(type.getTypeId(), type);
        }
    }

    public static Rarity getByType(final String typeId)  {
        return rarityByType.get(typeId);
    }
}
