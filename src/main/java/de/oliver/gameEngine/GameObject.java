package de.oliver.gameEngine;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private String name;
    private List<Component> components;
    public Transform transform;
    private int zIndex;

    public GameObject(String name){
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        this.zIndex = 0;
    }

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
        this.zIndex = zIndex;
    }

    public GameObject(String name, Transform transform){
        this(name, transform, 0);
    }

    public void update(float dt){
        for (Component c : components) {
            c.update(dt);
        }
    }

    public void start(){
        for (Component c : components) {
            c.start();
        }
    }
    
    public void imgui(){
        for (Component c : components) {
            c.imgui();
        }
    }

    public <T extends Component> T getComponent(Class<T> componentClass){
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e){
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass){
        for (int i = components.size() - 1; i > 0; i--) {
            Component c = components.get(i);
            if(componentClass.isAssignableFrom(c.getClass())){
                components.remove(i);
            }
        }
    }

    public void addComponent(Component c){
        components.add(c);
        c.setGameObject(this);
    }

    public int getZIndex() {
        return zIndex;
    }
}
