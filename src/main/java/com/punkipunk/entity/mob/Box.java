package com.punkipunk.entity.mob;

import com.punkipunk.Dialogue;
import com.punkipunk.Direction;
import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.states.State;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.entity.base.Entity;
import com.punkipunk.entity.base.Type;
import com.punkipunk.entity.interactive.Interactive;
import com.punkipunk.entity.interactive.MetalPlate;
import com.punkipunk.entity.item.IronDoor;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static com.punkipunk.utils.Global.tile;

public class Box extends Mob {

    public static final String NAME = "Box";

    public Box(Game game, World world, int col, int row) {
        super(game, world, col, row);
        dialogue = new Dialogue(game);
        type = Type.NPC;
        stats.name = NAME;
        stats.speed = 1;
        hitbox = new Rectangle(1, 1, tile - 2, tile - 2);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        dialogue.set = -1;
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.BOX), tile, tile);
        initDialogue();
    }

    @Override
    public void update() {

    }

    @Override
    public void move(Direction direction) {
        this.direction = direction;
        checkCollisions();
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
        dialogue.startDialogue(State.DIALOGUE, this, dialogue.set);
        dialogue.set++;
        if (dialogue.dialogues[dialogue.set][dialogue.index] == null) dialogue.set = 0;
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "It's a giant box.";
    }

    /**
     * Detect the plates and if all the boxes are linked to it, the iron door opens.
     */
    private void detectedPlate() {
        int c = 0;
        ArrayList<Interactive> plates = new ArrayList<>();
        ArrayList<Entity> boxes = new ArrayList<>();

        // Add the plates to the list
        for (int i = 0; i < world.entities.interactives[1].length; i++)
            // TODO Hace falta comprobar si el nombre de la placa es distinto a null?
            if (world.entities.interactives[world.map.num][i] != null && world.entities.interactives[world.map.num][i].stats.name != null && world.entities.interactives[world.map.num][i].stats.name.equals(MetalPlate.NAME))
                plates.add(world.entities.interactives[world.map.num][i]);

        // Add the boxes to the list
        for (int i = 0; i < world.entities.mobs[1].length; i++)
            if (world.entities.mobs[world.map.num][i] != null && world.entities.mobs[world.map.num][i].stats.name.equals(Box.NAME))
                boxes.add(world.entities.mobs[world.map.num][i]);

        // Iterate the plates and check the distance with the box
        for (Interactive plate : plates) {
            int xDistance = Math.abs(pos.x - plate.pos.x);
            int yDistance = Math.abs(pos.y - plate.pos.y);
            int distance = Math.max(xDistance, yDistance);
            if (distance < 8) { // Link the box to the plate if it is less than 8 pixels away
                if (linkedEntity == null) {
                    linkedEntity = plate;
                    game.system.audio.playSound(AudioID.Sound.CHIPWALL);
                }
                // Detaches the box from the plate if it moves from this plate again
            } else if (linkedEntity == plate) linkedEntity = null;
        }

        // Count the list of boxes linked to the plates
        for (Entity box : boxes)
            if (box.linkedEntity != null) c++;

        // If all the boxes are on the plates, the iron door opens
        if (c == boxes.size()) {
            for (int i = 0; i < world.entities.items[1].length; i++) {
                if (world.entities.items[world.map.num][i] != null && world.entities.items[world.map.num][i].stats.name.equals(IronDoor.NAME)) {
                    world.entities.items[world.map.num][i] = null;
                    game.system.audio.playSound(AudioID.Sound.DOOR_IRON_OPENING);
                }
            }
        }

    }

}
