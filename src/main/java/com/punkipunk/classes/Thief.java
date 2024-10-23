package com.punkipunk.classes;

/**
 * <p>
 * In the dark alleys and shadowy corners of the medieval land, the Thief glides like a shadow itself. This skilled criminal is
 * not a magician, but he possesses an ability that seems magical: he can disappear from your sight before you realize it, leaving
 * you empty-handed and without having noticed his departure. Dexterity and stealth are his most powerful weapons.
 * <p>
 * Never trust them, because they are masters in the art of lies and deceptive courtesy. They may present themselves as loyal
 * friends and companions, hiding their true intentions under a mask of camaraderie. Their words are sweet as honey, but their
 * heart is as dark as the night in which they operate.
 * <p>
 * Silk ropes for climbing walls, lockpicks for opening locks, and daggers sharp as snake fangs are usually the Thief's ideal
 * equipment. They glide silently across the roofs and shadows of the forests, stalking their prey with keen eyes. No vault is
 * safe, no treasure is out of reach.
 * <p>
 * But be careful, for if you cross the path of a Thief, your wealth and possessions may vanish like smoke. In a medieval world
 * where betrayal and deception are the order of the day, the Thief is a master of his craft, an enigma shrouded in shadows and
 * mystery.
 */

public class Thief extends Character {

    private static Thief instance = null;

    protected Thief() {
        this.name = "Thief";
    }

    public static Thief getInstance() {
        if (instance == null) instance = new Thief();
        return instance;
    }

    protected int getIncreaseMaxHp() {
        return 4;
        // return Utils.random(6);
    }

}
