package com.punkipunk.entity.spells;

import com.punkipunk.audio.AudioID;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.mob.MobCategory;
import com.punkipunk.entity.player.Player;
import com.punkipunk.gfx.Animation;
import com.punkipunk.gfx.Renderer2D;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.SpellData;
import com.punkipunk.world.World;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

/**
 * <p>
 * Este hechizo es diferente de Fireball ya que tiene 5 frames en lugar de 2. Por lo tanto, la logica seria que solo se rendericen
 * los 5 frames para cumplir correctamente la animacion del hechizo y no hasta que se acabe su vida (por lo tanto, la vida seria
 * innecesaria). Por lo tanto el hechizo deja de estar vivo (alive = false) cuando colisiona con un mob o cuando llega al ultimo
 * frame.
 * <p>
 * TODO Aumentar la duracion del ultimo fotograma
 */

public class BurstOfFire extends Spell {

    public BurstOfFire(IGame game, World world) {
        super(game, world, JsonLoader.getInstance().deserialize("spells.burstOfFire", SpellData.class));
        sound = AudioID.Sound.BURST_OF_FIRE;
        hitbox = new Rectangle(0, 0, tile * spellData.frameScale() - 35, tile * spellData.frameScale());
        sheet.loadBurstOfFireFrames(spellData.spriteSheetPath(), spellData.frameScale());
        down = new Animation(spellData.animationSpeed(), sheet.down);
        up = new Animation(spellData.animationSpeed(), sheet.up);
        left = new Animation(spellData.animationSpeed(), sheet.left);
        right = new Animation(spellData.animationSpeed(), sheet.right);
    }

    @Override
    public void update() {

        if (entity instanceof Player) {
            game.getGameSystem().collisionChecker.checkMob(this).ifPresent(mob -> {
                // flags.colliding = false;
                if (!mob.flags.invincible && mob.mobCategory != MobCategory.NPC) {
                    world.entitySystem.player.hitMob(mob, this, stats.knockback, getAttack());
                    generateParticle(entity.spell, mob);
                    flags.alive = false;
                    resetFrames();
                }
            });
        }

        if (!(entity instanceof Player)) {
            boolean contact = game.getGameSystem().collisionChecker.checkPlayer(this);
            if (contact && !world.entitySystem.player.flags.invincible) {
                hitPlayer(this, true, stats.attack);
                generateParticle(entity.spell, world.entitySystem.player);
                flags.alive = false;
                resetFrames();
            }
        }

        if (flags.alive) {
            position.update(this, direction);
            down.tick();
            up.tick();
            left.tick();
            right.tick();
        }

        // TODO No tendria que ir dentro del if flags.alive?
        // Cuando llega al ultimo frame, deja de vivir
        if (down.getCurrentFrame().equals(down.getLastFrame())
                || up.getCurrentFrame().equals(up.getLastFrame())
                || left.getCurrentFrame().equals(left.getLastFrame())
                || right.getCurrentFrame().equals(right.getLastFrame()))
            flags.alive = false;

    }

    // FIXME Mismo problema que el renderizado del player
    @Override
    public void render(Renderer2D renderer) {
        renderRegion(renderer, getCurrentAnimationFrame(), getScreenX() - 18, getScreenY());
        if (game.getGameSystem().keyboard.isKeyToggled(Key.RECTS)) drawRects(renderer);
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

    private SpriteSheet.SpriteRegion getCurrentAnimationFrame() {
        switch (direction) {
            case DOWN -> currentFrame = down.getCurrentFrame();
            case UP -> currentFrame = up.getCurrentFrame();
            case LEFT -> currentFrame = left.getCurrentFrame();
            case RIGHT -> currentFrame = right.getCurrentFrame();
        }
        return currentFrame;
    }

    /**
     * Resets frames to index 0.
     */
    private void resetFrames() {
        down.setFrame(0);
        up.setFrame(0);
        left.setFrame(0);
        right.setFrame(0);
    }

    private void drawRects(Renderer2D renderer) {
        // Frame
        renderer.setStroke(Color.MAGENTA);
        renderer.setLineWidth(1);
        renderer.strokeRect(getScreenX(), getScreenY(), currentFrame.width, currentFrame.height);
        // Hitbox
        renderer.setStroke(Color.BLUE);
        renderer.strokeRect(getScreenX() + hitbox.getX(), getScreenY() + hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
    }

}
