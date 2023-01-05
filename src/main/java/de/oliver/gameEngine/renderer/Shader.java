package de.oliver.gameEngine.renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class Shader {

    private final String filePath;
    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;

    public Shader(String filePath){
        this.filePath = filePath;

        try{
            String source = Files.readString(Path.of(filePath));
            String[] split = source.split("(#type)( )+([a-zA-Z]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if(firstPattern.equalsIgnoreCase("vertex")){
                vertexSource = split[1];
            } else if(firstPattern.equalsIgnoreCase("fragment")){
                fragmentSource = split[1];
            } else {
                throw new IOException("Unexpected shader type");
            }

            if(secondPattern.equalsIgnoreCase("vertex")){
                vertexSource = split[2];
            } else if(secondPattern.equalsIgnoreCase("fragment")){
                fragmentSource = split[2];
            } else {
                throw new IOException("Unexpected shader type");
            }

        } catch (IOException e){
            System.err.println("Could not open file for shader: " + filePath);
            e.printStackTrace();
        }
    }

    public void compile(){
        int vertexID;
        int fragmentID;

        // Compile and link shaders

        // Load and compile the vertex shader
        vertexID = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);

        // Pass the shader source to the GPU
        GL30.glShaderSource(vertexID, vertexSource);
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
        GL30.glShaderSource(fragmentID, fragmentSource);
        GL30.glCompileShader(fragmentID);

        // Check for error in compilation
        success = GL30.glGetShaderi(fragmentID, GL30.GL_COMPILE_STATUS);
        if(success == GL30.GL_FALSE){
            int len = GL30.glGetShaderi(fragmentID, GL30.GL_INFO_LOG_LENGTH);
            System.err.println("Error in: " + filePath);
            System.err.println("Fragment shader compilation failed");
            System.err.println(GL30.glGetShaderInfoLog(fragmentID, len));
            return;
        }

        // Link shaders
        shaderProgramID = GL30.glCreateProgram();
        GL30.glAttachShader(shaderProgramID, vertexID);
        GL30.glAttachShader(shaderProgramID, fragmentID);
        GL30.glLinkProgram(shaderProgramID);

        success = GL30.glGetProgrami(shaderProgramID, GL30.GL_LINK_STATUS);
        if(success == GL30.GL_FALSE){
            int len = GL30.glGetProgrami(shaderProgramID, GL30.GL_INFO_LOG_LENGTH);
            System.err.println("Error in: " + filePath);
            System.err.println("Linking shaders failed");
            System.err.println(GL30.glGetProgramInfoLog(shaderProgramID, len));
            return;
        }
    }

    public void use(){
        // Bind shader program
        GL30.glUseProgram(shaderProgramID);
    }

    public void detach(){
        GL30.glUseProgram(0);
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = GL30.glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(4*4);
        mat4.get(matBuffer);

        GL30.glUniformMatrix4fv(varLocation, false, matBuffer);
    }

}
