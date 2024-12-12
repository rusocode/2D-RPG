package com.punkipunk.gui.container.equipment;

import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.assets.SpriteSheetAssets;
import com.punkipunk.world.entity.Player;
import com.punkipunk.world.entity.item.Item;
import com.punkipunk.world.entity.item.ItemType;

public class WeaponEquipmentStrategy extends BaseEquipmentStrategy {

    @Override
    protected void onEquip(Player player, Item item) {
        player.weapon = item;
        player.stats.attack = player.getAttack();
        loadWeaponFrames(player, item.itemType);
        playWeaponAudio(player, item.itemType);
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

    private void loadWeaponFrames(Player player, ItemType type) {
        SpriteSheetAssets spriteSheetAssets = switch (type) {
            case AXE -> SpriteSheetAssets.AXE_FRAME;
            case PICKAXE -> SpriteSheetAssets.PICKAXE_FRAME;
            case SWORD -> SpriteSheetAssets.SWORD_FRAME;
            default -> null;
        };
        if (spriteSheetAssets != null) player.sheet.loadWeaponFrames(Assets.getSpriteSheet(spriteSheetAssets), 16, 16, 1);
    }

    private void playWeaponAudio(Player player, ItemType type) {
        AudioAssets audioAsset = switch (type) {
            case PICKAXE -> AudioAssets.DRAW_PICKAXE;
            case SWORD -> AudioAssets.DRAW_SWORD;
            default -> null;
        };
        if (audioAsset != null) player.game.system.audio.playSound(Assets.getAudio(audioAsset));
    }

}
