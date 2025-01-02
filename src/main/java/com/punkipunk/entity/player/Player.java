package com.punkipunk.entity.player;

import com.punkipunk.Dialogue;
import com.punkipunk.Direction;
import com.punkipunk.assets.Assets;
import com.punkipunk.audio.AudioID;
import com.punkipunk.assets.SpriteSheetAssets;
import com.punkipunk.classes.Character;
import com.punkipunk.classes.Jester;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.mob.MobType;
import com.punkipunk.entity.interactive.Interactive;
import com.punkipunk.entity.item.IronDoor;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.item.ItemType;
import com.punkipunk.entity.mob.Lizard;
import com.punkipunk.entity.mob.Mob;
import com.punkipunk.entity.mob.RedSlime;
import com.punkipunk.entity.mob.Slime;
import com.punkipunk.entity.projectile.BurstOfFire;
import com.punkipunk.entity.projectile.Projectile;
import com.punkipunk.gfx.Animation;
import com.punkipunk.gui.container.hotbar.Hotbar;
import com.punkipunk.gui.container.inventory.Inventory;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.states.State;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.*;

// TODO Shouldn't it become the Client class?
public class Player extends Mob {

    public Item weapon, shield, light;
    public Inventory inventory;
    public Hotbar hotbar;
    public boolean attackCanceled, lightUpdate;
    public Character character = Jester.getInstance();
    /* Variable to know when the player is inside the boss area to prevent the same event from occurring every time he goes
     * through that event. It is only deactivated again when you die in the boss area. */
    public boolean bossBattleOn;
    private Entity auxEntity; // Auxiliary variable to get the attributes of the current entity

    public Player(Game game, World world) {
        super(game, world);
        init();
    }

    @Override
    public void update() {
        // The order of the methods is very important
        if (flags.hitting) hit();
        if (game.system.keyboard.checkKeys()) {
            direction.get(this);
            checkCollisions();
            if (!flags.colliding && !game.system.keyboard.checkActionKeys()) pos.update(this, direction);
            mechanics.checkDirectionSpeed(this, auxEntity);
            checkAttack();
            checkShoot();
            // Resetea las teclas de accion (atacar, por ejemplo) para darle prioridad a las teclas de movimiento y asi evitar que se "choquen"
            game.system.keyboard.resetActionKeys();
            if (game.system.keyboard.checkMovementKeys()) {
                // TODO Move to method
                down.tick();
                up.tick();
                left.tick();
                right.tick();
            }
        } else timer.timeStopMovement(this, 20); // TODO This else does not work because the stopping of new frames is not timed

        timer.checkTimers(this);
        checkStats();
    }

    @Override
    public void render(GraphicsContext g2) {
        if (flags.invincible) Utils.changeAlpha(g2, 0.3f);
        if (drawing) {
            // game.systems.ui.renderHpBar(this);
            // game.systems.ui.renderManaBar(this);
            if (!flags.hitting) g2.drawImage(getCurrentAnimationFrame(), X_OFFSET, Y_OFFSET);
            else getCurrentItemFrame(g2);
        }
        if (game.system.keyboard.isKeyToggled(Key.RECTS)) drawRects(g2);

        Utils.changeAlpha(g2, 1);
    }

    private void init() {
        inventory = new Inventory(game, world, this);
        hotbar = new Hotbar(game, world, this, inventory);
        dialogue = new Dialogue(game);

        mobType = MobType.PLAYER;
        stats.init();

        projectile = new BurstOfFire(game, world);
        // TODO No hace falta null creo
        weapon = null;
        shield = null;
        light = null;
        stats.attack = getAttack();
        stats.defense = getDefense();

        int scale = 1;
        sheet.loadPlayerMovementFrames(Assets.getSpriteSheet(SpriteSheetAssets.PLAYER), scale);
        sheet.loadWeaponFrames(Assets.getSpriteSheet(SpriteSheetAssets.SWORD_FRAME), 16, 16, scale);

        int additionalPixelsForY = 19;
        hitbox.setWidth(sheet.frame.getWidth() / 2);
        hitbox.setHeight(sheet.frame.getHeight() / 2 - 6);
        hitbox.setX(hitbox.getWidth() - hitbox.getWidth() / 2);
        hitbox.setY(hitbox.getHeight() - hitbox.getHeight() / 2 + additionalPixelsForY);
        hitbox = new Rectangle(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();

        int animationSpeed = 90;
        down = new Animation(animationSpeed, sheet.down);
        up = new Animation(animationSpeed, sheet.up);
        left = new Animation(animationSpeed, sheet.left);
        right = new Animation(animationSpeed, sheet.right);

        pos.set(world, this, ABANDONED_ISLAND, OVERWORLD, 23, 20, Direction.DOWN);
    }

    public void hit() {
        timer.attackAnimationCounter++;
        if (timer.attackAnimationCounter <= stats.motion1) sheet.attackNum = 1; // (0-motion1ms attack frame 1)
        if (timer.attackAnimationCounter > stats.motion1 && timer.attackAnimationCounter <= stats.motion2) { // (from motion1-motion2ms attack frame 2)
            sheet.attackNum = 2;

            // Saves the current x-y position and hitbox size
            int currentX = pos.x, currentY = pos.y;
            int hitboxWidth = (int) hitbox.getWidth(), hitboxHeight = (int) hitbox.getHeight();

            /* Adjusts the attackbox (on the sword blade to be more specific) of the player depending on the attack
             * direction. It is important to clarify that the x-y coordinates of the attackbox start from the upper left
             * corner of the player's hitbox (i don't know if it is necessary to start from that corner). */
            switch (direction) {
                // TODO Las coordenadas de cada hitbox deberian comenzar a partir de la imagen
                case DOWN -> {
                    attackbox.setX(0);
                    attackbox.setY(4);
                    attackbox.setWidth(4);
                    attackbox.setHeight(44);
                }
                case UP -> {
                    attackbox.setX(13);
                    attackbox.setY(-43);
                    attackbox.setWidth(4);
                    attackbox.setHeight(42);
                }
                case LEFT -> {
                    attackbox.setX(-20);
                    attackbox.setY(3);
                    attackbox.setWidth(19);
                    attackbox.setHeight(4);
                }
                case RIGHT -> {
                    attackbox.setX(13);
                    attackbox.setY(5);
                    attackbox.setWidth(18);
                    attackbox.setHeight(4);
                }
            }
            /* Accumulate the position of the attackbox to the position of the player to check the collision with the
             * adjusted coordinates of the attackbox. */
            pos.x += (int) attackbox.getX();
            pos.y += (int) attackbox.getY();

            // Convert the hitbox (width and height) into the attackbox to check the collision only with the attackbox
            hitbox.setWidth(attackbox.getWidth());
            hitbox.setHeight(attackbox.getHeight());

            // Check the collision with the mob using the position and size of the updated hitbox, that is, with the attackbox
            int mobIndex = game.system.collisionChecker.checkEntity(this, world.entities.mobs);
            world.entities.player.hitMob(mobIndex, this, weapon.stats.knockbackValue, stats.attack);

            int interactiveIndex = game.system.collisionChecker.checkEntity(this, world.entities.interactives);
            world.entities.player.hitInteractive(interactiveIndex);

            int projectileIndex = game.system.collisionChecker.checkEntity(this, world.entities.projectiles);
            world.entities.player.hitProjectile(projectileIndex);

            // After verifying the collision, reset the original data
            pos.x = currentX;
            pos.y = currentY;
            hitbox.setWidth(hitboxWidth);
            hitbox.setHeight(hitboxHeight);
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
        if (game.system.keyboard.isKeyPressed(Key.ENTER) && !attackCanceled && timer.attackCounter == INTERVAL_WEAPON && !flags.shooting && weapon != null) {
            if (weapon.itemType == ItemType.SWORD) game.system.audio.playSound(AudioID.Sound.SWING_WEAPON);
            if (weapon.itemType != ItemType.SWORD) game.system.audio.playSound(AudioID.Sound.SWING_AXE);
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
        if (game.system.keyboard.isKeyPressed(Key.SHOOT) && !projectile.flags.alive && timer.projectileCounter == INTERVAL_PROJECTILE && projectile.haveResource(this) && !flags.hitting) {
            flags.shooting = true;
            game.system.audio.playSound(projectile.sound);
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
        if (!game.system.keyboard.isKeyToggled(Key.TEST)) if (stats.hp <= 0) die();
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
            if (game.system.keyboard.isKeyPressed(Key.ENTER) && mob.mobType == MobType.NPC) {
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
            if (!flags.invincible && !mob.flags.dead && mob.mobType == MobType.HOSTILE) {
                game.system.audio.playSound(AudioID.Sound.PLAYER_DAMAGE);
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
            if (!mob.flags.invincible && mob.mobType != MobType.NPC) {

                if (knockbackValue > 0) mechanics.setKnockback(mob, attacker, knockbackValue);

                int damage = Math.max(attack - mob.stats.defense, 1);
                mob.stats.decreaseHp(damage);
                game.system.ui.addMessageToConsole(damage + " damage!");
                if (mob.stats.hp > 0) {
                    game.system.audio.playSound(mob.soundHit);
                    if (!(mob instanceof Slime)) game.system.audio.playSound(AudioID.Sound.MOB_HIT);
                }

                mob.flags.invincible = true;
                mob.flags.hpBar = true;
                mob.damageReaction();

                // TODO No deberia ganar exp en cada golpe al mob o solo cuando lo mata?
                // TODO Should there be a method?
                if (mob.stats.hp <= 0) {
                    game.system.audio.playSound(AudioID.Sound.MOB_DEATH);
                    if (!(mob instanceof Slime || mob instanceof RedSlime)) game.system.audio.playSound(mob.soundDeath);
                    mob.flags.dead = true;
                    game.system.ui.addMessageToConsole("Killed the " + mob.stats.name + "!");
                    game.system.ui.addMessageToConsole("Exp + " + mob.stats.exp);
                    stats.exp += mob.stats.exp;
                    checkLevelUp();

                    // TODO Verificar de otra forma y en otro lugar
                    if (mob instanceof Lizard) {
                        for (int j = 0; j < world.entities.items[1].length; j++) {
                            if (world.entities.items[world.map.num][j] != null && world.entities.items[world.map.num][j].stats.name.equals(IronDoor.NAME)) {
                                world.entities.items[world.map.num][j] = null;
                                game.system.audio.playSound(AudioID.Sound.DOOR_IRON_OPENING);
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
                game.system.audio.playSound(AudioID.Sound.PLAYER_DAMAGE);
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
            stats.nextExp = 0;
            return;
        }
        /* Checks the exp with a while loop to verify if the player exceeded the amount of exp for the next level
         * several times (for example, killing a mob that gives a lot of exp). Therefore, raise the lvl as long as the
         * exp is greater than the exp of the next lvl. */
        while (stats.exp >= stats.nextExp) {
            game.system.audio.playSound(AudioID.Sound.LEVEL_UP);
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
            if (game.system.keyboard.isKeyPressed(Key.PICKUP) && item.itemType != ItemType.OBSTACLE) {
                if (item.itemType == ItemType.PICKUP) item.use(world.entities.player);
                    // Si puede agregar items en el inventario o en la hotbar, entonces agrega los items a la hotbar
                else if (inventory.canAddItem(item) || hotbar.canAddItem(item)) {
                    hotbar.add(item);
                    game.system.audio.playSound(AudioID.Sound.ITEM_PICKUP);
                } else {
                    game.system.ui.addMessageToConsole("You cannot carry any more!");
                    return;
                }
                world.entities.items[world.map.num][i] = null;
            }
            if (game.system.keyboard.isKeyPressed(Key.ENTER) && item.itemType == ItemType.OBSTACLE) {
                world.entities.player.attackCanceled = true;
                item.interact();
            }
        }
    }

    @Override
    public void checkCollisions() {
        flags.colliding = false;
        if (!game.system.keyboard.isKeyToggled(Key.TEST)) game.system.collisionChecker.checkTile(this);
        pickup(game.system.collisionChecker.checkItem(this)); // FIXME Se genera un pequeño lag al apretar muchas veces la P mientras camino y no hay items
        interactNpc(game.system.collisionChecker.checkEntity(this, world.entities.mobs));
        hurt(game.system.collisionChecker.checkEntity(this, world.entities.mobs));
        setCurrentInteractive(game.system.collisionChecker.checkEntity(this, world.entities.interactives));
        // game.systems.collision.checkEntity(this, world.entities.interactives);
        game.system.event.check(this);
    }

    private void setCurrentInteractive(int i) {
        if (i != -1) auxEntity = world.entities.interactives[world.map.num][i];
    }

    private void die() {
        game.system.audio.playSound(AudioID.Sound.PLAYER_DEATH);
        State.setState(State.GAME_OVER);
        game.system.ui.command = -1;
        game.system.audio.stopAll();
    }

    private void getCurrentItemFrame(GraphicsContext g2) {
        switch (direction) {
            case DOWN -> {
                currentFrame = down.getFirstFrame();
                g2.drawImage(sheet.down[1], X_OFFSET, Y_OFFSET);
                g2.drawImage(sheet.weapon[0], X_OFFSET, Y_OFFSET + 34);
            }
            case UP -> {
                currentFrame = up.getFirstFrame();
                g2.drawImage(sheet.up[2], X_OFFSET, Y_OFFSET);
                g2.drawImage(sheet.weapon[1], X_OFFSET + 13, Y_OFFSET + 17);
            }
            case LEFT -> {
                currentFrame = left.getFirstFrame();
                g2.drawImage(sheet.left[2], X_OFFSET, Y_OFFSET);
                g2.drawImage(sheet.weapon[2], X_OFFSET - 7, Y_OFFSET + 26);
            }
            case RIGHT -> {
                currentFrame = right.getFirstFrame();
                g2.drawImage(sheet.right[4], X_OFFSET, Y_OFFSET);
                g2.drawImage(sheet.weapon[3], X_OFFSET + 15, Y_OFFSET + 28);
            }
        }
    }

    /**
     * Gets the current animation frame.
     *
     * @return the current animation frame.
     */
    private Image getCurrentAnimationFrame() {
        // When it stops moving, returns the first saved frame of the last direction to represent the stop of the player
        if (game.system.keyboard.checkMovementKeys() && !State.isState(State.INVENTORY)) { // Evita que se cambie el frame de movimiento mientras esta en el inventario
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
        return stats.strength + (weapon != null ? weapon.attackValue : 0);
    }

    public int getDefense() {
        return stats.dexterity + (shield != null ? shield.defenseValue : 0);
    }

    public void initSleepImage(Image image) {
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
        if (fullReset) inventory.initializeDefaultItems();
    }

    private void drawRects(GraphicsContext gc) {
        // Frame
        gc.setStroke(Color.MAGENTA);
        gc.setLineWidth(1);
        gc.strokeRect(getScreenX(), getScreenY(), sheet.frame.getWidth(), sheet.frame.getHeight());

        // Hitbox
        gc.setStroke(Color.YELLOW);
        gc.strokeRect(getScreenX() + hitbox.getX(), getScreenY() + hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());

        // Attackbox
        if (flags.hitting) {
            gc.setStroke(Color.RED);
            /* The position of the attackbox is added to the position of the player because after verifying the
             * detection of the hit in the hit method, the position of the player is reset, therefore it is added from
             * here so that the drawn rectangle coincides with the position specified in the hit method. */
            gc.strokeRect(getScreenX() + attackbox.getX() + hitbox.getX(),
                    getScreenY() + attackbox.getY() + hitbox.getY(),
                    attackbox.getWidth(), attackbox.getHeight());
        }
    }

}
