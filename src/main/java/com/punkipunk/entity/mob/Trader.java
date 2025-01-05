package com.punkipunk.entity.mob;

import com.punkipunk.Dialogue;
import com.punkipunk.audio.AudioID;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.MobData;
import com.punkipunk.core.Game;
import com.punkipunk.states.State;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import javafx.scene.shape.Rectangle;

public class Trader extends Mob {

    public Trader(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("mobs.trader", MobData.class), pos);
        dialogue = new Dialogue(game);
        mobType = MobType.NPC;
        hitbox = new Rectangle(7, 40, 15, 24);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        sheet.frame = Utils.scaleTexture(Utils.loadTexture(mobData.texturePath()), mobData.frameWidth(), mobData.frameHeight());
        initDialogue();
        addItemsToInventory();
    }

    @Override
    public void dialogue() {
        game.system.audio.playSound(AudioID.Sound.TRADE_OPENING);
        dialogue.startDialogue(State.TRADE, this, 0);
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "He he, so you found me. I have some \ngood stuff. Do you want to trade?";
    }

    private void addItemsToInventory() {

    }

}
