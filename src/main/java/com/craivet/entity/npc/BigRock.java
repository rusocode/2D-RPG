package com.craivet.entity.npc;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;
import com.craivet.entity.item.DoorIron;
import com.craivet.tile.Interactive;
import com.craivet.tile.MetalPlate;
import com.craivet.utils.Utils;

import java.util.ArrayList;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class BigRock extends Npc {

    public static final String npc_name = "Big Rock";

    public BigRock(Game game, World world, int x, int y) {
        super(game, world, x, y);
        initDefaultValues();
    }

    private void initDefaultValues() {
        name = npc_name;
        type = TYPE_NPC;
        image = Utils.scaleImage(entity_bigrock, tile_size, tile_size);
        speed = 2;
        hitbox.x = 2;
        hitbox.y = 6;
        hitbox.width = 44;
        hitbox.height = 40;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        dialogueSet = -1;
        initDialogue();
    }

    public void update() {

    }

    public void setAction() {

    }

    public void move(int direction) {
        this.direction = direction;
        checkCollision();
        if (!flags.colliding) {
            switch (direction) {
                case DOWN -> y += speed;
                case UP -> y -= speed;
                case LEFT -> x -= speed;
                case RIGHT -> x += speed;
            }
        }

        detectedPlate();
    }

    public void speak() {
        facePlayer();
        startDialogue(DIALOGUE_STATE, this, dialogueSet);

        dialogueSet++;
        if (dialogues[dialogueSet][dialogueIndex] == null) dialogueSet = 0;
    }

    private void initDialogue() {
        dialogues[0][0] = "It's a giant rock.";
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
            if (world.interactives[world.map][i] != null && world.interactives[world.map][i].name != null && world.interactives[world.map][i].name.equals(MetalPlate.item_name))
                plates.add(world.interactives[world.map][i]);
        }

        // Agrega las rocas a la lista
        for (int i = 0; i < world.npcs[1].length; i++) {
            if (world.npcs[world.map][i] != null && world.npcs[world.map][i].name.equals(BigRock.npc_name))
                rocks.add(world.npcs[world.map][i]);
        }

        // Itera las placas y verifica la distancia con la roca
        for (Interactive plate : plates) {
            int xDistance = Math.abs(x - plate.x);
            int yDistance = Math.abs(y - plate.y);
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
                if (world.items[world.map][i] != null && world.items[world.map][i].name.equals(DoorIron.item_name)) {
                    world.items[world.map][i] = null;
                    game.playSound(sound_door_iron_opening);
                }
            }
        }

    }

}
