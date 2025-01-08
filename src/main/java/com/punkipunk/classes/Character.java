package com.punkipunk.classes;

import com.punkipunk.entity.components.Stats;
import com.punkipunk.entity.player.Player;

/**
 * Representa las diferentes clases que forman parte del juego.
 */

public abstract class Character {

    protected String name;
    protected boolean magic;

    /**
     * Sube las estadisticas.
     */
    public void upStats(Player player) {
        Stats stats = player.stats;
        stats.lvl++;
        /* Si al pasar de nivel y sobra exp, entonces la ajusta para el siguiente nivel. Por ejemplo, si para pasar de nivel 1 a
         * nivel 2 necesito 10 de exp y mate dos Bat que dan 7 exp, entonces el total que obtengo es 14 exp que me sirven para
         * pasar a nivel 2, por lo tanto se resta la exp del nivel 2 (nextExp) a la exp actual para que quede en 4 de exp y no se
         * pierda. */
        stats.exp -= stats.nextExp;
        stats.nextExp *= 2;
        // Incrementa strenght y dexterity en 1 cada dos niveles
        stats.strength += stats.lvl % 2 == 0 ? 1 : 0;
        stats.dexterity += stats.lvl % 2 == 0 ? 1 : 0;
        // Incrementa maxHp y maxMana dependiendo de la clase cada dos niveles
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
