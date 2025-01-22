package com.punkipunk.entity.player;

import com.punkipunk.Dialogue;
import com.punkipunk.Direction;
import com.punkipunk.audio.AudioID;
import com.punkipunk.classes.Character;
import com.punkipunk.classes.Jester;
import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.combat.AttackSystem;
import com.punkipunk.entity.interactive.Interactive;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.item.ItemCategory;
import com.punkipunk.entity.item.ItemType;
import com.punkipunk.entity.mob.Mob;
import com.punkipunk.entity.mob.MobCategory;
import com.punkipunk.entity.mob.MobType;
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
 * TODO La interaccion con los NPC y los interactives deberian ser con el mouse, y no con ENTER
 */

public class Player extends Entity {

    private static final int DEFAULT_ANIMATION_SPEED = 90;
    private static final int HITBOX_Y_OFFSET = 19;
    private static final double HITBOX_WIDTH_RATIO = 0.5;  // 1/2
    private static final double HITBOX_HEIGHT_RATIO = 0.44; // Aproximadamente 1/2 - 6px
    private final AttackSystem attackSystem;
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
        attackSystem = new AttackSystem(game, this);
        initializeContainers(game, world);
        initializePlayerData();
        initializeSpells();
        initializeHitbox();
        initializeAnimations();
        setInitialPosition();
    }

    private void initializeContainers(Game game, World world) {
        inventory = new Inventory(game, world, this);
        hotbar = new Hotbar(game, world, this, inventory);
        dialogue = new Dialogue(game);
    }

    private void initializePlayerData() {
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
        stats.attack = getAttack();
        stats.defense = getDefense();

        sheet.loadPlayerMovementFrames(new SpriteSheet(Utils.loadTexture(playerData.spriteSheetPath())), playerData.frameScale());
    }

    private void initializeSpells() {
        spell = new BurstOfFire(game, world);
    }

    private void initializeHitbox() {
        double frameWidth = sheet.frame.getWidth();
        double frameHeight = sheet.frame.getHeight();

        hitbox = new Rectangle(
                frameWidth * HITBOX_WIDTH_RATIO - (frameWidth * HITBOX_WIDTH_RATIO / 2),  // x
                frameHeight * HITBOX_HEIGHT_RATIO - (frameHeight * HITBOX_HEIGHT_RATIO / 2) + HITBOX_Y_OFFSET,  // y
                frameWidth * HITBOX_WIDTH_RATIO,  // width
                frameHeight * HITBOX_HEIGHT_RATIO - 6  // height
        );

        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
    }

    private void initializeAnimations() {
        down = new Animation(DEFAULT_ANIMATION_SPEED, sheet.down);
        up = new Animation(DEFAULT_ANIMATION_SPEED, sheet.up);
        left = new Animation(DEFAULT_ANIMATION_SPEED, sheet.left);
        right = new Animation(DEFAULT_ANIMATION_SPEED, sheet.right);
    }

    private void setInitialPosition() {
        position.set(world, this, ABANDONED_ISLAND, OVERWORLD, 23, 20, Direction.DOWN);
    }

    @Override
    public void update() {
        // El orden de los metodos es importante
        attackSystem.update();
        if (game.gameSystem.keyboard.checkKeys()) {
            checkCollisions();
            // Si no esta colisionando y esta presionando las teclas de movimiento
            if (!flags.colliding && game.gameSystem.keyboard.checkMovementKeys()) position.update(this, direction);
            mechanics.checkSpeed(this, otherEntity);
            checkAttack();
            checkShoot();
            // Resetea las teclas de accion (atacar, por ejemplo) para darle prioridad a las teclas de movimiento y asi evitar que se "choquen"
            game.gameSystem.keyboard.resetActionKeys();
            if (game.gameSystem.keyboard.checkMovementKeys()) updateAnimation();
        }

        timer.checkTimers(this);
        checkStats();
    }

    @Override
    public void render(GraphicsContext g2) {
        if (flags.invincible) Utils.changeAlpha(g2, 0.3f);
        if (drawing) {
            if (!flags.hitting) g2.drawImage(getCurrentAnimationFrame(), X_OFFSET, Y_OFFSET);
            else getCurrentItemFrame(g2);
        }
        if (game.gameSystem.keyboard.isKeyToggled(Key.RECTS)) drawRects(g2);
        Utils.changeAlpha(g2, 1);
    }

    /**
     * Comprueba si puede atacar.
     */
    private void checkAttack() {
        if (canAttack()) attack();
        attackCanceled = false;
    }

    /**
     * Si se presiono ENTER, y el ataque no se cancelo, y se completo el intervalo de ataque, y el arma esta equipada.
     */
    private boolean canAttack() {
        return game.gameSystem.keyboard.isKeyPressed(Key.ENTER) && !attackCanceled && timer.isAttackReady() && inventory.getEquipment().isEquipped(weapon);
    }

    private void attack() {
        game.gameSystem.audio.playSound(weapon.soundSwing);
        flags.hitting = true; // Indica que esta atacando para poder generar la animacion de ataque
        timer.attackCounter = 0; // Una vez que ataca, resetea el contador de ataque
    }

    private void checkShoot() {
        if (canShoot()) shoot();
    }

    private boolean canShoot() {
        return game.gameSystem.keyboard.isKeyPressed(Key.SHOOT) && !spell.flags.alive && timer.isShootReady() && spell.haveResource(this);
    }

    private void shoot() {
        game.gameSystem.audio.playSound(spell.sound);
        spell.set(position.x, position.y, direction, true, this);
        world.entitySystem.getSpells(world.map.num).add(spell);
        spell.subtractResource(this);
        timer.projectileCounter = 0; // Una vez que lanza un hechizo, resetea el contador de proyectiles
    }

    private void checkStats() {
        if (!game.gameSystem.keyboard.isKeyToggled(Key.TEST)) {
            if (stats.hp <= 0) die();
        }
        if (stats.hp > stats.maxHp) stats.hp = stats.maxHp;
        if (stats.mana > stats.maxMana) stats.mana = stats.maxMana;
    }

    public void hitMob(Mob mob, Entity attacker, int knockback, int attack) {
        if (!mob.flags.invincible && mob.mobCategory != MobCategory.NPC) {
            // Aplica knockback si es necesario
            if (knockback > 0) mob.mechanics.setKnockback(mob, attacker, knockback);
            // Calcula y aplica daño
            int damage = Math.max(attack - mob.stats.defense, 1);
            mob.stats.decreaseHp(damage);
            game.gameSystem.ui.addMessageToConsole(damage + " damage!");
            if (mob.stats.hp > 0) {
                game.gameSystem.audio.playSound(AudioID.Sound.MOB_HIT); // Sonido de mob golpeado
                game.gameSystem.audio.playSound(mob.soundHit); // Sonido especifico de mob golpeado
            }
            mob.flags.invincible = true;
            mob.flags.hpBar = true;
            mob.damageReaction();
            if (mob.stats.hp <= 0) handleMobDeath(mob);
        }
    }

    private void handleMobDeath(Mob mob) {
        game.gameSystem.audio.playSound(AudioID.Sound.MOB_DEATH);
        game.gameSystem.audio.playSound(mob.soundDeath);
        mob.flags.dead = true;
        game.gameSystem.ui.addMessageToConsole("Killed the " + mob.stats.name + "!");
        game.gameSystem.ui.addMessageToConsole("Exp + " + mob.stats.exp);
        stats.exp += mob.stats.exp;
        checkLevelUp();
        if (mob.getType() == MobType.LIZARD) handleLizardDeath();
    }

    // Remueve IronDoor cuando el boss muere
    private void handleLizardDeath() {
        world.entitySystem.getItems(world.map.num).stream()
                .filter(item -> item.getType() == ItemType.IRON_DOOR)
                .findFirst()
                .ifPresent(door -> {
                    world.entitySystem.removeItem(world.map.num, door);
                    game.gameSystem.audio.playSound(AudioID.Sound.DOOR_IRON_OPENING);
                });
    }

    public void hitInteractive(Interactive interactive) {
        if (interactive.destructible && interactive.isCorrectWeapon(weapon) && !interactive.flags.invincible) {
            interactive.playSound();
            interactive.stats.hp--;
            interactive.flags.invincible = true;
            generateParticle(interactive, interactive);
            if (interactive.stats.hp == 0) {
                interactive.checkDrop();
                // ?
                // Reemplazar el interactive si es necesario
                if (interactive.replaceBy() != null)
                    world.entitySystem.replaceInteractive(world.map.num, interactive, interactive.replaceBy());
                else world.entitySystem.removeInteractive(world.map.num, interactive);
            }
        }
    }

    public void hitSpell(Spell spell) {
        if (spell != this.spell) { // Evita dañar el propio hechizo
            game.gameSystem.audio.playSound(AudioID.Sound.PLAYER_DAMAGE);
            spell.flags.alive = false;
            generateParticle(spell, spell);
        }
    }

    private void executeItemInteraction(Item item) {
        // FIXME Se genera un pequeño lag al apretar muchas veces la P mientras camino y no hay items
        if (game.gameSystem.keyboard.isKeyPressed(Key.PICKUP) && item.itemCategory != ItemCategory.OBSTACLE) {
            if (item.itemCategory == ItemCategory.PICKUP) item.use(this);
            else if (inventory.canAddItem(item) || hotbar.canAddItem(item)) {
                hotbar.add(item);
                game.gameSystem.audio.playSound(AudioID.Sound.ITEM_PICKUP);
            } else {
                game.gameSystem.ui.addMessageToConsole("You cannot carry any more!");
                return;
            }
            world.entitySystem.removeItem(world.map.num, item);
        } else if (game.gameSystem.keyboard.isKeyPressed(Key.ENTER) && item.itemCategory == ItemCategory.OBSTACLE) {
            attackCanceled = true;
            item.interact();
        }
    }

    private void executeMobInteraction(Mob mob) {
        otherEntity = mob;
        switch (mob.mobCategory) {
            case NPC -> {
                if (game.gameSystem.keyboard.isKeyPressed(Key.ENTER)) {
                    attackCanceled = true;
                    mob.dialogue();
                }
                if (mob.getType() == MobType.BOX) mob.move(this, direction);
            }
            case HOSTILE, NEUTRAL -> {
                if (!flags.invincible && !mob.flags.dead) {
                    game.gameSystem.audio.playSound(AudioID.Sound.PLAYER_DAMAGE);
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
        direction.get(this);
        if (!game.gameSystem.keyboard.isKeyToggled(Key.TEST)) game.gameSystem.collisionChecker.checkTile(this);
        game.gameSystem.collisionChecker.checkItem(this).ifPresent(this::executeItemInteraction);
        game.gameSystem.collisionChecker.checkMob(this).ifPresent(this::executeMobInteraction);
        game.gameSystem.collisionChecker.checkInteractive(this);
        game.gameSystem.event.check(this);
    }

    private void updateAnimation() {
        down.tick();
        up.tick();
        left.tick();
        right.tick();
    }

    private void die() {
        game.gameSystem.audio.playSound(AudioID.Sound.PLAYER_DEATH);
        State.setState(State.GAME_OVER);
        game.gameSystem.ui.command = -1;
        game.gameSystem.audio.stopAll();
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
            game.gameSystem.audio.playSound(AudioID.Sound.LEVEL_UP);
            character.upStats(this);
            stats.attack = getAttack();
            stats.defense = getDefense();
        }
    }

    private void getCurrentItemFrame(GraphicsContext g2) {
        switch (direction) {
            case DOWN -> {

                /* Cuando ataco, por ejemplo hacia arriba (UP) y en ese transcurso de ataque me muevo hacia abajo (DOWN), entonces
                 * necesito guardar el ultimo estado de la posicion de ataque (DOWN), para evitar que el player se mantenga en la
                 * posicion de ataque inicial (UP). */
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
                g2.drawImage(sheet.right[2], X_OFFSET, Y_OFFSET);
                g2.drawImage(sheet.weapon[3], X_OFFSET + 15, Y_OFFSET + 28);
            }
        }
    }

    private Image getCurrentAnimationFrame() {
        // Cuando deja de moverse, devuelve el primer frame guardado de la ultima direccion para representar la parada del player
        if (game.gameSystem.keyboard.checkMovementKeys()) {
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
