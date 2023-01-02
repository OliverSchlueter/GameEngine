package de.oliver;

import de.oliver.gameEngine.Window;
import de.oliver.gameEngine.scenes.LevelEditorScene;

public class Main {
    public static void main(String[] args) {
        Window window = Window.get();
        window.setCurrentScene(new LevelEditorScene());
        window.run();
    }
}