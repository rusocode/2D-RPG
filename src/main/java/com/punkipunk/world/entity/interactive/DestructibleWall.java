package com.punkipunk.world.entity.interactive;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Type;
import com.punkipunk.world.entity.item.Item;
import com.punkipunk.world.entity.item.ItemType;
import com.punkipunk.world.entity.item.Stone;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.PROBABILITY_STONE_DROP;
import static com.punkipunk.utils.Global.tile;

public class DestructibleWall extends Interactive {

    public DestructibleWall(Game game, World world, int x, int y) {
        super(game, world, x, y);
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.ITILE_DESTRUCTIBLE_WALL), tile, tile);
        destructible = true;
        stats.hp = 3;
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
        if (Utils.random(100) <= PROBABILITY_STONE_DROP) drop(this, new Stone(game, world, 1));
    }

    @Override
    public void playSound() {
        game.system.audio.playSound(Assets.getAudio(AudioAssets.MINE));
    }
}
