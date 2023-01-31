package de.oliver.gameEngine;

import de.oliver.gameEngine.listeners.KeyListener;
import de.oliver.gameEngine.listeners.MouseListener;
import de.oliver.gameEngine.scenes.DefaultScene;
import org.joml.Vector4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;


public class Window {
    private static Window instance = null;

    private int width;
    private int height;
    private String title;
    private Vector4f backgroundColor;
    private Scene currentScene;
    private float fps;
    private boolean isRunning;

    private long glfwWindow;
    private ImGuiLayer imGuiLayer;


    private Window(int width, int height, String title, Vector4f backgroundColor){
        this.width = width;
        this.height = height;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.currentScene = new DefaultScene();
        this.fps = 0;
        isRunning = false;
    }

    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        isRunning = true;
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
        GLFW.glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
           width =  newWidth;
           height = newHeight;
        });

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync
        GLFW.glfwSwapInterval(1);

        // Make the window visible
        GLFW.glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        GL30.glEnable(GL30.GL_BLEND);
        GL30.glBlendFunc(GL30.GL_ONE, GL30.GL_ONE_MINUS_SRC_ALPHA);

        imGuiLayer = new ImGuiLayer(glfwWindow);
        imGuiLayer.initImGui();

        currentScene.start();
    }

    private void loop(){
        float beginTime = (float) GLFW.glfwGetTime();
        float endTime;
        float dt = -1f;

        currentScene.init();

        while (!GLFW.glfwWindowShouldClose(glfwWindow)){
            // Poll events
            GLFW.glfwPollEvents();

            GL11.glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, backgroundColor.w);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            if(dt >= 0){
                currentScene.update(dt);
                currentScene.updateGameObjects(dt);
            }

            imGuiLayer.update(dt);

            GLFW.glfwSwapBuffers(glfwWindow);

            endTime = (float) GLFW.glfwGetTime();
            dt = endTime - beginTime;
            fps = 1f/dt;
            beginTime = endTime;
        }

        isRunning = false;
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

    public Vector4f getBackgroundColor() {
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

    public boolean isRunning() {
        return isRunning;
    }

    public static Window create(int width, int height, String title, Vector4f backgroundColor){
        instance = new Window(width, height, title, backgroundColor);
        return instance;
    }

    public static Window get(){
        if(instance == null){
            instance = new Window(500, 500, "hello world", new Vector4f(1, 1, 1, 1));
        }

        return instance;
    }

}
