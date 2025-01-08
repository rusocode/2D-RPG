package com.punkipunk.json.model;

public record InteractiveData(
        Integer hp,
        Boolean destructible,
        String texturePath,
        HitboxData hitbox) {

    public InteractiveData {
        if (hp == null) hp = 0;
        if (destructible == null) destructible = false;
    }

}

