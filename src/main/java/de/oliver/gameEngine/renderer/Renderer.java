package de.oliver.gameEngine.renderer;

import de.oliver.gameEngine.GameObject;
import de.oliver.gameEngine.components.SpriteComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {

    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void render(){
        for (RenderBatch batch : batches) {
            batch.render();
        }
    }

    public void add(GameObject gameObject){
        SpriteComponent sprite = gameObject.getComponent(SpriteComponent.class);
        if(sprite != null){
            add(sprite);
        }
    }

    private void add(SpriteComponent sprite){
        boolean added = false;
        for (RenderBatch batch : batches) {
            if (batch.hasRoom() && batch.getZIndex() == sprite.getGameObject().getZIndex()) {
            Texture tex = sprite.getSprite().getTexture();
            if(tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom())) {
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if(!added){
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.getGameObject().getZIndex());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }
}
