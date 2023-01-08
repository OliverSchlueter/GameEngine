package de.oliver.gameEngine.renderer;

import de.oliver.gameEngine.Window;
import de.oliver.gameEngine.components.SpriteComponent;
import de.oliver.gameEngine.utils.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.ARBVertexArrayObject;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class RenderBatch implements Comparable<RenderBatch>{

    // Vertex
    // ======
    // Pos                Color                              tex coords         tex id
    // float, float,      float, float, float, float         float, float       float

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;

    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + TEX_COORDS_SIZE + TEX_ID_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteComponent[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    private List<Texture> textures;

    private int vaoID;
    private int vboID;
    private int maxBatchSize;
    private Shader shader;
    private int zIndex;

    public RenderBatch(int maxBatchSize, int zIndex) {
        this.maxBatchSize = maxBatchSize;
        this.zIndex = zIndex;
        sprites = new SpriteComponent[maxBatchSize];
        shader = AssetPool.getShader("D:\\Workspaces\\Java\\GameEngine\\src\\main\\resources\\shaders\\default.glsl");

        // 4 vertices per quad
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        numSprites = 0;
        hasRoom = true;
        textures = new ArrayList<>();
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

        GL30.glVertexAttribPointer(2, TEX_COORDS_SIZE, GL30.GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        GL30.glEnableVertexAttribArray(2);

        GL30.glVertexAttribPointer(3, TEX_ID_SIZE, GL30.GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        GL30.glEnableVertexAttribArray(3);
    }

    public void addSprite(SpriteComponent sprite){
        // Get index and add renderObject
        int index = numSprites;
        sprites[index] = sprite;
        numSprites++;

        if(sprite.getSprite().getTexture() != null && !textures.contains(sprite.getSprite().getTexture())){
            textures.add(sprite.getSprite().getTexture());
        }

        // Add properties to local vertices array
        loadVertexProperties(index);

        if(numSprites >= maxBatchSize){
            hasRoom = false;
        }
    }

    public void render(){
        boolean rebufferData = false;
        for (int i = 0; i < numSprites; i++) {
            SpriteComponent sprite = sprites[i];
            if (sprite.isDirty()) {
                loadVertexProperties(i);
                sprite.setDirty(false);
                rebufferData = true;
            }
        }

        if(rebufferData) {
            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
            GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 0, vertices);
        }

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.get().getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.get().getCurrentScene().getCamera().getViewMatrix());

        for (int i = 0; i < textures.size(); i++) {
            GL30.glActiveTexture(GL30.GL_TEXTURE1 + i);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlots);

        GL30.glBindVertexArray(vaoID);
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        GL30.glDrawElements(GL30.GL_TRIANGLES, numSprites * 6, GL30.GL_UNSIGNED_INT, 0);

        // Unbind everything
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);

        for (int i = 0; i < textures.size(); i++) {
            GL30.glActiveTexture(GL30.GL_TEXTURE1 + i);
            textures.get(i).unbind();
        }

        shader.detach();
    }

    private void loadVertexProperties(int index){
        SpriteComponent sprite = sprites[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();
        Vector2f[] texCoords = sprite.getSprite().getTexCoords();

        int texId = 0;
        if(sprite.getSprite().getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if(textures.get(i) == sprite.getSprite().getTexture()){
                    texId = i + 1;
                    break;
                }
            }
        }


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

            // Load tex coords
            vertices[offset + 6] = texCoords[i].x;
            vertices[offset + 7] = texCoords[i].y;

            // Load tex id
            vertices[offset + 8] = texId;

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

    public boolean hasTextureRoom(){
        return textures.size() < 8;
    }

    public boolean hasTexture(Texture texture){
        return textures.contains(texture);
    }

    public int getZIndex() {
        return zIndex;
    }

    @Override
    public int compareTo(RenderBatch o) {
        return Integer.compare(this.zIndex, o.zIndex);
    }
}
