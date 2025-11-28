package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;

public class Key extends Item {

    public Key(IGame game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    public boolean use(Entity entity) {
        // Si la entidad detecta una puerta de madera, entonces puede usar la llave
        if (isNearby(entity, ItemID.WOOD_DOOR)) {
            world.entitySystem.getItems(world.map.id).stream()
                    .filter(item -> item.getID() == ItemID.WOOD_DOOR)
                    .filter(door -> isAdjacentTo(entity, door))
                    .findFirst()
                    .ifPresent(door -> {
                        game.getGameSystem().audio.playSound(AudioID.Sound.DOOR_OPENING);
                        world.entitySystem.removeItem(world.map.id, door);
                    });
            return true;
        }
        return false;
    }

    @Override
    public ItemID getID() {
        return ItemID.KEY;
    }

}
