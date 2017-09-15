package com.lukederrynz.application_Hub.OpenGL;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Luke Derry 12/09/2017
 *
 * A Line primitive consisting of two vertices (surprising huh)
 */
public class OpenGL_Line {

    // Number of coords per vertex
    static final int COORDS_PER_VERTEX = 3;
    private final int vertexCount = lineCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 Bytes per vertex
    private FloatBuffer vertexBuffer;
    private int mProgram;
    private int mPositionHandle;
    private int mMVPMatrixHandle;
    private int mColorHandle;

    public float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    // Coords in counter-clockwise order
    static float lineCoords[] = { 0.0f, 1.0f, MyGLRenderer.getDepth(),  // Start
                                  0.0f, 1.0f, MyGLRenderer.getDepth()}; // End

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position
                    // NOTE: The uMVPMatrix must be first.
                    "   gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "   gl_FragColor = vColor;" +
                    "}";

    public OpenGL_Line() {
        // Prepare shaders
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        // Create empty OpenGLES program
        mProgram = GLES20.glCreateProgram();
        // Attach vertex shader
        GLES20.glAttachShader(mProgram, vertexShader);
        // Attach fragment shader
        GLES20.glAttachShader(mProgram, fragmentShader);
        // Create OpenGLES program exe
        GLES20.glLinkProgram(mProgram);

        // Init vertex byte buffer for shape coords ( 4bytes per tri cord )
        ByteBuffer bb = ByteBuffer.allocateDirect(lineCoords.length * 4);
        // Use native byte order
        bb.order(ByteOrder.nativeOrder());
        // Create a float buffer from bb
        vertexBuffer = bb.asFloatBuffer();
        // Add coords to float buffer
        vertexBuffer.put(lineCoords);
        // Set buffer to read first coord
        vertexBuffer.position(0);
    }


    /**
     * Draw functionality for our Line.
     *
     * @param mvpMatrix - float[] Our Model View Projection Matrix
     */
    public void draw(float[] mvpMatrix) {

        // Add program to OpenGLES
        GLES20.glUseProgram(mProgram);

        // Get a handle to the shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        // Get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // Enable a handle to the triangle's vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // Get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Prepare the triangle's coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        // Set color for drawing triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        // Pass projection and view transformation to shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw triangle
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexCount);
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    //region PUBLIC ACCESSORS //////////////////////////////////////////////////////////////////

    /**
     * Set the Start Vertex values.
     *
     * TODO: Overload with Vector3f param?
     *
     * @param x - float : X axis value
     * @param y - float : Y axis value
     */
    public void setStartVerts(float x, float y) {
        lineCoords[0] = x;
        lineCoords[1] = y;
        lineCoords[2] = MyGLRenderer.getDepth();
    }


    /**
     * Set the End Vertex values.
     *
     * TODO: Overload with vector3f param?
     *
     * @param x - float : X axis Value
     * @param y - float : Y axis Value
     */
    public void setEndVerts(float x, float y) {
        lineCoords[3] = x;
        lineCoords[4] = y;
        lineCoords[5] = MyGLRenderer.getDepth();
    }


    /**
     * Gets all 3 axis values for the Start Vertex.
     *
     * @return - float[3] : Array of values [x, y, z]
     */
    public Vector3f getStartVerts() {
        return new Vector3f(lineCoords[0], lineCoords[1], lineCoords[2]);
    }


    /**
     * Gets all 3 axis values for the End Vertex.
     *
     * @return - float[3] : Array of values [x, y, z]
     */
    public Vector3f getEndVerts() {
        return new Vector3f(lineCoords[3], lineCoords[4], lineCoords[5]);
    }


    /**
     * Sets the Color Values for the Line.
     *
     * @param r - float : Red component
     * @param g - float : Green component
     * @param b - float : Blue component
     * @param a - float : Alpha component
     */
    public void setColor(float r, float g, float b, float a) {
        // Bail if any value is outside of range 0.0 -> 1.0
        if (r<0.0f || r>1.0f || g<0.0f || g>1.0f || b<0.0f || b>1.0f || a<0.0f || a>1.0f) {
            return;
        }
        color[0] = r;
        color[1] = g;
        color[2] = b;
        color[3] = a;
    }


    /**
     * Get the length of the line
     * @return - float : Length
     */
    public float getLength() {
        Vector3f sv = getStartVerts();
        Vector3f ev = getEndVerts();
        Vector3f v = new Vector3f(sv.x-ev.x, sv.y-ev.y, sv.z-ev.z);
        float len = (float)Math.sqrt(v.x*v.x + v.y*v.y + v.z*v.z);
        return len;
    }


    /**
     * Check is input vector is inside line Axis-Aligned Bounding Box.
     *
     * @param v - Vector3f : Input Vector
     * @return - boolean : Return true if point is within AABB
     */
    public boolean insideAABB(Vector3f v) {

        float minX = Math.min(getStartVerts().x, getEndVerts().x);
        float maxX = Math.max(getStartVerts().x, getEndVerts().x);
        float minY = Math.min(getStartVerts().y, getEndVerts().y);
        float maxY = Math.max(getStartVerts().y, getEndVerts().y);

        return !(v.x < minX || v.x > maxX || v.y < minY || v.y > maxY);
    }

    //endregion


}
