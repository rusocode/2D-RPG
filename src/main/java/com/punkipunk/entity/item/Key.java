package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.config.Config;
import com.punkipunk.config.json.ItemConfig;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;

public class Key extends Item {

    public static final String NAME = "Key";

    public Key(Game game, World world, int... pos) {
        super(game, world, Config.getInstance().getJsonValue("items.key", ItemConfig.class), pos);
    }

    @Override
    public boolean use(Entity entity) {
        // If the entity detects a door, then it can use the key
        int i = detect(entity, world.entities.items, WoodDoor.NAME);
        if (i != -1) {
            game.system.audio.playSound(AudioID.Sound.DOOR_OPENING);
            world.entities.items[world.map.num][i] = null;
            return true;
        }
        return false;
    }

}
