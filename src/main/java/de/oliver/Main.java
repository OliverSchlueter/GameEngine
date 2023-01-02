package de.oliver;

import de.oliver.gameEngine.Window;
import de.oliver.gameEngine.scenes.LevelEditorScene;

public class Main {
    public static void main(String[] args) {
        Window window = Window.get();
        window.changeScene(new LevelEditorScene());
        window.run();
    }
}