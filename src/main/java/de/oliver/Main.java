package de.oliver;

import de.oliver.gameEngine.Window;
import de.oliver.gameEngine.scenes.LevelEditorScene;
import org.joml.Vector4f;

public class Main {
    public static void main(String[] args) {
        Window window = Window.create(800, 500, "Test window", new Vector4f());
        window.setCurrentScene(new LevelEditorScene());
        window.run();
    }
}