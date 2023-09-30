package com.craivet.classes;

import com.craivet.world.entity.Player;
import com.craivet.world.entity.Stats;

/**
 * Representa las diferentes clases que forman parte del juego.
 */

public abstract class Character {

    protected String name;
    protected boolean magic;

    /**
     * Obtiene el incremento de maxHp.
     *
     * @return el incremento de maxHp.
     */
    protected int getIncreaseMaxHp() {
        return 0;
    }

    /**
     * Obtiene el incremento de maxMana.
     *
     * @return el incremento de maxMana.
     */
    protected int getIncreaseMaxMana() {
        return 0;
    }

    /**
     * Sube las estadisticas segun la clase.
     *
     * @param player player.
     */
    public void upStats(Player player) {
        Stats stats = player.stats;
        stats.lvl++;
        stats.exp -= stats.nextLvlExp;
        stats.nextLvlExp *= 2;
        stats.strength++;
        stats.dexterity++;
        int increasedMaxHp = getIncreaseMaxHp();
        int increasedMaxMana = getIncreaseMaxMana();
        if (increasedMaxHp > 0) {
            stats.increaseMaxHp(increasedMaxHp);
            stats.fullHp();
        }
        if (increasedMaxMana > 0) {
            stats.increaseMaxMana(increasedMaxMana);
            stats.fullMana();
        }
    }

    public String getName() {
        return name;
    }

    public boolean isMagic() {
        return magic;
    }

}
