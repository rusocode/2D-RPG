package com.punkipunk.ai;

import com.punkipunk.Direction;
import com.punkipunk.world.World;
import com.punkipunk.entity.base.Entity;

import java.util.ArrayList;

import static com.punkipunk.utils.Global.*;

// TODO Rename Pathfinding
public class AStar {

    private final World world;
    public ArrayList<Node> pathList = new ArrayList<>();
    Node[][] node;
    Node startNode, goalNode, currentNode;
    ArrayList<Node> openList = new ArrayList<>();
    boolean goalReached;
    int step;

    public AStar(World world) {
        this.world = world;
        initNodes();
    }

    /**
     * Find the best path for the entity.
     *
     * @param goalRow goal row.
     * @param goalCol goal column.
     */
    public void searchPath(Entity entity, int goalRow, int goalCol) {
        /* TODO Hay un bug con respecto a el rectangulo colisionador de la entidad cuando este es mas grande o igual al
         * tile (32 en este caso). Es decir que cuando es mas grande, la ruta que encuentra la entidad hacia el objetivo
         * (player, por ejemplo) se bugea. No entiendo la razon de esto. */
        int startRow = (int) ((entity.pos.y + entity.hitbox.getY()) / tile);
        int startCol = (int) ((entity.pos.x + entity.hitbox.getX()) / tile);

        setNodes(startRow, startCol, goalRow, goalCol);

        // If it returns true, it means that you have found a path to guide the entity to the goal
        if (search()) {

            // Gets the next x/y position of the path
            int nextX = pathList.get(0).col * tile;
            int nextY = pathList.get(0).row * tile;
            // Gets the position of the entity
            int left = (int) (entity.pos.x + entity.hitbox.getX());
            int right = (int) (entity.pos.x + entity.hitbox.getX() + entity.hitbox.getWidth());
            int top = (int) (entity.pos.y + entity.hitbox.getY());
            int bottom = (int) (entity.pos.y + entity.hitbox.getY() + entity.hitbox.getHeight());

            // Find out the relative address of the next node based on the current position of the entity
            /* If the left and right side of the entity are between the next x position of the path, then its movement
             * up or down is defined. */
            if (left >= nextX && right < nextX + tile) entity.direction = top > nextY ? Direction.UP : Direction.DOWN;
            /* If the top and bottom side of the entity are between the next position and the path, then its movement to
             * the left or right is defined. */
            if (top >= nextY && bottom < nextY + tile)
                entity.direction = left > nextX ? Direction.LEFT : Direction.RIGHT;

                /* So far it works fine, but in the case that an entity is in the tile that is below the next tile, BUT
                 * it cannot change to the UP because there is a tree. */
            else if (top > nextY && left > nextX) {
                // up o left
                entity.direction = Direction.UP;
                entity.checkCollisions();
                if (entity.flags.colliding) entity.direction = Direction.LEFT;
            } else if (top > nextY && left < nextX) {
                // up o right
                entity.direction = Direction.UP;
                entity.checkCollisions();
                if (entity.flags.colliding) entity.direction = Direction.RIGHT;
            } else if (top < nextY && left > nextX) {
                // down o left
                entity.direction = Direction.DOWN;
                entity.checkCollisions();
                if (entity.flags.colliding) entity.direction = Direction.LEFT;
            } else if (top < nextY && left < nextX) {
                // down o right
                entity.direction = Direction.DOWN;
                entity.checkCollisions();
                if (entity.flags.colliding) entity.direction = Direction.RIGHT;
            }

        }
    }

    // FIXME The problem with this is that the search updates every 60 seconds
    private boolean search() {
        while (!goalReached && step < 500) {

            currentNode.checked = true;
            openList.remove(currentNode);

            int row = currentNode.row;
            int col = currentNode.col;
            if (row - 1 >= 0) openNode(node[row - 1][col]);
            if (col - 1 >= 0) openNode(node[row][col - 1]);
            if (row + 1 < MAX_MAP_ROW) openNode(node[row + 1][col]);
            if (col + 1 < MAX_MAP_COL) openNode(node[row][col + 1]);

            int bestNodeIndex = 0;
            int bestNodefCost = 999;
            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodefCost)
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) bestNodeIndex = i;
            }

            if (openList.isEmpty()) break;

            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }

            step++;
        }

        return goalReached;
    }

    /**
     * Sets the start node, target node and solid nodes.
     */
    private void setNodes(int startRow, int startCol, int goalRow, int goalCol) {
        // TODO It would not be necessary to reset the nodes in case the objective is fixed
        resetNodes();

        startNode = node[startRow][startCol];
        goalNode = node[goalRow][goalCol];
        currentNode = startNode;

        openList.add(currentNode);

        // Set solid nodes by checking solid tiles and destructible interactive tiles
        for (int row = 0; row < MAX_MAP_ROW; row++) {
            for (int col = 0; col < MAX_MAP_COL; col++) {
                int tileIndex = world.map.tileIndex[world.map.num][row][col];
                if (world.map.tileData[tileIndex].solid) node[row][col].solid = true;

                for (int i = 0; i < world.entities.interactives[1].length; i++) {
                    if (world.entities.interactives[world.map.num][i] != null && world.entities.interactives[world.map.num][i].destructible) {
                        int itRow = world.entities.interactives[world.map.num][i].pos.y / tile; // TODO The hitbox needs to be added
                        int itCol = world.entities.interactives[world.map.num][i].pos.x / tile;
                        node[itRow][itCol].solid = true;
                    }
                }

                // It works fine, but when the entity is in a closed position (not literally) of interactive tiles, it gets stuck
                for (int i = 0; i < world.entities.items[1].length; i++) {
                    if (world.entities.items[world.map.num][i] != null && world.entities.items[world.map.num][i].solid) {
                        int itRow = (int) ((world.entities.items[world.map.num][i].pos.y + world.entities.items[world.map.num][i].hitbox.getY()) / tile);
                        int itCol = (int) ((world.entities.items[world.map.num][i].pos.x + world.entities.items[world.map.num][i].hitbox.getX()) / tile);
                        node[itRow][itCol].solid = true;
                    }
                }

                for (int i = 0; i < world.entities.mobs[1].length; i++) {
                    if (world.entities.mobs[world.map.num][i] != null) {
                        int itRow = (int) ((world.entities.mobs[world.map.num][i].pos.y + world.entities.mobs[world.map.num][i].hitbox.getY()) / tile);
                        int itCol = (int) ((world.entities.mobs[world.map.num][i].pos.x + world.entities.mobs[world.map.num][i].hitbox.getX()) / tile);
                        node[itRow][itCol].solid = true;
                    }
                }

                getCost(node[row][col]);
            }
        }

    }

    private void initNodes() {
        node = new Node[MAX_MAP_ROW][MAX_MAP_COL];
        for (int row = 0; row < MAX_MAP_ROW; row++)
            for (int col = 0; col < MAX_MAP_COL; col++)
                node[row][col] = new Node(row, col);
    }

    private void resetNodes() {
        for (int row = 0; row < MAX_MAP_ROW; row++) {
            for (int col = 0; col < MAX_MAP_COL; col++) {
                node[row][col].open = false;
                node[row][col].checked = false;
                node[row][col].solid = false;
            }
        }
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    private void getCost(Node node) {
        // G Cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;
        // H Cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;
        // F Cost
        node.fCost = node.gCost + node.hCost;
    }

    private void openNode(Node node) {
        if (!node.open && !node.checked && !node.solid) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackThePath() {
        Node current = goalNode;
        while (current != startNode) {
            // With this list any entity can trace the path
            pathList.add(0, current); // Add the node to the first slot so that the last added node is in [0]
            current = current.parent;
        }
    }

}
