package com.craivet.ai;

public class Node {

	Node parent;
	int row, col;
	int gCost, hCost, fCost;
	boolean open, checked, solid;

	public Node(int row, int col) {
		this.row = row;
		this.col = col;
	}

}
