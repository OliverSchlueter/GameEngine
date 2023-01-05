package de.oliver.gameEngine.components;

import de.oliver.gameEngine.Component;

public class FontComponent extends Component {

    @Override
    public void start() {
        if(gameObject.getComponent(SpriteComponent.class) != null){
            System.out.println("Found sprite compononent");
        }
    }

    @Override
    public void update(float dt) {

    }
}
