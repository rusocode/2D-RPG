package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.entity.base.Entity;
import com.punkipunk.entity.base.Type;

import static com.punkipunk.utils.Global.tile;

public class Gold extends Item {

    public static final String NAME = "Gold";

    public Gold(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.PICKUP;
        stats.name = NAME;
        this.amount = amount;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.GOLD), tile, tile);
    }

    @Override
    public boolean use(Entity entity) {
        game.system.audio.playSound(AudioID.Sound.GOLD_PICKUP);
        game.system.ui.addMessageToConsole("Gold +" + amount);
        entity.stats.gold += amount;
        return true;
    }

}
