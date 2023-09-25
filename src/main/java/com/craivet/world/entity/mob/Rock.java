package com.craivet.world.entity.mob;

import com.craivet.Dialogue;
import com.craivet.Direction;
import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.DoorIron;
import com.craivet.utils.*;
import com.craivet.world.tile.Interactive;
import com.craivet.world.tile.MetalPlate;

import java.util.ArrayList;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class Rock extends Mob {

    public static final String NAME = "Rock";

    public Rock(Game game, World world, int col, int row) {
        super(game, world, col, row);
        dialogue = new Dialogue();
        type = Type.NPC;
        stats.name = NAME;
        stats.speed = 1;
        hitbox.x = 1;
        hitbox.y = 6;
        hitbox.width = tile - hitbox.x - 1;
        hitbox.height = tile - hitbox.y;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        dialogue.set = -1;
        sheet.frame = Utils.scaleImage(rock, tile, tile);
        initDialogue();
    }

    @Override
    public void update() {

    }

    @Override
    public void move(Direction direction) {
        this.direction = direction;
        checkCollision();
        if (!flags.colliding) {
            switch (direction) {
                case DOWN -> pos.y += stats.speed;
                case UP -> pos.y -= stats.speed;
                case LEFT -> pos.x -= stats.speed;
                case RIGHT -> pos.x += stats.speed;
            }
        }

        detectedPlate();
    }

    @Override
    public void dialogue() {
        startDialogue(DIALOGUE_STATE, this, dialogue.set);
        dialogue.set++;
        if ( dialogue.dialogues[dialogue.set][dialogue.index] == null) dialogue.set = 0;
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "It's a giant rock.";
    }

    /**
     * Detecta las placas y si todas las rocas estan vinculadas a esta, se abre la puerta de hierro.
     */
    private void detectedPlate() {
        int c = 0;
        ArrayList<Interactive> plates = new ArrayList<>();
        ArrayList<Entity> rocks = new ArrayList<>();

        // Agrega las placas a la lista
        for (int i = 0; i < world.interactives[1].length; i++) {
            if (world.interactives[world.map][i] != null && world.interactives[world.map][i].stats.name != null && world.interactives[world.map][i].stats.name.equals(MetalPlate.item_name))
                plates.add(world.interactives[world.map][i]);
        }

        // Agrega las rocas a la lista
        for (int i = 0; i < world.mobs[1].length; i++) {
            if (world.mobs[world.map][i] != null && world.mobs[world.map][i].stats.name.equals(Rock.NAME))
                rocks.add(world.mobs[world.map][i]);
        }

        // Itera las placas y verifica la distancia con la roca
        for (Interactive plate : plates) {
            int xDistance = Math.abs(pos.x - plate.pos.x);
            int yDistance = Math.abs(pos.y - plate.pos.y);
            int distance = Math.max(xDistance, yDistance);
            if (distance < 8) { // Vincula la roca a la placa si esta a menos de 8 pixeles de distancia
                if (linkedEntity == null) {
                    linkedEntity = plate;
                    game.playSound(sound_chipwall);
                }
                // Desvincula la roca de la placa si se vuelve a mover de esta
            } else if (linkedEntity == plate) linkedEntity = null;
        }

        // Cuenta la lista de rocas vinculadas con las placas
        for (Entity rock : rocks)
            if (rock.linkedEntity != null) c++;

        // Si todas las rocas estan en las placas, la puerta de hierro se abre
        if (c == rocks.size()) {
            for (int i = 0; i < world.items[1].length; i++) {
                if (world.items[world.map][i] != null && world.items[world.map][i].stats.name.equals(DoorIron.NAME)) {
                    world.items[world.map][i] = null;
                    game.playSound(sound_door_iron_opening);
                }
            }
        }

    }

}
