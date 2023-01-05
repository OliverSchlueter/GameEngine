package de.oliver.gameEngine.renderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {

    private String filePath;
    private int texID;

    public Texture(String filePath) {
        this.filePath = filePath;

        // Generate texture on GPU
        texID = GL30.glGenTextures();
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texID);

        // Set texture parameters
        // Repeate imgae in both directions
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_REPEAT);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_REPEAT);
        // When stretching the image -> pixelate
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
        // When shrinking an imgage -> pixelate
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);

        // Load image
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = STBImage.stbi_load(filePath, width, height, channels, 0);

        if(image != null){
            if(channels.get(0) == 3) {
                GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGB, width.get(0), height.get(0), 0, GL30.GL_RGB, GL30.GL_UNSIGNED_BYTE, image);
            } else if(channels.get(0) == 4){
                GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, width.get(0), height.get(0), 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, image);
            } else {
                System.err.println("Unknown number of channels in texture '" + filePath + "'");
            }
            STBImage.stbi_image_free(image);
        } else {
            System.err.println("Could not load texture '" + filePath + "'");
        }

    }

    public void bind(){
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texID);
    }

    public void unbind(){
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
    }

}
