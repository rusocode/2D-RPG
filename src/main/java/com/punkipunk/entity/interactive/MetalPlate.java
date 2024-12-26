package com.punkipunk.entity.interactive;

import com.punkipunk.core.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

public class MetalPlate extends Interactive {

    public static final String NAME = "Metal Plate";

    public MetalPlate(Game game, World world, int x, int y) {
        super(game, world, x, y);
        stats.name = NAME;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.ITILE_METAL_PLATE), tile, tile);
        hitbox = new Rectangle(0, 0, 0, 0);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
    }


}
