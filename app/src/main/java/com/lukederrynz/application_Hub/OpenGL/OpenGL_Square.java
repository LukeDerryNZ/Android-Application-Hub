package com.lukederrynz.application_Hub.OpenGL;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Luke on 6/09/2017
 *
 * A square primitive constructed from two adjacent triangles
 */

class OpenGL_Square {

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private int mProgram;
    private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 };
    private float color[] = {0.2f, 0.709803922f, 0.898039216f, 1.0f};

    // Number of coords per vertex
    static final int COORDS_PER_VERTEX = 3;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;


    private float size = 64.0f;
    private float posX = 0f;
    private float posY = 0f;

    // Coords in counter-clockwise order
    private float squareCoords[];


    private final String vertexShaderCode =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "void main() {" +
        // The matrix must be included as a modifier of gl_Position.
        // NOTE: The uMVPMatrix must be first.
        "   gl_Position = uMVPMatrix * vPosition;" +
        "}";

    private final String fragmentShaderCode =
        "precision mediump float;" +
        "uniform vec4 vColor;" +
        "void main() {" +
        "   gl_FragColor = vColor;" +
        "}";


    /**
     * Sets up the drawing object data for use in OpenGLES context.
     *
     * @param x - float : X Axis position
     * @param y  - float : Y Axis position
     * @param s - float : Height and Width dimension
     */
    public OpenGL_Square(float x, float y, float s) {

        size = s;
        posX = x;
        posY = y;

        init();
    }


    /**
     * Initialize Coordinate buffers, OpenGLES vars.
     *
     */
    public void init() {

        setCoords();

        // Init vertex byte buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // Init byte buffer for draw list ( 2 bytes per short)
        ByteBuffer dlbb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        // Use native byte order
        dlbb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlbb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // Prepare shaders
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        // Create an empty OpenGLES program
        mProgram = GLES20.glCreateProgram();
        // Attach vertex and fragment shader to program and create program exe's
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }


    /**
     * Encapsulates the OpenGLES instructions for a square.
     *
     * @param mvpMatrix - float[] : The Model View Project Matrix in which to draw this shape.
     */
    public void draw(float[] mvpMatrix) {

        // Add program to OpenGLES environment
        GLES20.glUseProgram(mProgram);

        // Get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // Enable handle to triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // Prepare triangle coords data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        // Get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // Set color for triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // Get handle to square's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGLError("glGetUniformLocation");
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGLError("glGetUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }


    /**
     * Sets this square's coordinates.
     *
     */
    private void setCoords() {
        float hs = size * 0.5f;
        squareCoords = new float[]{
                posX - hs, posY + hs, MyGLRenderer.getDepth(),  // Top Left
                posX - hs, posY - hs, MyGLRenderer.getDepth(),  // Bottom Left
                posX + hs, posY - hs, MyGLRenderer.getDepth(),  // Bottom Right
                posX + hs, posY + hs, MyGLRenderer.getDepth()}; // Top Right}
    }


    /**
     * Sets the square's position.
     * Clears the buffers.
     * Calls setCoords().
     *
     * @param x - float : X Axis value
     * @param y - float : Y Axis value
     */
    public void setPosition(float x, float y) {
        posX = x;
        posY = y;
        //vertexBuffer.clear();
        //drawListBuffer.clear();
        init();
    }


    /**
     * Get the position in form of a Vector3f
     * @return - Vector3f : The position(x,y,z)
     */
    public Vector3f getPosition() {
        return new Vector3f(this.posX, this.posY, MyGLRenderer.getDepth());
    }

    /**
     * Get the X Axis position coordinate.
     *
     * @return - float : X Axis coordinate
     */
    public float getPosX() {
        return posX;
    }


    /**
     * Get the Y Axis position coordinate.
     *
     * @return - float : Y Axis coordinate
     */
    public float getPosY() {
        return posY;
    }


    /**
     * Set the X Axis coordinate.
     * Call setCoords().
     *
     * @param x - float : New X Axis coordinate
     */
    public void setPosX(float x) {
        posX = x;
        init();
    }


    /**
     * Set the Y Axis coordinate.
     * Call setCoords()
     *
     * @param y - float : New Y Axis coordinate
     */
    public void setPosY(float y) {
        posY = y;
        init();
    }



    /**
     * Get the square's size.
     *
     * @return - float : The size
     */
    public float getSize() {
        return size;
    }


    /**
     * Set the square's size.
     * Call setCoords().
     *
     * @param s - float : New size
     */
    public void setSize(float s) {
        size = s;
        init();
    }

}
