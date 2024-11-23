package com.punkipunk.world.entity.item;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Entity;
import com.punkipunk.world.entity.Type;

import static com.punkipunk.utils.Global.tile;

/**
 * To specify the texture of the item on the ground with a smaller size:
 * <pre>{@code
 * sheet.frame = pos.length > 0 ? Utils.scaleImage(key, tile / 2, tile / 2) : Utils.scaleImage(key, tile, tile);
 * }</pre>
 */

public class Key extends Item {

    public static final String NAME = "Key";

    public Key(Game game, World world, int amount, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        type = Type.CONSUMABLE;
        stats.name = NAME;
        description = "[" + stats.name + "]\nIt opens a door.";
        this.amount = amount;
        stackable = true;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.KEY), tile, tile);
    }

    @Override
    public boolean use(Entity entity) {
        // If the entity detects a door, then it can use the key
        int i = detect(entity, world.entities.items, Door.NAME);
        if (i != -1) {
            game.system.audio.playSound(Assets.getAudio(AudioAssets.DOOR_OPENING));
            world.entities.items[world.map.num][i] = null;
            return true;
        }
        return false;
    }

}
