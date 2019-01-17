package br.com.andersonsv.blacklotus.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import br.com.andersonsv.blacklotus.R;

public enum Rarity  {
    @SerializedName("Common")
    COMMON("Common", R.color.colorRarityCommon),
    @SerializedName("Uncommon")
    UNCOMMON("Uncommon", R.color.colorRarityUncommon),
    @SerializedName("Rare")
    RARE("Rare", R.color.colorRarityRare),
    @SerializedName("Mythic Rare")
    MYTHIC_RARE("Mythic Rare", R.color.colorRarityMithicRare),
    @SerializedName("Special")
    SPECIAL("Special", R.color.colorRarityTimeshifted),
    @SerializedName("Basic Land")
    BASIC_LAND("Basic Land", R.color.colorRarityMasterpice);

    private String typeId;
    private int color;

    private static final Map<String, Rarity> rarityByType = new HashMap<>();

    Rarity(String typeId, int color) {
        this.typeId = typeId;
        this.color = color;
    }

    public String getTypeId() {
        return typeId;
    }

    public int getColor() {
        return color;
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
