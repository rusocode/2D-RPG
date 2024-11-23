package com.punkipunk.scene;

/**
 * <p>
 * Enumeration representing the different view states available in the game. Each state corresponds to a specific view that can be
 * displayed in the game interface.
 */

public enum ViewState {

    /**
     * No view is currently displayed.
     */
    NONE,

    /**
     * Statistics view is displayed.
     */
    STATS,

    /**
     * Options view is displayed.
     */
    OPTIONS,

    /**
     * Inventory view is displayed.
     */
    INVENTORY,

    /**
     * Controls view is displayed.
     */
    CONTROLS;

    /**
     * Checks if this state represents an active view (not NONE).
     *
     * @return true if the state represents an active view, false otherwise.
     */
    public boolean isActive() {
        return this != NONE;
    }

    /**
     * Gets the view name in a formatted string.
     *
     * @return the lowercase name of the view state.
     */
    public String getViewName() {
        return this == NONE ? "none" : this.name().toLowerCase();
    }

}
