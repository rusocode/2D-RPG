package com.punkipunk.entity.mob;

import com.punkipunk.Dialogue;
import com.punkipunk.Direction;
import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.interactive.Interactive;
import com.punkipunk.entity.interactive.InteractiveID;
import com.punkipunk.entity.item.ItemID;
import com.punkipunk.states.State;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

import java.util.List;

/**
 * TODO No deberia ser un interactive?
 */

public class Box extends Mob {

    public Box(IGame game, World world, int... pos) {
        super(game, world, pos);
        dialogue = new Dialogue(game);
        dialogue.set = -1;
        sheet.frame = Utils.scaleTexture(Utils.loadTexture(mobData.texturePath()), mobData.frameWidth(), mobData.frameHeight());
        initDialogue();
    }

    @Override
    public void update() {

    }

    @Override
    public void move(Entity entity, Direction direction) {
        this.direction = direction;
        checkCollisions();
        if (!flags.colliding) {
            switch (direction) {
                case DOWN -> position.y += stats.speed;
                case UP -> position.y -= stats.speed;
                case LEFT -> position.x -= stats.speed;
                case RIGHT -> position.x += stats.speed;
            }
        }
        detectedPlate();
    }

    @Override
    public void dialogue() {
        dialogue.startDialogue(State.DIALOGUE, this, dialogue.set);
        dialogue.set++;
        if (dialogue.dialogues[dialogue.set][dialogue.index] == null) dialogue.set = 0;
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "It's a giant box.";
    }

    /**
     * Detecta las placas y si todas las cajas estan vinculadas a ella, la puerta de hierro se abre.
     */
    private void detectedPlate() {

        int c;
        List<Interactive> plates;
        List<Mob> boxes;

        /* Obtiene un flujo secuencial de las entidades interactivas del mapa actual y en base al flujo filtra todas las placas de
         * metal con nombre asignado para convertirlas en una lista, lo mismo hace para las cajas. */
        plates = world.entitySystem.getInteractives(world.map.id).stream()
                .filter(interactive -> interactive.getID() == InteractiveID.METAL_PLATE)
                .toList();

        boxes = world.entitySystem.getMobs(world.map.id).stream()
                .filter(mob -> mob.getID() == MobID.BOX)
                .toList();

        // Itera las placas y verifica la distancia con la caja
        for (Interactive plate : plates) {
            int xDistance = Math.abs(position.x - plate.position.x);
            int yDistance = Math.abs(position.y - plate.position.y);
            int distance = Math.max(xDistance, yDistance);
            // Vincula la caja a la placa si esta a menos de 8 pixeles de distancia
            if (distance < 8) {
                if (linkedEntity == null) {
                    linkedEntity = plate;
                    game.getGameSystem().audio.playSound(AudioID.Sound.CHIPWALL);
                }
            } else if (linkedEntity == plate) linkedEntity = null; // Desvincula la caja de la placa si se mueve de esta placa
        }

        // Cuenta la lista de cajas vinculadas a las placas
        c = (int) boxes.stream().filter(box -> box.linkedEntity != null).count();

        // Si todas las cajas estan sobre las placas, la puerta de hierro se abre
        if (c == boxes.size()) {
            world.entitySystem.getItems(world.map.id).stream()
                    .filter(item -> item.getID() == ItemID.IRON_DOOR)
                    .findFirst()
                    .ifPresent(door -> {
                        world.entitySystem.removeItem(world.map.id, door);
                        game.getGameSystem().audio.playSound(AudioID.Sound.DOOR_IRON_OPENING);
                    });
        }

    }

    @Override
    public MobID getID() {
        return MobID.BOX;
    }

}
