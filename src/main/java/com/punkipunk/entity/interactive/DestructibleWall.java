package com.punkipunk.entity.interactive;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.item.ItemType;
import com.punkipunk.entity.item.Stone;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.InteractiveData;
import com.punkipunk.world.World;
import javafx.scene.paint.Color;

public class DestructibleWall extends Interactive {

    public DestructibleWall(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("interactive.destructibleWall", InteractiveData.class), pos);
    }

    public boolean isCorrectWeapon(Item weapon) {
        return weapon.itemType == ItemType.PICKAXE;
    }

    public Color getParticleColor() {
        return Color.rgb(131, 130, 130);
    }

    public int getParticleSize() {
        return 6;
    }

    public int getParticleSpeed() {
        return 1;
    }

    public int getParticleMaxLife() {
        return 20;
    }

    public void checkDrop() {
        drop(this, new Stone(game, world, 1));
    }

    @Override
    public void playSound() {
        game.system.audio.playSound(AudioID.Sound.MINE);
    }
}
