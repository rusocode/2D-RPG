package com.punkipunk.entity.interactive;

import com.punkipunk.core.IGame;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.item.Item;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.InteractiveData;
import com.punkipunk.world.World;
import com.punkipunk.physics.Rectangle;

import static com.punkipunk.utils.Global.INTERVAL_INVINCIBLE_INTERACTIVE;
import static com.punkipunk.utils.Global.tile;

public abstract class Interactive extends Entity {

    public boolean destructible;
    protected InteractiveData interactiveData;

    public Interactive(IGame game, World world, int... pos) {
        super(game, world, pos);

        this.interactiveData = JsonLoader.getInstance().deserialize("interactive." + getID().name, InteractiveData.class);

        stats.hp = interactiveData.hp();
        destructible = interactiveData.destructible();
        sheet.loadStaticFrame(interactiveData.texturePath(), tile, tile);

        hitbox = new Rectangle(
                interactiveData.hitbox().x(),
                interactiveData.hitbox().y(),
                interactiveData.hitbox().width(),
                interactiveData.hitbox().height()
        );
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();

    }

    public abstract InteractiveID getID();

    public void update() {
        if (flags.invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE_INTERACTIVE);
    }

    /**
     * Comprueba si el arma seleccionada es la correcta para usar con el tile interactivo.
     *
     * @param weapon arma seleccionada
     * @return true si el arma seleccionada es correcta
     */
    public boolean isCorrectWeapon(Item weapon) {
        return false;
    }

    /**
     * Reemplaza el interactivo (cuando se destruye) con el nuevo interactivo.
     *
     * @return el nuevo interactivo
     */
    public Interactive replaceBy() {
        return null;
    }

    public void playSound() {
    }

}
