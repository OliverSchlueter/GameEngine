package de.oliver.gameEngine;

import de.oliver.gameEngine.scenes.DefaultScene;
import de.oliver.gameEngine.utils.Color;
import de.oliver.gameEngine.utils.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;


public class Window {
    private static Window instance = null;

    private final int width;
    private final int height;
    private final String title;
    private Color backgroundColor;
    private Scene currentScene;
    private float fps;

    private long glfwWindow;


    private Window(){
        this.width = 1080;
        this.height = 720;
        this.title = "Unity++";
        this.backgroundColor = new Color(1f, 1f, 1f, 1f);
        this.currentScene = new DefaultScene();
        this.fps = 0;
    }

    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        init();
        loop();

        // Free the memory
        Callbacks.glfwFreeCallbacks(glfwWindow);
        GLFW.glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    private void init(){
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initalize GLFW
        if(!GLFW.glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);

        // Create the window
        glfwWindow = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);

        if(glfwWindow == MemoryUtil.NULL){
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        GLFW.glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        GLFW.glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        GLFW.glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        GLFW.glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync
        GLFW.glfwSwapInterval(1);

        // Make the window visible
        GLFW.glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        currentScene.start();
    }

    private void loop(){
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1f;

        currentScene.init();

        while (!GLFW.glfwWindowShouldClose(glfwWindow)){
            // Poll events
            GLFW.glfwPollEvents();

            GL11.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            if(dt >= 0){
                currentScene.update(dt);
                currentScene.updateGameObjects(dt);
            }

            GLFW.glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            fps = 1f/dt;
            beginTime = endTime;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public void changeScene(Scene newScene){
        currentScene = newScene;
        currentScene.init();
        currentScene.start();
    }

    public float getFps() {
        return fps;
    }

    public static Window get(){
        if(instance == null){
            instance = new Window();
        }

        return instance;
    }

}
