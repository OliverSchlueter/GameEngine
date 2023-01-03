package de.oliver.gameEngine.scenes;

import de.oliver.gameEngine.Scene;
import de.oliver.gameEngine.renderer.Shader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class LevelEditorScene extends Scene {

    private float[] vertexArray = {
         // position              color
            0.5f,  -0.5f, 0f,     1f, 0f, 0f, 1f,  // Bottom right  0
            -0.5f, 0.5f,  0f,     0f, 1f, 0f, 1f,  // Top left      1
            0.5f,  0.5f,  0f,     0f, 0f, 1f, 1f,  // Top right     2
            -0.5f, -0.5f, 0f,     1f, 1f, 0f, 1f,  // Bottom left   3
    };

    // Must be in counter-clockwise order
    private int[] elementArray = {
            2, 1, 0, // Top right triangle
            0, 1, 3  // Bottom left triangle
    };

    private int vaoID;
    private int vboID;
    private int eboID;
    private Shader defaultShader;

    @Override
    public void init() {
        super.init();

        defaultShader = new Shader("D:\\Workspaces\\Java\\GameEngine\\src\\main\\resources\\shaders\\default.glsl");

        defaultShader.compile();

        // Generate VAO, VBO and EBO buffer objects and send to GPU
        vaoID = ARBVertexArrayObject.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO and upload the vertex buffer
        vboID = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertexBuffer, GL30.GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, eboID);
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL30.GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;

        GL30.glVertexAttribPointer(0, positionsSize, GL30.GL_FLOAT, false, vertexSizeBytes, 0);
        GL30.glEnableVertexAttribArray(0);

        GL30.glVertexAttribPointer(1, colorSize, GL30.GL_FLOAT, false, vertexSizeBytes, positionsSize*floatSizeBytes);
        GL30.glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        defaultShader.use();

        // Bind the VAO
        GL30.glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        GL30.glDrawElements(GL30.GL_TRIANGLES, elementArray.length, GL30.GL_UNSIGNED_INT, 0);

        // Unbind everything
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);

        GL30.glBindVertexArray(0);

        defaultShader.detach();
    }

}
