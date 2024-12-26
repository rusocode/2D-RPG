package com.punkipunk.classes;

import com.punkipunk.entity.player.Player;
import com.punkipunk.entity.base.Stats;

/**
 * It represents the different classes that are part of the game.
 */

public abstract class Character {

    protected String name;
    protected boolean magic;

    /**
     * Upload stats according to class.
     *
     * @param player player.
     */
    public void upStats(Player player) {
        Stats stats = player.stats;
        stats.lvl++;
        stats.exp -= stats.nextExp;
        stats.nextExp *= 2;
        // Incrementa strenght y dexterity en 1 cada dos niveles
        stats.strength += stats.lvl % 2 == 0 ? 1 : 0;
        stats.dexterity += stats.lvl % 2 == 0 ? 1 : 0;
        int increasedMaxHp = stats.lvl % 2 == 0 ? getIncreaseMaxHp() : 0;
        int increasedMaxMana = stats.lvl % 2 == 0 ? getIncreaseMaxMana() : 0;
        if (increasedMaxHp > 0) stats.increaseMaxHp(increasedMaxHp);
        if (increasedMaxMana > 0) stats.increaseMaxMana(increasedMaxMana);
    }

    /**
     * Gets the maxHp increase.
     *
     * @return the increase of maxHp.
     */
    protected int getIncreaseMaxHp() {
        return 0;
    }

    /**
     * Gets the maxMana increase.
     *
     * @return the increase of maxMana.
     */
    protected int getIncreaseMaxMana() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public boolean isMagic() {
        return magic;
    }

}
