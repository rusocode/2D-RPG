package com.punkipunk.json.model.map;

import com.punkipunk.world.MapID;

public record MapData(
        MapID id,
        String name,
        String zone,
        String ambient,
        String path) {

}
