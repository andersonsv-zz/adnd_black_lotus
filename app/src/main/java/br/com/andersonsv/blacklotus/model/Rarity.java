package br.com.andersonsv.blacklotus.model;

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
}
