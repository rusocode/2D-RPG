package com.punkipunk.entity.player;

import com.punkipunk.Dialogue;
import com.punkipunk.Direction;
import com.punkipunk.audio.AudioID;
import com.punkipunk.classes.Character;
import com.punkipunk.classes.Jester;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.combat.AttackSystem;
import com.punkipunk.entity.interactive.Interactive;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.item.ItemCategory;
import com.punkipunk.entity.item.ItemID;
import com.punkipunk.entity.mob.Mob;
import com.punkipunk.entity.mob.MobCategory;
import com.punkipunk.entity.mob.MobID;
import com.punkipunk.entity.spells.BurstOfFire;
import com.punkipunk.entity.spells.Spell;
import com.punkipunk.gfx.Animation;
import com.punkipunk.gfx.Renderer2D;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.gui.container.hotbar.Hotbar;
import com.punkipunk.gui.container.inventory.Inventory;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.PlayerData;
import com.punkipunk.states.State;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.MapID;
import com.punkipunk.world.World;
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

    public Player(IGame game, World world) {
        super(game, world);
        attackSystem = new AttackSystem(game, this);
        initializeContainers(game, world);
        initializePlayerData();
        initializeSpells();
        initializeHitbox();
        initializeAnimations();
        setInitialPosition();
    }

    @Override
    public void update() {
        // El orden de los metodos es importante
        attackSystem.update();
        if (game.getGameSystem().keyboard.checkKeys()) {
            checkCollisions();
            // Si no esta colisionando y esta presionando las teclas de movimiento
            if (!flags.colliding && game.getGameSystem().keyboard.checkMovementKeys()) position.update(this, direction);
            mechanics.checkSpeed(this, otherEntity);
            checkAttack();
            checkShoot();
            // Resetea las teclas de accion (atacar, por ejemplo) para darle prioridad a las teclas de movimiento y asi evitar que se "choquen"
            game.getGameSystem().keyboard.resetActionKeys();
            if (game.getGameSystem().keyboard.checkMovementKeys()) updateAnimation();
        }

        timer.checkTimers(this);
        checkStats();
    }

    @Override
    public void render(Renderer2D renderer) {
        if (flags.invincible) Utils.changeAlpha(renderer, 0.3f);
        if (drawing) {
            if (!flags.hitting) renderRegion(renderer, getCurrentAnimationFrame(), X_OFFSET, Y_OFFSET);
            else getCurrentItemFrame(renderer);
        }
        if (game.getGameSystem().keyboard.isKeyToggled(Key.RECTS)) drawRects(renderer);
        Utils.changeAlpha(renderer, 1);
    }

    public void hitMob(Mob mob, Entity attacker, int knockback, int attack) {
        if (!mob.flags.invincible && mob.mobCategory != MobCategory.NPC) {
            // Aplica knockback si es necesario
            if (knockback > 0) mob.mechanics.setKnockback(mob, attacker, knockback);
            // Calcula y aplica daño
            int damage = Math.max(attack - mob.stats.defense, 1);
            mob.stats.decreaseHp(damage);
            game.getGameSystem().ui.addMessageToConsole(damage + " damage!");
            if (mob.stats.hp >= 0) {
                game.getGameSystem().audio.playSound(AudioID.Sound.MOB_HIT); // Sonido de mob golpeado
                game.getGameSystem().audio.playSound(mob.soundHit); // Sonido especifico de mob golpeado
            }
            mob.flags.invincible = true;
            mob.flags.hpBar = true;
            mob.damageReaction();
            if (mob.stats.hp <= 0) handleMobDeath(mob);
        }
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
                    world.entitySystem.replaceInteractive(world.map.id, interactive, interactive.replaceBy());
                else world.entitySystem.removeInteractive(world.map.id, interactive);
            }
        }
    }

    public void hitSpell(Spell spell) {
        if (spell != this.spell) { // Evita dañar el propio hechizo
            game.getGameSystem().audio.playSound(AudioID.Sound.PLAYER_DAMAGE);
            spell.flags.alive = false;
            generateParticle(spell, spell);
        }
    }

    @Override
    public void checkCollisions() {
        flags.colliding = false;
        direction.get(this);
        if (!game.getGameSystem().keyboard.isKeyToggled(Key.TEST)) game.getGameSystem().collisionChecker.checkTile(this);
        game.getGameSystem().collisionChecker.checkItem(this).ifPresent(this::executeItemInteraction);
        game.getGameSystem().collisionChecker.checkMob(this).ifPresent(this::executeMobInteraction);
        game.getGameSystem().collisionChecker.checkInteractive(this);
        game.getGameSystem().eventSystem.checkAndTriggerEvents(this);
    }

    public int getAttack() {
        return stats.strength + (weapon != null ? weapon.attack : 0);
    }

    public int getDefense() {
        return stats.dexterity + (shield != null ? shield.defense : 0);
    }

    public void initSleepImage(SpriteSheet.SpriteRegion spriteRegion) {
        currentFrame = spriteRegion;
    }

    public void reset(boolean fullReset) {
        position.set(world, this, MapID.ABANDONED_ISLAND, 23, 21, Direction.DOWN);
        stats.reset(fullReset);
        timer.reset();
        flags.reset();
        if (fullReset) inventory.initializeDefaultItems();
    }

    private void initializeContainers(IGame game, World world) {
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

        sheet.loadPlayerMovementFrames(playerData.spriteSheetPath(), playerData.frameScale());
    }

    private void initializeSpells() {
        spell = new BurstOfFire(game, world);
    }

    private void initializeHitbox() {
        double frameWidth = currentFrame.width;
        double frameHeight = currentFrame.height;

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
        position.set(world, this, MapID.ABANDONED_ISLAND, 23, 20, Direction.DOWN);
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
        return game.getGameSystem().keyboard.isKeyPressed(Key.ENTER) && !attackCanceled && timer.isAttackReady() && inventory.getEquipment().isEquipped(weapon);
    }

    private void attack() {
        game.getGameSystem().audio.playSound(weapon.soundSwing);
        flags.hitting = true; // Indica que esta atacando para poder generar la animacion de ataque
        timer.attackCounter = 0; // Una vez que ataca, resetea el contador de ataque
    }

    private void checkShoot() {
        if (canShoot()) shoot();
    }

    private boolean canShoot() {
        return game.getGameSystem().keyboard.isKeyPressed(Key.SHOOT) && !spell.flags.alive && timer.isShootReady() && spell.haveResource(this);
    }

    private void shoot() {
        game.getGameSystem().audio.playSound(spell.sound);
        spell.set(position.x, position.y, direction, true, this);
        world.entitySystem.getSpells(world.map.id).add(spell);
        spell.subtractResource(this);
        timer.projectileCounter = 0; // Una vez que lanza un hechizo, resetea el contador de proyectiles
    }

    private void checkStats() {
        if (!game.getGameSystem().keyboard.isKeyToggled(Key.TEST)) {
            if (stats.hp <= 0) die();
        }
        if (stats.hp > stats.maxHp) stats.hp = stats.maxHp;
        if (stats.mana > stats.maxMana) stats.mana = stats.maxMana;
    }

    private void handleMobDeath(Mob mob) {
        game.getGameSystem().audio.playSound(mob.soundDeath);
        mob.flags.dead = true;
        game.getGameSystem().ui.addMessageToConsole("Killed the " + mob.stats.name + "!");
        game.getGameSystem().ui.addMessageToConsole("Exp + " + mob.stats.exp);
        stats.exp += mob.stats.exp;
        checkLevelUp();
        if (mob.getID() == MobID.LIZARD) handleLizardDeath();
    }

    // Remueve IronDoor cuando el boss muere
    private void handleLizardDeath() {
        world.entitySystem.getItems(world.map.id).stream()
                .filter(item -> item.getID() == ItemID.IRON_DOOR)
                .findFirst()
                .ifPresent(door -> {
                    world.entitySystem.removeItem(world.map.id, door);
                    game.getGameSystem().audio.playSound(AudioID.Sound.DOOR_IRON_OPENING);
                });
    }

    private void executeItemInteraction(Item item) {
        // FIXME Se genera un pequeño lag al apretar muchas veces la P mientras camino y no hay items
        if (game.getGameSystem().keyboard.isKeyPressed(Key.PICKUP) && item.itemCategory != ItemCategory.OBSTACLE) {
            if (item.itemCategory == ItemCategory.PICKUP) item.use(this);
            else if (inventory.canAddItem(item) || hotbar.canAddItem(item)) {
                hotbar.add(item);
                game.getGameSystem().audio.playSound(AudioID.Sound.ITEM_PICKUP);
            } else {
                game.getGameSystem().ui.addMessageToConsole("You cannot carry any more!");
                return;
            }
            world.entitySystem.removeItem(world.map.id, item);
        } else if (game.getGameSystem().keyboard.isKeyPressed(Key.ENTER) && item.itemCategory == ItemCategory.OBSTACLE) {
            attackCanceled = true;
            item.interact();
        }
    }

    private void executeMobInteraction(Mob mob) {
        otherEntity = mob;
        switch (mob.mobCategory) {
            case NPC -> {
                if (game.getGameSystem().keyboard.isKeyPressed(Key.ENTER)) {
                    attackCanceled = true;
                    mob.dialogue();
                }
                if (mob.getID() == MobID.BOX) mob.move(this, direction);
            }
            case HOSTILE, NEUTRAL -> {
                if (!flags.invincible && !mob.flags.dead) {
                    game.getGameSystem().audio.playSound(AudioID.Sound.PLAYER_DAMAGE);
                    int damage = Math.max(mob.stats.attack - stats.defense, 1);
                    stats.decreaseHp(damage);
                    flags.invincible = true;
                }
            }
        }
    }

    // TODO Tendria que sincronizar animationIndex o llamar al metodo padre en este caso?
    /* private void updateAnimation() {
        down.tick();
        up.tick();
        left.tick();
        right.tick();
    } */

    private void die() {
        game.getGameSystem().audio.playSound(AudioID.Sound.PLAYER_DEATH);
        State.setState(State.GAME_OVER);
        game.getGameSystem().ui.command = -1;
        game.getGameSystem().audio.stopPlayback();
    }

    private void checkLevelUp() {
        if (stats.lvl == MAX_LVL) {
            stats.exp = 0;
            stats.nextExp = 0;
            return;
        }
        /* Comprueba la exp con un bucle while para verificar si el jugador supero la cantidad de exp para el siguiente nivel
         * varias veces (por ejemplo, matando a un mob que otorga mucha exp). Por lo tanto, aumenta el nivel siempre que la
         * exp sea mayor que la exp del siguiente nivel. */
        while (stats.exp >= stats.nextExp) {
            game.getGameSystem().audio.playSound(AudioID.Sound.LEVEL_UP);
            character.upStats(this);
            stats.attack = getAttack();
            stats.defense = getDefense();
        }
    }

    private void getCurrentItemFrame(Renderer2D renderer) {
        switch (direction) {
            case DOWN -> {

                /* Cuando ataco, por ejemplo hacia arriba (UP) y en ese transcurso de ataque me muevo hacia abajo (DOWN), entonces
                 * necesito guardar el ultimo estado de la posicion de ataque (DOWN), para evitar que el player se mantenga en la
                 * posicion de ataque inicial (UP). */
                currentFrame = down.getFirstFrame();

                renderRegion(renderer, sheet.down[1], X_OFFSET, Y_OFFSET);
                renderRegion(renderer, sheet.weapon[0], X_OFFSET, Y_OFFSET + 34);

            }
            case UP -> {
                currentFrame = up.getFirstFrame();
                renderRegion(renderer, sheet.up[2], X_OFFSET, Y_OFFSET);
                renderRegion(renderer, sheet.weapon[1], X_OFFSET + 13, Y_OFFSET + 17);
            }
            case LEFT -> {
                currentFrame = left.getFirstFrame();
                renderRegion(renderer, sheet.left[2], X_OFFSET, Y_OFFSET);
                renderRegion(renderer, sheet.weapon[2], X_OFFSET - 7, Y_OFFSET + 26);
            }
            case RIGHT -> {
                currentFrame = right.getFirstFrame();
                renderRegion(renderer, sheet.right[4], X_OFFSET, Y_OFFSET);
                renderRegion(renderer, sheet.weapon[3], X_OFFSET + 15, Y_OFFSET + 28);
            }
        }
    }

    private SpriteSheet.SpriteRegion getCurrentAnimationFrame() {
        // Cuando deja de moverse, devuelve el primer frame guardado de la ultima direccion para representar la parada del player
        if (game.getGameSystem().keyboard.checkMovementKeys()) {
            switch (direction) {
                case DOWN -> {
                    // Guarda el primer frame hacia abajo
                    currentFrame = down.getFirstFrame();
                    /* Comprueba si el player esta colisionando con el mob para evitar un bug visual que se genera cuando el
                     * player esta siguiendo al mob justo por detras. Entonces, para este caso, al estar en movimiento y al mismo
                     * tiempo colisionando con el mob, devuelve el frame actual para esa direccion y asi no alterna entre devolver
                     * el primer frame y el frame actual. */
                    if (flags.collidingOnMob)
                        return down.getCurrentFrame(); // TODO O deberia devolver sheet.down[player.animationIndex];?
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

    private void drawRects(Renderer2D renderer) {
        // Frame
        renderer.setStroke(Color.MAGENTA);
        renderer.setLineWidth(1);
        renderer.strokeRect(getScreenX(), getScreenY(), currentFrame.width, currentFrame.height);
        // Hitbox
        renderer.setStroke(Color.YELLOW);
        renderer.strokeRect(getScreenX() + hitbox.getX(), getScreenY() + hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
        // Attackbox
        if (flags.hitting) {
            renderer.setStroke(Color.RED);
            /* La posicion del cuadro de ataque se suma a la posicion del player porque despues de verificar la deteccion de
             * ataque, la posicion del player se reinicia, por lo tanto, se calcula desde aqui para que el rectangulo dibujado
             * coincida con la posicion especificada de ataque. */
            renderer.strokeRect(getScreenX() + attackbox.getX() + hitbox.getX(),
                    getScreenY() + attackbox.getY() + hitbox.getY(),
                    attackbox.getWidth(), attackbox.getHeight());
        }
    }

}
