package de.oliver.gameEngine.utils;

import de.oliver.gameEngine.components.Spritesheet;
import de.oliver.gameEngine.renderer.Shader;
import de.oliver.gameEngine.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {

    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();

    public static Shader getShader(String resourceName){
        File file = new File(resourceName);
        if(shaders.containsKey(file.getAbsolutePath())){
            return shaders.get(resourceName);
        } else {
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName){
        File file = new File(resourceName);
        if(textures.containsKey(file.getAbsolutePath())){
            return textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpriteSheet(String resourceName, Spritesheet spritesheet){
        File file = new File(resourceName);
        if(!spritesheets.containsKey(file.getAbsolutePath())){
            spritesheets.put(resourceName, spritesheet);
        }
    }

    public static Spritesheet getSpriteSheet(String resourceName){
        File file = new File(resourceName);
        if(spritesheets.containsKey(file.getAbsolutePath())){
            return spritesheets.get(file.getAbsolutePath());
        }

        System.err.println("Could not find spritesheet: " + resourceName);

        return null;
    }
}
