package com.punkipunk.json.model;

public record InteractiveData(
        String name,
        Integer hp,
        Boolean destructible,
        String sound,
        String texturePath
) {

    public InteractiveData {
        if (hp == null) hp = 0;
        if (destructible == null) destructible = false;
    }
}
