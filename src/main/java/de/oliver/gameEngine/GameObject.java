package de.oliver.gameEngine;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private String name;
    private List<Component> components;

    public GameObject(String name){
        this.name = name;
        this.components = new ArrayList<>();
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

}
