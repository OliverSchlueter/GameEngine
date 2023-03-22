package de.oliver.gameEngine.scenes;

import de.oliver.gameEngine.*;
import de.oliver.gameEngine.components.SpriteComponent;
import de.oliver.gameEngine.components.Spritesheet;
import de.oliver.gameEngine.utils.AssetPool;
import imgui.ImGui;
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

        spritesheet = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 1);
        obj1.addComponent(new SpriteComponent(spritesheet.getSprite(0)));
        addGameObject(obj1);

        obj2 = new GameObject("Object 1", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), -1);
        obj2.addComponent(new SpriteComponent(spritesheet.getSprite(15)));
        addGameObject(obj2);

        GameObject obj3 = new GameObject("Object 3", new Transform(new Vector2f(500, 500), new Vector2f(100f, 100)));
        obj3.addComponent(new SpriteComponent(new Vector4f(1, 0, 0, 1)));
        addGameObject(obj3);
        activeGameObject = obj3;

    }

    @Override
    public void update(float dt) {

        renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("Test window");
        ImGui.text("Random text");
        ImGui.end();
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet(
                "assets/images/spritesheet.png",
                new Spritesheet(
                        AssetPool.getTexture("assets/images/spritesheet.png"),
                        16,
                        16,
                        26,
                        0
                )
        );
    }

}
