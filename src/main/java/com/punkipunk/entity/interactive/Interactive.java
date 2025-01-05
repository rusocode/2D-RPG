package com.punkipunk.entity.interactive;

import com.punkipunk.json.model.InteractiveData;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.item.Item;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.INTERVAL_INVINCIBLE_INTERACTIVE;
import static com.punkipunk.utils.Global.tile;

public abstract class Interactive extends Entity {

    public boolean destructible;

    protected InteractiveData interactiveData;

    public Interactive(Game game, World world, InteractiveData interactiveData, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);

        this.interactiveData = interactiveData;

        stats.hp = interactiveData.hp();
        destructible = interactiveData.destructible();
        sheet.frame = Utils.scaleTexture(Utils.loadTexture(interactiveData.texturePath()), tile, tile);

    }

    public void update() {
        if (flags.invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE_INTERACTIVE);
    }

    /**
     * Check if the selected weapon is the correct one to use with the interactive tile.
     *
     * @param weapon selected weapon.
     * @return true if the selected weapon is correct.
     */
    public boolean isCorrectWeapon(Item weapon) {
        return false;
    }

    /**
     * Replaces the interactive tile (when destroyed) with the new interactive tile.
     *
     * @return the new interactive tile.
     */
    public Interactive replaceBy() {
        return null;
    }

    public void playSound() {
    }

}
