package com.punkipunk.world.entity.item;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.states.State;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Entity;
import com.punkipunk.world.entity.Type;

import static com.punkipunk.utils.Global.tile;

public class Tent extends Item {

    public static final String NAME = "Tent";

    public Tent(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.CONSUMABLE;
        stats.name = NAME;
        description = "[" + stats.name + "]\nYou can sleep until\nnext morning.";
        price = 380;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.TENT), tile, tile);
    }

    @Override
    public boolean use(Entity entity) {
        State.setState(State.SLEEP);
        game.system.audio.playSound(Assets.getAudio(AudioAssets.SLEEP));
        world.entities.player.stats.hp = world.entities.player.stats.maxHp;
        world.entities.player.stats.mana = world.entities.player.stats.maxMana;
        world.entities.player.initSleepImage(sheet.frame);
        return true;
    }

}
