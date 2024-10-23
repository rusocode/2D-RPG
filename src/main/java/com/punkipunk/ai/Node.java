package com.punkipunk.ai;

public class Node {

    public int row, col;
    Node parent;
    int gCost, hCost, fCost;
    boolean open, checked, solid;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }

}
