package com.punkipunk.entity.spells;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.SpellData;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.mob.Mob;
import com.punkipunk.entity.mob.MobType;
import com.punkipunk.gfx.Animation;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

/**
 * <p>
 * Este hechizo es diferente de Fireball ya que tiene 5 frames en lugar de 2. Por lo tanto, la logica seria que solo se
 * rendericen los 5 frames para cumplir correctamente la animacion del hechizo y no hasta que se acabe su vida (por lo tanto, la
 * vida seria innecesaria).
 * <p>
 * TODO Aumentar la duracion del ultimo fotograma
 */

public class BurstOfFire extends Spell {

    public BurstOfFire(Game game, World world) {
        super(game, world, JsonLoader.getInstance().deserialize("spells.burstOfFire", SpellData.class));
        hitbox = new Rectangle(0, 0, tile * spellData.frameScale() - 35, tile * spellData.frameScale());
        sheet.loadBurstOfFireFrames(new SpriteSheet(Utils.loadTexture(spellData.spriteSheetPath())), spellData.frameScale());
        down = new Animation(spellData.animationSpeed(), sheet.down);
        up = new Animation(spellData.animationSpeed(), sheet.up);
        left = new Animation(spellData.animationSpeed(), sheet.left);
        right = new Animation(spellData.animationSpeed(), sheet.right);
    }

    @Override
    public void update() {

        // FIXME Hay un bug cuando colisiona con un mob, el siguiente hechizo hace una distancia mas corta

        // The spell stops living (alive = false) when it collides with a mob or when it reaches the last frame

        int mobIndex = game.system.collisionChecker.checkEntity(this, world.entities.mobs);
        if (mobIndex != -1) {
            Mob mob = world.entities.mobs[world.map.num][mobIndex];
            if (!mob.flags.invincible && mob.mobType != MobType.NPC) {
                world.entities.player.hitMob(mobIndex, this, stats.knockback, getAttack());
                generateParticle(entity.spell, mob);
                flags.alive = false;
                resetFrames();
            }
        }

        if (flags.alive) {
            // Update the position!
            pos.update(this, direction);
            // Update the animation!
            down.tick();
            up.tick();
            left.tick();
            right.tick();
        }

        // When it reaches the last frame, it stops living
        if (down.getCurrentFrame().equals(down.getLastFrame()) || up.getCurrentFrame().equals(up.getLastFrame()) || left.getCurrentFrame().equals(left.getLastFrame()) || right.getCurrentFrame().equals(right.getLastFrame()))
            flags.alive = false;

    }

    @Override
    public void render(GraphicsContext g2) {
        g2.drawImage(getCurrentAnimationFrame(), getScreenX() - 18, getScreenY());
        if (game.system.keyboard.isKeyPressed(Key.RECTS)) drawRects(g2);
    }

    @Override
    public boolean haveResource(Entity entity) {
        return entity.stats.mana >= cost;
    }

    @Override
    public void subtractResource(Entity entity) {
        entity.stats.mana -= cost;
    }

    @Override
    public Color getParticleColor() {
        return Color.rgb(215, 73, 36);
    }

    @Override
    public int getParticleSize() {
        return 15;
    }

    @Override
    public int getParticleSpeed() {
        return 1;
    }

    @Override
    public int getParticleMaxLife() {
        return 30;
    }

    private Image getCurrentAnimationFrame() {
        switch (direction) {
            case DOWN -> currentFrame = down.getCurrentFrame();
            case UP -> currentFrame = up.getCurrentFrame();
            case LEFT -> currentFrame = left.getCurrentFrame();
            case RIGHT -> currentFrame = right.getCurrentFrame();
        }
        return currentFrame;
    }

    /**
     * Resets frames to the 0 index.
     */
    private void resetFrames() {
        down.setFrame(0);
        up.setFrame(0);
        left.setFrame(0);
        right.setFrame(0);
    }

    private void drawRects(GraphicsContext gc) {
        // Frame
        gc.setStroke(Color.MAGENTA);
        gc.setLineWidth(0);
        gc.strokeRect(getScreenX(), getScreenY(), sheet.frame.getWidth(), sheet.frame.getHeight());
        // Hitbox
        gc.setStroke(Color.GREEN);
        gc.strokeRect(getScreenX() + hitbox.getX(), getScreenY() + hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
        // Attackbox
        if (flags.hitting) {
            gc.setStroke(Color.RED);
            gc.strokeRect(getScreenX() + attackbox.getX() + hitbox.getX(),
                    getScreenY() + attackbox.getY() + hitbox.getY(),
                    attackbox.getWidth(), attackbox.getHeight());
        }
    }

}
