package de.oliver.gameEngine.components;

import de.oliver.gameEngine.Component;
import de.oliver.gameEngine.Transform;
import org.joml.Vector4f;

public class SpriteComponent extends Component {

    private Vector4f color;
    private Sprite sprite;

    private Transform lastTransform;

    private boolean isDirty;

    public SpriteComponent(Vector4f color) {
        this.color = color;
        sprite = new Sprite(null);
        isDirty = false;
    }

    public SpriteComponent(Sprite sprite){
        this.sprite = sprite;
        color = new Vector4f(1f, 1f, 1f, 1f);
        isDirty = false;
    }

    @Override
    public void start() {
        lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if(!lastTransform.equals(gameObject.transform)){
            gameObject.transform.copy(lastTransform);
            isDirty = true;
        }
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        if(!this.color.equals(color)) {
            this.color.set(color);
            isDirty = true;
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        isDirty = true;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }
}
