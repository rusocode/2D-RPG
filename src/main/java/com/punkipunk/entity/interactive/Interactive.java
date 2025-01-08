package com.punkipunk.entity.interactive;

import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.item.Item;
import com.punkipunk.json.model.InteractiveData;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.INTERVAL_INVINCIBLE_INTERACTIVE;

public abstract class Interactive extends Entity {

    public InteractiveData interactiveData;

    public Interactive(Game game, World world, InteractiveData interactiveData, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);

        this.interactiveData = interactiveData;

        stats.hp = interactiveData.hp();
        sheet.frame = Utils.loadTexture(interactiveData.texturePath());

        hitbox = new Rectangle(
                interactiveData.hitbox().x(),
                interactiveData.hitbox().y(),
                interactiveData.hitbox().width(),
                interactiveData.hitbox().height()
        );
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();

    }

    public void update() {
        if (flags.invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE_INTERACTIVE);
    }

    /**
     * Comprueba si el arma seleccionada es la correcta para usar con el tile interactivo.
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
