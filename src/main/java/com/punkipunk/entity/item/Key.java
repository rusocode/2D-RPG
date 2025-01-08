package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;

public class Key extends Item {

    public static final String NAME = "Key";

    public Key(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("items.key", ItemData.class), pos);
        itemType = ItemType.USABLE;
    }

    @Override
    public boolean use(Entity entity) {
        // Si la entidad detecta una puerta, entonces puede usar la llave
        int i = detect(entity, world.entities.items, WoodDoor.NAME);
        if (i != -1) {
            game.system.audio.playSound(AudioID.Sound.DOOR_OPENING);
            world.entities.items[world.map.num][i] = null;
            return true;
        }
        return false;
    }

}
