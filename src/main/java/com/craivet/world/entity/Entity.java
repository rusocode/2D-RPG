package com.craivet.world.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.*;
import com.craivet.gfx.Animation;
import com.craivet.gfx.Screen;
import com.craivet.gfx.SpriteSheet;
import com.craivet.physics.Mechanics;
import com.craivet.world.Position;
import com.craivet.world.entity.item.Item;
import com.craivet.world.entity.mob.Mob;
import com.craivet.world.World;
import com.craivet.utils.Timer;
import com.craivet.utils.Utils;
import com.craivet.world.entity.projectile.BurstOfFire;
import com.craivet.world.entity.projectile.Projectile;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public abstract class Entity {

    public final Game game;
    public final World world;

    public Type type = Type.HOSTILE;
    public Direction direction = Direction.DOWN;
    public Position pos = new Position();
    public Stats stats = new Stats();
    public Flags flags = new Flags();
    public Screen screen = new Screen();
    public SpriteSheet sheet = new SpriteSheet();
    public Timer timer = new Timer();
    public Mechanics mechanics = new Mechanics();
    public Rectangle hitbox = new Rectangle(0, 0, tile, tile);
    public Rectangle attackbox = new Rectangle(0, 0, 0, 0);
    public Inventory inventory;
    public Dialogue dialogue;
    public int hitboxDefaultX, hitboxDefaultY;

    public Animation down, up, left, right;
    public BufferedImage currentFrame, currentSwordFrame;

    public Projectile projectile;

    public Entity linkedEntity;

    public boolean boss; // TODO Move to Mob
    public boolean nextFrame;

    public Entity(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    public Entity(Game game, World world, int col, int row) {
        this.game = game;
        this.world = world;
        pos.set(col, row);
    }

    public void update() {
        if (flags.knockback) {
            checkCollisions();
            // If it doesn't collide, then update the position depending on the attacker's direction
            if (!flags.colliding) pos.update(this, direction.knockbackDirection);
            else mechanics.stopKnockback(this); // If it collides, it stops the knockback
            timer.timerKnockback(this, INTERVAL_KNOCKBACK);
        } else if (flags.hitting) hit();
        else {
            // It is important that you perform the actions before checking for collisions
            doActions();
            checkCollisions();
            if (!flags.colliding) pos.update(this, direction);
        }
        timer.checkTimers(this);
    }

    public void render(Graphics2D g2) {
        if (isOnCamera()) {
            screen.tempScreenX = getScreenX();
            screen.tempScreenY = getScreenY();

            // If the hostile mob that is not a boss has the life bar activated
            if (type == Type.HOSTILE && flags.hpBar && !boss) game.ui.renderHpBar(this);
            if (flags.invincible) {
                // Without this, the bar disappears after 4 seconds, even if the player continues attacking the mob
                timer.hpBarCounter = 0;
                Utils.changeAlpha(g2, 0.4f);
            }
            if (flags.dead) timer.timeDeadAnimation(this, INTERVAL_DEAD_ANIMATION, g2);

            // If it is an animation
            if (sheet.movement != null || sheet.attack != null)
                g2.drawImage(sheet.getCurrentAnimationFrame(this), screen.tempScreenX, screen.tempScreenY, null);
                // If it is a static image (item, interactive tile)
            else g2.drawImage(sheet.frame, getScreenX(), getScreenY(), null);

            // drawRects(g2);

            Utils.changeAlpha(g2, 1f);
        }
    }

    /**
     * Perform a sequence of actions.
     */
    protected void doActions() {
    }

    /**
     * Check if I drop an object.
     */
    public void checkDrop() {
    }

    public void startDialogue(int state, Entity entity, int set) {
        game.state = state;
        game.ui.entity = entity;
        dialogue.set = set;
    }

    public Color getParticleColor() {
        return null;
    }

    public int getParticleSize() {
        return 0;
    }

    public int getParticleSpeed() {
        return 0;
    }

    public int getParticleMaxLife() {
        return 0;
    }

    /**
     * Generates 4 particles on the target.
     *
     * @param generator entity that will generate the particles.
     * @param target    target where the particles will be generated.
     */
    protected void generateParticle(Entity generator, Entity target) {
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, -1)); // Top left
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, -1)); // Top right
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, 1)); // Down left
        world.particles.add(new Particle(game, world, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, 1)); // Down right
    }

    /**
     * Hits the entity if the attackbox in the attack frame collides with the target's hitbox.
     * <p>
     * From 0 to motion1 ms the first attack frame is shown. From motion1 to motion2 ms the second attack frame is shown.
     * After motion2 it returns to the motion frame. In the case of the player there is only one attack frame.
     * <p>
     * In the second attack frame, the x-y position is adjusted for the attackbox and checks if it collides with an
     * entity.
     */
    public void hit() {
        timer.attackAnimationCounter++;
        if (timer.attackAnimationCounter <= stats.motion1) sheet.attackNum = 1;
        if (timer.attackAnimationCounter > stats.motion1 && timer.attackAnimationCounter <= stats.motion2) {
            sheet.attackNum = 2;

            int currentX = pos.x, currentY = pos.y;
            int hitboxWidth = hitbox.width, hitboxHeight = hitbox.height;

            switch (direction) {
                case DOWN -> pos.y += attackbox.height;
                case UP -> pos.y -= attackbox.height;
                case LEFT -> pos.x -= attackbox.width;
                case RIGHT -> pos.x += attackbox.width;
            }

            hitbox.width = attackbox.width;
            hitbox.height = attackbox.height;

            hitPlayer(game.collision.checkPlayer(this), stats.attack);

            pos.x = currentX;
            pos.y = currentY;
            hitbox.width = hitboxWidth;
            hitbox.height = hitboxHeight;
        }
        if (timer.attackAnimationCounter > stats.motion2) {
            sheet.attackNum = 1;
            timer.attackAnimationCounter = 0;
            flags.hitting = false;
        }
    }

    /**
     * Drops an object.
     *
     * @param entity entity.
     * @param item   object that drops the entity.
     */
    protected void drop(Entity entity, Item item) {
        for (int i = 0; i < world.items[1].length; i++) {
            if (world.items[world.map][i] == null) {
                world.items[world.map][i] = item;
                world.items[world.map][i].pos.x = pos.x + (sheet.frame.getWidth() / 2 - item.sheet.frame.getWidth() / 2);
                // Add half of the hitbox of just the mobs to the position and the item
                world.items[world.map][i].pos.y = pos.y + (sheet.frame.getHeight() / 2 + (entity instanceof Mob ? hitbox.height / 2 : 0) - item.sheet.frame.getHeight() / 2);
                break;
            }
        }
    }

    /**
     * Hit the player.
     *
     * @param contact if the hostile mob makes contact with the player.
     * @param attack  attack points.
     */
    public void hitPlayer(boolean contact, int attack) {
        // If the entity is hostile and makes contact with the player who is not invincible
        if (type == Type.HOSTILE && contact && !world.player.flags.invincible) {
            game.playSound(sound_player_damage);
            // Subtract the player's defense from the mob's attack to calculate fair damage
            int damage = Math.max(attack - world.player.stats.defense, 1);
            world.player.stats.decreaseHp(damage);
            world.player.flags.invincible = true;
        }
    }

    /**
     * Check collisions.
     */
    public void checkCollisions() {
        flags.colliding = false;
        game.collision.checkTile(this);
        game.collision.checkItem(this);
        game.collision.checkEntity(this, world.mobs);
        game.collision.checkEntity(this, world.interactives);
        hitPlayer(game.collision.checkPlayer(this), stats.attack);
    }

    /**
     * Check if the entity is inside the camera.
     *
     * @return true if the entity is inside the camera or false.
     */
    public boolean isOnCamera() {
        /* If the render is with a larger entity (for example, Skeleton) than normal, it will not be represented when
         * the player displays only its feet, since the distance from the boss that starts from the upper left corner
         * (or the center?) is a lot with respect to the view of the player on the screen. Therefore, this vision is
         * increased by multiplying the bossArea. */
        int bossArea = 5;
        return pos.x + tile * bossArea > world.player.pos.x - world.player.screen.xOffset &&
                pos.x - tile < world.player.pos.x + world.player.screen.xOffset &&
                pos.y + tile * bossArea > world.player.pos.y - world.player.screen.yOffset &&
                pos.y - tile < world.player.pos.y + world.player.screen.yOffset;
    }

    public int getScreenX() {
        return pos.x - world.player.pos.x + world.player.screen.xOffset;
    }

    public int getScreenY() {
        return pos.y - world.player.pos.y + world.player.screen.yOffset;
    }

    /**
     * Draw the rectangles that represent the frames, hitbox and attackbox.
     */
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
