package com.punkipunk;

import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.assets.SpriteSheetAssets;
import com.punkipunk.states.State;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.Type;
import com.punkipunk.world.entity.item.Item;
import com.punkipunk.world.entity.item.Lantern;
import com.punkipunk.world.entity.item.PotionRed;
import com.punkipunk.world.entity.item.Tent;

import static com.punkipunk.utils.Global.MAX_INVENTORY_SLOTS;

/**
 * This class represents the additional functions of the player's inventory, such as equipping or unequipping an item, grabbing an
 * item, select an item, among others.
 */

public class PlayerInventory extends Inventory {

    private final Game game;
    private final World world;
    private final Player player;

    // Variables that the cursor uses to represent its position in the inventory
    public int playerSlotCol, playerSlotRow, npcSlotCol, npcSlotRow;

    public PlayerInventory(Game game, World world, Player player) {
        super();
        this.game = game;
        this.world = world;
        this.player = player;
    }

    /**
     * Initializes the player's inventory with the default items.
     */
    public void init() {
        clear(); // Clear the inventory in case a new game is created from the menu without closing the window
        add(player.weapon);
        add(player.shield);
        add(new Lantern(game, world));
        add(new PotionRed(game, world, 2));
        add(new Tent(game, world));
    }

    /**
     * It checks if it can pick up the item and if so it adds it to the inventory.
     *
     * @param item item.
     * @return true if the item can be picked up or false.
     */
    public boolean canPickup(Item item) {
        Item newItem = game.system.itemGenerator.generate(item.stats.name);
        if (item.stackable) {
            int i = search(item.stats.name);
            // If it exists in inventory, then just increase the quantity
            if (i != -1) {
                get(i).amount += item.amount;
                return true;
                // If it does not exist in the inventory, add it as a new item with its respective quantity
            } else if (size() != MAX_INVENTORY_SLOTS) {
                add(newItem);
                // When adding a new item, you cannot use the index of the previous item, you have to search the index from the new item
                get(search(item.stats.name)).amount += item.amount;
                return true;
            }
        } else if (size() != MAX_INVENTORY_SLOTS) {
            add(newItem);
            return true;
        }
        return false;
    }

    /**
     * Select the item.
     */
    public void select() {
        int i = getSlot(playerSlotCol, playerSlotRow);
        if (i < inventory.size()) {
            Item item = inventory.get(i);
            // Verifica si esta en estado de inventario para evitar equipar/consumir items mientrtas esta vendiendo
            if (State.isState(State.INVENTORY)) {
                if (item.type != Type.CONSUMABLE) equip(item);
                if (item.type == Type.CONSUMABLE) consume(item, i);
            }
        }
    }

    /**
     * Equip the item.
     *
     * @param item item.
     */
    private void equip(Item item) {
        if (item.type == Type.AXE || item.type == Type.PICKAXE || item.type == Type.SWORD) {
            player.weapon = player.weapon == item ? null : item; // Check if the weapon is already equipped
            if (player.weapon != null) {
                player.stats.attack = player.getAttack();
                switch (player.weapon.type) {
                    case SWORD -> {
                        player.sheet.loadWeaponFrames(Assets.getSpriteSheet(SpriteSheetAssets.SWORD_FRAME), 16, 16, 1);
                        player.game.playSound(Assets.getAudio(AudioAssets.DRAW_SWORD));
                    }
                    // case AXE -> player.sheet.loadWeaponFrames(Assets.getSpriteSheet(SpriteSheetAssets.AXE_FRAME), 16, 16, 1);
                    case PICKAXE ->
                            player.sheet.loadWeaponFrames(Assets.getSpriteSheet(SpriteSheetAssets.PICKAXE_FRAME), 16, 16, 1);
                }
            }
        }
        if (item.type == Type.SHIELD) {
            player.shield = player.shield == item ? null : item;
            if (player.shield != null) player.stats.defense = player.getDefense();
        }
        if (item.type == Type.LIGHT) {
            player.light = player.light == item ? null : item;
            player.lightUpdate = true;
        }
    }

    /**
     * Consume the item.
     *
     * @param item item.
     * @param i    index of the item.
     */
    private void consume(Item item, int i) {
        if (item.use(player)) {
            if (item.amount > 1) item.amount--;
            else inventory.remove(i);
        }
    }

}
