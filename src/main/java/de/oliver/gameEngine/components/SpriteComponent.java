package de.oliver.gameEngine.components;

import de.oliver.gameEngine.Component;
import org.joml.Vector4f;

public class SpriteComponent extends Component {

    private Vector4f color;
    private Sprite sprite;

    public SpriteComponent(Vector4f color) {
        this.color = color;
        sprite = new Sprite(null);
    }

    public SpriteComponent(Sprite sprite){
        this.sprite = sprite;
        color = new Vector4f(1f, 1f, 1f, 1f);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {

    }

    public Vector4f getColor() {
        return color;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
