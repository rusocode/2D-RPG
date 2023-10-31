package com.craivet.world.entity.projectile;

import com.craivet.Direction;
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
 * Este hechizo es diferente a Fireball ya que cuenta con 5 frames a diferencia de 2. Por lo tanto, lo logico seria
 * que se renderizen solo los 5 frames para cumplir con la animacion del hechizo correctamente y no hasta que se acabe
 * la vida de este (por lo tanto la vida estaria de mas).
 */

// TODO Los hechizos podrian extender de una clase llamada Spell
public class BurstOfFire extends Projectile {

    private Entity entity;

    public BurstOfFire(Game game, World world) {
        super(game, world);
        stats.name = "Burst of Fire";
        stats.speed = 4;
        stats.hp = stats.maxHp = 80;
        stats.attack = 4;
        stats.knockbackValue = 7;
        flags.alive = false;
        cost = 0;
        int scale = 2;
        hitbox = new Rectangle(0, 0, tile * scale, tile * scale);
        sheet.loadBurstOfFireFrames(burst_of_fire, scale);

        int animationSpeed = 160;
        down = new Animation(animationSpeed, sheet.down);
        up = new Animation(animationSpeed, sheet.up);
        left = new Animation(animationSpeed, sheet.left);
        right = new Animation(animationSpeed, sheet.right);
    }

    /* Es importante saber que si se sobreescribe un metodo como por ejemplo update(), entonces se ANULA la funcion del
     * metodo original (metodo heredado) por la nueva implementacion. */

    @Override
    public void update() {

        // El proyectil deja de vivir (alive = false) cuando colisiona con un mob o cuando se le acaba el hp

        if (entity instanceof Player) { // TODO No creo que haga falta comprobar si el que lanza el hechizo es el player ya que es el unico que lo puede lanzar (por ahora)
            int mobIndex = game.collision.checkEntity(this, world.mobs);
            if (mobIndex != -1) {
                Mob mob = world.mobs[world.map][mobIndex];
                if (!mob.flags.invincible && mob.type != Type.NPC) {
                    world.player.hitMob(mobIndex, this, stats.knockbackValue, getAttack());
                    generateParticle(entity.projectile, mob);
                    flags.alive = false;
                    // right.setFrame(0);
                }
            }
        }

        // Si esta vivo
        if (flags.alive) { // TODO Creo que no hace falta comprobar si esta vivo
            // Actualiza la posicion!
            pos.update(this, direction);

            // Actualiza la animacion!
            // down.tick();
            // up.tick();
            // left.tick();
            right.tick();
        }

        // Cuando llega al ultimo frame, deja de vivir
        if (right.getCurrentFrame().equals(right.getLastFrame())) flags.alive = false;

    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(getCurrentAnimationFrame(), getScreenX(), getScreenY(), null);
        // drawRects(g2);
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
        return 20;
    }

    @Override
    public void set(int x, int y, Direction direction, boolean alive, Entity entity) {
        pos.x = x;
        pos.y = y;
        this.direction = direction;
        flags.alive = alive;
        this.entity = entity;
        stats.hp = stats.maxHp;
    }

    private BufferedImage getCurrentAnimationFrame() {
        switch (direction) {
            case DOWN -> currentFrame = down.getCurrentFrame();
            case UP -> currentFrame = up.getCurrentFrame();
            case LEFT -> currentFrame = left.getCurrentFrame();
            case RIGHT -> {
                /* Cuando el hechizo golpea a un mob y cuando se vuelve a lanzar, el frame del hechizo comienza a partir
                 * desde el ultimo frame que golpeo al mob. Por lo tanto, lo ideal seria que el hechizo vuelva al frame
                 * inicial cuando golpea a un mob. */
                currentFrame = right.getCurrentFrame();
            }
        }
        return currentFrame;
    }

    /**
     * Calcula el ataque dependiendo del lvl del player. Para el lvl 1, el ataque disminuye el doble. Osea, si el ataque
     * del hechizo es 4 y el lvl del player es 1, entonces el ataque es de 2. Para el lvl 2, el ataque es el mismo, y
     * para los siguientes niveles el ataque aumenta en 2.
     *
     * @return el valor de ataque.
     */
    private int getAttack() {
        return stats.attack * (entity.stats.lvl / 2);
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
