package com.craivet.classes;

import com.craivet.world.entity.Player;
import com.craivet.world.entity.Stats;

/**
 * It represents the different classes that are part of the game.
 */

public abstract class Character {

    protected String name;
    protected boolean magic;

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

    /**
     * Upload stats according to class.
     *
     * @param player player.
     */
    public void upStats(Player player) {
        Stats stats = player.stats;
        stats.lvl++;
        stats.exp -= stats.nextLvlExp;
        stats.nextLvlExp *= 2;
        // Incrementa strenght y dexterity en 1 cada dos niveles
        stats.strength += stats.lvl % 2 == 0 ? 1 : 0;
        stats.dexterity += stats.lvl % 2 == 0 ? 1 : 0;
        int increasedMaxHp = stats.lvl % 2 == 0 ? getIncreaseMaxHp() : 0;
        int increasedMaxMana = stats.lvl % 2 == 0 ? getIncreaseMaxMana() : 0;
        if (increasedMaxHp > 0) stats.increaseMaxHp(increasedMaxHp);
        if (increasedMaxMana > 0) stats.increaseMaxMana(increasedMaxMana);
    }

    public String getName() {
        return name;
    }

    public boolean isMagic() {
        return magic;
    }

}
