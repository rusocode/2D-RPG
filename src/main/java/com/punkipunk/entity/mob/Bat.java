package com.punkipunk.entity.mob;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.MobData;
import com.punkipunk.core.Game;
import com.punkipunk.entity.item.Gold;
import com.punkipunk.entity.item.StoneAxe;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

public class Bat extends Mob {

    public Bat(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("mobs.bat", MobData.class), pos);
        mobType = MobType.PEACEFUL;
        sheet.loadMovementFrames(new SpriteSheet(Utils.loadTexture(mobData.spriteSheetPath())), mobData.frameWidth(), mobData.frameHeight(), mobData.frameScale());
    }

    @Override
    public void doActions() {
        timer.timeDirection(this, Utils.random(100, 200));
    }

    @Override
    public void checkDrop() {
        drop(this, new Gold(game, world, mobData.gold()));
        if (Utils.random(100) <= mobData.probabilityItemDrop()) drop(this, new StoneAxe(game, world));
    }

}