package com.punkipunk.entity.mob;

import com.punkipunk.core.IGame;
import com.punkipunk.entity.player.PlayerDummy;
import com.punkipunk.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MobFactory {

    private final IGame game;
    private final World world;

    public MobFactory(IGame game, World world) {
        this.game = game;
        this.world = world;
    }

    public Mob create(MobID id, int... pos) {
        return switch (id) {
            case BAT -> new Bat(game, world, pos);
            case BOX -> new Box(game, world, pos);
            case LIZARD -> new Lizard(game, world, pos);
            case OLDMAN -> new Oldman(game, world, pos);
            case ORC -> new Orc(game, world, pos);
            case RED_SLIME -> new RedSlime(game, world, pos);
            case SLIME -> new Slime(game, world, pos);
            case TRADER -> new Trader(game, world, pos);
            case PLAYER_DUMMY -> new PlayerDummy(game, world);
        };
    }

    public List<Mob> createMobsByCategory(MobCategory category, int count) {
        return Arrays.stream(MobID.values())
                .filter(type -> type.category == category)
                .limit(count)
                .map(this::create)
                .collect(Collectors.toList());
    }

    // List<Mob> hostileMobs = mobFactory.createMobsByCategory(MobCategory.HOSTILE, 5);

}
