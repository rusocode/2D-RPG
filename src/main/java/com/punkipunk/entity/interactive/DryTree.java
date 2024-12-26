package com.punkipunk.entity.interactive;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.item.ItemType;
import com.punkipunk.entity.item.Wood;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.PROBABILITY_WOOD_DROP;
import static com.punkipunk.utils.Global.tile;

public class DryTree extends Interactive {

    public DryTree(Game game, World world, int x, int y) {
        super(game, world, x, y);
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.ITILE_DRY_TREE), tile, tile);
        destructible = true;
        stats.hp = 1;
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
        if (Utils.random(100) <= PROBABILITY_WOOD_DROP) drop(this, new Wood(game, world, 1));
    }

    @Override
    public void playSound() {
        game.system.audio.playSound(AudioID.Sound.CUT_TREE);
    }
}
