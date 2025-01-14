package com.punkipunk.entity.mob;

import com.punkipunk.Dialogue;
import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.states.State;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

public class Trader extends Mob {

    public Trader(Game game, World world, int... pos) {
        super(game, world, pos);
        dialogue = new Dialogue(game);
        sheet.frame = Utils.scaleTexture(Utils.loadTexture(mobData.texturePath()), mobData.frameWidth(), mobData.frameHeight());
        initDialogue();
        addItemsToInventory();
    }

    @Override
    public void dialogue() {
        game.system.audio.playSound(AudioID.Sound.TRADE_OPENING);
        dialogue.startDialogue(State.TRADE, this, 0);
    }

    @Override
    protected MobType getType() {
        return MobType.TRADER;
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "He he, so you found me. I have some \ngood stuff. Do you want to trade?";
    }

    private void addItemsToInventory() {

    }

}
