package com.punkipunk.entity.item;

public enum ItemID {
    BOOTS("boots", ItemCategory.USABLE),
    CHEST("chest", ItemCategory.OBSTACLE),
    CHICKEN("chicken", ItemCategory.USABLE),
    GOLD("gold", ItemCategory.PICKUP),
    IRON_DOOR("ironDoor", ItemCategory.OBSTACLE),
    IRON_SHIELD("ironShield", ItemCategory.SHIELD),
    KEY("key", ItemCategory.USABLE),
    LANTERN("lantern", ItemCategory.LIGHT),
    POTION_BLUE("potionBlue", ItemCategory.USABLE),
    POTION_RED("potionRed", ItemCategory.USABLE),
    STONE("stone", ItemCategory.USABLE),
    STONE_AXE("stoneAxe", ItemCategory.AXE),
    STONE_PICKAXE("stonePickaxe", ItemCategory.PICKAXE),
    STONE_SWORD("stoneSword", ItemCategory.SWORD),
    TENT("tent", ItemCategory.USABLE),
    WOOD("wood", ItemCategory.USABLE),
    WOOD_DOOR("woodDoor", ItemCategory.OBSTACLE),
    WOOD_SHIELD("woodShield", ItemCategory.SHIELD);

    private final String name;
    private final ItemCategory category;

    ItemID(String name, ItemCategory category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public ItemCategory getCategory() {
        return category;
    }

}
