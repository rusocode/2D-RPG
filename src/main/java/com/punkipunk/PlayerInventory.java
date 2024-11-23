package com.punkipunk;

import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.assets.SpriteSheetAssets;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.Type;
import com.punkipunk.world.entity.item.Axe;
import com.punkipunk.world.entity.item.Item;
import com.punkipunk.world.entity.item.Key;
import com.punkipunk.world.entity.item.PotionBlue;

import static com.punkipunk.utils.Global.MAX_INVENTORY_SLOTS;

/**
 * Esta clase representa las funciones adicionales del inventario del jugador, como equipar o desequipar un objeto, agarrar un
 * objeto, seleccionar un objeto, entre otras.
 */

public class PlayerInventory extends GridInventory {

    public static final int ROWS = 4;
    public static final int COLS = 4;

    private final Game game;
    private final World world;
    private final Player player;

    public PlayerInventory(Game game, World world, Player player) {
        super(ROWS, COLS);
        this.game = game;
        this.world = world;
        this.player = player;
    }

    public void init() {
        clear();
        addAt(player.weapon, 3, 3);
        addAt(player.shield, 0, 1);
        addAt(new PotionBlue(game, world, 1), 0, 2);
        addAt(new Key(game, world, 1), 0, 3);
        addAt(new Axe(game, world), 1, 0);
        addAt(new Axe(game, world), 1, 1);
        addAt(new Axe(game, world), 1, 2);
        addAt(new Axe(game, world), 1, 3);
    }

    /**
     * Comprueba si puede recoger el item y, si es asi, lo agrega al inventario.
     *
     * @param item item.
     * @return true si el item se puede recoger o false.
     */
    public boolean canPickup(Item item) {

        /* Comprueba si no hay espacio en el inventario y si no hay items apilables. La ultima condicion se debe a que si el
         * inventario esta lleno, pero hay items apilables, entonces si se puede agarrar el item solo si el item que se agarra es
         * apilable. */
        if (size() >= MAX_INVENTORY_SLOTS && !checkIfStackableItems(item)) return false;

        // Manejo de items apilables
        if (item.stackable) {
            // Itera los items existentes
            for (Item existingItem : getAllItems()) {
                // Si el item existente es igual al item actual, entonces incrementa la cantidad
                if (existingItem.stats.name.equals(item.stats.name)) {
                    existingItem.amount += item.amount;
                    updateInventoryView();
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

        updateInventoryView();

        return true;
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

    public void select(int row, int col) {
        Item item = get(row, col);
        if (item != null) {
            if (item.type == Type.CONSUMABLE) consume(item, row, col);
            if (item.type != Type.CONSUMABLE) equip(item);
        }
    }

    /**
     * Consume the item.
     *
     * @param item item.
     */
    private void consume(Item item, int row, int col) {
        if (item.use(player)) {
            item.amount--;
            if (item.amount <= 0) remove(row, col);
            updateInventoryView();
        }
    }

    private void equip(Item item) {
        if (item.type == Type.AXE || item.type == Type.PICKAXE || item.type == Type.SWORD) {
            player.weapon = player.weapon == item ? null : item; // Check if the weapon is already equipped
            if (player.weapon != null) {
                player.stats.attack = player.getAttack();
                switch (player.weapon.type) {
                    case SWORD -> {
                        player.sheet.loadWeaponFrames(Assets.getSpriteSheet(SpriteSheetAssets.SWORD_FRAME), 16, 16, 1);
                        game.system.audio.playSound(Assets.getAudio(AudioAssets.DRAW_SWORD));
                    }
                    case AXE -> player.sheet.loadWeaponFrames(Assets.getSpriteSheet(SpriteSheetAssets.AXE_FRAME), 16, 16, 1);
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
     * Actualiza la vista del inventario.
     */
    private void updateInventoryView() {
        if (game.getGameController().getInventoryViewController() != null)
            game.getGameController().getInventoryViewController().updateInventory();
    }

}
