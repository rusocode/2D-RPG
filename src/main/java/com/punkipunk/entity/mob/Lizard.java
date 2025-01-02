package com.punkipunk.entity.mob;

import com.punkipunk.Dialogue;
import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.SpriteSheetAssets;
import com.punkipunk.io.Progress;
import com.punkipunk.world.World;
import com.punkipunk.entity.item.IronDoor;
import com.punkipunk.entity.item.Gold;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.INTERVAL_DIRECTION;
import static com.punkipunk.utils.Global.tile;

public class Lizard extends Mob {

    public static final String NAME = "Lizard";

    public Lizard(Game game, World world, int col, int row) {
        super(game, world, col, row);
        dialogue = new Dialogue(game);
        mobType = MobType.HOSTILE;
        stats.name = NAME;
        stats.speed = stats.defaultSpeed = 1;
        stats.hp = stats.maxHp = 50;
        stats.exp = 50;
        stats.attack = 10;
        stats.defense = 2;
        stats.motion1 = 25;
        stats.motion2 = 50;
        soundHit = AudioID.Sound.MOB_HIT;
        soundDeath = AudioID.Sound.MOB_DEATH;
        flags.boss = true;
        sleep = true;
        int scale = 1;
        int size = tile * scale;
        hitbox = new Rectangle(tile, tile * 2, 170 - tile * 2, 180 - tile * 3);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        attackbox.setWidth(90);
        attackbox.setHeight(90);
        sheet.loadMovementFrames(Assets.getSpriteSheet(SpriteSheetAssets.LIZARD), 170, 180, scale);

        initDialogue();
    }

    @Override
    public void doActions() {
        // TODO Make it follow the player when the Skeleton is stuck on a tile
        // If the distance of the player with respect to the mob is less than 10 tiles
        if (getTileDistance(game.system.world.entities.player) < 10) moveTowardPlayer(game.system.world.entities.player, 30);
        else timer.timeDirection(this, INTERVAL_DIRECTION);
        // if (!flags.hitting) isPlayerWithinAttackRange(60, tile * 6, tile * 4, 60); // TODO No se utiliza ya que por ahora el boss no tiene un sprite de ataque
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "No one can steal my treasure.";
        dialogue.dialogues[0][1] = "You will die here.";
        dialogue.dialogues[0][2] = "WELCOME TO YOUR DOOM!";
    }

    @Override
    public void damageReaction() {
        // timer.directionCounter = 0;
    }

    @Override
    public void checkDrop() {

        drop(this, new Gold(game, world, 1000));

        Progress.bossDefeated = true;

        // Remove the iron doors
        for (int i = 0; i < world.entities.items[1].length; i++)
            if (world.entities.items[world.map.num][i] != null && world.entities.items[world.map.num][i].stats.name.equals(IronDoor.NAME))
                world.entities.items[world.map.num][i] = null;

    }

}
