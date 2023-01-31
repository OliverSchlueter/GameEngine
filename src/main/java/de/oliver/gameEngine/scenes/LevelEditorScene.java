package de.oliver.gameEngine.scenes;

import de.oliver.gameEngine.*;
import de.oliver.gameEngine.components.SpriteComponent;
import de.oliver.gameEngine.components.Spritesheet;
import de.oliver.gameEngine.utils.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private GameObject obj2;
    private Spritesheet spritesheet;

    @Override
    public void init() {
        loadResources();
        camera = new Camera(new Vector2f());

        spritesheet = AssetPool.getSpriteSheet("D:\\Workspaces\\Java\\GameEngine\\src\\main\\resources\\images\\spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 1);
        obj1.addComponent(new SpriteComponent(spritesheet.getSprite(0)));
        addGameObject(obj1);

        obj2 = new GameObject("Object 1", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), -1);
        obj2.addComponent(new SpriteComponent(spritesheet.getSprite(15)));
        addGameObject(obj2);

        GameObject obj3 = new GameObject("Object 3", new Transform(new Vector2f(500, 500), new Vector2f(1f, 100)));
        obj3.addComponent(new SpriteComponent(new Vector4f(1, 0, 0, 1)));
        addGameObject(obj3);

    }

    @Override
    public void update(float dt) {

        renderer.render();
    }

    private void loadResources() {
        AssetPool.getShader("D:\\Workspaces\\Java\\GameEngine\\src\\main\\resources\\shaders\\default.glsl");

        AssetPool.addSpriteSheet(
                "D:\\Workspaces\\Java\\GameEngine\\src\\main\\resources\\images\\spritesheet.png",
                new Spritesheet(
                        AssetPool.getTexture("D:\\Workspaces\\Java\\GameEngine\\src\\main\\resources\\images\\spritesheet.png"),
                        16,
                        16,
                        26,
                        0
                )
        );
    }

}
