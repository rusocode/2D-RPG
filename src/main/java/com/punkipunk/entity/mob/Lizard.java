package com.punkipunk.entity.mob;

import com.punkipunk.Dialogue;
import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.item.Gold;
import com.punkipunk.entity.item.ItemID;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.io.Progress;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.INTERVAL_DIRECTION;

/**
 * Es el unico mob que va a tener animacion de ataque.
 */

public class Lizard extends Mob {

    public Lizard(IGame game, World world, int... pos) {
        super(game, world, pos);
        soundHit = AudioID.Sound.MOB_HIT;
        soundDeath = AudioID.Sound.BAT_DEATH;
        dialogue = new Dialogue(game);
        attackbox.setWidth(mobData.attackboxWidth());
        attackbox.setHeight(mobData.attackboxHeight());
        sheet.loadMovementFrames(new SpriteSheet(Utils.loadTexture(mobData.spriteSheetPath())), mobData.frameWidth(), mobData.frameHeight(), mobData.frameScale());
        initDialogue();
    }

    @Override
    public void doActions() {
        // TODO Haz que siga al player cuando este atascado en un tile
        // Si la distancia del jugador con respecto al mob es menor a 10 tiles
        if (getTileDistance(game.getGameSystem().world.entitySystem.player) < 10) moveTowardPlayer(game.getGameSystem().world.entitySystem.player, 30);
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
        drop(new Gold(game, world, mobData.gold()));
        Progress.bossDefeated = true;
        // Elimina la IronDoor
        world.entitySystem.getItems(world.map.id).stream()
                .filter(item -> item.getID() == ItemID.IRON_DOOR)
                .findFirst()
                .ifPresent(door -> world.entitySystem.removeItem(world.map.id, door));
    }

    @Override
    public MobID getID() {
        return MobID.LIZARD;
    }

}
