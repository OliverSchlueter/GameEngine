package de.oliver.gameEngine.scenes;

import de.oliver.gameEngine.Scene;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class LevelEditorScene extends Scene {

    private String vertexShaderSrc = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID;
    private int fragmentID;
    private int shaderProgram;

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

    private int vaoID;  // vertex array object
    private int vboID;  // vertex buffer object
    private int eboID;  // element buffer object


    @Override
    public void init() {
        super.init();

        // Compile and link shaders

        // Load and compile the vertex shader
        vertexID = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);

        // Pass the shader source to the GPU
        GL30.glShaderSource(vertexID, vertexShaderSrc);
        GL30.glCompileShader(vertexID);

        // Check for error in compilation
        int success = GL30.glGetShaderi(vertexID, GL30.GL_COMPILE_STATUS);
        if(success == GL30.GL_FALSE){
            int len = GL30.glGetShaderi(vertexID, GL30.GL_INFO_LOG_LENGTH);
            System.err.println("Error in defaultShader.glsl");
            System.err.println("Vertex shader compilation failed");
            System.err.println(GL30.glGetShaderInfoLog(vertexID, len));
            return;
        }

        // Load and compile the fragment shader
        fragmentID = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);

        // Pass the shader source to the GPU
        GL30.glShaderSource(fragmentID, fragmentShaderSrc);
        GL30.glCompileShader(fragmentID);

        // Check for error in compilation
        success = GL30.glGetShaderi(fragmentID, GL30.GL_COMPILE_STATUS);
        if(success == GL30.GL_FALSE){
            int len = GL30.glGetShaderi(fragmentID, GL30.GL_INFO_LOG_LENGTH);
            System.err.println("Error in defaultShader.glsl");
            System.err.println("Fragment shader compilation failed");
            System.err.println(GL30.glGetShaderInfoLog(fragmentID, len));
            return;
        }

        // Link shaders
        shaderProgram = GL30.glCreateProgram();
        GL30.glAttachShader(shaderProgram, vertexID);
        GL30.glAttachShader(shaderProgram, fragmentID);
        GL30.glLinkProgram(shaderProgram);

        success = GL30.glGetProgrami(shaderProgram, GL30.GL_LINK_STATUS);
        if(success == GL30.GL_FALSE){
            int len = GL30.glGetProgrami(shaderProgram, GL30.GL_INFO_LOG_LENGTH);
            System.err.println("Error in defaultShader.glsl");
            System.err.println("Linking shaders failed");
            System.err.println(GL30.glGetProgramInfoLog(shaderProgram, len));
            return;
        }

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
        // Bind shader program
        GL30.glUseProgram(shaderProgram);

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
        GL30.glUseProgram(0);
    }

}
