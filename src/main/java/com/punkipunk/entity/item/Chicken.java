package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.entity.base.Entity;

import static com.punkipunk.utils.Global.tile;

public class Chicken extends Item {

    public static final String NAME = "Chicken";

    public Chicken(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        itemType = ItemType.CONSUMABLE;
        stats.name = NAME;
        description = "[" + stats.name + "]\nYes...\nLizard's treasure.";
        price = 1;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.CHICKEN), tile, tile);
    }

    @Override
    public boolean use(Entity entity) {
        game.system.audio.playSound(AudioID.Sound.EAT);
        world.entities.player.stats.hp = world.entities.player.stats.maxHp;
        world.entities.player.stats.mana = world.entities.player.stats.maxMana;
        return true;
    }

}


