package com.punkipunk.audio;

/**
 * Define las constantes para identificar los diferentes archivos de audio del juego.
 * <p>
 * Esta clase agrupa los identificadores en categorias internas:
 * <ul>
 * <li>{@link Music} - Pistas musicales de fondo
 * <li>{@link Ambient} - Sonidos ambientales de las zonas
 * <li>{@link Sound} - Efectos de sonido del juego
 * </ul>
 * <p>
 * Las constantes se usan para referenciar archivos de audio sin acoplar el codigo a rutas especificas. Las rutas reales se
 * definen en el archivo {@code audio.json}.
 */

public final class AudioID {

    private AudioID() {
    }

    public static final class Music {

        public static final String BOSS = "boss";
        public static final String MAIN = "main";

        private Music() {
        }
    }

    public static final class Ambient {

        public static final String CITY = "city";
        public static final String VILLAGE = "village";
        public static final String FOREST = "forest";
        public static final String DUNGEON = "dungeon";

        private Ambient() {
        }
    }

    public static final class Sound {

        public static final String BAT_DEATH = "batDeath";
        public static final String BAT_DEATH2 = "batDeath2";
        public static final String BAT_HIT = "batHit";
        public static final String BAT_HIT2 = "batHit2";
        public static final String BURST_OF_FIRE = "burstOfFire";
        public static final String BURST_OF_FIRE2 = "burstOfFire2";
        public static final String CHEST_OPENING = "chestOpening";
        public static final String CHIPWALL = "chipwall";
        public static final String CLICK2 = "click2";
        public static final String CUT_TREE = "cutTree";
        public static final String DOOR_IRON_OPENING = "doorIronOpening";
        public static final String DOOR_OPENING = "doorOpening";
        public static final String DRAW_PICKAXE = "drawPickaxe";
        public static final String DRAW_SWORD = "drawSword";
        public static final String DRINK_POTION = "drinkPotion";
        public static final String EAT = "eat";
        public static final String FIREBALL = "fireball";
        public static final String GOLD_PICKUP = "goldPickup";
        public static final String HOVER = "hover";
        public static final String ITEM_PICKUP = "itemPickup";
        public static final String LEVEL_UP = "levelUp";
        public static final String MINE = "mine";
        public static final String MOB_DEATH = "mobDeath";
        public static final String MOB_HIT = "mobHit";
        public static final String ORC_DEATH = "orcDeath";
        public static final String ORC_HIT = "orcHit";
        public static final String PLAYER_DAMAGE = "playerDamage";
        public static final String PLAYER_DEATH = "playerDeath";
        public static final String SLEEP = "sleep";
        public static final String SLIME_HIT = "slimeHit";
        public static final String SPAWN = "spawn";
        public static final String SPAWN2 = "spawn2";
        public static final String SWING_AXE = "swingAxe";
        public static final String SWING_WEAPON = "swingWeapon"; // TODO Cambiar por SWING_SWORD
        public static final String TRADE_BUY = "tradeBuy";
        public static final String TRADE_OPENING = "tradeOpening";
        public static final String TRADE_SELL = "tradeSell";

        private Sound() {
        }
    }

}
