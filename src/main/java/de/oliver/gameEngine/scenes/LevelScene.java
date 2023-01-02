package de.oliver.gameEngine.scenes;

import de.oliver.gameEngine.Scene;
import de.oliver.gameEngine.Window;

public class LevelScene extends Scene {

    @Override
    public void init() {
        super.init();

        Window.get().getBackgroundColor().r = 1;
        Window.get().getBackgroundColor().g = 1;
        Window.get().getBackgroundColor().b = 1;

        System.out.println("In level");
    }

    @Override
    public void update(float dt) {
        System.out.println(Window.get().getFps() + " fps");
    }

}
