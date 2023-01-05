package de.oliver.gameEngine;

public abstract class Component {

    protected GameObject gameObject = null;

    public abstract void update(float dt);

    public void start(){

    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
