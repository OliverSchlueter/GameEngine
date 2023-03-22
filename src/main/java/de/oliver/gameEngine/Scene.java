package de.oliver.gameEngine;


import de.oliver.gameEngine.renderer.Renderer;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer;
    protected Camera camera;
    protected List<GameObject> gameObjects;
    protected boolean isRunning;
    protected GameObject activeGameObject;

    public Scene(){
        renderer = new Renderer();
        isRunning = false;
        gameObjects = new ArrayList<>();
        activeGameObject = null;
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

    public void sceneImgui(){
        if(activeGameObject != null){
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }

        imgui();
    }

    public void imgui(){

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
