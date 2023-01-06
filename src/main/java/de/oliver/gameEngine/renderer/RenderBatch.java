package de.oliver.gameEngine.renderer;

import de.oliver.gameEngine.Window;
import de.oliver.gameEngine.components.SpriteComponent;
import de.oliver.gameEngine.utils.Time;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexArrayObject;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

public class RenderBatch {

    // Vertex
    // ======
    // Pos                Color
    // float, float,      float, float, float, float

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = (POS_OFFSET + POS_SIZE) * Float.BYTES;
    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteComponent[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoID;
    private int vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
        sprites = new SpriteComponent[maxBatchSize];
        shader = new Shader("D:\\Workspaces\\Java\\GameEngine\\src\\main\\resources\\shaders\\default.glsl");
        shader.compile();

        // 4 vertices per quad
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        numSprites = 0;
        hasRoom = true;
    }

    public void start(){
        // Generate VAO, VBO and EBO buffer objects and send to GPU
        vaoID = ARBVertexArrayObject.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL30.GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = GL30.glGenBuffers();
        int[] indices = generateIndices();
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, eboID);
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indices, GL30.GL_STATIC_DRAW);

        // Enable the buffer attribute pointers
        GL30.glVertexAttribPointer(0, POS_SIZE, GL30.GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        GL30.glEnableVertexAttribArray(0);

        GL30.glVertexAttribPointer(1, COLOR_SIZE, GL30.GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        GL30.glEnableVertexAttribArray(1);
    }

    public void addSprite(SpriteComponent sprite){
        // Get index and add renderObject
        int index = numSprites;
        sprites[index] = sprite;
        numSprites++;

        // Add properties to local vertices array
        loadVertexProperties(index);

        if(numSprites >= maxBatchSize){
            hasRoom = false;
        }
    }

    public void render(){
        // For now, rebuffer all data every frame
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
        GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 0, vertices);

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.get().getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.get().getCurrentScene().getCamera().getViewMatrix());
        shader.uploadFloat("uTime", Time.getTime());

        GL30.glBindVertexArray(vaoID);
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        GL30.glDrawElements(GL30.GL_TRIANGLES, numSprites * 6, GL30.GL_UNSIGNED_INT, 0);

        // Unbind everything
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);

        shader.detach();
    }

    private void loadVertexProperties(int index){
        SpriteComponent sprite = sprites[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();

        // Add vertices with the appropriate properties

        float xAdd = 1f;
        float yAdd = 1f;
        for (int i = 0; i < 4; i++) {
            if(i == 1){
                yAdd = 0f;
            } else if(i == 2){
                xAdd = 0f;
            } else if(i == 3){
                yAdd = 1f;
            }

            // Load position
            vertices[offset] = sprite.getGameObject().transform.position.x + (xAdd * sprite.getGameObject().transform.scale.x);
            vertices[offset + 1] = sprite.getGameObject().transform.position.y + (yAdd * sprite.getGameObject().transform.scale.y);

            // Load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndices(){
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];

        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int index){
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        // Triangle 1
        elements[offsetArrayIndex + 0] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean hasRoom() {
        return hasRoom;
    }
}
