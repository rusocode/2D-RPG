package com.craivet.world.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.*;
import com.craivet.assets.Assets;
import com.craivet.assets.AudioAssets;
import com.craivet.assets.SpriteSheetAssets;
import com.craivet.classes.Character;
import com.craivet.classes.Jester;
import com.craivet.gfx.Animation;
import com.craivet.states.State;
import com.craivet.world.World;
import com.craivet.world.entity.mob.Mob;
import com.craivet.world.entity.mob.Lizard;
import com.craivet.world.entity.mob.RedSlime;
import com.craivet.world.entity.mob.Slime;
import com.craivet.world.entity.projectile.BurstOfFire;
import com.craivet.world.entity.projectile.Projectile;
import com.craivet.world.entity.interactive.Interactive;
import com.craivet.utils.*;
import com.craivet.world.entity.item.*;

import static com.craivet.utils.Global.*;

// TODO Shouldn't it become the Client class?
public class Player extends Mob {

    public Item weapon, shield, light;
    public PlayerInventory inventory;
    private Entity auxEntity; // Auxiliary variable to get the attributes of the current entity
    public boolean attackCanceled, lightUpdate;
    public Character character = Jester.getInstance();

    /* Variable to know when the player is inside the boss area to prevent the same event from occurring every time he goes
     * through that event. It is only deactivated again when you die in the boss area. */
    public boolean bossBattleOn;

    public Player(Game game, World world) {
        super(game, world);
        init();
    }

    @Override
    public void update() {
        // The order of the methods is very important
        if (flags.hitting) hit();
        if (game.keyboard.checkKeys()) {
            direction.get(this);
            checkCollisions();
            if (!flags.colliding && !game.keyboard.checkAccionKeys()) pos.update(this, direction);
            mechanics.checkDirectionSpeed(this, auxEntity);
            checkAttack();
            game.keyboard.resetAccionKeys();
            if (game.keyboard.checkMovementKeys()) {
                // TODO Move to method
                down.tick();
                up.tick();
                left.tick();
                right.tick();
            }
        } else // TODO This else does not work because the stopping of new frames is not timed
            timer.timeStopMovement(this, 20);

        checkShoot();
        timer.checkTimers(this);
        checkStats();
    }

    @Override
    public void render(Graphics2D g2) {
        if (flags.invincible) Utils.changeAlpha(g2, 0.3f);
        if (drawing) {
            game.ui.renderHpBar(this);
            game.ui.renderManaBar(this);
            if (!flags.hitting) g2.drawImage(getCurrentAnimationFrame(), screen.xOffset, screen.yOffset, null);
            else getCurrentItemFrame(g2);
        }
        if (game.keyboard.hitbox) drawRects(g2);

        Utils.changeAlpha(g2, 1);
    }

    private void init() {
        inventory = new PlayerInventory(game, world, this);
        dialogue = new Dialogue(game);

        type = Type.PLAYER;
        stats.init();

        projectile = new BurstOfFire(game, world);
        weapon = new SwordIron(game, world);
        shield = new ShieldWood(game, world);
        light = null;
        stats.attack = getAttack();
        stats.defense = getDefense();

        int scale = 1;
        sheet.loadPlayerMovementFrames(Assets.getSpriteSheet(SpriteSheetAssets.PLAYER), scale);
        sheet.loadWeaponFrames(Assets.getSpriteSheet(SpriteSheetAssets.SWORD_FRAME), 16, 16, scale);

        int additionalPixelsForY = 19;
        hitbox.width = sheet.frame.getWidth() / 2;
        hitbox.height = sheet.frame.getHeight() / 2 - 6;
        hitbox.x = hitbox.width - hitbox.width / 2;
        hitbox.y = hitbox.height - hitbox.height / 2 + additionalPixelsForY;
        hitbox = new Rectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        int animationSpeed = 90;
        down = new Animation(animationSpeed, sheet.down);
        up = new Animation(animationSpeed, sheet.up);
        left = new Animation(animationSpeed, sheet.left);
        right = new Animation(animationSpeed, sheet.right);

        inventory.init();

        pos.set(world, this, ABANDONED_ISLAND, OVERWORLD, 23, 20, Direction.DOWN);
    }

    public void hit() {
        timer.attackAnimationCounter++;
        if (timer.attackAnimationCounter <= stats.motion1) sheet.attackNum = 1; // (0-motion1ms attack frame 1)
        if (timer.attackAnimationCounter > stats.motion1 && timer.attackAnimationCounter <= stats.motion2) { // (from motion1-motion2ms attack frame 2)
            sheet.attackNum = 2;

            // Saves the current x-y position and hitbox size
            int currentX = pos.x, currentY = pos.y;
            int hitboxWidth = hitbox.width, hitboxHeight = hitbox.height;

            /* Adjusts the attackbox (on the sword blade to be more specific) of the player depending on the attack
             * direction. It is important to clarify that the x-y coordinates of the attackbox start from the upper left
             * corner of the player's hitbox (i don't know if it is necessary to start from that corner). */
            switch (direction) {
                // TODO Las coordenadas de cada hitbox deberian comenzar a partir de la imagen
                case DOWN -> {
                    attackbox.x = 0;
                    attackbox.y = 4;
                    attackbox.width = 4;
                    attackbox.height = 44;
                }
                case UP -> {
                    attackbox.x = 13;
                    attackbox.y = -43;
                    attackbox.width = 4;
                    attackbox.height = 42;
                }
                case LEFT -> {
                    attackbox.x = -20;
                    attackbox.y = 3;
                    attackbox.width = 19;
                    attackbox.height = 4;
                }
                case RIGHT -> {
                    attackbox.x = 13;
                    attackbox.y = 5;
                    attackbox.width = 18;
                    attackbox.height = 4;
                }
            }
            /* Accumulate the position of the attackbox to the position of the player to check the collision with the
             * adjusted coordinates of the attackbox. */
            pos.x += attackbox.x;
            pos.y += attackbox.y;

            // Convert the hitbox (width and height) into the attackbox to check the collision only with the attackbox
            hitbox.width = attackbox.width;
            hitbox.height = attackbox.height;

            // Check the collision with the mob using the position and size of the updated hitbox, that is, with the attackbox
            int mobIndex = game.collision.checkEntity(this, world.entities.mobs);
            world.entities.player.hitMob(mobIndex, this, weapon.stats.knockbackValue, stats.attack);

            int interactiveIndex = game.collision.checkEntity(this, world.entities.interactives);
            world.entities.player.hitInteractive(interactiveIndex);

            int projectileIndex = game.collision.checkEntity(this, world.entities.projectiles);
            world.entities.player.hitProjectile(projectileIndex);

            // After verifying the collision, reset the original data
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
     * Check if it can attack.
     */
    private void checkAttack() {
        if (game.keyboard.enter && !attackCanceled && timer.attackCounter == INTERVAL_WEAPON && !flags.shooting && weapon != null) {
            if (weapon.type == Type.SWORD) game.playSound(Assets.getAudio(AudioAssets.SWING_WEAPON));
            if (weapon.type != Type.SWORD) game.playSound(Assets.getAudio(AudioAssets.SWING_AXE));
            flags.hitting = true;
            timer.attackCounter = 0;
        }
        flags.shooting = false;
        attackCanceled = false; // So you can attack again after interacting with an npc or drinking water
    }

    /**
     * Check if it can shooting a projectile.
     */
    private void checkShoot() {
        if (game.keyboard.shoot && !projectile.flags.alive && timer.projectileCounter == INTERVAL_PROJECTILE && projectile.haveResource(this) && !flags.hitting) {
            flags.shooting = true;
            game.playSound(projectile.sound);
            projectile.set(pos.x, pos.y, direction, true, this);
            // Check vacancy to add the projectile
            for (int i = 0; i < world.entities.projectiles[1].length; i++) {
                if (world.entities.projectiles[world.map.num][i] == null) {
                    world.entities.projectiles[world.map.num][i] = projectile;
                    break;
                }
            }
            projectile.subtractResource(this);
            timer.projectileCounter = 0;
        }
    }

    private void checkStats() {
        if (!game.keyboard.test) if (stats.hp <= 0) die();
        if (stats.hp > stats.maxHp) stats.hp = stats.maxHp;
        if (stats.mana > stats.maxMana) stats.mana = stats.maxMana;
    }

    /**
     * Interact with the npc.
     *
     * @param i index of the npc.
     */
    private void interactNpc(int i) {
        if (i != -1) {
            auxEntity = world.entities.mobs[world.map.num][i];
            Mob mob = world.entities.mobs[world.map.num][i];
            if (game.keyboard.enter && mob.type == Type.NPC) {
                attackCanceled = true;
                mob.dialogue();
            } else mob.move(direction); // In case it's the box
        }
    }

    /**
     * Hurt the player if he collides with a hostile mob.
     *
     * @param i index of the mob.
     */
    private void hurt(int i) {
        if (i != -1) {
            auxEntity = world.entities.mobs[world.map.num][i];
            Mob mob = world.entities.mobs[world.map.num][i];
            if (!flags.invincible && !mob.flags.dead && mob.type == Type.HOSTILE) {
                game.playSound(Assets.getAudio(AudioAssets.PLAYER_DAMAGE));
                int damage = Math.max(mob.stats.attack - stats.defense, 1);
                stats.decreaseHp(damage);
                flags.invincible = true;
            }
        }
    }

    /**
     * Hit the mob.
     *
     * @param i              index of the mob.
     * @param attacker       attacker of the mob.
     * @param knockbackValue knockback value.
     * @param attack         attack type (sword or fireball).
     */
    public void hitMob(int i, Entity attacker, int knockbackValue, int attack) {
        if (i != -1) { // TODO I change it to >= 0 to avoid double negation and comparison -1?
            auxEntity = world.entities.mobs[world.map.num][i];
            Mob mob = world.entities.mobs[world.map.num][i];
            if (!mob.flags.invincible && mob.type != Type.NPC) {

                if (knockbackValue > 0) mechanics.setKnockback(mob, attacker, knockbackValue);

                int damage = Math.max(attack - mob.stats.defense, 1);
                mob.stats.decreaseHp(damage);
                game.ui.addMessageToConsole(damage + " damage!");
                if (mob.stats.hp > 0) {
                    game.playSound(mob.soundHit);
                    if (!(mob instanceof Slime)) game.playSound(Assets.getAudio(AudioAssets.MOB_HIT));
                }

                mob.flags.invincible = true;
                mob.flags.hpBar = true;
                mob.damageReaction();

                // TODO No deberia ganar exp en cada golpe al mob o solo cuando lo mata?
                // TODO Should there be a method?
                if (mob.stats.hp <= 0) {
                    game.playSound(Assets.getAudio(AudioAssets.MOB_DEATH));
                    if (!(mob instanceof Slime || mob instanceof RedSlime)) game.playSound(mob.soundDeath);
                    mob.flags.dead = true;
                    game.ui.addMessageToConsole("Killed the " + mob.stats.name + "!");
                    game.ui.addMessageToConsole("Exp + " + mob.stats.exp);
                    stats.exp += mob.stats.exp;
                    checkLevelUp();

                    // TODO Verificar de otra forma y en otro lugar
                    if (mob instanceof Lizard) {
                        for (int j = 0; j < world.entities.items[1].length; j++) {
                            if (world.entities.items[world.map.num][j] != null && world.entities.items[world.map.num][j].stats.name.equals(DoorIron.NAME)) {
                                world.entities.items[world.map.num][j] = null;
                                game.playSound(Assets.getAudio(AudioAssets.DOOR_IRON_OPENING));
                            }
                        }
                    }

                }
            }
        }
    }

    /**
     * Hit the interactive tile.
     *
     * @param i index of the interactive tile.
     */
    public void hitInteractive(int i) {
        if (i != -1) {
            auxEntity = world.entities.interactives[world.map.num][i];
            Interactive interactive = world.entities.interactives[world.map.num][i];
            if (interactive.destructible && interactive.isCorrectWeapon(weapon) && !interactive.flags.invincible) {
                interactive.playSound();
                interactive.stats.hp--;
                interactive.flags.invincible = true;

                generateParticle(interactive, interactive);

                if (interactive.stats.hp == 0) {
                    interactive.checkDrop();
                    world.entities.interactives[world.map.num][i] = interactive.replaceBy();
                }
            }
        }
    }

    public void hitProjectile(int i) {
        if (i != -1) {
            auxEntity = world.entities.projectiles[world.map.num][i];
            Projectile projectile = world.entities.projectiles[world.map.num][i];
            // Avoid damaging the projectile itself
            if (projectile != this.projectile) {
                game.playSound(Assets.getAudio(AudioAssets.PLAYER_DAMAGE));
                projectile.flags.alive = false;
                generateParticle(projectile, projectile);
            }
        }
    }

    /**
     * Check if the player has leveled up.
     */
    private void checkLevelUp() {
        if (stats.lvl == MAX_LVL) {
            stats.exp = 0;
            stats.nextLvlExp = 0;
            return;
        }
        /* Checks the exp with a while loop to verify if the player exceeded the amount of exp for the next level
         * several times (for example, killing a mob that gives a lot of exp). Therefore, raise the lvl as long as the
         * exp is greater than the exp of the next lvl. */
        while (stats.exp >= stats.nextLvlExp) {
            game.playSound(Assets.getAudio(AudioAssets.LEVEL_UP));
            character.upStats(this);
            stats.attack = getAttack();
            stats.defense = getDefense();
        }
    }

    /**
     * Pick up an item.
     *
     * @param i index of the item.
     */
    public void pickup(int i) {
        if (i != -1) {
            Item item = world.entities.items[world.map.num][i];
            if (game.keyboard.pickup && item.type != Type.OBSTACLE) {
                if (item.type == Type.PICKUP) item.use(world.entities.player);
                else if (inventory.canPickup(item)) game.playSound(Assets.getAudio(AudioAssets.ITEM_PICKUP));
                else {
                    game.ui.addMessageToConsole("You cannot carry any more!");
                    return;
                }
                world.entities.items[world.map.num][i] = null;
            }
            if (game.keyboard.enter && item.type == Type.OBSTACLE) {
                world.entities.player.attackCanceled = true;
                item.interact();
            }
        }
    }

    @Override
    public void checkCollisions() {
        flags.colliding = false;
        if (!game.keyboard.test) game.collision.checkTile(this);
        pickup(game.collision.checkItem(this));
        interactNpc(game.collision.checkEntity(this, world.entities.mobs));
        hurt(game.collision.checkEntity(this, world.entities.mobs));
        setCurrentInteractive(game.collision.checkEntity(this, world.entities.interactives));
        // game.collision.checkEntity(this, world.entities.interactives);
        game.event.check(this);
    }

    private void setCurrentInteractive(int i) {
        if (i != -1) auxEntity = world.entities.interactives[world.map.num][i];
    }

    private void die() {
        game.playSound(Assets.getAudio(AudioAssets.PLAYER_DEATH));
        State.setState(State.GAME_OVER);
        game.ui.command = -1;
        game.music.stop();
    }

    private void getCurrentItemFrame(Graphics2D g2) {
        switch (direction) {
            case DOWN -> {
                currentFrame = down.getFirstFrame();
                g2.drawImage(sheet.down[1], screen.xOffset, screen.yOffset, null);
                g2.drawImage(sheet.weapon[0], screen.xOffset, screen.yOffset + 34, null);
            }
            case UP -> {
                currentFrame = up.getFirstFrame();
                g2.drawImage(sheet.up[2], screen.xOffset, screen.yOffset, null);
                g2.drawImage(sheet.weapon[1], screen.xOffset + 13, screen.yOffset + 17, null);
            }
            case LEFT -> {
                currentFrame = left.getFirstFrame();
                g2.drawImage(sheet.left[2], screen.xOffset, screen.yOffset, null);
                g2.drawImage(sheet.weapon[2], screen.xOffset - 7, screen.yOffset + 26, null);
            }
            case RIGHT -> {
                currentFrame = right.getFirstFrame();
                g2.drawImage(sheet.right[4], screen.xOffset, screen.yOffset, null);
                g2.drawImage(sheet.weapon[3], screen.xOffset + 15, screen.yOffset + 28, null);
            }
        }
    }

    /**
     * Gets the current animation frame.
     *
     * @return the current animation frame.
     */
    private BufferedImage getCurrentAnimationFrame() {
        // When it stops moving, returns the first saved frame of the last direction to represent the stop of the player
        if (game.keyboard.checkMovementKeys()) {
            switch (direction) {
                case DOWN -> {
                    // Guarda el primer frame hacia abajo
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
        }
        return currentFrame;
    }

    public int getAttack() {
        return stats.strength * weapon.attackValue;
    }

    public int getDefense() {
        return stats.dexterity * shield.defenseValue;
    }

    public void initSleepImage(BufferedImage image) {
        currentFrame = image;
    }

    /**
     * Reset the Player.
     *
     * @param fullReset true to fully reset; false otherwise.
     */
    public void reset(boolean fullReset) {
        pos.set(world, this, ABANDONED_ISLAND, OVERWORLD, 23, 21, Direction.DOWN);
        stats.reset(fullReset);
        timer.reset();
        flags.reset();
        if (fullReset) inventory.init();
    }

    private void drawRects(Graphics2D g2) {
        g2.setStroke(new BasicStroke(0));
        // Frame
        g2.setColor(Color.magenta);
        g2.drawRect(screen.xOffset, screen.yOffset, currentFrame.getWidth(), currentFrame.getHeight());
        // Hitbox
        g2.setColor(Color.green);
        g2.drawRect(screen.xOffset + hitbox.x, screen.yOffset + hitbox.y, hitbox.width, hitbox.height);
        // Attackbox
        if (flags.hitting) {
            g2.setColor(Color.red);
            /* The position of the attackbox is added to the position of the player because after verifying the
             * detection of the hit in the hit method, the position of the player is reset, therefore it is added from
             * here so that the drawn rectangle coincides with the position specified in the hit method. */
            g2.drawRect(screen.xOffset + attackbox.x + hitbox.x, screen.yOffset + attackbox.y + hitbox.y, attackbox.width, attackbox.height);
        }
    }

}
