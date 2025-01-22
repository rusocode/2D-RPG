package com.punkipunk.entity.item;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.world.World;

public class Key extends Item {

    public Key(Game game, World world, int... pos) {
        super(game, world, pos);
    }

    @Override
    public boolean use(Entity entity) {
        // Si la entidad detecta una puerta de madera, entonces puede usar la llave
        if (isNearby(entity, ItemType.WOOD_DOOR)) {
            world.entitySystem.getItems(world.map.num).stream()
                    .filter(item -> item.getType() == ItemType.WOOD_DOOR)
                    .filter(door -> isAdjacentTo(entity, door))
                    .findFirst()
                    .ifPresent(door -> {
                        game.gameSystem.audio.playSound(AudioID.Sound.DOOR_OPENING);
                        world.entitySystem.removeItem(world.map.num, door);
                    });
            return true;
        }
        return false;
    }

    @Override
    public ItemType getType() {
        return ItemType.KEY;
    }

}
