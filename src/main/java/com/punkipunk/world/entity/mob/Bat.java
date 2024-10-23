package com.punkipunk.world.entity.mob;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.assets.SpriteSheetAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Type;
import com.punkipunk.world.entity.item.Gold;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.*;

public class Bat extends Mob {

    public Bat(Game game, World world, int col, int row) {
        super(game, world, col, row);
        type = Type.HOSTILE;
        stats.name = "Bat";
        stats.speed = stats.defaultSpeed = 2;
        stats.hp = stats.maxHp = 2;
        stats.exp = 7;
        stats.attack = 1;
        stats.defense = 1;
        soundHit = Assets.getAudio(AudioAssets.BAT_HIT2);
        soundDeath = Assets.getAudio(AudioAssets.BAT_DEATH2);
        hitbox = new Rectangle(0, 0, tile, tile);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        sheet.loadMovementFrames(Assets.getSpriteSheet(SpriteSheetAssets.BAT), 32, 32, 1);
    }

    @Override
    public void doActions() {
        timer.timeDirection(this, INTERVAL_DIRECTION_BAT);
    }

    @Override
    public void checkDrop() {
        if (Utils.random(100) <= PROBABILITY_GOLD_DROP) drop(this, new Gold(game, world, 10));
    }

}