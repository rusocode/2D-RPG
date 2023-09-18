package com.craivet.ai;

import com.craivet.Direction;
import com.craivet.world.World;
import com.craivet.world.entity.Entity;

import java.util.ArrayList;

import static com.craivet.utils.Global.*;

public class AStar {

    private final World world;

    Node[][] node;
    Node startNode, goalNode, currentNode;

    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();

    boolean goalReached;
    int step;

    public AStar(World world) {
        this.world = world;
        initNodes();
    }

    /**
     * Busca la mejor ruta para la entidad.
     *
     * @param goalRow fila objetivo.
     * @param goalCol columna objetivo.
     */
    public void searchPath(Entity entity, int goalRow, int goalCol) {
        int startRow = (entity.pos.y + entity.stats.hitbox.y) / tile;
        int startCol = (entity.pos.x + entity.stats.hitbox.x) / tile;

        setNodes(startRow, startCol, goalRow, goalCol);

        // Si devuelve verdadero, significa que ha encontrado un camino para guiar a la entidad hacia el objetivo
        if (search()) {

            // Obtiene la siguiente posicion x/y de la ruta
            int nextX = pathList.get(0).col * tile;
            int nextY = pathList.get(0).row * tile;
            // Obtiene la posicion de la entidad
            int left = entity.pos.x + entity.stats.hitbox.x;
            int right = entity.pos.x + entity.stats.hitbox.x + entity.stats.hitbox.width;
            int top = entity.pos.y + entity.stats.hitbox.y;
            int bottom = entity.pos.y + entity.stats.hitbox.y + entity.stats.hitbox.height;

            // Averigua la direccion relativa del siguiente nodo segun la posicion actual de la entidad
            /* Si el lado izquierdo y derecho de la entidad estan entre la siguiente posicion x de la ruta, entonces
             * se define su movimiento hacia arriba o abajo. */
            if (left >= nextX && right < nextX + tile) entity.stats.direction = top > nextY ? Direction.UP : Direction.DOWN;
            /* Si el lado superior y inferior de la entidad estan entre la siguiente posicion y de la ruta, entonces
             * se define su movimiento hacia la izquierda o derecha. */
            if (top >= nextY && bottom < nextY + tile) entity.stats.direction = left > nextX ? Direction.LEFT : Direction.RIGHT;

                /* Hasta ahora funciona bien, pero en el caso de que una entidad este en el tile que esta debajo del
                 * siguiente tile, PERO no puede cambiar a la direccion DIR_UP por que hay un arbol. */
            else if (top > nextY && left > nextX) {
                // up o left
                entity.stats.direction = Direction.UP;
                entity.checkCollision();
                if (entity.flags.colliding) entity.stats.direction = Direction.LEFT;
            } else if (top > nextY && left < nextX) {
                // up o right
                entity.stats.direction = Direction.UP;
                entity.checkCollision();
                if (entity.flags.colliding) entity.stats.direction = Direction.RIGHT;
            } else if (top < nextY && left > nextX) {
                // down o left
                entity.stats.direction = Direction.DOWN;
                entity.checkCollision();
                if (entity.flags.colliding) entity.stats.direction = Direction.LEFT;
            } else if (top < nextY && left < nextX) {
                // down o right
                entity.stats.direction = Direction.DOWN;
                entity.checkCollision();
                if (entity.flags.colliding) entity.stats.direction = Direction.RIGHT;
            }

        }
    }

    // TODO El problema de esto es que la busqueda se actualiza cada 60 segundos
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
     * Establece el nodo inicial, el nodo objetivo y los nodos solidos.
     */
    public void setNodes(int startRow, int startCol, int goalRow, int goalCol) {
        resetNodes(); // TODO No haria falta resetear los nodos en caso de que el objetivo sea fijo

        startNode = node[startRow][startCol];
        goalNode = node[goalRow][goalCol];
        currentNode = startNode;

        openList.add(currentNode);

        // Establece los nodos solidos verificando los tiles solidos y los tiles interactivos destructibles
        for (int row = 0; row < MAX_MAP_ROW; row++) {
            for (int col = 0; col < MAX_MAP_COL; col++) {
                int tileIndex = world.tileIndex[world.map][row][col];
                if (world.tileData[tileIndex].solid) node[row][col].solid = true;

                for (int i = 0; i < world.interactives[1].length; i++) {
                    if (world.interactives[world.map][i] != null && world.interactives[world.map][i].destructible) {
                        int itRow = world.interactives[world.map][i].pos.y / tile; // TODO Falta sumarle la hitbox
                        int itCol = world.interactives[world.map][i].pos.x / tile;
                        node[itRow][itCol].solid = true;
                    }
                }

                // Funciona bien, pero cuando la entidad esta en una posicion cerrada (no literalmente) de tiles interactivos, se queda atascada
                for (int i = 0; i < world.items[1].length; i++) {
                    if (world.items[world.map][i] != null && world.items[world.map][i].stats.solid) {
                        int itRow = (world.items[world.map][i].pos.y + world.items[world.map][i].stats.hitbox.y) / tile;
                        int itCol = (world.items[world.map][i].pos.x + world.items[world.map][i].stats.hitbox.x) / tile;
                        node[itRow][itCol].solid = true;
                    }
                }

                for (int i = 0; i < world.mobs[1].length; i++) {
                    if (world.mobs[world.map][i] != null) {
                        int itRow = (world.mobs[world.map][i].pos.y + world.mobs[world.map][i].stats.hitbox.y) / tile;
                        int itCol = (world.mobs[world.map][i].pos.x + world.mobs[world.map][i].stats.hitbox.x) / tile;
                        node[itRow][itCol].solid = true;
                    }
                }

                for (int i = 0; i < world.mobs[1].length; i++) {
                    if (world.mobs[world.map][i] != null) {
                        int itRow = (world.mobs[world.map][i].pos.y + world.mobs[world.map][i].stats.hitbox.y) / tile;
                        int itCol = (world.mobs[world.map][i].pos.x + world.mobs[world.map][i].stats.hitbox.x) / tile;
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
            // Con esta lista cualquier entidad puede rastrear la ruta
            pathList.add(0, current); // Agrega el nodo al primer slot para que el ultimo nodo agregado este en [0]
            current = current.parent;
        }
    }

}
