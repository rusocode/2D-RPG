package com.craivet.ai;

import com.craivet.world.World;

import java.util.ArrayList;

import static com.craivet.util.Global.*;

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

    // TODO El problema de esto es que la busqueda se actualiza cada 60 segundos
    public boolean search() {
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
                        int itRow = world.interactives[world.map][i].y / tile_size; // TODO Falta sumarle la hitbox
                        int itCol = world.interactives[world.map][i].x / tile_size;
                        node[itRow][itCol].solid = true;
                    }
                }

                // Funciona bien, pero cuando la entidad esta en una posicion cerrada (no literalmente) de tiles interactivos, se queda atascada
                for (int i = 0; i < world.items[1].length; i++) {
                    if (world.items[world.map][i] != null && world.items[world.map][i].solid) {
                        int itRow = (world.items[world.map][i].y + world.items[world.map][i].hitbox.y) / tile_size;
                        int itCol = (world.items[world.map][i].x + world.items[world.map][i].hitbox.x) / tile_size;
                        node[itRow][itCol].solid = true;
                    }
                }

                for (int i = 0; i < world.mobs[1].length; i++) {
                    if (world.mobs[world.map][i] != null) {
                        int itRow = (world.mobs[world.map][i].y + world.mobs[world.map][i].hitbox.y) / tile_size;
                        int itCol = (world.mobs[world.map][i].x + world.mobs[world.map][i].hitbox.x) / tile_size;
                        node[itRow][itCol].solid = true;
                    }
                }

                for (int i = 0; i < world.mobs[1].length; i++) {
                    if (world.mobs[world.map][i] != null) {
                        int itRow = (world.mobs[world.map][i].y + world.mobs[world.map][i].hitbox.y) / tile_size;
                        int itCol = (world.mobs[world.map][i].x + world.mobs[world.map][i].hitbox.x) / tile_size;
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
