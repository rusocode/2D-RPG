package com.punkipunk.entity.interactive;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.item.ItemCategory;
import com.punkipunk.entity.item.Stone;
import com.punkipunk.world.World;
import javafx.scene.paint.Color;

public class DestructibleWall extends Interactive {

    public DestructibleWall(Game game, World world, int... pos) {
        super(game, world, pos);
    }

    public boolean isCorrectWeapon(Item weapon) {
        return weapon.itemCategory == ItemCategory.PICKAXE;
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
        drop(new Stone(game, world, 1));
    }

    @Override
    public void playSound() {
        game.gameSystem.audio.playSound(AudioID.Sound.MINE);
    }

    @Override
    public InteractiveType getType() {
        return InteractiveType.DESTRUCTIBLE_WALL;
    }

}
