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
