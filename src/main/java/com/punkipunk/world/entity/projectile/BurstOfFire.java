package com.punkipunk.world.entity.projectile;

import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.assets.SpriteSheetAssets;
import com.punkipunk.gfx.Animation;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Entity;
import com.punkipunk.world.entity.Type;
import com.punkipunk.world.entity.mob.Mob;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

/**
 * This spell is different from Fireball since it has 5 frames as opposed to 2. Therefore, the logic would be that only the 5
 * frames are rendered to fulfill the animation of the spell correctly and not until its life runs out (therefore life would be
 * unnecessary).
 * <p>
 * TODO Increase the duration of the last frame
 */

public class BurstOfFire extends Projectile {

    public BurstOfFire(Game game, World world) {
        super(game, world);
        stats.name = "Burst of Fire";
        stats.speed = 5;
        stats.attack = 4;
        stats.knockbackValue = 7;
        flags.alive = false;
        cost = 2;
        int scale = 2;
        sound = Assets.getAudio(AudioAssets.BURST_OF_FIRE2);
        hitbox = new Rectangle(0, 0, tile * scale - 35, tile * scale);
        sheet.loadBurstOfFireFrames(Assets.getSpriteSheet(SpriteSheetAssets.BURST_OF_FIRE), scale);
        interval = 180;

        int animationSpeed = 120; // 80
        down = new Animation(animationSpeed, sheet.down);
        up = new Animation(animationSpeed, sheet.up);
        left = new Animation(animationSpeed, sheet.left);
        right = new Animation(animationSpeed, sheet.right);
    }

    /* It is important to know that if a method such as update() is overridden, then the function of the original method
     * (inherited method) is OVERRIDED by the new implementation. */

    @Override
    public void update() {

        // FIXME Hay un bug cuando colisiona con un mob, el siguiente hechizo hace una distancia mas corta

        // The spell stops living (alive = false) when it collides with a mob or when it reaches the last frame

        int mobIndex = game.systems.collision.checkEntity(this, world.entities.mobs);
        if (mobIndex != -1) {
            Mob mob = world.entities.mobs[world.map.num][mobIndex];
            if (!mob.flags.invincible && mob.type != Type.NPC) {
                world.entities.player.hitMob(mobIndex, this, stats.knockbackValue, getAttack());
                generateParticle(entity.projectile, mob);
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
        if (game.systems.keyboard.isKeyPressed(Key.RECTS)) drawRects(g2);
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
