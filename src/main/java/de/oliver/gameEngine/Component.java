package de.oliver.gameEngine;

public abstract class Component {

    protected GameObject gameObject = null;

    public void start(){

    }

    public void update(float dt){

    }

    public void imgui(){

    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
