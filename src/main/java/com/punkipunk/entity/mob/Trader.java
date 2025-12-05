package com.punkipunk.entity.mob;

import com.punkipunk.Dialogue;
import com.punkipunk.audio.AudioID;
import com.punkipunk.core.IGame;
import com.punkipunk.states.State;
import com.punkipunk.world.World;

public class Trader extends Mob {

    public Trader(IGame game, World world, int... pos) {
        super(game, world, pos);
        dialogue = new Dialogue(game);
        sheet.loadStaticFrame(mobData.texturePath(), mobData.frameWidth(), mobData.frameHeight());
        currentFrame = sheet.frame;
        initDialogue();
        addItemsToInventory();
    }

    @Override
    public void dialogue() {
        game.getGameSystem().audio.playSound(AudioID.Sound.TRADE_OPENING);
        dialogue.startDialogue(State.TRADE, this, 0);
    }

    @Override
    public MobID getID() {
        return MobID.TRADER;
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "He he, so you found me. I have some \ngood stuff. Do you want to trade?";
    }

    private void addItemsToInventory() {

    }

}
