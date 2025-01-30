package com.punkipunk.json.model.event;

import java.util.Map;

public record EventsConfig(
        Map<String, EventData> events) {
}
