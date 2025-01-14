package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;

public class Key extends Item {

    public static final String NAME = "Key";

    public Key(Game game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    public boolean use(Entity entity) {
        // Si la entidad detecta una puerta, entonces puede usar la llave
        if (isNearby(entity, WoodDoor.NAME)) {
            world.entities.getItems(world.map.num).stream()
                    .filter(item -> item instanceof WoodDoor)
                    .filter(door -> isAdjacentTo(entity, door))
                    .findFirst()
                    .ifPresent(door -> {
                        game.system.audio.playSound(AudioID.Sound.DOOR_OPENING);
                        world.entities.removeItem(world.map.num, door);
                    });
            return true;
        }
        return false;
    }

    @Override
    protected ItemType getType() {
        return ItemType.KEY;
    }

}
