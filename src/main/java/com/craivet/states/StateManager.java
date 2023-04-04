package com.craivet.states;


public class StateManager {

	private GameState state;

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}
}
