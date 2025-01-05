package com.punkipunk.entity.interactive;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.InteractiveData;
import com.punkipunk.core.Game;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.item.ItemType;
import com.punkipunk.entity.item.Wood;
import com.punkipunk.world.World;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

public class DryTree extends Interactive {

    public DryTree(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("interactive.dryTree", InteractiveData.class), pos);
        hitbox = new Rectangle(0, 0, tile, tile);
    }

    public boolean isCorrectWeapon(Item weapon) {
        return weapon.itemType == ItemType.AXE;
    }

    public Color getParticleColor() {
        return Color.rgb(121, 90, 47);
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
        drop(this, new Wood(game, world, 1));
    }

    @Override
    public void playSound() {
        game.system.audio.playSound(interactiveData.sound());
    }
}
