package de.oliver.gameEngine.scenes;

import de.oliver.gameEngine.*;
import de.oliver.gameEngine.components.SpriteComponent;
import de.oliver.gameEngine.components.Spritesheet;
import de.oliver.gameEngine.utils.AssetPool;
import org.joml.Vector2f;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private GameObject obj2;
    private Spritesheet spritesheet;

    @Override
    public void init() {
        loadResources();
        camera = new Camera(new Vector2f());

        spritesheet = AssetPool.getSpriteSheet("D:\\Workspaces\\Java\\GameEngine\\src\\main\\resources\\images\\spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteComponent(spritesheet.getSprite(0)));
        addGameObject(obj1);

        obj2 = new GameObject("Object 1", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteComponent(spritesheet.getSprite(15)));
        addGameObject(obj2);

    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;
    @Override
    public void update(float dt) {

        spriteFlipTimeLeft -= dt;
        if (spriteFlipTimeLeft <= 0) {
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex > 4) {
                spriteIndex = 0;
            }
            obj1.getComponent(SpriteComponent.class).setSprite(spritesheet.getSprite(spriteIndex));
        }

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
