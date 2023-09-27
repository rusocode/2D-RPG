package com.craivet.world.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.*;
import com.craivet.gfx.Animation;
import com.craivet.world.World;
import com.craivet.world.entity.mob.Mob;
import com.craivet.world.entity.mob.Slime;
import com.craivet.world.entity.projectile.Fireball;
import com.craivet.world.entity.projectile.Projectile;
import com.craivet.world.tile.Interactive;
import com.craivet.utils.*;
import com.craivet.world.entity.item.*;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

// TODO No tendria que crear el objeto UI desde aca?
// TODO No tendria que convertirse en la fomasa clase Client?
public class Player extends Mob {

    public PlayerInventory inventory;
    private Entity auxEntity; // Variable auxiliar para obtener los atributos de la entidad actual
    public boolean attackCanceled, lightUpdate;

    public Player(Game game, World world) {
        super(game, world);
        inventory = new PlayerInventory(game, world, this);
        dialogue = new Dialogue();
        setDefaultValues();
        pos.set(world, this, NASHE, OUTSIDE, 23, 21, Direction.DOWN);
    }

    /**
     * Es muy importante el orden de los metodos.
     */
    @Override
    public void update() {
        if (flags.hitting) hit();
        if (game.keyboard.checkKeys()) {
            direction.get(this);
            checkCollision();
            if (!flags.colliding && !game.keyboard.checkAccionKeys()) pos.update(this, direction);
            mechanics.checkDirectionSpeed(this, auxEntity);
            checkAttack();
            game.keyboard.resetAccionKeys();
            if (game.keyboard.checkMovementKeys()) {
                down.tick();
                up.tick();
                left.tick();
                right.tick();
            }
        } else // TODO Este else no funciona porque no se temporiza la detencion de los nuevos frames
            timer.timeStopMovement(this, 20);

        checkShoot();
        timer.checkTimers(this);
        checkStats();
    }

    @Override
    public void render(Graphics2D g2) {
        if (flags.invincible) Utils.changeAlpha(g2, 0.3f);
        if (!flags.hitting) g2.drawImage(getCurrentAnimationFrame(), screen.xOffset, screen.yOffset, null);
        else getCurrentItemFrame(g2);
        drawRects(g2);
        Utils.changeAlpha(g2, 1);
    }

    private void setDefaultValues() {
        type = Type.PLAYER;
        stats.speed = stats.defaultSpeed = 2;
        stats.hp = stats.maxHp = 6;
        stats.mana = stats.maxMana = 4;
        stats.ammo = 5;
        stats.lvl = 1;
        stats.exp = 0;
        // stats.nextLvlExp = 5; // TODO No tendria que ir en una clase Level?
        stats.gold = 500;
        stats.strength = 1;
        stats.dexterity = 1;

        projectile = new Fireball(game, world);
        weapon = new SwordIron(game, world);
        shield = new ShieldWood(game, world);
        light = null;
        stats.attack = getAttack();
        stats.defense = getDefense();

        hitbox.x = 7;
        hitbox.y = 32;
        hitbox.width = 10;
        hitbox.height = 24;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        stats.motion1 = 5;
        stats.motion2 = 18;

        sheet.loadPlayerMovementFrames(player_movement, 1);
        sheet.loadWeaponFrames(sword_frame, 16, 16);

        int animationSpeed = 90;
        down = new Animation(animationSpeed, sheet.down);
        up = new Animation(animationSpeed, sheet.up);
        left = new Animation(animationSpeed, sheet.left);
        right = new Animation(animationSpeed, sheet.right);
        currentFrame = down.getFirstFrame();
        inventory.init();
    }

    /**
     * Golpea a la entidad si la attackbox en el frame de ataque colisiona con la hitbox del objetivo.
     * <p>
     * De 0 a motion1 ms se muestra el primer frame de ataque. De motion1 a motion2 ms se muestra el segundo frame de
     * ataque. Despues de motion2 vuelve al frame de movimiento. Para el caso del player solo hay un frame de ataque.
     * <p>
     * En el segundo frame de ataque, la posicion x/y se ajusta para la attackbox y verifica si colisiona con una
     * entidad.
     */
    public void hit() {

        timer.attackAnimationCounter++;
        if (timer.attackAnimationCounter <= stats.motion1) sheet.attackNum = 1; // (de 0-motion1ms frame de ataque 1)
        if (timer.attackAnimationCounter > stats.motion1 && timer.attackAnimationCounter <= stats.motion2) { // (de motion1-motion2ms frame de ataque 2)
            sheet.attackNum = 2;

            // Guarda la posicion actual de x/y y el tamaño de la hitbox
            int currentX = pos.x, currentY = pos.y;
            int hitboxWidth = hitbox.width, hitboxHeight = hitbox.height;

            /* Ajusta la attackbox (en la hoja de la espada para ser mas especificos) del player dependiendo de la
             * direccion de ataque. Es importante aclarar que las coordenadas x/y de la attackbox parten de la esquina
             * superior izquierda de la hitbox del player (nose si es necesario partir desde esa esquina). */
            switch (direction) {
                case DOWN -> {
                    attackbox.x = -1;
                    attackbox.y = 4;
                    attackbox.width = 4;
                    attackbox.height = 36;
                }
                case UP -> {
                    attackbox.x = 12;
                    attackbox.y = -43;
                    attackbox.width = 4;
                    attackbox.height = 42;
                }
                case LEFT -> {
                    attackbox.x = -20;
                    attackbox.y = 0;
                    attackbox.width = 19;
                    attackbox.height = 4;
                }
                case RIGHT -> {
                    attackbox.x = 10;
                    attackbox.y = 2;
                    attackbox.width = 19;
                    attackbox.height = 4;
                }
            }
            /* Acumula la posicion de la attackbox a la posicion del player para verificar la colision con las
             * coordenas ajustadas de la attackbox. */
            pos.x += attackbox.x;
            pos.y += attackbox.y;

            // Convierte la hitbox (el ancho y alto) en la attackbox para verificar la colision solo con la attackbox
            hitbox.width = attackbox.width;
            hitbox.height = attackbox.height;

            // Verifica la colision con el mob usando la posicion y tamaño de la hitbox actualizada, osea con la attackbox
            int mobIndex = game.collision.checkEntity(this, world.mobs);
            world.player.hitMob(mobIndex, this, weapon.stats.knockbackValue, stats.attack);

            int interactiveIndex = game.collision.checkEntity(this, world.interactives);
            world.player.hitInteractive(interactiveIndex);

            int projectileIndex = game.collision.checkEntity(this, world.projectiles);
            world.player.hitProjectile(projectileIndex);

            // Despues de verificar la colision, resetea los datos originales
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
     * Comprueba si puede atacar.
     */
    private void checkAttack() {
        if (game.keyboard.enter && !attackCanceled && timer.attackCounter == INTERVAL_WEAPON && !flags.shooting && weapon != null) {
            if (weapon.type == Type.SWORD) game.playSound(sound_swing_weapon);
            if (weapon.type != Type.SWORD) game.playSound(sound_swing_axe);
            flags.hitting = true;
            timer.attackCounter = 0;
        }
        flags.shooting = false;
        attackCanceled = false; // Para que pueda volver a atacar despues de interactuar con un npc o beber agua
    }

    /**
     * Comprueba si puede lanzar un proyectil.
     */
    private void checkShoot() {
        if (game.keyboard.f && !projectile.flags.alive && timer.projectileCounter == INTERVAL_PROJECTILE && projectile.haveResource(this) && !flags.hitting) {
            flags.shooting = true;
            game.playSound(sound_fireball);
            projectile.set(pos.x, pos.y, direction, true, this);
            // Comprueba vacante para agregar el proyectil
            for (int i = 0; i < world.projectiles[1].length; i++) {
                if (world.projectiles[world.map][i] == null) {
                    world.projectiles[world.map][i] = projectile;
                    break;
                }
            }
            projectile.subtractResource(this);
            timer.projectileCounter = 0;
        }
    }

    private void checkStats() {
        if (!game.keyboard.godMode) if (stats.hp <= 0) die();
        if (stats.hp > stats.maxHp) stats.hp = stats.maxHp;
        if (stats.mana > stats.maxMana) stats.mana = stats.maxMana;
    }

    /**
     * Interactua con el npc.
     *
     * @param i indice del npc.
     */
    private void interactNpc(int i) {
        if (i != -1) {
            auxEntity = world.mobs[world.map][i];
            Mob mob = world.mobs[world.map][i];
            if (game.keyboard.enter && mob.type == Type.NPC) {
                attackCanceled = true;
                mob.dialogue();
            } else mob.move(direction); // En caso de que sea la roca
        }
    }

    /**
     * Daña al player si colisiona con un mob hostil.
     *
     * @param i indice del mob.
     */
    private void hurt(int i) {
        if (i != -1) {
            auxEntity = world.mobs[world.map][i];
            Mob mob = world.mobs[world.map][i];
            if (!flags.invincible && !mob.flags.dead && mob.type == Type.HOSTILE) {
                game.playSound(sound_player_damage);
                int damage = Math.max(mob.stats.attack - stats.defense, 1);
                stats.hp -= damage;
                flags.invincible = true;
            }
        }
    }

    /**
     * Golpea al mob.
     *
     * @param i              indice del mob.
     * @param attacker       atacante del mob.
     * @param knockbackValue valor de knockback.
     * @param attack         tipo de ataque (sword o fireball).
     */
    public void hitMob(int i, Entity attacker, int knockbackValue, int attack) {
        if (i != -1) { // TODO Lo cambio por >= 0 para evitar la doble negacion y comparacion -1?
            auxEntity = world.mobs[world.map][i];
            Mob mob = world.mobs[world.map][i];
            if (!mob.flags.invincible && mob.type != Type.NPC) {

                if (knockbackValue > 0) mechanics.setKnockback(mob, attacker, knockbackValue);

                int damage = Math.max(attack - mob.stats.defense, 1);
                mob.stats.hp -= damage;
                game.ui.addMessageToConsole(damage + " damage!");
                if (mob.stats.hp > 0) game.playSound(mob.soundHit);

                mob.flags.invincible = true;
                mob.flags.hpBar = true;
                mob.damageReaction();

                // TODO Tendria que ir un metodo?
                if (mob.stats.hp <= 0) {
                    game.playSound(sound_mob_death);
                    if (!(mob instanceof Slime)) game.playSound(mob.soundDeath);
                    mob.flags.dead = true;
                    game.ui.addMessageToConsole("Killed the " + mob.stats.name + "!");
                    game.ui.addMessageToConsole("Exp + " + mob.stats.exp);
                    stats.exp += mob.stats.exp;
                    checkLevelUp();
                }
            }
        }
    }

    /**
     * Daña al tile interactivo.
     *
     * @param i indice del tile interactivo.
     */
    public void hitInteractive(int i) {
        if (i != -1) {
            auxEntity = world.interactives[world.map][i];
            Interactive interactive = world.interactives[world.map][i];
            if (interactive.destructible && interactive.isCorrectWeapon(weapon) && !interactive.flags.invincible) {
                interactive.playSound();
                interactive.stats.hp--;
                interactive.flags.invincible = true;

                generateParticle(interactive, interactive);

                if (interactive.stats.hp == 0) {
                    interactive.checkDrop();
                    world.interactives[world.map][i] = interactive.replaceBy();
                }
            }
        }
    }

    public void hitProjectile(int i) {
        if (i != -1) {
            auxEntity = world.projectiles[world.map][i];
            Projectile projectile = world.projectiles[world.map][i];
            // Evita dañar el propio proyectil
            if (projectile != this.projectile) {
                game.playSound(sound_player_damage);
                projectile.flags.alive = false;
                generateParticle(projectile, projectile);
            }
        }
    }

    /**
     * Comprueba si subio de nivel.
     */
    private void checkLevelUp() {
        if (stats.lvl == MAX_LVL) {
            stats.exp = 0;
            stats.nextLvlExp = 0;
            return;
        }
        if (stats.exp >= stats.nextLvlExp) {
            // TODO Separar el aumento de las estadisticas en otra clase (Stats o otra)
            stats.lvl++;
            stats.exp = 0;
            stats.nextLvlExp *= 2;
            stats.maxHp += 2;
            stats.strength++;
            stats.dexterity++;
            stats.attack = getAttack();
            stats.defense = getDefense();
            game.playSound(sound_level_up);
            dialogue.dialogues[0][0] = "You are level " + stats.lvl + "!";
            startDialogue(DIALOGUE_STATE, this, 0);
        }
    }

    /**
     * Recoge un item.
     *
     * @param i indice del item.
     */
    public void pickup(int i) { // TODO Seria el metodo getObj() de la clase Client en AO-Java
        if (i != -1) {
            Item item = world.items[world.map][i];
            if (game.keyboard.p && item.type != Type.OBSTACLE) {
                if (item.type == Type.PICKUP) item.use(world.player);
                else if (inventory.canPickup(item)) game.playSound(sound_item_pickup);
                else {
                    game.ui.addMessageToConsole("You cannot carry any more!");
                    return;
                }
                world.items[world.map][i] = null;
            }
            if (game.keyboard.enter && item.type == Type.OBSTACLE) {
                world.player.attackCanceled = true;
                item.interact();
            }
        }
    }

    @Override
    public void checkCollision() {
        flags.colliding = false;
        if (!game.keyboard.godMode) game.collision.checkTile(this);
        pickup(game.collision.checkItem(this));
        interactNpc(game.collision.checkEntity(this, world.mobs));
        hurt(game.collision.checkEntity(this, world.mobs));
        setCurrentInteractive(game.collision.checkEntity(this, world.interactives));
        // game.collision.checkEntity(this, world.interactives);
        game.event.checkEvent(this);
    }

    private void setCurrentInteractive(int i) {
        if (i != -1) auxEntity = world.interactives[world.map][i];
    }

    private void die() {
        game.playSound(sound_player_death);
        game.state = GAME_OVER_STATE;
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
     * Obtiene el frame de animacion actual.
     *
     * @return el frame de animacion actual.
     */
    private BufferedImage getCurrentAnimationFrame() {
        /* Cuando se deja de mover, devuelve el primer frame guardado de la ultima direccion para representar la
         * detencion del player. */
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
     * Reinicia el Player.
     *
     * @param fullReset true para reiniciar por completo; falso en caso contrario.
     */
    public void reset(boolean fullReset) {
        pos.set(world, this, NASHE, OUTSIDE, 23, 21, Direction.DOWN);
        stats.reset(fullReset);
        flags.reset();
        if (fullReset) inventory.init();
    }

    // TODO Activar con tecla
    private void drawRects(Graphics2D g2) {
        g2.setStroke(new BasicStroke(0));
        // Frame
        g2.setColor(Color.magenta);
        g2.drawRect(screen.xOffset, screen.yOffset, currentFrame.getWidth(), currentFrame.getHeight()); // TODO Creo que se podria reemplazar por image.getWidth()
        // Hitbox
        g2.setColor(Color.green);
        g2.drawRect(screen.xOffset + hitbox.x, screen.yOffset + hitbox.y, hitbox.width, hitbox.height);
        // Attackbox
        if (flags.hitting) {
            g2.setColor(Color.red);
            /* Se suma la posicion de la attackbox a la posicion del player porque despues de verificar la deteccion del
             * golpe en el metodo hit, se resetea la posicio del player, por lo tanto se suma desde aca para que el
             * rectangulo dibujado coincida con la posicion especificada en el metodo hit. */
            g2.drawRect(screen.xOffset + attackbox.x + hitbox.x, screen.yOffset + attackbox.y + hitbox.y, attackbox.width, attackbox.height);
        }
    }

}
