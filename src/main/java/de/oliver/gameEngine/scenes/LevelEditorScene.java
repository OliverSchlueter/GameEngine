package de.oliver.gameEngine.scenes;

import de.oliver.gameEngine.*;
import de.oliver.gameEngine.components.SpriteComponent;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {

    @Override
    public void init() {
        camera = new Camera(new Vector2f());

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(300 - yOffset * 2);
        float sizeX = totalWidth / 100f;
        float sizeY = totalHeight / 100f;

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject gameObject = new GameObject("Obj" + x + "" + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                gameObject.addComponent(new SpriteComponent(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
                addGameObject(gameObject);
            }
        }

        GameObject gameObject = new GameObject(
                "test obj",
                new Transform(new Vector2f(500, 500),
                new Vector2f(100, 100))
        );

        gameObject.addComponent(new SpriteComponent(new Vector4f(1, 0, 0, 1)));
        addGameObject(gameObject);
    }

    @Override
    public void update(float dt) {
        System.out.println("FPS: " + Window.get().getFps());
        renderer.render();
    }

}
