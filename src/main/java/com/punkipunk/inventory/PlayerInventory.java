package com.punkipunk.inventory;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.assets.SpriteSheetAssets;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.Type;
import com.punkipunk.world.entity.item.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Representa las funciones del inventario del jugador, como equipar o desequipar un item, agarrar un item, seleccionar un item,
 * entre otras.
 */

public class PlayerInventory extends Inventory {

    public static final int ROWS = 4;
    public static final int COLS = 4;

    private final Game game;
    private final World world;
    private final Player player;
    private final List<InventoryListener> listeners = new ArrayList<>(); // Lista de observadores para notificar cambios
    private final Equipment equipment;

    public PlayerInventory(Game game, World world, Player player) {
        super(ROWS, COLS);
        this.game = game;
        this.world = world;
        this.player = player;
        equipment = new Equipment(player, game);
    }

    public void initializeDefaultItems() {
        add(new SwordIron(game, world));
        add(new ShieldWood(game, world));
        add(new PotionBlue(game, world, 1));
        add(new Key(game, world, 1));
        add(new Axe(game, world));
        add(new Axe(game, world));
    }

    /**
     * Comprueba si puede recoger el item, y si es asi, lo agrega al inventario.
     *
     * @param item item.
     * @return true si el item se puede recoger, false en caso contrario.
     */
    public boolean canPickup(Item item) {

        int maxInventorySlots = ROWS * COLS;

        /* Comprueba si no hay espacio en el inventario y si no hay items apilables. La ultima condicion se debe a que si el
         * inventario esta lleno, pero hay items apilables, entonces si se puede agarrar el item solo si el item que se agarra es
         * apilable. */
        if (size() >= maxInventorySlots && !checkIfStackableItems(item)) return false;

        // Manejo de items apilables
        if (item.stackable) {
            // Itera los items existentes
            for (Item existingItem : getAllItems()) {
                // Si el item existente es igual al item actual, entonces incrementa la cantidad
                if (existingItem.stats.name.equals(item.stats.name)) {
                    existingItem.amount += item.amount;
                    notifyInventoryChanged();
                    return true;
                }
            }
        }

        // Si no es apilable o no existe uno del mismo tipo, lo agrega al inventario
        Item newItem = game.system.itemGenerator.generate(item.stats.name);
        add(newItem);

        /* En caso de agarrar, por ejemplo, unas pociones azules y que no esten (todavia) en el inventario, entonces ademas de
         * crear el item, ajusta la cantidad. */
        if (item.stackable) newItem.amount = item.amount;

        notifyInventoryChanged();

        return true;
    }

    public void equip(Item item) {
        switch (item.type) {
            case AXE, PICKAXE, SWORD -> equipment.equipWeapon(item);
            case SHIELD -> equipment.equipShield(item);
            case LIGHT -> equipment.equipLight(item);
        }
        // notifyInventoryChanged();
    }

    public void consume(Item item, int row, int col) {
        if (!item.use(player)) return;
        item.amount--;
        if (item.amount <= 0) remove(row, col);
        notifyInventoryChanged();
    }

    /**
     * Comprueba si hay items apilables en el invenatario.
     *
     * @param currentItem item actual.
     * @return true si hay un item apilable en el inventario.
     */
    private boolean checkIfStackableItems(Item currentItem) {
        /* Comprueba si el item actual no es apilable ya que si por ejemplo agarra un Axe, entonces no tendria que agregarlo al
         * inventario aunque haya items apilables en el inventario. */
        if (!currentItem.stackable) return false;
        for (Item item : getAllItems()) {
            /* Comprueba si el item del inventario es apilable y si es igual al item actual (el que se agarra). La ultima
             * condicion sirve evitar agarrar un item apilable que es distinto al item apilable del inventario, evitando asi un
             * IndexOutOfBoundsException. Por ejemplo, si el item que se agarra es una pocion roja y el item del inventario es una
             * pocion azul, entonces no lo agarra. */
            if (item.stackable && item.stats.name.equals(currentItem.stats.name)) return true;
        }
        return false;
    }

    public void addListener(InventoryListener listener) {
        listeners.add(listener);
    }

    public void removeListener(InventoryListener listener) {
        listeners.remove(listener);
    }

    private void notifyInventoryChanged() {
        listeners.forEach(InventoryListener::onInventoryChanged);
    }

    private record Equipment(Player player, Game game) {

        public void equipWeapon(Item weapon) {
            if (player.weapon == weapon) {
                unequipWeapon();
                return;
            }
            player.weapon = weapon;
            player.stats.attack = player.getAttack();
            updateWeaponAnimation(weapon.type);
        }

        private void unequipWeapon() {
            player.weapon = null;
            // player.stats.attack = player.getBaseAttack();
        }

        private void updateWeaponAnimation(Type weaponType) {

            switch (weaponType) {
                case SWORD -> {
                    player.sheet.loadWeaponFrames(Assets.getSpriteSheet(SpriteSheetAssets.SWORD_FRAME), 16, 16, 1);
                    game.system.audio.playSound(Assets.getAudio(AudioAssets.DRAW_SWORD));
                }
                case AXE -> player.sheet.loadWeaponFrames(Assets.getSpriteSheet(SpriteSheetAssets.AXE_FRAME), 16, 16, 1);
                case PICKAXE -> player.sheet.loadWeaponFrames(Assets.getSpriteSheet(SpriteSheetAssets.PICKAXE_FRAME), 16, 16, 1);
            }
        }

        private void equipShield(Item shield) {
            if (player.shield == shield) {
                player.shield = null;
                return;
            }
            player.shield = shield;
            player.stats.defense = player.getDefense();
        }

        public void equipLight(Item light) {
            player.light = player.light == light ? null : light;
            player.lightUpdate = true;
        }

    }

}
