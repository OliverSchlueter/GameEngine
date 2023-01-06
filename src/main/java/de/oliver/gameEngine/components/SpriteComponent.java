package de.oliver.gameEngine.components;

import de.oliver.gameEngine.Component;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteComponent extends Component {

    private Vector4f color;

    public SpriteComponent(Vector4f color) {
        this.color = color;
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
}
