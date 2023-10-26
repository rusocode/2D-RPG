package com.craivet.world.entity.projectile;

import com.craivet.Direction;
import com.craivet.Game;
import com.craivet.gfx.Animation;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Player;
import com.craivet.world.entity.Type;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class BurstOfFire extends Projectile {

    private Entity entity;

    public BurstOfFire(Game game, World world) {
        super(game, world);
        stats.name = "Burst of Fire";
        stats.speed = 5;
        stats.hp = stats.maxHp = 80;
        stats.attack = 3;
        stats.knockbackValue = 7;
        flags.alive = false;
        cost = 0;
        int scale = 2;
        hitbox = new Rectangle(0, 0, tile * scale, tile * scale);
        sheet.loadBurstOfFireFrames(burst_of_fire, scale);

        int animationSpeed = 120;
        down = new Animation(animationSpeed, sheet.down);
        up = new Animation(animationSpeed, sheet.up);
        left = new Animation(animationSpeed, sheet.left);
        right = new Animation(animationSpeed, sheet.right);
        currentFrame = down.getFirstFrame();
    }

    /* Es importante saber que si se sobreescribe un metodo como update(), entonces se ANULA la funcion del metodo
     * original (metodo heredado) por la nueva implementacion. */
    @Override
    public void update() {

        // If the player shooting a projectile
        if (entity instanceof Player) {
            int mobIndex = game.collision.checkEntity(this, world.mobs);
            /* When the projectile collides with a mob, set the colliding state to true. Therefore, when the projectilew
             * is redrawn, it will remain in motion frame 1 since in the ternary operator, the condition remains true
             * and never changes to false in order to display motion frame 2. The following line solves this problem. */
            flags.colliding = false;
            if (mobIndex != -1 && !world.mobs[world.map][mobIndex].flags.invincible && world.mobs[world.map][mobIndex].type != Type.NPC) {
                world.player.hitMob(mobIndex, this, stats.knockbackValue, stats.attack * (entity.stats.lvl / 2));
                // In this case, the particle generator is the fireball when the player throws it against a mob
                generateParticle(entity.projectile, world.mobs[world.map][mobIndex]);
                flags.alive = false;
            }
        }

        if (stats.hp-- <= 0) flags.alive = false;

        // Si esta vivo
        if (flags.alive) { // TODO Creo que no hace falta comprobar si esta vivo
            // Actualiza la posicion!
            pos.update(this, direction);

            // Actualiza la animacion!
            // down.tick();
            //up.tick();
            //left.tick();
            right.tick();
        }
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(getCurrentAnimationFrame(), getScreenX(), getScreenY(), null);
        drawRects(g2);
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
        return new Color(240, 50, 0);
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
        return 20;
    }

    @Override
    public void set(int x, int y, Direction direction, boolean alive, Entity entity) {
        pos.x = x;
        pos.y = y;
        this.direction = direction;
        flags.alive = alive;
        this.entity = entity;
        /* Once the projectile dies (alive=false) the hp remains at 0, therefore to launch the next one you need to set
         * the life to maximum again. */
        stats.hp = stats.maxHp;
    }

    private BufferedImage getCurrentAnimationFrame() {
        switch (direction) {
            case DOWN -> {
                currentFrame = down.getFirstFrame();
                if (flags.collidingOnMob) return down.getCurrentFrame();
                else return flags.colliding ? down.getFirstFrame() : down.getCurrentFrame();
            }
            case UP -> {
                currentFrame = up.getFirstFrame();
                if (flags.collidingOnMob) return up.getCurrentFrame();
                else return flags.colliding ? up.getFirstFrame() : up.getCurrentFrame();
            }
            case LEFT -> {
                currentFrame = left.getFirstFrame();
                if (flags.collidingOnMob) return left.getCurrentFrame();
                else return flags.colliding ? left.getFirstFrame() : left.getCurrentFrame();
            }
            case RIGHT -> {
                currentFrame = right.getFirstFrame();
                if (flags.collidingOnMob) return right.getCurrentFrame();
                else return flags.colliding ? right.getFirstFrame() : right.getCurrentFrame();
            }
        }
        return currentFrame;
    }

    private void drawRects(Graphics2D g2) {
        g2.setStroke(new BasicStroke(0));
        // Frame
        g2.setColor(Color.magenta);
        g2.drawRect(getScreenX(), getScreenY(), sheet.frame.getWidth(), sheet.frame.getHeight());
        // Hitbox
        g2.setColor(Color.green);
        g2.drawRect(getScreenX() + hitbox.x, getScreenY() + hitbox.y, hitbox.width, hitbox.height);
        // Attackbox
        if (flags.hitting) {
            g2.setColor(Color.red);
            g2.drawRect(getScreenX() + attackbox.x + hitbox.x, getScreenY() + attackbox.y + hitbox.y, attackbox.width, attackbox.height);
        }
    }

}
