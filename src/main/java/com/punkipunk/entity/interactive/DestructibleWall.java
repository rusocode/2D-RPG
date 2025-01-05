package com.punkipunk.entity.interactive;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.InteractiveData;
import com.punkipunk.core.Game;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.item.ItemType;
import com.punkipunk.entity.item.Stone;
import com.punkipunk.world.World;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

public class DestructibleWall extends Interactive {

    public DestructibleWall(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("interactive.destructibleWall", InteractiveData.class), pos);
        hitbox = new Rectangle(0, 0, tile, tile);
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
        game.system.audio.playSound(interactiveData.sound());
    }
}
