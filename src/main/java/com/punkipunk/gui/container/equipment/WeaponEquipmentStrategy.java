package com.punkipunk.gui.container.equipment;

import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.player.Player;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.utils.Utils;

public class WeaponEquipmentStrategy extends BaseEquipmentStrategy {

    @Override
    protected void onEquip(Player player, Item item) {
        player.weapon = item;
        player.stats.attack = player.getAttack();
        // FIXME Se crea un nuevo objeto SS cada vez que equipo el arma
        player.sheet.loadWeaponFrames(new SpriteSheet(Utils.loadTexture(item.itemData.spriteSheetPath())), 16, 16, 1);
        player.game.system.audio.playSound(item.itemData.sound());
    }

    @Override
    protected void onUnequip(Player player, Item item) {
        player.weapon = null;
        // player.stats.attack = player.getAttack(); // Recalcular ataque al desequipar
    }

    @Override
    protected boolean checkEquipped(Player player, Item item) {
        return player.weapon == item;
    }

}
