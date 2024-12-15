package com.punkipunk.classes;

import com.punkipunk.utils.Utils;

/**
 * <p>
 * In the confines of the majestic medieval court, where royalty and nobles rub shoulders with intrigue and secrets, the jester is
 * a singular figure. Dressed in multicolored rags, his extravagant appearance and his face painted with a perpetual smile hide
 * his true nature. He is a character who pursues the illusion of being funny while carrying the weight of repulsion.
 * <p>
 * This buffoonish character is known for his ability to fit well into any situation, although his humor often falls on the clumsy
 * and grotesque side. The laughter he provokes is rather an uncomfortable laugh, where the nobles laugh not at his wit, but at
 * the strange and often incomprehensible series of stunts and jokes he performs.
 * <p>
 * The jester is always close to the nobles and kings, his presence is obligatory at banquets and festivities. But behind his
 * antics and whining attempts to please the court, he lies an astute observer of palace politics. He does not waste the
 * opportunities that life at the medieval court offers him, and his sharp tongue can be an unexpected source of information for
 * those willing to listen beyond his jokes.
 * <p>
 * However, his lack of filter and disregard for consequences can lead to sticky situations. His jokes often border on irreverence
 * and sarcasm, endangering his position and, sometimes, his life. But the jester continues on, with his painted smile and his
 * clownish wisdom, an enigma at the medieval court that is never completely solved.
 */

public class Jester extends Character {

    private static Jester instance;

    protected Jester() {
        this.name = "Jester";
        this.magic = true;
    }

    public static Jester getInstance() {
        if (instance == null) instance = new Jester();
        return instance;
    }

    protected int getIncreaseMaxHp() {
        return 2;
        // return Utils.random(4);
    }

    protected int getIncreaseMaxMana() {
        return Utils.random(2);
    }

}
