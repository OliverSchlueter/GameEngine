package de.oliver.gameEngine;


import de.oliver.gameEngine.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer;
    protected Camera camera;
    protected List<GameObject> gameObjects;
    protected boolean isRunning;

    public Scene(){
        renderer = new Renderer();
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
            renderer.add(gameObject);
        }

        isRunning = true;
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        if (isRunning) {
            gameObject.start();
            renderer.add(gameObject);
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
