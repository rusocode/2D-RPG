package com.punkipunk.audio;

public enum AudioType {

    MUSIC(true),  // La música se reproduce en loop por defecto
    SOUND(false); // Los efectos de sonido no

    private final boolean loopByDefault;

    AudioType(boolean loopByDefault) {
        this.loopByDefault = loopByDefault;
    }

    public boolean shouldLoop() {
        return loopByDefault;
    }

}
