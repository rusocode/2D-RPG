package com.punkipunk.json.model.event;

public record EventData(
        String map,
        int x,
        int y,
        String direction,
        String type,
        TeleportTarget target) {
}

