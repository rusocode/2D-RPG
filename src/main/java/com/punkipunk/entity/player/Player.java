package com.punkipunk.entity.player;

import com.punkipunk.Dialogue;
import com.punkipunk.Direction;
import com.punkipunk.audio.AudioID;
import com.punkipunk.classes.Character;
import com.punkipunk.classes.Jester;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.interactive.Interactive;
import com.punkipunk.entity.item.IronDoor;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.item.ItemCategory;
import com.punkipunk.entity.mob.*;
import com.punkipunk.entity.spells.BurstOfFire;
import com.punkipunk.entity.spells.Spell;
import com.punkipunk.gfx.Animation;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.gui.container.hotbar.Hotbar;
import com.punkipunk.gui.container.inventory.Inventory;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.PlayerData;
import com.punkipunk.states.State;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.*;

/**
 * TODO No deberia ser la clase Client?
 */

public class Player extends Entity {

    public Item weapon, shield, light;
    public Inventory inventory;
    public Hotbar hotbar;
    public boolean attackCanceled, lightUpdate;
    public Character character = Jester.getInstance();

    /* Variable para saber cuando el player esta dentro del area del boss para evitar que ocurra el mismo evento cada vez que pase
     * por ese evento. Solo se vuelve a desactivar cuando mueres en el area del boss. */
    public boolean bossBattleOn;

    /** Registra la otra entidad para poder comprobar la velocidad del player con respecto a la entidad a la que esta siguiendo. */
    private Entity otherEntity;

    public Player(Game game, World world) {
        super(game, world);

        inventory = new Inventory(game, world, this);
        hotbar = new Hotbar(game, world, this, inventory);
        dialogue = new Dialogue(game);

        PlayerData playerData = JsonLoader.getInstance().deserialize("player", PlayerData.class);

        stats.lvl = playerData.lvl();
        stats.exp = playerData.exp();
        stats.nextExp = playerData.nextExp();
        stats.speed = stats.baseSpeed = playerData.speed();
        stats.hp = stats.maxHp = playerData.hp();
        stats.mana = stats.maxMana = playerData.mana();
        stats.ammo = playerData.ammo();
        stats.gold = playerData.gold();
        stats.strength = playerData.strength();
        stats.dexterity = playerData.dexterity();
        stats.motion1 = playerData.motion1();
        stats.motion2 = playerData.motion2();

        spell = new BurstOfFire(game, world);

        stats.attack = getAttack();
        stats.defense = getDefense();

        sheet.loadPlayerMovementFrames(new SpriteSheet(Utils.loadTexture(playerData.spriteSheetPath())), playerData.frameScale());

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

        position.set(world, this, ABANDONED_ISLAND, OVERWORLD, 23, 20, Direction.DOWN);

    }

    @Override
    public void update() {
        // El orden de los metodos es muy importante
        if (flags.hitting) hit();
        if (game.system.keyboard.checkKeys()) {
            direction.get(this);
            checkCollisions();
            if (!flags.colliding && !game.system.keyboard.checkActionKeys()) position.update(this, direction);
            mechanics.checkSpeed(this, otherEntity);
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

    private void checkAttack() {
        if (game.system.keyboard.isKeyPressed(Key.ENTER) && !attackCanceled && timer.attackCounter == INTERVAL_WEAPON && !flags.shooting && weapon != null) {
            if (weapon.itemCategory == ItemCategory.SWORD) game.system.audio.playSound(AudioID.Sound.SWING_WEAPON);
            if (weapon.itemCategory != ItemCategory.SWORD) game.system.audio.playSound(AudioID.Sound.SWING_AXE);
            flags.hitting = true;
            timer.attackCounter = 0;
        }
        flags.shooting = false;
        attackCanceled = false; // Para poder atacar de nuevo despues de interactuar con un NPC o beber agua
    }

    private void checkShoot() {
        if (game.system.keyboard.isKeyPressed(Key.SHOOT) &&
                !spell.flags.alive &&
                timer.projectileCounter == INTERVAL_PROJECTILE &&
                spell.haveResource(this) &&
                !flags.hitting) {
            flags.shooting = true;
            game.system.audio.playSound(spell.sound);
            spell.set(position.x, position.y, direction, true, this);
            world.entities.getSpells(world.map.num).add(spell);
            spell.subtractResource(this);
            timer.projectileCounter = 0;
        }
    }

    private void checkStats() {
        if (!game.system.keyboard.isKeyToggled(Key.TEST)) if (stats.hp <= 0) die();
        if (stats.hp > stats.maxHp) stats.hp = stats.maxHp;
        if (stats.mana > stats.maxMana) stats.mana = stats.maxMana;
    }

    public void hit() {
        timer.attackAnimationCounter++;
        if (timer.attackAnimationCounter <= stats.motion1) sheet.attackNum = 1; // De 0 a motion1 frame de ataque 1
        if (timer.attackAnimationCounter > stats.motion1 && timer.attackAnimationCounter <= stats.motion2) { // Entre motion1 y motion2 frame de ataque 2
            sheet.attackNum = 2;

            // Guarda la posicion x-y actual y el tamaño del hitbox
            int currentX = position.x, currentY = position.y;
            int hitboxWidth = (int) hitbox.getWidth(), hitboxHeight = (int) hitbox.getHeight();

            /* Ajusta el frame de ataque (en la hoja de la espada para ser mas especifico) del player dependiendo de la direccion.
             * Es importante aclarar que las coordenadas x-y del frame de ataque comienzan desde la esquina superior izquierda del
             * hitbox del player (no se si es necesario comenzar desde esa esquina). */
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

            /* Acumula la posicion del frame de ataque con la posicion del jugador para comprobar la colision con las coordenadas
             * ajustadas del frame de ataque. */
            position.x += (int) attackbox.getX();
            position.y += (int) attackbox.getY();

            // Convierte el hitbox (ancho y alto) en el attackbox para comprobar la colision solo con el attackbox
            hitbox.setWidth(attackbox.getWidth());
            hitbox.setHeight(attackbox.getHeight());

            // Comprueba la colision con las entidades usando la posicion y el tamaño del hitbox actualizado, es decir, con el attackbox
            game.system.collisionChecker.checkMob(this).ifPresent(mob -> hitMob(mob, this, weapon.stats.knockback, stats.attack));
            // Si hay un valor presente (usando Optional), realiza la accion indicada con el valor presente
            game.system.collisionChecker.checkInteractive(this).ifPresent(this::hitInteractive); // Referencia a metodo como una forma mas concisa de escribir interactive -> hitInteractive(interactive)
            game.system.collisionChecker.checkSpell(this).ifPresent(this::hitSpell);

            // Despues de verificar la colision restablece los datos originales
            position.x = currentX;
            position.y = currentY;
            hitbox.setWidth(hitboxWidth);
            hitbox.setHeight(hitboxHeight);
        }
        if (timer.attackAnimationCounter > stats.motion2) {
            sheet.attackNum = 1;
            timer.attackAnimationCounter = 0;
            flags.hitting = false;
        }
    }

    public void hitMob(Mob mob, Entity attacker, int knockback, int attack) {
        if (!mob.flags.invincible && mob.mobCategory != MobCategory.NPC) {
            // Aplica knockback si es necesario
            if (knockback > 0) mob.mechanics.setKnockback(mob, attacker, knockback);
            // Calcula y aplica daño
            int damage = Math.max(attack - mob.stats.defense, 1);
            mob.stats.decreaseHp(damage);
            game.system.ui.addMessageToConsole(damage + " damage!");
            if (mob.stats.hp > 0) {
                game.system.audio.playSound(mob.soundHit);
                if (!(mob instanceof Slime || mob instanceof RedSlime)) game.system.audio.playSound(AudioID.Sound.MOB_HIT);
            }
            mob.flags.invincible = true;
            mob.flags.hpBar = true;
            mob.damageReaction();
            if (mob.stats.hp <= 0) handleMobDeath(mob);
        }
    }

    private void handleMobDeath(Mob mob) {
        game.system.audio.playSound(AudioID.Sound.MOB_DEATH);
        if (!(mob instanceof Slime || mob instanceof RedSlime)) game.system.audio.playSound(mob.soundDeath);
        mob.flags.dead = true;
        game.system.ui.addMessageToConsole("Killed the " + mob.stats.name + "!");
        game.system.ui.addMessageToConsole("Exp + " + mob.stats.exp);
        stats.exp += mob.stats.exp;
        checkLevelUp();
        if (mob instanceof Lizard) handleLizardDeath();
    }

    // Remueve IronDoor cuando el boss muere
    private void handleLizardDeath() {
        world.entities.getItems(world.map.num).stream()
                .filter(item -> item instanceof IronDoor)
                .findFirst()
                .ifPresent(door -> {
                    world.entities.removeItem(world.map.num, door);
                    game.system.audio.playSound(AudioID.Sound.DOOR_IRON_OPENING);
                });
    }

    private void hitInteractive(Interactive interactive) {
        if (interactive.interactiveData.destructible() && interactive.isCorrectWeapon(weapon) && !interactive.flags.invincible) {
            interactive.playSound();
            interactive.stats.hp--;
            interactive.flags.invincible = true;
            generateParticle(interactive, interactive);
            if (interactive.stats.hp == 0) {
                interactive.checkDrop();
                // ?
                // Reemplazar el interactive si es necesario
                if (interactive.replaceBy() != null)
                    world.entities.replaceInteractive(world.map.num, interactive, interactive.replaceBy());
                else world.entities.removeInteractive(world.map.num, interactive);
            }
        }
    }

    private void hitSpell(Spell spell) {
        if (spell != this.spell) { // Evita dañar el propio hechizo
            game.system.audio.playSound(AudioID.Sound.PLAYER_DAMAGE);
            spell.flags.alive = false;
            generateParticle(spell, spell);
        }
    }

    private void executeItemInteraction(Item item) {
        // FIXME Se genera un pequeño lag al apretar muchas veces la P mientras camino y no hay items
        if (game.system.keyboard.isKeyPressed(Key.PICKUP) && item.itemCategory != ItemCategory.OBSTACLE) {
            if (item.itemCategory == ItemCategory.PICKUP) item.use(this);
            else if (inventory.canAddItem(item) || hotbar.canAddItem(item)) {
                hotbar.add(item);
                game.system.audio.playSound(AudioID.Sound.ITEM_PICKUP);
            } else {
                game.system.ui.addMessageToConsole("You cannot carry any more!");
                return;
            }
            world.entities.removeItem(world.map.num, item);
        } else if (game.system.keyboard.isKeyPressed(Key.ENTER) && item.itemCategory == ItemCategory.OBSTACLE) {
            attackCanceled = true;
            item.interact();
        }
    }

    private void executeMobInteraction(Mob mob) {
        otherEntity = mob;
        switch (mob.mobCategory) {
            case NPC -> {
                if (game.system.keyboard.isKeyPressed(Key.ENTER)) {
                    attackCanceled = true;
                    mob.dialogue();
                }
                if (mob instanceof Box) mob.move(this, direction);
            }
            case HOSTILE, NEUTRAL -> {
                if (!flags.invincible && !mob.flags.dead) {
                    game.system.audio.playSound(AudioID.Sound.PLAYER_DAMAGE);
                    int damage = Math.max(mob.stats.attack - stats.defense, 1);
                    stats.decreaseHp(damage);
                    flags.invincible = true;
                }
            }
        }
    }

    @Override
    public void checkCollisions() {
        flags.colliding = false;
        if (!game.system.keyboard.isKeyToggled(Key.TEST)) game.system.collisionChecker.checkTile(this);
        game.system.collisionChecker.checkItem(this).ifPresent(this::executeItemInteraction);
        game.system.collisionChecker.checkMob(this).ifPresent(this::executeMobInteraction);
        game.system.collisionChecker.checkInteractive(this);
        game.system.event.check(this);
    }

    private void die() {
        game.system.audio.playSound(AudioID.Sound.PLAYER_DEATH);
        State.setState(State.GAME_OVER);
        game.system.ui.command = -1;
        game.system.audio.stopAll();
    }

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

    private Image getCurrentAnimationFrame() {
        // Cuando deja de moverse, devuelve el primer frame guardado de la ultima direccion para representar la parada del player
        if (game.system.keyboard.checkMovementKeys()) {
            switch (direction) {
                case DOWN -> {
                    // Guarda el primer frame hacia abajo
                    currentFrame = down.getFirstFrame();
                    /* Comprueba si el player esta colisionando con el mob para evitar un bug visual que se genera cuando el
                     * player esta siguiendo al mob justo por detras. Entonces, para este caso, al estar en movimiento y al mismo
                     * tiempo colisionando con el mob, devuelve el frame actual para esa direccion y asi no alterna entre devolver
                     * el primer frame y el frame actual. */
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
        return stats.strength + (weapon != null ? weapon.attack : 0);
    }

    public int getDefense() {
        return stats.dexterity + (shield != null ? shield.defense : 0);
    }

    public void initSleepImage(Image image) {
        currentFrame = image;
    }

    public void reset(boolean fullReset) {
        position.set(world, this, ABANDONED_ISLAND, OVERWORLD, 23, 21, Direction.DOWN);
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
