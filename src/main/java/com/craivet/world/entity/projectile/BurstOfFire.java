package com.craivet.world.entity.projectile;

import com.craivet.Game;
import com.craivet.gfx.Animation;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Player;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.mob.Mob;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

/**
 * Este hechizo es distinto a Fireball ya que cuenta con 5 frames a diferencia de 2. Por lo tanto, lo logico seria que
 * se renderizen solo los 5 frames para cumplir con la animacion del hechizo correctamente y no hasta que se acabe la
 * vida de este (por lo tanto la vida estaria de mas).
 * <p>
 * TODO Aumentar la duracion del ultimo frame
 */

public class BurstOfFire extends Projectile {

    public BurstOfFire(Game game, World world) {
        super(game, world);
        stats.name = "Burst of Fire";
        stats.speed = 5;
        stats.attack = 4;
        stats.knockbackValue = 7;
        flags.alive = false;
        cost = 0;
        int scale = 2;
        sound = sound_burst_of_fire;
        hitbox = new Rectangle(0, 0, tile * scale - 35, tile * scale);
        sheet.loadBurstOfFireFrames(burst_of_fire, scale);
        interval = 180;

        int animationSpeed = 120; // 80
        down = new Animation(animationSpeed, sheet.down);
        up = new Animation(animationSpeed, sheet.up);
        left = new Animation(animationSpeed, sheet.left);
        right = new Animation(animationSpeed, sheet.right);
    }

    /* Es importante saber que si se sobreescribe un metodo como por ejemplo update(), entonces se ANULA la funcion del
     * metodo original (metodo heredado) por la nueva implementacion. */

    @Override
    public void update() {

        // El hechizo deja de vivir (alive = false) cuando colisiona con un mob o cuando llega al ultimo frame

        if (entity instanceof Player) { // TODO No creo que haga falta comprobar si el que lanza el hechizo es el player ya que es el unico que lo puede lanzar (por ahora)
            int mobIndex = game.collision.checkEntity(this, world.entities.mobs);
            if (mobIndex != -1) {
                Mob mob = world.entities.mobs[world.map.num][mobIndex];
                if (!mob.flags.invincible && mob.type != Type.NPC) {
                    world.entities.player.hitMob(mobIndex, this, stats.knockbackValue, getAttack());
                    generateParticle(entity.projectile, mob);
                    flags.alive = false;
                    resetFrames(0);
                }
            }
        }

        // Si esta vivo
        if (flags.alive) { // TODO Creo que no hace falta comprobar si esta vivo

            pos.update(this, direction); // Actualiza la posicion!

            // Actualiza la animacion!
            down.tick();
            up.tick();
            left.tick();
            right.tick();

        }

        // Cuando llega al ultimo frame, deja de vivir
        if (right.getCurrentFrame().equals(right.getLastFrame())) flags.alive = false;

    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(getCurrentAnimationFrame(), getScreenX() - 18, getScreenY(), null);
        if (game.keyboard.hitbox) drawRects(g2);
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
        return new Color(215, 73, 36);
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

    private BufferedImage getCurrentAnimationFrame() {
        switch (direction) {
            case DOWN -> currentFrame = down.getCurrentFrame();
            case UP -> currentFrame = up.getCurrentFrame();
            case LEFT -> currentFrame = left.getCurrentFrame();
            case RIGHT -> currentFrame = right.getCurrentFrame();
        }
        return currentFrame;
    }

    /**
     * Resets frames to the specified index.
     *
     * @param i frame index.
     */
    private void resetFrames(int i) {
        down.setFrame(i);
        up.setFrame(i);
        left.setFrame(i);
        right.setFrame(i);
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
