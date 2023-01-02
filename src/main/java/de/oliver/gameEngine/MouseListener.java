package de.oliver.gameEngine;

import org.lwjgl.glfw.GLFW;

public class MouseListener {

    private static MouseListener instance;

    private double scrollX;
    private double scrollY;
    private double xPos;
    private double yPos;
    private double lastX;
    private double lastY;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener(){
        this.scrollX = 0d;
        this.scrollY = 0d;
        this.xPos = 0d;
        this.yPos = 0d;
        this.lastX = 0d;
        this.lastY = 0d;
    }

    public static void mousePosCallback(long window, double xPos, double yPos){
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if(action == GLFW.GLFW_PRESS) {
            if(button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = true;
            }
        } else if(action == GLFW.GLFW_RELEASE) {
            if(button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float)get().xPos;
    }

    public static float getY() {
        return (float)get().yPos;
    }

    public static float getDx(){
        return (float)(get().lastX - get().xPos);
    }

    public static float getDy(){
        return (float)(get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        return button < get().mouseButtonPressed.length && get().mouseButtonPressed[button];
    }

    public static MouseListener get() {
        if(instance == null){
            instance = new MouseListener();
        }

        return instance;
    }
}
