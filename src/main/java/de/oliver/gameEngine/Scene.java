package de.oliver.gameEngine;


import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;
    protected List<GameObject> gameObjects;
    protected boolean isRunning;

    public Scene(){
        isRunning = false;
        gameObjects = new ArrayList<>();
    }

    public abstract void update(float dt);

    public void updateGameObjects(float dt){
        for (GameObject gameObject : gameObjects) {
            gameObject.update(dt);
        }
    }

    public void init(){

    }

    public void start(){
        for (GameObject gameObject : gameObjects) {
            gameObject.start();
        }

        isRunning = true;
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        if (isRunning) {
            gameObject.start();
        }
    }

}
