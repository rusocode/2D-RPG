package com.craivet.classes;

import com.craivet.utils.Utils;

/**
 * In the confines of the majestic medieval court, where royalty and nobles rub shoulders with intrigue and secrets, the jester is
 * a unique figure. Dressed in multicolored rags, his extravagant appearance and his face painted with
 * A perpetual smile hides his true nature. He is a character who pursues the illusion of being funny
 * while he carries the weight of repulsion.
 * <p>
 * This buffoonish character is known for his ability to fit well into any situation, although his humor sometimes
 * often falls on the clumsy and grotesque side. The laughter he provokes is rather an uncomfortable laugh, where the nobles laugh not
 * not because of his wit, but because of the strange and often incomprehensible series of stunts and jokes that he performs.
 * <p>
 * The jester is always close to the nobles and kings, his presence is obligatory at banquets and festivities.
 * But behind his antics and whining attempts to please the court, he lies an astute observer of politics.
 * palatial. He does not waste the opportunities that life at the medieval court offers him, and his sharp tongue can be
 * An unexpected source of information for those willing to listen beyond his jokes.
 * <p>
 * However, his lack of filter and disregard for consequences can lead to sticky situations. Their
 * His jokes often border on irreverence and sarcasm, endangering his position and, sometimes, his life. But
 * jester continues forward, with his painted smile and his clownish wisdom, an enigma in the medieval court that never was
 * resolves completely.
 */

public class Jester extends Character {

    private static Jester instance = null;

    public static Jester getInstance() {
        if (instance == null) instance = new Jester();
        return instance;
    }

    protected Jester() {
        this.name = "Jester";
        this.magic = true;
    }

    protected int getIncreaseMaxHp() {
        return 2;
        // return Utils.random(4);
    }

    protected int getIncreaseMaxMana() {
        return Utils.random(2);
    }

}
