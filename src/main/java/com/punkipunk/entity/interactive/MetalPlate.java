package com.punkipunk.entity.interactive;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.InteractiveData;
import com.punkipunk.core.Game;
import com.punkipunk.world.World;
import javafx.scene.shape.Rectangle;

public class MetalPlate extends Interactive {

    public static final String NAME = "Metal Plate";

    public MetalPlate(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("interactive.metalPlate", InteractiveData.class), pos);
        stats.name = NAME;
        hitbox = new Rectangle(0, 0, 0, 0);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
    }


}
