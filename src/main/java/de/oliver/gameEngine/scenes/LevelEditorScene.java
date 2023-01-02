package de.oliver.gameEngine.scenes;

import de.oliver.gameEngine.KeyListener;
import de.oliver.gameEngine.Scene;
import de.oliver.gameEngine.Window;
import org.lwjgl.glfw.GLFW;

public class LevelEditorScene extends Scene {

    private boolean changingScene = false;
    private float timeToChangeScene = 1.5f;

    @Override
    public void init() {
        super.init();

        System.out.println("In level editor");
    }

    @Override
    public void update(float dt) {
        if(!changingScene && KeyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE)){
            changingScene = true;
        }

        if(changingScene && timeToChangeScene > 0){
            timeToChangeScene -= dt;
            Window.get().getBackgroundColor().r -= dt * 5f;
            Window.get().getBackgroundColor().g -= dt * 5f;
            Window.get().getBackgroundColor().b -= dt * 5f;
        } else if(changingScene){
            Window.get().changeScene(new LevelScene());
        }
    }

    public boolean isChangingScene() {
        return changingScene;
    }
}
